/*
 * Copyright (c) Immotronic, 2014
 *
 * Contributors:
 *
 *  	Lionel Balme (lbalme@immotronic.fr)
 *
 * This file is part of snp-modbus, a component of the UBIKIT project.
 *
 * This software is a computer program whose purpose is to host third-
 * parties applications that make use of sensor and actuator networks.
 *
 * This software is governed by the CeCILL-C license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-C
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * As a counterpart to the access to the source code and  rights to copy,
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 *
 * CeCILL-C licence is fully compliant with the GNU Lesser GPL v2 and v3.
 *
 */

package fr.immotronic.http.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.http.HttpResponse;
import fr.immotronic.commons.http.HttpStatus;



final class HttpResponseImpl implements HttpResponse
{
	private Pattern p = Pattern.compile("charset=([^ ]*)");
	private byte[] content = null;
	private HttpStatus status = null;
	private final String contentType;
	private final boolean isAcceptable;
	private final Charset charset;
	private final Map<String, String> headers = new HashMap<String, String>();
	private final Object contentAsJSON;
	private final URL requestedURL;
	private final String requestMethod;

	final Logger logger = LoggerFactory.getLogger(HttpResponseImpl.class);






	/**
	 * Construct a HttpResponse object.
	 * 
	 * @param connection
	 *            The opened connection, ready to receive the response. If this argument is null,
	 *            the object created will have no content and its status will be
	 *            HttpStatus.SERVER_TIMEOUT.
	 */
	HttpResponseImpl(HttpURLConnection connection, String[] accept)
	{
		if (connection == null)
		{
			requestedURL = null;
			requestMethod = null;
			content = new byte[0];
			status = HttpStatus.SERVER_TIMEOUT;
			contentType = null;
			isAcceptable = false;
			charset = null;
			contentAsJSON = null;
		}
		else
		{
			requestedURL = connection.getURL();
			requestMethod = connection.getRequestMethod();

			if (logger.isDebugEnabled())
			{
				logger.debug("{} {}: Reading receiving response...", requestMethod, requestedURL
					.toString());
			}

			try
			{
				int statusCode = connection.getResponseCode();
				status = HttpStatus.valueof(statusCode);
				if (status == null)
				{
					logger.error("{} {}: Unsupported HTTP status code: {}", connection
						.getRequestMethod(), requestedURL.toString(), statusCode);
				}
				else if (logger.isDebugEnabled())
				{
					logger
						.debug("{} {}: HTTP status code: {}, HttpStatus={}.", connection
							.getRequestMethod(), requestedURL.toString(), statusCode, status
							.toString());
				}
			}
			catch (IOException e)
			{
				logger.error(
					"{} {}: cannot get HTTP status code.",
					connection.getRequestMethod(),
					requestedURL.toString(),
					e);
			}

			contentType = connection.getContentType();
			charset = readContentCharset();
			isAcceptable = computeResponseAcceptablity(accept);

			long expectedContentLength = connection.getContentLengthLong();

			if (logger.isDebugEnabled())
			{
				logger.debug(
					"{} {}: Content-Type: {}.",
					connection.getRequestMethod(),
					requestedURL.toString(),
					contentType);

				logger.debug(
					"{} {}: Content-Length: {}.",
					connection.getRequestMethod(),
					requestedURL.toString(),
					expectedContentLength);

				logger.debug(
					"{} {}: Content-Encoding: {}.",
					connection.getRequestMethod(),
					requestedURL.toString(),
					connection.getContentEncoding());
			}

			if (expectedContentLength > Integer.MAX_VALUE)
			{
				logger.error(
					"{} {}: Content is too big: {} bytes (max supported content length is {}).",
					connection.getRequestMethod(),
					requestedURL.toString(),
					expectedContentLength,
					Integer.MAX_VALUE);

				content = new byte[0];
				status = HttpStatus.WRONG_LENGTH;
				contentAsJSON = null;
				return;
			}

			if (expectedContentLength == -1)
			{
				content = new byte[0];
			}
			else
			{
				content = new byte[(int) expectedContentLength];
			}

			Object _contentAsJSON = null;

			try
			{
				if (status == HttpStatus.OK)
				{
					long totalLength = readContent(connection.getInputStream());

					if (totalLength != expectedContentLength
						&& expectedContentLength != -1)
					{
						status = HttpStatus.WRONG_LENGTH;
					}
					else
					{
						logger.debug("{} {}: Testing for JSON content...", connection
							.getRequestMethod(), requestedURL.toString());

						if (contentType != null
							&& contentType.toLowerCase().matches(".*json.*"))
						{
							try
							{
								_contentAsJSON = parseContentAsJSON();
							}
							catch (JSONException e)
							{
								logger.error("{} {}: Content-Type announce a JSON content, but "
									+ "content cannot be parsed as JSON", connection
									.getRequestMethod(), requestedURL.toString(), e);

							}
							catch (Exception e)
							{
								logger.error("{} {}: Unexpected Exception.", connection
									.getRequestMethod(), requestedURL.toString(), e);
							}
						}

					}
				}
				else
				{
					readContent(connection.getErrorStream());
				}

				readHeaders(connection);
			}
			catch (IOException e)
			{
				content = new byte[0];
				status = HttpStatus.SERVER_TIMEOUT;

				logger.error("{} {}: Cannot get content.", connection
					.getRequestMethod(), requestedURL.toString(), e);
			}

			contentAsJSON = _contentAsJSON;
		}
	}






