<%@ attribute name="widget" required="true"	type="it.cilea.core.widget.model.Widget"%>
<%@ attribute name="modeType" %>
<%@ attribute name="layoutMode"%>
<%@ attribute name="errorMessages"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<c:if test="${empty layoutMode || layoutMode != 'none'}"><c:set var="layoutMode" value="div"/></c:if>
<c:if test="${empty modeType}"><c:set var="modeType" value="enabled"/></c:if>
<c:if test="${widget.isWidgetGranted}">
	<c:set var="contentHtml"><widget:widgetCore modeType="${modeType}" widget="${widget}" hasError="${not empty errorMessages}"/></c:set>
	<c:choose>
		<c:when test="${layoutMode eq 'none'}">${contentHtml}</c:when>
		<c:when test="${fn:startsWith(widget.widgetType,'command-') || widget.widgetType eq 'displayTag:table'|| widget.widgetType eq 'fieldset' || widget.widgetType eq 'div' || widget.widgetType eq 'tabs' || widget.widgetType eq 'tab' || widget.widgetType eq 'fragment' || widget.widgetType eq 'html' || widget.widgetType eq 'if' || widget.widgetType eq 'infoLine' || widget.widgetType eq 'include-jsp'}">${contentHtml}</c:when>
		<c:when test="${layoutMode eq 'div' and widget.widgetType eq 'hidden'}">${contentHtml}</c:when>
		<c:when test="${layoutMode eq 'div' and widget.widgetType eq 'server-side'}"></c:when>
		<c:when test="${layoutMode eq 'div'}">
			<c:set var="labelHtml"><widget:label label="${widget.label}" labelKey="" helpKey="" help="" inputId="${widget.requestAttributeName}" modeType="${modeType}" showRequired=""/></c:set>
			<c:set var="rightCols" value=""/>
			<c:set var="cssClass" value="${widget.cssClass}"/>
			<c:if test="${empty cssClass && widget.widgetType eq 'date'}"><c:set var="cssClass" value="date"/></c:if>
			<c:if test="${empty cssClass && widget.widgetType eq 'autocomplete'}"><c:set var="cssClass" value="third"/></c:if>
			<c:choose>
				<c:when test="${cssClass=='half'}"><c:set var="rightCols" value="5"/></c:when>
				<c:when test="${cssClass=='quarter'}"><c:set var="rightCols" value="3"/></c:when>
				<c:when test="${cssClass=='third'}"><c:set var="rightCols" value="4"/></c:when>
				<c:when test="${cssClass=='date'}">
					<c:if test="${not widget.rangeSearch}">
						<c:set var="rightCols" value="3"/>
					</c:if>
					<c:if test="${widget.rangeSearch}">
						<c:set var="rightCols" value="10"/>
					</c:if>
				</c:when>
				<c:when test="${cssClass=='number'}"><c:set var="rightCols" value="3"/></c:when>
			</c:choose>
			<widget:line inputId="${widget.requestAttributeName}" label="${labelHtml}" content="${contentHtml}"
				errorMessage=""
				errorMessages="${errorMessages}"
				infoKey=""
				infoMessage="${widget.info}"
				rightCols="${rightCols}"
				/>
		</c:when>
	</c:choose>
</c:if>