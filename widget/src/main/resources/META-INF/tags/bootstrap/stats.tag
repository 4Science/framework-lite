<%@ attribute name="infoKey"%>
<%@ attribute name="infoMessage"%>
<%@ attribute name="infoMessages" type="java.lang.Object"%>
<%@ attribute name="wfItemElement"%>
<%@ attribute name="wfItemParent"%>
<%@ attribute name="wfItemDate"%>
<%@ attribute name="wfItemTitle"%>
<%@ attribute name="widgetMode"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<fmt:message key="scopus.table.selectcolumn.title" var="columnTitle"/>
<fmt:message key="label.id" var="labelId"/>
<fmt:message key="label.year" var="labelYear"/>
<fmt:message key="label.title" var="labelTitle"/>
<fmt:message key="button.add" var="buttonAdd"/>
<fmt:message key="button.remove" var="buttonRemove"/>

<c:choose>
 <c:when test="${widgetMode == 'conf'}">
  <div id="scopusCitationCountSearchAjaxDiv"></div>
  
  <div id="modal-charts" class="modal fade" >
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" title="<fmt:message key="button.close"/>">&nbsp;x</button>
      <button id="maxMinButtonID" class="close" type="button" title="<fmt:message key="scopus.dialog.maximize"/>">&nwarr;</button>
      <h4 class="modal-title"><fmt:message key="scopus.dialog.title"/></h4>
              </div>
  
     <div role="tabpanel">
       <!-- Nav tabs -->
       <ul class="nav nav-tabs" role="tablist">
         <li role="presentation" class="active">
          <a id="rawDataBtnScopus" href="#home" aria-controls="home" role="tab" data-toggle="tab">SCOPUS</a>
      </li>
      <li role="presentation">
          <a id="rawDataBtnWos" href="#rawDataWos" aria-controls="rawDataWos" role="tab" data-toggle="tab">WOS</a>
      </li>
         <li role="presentation">
          <a id="rawDataBtnAltro" href="#rawDataAltro" aria-controls="rawDataAltro" role="tab" data-toggle="tab">Altro</a>
      </li>
       </ul>
       <!-- Tab panes -->
       <div class="tab-content">
        <div role="tabpanel" class="active tab-pane" id="home">
       		<div id="rawDataScopusBody" class="active modal-body">
		        <br/>&nbsp;<b>Citation count</b><br /><div id="rawDataTable_01">...</div><br />
		        &nbsp;<b>Percentile (rivista</b>)<br /><div id="rawDataTable_02">...</div><br />
		        &nbsp;<b>Percentile (articolo)</b><br /><div id="rawDataTable_03">...</div><br />
       		</div>
      	</div>
        <div role="tabpanel" class="tab-pane" id="rawDataWos">
       		<div id="rawDataScopusBody" class="active modal-body">
		        <br/>&nbsp;<b>Wos count</b><br /><div id="rawDataTable_wos">...</div><br />
       		</div>
		</div>
        <div role="tabpanel" class="tab-pane" id="rawDataAltro">
       		<div id="rawDataScopusBody" class="active modal-body">
		        <br/>&nbsp;<b>Indicatori di rivista</b><br /><div id="rawDataTable_altro1">...</div><br />
		        &nbsp;<b>Indicatori di rivista</b><br /><div id="rawDataTable_altro2">...</div><br />
       		</div>
		</div>
      </div>
    </div>
              <div class="modal-footer">
                  <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="button.close"/></button>
              </div>
          </div>
          <!-- /.modal-content -->
      </div>
      <!-- /.modal-dialog -->
  </div>
  <!-- /.modal -->
  <link rel="stylesheet" href="/${BI_MODULE_NAME}/scopus/css/CitationCount.css">
  <script type="text/javascript">
  var dataTitle = '<fmt:message key="scopus.dialog.chart.data.title"/>';   
  var removeConfirm = '<fmt:message key="scopus.dialog.remove"/>';
  var labelYear = '<fmt:message key="scopus.dialog.year"/>';
  var labelTitle = '<fmt:message key="label.title"/>';
  var labelMinimize = '<fmt:message key="scopus.dialog.minimize"/>';
  var labelMaximize = '<fmt:message key="scopus.dialog.maximize"/>';
  var rawDataTableDivIDCit = "rawDataTable_01";
  var rawDataTableDivIDPercRiv = "rawDataTable_02";
  var rawDataTableDivIDPercArt = "rawDataTable_03";
  var rawDataTableDivIDWos = "rawDataTable_wos";
  var rawDataTableDivIDAltro1 = "rawDataTable_altro1";
  var rawDataTableDivIDAltro2 = "rawDataTable_altro2";
  var minYear = 2011;
  var maxYear = 2015;
		var urlScopusRow = '/${BI_MODULE_NAME}/ir/irItem/list/currentDate/searchList/widgetSearch.json';
		var urlPercRivRow = '/${BI_MODULE_NAME}/ir/irItem/percentile/journal/widgetSearch.json';
		var urlPercPubRow = '/${BI_MODULE_NAME}/ir/irItem/percentile/paper/widgetSearch.json';
		var urlScopusRowExists = '/bi/ir/irItem/list/currentDate/searchListIds/widgetSearch.json';
		var urlWosRow = "/${BI_MODULE_NAME}/ir/irItem/wos/widgetSearch.json";
		var urlAltro1Row = "/${BI_MODULE_NAME}/ir/irItem/indicatoriAltro1/widgetSearch.json";
		var urlAltro2Row = "/${BI_MODULE_NAME}/ir/irItem/indicatoriAltro2/widgetSearch.json";
		</script>
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript" src="/${BI_MODULE_NAME}/scopus/js/CitationStats.js"></script>

 </c:when>
 <c:otherwise>
  <c:set var="descr" scope="request" value="${wfItemTitle}"/>
  <div id="scopusChart_publicationVqr_${wfItemElement}" class="btn-group">
   <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
    <i class="fa fa-bar-chart-o"></i>
    <span class="caret"></span>
   </button>
   <ul id="bar-chart_${wfItemElement}" class="dropdown-menu icons-right dropdown-menu-right">
    <li><a href="#" onClick="chartAddSerie(${wfItemElement},${wfItemDate},'<c:out value="${descr}"/>'); return false;">${buttonAdd}</a></li>
    <li><a href="#" onClick="chartRemoveSerieById(${wfItemElement}); return false;">${buttonRemove}</a></li>
   </ul>
  </div>
 </c:otherwise>
</c:choose>