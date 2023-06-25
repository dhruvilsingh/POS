
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
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var p = data[i];
		console.log("itemno" + p.itemNo);
		var buttonHtml = '<button onclick="displayEditProduct(' + p.itemNo + ')">edit</button>'
		buttonHtml += '<button onclick="deleteItem(' + p.itemNo + ')">delete</button>'
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
}

$(document).ready(init);
$(document).ready(getProductList);

