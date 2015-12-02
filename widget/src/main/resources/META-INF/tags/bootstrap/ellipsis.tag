<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="divId" type="java.lang.String" required="true" %>
<%@ attribute name="text" type="java.lang.String" required="true" %>
<%@ attribute name="length" type="java.lang.Integer" %>
<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="height" type="java.lang.Integer" %>

<c:if test="${empty length}"><c:set var="length" value="50"/></c:if>
<c:if test="${empty width}"><c:set var="width" value="800"/></c:if>
<c:if test="${empty height}"><c:set var="height" value="400"/></c:if>

<c:if test="${fn:length(text) gt length}">
	<div id="${divId}" style="display:none;">${text}</div>
	<div id="${divId}Show">
		${fn:substring(text, 0, length)}<br/>
		[...] <a id="${divId}Show" 
			onclick="showDescriptionDialog('${divId}', ${width}, ${height});" 
			onmouseover="$('#'+this.id).css('cursor', 'pointer');"
			onmouseout="$('#'+this.id).css('cursor', 'default');">
			<img style="width:16px;" class="imgIcon" src="${documentViewImg}" title="<fmt:message key="label.showFullText"/>">
		</a>
	</div>
</c:if>
<c:if test="${fn:length(text) le length}">
	${text}
</c:if>
