package it.fadeout.mercurius.webapi;

import java.util.ArrayList;
import java.util.List;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.business.PrimitiveResult;
import it.fadeout.mercurius.data.ContactsRepository;
import it.fadeout.mercurius.webapi.viewmodels.ContactListItem;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/contacts")
public class ContactsResource {
	
	@GET
	@Produces({"application/xml", "application/json", "text/xml"})
	@Path("/{idcontact}")
	public Contact getContact(@PathParam("idcontact") int idContact) {		
		ContactsRepository oContactRepository = new ContactsRepository();
		Contact oContact = oContactRepository.Select(idContact, Contact.class);
		return oContact;
	}
	
	
	@GET
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Path("/all")
	public List<Contact> getContacts() {
		ContactsRepository oContactRepository = new ContactsRepository();
		List<Contact> aoContacts = oContactRepository.SelectAll(Contact.class);
		return aoContacts;
	}	
	
	@PUT
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	public PrimitiveResult saveContact(Contact oContact) {
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.IntValue = -1;
		
		if (oContact == null) return oResult;
		
		ContactsRepository oContactRepository = new ContactsRepository();
		oContactRepository.Save(oContact);
		int iIdContact = oContact.getIdContact();
		
		oResult.IntValue = iIdContact;
		return oResult;
	}
	
	@DELETE
	@Produces({"application/xml", "application/json", "text/xml"})
	@Path("/{idcontact}")
	public PrimitiveResult deleteContact(@PathParam("idcontact") int idContact) {
		PrimitiveResult oResult = new PrimitiveResult();
		ContactsRepository oContactRepository = new ContactsRepository();
		Contact oContact = oContactRepository.Select(idContact, Contact.class);
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
	public PrimitiveResult updateContact(Contact oContact) {
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.BoolValue = false;
		
		if (oContact == null) return oResult;
		
		ContactsRepository oContactRepository = new ContactsRepository();
		//oContactRepository.Select(oContact.getIdContact(), Contact.class);
		oResult.BoolValue = oContactRepository.Update(oContact);
		
		return oResult;
	}	

}
