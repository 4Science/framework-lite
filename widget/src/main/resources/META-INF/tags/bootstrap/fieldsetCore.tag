<%@ attribute name="legendKey"%>
<%@ attribute name="widgetList" type="java.util.Collection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<fieldset>
	<legend><fmt:message key="${legendKey}"/></legend>
	<c:forEach items="${widgetList}" var="widgetIterator">
		<widget:widget widget="${widgetIterator}"/>
	</c:forEach>
</fieldset>