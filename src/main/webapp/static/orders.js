function getBaseUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function getOrderList(){
	var url = getBaseUrl() + "/api/order";
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function generateInvoice(id){
    	var url = getBaseUrl() + "/api/invoicesender/" + id;
    	console.log(url);
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		sendData(data);
    	   },
    	   error: handleAjaxError
    	});
}

function sendData(data){
        var url = getBaseUrl() + "/api/send-invoice";
        console.log(url);
        var json = JSON.stringify(data);
        console.log(json);
          $.ajax({
            url: url,
            type: 'POST',
            data: json,
        	headers: {
               'Content-Type': 'application/json'
            },
            success: function(response) {
               console.log("data sent from order js");
            },
            error: handleAjaxError
          });
          return false;
}

//UI DISPLAY METHODS
function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var o = data[i];
		var buttonHtml = '<button class="btn btn-info mr-1" onclick="getOrderItemList(' + o.orderId + ')">View Order</button>';
        buttonHtml += '<button class="btn btn-secondary" id="generate-invoice" onclick="generateInvoice(' + o.orderId + ')">Generate Invoice</button>';
		var row = '<tr>'
		+ '<td>' + o.orderId + '</td>'
		+ '<td>' + o.orderTime + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function openModal(){
    $('#create-order-modal').modal('toggle');
    getProductList();
}

$(document).ready(function(){
    getOrderList();
    $('#create-order').click(openModal);
});



//dfcghvjbknlmjhugytfrdtfgjhklnhghfdxghjkjhcfgfhj






function getCreateOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/cart";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#create-order-form");
	var json = toJson($form);
	var url = getCreateOrderUrl();
    console.log(url);
    console.log(json);
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});
	return false;
}

function confirmOrder(event) {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  var url = baseUrl + "/api/orderitem";
  $.ajax({
    url: url,
    type: 'POST',
    success: function(response) {
        getProductList();
        getOrderList();
    },
    error: handleAjaxError
  });
  return false;
}

function updateProduct(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-edit-form input[name=id]").val();
	var url = getCreateOrderUrl() + "/" + id;
	console.log(url);
	//Set the values to update
	var $form = $("#order-edit-form");
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
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getCreateOrderUrl();
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteItem(id){
	var url = getCreateOrderUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});
}



//UI DISPLAY METHODS
function displayProductList(data){
	var $tbody = $('#create-order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var p = data[i];
		console.log("itemno" + p.itemNo);
		var buttonHtml = '<button class="btn btn-warning mr-1" onclick="displayEditProduct(' + p.itemNo + ')">Edit</button>'
		buttonHtml += '<button class="btn btn-danger" onclick="deleteItem(' + p.itemNo + ')">Delete</button>'
		var row = '<tr>'
		+ '<td>' + p.productBarcode + '</td>'
		+ '<td>'  + p.productQuantity + '</td>'
		+ '<td>'  + p.productSP + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditProduct(id){
	var url = getCreateOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function displayProduct(data){
	$("#order-edit-form input[name=productBarcode]").val(data.productBarcode);
	$("#order-edit-form input[name=id]").val(data.itemNo);
    $("#order-edit-form input[name=productQuantity]").val(data.productQuantity);
    $("#order-edit-form input[name=productSP]").val(data.productSP);
	$('#edit-order-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-order').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#confirm-order').click(confirmOrder);
	$('#generate-invoice').click(generateInvoice);
}

$(document).ready(init);



