function getDailySalesUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/daily-sales";
}

function getDailySales(){
        var url = getDailySalesUrl();
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

var button = document.getElementById("download-report");
button.addEventListener("click", function() {
        var url = getDailySalesUrl();
        $.ajax({
           url: url,
           type: 'GET',
           success: function(data) {
                downloadReport(data, 'Daily_Sales_Report.tsv');
           },
           error: handleAjaxError
    });
});


function displayDailySalesList(data){
	table.clear().draw();
	var dataRows=[];
	for(var i in data){
		var ds = data[i];
    row = [ds.date,  ds.orderCount,  ds.orderItemCount, ds.revenue];
                      dataRows.push(row);
  	}
  		table.rows.add(dataRows).draw();
}

var table;
$(document).ready(function() {
    table = $('#daily-sales-table').DataTable({
        order: [],
        "columns": [
                      { "searchable": true, "orderable": true },
                      { "searchable": false, "orderable": true },
                      { "searchable": false, "orderable": true },
                      { "searchable": false, "orderable": true }
                    ],
    });
});


$(document).ready(getDailySales);
$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('reports-nav');
    navItem.classList.add('active');
});