
var data = JSON.parse(localStorage.getItem('data'));
displayOrderItemList(data);

//UI DISPLAY METHODS
function displayOrderItemList(data){
    console.log("entered displayorderitem list");
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var oi = data[i];
		var row = '<tr>'
		+ '<td>' + oi.productBarcode + '</td>'
		+ '<td>' + oi.productName + '</td>'
		+ '<td>' + oi.productQuantity + '</td>'
		+ '<td>' + oi.productSP + '</td>'
		+ '<td>' + oi.productMrp + '</td>'
		+ '</tr>';
        $tbody.append(row);
        console.log("populated data in table");
	}
}