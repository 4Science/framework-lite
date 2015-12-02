<%@ attribute name="warningKey"%>
<%@ attribute name="warningMessage"%>
<%@ attribute name="warningMessages" type="java.lang.Object"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty warningKey || not empty warningMessage || not empty warningMessages}">
	<c:if test="${not empty warningKey}">
		<c:if test="${not fn:startsWith(warningKey,'warning.')}"><c:set var="warningKey" value="warning.${warningKey}"/></c:if>
		<p><fmt:message key="${warningKey}"/></p>
	</c:if>
	<c:if test="${not empty warningMessage}"><p>${warningMessage}</p></c:if>
	<c:forEach items="${warningMessages}" var="warningMessage"><p>${warningMessage}</p></c:forEach>
</c:if>