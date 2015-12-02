<%@ tag import="org.apache.commons.lang.StringUtils"%>
<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="modeType" type="java.lang.String" %>
<%@ attribute name="layoutMode" type="java.lang.String" %>
<%@ attribute name="allowMultiple" type="java.lang.Boolean" %>
<%@ attribute name="multipleIndexToShow" type="java.lang.Integer" %>
<%@ attribute name="multipleCssClass" type="java.lang.String" %>
<%@ attribute name="multipleModelAttributeName" type="java.lang.String" %>
<%@ attribute name="activeMultipleButton" type="java.lang.String" %>
<%@ attribute name="widgetType" type="java.lang.String" %>
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

<c:if test="${empty layoutMode}"><c:set var="layoutMode" value="div"/></c:if>
<c:if test="${empty allowMultiple}"><c:set var="allowMultiple" value="${false}"/></c:if>
<c:if test="${empty showCounter}"><c:set var="showCounter" value="false"/></c:if>
<c:if test="${empty modeType || modeType==''}"><c:set var="modeType" value="enabled"/></c:if>
<c:if test="${empty suffix}"><c:set var="suffix" value=""/></c:if>
<c:if test="${empty suffixIdOnly}"><c:set var="suffixIdOnly" value=""/></c:if>
<c:if test="${empty allowNegativeNumbers}"><c:set var="allowNegativeNumbers" value="false"/></c:if>
<c:if test="${empty readonly}"><c:set var="readonly" value="${false}"/></c:if>
<c:if test="${empty disabled}"><c:set var="disabled" value="${false}"/></c:if>
<c:if test="${empty hideName}"><c:set var="hideName" value="${false}"/></c:if>
<c:if test="${modeType=='display'}"><c:set var="showCounter" value="false"/></c:if>

<c:set var="objectPath" value="${widget:getObjectPath(propertyPath)}"/>
<spring:bind path="${objectPath}">
	<c:set var="object" value="${status.value}"/><c:if test="${empty object}"><c:set var="object" value="${commandObject}"/></c:if>
</spring:bind>
<c:if test="${empty required}"><c:set var="required" value="${widget:required(object,propertyPath)}"/></c:if>
<c:if test="${empty required}"><c:set var="required" value="${widget:required(object,propertyPath)}"/></c:if>
<c:if test="${empty maxlength}"><c:set var="maxlength" value="${widget:maxLength(object,propertyPath)}"/></c:if>
<c:if test="${empty maxlength}"><c:set var="maxlength" value="255"/></c:if>
<c:if test="${empty isEuro}"><c:set var="isEuro" value="${widget:isEuro(object,propertyPath)}"/></c:if>
<c:if test="${empty isMoney}"><c:set var="isMoney" value="${widget:isMoney(object,propertyPath)}"/></c:if>
<c:if test="${empty isPercent}"><c:set var="isPercent" value="${widget:isPercent(object,propertyPath)}"/></c:if>
<c:set var="isDate" value="${widget:isDate(object,propertyPath)}"/>
<c:if test="${empty isTime}"><c:set var="isTime" value="${widget:isTime(object,propertyPath)}"/></c:if>
<c:set var="isMoneyPatternDefault" value="${widget:isMoneyPatternDefault(object,propertyPath)}"/>
<c:if test="${empty moneyPattern && isMoneyPatternDefault}"><c:set var="moneyPattern" value="prompt.moneyPattern.default"/></c:if>
<c:if test="${empty cssClass}">
	<c:if test="${isDate}"><c:set var="cssClass" value="date"/></c:if>
	<c:if test="${isMoney}"><c:set var="cssClass" value="number"/></c:if>
	<c:if test="${isPercent}"><c:set var="cssClass" value="quarter"/></c:if>