	@Override
	public byte[] getContent()
	{
		return content;
	}






	@Override
	public String getContentAsString()
	{
		if (charset != null)
		{
			return new String(content, charset);
		}

		return new String(content);
	}






	@Override
	public Object getContentAsJSON()
	{
		if (contentAsJSON == null)
		{
			try
			{
				parseContentAsJSON();
			}
			catch (JSONException e)
			{}
		}

		return contentAsJSON;
	}






	@Override
	public Map<String, String> getHeaders()
	{
		return Collections.unmodifiableMap(headers);
	}






	@Override
	public String getHeader(String headerName)
	{
		return headers.get(headerName);
	}






	@Override
	public boolean isAcceptable()
	{
		return isAcceptable;
	}






	@Override
	public String getContentType()
	{
		return contentType;
	}






	@Override
	public int getContentLength()
	{
		return content.length;
	}






	@Override
	public HttpStatus getStatus()
	{
		return status;
	}






	@Override
	public URL getRequestedURL()
	{
		return requestedURL;
	}






	private long readContent(InputStream stream) throws IOException
	{
		int totalLength = 0;
		if (stream != null)
		{
			int readLength = -1;
			byte[] buffer = new byte[1024];
			while ((readLength = stream.read(buffer, 0, 1024)) > 0)
			{
				int newLength = totalLength
					+ readLength;

				if (content.length < newLength)
				{
					content = Arrays.copyOf(content, newLength);
				}
				System.arraycopy(buffer, 0, content, totalLength, readLength);
				totalLength = newLength;
			}
		}

		return totalLength;
	}






	private void readHeaders(HttpURLConnection connection)
	{
		int index = 1;
		String headerName = null;
		while ((headerName = connection.getHeaderFieldKey(index)) != null)
		{
			headers.put(headerName, connection.getHeaderField(index));
			index++;
		}
	}






	private boolean computeResponseAcceptablity(String[] accept)
	{
		if (accept != null
			&& contentType != null)
		{
			String[] contentTypeComponents = contentType.split(";");
			String responseMediaType = contentTypeComponents[0].trim();

			if (logger.isDebugEnabled())
			{
				StringBuilder sb = new StringBuilder(
					"Testing for acceptability: acceptable media-types are:\n");

				for (String acceptableMediaType : accept)
				{
					sb.append("  - ").append(acceptableMediaType).append("\n");
				}

				logger.debug(sb.toString());
			}

			for (String acceptableMediaType : accept)
			{
				if (acceptableMediaType.trim().equalsIgnoreCase(responseMediaType))
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("Testing for acceptability with '{}': OK.",
							acceptableMediaType.trim());
					}

					return true;
				}
			}

			if (logger.isDebugEnabled())
			{
				logger.debug("Testing for acceptability of '{}': NOT ACCEPTABLE",
					responseMediaType);
			}
		}
		else
		{
			return true;
		}

		return false;
	}






	private Charset readContentCharset()
	{
		if (contentType != null)
		{
			Matcher matcher = p.matcher(contentType);
			if (matcher.find())
			{
				Charset charset = Charset.forName(matcher.group(1));
				if (charset == null)
				{
					logger.warn("Unsupported charset: {}.", matcher.group(1));
					status = HttpStatus.UNSUPPORTED_CHARSET;
				}

				return charset;
			}
		}

		return null;
	}






	private Object parseContentAsJSON() throws JSONException
	{
		logger.debug("Parsing JSON content...");

		String ctnt = getContentAsString().trim();
		if (!ctnt.isEmpty())
		{
			if (ctnt.charAt(0) == '{')
			{
				return new JSONObject(ctnt);
			}
			else
			{
				return new JSONArray(ctnt);
			}
		}

		return null;
	}






	@Override
	public String getRequestMethod()
	{
		return requestMethod;
	}
}
