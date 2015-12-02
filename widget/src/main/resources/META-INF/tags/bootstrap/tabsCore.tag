<%@ attribute name="widgetList" type="java.util.Collection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<div id="tabs">
	<ul>
		<c:forEach items="${widgetList}" var="widgetIterator" varStatus="status">
			<li><a href="#tabs-${status.index}"><fmt:message key="${widgetIterator.labelKey}"/></a></li>
		</c:forEach>
	</ul>
	<c:forEach items="${widgetList}" var="widgetIterator" varStatus="status">
		<div id="tabs-${status.index}">
			<widget:widget widget="${widgetIterator}"/>
		</div>
	</c:forEach>
</div>
<script type="text/javascript">
  JQ(document).ready(function(){
	jqueryToBootstrapTabInitializer('#tabs',0);
  });
</script>
