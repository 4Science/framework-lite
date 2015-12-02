<%@tag import="org.springframework.beans.NullValueInNestedPathException"%>
<%@tag import="it.cilea.core.widget.model.OptionsWidget"%>
<%@tag import="it.cilea.core.widget.model.Widget"%>
<%@ attribute name="widget" required="true"	type="it.cilea.core.widget.model.Widget"%>
<%@ attribute name="modeType" %>
<%@ attribute name="hasError" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display-el" %>
<c:if test="${empty modeType}"><c:set var="modeType" value="enabled"/></c:if>

<c:if test="${widget.isWidgetGranted}">
<%
	Widget widget = (Widget) jspContext.getAttribute("widget");
	String[] requestValues=widget.getRequestValues(request);
	if (requestValues==null || requestValues.length==0){
		jspContext.setAttribute("requestValues","");
		jspContext.setAttribute("requestValues1","");
		jspContext.setAttribute("requestValues2","");
	}else if (requestValues!=null && requestValues.length==1){
		jspContext.setAttribute("requestValues",requestValues[0]);
		}else {
		jspContext.setAttribute("requestValues",requestValues);
		if (requestValues!=null && requestValues.length==2){
			jspContext.setAttribute("requestValues1",requestValues[0]);
			jspContext.setAttribute("requestValues2",requestValues[1]);
		}
	}
	if (widget instanceof OptionsWidget)
		jspContext.setAttribute("collection",((OptionsWidget) widget).getCollection(request));
