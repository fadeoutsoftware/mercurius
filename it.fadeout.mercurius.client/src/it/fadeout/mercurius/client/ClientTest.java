package it.fadeout.mercurius.client;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.business.Group;

import java.util.List;

public class ClientTest {
  public static void main(String[] args) {
    
	  MercuriusAPI oAPI = new MercuriusAPI("http://localhost:8080/it.fadeout.mercurius.webapi");
	  //MercuriusAPI oAPI = new MercuriusAPI("http://130.251.104.84:8080/it.fadeout.mercurius.webapi");
	  
//	  System.setProperty ("http.proxyHost", "127.0.0.1");
//	  System.setProperty ("http.proxyPort", "8889");
	    
	  ClientTest oTest = new ClientTest();
	  //oTest.TestContacts(oAPI);
	  oTest.TestSms(oAPI);
	  //oTest.TestMails(oAPI);
  }

  public void TestContacts(MercuriusAPI oAPI) {
	  
	  Contact oInsertContact = new Contact();
	  oInsertContact.setName("Pinco");
	  oInsertContact.setSurname("Pallino");
	  int iIDContact = oAPI.createContact(oInsertContact);
	  
	  Contact oContact = oAPI.getContact(iIDContact);
	  
	  if (oContact != null) {
		  System.out.println(oContact.getName() + " " + oContact.getSurname());
	  }
	  
	  List<Contact> aoContacts = oAPI.getAllContacts();
	  if (aoContacts!= null) {
		  System.out.println("Contacts = " + aoContacts.size());
	  }
	  
	  oContact.setEMail("pinco@pallino.it");
	  
	  oAPI.updateContact(oContact);
	  //oAPI.deleteContact(oContact.getIdContact());
	  
  }
  
  public void TestSms(MercuriusAPI oAPI) {
	  String sContacts = "22;24";
	  oAPI.sendSmsToContacts(sContacts, "Ora inviamo agli Id dei Contatti");

	  List<Group> aoGroups = oAPI.getAllGroups();
	  if (aoGroups!=null) {
		  if (aoGroups.size()>0) {
			  oAPI.sendSmsToGroup(aoGroups.get(0).getIdGroup(), "Ciao ciao da Paolo");
		  }
	  }
	  
	  String sAddresses = "+393493506892;3396713586";
	  oAPI.sendSmsDirect(sAddresses, "Questo invece e' un invio diretto");	  
  }
  
  public void TestMails(MercuriusAPI oAPI) {
	  
	  String sContacts = "22;24";
	  oAPI.sendMailToContacts(sContacts, "Ora inviamo agli Id dei Contatti");
	  
	  List<Group> aoGroups = oAPI.getAllGroups();
	  if (aoGroups!=null) {
		  if (aoGroups.size()>0) {
			  oAPI.sendMailToGroup(aoGroups.get(0).getIdGroup(), "Ciao ciao da Paolo");
		  }
	  }
	  
	  String sAddresses = "p.campanella@fadeout.it;info@fadeout.it";
	  oAPI.sendMailDirect(sAddresses, "Questo invece e' un invio diretto");	  
  }  
} 