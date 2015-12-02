<%@ attribute name="propertyPath" required="true" %>
<%@ attribute name="modeType" type="java.lang.String" %>
<%@ attribute name="layoutMode" type="java.lang.String" %>
<%@ attribute name="cssClass" type="java.lang.String" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="helpKey"%>
<%@ attribute name="infoKey" type="java.lang.String" %>
<%@ attribute name="suffix" type="java.lang.String" %>
<%@ attribute name="required" type="java.lang.Boolean" %>
<%@ attribute name="maxlength" type="java.lang.Integer" %>
<%@ attribute name="onchange" type="java.lang.String" %>
<%@ attribute name="onkeyup" type="java.lang.String" %>
<%@ attribute name="showCounter" type="java.lang.Boolean" %>
<%@ attribute name="readonly" type="java.lang.Boolean" %>
<%@ attribute name="ckEditor"  type="java.lang.Boolean" %>
<%@ attribute name="hideName" type="java.lang.String" %>
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

<c:if test="${empty layoutMode}"><c:set var="layoutMode" value="div"/></c:if>
<c:if test="${empty showCounter}"><c:set var="showCounter" value="false"/></c:if>
<c:if test="${empty modeType || modeType==''}"><c:set var="modeType" value="enabled"/></c:if>
<c:if test="${empty suffix}"><c:set var="suffix" value=""/></c:if>
<c:if test="${empty readonly}"><c:set var="readonly" value="${false}"/></c:if>
<c:if test="${empty ckEditor}"><c:set var="ckEditor" value="${false}"/></c:if>
<c:if test="${empty hideName}"><c:set var="hideName" value="${false}"/></c:if>
<c:if test="${modeType=='display'}"><c:set var="showCounter" value="false"/></c:if>

<c:set var="objectPath" value="${widget:getObjectPath(propertyPath)}"/>
<spring:bind path="${objectPath}">
	<c:set var="object" value="${status.value}"/><c:if test="${empty object}"><c:set var="object" value="${commandObject}"/></c:if>
</spring:bind>
<c:if test="${empty required}"><c:set var="required" value="${widget:required(object,propertyPath)}"/></c:if>
<c:if test="${empty maxlength}"><c:set var="maxlength" value="${widget:maxLength(object,propertyPath)}"/></c:if>
<c:if test="${empty maxlength}"><c:set var="maxlength" value="255"/></c:if>
<spring:bind path="${propertyPath}">
	<c:set var="inputName" value="${status.expression}${suffix}" />
	<c:set var="inputValue">${status.value}</c:set>	
	<c:set var="inputId" value="${status.expression}${suffix}" />
	<c:set var="inputId" value="${fn:replace(inputId,'[','__')}" />
	<c:set var="inputId" value="${fn:replace(inputId,']','__')}" />
	<c:set var="ampersand">'</c:set>
	<c:set var="inputId" value="${fn:replace(inputId,ampersand,'')}" />
	<c:set var="errorMessages" value="${status.errorMessage}" />
</spring:bind>
<c:set var="content">
	 <widget:textareaCore modeType="${modeType}" inputName="${inputName}" inputValue="${inputValue}" inputId="${inputId}"
		showCounter="${showCounter}" maxlength="${maxlength}" cssClass="${cssClass}"
		onchange="${onchange}" onkeyup="${onkeyup}" readonly="${readonly}" ckEditor="${ckEditor}"/>
	<c:if test="${not empty showI18n && showI18n}">
		<c:if test="${empty i18nFieldsetLabelKey}"><c:set var="i18nFieldsetLabelKey" value="fieldset.i18n.default"/></c:if>
		<div class="form-one-column">
			<%--<fieldset>
				<legend><fmt:message key="${i18nFieldsetLabelKey}"/></legend> --%>
				<c:if test="${empty i18nPropertyPath}"><c:set var="i18nPropertyPath" value="${widget:getI18nPropertyPath(propertyPath)}"/></c:if>
				<c:forEach items="${i18nList}" var="i18n">
					<c:set var="i18nPropertyPathNow">${fn:replace(i18nPropertyPath,'{I18N}',i18n.identifyingValue)}</c:set>
					<widget:textarea propertyPath="${i18nPropertyPathNow}" labelKey="${i18n.labelKey}"
						modeType="${modeType}" layoutMode="${layoutMode}" cssClass="${cssClass}"
						suffix="${suffix}"  ckEditor="${ckEditor}"
						required="${required}" maxlength="${maxlength}"
						onchange="${onchange}" onkeyup="${onkeyup}" showCounter="${showCounter}"
						readonly="${readonly}" hideName="${hideName}"
					/>
				</c:forEach>
			<%--</fieldset> --%>
		</div>
	</c:if>
</c:set>
<c:if test="${layoutMode=='div'}">
	<c:set var="label">
		<c:if test="${empty labelKey}"><c:set var="labelKey" value="label.${widget:findEndLabel(propertyPath)}"/></c:if>
		<c:set var="showRequired" value="${false}"></c:set>
		<c:if test="${required && modeType != 'display'}"><c:set var="showRequired" value="${true}"></c:set></c:if>
		<widget:label labelKey="${labelKey}" inputId="${inputId}" showRequired="${showRequired}" helpKey="${helpKey}" modeType="${modeType}"/>
	</c:set>
	<widget:line inputId="${inputId}" label="${label}" content="${content}" errorMessages="${errorMessages}" infoKey="${infoKey}" rightCols="${rightCols}" leftCols="${leftCols}"/>
</c:if>
<c:if test="${layoutMode=='none'}">${content}</c:if>