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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.http.HttpClientService;
import fr.immotronic.commons.http.HttpCredential;
import fr.immotronic.commons.http.HttpResponse;
import fr.immotronic.commons.http.HttpStatus;
import fr.immotronic.commons.http.tools.HttpRequester;




/**
 * A base class to perform operations onto a web service.
 * 
 * In this context, a web service operation is a GET, a POST or a DELETE HTTP request to access a
 * JSON resource.
 * 
 * @author Lionel Balme <lbalme@immotronic.fr>
 *
 */
public abstract class HttpServiceConnector
{
	/**
	 * Definition of errors that could occurred when requesting a distant service.
	 * 
	 * @author Lionel Balme <lbalme@immotronic.fr>
	 *
	 */
	public static enum Error
	{
		BAD_URL,
		CHARSET_NOT_SUPPORTED,
		INVALID_RESPONSE,
		NO_HTTP_CLIENT,
		RESOURCE_NOT_FOUND,
		SERVICE_INTERNAL_ERROR,
		SERVICE_REJECT_REQUEST,
		SERVICE_UNREACHABLE,
		UNAUTHORIZED,
		UNEXPECTED_RESPONSE_FORMAT
	}

	private Error error = null;
	private String errorContext = null;
	private HttpClientService httpClient = null;
	private String serviceEntryPointURL;

	final Logger logger = LoggerFactory.getLogger(HttpServiceConnector.class);




	/**
	 * Return a string that represent the version identifier of the API implemented by this
	 * connector.
	 * 
	 * @return an API version identifier, such as 'v1.0'
	 */
	public abstract String getVersionID();






	/**
	 * Set the URL of the HTTP service entry point.
	 * 
	 * Implementation SHOULD use validateServiceEntryPointURL() method to validate the correctness
	 * of the given URL and get it as a String. Then, it SHOULD instantiates adequate HttpRequester
	 * objects.
	 * 
	 * @param serviceEntryPointURL
	 *            the entry point of the service. It must refer to the HTTP protocol and be composed
	 *            of the host name or its IP address, and the path to the service entry point.
	 */
	public abstract void setServiceEntryPointURL(URL serviceEntryPointURL);






	/**
	 * Get the error returned by the last request performed, or null if no error occurred in the
	 * last request.
	 * 
	 * @return an error identifier, or null if there is no error to report.
	 */
	public Error getLastError()
	{
		return error;
	}






	/**
	 * Set the HttpClientService instance to use to perform request to the distant service.
	 * 
	 * @param httpClient
	 */
	public synchronized void setHttpClientService(HttpClientService httpClient)
	{
		this.httpClient = httpClient;
	}






	/**
	 * Validate the service entry point URL for this HTTP connector, and return it as a String. This
	 * method checks that 'serviceEntryPointURL' is not null, refers to HTTP protocol and ensures
	 * that the returned URL string ends with a trailing slash.
	 * 
	 * 
	 * @param serviceEntryPointURL
	 *            the entry point of the service. It must refer to the HTTP protocol and be composed
	 *            of the host name or its IP address, and the path to the service entry point.
	 *
	 * @return the URL as a String.
	 *
	 * @throws IllegalArgumentException
	 *             if serviceEntryPointURL argument is null or does not refer to HTTP protocol.
	 */
	protected String validateServiceEntryPointURL(URL serviceEntryPointURL)
	{
		if (serviceEntryPointURL == null)
		{
			throw new IllegalArgumentException("serviceEntryPointURL argument cannot be null.");
		}

		if (!serviceEntryPointURL.getProtocol().equalsIgnoreCase("http"))
		{
			throw new IllegalArgumentException("serviceEntryPointURL argument contains an URL that"
				+ " does not refer to the HTTP protocol");
		}

		String url = serviceEntryPointURL.toString();
		if (!url.endsWith("/"))
		{
			url = url
				+ "/";
		}

		return url;
	}






	/**
	 * Return as a string the entry point URL of the API of the distant service. This string is
	 * <b>always</b> terminated by a final '/' character.
	 * 
	 * @return
	 */
	protected String getEntryPointURL()
	{
		return serviceEntryPointURL;
	}






