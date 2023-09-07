function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to add
    var isValid = $("#product-form")[0].checkValidity();
                if(!isValid){
                  $("#product-form")[0].reportValidity();
                     return;
                }
	var sellingPrice = $("#product-form input[name=mrp]").val();
        if(sellingPrice==""){
            $.notify("MRP cannot be empty!");
            return;
        }
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
	        $('#add-product-modal').modal('toggle');
            document.getElementById("product-form").reset();
	        $.notify("Product added!", "success");
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
 var isValid = $("#product-edit-form")[0].checkValidity();
                if(!isValid){
                  $("#product-edit-form")[0].reportValidity();
                     return;
                }
    var sellingPrice = $("#product-edit-form input[name=mrp]").val();
        if(sellingPrice==""){
            $.notify("MRP cannot be empty!");
            return;
        }
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
	   	    $('#edit-product-modal').modal('toggle');
	        $.notify("Product updated!", "success");
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
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
        if (brandData.hasOwnProperty(b.brand)) {
                brandData[b.brand].push(b.category);
        } else {
                brandData[b.brand] = [b.category];
        }
    }
      var brandSel = document.getElementById("inputProductBrand");
      var catSel = document.getElementById("inputProductCategory");
      brandSel.length=1;
      for (var brand in brandData) {
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

function getProductList(){
	var url = getProductUrl();
	table.clear().draw();
    table.row.add(["","","<i class='fa fa-refresh fa-spin'></i>","","",""]).draw();
        console.log(url);
        $.ajax({
            url: url,
            type: 'GET',
            success: function(data) {
                displayProductList(data);
            },
            error: function(error) {
                handleAjaxError(error);
            }
        });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];

function processData() {
 var isValid = $("#productFile")[0].checkValidity();
            if(!isValid){
              $("#productFile")[0].reportValidity();
                 return;
            }
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
  for ( var i = 0; i < fileData.length; i++) {
    var row = fileData[i];
    if (row.hasOwnProperty("")) {
                  delete row[""];
                }
            const desiredOrder = ["barcode","brand","category","mrp","name"];
            row = trimLowerCase(row, desiredOrder);
            if(row==null){
                    $.notify("Incorrect TSV format!");
                    return;
             }
    var json = JSON.stringify(row);
        console.log("json = "+ json);
        jsonData.push(json);
        console.log("json data = "+ jsonData)
  }

  var url = getProductUrl() + "/upload";
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
        getProductList();
        $.notify("Products uploaded", "success");
        $('#upload-product-modal').modal('toggle');
    },
    error: function(xhr, textStatus, errorThrown) {
                   var errorList = xhr.responseJSON.errors;
                   const propertyName = "error";
                   fileData.forEach(obj => {
                     obj[propertyName] = "";
                   });
                    for (var i = 0; i < errorList.length; i++) {
                         var index = parseInt(errorList[i].index);
                         console.log(index);
                         if(fileData[index][propertyName]!="")
                           fileData[index][propertyName] += "\t" + errorList[i].error;
                         else
                           fileData[index][propertyName] += errorList[i].error;
                         console.log(errorList[i].error);
                         console.log(fileData[index].error);
                    }
                    errorData = [...fileData];
                    errorCount = errorList.length;
                    updateUploadDialog();
                    $.notify(xhr.responseJSON.message);
          }
  });
}

function downloadErrors() {
 var isValid = $("#productFile")[0].checkValidity();
            if(!isValid){
              $("#productFile")[0].reportValidity();
                 return;
            }
 if(errorData.length == 0){
       $.notify("No errors to download!");
       return;
   }
   writeFileData(errorData, 'Product_Upload_Errors.tsv');
   $.notify("Error list downloaded!", "success");
}

//UI DISPLAY METHODS
function displayProductList(data){
    table.clear().draw();
    var dataRows = [];
	for(var i in data){
	console.log(i);
		var p = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="displayEditProduct(' + p.id + ')"> <i class="fas fa-edit white-icon mr-1"></i>Edit</button>';
        row = [p.barcode, p.name, p.brand, p.category, p.mrp, buttonHtml];
        dataRows.push(row);
	}
		table.rows.add(dataRows).draw();
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
	errorCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

$(document).ready(function(){
    $('#productFile').on('change', function() {
        var fileInput = this;
        resetUploadDialog();
        if (fileInput.files && fileInput.files[0]) {
            var fileName = fileInput.files[0].name;
            $('#productFileName').html(fileName);
        }
    });
});

var errorCount=0;
function updateUploadDialog(){
	$('#errorCount').html("" + errorCount);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=id]").val(data.id);
    $("#product-edit-form input[name=mrp]").val(data.mrp);
    $("#product-edit-form input[name=brand]").val(data.brand);
    $("#product-edit-form input[name=category]").val(data.category);
	$('#edit-product-modal').modal('toggle');
}

function openAddModal(){
    document.getElementById("product-form").reset();
	$('#add-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#open-modal').click(openAddModal);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    getBrandList();
}

$(document).ready(init);

var table;
$(document).ready(function() {
    if($("meta[name=role]").attr("content") == "OPERATOR"){
        table = $('#product-table').DataTable({
        order: [],
        scrollCollapse: true,
        "columns": [
                          { "searchable": true, "orderable": true },
                          { "searchable": true, "orderable": true },
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
                        },
                         { width: '20%', targets: 0 },
                          { width: '20%', targets: 1 },
                          { width: '20%', targets: 2 },
                          { width: '20%', targets: 3 },
                          { width: '20%', targets: 4 },
                        ]
    });
    }
    else if($("meta[name=role]").attr("content") == "ADMIN"){
       table = $('#product-table').DataTable({
               order: [],
               scrollCollapse: true,
               "columns": [
                                 { "searchable": true, "orderable": true },
                                 { "searchable": true, "orderable": true },
                                 { "searchable": true, "orderable": true },
                                 { "searchable": true, "orderable": true },
                                 { "searchable": false, "orderable": true },
                                 { "searchable": false, "orderable": false },
                               ],
               columnDefs:[
                               {
                               targets: '_all',
                                        render:function(data,type,row,meta){
                                           return '<div>'+data+'</div>';
                                        }
                               },
                                { width: '20%', targets: 0 },
                                 { width: '20%', targets: 1 },
                                 { width: '17%', targets: 2 },
                                 { width: '17%', targets: 3 },
                                 { width: '17%', targets: 4 },
                                 { width: '9%', targets: 5 },
                               ]
           });
}
});


$(document).ready(getProductList);

$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('products-nav');
    navItem.classList.add('active');
});

