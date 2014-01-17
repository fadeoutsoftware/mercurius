package it.fadeout.mercurius.daemon.SmsUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.lf5.LogLevel;
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
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

public class SMSReader {
	
	String m_sPin;
	String m_sComPort;
	boolean m_bInitialized = false;
	
	public boolean readSmsXmlConfigFile(String sFilePath) {
		
		System.out.println("SMSReader: read Sms Xml Config File");
		
		File oXmlConfig = new File(sFilePath);

		if (oXmlConfig.exists()) {

			try {
				SAXBuilder oBuilder = new SAXBuilder();
				Document oDocument = oBuilder.build(oXmlConfig);

				Element oRoot = oDocument.getRootElement();
				Element oMailNode = oRoot.getChild("ACRONETSMSCONFIG");

				m_sPin = oMailNode.getChild("PIN").getText();
				m_sComPort = oMailNode.getChild("COM").getText();
				
				System.out.println("SMSReader: using Com Port " + m_sComPort);

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

	
	protected void InitializeSmsService() {
		try {
			System.out.println("SMSReader.Initialize: initializing Sms Reader Service");
			
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
			// Set up the notification methods.
			Service.getInstance().setInboundMessageNotification(inboundNotification);
			Service.getInstance().setCallNotification(callNotification);
			Service.getInstance().setGatewayStatusNotification(statusNotification);
			Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);
			// Add the Gateway to the Service object.
			Service.getInstance().addGateway(gateway);
		} catch (Exception e) {
			System.out.println("SMSReader.initialize: Eccezione " + e.getMessage());
		}	
		
		
		m_bInitialized = true;
	
	}
	
	public ArrayList<InboundMessage> readNewMessages() throws Exception
	{
		System.out.println("SMSReader: readNewMessages");
		
		// Define a list which will hold the read messages.
		ArrayList<InboundMessage> msgList;
		try
		{
			if (m_bInitialized== false && Service.getInstance().getGateways().size() == 0) {
				InitializeSmsService();
			}
			
			System.out.println("SMSReader: start service");

			// Similarly, you may define as many Gateway objects, representing
			// various GSM modems, add them in the Service object and control all of them.
			// Start! (i.e. connect to all defined Gateways)
			Service.getInstance().startService();
			
			System.out.println("SMSReader: Service started, reading messages");

			// Read Messages. The reading is done via the Service object and
			// affects all Gateway objects defined. This can also be more directed to a specific
			// Gateway - look the JavaDocs for information on the Service method calls.
			msgList = new ArrayList<InboundMessage>();
			Service.getInstance().readMessages(msgList, MessageClasses.UNREAD);
			
			System.out.println("SMSReader: Messages read return");
			
			return msgList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			Service.getInstance().stopService();
		}
		
		return null;
	}

	
	public void DeleteMessages(ArrayList<InboundMessage> aoMessagesToDelete) {
		
		System.out.println("SMSReader: DeleteMessages");
		
		if (aoMessagesToDelete == null)return;
		if (aoMessagesToDelete.size() == 0) return;
		
		try
		{
			if (m_bInitialized== false && Service.getInstance().getGateways().size() == 0) {
				InitializeSmsService();
			}
			
			System.out.println("SMSReader.DeleteMessages: start service");

			// Similarly, you may define as many Gateway objects, representing
			// various GSM modems, add them in the Service object and control all of them.
			// Start! (i.e. connect to all defined Gateways)
			Service.getInstance().startService();
			
			System.out.println("SMSReader.DeleteMessages: Service started, reading messages");

			// Delete!!
			for (InboundMessage msg : aoMessagesToDelete) {
				Service.getInstance().deleteMessage(msg);
			}
			
			
			System.out.println("SMSReader.DeleteMessages: Messages Delete return");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				Service.getInstance().stopService();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (GatewayException e) {
				e.printStackTrace();
			} catch (SMSLibException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
