package it.fadeout.mercurius.business;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Mercurius Message, that can have many forwards.
 * @author p.campanella
 *
 */
@Entity
@Table(name="messages")
@XmlRootElement
public class Message {
	@Id
	@GeneratedValue
	@Column(name="idmessage")
	int IdMessage;
	
	@Column(name="title")
	String Tilte;
	
	@Column(name="message")
	String Message;
	
	@Column(name="sender")
	String Sender;
	
	@Column(name="creationdate")
	Date CreationDate;
	
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	@JoinColumn(name = "idmessage")
	Set<Forward> forwards = new HashSet<Forward>(0);	

	public int getIdMessage() {
		return IdMessage;
	}

	public void setIdMessage(int idMessage) {
		IdMessage = idMessage;
	}

	public String getTilte() {
		return Tilte;
	}

	public void setTilte(String tilte) {
		Tilte = tilte;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}

	public Set<Forward> getForwards() {
		return forwards;
	}

	public void setForwards(Set<Forward> forwards) {
		this.forwards = forwards;
	}
	
	public String getSender() {
		return Sender;
	}

	public void setSender(String sender) {
		Sender = sender;
	}
}
