<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String"%>
<%@ attribute name="divId" required="false" type="java.lang.String"%>
<%@ attribute name="cssReplace" required="false" type="java.lang.String"%>
<%@ attribute name="includeThisNode" type="java.lang.Boolean"%>
<%@ attribute name="classIcon" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu"%>

<c:if test="${not empty cssReplace}"><c:set var="css" value="${cssReplace}" /></c:if>
<c:if test="${not empty item.cssClass}"><c:set var="css" value="${item.cssClass}" /></c:if>

<c:if test="${empty divId}"><c:set var="divId" value="jstree_${item.id}" /></c:if>

<c:if test="${empty includeThisNode}"><c:set var="includeThisNode" >false</c:set></c:if>

<c:if test="${empty classIcon && not empty item.icon}"><c:set var="htmlIcon" ><span class="${item.icon}"></span></c:set></c:if>
<c:if test="${not empty classIcon}"><c:set var="htmlIcon" ><span class="${classIcon}"></span></c:set></c:if>


<ul id="${divId}" class="${css}">
	<c:if test="${includeThisNode}">
		<c:if test="${empty item.childrenList}">
			<c:set var="onclick"></c:set>
			<c:if test="${not empty item.link}"><c:set var="onclick">location.href='${menu:buildUrl(item,pageContext.request)}'</c:set></c:if>
			<c:if test="${not empty item.onClick}"><c:set var="onclick">location.href='${menu:buildUrl(item,pageContext.request)}'</c:set></c:if>
			<li onclick="${onclick}"><span>${item.displayValue}</span><c:if test="${not empty htmlIcon}"><i class="${htmlIcon}"></i></c:if></li>
		</c:if>
		<c:if test="${not empty item.childrenList}">
			<li>
	          ${htmlIcon} ${item.displayValue}<b class="caret"></b>
	          <ul>
	          	<c:forEach items="${item.childrenList}" var="child">
					<menu:jstree3Item item="${child}" jsRules="${jsRules}"/>
				</c:forEach>
	          </ul>
	         </li>
		</c:if>
	</c:if>
	<c:if test="${!includeThisNode}">
		<c:forEach items="${item.childrenList}" var="child">
				<c:if test="${not empty child.childrenList}">
					<li>
						<span>${child.displayValue}</span><c:if test="${not empty child.icon}"><i class="${child.icon}"></i></c:if>
						<ul>
						<c:forEach items="${child.childrenList}" var="grandchild">
							<menu:jstree3Item item="${grandchild}" jsRules="${jsRules}"/>
						</c:forEach>
						</ul>
					</li>
				</c:if>
				<c:if test="${empty child.childrenList}">
					<c:set var="onclick"></c:set>
					<c:if test="${not empty child.link}"><c:set var="onclick">location.href='${menu:buildUrl(child,pageContext.request)}'</c:set></c:if>
					<c:if test="${not empty child.onClick}"><c:set var="onclick">location.href='${menu:buildUrl(child,pageContext.request)}'</c:set></c:if>
					<li onclick="${onclick}">${child.displayValue}<c:if test="${not empty child.icon}"><i class="${child.icon}"></i></c:if></li>
				</c:if>
		</c:forEach>
	</c:if>
</ul>