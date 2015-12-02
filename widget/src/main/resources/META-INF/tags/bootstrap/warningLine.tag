<%@ attribute name="inputId"%>
<%@ attribute name="warningKey"%>
<%@ attribute name="warningMessage"%>
<%@ attribute name="warningMessages" type="java.lang.Object"%>
<%@ attribute name="icon" type="java.lang.String"%>
<%@ attribute name="warningTitleKey" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${not empty warningKey || not empty warningMessage || not empty warningMessages}"><c:set var="styleWarning">display:block;</c:set></c:if>
<c:if test="${empty icon}"><c:set var="icon" value="warning-circle"/></c:if>
<c:if test="${empty warningTitleKey}"><c:set var="warningTitleKey"><fmt:message key="warning.title"/></c:set></c:if>
<div class="callout callout-warning fade in" id="${inputId}_line">
	<button data-dismiss="alert" class="close" type="button">x</button>
	<h5><i class="fa fa-${icon}"></i>${warningTitleKey}</h5>
	<c:set var="styleWarning">display:none;</c:set>
	<widget:warning warningKey="${warningKey}" warningMessage="${warningMessage}" warningMessages="${warningMessages}"/>
</div>