package it.fadeout.mercurius.daemon.SmsUtils;

import it.fadeout.mercurius.business.Forward;

import java.util.HashMap;
import java.util.List;

public interface ISmsUtil {
	public boolean initializeSMS(HashMap<String, Object> aoParams);
	public boolean initializeSMS(String sConfigFile);
	public boolean sendSMS(List<Forward> aoSmsList);
	public boolean sendDirectSMS(String sNumber, String sMessage);
}
