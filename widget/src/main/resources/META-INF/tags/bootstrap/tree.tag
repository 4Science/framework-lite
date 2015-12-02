<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="modeType" type="java.lang.String" %>
<%@ attribute name="layoutMode" type="java.lang.String" %>
<%@ attribute name="cssClass" type="java.lang.String" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="helpKey"%>
<%@ attribute name="infoKey" type="java.lang.String" %>
<%@ attribute name="suffix" type="java.lang.String" %>
<%@ attribute name="suffixIdOnly" type="java.lang.String" %>
<%@ attribute name="required" type="java.lang.Boolean" %>
<%@ attribute name="readonly" type="java.lang.Boolean" %>
<%@ attribute name="hideName" type="java.lang.String" %>
<%@ attribute name="callbackFunction" type="java.lang.String" %>
<%@ attribute name="showTreeOnload" type="java.lang.Boolean" %>
<%@ attribute name="fragmentDivId" type="java.lang.String"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>

<%@ attribute name="jsonData" type="java.lang.String"%>
<%@ attribute name="treeRootNode" type="java.lang.String"%>
<%@ attribute name="jsonDataUrl" type="java.lang.String" %>
<%@ attribute name="acceptLeafOnly" type="java.lang.Boolean" %>

<c:if test="${empty layoutMode}"><c:set var="layoutMode" value="div"/></c:if>
<c:if test="${empty modeType || modeType==''}"><c:set var="modeType" value="enabled"/></c:if>
<c:if test="${empty readonly}"><c:set var="readonly" value="${false}"/></c:if>
<c:if test="${empty hideName}"><c:set var="hideName" value="${false}"/></c:if>
<c:if test="${empty acceptLeafOnly}"><c:set var="acceptLeafOnly" value="false"/></c:if>
<c:if test="${empty showTreeOnload}"><c:set var="showTreeOnload" value="false"/></c:if>

<c:set var="objectPath" value="${widget:getObjectPath(propertyPath)}"/>
<spring:bind path="${objectPath}">
	<c:set var="object" value="${status.value}"/><c:if test="${empty object}"><c:set var="object" value="${commandObject}"/></c:if>
</spring:bind>
<c:if test="${empty required}"><c:set var="required" value="${widget:required(object,propertyPath)}"/></c:if>
<spring:bind path="${propertyPath}">
	<c:set var="inputName" value="${status.expression}${suffix}" />
	<c:set var="inputValue" value="${widget:formatText(status.value)}"/>
	<c:set var="inputId" value="${status.expression}${suffix}${suffixIdOnly}" />	
	<c:set var="inputId" value="${fn:replace(inputId,'[','__')}" />
	<c:set var="inputId" value="${fn:replace(inputId,']','__')}" />
	<c:set var="ampersand">'</c:set>
	<c:set var="inputId" value="${fn:replace(inputId,ampersand,'')}" />
	<c:set var="errorMessages" value="${status.errorMessages}" />
</spring:bind>
<c:if test="${empty callbackFunction}"><c:set var="callbackFunction">add${inputId}NodeAndClose</c:set></c:if>

<c:set var="widgetHtml">
	<div id="${inputId}TreeDiv" style="width:200;height:200;" style="display:none">
		<div id="${inputId}DhtmlTreeDiv" style="width:200;height:200;"></div>
	</div>
	<div id="${inputId}ValueDiv">
		<div id="${inputId}LabelDiv" style="float:left;">
			<c:if test="${inputValue ne null}"><img title="<fmt:message key="prompt.loading" />" src="${loadingImg}" class="imgIconTransparent"/></c:if>
		</div>
		<c:if test="${modeType eq null || modeType eq 'enabled'}">
			<div id="${inputId}EditTreeNodeImageDiv" style="float:left;display:none;">
				&nbsp;&nbsp;&nbsp;
				<img title="<fmt:message key="prompt.edit" />" src="${documentEditImg}" class="imgIconTransparent" 
					onclick="deleteSelectedNodeAndShowTree('${inputId}');"/>
			</div>
			<div id="${inputId}DeleteTreeNodeImageDiv" style="float:left;display:none;">
				&nbsp;&nbsp;&nbsp;
				<img title="<fmt:message key="prompt.delete" />" src="${documentDeleteImg}" class="imgIconTransparent" 
					onclick="deleteSelectedNode('${inputId}');"/>
			</div>
			<div id="${inputId}AddTreeNodeImageDiv" style="float:left;display:none;">
				&nbsp;&nbsp;&nbsp;
				<img title="<fmt:message key="prompt.add" />" src="${documentAddImg}" class="imgIconTransparent" 
					onclick="showTree('${inputId}')"/>
			</div>
		</c:if>
	</div>
	<div id="${inputId}TreeUndoDiv" class="form-actions text-right" style="display:none;">
		<input type="button" value="<fmt:message key="button.undo"/>" class="ui-button-text" onclick="hideTree('${inputId}');"/>
	</div>
</c:set>
<c:if test="${layoutMode=='div'}">
	<c:set var="labelHtml">
		<c:if test="${empty labelKey}"><c:set var="labelKey" value="label.${widget:findEndLabel(propertyPath)}"/></c:if>
		<c:set var="showRequired" value="${false}"></c:set>
		<c:if test="${required && modeType != 'display'}"><c:set var="showRequired" value="${true}"></c:set></c:if>
		<widget:label labelKey="${labelKey}" inputId="${inputId}" showRequired="${showRequired}"
			helpKey="${helpKey}" modeType="${modeType}"/>
	</c:set>
	<widget:line inputId="${inputId}" label="${labelHtml}" content="${widgetHtml}" errorMessages="${errorMessages}" infoMessage="${infoKey}"/>
