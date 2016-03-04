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

package fr.immotronic.commons.http.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.http.HttpClientService;
import fr.immotronic.commons.http.HttpCredential;
import fr.immotronic.commons.http.HttpResponse;
import fr.immotronic.commons.http.HttpResponseHandler;



/**
 * A class to generate HTTP requests to a web server. An instance of this class specified a method,
 * an URL template (a URL with placeholders for arguments), and values for Accept and Content-Type
 * headers.
 * 
 * Then, an HTTP request is sent by calling the sendRequest() method with arguments expected in the
 * URL template.
 * 
 * @author Lionel Balme <lbalme@immotronic.fr>
 *
 */
public final class HttpRequester
{
	public static enum Method
	{
		GET,
		POST,
		DELETE;
	};

	public static enum Charset
	{
		UTF_8("UTF-8");

		private String name;






		private Charset(String charsetName)
		{
			this.name = charsetName;
		}






		public String toString()
		{
			return name;
		}
	}

	private final Method method;
	private final String urlTemplate;
	private final String accept;
	private final String contentType;
	private final String charsetParameter;
	private final Charset charset;

	final Logger logger = LoggerFactory.getLogger(HttpRequester.class);






	/**
	 * Create an instance of a HttpRequester class.
	 * 
	 * @param method
	 *            the HTTP method to use for the request. One of GET, POST or DELETE.
	 * @param urlTemplate
	 *            the URL schema of the resource. Arguments placeholders could be specified. ex:
	 *            http://myhost.com/path/to/resource/$1/foo/$2/bar?a=$3
	 * 
	 *            $1, $2 and $3 will be replace by actual value when calling the sendRequest()
	 *            method.
	 * 
	 * @param accept
	 *            A comma-separated list of acceptable media-type of the response.
	 * @param contentType
	 *            In case of a POST request, the media-type of the content.
	 * @param charset
	 *            A charset used to send content and expected in the response. By default, charset
	 *            is set to UTF-8.
	 */
	public HttpRequester(	Method method,
							String urlTemplate,
							String accept,
							String contentType,
							Charset charset)
	{
		logger.trace("Entering constructor.");

		if (method == null)
		{
			throw new IllegalArgumentException("'method' argument cannot be null");
		}

		if (urlTemplate == null)
		{
			throw new IllegalArgumentException("'urlTemplate' argument cannot be null");
		}

		this.method = method;
		this.urlTemplate = urlTemplate;
		this.accept = accept;

		if (charset == null)
		{
			charset = Charset.UTF_8;
		}

		this.charset = charset;
		this.charsetParameter = "charset=\""
			+ charset.toString() + "\"";

		if (contentType != null)
		{
			this.contentType = contentType
				+ ";" + this.charsetParameter;
		}
		else
		{
			this.contentType = null;
		}
	}






	public HttpRequester(Method method, String urlTemplate, String accept, String contentType)
	{
		this(method, urlTemplate, accept, contentType, null);
	}






	public HttpRequester(Method method, String urlTemplate, String accept)
	{
		this(method, urlTemplate, accept, null, null);
	}






	public HttpRequester(Method method, String urlTemplate)
	{
		this(method, urlTemplate, null, null, null);
	}






	public HttpResponse
		sendRequest(HttpClientService httpClientService, String... arguments) throws MalformedURLException
	{
		return sendRequest(httpClientService, null, null, null, arguments);
	}






	public HttpResponse sendRequest(HttpClientService httpClientService,
									HttpCredential credential,
									String... arguments) throws MalformedURLException
	{
		return sendRequest(httpClientService, credential, null, null, arguments);
	}






	public HttpResponse sendRequest(HttpClientService httpClientService,
									Map<String, String> headers,
									String... arguments) throws MalformedURLException
	{
		return sendRequest(httpClientService, null, headers, null, arguments);
	}






	public HttpResponse sendRequest(HttpClientService httpClientService,
									HttpCredential credential,
									byte[] content,
									String... arguments) throws MalformedURLException
	{
		return sendRequest(httpClientService, credential, null, content, arguments);
	}






