package it.fadeout.mercurius.business;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Final message to send, specifying text media and address.
 * If the recipient is a Mercurius Contact also contactid is saved
 * @author p.campanella
 *
 */
@Entity
@Table(name="forwards")
@XmlRootElement
public class Forward {
	@Id
	@GeneratedValue
	@Column(name="idforward")
	int IdForward;
	
	@Column(name="idrecipient")
	int IdRecipient;
	
	@Column(name="media")
	String Media;
	
	@Column(name="finaltext")
	String FinalText;
	
	@Column(name="idmessage")
	int IdMessage;
	
	@Column(name="address")
	String Address;
	
	@Column(name="retrycount")
	int RetryCount;
	
	@Column(name="issent")
	boolean IsSent;
	
	@Column(name="scheduledon")
	Date ScheduledOn;
	
	@Column(name="lastsendon")
	Date LastSendOn;
	
	@Column(name="maxretry")
	int MaxRetry;

	public int getIdForward() {
		return IdForward;
	}

	public void setIdForward(int idForward) {
		IdForward = idForward;
	}

	public int getIdRecipient() {
		return IdRecipient;
	}

	public void setIdRecipient(int idRecipient) {
		IdRecipient = idRecipient;
	}

	public String getMedia() {
		return Media;
	}

	public void setMedia(String media) {
		Media = media;
	}

	public String getFinalText() {
		return FinalText;
	}

	public void setFinalText(String finalText) {
		FinalText = finalText;
	}

	public int getIdMessage() {
		return IdMessage;
	}

	public void setIdMessage(int idMessage) {
		IdMessage = idMessage;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public int getRetryCount() {
		return RetryCount;
	}

	public void setRetryCount(int retryCount) {
		RetryCount = retryCount;
	}

	public boolean isIsSent() {
		return IsSent;
	}

	public void setIsSent(boolean isSent) {
		IsSent = isSent;
	}

	public Date getScheduledOn() {
		return ScheduledOn;
	}

	public void setScheduledOn(Date scheduledOn) {
		ScheduledOn = scheduledOn;
	}

	public Date getLastSendOn() {
		return LastSendOn;
	}

	public void setLastSendOn(Date lastSendOn) {
		LastSendOn = lastSendOn;
	}

	public int getMaxRetry() {
		return MaxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		MaxRetry = maxRetry;
	}
}
