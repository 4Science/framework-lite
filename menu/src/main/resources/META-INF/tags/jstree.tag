<%@ attribute name="jstreeJson" required="true"  type="java.lang.String"%>
<%@ attribute name="jstreeUpdate" required="true"  type="java.lang.String"%>
<%@ attribute name="jstreeEditNode" required="true" type="java.lang.String" %>
<%@ attribute name="titleUrl" required="true"  type="java.lang.String"%>
<%@ attribute name="jstreeRightMenuEnabled" required="false"  type="java.lang.Boolean"%>
<%@ attribute name="jstreeReload" required="false"  type="java.lang.String"%>
<%@ attribute name="imagePath" required="false" type="java.lang.String" %>
<%@ attribute name="isAdmin" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showRoot" required="false" type="java.lang.Boolean" %>
<%@ attribute name="rootNodeIdentifier" required="false" type="java.lang.String" %>
<%@ attribute name="linkTarget" required="false" type="java.lang.String" %>
<%@ attribute name="jstreeContextMenu" required="false" type="java.lang.String" %>
<%@ attribute name="treeDivId" required="false" type="java.lang.String" %>
<%@ attribute name="openAll" required="false" type="java.lang.Boolean" %>
<%--
Potrebbe avere senso applicare questo approccio per gestire le diverse operazioni
in base alla tipologia di nodo in questione. 
http://stackoverflow.com/questions/4559543/configuring-jstree-right-click-contextmenu-for-different-node-types --%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${empty jstreeRightMenuEnabled}"><c:set var="jstreeRightMenuEnabled" value="false"/></c:if>

<c:if test="${not empty jstreeJson}"><c:url value="${jstreeJson}" var="jstreeJsonUrl"/></c:if>
<c:if test="${not empty jstreeUpdate}"><c:url value="${jstreeUpdate}" var="jstreeUpdateUrl"/></c:if>
<c:if test="${not empty jstreeReload}"><c:url value="${jstreeReload}" var="jstreeReloadUrl"/></c:if>
<c:if test="${not empty jstreeEditNode}"><c:url value="${jstreeEditNode}" var="jstreeEditNode"/></c:if>

<c:if test="${empty imagePath}"><c:set var="imagePathVar" value="/${SR_MODULE_NAME}/cineca/images/menu/"/></c:if>
<c:if test="${not empty imagePath}"><c:set var="imagePathVar" value="${imagePath}"/></c:if>
<c:if test="${empty isAdmin}"><c:set var="isAdminVar" value="false"/></c:if>
<c:if test="${not empty isAdmin}"><c:set var="isAdminVar" value="${isAdmin}"/></c:if>
<c:if test="${empty showRoot}"><c:set var="showRootVar" value="false"/></c:if>
<c:if test="${not empty showRoot}"><c:set var="showRootVar" value="${showRoot}"/></c:if>
<c:if test="${empty rootNodeIdentifier}"><c:set var="rootNodeIdentifierVar" value="${param.treeIdentifier}"/></c:if>
<c:if test="${not empty rootNodeIdentifier}"><c:set var="rootNodeIdentifierVar" value="${rootNodeIdentifier}"/></c:if>
<c:if test="${empty linkTarget}"><c:set var="linkTargetVar" value="self"/></c:if>
<c:if test="${not empty linkTarget}"><c:set var="linkTargetVar" value="${linkTarget}"/></c:if>
<c:if test="${empty treeDivId}"><c:set var="treeDivIdVar" value="treesx"/></c:if>
<c:if test="${not empty treeDivId}"><c:set var="treeDivIdVar" value="${treeDivId}"/></c:if>
<c:if test="${empty jstreeContextMenu}"><c:set var="jstreeContextMenuVar" value="create,rename,delete,move"/></c:if>
<c:if test="${not empty jstreeContextMenu}"><c:set var="jstreeContextMenuVar" value="${jstreeContextMenu}"/></c:if>
<c:if test="${empty openAll}"><c:set var="openAll" value="${false}"/></c:if>

<%request.setAttribute("randomNumber",(new Double(java.lang.Math.random()*1000d)).intValue()); %>

<div id="waitingBody${randomNumber}" style="display:none;"><img src="${waitImg}" title="<fmt:message key="prompt.wait"/>"  alt="<fmt:message key="prompt.wait"/>"/></div>

<%--
This fragment is responsible for generating the jstree script
The following info must be supplied:
- jstreeJsonUrl
- jstreeUpdateUrl
- jstreeReloadUrl
- jstreeEditNodeUrl

Example:
- jstreeJsonUrl ==> <c:url value="/fragment/manager/tree/json/index.htm" />
- jstreeUpdateUrl ==> <c:url value="/manager/tree/update/index.htm" />
- jstreeReloadUrl ==> <c:url value="/manager/tree/context/reload/index.htm" />
- jstreeEditNodeUrl ==> <c:url value="/fragment/manager/tree/form/index.htm" />
--%>

