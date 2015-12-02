<%@tag import="org.apache.commons.beanutils.PropertyUtils"%>
<%@ attribute name="paths" required="true"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget"%>
<c:set var="pathList" value="${widget:findPath(paths)}"/>
<c:forEach items="${pathList}" var="path">
	<c:set var="pathToReplace">$</c:set><c:set var="pathToReplace">${pathToReplace}{</c:set><c:set var="pathToReplace">${pathToReplace}${path}</c:set><c:set var="pathToReplace">${pathToReplace}}</c:set>
	<spring:bind path="${path}"><c:set var="paths" value="${fn:replace(paths,pathToReplace,status.value)}"/></spring:bind>
</c:forEach>
${paths}