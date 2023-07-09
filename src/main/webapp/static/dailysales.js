function displayDailySales(){
        var baseUrl = $("meta[name=baseUrl]").attr("content")
        var url = baseUrl + "/api/dailysales/";
        console.log("in display dailysales function " + url);
       $.ajax({
       	   url: url,
       	   type: 'GET',
       	   success: function(data) {
       	   		displayDailySalesList(data);
       	   },
       	   error: handleAjaxError
       	});
}

function displayDailySalesList(data){
    console.log("entered display daily sales list");
	var $tbody = $('#daily-sales-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var ds = data[i];
		var row = '<tr>'
		+ '<td>' + ds.date + '</td>'
		+ '<td>' + ds.orderCount + '</td>'
		+ '<td>' + ds.orderItemCount + '</td>'
		+ '<td>' + ds.revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
        console.log("populated data in table");
	}
}

$(document).ready(displayDailySales);
$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('reports-nav');
    navItem.classList.add('active');
});