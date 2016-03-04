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

package fr.immotronic.commons.http;

import java.net.URL;
import java.util.Map;



public interface HttpClientService
{
	/**
	 * Create an HttpCredential object from login and password strings. HttpCredential object are
	 * mandatory to access resources protected by HTTP access authentication.
	 * 
	 * Only Basic HTTP Authentication methods is supported.
	 * 
	 * @param username
	 *            The user name
	 * @param password
	 *            The password in plain text
	 * @return An instance of HttpCredential object or null if, at least, one of login or password
	 *         is null.
	 */
	public HttpCredential createCredential(String username, String password);






	/**
	 * Get the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param headers
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse get(URL url, Map<String, String> headers);






	/**
	 * Asynchronously get the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param headers
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void get(URL url, Map<String, String> headers, HttpResponseHandler responseHandler);






	/**
	 * Get the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse get(URL url, String accept, Map<String, String> otherHeaders);






	/**
	 * Asynchronously get the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void get(URL url,
					String accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler);






	/**
	 * Get the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse get(URL url, String[] accept, Map<String, String> otherHeaders);






	/**
	 * Asynchronously get the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void get(URL url,
					String[] accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler);






	/**
	 * Get the resource available at the given URL and protected by HTTP access authentication.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>headers</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse get(URL url, HttpCredential credential, Map<String, String> otherHeaders);






	/**
	 * Asynchronously get the resource available at the given URL and protected by HTTP access
	 * authentication.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>headers</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void get(URL url,
					HttpCredential credential,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler);






	/**
	 * Get the resource available at the given URL and protected by HTTP access authentication.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse get(URL url,
							HttpCredential credential,
							String accept,
							Map<String, String> otherHeaders);






	/**
	 * Asynchronously get the resource available at the given URL and protected by HTTP access
	 * authentication.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void get(URL url,
					HttpCredential credential,
					String accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler);






	/**
	 * Get the resource available at the given URL. This method facilitate access to resources
	 * protected by HTTP access authentication.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. . This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse get(URL url,
							HttpCredential credential,
							String[] accept,
							Map<String, String> otherHeaders);






	/**
	 * Asynchronously get the resource available at the given URL. This method facilitate access to
	 * resources protected by HTTP access authentication.
	 * 
	 * @param url
	 *            The URL of the resource to get. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. . This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void get(URL url,
					HttpCredential credential,
					String[] accept,
					Map<String, String> otherHeaders,
					HttpResponseHandler responseHandler);






	/**
	 * Post content to the resource available at the given URL.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse post(	URL url,
								String contentType,
								byte[] content,
								Map<String, String> otherHeaders);






	/**
	 * Asynchronously post content to the resource available at the given URL.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void post(	URL url,
						String contentType,
						byte[] content,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler);






	/**
	 * Post content to the resource available at the given URL.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse post(	URL url,
								String contentType,
								byte[] content,
								String accept,
								Map<String, String> otherHeaders);






	/**
	 * Asynchronously post content to the resource available at the given URL.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void post(	URL url,
						String contentType,
						byte[] content,
						String accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler);






	/**
	 * Post content to the resource available at the given URL.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. . This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse post(	URL url,
								String contentType,
								byte[] content,
								String[] accept,
								Map<String, String> otherHeaders);






	/**
	 * Asynchronously post content to the resource available at the given URL.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. . This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void post(	URL url,
						String contentType,
						byte[] content,
						String[] accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler);






	/**
	 * Post content to the resource available at the given URL. This method facilitate access to
	 * resources protected by HTTP access authentication.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse post(	URL url,
								HttpCredential credential,
								String contentType,
								byte[] content,
								Map<String, String> otherHeaders);






	/**
	 * Asynchronously post content to the resource available at the given URL. This method
	 * facilitate access to resources protected by HTTP access authentication.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void post(	URL url,
						HttpCredential credential,
						String contentType,
						byte[] content,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler);






	/**
	 * Post content to the resource available at the given URL. This method facilitate access to
	 * resources protected by HTTP access authentication.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse post(	URL url,
								HttpCredential credential,
								String contentType,
								byte[] content,
								String accept,
								Map<String, String> otherHeaders);






	/**
	 * Asynchronously post content to the resource available at the given URL. This method
	 * facilitate access to resources protected by HTTP access authentication.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A comma separated list of acceptable media-type in response. This value will
	 *            override 'Accept' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void post(	URL url,
						HttpCredential credential,
						String contentType,
						byte[] content,
						String accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler);






	/**
	 * Post content to the resource available at the given URL. This method facilitate access to
	 * resources protected by HTTP access authentication.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. . This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse post(	URL url,
								HttpCredential credential,
								String contentType,
								byte[] content,
								String[] accept,
								Map<String, String> otherHeaders);






	/**
	 * Asynchronously post content to the resource available at the given URL. This method
	 * facilitate access to resources protected by HTTP access authentication.
	 * 
	 * The 'Content-Length' header will be automatically added. If <i>headers</i> argument provide a
	 * 'Content-Length' header, it will be overridden.
	 * 
	 * @param url
	 *            The URL of the resource to post to. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param contentType
	 *            The content-type that describe the request content. . This value will override
	 *            'Content-Type' header provided in <i>headers</i> argument, if any.
	 * @param content
	 *            the request content.
	 * @param accept
	 *            A list of acceptable media-type in response. One media-type by array item. . This
	 *            value will override 'Accept' header provided in <i>otherHeaders</i> argument, if
	 *            any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No other headers than
	 *            'Content-Length' and 'Content-Type' will be sent if this argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void post(	URL url,
						HttpCredential credential,
						String contentType,
						byte[] content,
						String[] accept,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler);






	/**
	 * Delete the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to delete. The URL protocol MUST be HTTP.
	 * @param headers
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse delete(URL url, Map<String, String> headers);






	/**
	 * Asynchronously delete the resource available at the given URL.
	 * 
	 * @param url
	 *            The URL of the resource to delete. The URL protocol MUST be HTTP.
	 * @param headers
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void delete(URL url, Map<String, String> headers, HttpResponseHandler responseHandler);






	/**
	 * Delete the resource available at the given URL. This method facilitate access to resources
	 * protected by HTTP access authentication.
	 * 
	 * @param url
	 *            The URL of the resource to delete. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @return An instance of HttpResponse object that contains the server response, or null if at
	 *         least one of given arguments is invalid.
	 */
	public HttpResponse
		delete(URL url, HttpCredential credential, Map<String, String> otherHeaders);






	/**
	 * Asynchronously delete the resource available at the given URL. This method facilitate access
	 * to resources protected by HTTP access authentication.
	 * 
	 * @param url
	 *            The URL of the resource to delete. The URL protocol MUST be HTTP.
	 * @param credential
	 *            the login and password required by HTTP access authentication. This value will
	 *            override 'Authorization' header provided in <i>otherHeaders</i> argument, if any.
	 * @param otherHeaders
	 *            A set of HTTP headers to send within the request. No headers will be sent if this
	 *            argument is null.
	 * @param responseHandler
	 *            a response handler that will be notified when the response will be available.
	 */
	public void delete(	URL url,
						HttpCredential credential,
						Map<String, String> otherHeaders,
						HttpResponseHandler responseHandler);
}
