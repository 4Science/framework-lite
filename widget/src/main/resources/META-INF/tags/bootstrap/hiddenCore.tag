<%@ attribute name="inputName"%>
<%@ attribute name="inputValue" type="java.lang.String"%>
<%@ attribute name="inputId"%>
<%@ attribute name="hideName"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>

<input type="hidden" <c:if test="${not hideName}"> name="${inputName}"</c:if> value="${inputValue}" id="${inputId}"/>