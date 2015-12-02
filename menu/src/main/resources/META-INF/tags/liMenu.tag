<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String"%>
<%@ attribute name="divId" required="false" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu"%>

<c:if test="${empty divId}"><c:set var="divId" value="ulMenu_${item.id}" /></c:if>

<ul id="${divId}" ${item.cssClass}>
	<c:forEach items="${item.childrenList}" var="child">
		<menu:liItem item="${child}" jsRules="${jsRules}"/>
	</c:forEach>
</ul>