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

import org.osgi.service.upnp.UPnPException;
import org.osgi.service.upnp.UPnPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.upnp.service.WANxConnectionService;
import fr.immotronic.commons.upnp.type.NATRSIPStatus;
import fr.immotronic.commons.upnp.type.PortMappingEntry;
import fr.immotronic.commons.upnp.type.PortMappingEntry.Protocol;
import fr.immotronic.commons.upnp.type.StatusInfo;



class WANxConnectionServiceImpl extends GenericServiceImpl implements WANxConnectionService
{
	public static enum Argument
	{
		NewConnectionStatus,
		NewEnabled,
		NewExternalIPAddress,
		NewExternalPort,
		NewInternalClient,
		NewInternalPort,
		NewLastConnectionError,
		NewLeaseDuration,
		NewNATEnabled,
		NewPortMappingDescription,
		NewPortMappingIndex,
		NewProtocol,
		NewRemoteHost,
		NewRSIPAvailable,
		NewUptime
	}

	public static enum Operation
	{
		AddPortMapping,
		DeletePortMapping,
		GetExternalIPAddress,
		GetGenericPortMappingEntry,
		GetNATRSIPStatus,
		GetStatusInfo
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Statics Initializers & Methods

	private static final String[] SUPPORTED_URNS = {
			"urn:schemas-upnp-org:service:WANPPPConnection",
			"urn:schemas-upnp-org:service:WANIPConnection" };

	private static final String[] SUPPORTED_VERSIONS = { "1", "2" };

	final Logger logger = LoggerFactory.getLogger(WANxConnectionServiceImpl.class);






