import java.util.List;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.client.MercuriusAPI;


public class MercuriusClientApiTest {

	public static void main(String[] args) {

		MercuriusAPI oAPI = new MercuriusAPI("http://130.251.104.84:8080/it.fadeout.mercurius.webapi");
		
		List<Contact>  aoContacts = oAPI.getAllContacts();
		System.out.println("Contatti: " + aoContacts.size());
	}

}
