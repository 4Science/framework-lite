<%@ attribute name="servletPath" required="true"	type="java.lang.String"%>
<%@ attribute name="ajax" required="false"	type="java.lang.Boolean"%>
<%@ attribute name="formDivId" required="false"	type="java.lang.String"%>
<%@ attribute name="listDivId" required="false"	type="java.lang.String"%>
<%@ attribute name="listUrl" required="false"	type="java.lang.String"%>
<%@ attribute name="buttonList" required="false"	type="java.lang.String"%>
<%@ attribute name="addFieldsetHeader" required="false"	type="java.lang.Boolean"%>
<%@tag import="it.cilea.core.configuration.util.ConfigurationUtil"%>
<%@tag import="it.cilea.core.widget.model.TextWidget"%>
<%@tag import="it.cilea.core.widget.model.Widget"%>
<%@tag import="java.util.Set"%>
<%@tag import="it.cilea.core.widget.listener.WidgetStartupListenerWorker"%>
<%@tag import="it.cilea.core.widget.model.SearchBuilder"%>
<%@tag import="java.util.Map"%>
<%@tag import="it.cilea.core.widget.WidgetConstant"%>
<%@tag import="it.cilea.core.widget.model.SearchBuilderWidgetRelation"%>
<%@tag import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://www.cineca.it/widget" prefix="widget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<div class="line form-group">
  <div class="line-content col-sm-10">
  	<div id="annotateTextInput" class="inputStyle form-control" style="float: left;">
  		<textarea>testo di esempio con highlight di parole da mostare all'utente che ha premuto il testo Annotate</textarea>
  	</div>
  </div>
  <div class="line-label col-sm-2">
  	<label class="control-label" for="lastName">
		<span class="line-label-text" style="border: 1px solid red;padding: 10px;" onclick="highlight();" onMouseOver="this.style.backgroundColor='#999999'" onMouseOut="this.style.backgroundColor='#FFFFFF'">Annotate</span>
	</label>
  </div>
</div>

<script type="text/javascript">
  function highlight(){
    alert('highlight');
    var textInput = $('#annotateTextInput').text();
    var terms = textInput.split(" ");
    var ret = false;
    var strOutput = "";
    $.each(terms,function(index,value){
    	ret = callAnnotateWS(value);
    	if (ret){
    		strOutput += '<span class="value">'+value+'</span>';
    	} else {
    		strOutput += ' '+value+' ';
    	} 
      
    });
    $('#annotateTextInput').html(strOutput);
  }
  
  function callAnnotateWS(value){
	  if (value == 'highlight'){
		  return new Array("term1", "term2", "term3");
	  }
	  if (value == 'mostare'){
		  return new Array("term41", "term42", "term43");
	  }
	  if (value == 'Annotate'){
		  return new Array("term51", "term52", "term53");
	  }
	  return false;

  }
</script>
testo di esempio con highlight di parole da mostare all'utente che ha premuto il testo Annotate