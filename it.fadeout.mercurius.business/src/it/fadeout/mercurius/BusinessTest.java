package it.fadeout.mercurius;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.business.Forward;
import it.fadeout.mercurius.business.Group;
import it.fadeout.mercurius.business.Message;
import it.fadeout.mercurius.data.ContactsRepository;
import it.fadeout.mercurius.data.ForwardsRepository;
import it.fadeout.mercurius.data.GroupsRepository;
import it.fadeout.mercurius.data.MessagesRepository;

public class BusinessTest {

	int iExistingContactId = 21;//10
	
	public static void main(String[] args) {
		
		BusinessTest oTest = new BusinessTest();
		oTest.TestContacts();
		//oTest.TestGroups();
		//oTest.TestMessages();
	}
	
	public void TestContacts() {
		Contact oPaolo = new Contact();
		oPaolo.setEMail("s.adamo@fadeout.it");
		oPaolo.setName("Angelo");
		oPaolo.setSurname("Adamo");
		oPaolo.setIsHuman(true);
		
		ContactsRepository oRepository = new ContactsRepository();
		
		List<Contact> aoOrdered = oRepository.getOrderedList();
		
		if (aoOrdered!=null) {
			System.out.println("Ordered Tot " + aoOrdered.size());
		}
		
		ArrayList<Integer> aoId = new ArrayList<Integer>();
		aoId.add(22);
		aoId.add(24);
		
		List<Contact> aoContacts = oRepository.getListById(aoId);
		
		
		oRepository.Save(oPaolo);
		
		List<Contact> aoContacts2 = oRepository.SelectAll(Contact.class);
		
		for (Contact oContact : aoContacts) {
			System.out.println("Contact " + oContact.getName());
		}
		
		Contact oContact = oRepository.Select(iExistingContactId, Contact.class);
		
		if (oContact != null) {
			oContact.setMobile("+393493506892");
			System.out.println("Contact " + oContact.getSurname());
			oRepository.Update(oContact);					
		}
	}
	
	public void TestGroups() {
		Group oGroup = new Group();
		oGroup.setGroupName("Terzo");
		
		GroupsRepository oRep = new GroupsRepository();
		oRep.Save(oGroup);
		
		oGroup = oRep.Select(oGroup.getIdGroup(), Group.class);
		oGroup.setGroupName(oGroup.getGroupName() + " 2 ");
		oRep.Update(oGroup);
		
		ContactsRepository oRepository = new ContactsRepository();
		Contact oContact = oRepository.Select(iExistingContactId, Contact.class);
		oGroup.getMembers().add(oContact);
		oRep.Update(oGroup);
	}
	
	public void TestMessages() {
		
		ForwardsRepository oForwardRep = new ForwardsRepository();
		List<Forward> aoSmsToSend = oForwardRep.GetSmsToSend();
		
		if(aoSmsToSend != null) {
			System.out.println("SMS to send = " + aoSmsToSend.size());
		}
		
		List<Forward> aoMailToSend = oForwardRep.GetMailToSend();
		
		if(aoMailToSend != null) {
			System.out.println("Mail to send = " + aoMailToSend.size());
		}
		
		Message oMessage = new Message();
		oMessage.setCreationDate(new Date());
		oMessage.setMessage("Ciao Mercurius");
		oMessage.setTilte("Mercurius");
		
		Forward oForeward = new Forward();
		oForeward.setAddress("+393493506892");
		oForeward.setFinalText("Ciao ciao");
		oForeward.setMedia("SMS");
		oForeward.setScheduledOn(oMessage.getCreationDate());
		
		oMessage.getForwards().add(oForeward);
		
		MessagesRepository oMessagesRepository = new MessagesRepository();
		oMessagesRepository.Save(oMessage);
		
		List<Message> aoMessages = oMessagesRepository.SelectAll(Message.class);
		if (aoMessages != null) {
			System.out.println("Messages count = " + aoMessages.size());
		}
	}

}
