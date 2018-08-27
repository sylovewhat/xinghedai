
/*
 *全局变量
 */

//项目版本

//html总版本号
var HTML_VERSONNO="10";
var HTML498_VERSION="html498_version";
//文件资源总版本号
var IMG_VERSONNO="10";
var IMG498_VERSION="img498_version";
//广告缓存
var ADVERTISING_JSON="ADVERTISING_JSON";

/*缓存沙盒全局变量 */
var source_id="source_id";
var proxy_id="proxy_id";
var salesman_id="salesman_id";
var role_type_id="role_type_id";

var USERACCOUNT="USERACCOUNT";
var USERPASSWORD="USERPASSWORD";
var IS_PASSWORD_SAVE="IS_PASSWORD_SAVE";
var SELECT_U_TYPE="SELECT_U_TYPE";
/*是否要刷新*/
var IS_NOW_REFRESH="IS_NOW_REFRESH";
/***** 统一配置全局提示信息   *****/
var POST_RETURN_NO_MORE = "没有更多数据了";

//set/get：全局变量
var wireless_app_type="wireless_app_type";//0,android;1,ios


//版本更新
var IS_UPDATE = "IS_UPDATE";
var VERSION_NO = "VERSION_NO";
var UPDATE_DES = "UPDATE_DES";
var UPDATE_FLAG = "UPDATE_FLAG";
/*
 *全局颜色替换，还需设置style.css
 */

//头部导航栏背景颜色
var NAVCOLOR="#528ee7";
//全局内容颜色设置
var CONTENTCOLOR="#dc2d34";

/*自定义存储 【单个】*/
function set(key, val) {
	window.localStorage.setItem(key, val)
}
function get(key) {
	var data = window.localStorage.getItem(key);
	return data;
}
/**
 * 把字符串转json object Object '{"a":"001","b":"002"}'
 */
function strToJson(str) {
	var json = eval('(' + str + ')');
	return json;
}
/** 把json转字符串 */
function JsonToStr(json) {
	var temp = "{";
	var length = Object.keys(json).length;
	var index = 0;
	for ( var i in json) {
		/* 循环遍历对象的属性 */
		index++;
		if (index == length) {
			temp += '"' + i + '"' + ":" + '"' + json[i] + '"';
		} else {
			temp += '"' + i + '"' + ":" + '"' + json[i] + '",';
		}
	}
	temp += "}";
	return temp;
}
/**
 * 把字符串转json new
 */
function getstrToJson(str) {
	var json = JSON.parse(str);
	return json;
}
/** 把json转字符串 new */
function setJsonToStr(json) {
	var temp = JSON.stringify(json);
	return temp;
}

/*
 * 自定义存储 【多个】 var map = {}; map["key1"] = "value1";
 */
function set_list(valList) {
	/** 遍历map */
	for ( var prop in valList) {
		if (valList.hasOwnProperty(prop)) {
			console.log('key is ' + prop + ' and value is' + valList[prop]);
			window.localStorage.setItem(prop, valList[prop]);
		} else {
			alert('no data');
		}
	}
}
/* 从1970年开始的毫秒数然后截取10位变成 从1970年开始的秒数 */
function get_nowtime() {
	var tmp = Date.parse(new Date()).toString();
	tmp = tmp.substr(0, 10);
	return tmp;
}
/* 将当前时间换成时间格式字符串 */
function get_nowtime(timestamp) {
	var date = new Date();
	date.setTime(timestamp * 1000);
	var seperator1 = "-";
	var s_time = date.getFullYear() + seperator1 + (date.getMonth() + 1)
			+ seperator1 + date.getDate();
	return s_time;
}

