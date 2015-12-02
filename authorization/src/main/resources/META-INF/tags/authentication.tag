<%@tag import="it.cilea.core.authorization.context.AuthorizationUserHolder"%>
<%@ attribute name="property" required="true"  type="java.lang.String"%>
<%@ attribute name="var" required="true"  type="java.lang.String" %>

<%
	String varName=(String)jspContext.getAttribute("var");
	request.setAttribute(varName,AuthorizationUserHolder.getUser());
%>