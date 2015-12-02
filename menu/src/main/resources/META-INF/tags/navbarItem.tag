<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String" %>
<%@ attribute name="top" type="java.lang.Boolean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu" %>

<c:if test="${empty jsRules}"><c:set var="jsRules" value="it.cilea.core.menu.rhino.iterfaces.implementation.AddJsInfo"/></c:if>
<c:if test="${menu:isVisibleByPath(item,pageContext.request.requestURI) && menu:isVisibleByRole(item) && menu:isVisibleByRules(item,jsRules,pageContext.request)}">
	<c:set var="hasChildren" value="${false}"/>
	<c:if test="${not empty item.childrenList}">
		<c:forEach items="${item.childrenList}" var="child">
			<c:if test="${menu:isVisibleByPath(child,pageContext.request.requestURI) && menu:isVisibleByRole(child) && menu:isVisibleByRules(child,jsRules,pageContext.request)}">
				<c:set var="hasChildren" value="${true}"/>
			</c:if>
		</c:forEach>
	</c:if>

	<c:if test="${hasChildren}">
		<c:if test="${top}">
			<li class="dropdown-submenu"><a id="navbar_a_${item.id}" href="#" class="dropdown-toggle" data-toggle="dropdown">${item.displayValue}</a>
	        	<ul class="dropdown-menu">
		</c:if>
		<c:if test="${!top}">
			<li><a id="navbar_a_${item.id}" href="#" class="expand"><span>${item.displayValue}</span><c:if test="${not empty item.icon}"><i class="${item.icon}"></i></c:if></a>
	        	<ul style="display: none;">
		</c:if>
		<c:forEach items="${item.childrenList}" var="child">        	
			<menu:navbarItem top="${top}" item="${child}" jsRules="${jsRules}"/>
		</c:forEach>
				</ul>
			</li>
	</c:if>
	<c:if test="${not hasChildren}">
		<li><a id="navbar_a_${item.id}" <c:if test="${not empty item.link}">href="${menu:buildUrl(item,pageContext.request)}"</c:if> <c:if test="${not empty item.onClick}">onclick="${item.onClick}"</c:if> >${item.displayValue}
		<c:if test="${not empty item.icon}"><i class="${item.icon}"></i></c:if></a>
		</li>
	</c:if>
</c:if>