</c:if>
<c:if test="${layoutMode=='none'}">${widgetHtml}</c:if>

<input type="hidden" id="${inputId}" name="${inputName}" value="${inputValue}"/>

<c:if test="${showTreeOnload && inputValue ne null}"><c:set var="showTreeOnload" value="false"/></c:if>

<script type="text/javascript">
var ${inputId}Tree= new Object;
</script>

<script type="text/javascript">
$(document).ready(function() {
	$.ajax({
		url: "${jsonDataUrl}",
		dataType: "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		success: function(json) {
			${inputId}Tree=new dhtmlXTreeObject(document.getElementById('${inputId}DhtmlTreeDiv'),"100%","100%",0);
			${inputId}Tree.setImagePath("/${SR_MODULE_NAME}/dhtmlxTree/imgs/");
			${inputId}Tree.enableCheckBoxes(false);
			${inputId}Tree.enableDragAndDrop(false);
			${inputId}Tree.loadJSONObject($.parseJSON(json.jsonString));
			${inputId}Tree.sortTree(0,"ASC",true);
			${inputId}Tree.attachEvent("onClick",${callbackFunction});
			${inputId}Tree.openItem(json.rootNode);
			$('#${inputId}TreeDiv').hide();
			$('#${inputId}TreeUndoDiv').hide();
			
			<c:if test="${inputValue ne null}">
				// Show/hide icons
				$("#${inputId}LabelDiv").html(${inputId}Tree.getItemText(${inputValue}));
				<c:if test="${modeType ne 'display'}">
					$("#${inputId}EditTreeNodeImageDiv").show();
					$("#${inputId}DeleteTreeNodeImageDiv").show();
					$('#${inputId}AddTreeNodeImageDiv').hide();
				</c:if>
			</c:if>
			<c:if test="${inputValue eq null && modeType ne 'display'}">
				// Show/hide icons
				$("#${inputId}EditTreeNodeImageDiv").hide();
				$("#${inputId}DeleteTreeNodeImageDiv").hide();
				$("#${inputId}AddTreeNodeImageDiv").show();
			</c:if>
			<c:if test="${showTreeOnload}">showTree('${inputId}');</c:if>
		}
	});
});

</script>

<script type="text/javascript">
// Function for single selection
function add${inputId}NodeAndClose(nodeId) {
	var addNode = true;
	
	<c:if test="${acceptLeafOnly}">
	if (${inputId}Tree.getSubItems(nodeId) != '')
		addNode = false;
	</c:if>
	
	if (addNode == true) {
		var text = ${inputId}Tree.getItemText(nodeId);
		$("#${inputId}LabelDiv").html(text);
		$("#${inputId}").val(nodeId);
		hideTree('${inputId}');
		// Show/hide icons
		$("#${inputId}EditTreeNodeImageDiv").show();
		$("#${inputId}DeleteTreeNodeImageDiv").show();
		$("#${inputId}AddTreeNodeImageDiv").hide();
	}
}

// Function for multiple selections, TO COMPLETE!!!
/*function add${inputId}Node(nodeId) {
	if(nodeId != -1) {
		// Loading tree node
		var text = ${inputId}Tree.getItemText(nodeId);
		// Showing new label to user
		$("#${inputId}LabelDiv").html(text);
		text = text + " (<fmt:message key="label.added"/>)";
		${inputId}Tree.setItemText(nodeId, text);
		${inputId}Tree.setItemColor(nodeId,'black','green');
		
		$("#${inputId}").val(nodeId);
		$("#${inputId}LabelDiv").html(text);
		//fragmentElementPost('${fragmentDivId}', 'save');
		hideTree('${inputId}');
		
		$("#${inputId}EditTreeNodeImageDiv").show();
		$("#${inputId}DeleteTreeNodeImageDiv").show();
		$("#${inputId}AddTreeNodeImageDiv").hide();
	}
}*/
// Method for fragments
function add${inputId}NodeToFragment(nodeId) {
	if(nodeId != -1) {
		var text = ${inputId}Tree.getItemText(nodeId);
		text = text + " (AGGIUNTO)";
		${inputId}Tree.setItemText(nodeId, text);
		${inputId}Tree.setItemColor(nodeId,'black','green');
		
		$("#${inputId}").val(nodeId);
		fragmentElementPost('${fragmentDivId}', 'save');
	}
}

function showTree(input) {
	$("#"+input+"TreeDiv").show();
	$("#"+input+"TreeUndoDiv").show();
	$("#"+input+"ValueDiv").hide();
}
function hideTree(input) {
	$("#"+input+"TreeDiv").hide();
	$("#"+input+"TreeUndoDiv").hide();
	$("#"+input+"ValueDiv").show();
}
function deleteSelectedNodeAndShowTree(input) {
	$("#"+input).val("");
	showTree(input);
}
function deleteSelectedNode(input) {
	// Cleaning fields
	$("#"+input).val("");
	$("#"+input+"LabelDiv").html("");
	// Show/hide icons
	$("#"+input+"EditTreeNodeImageDiv").hide();
	$("#"+input+"DeleteTreeNodeImageDiv").hide();
	$("#"+input+"AddTreeNodeImageDiv").show();
}
</script>