<%@ attribute name="identifier" required="true"	type="java.lang.String"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/view" prefix="view" %>
<%@ taglib uri="http://www.cineca.it/config" prefix="config" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<c:set var="currentViewBuilder" value="${view:get(identifier)}"/>

<c:forEach items="${currentViewBuilder.viewBuilderWidgetLinkSet}" var="viewBuilderWidgetLink" >
	<widget:widget widget="${viewBuilderWidgetLink.widget}" layoutMode="${viewBuilderWidgetLink.layoutMode}"/>
</c:forEach>
