<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="suffix" type="java.lang.String" %>
<%@ attribute name="suffixIdOnly" type="java.lang.String" %>
<%@ attribute name="isMoney" type="java.lang.Boolean" %>
<%@ attribute name="isEuro" type="java.lang.Boolean" %>
<%@ attribute name="isPercent" type="java.lang.Boolean" %>
<%@ attribute name="isTime" type="java.lang.Boolean" %>
<%@ attribute name="hideName" type="java.lang.String" %>
<%@ attribute name="showI18n" type="java.lang.Boolean"%>
<%@ attribute name="i18nList" type="java.util.List"%>
<%@ attribute name="i18nPropertyPath" type="java.lang.String"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>

<c:if test="${empty suffix}"><c:set var="suffix" value=""/></c:if>
<c:if test="${empty suffixIdOnly}"><c:set var="suffixIdOnly" value=""/></c:if>
<c:if test="${empty hideName}"><c:set var="hideName" value="${false}"/></c:if>

<c:set var="objectPath" value="${widget:getObjectPath(propertyPath)}"/>
<spring:bind path="${objectPath}">
	<c:set var="object" value="${status.value}"/><c:if test="${empty object}"><c:set var="object" value="${commandObject}"/></c:if>
</spring:bind>
<c:if test="${empty isEuro}"><c:set var="isEuro" value="${widget:isEuro(object,propertyPath)}"/></c:if>
<c:if test="${empty isMoney}"><c:set var="isMoney" value="${widget:isMoney(object,propertyPath)}"/></c:if>
<c:if test="${empty isPercent}"><c:set var="isPercent" value="${widget:isPercent(object,propertyPath)}"/></c:if>
<c:set var="isDate" value="${widget:isDate(object,propertyPath)}"/>
<c:if test="${empty isTime}"><c:set var="isTime" value="${widget:isTime(object,propertyPath)}"/></c:if>
<spring:bind path="${propertyPath}">
	<c:set var="inputName" value="${status.expression}${suffix}" />
	<c:set var="inputValue">${status.value}</c:set>
	<c:choose>
		<c:when test="${isDate}"> <c:set var="inputValue" value="${fn:replace(inputValue,' 00:00','')}"/></c:when>
		<c:when test="${isMoney || isPercent}"><c:set var="inputValue" value="${widget:formatMoney(inputValue)}"/></c:when>
		<c:otherwise><c:set var="inputValue" value="${widget:formatText(inputValue)}"/></c:otherwise>
	</c:choose>
	<c:set var="inputId" value="${status.expression}${suffix}${suffixIdOnly}" />	
	<c:set var="inputId" value="${fn:replace(inputId,'[','__')}" />
	<c:set var="inputId" value="${fn:replace(inputId,']','__')}" />
	<c:set var="ampersand">'</c:set>
	<c:set var="inputId" value="${fn:replace(inputId,ampersand,'')}" />
</spring:bind>
<c:set var="content">
	<widget:hiddenCore inputName="${inputName}" inputValue="${inputValue}" inputId="${inputId}" hideName="${hideName}"/>
		<c:if test="${not empty showI18n && showI18n}">
			<c:if test="${empty i18nPropertyPath}"><c:set var="i18nPropertyPath" value="${widget:getI18nPropertyPath(propertyPath)}"/></c:if>
			<c:forEach items="${i18nList}" var="i18n">
				<c:set var="i18nPropertyPathNow">${fn:replace(i18nPropertyPath,'{I18N}',i18n.identifyingValue)}</c:set>
				<widget:hidden propertyPath="${i18nPropertyPathNow}"
					suffix="${suffix}" suffixIdOnly="${suffixIdOnly}"
					isMoney="${isMoney}" isEuro="${isEuro}" isPercent="${isPercent}" 
					isTime="${isTime}" 
					hideName="${hideName}"
				/>
			</c:forEach>					
		</c:if>
</c:set>
${content}