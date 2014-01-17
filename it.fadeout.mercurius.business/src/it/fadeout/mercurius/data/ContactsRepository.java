package it.fadeout.mercurius.data;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import it.fadeout.mercurius.business.Contact;

public class ContactsRepository extends Repository<Contact> {

	public List<Contact> getListById(List<Integer> aiContactsId) {
		Session oSession = null;
		
		List<Contact> aoList = new ArrayList<Contact>();
		
		try {
			oSession = HibernateUtils.getSessionFactory().openSession();
			oSession.beginTransaction();
			Query oQuery = oSession.createQuery("from Contact where IdContact in :idlist");
			oQuery.setParameterList("idlist", aiContactsId);
			aoList = oQuery.list();
			oSession.getTransaction().commit();			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
		}
		finally {
			if (oSession!=null) oSession.close();
		}
		
		
		return aoList;
	}
	

	public List<Contact> getOrderedList() {
		Session oSession = null;
		
		List<Contact> aoList = new ArrayList<Contact>();
		
		try {
			oSession = HibernateUtils.getSessionFactory().openSession();
			oSession.beginTransaction();
			Query oQuery = oSession.createQuery("from Contact order by Surname, Name");
			aoList = oQuery.list();
			oSession.getTransaction().commit();			
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
		}
		finally {
			if (oSession!=null) oSession.close();
		}
		
		
		return aoList;
	}	
}
