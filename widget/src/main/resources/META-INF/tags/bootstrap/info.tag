<%@ attribute name="infoKey"%>
<%@ attribute name="infoMessage"%>
<%@ attribute name="infoMessages" type="java.lang.Object"%>
<%@ attribute name="infoParameters" type="java.lang.String"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<c:if test="${not empty infoKey || not empty infoMessage || not empty infoMessages}">
	<c:if test="${not empty infoKey}">
		<c:if test="${not fn:startsWith(infoKey,'info.')}"><c:set var="infoKey" value="info.${infoKey}"/></c:if>
		<c:if test="${empty infoParameters}"><p><fmt:message key="${infoKey}"/></p></c:if>
		<c:if test="${not empty infoParameters}"><spring:message code="${infoKey}" arguments="${infoParameters}"/></c:if>
	</c:if>
	<c:if test="${not empty infoMessage}"><p>${infoMessage}</p></c:if>
	<c:forEach items="${infoMessages}" var="infoMessage"><p>${infoMessage}</p></c:forEach>
</c:if>