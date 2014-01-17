package it.fadeout.mercurius.daemon.SmsUtils.impl;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import it.fadeout.mercurius.business.Forward;
import it.fadeout.mercurius.daemon.SmsUtils.ISmsUtil;
import it.fadeout.mercurius.daemon.business.ForwardResult;
import it.fadeout.mercurius.daemon.business.MessageResults;
import it.fadeout.mercurius.daemon.business.Sms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Sends SMS With the Acronet SMS Machine
 * @author p.campanella
 *
 */
public class SmsUtilsAcroNet implements ISmsUtil{
	
	/**
	 * Pin for the SIM Card
	 */
	String m_sPin;
	
	/**
	 * Com Port in use
	 */
	String m_sComPort = "COM1";
	
	int WAIT_SHORT = 1500;
	int WAIT_LONG = 15000;

	/**
	 * Intializes the system
	 */
	@Override
	public boolean initializeSMS(HashMap<String, Object> aoParams) {
		
		if (aoParams != null) {
			if (aoParams.containsKey("sPin"))  {
				m_sPin = (String) aoParams.get("sPin");
			}
			
			if (aoParams.containsKey("sComPort"))  {
				m_sComPort = (String) aoParams.get("sComPort");
			}
		}
		
		return internalInit();
	}
	
	/**
	 * Internal Init: opens the port and sets the PIN
	 * @return
	 */
	protected boolean internalInit() {
		
		if (m_sComPort==null) return false;
		if (m_sComPort.isEmpty()) return false;
		
		try {
	        CommPortIdentifier oPortIdentifier = CommPortIdentifier.getPortIdentifier(m_sComPort);
	        if ( oPortIdentifier.isCurrentlyOwned() )
	        {
	            System.out.println("Error: Port is currently in use");
	            return false;
	        }
	        else
	        {
	            CommPort oCommPort = oPortIdentifier.open("Communicator",2000);
	            
	            if ( oCommPort instanceof SerialPort )
	            {
	                SerialPort serialPort = (SerialPort) oCommPort;
	                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	                
	                InputStream in = serialPort.getInputStream();
	                OutputStream out = serialPort.getOutputStream();                
	                
	                String sCommand = "AT+CPIN="+m_sPin+"\r\n";
	                out.write(sCommand.getBytes());
	                
	                Thread.sleep(WAIT_LONG);
	                
	                oCommPort.close();
	            }
	            else
	            {
	            	System.out.println("Error: Only serial ports are handled by this example.");
	                return false;
	            }
	        }  			
		}
		catch(Exception oEx){
			System.out.println("SmsUtilsAcroNet: init exception " + oEx.toString());
			return false;
		}
		
		return true;
   		
	}

	/**
	 * Initializes the System
	 */
	@Override
	public boolean initializeSMS(String sConfigFile) {
		readSmsXmlConfigFile(sConfigFile);
		
		return internalInit();
	}

	/**
	 * Sends a Set of SMS
	 */
	@Override
	public boolean sendSMS(List<Forward> aSmsList) {
		
		if (aSmsList == null) return true;
		if (aSmsList.size()==0) return true;
		
		System.out.println("Sms Utils Acronet: sendSMS called");
				
		Date dtNow = new Date();
		
		for(int iSms = 0; iSms<aSmsList.size(); iSms++) {
			
			System.out.println("Sms Utils Acronet: sending message " + iSms);
			
			Forward oMessage  = aSmsList.get(iSms);
			
			boolean bRet = sendSms(oMessage.getAddress(), oMessage.getFinalText());
			
			if (bRet)  {
				
				System.out.println("Sms Utils Acronet: message sent");

				oMessage.setIsSent(true);
			}
			
			oMessage.setRetryCount(oMessage.getRetryCount()+1);
			oMessage.setLastSendOn(new Date());
		}
				
		return true;
	}



