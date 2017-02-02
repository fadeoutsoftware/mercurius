package it.fadeout.mercurius.webapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.business.Forward;
import it.fadeout.mercurius.business.Group;
import it.fadeout.mercurius.business.Message;
import it.fadeout.mercurius.business.PrimitiveResult;
import it.fadeout.mercurius.data.ContactsRepository;
import it.fadeout.mercurius.data.GroupsRepository;
import it.fadeout.mercurius.data.MessagesRepository;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/mail")
public class MailResource {
	/**
	 * Translates a Message Entity in a SMS Text
	 * @param oMessage Message Entity
	 * @return SMS Text
	 */
	protected String getFinalMessage(Message oMessage) {
		
		// Result string
		String sFinalText = "Subject: ";
		// Is there a title?
		String sTitle = oMessage.getTilte();
		// Null is not wanted
		if (sTitle==null) sTitle = "";
		// Is the title a real text?
		if (sTitle.length()>0) {
			// add it to the final text
			sFinalText += sTitle; 
		}
		
		// get the message body
		sFinalText+= " - Body: " + oMessage.getMessage();
		
		// done
		return sFinalText;
	}
	
	/**
	 * Send a Mail to a Group
	 * @param oMessage Message Entity (POST received, XML or JSON)
	 * @param idGroup Group Id (Path parameter)
	 * @return Message Id or -1
	 */
	@PUT
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	@Path("/group/{idgroup}")
	public PrimitiveResult sendToGroup(@Context HttpServletResponse oResponse, Message oMessage, @PathParam("idgroup") int idGroup, @HeaderParam("x-mercurius-sender") String sSender) {
		
		// Result preparation
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.IntValue = -1;
		
		// Is there a valid message?
		if (oMessage == null) return oResult;
		
		// Check the requested group
		GroupsRepository oGroupsRepository = new GroupsRepository();
		Group oGroup = oGroupsRepository.Select(idGroup, Group.class);
		// Not found
		if (oGroup == null) return oResult;
		
		// Is there anyone in the group?
		if (oGroup.getMembers().size()==0) return oResult;
		
		
		// Create repository
		MessagesRepository oMessageRepository = new MessagesRepository();
		// Translate message
		String sFinalText = getFinalMessage(oMessage);
		
		oMessage.setCreationDate(new Date());
		
		// Generate forwards
		for (Contact oContact : oGroup.getMembers()) {
			Forward oForward = new Forward();
			oForward.setAddress(oContact.getEMail());
			oForward.setFinalText(sFinalText);
			oForward.setIdRecipient(oContact.getIdContact());
			oForward.setMedia("EMAIL");
			oForward.setScheduledOn(oMessage.getCreationDate());
			oMessage.getForwards().add(oForward);
		}
		
		if (sSender != null)
		{
			oMessage.setSender(sSender);
		}
		
		// Save the message
		oMessageRepository.Save(oMessage);
		int iIdMessage = oMessage.getIdMessage();
		
		// return Message Id in the result
		oResult.IntValue = iIdMessage;
		
		oResponse.addHeader("Access-Control-Allow-Origin", "*");
		oResponse.addHeader("Access-Control-Allow-Methods", "GET");		
		return oResult;
	}
	
