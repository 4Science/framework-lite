<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${not empty facetData}">
	<div class="facetHeading"><fmt:message key="label.refineFacetSearch"/></div>
</c:if>

<c:if test="${not empty removeFacetData}">
<div id="removeFacet">					
		<div class="facetRemoveHeading"><fmt:message key="label.removeFacet"/></div>
		<ul>
			<c:forEach items="${removeFacetData}" var="facet">
				<c:set var="facetValue">
					${fn:replace(fn:substring(facet.key,fn:indexOf(facet.key,':')+1, fn:length(facet.key)), "\"", "")}
				</c:set>
				
				<li class="removeFacet removeFacet${fn:toUpperCase(fn:substring(facetValue, 0, 1))}${fn:toLowerCase(fn:substring(facetValue, 1,fn:length(facetValue)))}">
					<a href="?${facet.value}">
					<fmt:message key="label.facet${fn:substring(facet.key,0,fn:indexOf(facet.key,':'))}"/>:
					${facetValue}
					<%--${fn:replace(fn:substring(facet.key,fn:indexOf(facet.key,':')+1, fn:length(facet.key)), "\"", "")} --%>					
					</a></li>
			</c:forEach> 
		</ul>
</div>
</c:if>		
<c:set var="visibleFacetNumber" value="${visibleFacetNumber}"/>

<script>
function showAllFacet(id){
	$('#'+id).children().show();
	$('#'+id+'Link').html("<a href=\"javascript:void(showInitialFacet('"+id+"'));\"><fmt:message key="label.facetLess"/></a>");	
}

function showInitialFacet(id){	
	var children=$('#'+id).children().filter(
		function (index) {
	          return $(this).attr("furtherValue") == "true";
	        }		
	).hide();

	$('#'+id+'Link').html("<a href=\"javascript:void(showAllFacet('"+id+"'));\"><fmt:message key="label.facetMore"/></a>");
}
</script>
<div id="addFacet">
	<c:forEach items="${facetData}" var="facet">
		<div class="facetAddHeading"><fmt:message key="label.facet${facet.key}"/></div>
		<ul id="${facet.key}">
			<c:set var="counter" value="1"/>
			<c:set var="hiddenItems" value="${false}"/>
			<c:forEach items="${facetData[facet.key]}" var="facetValue">				
				<c:if test="${counter <= visibleFacetNumber}">
					<c:set var="style" value="display:block"/>
					<c:set var="furtherValue" value="false"/>
				</c:if>
				
				<c:if test="${counter > visibleFacetNumber}">
					<c:set var="hiddenItems" value="${true}"/>
					<c:set var="style" value="display:none"/>
					<c:set var="furtherValue" value="true"/>
				</c:if>
								
				<li style="${style}" furtherValue="${furtherValue}" class="capitalize ${facetValue.value.value}"><a href="?${facetValue.value.url}">${facetValue.value.value} (${facetValue.value.count})</a></li>
				<c:set var="counter" value="${counter+1}"/>	
			</c:forEach>
			
			<c:if test="${hiddenItems}">
				<li id="${facet.key}Link"><a href="javascript:void(showAllFacet('${facet.key}'));"><fmt:message key="label.facetMore"/></a></li>
			</c:if>
			
		</ul>														
	</c:forEach>				
</div>
