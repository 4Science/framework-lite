<%@tag import="it.cilea.core.spring.CoreSpringConstant"%>
<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu"%>
<% request.setAttribute("_path",request.getAttribute(CoreSpringConstant.SPRING_ATTRIBUTE_URL).toString()); %>
<c:if test="${menu:isVisibleByPath(item,_path) && menu:isVisibleByRole(item) && menu:isVisibleByRules(item,jsRules,pageContext.request)}">
	<c:forEach items="${item.childrenList}" var="child">
		<c:if test="${menu:isVisibleByPath(child,_path) && menu:isVisibleByRole(child) && menu:isVisibleByRules(child,jsRules,pageContext.request)}">
			<c:if test="${empty child.childrenList}">
				<a id="navbar_a_${child.id}" class="btn btn-info" <c:if test="${not empty child.link}">href="${menu:buildUrl(child,pageContext.request)}"</c:if> <c:if test="${not empty child.onClick}">onclick="${child.onClick}"</c:if> >${child.displayValue}
				<c:if test="${not empty child.icon}"><i class="${child.icon}"></i></c:if>
				</a>
			</c:if>
		</c:if>
	</c:forEach>
</c:if>