	/**
	 * Sends a Mail to a contacts list
	 * @param oMessage Message Entity (POST Parameter)
	 * @param sContactsIds A ";" separated string with the ids of target contacts (Query Parameter)
	 * @return Message Id or -1
	 */
	@PUT
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	@Path("/contacts")
	public PrimitiveResult sendToContacts(@Context HttpServletResponse oResponse, Message oMessage, @DefaultValue("") @QueryParam("contactsids") String sContactsIds, @HeaderParam("x-mercurius-sender") String sSender) {
		// Prepare result
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.IntValue = -1;
		
		// Is a valid message?
		if (oMessage == null) return oResult;
		
		// Is there some contact id?
		if (sContactsIds == null) return oResult;
		if (sContactsIds.length() == 0) return oResult;
		
		// Split contacts
		String [] asContacts = sContactsIds.split(";");
		
		// Something there?
		if (asContacts == null) return oResult;
		if (asContacts.length == 0) return oResult;
		
		// Try to translate contacts
		ArrayList<Integer> aoContactsIdList = new ArrayList<Integer>();
		
		for (String sContactId : asContacts) {
			try {
				// Parse int
				int iId = Integer.parseInt(sContactId);
				// add to the list
				aoContactsIdList.add(iId);
			}
			catch(Exception oEx) {
				System.err.println(oEx.toString());
				oEx.printStackTrace();
			}
		}
		
		// Is there any contact?
		if (aoContactsIdList.size()==0) return oResult;
		
		// Create repository
		ContactsRepository oContactsRepository = new ContactsRepository();
		// Try to read contacts
		List<Contact> aoRecipients = oContactsRepository.getListById(aoContactsIdList);
		// Is there any valid recipient? 
		if (aoRecipients.size() == 0 ) return oResult;
		
		// Create message rep
		MessagesRepository oMessageRepository = new MessagesRepository();
		
		// Get the final text
		String sFinalText = getFinalMessage(oMessage);
		
		oMessage.setCreationDate(new Date());
		
		// Foreach contact
		for (Contact oContact : aoRecipients) {
			// Create a foreward
			Forward oForward = new Forward();
			oForward.setAddress(oContact.getEMail());
			oForward.setFinalText(sFinalText);
			oForward.setIdRecipient(oContact.getIdContact());
			oForward.setMedia("EMAIL");
			oForward.setScheduledOn(oMessage.getCreationDate());
			oMessage.getForwards().add(oForward);
		}
		
		if (sSender != null)
		{
			oMessage.setSender(sSender);
		}
		
		// Save the message
		oMessageRepository.Save(oMessage);
		// get the id
		int iIdMessage = oMessage.getIdMessage();
		oResult.IntValue = iIdMessage;
		
		oResponse.addHeader("Access-Control-Allow-Origin", "*");
		oResponse.addHeader("Access-Control-Allow-Methods", "GET");		
		
		// return id to the user
		return oResult;
	}
	
	/**
	 * Sends Mail directly to some ";" separated addresses
	 * @param oMessage Message Entity (POST Paramter)
	 * @param sAddresses String ";" seperated with target mail addresses (Query Parameter)
	 * @return Message Id or -1
	 */
	@PUT
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	@Path("/direct")
	public PrimitiveResult sendToNumbers(@Context HttpServletResponse oResponse, Message oMessage, @DefaultValue("") @QueryParam("addresses") String sNumbers,  @HeaderParam("x-mercurius-sender") String sSender) {
		// Prepare result
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.IntValue = -1;		
		try {
			
			System.out.println("Mercurius Send Direct Mail ");
			
			// Is there a valid message?
			if (oMessage == null) return oResult;
			
			// Is there a valid recipient list?
			if (sNumbers == null) return oResult;
			if (sNumbers.length() == 0) return oResult;
			
			// Split numbers
			String [] asContacts = sNumbers.split(";");
			
			// Is there any one?
			if (asContacts == null) return oResult;
			if (asContacts.length == 0) return oResult;
			
			// Create rep
			MessagesRepository oMessageRepository = new MessagesRepository();
			
			oMessage.setCreationDate(new Date());
			
			// Get text
			String sFinalText = getFinalMessage(oMessage);
			
			// Foreach contact
			for (String sContact : asContacts) {
				// create forward
				Forward oForward = new Forward();
				oForward.setAddress(sContact);
				oForward.setFinalText(sFinalText);
				oForward.setMedia("EMAIL");
				oForward.setScheduledOn(oMessage.getCreationDate());
				oMessage.getForwards().add(oForward);
			}
			
			if (sSender != null)
			{
				oMessage.setSender(sSender);
			}
			
			// save message
			oMessageRepository.Save(oMessage);
			// Get message id
			int iIdMessage = oMessage.getIdMessage();
			oResult.IntValue = iIdMessage;
			
			oResponse.addHeader("Access-Control-Allow-Origin", "*");
			oResponse.addHeader("Access-Control-Allow-Methods", "GET");		
			// return it to the user
			return oResult;
			
		}
		catch(Exception oEx)
		{
			oEx.printStackTrace();
			
			return oResult;
		}
	}		

	private String m_sCorsHeaders;

	private Response makeCORS(ResponseBuilder req, String returnMethod) {
	   ResponseBuilder oRequestBuilder = req.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");

	   if (!"".equals(returnMethod)) {
	      oRequestBuilder.header("Access-Control-Allow-Headers", returnMethod);
	   }

	   return oRequestBuilder.build();
	}

	private Response makeCORS(ResponseBuilder oRequestBuilder) {
	   return makeCORS(oRequestBuilder, m_sCorsHeaders);
	}
	
	 @OPTIONS
	 @Path("/contacts")
	 public Response corsMyResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		 m_sCorsHeaders = requestH;
	     return makeCORS(Response.ok(), requestH);
	 }
	 
	 @OPTIONS
	 public Response corsMyResourcePut(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		 m_sCorsHeaders = requestH;
	     return makeCORS(Response.ok(), requestH);
	 }
}
