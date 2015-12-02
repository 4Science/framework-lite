<%@ attribute name="infoKey"%>
<%@ attribute name="infoMessage"%>
<%@ attribute name="infoMessages" type="java.lang.Object"%>
<%@ attribute name="wfItemElement"%>
<%@ attribute name="wfItemParent"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty infoKey || not empty infoMessage || not empty infoMessages}">
	<p>ACTION<!-- fmt:message key="${infoKey}"/ --></p>
	<c:if test="${not empty infoMessage}"><p>${infoMessage}</p></c:if>
	<c:if test="${not empty infoKey}"><p>${infoKey}</p></c:if>
	##${wfItemElement}-${wfItemParent}##
	<c:forEach items="${infoMessages}" var="infoMessage"><p>${infoMessage}</p></c:forEach>
</c:if>