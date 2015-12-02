package it.cilea.core.util;

public class MessageUtilConstant {

	public static MessageUtilInterface messageUtil;

	public static void loadMessageUtil(MessageUtilInterface messageUtil) {
		MessageUtilConstant.messageUtil = messageUtil;
	}

}
