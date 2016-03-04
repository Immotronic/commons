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

import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.upnp.device.WANConnectionDevice;
import fr.immotronic.commons.upnp.service.WANxConnectionService;
import fr.immotronic.commons.upnp.type.StatusInfo;



/**
 * Implementation of a UPnP WANConnectionDevice, that conforms to the WANConnectionDevice interface.
 * 
 * This class aimed to be instantiated by the InternetGatewayDeviceImpl class.
 * 
 * @author Lionel BALME
 */
final class WANConnectionDeviceImpl extends GenericDeviceImpl implements WANConnectionDevice
{
	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Statics Initializers & Methods

	private static final String BASE_URN = "urn:schemas-upnp-org:device:WANConnectionDevice";
	private static final String[] SUPPORTED_VERSIONS = { "1", "2" };

	final Logger logger = LoggerFactory.getLogger(WANConnectionDeviceImpl.class);






	static WANConnectionDeviceImpl createInstance(UPnPDevice device)
	{
		try
		{
			return new WANConnectionDeviceImpl(device);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Fields

	/*
	 * A WANConnectionDevice could expose several WAN*Connection services. This class automatically
	 * selects a default service to deals with WANConnection. The selection process picks the first
	 * WAN*Connection service encountered that is marked as connected to the WAN. If no service is
	 * marked as connected, the selection process picks the first service. If no WAN*Connection
	 * service is exposed by the WANConnectionDevice (buggy device case), the constructor throw a
	 * Runtime exception.
	 * 
	 * Then, 'defaultService' member is never null.
	 */
	private WANxConnectionServiceImpl defaultService;






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Constructor

	private WANConnectionDeviceImpl(UPnPDevice device)
	{
		super(device);

		if (!UPnPUtils.isSupportedDevice(BASE_URN, SUPPORTED_VERSIONS, getType()))
		{
			throw new IllegalArgumentException();
		}

		UPnPService[] services = device.getServices();
		for (UPnPService service : services)
		{
			WANxConnectionServiceImpl wcs;
			if ((wcs = WANxConnectionServiceImpl.createInstance(service)) != null)
			{
				defaultService = wcs;

				if (isConnected())
				{
					logger.info(
						"WANConnectionDevice '{}': default-service={} **Connected**",
						getUDN(),
						defaultService.getId());

					break;
				}
			}
		}

		if (defaultService == null)
		{
			logger.warn("WANConnectionDevice: no connection service on '{}' device ({})",
				getUDN(), getFriendlyName());

			throw new RuntimeException("No Connection Service available of this device.");
		}

		if (!isConnected())
		{
			logger.info("WANConnectionDevice '{}': default-service={} (NOT Connected)",
				getUDN(), defaultService.getId());
		}
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Public methods

	@Override
	public synchronized WANxConnectionService getDefaultService()
	{
		return defaultService;
	}






	public synchronized boolean isConnected()
	{
		if (defaultService != null)
		{
			StatusInfo si = defaultService.getStatusInfo();
			if (si != null)
			{
				if (si.getConnectionState() == StatusInfo.ConnectionState.Connected)
				{
					return true;
				}
			}
		}

		return false;
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Package methods

	synchronized void dispose()
	{
		if (defaultService != null)
		{
			defaultService.dispose();
		}

		defaultService = null;
	}
}
