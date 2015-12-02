<%@tag import="it.cilea.core.spring.CoreSpringConstant"%>
<%@ attribute name="servletPath" required="true"	type="java.lang.String"%>
<%@ attribute name="ajax" required="false"	type="java.lang.Boolean"%>
<%@ attribute name="formDivId" required="false"	type="java.lang.String"%>
<%@ attribute name="listDivId" required="false"	type="java.lang.String"%>
<%@ attribute name="listUrl" required="false"	type="java.lang.String"%>
<%@ attribute name="buttonList" required="false"	type="java.lang.String"%>
<%@ attribute name="addFieldsetHeader" required="false"	type="java.lang.Boolean"%>
<%@tag import="it.cilea.core.configuration.util.ConfigurationUtil"%>
<%@tag import="it.cilea.core.widget.model.impl.core.TextWidget"%>
<%@tag import="it.cilea.core.widget.model.Widget"%>
<%@tag import="java.util.Set"%>
<%@tag import="it.cilea.core.search.model.SearchBuilder"%>
<%@tag import="java.util.Map"%>
<%@tag import="it.cilea.core.search.SearchConstant"%>
<%@tag import="it.cilea.core.search.model.SearchBuilderWidgetLink"%>
<%@tag import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${empty ajax}"><c:set var="ajax" value="${false}"/></c:if>
<c:if test="${empty formDivId}"><c:set var="formDivId" value="widgetSearchForm"/></c:if>
<c:if test="${empty listDivId}"><c:set var="listDivId" value=""/></c:if>
<c:if test="${empty buttonList}"><c:set var="buttonList" value="button.search|submit"/></c:if>
<c:if test="${empty addFieldsetHeader}"><c:set var="addFieldsetHeader" value="${false}"/></c:if>
<c:set var="servletUrl" value="${servletPath}" />
<%
	Map<String, SearchBuilder> searchBuilderMap = SearchConstant.searchBuilderMap;
	String fullPath = request.getAttribute("javax.servlet.forward.request_uri").toString();
	String searchBuilderId = request.getAttribute(CoreSpringConstant.SPRING_ATTRIBUTE_URL).toString();
	SearchBuilder searchBuilder=searchBuilderMap.get(searchBuilderId);	
	jspContext.setAttribute("currentSearchBuilder", searchBuilder);		
	jspContext.setAttribute("fullPath", fullPath);
	jspContext.setAttribute("searchBuilderId", searchBuilderId);
	jspContext.setAttribute("sectionSelect", SearchConstant.SECTION_SELECT);
	jspContext.setAttribute("sectionFilter", SearchConstant.SECTION_FILTER);
	jspContext.setAttribute("sectionGroup", SearchConstant.SECTION_GROUP);
	if (StringUtils.isNotBlank(searchBuilder.getSearchBuilderParameterMap().get("BUTTON_LIST")))
		jspContext.setAttribute("buttonList", ConfigurationUtil.replaceText(searchBuilder.getSearchBuilderParameterMap().get("BUTTON_LIST")));
