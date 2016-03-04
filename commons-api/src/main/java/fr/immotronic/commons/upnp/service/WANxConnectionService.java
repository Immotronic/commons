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

package fr.immotronic.commons.upnp.service;

import fr.immotronic.commons.upnp.basic.GenericService;
import fr.immotronic.commons.upnp.type.NATRSIPStatus;
import fr.immotronic.commons.upnp.type.PortMappingEntry;
import fr.immotronic.commons.upnp.type.StatusInfo;
import fr.immotronic.commons.upnp.type.PortMappingEntry.Protocol;



/**
 * This interface models a UPnP generic WAN Connection Service that works for IP or PPP connection.
 * This service offers operation to get information from the router (about the status of its
 * connection to the WAN, up time, existing NAT rules) and to configure the router (for instance
 * setting / deleting NAT rules).
 * 
 * @author Lionel BALME
 */
public interface WANxConnectionService extends GenericService
{
	/**
	 * Ask the gateway to create a port mapping (also known as a NAT rule).
	 * 
	 * @param remoteHost
	 *            This variable represents the source of inbound IP packets. This will be a wildcard
	 *            in most cases (i.e. an empty string or null). NAT vendors are only required to
	 *            support wildcards. A non-wildcard value will allow for "narrow" port mappings,
	 *            which may be desirable in some usage scenarios.When RemoteHost is a wildcard, all
	 *            traffic sent to the ExternalPort on the WAN interface of the gateway is forwarded
	 *            to the InternalClient on the InternalPort. When RemoteHost is specified as one
	 *            external IP address as opposed to a wildcard, the NAT will only forward inbound
	 *            packets from this RemoteHost to the InternalClient, all other packets will be
	 *            dropped.
	 * 
	 * @param externalPort
	 *            This variable represents the external port that the NAT gateway would "listen" on
	 *            for connection requests to a corresponding InternalPort on an InternalClient..
	 *            Inbound packets to this external port on the WAN interface of the gateway should
	 *            be forwarded to InternalClient on the InternalPort on which the message was
	 *            received. If this value is specified as a wildcard (i.e. 0), connection request on
	 *            all external ports (that are not otherwise mapped) will be forwarded to
	 *            InternalClient. In the wildcard case, the value(s) of InternalPort on
	 *            InternalClient are ignored by the IGD for those connections that are forwarded to
	 *            InternalClient. Obviously only one such entry can exist in the NAT at any time and
	 *            conflicts are handled with a "first write wins" behavior.
	 * 
	 * @param protocol
	 *            This variable represents the protocol of the port mapping. Possible values are TCP
	 *            or UDP.
	 * 
	 * @param internalPort
	 *            This variable represents the port on InternalClient that the gateway should
	 *            forward connection requests to. A value of 0 is not allowed. NAT implementations
	 *            that do not permit different values for ExternalPort and InternalPort will return
	 *            an error.
	 * 
	 * @param internalClient
	 *            This variable represents the IP address or DNS host name of an internal client (on
	 *            the residential LAN). Note that if the gateway does not support DHCP, it does not
	 *            have to support DNS host names. Consequently, support for an IP address is
	 *            mandatory and support for DNS host names is recommended. This value cannot be a
	 *            wildcard (i.e. empty string). It must be possible to set the InternalClient to the
	 *            broadcast IP address 255.255.255.255 for UDP mappings. This is to enable multiple
	 *            NAT clients to use the same well-known port simultaneously.
	 * 
	 * @param enabled
	 *            This variable allows security conscious users to disable and enable dynamic and
	 *            static NAT port mappings on the IGD.
	 * 
	 * @param description
	 *            This is a string representation of a port mapping and is applicable for static and
	 *            dynamic port mappings. The format of the description string is not specified and
	 *            is application dependent. If specified, the description string can be displayed to
	 *            a user via the UI of a control point, enabling easier management of port mappings.
	 *            The description string for a port mapping (or a set of related port mappings) may
	 *            or may not be unique across multiple instantiations of an application on multiple
	 *            nodes in the residential LAN.
	 * 
	 * @param leaseDuration
	 *            This variable determines the time to live in seconds of a port-mapping lease. A
	 *            value of 0 means the port mapping is static. Non-zero values will allow support
	 *            for dynamic port mappings. Note that static port mappings do not necessarily mean
	 *            persistence of these mappings across device resets or reboots. It is up to a
	 *            gateway vendor to implement persistence as appropriate for their IGD device.
	 * 
	 * @return true if the operation succeed, false otherwise.
	 */
	public boolean addPortMapping(	String remoteHost,
									Integer externalPort,
									Protocol protocol,
									Integer internalPort,
									String internalClient,
									Boolean enabled,
									String description,
									Long leaseDuration);






	/**
	 * Ask the gateway to remove a port mapping
	 * 
	 * @param remoteHost
	 *            The IP address of the remote host, or an empty string for wildcard value.
	 * 
	 * @param externalPort
	 *            The opened external port
	 * 
	 * @param protocol
	 *            The protocol of the port mapping (Either TCP or UDP)
	 * 
	 * @return true if the operation succeed, false otherwise.
	 * 
	 * @see #addPortMapping(String, Integer, PortMappingEntry.Protocol, Integer, String, Boolean,
	 *      String, Long) addPortMapping
	 */
	public boolean deletePortMapping(String remoteHost, Integer externalPort, Protocol protocol);






	/**
	 * Return the IPv4 address of the device on the WAN.
	 * 
	 * @return An IPv4 address string (ex: "90.42.73.18").
	 */
	public String getExternalIPAddress();






	/**
	 * Return information about NAT and RSIP for this connection.
	 * 
	 * @return a NATRSIPStatus object or null if the operation failed.
	 */
	public NATRSIPStatus getNATRSIPStatus();






	/**
	 * Get the specification of the port mapping stored at the given index.
	 * 
	 * @param index
	 *            the index of the Port Mapping to get. First port mapping has index 0.
	 * 
	 * @return A PortMappingEntry object or null if no port mapping exists at the given index or if
	 *         the operation failed for another reason.
	 */
	public PortMappingEntry getPortMappingEntryByIndex(int index);






	/**
	 * Return information about the status of the device: ( Connection state, Last Connection error,
	 * and uptime )
	 * 
	 * @return a StatusInfo object or null if the operation failed.
	 */
	public StatusInfo getStatusInfo();
}
