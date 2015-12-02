<%@ attribute name="test" required="true" type="java.lang.String"%>
<%@ attribute name="widgetList" type="java.util.Collection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget"%>
<c:set var="pageContext_" value="${pageContext}" scope="request"/>
<c:set var="test_" value="${test}" scope="request"/>
<%
javax.servlet.jsp.PageContext pageContext_=(javax.servlet.jsp.PageContext)request.getAttribute("pageContext_");
Object result=org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager.evaluate("evaluate",(String)request.getAttribute("test_"),Object.class,null,pageContext_);
request.setAttribute("result",result);
%>
<c:if test="${result}">
	<c:forEach items="${widgetList}" var="widgetIterator">
		<widget:widget widget="${widgetIterator}"/>
	</c:forEach>
</c:if>
