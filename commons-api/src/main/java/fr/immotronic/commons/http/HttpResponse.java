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



public interface HttpResponse
{
	/**
	 * Get the response raw content without any interpretation of any kind.
	 * 
	 * @return The response content as an array of bytes. An empty array is returned if response has
	 *         no content.
	 */
	public byte[] getContent();






	/**
	 * Get the response content as a string encoded as specified in response headers.
	 * 
	 * @return The response content as a string. An empty string is returned if response has no
	 *         content.
	 */
	public String getContentAsString();






	/**
	 * Get the response content as either a JSONObject instance or a JSONArray instance according to
	 * data kind. If there is no content or if content is not JSON formatted data, this method
	 * return null.
	 * 
	 * @return Depending of the nature of data, a JSONObject instance, a JSONArray instance or null.
	 */
	public Object getContentAsJSON();






	/**
	 * Get all response headers as an unmodifiable Map. Map's keys are header's names. If response
	 * has no header, this method return an unmodifiable empty map.
	 * 
	 * @return a map that contains all response headers.
	 */
	public Map<String, String> getHeaders();






	/**
	 * Return the value for a given response header name.
	 * 
	 * @param headerName
	 *            The name of the header to get the value
	 * @return The header value, or null if response contains no header named <i>headerName</i>.
	 */
	public String getHeader(String headerName);






	/**
	 * Check if the response will be readable by the response handler.
	 * 
	 * @return true if 'response 'Content-Type' is compliant with the 'Accept' header provided in
	 *         the request or if no accept header has been send within the request. False otherwise.
	 */
	public boolean isAcceptable();






	/**
	 * Return the value of the 'Content-Type' header of the response, if any. Return null otherwise.
	 * 
	 * @return the response Content-Type, or null if no Content-Type is specified.
	 */
	public String getContentType();






	/**
	 * Return content size in bytes.
	 * 
	 * @return the content size in bytes.
	 */
	public int getContentLength();






	/**
	 * Return the HTTP status of the response (e.g. '200 OK', '404 Not Found', etc.).
	 * 
	 * @return The HTTP status of the response.
	 */
	public HttpStatus getStatus();






	/**
	 * Return the requested URL
	 * 
	 * @return the URL used to get this HttpResponse object
	 */
	public URL getRequestedURL();
	
	
	/**
	 * Return the request method (e.g. GET, POST, DELETE, ...)
	 *  
	 * @return a string in uppercase that represent the method for the request.
	 */
	public String getRequestMethod();
}
