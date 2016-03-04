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

package fr.immotronic.upnp.impl;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPException;
import org.osgi.service.upnp.UPnPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



final class UPnPUtils
{
	private static final String URN_DELIMITER = ":";
	private static final String EQ = "='";
	private static final String DEL = "', ";

	static final Logger logger = LoggerFactory.getLogger(UPnPUtils.class);






	static boolean checkServiceVersion(	UPnPService service,
										String[] supportedURNs,
										String[] supportedVersions)
	{
		if (service == null)
		{
			throw new IllegalArgumentException("'service' must NOT be null");
		}

		if (supportedURNs == null
			|| supportedURNs.length == 0)
		{
			throw new IllegalArgumentException("'supportedURNs' must be a non-empty array");
		}

		if (supportedVersions == null
			|| supportedVersions.length == 0)
		{
			throw new IllegalArgumentException("'supportedVersions' must be a non-empty array");
		}

		String serviceType = service.getType();

		for (String supportedURN : supportedURNs)
		{
			for (String supportedVersion : supportedVersions)
			{
				if (serviceType.equals(supportedURN
					+ URN_DELIMITER + supportedVersion))
				{
					return true;
				}
			}
		}

		return false;
	}






	static Dictionary<?, ?>
		invokeAction(UPnPService service, String actionName, Dictionary<String, Object> arguments) throws UPnPException
	{
		if (service != null)
		{
			UPnPAction action = service.getAction(actionName);
			if (action == null)
			{
				logger.warn(
					"UPnPUtils : Action '{}' seems not supported by service '{}'.",
					actionName,
					service.getId());
			}

			try
			{
				return action.invoke(arguments);
			}
			catch (UPnPException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				logger.warn(
					"UPnPUtils : the invokation of '{}' action failed on exception.",
					actionName,
					e);
			}
		}

		return new Hashtable<String, Object>();
	}






	static boolean isSupportedDevice(String urn, String[] supportedVersions, String type)
	{
		if (urn == null
			|| urn.isEmpty())
		{
			throw new IllegalArgumentException("'urn' must be an non-empty string");
		}

		if (supportedVersions == null
			|| supportedVersions.length == 0)
		{
			throw new IllegalArgumentException("'supportedVersions' must be a non-empty array");
		}

		if (type != null)
		{
			for (String version : supportedVersions)
			{
				if (type != null
					&& type.equals(urn
						+ URN_DELIMITER + version))
				{
					return true;
				}
			}

			return false;
		}

		throw new IllegalArgumentException("'type' must not be null");
	}






	static String dictionaryArgsToString(Dictionary<String, Object> arguments)
	{
		if (arguments == null)
		{
			return null;
		}

		StringBuilder sb = new StringBuilder();

		Enumeration<String> keys = arguments.keys();
		while (keys.hasMoreElements())
		{
			String key = keys.nextElement();
			sb.append(key).append(EQ).append(arguments.get(key)).append(DEL);
		}

		return sb.substring(0, sb.length()
			- (DEL.length() - 1));
	}
}
