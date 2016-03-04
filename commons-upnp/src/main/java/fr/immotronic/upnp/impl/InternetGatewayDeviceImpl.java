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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.upnp.UPnPDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.upnp.basic.GenericSubDevice;
import fr.immotronic.commons.upnp.device.InternetGatewayDevice;
import fr.immotronic.commons.upnp.device.WANConnectionDevice;



final class InternetGatewayDeviceImpl extends GenericDeviceImpl implements InternetGatewayDevice
{
	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Statics Initializers & Methods

	private static final String BASE_URN = "urn:schemas-upnp-org:device:InternetGatewayDevice";
	private static final String[] SUPPORTED_VERSIONS = { "1", "2" };

	final Logger logger = LoggerFactory.getLogger(InternetGatewayDeviceImpl.class);




	static InternetGatewayDeviceImpl createInstance(BundleContext bundleContext, UPnPDevice device)
	{
		try
		{
			return new InternetGatewayDeviceImpl(bundleContext, device);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Fields

	private WANConnectionDevice defaultWANConnectionDevice = null;

	private final ServiceRegistration serviceRegistration;
	private final Map<String, WANConnectionDeviceImpl> wanConnectionDevices;
	private final Collection<String> wanDevicesUDNs;






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Constructor

	private InternetGatewayDeviceImpl(BundleContext bundleContext, UPnPDevice device)
	{
		super(device);

		if (bundleContext == null)
		{
			throw new IllegalArgumentException();
		}

		if (!UPnPUtils.isSupportedDevice(BASE_URN, SUPPORTED_VERSIONS, getType()))
		{
			throw new IllegalArgumentException();
		}

		wanConnectionDevices =
			Collections.synchronizedMap(new HashMap<String, WANConnectionDeviceImpl>());

		wanDevicesUDNs = Collections.synchronizedCollection(new ArrayList<String>());

		serviceRegistration =
			bundleContext.registerService(InternetGatewayDevice.class.getName(), this, null);
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Public visibility methods

	@Override
	public synchronized WANConnectionDevice[] getAvailableWANConnectionDevice()
	{
		return wanConnectionDevices.values().toArray(new WANConnectionDevice[0]);
	}






	@Override
	public synchronized WANConnectionDevice getDefaultWANConnectionDevice()
	{
		return defaultWANConnectionDevice;
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Package visibility methods

	synchronized boolean addWANConnectionDevice(WANConnectionDeviceImpl wanConnectionDevice)
	{
		if (wanDevicesUDNs.contains(wanConnectionDevice.getParentUDN()))
		{
			logger.debug("IGD '{}' get WANConnectionDevice '{}'.", getUDN(),
				wanConnectionDevice.getUDN());

			wanConnectionDevices.put(wanConnectionDevice.getUDN(), wanConnectionDevice);
			if (defaultWANConnectionDevice == null || !defaultWANConnectionDevice.isConnected())
			{
				defaultWANConnectionDevice = wanConnectionDevice;

				logger.debug("IGD '{}' get a DEFAULT WANConnectionDevice '{}'", getUDN(),
					wanConnectionDevice.getUDN());
			}

			return true;
		}

		return false;
	}






	synchronized void addWANDeviceUDNs(GenericSubDevice wanDevice)
	{
		if (getUDN().equals(wanDevice.getParentUDN()))
		{
			wanDevicesUDNs.add(wanDevice.getUDN());
			logger.debug("IGD '{}' get WANDevice '{}'", getUDN(), wanDevice.getUDN());
		}
	}






	synchronized boolean removeWANConnectionDevice(GenericDeviceImpl device)
	{
		WANConnectionDeviceImpl wcd = wanConnectionDevices.remove(device.getUDN());
		if (wcd != null)
		{
			wcd.dispose();
			dispose();
			return true;
		}
		
		return false;
	}






	synchronized boolean removeWANDeviceUDN(GenericDeviceImpl device)
	{
		if(wanDevicesUDNs.remove(device.getUDN()))
		{
			dispose();
			return true;
		}
		
		return false;
	}






	synchronized void dispose()
	{
		serviceRegistration.unregister();

		wanDevicesUDNs.clear();
		Iterator<WANConnectionDeviceImpl> i = wanConnectionDevices.values().iterator();
		while (i.hasNext())
		{
			i.next().dispose();
		}
		
		defaultWANConnectionDevice = null;
		
		wanConnectionDevices.clear();
	}
}
