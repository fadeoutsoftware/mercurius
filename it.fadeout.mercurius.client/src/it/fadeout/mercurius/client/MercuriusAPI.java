package it.fadeout.mercurius.client;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.business.Group;
import it.fadeout.mercurius.business.Message;
import it.fadeout.mercurius.business.PrimitiveResult;

import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

/**
 * API for RESTFul Mercurius Services
 * @author p.campanella
 *
 */
public class MercuriusAPI {
	String m_sBaseServerPath;
	Client m_oClient;
	WebTarget m_oTarget;
	
	/**
	 * Creates API Helper 
	 * @param sServerPath Main server Path (sample www.mercurius.org/it.fadeout.mercurius.webapi )
	 */
	public MercuriusAPI(String sServerPath) {
		m_sBaseServerPath = sServerPath;
		
	    ClientConfig config = new ClientConfig();
//	    config.property(ClientProperties.PROXY_URI, "127.0.0.1:8889");
//	    config.property(ClientProperties.PROXY_USERNAME, "");
//	    config.property(ClientProperties.PROXY_PASSWORD, "");
	    
	    m_oClient = ClientBuilder.newClient(config);
	    m_oTarget = m_oClient.target( UriBuilder.fromUri(m_sBaseServerPath).build());
	}
	

	/**
	 * Gets a Contact 
	 * @param iContactId Id of the contact
	 * @return Contact Instance
	 */
	public Contact getContact(int iContactId) {
		// Get the path
		String sPath = "contacts/" + iContactId;
		try {
			// call the server
			Contact oContact = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).get().readEntity(Contact.class);
			// return entity
			return oContact;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the Contacts list
	 * @return Contacts List
	 */
	public List<Contact> getAllContacts() {
		// All path
		String sPath = "contacts/all";
		try {
			// Get the list
			List<Contact> aoContacts = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).get().readEntity(new GenericType<List<Contact>>(){});
			// return to the user
			return aoContacts;
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Create a Contact
	 * @param oContact Contact Entity
	 * @return Contact Id
	 */
	public int createContact(Contact oContact) {
		// Is a valid entity?
		if (oContact == null) return -1;
		
		try {
			// Serialize 
		    Entity<Contact> oEntity = Entity.entity(oContact, "text/xml");
		    // call the server
		    PrimitiveResult oResult = m_oTarget.path("rest").path("contacts").request(MediaType.TEXT_XML).put(oEntity).readEntity(PrimitiveResult.class);
		    // return int value as Contact Id
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}
	}
	

	/**
	 * Deletes a Contact
	 * @param iIdContact Id of the contact to delete
	 * @return True if successful, False otherwise 
	 */
	public boolean deleteContact(int iIdContact) {
		
		try {
			// Get the user path
			String sPath ="contacts/" + iIdContact;
			// call the server
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).delete().readEntity(PrimitiveResult.class);
		    // Return Bool result
		    return oResult.BoolValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return false;
		}		
	}
	
	/**
	 * Updates a Contact
	 * @param oContact Contact Entity to Update
	 * @return True if successful, False otherwise
	 */
	public boolean updateContact(Contact oContact) {
		if (oContact == null) return false;
		
		try {
			// Serialize entity
		    Entity<Contact> oEntity = Entity.entity(oContact, "text/xml");
		    // call the server
		    PrimitiveResult oResult = m_oTarget.path("rest").path("contacts").request(MediaType.TEXT_XML).post(oEntity).readEntity(PrimitiveResult.class);
		    // return bool result
		    return oResult.BoolValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return false;
		}		
	}
	
	


	/**
	 * Gets a group from the server
	 * @param iGroupId Group Id
	 * @return Group entity
	 */
	public Group getGroup(int iGroupId) {
		// get the path
		String sPath = "groups/" + iGroupId;
		try {
			// call the server
			Group oGroup = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).get().readEntity(Group.class);
			// return entity
			return oGroup;
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the Groups list
	 * @return Groups List
	 */
	public List<Group> getAllGroups() {
		// Get the path
		String sPath = "groups/all";
		try {
			// call the server
			List<Group> aoGroups = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).get().readEntity(new GenericType<List<Group>>(){});
			// return the list
			return aoGroups;
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Create a Group
	 * @param oGroup Group to create
	 * @return Group Id or -1
	 */
	public int createGroup(Group oGroup) {
		// Is there a valid entity?
		if (oGroup == null) return -1;
		
		try {
			// Serialize entity
		    Entity<Group> oEntity = Entity.entity(oGroup, "text/xml");
		    // Call the server
		    PrimitiveResult oResult = m_oTarget.path("rest").path("groups").request(MediaType.TEXT_XML).put(oEntity).readEntity(PrimitiveResult.class);
		    // return group id
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}
	}
	

	/**
	 * Deletes a group
	 * @param iIdGroup Id of the group to delete
	 * @return True if successful, False otherwise
	 */
	public boolean deleteGroup(int iIdGroup) {
		
		try {
			// create the path
			String sPath ="groups/" + iIdGroup;
			// call the server
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).delete().readEntity(PrimitiveResult.class);
		    // return bool result
		    return oResult.BoolValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return false;
		}		
	}
	
	/**
	 * Updates a group
	 * @param oGroup Group Entity to delete
	 * @return True if successful, False otherwise
	 */
	public boolean updateGroup(Group oGroup) {
		// Check if is a valid group
		if (oGroup == null) return false;
		
		try {
			// Serialize entity
		    Entity<Group> oEntity = Entity.entity(oGroup, "text/xml");
		    // Call the server
		    PrimitiveResult oResult = m_oTarget.path("rest").path("groups").request(MediaType.TEXT_XML).post(oEntity).readEntity(PrimitiveResult.class);
		    // return bool result
		    return oResult.BoolValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return false;
		}		
	}
	
	/**
	 * Sends an SMS to a group
	 * @param iGroupId Group Id
	 * @param sMessage Message Text
	 * @return Generated Message Id for tracing, -1 otherwise 
	 */
	public int sendSmsToGroup(int iGroupId, String sMessage)  {
		return sendSmsToGroup(iGroupId, sMessage, "");
	}
	
	/**
	 * Sends an SMS to a group
	 * @param iGroupId Group Id
	 * @param sMessage Message Text
	 * @param sTitle Message Title: if present the sms will be Title - Message
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToGroup(int iGroupId, String sMessage, String sTitle)  {
		Message oMessage = new Message();
		oMessage.setCreationDate(new Date());
		oMessage.setMessage(sMessage);
		oMessage.setTilte(sTitle);
		
		return sendSmsToGroup(iGroupId, oMessage);
	}
	
	/**
	 * Sends an SMS to a group
	 * @param iGroupId Group Id
	 * @param oMessage Message Entity: please let Message Id and Forwards empty
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToGroup(int iGroupId, Message oMessage)  {
		if (oMessage == null) return -1;
		
		try {
		    Entity<Message> oEntity = Entity.entity(oMessage, "text/xml");
		    String sPath = "sms/group/" + iGroupId;
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).put(oEntity).readEntity(PrimitiveResult.class);
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Sends Sms to a set of contacts
	 * @param sContacts String ";" separated with contacts id (ie "9;18;93")
	 * @param sMessage Message to send
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToContacts(String sContacts, String sMessage) {
		return sendSmsToContacts(sContacts, sMessage,"");
	}
	
	/**
	 * Sends Sms to a set of contacts
	 * @param sContacts String ";" separated with contacts id (ie "9;18;93")
	 * @param sMessage Message to send
	 * @param sTitle sTitle Message Title: if present the sms will be Title - Message
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToContacts(String sContacts, String sMessage, String sTitle) {
		Message oMessage = new Message();
		oMessage.setCreationDate(new Date());
		oMessage.setMessage(sMessage);
		oMessage.setTilte(sTitle);
		return sendSmsToContacts(sContacts, oMessage);
		
	}
	
	/**
	 * Sends Sms to a contact
	 * @param oContact Contact Entity
	 * @param sMessage Message Entity
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToContacts(Contact oContact, String sMessage) {
		if (oContact == null) return -1;
		String sContacts = "" + oContact.getIdContact();
		return sendSmsToContacts(sContacts, sMessage);
	}
	
	/**
	 * Sends Sms to a contact
	 * @param oContact Contact Entity
	 * @param oMessage Message Entity
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToContacts(Contact oContact, Message oMessage) {
		if (oContact == null) return -1;
		String sContacts = "" + oContact.getIdContact();
		return sendSmsToContacts(sContacts, oMessage);
	}

	/**
	 * Sends Sms to a set of Contacts
	 * @param aoContacts List of Contacts
	 * @param sMessage Message Text
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToContacts(List<Contact> aoContacts, String sMessage) {
		if (aoContacts == null) return -1;
		if (aoContacts.size() == 0) return -1;
		String sContacts = "";
		
		for (Contact oContact : aoContacts) {
			sContacts += oContact.getIdContact() + ";";
		}
		
		sContacts = sContacts.substring(0, sContacts.length()-1);
		
		return sendSmsToContacts(sContacts, sMessage);
	}	
	
	/**
	 * Sends Sms to a set of contacts
	 * @param aoContacts List of Contacts
	 * @param oMessage Message Entity
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsToContacts(List<Contact> aoContacts, Message oMessage) {
		if (aoContacts == null) return -1;
		if (aoContacts.size() == 0) return -1;
		String sContacts = "";
		
		for (Contact oContact : aoContacts) {
			sContacts += oContact.getIdContact() + ";";
		}
		
		sContacts = sContacts.substring(0, sContacts.length()-1);
		
		return sendSmsToContacts(sContacts, oMessage);
	}
	
	public int sendSmsToContacts(String sContacts, Message oMessage) {
		if (oMessage == null) return -1;
		
		try {
		    Entity<Message> oEntity = Entity.entity(oMessage, "application/json");
		    String sPath = "sms/contacts";
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).queryParam("contactsids", sContacts).request(MediaType.APPLICATION_JSON).put(oEntity).readEntity(PrimitiveResult.class);
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}			
	}	

	/**
	 * Sends Sms to a set of direct numbers 
	 * @param sAddresses String ";" separated containing destination numbers (ie "+39333111222;+44123456789")
	 * @param sMessage Message to send
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsDirect(String sAddresses, String sMessage) {
		return sendSmsDirect(sAddresses, sMessage, "");
	}

	/**
	 * Sends Sms to a set of direct numbers 
	 * @param sAddresses String ";" separated containing destination numbers (ie "+39333111222;+44123456789")
	 * @param sMessage Message to send
	 * @param sTitle Title of the message (if set sms will be Title - Message)
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsDirect(String sAddresses, String sMessage, String sTitle) {
		Message oMessage = new Message();
		oMessage.setCreationDate(new Date());
		oMessage.setMessage(sMessage);
		oMessage.setTilte(sTitle);
		return sendSmsDirect(sAddresses, oMessage);
	}
	
	/**
	 * Sends Sms to a set of direct numbers
	 * @param sAddresses String ";" separated containing destination numbers (ie "+39333111222;+44123456789")
	 * @param oMessage Message to send
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendSmsDirect(String sAddresses, Message oMessage) {
		if (oMessage == null) return -1;
		
		try {
		    Entity<Message> oEntity = Entity.entity(oMessage, "text/xml");
		    String sPath = "sms/direct";
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).queryParam("numbers", sAddresses).request(MediaType.TEXT_XML).put(oEntity).readEntity(PrimitiveResult.class);
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}		
	}
		
	/**
	 * Sends a Mail to a group
	 * @param iGroupId Group Id
	 * @param sMessage Message Text
	 * @return Generated Message Id for tracing, -1 otherwise 
	 */
	public int sendMailToGroup(int iGroupId, String sMessage)  {
		return sendMailToGroup(iGroupId, sMessage, "");
	}
	
	/**
	 * Sends a Mail to a group
	 * @param iGroupId Group Id
	 * @param sMessage Message Text
	 * @param sTitle Message Subject
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToGroup(int iGroupId, String sMessage, String sTitle)  {
		Message oMessage = new Message();
		oMessage.setCreationDate(new Date());
		oMessage.setMessage(sMessage);
		oMessage.setTilte(sTitle);
		
		return sendMailToGroup(iGroupId, oMessage);
	}
	
	/**
	 * Sends a Mail to a group
	 * @param iGroupId Group Id
	 * @param oMessage Message Entity: please let Message Id and Forwards empty
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToGroup(int iGroupId, Message oMessage)  {
		if (oMessage == null) return -1;
		
		try {
		    Entity<Message> oEntity = Entity.entity(oMessage, "text/xml");
		    String sPath = "mail/group/" + iGroupId;
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).request(MediaType.TEXT_XML).put(oEntity).readEntity(PrimitiveResult.class);
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Sends Mail to a set of contacts
	 * @param sContacts String ";" separated with contacts id (ie "9;18;93")
	 * @param sMessage Message to send
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToContacts(String sContacts, String sMessage) {
		return sendMailToContacts(sContacts, sMessage,"");
	}
	
	/**
	 * Sends Mail to a set of contacts
	 * @param sContacts String ";" separated with contacts id (ie "9;18;93")
	 * @param sMessage Message to send
	 * @param sTitle Message Subject
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToContacts(String sContacts, String sMessage, String sTitle) {
		Message oMessage = new Message();
		oMessage.setCreationDate(new Date());
		oMessage.setMessage(sMessage);
		oMessage.setTilte(sTitle);
		return sendMailToContacts(sContacts, oMessage);
		
	}
	
	/**
	 * Sends Mail to a contact
	 * @param oContact Contact Entity
	 * @param sMessage Message Entity
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToContacts(Contact oContact, String sMessage) {
		if (oContact == null) return -1;
		String sContacts = "" + oContact.getIdContact();
		return sendMailToContacts(sContacts, sMessage);
	}
	
	/**
	 * Sends Mail to a contact
	 * @param oContact Contact Entity
	 * @param oMessage Message Entity
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToContacts(Contact oContact, Message oMessage) {
		if (oContact == null) return -1;
		String sContacts = "" + oContact.getIdContact();
		return sendMailToContacts(sContacts, oMessage);
	}

	/**
	 * Sends Mail to a set of Contacts
	 * @param aoContacts List of Contacts
	 * @param sMessage Message Text
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToContacts(List<Contact> aoContacts, String sMessage) {
		if (aoContacts == null) return -1;
		if (aoContacts.size() == 0) return -1;
		String sContacts = "";
		
		for (Contact oContact : aoContacts) {
			sContacts += oContact.getIdContact() + ";";
		}
		
		sContacts = sContacts.substring(0, sContacts.length()-1);
		
		return sendMailToContacts(sContacts, sMessage);
	}	
	
	/**
	 * Sends Mail to a set of contacts
	 * @param aoContacts List of Contacts
	 * @param oMessage Message Entity
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailToContacts(List<Contact> aoContacts, Message oMessage) {
		if (aoContacts == null) return -1;
		if (aoContacts.size() == 0) return -1;
		String sContacts = "";
		
		for (Contact oContact : aoContacts) {
			sContacts += oContact.getIdContact() + ";";
		}
		
		sContacts = sContacts.substring(0, sContacts.length()-1);
		
		return sendMailToContacts(sContacts, oMessage);
	}
	
	public int sendMailToContacts(String sContacts, Message oMessage) {
		if (oMessage == null) return -1;
		
		try {
		    Entity<Message> oEntity = Entity.entity(oMessage, "text/xml");
		    String sPath = "mail/contacts";
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).queryParam("contactsids", sContacts).request(MediaType.TEXT_XML).put(oEntity).readEntity(PrimitiveResult.class);
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}			
	}	

	/**
	 * Sends Mail to a set of direct numbers 
	 * @param sAddresses String ";" separated containing destination addresses (ie "test@mail.com;user@gmail.com")
	 * @param sMessage Message to send
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailDirect(String sAddresses, String sMessage) {
		return sendMailDirect(sAddresses, sMessage, "");
	}

	/**
	 * Sends Mail to a set of direct numbers 
	 * @param sAddresses String ";" separated containing destination  addresses (ie "test@mail.com;user@gmail.com")
	 * @param sMessage Message to send
	 * @param sTitle Title of the message (if set sms will be Title - Message)
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailDirect(String sAddresses, String sMessage, String sTitle) {
		Message oMessage = new Message();
		oMessage.setCreationDate(new Date());
		oMessage.setMessage(sMessage);
		oMessage.setTilte(sTitle);
		return sendMailDirect(sAddresses, oMessage);
	}
	
	/**
	 * Sends Mail to a set of direct numbers
	 * @param sAddresses String ";" separated containing destination  addresses (ie "test@mail.com;user@gmail.com")
	 * @param oMessage Message to send
	 * @return Generated Message Id for tracing, -1 otherwise
	 */
	public int sendMailDirect(String sAddresses, Message oMessage) {
		if (oMessage == null) return -1;
		
		try {
		    Entity<Message> oEntity = Entity.entity(oMessage, "text/xml");
		    String sPath = "mail/direct";
		    PrimitiveResult oResult = m_oTarget.path("rest").path(sPath).queryParam("addresses", sAddresses).request(MediaType.TEXT_XML).put(oEntity).readEntity(PrimitiveResult.class);
		    return oResult.IntValue;			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			return -1;
		}		
	}
	
		
}
