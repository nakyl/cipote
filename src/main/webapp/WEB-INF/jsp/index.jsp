<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="es">
<head>
<link rel="stylesheet" href="../css/bootstrap/bootstrap.min.css">

<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../js/bootstrap/bootstrap.min.js"></script>
<script src="https://github.highcharts.com/master/highstock.src.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

<script>
$(document).ready(function() {
	Highcharts.setOptions({
	    lang: {
	        months: [
	            'Enero', 'Febrero', 'Marzo', 'Abril',
	            'Mayo', 'Junio', 'Julio', 'Agosto',
	            'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
	        ],
	        weekdays: [
	            'Domingo', 'Lunes', 'Martes', 'Miercoles',
	            'Jueves', 'Viernes', 'Sabado'
	        ]
	    }
	});
	
    test();
});

function test () {
	
	// Create the chart
	Highcharts.chart('total', {
	    chart: {
	        type: 'pie'
	    },
	    title: {
	        text: 'Inversión total'
	    },
	    subtitle: {
	        text: ''
	    },
	    plotOptions: {
	        series: {
	            dataLabels: {
	                enabled: true,
	                format: '{point.name}: {point.y:.1f}€'
	            }
	        }
	    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
	    },
	    series: [{
	        name: 'Brands',
	        colorByPoint: true,
	        data: [
	        	<c:forEach items="${crypto}" varStatus="item">
	        	{
		            name: '${item.current.coinByExchange.coin.name}',
		            y: ${item.current.invested},
		            drilldown: '${item.current.coinByExchange.coin.name}'
		        },
		        </c:forEach>
		    ]
	    }]
	});
    
    var messageList = $("#messages");
	<c:forEach items="${crypto}" varStatus="item">
	var chart${item.index} = createChart(${item.index}, ${item.current.coinByExchange.coin.id}, ${item.current.coinByExchange.exchange.id}, "${item.current.coinByExchange.coin.name}");
    </c:forEach>
}

function createChart(idContainer, idCrypto, idExchange, name) {
    var groupingUnits = [
        [
            'minute', // unit name
        [60] // allowed multiples
        ],
        [
            'day', [1]],
        [
            'week', [1]],
        [
            'month', [1]]
    ];
	
	
	 $.ajax({
		    url: '/getHistoricData?idCrypto='+idCrypto+'&idExchange='+idExchange,
		    type: 'GET',
		    async: true,
		    dataType: "json",
		    success: function (data) {
		    	var socket = new SockJS('/stomp');
		    	var stompClient = Stomp.over(socket);
		    	var processed_json = new Array(); 
		    	
		        for (i = 0; i < data.length; i++){
		        	processed_json.push([data[i].time, data[i].price]);
		        }
		        var options = {
		                
		                chart: {
		                    renderTo: 'container'+idContainer,
                   rangeSelector: {
                       inputDateFormat: '%d-%m-%Y %H:%M'
                   },
                   type: 'line'
               },
               legend: {
                   enabled: true,
                  /* labelFormatter: function() {
                       return this.name + '<br>' + 'Now: ' + '0.0' + ' €' + '<br>Max: ' + '0.0' + '<br>Min: ' + '0.0' + '<br>Avg: ' + '0.0';
                       }*/
               },
               title: {
                   text: name
               },
               xAxis: {
                   type: 'datetime',
                   title: {
                       text: 'Date'
                   },
                   minRange: 3600 * 1000
               },
               yAxis: {
                   title: {
                       text: 'EUR'
                   },
                   plotLines: [{
                       label: {
                           text: 'Baseline',
                           x: 25
                       },
                       color: 'orange',
                       width: 2,
                       value: 0,
                       dashStyle: 'longdashdot'
                   }],
               },
               rangeSelector: {
                   inputEnabled: false,
                   selected: 2,
                   buttons: [{
                       type: 'minute',
                       count: 60,
                       text: '1h'
                   }, {
                       type: 'day',
                       count: 1,
                       text: '1d'
                   }, {
                       type: 'week',
                       count: 1,
                       text: '1w'
                   }, {
                       type: 'month',
                       count: 1,
                       text: '1m'
                   }, {
                       type: 'year',
                       count: 1,
                       text: '1y'
                   }, {
                       type: 'all',
                       text: 'All'
                   }]
               },
               plotOptions: {
                   series: {
                       compare: 'datetime'
                   }
               },               
               series: [{
                   threshold: 0,
                   negativeColor: 'red',
                   color: 'green',
                   name: name,
                   data: processed_json,
                   pointStart: new Date().getTime()
               }]
	  };
		    	var chart = new Highcharts.StockChart(options);
		    	
		        stompClient.connect({ }, function(frame) {
		            stompClient.subscribe('/user/'+idCrypto+''+idExchange, function(data) {
		                var message = data.body;
		                
		                var obj = JSON.parse(message);
		                $("#prices"+idCrypto).empty();
		                $("#prices"+idCrypto).append(name+": " + parseFloat(obj.price).toFixed(2) + "€");
		                chart.series[0].addPoint([obj.time, obj.price]);
		                chart.redraw();
		            });

		        });
		    }
	
	});
}

</script>

</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<c:forEach items="${crypto}" varStatus="item">
				<div id="prices${item.current.coinByExchange.coin.id}"
					class="col-sm-2"></div>
			</c:forEach>
		</div>
		<hr />
		<div class="row">
			<div id="total" class="col-sm-4"></div>
			<c:forEach items="${crypto}" varStatus="item">
				<div id="container${item.index}" class="col-sm-4"></div>
			</c:forEach>
		</div>
		<div class="row">
			<div id="balance" class="col-sm-12"></div>
		</div>
	</div>
</body>

</html>
