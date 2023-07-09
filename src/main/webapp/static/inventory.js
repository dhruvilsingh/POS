function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function addInventory(event){
                var id = $("#inventory-add-form input[name=id]").val();
            	var url = getInventoryUrl() + "/" + id;
                	$.ajax({
                	   url: url,
                	   type: 'GET',
                	   success: function(data) {
                            add(data);
                	   },
                	   error: handleAjaxError
                });
}

function add(data){
    console.log("in add inventory");
        	$('#add-inventory-modal').modal('toggle');
        	var id = $("#inventory-add-form input[name=id]").val();
        	var url = getInventoryUrl() + "/" + id;
        	var quantity = $("#inventory-add-form input[name=productQuantity]").val();
            data.productQuantity =  data.productQuantity + parseInt(quantity);
            var json = JSON.stringify(data);
        	$.ajax({
        	   url: url,
        	   type: 'PUT',
        	   data: json,
        	   headers: {
               	'Content-Type': 'application/json'
               },
        	   success: function(response) {
        	        console.log("put done");
        	   		getInventoryList();
        	   },
        	   error: handleAjaxError
        	});

        	return false;
}


function getInventoryList(){
	var url = getInventoryUrl();
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
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
  var file = $('#inventoryFile')[0].files[0];
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

  var url = getInventoryUrl() + "-list";
  console.log("url = "+url);

  // Make ajax call
  $.ajax({
    url: url,
    type: 'POST',
    data: '[' + jsonData.join(',') + ']',
    headers: {
      'Content-Type': 'application/json'
    },
    success: function(data){
        for(var i = 0; i < data.length; i++){
            var row = data[i];
            console.log(row);
            console.log(row.error);
            if(row.error != null)
                errorData.push(row);
        }
        getInventoryList();
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
function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var inv = data[i];
		var buttonHtml = '<button class="btn btn-warning mr-1" onclick="getInventory(' + inv.productId + ',' + 1 + ')">Edit</button>'
	    buttonHtml += '<button class="btn btn-success" onclick="getInventory(' + inv.productId + ',' + 2 + ')">Add</button>'
		var row = '<tr>'
		+ '<td>' + inv.productBarcode + '</td>'
		+ '<td>' + inv.productQuantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function getInventory(id,type){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
            displayInventory(data,type);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data ,type){
	if(type == 1){
	    $("#inventory-edit-form input[name=id]").val(data.productId);
        $("#inventory-edit-form input[name=productBarcode]").val(data.productBarcode);
	    $("#inventory-edit-form input[name=productQuantity]").val(data.productQuantity);
	    $('#edit-inventory-modal').modal('toggle');
	}
	else if(type==2){
	    $("#inventory-add-form input[name=id]").val(data.productId);
        $("#inventory-add-form input[name=productBarcode]").val(data.productBarcode);
	    $('#add-inventory-modal').modal('toggle');
	}
}

//INITIALIZATION CODE
function init(){
	$('#update-inventory').click(updateInventory);
	$('#add-inventory').click(addInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getInventoryList);

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('inventory-nav');
    navItem.classList.add('active');
});

