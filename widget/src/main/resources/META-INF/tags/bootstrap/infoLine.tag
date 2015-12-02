<%@ attribute name="inputId"%>
<%@ attribute name="infoKey"%>
<%@ attribute name="infoMessage"%>
<%@ attribute name="infoMessages" type="java.lang.Object"%>
<%@ attribute name="icon" type="java.lang.String"%>
<%@ attribute name="infoTitleKey" type="java.lang.String"%>
<%@ attribute name="infoParameters" type="java.lang.String"%>
<%@ attribute name="canClose" type="java.lang.Boolean"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${not empty infoKey || not empty infoMessage || not empty infoMessages}"><c:set var="styleInfo">display:block;</c:set></c:if>
<c:if test="${empty icon}"><c:set var="icon" value="info-circle"/></c:if>
<c:if test="${empty infoTitleKey}"><c:set var="infoTitleKey">info.title</c:set></c:if>

<div class="callout callout-info fade in" id="${inputId}_line">
	<c:if test="${empty canClose || canClose}"><button data-dismiss="alert" class="close" type="button">x</button></c:if>
	<h5><i class="fa fa-${icon}"></i><fmt:message key="${infoTitleKey}"/></h5>
	<c:set var="styleInfo">display:none;</c:set>
	<widget:info infoKey="${infoKey}" infoMessage="${infoMessage}" infoMessages="${infoMessages}" infoParameters="${infoParameters}"/>
</div>