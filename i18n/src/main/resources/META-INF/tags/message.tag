<%@ attribute name="key" required="true" %>
<%@ attribute name="replaceConfig" type="java.lang.Boolean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.cineca.it/config" prefix="config" %>
<c:choose>
	<c:when test="${empty replaceConfig || replaceConfig}"><c:set var="message"><fmt:message key="${key}"/></c:set>${config:replace(message)}</c:when>
	<c:otherwise><fmt:message key="${key}"/></c:otherwise>
</c:choose>