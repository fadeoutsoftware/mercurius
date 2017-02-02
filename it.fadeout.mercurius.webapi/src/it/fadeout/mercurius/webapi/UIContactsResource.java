package it.fadeout.mercurius.webapi;

import java.util.ArrayList;
import java.util.List;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.business.PrimitiveResult;
import it.fadeout.mercurius.data.ContactsRepository;
import it.fadeout.mercurius.webapi.viewmodels.ContactData;
import it.fadeout.mercurius.webapi.viewmodels.ContactListItem;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/uicontacts")
public class UIContactsResource {
	
	
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
	 @Path("/orderedall")
	 public Response corsMyResourceAll(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		 m_sCorsHeaders = requestH;
	     return makeCORS(Response.ok(), requestH);
	 }
	 
	 @OPTIONS
	 @Path("/{idcontact}")
	 public Response corsMyResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		 m_sCorsHeaders = requestH;
	     return makeCORS(Response.ok(), requestH);
	 }
	 
	 @OPTIONS
	 public Response corsMyResourcePut(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		 m_sCorsHeaders = requestH;
	     return makeCORS(Response.ok(), requestH);
	 }
	
	@GET
	@Produces({"application/xml", "application/json", "text/xml"})
	@Path("/{idcontact}")
	public ContactData getContactData(@Context HttpServletResponse oResponse, @PathParam("idcontact") int idContact) {		
		ContactsRepository oContactRepository = new ContactsRepository();
		Contact oContact = oContactRepository.Select(idContact, Contact.class);
		
		if (oContact == null) return null;
		
		ContactData oRet = new ContactData();
		
		oRet.setId(oContact.getIdContact());
		oRet.setEmail(oContact.getEMail());
		oRet.setName(oContact.getSurname()+" " + oContact.getName());
		oRet.setPhone(oContact.getMobile());
		
		oResponse.addHeader("Access-Control-Allow-Origin", "*");
		oResponse.addHeader("Access-Control-Allow-Methods", "GET");		
		return oRet;
	}
	
	@GET
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Path("/orderedall")
	public List<ContactListItem> getOrderedContacts(@Context HttpServletResponse oResponse) {
		ContactsRepository oContactRepository = new ContactsRepository();
		List<Contact> aoContacts = oContactRepository.getOrderedList();
		
		ArrayList<ContactListItem> aoRetList = new ArrayList<ContactListItem>();
		
		String sLastStart = "";
		
		for (Contact oContact : aoContacts) {
			String sName = oContact.getSurname() + " " + oContact.getName();
			if (sName.length()==0) continue;
			
			if (!sName.substring(0, 1).equals(sLastStart)) {
				sLastStart = sName.substring(0, 1);
				aoRetList.add(new ContactListItem());
				aoRetList.get(aoRetList.size()-1).setName(sLastStart);
			}
			
			ContactListItem oContactItem = new ContactListItem();
			oContactItem.setName(sName);
			oContactItem.setId(oContact.getIdContact());
			
			aoRetList.get(aoRetList.size()-1).getContactListItems().add(oContactItem);
		}
		
		oResponse.addHeader("Access-Control-Allow-Origin", "*");
		oResponse.addHeader("Access-Control-Allow-Methods", "GET");
		return aoRetList;
	}	
	
	protected void SetContactNameFromContactData(Contact oContact, ContactData oContactData) {
		String sName = "";
		String sSurname = "";
		
		if (oContactData.getName() != null) {
			String [] asSplitted = oContactData.getName().split(" ");
			if (asSplitted != null) {
				if (asSplitted.length>0) {
					sSurname = asSplitted[0];
				}
				
				if (asSplitted.length>1){
					
					for (int iStrings=1; iStrings<asSplitted.length; iStrings++) {
						sName += asSplitted[iStrings] + " ";
					}
					
					sName = sName.substring(0, sName.length()-1);
				}
			}
		}
		
		oContact.setName(sName);
		oContact.setSurname(sSurname);
	}
	
	@PUT
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	public PrimitiveResult saveContactData(@Context HttpServletResponse oResponse, ContactData oContactData) {
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.IntValue = -1;
		
		if (oContactData == null) return oResult;
		
		Contact oContact = new Contact();
		
		SetContactNameFromContactData(oContact,oContactData);
		
		oContact.setEMail(oContactData.getEmail());
		oContact.setIsHuman(true);
		oContact.setMobile(oContactData.getPhone());
		
		ContactsRepository oContactRepository = new ContactsRepository();
		oContactRepository.Save(oContact);
		int iIdContact = oContact.getIdContact();
		
		oResult.IntValue = iIdContact;
		
		oResponse.addHeader("Access-Control-Allow-Origin", "*");
		oResponse.addHeader("Access-Control-Allow-Methods", "PUT");		
		return oResult;
	}
	
	@DELETE
	@Produces({"application/xml", "application/json", "text/xml"})
	@Path("/{idcontact}")
	public PrimitiveResult deleteContact(@Context HttpServletResponse oResponse, @PathParam("idcontact") int idContact) {
		PrimitiveResult oResult = new PrimitiveResult();
		ContactsRepository oContactRepository = new ContactsRepository();
		Contact oContact = oContactRepository.Select(idContact, Contact.class);
		
		oResponse.addHeader("Access-Control-Allow-Origin", "*");
		oResponse.addHeader("Access-Control-Allow-Methods", "DELETE");		
		if (oContact == null) {
			oResult.BoolValue = false;
			return oResult;
		}
		else {
			
			oResult.BoolValue = oContactRepository.Delete(oContact);
			return oResult;
		}
	}	
	
	
	@POST
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	public PrimitiveResult updateContactData(@Context HttpServletResponse oResponse, ContactData oContactData) {
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.BoolValue = false;
		
		if (oContactData == null) return oResult;
		
		ContactsRepository oContactRepository = new ContactsRepository();
		Contact oContact =  oContactRepository.Select(oContactData.getId(), Contact.class);
		
		if (oContact == null) return oResult;
		
		SetContactNameFromContactData(oContact,oContactData);
		oContact.setEMail(oContactData.getEmail());
		oContact.setMobile(oContactData.getPhone());
		
		oResult.BoolValue = oContactRepository.Update(oContact);
		
		oResponse.addHeader("Access-Control-Allow-Origin", "*");
		oResponse.addHeader("Access-Control-Allow-Methods", "POST");
		return oResult;
	}		
}
