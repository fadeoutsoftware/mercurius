package it.fadeout.mercurius.data;

import it.fadeout.mercurius.business.User;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class UserRepository extends Repository<User> {
	
	public boolean Login(String sUserName, String sUserPassword) {
		Session oSession = null;
		boolean bRet = false;
		
		try {
			oSession = HibernateUtils.getSessionFactory().openSession();
			oSession.beginTransaction();
			Query oQuery = oSession.createQuery("from User where userName = :username");
			oQuery.setParameter("username", sUserName);
			List<User> aoUsers = oQuery.list();
			oSession.getTransaction().commit();
			
			if (aoUsers != null) {
				if (aoUsers.size()>0) {
					if (aoUsers.get(0).getPassword().equals(sUserPassword)) bRet = true;
				}
			}
		}
		catch(Throwable oEx) {
			System.err.println(oEx.toString());
			oEx.printStackTrace();
			bRet = false;
		}
		finally {
			if (oSession!=null) oSession.close();
		}
		
		
		return bRet;
	}

}
