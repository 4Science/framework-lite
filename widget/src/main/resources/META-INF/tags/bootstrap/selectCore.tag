<%@ attribute name="modeType" required="true"%>
<%@ attribute name="collection" required="true" type="java.util.Collection"%>
<%@ attribute name="inputName"%>
<%@ attribute name="inputValue" type="java.lang.Object"%>
<%@ attribute name="inputId"%>
<%@ attribute name="cssClass"%>
<%@ attribute name="readonly"%>
<%@ attribute name="onkeyup"%>
<%@ attribute name="onchange"%>
<%@ attribute name="onmouseover"%>
<%@ attribute name="hideName" type="java.lang.Boolean"%>
<%@ attribute name="size" type="java.lang.Integer"%>
<%@ attribute name="multiple" type="java.lang.Boolean" %>
<%@ attribute name="hasError" type="java.lang.Boolean"%>
<%@ attribute name="type"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>

<c:if test="${empty type}"><c:set var="type" value="select"/></c:if>
<c:choose>
	<c:when test="${modeType == 'display'}"><p class="form-control-static">${inputValue}&nbsp;</p></c:when>
	<c:when test="${type == 'select' || type == 'multi-select' || type == 'select2'}">
		<select <c:if test="${not hideName}"> name="${inputName}"</c:if> id="${inputId}" size="${size}"
			<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
			<c:if test="${not empty onmouseover}"> onchange="${onmouseover}"</c:if>
			<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${modeType == 'disabled'}"> disabled="disabled"</c:if>
			class="<c:if test="${type != 'select2'}">form-control </c:if><c:if test="${not empty cssClass}">${cssClass}</c:if>"
			<c:if test="${multiple}"> multiple="multiple"</c:if>
			>
			<c:if test="${collection != null}">
				<c:forEach var="item" items="${collection}" varStatus="loop">
					<c:set var="optionSelected" value="false"/>
					<c:if test="${not multiple}"><c:if test="${item.identifyingValue == inputValue}"><c:set var="optionSelected" value="true"/></c:if></c:if>
					<c:if test="${multiple}">
						<c:forEach items="${inputValue}" var="valoreSingolo">
							<c:if test="${item.identifyingValue == valoreSingolo}"><c:set var="optionSelected" value="true"/></c:if>
						</c:forEach>
					</c:if>
					<option value="${item.identifyingValue}"<c:if test="${optionSelected == 'true'}"> selected="selected"</c:if>>${item.displayValue}</option>
				</c:forEach>
			</c:if>
		</select>
		<c:if test="${type == 'multi-select'}">
			<script type="text/javascript">
			$(document).ready(function() {
				$('#${inputId}').multiselect({
					numberDisplayed: 5
					, nonSelectedText: '<fmt:message key="select.nonSelectedText"/>'
					, nSelectedText: '<fmt:message key="select.nSelectedText"/>'
					, selectAllText: '<fmt:message key="select.selectAllText"/>'
					, maxHeight: 400
					, includeSelectAllOption: true
					<c:if test="${collection != null && fn:length(collection)>10}">
						, enableCaseInsensitiveFiltering: true
						, filterBehavior: 'text'
						, filterPlaceholder: '<fmt:message key="select.filterPlaceholder"/>'
					</c:if>
				});
			});
			</script>
		</c:if>
		<c:if test="${type == 'select2'}">
			<script type="text/javascript">
			$(document).ready(function() {
				$('#${inputId}').select2({allowClear: true, width: "100%", closeOnSelect: false});
			});
			</script>
		</c:if>
	</c:when>
	<c:when test="${type == 'selectAndMove-horizontal'}">
		<div class="select-and-move select-and-move-horizontal">
			<div class="col-sm-6">
				<div class="btn-group btn-group-justified">
					<div class="btn-group">
						<button type="button" class="btn btn-default" title="<fmt:message key="info.select.move.right.all"/>" onclick="javascript:moveAllSelectOption('${inputId}_toSelect','${inputId}');"><i class="fa fa-angle-double-right"></i></button>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-default" title="<fmt:message key="info.select.move.right"/>" onclick="javascript:moveSelectOption('${inputId}_toSelect','${inputId}');"><i class="fa fa-angle-right"></i></button>
					</div>
				</div>
				<select <c:if test="${not hideName}"> name="${inputName}_toSelect"</c:if> id="${inputId}_toSelect" size="${size}"
					<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
					<c:if test="${not empty onmouseover}"> onchange="${onmouseover}"</c:if>
					<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${modeType == 'disabled'}"> disabled="disabled"</c:if>
					class="form-control select-left <c:if test="${not empty cssClass}">${cssClass}</c:if>"
					<c:if test="${multiple}"> multiple="multiple"</c:if>
					>
					<c:if test="${collection != null}">
						<c:forEach var="item" items="${collection}" varStatus="loop">
							<c:set var="optionSelected" value="false"/>
							<c:if test="${not multiple}"><c:if test="${item.identifyingValue == inputValue}"><c:set var="optionSelected" value="true"/></c:if></c:if>
							<c:if test="${multiple}">
								<c:forEach items="${inputValue}" var="valoreSingolo">
									<c:if test="${item.identifyingValue == valoreSingolo}"><c:set var="optionSelected" value="true"/></c:if>
								</c:forEach>
							</c:if>
							<c:if test="${optionSelected == 'false'}">
								<option value="${item.identifyingValue}">${item.displayValue}</option>
							</c:if>
						</c:forEach>
					</c:if>
				</select>
			</div>
			<div class="col-sm-6">
				<div class="btn-group btn-group-justified">
					<div class="btn-group">
						<button type="button" class="btn btn-default" title="<fmt:message key="info.select.move.left"/>" onclick="javascript:moveSelectOption('${inputId}','${inputId}_toSelect');"><i class="fa fa-angle-left"></i></button>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-default" title="<fmt:message key="info.select.move.left.all"/>" onclick="javascript:moveAllSelectOption('${inputId}','${inputId}_toSelect');"><i class="fa fa-angle-double-left"></i></button>
					</div>
				</div>
				<div class="input-group">
					<select <c:if test="${not hideName}"> name="${inputName}"</c:if> id="${inputId}" size="${size}"
						<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
						<c:if test="${not empty onmouseover}"> onchange="${onmouseover}"</c:if>
						<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${modeType == 'disabled'}"> disabled="disabled"</c:if>
						class="form-control select-right <c:if test="${not empty cssClass}">${cssClass}</c:if>"
						<c:if test="${multiple}"> multiple="multiple"</c:if>
						>
						<c:if test="${not empty inputValue}">
							<c:forEach var="inputValueNow" items="${inputValue}">
								<c:set var="optionSelected" value="false"/>
								<c:forEach var="item" items="${collection}">
									<c:if test="${item.identifyingValue == inputValueNow}">
										<option value="${item.identifyingValue}">${item.displayValue}</option>
									</c:if>
								</c:forEach>
							</c:forEach>
						</c:if>
					</select>
					<div class="input-group-addon btn-group-vertical">
						<span class="fa fa-arrow-up btn " onclick="javascript:moveUpOptionOfSelect('${inputId}');" title="<fmt:message key="info.select.move.up.within.select"/>"></span>
						<span class="fa fa-arrow-down btn " onclick="javascript:moveDownOptionOfSelect('${inputId}');" title="<fmt:message key="info.select.move.down.within.select"/>"></span>
					</div>
				</div>
			</div>
			<script type="text/javascript">
			$(document).ready(function() {
				$('#${inputId}_toSelect').click(function(){
					moveSelectOption('${inputId}_toSelect','${inputId}');
				})
			});
			</script>
		</div>
	</c:when>
	<c:when test="${type == 'selectAndMove-vertical'}">
		<div class="select-and-move select-and-move-vertical">
			<select <c:if test="${not hideName}"> name="${inputName}_toSelect"</c:if> id="${inputId}_toSelect" size="${size}"
				<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
				<c:if test="${not empty onmouseover}"> onchange="${onmouseover}"</c:if>
				<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${modeType == 'disabled'}"> disabled="disabled"</c:if>
				class="form-control select-north <c:if test="${not empty cssClass}">${cssClass}</c:if>"
				<c:if test="${multiple}"> multiple="multiple"</c:if>
				>
				<c:if test="${collection != null}">
					<c:forEach var="item" items="${collection}" varStatus="loop">
						<c:set var="optionSelected" value="false"/>
						<c:if test="${not multiple}"><c:if test="${item.identifyingValue == inputValue}"><c:set var="optionSelected" value="true"/></c:if></c:if>
						<c:if test="${multiple}">
							<c:forEach items="${inputValue}" var="valoreSingolo">
								<c:if test="${item.identifyingValue == valoreSingolo}"><c:set var="optionSelected" value="true"/></c:if>
							</c:forEach>
						</c:if>
						<c:if test="${optionSelected == 'false'}">
							<option value="${item.identifyingValue}">${item.displayValue}</option>
						</c:if>
					</c:forEach>
				</c:if>
			</select>
			<div class="btn-group btn-group-justified">
				<div class="btn-group">
					<button type="button" class="btn btn-default" title="<fmt:message key="info.select.move.down.all"/>" onclick="javascript:moveAllSelectOption('${inputId}_toSelect','${inputId}');"><i class="fa fa-angle-double-down"></i></button>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-default" title="<fmt:message key="info.select.move.up"/>" onclick="javascript:moveSelectOption('${inputId}','${inputId}_toSelect');"><i class="fa fa-angle-up"></i></button>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-default" title="<fmt:message key="info.select.move.up.all"/>" onclick="javascript:moveAllSelectOption('${inputId}','${inputId}_toSelect');"><i class="fa fa-angle-double-up"></i></button>
				</div>
			</div>
			<div class="input-group">
				<select <c:if test="${not hideName}"> name="${inputName}"</c:if> id="${inputId}" size="${size}"
					<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
					<c:if test="${not empty onmouseover}"> onchange="${onmouseover}"</c:if>
					<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${modeType == 'disabled'}"> disabled="disabled"</c:if>
					class="form-control select-south <c:if test="${not empty cssClass}">${cssClass}</c:if>"
					<c:if test="${multiple}"> multiple="multiple"</c:if>
					>
					<c:if test="${not empty inputValue}">
						<c:forEach var="inputValueNow" items="${inputValue}">
							<c:set var="optionSelected" value="false"/>
							<c:forEach var="item" items="${collection}">
								<c:if test="${item.identifyingValue == inputValueNow}">
									<option value="${item.identifyingValue}">${item.displayValue}</option>
								</c:if>
							</c:forEach>
						</c:forEach>
					</c:if>
				</select>
				<div class="input-group-addon btn-group-vertical">
					<span class="fa fa-arrow-up btn " onclick="javascript:moveUpOptionOfSelect('${inputId}');" title="<fmt:message key="info.select.move.up.within.select"/>"></span>
					<span class="fa fa-arrow-down btn " onclick="javascript:moveDownOptionOfSelect('${inputId}');" title="<fmt:message key="info.select.move.down.within.select"/>"></span>
				</div>
			</div>
			<script type="text/javascript">
			$(document).ready(function() {
				$('#${inputId}_toSelect').click(function(){
					moveSelectOption('${inputId}_toSelect','${inputId}');
				})
			});
			</script>
		</div>
	</c:when>
</c:choose>