package it.fadeout.mercurius.daemon.EMailUtils;

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

	// /**
	// * Configurazione dell'email utils
	// */
	protected static EMailUtilsConfig s_oConfiguration = null;

	protected static org.apache.log4j.Logger getLogger() {
		return org.apache.log4j.Logger.getRootLogger();
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

		// // Controllo che ci sia la password
		// if (sPwd.equals("")) {
		// getLogger().error("SendMessage sPwd e' vuota, torno false");
		// return;
		// }

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
			// Creating a Mime message with given details.
			MimeMessage oMsg = createMessage(sUserName, sPwd, sFrom, asTo,
					sSubject, sBody, asAttachments);
			// oMsg.setFrom(new InternetAddress(sUserName));

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

	// /**
	// * Riceve la posta di un utente e la salva nella cartella opportuna
	// * @param sUserName
	// * @param sPwd
	// * @return
	// */
	// protected boolean Receive(String sUserName, String sPwd, String
	// sUserNameForPath, boolean bReadOnly,int iHasMaxSize,int iMaxSize) {
	//
	// // Apro la sessione Pop
	// Session oSmtpSession = getPopSession(sUserName, sPwd);
	//
	// try {
	//
	// Logger.Log("EMailUtils.Receive: leggo la posta dell'utente :" + sUserName
	// + " Usa Max Size = " + iHasMaxSize + " Valore Max Size = " + iMaxSize,
	// LogLevel.INFO);
	//
	// Store oStore = oSmtpSession.getStore("pop3");
	// oStore.connect();
	//
	// // Apro il folder InBox
	// Folder oFolder = oStore.getDefaultFolder();
	// oFolder = oFolder.getFolder("Inbox");
	//
	// if (bReadOnly){
	// // Lasciando i messaggi sul server
	// oFolder.open(Folder.READ_ONLY);
	// }
	// else{
	// // Non lasciando i messaggi sul server
	// oFolder.open(Folder.READ_WRITE);
	// }
	//
	// // Message oRead = oFolder.getMessage(1);
	// // String oFrom = oRead.getSubject();
	// // Logger.Log("Mail 1 subject: " + oFrom, LogLevel.ERROR);
	//
	// // Cerco le ultime mail
	// Message[] oUnread = oFolder.search(new FlagTerm(new
	// Flags(Flags.Flag.RECENT), false));
	// // Quanti sono?
	// int iMsgSize = oUnread.length;
	//
	// int iNewMessages = 0;
	//
	// // Per tutti i messaggi da leggere
	// for(int iMsgCount = 0; iMsgCount<iMsgSize;iMsgCount++){
	// // Saving on disk all downloaded messages in eml format.
	// //Logger.Log("EMailUtils.Receive: " + ((MimeMessage)
	// oUnread[iMsgCount]).toString(), LogLevel.ERROR);
	//
	// // Ottengo nome univoco del file, che poi è il suo msg Id
	// String sFileName =
	// GetMailFileName(((MimeMessage)oUnread[iMsgCount]).getMessageID());
	//
	// // Verifico che non sia già nelle mail inviate
	// File oSentFile = new
	// File(GetMailSentCompletePath(sUserNameForPath)+sFileName);
	// if (oSentFile.exists()) continue;
	//
	// // Verifico che non sia già nelle mail gestite
	// File oDoneFile = new
	// File(GetMailDoneCompletePath(sUserNameForPath)+sFileName);
	// if (oDoneFile.exists()) continue;
	//
	// // Preparo il path completo: {MailHome}\{userMail}\MailReceived
	// String sCompletePath = GetMailReceivedCompletePath(sUserNameForPath);
	// sCompletePath += sFileName;
	//
	// // Verifico che non sia già nelle mail ricevute
	// File oReceivedFile = new File(sCompletePath);
	// if (oReceivedFile.exists()) continue;
	//
	// iNewMessages++;
	//
	// // Creo il file
	// OutputStream oStream = new FileOutputStream(sCompletePath);
	//
	// // Lo scrivo
	// oUnread[iMsgCount].writeTo(oStream);
	//
	// // Eseguo il flush dello stream
	// oStream.flush();
	//
	// // Chiduo lo stream
	// oStream.close();
	//
	// // Controllo se ho la dimensoine massima degli allegati
	// if (iHasMaxSize == 1){
	// // La ho! Controllo la dimensione del file
	// if (oReceivedFile.length()> iMaxSize*1024){
	// // E' superiore: sposto la mail come "Done"
	// Logger.Log("EMailUtils.Receive: trovata mail che supera la dimensione stabilita, la sposto in Done",
	// LogLevel.WARNING);
	// sCompletePath = GetMailDoneCompletePath(sUserNameForPath);
	// sCompletePath += sFileName;
	// if (!oReceivedFile.renameTo(new File(sCompletePath))){
	// Logger.Log("Receive: impossibile spostare un file di dimensione superiore al limite stabilito in "
	// + sCompletePath, LogLevel.ERROR);
	// }
	//
	// }
	// }
	// }
	//
	// oFolder.close(false);
	// oStore.close();
	//
	// // Finito il ciclo
	// Logger.Log("Receive: unread Mails for user " + sUserName + " : " +
	// iNewMessages, LogLevel.ERROR);
	//
	// } catch (Exception oEx) {
	// Logger.Log("EMailUtils.Receive: " + oEx.getMessage(), LogLevel.ERROR);
	// return false;
	// }
	// return true;
	// }

	/**
	 * Builds a not shared SMTP session for sending emails.
	 * 
	 * @return the not shared SMTP session
	 */
	protected static Session getSmtpSession(final String sUserName,
			final String sPwd, boolean bUseAuthentication) {
		getLogger().error("EMailUtils.getSmtpSession: begin");
		// Properties for sessions can be read from resourse file.
		// Required data validation.
		if (s_oConfiguration.getSmtpServer() == null) {
			getLogger()
					.error("getSmtpSession: Smtp Server has not been set yet, exiting.");
			return null;
		}
		if (sUserName == null) {
			getLogger().error(
					"getSmtpSession: User name has not been set yet, exiting.");
			return null;
		}
		if (sPwd == null) {
			getLogger().error(
					"getSmtpSession: User pwd has not been set yet, exiting.");
			return null;
		}

		// Properties oSmtpProps = new Properties();
		// Getting system properties.
		Properties oSmtpProps = System.getProperties();
		oSmtpProps.setProperty("mail.smtp.host",
				s_oConfiguration.getSmtpServer());
		oSmtpProps.setProperty("mail.smtp.port",
				s_oConfiguration.getSmtpServerPort());

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
	protected static MimeMessage createMessage(String sUserName, String sPwd,
			String sFrom, String[] asTo, String sSubject, String sBody,
			String[] asAttachments) throws MessagingException {
		// Creating the MimeMessage.
		// create a smtp session with authentication.

		MimeMessage oMsg = createMessage(sUserName, sPwd,
				s_oConfiguration.isUseAuthentication());
		// Setting message fields.

			// From field.
			if (sFrom != null) {
				if (sFrom.equals("") == false) {
					try {
						oMsg.setFrom(new InternetAddress(sFrom));
					} catch (AddressException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						getLogger().error("EMailUtils.createMessage: " + e.getMessage());
						throw e;
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
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
								aoRecvAddresses[iRecvCounter] = new InternetAddress(
										asTo[iRecvCounter]);
							} catch (AddressException e) {
								// TODO Auto-generated catch block
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
					oMsg.setRecipients(Message.RecipientType.TO, aoRecvAddresses);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
						e.printStackTrace();
						getLogger().error("EMailUtils.createMessage: " + e.getMessage());
						throw e;
					}
				}
			} else {
				// Subject field can be omitted.
			}
			// Body.
			if (sBody != null) {
				if (sBody.equals("") == false) {
					Multipart oMimeBody = new MimeMultipart();
					MimeBodyPart oBodyPart = new MimeBodyPart();
					try {
						oBodyPart.setText(sBody);
						oMimeBody.addBodyPart(oBodyPart);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						getLogger().error("EMailUtils.createMessage: " + e.getMessage());
						throw e;
					}
					

					// Attachments.
					if (asAttachments != null) {
						for (String asAttachPath : asAttachments) {
							MimeBodyPart oAttachmentPart = new MimeBodyPart();

							DataSource oAttachmentSource = new FileDataSource(
									asAttachPath);

							try {
								oAttachmentPart
										.setDataHandler(new javax.activation.DataHandler(
												oAttachmentSource));
								oAttachmentPart.setFileName(oAttachmentSource
										.getName());
								// add the attachment to the whole message.
								oMimeBody.addBodyPart(oAttachmentPart);
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
						e.printStackTrace();
						getLogger().error("EMailUtils.createMessage: " + e.getMessage());
						throw e;
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
	protected static MimeMessage createMessage(String sUserName, String sPwd,
			boolean bUseAuthentication) {
		Session oSmtpSession = getSmtpSession(sUserName, sPwd,
				bUseAuthentication);
		MimeMessage oMsg = new MimeMessage(oSmtpSession);
		return oMsg;

	}

	public static boolean SendMessage(String[] asTo, String sSubject,
			String sBody, String[] asAttachments) throws MessagingException {
		// TODO Auto-generated method stub
		String sUserName = s_oConfiguration.getSmtpUser();
		String sPwd = s_oConfiguration.getSmtpPwd();
		String sFrom = s_oConfiguration.getSmtpSender();

		return SendMessage(sUserName, sPwd, sFrom, asTo, sSubject, sBody,
				asAttachments);
	}

}
