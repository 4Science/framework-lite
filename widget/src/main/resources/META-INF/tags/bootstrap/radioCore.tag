<%@ attribute name="modeType" required="true"%>
<%@ attribute name="collection" required="true" type="java.util.Collection"%>
<%@ attribute name="inputName"%>
<%@ attribute name="inputValue"%>
<%@ attribute name="inputId"%>
<%@ attribute name="cssClass"%>
<%@ attribute name="readonly"%>
<%@ attribute name="onkeyup"%>
<%@ attribute name="onchange"%>
<%@ attribute name="onmouseover"%>
<%@ attribute name="onclick"%>
<%@ attribute name="hideName" type="java.lang.Boolean"%>
<%@ attribute name="positioning" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<c:if test="${empty positioning}"><c:set var="positioning" value="horizontal"/></c:if>
<c:if test="${empty cssClass}"><c:set var="cssClass" value="styled"/></c:if>
<c:choose>
	<c:when test="${modeType == 'display'}"><p class="form-control-static">${inputValue}&nbsp;</p></c:when>
	<c:otherwise>
			<c:if test="${collection != null}">
				<c:forEach var="item" items="${collection}" varStatus="loop">
					<c:set var="optionSelected" value="false"/>
					<c:if test="${item.identifyingValue == inputValue || (empty valoreSingolo && empty item.identifyingValue)}"><c:set var="optionSelected" value="true"/></c:if>
					<c:choose>
						<c:when test="${positioning eq 'horizontal'}">
							<div class="radio">
								<label>
									<input type="radio" <c:if test="${not hideName}"> name="${inputName}"</c:if> id="${inputId}_${item.identifyingValue}" value="${item.identifyingValue}"
										<c:if test="${optionSelected == 'true'}"> checked="checked"</c:if>
										<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if> <c:if test="${not empty onclick}"> onclick="${onclick}"</c:if>
										<c:if test="${not empty onmouseover}"> onchange="${onmouseover}"</c:if>
										<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${modeType == 'disabled'}"> disabled="disabled"</c:if>
										class="${cssClass}" 
									/>
									${item.displayValue}
								</label>
							</div>
						</c:when>
						<c:otherwise>
							<label class="radio-inline">
								<input type="radio" <c:if test="${not hideName}"> name="${inputName}"</c:if> id="${inputId}_${item.identifyingValue}" value="${item.identifyingValue}"
									<c:if test="${optionSelected == 'true'}"> checked="checked"</c:if>
									<c:if test="${not empty onkeyup}"> onkeyup="${onkeyup}"</c:if> <c:if test="${not empty onchange}"> onchange="${onchange}"</c:if> <c:if test="${not empty onclick}"> onclick="${onclick}"</c:if>
									<c:if test="${not empty onmouseover}"> onchange="${onmouseover}"</c:if>
									<c:if test="${readonly}"> readonly="readonly"</c:if> <c:if test="${modeType == 'disabled'}"> disabled="disabled"</c:if>
									class="${cssClass}" 
								/>
								${item.displayValue}
							</label>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:if>
	</c:otherwise>
</c:choose>