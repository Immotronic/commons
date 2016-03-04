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

public enum HttpStatus
{
	OK(200, "Ok"),
	BAD_REQUEST(400, "Bad Request"),
	UNAUTHORIZED(401, "Unauthorized"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Not found"),
	METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
	NOT_ACCEPTABLE(406, "Not Acceptable"),
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	SERVER_TIMEOUT(1000, "Server Is Not Reachable"),
	WRONG_LENGTH(2000, "Content Has Unexpected Length"),
	INVALID_JSON(2001, "Content Is Expected To Be JSON Formatted, But Is Not"),
	UNSUPPORTED_CHARSET(2002, "Content Charset Is Not Supported In JVM");
	
	public static HttpStatus valueof(int statusCode)
	{
		switch(statusCode)
		{
			case 200:
				return OK;
			
			case 400:
				return BAD_REQUEST;
				
			case 401:
				return UNAUTHORIZED;
				
			case 403:
				return FORBIDDEN;
				
			case 404:
				return NOT_FOUND;
				
			case 405:
				return METHOD_NOT_ALLOWED;
				
			case 406:
				return NOT_ACCEPTABLE;
				
			case 415:
				return UNSUPPORTED_MEDIA_TYPE;
				
			case 500:
				return INTERNAL_SERVER_ERROR;
				
			default:
				return null;
		}
	}
	
	
	private int code;
	private String message;






	private HttpStatus(int code, String message)
	{
		this.code = code;
		this.message = message;
	}






	public String getMessage()
	{
		return message;
	}






	public int getCode()
	{
		return code;
	}
}
