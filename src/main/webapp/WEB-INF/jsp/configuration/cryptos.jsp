<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="en">
<head>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<script src="../js/bootstrap.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<script src="http://code.highcharts.com/stock/highstock.js"></script>
<script src="http://code.highcharts.com/stock/modules/exporting.js"></script>


<script>
$(document).ready(function() {
    var url="http://localhost:8080/getall";
    var url2="http://localhost:8080/lastbycryptoexange?crypto=BTC-EUR&exange=1";
    var dataa = $.getJSON(url,
        function(data1){
    	var processed_json = new Array();  
        for (i = 0; i < data1.length; i++){
            processed_json.push([new Date(data1[i].time).getTime(), data1[i].price]);
        }
           /** Create a chart instance and pass options. */
           var options = {
                   
                   chart: {
                       renderTo: 'container',
                       events: {
                           load: function () {
                               // set up the updating of the chart each second
                               var series = this.series[0];
                               setInterval(function () {
                              	 $.getJSON(url2, function(data1){
                              		    var data = chart.yAxis[0].series[0].processedYData;

                              		   if(data[data.length-1] != data1.price) {
                              			   series.addPoint([new Date(data1.time).getTime(), data1.price], true, true);
                              			   
                              		   }
                              		 
                              	 });
                               }, 1000);

                           }
                       }
                   },
                   xAxis: {
                       type: 'datetime',
                       dateTimeLabelFormats: { // don't display the dummy year
                           month: '%e. %b',
                           year: '%b'
                       },
                       title: {
                           text: 'Date'
                       }
                   },
                   yAxis: {
                       title: {
                           text: 'Snow depth (EUR)'
                       }
                   },
                   
                   rangeSelector: {
                       selected: 1
                   },
                   
                   series: [{
                       name: 'BTC a EUR',
                       data: processed_json
                   }]
           };
           var chart = new Highcharts.StockChart(options);
    });
</script>

</head>
<body>
	<div id="container"
		style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	<div id="report" style="font: 0.8em sans-serif"></div>
</body>

</html>
