package it.fadeout.mercurius.daemon.EMailUtils;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class EMailUtilsConfig {

	protected static org.apache.log4j.Logger getLogger(){
		return org.apache.log4j.Logger.getRootLogger();
	}
	/**
	 * SMTP Server name. It is used for sending emails.
	 */
	private String m_sSmtpServer;

	private String m_sSmtpUser;
	
	private String m_sSmtpPwd;
	
	private String m_sSmtpSender;
	/**
	 * POP3 Server name. It is used for receiving emails.
	 */
	private String m_sPopServer;
	
	/**
	 * Flag to set if there is an SMTP Auth
	 */
	private boolean m_bUseAuthentication;

	/**
	 * Path locale del sito ftp
	 */
	private String m_sFtpMainPath;
	
	/**
	 * Path locale in cui si trovano le cartelle di posta
	 */
	private String m_sMailHomePath;
	
	/**
	 * Stringa di connessione Al Db
	 */
	private String m_sDbConnectionString;
	
	/**
	 * Nome della cartella "Ricevute"
	 */
	private String m_sMailReceivedFolder = "MailReceived";
	
	/**
	 * Nome della cartella "Inviate"
	 */
	private String m_sMailSentFolder = "MailSent";
	
	/**
	 * Nome della cartella "Inviate" e "Lette"
	 */
	private String m_sMailDoneFolder = "MailDone";	

	/**
	 * Nome della cartella "Temp"
	 */
	private String m_sUserTempFolder = "Temp";
	
	/**
	 * Nome della cartella per la configurazione delle navi
	 */
	private String m_sShipConfigFolder = "Config";	
	
	/**
	 * Smtp server port
	 */
	private String m_sSmtpServerPort = "25";
	
	/**
	 * Flag per sapere se usare il nome utente con dominio o senza
	 */
	private boolean m_bUseUserNameWithoutDomain = false;
	
	
	private String m_sConnectionString;

	private String m_sConnectionUser;

	private String m_sConnectionPwd;

	/**
	 * Sets the pop server
	 * @param m_sPopServer
	 */
	public synchronized void setPopServer(String sPopServer) {
		m_sPopServer = sPopServer;
	}

	/**
	 * gets the Pop Server
	 * @return
	 */
	public synchronized String getPopServer() {
		return m_sPopServer;
	}

	/**
	 * Sets the SMTP Server
	 * @param m_sSmtpServer
	 */
	public synchronized  void setSmtpServer(String sSmtpServer) {
		m_sSmtpServer = sSmtpServer;
	}

	/**
	 * Gets the SMTP Server
	 * @return
	 */
	public synchronized String getSmtpServer() {
		return m_sSmtpServer;
	}
	
	/**
	 * Gets the ftp server local Path
	 * @return
	 */
	public synchronized String getFtpMainPath() {
		return m_sFtpMainPath;
	}

	/**
	 * Sets the ftp server local path
	 * @param sSFtpMainPath
	 */
	public synchronized void setFtpMainPath(String sSFtpMainPath) {
		m_sFtpMainPath = sSFtpMainPath;
	}

	/**
	 * Gets the MailBox local path
	 * @return
	 */
	public synchronized String getMailHomePath() {
		return m_sMailHomePath;
	}

	/**
	 * Sets the MailBox local Path
	 * @param sSMailHomePath
	 */
	public synchronized void setMailHomePath(String sSMailHomePath) {
		m_sMailHomePath = sSMailHomePath;
	}

	/**
	 * Gets the Db Connection String
	 * @return
	 */
	public synchronized String getDbConnectionString() {
		return m_sDbConnectionString;
	}

	/**
	 * Sets the Db Connection String
	 * @param sSDbConnectionString
	 */
	public synchronized void setDbConnectionString(String sSDbConnectionString) {
		m_sDbConnectionString = sSDbConnectionString;
	}
	

	/**
	 * gets mail received folder name
	 * @return
	 */
	public synchronized String getMailReceivedFolder() {
		return m_sMailReceivedFolder;
	}

	/**
	 * sets mail received folder name
	 * @param sSMailReceivedFolder folder name
	 */
	public synchronized void setMailReceivedFolder(String sSMailReceivedFolder) {
		m_sMailReceivedFolder = sSMailReceivedFolder;
	}

	/**
	 * gets mail sent folder name
	 * @return
	 */
	public synchronized String getMailSentFolder() {
		return m_sMailSentFolder;
	}

	/**
	 * sets mail sent folder name
	 * @param sSMailSentFolder folder name
	 */
	public synchronized void setMailSentFolder(String sSMailSentFolder) {
		m_sMailSentFolder = sSMailSentFolder;
	}
	
	/**
	 * get temp folder name
	 * @return
	 */
	public synchronized String getUserTempFolder() {
		return m_sUserTempFolder;
	}

	/**
	 * set temp folder name
	 * @param sSUserTempFolder
	 */
	public synchronized void setUserTempFolder(String sSUserTempFolder) {
		m_sUserTempFolder = sSUserTempFolder;
	}
	
	/**
	 * Get if the SMTP servers uses authentication
	 * @return
	 */
	public synchronized boolean isUseAuthentication() {
		return m_bUseAuthentication;
	}
	
	/**
	 * Set if the SMTP servers uses authentication
	 * @param mBUseAuthentication
	 */
	public synchronized void setUseAuthentication(boolean mBUseAuthentication) {
		m_bUseAuthentication = mBUseAuthentication;
	}

	/**
	 * gets smtp server port
	 * @return
	 */
	public synchronized String getSmtpServerPort() {
		return m_sSmtpServerPort;
	}

	/**
	 * sets smtp server port
	 * @param iSmtpServerPort
	 */
	public synchronized void setSmtpServerPort(String sSmtpServerPort) {
		m_sSmtpServerPort = sSmtpServerPort;
	}	

	/**
	 * Gets if to use user name without domain
	 * @return true if uses user name without domain
	 */
	public synchronized boolean isUseUserNameWithoutDomain() {
		return m_bUseUserNameWithoutDomain;
	}

	/**
	 * Sets if to use user name without domain
	 * @param mBUseUserNameWithoutDomain
	 */
	public synchronized void setUseUserNameWithoutDomain( boolean bUseUserNameWithoutDomain) {
		m_bUseUserNameWithoutDomain = bUseUserNameWithoutDomain;
	}
	
	/**
	 * Gets the Mail Done Folder
	 * @return
	 */
	public synchronized String getMailDoneFolder() {
		return m_sMailDoneFolder;
	}

	/**
	 * Sets the Mail Done Folder
	 * @param sMailDoneFolder
	 */
	public synchronized void setMailDoneFolder(String sMailDoneFolder) {
		m_sMailDoneFolder = sMailDoneFolder;
	}
	
	/**
	 * Gets the Mail Done Folder
	 * @return
	 */
	public synchronized String getShipConfigFolder() {
		return m_sShipConfigFolder;
	}

	/**
	 * Sets the Mail Done Folder
	 * @param sMailDoneFolder
	 */
	public synchronized void setShipConfigFolder(String sShipConfigFolder) {
		m_sShipConfigFolder = sShipConfigFolder;
	}
	
	
	public boolean initEMailConfigByXmlFile(String sFilePath) {
		File oXmlConfig = new File(sFilePath);
		
		if(oXmlConfig.exists()) {
		
			try {
				SAXBuilder oBuilder = new SAXBuilder();
				Document oDocument = oBuilder.build(oXmlConfig);
				
				Element oRoot = oDocument.getRootElement();
				Element oMailNode = oRoot.getChild("MAILCONFIG");
				
				m_sSmtpServer = oMailNode.getChild("SMTP").getText();
				m_sSmtpServerPort = oMailNode.getChild("SMTPPORT").getText();
				m_sSmtpSender = oMailNode.getChild("SMTPSENDER").getText();
				m_sSmtpUser = oMailNode.getChild("SMTPSENDER").getText();
				m_sSmtpPwd = oMailNode.getChild("SMTPPWD").getText();
				//m_sPopServer = oMailNode.getChild("POP").getText();
				m_bUseAuthentication = Boolean.parseBoolean(oMailNode.getChild("AUTHETICATION").getText());
				//m_sFtpMainPath = oMailNode.getChild("FTPPATH").getText();
				//m_sMailHomePath = oMailNode.getChild("MAILHOMEPATH").getText();
				//m_sMailReceivedFolder = oMailNode.getChild("MAILSRECEIVEDPATH").getText();
				//m_sMailSentFolder = oMailNode.getChild("MAILSSENTPATH").getText();
				//m_sMailDoneFolder = oMailNode.getChild("MAILSDONEPATH").getText();
				//m_sUserTempFolder = oMailNode.getChild("MAILSTEMPPATH").getText();
				//m_bUseUserNameWithoutDomain = Boolean.parseBoolean(oMailNode.getChild("USERNAMEWITHOUTDOMAIN").getText());
				
				Element oMainNode = oRoot.getChild("MAINCONFIG");
				
				return true;
			} catch (IOException oEx) {
				getLogger().error("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;
			} catch (JDOMException oEx) {
				getLogger().error("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;				
			} catch (NumberFormatException oEx) {
				getLogger().error("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;	
			} catch (Throwable oEx) {
				getLogger().error("EMailUtilsConfig.initEMailConfigByXmlFile: " + oEx.getMessage());
				return false;	
			}
		}
		
		return false;
	}

//	/**
//	 * @param m_sConnectionString the m_sConnectionString to set
//	 */
//	public void setConnectionString(String m_sConnectionString) {
//		this.m_sConnectionString = m_sConnectionString;
//	}

	/**
	 * @return the m_sConnectionString
	 */
	public String getConnectionString() {
		return m_sConnectionString;
	}

	/**
//	 * @param m_sConnectionUser the m_sConnectionUser to set
//	 */
//	public void setConnectionUser(String m_sConnectionUser) {
//		this.m_sConnectionUser = m_sConnectionUser;
//	}

	/**
	 * @return the m_sConnectionUser
	 */
	public String getConnectionUser() {
		return m_sConnectionUser;
	}

//	/**
//	 * @param m_sConnectionPwd the m_sConnectionPwd to set
//	 */
//	public void setConnectionPwd(String m_sConnectionPwd) {
//		this.m_sConnectionPwd = m_sConnectionPwd;
//	}

	/**
	 * @return the m_sConnectionPwd
	 */
	public String getConnectionPwd() {
		return m_sConnectionPwd;
	}

	/**
	 * @param smtpUser the smtpUser to set
	 */
	public void setSmtpUser(String smtpUser) {
		m_sSmtpUser = smtpUser;
	}

	/**
	 * @return the smtpUser
	 */
	public String getSmtpUser() {
		return m_sSmtpUser;
	}

	/**
	 * @param smtpPwd the smtpPwd to set
	 */
	public void setSmtpPwd(String smtpPwd) {
		m_sSmtpPwd = smtpPwd;
	}

	/**
	 * @return the smtpPwd
	 */
	public String getSmtpPwd() {
		return m_sSmtpPwd;
	}

	/**
	 * @param smtpSender the smtpSender to set
	 */
	public void setSmtpSender(String smtpSender) {
		m_sSmtpSender = smtpSender;
	}

	/**
	 * @return the smtpSender
	 */
	public String getSmtpSender() {
		return m_sSmtpSender;
	}
}