</c:if>
<c:choose>
<c:when test="${not allowMultiple}">
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
		<c:set var="errorMessages" value="${status.errorMessages}" />
	</spring:bind>
	<c:set var="hasError" value="${not empty errorMessages}"/>
	<c:set var="content">
		<widget:textCore modeType="${modeType}" isDate="${isDate}" isTime="${isTime}" isMoney="${isMoney}" isEuro="${isEuro}" isPercent="${isPercent}"
			inputName="${inputName}" inputValue="${inputValue}" inputId="${inputId}"
			allowNegativeNumbers="${allowNegativeNumbers}" showCounter="${showCounter}" maxlength="${maxlength}" cssClass="${cssClass}"
			onchange="${onchange}" readonly="${readonly}" disabled="${disabled}" calendarFutureOnly="${calendarFutureOnly}" moneyPattern="${moneyPattern}" onkeyup="${onkeyup}"
			hideName="${hideName}"
			autocompleteUrl="${autocompleteUrl}" autocompleteGetUrl="${autocompleteGetUrl}" autocompleteInputName="${autocompleteInputName}" 
			autocompleteInputValue="${autocompleteInputValue}" autocompleteData="${autocompleteData}" autocompleteGetData="${autocompleteGetData}"
			password="${password}" hasError="${hasError}" activeMultipleButton="${activeMultipleButton}" multipleCssClass="${multipleCssClass}"/>
			<c:if test="${not empty showI18n && showI18n}">
				<c:if test="${empty i18nFieldsetLabelKey}"><c:set var="i18nFieldsetLabelKey" value="fieldset.i18n.default"/></c:if>
				<div class="i18n-form">
					<%-- <fieldset>
						<legend><fmt:message key="${i18nFieldsetLabelKey}"/></legend>--%>
						<c:if test="${empty i18nPropertyPath}"><c:set var="i18nPropertyPath" value="${widget:getI18nPropertyPath(propertyPath)}"/></c:if>
						<c:forEach items="${i18nList}" var="i18n">
							<c:set var="i18nPropertyPathNow">${fn:replace(i18nPropertyPath,'{I18N}',i18n.identifyingValue)}</c:set>
							<widget:text propertyPath="${i18nPropertyPathNow}" labelKey="${i18n.labelKey}"
								modeType="${modeType}" layoutMode="${layoutMode}" cssClass="${cssClass}"
								suffix="${suffix}" suffixIdOnly="${suffixIdOnly}"
								required="${required}" maxlength="${maxlength}"
								isMoney="${isMoney}" allowNegativeNumbers="${allowNegativeNumbers}" isEuro="${isEuro}" isPercent="${isPercent}" moneyPattern="${moneyPattern}"
								isTime="${isTime}" calendarFutureOnly="${calendarFutureOnly}"
								onchange="${onchange}" onkeyup="${onkeyup}" showCounter="${showCounter}"
								readonly="${readonly}" hideName="${hideName}" password="${password}"
								leftCols="2" rightCols="10"
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
		<c:choose>
			<c:when test="${cssClass=='half'}"><c:set var="rightCols" value="5"/></c:when>
			<c:when test="${cssClass=='quarter'}"><c:set var="rightCols" value="3"/></c:when>
			<c:when test="${cssClass=='third'}"><c:set var="rightCols" value="4"/></c:when>
			<c:when test="${cssClass=='date'}"><c:set var="rightCols" value="5"/></c:when>
			<c:when test="${cssClass=='number'}"><c:set var="rightCols" value="5"/></c:when>
		</c:choose>
		<c:if test="${empty rightCols}"><c:set var="rightCols" value=""/></c:if>
		<widget:line inputId="${inputId}" label="${label}" content="${content}" errorMessages="${errorMessages}" infoKey="${infoKey}" rightCols="${rightCols}" leftCols="${leftCols}"/>
	</c:if>
	<c:if test="${layoutMode=='none'}">${content}</c:if>
