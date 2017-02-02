///**
// * 
// */
//package it.fadeout.mercurius.daemon.SmsUtils.impl;
//
//import it.fadeout.mercurius.daemon.SmsUtils.ISmsUtil;
//import it.fadeout.mercurius.daemon.SmsUtils.SmsUtilsCardBoardFishConfig;
//import it.fadeout.mercurius.daemon.business.MessageResults;
//import it.fadeout.mercurius.daemon.business.Sms;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.UUID;
//import java.util.Vector;
//
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.input.SAXBuilder;
//
///**
// * @author l.platania
// * 
// */
//public class SmsUtilCardBoardFish implements ISmsUtil {
//
//	protected org.apache.log4j.Logger getLogger() {
//		return org.apache.log4j.Logger.getRootLogger();
//	}
//
//	protected SmsUtilsCardBoardFishConfig s_oConfiguration = null;
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see it.fadeout.nie.SmsUtils.ISmsUtil#initializeSMS(java.util.HashMap)
//	 */
//	@Override
//	public boolean initializeSMS(HashMap<String, Object> aoParams) {
//		if (aoParams != null) {
//			String sUserName = "", sPwd = "";
//			String sWebServiceUrl1 = "";
//			String sWebServiceUrl2 = "";
//			String sWebServiceUrl3 = "";
//
//			if (aoParams.containsKey("sUserName")) {
//				sUserName = (String) aoParams.get("sUserName");
//			}
//			if (aoParams.containsKey("sPwd")) {
//				sPwd = (String) aoParams.get("sPwd");
//			}
//			if (aoParams.containsKey("sWebServiceUrl1")) {
//				sUserName = (String) aoParams.get("sWebServiceUrl1");
//			}
//			if (aoParams.containsKey("sWebServiceUrl2")) {
//				sPwd = (String) aoParams.get("sWebServiceUrl2");
//			}
//			if (aoParams.containsKey("sWebServiceUrl3")) {
//				sPwd = (String) aoParams.get("sWebServiceUrl3");
//			}
//
//			initializeSMS(sUserName, sPwd, sWebServiceUrl1, sWebServiceUrl2, sWebServiceUrl3);
//		}
//
//		return true;
//	}
//
//	protected void initializeSMS(String sUserName, String sPwd, String sWebServiceUrl1, String sWebServiceUrl2, String sWebServiceUrl3) {
//		if (s_oConfiguration == null) {
//			s_oConfiguration = new SmsUtilsCardBoardFishConfig();
//		}
//		s_oConfiguration.setUserName(sUserName);
//		s_oConfiguration.setPwd(sPwd);
//		s_oConfiguration.setWebServiceUrl1(sWebServiceUrl1);
//		s_oConfiguration.setWebServiceUrl2(sWebServiceUrl2);
//		s_oConfiguration.setWebServiceUrl3(sWebServiceUrl3);
//		// not needed when using CardBoardFish service
//		// s_oConfiguration.setCentroCosto(sCentroCosto);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see it.fadeout.nie.SmsUtils.ISmsUtil#initializeSMS(java.lang.String)
//	 */
//	@Override
//	public boolean initializeSMS(String sConfigFile) {
//		return readSmsXmlConfigFile(sConfigFile);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see it.fadeout.nie.SmsUtils.ISmsUtil#sendSMS(java.util.List)
//	 */
//	@Override
//	public MessageResults sendSMS(List<Sms> aoSmsList) {
//		// TODO
//		MessageResults oLottoResponse = new MessageResults();
//		Vector<SmsResponse> aoResponses = new Vector<SmsResponse>();
//		oLottoResponse.setIdLotto(UUID.randomUUID().toString());
//		if (aoSmsList != null) {
//			for (Sms oSms : aoSmsList) {
//				if (oSms != null) {
//					// each sms is sent individually. It may be worth to
//					// open a persistent http connection (at least when
//					// dealing with a lot of messages).
//					SmsResponse oResponse = sendGetSms(oSms);
//					aoResponses.add(oResponse);
//				}
//			}
//			oLottoResponse.setSmsResponse(aoResponses);
//		}
//		return null;
//	}
//
//	/**
//	 * Not working, something wrong in the POST, not working using fiddler too.
//	 * 
//	 * @param oSms
//	 */
//	private void sendPostSms(Sms oSms) {
//		// TODO use a persistent connection
//		String sServiceUrl = s_oConfiguration.getWebServiceUrl1();
//		URL oUrl;
//		try {
//			oUrl = new URL(sServiceUrl);
//			HttpURLConnection oConn = (HttpURLConnection) oUrl.openConnection();
//
//			oConn.setRequestMethod("POST");
//			oConn.setDoOutput(true);
//			oConn.setDoInput(true);
//			oConn.setRequestProperty("charset", "utf-8");
//			// Getting an output stream to write to the setup url
//			OutputStreamWriter oWriter = new OutputStreamWriter(oConn.getOutputStream());
//
//			String sDestNumber = oSms.getNumeroTelefono();
//			String sSmsContent = oSms.getMessaggioSms();
//			// system type
//			String sData = URLEncoder.encode("S", "UTF-8") + "=" + URLEncoder.encode("H", "UTF-8");
//			// user name
//			String sUserName = s_oConfiguration.getUserName();
//			sData += "&" + URLEncoder.encode("UN", "UTF-8") + "=" + URLEncoder.encode(sUserName, "UTF-8");
//			String sUserPwd = s_oConfiguration.getPwd();
//			sData += "&" + URLEncoder.encode("P", "UTF-8") + "=" + URLEncoder.encode(sUserPwd, "UTF-8");
//			// Destination Number.
//			// there may be up to 10 numbers, it can be an optimization.
//			// numbers should be in international format.
//			sData += "&DA=" + sDestNumber;
//			sData += "&" + URLEncoder.encode("DA", "UTF-8") + "=" + URLEncoder.encode(sDestNumber, "UTF-8");
//			// source address/ID
//			sData += "&" + URLEncoder.encode("SA", "UTF-8") + "=" + URLEncoder.encode("NIE-TEST", "UTF-8");
//			// message body.
//			sData += "&" + URLEncoder.encode("M", "UTF-8") + "=" + URLEncoder.encode(sSmsContent, "UTF-8");
//
//			try {
//				if (sData != null) {
//					// Writing data to the device.
//					oWriter.write(sData);
//					oWriter.flush();
//				}
//			} catch (Exception oEx) {
//				oEx.printStackTrace();
//				getLogger().error("SmsUtilCardBoardFish.sendSms: " + oEx.getMessage());
//			} finally {
//				// Closing the output stream.
//				oWriter.close();
//			}
//			// Get the response
//			BufferedReader oReader = new BufferedReader(new InputStreamReader(oConn.getInputStream()));
//			try {
//				String sResponse = "";
//				String sLine;
//				while ((sLine = oReader.readLine()) != null) {
//					// System.out.print(sLine + "\n");
//					sResponse += sLine;
//				}
//
//			} catch (MalformedURLException oEx) {
//				oEx.printStackTrace();getLogger().error("SmsUtilCardBoardFish.sendSms: " + oEx.getMessage());
//			}
//			// Opening a connection to the device setup url.
//			catch (IOException oEx) {
//				oEx.printStackTrace();
//				getLogger().error("SmsUtilCardBoardFish.sendSms: " + oEx.getMessage());
//			}
//		} catch (Exception oEx) {
//			oEx.printStackTrace();
//			getLogger().error("SmsUtilCardBoardFish.sendSms: " + oEx.getMessage());
//		}
//
//	}
//
//	private SmsResponse sendGetSms(Sms oSms) {
//		String sServiceUrl = s_oConfiguration.getWebServiceUrl1();
//		URL oUrl;
//		SmsResponse oResponse = new SmsResponse();
//		try {
//			String sDestNumber = oSms.getNumeroTelefono();
//			String sSmsContent = oSms.getMessaggioSms();
//			// system type
//			String sData = URLEncoder.encode("S", "UTF-8") + "=" + URLEncoder.encode("H", "UTF-8");
//			// user name
//			String sUserName = s_oConfiguration.getUserName();
//			sData += "&" + URLEncoder.encode("UN", "UTF-8") + "=" + URLEncoder.encode(sUserName, "UTF-8");
//			String sUserPwd = s_oConfiguration.getPwd();
//			sData += "&" + URLEncoder.encode("P", "UTF-8") + "=" + URLEncoder.encode(sUserPwd, "UTF-8");
//			// Destination Number.
//			// there may be up to 10 numbers, it can be an optimization.
//			// numbers should be in international format.
//			sData += "&DA=" + sDestNumber;
//			sData += "&" + URLEncoder.encode("DA", "UTF-8") + "=" + URLEncoder.encode(sDestNumber, "UTF-8");
//			// source address/ID
//			sData += "&" + URLEncoder.encode("SA", "UTF-8") + "=" + URLEncoder.encode("NIE-TEST", "UTF-8");
//			// message body.
//			sData += "&" + URLEncoder.encode("M", "UTF-8") + "=" + URLEncoder.encode(sSmsContent, "UTF-8");
//
//			oUrl = new URL(sServiceUrl + sData);
//			HttpURLConnection oConn = (HttpURLConnection) oUrl.openConnection();
//
//			oConn.setRequestMethod("GET");
//			// oConn.setDoOutput(true);
//			oConn.setDoInput(true);
//			oConn.setRequestProperty("charset", "utf-8");
//			// this would make the connection persistent, it may
//			// be worth to make this optional, depending on the number of sms
//			// to be sent in the current batch.
//			// oConn.setRequestProperty("Connection", "Keep-Alive");
//
//			// Get the response
//			BufferedReader oReader = new BufferedReader(new InputStreamReader(
//					oConn.getInputStream()));
//			try {
//				String sResponse = "";
//				String sLine;
//				while ((sLine = oReader.readLine()) != null) {
//					// System.out.print(sLine + "\n");
//					sResponse += sLine;
//				}
//				
//				// parse response
//				String[] asResponseItems = sResponse.split(" ");
//
//				if (asResponseItems.length == 2) {
//					if ("OK".compareTo(asResponseItems[0]) == 0) {
//						// sms succesfully sent.
//						// get message id.
//						oSms.setIdMessaggio(asResponseItems[1]);
//
//					}
//					oResponse.setCode(asResponseItems[0]);
//					oResponse.setDescription(asResponseItems[1]);
//					Date dtNow = new Date();
//					oSms.setDataInvio(dtNow);
//					oSms.setDataInvio(dtNow);
//					oSms.setDataOraInvio(dtNow);
//				}
//
//				return oResponse;
//
//			} catch (MalformedURLException oEx) {
//				oEx.printStackTrace();
//				getLogger().error("SmsUtilCardBoardFish.sendSms: " + oEx.getMessage());
//				oResponse.setCode("ERR");
//				oResponse.setDescription("MalformedURLException");
//			}
//			// Opening a connection to the device setup url.
//			catch (IOException oEx) {
//				oEx.printStackTrace();
//				getLogger().error("SmsUtilCardBoardFish.sendSms: " + oEx.getMessage());
//				oResponse.setCode("ERR");
//				oResponse.setDescription("IOException");
//			}
//		} catch (Exception oEx) {
//			oEx.printStackTrace();
//			getLogger().error("SmsUtilCardBoardFish.sendSms: " + oEx.getMessage());
//			oResponse.setCode("ERR");
//			oResponse.setDescription("Exception");
//		}
//		return oResponse;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see it.fadeout.nie.SmsUtils.ISmsUtil#GetInfoLotto(int)
//	 */
//	@Override
//	public LottoResponse GetInfoLotto(int iIdLotto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	protected boolean readSmsXmlConfigFile(String sFilePath) {
//		File oXmlConfig = new File(sFilePath);
//
//		if (oXmlConfig.exists()) {
//
//			try {
//				SAXBuilder oBuilder = new SAXBuilder();
//				Document oDocument = oBuilder.build(oXmlConfig);
//
//				Element oRoot = oDocument.getRootElement();
//				Element oMailNode = oRoot.getChild("CARDBOARDFISHSMSCONFIG");
//
//				String sUserName = oMailNode.getChild("USERNAME").getText();
//				String sPwd = oMailNode.getChild("PASSWORD").getText();
//				// String sCentroCosto =
//				// oMailNode.getChild("CENTRODICOSTO").getText();
//				String sWebServiceUrl1 = oMailNode.getChild("URL1").getText();
//				String sWebServiceUrl2 = oMailNode.getChild("URL2").getText();
//				String sWebServiceUrl3 = oMailNode.getChild("URL3").getText();
//				initializeSMS(sUserName, sPwd, sWebServiceUrl1,sWebServiceUrl2, sWebServiceUrl3);
//				return true;
//
//			} catch (IOException oEx) {
//				getLogger().error("SmsUtilCardBoardFish.readSmsXmlConfigFile: " + oEx.getMessage());
//				return false;
//			} catch (JDOMException oEx) {
//				getLogger().error("SmsUtilCardBoardFish.readSmsXmlConfigFile: " + oEx.getMessage());
//				return false;
//			} catch (NumberFormatException oEx) {
//				getLogger().error("SmsUtilCardBoardFish.readSmsXmlConfigFile: " + oEx.getMessage());
//				return false;
//			} catch (Throwable oEx) {
//				getLogger().error("SmsUtilCardBoardFish.readSmsXmlConfigFile: " + oEx.getMessage());
//				return false;
//			}
//		}
//
//		return false;
//	}
//}
