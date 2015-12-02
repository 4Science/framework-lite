<%@ attribute name="text" required="true" type="java.lang.String"%>
<%@ attribute name="length" type="java.lang.Integer" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="_text" value="${text}"/>
<c:if test="${fn:length(text)>length}"><c:set var="_text">${fn:substring(text, 0, length)}<b class="tip" title="${fn:escapeXml(text)}">...</b></c:set></c:if>
${_text}