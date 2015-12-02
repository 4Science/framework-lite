<%@ attribute name="errorKey"%>
<%@ attribute name="errorMessage"%>
<%@ attribute name="errorMessages" type="java.lang.Object"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty errorKey || not empty errorMessage || not empty errorMessages}">
	<c:if test="${not empty errorKey}">
		<c:if test="${not fn:startsWith(errorKey,'error.')}"><c:set var="errorKey" value="error.${errorKey}"/></c:if>
		<p><fmt:message key="${errorKey}"/></p>
	</c:if>
	<c:if test="${not empty errorMessage}"><p>${errorMessage}</p></c:if>
	<c:forEach items="${errorMessages}" var="errorMessage"><p>${errorMessage}</p></c:forEach>
</c:if>