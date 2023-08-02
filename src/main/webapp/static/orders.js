//Functions to get Url
function getBaseUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function getInvoiceUrl(){
    return getBaseUrl() + "/api/invoices"
}

function getOrderItemUrl(){
	return getBaseUrl() + "/api/order-items";
}

function getCartUrl(){
	return getBaseUrl() + "/api/carts";
}

function getOrderUrl(){
    return getBaseUrl() + "/api/orders";
}


//Functions to get data
function getOrderList(){
	var url = getOrderUrl();
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

function getCartItems(){
	var url = getCartUrl();
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayCartItems(data);
	   },
	   error: handleAjaxError
	});
}


function getOrderItems(id, orderStatus){
        var url = getOrderItemUrl() + "/order/" + id;
        console.log("in display orderItem function " + url);
       $.ajax({
       	   url: url,
       	   type: 'GET',
       	   success: function(data) {
                displayOrderItems(data, orderStatus);
       	   },
       	   error: handleAjaxError
       	});
}

//Button actions
function addCartItem(event){
  var isValid = $("#create-order-form")[0].checkValidity();
                        if(!isValid){
                          $("#create-order-form")[0].reportValidity();
                             return;
                        }
	//Set the values to update
    var sellingPrice = $("#create-order-form input[name=sellingPrice]").val();
        if(sellingPrice==""){
            $.notify("Selling Price cannot be empty!",{
                                                        autoHide: false,
                                                        clickToHide: true
                                                      });
            return;
        }
	var $form = $("#create-order-form");
	var json = toJson($form);
	var url = getCartUrl();
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
	        $.notify("Item added to cart!", "success");
	        document.getElementById("create-order-form").reset();
	   		getCartItems();
	   },
	   error: handleAjaxError
	});
	return false;
}

function addOrderItem(orderId){
    var isValid = $("#add-item-form")[0].checkValidity();
                            if(!isValid){
                              $("#add-item-form")[0].reportValidity();
                                 return;
                            }
    var $form = $("#add-item-form");
    	var json = toJson($form);
    	var url = getOrderItemUrl() + "/" + orderId;
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
    	        $.notify("Item added to Order!", "success");
    	        document.getElementById("add-item-form").reset();
    	   		getOrderItems();
    	   },
    	   error: handleAjaxError
    	});
    	return false;
}



function confirmOrder(event) {
  var url = getOrderUrl();
  $.ajax({
    url: url,
    type: 'POST',
    success: function(response) {
        $('#create-order-modal').modal('toggle');
        $.notify("Order confirmed!", "success");
        getCartItems();
        getOrderList();
    },
    error: handleAjaxError
  });
  return false;
}

function updateCartItem(event){
  var isValid = $("#order-edit-form")[0].checkValidity();
                        if(!isValid){
                          $("#order-edit-form")[0].reportValidity();
                             return;
                        }
    var sellingPrice = $("#order-edit-form input[name=sellingPrice]").val();
        if(sellingPrice==""){
            $.notify("Selling Price cannot be empty!",{
                                                         autoHide: false,
                                                         clickToHide: true
                                                       });
            return;
        }
	var id = $("#order-edit-form input[name=id]").val();
	var url = getCartUrl() + "/" + id;
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
	        $('#edit-order-modal').modal('toggle');
	        $.notify("Order updated!", "success");
	   		getCartItems();
	   },
	   error: handleAjaxError
	});

	return false;
}


function deleteCartItem(id){
	var url = getCartUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	        $.notify("Item deleted!", "info");
	   		getCartItems();
	   },
	   error: handleAjaxError
	});
}

function updateOrderItem(event){
  var isValid = $("#item-edit-form")[0].checkValidity();
                        if(!isValid){
                          $("#item-edit-form")[0].reportValidity();
                             return;
                        }
     var sellingPrice = $("#item-edit-form  input[name=sellingPrice]").val();
            if(sellingPrice==""){
                $.notify("Selling Price cannot be empty!",{
                                                             autoHide: false,
                                                             clickToHide: true
                                                           });
                return;
            }
	var id = $("#item-edit-form input[name=id]").val();
	var orderId = $("#item-edit-form input[name=orderId]").val();
	var url = getOrderItemUrl() + "/" + id;
	console.log(url);
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
	        $('#edit-item-modal').modal('toggle');
	        $.notify("Order updated!", "success");
	   		getOrderItems(orderId);
	   },
	   error: handleAjaxError
	});
	return false;
}

