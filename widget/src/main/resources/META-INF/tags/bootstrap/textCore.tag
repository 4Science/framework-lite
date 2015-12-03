<%@ attribute name="modeType" required="true"%>
<%@ attribute name="inputName"%>
<%@ attribute name="inputValue" type="java.lang.String"%>
<%@ attribute name="inputId"%>
<%@ attribute name="isDate" type="java.lang.Boolean"%>
<%@ attribute name="isTime" type="java.lang.Boolean"%>
<%@ attribute name="isHidden" type="java.lang.Boolean"%>
<%@ attribute name="calendarFutureOnly"%>
<%@ attribute name="isMoney" type="java.lang.Boolean"%>
<%@ attribute name="isEuro" type="java.lang.Boolean"%>
<%@ attribute name="isPercent" type="java.lang.Boolean"%>
<%@ attribute name="allowNegativeNumbers" type="java.lang.Boolean"%>
<%@ attribute name="moneyPattern"%>
<%@ attribute name="maxlength"%>
<%@ attribute name="showCounter" type="java.lang.Boolean"%>
<%@ attribute name="cssClass"%>
<%@ attribute name="readonly"%>
<%@ attribute name="disabled"%>
<%@ attribute name="onkeyup"%>
<%@ attribute name="onchange"%>
<%@ attribute name="hideName"%>
<%@ attribute name="autocompleteUrl"%>
<%@ attribute name="autocompleteInputName" %>
<%@ attribute name="autocompleteInputValue"%>
<%@ attribute name="autocompleteData"%>
<%@ attribute name="autocompleteGetUrl"%>
<%@ attribute name="autocompleteGetData"%>
<%@ attribute name="password" type="java.lang.Boolean"%>
<%@ attribute name="hasError" type="java.lang.Boolean"%>
<%@ attribute name="activeMultipleButton" type="java.lang.String"%>
<%@ attribute name="multipleCssClass" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>

