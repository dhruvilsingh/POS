
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event){
    $('#add-product-modal').modal('toggle');
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();
    console.log(url);
    console.log(json);
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;
	console.log(url);

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);
    console.log(json);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
  processCount = 0;
  fileData = [];
  errorData = [];
  var file = $('#productFile')[0].files[0];
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

  var url = getProductUrl() + "-list";
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
        getProductList();
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
function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var p = data[i];
		console.log(p);
		var buttonHtml = '<button class="btn btn-warning" onclick="displayEditProduct(' + p.productId + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + p.productBarcode + '</td>'
		+ '<td>'  + p.productName + '</td>'
		+ '<td>'  + p.productBrand + '</td>'
		+ '<td>' + p.productCategory + '</td>'
		+ '<td>' + p.productMrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFIle');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=productName]").val(data.productName);
	$("#product-edit-form input[name=productBarcode]").val(data.productBarcode);
	$("#product-edit-form input[name=id]").val(data.productId);
    $("#product-edit-form input[name=productMrp]").val(data.productMrp);
    $("#product-edit-form input[name=productBrand]").val(data.productBrand);
    $("#product-edit-form input[name=productCategory]").val(data.productCategory);
	$('#edit-product-modal').modal('toggle');
}

function openModal(){
	$('#add-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#open-modal').click(openModal);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
}


$(document).ready(init);
$(document).ready(getProductList);

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('products-nav');
    navItem.classList.add('active');
});

