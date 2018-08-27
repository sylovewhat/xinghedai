
//var ip ="192.168.0.2";
//var port=":8080";
var ip ="112.74.125.175";
var port="";

var http_url="http://"+ip+port+"/taxi";
//配置接口0
var configFace=http_url+"/appConfig/interfaceConfig";
//资源更新接口1
var sourceUpdateFace=http_url+"/appConfig/sourceUpdate";
//云闪付登录2
var loginFace=http_url+"/appLogin/login";
//首页初始3
var indexFace=http_url+"/appIndex/index";
//订单列表4
var orderListFace=http_url+"/appOrder/list";
//订单详情5
var orderDetailFace=http_url+"/appOrder/detail";
//我的6
var centerIndexFace=http_url+"/appCenter/index";
//订单统计7
var orderTotalFace=http_url+"/appCenter/orderTotal";
//评价统计8
var estimateTotalFace=http_url+"/appCenter/estimateTotal";
//退出登陆9
var logoutFace=http_url+"/appLogin/logout";

var requestAry=[configFace,sourceUpdateFace,loginFace,indexFace,
orderListFace,orderDetailFace,centerIndexFace,orderTotalFace,estimateTotalFace,logoutFace];

function requestJson(num){
	var json = {};
	json["userPhone"] = get("userPhone");
	json["token"] = get('tocken');
	json["post_url"]=requestAry[num];
	return json;
}
/*自定义存储 【单个】*/
function set(key, val) {
	window.localStorage.setItem(key, val)
}
function get(key) {
	var data = window.localStorage.getItem(key);
	return data;
}