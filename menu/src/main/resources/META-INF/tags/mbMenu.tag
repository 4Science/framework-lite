<%@ attribute name="item" required="true" type="it.cilea.core.menu.model.TreeNode"%>
<%@ attribute name="jsRules" required="false" type="java.lang.String"%>
<%@ attribute name="divId" required="false" type="java.lang.String"%>
<%@ attribute name="iconBasePath" required="false" type="java.lang.String" %>
<%@ attribute name="openOnRight" required="false" type="java.lang.Boolean" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/menu" prefix="menu"%>

<c:if test="${empty divId}"><c:set var="divId" value="mbMenu_${item.id}" /></c:if>
<c:if test="${empty iconBasePath}"><c:set var="iconBasePath" value="/${SR_MODULE_NAME}/cineca/images/menu/"/></c:if>
<c:if test="${empty openOnRight}"><c:set var="openOnRight" value="false"/></c:if>
<c:if test="${empty jsRules}"><c:set var="jsRules" value="it.cilea.core.menu.rhino.iterfaces.implementation.AddJsInfo"/></c:if>

<script type="text/javascript">
	JQ(function() {
			JQ(".${fn:replace(item.label,'.','_')}").buildMenu(
			{
  			  template:"",
			  menuWidth:150,
			  openOnRight:${openOnRight},
				<c:if test="${openOnRight}">
					containment:"wrapper",
					adjustLeft:0,
					adjustTop:0,
					closeOnMouseOut:true,
				</c:if>
				<c:if test="${not openOnRight}">
					adjustLeft:2,
					adjustTop:10,
					closeOnMouseOut:false,
				</c:if>
			  menuSelector: ".menuContainer",
			  iconPath:"${iconBasePath}",
			  hasImages:true,
			  fadeInTime:100,
			  fadeOutTime:300,
			  minZindex:"auto",
			  opacity:.95,
			  shadow:true,
			  shadowColor:"#ccc",
			  hoverIntent:0,
			  openOnClick:true,
			  closeAfter:1000,
			  submenuHoverIntent:200
			});
		});
</script>
<table  border="0" cellpadding="0" cellspacing="0" class="container" <c:if test="${openOnRight}">style="width:208px"</c:if>>
	<tr>
		<td class="${fn:replace(item.label,'.','_')}">
			<table class="rootVoices <c:if test="${openOnRight}">vertical</c:if>" cellspacing="0" cellpadding="0" border="0">
				<c:if test="${openOnRight}">
					<c:forEach items="${item.childrenList}" var="child">
						<c:if test="${menu:isVisibleByPath(child,pageContext.request.requestURI) && menu:isVisibleByRole(child) && menu:isVisibleByRules(child,jsRules,pageContext.request)}">
							<tr><td class="rootVoice {menu: 'menu_${child.id}'}">${child.label}</td></tr>
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${!openOnRight}">
					<tr>
						<c:forEach items="${item.childrenList}" var="child">
							<c:if test="${menu:isVisibleByPath(child,pageContext.request.requestURI) && menu:isVisibleByRole(child) && menu:isVisibleByRules(child,jsRules,pageContext.request)}">
								<td class="rootVoice {menu: 'menu_${child.id}'}">${child.label}</td>
							</c:if>
						</c:forEach>
					</tr>
				</c:if>
			</table>
		</td>
	</tr>
</table>
<c:forEach items="${item.childrenList}" var="child">
	<menu:mbItem item="${child}" jsRules="${jsRules}"/>
</c:forEach>