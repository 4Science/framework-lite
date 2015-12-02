<%@ attribute name="inputId" required="true"%>
<%@ attribute name="label" required="true"%>
<%@ attribute name="content" required="true"%>
<%@ attribute name="errorMessage"%>
<%@ attribute name="errorMessages" type="java.lang.Object"%>
<%@ attribute name="infoKey"%>
<%@ attribute name="infoMessage"%>
<%@ attribute name="leftCols"%>
<%@ attribute name="rightCols"%>
<%@ attribute name="isStaticContent" type="java.lang.Boolean"%>
<%@ attribute name="cssClassMore"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="lineClass">line form-group</c:set>
<c:if test="${(not empty errorMessage && errorMessage != '') || not empty errorMessages}"><c:set var="lineClass">line form-group has-error has-feedback</c:set></c:if>
<c:if test="${empty leftCols}"><c:set var="leftCols" value="2"/></c:if>
<c:if test="${empty rightCols}"><c:set var="rightCols" value="10"/></c:if>
<div class="${lineClass}${cssClassMore}" id="${inputId}_line">
	<div class="line-label col-sm-${leftCols}">${label}</div>
	<div class="line-content col-sm-${rightCols}" id="${inputId}_content">
		<c:choose>
			<c:when test="${isStaticContent}"><p class="form-control-static">${content}<c:if test="${empty content}">&nbsp;</c:if></p></c:when>
			<c:otherwise>${content}</c:otherwise>
		</c:choose>
		<c:if test="${(not empty errorMessage && errorMessage != '') || not empty errorMessages}">
			<label for="${inputId}" class="error" id="${inputId}_errorDiv">
				<c:if test="${not empty errorMessage}"><div><i class="fa fa-exclamation-circle"></i>&nbsp;${errorMessage}</div></c:if>
				<c:forEach items="${errorMessages}" var="errorMessageNow"><div><i class="fa fa-exclamation-circle"></i>&nbsp;${errorMessageNow}</div></c:forEach>
			</label>
		</c:if>
		<c:if test="${not empty infoKey || not empty infoMessage}">
			<label for="${inputId}" class="info" id="${inputId}_infoDiv">
				<c:if test="${empty icon}"><c:set var="icon" value="info-circle"/></c:if>
				<c:if test="${not empty infoKey}">
					<c:if test="${not fn:startsWith(infoKey,'info.')}"><c:set var="infoKey" value="info.${infoKey}"/></c:if>
					<div><i class="fa fa-${icon}"></i>&nbsp;<fmt:message key="${infoKey}"/></div>
				</c:if>
				<c:if test="${not empty infoMessage}"><div><i class="fa fa-${icon}"></i>&nbsp;${infoMessage}</div></c:if>
				<c:forEach items="${infoMessages}" var="infoMessage"><div><i class="fa fa-${icon}"></i>&nbsp;${infoMessage}</div></c:forEach>
			</label>
		</c:if>
	</div>
</div>