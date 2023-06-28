function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function displayOrderItem(id){
        var url = getOrderItemUrl() + "/api/orderitem/" + id ;
        console.log("in display orderItem function " + url);
       $.ajax({
       	   url: url,
       	   type: 'GET',
       	   success: function(data) {
       	   		 sessionStorage.setItem('data', JSON.stringify(data));
       	   		 window.location.href = getOrderItemUrl() + "/ui/orderitem";
       	   },
       	   error: handleAjaxError
       	});
}