<c:if test="${empty autocompleteData}"><c:set var="autocompleteData" value="description: request.term"/></c:if>
<c:choose>
	<c:when test="${modeType == 'display'}">
		<p class="form-control-static">
  		<c:if test="${!password}">
			<c:if test="${not empty autocompleteUrl}">
				<c:if test="${inputValue ne null && autocompleteGetUrl ne null && autocompleteGetUrl ne '' && autocompleteGetData ne null && autocompleteGetData ne ''}">
					<script type="text/javascript">
						JQ.ajax({
							url: "${autocompleteGetUrl}",
							dataType: "json",
							contentType: "application/x-www-form-urlencoded;charset=UTF-8",
							data: {
								${autocompleteGetData}
							},
							success: function(data) {
								JQ('#${inputId}_content p').html(data.displayValue+"&nbsp;");
							}
						});
					</script>
				</c:if>
			</c:if>
			<c:if test="${empty autocompleteUrl}">
				${inputValue}&nbsp;
			</c:if>	  	
		</c:if>	
 	    <c:if test="${password}">*********&nbsp;</c:if>
 	    <c:if test="${not empty inputValue}">
	 	    <c:if test="${isPercent}"><fmt:message key="prompt.percent"/></c:if>
			<c:if test="${isEuro}"><fmt:message key="prompt.euro"/></c:if>
			<c:if test="${not empty moneyPattern}"><fmt:message key="${moneyPattern}"/></c:if>
 		</c:if>
 		</p>
	</c:when>
	 <c:otherwise>
	 	<c:set var="controlHtml">
		  	<c:set var="inputType" value="text"/><c:choose><c:when test="${password}"><c:set var="inputType" value="password"/></c:when><c:when test="${isHidden}"><c:set var="inputType" value="hidden"/></c:when><c:when test="${not empty autocompleteUrl}"><c:set var="inputType" value="hidden"/></c:when></c:choose>
			<input type="${inputType}" <c:if test="${not hideName}"> name="${inputName}"</c:if> value="${inputValue}" id="${widget:escapeForJQ(inputId)}"
				<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
				<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${disabled || modeType == 'disabled'}"> disabled="disabled"</c:if>
				<c:if test="${not empty maxlength}"> maxlength="${maxlength}"</c:if>
				<c:if test="${inputType ne 'hidden'}">class="form-control <c:if test="${not empty cssClass}">${cssClass}</c:if> <c:if test="${not empty multipleCssClass}">${multipleCssClass}</c:if>"</c:if>
				<c:if test="${disabled || modeType == 'disabled'}"> disabled</c:if>/>
			<c:if test="${modeType == 'enabled' && not readonly}">
				<c:if test="${not empty autocompleteUrl}">
					<c:if test="${empty autocompleteInputName}">
						<c:choose>
							<c:when test="${fn:endsWith(inputId,'Id') && not empty inputId}"><c:set var="autocompleteInputName" value="${fn:replace(inputId,'Id','String')}"/></c:when>
							<c:when test="${not empty inputId}"><c:set var="autocompleteInputName" value="${inputId}String"/></c:when>
							<c:when test="${fn:endsWith(inputName,'Id') && not empty inputName}"><c:set var="autocompleteInputName" value="${fn:replace(inputName,'Id','String')}"/></c:when>
							<c:when test="${not empty inputName}"><c:set var="autocompleteInputName" value="${inputName}String"/></c:when>
							<c:otherwise><c:set var="autocompleteInputName" value="autocompleteInput"/></c:otherwise>
						</c:choose>
					</c:if>
					<c:set var="autocompleteInputId">${autocompleteInputName}</c:set>
					<c:set var="autocompleteInputId" value="${autocompleteInputId}${suffix}" />
					<c:set var="autocompleteInputId" value="${fn:replace(autocompleteInputId,'[','__')}" />
					<c:set var="autocompleteInputId" value="${fn:replace(autocompleteInputId,']','__')}" />
					<c:set var="ampersand">'</c:set>
					<c:set var="autocompleteInputId" value="${fn:replace(autocompleteInputId,ampersand,'')}" />
					<input type="text" name="${autocompleteInputName}" value="${autocompleteInputValue}" id="${autocompleteInputId}"
						<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
						<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${disabled || modeType == 'disabled'}"> disabled="disabled"</c:if>
						<c:if test="${not empty maxlength}"> maxlength="${maxlength}"</c:if>
						class="form-control <c:if test="${not empty cssClass}">${cssClass}</c:if>"
						<c:if test="${disabled || modeType == 'disabled'}"> disabled</c:if>/>
				</c:if>
			</c:if>
			<c:if test="${hasError && empty autocompleteUrl}"><i class="fa fa-times-circle-o form-control-feedback"></i></c:if>
		</c:set>
		<c:set var="controlHtml">
		<c:choose>
			<c:when test="${isTime}">
				<div class="input-group date">
					${controlHtml}<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
				</div>
				<script type="text/javascript">
				<c:choose>
					<c:when test="${calendarFutureOnly}">$('#${widget:escapeForJQ(inputId)}').datetimepicker({changeMonth: true, changeYear: true, yearRange: '-120:+20', dateFormat:'dd/mm/yy', timeFormat: 'hh:mm', minDate: +1});</c:when>
					<c:otherwise>$('#${widget:escapeForJQ(inputId)}').datetimepicker({changeMonth: true, changeYear: true, yearRange: '-120:+20', dateFormat:'dd/mm/yy', timeFormat: 'hh:mm'});</c:otherwise>
				</c:choose>
				</script>
			</c:when>
			<c:when test="${isDate}">
				<div class="input-group date">
					${controlHtml}<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
				</div>
				<script type="text/javascript">
				<c:choose>
					<c:when test="${calendarFutureOnly}">$('#${widget:escapeForJQ(inputId)}').datepicker({changeMonth: true, changeYear: true, yearRange: '-120:+20', dateFormat:'dd/mm/yy', minDate: +1});</c:when>
					<c:otherwise>$('#${widget:escapeForJQ(inputId)}').datepicker({changeMonth: true, changeYear: true, yearRange: '-120:+20', dateFormat:'dd/mm/yy'});</c:otherwise>
				</c:choose>
				<%-- bootstrap plugin
				<c:choose>
					<c:when test="${calendarFutureOnly}">$('#${widget:escapeForJQ(inputId)}').datepicker({format: 'dd/mm/yyyy', language: 'it', autoclose: true, todayHighlight: true,  startDate: '+1d'});</c:when>
					<c:otherwise>$('#${widget:escapeForJQ(inputId)}').datepicker({format: 'dd/mm/yyyy', language: 'it', autoclose: true, todayHighlight: true});</c:otherwise>
				</c:choose>
				--%>
				</script>
			</c:when>
			<c:when test="${isMoney}">
				<div class="input-group date">
					<c:choose>
						<c:when test="${isPercent}"><span class="input-group-addon"><fmt:message key="prompt.percent"/></span></c:when>
						<c:when test="${isEuro}"><span class="input-group-addon"><fmt:message key="prompt.euro"/></span></c:when>
					</c:choose>
					${controlHtml}
					<c:choose>
						<c:when test="${not empty moneyPattern}"><span class="input-group-addon"><fmt:message key="${moneyPattern}"/></span></c:when>
						<c:otherwise><span class="input-group-addon">#.#</span></c:otherwise>
					</c:choose>
				</div>
				<c:if test="${not readonly && not disabled && modeType != 'disabled'}">
					<script type="text/javascript">				
					<c:choose>
						<c:when test="${allowNegativeNumbers}">$('#${widget:escapeForJQ(inputId)}').autoNumeric({aNeg: '-', aSep: '.', aDec: ',', aSign: '', mDec: 3});</c:when>
						<c:otherwise>$('#${widget:escapeForJQ(inputId)}').autoNumeric({aNeg: '', aSep: '.', aDec: ',', aSign: '', mDec: 3});</c:otherwise>
					</c:choose>				
					</script>
				</c:if>
			</c:when>
			<c:when test="${not empty autocompleteUrl}">
				<div class="input-group">
					${controlHtml}<span class="input-group-addon"><i class="fa fa-list-alt"></i></span>
				</div>
				<c:if test="${empty autocompleteInputName}">
					<c:choose>
						<c:when test="${fn:endsWith(inputId,'Id') && not empty inputId}"><c:set var="autocompleteInputName" value="${fn:replace(inputId,'Id','String')}"/></c:when>
						<c:when test="${not empty inputId}"><c:set var="autocompleteInputName" value="${inputId}String"/></c:when>
						<c:when test="${fn:endsWith(inputName,'Id') && not empty inputName}"><c:set var="autocompleteInputName" value="${fn:replace(inputName,'Id','String')}"/></c:when>
						<c:when test="${not empty inputName}"><c:set var="autocompleteInputName" value="${inputName}String"/></c:when>
						<c:otherwise><c:set var="autocompleteInputName" value="autocompleteInput"/></c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${empty autocompleteInputValue && inputValue ne null && autocompleteGetUrl ne null && autocompleteGetUrl ne '' && autocompleteGetData ne null && autocompleteGetData ne ''}">
					<script type="text/javascript">
						$.ajax({
							url: "${autocompleteGetUrl}",
							dataType: "json",
							contentType: "application/x-www-form-urlencoded;charset=UTF-8",
							data: {
								${autocompleteGetData}
							},
							success: function(data) {
								$('#${autocompleteInputId}').val(data.displayValue);
							}
						});
						</script>
				</c:if>
				<c:set var="autocompleteInputId">${autocompleteInputName}</c:set>
				<c:set var="autocompleteInputId" value="${autocompleteInputId}${suffix}" />
				<c:set var="autocompleteInputId" value="${fn:replace(autocompleteInputId,'[','__')}" />
				<c:set var="autocompleteInputId" value="${fn:replace(autocompleteInputId,']','__')}" />
				<c:set var="ampersand">'</c:set>
				<c:set var="autocompleteInputId" value="${fn:replace(autocompleteInputId,ampersand,'')}" />
				<script type="text/javascript">
					$( "#${widget:escapeForJQ(autocompleteInputId)}" ).autocomplete({
						source: function( request, response ) {	
							$.ajax({
								traditional: true,
								url: "${autocompleteUrl}",
								dataType: "json",		
								contentType: "application/x-www-form-urlencoded;charset=UTF-8",													
								data: {
									${autocompleteData}
								},
								success: function(data) {
									response( $.map( data, function( item ) {
										return {
											label: item.displayValue,
											value: item.identifyingValue
										}
									}));
								}
							});
						},
						minLength: 3,
						select: function( event, ui ) {
							var myLabel=ui.item.label;							
							var i18nPattern=/^\s\[\w{2}\]\s.*$/i
							if (i18nPattern.test(myLabel)){	
								myLabel=myLabel.replace(/\s\[\w{2}\]\s/i, "");
							}
							this.value=myLabel;
							$("#${widget:escapeForJQ(inputId)}").val(ui.item.value);
							return false;
						},
						focus: function( event, ui ) {
							var myLabel=ui.item.label;							
							var i18nPattern=/^\s\[\w{2}\]\s.*$/i
							if (i18nPattern.test(myLabel)){	
								myLabel=myLabel.replace(/\s\[\w{2}\]\s/i, "");
							}
							this.value=myLabel;
							$("#${widget:escapeForJQ(inputId)}").val(ui.item.value);
							return false;
						},						
						open: function() {
							$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
						},
						close: function() {
							$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
						}
					});
					$('#${widget:escapeForJQ(autocompleteInputId)}').keypress(function(event) {
						var keyCode = $.ui.keyCode;
                        if (event.keyCode!=keyCode.ENTER)
							$("#${widget:escapeForJQ(inputId)}").val('');
					});
				</script>
			</c:when>
			<c:when test="${showCounter}">
				${controlHtml}
				<span class="help-block">
					<span id="${widget:escapeForJQ(inputId)}_counter"></span> <fmt:message key="prompt.counter.characters.entered"/>
					<c:if test="${not empty maxlength}">
						; <span id="${widget:escapeForJQ(inputId)}_maxcounter"></span> <fmt:message key="prompt.counter.characters.available"/>
					</c:if>
				</span>
				<script type="text/javascript">
					$('#${widget:escapeForJQ(inputId)}').keyup(function() {
						characterCounter('${widget:escapeForJQ(inputId)}');
					});
					$('#${widget:escapeForJQ(inputId)}').change(function() {
						characterCounter('${widget:escapeForJQ(inputId)}');
					});
					characterCounter('${widget:escapeForJQ(inputId)}');
					<c:if test="${not empty maxlength}">
					$('#${widget:escapeForJQ(inputId)}').keyup(function() {
						characterMaxCounter('${widget:escapeForJQ(inputId)}', ${maxlength});
					});
					$('#${widget:escapeForJQ(inputId)}').change(function() {
						characterMaxCounter('${widget:escapeForJQ(inputId)}', ${maxlength});
					});
					characterMaxCounter('${widget:escapeForJQ(inputId)}', ${maxlength});
					</c:if>
				</script>
			</c:when>
			<c:otherwise>${controlHtml}</c:otherwise>
		</c:choose>
		</c:set>
		
		<c:choose>
		<c:when test="${not empty activeMultipleButton}">
			<div class="col-md-8">${controlHtml}</div>
			<div class="col-md-4">
				<a id="${inputId}_buttonAdd" class="btn btn-danger<c:if test="${activeMultipleButton=='add'}"> hidden</c:if>" name="" value="<fmt:message key="prompt.widget.remove"/>" onclick="removeWidgetMultiple('${inputId}');"><i class="fa fa-trash-o"></i><fmt:message key="prompt.widget.remove"/></a>
				<a id="${inputId}_buttonRemove" class="btn btn-default<c:if test="${activeMultipleButton=='remove'}"> hidden</c:if>" name="" value="<fmt:message key="prompt.widget.add"/>" onclick="addWidgetMultiple${multipleCssClass}('${inputId}');"><i class="fa fa-plus"></i><fmt:message key="prompt.widget.add"/></a>
			</div>
		</c:when>
		<c:otherwise>${controlHtml}</c:otherwise>
		</c:choose>
		
		
	</c:otherwise>
</c:choose>