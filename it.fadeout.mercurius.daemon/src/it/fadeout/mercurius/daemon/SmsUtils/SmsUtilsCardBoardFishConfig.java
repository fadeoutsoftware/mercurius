/**
 * 
 */
package it.fadeout.mercurius.daemon.SmsUtils;

import it.fadeout.mercurius.daemon.SmsUtils.SmsUtilsConfig;

/**
 * @author l.platania
 *
 */
public class SmsUtilsCardBoardFishConfig extends SmsUtilsConfig {

	private String m_sWebServiceUrl1;
	private String m_sWebServiceUrl2;
	private String m_sWebServiceUrl3;
	/**
	 * @param The url of the main web service to set
	 */
	public void setWebServiceUrl1(String webServiceUrl1) {
		m_sWebServiceUrl1 = webServiceUrl1;
	}
	/**
	 * @return The url of the main web service
	 */
	public String getWebServiceUrl1() {
		return m_sWebServiceUrl1;
	}
	/**
	 * @param The url of the secondary web service to set
	 */
	public void setWebServiceUrl2(String webServiceUrl2) {
		m_sWebServiceUrl2 = webServiceUrl2;
	}
	/**
	 * @return The url of the secondary web service
	 */
	public String getWebServiceUrl2() {
		return m_sWebServiceUrl2;
	}
	/**
	 * @param The url of the tertiary web service to set
	 */
	public void setWebServiceUrl3(String webServiceUrl3) {
		m_sWebServiceUrl3 = webServiceUrl3;
	}
	/**
	 * @return The url of the tertiary web service
	 */
	public String getWebServiceUrl3() {
		return m_sWebServiceUrl3;
	}
}
