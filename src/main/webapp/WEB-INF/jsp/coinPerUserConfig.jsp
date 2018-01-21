<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="header.jsp" />

<script>
var datatable;
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
		        
		        data: function (term) {
		            return {
		                term: term
		            };
		        },
		        processResults: function (data, params) {
		            return {
		                results: $.map(data, function(obj) {
		                	return { id: obj.id, text: obj.formatedName };
		                })
		            };
		        }
		    }
		});
		
		
		datatable = $('#table_id').DataTable({
			"sAjaxSource": "/coinPerUserConfig/list",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    { "mData": "coinByExchange.coin.formatedName"},
			    { "mData": "coinByExchange.exchange.name"},
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
				{ "mData": "id",
					"render": function (data) {
			        	return '<a href="#" onclick="deleteCoin('+data+');">1<a/>';
			    	}
				},
			]
		});
		
		   $('button[type=submit]').click(function(e) {
			   
			      //Prevent default submission of form
			      e.preventDefault();
			      
			      //Remove all errors
			      $('input').next().remove();
			      
			      $.post({
			         url : 'coinPerUserConfig/add',
			         data : $('form[name=coinPerUserAdd]').serialize(),
			         success : function(res) {
			        	 datatable.ajax.reload();
			         
			            if(!res.validated){
			              //Set error messages
			              $.each(res.errorMessages,function(key,value){
			  	            $('input[name='+key+']').after('<span class="error">'+value+'</span>');
			              });
			            }
			         }
			      })
			   });
		   
		   $('#buyDate').datepicker();
		
	});
	
	function deleteCoin(coinPerUserID) {
		$.post( "coinPerUserConfig/removeValue", { coinPerUserID: coinPerUserID }, function( data ) {
			datatable.ajax.reload();
		});
		return false;
	}
</script>

	<div class="col-sm-12">
		<table id="table_id" class="display">
			<thead>
				<tr>
					<th>Nombre</th>
					<th>Exchange</th>
					<th>Invertido</th>
					<th>Cantidad</th>
					<th>Satoshis Compra</th>
					<th>Fecha compra</th>
					<th>Eliminar</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<th>Nombre</th>
					<th>Exchange</th>
					<th>Invertido</th>
					<th>Cantidad</th>
					<th>Satoshis Compra</th>
					<th>Fecha compra</th>
					<th>Eliminar</th>
				</tr>
			</tfoot>
		</table>
	</div>
	<spring:hasBindErrors name="userForm">
		<c:forEach var="error" items="${errors.allErrors}">
			<b><spring:message message="${error}" /></b>
			<br />
		</c:forEach>
	</spring:hasBindErrors>
	<form:form method="POST" name="coinPerUserAdd" modelAttribute="registroEditado" action="/coinPerUserConfig/add">

		<fieldset class="form-group">
			<div class="form-check disabled">
				<div class="row">

					<div class="col-sm-2">
						<div class="row">
							<div class="col-sm-12">
								Nombre cripto
								<form:select id="coin" class="col-sm-12" path="coinByExchange.coin.id"></form:select>
							</div>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="row">
							<div class="col-sm-12">
								Nombre exchange
								<form:select id="exchange" class="col-sm-12" path="coinByExchange.exchange.id"></form:select>
							</div>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="row">
							<div class="col-sm-6">
								Cantidad
								<form:input path="quantity" />
							</div>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="row">
							<div class="col-sm-6">
								Invertido
								<form:input path="invested" />
							</div>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="row">
							<div class="col-sm-6">
								Satoshis
								<form:input path="satoshiBuy" />
							</div>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="row">
							<div class="col-sm-6">
								Fecha de compra
								<form:input id="buyDate" path="buyDate" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</fieldset>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form:form>
<jsp:include page="footer.jsp" />
