package it.fadeout.mercurius.daemon;

import it.fadeout.mercurius.business.Forward;
import it.fadeout.mercurius.daemon.EMailUtils.EMailUtils;
import it.fadeout.mercurius.daemon.EMailUtils.EMailUtilsConfig;
import it.fadeout.mercurius.daemon.SmsUtils.ISmsUtil;
import it.fadeout.mercurius.daemon.business.MessageResults;
import it.fadeout.mercurius.daemon.business.Sms;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;
import javax.xml.rpc.ServiceException;


public class MercuriusHAL {
	
	static ISmsUtil s_oSmsUtils = null;
	
	public static void setSmsUtils(ISmsUtil oSmsUtil) {
		s_oSmsUtils = oSmsUtil;
	}

	public static void initializeEmails(String sSmptAddress, String sSmtpPort, String sSmptUser, String sSmptPwd, String sSmtpSender) {
		EMailUtils.initialize(sSmptAddress, sSmtpPort, sSmptUser, sSmptPwd, sSmtpSender);
	}
	
	public static void initializeEmails(EMailUtilsConfig oConfig) {
		EMailUtils.initialize(oConfig);
	}

	public static void initializeSms(String sConfigFile) {
		s_oSmsUtils.initializeSMS(sConfigFile);
	}
	
	public static void initializeSms(HashMap<String, Object> aoParams) {
		
		s_oSmsUtils.initializeSMS(aoParams);
	}


	/**
	 * Entry point for email sending.
	 * 
	 * @param asTo
	 * @param sSubject
	 * @param sBody
	 * @param asAttachments
	 * @return
	 * @throws MessagingException 
	 */
	public static boolean sendMail(String sTo, String sSubject, String sBody, String[] asAttachments) throws MessagingException {
		String[] asTo = new String[1];
		asTo[0] = sTo;
		return EMailUtils.SendMessage(asTo, sSubject, sBody, asAttachments);
	}

	
	/**
	 * 
	 * @param aSmsList
	 * @return
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public static boolean sendSMS(List<Forward> aSmsList) throws RemoteException, ServiceException {
		return s_oSmsUtils.sendSMS(aSmsList);
	}
	
}
