function getSaleReportBaseUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sale-report";
}

function getSaleReport(){
    var url = getSaleReportBaseUrl();
    console.log(url);
    var $form = $("#sale-report-form");
    var json = toJson($form);
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

function displayReportData(data){
    var $tbody = $('#sale-report-table').find('tbody');
    	$tbody.empty();
    	console.log(data);
        for(var i in data){
            console.log(i);
            var b = data[i];
            console.log(b);
    		var row = '<tr>'
    		+ '<td>' + b.brandName + '</td>'
    		+ '<td>'  + b.category + '</td>'
    		+ '<td>'  + b.quantity + '</td>'
    		+ '<td>' + b.revenue + '</td>'
    		+ '</tr>';
            $tbody.append(row);
        }
}

function getBrandList(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
    var url = baseUrl + "/api/brand";
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
        if (brandData.hasOwnProperty(b.brandName)) {
                brandData[b.brandName].push(b.category);
        } else {
                brandData[b.brandName] = [b.category];
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

function init(){
	$('#get-report').click(getSaleReport);
	getSaleReport();
	getBrandList();
}

$(document).ready(init);
$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('reports-nav');
    navItem.classList.add('active');
});
