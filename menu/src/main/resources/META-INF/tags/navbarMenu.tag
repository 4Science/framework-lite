<%--
I css sul database e cssReplace sostituiscono i css di default

 --%>
<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String"%>
<%@ attribute name="divId" required="false" type="java.lang.String"%>
<%@ attribute name="cssReplace" required="false" type="java.lang.String"%>
<%@ attribute name="includeThisNode" type="java.lang.Boolean"%>
<%@ attribute name="classIcon" type="java.lang.String"%>
<%@ attribute name="top" type="java.lang.Boolean"%>
<%@ attribute name="type" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu"%>

<c:if test="${empty top}"><c:set var="top" >true</c:set></c:if>

<c:if test="${top}"><c:set var="css" value="nav navbar-nav" /></c:if>
<c:if test="${!top}"><c:set var="css" value="navigation" /></c:if>
<c:if test="${type == 'button' && top}">
	<c:set var="css" value="" />
	<c:set var="itemClass" value="btn btn-info" />
</c:if>

<c:if test="${not empty cssReplace}"><c:set var="css" value="${cssReplace}" /></c:if>
<c:if test="${not empty item.cssClass}"><c:set var="css" value="${item.cssClass}" /></c:if>


<c:if test="${empty divId}"><c:set var="divId" value="ulMenu_${item.id}" /></c:if>

<c:if test="${empty includeThisNode}"><c:set var="includeThisNode" >false</c:set></c:if>

<c:if test="${empty classIcon && not empty item.icon}"><c:set var="htmlIcon" ><span class="${item.icon}"></span></c:set></c:if>
<c:if test="${not empty classIcon}"><c:set var="htmlIcon" ><span class="${classIcon}"></span></c:set></c:if>


<ul id="${divId}" class="${css}">
<c:if test="${menu:isVisibleByPath(item,pageContext.request.requestURI) && menu:isVisibleByRole(item) && menu:isVisibleByRules(item,jsRules,pageContext.request)}">
	<c:if test="${includeThisNode}">
		<c:if test="${empty item.childrenList}">
			<li><a id="navbar_a_${item.id}" <c:if test="${not empty item.link}">href="${menu:buildUrl(item,pageContext.request)}"</c:if> <c:if test="${not empty item.onClick}">style="cursor: pointer;" onclick="${item.onClick}"</c:if> ><span>${item.displayValue}</span>
			<c:if test="${not empty htmlIcon}"><i class="${htmlIcon}"></i></c:if>
			</a></li>
		</c:if>
		<c:if test="${not empty item.childrenList}">
			<li class="dropdown">
	          <a id="navbar_a_${item.id}" href="#" class="dropdown-toggle" data-toggle="dropdown">${htmlIcon} ${item.displayValue}<b class="caret"></b></a>
	          <ul class="dropdown-menu">
	          	<c:forEach items="${item.childrenList}" var="child">
					<menu:navbarItem top="${top}" item="${child}" jsRules="${jsRules}"/>
				</c:forEach>
	          </ul>
	         </li>
		</c:if>
	</c:if>
	<c:if test="${!includeThisNode}">
		<c:forEach items="${item.childrenList}" var="child">
			<c:if test="${menu:isVisibleByPath(child,pageContext.request.requestURI) && menu:isVisibleByRole(child) && menu:isVisibleByRules(child,jsRules,pageContext.request)}">
				<c:if test="${not empty child.childrenList}">
					<c:if test="${top}">
						<li class="dropdown">
							<a id="navbar_a_${child.id}" href="#" class="dropdown-toggle" data-toggle="dropdown">${child.displayValue}<b class="caret"></b></a>
							<ul class="dropdown-menu">
					</c:if>
					<c:if test="${!top}">
						<li>
							<a id="navbar_a_${child.id}" href="#" class="expand"><span>${child.displayValue}</span><c:if test="${not empty child.icon}"><i class="${child.icon}"></i></c:if></a>
							<ul>
					</c:if>
					<c:forEach items="${child.childrenList}" var="grandchild">
						<menu:navbarItem top="${top}" item="${grandchild}" jsRules="${jsRules}"/>
					</c:forEach>
							</ul>
						</li>
				</c:if>
				<c:if test="${empty child.childrenList}">
					<li><a id="navbar_a_${child.id}" class="${itemClass}" <c:if test="${not empty child.link}">href="${menu:buildUrl(child,pageContext.request)}"</c:if> <c:if test="${not empty child.onClick}">onclick="${child.onClick}"</c:if> >${child.displayValue}
					<c:if test="${not empty child.icon}"><i class="${child.icon}"></i></c:if>
					</a></li>
				</c:if>
			</c:if>
		</c:forEach>
	</c:if>
</c:if>
</ul>