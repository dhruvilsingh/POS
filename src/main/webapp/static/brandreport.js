function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getBrandList(){
	var url = getBrandUrl();
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

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var b = data[i];
		var row = '<tr>'
		+ '<td>' + b.brandName + '</td>'
		+ '<td>'  + b.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

$(document).ready(getBrandList);
