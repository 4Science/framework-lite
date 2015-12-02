<%@ attribute name="modeType" required="true"%>
<%@ attribute name="inputName"%>
<%@ attribute name="inputValue" type="java.lang.String"%>
<%@ attribute name="inputId"%>
<%@ attribute name="maxlength"%>
<%@ attribute name="showCounter" type="java.lang.Boolean"%>
<%@ attribute name="cssClass"%>
<%@ attribute name="readonly"%>
<%@ attribute name="onkeyup"%>
<%@ attribute name="onchange"%>
<%@ attribute name="ckEditor" type="java.lang.Boolean"%>
<%@ attribute name="hideName"%>
<%@ attribute name="hasError" type="java.lang.Boolean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>

<c:if test="${not ckEditor and modeType == 'display'}">
	<c:set var="inputValue" value="${widget:formatText(inputValue)}"/>
</c:if>
	
<c:choose>	
	<c:when test="${modeType == 'display'}"><p class="form-control-static">${inputValue}&nbsp;</p></c:when>
	<c:otherwise>
		<textarea <c:if test="${not hideName}"> name="${inputName}"</c:if> id="${inputId}"
			<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if>
			class="form-control <c:if test="${not empty cssClass}">${cssClass}</c:if>"
			<c:if test="${not empty maxlength}"> maxlength="${maxlength}"</c:if>
			<c:if test="${readonly}"> readonly="readonly"</c:if>
			>${inputValue}</textarea>
		<c:if test="${hasError}"><i class="fa fa-times-circle-o form-control-feedback"></i></c:if>
		<c:if test="${modeType == 'enabled' && not readonly}">
			<c:choose>
				<c:when test="${ckEditor}">
					<script type="text/javascript">
						CKEDITOR.replace('${inputId}',{
							language:'<%= org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()%>',
							<c:if test="${showCounter}">
							wordcount:{
								showWordCount: false,showCharCount: true,
								<c:if test="${not empty maxlength}">charLimit: '${maxlength}'</c:if><c:if test="${maxlength}">charLimit: 'unlimited'</c:if>
							}
							</c:if>
						});
					</script>
				</c:when>
				<c:otherwise>
					<c:if test="${showCounter}">
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
					</c:if>				
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:otherwise>
</c:choose>