%>
	<c:choose>
		<c:when test="${widget.widgetType eq 'date'}">
			<c:set var="cssClass" value="${widget.cssClass}"/>
			<c:if test="${empty cssClass}"><c:set var="cssClass" value="date"/></c:if>
			<c:if test="${not widget.rangeSearch}">
			<widget:textCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${cssClass}"
				password="" 
				autocompleteData="" autocompleteGetData="" autocompleteGetUrl="" autocompleteInputName="" autocompleteInputValue="" autocompleteUrl=""
				isDate="true" isTime="" calendarFutureOnly=""
				isEuro="" isMoney="" isPercent="" allowNegativeNumbers="" moneyPattern=""
				hasError="${hasError}"
				maxlength="${widget.maxLength}" showCounter=""
				onchange="" onkeyup=""
				readonly=""
				isHidden="false"
				/>
			</c:if>
			<c:if test="${widget.rangeSearch}">
			<div class="line-label col-sm-1"  style="padding-left: 0px;" ><label class="control-label"><span class="line-label-text"><fmt:message key="label.from"/></span></label></div>
			<div class="line-content col-sm-4" >
			<widget:textCore modeType="${modeType}"
				inputId="1_${widget.requestAttributeName}" 
				inputName="1_${widget.requestAttributeName}"
				inputValue="${requestValues1}"
				hideName=""
				cssClass="${cssClass}"
				password="" 
				autocompleteData="" autocompleteGetData="" autocompleteGetUrl="" autocompleteInputName="" autocompleteInputValue="" autocompleteUrl=""
				isDate="true" isTime="" calendarFutureOnly=""
				isEuro="" isMoney="" isPercent="" allowNegativeNumbers="" moneyPattern=""
				hasError="${hasError}"
				maxlength="${widget.maxLength}" showCounter=""
				onchange="" onkeyup=""
				readonly=""
				isHidden="false"
				/>
			</div>
			<div class="line-label col-sm-1" ><label class="control-label"><span class="line-label-text"><fmt:message key="label.to"/></span></label></div>
			<div class="line-content col-sm-4" >
			<widget:textCore modeType="${modeType}"
				inputId="2_${widget.requestAttributeName}" 
				inputName="2_${widget.requestAttributeName}"
				inputValue="${requestValues2}"
				hideName=""
				cssClass="${cssClass}"
				password="" 
				autocompleteData="" autocompleteGetData="" autocompleteGetUrl="" autocompleteInputName="" autocompleteInputValue="" autocompleteUrl=""
				isDate="true" isTime="" calendarFutureOnly=""
				isEuro="" isMoney="" isPercent="" allowNegativeNumbers="" moneyPattern=""
				hasError="${hasError}"
				maxlength="${widget.maxLength}" showCounter=""
				onchange="" onkeyup=""
				readonly=""
				isHidden="false"
				/>
			</div>
			</c:if>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-date'}">
			<c:set var="cssClass" value="${widget.cssClass}"/>
			<c:if test="${empty cssClass}"><c:set var="cssClass" value="date"/></c:if>
			<c:if test="${not widget.rangeSearch}">
			<widget:text modeType="${modeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				cssClass="${cssClass}"
				required="${widget.required}"
				suffixIdOnly="${widget.suffixIdOnly}"
				allowMultiple="${widget.allowMultiple}"
				widgetType="${widget.widgetType}"
				multipleIndexToShow="${widget.multipleIndexToShow}"
				/>
			</c:if>
			<c:if test="${widget.rangeSearch}">
				NOT IMPLEMENTED YET
			</c:if>
		</c:when>
		<c:when test="${widget.widgetType eq 'text'}">
			<widget:textCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${widget.cssClass}"
				password="" 
				autocompleteData="" autocompleteGetData="" autocompleteGetUrl="" autocompleteInputName="" autocompleteInputValue="" autocompleteUrl=""
				isDate="" isTime="" calendarFutureOnly=""
				isEuro="" isMoney="" isPercent="" allowNegativeNumbers="" moneyPattern=""
				hasError="${hasError}"
				maxlength="${widget.maxLength}" showCounter="${widget.showCounter}"
				onchange="" onkeyup=""
				readonly=""
				isHidden="false"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-text'}">
			<c:set var="commandTextModeType" value="${modeType}"/>
			<c:if test="${not empty widget.modeType}">
				<c:set var="commandTextModeType" value="${widget.modeType}"/>
			</c:if>
			<widget:text modeType="${commandTextModeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				cssClass="${widget.cssClass}"
				showI18n="${widget.showI18n}"
				required="${widget.required}"
				suffixIdOnly="${widget.suffixIdOnly}"
				isMoney="${widget.isMoney}"
				allowMultiple="${widget.allowMultiple}"
				widgetType="${widget.widgetType}"
				multipleIndexToShow="${widget.multipleIndexToShow}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-textList'}">
			<c:set var="commandTextModeType" value="${modeType}"/>
			<c:if test="${not empty widget.modeType}">
				<c:set var="commandTextModeType" value="${widget.modeType}"/>
			</c:if>
			<widget:textList modeType="${commandTextModeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				cssClass="${widget.cssClass}"
				showI18n="${widget.showI18n}"
				required="${widget.required}"
				suffixIdOnly="${widget.suffixIdOnly}"
				isMoney="${widget.isMoney}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'autocomplete'}">
			<c:set var="cssClass" value="${widget.cssClass}"/>
			<c:if test="${empty cssClass}"><c:set var="cssClass" value="third"/></c:if>
			<widget:textCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${cssClass}"
				password="" 
				autocompleteUrl="${widget.autocompleteUrl}" autocompleteData="${widget.autocompleteData}" autocompleteGetUrl="${widget.autocompleteGetUrl}" autocompleteGetData="${widget.autocompleteGetData}" autocompleteInputName="${widget.autocompleteInputName}" autocompleteInputValue="${widget.autocompleteInputValue}"
				isDate="" isTime="" calendarFutureOnly=""
				isEuro="" isMoney="" isPercent="" allowNegativeNumbers="" moneyPattern=""
				hasError="${hasError}"
				maxlength="" showCounter=""
				onchange="" onkeyup=""
				readonly=""
				isHidden="false"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-autocomplete'}">
			<c:set var="cssClass" value="${widget.cssClass}"/>
			<c:if test="${empty cssClass}"><c:set var="cssClass" value="third"/></c:if>
			<widget:text modeType="${modeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				infoKey="${widget.infoKey}"
				cssClass="${cssClass}"
				required="${widget.required}"
				autocompleteUrl="${widget.autocompleteUrl}" 
				autocompleteData="${widget.autocompleteData}" 
				autocompleteGetUrl="${widget.autocompleteGetUrl}" 
				autocompleteGetData="${widget.autocompleteGetData}" 
				autocompleteInputName="${widget.autocompleteInputName}" 
				autocompleteInputValue="${widget:evaluateExpression(widget.autocompleteInputValue,pageContext)}"
				allowMultiple="${widget.allowMultiple}"
				widgetType="${widget.widgetType}"
				multipleIndexToShow="${widget.multipleIndexToShow}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'textarea'}">
			<widget:textareaCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${widget.cssClass}"
				hasError="${hasError}"
				maxlength="${widget.maxLength}" showCounter="${widget.showCounter}"
				onchange="" onkeyup=""
				readonly=""
				ckEditor=""
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-textarea'}">
			<widget:textarea modeType="${modeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				cssClass="${cssClass}"
				maxlength="${widget.maxLength}" showCounter="${widget.showCounter}"
				onchange="" onkeyup=""
				readonly=""
				ckEditor="${widget.ckEditor}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'hidden'}">
			<widget:textCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${widget.cssClass}"
				password="" 
				autocompleteData="" autocompleteGetData="" autocompleteGetUrl="" autocompleteInputName="" autocompleteInputValue="" autocompleteUrl=""
				isDate="" isTime="" calendarFutureOnly=""
				isEuro="" isMoney="" isPercent="" allowNegativeNumbers="" moneyPattern=""
				hasError="${hasError}"
				maxlength="" showCounter=""
				onchange="" onkeyup=""
				readonly=""
				isHidden="true"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-hidden'}">
			<widget:hidden
				propertyPath="${widget.modelAttributeName}" 
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'select' || widget.widgetType eq 'multi-select'  || widget.widgetType eq 'select2' }">
			<widget:selectCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${widget.cssClass}"
				collection="${collection}"
				size="${widget.size}" multiple="${widget.multipleSelection}"
				onchange="" onkeyup="" onmouseover=""
				hasError="${hasError}"
				readonly=""
				type="${widget.widgetType}"
				/>
			<c:if test="${not empty widget.parentWidget}">
				<script type="text/javascript">
					JQ('#${widget.parentWidget.requestAttributeName}').change(function() {
						refreshOptions(
								'${widget.parentWidget.requestAttributeName}',
								'${widget.requestAttributeName}',
								'${widget.ajaxUrlReplaced}'
							<c:if test="${widget.renderEmptyOption}">
								,'true'
							</c:if>
						)
					});
				</script>
			</c:if>
			<c:if test="${empty widget.parentWidget and fn:length(widget.childWidgetSet)>0}">
				<script type="text/javascript">
					JQ(function(){
						JQ('#${widget.requestAttributeName}').change();
					});
				</script>
			</c:if>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-select' || widget.widgetType eq 'command-multi-select'  || widget.widgetType eq 'command-select2' }">
			<widget:select modeType="${modeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				hideName=""
				cssClass="${widget.cssClass}"
				collection="${collection}"
				size="${widget.size}" multiple="${widget.multipleSelection}"
				onchange="" onkeyup="" onmouseover=""
				readonly=""
				type="${fn:replace(widget.widgetType,'command-','')}"
				/>
			<c:if test="${not empty widget.parentWidget}">
				<script type="text/javascript">
					JQ('#${widget.parentWidget.requestAttributeName}').change(function() {
						refreshOptions(
								'${widget.parentWidget.requestAttributeName}',
								'${widget.requestAttributeName}',
								'${widget.ajaxUrlReplaced}'
							<c:if test="${widget.renderEmptyOption}">
								,'true'
							</c:if>
						)
					});
				</script>
			</c:if>
			<c:if test="${empty widget.parentWidget and fn:length(widget.childWidgetSet)>0}">
				<script type="text/javascript">
					JQ(function(){
						JQ('#${widget.requestAttributeName}').change();
					});
				</script>
			</c:if>
		</c:when>
		<c:when test="${widget.widgetType eq 'checkbox'}">
			<widget:checkboxCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${widget.cssClass}"
				collection="${collection}"
				onchange="" onkeyup="" onmouseover=""
				positioning=""
				readonly=""
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-checkbox'}">
			<widget:checkbox modeType="${modeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				positioning="${widget.positioning}"
				cssClass="${widget.cssClass}"
				collection="${collection}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'radio'}">
			<widget:radioCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${widget.cssClass}"
				collection="${collection}"
				onchange="" onkeyup="" onmouseover=""
				positioning="${widget.positioning}"
				readonly=""
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-radio'}">
			<widget:radio modeType="${modeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				positioning="${widget.positioning}"
				cssClass="${widget.cssClass}"
				collection="${collection}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'selectAndMove' || widget.widgetType eq 'selectAndMove-horizontal' || widget.widgetType eq 'selectAndMove-vertical' }">
			<widget:selectCore modeType="${modeType}"
				inputId="${widget.requestAttributeName}" 
				inputName="${widget.requestAttributeName}"
				inputValue="${requestValues}"
				hideName=""
				cssClass="${widget.cssClass}"
				collection="${collection}"
				size="${widget.size}" multiple="${widget.multipleSelection}"
				onchange="" onkeyup="" onmouseover=""
				hasError="${hasError}"
				readonly=""
				type="${widget.widgetType}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'command-selectAndMove' || widget.widgetType eq 'command-selectAndMove-horizontal' || widget.widgetType eq 'command-selectAndMove-vertical' }">
			<widget:select modeType="${modeType}"
				propertyPath="${widget.modelAttributeName}" 
				labelKey="${widget.labelKey}"
				cssClass="${widget.cssClass}"
				collection="${collection}"
				size="${widget.size}" multiple="${widget.multipleSelection}"
				onchange="" onkeyup="" onmouseover=""
				readonly=""
				type="${fn:replace(widget.widgetType,'command-','')}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'server-side'}">
		</c:when>
		<c:when test="${widget.widgetType eq 'fieldset'}">
			<widget:fieldsetCore legendKey="${widget.labelKey}"
				widgetList="${widget.childWidgetSetFromLink}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'if'}">
			<widget:if test="${widget.test}"
				widgetList="${widget.childWidgetSetFromLink}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'infoLine'}">
			<widget:infoLine infoKey="${widget.infoKey}" />
		</c:when>
		<c:when test="${widget.widgetType eq 'div'}">
			<widget:divCore widgetList="${widget.childWidgetSetFromLink}"/>
		</c:when>
		
		<c:when test="${widget.widgetType eq 'tabs'}">
			<widget:tabsCore widgetList="${widget.childWidgetSetFromLink}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'tab'}">
			<widget:tabCore widgetList="${widget.childWidgetSetFromLink}"
				/>
		</c:when>
		<c:when test="${widget.widgetType eq 'displayTag:table'}">
			<display:table name="${displayTagData}" cellspacing="${widget.cellspacing}" cellpadding="${widget.cellpadding}" requestURI="${widget.requestURI}" id="${widget.rowId}" class="${widget.cssClass}">
				<c:set var="element" value="${element}" scope="request"/>
				<c:if test="${widget.isFragmentTable}">
					<display:setProperty name="paging.banner.onepage" value=""/>
					<display:setProperty name="paging.banner.all_items_found" value=""/>
					<display:setProperty name="paging.banner.all_items_found" value=""/>
					<display:setProperty name="paging.banner.one_item_found" value=""/>
				</c:if>
				<c:set var="columnNumber" value="${0}"/>
				<c:forEach items="${widget.childWidgetSetFromLink}" var="child">
					<c:choose>
						<c:when test="${not empty child.childWidgetSetFromLink}">
							<display:column titleKey="${child.labelKey}" decorator="${child.decoratorClass}" style="${child.style}" sortable="${child.sortable}" sortProperty="${child.sortProperty}" media="${child.media}">
								<c:forEach items="${child.childWidgetSetFromLink}" var="childChild">
									<c:if test="${childChild.widgetType eq 'html'}">
										<c:if test="${not empty childChild.expression}">${widget:evaluateExpression(childChild.expression,pageContext)}</c:if>
										<c:if test="${not empty childChild.script}">${widget:evalScript(childChild.script,element)}</c:if>
									</c:if>
									<c:if test="${not childChild.widgetType eq 'html'}">
										<widget:widget widget="${childChild}"/>
									</c:if>
								</c:forEach>
							</display:column>
						</c:when>
						<c:when test="${not empty child.script}">
							<display:column titleKey="${child.labelKey}" decorator="${child.decoratorClass}" style="${child.style}" sortable="${child.sortable}" sortProperty="${child.sortProperty}" media="${child.media}">${widget:evalScript(child.script,element)}</display:column>
						</c:when>
						<c:when test="${not fn:contains(child.modelAttributeName,'[')}">
							<display-el:column titleKey="${child.labelKey}" decorator="${child.decoratorClass}" property="${child.modelAttributeName}" style="${child.style}" sortable="${child.sortable}" sortProperty="${child.sortProperty}" media="${child.media}"/>
						</c:when>
						<c:otherwise>
							<display:column titleKey="${child.labelKey}" decorator="${child.decoratorClass}" style="${child.style}" sortable="${child.sortable}" sortProperty="${child.sortProperty}" media="${child.media}"><% try {  %><spring:bind path="${child.modelAttributeName}">${status.value}</spring:bind><% } catch (NullValueInNestedPathException e ) {} %></display:column>
						</c:otherwise>
					</c:choose>
					<c:set var="columnNumber" value="${columnNumber+1}"/>
				</c:forEach>
				<c:if test="${widget.isFragmentTable && (empty modeType || modeType == 'enabled')}">
					<display:column titleKey="label.operations" media="html" style="width:10%;" sortable="false">
						<i class="fa fa-pencil" title="<fmt:message key="prompt.edit" />" onclick="fragmentElementForm('${fragmentDivId}','${element.id}','${element.uniqueIdentifier}')" style="cursor:pointer"></i>&nbsp;&nbsp;
						<i class="fa fa-trash-o" title="<fmt:message key="prompt.delete" />" onclick="fragmentElementDelete('${fragmentDivId}', '<fmt:message key="prompt.delete.confirm" />','${element.id}','${element.uniqueIdentifier}')" style="cursor:pointer"></i>&nbsp;&nbsp;
					</display:column>
					<display:footer>
						<tr>
							<th colspan="${columnNumber}"></th>
							<th>
								<i class="fa fa-plus-square" title="<fmt:message key="prompt.add.new" />" onclick="fragmentElementAdd('${fragmentDivId}')" style="cursor:pointer"></i>&nbsp;&nbsp;
							</th>
						</tr>
					</display:footer>
				</c:if>
			</display:table>
		</c:when>
		<c:when test="${widget.widgetType eq 'html'}">
			<c:if test="${not empty widget.script}"><% request.setAttribute("request__", request); %>${widget:evalScript(widget.script,request__)}</c:if>
			<c:if test="${not empty widget.expression}">${widget:evaluateExpression(widget.expression,pageContext)}</c:if>
		</c:when>
		<c:when test="${widget.widgetType eq 'include-jsp'}">
			<widget:include page="${widget.page}"/>
		</c:when>
	</c:choose>
</c:if>