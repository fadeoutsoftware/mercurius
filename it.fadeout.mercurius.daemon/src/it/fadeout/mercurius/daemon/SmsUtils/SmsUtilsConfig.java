package it.fadeout.mercurius.daemon.SmsUtils;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class SmsUtilsConfig {

	protected static org.apache.log4j.Logger getLogger() {
		return org.apache.log4j.Logger.getRootLogger();
	}

	private String m_sUserName;

	private String m_sOldPwd;

	private String m_sPwd;

	private String m_sCentroCosto;

	public void setUserName(String m_sUserName) {
		this.m_sUserName = m_sUserName;
	}

	public String getUserName() {
		return m_sUserName;
	}

	public void setOldPwd(String m_sOldPwd) {
		this.m_sOldPwd = m_sOldPwd;
	}

	public String getOldPwd() {
		return m_sOldPwd;
	}

	public void setPwd(String m_sPwd) {
		this.m_sPwd = m_sPwd;
	}

	public String getPwd() {
		return m_sPwd;
	}

	public void setCentroCosto(String m_sCentroCosto) {
		this.m_sCentroCosto = m_sCentroCosto;
	}

	public String getCentroCosto() {
		return m_sCentroCosto;
	}

	public boolean initSmsConfigByXmlFile(String sFilePath) {
		File oXmlConfig = new File(sFilePath);

		if (oXmlConfig.exists()) {

			try {
				SAXBuilder oBuilder = new SAXBuilder();
				Document oDocument = oBuilder.build(oXmlConfig);

				Element oRoot = oDocument.getRootElement();
				Element oMailNode = oRoot.getChild("SMSCONFIG");

				m_sUserName = oMailNode.getChild("USERNAME").getText();
				m_sOldPwd = oMailNode.getChild("OLDPASSWORD").getText();
				m_sPwd = oMailNode.getChild("PASSWORD").getText();
				m_sCentroCosto = oMailNode.getChild("CENTRODICOSTO").getText();

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

}
