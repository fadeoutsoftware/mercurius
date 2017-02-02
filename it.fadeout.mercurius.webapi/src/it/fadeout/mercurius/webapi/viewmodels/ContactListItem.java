package it.fadeout.mercurius.webapi.viewmodels;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactListItem {
	String Name;
	
	int id;
	
	List<ContactListItem> contactListItems = new ArrayList<ContactListItem>();

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<ContactListItem> getContactListItems() {
		return contactListItems;
	}

	public void setContactListItems(List<ContactListItem> contactListItems) {
		this.contactListItems = contactListItems;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