	static WANxConnectionServiceImpl createInstance(UPnPService service)
	{
		try
		{
			return new WANxConnectionServiceImpl(service);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Constructor

	private WANxConnectionServiceImpl(UPnPService service)
	{
		super(service);

		if (!UPnPUtils.checkServiceVersion(service, SUPPORTED_URNS, SUPPORTED_VERSIONS))
		{
			throw new IllegalArgumentException("This service is not supported by this class.");
		}

		logger.debug(" New service instance for {} of type '{}'", service.getId(), service
			.getType());
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Public methods

	@Override
	public boolean addPortMapping(	String remoteHost,
									Integer externalPort,
									Protocol protocol,
									Integer internalPort,
									String internalClient,
									Boolean enabled,
									String description,
									Long leaseDuration)
	{
		if ("2".equals(getVersion())
			&& leaseDuration == 0)
		{
			/*
			 * Note from 'WANIPConnection:2 Service - Standardized DCP (SDCP)-September 10, 2010'
			 * 
			 * PortMappingLeaseDuration can be either a value between 1 and 604800 seconds or the
			 * zero value (for infinite lease time). Note that an infinite lease time can be only
			 * set by out-of-band mechanisms like WWW-administration, remote management or local
			 * management.
			 * 
			 * If a control point uses the value 0 to indicate an infinite lease time mapping, it is
			 * REQUIRED that gateway uses the maximum value instead (e.g. 604800 seconds),
			 */
			leaseDuration = 604800L;
		}

		if (remoteHost == null)
		{
			remoteHost = "";
		}

		Dictionary<String, Object> args = new Hashtable<String, Object>();
		args.put(Argument.NewRemoteHost.name(), remoteHost);
		args.put(Argument.NewExternalPort.name(), externalPort);
		args.put(Argument.NewProtocol.name(), protocol.name());
		args.put(Argument.NewInternalPort.name(), internalPort);
		args.put(Argument.NewInternalClient.name(), internalClient);
		args.put(Argument.NewEnabled.name(), enabled);
		args.put(Argument.NewPortMappingDescription.name(), description);
		args.put(Argument.NewLeaseDuration.name(), leaseDuration);

		if (logger.isDebugEnabled())
		{
			logger.debug("AppdPortMapping with: {}", UPnPUtils.dictionaryArgsToString(args));
		}

		try
		{
			UPnPUtils.invokeAction(getService(), Operation.AddPortMapping.name(), args);
			return true;
		}
		catch (UPnPException e)
		{
			switch (e.getUPnPError_Code())
			{
				case 725:
					logger.warn("'AddPortMapping' action: The gateway does not "
						+ "authorize non-permament lease. Then, the specified lease duration has "
						+ "been changed to 0 (aka permanent lease)");

					return addPortMapping(
						remoteHost,
						externalPort,
						protocol,
						internalPort,
						internalClient,
						enabled,
						description,
						0L);

				default:
					logger.warn("Invokation of 'AddPortMapping' action failed on UPnP error "
						+ "(code={}, {}).", e.getUPnPError_Code(), e.getMessage());

					logger.warn("Args: {}", UPnPUtils.dictionaryArgsToString(args));
					setLastUPnPErrorCode(e.getUPnPError_Code());
					break;
			}

		}

		return false;
	}






	@Override
	public boolean deletePortMapping(String remoteHost, Integer externalPort, Protocol protocol)
	{
		if (remoteHost == null)
		{
			remoteHost = "";
		}

		Dictionary<String, Object> args = new Hashtable<String, Object>();
		args.put(Argument.NewRemoteHost.name(), remoteHost);
		args.put(Argument.NewExternalPort.name(), externalPort);
		args.put(Argument.NewProtocol.name(), protocol.name());

		try
		{
			UPnPUtils.invokeAction(getService(), Operation.DeletePortMapping.name(), args);
			return true;
		}
		catch (UPnPException e)
		{
			logger.warn(
				"Invokation of 'DeletePortMapping' action failed on UPnP error (code={}).",
				e.getUPnPError_Code(),
				e);
			setLastUPnPErrorCode(e.getUPnPError_Code());
		}

		return false;
	}






	@Override
	public String getExternalIPAddress()
	{
		try
		{
			Dictionary<?, ?> res = UPnPUtils.invokeAction(
				getService(),
				Operation.GetExternalIPAddress.name(),
				null);

			if (res != null)
			{
				return (String) res.get(Argument.NewExternalIPAddress.name());
			}
		}
		catch (UPnPException e)
		{
			logger.warn(
				"Invokation of 'GetExternalIPAddress' action failed on UPnP error (code={}).",
				e.getUPnPError_Code(),
				e);
			setLastUPnPErrorCode(e.getUPnPError_Code());
		}
		return null;
	}






	@Override
	public NATRSIPStatus getNATRSIPStatus()
	{
		try
		{
			Dictionary<?, ?> res = UPnPUtils.invokeAction(getService(), Operation.GetNATRSIPStatus
				.name(), null);

			if (res != null)
			{
				return new NATRSIPStatusImpl(
					(Boolean) res.get(Argument.NewNATEnabled.name()),
					(Boolean) res.get(Argument.NewRSIPAvailable.name()));
			}
		}
		catch (UPnPException e)
		{
			logger.warn(
				"Invokation of 'GetNATRSIPStatus' action failed on UPnP error (code={}).",
				e.getUPnPError_Code(),
				e);
			setLastUPnPErrorCode(e.getUPnPError_Code());
		}

		return null;
	}






	@Override
	public PortMappingEntry getPortMappingEntryByIndex(int index)
	{
		Dictionary<String, Object> args = new Hashtable<String, Object>();
		args.put(Argument.NewPortMappingIndex.name(), Integer.valueOf(index));

		try
		{
			Dictionary<?, ?> res = UPnPUtils.invokeAction(
				getService(),
				Operation.GetGenericPortMappingEntry.name(),
				args);

			if (res != null)
			{
				return new PortMappingEntryImpl(
					(String) res.get(Argument.NewRemoteHost.name()),
					(Integer) res.get(Argument.NewExternalPort.name()),
					(String) res.get(Argument.NewProtocol.name()),
					(Integer) res.get(Argument.NewInternalPort.name()),
					(String) res.get(Argument.NewInternalClient.name()),
					(Boolean) res.get(Argument.NewEnabled.name()),
					(String) res.get(Argument.NewPortMappingDescription.name()),
					(Long) res.get(Argument.NewLeaseDuration.name()));
			}
		}
		catch (UPnPException e)
		{
			if (e.getUPnPError_Code() != 713
				&& e.getUPnPError_Code() != 714)
			{
				logger.warn(
					"Invokation of 'GetGenericPortMappingEntry' action failed on UPnP error"
						+ " (code={}).",
					e.getUPnPError_Code(),
					e);
				setLastUPnPErrorCode(e.getUPnPError_Code());
			}
		}

		return null;
	}






	@Override
	public StatusInfo getStatusInfo()
	{
		try
		{
			Dictionary<?, ?> res = UPnPUtils.invokeAction(getService(), Operation.GetStatusInfo
				.name(), null);

			if (res != null)
			{
				return new StatusInfoImpl(
					(String) res.get(Argument.NewConnectionStatus.name()),
					(String) res.get(Argument.NewLastConnectionError.name()),
					(Long) res.get(Argument.NewUptime.name()));
			}
		}
		catch (UPnPException e)
		{
			logger.warn("Invokation of 'GetStatusInfo' action failed on UPnP error (code={}).", e
				.getUPnPError_Code(), e);
			
			setLastUPnPErrorCode(e.getUPnPError_Code());
		}

		return null;
	}






	@Override
	void setStateVariables(Dictionary<?, ?> events)
	{
		logger.info(" # EVENT from {}", getId());
		Enumeration<?> keys = events.keys();
		while (keys.hasMoreElements())
		{
			String key = (String) keys.nextElement();
			Object value = events.get(key);
			if (value instanceof String)
			{
				logger.info("  % {}: {}", key, value);
			}
			else if (value instanceof String[])
			{
				String[] values = (String[]) value;
				logger.info("  % {}: ", key);
				for (String item : values)
				{
					logger.info("     ' {}", item);
				}
			}
			else if (value instanceof Boolean)
			{
				logger.info("  % {}: {} (boolean)", key, value);
			}
			else if (value instanceof Number)
			{
				logger.info("  % {}: {} ({})", key, value, value.getClass().getSimpleName());
			}
			else
			{
				logger.info("  # {}: UNSUPPORTED INFORMATION TYPE", key);
			}
		}
	}
}