%>
<c:if test="${not empty currentSearchBuilder}">
	<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionSelect]}">
		<c:set var="styleSelectFieldset"> style="display: none;"</c:set>
		<c:forEach items="${currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionSelect]}" var="searchBuilderWidgetLink" >
			<c:if test="${!widget:isInvisibleWidget(searchBuilderWidgetLink.widget)}">
				<c:set var="styleSelectFieldset"> style="display: visible;"</c:set>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionFilter]}">
		<c:set var="styleFilterFieldset"> style="display: none;"</c:set>
		<c:forEach items="${currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionFilter]}" var="searchBuilderWidgetLink" >
			<c:if test="${!widget:isInvisibleWidget(searchBuilderWidgetLink.widget)}">
				<c:set var="styleFilterFieldset"> style="display: visible;"</c:set>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionGroup]}">
		<c:set var="styleGroupFieldset"> style="display: none;"</c:set>
		<c:forEach items="${currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionGroup]}" var="searchBuilderWidgetLink" >
			<c:if test="${!widget:isInvisibleWidget(searchBuilderWidgetLink.widget)}">
				<c:set var="styleGroupFieldset"> style="display: visible;"</c:set>
			</c:if>
		</c:forEach>
	</c:if>

	<c:if test="${addFieldsetHeader&&(fn:contains(styleSelectFieldset,'visible')||fn:contains(styleFilterFieldset,'visible')||fn:contains(styleGroupFieldset,'visible'))}">	
		<fieldset class="searchFieldset max1024">
			<h2><fmt:message key="title.filter"/></h2>
	</c:if>
	
	<form action="${fullPath}" method="get" id="${formDivId}"  onsubmit="javascript:checkAllSelectMovable();" class="form-horizontal">
		<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionSelect]}">
			<fieldset ${styleSelectFieldset}>
		</c:if>
		<c:forEach items="${currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionSelect]}" var="searchBuilderWidgetLink" >
			<widget:widget widget="${searchBuilderWidgetLink.widget}" />
		</c:forEach>
		<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionSelect]}">
			</fieldset>
		</c:if>

		<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionFilter]}">
			<fieldset ${styleFilterFieldset}>
		</c:if>
		<c:forEach items="${currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionFilter]}" var="searchBuilderWidgetLink" >
			<widget:widget widget="${searchBuilderWidgetLink.widget}" />
		</c:forEach>
		<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionFilter]}">
			</fieldset>
		</c:if>
		
		<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionGroup]}">
			<fieldset ${styleGroupFieldset}>
		</c:if>
		<c:forEach items="${currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionGroup]}" var="searchBuilderWidgetLink" >
			<widget:widget widget="${searchBuilderWidgetLink.widget}" />
		</c:forEach>
		<c:if test="${not empty currentSearchBuilder.searchBuilderWidgetLinkSetBySectionMap[sectionGroup]}">
			</fieldset>
		</c:if>

		<input type="hidden" name="posting" value="1"/>
		<c:if test="${not empty param.sort}">
			<input type="hidden" name="sort" value="${param.sort}"/>
		</c:if>
		<c:if test="${not empty param.dir}">
			<input type="hidden" name="dir" value="${param.dir}"/>
		</c:if>
		<c:set var="styleButtonSearchdiv"> style="display: none;"</c:set>
		<c:if test="${fn:contains(styleSelectFieldset,'visible')||fn:contains(styleFilterFieldset,'visible')||fn:contains(styleGroupFieldset,'visible')}">
				<c:set var="styleButtonSearchdiv"> style="display: visible;"</c:set>
		</c:if>
		<div class="form-actions text-right" ${styleButtonSearchdiv}>
			<c:if test="${not ajax}">
				<c:forEach items="${fn:split(buttonList,',')}" var="button">
					<c:set var="labelValue"><fmt:message key="${fn:split(button,'|')[0]}"/></c:set>
					<c:set var="typeValue">${fn:split(button,'|')[1]}</c:set>
					<c:set var="urlValue"></c:set>
					<c:if test="${fn:length(fn:split(button,'|'))>2}"><c:set var="urlValue">${fn:split(button,'|')[2]}</c:set></c:if>
					<c:if test="${typeValue=='submit'}">
						<c:set var="waitExportMessage"><fmt:message key="info.search.mediaType.wait"/></c:set>
						<c:set var="waitExportMessage" value="${widget:escapeForJavascriptString(waitExportMessage)}"/>
						<c:choose>
							<c:when test="${empty currentSearchBuilder.mediaTypes}">
								<input type="submit" value="${labelValue}" class="btn btn-primary"/>
							</c:when>
							<c:when test="${fn:length(currentSearchBuilder.mediaTypes)==1}">
								<c:forEach items="${currentSearchBuilder.mediaTypes}" var="mediaType">
									<c:if test="${mediaType=='html'}"><input type="submit" value="<fmt:message key="button.search.${mediaType}"/>" class="btn btn-primary"/></c:if>
									<c:if test="${mediaType!='html'}"><input type="button" value="<fmt:message key="button.search.${mediaType}"/>" class="btn btn-primary" onclick="submitSearchBuilderNewMediaType('${formDivId}','${mediaType}','${waitExportMessage}');"/></c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="btn-group">
									<c:if test="${currentSearchBuilder.mediaTypes[0]=='html'}"><input type="submit" value="<fmt:message key="button.search.${currentSearchBuilder.mediaTypes[0]}"/>" class="btn btn-primary"/></c:if>
									<c:if test="${currentSearchBuilder.mediaTypes[0]!='html'}"><input type="button" value="<fmt:message key="button.search.${currentSearchBuilder.mediaTypes[0]}"/>" class="btn btn-primary" onclick="submitSearchBuilderNewMediaType('${formDivId}','${currentSearchBuilder.mediaTypes[0]}','${waitExportMessage}');"/></c:if>
									<button class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><fmt:message key="button.search.export"/></button>
									<ul class="dropdown-menu dropdown-menu-right">
										<c:forEach items="${currentSearchBuilder.mediaTypes}" var="mediaType" begin="1">
											<li>
												<c:if test="${mediaType=='html'}"><a onclick="submitSearchBuilderNewMediaType('${formDivId}','','${waitExportMessage}');"><fmt:message key="button.search.${mediaType}"/></a></c:if>
												<c:if test="${mediaType!='html'}"><a onclick="submitSearchBuilderNewMediaType('${formDivId}','${mediaType}','${waitExportMessage}');"><fmt:message key="button.search.${mediaType}"/></a></c:if>
											</li>
										</c:forEach>
									</ul>
								</div>							
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${typeValue=='export'}">
						<input type="button" value="${labelValue}" class="btn btn-primary" onclick="submitSearchBuilderExportView('${formDivId}','${urlValue}');"/>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${ajax}">
				<input type="button" value="<fmt:message key="button.search"/>" class="btn btn-primary" onclick="submitSearchBuilderAjax('${formDivId}','${listDivId}','${listUrl}');"/>
				<c:if test="${not empty param.posting}">
				<script type="text/javascript">
					$().ready(function(){ 
						submitSearchBuilderAjax('${formDivId}','${listDivId}','${listUrl}');
					}); 
					$('#${formDivId}').keydown( function(e) {
						var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
						if(key == 13) {
							e.preventDefault();
						}
					});
				</script>
				</c:if>
			</c:if>
		</div>
	</form>
		<c:if test="${addFieldsetHeader&&(fn:contains(styleSelectFieldset,'visible')||fn:contains(styleFilterFieldset,'visible')||fn:contains(styleGroupFieldset,'visible'))}">
		</fieldset>
	</c:if>
</c:if>