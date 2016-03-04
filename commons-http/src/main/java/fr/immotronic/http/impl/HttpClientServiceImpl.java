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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.Strings;
import fr.immotronic.commons.http.HttpClientService;
import fr.immotronic.commons.http.HttpCredential;
import fr.immotronic.commons.http.HttpResponse;
import fr.immotronic.commons.http.HttpResponseHandler;



@Component
@Instantiate
@Provides(specifications = { HttpClientService.class })
final class HttpClientServiceImpl implements HttpClientService
{
	private final static int connectionTimeout = 30000;
	private final ExecutorService executor;

	final Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);






	public HttpClientServiceImpl(BundleContext bundleContext)
	{
		executor = Executors.newCachedThreadPool();
	}






	@Invalidate
	public synchronized void invalidate()
	{
		logger.info("HttpClientService has stopped.");
	}






	@Validate
	public synchronized void validate()
	{
		logger.info("HttpClientService is running.");
		logger.debug("HttpClientService DEBUG mode is activated.");
	}






	@Override
	public HttpCredential createCredential(String username, String password)
	{
		return new HttpCredentialImpl(username, password);
	}






	@Override
	public HttpResponse get(URL url, Map<String, String> headers)
	{
		return get(url, null, (String) null, headers);
	}






	@Override
	public void get(URL url, Map<String, String> headers, HttpResponseHandler responseHandler)
	{
		get(url, null, (String) null, headers, responseHandler);
	}






	@Override
	public HttpResponse get(URL url, String accept, Map<String, String> otherHeaders)
	{
		return get(url, null, accept, otherHeaders);
	}






	@Override
	public void get(URL url,
					String accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler)
	{
		get(url, null, accept, otherHeaders, responseHandler);
	}






	@Override
	public HttpResponse get(URL url, String[] accept, Map<String, String> otherHeaders)
	{
		return get(url, null, accept, otherHeaders);
	}






	@Override
	public void get(URL url,
					String[] accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler)
	{
		get(url, null, accept, otherHeaders, responseHandler);
	}






	@Override
	public HttpResponse get(URL url, HttpCredential credential, Map<String, String> otherHeaders)
	{
		return get(url, credential, (String) null, otherHeaders);
	}






	@Override
	public void get(URL url,
					HttpCredential credential,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler)
	{
		get(url, credential, (String) null, otherHeaders, responseHandler);
	}






	@Override
	public HttpResponse get(URL url,
							HttpCredential credential,
							String accept,
							Map<String, String> otherHeaders)
	{
		return get(url, credential, explodeAccept(accept), otherHeaders);
	}






	@Override
	public void get(URL url,
					HttpCredential credential,
					String accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler)
	{
		get(url, credential, explodeAccept(accept), otherHeaders, responseHandler);
	}






	@Override
	public HttpResponse get(URL url,
							HttpCredential credential,
							String[] accept,
							Map<String, String> otherHeaders)
	{
		return sendRequest(HttpMethod.GET, url, credential, null, null, accept, otherHeaders);
	}






	@Override
	public void get(URL url,
					HttpCredential credential,
					String[] accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.GET,
			url,
			credential,
			null,
			null,
			accept,
			otherHeaders,
			responseHandler);
	}






	@Override
	public HttpResponse post(	URL url,
								String contentType,
								byte[] content,
								Map<String, String> otherHeaders)
	{
		return sendRequest(HttpMethod.POST, url, null, contentType, content, null, otherHeaders);
	}






	@Override
	public void post(	URL url,
						String contentType,
						byte[] content,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.POST,
			url,
			null,
			contentType,
			content,
			null,
			otherHeaders,
			responseHandler);
	}






	@Override
	public HttpResponse post(	URL url,
								String contentType,
								byte[] content,
								String accept,
								Map<String, String> otherHeaders)
	{
		return sendRequest(
			HttpMethod.POST,
			url,
			null,
			contentType,
			content,
			explodeAccept(accept),
			otherHeaders);
	}






	@Override
	public void post(	URL url,
						String contentType,
						byte[] content,
						String accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.POST,
			url,
			null,
			contentType,
			content,
			explodeAccept(accept),
			otherHeaders,
			responseHandler);
	}






	@Override
	public HttpResponse post(	URL url,
								String contentType,
								byte[] content,
								String[] accept,
								Map<String, String> otherHeaders)
	{
		return sendRequest(HttpMethod.POST, url, null, contentType, content, accept, otherHeaders);
	}






	@Override
	public void post(	URL url,
						String contentType,
						byte[] content,
						String[] accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.POST,
			url,
			null,
			contentType,
			content,
			accept,
			otherHeaders,
			responseHandler);
	}






	@Override
	public HttpResponse post(	URL url,
								HttpCredential credential,
								String contentType,
								byte[] content,
								Map<String, String> otherHeaders)
	{
		return sendRequest(
			HttpMethod.POST,
			url,
			credential,
			contentType,
			content,
			null,
			otherHeaders);
	}






	@Override
	public void post(	URL url,
						HttpCredential credential,
						String contentType,
						byte[] content,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.POST,
			url,
			credential,
			contentType,
			content,
			null,
			otherHeaders,
			responseHandler);
	}






	@Override
	public HttpResponse post(	URL url,
								HttpCredential credential,
								String contentType,
								byte[] content,
								String accept,
								Map<String, String> otherHeaders)
	{
		return sendRequest(
			HttpMethod.POST,
			url,
			credential,
			contentType,
			content,
			explodeAccept(accept),
			otherHeaders);
	}






	@Override
	public void post(	URL url,
						HttpCredential credential,
						String contentType,
						byte[] content,
						String accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.POST,
			url,
			credential,
			contentType,
			content,
			explodeAccept(accept),
			otherHeaders,
			responseHandler);
	}






	@Override
	public HttpResponse post(	URL url,
								HttpCredential credential,
								String contentType,
								byte[] content,
								String[] accept,
								Map<String, String> otherHeaders)
	{
		return sendRequest(
			HttpMethod.POST,
			url,
			credential,
			contentType,
			content,
			accept,
			otherHeaders);
	}






	@Override
	public void post(	URL url,
						HttpCredential credential,
						String contentType,
						byte[] content,
						String[] accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.POST,
			url,
			credential,
			contentType,
			content,
			accept,
			otherHeaders,
			responseHandler);
	}






	@Override
	public HttpResponse delete(URL url, Map<String, String> headers)
	{
		return sendRequest(HttpMethod.DELETE, url, null, null, null, null, headers);
	}






	@Override
	public void delete(URL url, Map<String, String> headers, HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(HttpMethod.DELETE, url, null, null, null, null, headers, responseHandler);
	}






	@Override
	public HttpResponse
		delete(URL url, HttpCredential credential, Map<String, String> otherHeaders)
	{
		return sendRequest(HttpMethod.DELETE, url, credential, null, null, null, otherHeaders);
	}






	@Override
	public void delete(	URL url,
						HttpCredential credential,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler)
	{
		sendAsyncRequest(
			HttpMethod.DELETE,
			url,
			credential,
			null,
			null,
			null,
			otherHeaders,
			responseHandler);
	}






	private String[] explodeAccept(String accept)
	{
		String[] _accept = null;
		if (accept != null)
		{
			_accept = accept.split(",");
		}

		return _accept;
	}






	private HttpResponse sendRequest(	HttpMethod method,
										URL url,
										HttpCredential credential,
										String contentType,
										byte[] content,
										String[] accept,
										Map<String, String> otherHeaders)
	{
		if (method == null)
		{
			throw new IllegalArgumentException(
				"The specified HTTP method is null. 'method' argument must be valid");
		}

		if (url == null)
		{
			throw new IllegalArgumentException(
				"The specified URL is null. 'url' argument must be valid");
		}

		if (!url.getProtocol().equalsIgnoreCase("http"))
		{
			throw new IllegalArgumentException("The URL protocol ("
				+ url.getProtocol() + ") is not supported. Supported protocol is HTTP");
		}

		try
		{
			logger.info("{} {}", method, url);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(connectionTimeout);
			connection.setDoOutput(content != null);
			connection.setDoInput(true);
			connection.setRequestMethod(method.toString());

			if (otherHeaders != null)
			{
				Set<String> headerNames = otherHeaders.keySet();
				for (String headerName : headerNames)
				{
					connection.setRequestProperty(headerName, otherHeaders.get(headerName));

					if (logger.isDebugEnabled())
					{
						logger.debug("{} {}: Header '{}: {}'", method, url, headerName, connection
							.getRequestProperty(headerName));
					}
				}
			}

			if (credential != null
				&& credential instanceof HttpCredentialImpl)
			{
				HttpCredentialImpl c = (HttpCredentialImpl) credential;
				connection.setRequestProperty("Authorization", c.getAuthorizationValue());
				
				if (logger.isDebugEnabled())
				{
					logger.debug("{} {}: Header 'Authorization: {}'", method, url,
						connection.getRequestProperty("Authorization"));
				}
			}

			if (contentType != null)
			{
				connection.setRequestProperty("Content-Type", contentType);
				
				if (logger.isDebugEnabled())
				{
					logger.debug("{} {}: Header 'Content-Type: {}'", method, url,
						connection.getRequestProperty("Content-Type"));
				}
			}

			if (accept != null)
			{
				String _accept = Strings.join(accept, ", ");
				connection.setRequestProperty("Accept", _accept);
				
				if (logger.isDebugEnabled())
				{
					logger.debug("{} {}: Header 'Accept: {}'", method, url,
						connection.getRequestProperty("Accept"));
				}
			}

			if (content != null)
			{
				String contentLength = String.valueOf(content.length);
				connection.setRequestProperty("Content-Length", contentLength);

				if (logger.isDebugEnabled())
				{
					logger.debug("{} {}: Header 'Content-Length: {}'", method, url,
						connection.getRequestProperty("Content-Length"));
				}
				
				DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
				if (writer != null)
				{
					try
					{
						writer.write(content);
						writer.flush();
					}
					catch (IOException e)
					{
						logger.error("{} {}: Cannot send content.", method, url, e);
					}
				}
			}

			logger.debug("{} {}: Now building the HttpResponse object...", method, url);

			return new HttpResponseImpl(connection, accept);
		}
		catch (IOException e)
		{
			logger.error("{} {}: Cannot send request.", method, url, e);
			return new HttpResponseImpl(null, null);
		}
	}






	private void sendAsyncRequest(	HttpMethod method,
									URL url,
									HttpCredential credential,
									String contentType,
									byte[] content,
									String[] accept,
									Map<String, String> otherHeaders,
									HttpResponseHandler responseHandler)
	{
		final HttpMethod _method = method;
		final URL _url = url;
		final HttpCredential _credential = credential;
		final String _contentType = contentType;
		final byte[] _content = content;
		final String[] _accept = accept;
		final Map<String, String> _otherHeaders = otherHeaders;
		final HttpResponseHandler _responseHandler = responseHandler;

		executor.execute(new Runnable() {

			@Override
			public void run()
			{
				HttpResponse response = sendRequest(
					_method,
					_url,
					_credential,
					_contentType,
					_content,
					_accept,
					_otherHeaders);

				if (_responseHandler != null)
				{
					_responseHandler.processResponse(response);
				}
			}
		});
	}

}