	public HttpResponse sendRequest(HttpClientService httpClientService,
									Map<String, String> headers,
									byte[] content,
									String... arguments) throws MalformedURLException
	{
		return sendRequest(httpClientService, null, headers, content, arguments);
	}






	public HttpResponse sendRequest(HttpClientService httpClientService,
									HttpCredential credential,
									Map<String, String> headers,
									byte[] content,
									String... arguments) throws MalformedURLException
	{
		logger.trace("Entering sendRequest() method.");

		if (httpClientService == null)
		{
			throw new IllegalArgumentException("'httpClientService' argument cannot be null");
		}

		URL url = transformURL(arguments);

		HttpResponse res = null;
		switch (method)
		{
			case GET:
				logger.debug("sendRequest() Performing a GET");
				res = httpClientService.get(url, credential, this.accept, headers);
				break;
			case POST:
				logger.debug("sendRequest() Performing a POST");
				res = httpClientService.post(
					url,
					credential,
					contentType,
					content,
					this.accept,
					headers);
				break;
			case DELETE:
				logger.debug("sendRequest() Performing a DELETE");
				res = httpClientService.delete(url, credential, headers);
				break;
		}

		logger.trace("Exiting sendRequest() method.");

		return res;
	}






	public void sendAsyncRequest(	HttpClientService httpClientService,
									HttpResponseHandler responseHandler,
									String... arguments) throws MalformedURLException
	{
		sendAsyncRequest(httpClientService, null, null, null, responseHandler, arguments);
	}






	public void sendAsyncRequest(	HttpClientService httpClientService,
									HttpCredential credential,
									HttpResponseHandler responseHandler,
									String... arguments) throws MalformedURLException
	{
		sendAsyncRequest(httpClientService, credential, null, null, responseHandler, arguments);
	}






	public void sendAsyncRequest(	HttpClientService httpClientService,
									Map<String, String> headers,
									HttpResponseHandler responseHandler,
									String... arguments) throws MalformedURLException
	{
		sendAsyncRequest(httpClientService, null, headers, null, responseHandler, arguments);
	}






	public void sendAsyncRequest(	HttpClientService httpClientService,
									HttpCredential credential,
									byte[] content,
									HttpResponseHandler responseHandler,
									String... arguments) throws MalformedURLException
	{
		sendAsyncRequest(httpClientService, credential, null, content, responseHandler, arguments);
	}






	public void sendAsyncRequest(	HttpClientService httpClientService,
									Map<String, String> headers,
									byte[] content,
									HttpResponseHandler responseHandler,
									String... arguments) throws MalformedURLException
	{
		sendAsyncRequest(httpClientService, null, headers, content, responseHandler, arguments);
	}






	public void sendAsyncRequest(	HttpClientService httpClientService,
									HttpCredential credential,
									Map<String, String> headers,
									byte[] content,
									HttpResponseHandler responseHandler,
									String... arguments) throws MalformedURLException
	{
		if (httpClientService == null)
		{
			throw new IllegalArgumentException("'httpClientService' argument cannot be null");
		}

		if (responseHandler == null)
		{
			throw new IllegalArgumentException("'responseHandler' argument cannot be null");
		}

		URL url = transformURL(arguments);

		switch (method)
		{
			case GET:
				httpClientService.get(url, credential, this.accept, headers, responseHandler);
				break;
			case POST:
				httpClientService.post(
					url,
					credential,
					contentType,
					content,
					this.accept,
					headers,
					responseHandler);
				break;
			case DELETE:
				httpClientService.delete(url, credential, headers, responseHandler);
				break;
		}
	}






	public Charset getCharset()
	{
		return charset;
	}






	private URL transformURL(String... arguments) throws MalformedURLException
	{
		String urlText = urlTemplate;
		int i = 1;
		for (String argument : arguments)
		{
			urlText = urlText.replace("$"
				+ i, argument);

			i++;
		}

		logger.debug("Requesting {}", urlText);
		return new URL(urlText);
	}
}