	/**
	 * Sends a single SMS
	 * @param sPhoneNumber
	 * @param sText
	 * @return
	 */
    protected boolean sendSms( String sPhoneNumber, String sText )
    {
    	
		if (m_sComPort==null) return false;
		if (m_sComPort.isEmpty()) return false;
		if (sText == null) return false;
		
		try {
	        CommPortIdentifier oPortIdentifier = CommPortIdentifier.getPortIdentifier(m_sComPort);
	        if ( oPortIdentifier.isCurrentlyOwned() )
	        {
	            System.out.println("Error: Port is currently in use");
	        }
	        else
	        {
	            CommPort oCommPort = oPortIdentifier.open("Communicator",2000);
	            
	            if ( oCommPort instanceof SerialPort )
	            {
	                SerialPort serialPort = (SerialPort) oCommPort;
	                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	                
	                InputStream in = serialPort.getInputStream();
	                OutputStream out = serialPort.getOutputStream();                
	                	
	                String sCommand = "AT+CMGF=1\r\n";
	                out.write(sCommand.getBytes());
	                
	                Thread.sleep(WAIT_SHORT);
	
	                sCommand = "AT+CMGS=\""+sPhoneNumber+"\"\r\n";
	                out.write(sCommand.getBytes());
	                
	                Thread.sleep(WAIT_SHORT);
	
	                sCommand = sText;
	                out.write(sCommand.getBytes());
	                out.write(0x1A);
	                
	                //TODO: Leggo la risposta del modem?
	                
	                Thread.sleep(WAIT_LONG);
	                
	                oCommPort.close();
	            }
	            else
	            {
	            	System.out.println("Error: Only serial ports are handled by this example.");
	            	return false;
	            }
	        }     
		}
		catch(Exception oEx) {
        	System.out.println("sendSMS Exception: " + oEx.toString());
        	return false;			
		}
		
		return true;
    }
    
    /**
     * Reads the configuration from an XML File
     * @param sFilePath
     * @return
     */
	protected boolean readSmsXmlConfigFile(String sFilePath) {
		File oXmlConfig = new File(sFilePath);

		if (oXmlConfig.exists()) {

			try {
				SAXBuilder oBuilder = new SAXBuilder();
				Document oDocument = oBuilder.build(oXmlConfig);

				Element oRoot = oDocument.getRootElement();
				Element oMailNode = oRoot.getChild("ACRONETSMSCONFIG");

				m_sPin = oMailNode.getChild("PIN").getText();
				m_sComPort = oMailNode.getChild("COM").getText();

				return true;
				
			} 
			catch (IOException oEx) {
				System.out.println("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;
			} 
			catch (JDOMException oEx) {
				System.out.println("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;
			} 
			catch (NumberFormatException oEx) {
				System.out.println("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;
			} 
			catch (Throwable oEx) {
				System.out.println("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;
			}
		}

		return false;
	}
	
	
	
	
	
	
	/**
	 * Sends a single SMS
	 * @param sPhoneNumber
	 * @param sText
	 * @return
	 */
    public String readSms(String sText)
    {
    	
		if (m_sComPort==null) return "";
		if (m_sComPort.isEmpty()) return "";
		if (sText == null) return "";
		
		try {
	        CommPortIdentifier oPortIdentifier = CommPortIdentifier.getPortIdentifier(m_sComPort);
	        if ( oPortIdentifier.isCurrentlyOwned() )
	        {
	            System.out.println("Error: Port is currently in use");
	        }
	        else
	        {
	            CommPort oCommPort = oPortIdentifier.open("Communicator",2000);
	            
	            if ( oCommPort instanceof SerialPort )
	            {
	                SerialPort serialPort = (SerialPort) oCommPort;
	                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	                
	                InputStream in = serialPort.getInputStream();
	                OutputStream out = serialPort.getOutputStream();
	                
	                String sCommand = "AT+CMGF=0\r";
	                out.write(sCommand.getBytes());
	                out.flush();
	                	
	                Thread.sleep(WAIT_SHORT);
	                
	                sCommand = "AT+CMGL=4\r";
	                out.write(sCommand.getBytes());
	                out.flush();
	                
	                Thread.sleep(WAIT_SHORT);
	                
	                Vector<Integer> aoRead = new Vector<Integer>();
	                int iRead = 0;
	                do {
	                	iRead = in.read();
	                	aoRead.add(iRead);
	                }while(iRead!=-1);
	                
	                
	                oCommPort.close();
	            }
	            else
	            {
	            	System.out.println("Error: Only serial ports are handled by this example.");
	            	return "";
	            }
	        }     
		}
		catch(Exception oEx) {
        	System.out.println("sendSMS Exception: " + oEx.toString());
        	return "";			
		}
		
		return "";
    }
}
