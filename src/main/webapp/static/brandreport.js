function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/brand";
}

function getBrandList(){
	var url = getBrandReportUrl();
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}

var button = document.getElementById("download-report");
button.addEventListener("click", function() {
    var url = getBrandReportUrl();
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
            downloadReport(data, 'Brand_Report.tsv');
       },
       error: handleAjaxError
    });
});

function displayBrandList(data){
	table.clear().draw();
		var dataRows=[];
	for(var i in data){
		var b = data[i];
		     row = [b.brand, b.category];
                    dataRows.push(row);
	}
		table.rows.add(dataRows).draw();
}

$(document).ready(getBrandList);

var table;
$(document).ready(function() {
    table = $('#brand-table').DataTable({
        order: [],
        columnDefs:[
                { width: '50%', targets: 0 },
                { width: '50%', targets: 1 },
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
