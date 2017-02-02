package it.genova.comune.wssms.WSSms.WSSms_jws;

public class WSSmsProxy implements it.genova.comune.wssms.WSSms.WSSms_jws.WSSms {
  private String _endpoint = null;
  private it.genova.comune.wssms.WSSms.WSSms_jws.WSSms wSSms = null;
  
  public WSSmsProxy() {
    _initWSSmsProxy();
  }
  
  public WSSmsProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSSmsProxy();
  }
  
  private void _initWSSmsProxy() {
    try {
      wSSms = (new it.genova.comune.wssms.WSSms.WSSms_jws.WSSmsServiceLocator()).getWSSms();
      if (wSSms != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSSms)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSSms)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSSms != null)
      ((javax.xml.rpc.Stub)wSSms)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.genova.comune.wssms.WSSms.WSSms_jws.WSSms getWSSms() {
    if (wSSms == null)
      _initWSSmsProxy();
    return wSSms;
  }
  
  public java.lang.String wsCancellaSms(java.lang.String sXmlFile) throws java.rmi.RemoteException{
    if (wSSms == null)
      _initWSSmsProxy();
    return wSSms.wsCancellaSms(sXmlFile);
  }
  
  public java.lang.String wsCancellaLottoSms(java.lang.String sXmlFile) throws java.rmi.RemoteException{
    if (wSSms == null)
      _initWSSmsProxy();
    return wSSms.wsCancellaLottoSms(sXmlFile);
  }
  
  public java.lang.String wsGetInfoLottoSms(java.lang.String sXmlFile) throws java.rmi.RemoteException{
    if (wSSms == null)
      _initWSSmsProxy();
    return wSSms.wsGetInfoLottoSms(sXmlFile);
  }
  
  public java.lang.String wsSetPasswordSms(java.lang.String sXmlFile) throws java.rmi.RemoteException{
    if (wSSms == null)
      _initWSSmsProxy();
    return wSSms.wsSetPasswordSms(sXmlFile);
  }
  
  public java.lang.String wsSetInvioSms(java.lang.String sXmlFile) throws java.rmi.RemoteException{
    if (wSSms == null)
      _initWSSmsProxy();
    return wSSms.wsSetInvioSms(sXmlFile);
  }
  
  
}