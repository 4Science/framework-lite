<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu" %>

<c:if test="${empty jsRules}"><c:set var="jsRules" value="it.cilea.core.menu.rhino.iterfaces.implementation.AddJsInfo"/></c:if>
<div id="menu_${item.id}" class="menu">
	<c:if test="${menu:isVisibleByPath(item,pageContext.request.requestURI) && menu:isVisibleByRole(item) && menu:isVisibleByRules(item,jsRules,pageContext.request)}">
		<c:if test="${not empty item.childrenList}">
			<c:forEach items="${item.childrenList}" var="child">
				<c:if test="${menu:isVisibleByPath(child,pageContext.request.requestURI) && menu:isVisibleByRole(child) && menu:isVisibleByRules(child,jsRules,pageContext.request)}">
					<c:set var="action"><c:if test="${not empty child.onClick}">action: '${child.onClick}',</c:if></c:set>
					<c:set var="icon"><c:if test="${not empty child.icon}">img: '${child.icon}',</c:if></c:set>
					<c:set var="cssClass">${action}${icon}</c:set>
					<c:if test="${fn:endsWith(cssClass,',')}">
						<c:set var="cssClass">${fn:substring(cssClass,0,fn:length(cssClass)-1)}</c:set>
					</c:if>
					<c:if test="${not empty child.childrenList}">
						<c:set var="cssClass">${cssClass}, menu: 'menu_${child.id}'</c:set>
					</c:if>				
					<a <c:if test="${not empty child.link}">href="${menu:buildUrl(child,pageContext.request)}" </c:if>class="{${cssClass}}">${child.displayValue}</a>
				</c:if>
			</c:forEach>
		</c:if>
		<c:if test="${empty item.childrenList}">
			<c:set var="action"><c:if test="${not empty item.onClick}">action: '${item.onClick}',</c:if></c:set>
			<c:set var="icon"><c:if test="${not empty item.icon}">img: '${item.icon}',</c:if></c:set>
			<c:set var="cssClass">${action}${icon}</c:set>
			<c:if test="${fn:endsWith(cssClass,',')}">
				<c:set var="cssClass">${fn:substring(cssClass,0,fn:length(cssClass)-1)}</c:set>
			</c:if>
			<a <c:if test="${not empty item.link}">href="${menu:buildUrl(item,pageContext.request)}" </c:if>class="{${cssClass}}">${item.displayValue}</a>
		</c:if>
	</c:if>
</div>
<c:forEach items="${item.childrenList}" var="child">
	<menu:mbItem item="${child}" jsRules="${jsRules}"/>
</c:forEach>
