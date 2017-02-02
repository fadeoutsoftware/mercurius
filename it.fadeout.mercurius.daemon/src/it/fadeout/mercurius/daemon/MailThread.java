package it.fadeout.mercurius.daemon;

import it.fadeout.mercurius.business.Forward;
import it.fadeout.mercurius.business.Message;
import it.fadeout.mercurius.daemon.EMailUtils.EMailUtils;
import it.fadeout.mercurius.data.ForwardsRepository;
import it.fadeout.mercurius.data.MessagesRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

public class MailThread implements Runnable {
	
	Semaphore m_oSemaphore;
	
	public MailThread(Semaphore oSemaphore) {
		m_oSemaphore = oSemaphore;
	}

	@Override
	public void run() {
		SendEmails();
	}
	
	private void SendEmails() {
		
		try {
			ForwardsRepository oForwardsRepository = new ForwardsRepository();
			MessagesRepository oMessagesRepository  = new MessagesRepository();
			
			List<Forward> aoEMailsToSend = oForwardsRepository.GetMailToSend();
			
			if (aoEMailsToSend != null) {
				for (int iMessaggio = 0; iMessaggio < aoEMailsToSend.size(); iMessaggio++) {
					
					Forward oMail = aoEMailsToSend.get(iMessaggio);
					
					// If already sent continue. May be because of BCC messages
					if (oMail.isIsSent()) continue;
					
					Message oMessage = oMessagesRepository.Select(oMail.getIdMessage(), Message.class);
					
					String sSender = oMessage.getSender();

					// Verifico Email
					if (ValidateEmail(oMail.getAddress())) {
						
						ArrayList<Forward> aoRecipients = new ArrayList<Forward>();
						
						for (int iCC=iMessaggio; iCC < aoEMailsToSend.size(); iCC++)
						{
							Forward oCC = aoEMailsToSend.get(iCC);
							if (oCC.getIdMessage()==oMail.getIdMessage())
							{
								if (ValidateEmail(oCC.getAddress()))
								{
									aoRecipients.add(oCC);
								}
							}
						}
						
						// Verifico com'è andato l'invio della mail
						boolean bMailSent = false;
						String sBackUpSender = EMailUtils.getSender();
						
						try {
							if (aoRecipients.size()==0)
							{
								
								if (sSender != null)
								{
									if (ValidateEmail(sSender))
									{
										EMailUtils.SetSender(sSender);
									}
								}
								bMailSent = MercuriusHAL.sendMail(oMail.getAddress(), oMessage.getTilte(), oMessage.getMessage(),null);
							}
							else
							{
								String [] asTo = new String[aoRecipients.size()];
								
								for (int i=0; i<aoRecipients.size();i++)
								{
									asTo[i] = aoRecipients.get(i).getAddress();
								}
								
								if (sSender != null)
								{
									if (ValidateEmail(sSender))
									{
										EMailUtils.SetSender(sSender);
									}
								}
								
								bMailSent = MercuriusHAL.sendMail(asTo, oMessage.getTilte(), oMessage.getMessage(),null);
							}
						} 
						catch (MessagingException e) {
							e.printStackTrace();

							System.out.println("Mercurius Daemon.SendEmails: " + e.getMessage());
						}
						finally {
							EMailUtils.SetSender(sBackUpSender);
						}
						
						
						if (bMailSent) {
							oMail.setIsSent(true);
							
							for (int i=0; i<aoRecipients.size();i++)
							{
								aoRecipients.get(i).setIsSent(true);
							}
						}
						
						oMail.setLastSendOn(new Date());
						oMail.setRetryCount(oMail.getRetryCount() + 1);
						
						oForwardsRepository.Update(oMail);
						
						for (int i=1; i<aoRecipients.size();i++)
						{
							aoRecipients.get(i).setLastSendOn(new Date());
							aoRecipients.get(i).setRetryCount(aoRecipients.get(i).getRetryCount() + 1);
							oForwardsRepository.Update(aoRecipients.get(i));
						}					
					}
				}
			} 
			else {
				System.out.println("Mercurius Daemon.SendEmails: lista traccia == null. Controllare connessione a DB");
			}	
		}
		catch(Throwable oEx)
		{
			System.out.println("Mercurius Daemon.SendEmails: Eccezione ");
			oEx.toString();
		}
		finally
		{
			m_oSemaphore.release();
		}
		
	}


	private static boolean ValidateEmail(String sAddress) {
		if (sAddress != null) {
			// Set the email pattern string
			Pattern oPattern = Pattern.compile(".+@.+\\.[a-z]+");

			// Match the given string with the pattern
			Matcher oMatcher = oPattern.matcher(sAddress);

			// check whether match is found
			return oMatcher.matches();
		}

		return false;
	}

	

}
