function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}


//BUTTON ACTIONS
function updateInventory(event){
   var isValid = $("#inventory-edit-form")[0].checkValidity();
                        if(!isValid){
                          $("#inventory-edit-form")[0].reportValidity();
                             return;
                        }
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
	        $('#edit-inventory-modal').modal('toggle');
	        $.notify("Inventory updated!", "success");
	   		getInventoryList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function addInventory(event){
  var isValid = $("#inventory-add-form")[0].checkValidity();
                        if(!isValid){
                          $("#inventory-add-form")[0].reportValidity();
                             return;
                        }
                var $form = $("#inventory-add-form");
                var quantity = $("#inventory-add-form input[name=quantity]").val();
                if(quantity==""){
                    $.notify("Quantity cannot be empty!");
                    return;
                }
                var json = toJson($form);
            	var url = getInventoryUrl();
                $.ajax({
                       url: url,
                       type: 'PUT',
                       data: json,
                       headers: {
                        'Content-Type': 'application/json'
                       },
                       success: function(response) {
                            $('#add-inventory-modal').modal('toggle');
                            document.getElementById("inventory-add-form").reset();
                            $.notify("Inventory added!", "success");
                            console.log("post done");
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

function processData() {
    var isValid = $("#inventoryFile")[0].checkValidity();
            if(!isValid){
              $("#inventoryFile")[0].reportValidity();
                 return;
            }
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

  for (var i = 0; i < fileData.length; i++) {
    var row = fileData[i];
     if (row.hasOwnProperty("")) {
              delete row[""];
            }
        const desiredOrder = ["barcode", "quantity"];
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

  var url = getInventoryUrl() + "/upload";
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
        getInventoryList();
        $.notify("Inventory uploaded", "success");
        updateUploadDialog();
        $('#upload-inventory-modal').modal('toggle');
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
  var isValid = $("#inventoryFile")[0].checkValidity();
            if(!isValid){
              $("#inventoryFile")[0].reportValidity();
                 return;
            }
  if(errorData.length == 0){
      $.notify("No errors to download!");
      return;
  }
  writeFileData(errorData, 'Inventory_Upload_Errors.tsv');
  $.notify("Error list downloaded!", "success");
}

//UI DISPLAY METHODS
function displayInventoryList(data){
	table.clear().draw();
	var dataRows=[];
	for(var i in data){
		var inv = data[i];
		var buttonHtml = '<button class="btn btn-primary mr-1" onclick="getInventory(' + inv.productId + ')"><i class="fas fa-edit white-icon mr-1"></i>Edit</button>'
            row = [inv.barcode, inv.quantity, buttonHtml];
                    dataRows.push(row);
	}
			table.rows.add(dataRows).draw();
}

function getInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
            displayInventory(data);
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
	fileData = [];
	errorData = [];
	errorCount = 0;
	//Update counts
	updateUploadDialog();
}

$(document).ready(function(){
    $('#inventoryFile').on('change', function() {
        var fileInput = this;
        if (fileInput.files && fileInput.files[0]) {
            var fileName = fileInput.files[0].name;
            fileData = [];
            errorData = [];
            errorCount = 0;
            updateUploadDialog();
            $('#inventoryFileName').html(fileName);
        }
        else{
             resetUploadDialog();
        }
    });
});

var errorCount = 0;
function updateUploadDialog(){
	$('#errorCount').html("" + errorCount);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	    $("#inventory-edit-form input[name=id]").val(data.productId);
        $("#inventory-edit-form input[name=barcode]").val(data.barcode);
	    $("#inventory-edit-form input[name=quantity]").val(data.quantity);
	    $('#edit-inventory-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#update-inventory').click(updateInventory);
	$('#add-inventory').click(addInventory);
	$('#add-data').click(function(){
	        document.getElementById("inventory-add-form").reset();
            $('#add-inventory-modal').modal('toggle');
	});
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
}

$(document).ready(init);
$(document).ready(getInventoryList);

var table;
$(document).ready(function() {
    if($("meta[name=role]").attr("content") == "ADMIN"){
        table = $('#inventory-table').DataTable({
            order: [],
            "columns": [
                          { "searchable": true, "orderable": true },
                          { "searchable": false, "orderable": true },
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
            ]
        });
    }
    else if($("meta[name=role]").attr("content") == "OPERATOR"){
            table = $('#inventory-table').DataTable({
                order: [],
                "columns": [
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
                              { width: '50%', targets: 0 },
                              { width: '50%', targets: 1 },
                ]
            });
    }

});


$(document).ready(function(){
    const navbarContainer = document.getElementById('navbarContainer');
    const navItems = navbarContainer.querySelectorAll('.navbar-nav > .nav-item');
    navItems.forEach(item => {
       item.classList.remove('active')});
    const navItem = document.getElementById('inventory-nav');
    navItem.classList.add('active');
});


