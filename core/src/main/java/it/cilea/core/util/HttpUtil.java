package it.cilea.core.util;

import javax.servlet.ServletRequest;

public class HttpUtil {

	public static String getMimeTypeHeader(String outputType){
		if ("json".equalsIgnoreCase(outputType)){
			return "application/json;charset=UTF-8";
		} else if ("xml".equalsIgnoreCase(outputType)){
			return "text/xml;charset=UTF-8";
		} else {
			throw new IllegalArgumentException(outputType+" is not a valid outputType");
		}
	}

	/**
	 * Get BaseUrl from request
	 * 
	 * @param request
	 * @return
	 */
	public static String getBaseUrl(ServletRequest request) {
		StringBuffer baseUrl = new StringBuffer();
		baseUrl.append(request.getScheme() + "://" + request.getServerName());
		baseUrl.append(":" + request.getServerPort() + "/");
		return baseUrl.toString();
	}
}
