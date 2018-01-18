<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="es">
<head>
<link rel="stylesheet" href="../css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/jq-3.2.1/dt-1.10.16/r-2.2.1/datatables.min.css"/>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />


<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/jq-3.2.1/dt-1.10.16/r-2.2.1/datatables.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>


<script>

	$(document).ready(function() {
		
		$('#exchange').select2({
		    ajax: {
		        type: 'get',
		        url: '/exchangeList',
		        dataType: 'json',
		        data: function (data) {
		            return {
		                json: data
		            };
		        },
		        processResults: function (data) {
		            return {
		                results: $.map(data, function(obj) {
		                	return { id: obj.id, text: obj.name };
		                })
		            };
		        }
		    }
		});
		
		$('#coin').select2({
		    ajax: {
		        type: 'get',
		        url: '/coinList',
		        dataType: 'json',
		        data: function (data) {
		            return {
		                json: data
		            };
		        },
		        processResults: function (data, params) {
		            return {
		                results: $.map(data, function(obj) {
		                	return { id: obj.id, text: obj.name };
		                })
		            };
		        }
		    }
		});
		
		
		$('#table_id').DataTable({
			"sAjaxSource": "/coinPerUserList",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
				{ "mData": "id"},
			    { "mData": "coinByExchange.coin.name"},
		      	{ "mData": "invested" },
				{ "mData": "quantity" },
				{ "mData": "satoshiBuy" },
				{ "mData": "buyDate",
					"render": function (data) {
			        	var date = new Date(data);
			        	var month = date.getMonth() + 1;
			        	return (month.length > 1 ? month : "0" + month) + "/" + date.getDate() + "/" + date.getFullYear();
			    	} 
				},
			]
		});
	});
</script>

</head>
<body>

	<div class="col-sm-12">
		<table id="table_id" class="display">
			<thead>
				<tr>
					<th>Id</th>
					<th>Nombre</th>
					<th>Invertido</th>
					<th>Cantidad</th>
					<th>Satoshis Compra</th>
					<th>Fecha compra</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<th>Id</th>
					<th>Nombre</th>
					<th>Invertido</th>
					<th>Cantidad</th>
					<th>Satoshis Compra</th>
					<th>Fecha compra</th>
				</tr>
			</tfoot>
		</table>
	</div>

	<form:form method="POST" modelAttribute="registroEditado" action="/addCoinPerUser">
		<fieldset class="form-group">
			<div class="form-check disabled">
				<div class="row">

					<div class="col-sm-3">
						Nombre cripto
						<select id="coin" class="col-sm-5" name="coinByExchange.coin.id"></select>
					</div>
					<div class="col-sm-3">
						Nombre exchange
						<select id="exchange" class="col-sm-5" name="coinByExchange.exchange.id"></select>
					</div>
					<div class="col-sm-2">
						Cantidad
						<form:input path="quantity" />
					</div>
					<div class="col-sm-2">
						Invertido
						<form:input path="invested" />
					</div>
					<div class="col-sm-2">
						Satoshis
						<form:input path="satoshiBuy" />
					</div>
				</div>
			</div>
		</fieldset>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form:form>
</body>

</html>
