//package it.fadeout.mercurius.daemon.SmsUtils.impl;
//
//import it.fadeout.mercurius.daemon.SmsUtils.ISmsUtil;
//import it.fadeout.mercurius.daemon.SmsUtils.SmsUtilsConfig;
//import it.fadeout.mercurius.daemon.SmsUtils.results.BaseResult;
//import it.fadeout.mercurius.daemon.SmsUtils.results.LottoResponse;
//import it.fadeout.mercurius.daemon.SmsUtils.results.SmsResponse;
//import it.fadeout.mercurius.daemon.business.Sms;
//import it.genova.comune.wssms.WSSms.WSSms_jws.WSSms;
//import it.genova.comune.wssms.WSSms.WSSms_jws.WSSmsServiceLocator;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.StringReader;
//import java.io.UnsupportedEncodingException;
//import java.rmi.RemoteException;
//import java.sql.Timestamp;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.xml.rpc.ServiceException;
//
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.input.SAXBuilder;
//import org.xml.sax.InputSource;
//
//public class SmsUtilsComuneGenova implements ISmsUtil {
//
//	// /**
//	// * Configurazione dell'sms utils
//	// */
//	protected SmsUtilsConfig s_oConfiguration = null;
//
//	protected org.apache.log4j.Logger getLogger() {
//		return org.apache.log4j.Logger.getRootLogger();
//	}
//
//	public void initializeSMS(String sUserName, String sOldPwd, String sPwd,
//			String sCentroCosto) {
//		if (s_oConfiguration == null) {
//			s_oConfiguration = new SmsUtilsConfig();
//		}
//		s_oConfiguration.setUserName(sUserName);
//		s_oConfiguration.setOldPwd(sOldPwd);
//		s_oConfiguration.setPwd(sPwd);
//		s_oConfiguration.setCentroCosto(sCentroCosto);
//	}
//
//	/**
//	 * 
//	 * @param aSmsList
//	 *            List of SMS to be sent.
//	 * @param sUserName
//	 *            username to connect to the web service.
//	 * @param sPwd
//	 *            password to connect to the web service.
//	 * @return null if any problem occurred. Note that if no exception had been
//	 *         thrown but null is returned, the error occurred during web
//	 *         service response parsing.
//	 * @throws RemoteException
//	 *             If a problem connecting to the web service occurred.
//	 * @throws ServiceException
//	 *             If a problem connecting to the web service occurred.
//	 */
//	public LottoResponse InvioSms(List<Sms> aSmsList, String sUserName, String sPwd) {
//
//		if (aSmsList != null) {
//			// set missing fields.
//			int iSmsCount = 1;
//			for (Sms oSms : aSmsList) {
//				if (oSms.getDataInvio() == null) {
//					oSms.setDataInvio(new Timestamp(System.currentTimeMillis()));
//				}
//				if (oSms.getDataOraInvio() == null) {
//					oSms.setDataOraInvio(new Timestamp(System
//							.currentTimeMillis()));
//				}
//				if (oSms.getOraInvio() == null) {
//					oSms.setOraInvio(new Timestamp(System.currentTimeMillis()));
//				}
//				if (oSms.getCentroDiCosto() == null) {
//					oSms.setCentroDiCosto(s_oConfiguration.getCentroCosto());
//				} else if (oSms.getCentroDiCosto().isEmpty()) {
//					oSms.setCentroDiCosto(s_oConfiguration.getCentroCosto());
//				}
//				if (oSms.getNotify() == null) {
//					oSms.setNotify("N");
//				} else {
//					if (oSms.getNotify().isEmpty()) {
//						oSms.setNotify("N");
//					}
//				}
//				if (oSms.getIdMessaggio() == null) {
//					oSms.setIdMessaggio(String.valueOf(iSmsCount));
//				} else if (oSms.getIdMessaggio().isEmpty()) {
//					oSms.setIdMessaggio(String.valueOf(iSmsCount));
//				}
//				if (oSms.getValidity() == null) {
//					oSms.setValidity("10:00");
//				} else if (oSms.getValidity().isEmpty()) {
//					oSms.setValidity("10:00");
//				}
//				iSmsCount++;
//			}
//		}
//		WSSmsServiceLocator oLocator = new WSSmsServiceLocator();
//		String sWsSms = null;
//		try {
//			// find and submit sms list to the web service
//			WSSms oWSSms = oLocator.getWSSms();
//
//			String sXml = BuildSmsXml(aSmsList, sUserName, sPwd);
//			sWsSms = oWSSms.wsSetInvioSms(sXml);
//
//		} 
//		catch (ServiceException e) {
//			System.out.println("SmsUtils::InvioSms: " + e.getMessage());
//
//			return null;
//		} 
//		catch (RemoteException e) {
//			System.out.println("SmsUtils::InvioSms: " + e.getMessage());
//			return null;
//		}
//		LottoResponse oResult = null;
//		try {
//			oResult = parseLottoResponse(sWsSms);
//		} catch (Exception e) {
//			System.out.println("SmsUtils::InvioSms: " + e.getMessage());
//		}
//		return oResult;
//	}
//	
//
//
//	public LottoResponse InvioSms(List<Sms> aSmsList) {
//		return InvioSms(aSmsList, s_oConfiguration.getUserName(),
//				s_oConfiguration.getPwd());
//	}
//
//	public LottoResponse GetInfoLotto(int iIdLotto) {
//		return GetInfoLotto(iIdLotto, s_oConfiguration.getUserName(),
//				s_oConfiguration.getPwd());
//	}
//
//	public LottoResponse GetInfoLotto(int iIdLotto, String sUserName,
//			String sPassword) {
//
//		WSSmsServiceLocator oLocator = new WSSmsServiceLocator();
//		try {
//			WSSms oWSSms = oLocator.getWSSms();
//			return parseLottoResponse(oWSSms
//					.wsGetInfoLottoSms(BuildInfoLottoXml(sUserName, sPassword,
//							iIdLotto)));
//
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//
//	}
//
//	protected String BuildPwdXml(String sUserName, String sOldPwd,
//			String sNewPwd) {
//		String sReturn = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
//		sReturn += "<SMSCOMUNEGENOVA>";
//		sReturn += "<USER>";
//		sReturn += sUserName;
//		sReturn += "</USER>";
//		sReturn += "<PASSWORD_OLD>";
//		sReturn += sOldPwd;
//		sReturn += "</PASSWORD_OLD>";
//		sReturn += "<PASSWORD_NEW>";
//		sReturn += sNewPwd;
//		sReturn += "</PASSWORD_NEW>";
//		sReturn += "</SMSCOMUNEGENOVA>";
//
//		return sReturn;
//	}
//
//	protected String BuildInfoLottoXml(String sUserName, String sPwd,
//			int iIdLotto) {
//		String sReturn = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
//		sReturn += "<SMSCOMUNEGENOVA>";
//		sReturn += "<USER>";
//		sReturn += sUserName;
//		sReturn += "</USER>";
//		sReturn += "<PASSWORD>";
//		sReturn += sPwd;
//		sReturn += "</PASSWORD>";
//		sReturn += "<ID_LOTTO>";
//		sReturn += String.valueOf(iIdLotto);
//		sReturn += "</ID_LOTTO>";
//		sReturn += "</SMSCOMUNEGENOVA>";
//
//		return sReturn;
//	}
//
//	protected String BuildSmsXml(List<Sms> aSmsList, String sUserName,
//			String sPwd) {
//
//		String sReturn = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
//		sReturn += "<SMSCOMUNEGENOVA>";
//		sReturn += "<USER>";
//		sReturn += sUserName;
//		sReturn += "</USER>";
//		sReturn += "<PASSWORD>";
//		sReturn += sPwd;
//		sReturn += "</PASSWORD>";
//		sReturn += "<LISTASMS>";
//
//		for (int iSms = 0; iSms < aSmsList.size(); iSms++) {
//			sReturn += ((Sms) aSmsList.get(iSms)).ToXml();
//		}
//
//		// http://stackoverflow.com/questions/4717222/java-utf8-encoding
//		sReturn += "</LISTASMS>";
//		sReturn += "</SMSCOMUNEGENOVA>";
//		String sEncoded;
//		try {
//			sReturn = sanitizeSms(sReturn);
//			sEncoded = new String(sReturn.getBytes(), "UTF-8");
//			// sEncoded = sReturn;
//		} catch (UnsupportedEncodingException e) {
//			System.out.println("SmsUtils::BuildSmsXml: UnsupportedEncodingException" + e.getMessage());
//			e.printStackTrace();
//			sEncoded = sReturn;
//		}
//		return sEncoded;
//	}
//
//	/**
//	 * Substitutes chars אטילעש with their plain version and removes '£$%& chars
//	 * 
//	 * @param sReturn
//	 * @return
//	 */
//	public String sanitizeSms(String sReturn) {
//
//		if (sReturn == null) {
//			return "";
//		}
//		String sSanitized;
//		// remove accents
//		sSanitized = sReturn.replace('א', 'a');
//		sSanitized = sSanitized.replace('ט', 'e');
//		sSanitized = sSanitized.replace('י', 'e');
//		sSanitized = sSanitized.replace('ל', 'i');
//		sSanitized = sSanitized.replace('ע', 'o');
//		sSanitized = sSanitized.replace('ש', 'u');
//		// remove extra chars
//		sSanitized = sSanitized.replaceAll("['£$%&]", "");
//		return sSanitized;
//	}
//
//	protected BaseResult parseBaseResponse(String sResult) {
//
//		BaseResult oResult = new BaseResult();
//
//		if (sResult != null) {
//
//			SAXBuilder oBuilder = new SAXBuilder();
//			InputSource oISource = new InputSource(new StringReader(sResult));
//
//			try {
//				Document oDocument = oBuilder.build(oISource);
//				Element oRoot = oDocument.getRootElement();
//				Element oReturnNode = oRoot.getChild("RETURN");
//
//				String sCode = oReturnNode.getChild("CODICE").getText();
//				oResult.setCode(sCode);
//
//				String sDescription = oReturnNode.getChild("DESCRIZIONE")
//						.getText();
//				oResult.setDescription(sDescription);
//
//			} catch (JDOMException e) {
//				System.out.println("parseBaseResponse: " + e);
//				System.out.println("parseBaseResponse: response is " + sResult);
//			} catch (IOException e) {
//				System.out.println("parseBaseResponse: " + e);
//				e.printStackTrace();
//				System.out.println("parseBaseResponse: response is " + sResult);
//			}
//		}
//		return oResult;
//	}
//
//	protected List<SmsResponse> parseSmsResponse(String sXml) {
//
//		List<SmsResponse> oListResult = null;
//		SAXBuilder oBuilder = new SAXBuilder();
//		InputSource oISource = new InputSource(new StringReader(sXml));
//		DateFormat oDataFormatter = new SimpleDateFormat("dd/MM/yyyy");
//		DateFormat oOraFormatter = new SimpleDateFormat("HH:mm:ss");
//		DateFormat oDataOraFormatter = new SimpleDateFormat(
//				"dd/MM/yyyy HH:mm:ss");
//		try {
//
//			oListResult = new ArrayList<SmsResponse>();
//			Document oDocument = oBuilder.build(oISource);
//			Element oRoot = oDocument.getRootElement();
//			Element oEsitoNodes = oRoot.getChild("LISTA_ESITO_SMS");
//			if (oEsitoNodes != null) {
//				List<Element> oMessageNodes = oEsitoNodes
//						.getChildren("MESSAGGIO");
//				if (oMessageNodes != null) {
//					for (int iChild = 0; iChild < oMessageNodes.size(); iChild++) {
//						SmsResponse oSmsResult = new SmsResponse();
//						Element oChildNode = (Element) oMessageNodes
//								.get(iChild);
//
//						oSmsResult.getSms().setMobileNumber(
//								oChildNode.getChildText("NUMERO_TELEFONO"));
//						oSmsResult.getSms().setMessaggioSms(
//								oChildNode.getChildText("MESSAGGIO_SMS"));
//
//						String sDataInvio = oChildNode
//								.getChildText("DATA_INVIO");
//						oSmsResult.getSms().setDataInvio(
//								oDataFormatter.parse(sDataInvio));
//
//						String sOraInvio = oChildNode.getChildText("ORA_INVIO");
//						oSmsResult.getSms().setOraInvio(
//								oOraFormatter.parse(sOraInvio));
//
//						// DataOraInvio
//						String sDataOraInvio = sDataInvio + " " + sOraInvio;
//						oSmsResult.getSms().setDataOraInvio(
//								oDataOraFormatter.parse(sDataOraInvio));
//
//						oSmsResult.setCode(oChildNode
//								.getChildText("CODICE_RITORNO_SMS"));
//						oSmsResult.setDescription(oChildNode
//								.getChildText("DESCRIZIONE_RITORNO_SMS"));
//
//						oListResult.add(oSmsResult);
//					}
//				}
//			}
//
//		} catch (JDOMException e) {
//			System.out.println("parseBaseResponse: " + e);
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.out.println("parseBaseResponse: " + e);
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		return oListResult;
//
//	}
//
//	protected LottoResponse parseLottoResponse(String sResult) {
//
//		LottoResponse oResult;
//		BaseResult oBaseResult = parseBaseResponse(sResult);
//		oResult = new LottoResponse();
//		oResult.setCode(oBaseResult.getCode());
//		oResult.setDescription(oBaseResult.getDescription());
//
//		if (sResult != null) {
//
//			SAXBuilder oBuilder = new SAXBuilder();
//			InputSource oISource = new InputSource(new StringReader(sResult));
//
//			try {
//				Document oDocument = oBuilder.build(oISource);
//				Element oRoot = oDocument.getRootElement();
//				Element oReturnNode = oRoot.getChild("RETURN");
//
//				if (oReturnNode.getChild("ID_LOTTO") != null) {
//					oResult.setIdLotto(oReturnNode.getChild("ID_LOTTO")
//							.getText());
//				}
//
//				// Leggo il risultato degli SMS
//				oResult.setSmsResponse(parseSmsResponse(sResult));
//
//			} catch (JDOMException e) {
//
//				oResult = null;
//				System.out.println("parseLottoResponse: " + e);
//				e.printStackTrace();
//			} catch (IOException e) {
//				oResult = null;
//				System.out.println("parseLottoResponse: " + e);
//				e.printStackTrace();
//			}
//		}
//		return oResult;
//	}
//
//	@Override
//	public boolean initializeSMS(HashMap<String, Object> aoParams) {
//
//		if (aoParams != null) {
//			String sUserName = "", sOldPwd = "", sPwd = "", sCentroCosto = "";
//
//			if (aoParams.containsKey("sUserName")) {
//				sUserName = (String) aoParams.get("sUserName");
//			}
//
//			if (aoParams.containsKey("sOldPwd")) {
//				sOldPwd = (String) aoParams.get("sOldPwd");
//			}
//
//			if (aoParams.containsKey("sPwd")) {
//				sPwd = (String) aoParams.get("sPwd");
//			}
//
//			if (aoParams.containsKey("sCentroCosto")) {
//				sCentroCosto = (String) aoParams.get("sCentroCosto");
//			}
//
//			initializeSMS(sUserName, sOldPwd, sPwd, sCentroCosto);
//		}
//
//		return true;
//	}
//
//	@Override
//	public LottoResponse sendSMS(List<Sms> aSmsList) {
//		return InvioSms(aSmsList);
//	}
//
//	@Override
//	public boolean initializeSMS(String sConfigFile) {
//		return readSmsXmlConfigFile(sConfigFile);
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
//				Element oMailNode = oRoot.getChild("SMSCONFIG");
//
//				String sUserName = oMailNode.getChild("USERNAME").getText();
//				String sOldPwd = oMailNode.getChild("OLDPASSWORD").getText();
//				String sPwd = oMailNode.getChild("PASSWORD").getText();
//				String sCentroCosto = oMailNode.getChild("CENTRODICOSTO")
//						.getText();
//
//				initializeSMS(sUserName, sOldPwd, sPwd, sCentroCosto);
//				return true;
//
//			} catch (IOException oEx) {
//				getLogger().error(
//						"EMailUtilsConfig.initEMailConfigByXmlFile: "
//								+ oEx.getMessage());
//				return false;
//			} catch (JDOMException oEx) {
//				getLogger().error(
//						"EMailUtilsConfig.initEMailConfigByXmlFile: "
//								+ oEx.getMessage());
//				return false;
//			} catch (NumberFormatException oEx) {
//				getLogger().error(
//						"EMailUtilsConfig.initEMailConfigByXmlFile: "
//								+ oEx.getMessage());
//				return false;
//			} catch (Throwable oEx) {
//				getLogger().error(
//						"EMailUtilsConfig.initEMailConfigByXmlFile: "
//								+ oEx.getMessage());
//				return false;
//			}
//		}
//
//		return false;
//	}
//}
