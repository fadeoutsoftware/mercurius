package it.fadeout.mercurius.daemon.EMailUtils;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Class for sending/receiving emails to/from SMTP/POP3 servers.
 * 
 * @author l.platania
 * 
 */
public class EMailUtils {

	public static boolean UseHtmlMail = true;

	// /**
	// * Configurazione dell'email utils
	// */
	protected static EMailUtilsConfig s_oConfiguration = null;

	protected static org.apache.log4j.Logger getLogger() {
		return org.apache.log4j.Logger.getRootLogger();
	}
	
	public static void SetSender(String sSender)
	{
		if (s_oConfiguration!=null)
		{
			s_oConfiguration.setSmtpSender(sSender);
		}
	}
	
	public static String getSender()
	{
		if (s_oConfiguration!=null)
		{
			return s_oConfiguration.getSmtpSender();
		}
		
		return null;
	}

	/**
	 * @return true if configuration object has been created, false otherwise.
	 */
	public static boolean isInitialized() {
		return s_oConfiguration != null;
	}

	public static void initialize(String sSmptAddress, String sSmtpPort,
			String sSmptUser, String sSmptPwd, String sSmtpSender) {

		if (s_oConfiguration == null) {
			s_oConfiguration = new EMailUtilsConfig();
		}
		s_oConfiguration.setSmtpServer(sSmptAddress);
		s_oConfiguration.setSmtpServerPort(sSmtpPort);
		s_oConfiguration.setSmtpUser(sSmptUser);
		s_oConfiguration.setSmtpPwd(sSmptPwd);
		s_oConfiguration.setSmtpSender(sSmtpSender);
	}

	public static void initialize(EMailUtilsConfig oConfig) {
		s_oConfiguration = oConfig;
	}

	/**
	 * Sends an Email message.
	 * 
	 * @param sUserName
	 *            The sender.
	 * @param sPwd
	 *            The sender smtp password.
	 * @param oMsg
	 *            the message to be sent.
	 * @param sFrom
	 *            the message sender.
	 * @param asTo
	 *            the message receivers.
	 * @param sSubject
	 *            the message subject.
	 * @param sBody
	 *            the message body.
	 * @throws MessagingException 
	 */
	public static boolean SendMessage(String sUserName, String sPwd,
			String sFrom, String[] asTo, String sSubject, String sBody,
			String[] asAttachments) throws MessagingException {

		boolean bReturn = false;

		// Controllo che ci sia lo user name
		if (sUserName == null) {

			getLogger().error("SendMessage User Name e' null, torno false");
			return bReturn;
		}

		// Controllo che ci sia la password
		if (sPwd == null) {
			getLogger().error("SendMessage sPwd e' null, torno false");
			return bReturn;
		}

		// Controllo che ci sia lo user name
		if (sUserName.equals("")) {
			getLogger().error("SendMessage User Name e' vuota, torno false");
			return bReturn;
		}

		if (sFrom == null) {
			getLogger().error("SendMessage sFrom e' null, torno false");
			return bReturn;

		}

		if (sFrom.equals("")) {
			getLogger().error("SendMessage sFrom e' null, torno false");
			return bReturn;
		}

		if (asTo == null) {
			getLogger().error("SendMessage asTo e' null, torno false");
			return bReturn;
		}

		try {

			boolean bBCC = false;
			if (asTo.length>1) bBCC = true;

			// Creating a Mime message with given details.
			MimeMessage oMsg = createMessage(sUserName, sPwd, sFrom, asTo, sSubject, sBody, asAttachments,bBCC);

			// Actual sending of the email message.
			if (oMsg != null) {
				Transport.send(oMsg);
			}

			bReturn = true;

		} catch (MessagingException oEx) {
			System.out.println("EMailUtils.SendMessage: " + oEx.getMessage()
					+ oEx.getStackTrace());
			getLogger().error(
					"EMailUtils.SendMessage: " + oEx.getMessage()
					+ oEx.getStackTrace());
			throw oEx;

		}

		return bReturn;

	}

