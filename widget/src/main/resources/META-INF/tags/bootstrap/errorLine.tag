<%@ attribute name="inputId"%>
<%@ attribute name="errorKey"%>
<%@ attribute name="errorMessage"%>
<%@ attribute name="errorMessages" type="java.lang.Object"%>
<%@ attribute name="icon" type="java.lang.String"%>
<%@ attribute name="errorTitleKey" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${not empty errorKey || not empty errorMessage || not empty errorMessages}"><c:set var="styleInfo">display:block;</c:set></c:if>
<c:if test="${empty icon}"><c:set var="icon" value="exclamation-circle"/></c:if>
<c:if test="${empty errorTitleKey}"><c:set var="errorTitleKey"><fmt:message key="error.title"/></c:set></c:if>
<div class="callout callout-danger fade in" id="${inputId}_line">
	<button data-dismiss="alert" class="close" type="button">&times;</button>
	<h5><i class="fa fa-${icon}"></i>${errorTitleKey}</h5>
	<c:set var="styleInfo">display:none;</c:set>
	<widget:error errorKey="${errorKey}" errorMessage="${errorMessage}" errorMessages="${errorMessages}"/>
</div>