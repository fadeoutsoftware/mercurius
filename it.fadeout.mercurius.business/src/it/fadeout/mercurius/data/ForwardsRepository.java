package it.fadeout.mercurius.data;

import it.fadeout.mercurius.business.Forward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class ForwardsRepository extends Repository<Forward> {
	
	public List<Forward> GetSmsToSend() {
		Session oSession = null;
		
		List<Forward> aoList = new ArrayList<Forward>();
		
		try {
			oSession = HibernateUtils.getSessionFactory().openSession();
			oSession.beginTransaction();
			Query oQuery = oSession.createQuery("from Forward where ScheduledOn <= :scheduled and Media = 'SMS' and IsSent = false and RetryCount<MaxRetry");
			oQuery.setParameter("scheduled", new Date());
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
	
	public List<Forward> GetMailToSend() {
		Session oSession = null;
		
		List<Forward> aoList = new ArrayList<Forward>();
		
		try {
			oSession = HibernateUtils.getSessionFactory().openSession();
			oSession.beginTransaction();
			Query oQuery = oSession.createQuery("from Forward where ScheduledOn <= :scheduled and media = 'EMAIL' and IsSent = false");
			oQuery.setParameter("scheduled", new Date());
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