/* 时间戳转日期 */
function timeToDate(datetime, tmp) {
	var time_tmp = new Date(parseInt(datetime) * 1000);
	var y = time_tmp.getFullYear();
	var m = time_tmp.getMonth() + 1;
	var d = time_tmp.getDate();
	var h = time_tmp.getHours();
	var i = time_tmp.getMinutes();
	var s = time_tmp.getSeconds();
	if (tmp == 'Y-m-d') {
		return y + '-' + timeAddZeor(m) + '-' + timeAddZeor(d);
	} else if (tmp == 'Y-m') {
		return y + '-' + timeAddZeor(m);
	} else {
		return y + '-' + timeAddZeor(m) + '-' + timeAddZeor(d) + ' '
				+ timeAddZeor(h) + ':' + timeAddZeor(i) + ':' + timeAddZeor(s);
	}
}

function timeAddZeor(m) {
	return m < 10 ? '0' + m : m;
}
/* 将当前时间格式字符串 转时间戳 */
function get_unix_time(dateStr) {
	var newstr = dateStr.replace(/-/g, '/');
	var date = new Date(newstr);
	var time_str = date.getTime().toString();
	return time_str.substr(0, 10);
}
// 获取当前时间，格式YYYY-MM-DD
function getNowFormatDate() {
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}
// 获取前一天时间，格式YYYY-MM-DD
function getNowFormatDate_LAST() {
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate() - 1;
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}
function removeIndex(key) {
	window.localStorage.removeItem(key);
}
function clear() {
	window.localStorage.clear();
}
function paystyle(style) {
	if (style == '1') {
		return "条形码";
	} else if (style == '2') {
		return "声波";
	} else if (style == '3') {
		return "二维码";
	} else {
		return "线上支付";
	}
}
function q_trunk_chinese(q) {
	if (q == 'c') {
		return "银行间连";
	} else if (q == '1') {
		return "支付宝";
	} else if (q == '2') {
		return "微信支付";
	} else if (q == '3') {
		return "百度百付宝";
	} else if (q == '4') {
		return "苏宁易付宝";
	} else if (q == '6') {
		return "京东钱包";
	} else if (q == '7') {
		return "QQ钱包";
	} else if (q == '8') {
		return "翼支付";
	} else if (q == '9') {
		return "银联";
	} else {
		return "间连";
	}
}
/* 检测字符串是否包含特殊字符 */
function checkStr(str) {
	szReg = /[\',:;*?~`!@#$%^&+=)(<>{}]|\]|\[|\/|\\\|\"|\|/;
	var bChk = szReg.test(str);
	if (!bChk) {
		return true;
	}
}

/* 检测是否整数 */
function checkAllNum(num) {
	szReg = /^-?[0-9]\d*$/;// 整数
	var bChk = szReg.test(num);
	if (!bChk) {
		return true;
	}
}

/* 检测是否正整数 含0 */
function checkNum(num) {
	szReg = /^\d+(\d+)?$/;// 正整数 含0
	var bChk = szReg.test(num);
	if (!bChk) {
		return true;
	}
}

/* 检测是否正整数 不含0 */
function checkNums(num) {
	szReg = /^[0-9]*[1-9][0-9]*$/;// 正整数 不含0
	var bChk = szReg.test(num);
	if (!bChk) {
		return true;
	}
}

/* 检测是否数值为金额 */
function checkMoney(money) {
	szReg = /^\d+(\.\d+)?$/;
	var bChk = szReg.test(money);
	if (!bChk) {
		return true;
	}
}

/* 检测字符串是否邮箱 */
function checkEmail(email) {
	szReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var bChk = szReg.test(email);
	if (!bChk) {
		return true;
	}
}

/* 检测手机号 */
function checkPhone(phone) {
	szReg = /^((1[3,5,8][0-9])|(14[5,7])|(17[0,3,6,7,8]))\d{8}$/;
	var bChk = szReg.test(phone);
	if (!bChk) {
		return true;
	}
}

/* 检测身份证号码 */
function checkIdcard(Idcard) {
	szReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	var bChk = szReg.test(Idcard);
	if (!bChk) {
		return true;
	}
}

/* 检测银行卡号 */
function checkBank(Bank) {
	szReg = /^(\d{16}|\d{19})$/;
	var bChk = szReg.test(Bank);
	if (!bChk) {
		return true;
	}
}

// 字符串转换utf-8
function EncodeUtf8(s1) {
	// escape函数用于对除英文字母外的字符进行编码。如“Visit W3School!”->"Visit%20W3School%21"
	var s = escape(s1);
	var sa = s.split("%");// sa[1]=u6211
	var retV = "";
	if (sa[0] != "") {
		retV = sa[0];
	}
	for ( var i = 1; i < sa.length; i++) {
		if (sa[i].substring(0, 1) == "u") {
			retV += Hex2Utf8(Str2Hex(sa[i].substring(1, 5)));
			if (sa[i].length >= 6) {
				retV += sa[i].substring(5);
			}
		} else
			retV += "%" + sa[i];
	}
	return retV;
}
function Str2Hex(s) {
	var c = "";
	var n;
	var ss = "0123456789ABCDEF";
	var digS = "";
	for ( var i = 0; i < s.length; i++) {
		c = s.charAt(i);
		n = ss.indexOf(c);
		digS += Dec2Dig(eval(n));

	}
	// return value;
	return digS;
}
function Dec2Dig(n1) {
	var s = "";
	var n2 = 0;
	for ( var i = 0; i < 4; i++) {
		n2 = Math.pow(2, 3 - i);
		if (n1 >= n2) {
			s += '1';
			n1 = n1 - n2;
		} else
			s += '0';

	}
	return s;

}
function Dig2Dec(s) {
	var retV = 0;
	if (s.length == 4) {
		for ( var i = 0; i < 4; i++) {
			retV += eval(s.charAt(i)) * Math.pow(2, 3 - i);
		}
		return retV;
	}
	return -1;
}
function Hex2Utf8(s) {
	var retS = "";
	var tempS = "";
	var ss = "";
	if (s.length == 16) {
		tempS = "1110" + s.substring(0, 4);
		tempS += "10" + s.substring(4, 10);
		tempS += "10" + s.substring(10, 16);
		var sss = "0123456789ABCDEF";
		for ( var i = 0; i < 3; i++) {
			retS += "%";
			ss = tempS.substring(i * 8, (eval(i) + 1) * 8);

			retS += sss.charAt(Dig2Dec(ss.substring(0, 4)));
			retS += sss.charAt(Dig2Dec(ss.substring(4, 8)));
		}
		return retS;
	}
	return "";
}

// 采用正则表达式获取地址栏参数
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var l = decodeURI(window.location.search);
	var r = l.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]);
	}
	return "";
}

