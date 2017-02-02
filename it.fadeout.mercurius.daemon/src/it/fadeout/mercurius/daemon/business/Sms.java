package it.fadeout.mercurius.daemon.business;

import java.util.Date;

public class Sms {

	private String m_sMobileNumber;
	private Date m_dtDataOraInvio;
	private int m_iForwardId;
	private String m_sSMSMessage;

	public void setMobileNumber(String sMobileNumber) {
		this.m_sMobileNumber = sMobileNumber;
	}

	public String getMobileNumber() {
		return m_sMobileNumber;
	}

	public void setDataOraInvio(Date m_dtDataOraInvio) {
		this.m_dtDataOraInvio = m_dtDataOraInvio;
	}

	public Date getDataOraInvio() {
		return m_dtDataOraInvio;
	}

	public int getForwardId() {
		return m_iForwardId;
	}

	public void setForwardId(int iForwardId) {
		this.m_iForwardId = iForwardId;
	}

	public String getSMSMessage() {
		return m_sSMSMessage;
	}

	public void setSMSMessage(String sSMSMessage) {
		this.m_sSMSMessage = sSMSMessage;
	}
}
