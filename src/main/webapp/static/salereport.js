function getSaleReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/sale";
}

function getSaleReport(){
    var url = getSaleReportUrl();
    console.log(url);
    setDates();
    var $form = $("#sale-report-form");
    var json = toJson($form);
    console.log(json);
    $.ajax({
           url: url,
           type: 'POST',
           data: json,
           headers: {
            'Content-Type': 'application/json'
           },
           success: function(data) {
                displayReportData(data);
           },
           error: handleAjaxError
        });

        return false;
}

var button = document.getElementById("download-report");
button.addEventListener("click", function() {
         var url = getSaleReportUrl();
         setDates();
           var $form = $("#sale-report-form");
            var json = toJson($form);
            console.log(json);
            $.ajax({
                   url: url,
                   type: 'POST',
                   data: json,
                   headers: {
                    'Content-Type': 'application/json'
                   },
                   success: function(data) {
                        var endDate =  $("#sale-report-form input[name=endDate]").val() ;
                        var startDate =  $("#sale-report-form input[name=startDate]").val();
                        downloadReport(data, 'Revenue_Report_'+startDate+'_To_'+endDate+'.tsv');
                   },
                   error: handleAjaxError
                });
                return false;
});

function displayReportData(data){
    table.clear().draw();
    var dataRows = [];
        for(var i in data){
            console.log(i);
            var b = data[i];
             row = [b.brand, b.category, b.quantity, b.revenue];
            dataRows.push(row);
        }
                     table.rows.add(dataRows).draw();
}

function getBrandList(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
    var url = baseUrl + "/api/brands";
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		dropdown(data);
	   },
	   error: handleAjaxError
	});
}

function dropdown(data){
    var brandData = {};
    for (var i in data){
        var b = data[i];
        console.log(b);
        if (brandData.hasOwnProperty(b.brand)) {
                brandData[b.brand].push(b.category);
        } else {
                brandData[b.brand] = [b.category];
        }
    }
    console.log(brandData);
      var brandSel = document.getElementById("inputBrand");
      var catSel = document.getElementById("inputCategory");
      brandSel.length=1;
      for (var brand in brandData) {
        console.log(brand);
        brandSel.options[brandSel.options.length] = new Option(brand, brand);
      }
      brandSel.onchange = function() {
          var selectedBrand = this.value;
          catSel.length = 1; // Reset category dropdown
          var categories = brandData[selectedBrand];
          console.log(categories);
          for (var i = 0; i < categories.length; i++) {
                  catSel.options[catSel.options.length] = new Option(categories[i], categories[i]);
          }
      }
}

function setDates(){
       var endDate =  $("#sale-report-form input[name=endDate]").val() ;
        var startDate =  $("#sale-report-form input[name=startDate]").val();
       var currentDate = new Date();
        if(endDate == ""){
            const year = currentDate.getFullYear();
            const month = currentDate.getMonth() + 1;
            const day = currentDate.getDate();
            const dateString = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
            endDate = dateString;
            console.log(endDate);
            if(startDate == ""){
                currentDate.setDate(currentDate.getDate() - 30);
                const year = currentDate.getFullYear();
                const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
                const day = currentDate.getDate().toString().padStart(2, '0');
                const formattedDate = `${year}-${month}-${day}`;
                startDate = formattedDate;
                console.log(startDate);
            }
        }
        else {
            if (startDate == "") {
                currentDate = new Date(endDate);
                currentDate.setDate(currentDate.getDate() - 30);
                const year = currentDate.getFullYear();
                const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
                const day = currentDate.getDate().toString().padStart(2, '0');
                const formattedDate = `${year}-${month}-${day}`;
                startDate = formattedDate;
                console.log(startDate);
            }
        }
    $("#sale-report-form input[name=endDate]").val(endDate);
    $("#sale-report-form input[name=startDate]").val(startDate);
}


function init(){
	$('#get-report').click(getSaleReport);
	getSaleReport();
	getBrandList();
}

$(document).ready(init);

var table;
$(document).ready(function() {
    table = $('#sale-report-table').DataTable({
        order: [],
        "columns": [
                      { "searchable": true, "orderable": true },
                      { "searchable": true, "orderable": true },
                      { "searchable": false, "orderable": true },
                      { "searchable": false, "orderable": true }
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
