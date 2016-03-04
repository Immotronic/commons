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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.osgi.framework.BundleContext;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(publicFactory = false)
@Instantiate
@Provides
class UPnPDeviceManagerComponent implements UPnPEventListener
{
	private final BundleContext bundleContext;
	private final Map<String, InternetGatewayDeviceImpl> internetGatewayDevices;
	private final Collection<WANDeviceImpl> unaffectedWANDevices;
	private final Collection<WANConnectionDeviceImpl> unaffectedWANConnectionDevices;
	private final Map<String, GenericDeviceImpl> allDevices;

	final Logger logger = LoggerFactory.getLogger(UPnPDeviceManagerComponent.class);




	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Constructor

	UPnPDeviceManagerComponent(BundleContext bundleContext)
	{
		this.bundleContext = bundleContext;

		internetGatewayDevices =
			Collections.synchronizedMap(new HashMap<String, InternetGatewayDeviceImpl>());

		unaffectedWANDevices = Collections.synchronizedCollection(new ArrayList<WANDeviceImpl>());

		unaffectedWANConnectionDevices =
			Collections.synchronizedCollection(new ArrayList<WANConnectionDeviceImpl>());

		allDevices = Collections.synchronizedMap(new HashMap<String, GenericDeviceImpl>());

	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Service bindings

	@Bind(optional = true, aggregate = true)
	public void bind(UPnPDevice upnpDevice)
	{
		GenericDeviceImpl device = new GenericDeviceImpl(upnpDevice);

		InternetGatewayDeviceImpl igd;
		WANDeviceImpl wd;
		WANConnectionDeviceImpl wcd;

		if ((igd = InternetGatewayDeviceImpl.createInstance(bundleContext, upnpDevice)) != null)
		{
			bindUnaffectedWANDevice(igd);
			bindUnaffectedWANConnectionDevice(igd);

			internetGatewayDevices.put(igd.getUDN(), igd);
			allDevices.put(igd.getUDN(), igd);

			logger.info("InternetGatewayDevice '{}' has been discovered ({}).", igd.getUDN(),
				igd.getFriendlyName());
		}
		else if ((wd = WANDeviceImpl.createInstance(upnpDevice)) != null)
		{
			logger.debug("WANDevice '{}' has been discovered", wd.getUDN());

			igd = internetGatewayDevices.get(device.getParentUDN());
			if (igd != null)
			{
				igd.addWANDeviceUDNs(wd);
				bindUnaffectedWANConnectionDevice(igd);
			}
			else
			{
				unaffectedWANDevices.add(wd);
				logger.debug("WANDevice '{}' has no parent yet", wd.getUDN());
			}

			allDevices.put(wd.getUDN(), wd);
		}
		else if ((wcd = WANConnectionDeviceImpl.createInstance(upnpDevice)) != null)
		{

			logger.debug("WANConnectionDevice '{}' has been discovered", wcd.getUDN());

			boolean binded = false;
			Iterator<InternetGatewayDeviceImpl> i = internetGatewayDevices.values().iterator();
			while (i.hasNext())
			{
				InternetGatewayDeviceImpl item = i.next();
				if (item.addWANConnectionDevice(wcd))
				{
					binded = true;

					logger.debug("WANConnectionDevice '{}' has been binded to {}.", wcd.getUDN(),
						item.getUDN());

					break;
				}
			}

			if (!binded)
			{
				unaffectedWANConnectionDevices.add(wcd);

				logger.debug("WANConnectionDevice '{}' has no parent yet", wcd.getUDN());
			}

			allDevices.put(wcd.getUDN(), wcd);
		}
		else
		{
			logger.info("An UPnPDevice has been discovered: '{}', but is not yet supported.",
				device.getFriendlyName());
		}
	}






	@Unbind
	public void unbind(UPnPDevice upnpDevice)
	{
		GenericDeviceImpl device = new GenericDeviceImpl(upnpDevice);

		InternetGatewayDeviceImpl igd = internetGatewayDevices.remove(device.getUDN());
		if (igd != null)
		{
			igd.dispose();
			return;
		}

		Iterator<InternetGatewayDeviceImpl> k = internetGatewayDevices.values().iterator();
		while (k.hasNext())
		{
			InternetGatewayDeviceImpl item = k.next();
			if (item.removeWANDeviceUDN(device))
			{
				k.remove();
				return;
			}
			else if (item.removeWANConnectionDevice(device))
			{
				k.remove();
				return;
			}
		}

		Iterator<WANDeviceImpl> i = unaffectedWANDevices.iterator();
		while (i.hasNext())
		{
			WANDeviceImpl item = i.next();
			if (item.getUDN().equals(device.getUDN()))
			{
				i.remove();
				return;
			}
		}

		Iterator<WANConnectionDeviceImpl> j = unaffectedWANConnectionDevices.iterator();
		while (j.hasNext())
		{
			WANConnectionDeviceImpl item = j.next();
			if (item.getUDN().equals(device.getUDN()))
			{
				j.remove();
				return;
			}
		}
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Private methods

	private void bindUnaffectedWANDevice(InternetGatewayDeviceImpl igd)
	{
		Iterator<WANDeviceImpl> i = unaffectedWANDevices.iterator();
		while (i.hasNext())
		{
			WANDeviceImpl item = i.next();
			if (item.getParentUDN().equals(igd.getUDN()))
			{
				i.remove();
				igd.addWANDeviceUDNs(item);
			}
		}
	}






	private void bindUnaffectedWANConnectionDevice(InternetGatewayDeviceImpl igd)
	{
		Iterator<WANConnectionDeviceImpl> i = unaffectedWANConnectionDevices.iterator();
		while (i.hasNext())
		{
			WANConnectionDeviceImpl item = i.next();
			if (igd.addWANConnectionDevice(item))
			{
				i.remove();
			}
		}
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Event listeners

	@SuppressWarnings("rawtypes")
	@Override
	public void notifyUPnPEvent(String deviceId, String serviceId, Dictionary events)
	{
		logger.debug( " **** Events received from {}#{}.", deviceId, serviceId);

		GenericDeviceImpl device = this.allDevices.get(deviceId);
		if (device != null)
		{
			device.setServiceStateVariables(serviceId, events);
		}
	}

}
