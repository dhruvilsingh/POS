//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
    $.notify(response.message,{
                                 autoHide: false,
                                 clickToHide: true
                               });
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}

function writeFileData(arr, fileName){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, fileName);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', fileName);
    tempLink.click(); 
}


function trimLowerCase(obj, desiredOrder) {
  const updatedObject = {};
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      const trimmedKey = key.trim().toLowerCase();
      updatedObject[trimmedKey] = obj[key];
      console.log(updatedObject[trimmedKey]);
    }
  }

  console.log(Object.keys(updatedObject).length);
  console.log(Object.keys(desiredOrder).length);
   if (!desiredOrder.every((key) => updatedObject.hasOwnProperty(key)) || Object.keys(updatedObject).length !== Object.keys(desiredOrder).length) {
     return null;
   }

  const orderedObject = {};
  desiredOrder.forEach((key) => {
    if (updatedObject.hasOwnProperty(key)) {
      orderedObject[key] = updatedObject[key];
      console.log(orderedObject[key]);
    }
  });
  return orderedObject;
}


function downloadReport(data, fileName){
      writeFileData(data, fileName);
      $.notify("Report downloaded!", "success");
}

$(document).ready(function(){
  $('[data-toggle="tooltip"]').tooltip();
});