	/**
	 * Perform an HTTP request using the given HttpRequester object in an synchronous manner.
	 * 
	 * @param requester
	 *            the request template
	 * @param username
	 *            the username to provide if authentification is required to access the resource
	 * @param password
	 *            the password to provide if authentification is required to access the resource
	 * @param content
	 *            data to transmit in the request body
	 * @param arguments
	 *            arguments to use to parameterize the request
	 * 
	 * @return Depending the nature of received data, this method returns null, a JSONObject
	 *         instance or a JSONArray instance
	 */
	protected Object request(	HttpRequester requester,
								String username,
								String password,
								String content,
								String... arguments)
	{
		return request(requester, username, password, null, content, arguments);
	}






	/**
	 * Perform an HTTP request using the given HttpRequester object in an synchronous manner.
	 * 
	 * @param requester
	 *            the request template
	 * @param username
	 *            the username to provide if authentification is required to access the resource
	 * @param password
	 *            the password to provide if authentification is required to access the resource
	 * @param headers
	 *            a list of HTTP headers to add to the request, if any. null otherwise.
	 * @param content
	 *            data to transmit in the request body
	 * @param arguments
	 *            arguments to use to parameterize the request
	 * 
	 * @return Depending the nature of received data, this method returns null, a JSONObject
	 *         instance or a JSONArray instance
	 */
	protected Object request(	HttpRequester requester,
								String username,
								String password,
								Map<String, String> headers,
								String content,
								String... arguments)
	{
		Object res = null;
		error = null;
		HttpResponse response = null;

		if (httpClient != null)
		{
			HttpCredential credential = httpClient.createCredential(username, password);

			try
			{
				byte[] body = null;
				if(content != null)
				{
					body = content.getBytes(requester.getCharset().toString());
				}
				response = requester.sendRequest(httpClient, credential, headers, body, arguments);

				if (response.getStatus() == HttpStatus.OK)
				{
					if (response.isAcceptable())
					{
						return response.getContentAsJSON();
					}
					else
					{
						error = Error.UNEXPECTED_RESPONSE_FORMAT;
					}
				}
				else if (response.getStatus() == HttpStatus.UNAUTHORIZED)
				{
					error = Error.UNAUTHORIZED;
				}
				else if (response.getStatus() == HttpStatus.NOT_FOUND)
				{
					error = Error.RESOURCE_NOT_FOUND;
				}
				else if (response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR)
				{
					error = Error.SERVICE_INTERNAL_ERROR;
				}
				else
				{
					error = Error.SERVICE_REJECT_REQUEST;
				}
			}
			catch (MalformedURLException e)
			{
				error = Error.BAD_URL;
			}
			catch (UnsupportedEncodingException e)
			{
				error = Error.CHARSET_NOT_SUPPORTED;
				logger.error(requester.getCharset().toString()
					+ " charset is not supported", e);
			}
		}
		else
		{
			error = Error.NO_HTTP_CLIENT;
		}

		if (error != null)
		{
			if (response != null)
			{
				errorContext = response.getRequestMethod()
					+ " " + response.getRequestedURL().toString() + ": " + response.getStatus()
					+ " " + response.getContentAsString() + " " + response.getContentType();
			}
			else
			{
				errorContext = null;
			}

			reportError();
		}

		return res;
	}






	/**
	 * Log and report to client code that the received response of a request is invalid.
	 * 
	 */
	protected void reportInvalidResponseError()
	{
		error = Error.INVALID_RESPONSE;
		reportError();
	}






	/**
	 * Get the HttpClientService object use to perform HTTP request to the distant service.
	 * 
	 * @return an instance of HttpClientService, or null if none.
	 */
	protected HttpClientService getHttpClientService()
	{
		return httpClient;
	}






	private void reportError()
	{
		if (error != null)
		{
			if (errorContext != null)
			{
				logger.error("{}({})", error.toString(),errorContext);
			}
			else
			{
				logger.error(error.toString());
			}
		}
	}
}
