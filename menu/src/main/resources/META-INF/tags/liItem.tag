<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu" %>

<c:if test="${empty jsRules}"><c:set var="jsRules" value="it.cilea.core.menu.rhino.iterfaces.implementation.AddJsInfo"/></c:if>

<c:if test="${menu:isVisibleByPath(item,pageContext.request.requestURI) && menu:isVisibleByRole(item) && menu:isVisibleByRules(item,jsRules,pageContext.request)}">
	<li <c:if test="${not empty item.childrenList}">class="liClass"</c:if>>
		<a <c:if test="${not empty item.link}">href="${menu:buildUrl(item,pageContext.request)}"</c:if> <c:if test="${not empty item.onClick}">onclick="${item.onClick}"</c:if> >${item.displayValue}</a>
		<c:if test="${not empty item.childrenList}">
			<ul>
				<c:forEach items="${item.childrenList}" var="child">
					<menu:liItem item="${child}" jsRules="${jsRules}"/>
				</c:forEach>
			</ul>
		</c:if>
	</li>
</c:if>