var frame = function(){}

frame.execute = function(actionName,functionName,params,callback){
    if (typeof params == 'function') {
        callback = params;
        params = null;
    }
    var data;
    if(params != null){
        var flag = Util.isJson(params);
        if(flag){
            data = JSON.parse(params);
        }
    }
    $.ajax({
        type : "post",
        dataType : 'json',
        data : data,
        url : actionName + ".action?cmd=" + functionName,
        success : function(msg) {
            //console.log(msg);
            // var json = JSON.parse(msg);
            var data=eval(msg);
            $(":input").each(function(a,b){
                var field=$(b).attr("bind");
                if (field != null){
                    var value=data[field];
                    $(b).val(value);
                }
            });
            if (callback != null)
                callback(data);
        }
    });
}