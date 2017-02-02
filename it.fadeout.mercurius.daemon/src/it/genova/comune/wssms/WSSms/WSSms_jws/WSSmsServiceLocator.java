/**
 * WSSmsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.genova.comune.wssms.WSSms.WSSms_jws;

public class WSSmsServiceLocator extends org.apache.axis.client.Service implements it.genova.comune.wssms.WSSms.WSSms_jws.WSSmsService {

    public WSSmsServiceLocator() {
    }


    public WSSmsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSSmsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSSms
    private java.lang.String WSSms_address = "http://wssms.comune.genova.it/WSSms/WSSms.jws";

    public java.lang.String getWSSmsAddress() {
        return WSSms_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSSmsWSDDServiceName = "WSSms";

    public java.lang.String getWSSmsWSDDServiceName() {
        return WSSmsWSDDServiceName;
    }

    public void setWSSmsWSDDServiceName(java.lang.String name) {
        WSSmsWSDDServiceName = name;
    }

    public it.genova.comune.wssms.WSSms.WSSms_jws.WSSms getWSSms() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSSms_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSSms(endpoint);
    }

    public it.genova.comune.wssms.WSSms.WSSms_jws.WSSms getWSSms(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.genova.comune.wssms.WSSms.WSSms_jws.WSSmsSoapBindingStub _stub = new it.genova.comune.wssms.WSSms.WSSms_jws.WSSmsSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSSmsWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSSmsEndpointAddress(java.lang.String address) {
        WSSms_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.genova.comune.wssms.WSSms.WSSms_jws.WSSms.class.isAssignableFrom(serviceEndpointInterface)) {
                it.genova.comune.wssms.WSSms.WSSms_jws.WSSmsSoapBindingStub _stub = new it.genova.comune.wssms.WSSms.WSSms_jws.WSSmsSoapBindingStub(new java.net.URL(WSSms_address), this);
                _stub.setPortName(getWSSmsWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSSms".equals(inputPortName)) {
            return getWSSms();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://wssms.comune.genova.it/WSSms/WSSms.jws", "WSSmsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://wssms.comune.genova.it/WSSms/WSSms.jws", "WSSms"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSSms".equals(portName)) {
            setWSSmsEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
