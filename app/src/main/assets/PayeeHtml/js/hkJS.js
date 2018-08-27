if(!window.APPJS){
    
    window.sendDataToIOS = function(method,args){
        var ios_str = "ios:" + method;
        for(i=0;i<args.length;i++){
            ios_str += "|" + args[i];
        }
        //alert(ios_str);
        var iframe = document.createElement("IFRAME");
        iframe.setAttribute("src", ios_str);
        document.documentElement.appendChild(iframe);
        iframe.parentNode.removeChild(iframe);
        iframe = null;
        return null;
    }
    
    window.dataFromAPP = function(ret,method){
        method = 'return_' + method;
        var fun = window[method] || $('#frm_'+ tab)[0].contentWindow[method];
        if(fun){
            fun(ret);
        }else{
            return;
        }
    }
    
    window.APPJS = {
        unifyMethod: function(){// 进件成功返回通道页面
            return sendDataToIOS('unifyMethod',arguments);
        },unifyClassMethod: function(){// 进件成功返回通道页面
            return sendDataToIOS('unifyClassMethod',arguments);
        }
        /*
    setTitleBarColor: function(){
        //var args = arguments;//Array.prototype.slice.call(arguments);
        return sendDataToIOS('setTitleBarColor',arguments);
    },
    saveData: function(){
        return sendDataToIOS('saveData',arguments);
    },
    getKey: function(){
        return sendDataToIOS('getKey',arguments);
    },
    login: function(){
        return sendDataToIOS('login',arguments);
    },
    initLogin: function(){
        return sendDataToIOS('initLogin',arguments);
    },
    initIndex: function(){
        return sendDataToIOS('initIndex',arguments);
    },
    severLogRecode: function(){
        return sendDataToIOS('severLogRecode',arguments);
    },
    post_action: function(){
        return sendDataToIOS('post_action',arguments);
    },
    sec_jump_href: function(){
        return sendDataToIOS('sec_jump_href',arguments);
    },
    back: function(){
        return sendDataToIOS('back',arguments);
    },
    choose_acdcode: function(){
        return sendDataToIOS('choose_acdcode',arguments);
    },
    get_user_about: function(){
        return sendDataToIOS('get_user_about',arguments);
    },
    get_user_info: function(){
        return sendDataToIOS('get_user_info',arguments);
    },
    shanghu_details: function(){
        return sendDataToIOS('shanghu_details',arguments);
    },
    ActionSheet_jump: function(){
        return sendDataToIOS('ActionSheet_jump',arguments);
    },
    refresh_data: function(){
        return sendDataToIOS('refresh_data',arguments);
    },
    refresh: function(){//刷新商户列表
        return sendDataToIOS('refresh',arguments);
    },
    shanghu_ist: function(){//knowe进入了商会额表
        return sendDataToIOS('shanghu_ist',arguments);
    },getSQL_data: function(){//获取数据表－－－统一接口
        return sendDataToIOS('getSQL_data',arguments);
    },choose_saleman: function(){//新增业务员－－－选择业务员
        return sendDataToIOS('choose_saleman',arguments);
    },xstar_time: function(){//时间选择器－－－－－开始时间
        return sendDataToIOS('xstar_time',arguments);
    },xend_time: function(){//时间选择器－－－－－结束时间
        return sendDataToIOS('xend_time',arguments);
    },choose_salesmans: function(){//筛选业务员
        return sendDataToIOS('choose_salesmans',arguments);
    },clear_back: function(){// 晴空goback
        return sendDataToIOS('clear_back',arguments);
    },choose_picture: function(){// 调用相机相册
        return sendDataToIOS('choose_picture',arguments);
    },choose_other: function(){// 费率
        return sendDataToIOS('choose_other',arguments);
    },choose_weixin: function(){// 微信行业泪目选择
        return sendDataToIOS('choose_weixin',arguments);
    },choose_zhifubao: function(){// 微信行业泪目选择
        return sendDataToIOS('choose_zhifubao',arguments);
    },post_action_img: function(){// 微信行业泪目选择
        return sendDataToIOS('post_action_img',arguments);
    },showtabr: function(){// data show tabr
        return sendDataToIOS('showtabr',arguments);
    },choose_qq: function(){// data show tabr
        return sendDataToIOS('choose_qq',arguments);
    },choose_bank: function(){// data show tabr
        return sendDataToIOS('choose_bank',arguments);
    },showTiJiao: function(){// data show tabr
        return sendDataToIOS('showTiJiao',arguments);
    },dataIsSearch: function(){// data show tabr
        return sendDataToIOS('dataIsSearch',arguments);
    },initDataWithArray: function(){// data show tabr
        return sendDataToIOS('initDataWithArray',arguments);
    },isNotRequestHtml: function(){// data show tabr
        return sendDataToIOS('isNotRequestHtml',arguments);
    },get_user: function(){// data show tabr
        return sendDataToIOS('get_user',arguments);
    },login_out: function(){// data show tabr
        return sendDataToIOS('login_out',arguments);
    },homeTopCell: function(){// data show tabr
        return sendDataToIOS('homeTopCell',arguments);
    },show_toast_back: function(){// 弹出成功提示，返回
        return sendDataToIOS('show_toast_back',arguments);
    },getScanCode: function(){// 首页 - 扫码进件
        return sendDataToIOS('getScanCode',arguments);
    },view_shanghu: function(){// 显示商户页面
        return sendDataToIOS('view_shanghu',arguments);
    },search_data_main: function(){// 显示数据页面
        return sendDataToIOS('search_data_main',arguments);
    },back_tongdao: function(){// 进件成功返回通道页面
        return sendDataToIOS('back_tongdao',arguments);
    }*/
        
        
        
        
        
        
        
        
        
        
        
        
        
    };
    
}

