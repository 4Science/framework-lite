<%@tag import="org.apache.commons.beanutils.PropertyUtils"%>
<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="collection" required="true" type="java.util.Collection"%>
<%@ attribute name="modeType"%>
<%@ attribute name="layoutMode"%>
<%@ attribute name="cssClass"%>
<%@ attribute name="labelKey"%>
<%@ attribute name="helpKey"%>
<%@ attribute name="infoKey"%>
<%@ attribute name="suffix"%>
<%@ attribute name="required" type="java.lang.Boolean"%>
<%@ attribute name="onchange" type="java.lang.String" %>
<%@ attribute name="onkeyup" type="java.lang.String" %>
<%@ attribute name="onclick" type="java.lang.String" %>
<%@ attribute name="onmouseover"%>
<%@ attribute name="readonly" type="java.lang.Boolean"%>
<%@ attribute name="hideName"%>
<%@ attribute name="positioning" type="java.lang.String" %>
<%@ attribute name="autoDisplay" type="java.lang.Boolean"%>
<%@ attribute name="leftCols"%>
<%@ attribute name="rightCols"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget"%>
<%-- 
autoDisplay: default: true. Se true in caso di modetype=display ricava la property senza id da propertypath
 e visualizza se presente property.displayValue
--%>
<c:if test="${empty layoutMode}"><c:set var="layoutMode" value="div"/></c:if>
<c:if test="${empty modeType || modeType==''}"><c:set var="modeType" value="enabled"/></c:if>
<c:if test="${empty suffix}"><c:set var="suffix" value=""/></c:if>
<c:if test="${empty readonly}"><c:set var="readonly" value="${false}"/></c:if>
<c:if test="${empty hideName}"><c:set var="hideName" value="${false}"/></c:if>
<c:if test="${empty autoDisplay}"><c:set var="autoDisplay" value="${false}" /></c:if>
<%--calcolo required dal propertyPath passato --%>
<c:set var="objectPath" value="${widget:getObjectPath(propertyPath)}"/>
<spring:bind path="${objectPath}">			
	<c:set var="object" value="${status.value}"/><c:if test="${empty object}"><c:set var="object" value="${commandObject}"/></c:if>
</spring:bind>
<c:if test="${empty required}"><c:set var="required" value="${widget:required(object,propertyPath)}"/></c:if>
 
<c:if test="${autoDisplay && modeType == 'display'}">
	<%--
		Se entro qui devo modificare il propertyPath per andare a prendere il .displayValue (se status.value !=null)
		Aggiunta property  emptyValue per rilevare valore nullo
	--%>
	<c:set var="emptyValue" value="${true}"/>
	<spring:bind path="${propertyPath}">
		<c:if test="${not empty status.value}">
			<c:set var="propertyPath" value="${widget:getPropertyPathNoId(propertyPath)}"/>
			<c:set var="emptyValue" value="${false}"/>
		</c:if>
	</spring:bind>	
</c:if>
 
 <spring:bind path="${propertyPath}">
	<c:set var="inputName" value="${status.expression}${suffix}" />	
	<c:set var="inputValue" value="${status.value}" />	
	<c:if test="${modeType == 'display' && not empty collection}">
		<%-- Verifico che propertyPath contenga displayValue. Se finisce con allora sono in un detail 
		ma la property non ha un oggetto collegato. Mi aspetto di trovare una lista. La scorro
		per trovare cosa visualizzare--%>
		
		<%-- 
			Se propertyPath è vuoto (emptyValue==true) oppure contiene displayValue NON entrare (visualizzazione etichetta)
			Altrimenti entra e trova identifyingValue da utilizzare per selezionare il valore
		 --%>
		<c:if test="${not fn:contains(propertyPath,'displayValue') && not emptyValue}">	
			<c:set var="inputValue">			
				<c:forEach var="item" items="${collection}" varStatus="loop">
					<c:set var="optionSelected" value="false"/>
					<c:if test="${item.identifyingValue == inputValue || (empty inputValue && empty item.identifyingValue)}"><c:set var="optionSelected" value="true"/></c:if>
					<c:if test="${optionSelected == 'true'}">${item.displayValue}<br/></c:if>
				</c:forEach>			
			 </c:set>
		 </c:if>
	</c:if>
	<c:set var="inputId" value="${status.expression}${suffix}" />
	<c:set var="inputId" value="${fn:replace(inputId,'[','__')}" />
	<c:set var="inputId" value="${fn:replace(inputId,']','__')}" />
	<c:set var="inputId" value="${fn:replace(inputId,'.displayValue','')}" />
	<c:set var="ampersand">'</c:set>
	<c:set var="inputId" value="${fn:replace(inputId,ampersand,'')}" />
	<c:set var="errorMessages" value="${status.errorMessages}" />
</spring:bind>
<c:set var="content">
	<widget:radioCore modeType="${modeType}" collection="${collection}"
		inputName="${inputName}" inputValue="${inputValue}" inputId="${inputId}"
		cssClass="${cssClass}" readonly="${readonly}"
		onchange="${onchange}" onkeyup="${onkeyup}" onmouseover="${onmouseover}" onclick="${onclick}"
		hideName="${hideName}" positioning="${positioning}"/>
</c:set>
<c:if test="${layoutMode=='div'}">
	<c:set var="label">
		<c:if test="${empty labelKey}"><c:set var="labelKey" value="label.${widget:findEndLabel(propertyPath)}"/></c:if>
		<c:set var="showRequired" value="${false}"></c:set>
		<c:if test="${required && modeType != 'display'}"><c:set var="showRequired" value="${true}"></c:set></c:if>
		<widget:label labelKey="${labelKey}" inputId="${inputId}" showRequired="${showRequired}"
			helpKey="${helpKey}" modeType="${modeType}"/>
	</c:set>
	<widget:line inputId="${inputId}" label="${label}" content="${content}" errorMessages="${errorMessages}" infoKey="${infoKey}" rightCols="${rightCols}" leftCols="${leftCols}"/>
</c:if>
<c:if test="${layoutMode=='none'}">${content}</c:if>