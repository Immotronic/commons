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

package fr.immotronic.commons.upnp.device;

import fr.immotronic.commons.upnp.basic.GenericSubDevice;
import fr.immotronic.commons.upnp.service.WANxConnectionService;



/**
 * This interface models a UPnP WANConnectionDevice. Such devices represent a connection to WAN for
 * a given UPnP InternetGatewayDevice. A WANConnectionDevice expose services to retrieve information
 * and configure the WAN connection of a router.
 * 
 * @author Lionel BALME
 */
public interface WANConnectionDevice extends GenericSubDevice
{
	/**
	 * Return the default Connection Service for this WAN Connection Device
	 * 
	 * @return a WANxConnectionService object
	 */
	public WANxConnectionService getDefaultService();






	/**
	 * Return the connection status of the device
	 * 
	 * @return true if the device is connected to the WAN, false otherwise.
	 */
	public boolean isConnected();
}
