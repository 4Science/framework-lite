<%@ attribute name="labelKey"%>
<%@ attribute name="label"%>
<%@ attribute name="modeType"%>
<%@ attribute name="inputId"%>
<%@ attribute name="showRequired" type="java.lang.Boolean"%>
<%@ attribute name="helpKey"%>
<%@ attribute name="help"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<c:if test="${empty modeType || modeType==''}"><c:set var="modeType" value="enabled"/></c:if>
<c:if test="${empty showRequired}"><c:set var="showRequired" value="${false}"/></c:if>
<c:set var="ampersand">'</c:set><c:set var="labelKey" value="${fn:replace(labelKey,ampersand,'')}" />
<label for="${inputId}" class="control-label">
	<span class="line-label-text"><c:if test="${not empty label}">${label}</c:if><c:if test="${labelKey != 'none' && not empty labelKey}"><fmt:message key="${labelKey}"/></c:if></span>
	<c:if test="${modeType == 'enabled' && showRequired}"><sup class="required-sup"><i class="tip fa fa-asterisk" title="<fmt:message key="prompt.required" />"></i></sup></c:if>
	<c:if test="${modeType == 'enabled' && not empty helpKey}"><sup class="help-sup"><i class="tip fa fa-question-circle" title="<fmt:message key="${helpKey}"/>"></i></sup></c:if>
	<c:if test="${modeType == 'enabled' && not empty help}"><sup class="help-sup"><i class="tip fa fa-question-circle" title="${help}"></i></sup></c:if>
</label>
