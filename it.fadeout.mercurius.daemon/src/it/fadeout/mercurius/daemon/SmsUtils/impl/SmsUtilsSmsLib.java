package it.fadeout.mercurius.daemon.SmsUtils.impl;

import it.fadeout.mercurius.business.Forward;
import it.fadeout.mercurius.daemon.SmsUtils.ISmsUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

public class SmsUtilsSmsLib implements ISmsUtil{
	
	String m_sPin;
	String m_sComPort;
	boolean m_bInitialized = false;

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
		
		internalInitialize();
		
		return true;
	}

	@Override
	public boolean initializeSMS(String sConfigFile) {
		File oXmlConfig = new File(sConfigFile);

		if (oXmlConfig.exists()) {

			try {
				SAXBuilder oBuilder = new SAXBuilder();
				Document oDocument = oBuilder.build(oXmlConfig);

				Element oRoot = oDocument.getRootElement();
				Element oMailNode = oRoot.getChild("ACRONETSMSCONFIG");

				m_sPin = oMailNode.getChild("PIN").getText();
				m_sComPort = oMailNode.getChild("COM").getText();
				
				internalInitialize();

				return true;
				
			} 
			catch (IOException oEx) {
				System.out.println("SmsUtilsSmsLib.initializeSMS: " + oEx.getMessage());
				return false;
			} 
			catch (JDOMException oEx) {
				System.out.println("SmsUtilsSmsLib.initializeSMS: " + oEx.getMessage());
				return false;
			} 
			catch (NumberFormatException oEx) {
				System.out.println("SmsUtilsSmsLib.initializeSMS: " + oEx.getMessage());
				return false;
			} 
			catch (Throwable oEx) {
				System.out.println("SmsUtilsSmsLib.initializeSMS: " + oEx.getMessage());
				return false;
			}
		}

		return false;
	}
	
	protected void internalInitialize() {
		
		if (m_bInitialized== false) {
			// Create the notification callback method for inbound & status report messages.
			InboundNotification inboundNotification = new InboundNotification();
			// Create the notification callback method for inbound voice calls.
			CallNotification callNotification = new CallNotification();
			//Create the notification callback method for gateway statuses.
			GatewayStatusNotification statusNotification = new GatewayStatusNotification();
			OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();
			
			// Create the Gateway representing the serial GSM modem.
			SerialModemGateway gateway = new SerialModemGateway("modem."+m_sComPort, m_sComPort, 57600, "Siemens", "S45");
			// Set the modem protocol to PDU (alternative is TEXT). PDU is the default, anyway...
			gateway.setProtocol(Protocols.PDU);
			// Do we want the Gateway to be used for Inbound messages?
			gateway.setInbound(true);
			// Do we want the Gateway to be used for Outbound messages?
			gateway.setOutbound(true);
			// Let SMSLib know which is the SIM PIN.
			gateway.setSimPin(m_sPin);
			//gateway.setFrom("+393425404948");
			//gateway.setSmscNumber("+393492000200");
			// Set up the notification methods.
			Service.getInstance().setInboundMessageNotification(inboundNotification);
			Service.getInstance().setCallNotification(callNotification);
			Service.getInstance().setGatewayStatusNotification(statusNotification);
			Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);
			// Add the Gateway to the Service object.
			try {
				Service.getInstance().addGateway(gateway);
			} catch (GatewayException e) {
				System.out.println("SmsUtilsSmsLib.internalInitalize: eccezione nel tentativo di aggiungere il gateway " + e.toString());
			}	
			
			m_bInitialized = true;
		}		
	}

	@Override
	public boolean sendSMS(List<Forward> aSmsList) {
				
		try {			

			
			Service.getInstance().startService();
			Date dtNow = new Date();
			
			for(int iSms = 0; iSms<aSmsList.size(); iSms ++) {
				Forward oSms = aSmsList.get(iSms);
				
				OutboundMessage oMessage = new OutboundMessage(oSms.getAddress(), oSms.getFinalText());
				boolean bRet = Service.getInstance().sendMessage(oMessage);
				
				if (bRet)  {
					
					System.out.println("Sms Utils SmsLib: message sent");
					oSms.setIsSent(true);
				}
				
				oSms.setLastSendOn(new Date());
				oSms.setRetryCount(oSms.getRetryCount() + 1);
			}
			
		} catch (TimeoutException e) {
			System.out.println("SmsUtilsSmsLib.sendSMS: " + e.getMessage());
			return false;
		} catch (GatewayException e) {
			System.out.println("SmsUtilsSmsLib.sendSMS: " + e.getMessage());
			return false;
		} catch (SMSLibException e) {
			System.out.println("SmsUtilsSmsLib.sendSMS: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("SmsUtilsSmsLib.sendSMS: " + e.getMessage());
			return false;
		} catch (InterruptedException e) {
			System.out.println("SmsUtilsSmsLib.sendSMS: " + e.getMessage());
			return false;
		}
		finally {
			try {
				Service.getInstance().stopService();
			}
			catch (Exception oEx)
			{
				System.out.println("SmsUtilsSmsLib.sendSMS.stopService: " + oEx.getMessage());
			}
			
		}
		
		return true;
	}


	public class InboundNotification implements IInboundMessageNotification
	{
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg)
		{
			if (msgType == MessageTypes.INBOUND) System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
			else if (msgType == MessageTypes.STATUSREPORT) System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);
		}
	}

	public class CallNotification implements ICallNotification
	{
		public void process(AGateway gateway, String callerId)
		{
			System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
		}
	}

	public class GatewayStatusNotification implements IGatewayStatusNotification
	{
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus)
		{
			System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}

	public class OrphanedMessageNotification implements IOrphanedMessageNotification
	{
		public boolean process(AGateway gateway, InboundMessage msg)
		{
			System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
			System.out.println(msg);
			// Since we are just testing, return FALSE and keep the orphaned message part.
			return false;
		}
	}
}