	/**
	 * Builds a not shared SMTP session for sending emails.
	 * 
	 * @return the not shared SMTP session
	 */
	protected static Session getSmtpSession(final String sUserName, final String sPwd, boolean bUseAuthentication) {
		
		getLogger().error("EMailUtils.getSmtpSession: begin");
		// Properties for sessions can be read from resourse file.
		// Required data validation.
		if (s_oConfiguration.getSmtpServer() == null) {
			getLogger().error("getSmtpSession: Smtp Server has not been set yet, exiting.");
			return null;
		}
		if (sUserName == null) {
			getLogger().error("getSmtpSession: User name has not been set yet, exiting.");
			return null;
		}
		if (sPwd == null) {
			getLogger().error("getSmtpSession: User pwd has not been set yet, exiting.");
			return null;
		}

		// Properties oSmtpProps = new Properties();
		// Getting system properties.
		Properties oSmtpProps = System.getProperties();
		oSmtpProps.setProperty("mail.smtp.host", s_oConfiguration.getSmtpServer());
		oSmtpProps.setProperty("mail.smtp.port", s_oConfiguration.getSmtpServerPort());

		// Note: Other properties can be set.

		Session oSession = null;

		if (bUseAuthentication) {
			oSmtpProps.setProperty("mail.smtp.auth", "true");

			oSession = Session.getInstance(oSmtpProps, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// Getting a session for current properties and user.
					// Authentication credentials
					return new PasswordAuthentication(sUserName, sPwd);
				}
			});
		} else {
			oSmtpProps.setProperty("mail.smtp.auth", "false");
			oSession = Session.getInstance(oSmtpProps);
		}

		// oSession.setDebug(true);

		return oSession;
	}

	protected static MimeMessage createMessage(String sUserName, String sPwd, String sFrom, String[] asTo, String sSubject, String sBody, String[] asAttachments) throws MessagingException {
		return createMessage(sUserName, sPwd, sFrom, asTo, sSubject, sBody,asAttachments,false);
	}

	/**
	 * Method for creating a MimeMessage to be sent.
	 * 
	 * @param sFrom
	 *            the message sender.
	 * @param asTo
	 *            the message receivers.
	 * @param sSubject
	 *            the message subject.
	 * @param sBody
	 *            the message body.
	 * @return a MimeMessage object.
	 * @throws MessagingException 
	 */
	protected static MimeMessage createMessage(String sUserName, String sPwd, String sFrom, String[] asTo, String sSubject, String sBody, String[] asAttachments, boolean bRecipientsInCC) throws MessagingException {
		// Creating the MimeMessage.
		// create a smtp session with authentication.

		MimeMessage oMsg = createMessage(sUserName, sPwd, s_oConfiguration.isUseAuthentication());
		
		// Setting message fields.

		// From field.
		if (sFrom != null) {
			if (sFrom.equals("") == false) {
				try {
					oMsg.setFrom(new InternetAddress(sFrom));
				} catch (AddressException e) {
					e.printStackTrace();
					getLogger().error("EMailUtils.createMessage: " + e.getMessage());
					throw e;
				} catch (MessagingException e) {
					e.printStackTrace();
					getLogger().error("EMailUtils.createMessage: " + e.getMessage());
					throw e;
				}
			}
		} else {
			// From field can be omitted.
		}
		// To field.
		boolean bHasReceiver = false;
		
		if (asTo != null) {
			
			int iRecvSize = asTo.length;
			InternetAddress[] aoRecvAddresses = new InternetAddress[iRecvSize];
			
			for (int iRecvCounter = 0; iRecvCounter < iRecvSize; iRecvCounter++) {
				
				if (asTo[iRecvCounter] != null) {
					
					if (asTo[iRecvCounter].equals("") == false) {
						
						// The receiver must be not empty, otherwise skip
						// it.
						bHasReceiver = true;
						
						try {
							aoRecvAddresses[iRecvCounter] = new InternetAddress(asTo[iRecvCounter]);
						} catch (AddressException e) {
							e.printStackTrace();
							getLogger().error("EMailUtils.createMessage: " + e.getMessage());
							throw e;
						}
					}
				}
			}
			// aoRecvAddresses = new InternetAddress[]{new
			// InternetAddress("l.platania@fadeout.biz")};
			try {
				
				if (bRecipientsInCC==false)
				{
					oMsg.setRecipients(Message.RecipientType.TO, aoRecvAddresses);
				}
				else
				{
					oMsg.setRecipients(Message.RecipientType.BCC, aoRecvAddresses);
				}

			} 
			catch (MessagingException e) {
				e.printStackTrace();
				getLogger().error("EMailUtils.createMessage: " + e.getMessage());
				throw e;
			}
		}
		if (bHasReceiver == false) {
			// If the message has not a receiver, it is useless
			// to build it, just return an null message.
			return null;
		}

		// Email subject.
		if (sSubject != null) {
			
			if (sSubject.equals("") == false) {
				
				try {
					oMsg.setSubject(sSubject);
				} catch (MessagingException e) {
					e.printStackTrace();
					getLogger().error("EMailUtils.createMessage: " + e.getMessage());
					throw e;
				}
			}
		} 
		else {
			// Subject field can be omitted.
		}
		// Body.
		if (sBody != null) {
			
			if (sBody.equals("") == false) {
				
				Multipart oMimeBody = new MimeMultipart();
				MimeBodyPart oBodyPart = new MimeBodyPart();
				
				try {
					if (UseHtmlMail == false) {
						System.out.println("Sending Plain Text Mail");
						oBodyPart.setText(sBody);	
					}
					else {
						System.out.println("Sending Html Mail");
						oBodyPart.setContent(sBody, "text/html; charset=utf-8");
					}

					oMimeBody.addBodyPart(oBodyPart);
				} catch (MessagingException e) {
					e.printStackTrace();
					getLogger().error("EMailUtils.createMessage: " + e.getMessage());
					throw e;
				}


				// Attachments.
				if (asAttachments != null) {
					for (String asAttachPath : asAttachments) {
						MimeBodyPart oAttachmentPart = new MimeBodyPart();

						DataSource oAttachmentSource = new FileDataSource(asAttachPath);

						try {
							oAttachmentPart.setDataHandler(new javax.activation.DataHandler(oAttachmentSource));
							oAttachmentPart.setFileName(oAttachmentSource.getName());
							// add the attachment to the whole message.
							oMimeBody.addBodyPart(oAttachmentPart);
							
						} catch (MessagingException e) {
							e.printStackTrace();
							getLogger().error("EMailUtils.createMessage: " + e.getMessage());
							throw e;
						}

					}

				}
				// Adding the body to the message.
				try {
					oMsg.setContent(oMimeBody);
				} catch (MessagingException e) {
					e.printStackTrace();
					getLogger().error("EMailUtils.createMessage: " + e.getMessage());
					throw e;
				}

				// Sent Date
				try {
					oMsg.setSentDate(new Date());
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		} else {
			// Body field can be omitted.
		}
		//		} catch (Exception oEx) {
		//			getLogger().error("EMailUtils.createMessage: " + oEx.getMessage());
		//		}
		return oMsg;

	}

	/**
	 * Method for creating an empty (with just the session being set)
	 * MimeMessage to be sent.
	 * 
	 * @return a MimeMessage object.
	 */
	protected static MimeMessage createMessage(String sUserName, String sPwd,boolean bUseAuthentication) {
		Session oSmtpSession = getSmtpSession(sUserName, sPwd, bUseAuthentication);
		MimeMessage oMsg = new MimeMessage(oSmtpSession);

		return oMsg;

	}

	public static boolean SendMessage(String[] asTo, String sSubject, String sBody, String[] asAttachments) throws MessagingException {

		String sUserName = s_oConfiguration.getSmtpUser();
		String sPwd = s_oConfiguration.getSmtpPwd();
		String sFrom = s_oConfiguration.getSmtpSender();

		return SendMessage(sUserName, sPwd, sFrom, asTo, sSubject, sBody, asAttachments);
	}

}
