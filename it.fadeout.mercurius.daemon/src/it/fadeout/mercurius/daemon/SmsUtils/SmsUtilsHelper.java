/**
 * 
 */
package it.fadeout.mercurius.daemon.SmsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author l.platania Class containing helper functions related to SMS handling.
 */
public class SmsUtilsHelper {

	// LP: original version edited by me, it solves a bug in case the string
	// does not contains any of the separator chars assumed to be in it (look at
	// the regex). It also seems it makes substring of lenght iMsgMaxSize - 1.
//	public static List<String> splitMessage(String sMsg, int iMsgMaxSize) {
//
//		int iMsgSize = sMsg.length();
//		int iStart = 0, iEnd = iMsgSize, iLength = iMsgSize;
//
//		List<String> asMsgPartition = new ArrayList<String>();
//
//		while (iLength > 0) {
//
//			iEnd = Math.min(iStart + iMsgMaxSize - 1, iMsgSize);
//
//			if (iEnd > iStart) {
//				char cCurr = sMsg.charAt(iEnd - 1);
//				while (!String.valueOf(sMsg.charAt(iEnd - 1)).matches(
//						"^[\\,\\ \\.\\;\\:\\(\\)\\!\\?\\']")) {
//					// moves just before the first not alfanumeric char, if any.
//					cCurr = sMsg.charAt(iEnd - 1);
//					boolean bMatch = String.valueOf(cCurr).matches(
//							"^[\\,\\ \\.\\;\\:\\(\\)\\!\\?\\']");
//					iEnd--;
//
//					if (iEnd <= iStart) {
//						break;
//					}
//				}
//			}
//
//			if (iEnd == iStart) {
//				// no substring delimited by not alfanumeric chars
//				// just trunk the string
//				iEnd = Math.min(iStart + iMsgMaxSize - 1, iMsgSize);
//			}
//			asMsgPartition.add(sMsg.substring(iStart, iEnd));
//			iLength -= (iEnd - iStart);
//			iStart = iEnd;
//
//		}
//
//		return asMsgPartition;
//
//	}

	public static List<String> splitMessage(String sMsg, int iMsgMaxSize) {

		int iMsgSize = sMsg.length();
		int iStart = 0, iEnd = iMsgSize, iLength = iMsgSize;

		List<String> asMsgPartition = new ArrayList<String>();

		while (iLength > 0) {

			iEnd = Math.min(iStart + iMsgMaxSize - 1, iMsgSize);

			while (!String.valueOf(sMsg.charAt(iEnd - 1)).matches(
					"^([\\,\\ \\.\\;\\:\\(\\)\\!\\?\\'])")) {
				iEnd--;
			}

			asMsgPartition.add(sMsg.substring(iStart, iEnd));
			iLength -= (iEnd - iStart);
			iStart = iEnd;

		}

		return asMsgPartition;

	}
}