function deleteOrderItem(orderItemId, orderId){
    	var url = getOrderItemUrl() + "/" +orderItemId;
    	$.ajax({
    	   url: url,
    	   type: 'DELETE',
    	   success: function(data) {
    	        $.notify("Item deleted!", "info");
    	   		getOrderItems(orderId);
    	   },
    	   error: handleAjaxError
    	});
}

function cancelOrder(orderId, orderStatus){
    if(orderStatus=="CANCELLED"){
        $.notify("Order is already cancelled!",{
                                                  autoHide: false,
                                                  clickToHide: true
                                                });
        return;
    }
    if(orderStatus=="INVOICED"){
        $.notify("Order is invoiced, can't cancel!",{
                                                       autoHide: false,
                                                       clickToHide: true
                                                     });
        return;
    }
    var url = getOrderUrl() + "/" + orderId;
    	console.log(url);
    	$.ajax({
    	   url: url,
    	   type: 'PUT',
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	        $.notify("Order cancelled!", "info");
    	   		getOrderList();
    	   },
    	   error: handleAjaxError
    	});
    	return false;
}

function getInvoiceData(id, orderStatus){
if(orderStatus == "CANCELLED"){
    $.notify("Order is cancelled, can't generate invoice!",{
                                                              autoHide: false,
                                                              clickToHide: true
                                                            });
}
var url = getInvoiceUrl() + "/" + id;
    $.ajax({
        url: url,
        method: "GET",
        xhrFields: {
            responseType: 'blob'
        },
        success: function(data) {
            var downloadLink = document.createElement('a');
            var url = window.URL.createObjectURL(data);
            downloadLink.href = url;
            downloadLink.download = "Invoice_"+id+".pdf";
            downloadLink.click();
            window.URL.revokeObjectURL(url);
            getOrderList();
            $.notify("Invoice downloaded!","success");
        },
        error: function(xhr, status, error) {

        }
    });
}


//UI DISPLAY METHODS
function displayOrderList(data){
	table.clear().draw();
	var dataRows = [];
	for(var i in data){
		var o = data[i];
        var buttonHtml = '<button class="btn btn-primary mr-1" onclick="viewOrderItems(' + o.id + ', \''  + o.status + '\')"><i class="fas fa-eye white-icon mr-1"></i>View</button>';
        buttonHtml += '<button class="btn btn-danger mr-1" onclick="cancelOrder(' + o.id + ', \''  + o.status + '\')"> <i class="fas fa-times white-icon mr-1"></i>Cancel</button>'
        buttonHtml += '<button class="btn btn-info" onclick="getInvoiceData(' + o.id + ', \''  + o.status + '\')"> <i class="fas fa-print white-icon mr-1"></i>Generate Invoice</button>';
        row = [o.id, o.time, o.status, buttonHtml];
             dataRows.push(row);
	}
	table.rows.add(dataRows).draw();
}

function toggleTableHeader(cartTotal){
    var table = document.getElementById('create-order-table');
     const tbody = table.getElementsByTagName('tbody')[0];
     const thead = table.getElementsByTagName('thead')[0];
      if (tbody.children.length > 0) {
        document.getElementById('cartTotalSpan').textContent = "Total : Rs " + cartTotal.toFixed(2) ;
        thead.style.display = 'table-header-group';
      } else {
        thead.style.display = 'none';
      }
}

function displayCartItems(data){
    document.getElementById('cartTotalSpan').textContent = "" ;
	var $tbody = $('#create-order-table').find('tbody');
	$tbody.empty();
	var cartTotal = 0;
	for(var i in data){
		var p = data[i];
		console.log("itemno" + p.id);
		var buttonHtml = '<button class="btn btn-primary mr-1" onclick="displayEditCartItem(' + p.id + ')"><i class="fas fa-edit white-icon mr-1"></i>Edit</button>'
		buttonHtml += '<button class="btn btn-danger" onclick="deleteCartItem(' + p.id + ')"><i class="fas fa-trash-alt white-icon mr-1"></i>Delete</button>'
		var row = '<tr>'
		+ '<td class="wrap-text">' + p.barcode + '</td>'
		+ '<td>'  + p.quantity + '</td>'
		+ '<td>'  + p.sellingPrice + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		//toggleTableHeader();
        $tbody.append(row);
        cartTotal += p.sellingPrice * p.quantity;
	}
	toggleTableHeader(cartTotal);
}

