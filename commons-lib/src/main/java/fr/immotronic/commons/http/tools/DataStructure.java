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

/**
 * A base class for implementation of data structure defined in HTTP API specifications.
 * 
 * In HTTP APIs, peers may exchange structured information in the body of Http request and/or
 * response. Those structured information conforms to a particular formalism, such as JSON, a
 * specific XML language, or whatever. A sub-class of DataStructure should offer a classic Java API
 * to ease usage of those structured information in Java programs.
 * 
 * A sub-class of DataStructure MUST offer at least 2 constructors: one that accept Java raw types
 * arguments and/or specific Java classes, and another one that accept a JSONObject instance or a
 * JSONArray instance.
 * 
 * The toString() methods MUST be overloaded to produce the textual representation of the object in
 * the expected formalism.
 * 
 * @author Lionel Balme <lbalme@immotronic.fr>
 *
 */
public interface DataStructure
{
	/**
	 * Return the data structure media type. Generally, media types are unique strings build using
	 * the following schema:
	 * 
	 * application/vnd.QUALIFIED_TYPE_NAME-VERSION_ID+EXPECTED_FORMALISM
	 * 
	 * where:
	 * 
	 * - QUALIFIED_TYPE_NAME: a qualified type name, such as 'immotronic.backoffice.gateway-id',
	 * 
	 * - VERSION_ID: a version ID of the data structure, such as 'v1',
	 * 
	 * - EXPECTED_FORMALISM: the expected formalism to represent the structured information, such as
	 * 'json' or 'xml'.
	 * 
	 * ex: 'application/vnd.immotronic.backoffice.gateway-id-v1+json'
	 * 
	 * 
	 * @return
	 */
	public String getMediaType();
}