/* 生成指定位数随机数 */
function randomNum(n) {
	var t = '';
	for ( var i = 0; i < n; i++) {
		t += Math.floor(Math.random() * 10);
	}
	return t;
}

//制保留2位小数，如：2，会在2后面补上00.即2.00
function toDecimal2(x) {
	var f = parseFloat(x);
	if (isNaN(f)) {
		return false;
	}
	var f = Math.round(x * 100) / 100;
	var s = f.toString();
	var rs = s.indexOf('.');
	if (rs < 0) {
		rs = s.length;
		s += '.';
	}
	while (s.length <= rs + 2) {
		s += '0';
	}
	return s;
}


function encode_hk_soft(str){
	var chrArr = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	var key_b = '';
	for(i=0;i<6;i++){
		key_b += chrArr.charAt(parseInt(Math.random()*62,10));
	}
	var rand_key = key_b + '' + '498sijiuba498';
	rand_key = hex_md5(rand_key);
	var strlen = str.length;
	var resultstr = '';
	for(i=0;i<strlen;i++){
		a = str.charCodeAt(i);
		b = rand_key.charCodeAt(i % 32);
		resultstr += String.fromCharCode(a ^ b);
	}
	var b = new Base64();   
	resultstr = key_b + b.encode(resultstr).replace('=','');
	resultstr = hex_md5(resultstr).substring(0,8) + resultstr;
	return resultstr;
}

