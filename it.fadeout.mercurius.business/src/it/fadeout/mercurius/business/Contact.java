package it.fadeout.mercurius.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Mercurius Contact Entity: Represents a single contact
 * @author p.campanella
 *
 */
@Entity
@Table(name = "contacts")
@XmlRootElement
public class Contact {
	@Id
	@GeneratedValue
	@Column(name="idcontact")
	int IdContact;
	
	@Column(name="surname")
	String Surname;
	
	@Column(name="name")
	String Name;
	
	@Column(name="ishuman")
	Boolean IsHuman;
	
	@Column(name="fixedphone")
	String FixedPhone;
	
	@Column(name="fax")
	String Fax;
	
	@Column(name="email")
	String EMail;
	
	@Column(name="mobile")
	String Mobile;
	
	@Column(name="twitter")
	String Twitter;
	
	@Column(name="facebook")
	String Facebook;
	//String Position;
	
	
	public int getIdContact() {
		return IdContact;
	}
	public void setIdContact(int idContact) {
		IdContact = idContact;
	}
	public String getSurname() {
		return Surname;
	}
	public void setSurname(String surname) {
		Surname = surname;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Boolean isIsHuman() {
		return IsHuman;
	}
	public void setIsHuman(Boolean isHuman) {
		IsHuman = isHuman;
	}
	public String getFixedPhone() {
		return FixedPhone;
	}
	public void setFixedPhone(String fixedPhone) {
		FixedPhone = fixedPhone;
	}
	public String getFax() {
		return Fax;
	}
	public void setFax(String fax) {
		Fax = fax;
	}
	public String getEMail() {
		return EMail;
	}
	public void setEMail(String eMail) {
		EMail = eMail;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getTwitter() {
		return Twitter;
	}
	public void setTwitter(String twitter) {
		Twitter = twitter;
	}
	public String getFacebook() {
		return Facebook;
	}
	public void setFacebook(String facebook) {
		Facebook = facebook;
	}
	
}
