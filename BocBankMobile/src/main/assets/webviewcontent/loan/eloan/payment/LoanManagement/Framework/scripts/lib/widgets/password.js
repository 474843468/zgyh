define("password", [
    "zepto", "common","model", "routesUtil", "sip"
], function($, Common, Model, RoutesUtil){
    //创建键盘容器
    $("body").append('<div id="CFCA_CompleteKeyboard"></div><div id="CFCA_NumberKeyboard"></div>');
    //指定密码控件的公钥
    //可以通过url传参方式修改公钥文件
    //1为网银 0为开放平台(测试?)，在constant中定义
    var pkey = 1; //可以在url中指定publickey，否则默认为EBank
    Common.urlParams.publicKey == 0 ? (pkey = 0, require(["publicKey_test"], function(){})) : "";

    //定义各种密码框类型、样式
    var passTypes = {
        "password": { //口令
            publicKey: pkey,
            outputType: 2,
            minLength: 1,
            maxLength: 20,
            matchRegex: '^[!-~]*$',
            keyboard: 0 //全键盘
        },
        "wepassword": {
            publicKey: pkey,
            outputType: 1,
            minLength: 6,
            maxLength: 20,
            matchRegex: '^[!-~]*$',
            keyboard: 0 //全键盘
        },
        "new_pwd": { //新口令
            publicKey: pkey,
            outputType: 2,
            minLength: 8,
            maxLength: 20,
            matchRegex: '^[!-~]*$',
            keyboard: 0 //全键盘
        },
        "token_pwd": { //动态口令
            publicKey: pkey,
            outputType: 2,
            minLength: 6,
            maxLength: 6,
            matchRegex: '^[0-9]{6}$',
            keyboard: 1 //数字键盘
        },
        "tel_pwd": { //电话银行密码
            publicKey: pkey,
            outputType: 2,
            minLength: 6,
            maxLength: 6,
            matchRegex: '^[0-9]{6}$',
            keyboard: 1 //数字键盘
        },
        "mobile_pwd": { //手机银行密码
            publicKey: pkey,
            outputType: 1,
            minLength: 6,
            maxLength: 20,
            matchRegex: '^[!-~]*$',
            keyboard: 0 //全键盘
        },
        "confirm_pwd": { //确认密码
            publicKey: pkey,
            outputType: 2,
            minLength: 8,
            maxLength: 20,
            matchRegex: '(^[!-~]*[A-Za-z]+[!-~]*[0-9]+[!-~]*$)|(^[!-~]*[0-9]+[!-~]*[A-Za-z]+[!-~]*$)',
            keyboard: 0 //全键盘
        },
        "password2": { //口令
            publicKey: pkey,
            outputType: 2,
            minLength: 1,
            maxLength: 6,
            matchRegex: '^[0-9]{6}$',
            keyboard: 1 //数字键盘
        },
        "reserv_pwd": { //预留密码 6位数字 无卡取现，预留密码，token，短信验证码使用 hs3693
            publicKey: pkey,
            outputType: 2,
            minLength: 6,
            maxLength: 6,
            matchRegex: '^[0-9]{6}$',
            keyboard: 1 //数字键盘
        },
        "quick_confirm_pwd": { //速汇金 解付确认码
            publicKey: pkey,
            outputType: 1,
            minLength: 8,
            maxLength: 8,
            matchRegex: '^[0-9]{8}$',
            keyboard: 1 //数字键盘
        },
        "credit_cvv2": { //信用卡CVV2码
            publicKey: pkey,
            outputType: 2,
            minLength: 3,
            maxLength: 3,
            matchRegex: '^[0-9]{3}$',
            keyboard: 1 //数字键盘
        },
        "ATM_pwd": { //取款密码
            publicKey: pkey,
            outputType: 2,
            minLength: 6,
            maxLength: 6,
            matchRegex: '^[0-9]{6}$',
            keyboard: 1 //数字键盘
        },
        "west_quick_pwd": { //西联汇款解付码
            publicKey: pkey,
            outputType: 1,
            minLength: 8,
            maxLength: 10,
            matchRegex: '^[0-9]{10}$',
            keyboard: 1 //数字键盘
        },
        "ruia_quick_pwd": { //瑞亚速汇解付码
            publicKey: pkey,
            outputType: 1,
            minLength: 8,
            maxLength: 11,
            matchRegex: '^[0-9]{11}$',
            keyboard: 1 //数字键盘
        }
    };

    /**
     * 批量扫描所有需要绑定的密码框
     * @param el
     */
    function scanPassword(el){
        setTimeout(function(){
            $(el).find("input[widget_type='password']").each(function(i, item){
                bindPwdKeyBoard(item);
            });
            $(el).find("input[widget_type='password']").on("click", onBocPwdClick);
        }, 10);
    }

    /**
     * 密码框被点击事件
     */
    function onBocPwdClick(e){
        var t = $(this),
            id = t.attr("id"),
            passType = t.attr("passType") || "password",
            keyboard = passTypes[passType].keyboard,
            keyBoardObj = keyboard ? $.numberKeyboard : $.completeKeyboard;
        t.blur();
        $.completeKeyboard && $.completeKeyboard.hideKeyboard();
        $.numberKeyboard && $.numberKeyboard.hideKeyboard();
        keyBoardObj.bindInputBox(id);
        keyBoardObj.showKeyboard();
        e.stopPropagation();
    }

    /**
     * 初始化密码框样式属性
     */
    function setProperties(item){
    	
        var t = $(item),
            id = t.attr("id"),
            cvtId = t.attr("cvtId"),
//            loginpre = t.attr("loginpre")?t.attr("loginpre"):true,
            passType = t.attr("passType") || "password",
            keyboard = passTypes[passType].keyboard,
            keyBoardObj = keyboard ? $.numberKeyboard : $.completeKeyboard,
            publicKey = passTypes[passType].publicKey,
            outputType = passTypes[passType].outputType,
            minLength = passTypes[passType].minLength,
            maxLength = passTypes[passType].maxLength,
            matchRegex = passTypes[passType].matchRegex;
		var loginpre = t.attr("loginpre");
        loginpre = !loginpre ? false : (loginpre == "true");
        if(!cvtId){
            throw new Error("未正确初始化conversationid");
        }
        function _setProps(serverRandom){
            if(CFCA_OK != keyBoardObj.setMinLength(minLength, id)) Common.showAlert("setMinLength error", ["确定"]);
            if(CFCA_OK != keyBoardObj.setMaxLength(maxLength, id)) Common.showAlert("setMaxLength error", ["确定"]);
            if(CFCA_OK != keyBoardObj.setOutputType(outputType, id)) Common.showAlert("setOutputType error", ["确定"]);
            if(CFCA_OK != keyBoardObj.setPublicKeyToEncrypt(publicKey, id)) Common.showAlert("setEncryptToEncrypt error", ["确定"]);
            if(CFCA_OK != keyBoardObj.setServerRandom(serverRandom, id)) Common.showAlert("setServerRandom error", ["确定"]);
            if(CFCA_OK != keyBoardObj.setMatchRegex(matchRegex, id)) Common.showAlert("setMatchRegex error", ["确定"]);
            if(CFCA_OK != keyBoardObj.setCipherType(CIPHER_TYPE_SM2, id)) Common.showAlert("setMatchRegex error", ["确定"]);
        }
        if($._pwdRandoms[cvtId]){
        	if($._pwdRandoms[cvtId] == 'initializing'){
        		var si = setInterval(function(){
        			if($._pwdRandoms[cvtId] != 'initializing'){
        				clearInterval(si);
        				_setProps($._pwdRandoms[cvtId]);
        			}
        		}, 50);
        	}else{
        		_setProps($._pwdRandoms[cvtId]);
        	}
        }else{
        	$._pwdRandoms[cvtId] = 'initializing';
        	Model.interaction({
                method: loginpre ? RoutesUtil.method.CoinSeller.PSNGetRandomLoginPre : RoutesUtil.method.CoinSeller.PSNGetRandom ,
                params: {conversationId:cvtId}
            }, "CoinSeller", function(data) {
            	$._pwdRandoms[cvtId] = data;
            	_setProps(data);
            }, null, null);
        }
    }

    /**
     * 绑定密码框与键盘
     * @param item {Object}  需要绑定的input对象
     * @returns {boolean}
     */
    function bindPwdKeyBoard(item){
        if($('#CFCA_CompleteKeyboard').length <= 0){
        	//创建键盘容器
            $("body").append('<div id="CFCA_CompleteKeyboard"></div>');
            if($.completeKeyboard && $.completeKeyboard.ctls){
                for(var id in $.completeKeyboard.ctls){
                    clearById(id);
                }
            }
            $.completeKeyboard = null;
        }
        if($('#CFCA_NumberKeyboard').length <= 0){
        	$("body").append('<div id="CFCA_NumberKeyboard"></div>');
        	if($.numberKeyboard && $.numberKeyboard.ctls){
                for(var id in $.numberKeyboard.ctls){
                    clearById(id);
                }
            }
        	$.numberKeyboard = null;
        }
    	var passType = $(item).attr("passType") || "password", kb = passTypes[passType].keyboard, binded=$(item).attr("binded");
        if(!$(item).attr("id")){
            //键盘通过密码框的id进行绑定
            throw new Error("未正确初始化密码控件的唯一id");
        }
        if(binded){
            return false;
        }
        var a = {};
        if(kb){
            !$.numberKeyboard && ($('#CFCA_NumberKeyboard').empty(),$.numberKeyboard = new CFCAKeyboard("CFCA_NumberKeyboard", KEYBOARD_TYPE_DIGITAL), $.numberKeyboard.ctls = {});
            $.numberKeyboard.bindInputBox($(item).attr("id"));
            $.numberKeyboard.hideKeyboard();
            $.numberKeyboard.ctls[$(item).attr("id")] = passType;
        }
        else{
            !$.completeKeyboard && ($('#CFCA_CompleteKeyboard').empty(),$.completeKeyboard = new CFCAKeyboard("CFCA_CompleteKeyboard", KEYBOARD_TYPE_COMPLETE), $.completeKeyboard.ctls = {});
            $.completeKeyboard.bindInputBox($(item).attr("id"));
            $.completeKeyboard.hideKeyboard();
            $.completeKeyboard.ctls[$(item).attr("id")] = passType;
        }
        setProperties(item);
        $(item).attr("binded", true);
    }

    /**
     * 获取键盘
     * @param id
     * @returns {Object} Object(CFCAKeyboard)
     */
    function getKeyBoard(id){
        // var t = $("#" + id), passType = t.attr("passType") || "password", keyboard = passTypes[passType].keyboard;
        return $.numberKeyboard && $.numberKeyboard.ctls[id] ? $.numberKeyboard : ($.completeKeyboard && $.completeKeyboard.ctls[id] ? $.completeKeyboard : null)
        // return keyboard ? $.numberKeyboard : $.completeKeyboard;
    }
    
    function clearById(id){
        getKeyBoard(id).clearInputValue(id);
    }

    /**
     * 监听插入事件，支持插入一段EJS字符串时，自动绑定其中的密码控件
     */
    $.funAop($.fn, {
        "html" : {
            after : function(res, element) {
                if(!$.isTmplContainsCtrl(element, 'password')){
                    return res;
                }
                scanPassword(res);
                return res;
            }
        },
        append: {
            after : function(res, element) {
                if(!$.isTmplContainsCtrl(element, 'password')){
                    return res;
                }
                scanPassword(res);
                return res;
            }
        }
    }, true);

    //文档点击事件，控制键盘隐藏
    $(document).on("click", function(){
        $.completeKeyboard && $.completeKeyboard.hideKeyboard();
        $.numberKeyboard && $.numberKeyboard.hideKeyboard();
    });
    //停止键盘的点击事件冒泡触发document的点击事件
    $("#CFCA_NumberKeyboard, #CFCA_CompleteKeyboard").on("click", function(e){
        e.stopPropagation();
    });

    $.extend($.fn,{
        /**
         * 绑定某个密码框
         */
        bindPwdKeyboard: function(cvtId){
            this.attr("cvtId", cvtId);
            bindPwdKeyBoard(this);
            $(this).on("click", onBocPwdClick);
        },
        /**
         * 获取某个密码框的加密值和客户端随机数
         * @returns {Object}
         * @example
         * //returns {
         * //    "password1": "hsjklfio2jkljklsf8943==",
         * //    "password1_RC": "sjflksjfskljfskljflskoj239082390480==",
         * //    "password2": "hsjklfio2jkljklsf8943==",
         * //    "password2_RC": "sjflksjfskljfskljflskoj239082390480==",
         * //}
         * var pwds = $("#password1, #password2").boc_password();
         */
        boc_password: function(message){
            var obj = {};
            var _t = this;
            this.each(function(){
                var id = $(this).attr("id");
                var widgetType = $(this).attr("widget_type");
                if(widgetType != "password"){
                    return ""
                }
                var keyboard = getKeyBoard(id);
                var vkey = $(this).attr("name") || id,
                    rkey = vkey + "_RC";
                obj["activ"] = obj["version"] = getCFCAKeyboardVersion();
                
                if($(this).attr(rkey)){
                    obj[rkey] = $(this).attr(rkey);
                }
                else{
                    $(this).attr(rkey, keyboard.getEncryptedClientRandom(id));
                    obj[rkey] = $(this).attr(rkey);
                    var errorCode = keyboard.getErrorCode(id).toString(16);
                    if(errorCode != CFCA_OK) {
                        obj.isError = true;
                        obj.errerCode = errorCode;
                        obj.errorMessage = _t.getErrorMessage(errorCode, message || "密码");
                        //Common.showAlert("加密输入数据错误: 0x" + errorCode, ["确定"]);
                        return obj;
                    }
                }
                
                obj[vkey] = keyboard.getEncryptedInputValue(id);
                errorCode = keyboard.getErrorCode(id).toString(16);
                if(errorCode != CFCA_OK) {
                	obj.isError = true;
                	obj.errerCode = errorCode;
                    obj.errorMessage = _t.getErrorMessage(errorCode, message || "密码");
                    return obj;
                }
            });
            console.log(JSON.stringify(obj));
            return obj;
        },
        /**
         * 清空选择器所选择的所有密码控件
         */
        clearPwd: function(){
            this.each(function(){
                clearById( $(this).attr("id") );
            });
        },
        
        /**
         *	根据错误码生成错误信息 
         */
        getErrorMessage:function(code, param){
        	var result = "";
        	code = parseInt(code, 16);
			switch(code){
				case CFCA_ERROR_INVALID_PARAMETER:
					result = param + "参数错误";
					break;
				case CFCA_ERROR_INVALID_SIP_HANDLE_ID:
					result = param + "SIP ID无效，键盘创建失败";
					break;
				case CFCA_ERROR_INPUT_LENGTH_OUT_OF_RANGE:
					result = param + "输入长度有误";
					break;
				case CFCA_ERROR_INPUT_VALUE_IS_NULL:
					result = param + "不能为空";
					break;
				case  CFCA_ERROR_SERVER_RANDOM_INVALID:
					result = param + "随机数错误";
					break;
				case CFCA_ERROR_SERVER_RANDOM_IS_NULL:
					result = param + "随机数不能为空";
					break;
				case CFCA_ERROR_INPUT_VALUE_NOT_MATCH_REGEX:
					result = param + "格式有误";
					break;
				case CFCA_ERROR_RSA_ENCRYPT_FAILED:
					result = param + "加密失败";
					break;
				default:
				
			}		
			return result;
        },
        
        /**
         * 检查当前密码框与另外一个密码框输入内容是否一致
         * @param selector 选择器
         * @returns {boolean}
         * @examples
         * $("#password1").checkEqual("#password2");
         */
        checkEqual: function(selector){
            var passType1 = $(this).attr("passType"),
                passType2 = $(selector).attr("passType"),
                id1 = $(this).attr("id"),
                id2 = $(selector).attr("id"),
                kbobj = getKeyBoard(id1);
            if(passType1 != passType2){
                throw new Error("密码框类型不一致，不能比较!");
            }
            return kbobj.checkInputValueMatch(id1, id2);
        },
        
        /**
         * 检测输入框内容与正则表达式是否匹配
         * @examples
         * if(!$("#password1").checkMatchRegex()){
         *     alert('请输入正确格式的密码');
         * }
         */
        checkMatchRegex: function(message){
        	var id = $(this).attr('id'),
        		kbobj = getKeyBoard(id);
            var res = kbobj.checkMatchRegex(id);
        	var errorCode = kbobj.getErrorCode(id).toString(16);
            if(errorCode != CFCA_OK) {
                Common.showAlert(_t.getErrorMessage(errorCode, message || "密码"), ["确定"]);
                return false;
            }
        	return res;
        }
    });
});