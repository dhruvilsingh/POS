function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderitem/";
}

var data = JSON.parse(sessionStorage.getItem('data'));
displayOrderItemList(data);

function updateItem(event){
	$('#edit-item-modal').modal('toggle');
	//Get the ID
	var id = $("#item-edit-form input[name=orderItemId]").val();
	var orderId = $("#item-edit-form input[name=orderId]").val();
	var url = getOrderItemUrl() + id;
	console.log(url);
	//Set the values to update
	var $form = $("#item-edit-form");
	var json = toJson($form);
    console.log(json);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getOrderItemList(orderId);
	   },
	   error: handleAjaxError
	});
	return false;
}

function deleteItem(orderItemId, orderId){
    	var url = getOrderItemUrl() + orderItemId;
    	$.ajax({
    	   url: url,
    	   type: 'DELETE',
    	   success: function(data) {
    	   		getOrderItemList(orderId);
    	   },
    	   error: handleAjaxError
    	});
}


//UI DISPLAY METHODS
function displayOrderItemList(data){
    console.log("entered displayorderitem list");
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var oi = data[i];
		var buttonHtml = '<button class="btn btn-warning mr-1" onclick="displayEditItem(' + oi.orderItemId + ')">Edit</button>';
		buttonHtml += '<button class="btn btn-danger" onclick="deleteItem(' + oi.orderItemId + ',' + oi.orderId +')">Delete</button>';
		var row = '<tr>'
		+ '<td>' + oi.productBarcode + '</td>'
		+ '<td>' + oi.productName + '</td>'
		+ '<td>' + oi.productQuantity + '</td>'
		+ '<td>' + oi.productSP + '</td>'
		+ '<td>' + oi.productMrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        console.log("populated data in table");
	}
}

function displayEditItem(itemId){
    var url = getOrderItemUrl() + itemId;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayItem(data);
    	   },
    	   error: handleAjaxError
    	});
}

function displayItem(data){
	$('#editBarcode').val(data.productBarcode);
	$('#editName').val(data.productName);
    $('#editMrp').val(data.productMrp);
	$("#item-edit-form input[name=orderItemId]").val(data.orderItemId);
	$("#item-edit-form input[name=orderId]").val(data.orderId);
	$("#item-edit-form input[name=productId]").val(data.productId);
    $("#item-edit-form input[name=quantity]").val(data.productQuantity);
    $("#item-edit-form input[name=sellingPrice]").val(data.productSP);
	$('#edit-item-modal').modal('toggle');
}

function init(){
	$('#update-item').click(updateItem);
}

$(document).ready(init);