<script type="text/javascript">
JQ(document).ready( function() {
JQ("#${treeDivIdVar}")
	.jstree({ 
		<c:choose>
			<c:when test="${jstreeRightMenuEnabled}">
			"plugins" : [ "themes", "json_data", "ui", "crrm", "dnd", "search", "types", "contextmenu" ],
			</c:when>
			<c:otherwise>
			"plugins" : [ "themes", "json_data", "ui", "types" ],
			</c:otherwise>
		</c:choose>
		"json_data" : {
	            "ajax" : {
	                "url" : "${jstreeJsonUrl}?isAdmin=${isAdminVar}&showRoot=${showRootVar}&nodeIdentifier=${rootNodeIdentifierVar}"
	            }
	        },
		"themes" : {
		        "theme" : "classic",
	        	"url" : "/${SR_MODULE_NAME}/jquery/css/tree/theme/classic/style.css",
				"icons" : true
	        },

	        
	      
		<c:if test="${jstreeRightMenuEnabled}">
		
	    "contextmenu" : {"items" : 
	    
	    
	    
	     { 
	    	<c:choose>
	    	<c:when test="${fn:contains(jstreeContextMenuVar, 'create')}">	    	
			"create" : {
				"separator_before"	: false,
				"separator_after"	: true,
				"label"				: "<fmt:message key="button.bookmark.create"/>",
				"action"			: function (obj) {					
					
					var tipo=prompt("tipo","folder");
					var js = {};
					js.attr = {};						
					js.attr["rel"] = tipo;
					this.create(obj, null, js);					
					
				}
			},
	    	</c:when>
			<c:otherwise>create:false,</c:otherwise>
			</c:choose>
			<c:choose>
	    	<c:when test="${fn:contains(jstreeContextMenuVar, 'rename')}">
			"rename" : {
				"separator_before"	: false,
				"separator_after"	: false,
				"label"				: "<fmt:message key="button.bookmark.rename"/>",
				"action"			: function (obj) { this.rename(obj); }
			},
	    	</c:when>
			<c:otherwise>rename:false,</c:otherwise>
			</c:choose>
			
			<c:choose>
	    	<c:when test="${fn:contains(jstreeContextMenuVar, 'delete')}">	    	
	    	
			"remove" : {
				"separator_before"	: false,
				"icon"				: false,
				"separator_after"	: false,
				"label"				: "<fmt:message key="button.bookmark.delete"/>",
				"action"			: function (obj) { this.remove(obj); }
			},
	    	</c:when>
	    	<c:otherwise>remove:false,</c:otherwise>
	    	</c:choose>
	    	<c:choose>
	    	<c:when test="${fn:contains(jstreeContextMenuVar, 'move')}">
			"ccp" : {
				"separator_before"	: true,
				"icon"				: false,
				"separator_after"	: false,
				"label"				: "<fmt:message key="button.bookmark.edit"/>",
				"action"			: false,
				"submenu" : { 
					"cut" : {
						"separator_before"	: false,
						"separator_after"	: false,
						"label"				: "<fmt:message key="button.bookmark.edit.cut"/>",
						"action"			: function (obj) { this.cut(obj); }
					},
					"copy" : {
						"separator_before"	: false,
						"icon"				: false,
						"separator_after"	: false,
						"label"				: "<fmt:message key="button.bookmark.edit.copy"/>",
						"action"			: function (obj) { this.copy(obj); }
					},
					"paste" : {
						"separator_before"	: false,
						"icon"				: false,
						"separator_after"	: false,
						"label"				: "<fmt:message key="button.bookmark.edit.paste"/>",
						"action"			: function (obj) { this.paste(obj); }
					}
				}
			}
	    	</c:when>
	    	<c:otherwise>ccp:false</c:otherwise>
	    	</c:choose>
		}
	},
	</c:if>
       
		"types" : {
			"valid_children" : [ "root" ],
			"types" : {
			<c:forEach items="${treeNodeTypeList}" var="nodeType" varStatus="status">
			
				<c:set var="validChildren" value=""/>
				<c:forEach items="${fn:split(nodeType.validChildren, ',')}" var="child" varStatus="statusChild">			
					<c:if test="${not statusChild.first}">
						<c:set var="validChildren" value="${validChildren},"/>
					</c:if>
					<c:set var="validChildren" value="${validChildren}'${child}'"/>			
				</c:forEach>
			 
				"${nodeType.description}" : {
					"valid_children" : [${validChildren}],
					"icon" : {
						"image" : "${imagePathVar}${nodeType.icon}"
					}
				}<c:if test="${not status.last}">,</c:if>
	        </c:forEach>
			}
		},
		"ui" : {
		},
		"core" : { 
		}
	})
	.bind("select_node.jstree", function (e, data) {
		<c:choose>
		<c:when test="${not empty jstreeEditNode}">
			editNode('${jstreeEditNodeUrl}',data.rslt.obj.attr("id"), data.rslt.obj.attr("rel"));
		</c:when>
		<c:otherwise>			
		<c:if test="${not empty titleUrl}">
			executeNavigation(data.rslt.obj.attr("onclick"), data.rslt.obj.attr("link"),data.rslt.obj.attr("identifier"),'${linkTargetVar}','<c:url value="/frame/menu/get.htm"/>');
		</c:if>
		<c:if test="${empty titleUrl}">
			executeNavigation(data.rslt.obj.attr("onclick"), data.rslt.obj.attr("link"),data.rslt.obj.attr("identifier"),'${linkTargetVar}',null);
		</c:if>
		</c:otherwise>
		</c:choose>		  
  	})
  	<c:if test="${openAll}">
	.bind("loaded.jstree", function (event, data) {
		JQ(this).jstree("open_all");
	})
	</c:if>
  	<c:if test="${jstreeRightMenuEnabled}">    
  	.bind("create.jstree", function (e, data) {
		JQ.ajax({
			type: 'POST',
			url: "${jstreeUpdateUrl}",
			dataType : 'json',
			data : { 
				"todo" : "create", 
				"parentId" : data.rslt.parent.attr("id"), 
				"index" : data.rslt.position,
				"title" : data.rslt.name,
				"type" : data.rslt.obj.attr("rel")
			},
			success : function (r) {				
				JQ(data.rslt.obj).attr("id", r.attr.id);
				editNode('${jstreeEditNodeUrl}',data.rslt.obj.attr("id"), data.rslt.obj.attr("rel"));
			}
			});
	})
	.bind("remove.jstree", function (e, data) {
		data.rslt.obj.each(function () {
			JQ.ajax({
				async : false,
				type: 'POST',
				url: "${jstreeUpdateUrl}",
				data : { 
					"todo" : "delete",
					"type" : data.rslt.obj.attr("rel"),
					"nodeId" : this.id
				}, 
				success : function (r) {
					if(!r.status) {
						data.inst.refresh();
					}
				}
			});
		});
	})
	.bind("rename.jstree", function (e, data) {
		JQ.post(
			"${jstreeUpdateUrl}", 
			{ 
				"todo" : "rename", 
				"nodeId" : data.rslt.obj.attr("id"),
				"type" : data.rslt.obj.attr("rel"),
				"title" : data.rslt.new_name
			}
		);
	})
	.bind("move_node.jstree", function (e, data) {
		data.rslt.o.each(function (i) {
			JQ.ajax({
				async : false,
				type: 'POST',
				url: "${jstreeUpdateUrl}",
				data : { 
					"todo" : "move", 
					"nodeId" : data.rslt.o.attr("id"),
					"type" : data.rslt.o.attr("rel"),
					"parentId" : data.rslt.op.attr("id"),
					"newJoinId" : data.rslt.np.attr("id"), 
					"index" : data.rslt.cp + i,
					"copy" : data.rslt.cy ? 1 : 0
				},
				success : function (r) {
						JQ(data.rslt.oc).attr("id", r.id);
						if(data.rslt.cy && JQ(data.rslt.oc).children("UL").length) {
							data.inst.refresh(data.inst._get_parent(data.rslt.oc));
						}
				}
			});
		});
	});
    </c:if>
	var errorHtml='<div class="line"><div class="line-label"><label><fmt:message key="label.error"/></label></div><div class="line-content"><img src="${error}"><fmt:message key="error.ajax.loadingData" /></div></div>';
	var refreshSuccessHtml='<div class="line"><div class="line-label"><label><fmt:message key="label.info"/></label></div><div class="line-content"><img src="${information}"><fmt:message key="prompt.refresh.success" /></div></div>';
    JQ("#treeRefresh").click(function() {
    	JQ.ajax({
			  url: '${jstreeReloadUrl}',
			  sync: true,
			  success: function(data) {
				JQ('#infoDiv').html(refreshSuccessHtml);
			  },
			  error: function(data) {
				JQ('#infoDiv').html(errorHtml);
	  			}
	  		});  
		 });
});
</script>


<c:choose>
	<c:when test="${jstreeRightMenuEnabled}">
		<div id="contentBodyDiv"></div>
	</c:when>
	<c:otherwise>			
		<c:if test="${not empty titleUrl}">
		<iframe width="100%" height="90px;" id="contentTitle" style="border: 0px solid #ffffff;">
		</iframe>
		</c:if>
		<iframe width="100%" height="85%" id="contentBody" style="border: 0px solid #ffffff;">
		</iframe>	
		<div id="menuDialogueNoteDiv" style="display: none;"> </div>
	</c:otherwise>
</c:choose>		