package it.fadeout.mercurius.daemon;

import it.fadeout.mercurius.business.Forward;
import it.fadeout.mercurius.daemon.EMailUtils.EMailUtils;
import it.fadeout.mercurius.daemon.EMailUtils.EMailUtilsConfig;
import it.fadeout.mercurius.daemon.SmsUtils.ISmsUtil;
import it.fadeout.mercurius.daemon.SmsUtils.SMSReader;
import it.fadeout.mercurius.daemon.SmsUtils.impl.SmsUtilsAcroNet;
import it.fadeout.mercurius.daemon.business.ExternalDbSetting;
import it.fadeout.mercurius.data.ForwardsRepository;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.xml.rpc.ServiceException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.smslib.InboundMessage;


public class MercuriusDaemon {

	private static int s_iPollingTime = 5;
	
	private static String s_SmsMailReceiver;
	
	private static String s_SmsTestReceiver;

	private static int s_iRetry = 3;
	
	private static int s_iEnableSms = 1;
	private static int s_iEnableMail = 1;
	
	private static String m_sSMSUtilsClass = "it.fadeout.nie.SmsUtils.impl.SmsUtilsComuneGenova";
	
	private static SMSReader s_oReader;
	
	public static ArrayList<ExternalDbSetting> s_aoExternalDbSettings = new ArrayList<ExternalDbSetting>();
	
	public static Semaphore s_oMailSemaphore = new Semaphore(1); 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyS0");
			
		// Domain Control
		if (args == null) {
			System.out.println("Mercurius Daemon: error reading configuration: set config path as firt argument");
			return;
		}

		if (args.length < 1) {
			System.out.println("Mercurius Daemon: error reading configuration: set config path as firt argument");
			return;
		}
				
		// Prendo il path completo del file di configurazione
		String sConfigFilePath = (String) args[0];

		System.out.println("Initializing Mercurius Daemon");
		// Initializzo il logger
		Initialize(sConfigFilePath);
		System.out.println("Mercurius Daemon initialized");
		
		// Init Hal

		// Init Email
		System.out.println("Initializing Mailing System");
		EMailUtilsConfig oEmailConfig = new EMailUtilsConfig();
		oEmailConfig.initEMailConfigByXmlFile(sConfigFilePath);
		System.out.println("Mail initialized");

		// Init Sms
		System.out.println("Initializing sms System");
		
		ISmsUtil oSmsUtil = new SmsUtilsAcroNet();
		
