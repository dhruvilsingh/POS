
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

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

//UI DISPLAY METHODS
function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var o = data[i];
		var buttonHtml = '<button onclick="displayOrderItem(' + o.orderId + ')">View Order</button>';
        buttonHtml += '<button onclick="generateInvoice(' + o.orderId + ')">Generate Invoice</button>';
		var row = '<tr>'
		+ '<td>' + o.orderId + '</td>'
		+ '<td>' + o.orderTime + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

$(document).ready(getOrderList);

