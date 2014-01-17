//package it.fadeout.mercurius.daemon.SmsUtils.impl;
//
//import it.fadeout.mercurius.daemon.SmsUtils.ISmsUtil;
//import it.fadeout.mercurius.daemon.SmsUtils.results.LottoResponse;
//import it.fadeout.mercurius.daemon.business.Sms;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.net.SocketException;
//import java.util.HashMap;
//import java.util.List;
//
//import org.apache.commons.net.ftp.FTP;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPHTTPClient;
//import org.apache.commons.net.ftp.FTPReply;
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.input.SAXBuilder;
//
//public class SmsUtilsMobyt implements ISmsUtil {
//	
//	private String m_sFtpServerAddress = "";
//	private int m_iServerPort = 21;
//	private String m_sRemotePath = "";
//	private String m_sLogin = "";
//	private String m_sDomain = "";
//	private String m_sPassword  = "";
//	private String m_sTempFolder = "C:\\temp";
//	private String m_sSender = "Communicator";
//	private boolean m_bUsePassiveMode = true;
//	
//	private boolean m_bUseProxy = false;
//	private String m_sProxyServer;
//	private int m_iProxyPort;
//	private String m_sProxyUser;
//	private String m_sProxyPassword;
//
//	@Override
//	public boolean initializeSMS(HashMap<String, Object> aoParams) {
//		
//		if (aoParams != null) {
//			if (aoParams.containsKey("sFtpServerAddress"))  {
//				m_sFtpServerAddress = (String) aoParams.get("sFtpServerAddress");
//			}
//			
//			if (aoParams.containsKey("iServerPort"))  {
//				m_iServerPort = (Integer) aoParams.get("iServerPort");
//			}
//			
//			if (aoParams.containsKey("sRemotePath")) {
//				m_sRemotePath = (String) aoParams.get("sRemotePath");
//			}
//			
//			if (aoParams.containsKey("sLogin"))  {
//				m_sLogin = (String) aoParams.get("sLogin");
//			}
//			
//			if (aoParams.containsKey("sDomain"))  {
//				m_sDomain = (String) aoParams.get("sDomain");
//			}
//			
//			if (aoParams.containsKey("sPassword"))  {
//				m_sPassword = (String) aoParams.get("sPassword");
//			}
//			
//			if (aoParams.containsKey("sTempFolder"))  {
//				m_sTempFolder = (String) aoParams.get("sTempFolder");
//			}
//			
//			if (aoParams.containsKey("sSender"))  {
//				m_sSender = (String) aoParams.get("sSender");
//			}
//			
//			if (aoParams.containsKey("bUsePassiveMode"))  {
//				m_bUsePassiveMode = (Boolean) aoParams.get("bUsePassiveMode");
//			}
//			
//			if (aoParams.containsKey("bUseProxy"))  {
//				m_bUseProxy = (Boolean) aoParams.get("bUseProxy");
//			}
//			
//			if (aoParams.containsKey("sProxyServer"))  {
//				m_sProxyServer = (String) aoParams.get("sProxyServer");
//			}
//			
//			if (aoParams.containsKey("iProxyPort"))  {
//				m_iProxyPort = (Integer) aoParams.get("iProxyPort");
//			}
//			
//			if (aoParams.containsKey("sProxyUser"))  {
//				m_sProxyUser = (String) aoParams.get("sProxyUser");
//			}
//			
//			if (aoParams.containsKey("sProxyPassword"))  {
//				m_sProxyPassword = (String) aoParams.get("sProxyPassword");
//			}
//		}
//		return true;
//	}
//
//	@Override
//	public boolean initializeSMS(String sConfigFile) {
//		File oXmlConfig = new File(sConfigFile);
//
//		if (oXmlConfig.exists()) {
//
//			try {
//				SAXBuilder oBuilder = new SAXBuilder();
//				Document oDocument = oBuilder.build(oXmlConfig);
//
//				Element oRoot = oDocument.getRootElement();
//				Element oMailNode = oRoot.getChild("MOBYTSMSCONFIG");
//
//				m_sFtpServerAddress = oMailNode.getChild("FTPSERVERADDRESS").getText();
//				String sServerPort =  oMailNode.getChild("SERVERPORT").getText();
//				
//				if (sServerPort != null) {
//					if (sServerPort.equals("") == false) {
//						try {
//							m_iServerPort = Integer.parseInt(sServerPort);
//						}
//						catch(Exception oEx) {
//							oEx.printStackTrace();
//						}
//					}
//				}
//				
//				m_sRemotePath = oMailNode.getChild("REMOTEPATH").getText();
//				m_sLogin = oMailNode.getChild("LOGIN").getText();
//				m_sDomain = oMailNode.getChild("DOMAIN").getText();
//				m_sPassword = oMailNode.getChild("PASSWORD").getText();
//				m_sTempFolder = oMailNode.getChild("TEMPFOLDER").getText();
//				m_sSender = oMailNode.getChild("SENDER").getText();
//				String sUsePassiveMode = oMailNode.getChild("USEPASSIVEMODE").getText();				
//				if (sUsePassiveMode != null) {
//					if (sUsePassiveMode.equals("") == false) {
//						if (sUsePassiveMode.equals("1") || sUsePassiveMode.toUpperCase().equals("TRUE")) {
//							m_bUsePassiveMode = true;
//						}
//						else if (sUsePassiveMode.equals("0") || sUsePassiveMode.toUpperCase().equals("FALSE")) {
//							m_bUsePassiveMode = false;
//						}
//					}
//				}
//				
//				String sUseProxy  = oMailNode.getChild("USEPROXY").getText();
//				if (sUseProxy != null) {
//					if (sUseProxy.equals("") == false) {
//						if (sUseProxy.equals("1") || sUseProxy.toUpperCase().equals("TRUE")) {
//							m_bUseProxy = true;
//						}
//						else if (sUseProxy.equals("0") || sUseProxy.toUpperCase().equals("FALSE")) {
//							m_bUseProxy = false;
//						}
//					}
//				}
//
//				if (m_bUseProxy) {
//					m_sProxyServer = oMailNode.getChild("PROXYSERVER").getText();
//					String sProxyPort = oMailNode.getChild("PROXYPORT").getText();
//					if (sProxyPort != null) {
//						if (sProxyPort.equals("") == false) {
//							try {
//								m_iProxyPort = Integer.parseInt(sProxyPort);
//							}
//							catch(Exception oEx) {
//								oEx.printStackTrace();
//							}
//						}
//					}
//					m_sProxyUser = oMailNode.getChild("PROXYUSER").getText();
//					m_sProxyPassword = oMailNode.getChild("PROXYPASSWORD").getText();					
//				}
//
//				return true;
//
//			} catch (IOException oEx) {
//				oEx.printStackTrace();
//				return false;
//			} catch (JDOMException oEx) {
//				oEx.printStackTrace();
//				return false;
//			} catch (NumberFormatException oEx) {
//				oEx.printStackTrace();
//				return false;
//			} catch (Throwable oEx) {
//				oEx.printStackTrace();
//				return false;
//			}
//		}
//
//		return false;
//	}
//
//	@Override
//	public LottoResponse sendSMS(List<Sms> aSmsList) {
//		
//		
//		// Local and remote File
//		String sFileName = m_sTempFolder+"\\communicator.txt";
//		String sRemote = "communicator.txt";
//		
//		
//		try {
//			
//			// Prepare local file: open File Stream
//			FileWriter oSmsFile = new FileWriter(sFileName);
//            PrintWriter out = new PrintWriter(oSmsFile);
//            
//            // Strings to compose lines
//            String sReceiverNumber = "";
//            String sSender = m_sSender;
//            String sText = ""; 
//            String sQuality = "n";
//            int iExpire = 48;
//            
//            // For each sms to send
//            for (int iSms =0; iSms<aSmsList.size(); iSms++) {
//            	
//            	// Take sms
//            	Sms oSms = aSmsList.get(iSms);
//            	
//            	// Get Phone Number
//            	sReceiverNumber = oSms.getNumeroTelefono();
//            	sText = oSms.getMessaggioSms();
//            	
//            	// Prepare the line
//            	String sLine = String.format("%-25s%-25s%-160s%010d%-2s", sReceiverNumber, sSender, sText, iExpire, sQuality);
//            	// Write the line
//            	out.println(sLine);
//            }
//            
//            // Write text to file
//            out.close();
//		} 
//		catch (IOException e){
//			e.printStackTrace();
//		}
//		
//		// Ftp Client
//		FTPClient oFtpClient = null;
//		
//		if (m_bUseProxy) {
//			// Use Http Proxy
//			oFtpClient = new FTPHTTPClient(m_sProxyServer, m_iProxyPort, m_sProxyUser, m_sProxyPassword);
//		}
//		else {
//			// Use standard ftp
//			oFtpClient = new FTPClient();
//		}
//		
//		
//		try {
//			
//			// Connect to the server
//			oFtpClient.connect(m_sFtpServerAddress, m_iServerPort);
//			int iReply = oFtpClient.getReplyCode();
//			
//			if (!FTPReply.isPositiveCompletion(iReply)) {
//				oFtpClient.disconnect();
//				return null;
//			}
//			
//			// Login
//			if (!oFtpClient.login(m_sLogin+"@"+m_sDomain, m_sPassword))
//	        {
//				oFtpClient.logout();
//				return null;
//			}
//			
//			//Set File Type
//			oFtpClient.setFileType(FTP.ASCII_FILE_TYPE);
//			
//			// Set Passive Mode
//			if (m_bUsePassiveMode) {
//				oFtpClient.enterLocalPassiveMode();
//			}
//			else {
//				oFtpClient.enterLocalActiveMode();
//			}
//			
//			// Read Local File
//			InputStream oInputStream;
//			oInputStream = new FileInputStream(sFileName);
//			
//			oFtpClient.changeWorkingDirectory(m_sRemotePath);
//			
//			// Store to remote site
//			boolean bStore = oFtpClient.storeFile(sRemote, oInputStream);
//			if (!bStore) {
//				System.out.println("Errore nell'upload del file degli sms");
//			}
//			oInputStream.close();
//			
//			
//			// Create the command file
//			String sCommandFile = sFileName+".do_send";
//			String sCommandRemote = sRemote + ".do_send";
//			File oFile = new File(sCommandFile);
//			try {
//				oFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			oInputStream = new FileInputStream(sCommandFile);
//			// Store to remote site
//			bStore = oFtpClient.storeFile(sCommandRemote, oInputStream);
//			if (!bStore) {
//				System.out.println("Errore nell'upload del file di comando");
//			}
//			
//			oInputStream.close();
//			
//			oFtpClient.logout();
//			oFtpClient.disconnect();
//			
//		} catch (SocketException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//        finally
//        {
//            if (oFtpClient.isConnected())
//            {
//                try
//                {
//                	oFtpClient.disconnect();
//                }
//                catch (IOException f)
//                {
//                	
//                }
//            }
//        }		
//		
//		return null;
//	}
//
//	@Override
//	public LottoResponse GetInfoLotto(int iIdLotto) {
//		return null;
//	}
//
//}
