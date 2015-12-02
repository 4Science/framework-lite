<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu" %>

<c:if test="${empty jsRules}"><c:set var="jsRules" value="it.cilea.core.menu.rhino.iterfaces.implementation.AddJsInfo"/></c:if>
	<c:if test="${not empty item.childrenList}">
		<li><span>${item.displayValue}</span><c:if test="${not empty item.icon}"><i class="${item.icon}"></i></c:if>
        	<ul style="display: none;">
				<c:forEach items="${item.childrenList}" var="child">        	
					<menu:jstree3Item item="${child}" jsRules="${jsRules}"/>
				</c:forEach>
			</ul>
		</li>
	</c:if>
	<c:if test="${empty item.childrenList}">
		<c:set var="onclick"></c:set>
		<c:if test="${not empty item.link}"><c:set var="onclick">location.href='${menu:buildUrl(item,pageContext.request)}'</c:set></c:if>
		<c:if test="${not empty item.onClick}"><c:set var="onclick">location.href='${menu:buildUrl(item,pageContext.request)}'</c:set></c:if>
		<li onclick="${onclick}">${item.displayValue}<c:if test="${not empty item.icon}"><i class="${item.icon}"></i></c:if></li>
	</c:if>