function Base64() {
    // private property
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
 
    // public method for encoding
    this.encode = function (input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = _utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    }
 
    // public method for decoding
    this.decode = function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }
 
    // private method for UTF-8 encoding
    _utf8_encode = function (string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
 
        }
        return utftext;
    }
 
    // private method for UTF-8 decoding
    _utf8_decode = function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while ( i < utftext.length ) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}

function Utf8Encode(string) {
    var utftext = "";
    for (var n = 0; n < string.length; n++) {
        var c = string.charCodeAt(n);
        if (c < 128) {
            utftext += String.fromCharCode(c);
        } else if ((c > 127) && (c < 2048)) {
            utftext += String.fromCharCode((c >> 6) | 192);
            utftext += String.fromCharCode((c & 63) | 128);
        } else {
            utftext += String.fromCharCode((c >> 12) | 224);
            utftext += String.fromCharCode(((c >> 6) & 63) | 128);
            utftext += String.fromCharCode((c & 63) | 128);
        }
    }
    return utftext;
}
//将URL中的UTF-8字符串转成中文字符串  
function getCharFromUtf8(str) {  
    var cstr = "";  
    var nOffset = 0;  
    if (str == "")  
        return "";  
    str = str.toLowerCase();  
    nOffset = str.indexOf("%e");  
    if (nOffset == -1)  
        return str;  
    while (nOffset != -1) {  
        cstr += str.substr(0, nOffset);  
        str = str.substr(nOffset, str.length - nOffset);  
        if (str == "" || str.length < 9)  
            return cstr;  
        cstr += utf8ToChar(str.substr(0, 9));  
        str = str.substr(9, str.length - 9);  
        nOffset = str.indexOf("%e");  
    }  
    return cstr + str;  
} 

function lastMonthDate(){
    var Nowdate = new Date();
    var vYear = Nowdate.getFullYear();
    var vMon = Nowdate.getMonth() + 1;
    var vDay = Nowdate.getDate();
　　//每个月的最后一天日期（为了使用月份便于查找，数组第一位设为0）
    var daysInMonth = new Array(0,31,28,31,30,31,30,31,31,30,31,30,31);
    if(vMon==1){
        vYear = Nowdate.getFullYear()-1;
        vMon = 12;
     }else{
        vMon = vMon -1;
    }
　　//若是闰年，二月最后一天是29号
    if(vYear%4 == 0 && vYear%100 != 0  || vYear%400 == 0 ){
        daysInMonth[2]= 29;
    }
    if(daysInMonth[vMon] < vDay){
        vDay = daysInMonth[vMon];
    }
     if(vDay<10){
        vDay="0"+vDay;
    }
     if(vMon<10){
        vMon="0"+vMon;
     }
    var date =vYear+"-"+ vMon +"-"+vDay;
    return date;
}

function andBackbtn(){
	window.APPJS.unifyClassMethod("NavgationViewClass", "back", "", "");
}

function andExitbtn(){
	var jsonstr = "{\"alertType\":\"0\",\"alertChoice\":\"确定/&取消\",\"alertTitle\":\"是否退出程序\"}";
    	window.APPJS.unifyClassMethod("FuctionClass", "alertSheetShow", jsonstr, "returnAlert");
}

function returnAlert(json){
	alert(json["alertChoice"]);
}

//跳转到一页
function gotoNextHtml(htmaname){
	var json = "{\"url_html\":\""+htmaname+"\",\"url_data\":\"\",\"viewHeigh\":\"0\",\"loadType\":\"0\"}";
	window.APPJS.unifyClassMethod("FuctionClass", "sec_jump_href", json, "tabBarHidden");
	var json2 = {};
	json2["showType"]="1";
	window.APPJS.unifyClassMethod("NavgationViewClass", "tabBarLoad", JSON.stringify(json2), "");
}
