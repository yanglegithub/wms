function createXMLRequst(){
    try{
        return new XMLHttpRequest();
    }catch(e)
    {
        try{
            return new ActiveXObject("Msxml2.XMLHTTP");
        }catch(e){
            try{
                return new ActiveXObject("Microsoft.XMLHTTP");
            }catch(e){
                alert("不支持浏览器版本");
                throw e;
            }
        }
    }
}
/*
{
    method:
    url:
    asyn:
    params:
    callback:
    type:
}
 */
function ajax(option)
{
    var xmlHttp=createXMLRequst();
    //打开链接
    if(!option.method)//默认get
    {
        option.method="GET";
    }
    if(option.asyn==null)//默认为异步处理
    {
        option.asyn=true;
    }

    xmlHttp.open(option.method,option.url,option.asyn);

    //POST需要设置请求头
    if(option.method=="POST")
    {
        xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    }
    //发送请求，加上请求参数
    xmlHttp.send(option.params);
    //给异步对象的onreadystatechange事件注册监听器
    xmlHttp.onreadystatechange=function()
    {
        //双重判断，判断是否为4的状态，并且响应状态码为：200
        if(xmlHttp.readyState==4 && xmlHttp.status==200)
        {
            var data;
            if(!option.type){
                data=xmlHttp.responseText;
            }else if(option.type=="xml"){
                data=xmlHttp.responseXML;
            }else if(option.type=="text"){
                data=xmlHttp.responseText;
            }else if(option.type=="json"){
                var text=xmlHttp.responseText;
                data=eval("("+text+")");
            }
            //调用回调函数
            option.callback(data);
        }
    }
}


$(document).ready(function(){
    var baseurl = "http://localhost:8077/wms/";
    $.get(baseurl+"dashboardController.do?inAndOut", function (data) {
        $('#materialIn .primeNumber span').html(data.MATERIALS);
        $('#materialOut .primeNumber span').html(data.MATERIALS);
        $('#materialIn .sumNumber').html(data.NOW_MATERIALS);
        $('#materialOut .sumNumber').html(data.NOW_MATERIALS);
        $('#productIn .primeNumber span').html(data.PRODUCT);
        $('#productOut .primeNumber span').html(data.PRODUCT);
        $('#productIn .sumNumber').html(data.NOW_PRODUCT);
        $('#productOut .sumNumber').html(data.NOW_PRODUCT);

        $('#materialIn .addNumber span').html(data.IN_MATERIALS);
        $('#materialOut .addNumber span').html(data.OUT_MATERIALS);
        $('#productIn .addNumber span').html(data.IN_PRODUCT);
        $('#productOut .addNumber span').html(data.OUT_PRODUCT);

        $('#materialIn .incpercNumber').html(((data.IN_MATERIALS / (data.MATERIALS == 0 ? 1 : data.MATERIALS)) * 100).toFixed(1) + '%');
        $('#materialOut .incpercNumber').html(((data.OUT_MATERIALS / (data.MATERIALS == 0 ? 1 : data.MATERIALS)) * 100).toFixed(1) + '%');
        $('#productIn .incpercNumber').html(((data.IN_PRODUCT / (data.PRODUCT == 0 ? 1 : data.PRODUCT)) * 100).toFixed(1) + '%');
        $('#productOut .incpercNumber').html(((data.OUT_PRODUCT / (data.PRODUCT == 0 ? 1 : data.PRODUCT)) * 100).toFixed(1) + '%');
    }, "json");


    $.get(baseurl+"dashboardController.do?imAndOutTasks", function (data) {
        $('#inTask1 span').html(data.imPalletsCount);
        $('#inTask2 span').html(data.imGoodsCount);
        $('#outTask span').html(data.outPalletsCount);
        $('#outTask span').html(data.outGoodsCount);
    }, "json")
});
