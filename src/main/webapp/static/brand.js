
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	$('#add-brand-modal').modal('toggle');
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   },
	   error: handleAjaxError
	});
	return false;
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

//FILE UPLOAD METHODS

var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
  processCount = 0;
  fileData = [];
  errorData = [];
  var file = $('#brandFile')[0].files[0];
  readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
  fileData = results.data;
  uploadRows();
}

function uploadRows() {
  var jsonData = [];

  for ( processCount = 0; processCount < fileData.length; processCount++) {
    var row = fileData[processCount];
        console.log("row = " + row);
    var json = JSON.stringify(row);
        console.log("json = "+ json);
        jsonData.push(json);
        console.log("json data = "+ jsonData)
  }

  var url = getBrandUrl() + "-list";
  console.log("url = "+url);

  // Make ajax call
  $.ajax({
    url: url,
    type: 'POST',
    data: '[' + jsonData.join(',') + ']',
    headers: {
      'Content-Type': 'application/json'
    },
    success: function(data) {
        for(var i = 0; i < data.length; i++){
            var row = data[i];
            console.log(row);
            console.log(row.error);
            if(row.error != null)
                errorData.push(row);
        }
        getBrandList();
        updateUploadDialog();
        if(errorData.length !=0)
            downloadErrors();
    },
    error: handleAjaxError
  });
}

function downloadErrors() {
  writeFileData(errorData);
}


//UI DISPLAY METHODS
function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var b = data[i];
		console.log(b.brandId + b.brandName + b.category);
		var buttonHtml = '<button class="btn btn-warning" onclick="displayEditBrand(' + b.brandId + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + b.brandName + '</td>'
		+ '<td>'  + b.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
}

function openModal(){
    $('#add-brand-modal').modal('toggle');
}

function displayBrand(data){
	$("#brand-edit-form input[name=brandName]").val(data.brandName);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.brandId);
	$('#edit-brand-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
	$('#open-modal').click(openModal);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
}


$(document).ready(init);
$(document).ready(getBrandList);

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('brands-nav');
    navItem.classList.add('active');
});