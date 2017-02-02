/**
 * WSSms.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.genova.comune.wssms.WSSms.WSSms_jws;

public interface WSSms extends java.rmi.Remote {
    public java.lang.String wsCancellaSms(java.lang.String sXmlFile) throws java.rmi.RemoteException;
    public java.lang.String wsCancellaLottoSms(java.lang.String sXmlFile) throws java.rmi.RemoteException;
    public java.lang.String wsGetInfoLottoSms(java.lang.String sXmlFile) throws java.rmi.RemoteException;
    public java.lang.String wsSetPasswordSms(java.lang.String sXmlFile) throws java.rmi.RemoteException;
    public java.lang.String wsSetInvioSms(java.lang.String sXmlFile) throws java.rmi.RemoteException;
}
