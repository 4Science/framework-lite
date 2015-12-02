<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="titleKey" type="java.lang.String"%>
<%@ attribute name="title" type="java.lang.String"%>

<% 
	if (request.getAttribute("javax.servlet.forward.servlet_path")!=null)
		jspContext.setAttribute("_titleKey",request.getAttribute("javax.servlet.forward.servlet_path").toString());
	else if (request.getAttribute("javax.servlet.forward.request_uri")!=null)
		jspContext.setAttribute("_titleKey",request.getAttribute("javax.servlet.forward.request_uri").toString());
	else
		jspContext.setAttribute("_titleKey",request.getServletPath());
%> 
<c:choose>
	<c:when test="${not empty titleKey}"><fmt:message key="${titleKey}" /></c:when>
	<c:when test="${not empty title}">${title}</c:when>
	<c:otherwise><fmt:message key="title${fn:replace(_titleKey,'/','.')}" /></c:otherwise>
</c:choose>