		try {
			oSmsUtil = (ISmsUtil) Class.forName(m_sSMSUtilsClass).newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			System.out.println("Mercurius Daemon: eccezione nella creazione dell'util SMS " + e1.toString());
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			System.out.println("Mercurius Daemon: eccezione nella creazione dell'util SMS " + e1.toString());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("Mercurius Daemon: eccezione nella creazione dell'util SMS " + e1.toString());
		}

		
		// Init HAL
		MercuriusHAL.setSmsUtils(oSmsUtil);
		if (s_iEnableMail!=0) 
		{
			MercuriusHAL.initializeEmails(oEmailConfig);

			/*
			String [] asTo = new String[2];
			asTo[0] ="cosimo@acrotec.it";
			asTo[1] = "p.campanella@fadeout.it";
			try {
				EMailUtils.SendMessage(oEmailConfig.getSmtpUser(), oEmailConfig.getSmtpPwd(), "topolino123123@paperopoli.it", asTo, "Prova", "Mail da te stesso", null);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}

		if (s_iEnableSms!=0) 
		{
			MercuriusHAL.initializeSms(sConfigFilePath);
			
			System.out.println("Creating SMS Reader...");
			s_oReader = new SMSReader();
			s_oReader.readSmsXmlConfigFile(sConfigFilePath);
			System.out.println("SMS Reader Created");

		}
		
		Date oLastDate = null;
		
		while (true) {
			
			Date oActualDate = new Date();
			
			if (DayChanged(oActualDate, oLastDate)) {
				oLastDate=oActualDate;
				DailyTask();
			}

			try {
				
				if (s_iEnableSms!=0) 
				{				
					System.out.println("Mercurius DAEMON CYCLE: Read SMS");
					ReadSms();
	
					System.out.println("Mercurius DAEMON CYCLE: SendSms");
					// Invio Sms
					SendSms();
				}

				if (s_iEnableMail!=0)
				{
					System.out.println("Mercurius DAEMON CYCLE: SendEmails");
					
					if (s_oMailSemaphore.tryAcquire(1, TimeUnit.SECONDS))
					{
						try {
							//s_oMailSemaphore.acquire();
							MailThread oSendMail = new MailThread(s_oMailSemaphore);
							oSendMail.run();							
						}
						catch(Exception oExSem)
						{
							System.out.println("Mercurius Daemon: eccezione nel ciclo di invio eseguito: " + oExSem.toString());
							oExSem.printStackTrace();							
						}
					}
				}

			} catch (Throwable oEx) {
				System.out.println("Mercurius Daemon: eccezione nel ciclo di invio eseguito: " + oEx.toString());
				oEx.printStackTrace();
			} finally {
				try {
					System.out.println("Mercurius DAEMON CYCLE: ciclo di invio eseguito, aspetto il prossimo ciclo");
					
					Thread.sleep(s_iPollingTime * 1000);
					
					System.out.println("Mercurius DAEMON CYCLE: START " + Calendar.getInstance().getTime().toString());
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("Mercurius Daemon: eccezione a ciclo di invio eseguito");
					System.out.println("Mercurius Daemon " + e.getMessage());
				}
			}
		}
	}
	
	
	/**
	 * Checks if date is changed
	 * @param oActualDate
	 * @param oLastDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean DayChanged(Date oActualDate, Date oLastDate)
	{
		if (oLastDate==null) return true;
		if (oActualDate.getHours()<8) return false;
		
		long lActualTime = oActualDate.getTime();
		long lLastTime = oLastDate.getTime();
		
		long lDay = 24L*60L*60L*1000L;
		
		if ((lActualTime-lLastTime)>lDay) return true;
		return false;
	}
	
	public static void DailyTask()
	{
		System.out.println("MERCURIUS DAILY TASK");
		try {
			
			if (s_iEnableSms!=0)
			{			
				if (s_SmsTestReceiver != null) {
					ArrayList<Forward> aoSmsList = new ArrayList<Forward>();
					String [] asAddress = s_SmsTestReceiver.split(";");
					
					if (asAddress!=null)
					{
						for (int iSms = 0; iSms<asAddress.length; iSms++)
						{
							Forward oTest1 = new Forward();
							oTest1.setAddress(asAddress[iSms]);
							oTest1.setFinalText("Mercurius Test SMS OK");
							oTest1.setMedia("sms");
							
							aoSmsList.add(oTest1);
						}
						
						MercuriusHAL.sendSMS(aoSmsList);
					}
				}
			}
		}
		catch(Exception oEx) {
			System.out.println("DailyTask Exception: " + oEx.toString());
			oEx.printStackTrace();
		}
	}
	
	/**
	 * Cycle to read new received SMS and forward to mail addresses and write to external db
	 */
	private static void ReadSms() {
		
		try {
			ArrayList<InboundMessage> aoMessages = s_oReader.readNewMessages();
			
			if (aoMessages == null) {
				System.out.println("Mercurius Daemon: ReadSms la lista e' null esco");
				return;
			}

			
			int iCount = aoMessages.size();
			
			System.out.println("Mercurius Daemon: ReadSms Letti: " + iCount);
			
			
			for (InboundMessage oSmsMsg : aoMessages) {
				System.out.println("Mercurius Daemon: SMS RECIVED: " + oSmsMsg);
				
				//DumpSmsMessage(oSmsMsg);
				
				if (oSmsMsg.getText().toUpperCase().equals("PING")) {
					System.out.println("Mercurius Daemon: PING from " + oSmsMsg.getOriginator());					
					MercuriusHAL.sendDirectSMS("+"+oSmsMsg.getOriginator(), "PONG");
				}
				else {
					String sMessage = "FROM: " + oSmsMsg.getOriginator() +"\n";
					sMessage += oSmsMsg.getText();
					
					MercuriusHAL.sendMail(s_SmsMailReceiver, "", sMessage,null);
					
					for (ExternalDbSetting oDbSetting : s_aoExternalDbSettings) {
						System.out.println("Tentativo inserito sms nell'external db " + oDbSetting.m_sDbAddress);
						SerializeReceivedSms(oDbSetting, oSmsMsg);
					}
				}
			}
			

			s_oReader.DeleteMessages(aoMessages);
		}
		catch(Exception oEx) {
			System.out.println("ReadSms Eccezione: " + oEx.toString());
		}
	}

	/**
	 * Writes received SMS in External Db as stated in settings
	 * @param oDbSetting External Db Setting
	 * @param oSmsMsg SMS to forward
	 */
	private static void SerializeReceivedSms(ExternalDbSetting oDbSetting, InboundMessage oSmsMsg ) {
		if (oDbSetting == null) {
			System.out.println("SerializeReceivedSms oDbSetting is null, return");
		}
		
		String sInsertQuery = "INSERT INTO " + oDbSetting.m_sSchema + "." + oDbSetting.m_sTableName;
		sInsertQuery += " (" + oDbSetting.m_sSenderColumn + ", " + oDbSetting.m_sTextColumn + ", " + oDbSetting.m_sDateColumn + ") VALUES (?,?,?)";
		
		try {
			 java.sql.Connection oDbConnection = DriverManager.getConnection(oDbSetting.m_sDbAddress, oDbSetting.m_sDbUser, oDbSetting.m_sDbPassword);
			 PreparedStatement oQuery = oDbConnection.prepareStatement(sInsertQuery);
			 String sNumber = "ND";
			 if (oSmsMsg.getOriginator() != null) {
				 if (oSmsMsg.getOriginator().equals("")==false) {
					 sNumber = oSmsMsg.getOriginator();
				 }
			 }
			 oQuery.setString(1, sNumber);
			 oQuery.setString(2, oSmsMsg.getText());
			 Timestamp oTimestamp = new Timestamp(oSmsMsg.getDate().getTime());
			 oQuery.setTimestamp(3, oTimestamp);
			 oQuery.executeUpdate();
			 
			 System.out.println("SMS Created in external database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	/*
	 * Initialize the Daemon Settings
	 */
	public static void Initialize(String sConfigFilePath) {
		
		s_aoExternalDbSettings.clear();

		File oXmlConfig = new File((String) sConfigFilePath);

		if (oXmlConfig.exists()) {

			try {
				SAXBuilder oBuilder = new SAXBuilder();
				Document oDocument = oBuilder.build(oXmlConfig);

				Element oRoot = oDocument.getRootElement();
				Element oMainConfigNode = oRoot.getChild("MAINCONFIG");

				s_iPollingTime = Integer.parseInt(oMainConfigNode.getChild("DAEMONPOLLINGTIME").getText());

				s_iRetry = Integer.parseInt(oMainConfigNode.getChild("RETRY").getText());
				
				m_sSMSUtilsClass = oMainConfigNode.getChild("SMSUTIL").getText();
				
				s_SmsMailReceiver = oMainConfigNode.getChild("SMSMAILRECEIVER").getText();
				
				s_SmsTestReceiver = oMainConfigNode.getChild("SMSTESTRECEIVER").getText();
				
				s_iEnableMail = Integer.parseInt(oMainConfigNode.getChild("ENABLEMAIL").getText());
				
				s_iEnableSms = Integer.parseInt(oMainConfigNode.getChild("ENABLESMS").getText());
				
				Element oOutputDatabases = oMainConfigNode.getChild("OUTPUTDATABASES");
				
				if (oOutputDatabases != null) {
					List<Element> aoOutputDbs = (List<Element>) oOutputDatabases.getChildren("OUTPUTDB");
					
					if (aoOutputDbs != null) {
						for (Element oOutputDbElement : aoOutputDbs) {
							ExternalDbSetting oSetting = new ExternalDbSetting();
							oSetting.m_sDbAddress = oOutputDbElement.getChild("DBADDRESS").getText();
							oSetting.m_sDbUser = oOutputDbElement.getChild("DBUSER").getText();
							oSetting.m_sDbPassword = oOutputDbElement.getChild("DBPASSWORD").getText();
							oSetting.m_sTableName = oOutputDbElement.getChild("TABLENAME").getText();
							oSetting.m_sTextColumn = oOutputDbElement.getChild("TEXTCOLUMN").getText();
							oSetting.m_sSenderColumn = oOutputDbElement.getChild("SENDERCOLUMN").getText();
							oSetting.m_sDateColumn  = oOutputDbElement.getChild("DATECOLUMN").getText();
							oSetting.m_sSchema = oOutputDbElement.getChild("DBSCHEMA").getText();
							
							s_aoExternalDbSettings.add(oSetting);
						}
					}
				}

			} catch (IOException oEx) {
				System.out.println("Mercurius Daemon.main: " + oEx.getMessage());
			} 
			catch (JDOMException oEx) {
				System.out.println("Mercurius Daemon.main: " + oEx.getMessage());
			} 
			catch (NumberFormatException oEx) {
				System.out.println("Mercurius Daemon.main: " + oEx.getMessage());
			} 
			catch (Throwable oEx) {
				System.out.println("Mercurius Daemon.main: " + oEx.getMessage());
			}
		}
	}

	private static void SendSms() {
		
		ForwardsRepository oForwardsRepository = new ForwardsRepository();
		
		List<Forward> aoSmsToSend = oForwardsRepository.GetSmsToSend();

		// Se ho le tracce
		if (aoSmsToSend != null) {
			
			System.out.println("SMS to send = " + aoSmsToSend.size());

			boolean bResult = false;
			
			try {
				bResult = MercuriusHAL.sendSMS(aoSmsToSend);
			} 
			catch (RemoteException e) {

				e.printStackTrace();
				System.out.println("Mercurius Daemon.SendSms: " + e.getMessage());

				return;
			} 
			catch (ServiceException e) {
				e.printStackTrace();
				System.out.println("Mercurius Daemon.SendSms: " + e.getMessage());

				return;
			}
			
			for (Forward oForward : aoSmsToSend) {
				oForwardsRepository.Update(oForward);
			}

		} else {
			System.out.println("Mercurius Daemon.SendSms: lista traccia == null. Controllare connessione a DB");

		}

	}



	
	/**
	 * Substitutes chars אטילעש with their plain version and
	 * removes '£$%& chars
	 * @param sReturn
	 * @return
	 */
	public static String sanitizeSms(String sReturn) {
		
		if(sReturn == null){
			return "";
		}
		String sSanitized;
		// remove accents
		sSanitized = sReturn.replace('א', 'a');
		sSanitized = sSanitized.replace('ט', 'e');
		sSanitized = sSanitized.replace('י', 'e');
		sSanitized = sSanitized.replace('ל', 'i');
		sSanitized = sSanitized.replace('ע', 'o');
		sSanitized = sSanitized.replace('ש', 'u');
		// remove extra chars
		sSanitized = sSanitized.replaceAll("['£$%&]", "");
		return sSanitized;
	}
	
	private static void DumpSmsMessage(InboundMessage oSmsMsg) {
		if (oSmsMsg == null) {
			return;
		}
		
		System.out.println("getDstPort " + oSmsMsg.getDstPort());
		System.out.println("getGatewayId " + oSmsMsg.getGatewayId());
		System.out.println("getId " + oSmsMsg.getId());
		System.out.println("getMemIndex " + oSmsMsg.getMemIndex());
		System.out.println("getMemLocation " + oSmsMsg.getMemLocation());
		System.out.println("MessageId " + oSmsMsg.getMessageId());
		System.out.println("getMpMaxNo " + oSmsMsg.getMpMaxNo());
		System.out.println("getMpMemIndex " + oSmsMsg.getMpMemIndex());
		System.out.println("getMpRefNo " + oSmsMsg.getMpRefNo());
		System.out.println("getMpSeqNo " + oSmsMsg.getMpSeqNo());
		System.out.println("getOriginator " + oSmsMsg.getOriginator());
		System.out.println("getPduUserData " + oSmsMsg.getPduUserData());
		System.out.println("getPduUserDataHeader " + oSmsMsg.getPduUserDataHeader());
		System.out.println("getSmscNumber " + oSmsMsg.getSmscNumber());
		System.out.println("getSrcPort " + oSmsMsg.getSrcPort());
		System.out.println("getText " + oSmsMsg.getText());
		System.out.println("getUuid " + oSmsMsg.getUuid());
		System.out.println("getDate " + oSmsMsg.getDate());
		System.out.println("getDCSMessageClass " + oSmsMsg.getDCSMessageClass().toString());
		System.out.println("toString " + oSmsMsg.toString());		
	}
}
