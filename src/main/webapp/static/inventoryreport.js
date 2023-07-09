function getInventoryReport(){
        var baseUrl = $("meta[name=baseUrl]").attr("content")
        var url = baseUrl + "/api/inventoryreport/";
        console.log("in display dailysales function " + url);
       $.ajax({
       	   url: url,
       	   type: 'GET',
       	   success: function(data) {
       	   		displayInventoryReport(data);
       	   },
       	   error: handleAjaxError
       	});
}

function displayInventoryReport(data){
    console.log("entered display daily sales list");
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var ir = data[i];
		var row = '<tr>'
		+ '<td>' + ir.brand + '</td>'
		+ '<td>' + ir.category + '</td>'
		+ '<td>' + ir.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
        console.log("populated data in table");
	}
}

$(document).ready(getInventoryReport);

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('reports-nav');
    navItem.classList.add('active');
});