<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html  style= "width: 100%;height: 100%;margin: 0;padding: 0;overflow: hidden;" >
<head>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" type="text/css" href="js/dojo-1.8.0/dojo/resources/dojo.css"> 
    <link rel="stylesheet" type="text/css" href="js/dojo-1.8.0/dijit/themes/claro/claro.css">
    <link rel="stylesheet" type="text/css" href="js/dojo-1.8.0/dojox/grid/resources/Grid.css"> 
    <link rel="stylesheet" type="text/css" href="js/dojo-1.8.0/dojox/grid/resources/claroGrid.css"> 
    <link rel="stylesheet" type="text/css" href="js/dojo-1.8.0/dojox/grid/enhanced/resources/EnhancedGrid.css"> 
    <link rel="stylesheet" type="text/css" href="js/dojo-1.8.0/dojox/grid/enhanced/resources/claroEnhancedGrid.css"> 
    <title><c:out value="seasonalevents"/>  </title>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/1.3.5/socket.io.min.js"></script>
    <script src="https://cdn.polyfill.io/v2/polyfill.min.js"></script>
 
     <script type="text/javascript">
            var dojoConfig = {dojoBlankHtmlUrl: location.pathname.replace(/\/[^/]+$/, '') + '/blank.html',
				packages: [{ name: 'jco',location: location.pathname.replace(/\/[^/]+$/, '') + '/js/jco'}]
			};
    </script>
       <script type="text/javascript" src="js/dojo-1.8.0/dojo/dojo.js" data-dojo-config="async: 1, parseOnLoad: 1"></script>
    <script type="text/javascript">  
    require([
    	"jco/show/seasonalevents/Ready",
        "dojo/ready",
        "dijit/layout/ContentPane",
        "dijit/layout/BorderContainer",
        "dojo/domReady!"], // wait until DOM is loaded
        function(relatedReady,dojoReady){
        dojoReady(function(){
            console.log("dojo is ready");
            seasonalReady();
        });
     });
     </script>  
        
</head>
<body class="claro"  style= "width: 100%;height: 100%;margin: 0;padding: 0;overflow: hidden;">

<div dojoType="dijit.layout.BorderContainer" id="BorderPane" design="screenDesign" style="width: 100%; height: 99%; border: none 0;">

<div dojoType="dijit.layout.ContentPane" id="summaryRegion" region="top"
    style="width: 98%;  height: 50px; min-height:50px; margin: 0px; padding-top: 1px">
<div id="summaryDisplay" style="height:98%;min-height:98%;padding-top:1px;">
<table >
<tr><td style="padding-right: 30px"><b>Seasonal Events: Top10 Items by Probability Rank<b></td>
<td style="padding-right: 3px"><b>Processing Start</b></td>
<td style="padding-right: 20px"><p id="StartDatetime"></p></td>
<td style="padding-right: 3px"><b>Processing Finish</b></td>
<td style="padding-right: 20px"><p id="FinishDatetime"></p></td>
<td style="padding-right: 3px"><b>Report Generated at</b></td>
<td style="padding-right: 3px"><p id="GeneratedAtDatetime"></p></td>
</tr></table>
</div>
</div>


<div dojoType="dijit.layout.ContentPane" id="entriesRegion" region="left"    
   style="width: 45%;  height: 85%; min-height:85%; margin: 0px; padding-top: 1px">
<div id="entriesDisplay" style="width:98%;height:98%;min-height:98%;padding-top:1px;">
<h3 id="entryResults">Groups</h3>
<div id="listEntriesContainer" style="width:96%;height:85%;min-height:85%"></div>
</div>
</div>

<div dojoType="dijit.layout.ContentPane" id="countsRegion"  region="center"  
   style="width: 55%; height:85%; min-height:75%;margin: 0px; padding-top: 1px"> 
<div id="countsDisplay" style="width:98%;height:98%;min-height:98%;padding-top:1px;">
<h3 id="countResults">Constituent Events</h3>
<div id="listCountsContainer" style="width:96%;height:90%;min-height:90%"> </div>
</div>
</div>
</div>
</body>
</html>