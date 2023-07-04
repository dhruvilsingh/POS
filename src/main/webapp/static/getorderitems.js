function getBaseUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function getOrderItemList(id){
        var url = getBaseUrl() + "/api/orderitems/" + id;
        console.log("in display orderItem function " + url);
       $.ajax({
       	   url: url,
       	   type: 'GET',
       	   success: function(data) {
       	   		 sessionStorage.setItem('data', JSON.stringify(data));
       	   		 window.location.href = getBaseUrl() + "/ui/orderitem";
       	   },
       	   error: handleAjaxError
       	});
}






