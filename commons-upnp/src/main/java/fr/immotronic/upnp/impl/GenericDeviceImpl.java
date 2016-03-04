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

import org.osgi.service.upnp.UPnPDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.immotronic.commons.upnp.basic.GenericDevice;



class GenericDeviceImpl implements GenericDevice
{
	private final UPnPDevice device;

	private final String friendlyName;
	private final String manufacturer;
	private final String parentUDN;
	private final String type;
	private final String udn;

	final Logger logger = LoggerFactory.getLogger(GenericDeviceImpl.class);




	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Constructor

	GenericDeviceImpl(UPnPDevice device)
	{
		if (device == null)
		{
			throw new IllegalArgumentException();
		}

		this.device = device;

		Dictionary<?, ?> deviceDescription = device.getDescriptions(null);

		udn = (String) deviceDescription.get(UPnPDevice.UDN);
		parentUDN = (String) deviceDescription.get(UPnPDevice.PARENT_UDN);
		friendlyName = (String) deviceDescription.get(UPnPDevice.FRIENDLY_NAME);
		manufacturer = (String) deviceDescription.get(UPnPDevice.MANUFACTURER);
		type = (String) deviceDescription.get(UPnPDevice.TYPE);
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getFriendlyName()
	{
		return friendlyName;
	}






	public String getManufacturer()
	{
		return manufacturer;
	}






	public String getParentUDN()
	{
		return parentUDN;
	}






	@Override
	public String getType()
	{
		return type;
	}






	@Override
	public String getUDN()
	{
		return udn;
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Protected methods

	protected UPnPDevice getDevice()
	{
		return device;
	}






	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Package methods

	void setServiceStateVariables(String serviceId, Dictionary<?, ?> events)
	{
		logger.warn("Subclass does not override 'setServiceStateVariables' "
			+ "methods. Incomings events are discarded.");
	}
}
