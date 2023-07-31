var table;

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

//BUTTON ACTIONS
function addBrand(event){
	var isValid = $("#brand-form")[0].checkValidity();
            if(!isValid){
              $("#brand-form")[0].reportValidity();
                 return;
            }
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
	        $('#add-brand-modal').modal('toggle');
	        document.getElementById("brand-form").reset();
	        $.notify("Brand added!", "success");
	   		getBrandList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){
    var isValid = $("#brand-edit-form")[0].checkValidity();
                if(!isValid){
                  $("#brand-edit-form")[0].reportValidity();
                     return;
                }
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
	        $('#edit-brand-modal').modal('toggle');
	        $.notify("Brand updated!", "success");
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
    var isValid = $("#brandFile")[0].checkValidity();
            if(!isValid){
              $("#brandFile")[0].reportValidity();
                 return;
            }
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
    if (row.hasOwnProperty("")) {
          delete row[""];
        }
    const desiredOrder = ["brand", "category"];
    row = trimLowerCase(row, desiredOrder);
    if(row==null){
            $.notify("Incorrect TSV format!");
            return;
     }
    var json = JSON.stringify(row);
        jsonData.push(json);
  }
  var url = getBrandUrl() + "/upload";
  // Make ajax call
  $.ajax({
    url: url,
    type: 'POST',
    data: '[' + jsonData.join(',') + ']',
    headers: {
      'Content-Type': 'application/json'
    },
    success: function(response) {
        getBrandList();
        $.notify("Brands uploaded", "success");
        $('#upload-brand-modal').modal('toggle');
    },
    error: function(xhr, textStatus, errorThrown) {
                 var errorList = xhr.responseJSON.errors;
                 for (var i = 0; i < errorList.length; i++) {
                      var errorRow = errorList[i];
                      errorData.push(errorRow);
                 }
                 updateUploadDialog();
                 $.notify(xhr.responseJSON.message);
    }
  });
}

function downloadErrors() {
  var isValid = $("#brandFile")[0].checkValidity();
            if(!isValid){
              $("#brandFile")[0].reportValidity();
                 return;
            }
 if(errorData.length == 0){
      $.notify("No errors to download!");
      return;
  }
  writeFileData(errorData, 'Brand_Upload_Errors.tsv');
  $.notify("Error list downloaded!", "success");
}


//UI DISPLAY METHODS
function displayBrandList(data){
	table.clear().draw();
	var dataRows=[];
	for(var i in data){
		var b = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="displayEditBrand(' + b.id + ')"><i class="fas fa-edit white-icon mr-1"></i>Edit</button>';
        row = [b.brand, b.category, buttonHtml];
        dataRows.push(row);
	}
	table.rows.add(dataRows).draw();
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

$(document).ready(function(){
    $('#brandFile').on('change', function() {
        var fileInput = this;
        if (fileInput.files && fileInput.files[0]) {
            var fileName = fileInput.files[0].name;
            processCount = 0;
            fileData = [];
            errorData = [];
            updateUploadDialog();
            $('#brandFileName').html(fileName);
        }
        else{
             resetUploadDialog();
        }
    });
});


function updateUploadDialog(){
	$('#errorCount').html("" + errorData.length);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
}

function openAddModal(){
    document.getElementById("brand-form").reset();
    $('#add-brand-modal').modal('toggle');
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
	$('#open-modal').click(openAddModal);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	getBrandList();
}

$(document).ready(function() {
    if($("meta[name=role]").attr("content") == "OPERATOR"){
        table = $('#brand-table').DataTable({
            order: [],
            "columns": [
                  { "searchable": true, "orderable": true },
                  { "searchable": true, "orderable": true },
                ],
            columnDefs:[
            {
            targets: '_all',
                     render:function(data,type,row,meta){
                        return '<div>'+data+'</div>';
                     }
            },
                          { width: '50%', targets: 0 },
                          { width: '50%', targets: 1 },
            ],
        });
        }
    else if($("meta[name=role]").attr("content") == "ADMIN"){
        table = $('#brand-table').DataTable({
                    order: [],
                    "columns": [
                          { "searchable": true, "orderable": true },
                          { "searchable": true, "orderable": true },
                          { "searchable": false, "orderable": false }
                        ],
                    columnDefs:[
                    {
                    targets: '_all',
                             render:function(data,type,row,meta){
                                return '<div>'+data+'</div>';
                             }
                    },
                                  { width: '40%', targets: 0 },
                                  { width: '40%', targets: 1 },
                                  { width: '20%', targets: 2 },
                    ],
                });
    }
});


$(document).ready(init);

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('brands-nav');
    navItem.classList.add('active');
});