</c:when>
<c:otherwise>
	<c:set var="propertyPathNow" value="${propertyPath}" scope="request"/>
	<%
		String propertyPath=(String)request.getAttribute("propertyPathNow");
		String propertyPathMap=StringUtils.substringBeforeLast(propertyPath, "[");
		String propertyPathKey=StringUtils.substringAfterLast(propertyPath, "[");
		propertyPathKey=StringUtils.substringBeforeLast(propertyPathKey,"]");
		request.setAttribute("propertyPathMap",propertyPathMap);
		request.setAttribute("propertyPathKey",propertyPathKey);
	%>
	<spring:bind path="${propertyPath}">
		<c:set var="multipleCssClass" value="${status.expression}${suffix}${suffixIdOnly}" />	
		<c:set var="multipleCssClass" value="${fn:replace(multipleCssClass,'[','__')}" />
		<c:set var="multipleCssClass" value="${fn:replace(multipleCssClass,']','')}" />
		<c:set var="ampersand">'</c:set>
		<c:set var="multipleCssClass" value="${fn:replace(multipleCssClass,ampersand,'')}" />
	</spring:bind>
	<c:if test="${empty multipleIndexToShow || multipleIndexToShow==0}">
		<c:if test="${empty labelKey}"><c:set var="labelKey" value="label.${widget:findEndLabel(propertyPath)}"/></c:if>
		<script type="text/javascript">
			function addWidgetMultiple${multipleCssClass}(inputId) {
				$('#'+inputId+'_buttonAdd').toggleClass('hidden');
				$('#'+inputId+'_buttonRemove').toggleClass('hidden');
				var url='http://suardi-p.sede.cilea.it:8080/jspui/metadata/newField.ajax';
				var prefix='${multipleCssClass}';
				var lastId=$('.'+prefix).last().attr('id');
				nextId=Number(lastId.replace(prefix+'_','').replace('__',''))+1;
				var $waitModal=$('<div class="overlay"><div class="opacity"></div><i class="icon-spinner2 spin"></i></div>');
				$('body').append($waitModal);
				$('.overlay').fadeIn(150);
				$.ajax({
					url: url,
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					type: "POST",
					data: {
						'widgetClass':'it.cilea.core.widget.model.impl.command.CommandTextWidget',
						'modelAttributeName': '${propertyPath}',
						'modeType': 'enabled',
						'section': 'filter',
						'discriminator': 'command-text',
						'allowMultiple': 'true',
						'labelKey': '${labelKey}',
						'multipleIndexToShow': nextId
					},
					success: function(data, textStatus, jqXHR) {
						$waitModal.remove();
						$('#'+lastId+'_line').after(data);
					},
					error: function(jqXHR, textStatus, errorThrown) {
						$waitModal.remove();
						alert('error');
					}
				});
			}
		</script>
	
		<spring:bind path="${propertyPathMap}">
			<c:set var="mappa" value="${status.value}" scope="request"/>
		</spring:bind>
		
		<%
		java.util.Map<String,Object> mappa=(java.util.Map<String,Object>)request.getAttribute("mappa");
		java.util.Map<String,Object> newMap = mappa.getClass().newInstance();
		
		for (String string : mappa.keySet()) {
			if (StringUtils.startsWith(string, propertyPathKey + "_")
					&& StringUtils.containsNone(StringUtils.substringAfter(string, propertyPathKey + "_"), "_")) 
				newMap.put(string, mappa.get(string));
		}
		if (newMap.size()==0)
			newMap.put(propertyPathKey+"_0", null);
		request.setAttribute("newpMap",newMap);
		%>
		
		
		<c:forEach items="${newpMap}" var="single" varStatus="varStatus">
			<c:set var="activeMultipleButton"><c:choose><c:when test="${not varStatus.last}">remove</c:when><c:when test="${varStatus.last}">add</c:when></c:choose></c:set>
			<widget:text propertyPath="${propertyPathMap}[${single.key}]"
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
				allowMultiple="false"
				activeMultipleButton="${activeMultipleButton}"
				multipleCssClass="${multipleCssClass}"
			/>
		</c:forEach>
	</c:if>
	<c:if test="${not empty multipleIndexToShow && multipleIndexToShow>0}">
		<widget:text propertyPath="${propertyPathMap}[${propertyPathKey}_${multipleIndexToShow}]"
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
			allowMultiple="false"
			activeMultipleButton="add"
			multipleCssClass="${multipleCssClass}"
		/>
	</c:if>
</c:otherwise>
</c:choose>