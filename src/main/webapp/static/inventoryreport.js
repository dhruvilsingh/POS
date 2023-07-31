function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/reports/inventory";
}

function getInventoryReport(){
        var url = getInventoryReportUrl();
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

var button = document.getElementById("download-report");
button.addEventListener("click", function() {
        var url = getInventoryReportUrl();
        $.ajax({
           url: url,
           type: 'GET',
           success: function(data) {
                downloadReport(data, 'Inventory_Report.tsv');
           },
           error: handleAjaxError
    });
});

function displayInventoryReport(data){
    table.clear().draw();
    var dataRows=[];
	for(var i in data){
		var ir = data[i];
                        row = [ir.brand,ir.category,ir.quantity];
                                dataRows.push(row);
    }
    	table.rows.add(dataRows).draw();
}


$(document).ready(getInventoryReport);

var table;
$(document).ready(function() {
    table = $('#inventory-table').DataTable({
        order: [],
                "columns": [
                                      { "searchable": true, "orderable": true },
                                      { "searchable": true, "orderable": true },
                                      { "searchable": false, "orderable": true },
                                    ],
                columnDefs:[
                {
                targets: '_all',
                         render:function(data,type,row,meta){
                            return '<div>'+data+'</div>';
                         }
                }
                ]
    });
});

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('reports-nav');
    navItem.classList.add('active');
});