function displayEditCartItem(id){
	var url = getCartUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayCartItemData(data);
	   },
	   error: handleAjaxError
	});
}

function displayOrderItems(data, orderStatus) {
  document.getElementById('orderTotalSpan').textContent = "" ;
  console.log("entered displayorderitem list");
  var $tbody = $('#order-item-table').find('tbody');
  var headerCell = document.getElementById("actions");
  $tbody.empty();
  if (orderStatus == "INVOICED" || orderStatus == "CANCELLED") {
      headerCell.style.display = 'none';
  }
  else{
      headerCell.style.display = '';
  }
  var orderTotal=0;
  for (var i in data) {
    var oi = data[i];
    var buttonHtml = '<button class="btn btn-primary mr-1" onclick="displayEditOrderItem(' + oi.id + ')"><i class="fas fa-edit white-icon mr-1"></i>Edit</button>';
    buttonHtml += '<button class="btn btn-danger" onclick="deleteOrderItem(' + oi.id + ',' + oi.orderId +')"><i class="fas fa-trash-alt white-icon mr-1"></i>Delete</button>';
    var row = '<tr>'
      + '<td class="wrap-text-product">' + oi.productBarcode + '</td>'
      + '<td class="wrap-text-product">' + oi.productName + '</td>'
      + '<td>' + oi.quantity + '</td>'
      + '<td>' + oi.sellingPrice + '</td>'
      + '<td>' + oi.productMrp + '</td>';
    if (orderStatus == "INVOICED" || orderStatus == "CANCELLED") {
      row += '<td style="display:none;"></td>';
    }
    else {
      row += '<td>' + buttonHtml + '</td>';
    }
    row += '</tr>';
    $tbody.append(row);
    orderTotal += oi.sellingPrice * oi.quantity;
  }
  if (orderStatus != "CANCELLED") {
        document.getElementById('orderTotalSpan').textContent = "Order Total : Rs " + orderTotal.toFixed(2) ;
  }
  console.log("populated data in table");
}


function displayEditOrderItem(itemId){
    var url = getOrderItemUrl() + "/" +itemId;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayOrderItemData(data);
    	   },
    	   error: handleAjaxError
    	});
}

function displayCartItemData(data){
	$("#order-edit-form input[name=barcode]").val(data.barcode);
	$("#order-edit-form input[name=id]").val(data.id);
    $("#order-edit-form input[name=quantity]").val(data.quantity);
    $("#order-edit-form input[name=sellingPrice]").val(data.sellingPrice);
	$('#edit-order-modal').modal('toggle');
}

function displayOrderItemData(data){
	$('#editBarcode2').val(data.productBarcode);
	$('#editName').val(data.productName);
    $('#editMrp').val(data.productMrp);
	$("#item-edit-form input[name=id]").val(data.id);
	$("#item-edit-form input[name=orderId]").val(data.orderId);
    $("#item-edit-form input[name=quantity]").val(data.quantity);
    $("#item-edit-form input[name=sellingPrice]").val(data.sellingPrice);
	$('#edit-item-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addCartItem);
	$('#update-order').click(updateCartItem);
	$('#confirm-order').click(confirmOrder);
	$('#update-item').click(updateOrderItem);
	$('#create-order').click(openModal);
    getOrderList();
}

function viewOrderItems(orderId, orderStatus){
    var itemAddForm = document.getElementById('add-item-form');
    if(orderStatus=='CREATED'){
         itemAddForm.style.display = '';
         $('#add-order-item').click(addOrderItem(orderId));
    }
    else{
         itemAddForm.style.display = 'none';
    }
    document.getElementById('orderIdSpan').textContent = "Order ID : " + orderId + " ("+ orderStatus+")";
    $('#view-order-modal').modal('toggle');
    getOrderItems(orderId, orderStatus);
}

function openModal(){
    $('#create-order-modal').modal('toggle');
    getCartItems();
}

$(document).ready(init);

var table;
$(document).ready(function() {
    table = $('#order-table').DataTable({
        order: [],
         "columns": [
                                  { "searchable": true, "orderable": true },
                                  { "searchable": true, "orderable": true },
                                  { "searchable": true, "orderable": true },
                                  { "searchable": false, "orderable": false }
                                ],
         columnDefs: [
              { width: '20%', targets: 0 },
              { width: '20%', targets: 1 },
              { width: '20%', targets: 2 },
              { width: '40%', targets: 3 },
            ]
    });
});

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('orders-nav');
    navItem.classList.add('active');
});


