<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="modeType" type="java.lang.String" %>
<%@ attribute name="layoutMode" type="java.lang.String" %>
<%@ attribute name="cssClass" type="java.lang.String" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="helpKey"%>
<%@ attribute name="infoKey" type="java.lang.String" %>
<%@ attribute name="suffix" type="java.lang.String" %>
<%@ attribute name="suffixIdOnly" type="java.lang.String" %>
<%@ attribute name="required" type="java.lang.Boolean" %>
<%@ attribute name="maxlength" type="java.lang.Integer" %>
<%@ attribute name="isMoney" type="java.lang.Boolean" %>
<%@ attribute name="allowNegativeNumbers" type="java.lang.Boolean" %>
<%@ attribute name="isEuro" type="java.lang.Boolean" %>
<%@ attribute name="isPercent" type="java.lang.Boolean" %>
<%@ attribute name="isTime" type="java.lang.Boolean" %>
<%@ attribute name="moneyPattern" type="java.lang.String" %>
<%@ attribute name="calendarFutureOnly" type="java.lang.Boolean" %>
<%@ attribute name="onchange" type="java.lang.String" %>
<%@ attribute name="onkeyup" type="java.lang.String" %>
<%@ attribute name="showCounter" type="java.lang.Boolean" %>
<%@ attribute name="readonly" type="java.lang.Boolean" %>
<%@ attribute name="disabled" type="java.lang.Boolean" %>
<%@ attribute name="hideName" type="java.lang.String" %>
<%@ attribute name="autocompleteUrl" %>
<%@ attribute name="autocompleteGetUrl" %>
<%@ attribute name="autocompleteInputName" %>
<%@ attribute name="autocompleteInputValue" %>
<%@ attribute name="autocompleteData"%>
<%@ attribute name="autocompleteGetData"%>
<%@ attribute name="password" type="java.lang.Boolean"%>
<%@ attribute name="showI18n" type="java.lang.Boolean"%>
<%@ attribute name="i18nList" type="java.util.List"%>
<%@ attribute name="i18nFieldsetLabelKey" type="java.lang.String"%>
<%@ attribute name="i18nPropertyPath" type="java.lang.String"%>
<%@ attribute name="leftCols"%>
<%@ attribute name="rightCols"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>

<spring:bind path="${propertyPath}">
	<c:set var="fullList" value="${status.value}" />
</spring:bind>

<c:forEach items="${fullList}" var="singleValue" varStatus="status">
	<widget:text propertyPath="${propertyPath}[${status.count-1}]"
		modeType="${modeType}"
		layoutMode="${layoutMode}"
		cssClass="${cssClass}"
		labelKey="${labelKey}"
		helpKey="${helpKey}"
		infoKey="${infoKey}"
		suffix="${suffix}"
		suffixIdOnly="${suffixIdOnly}"
		required="${required}"
		maxlength="${maxlength}"
		isMoney="${isMoney}"
		allowNegativeNumbers="${allowNegativeNumbers}"
		isEuro="${isEuro}"
		isPercent="${isPercent}"
		isTime="${isTime}"
		moneyPattern="${moneyPattern}"
		calendarFutureOnly="${calendarFutureOnly}"
		onchange="${onchange}"
		onkeyup="${onkeyup}"
		showCounter="${showCounter}"
		readonly="${readonly}"
		disabled="${disabled}"
		hideName="${hideName}"
		autocompleteUrl="${autocompleteUrl}"
		autocompleteGetUrl="${autocompleteGetUrl}"
		autocompleteInputName="${autocompleteInputName}"
		autocompleteInputValue="${autocompleteInputValue}"
		autocompleteData="${autocompleteData}"
		autocompleteGetData="${autocompleteGetData}"
		password="${password}"
		showI18n="${showI18n}"
		i18nList="${i18nList}"
		i18nFieldsetLabelKey="${i18nFieldsetLabelKey}"
		i18nPropertyPath="${i18nPropertyPath}"
		leftCols="${leftCols}"
		rightCols="${rightCols}"
	/>
	
</c:forEach>