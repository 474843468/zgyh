/**
 * common.js 公共类
 * @author LXD
 */

/**
 * 整个框架的公共类，集成了页面url处理、页面跳转封装、Ajax基础封装、消息提示框、集合类、模块类、日志类定义
 * @namespace Common
 * @name Common
 * @requires require,zepto,routesUtil,zepto.bocextend
 */
define('common', ['require', 'zepto', 'routesUtil', 'bocejs',  ''], function(require, $, RoutesUtil, EJS) {
	var Common = {
		/**
		 * 默认Ajax请求url地址定义
		 * @memberof Common
		 * @name defaultDataPostUrl
		 * @type String
		 */
		defaultDataPostUrl: 'BII',
		/**
		 * Ajax请求url地址定义
		 * @memberof Common
		 * @name dataPostUrl
		 * @type Object
		 * @example
		 * BII: "/BII/_bfwajax.do" test
		 * WBA: "/WeiBank/_bfwajax.do"
		 * WPM:"/WeiBankPublicModule/_bfwajax.do"
		 */
		dataPostUrl: {
			BII: "/BII/_bfwajax.do",
			WBA: "/WeiBank/_bfwajax.do",
			WPM:"/WeiBankPublicModule/_bfwajax.do",
            ADVICE:"/ActivityApp/_bfwajax.do",
            EXAM:"/ActivityApp/_bfwajax.do",
            CoinSeller: "/CoinSeller/_bfwajax.do"
//			WPM:"/WeiBank/_bfwajax.do"
		},
		/**
		 * 错误消息定义
		 * @memberof Common
		 * @name errorCode
		 * @type Object
		 * @example "ajaxError": "网络错误，请检查网络连接！"
		 */
		errorCode: {
			"ajaxError":  (navigator.language == 'zh-cn' || navigator.language == 'zh-CN'  ||  navigator.language == 'zh_cn')?  '网络错误，请检查网络连接！' :    ' network error,please check network!'
		},
		/**
		 * 系统路径定义
		 * @memberof Common
		 * @name filesDirPath
		 * @type Object
		 * @example 
		 * img:"framework/images/"
		 * js:"framework/scripts/"
		 * css:"framework/styles/"
		 */
		filesDirPath: {
            img:"images/",
            js:"scripts/",
            css:"styles/",
            nls_zh: "nls/zh/",
            nls_en:"nls/en"
        }
	};
	/**
	 * 短信验证码倒计时
	 * @memberof Common.Ajax
	 * @function
	 * @name replaceNull
	 */
	var count = 60;
	var tm;
	Common.countDown = function(obj) {
		clearTimeout(tm);
		if(count == 0){
			obj.removeAttribute("disabled");
			obj.value = "获取验证码";
			count = 60;
			return;
		}else{
			obj.setAttribute("disabled",true);
			obj.value = "重新发送("+ count +")";
			count--;
		}
		tm = setTimeout(function(){
				Common.countDown(obj);}
				,1000)
	};
	/**
	 * 页面url参数解析结果
	 * @memberof Common
	 * @name urlParams
	 * @type Object
	 * @example 
	 * Common.urlParams.appid
	 * Common.urlParams.entrance
	 */
	Common.urlParams = (function(){
		var search = window.location.search;
		if(!search){
			return {};
		}
		search += window.location.hash;
		(search.indexOf("?") > -1) && (search = search.substring(1));
		if(search == ""){
			return {};
		}
		search = "{\"" + decodeURIComponent(search.replace(/[&#?]/g, "\",\"").replace(/=/g, "\":\"").replace(/,[^,:]*,/g,",").replace(/\",[^,:]*$/,"")) + "\"}";
		var params = search.toObject();
        params._locale && ($.locale = params._locale.toLowerCase());

        return params;
	})();
	
	/**
	 * 框架参数存储，如routepath等
	 * @memberof Common
	 * @name params
	 * @type Object
	 * @example 
	 * Common.params.targetRouter.routerKey
	 * Common.params.targetRouter.funArgs
	 */
	Common.params = {
		loginEjsSelector:"",
		targetRouter:{} //routerKey:"",excuteFun:"",funArgs:"" isExcuteInit:true|false
	};

    /**
     *
     * @type {self|*|jQuery}
     */
    Common.historyManager = (function() {
        window.onpopstate = function (evt) {
          if(evt && evt.state){
              if(evt.state.mtype == Common.historyManager.types.module){
                  Common.triggerRouter(evt.state.data);
              }else if(evt.state.mtype == Common.historyManager.types.stackview){
                  window.Router.backStackView && window.Router.backStackView();
              }
          }else{
              window.history.back();
          }
        }
        return {
            types: {module: 1, step: 2, stackview: 3},
            page: 0
        };
    })();

	/**
	 * 全局Router，模块实例句柄
	 * @namespace global
	 * @name Router
	 * @type Object
	 * @example window.Router取得当前所在浏览模块
	 */
	window.Router = null;
	
	/**
	 * 获取模块的绝对地址
	 * @memberof Common
	 * @function
	 * @name getAppRootPath
	 * @returns (String) 返回模块绝对根路径
	 */
	Common.getAppRootPath = function() {
		var pos = window.location.href.indexOf(window.location.pathname);
		//获取主机地址，如： http://22.11.64.183:8083
		var hostName = window.location.href.substring(0, pos);
		//获取带"/"的项目名，如：/weibank
		var appName = window.location.pathname.substring(0, window.location.pathname.substr(1).indexOf('/') + 1);
		return hostName + appName + "/";
	}; 
	
	/**
	 * 获取文件路径
	 * @memberof Common
	 * @function
	 * @name getFilePath
	 * @param (String) fileType - 文件类型
	 * @param (String) fileName - 文件名称
	 * @returns (String) 返回文件路径
	 */
	Common.getFilePath = function(fileType, fileName) {
		return Common.getAppRootPath() + Common.filesDirPath[fileType] + fileName;
	};

	/**
	 * 处理title文字过长
	 * @memberof Common
	 * @function
	 * @name getFilePath
	 * @param (String) msg - 文字
	 * @param (String) number - 要截取的字数
	 * @returns (String) 返回处理后的文字
	 */
	Common.strLong = function(msg,number){
		var str = '';
		if(msg != null && msg != 'undefined'){
			if(msg.length > number){
				str = msg.substring(0,number)+"...";
			}else{
				str = msg;
			}
		}
		return str;
	};
	/**
	 * 获取模块路由路径
	 * @memberof Common
	 * @function
	 * @name getRouterPath
	 * @param (String) routerKey - 模块名称
	 * @returns (String) 返回模块文件路径
	 */
	Common.getRouterPath = function(routerKey) {
		return RoutesUtil.routerPath[routerKey];
	};

    Common.getNlsPath = function(routerKey){
        var m = routerKey.split('_');
        var lanage = $.locale.toLocaleLowerCase();
        if(m.length>1){
            return '../../' + m[0] + '/nls/' + m[1] + '/' + lanage;
        }else{
            return 'nls/' + routerKey + '/' + lanage;
        }
    }

    Common.setTargetRouter = function(router){
        Common.params.targetRouter = router;
        var p = Common.params.targetRouter;
        p.routerKey = p.routerKey || p.entryRouterKey;
        var content = {
            mtype: Common.historyManager.types.module,
            page: ++Common.historyManager.page,
            data: p
        };
//        window.history.pushState(content, "", "#" + Common.historyManager.page);
    };

	/**
	 * 路由到指定模块（分享key），若指定了执行方法，则执行
	 * @memberof Common
	 * @function
	 * @name triggerRouter
	 * @param (String) targetRouter - 目标模块
	 * @param (Function) callback - 进入模块后执行的回调函数
	 * @returns (String) undefined
	 */
	Common.triggerRouter = function(targetRouter,callback) {
		require([Common.getRouterPath(targetRouter.routerKey), Common.getNlsPath(targetRouter.routerKey)], function(Router, nls) {
            Common.currentRouterKey = targetRouter.routerKey;
            $.localeResource[targetRouter.routerKey] = nls;
            if (window.Router) {
				var oldRouter = window.Router;
				window.Router = new Router();
				oldRouter = null;
			} else {
				window.Router = new Router();
			}
			if (targetRouter.excuteFun) {
				window.Router[targetRouter.excuteFun].apply(window.Router, targetRouter.funArgs);
			}
			if(callback&&$.isFunction(callback)){
				callback();
			}			
		});
	};
	/**
	 * 路由到指定模块，若有分享，则进入分享路由
	 * @memberof Common
	 * @function
	 * @name Router
	 * @param (String) routerPath - 目标模块文件路径
	 * @param (Function) shareFlag - 分享标识
	 * @returns (String) undefined
	 */
	Common.Router=function(routerPath,shareFlag){
        var p = Common.params.targetRouter;
        p.routerKey = p.routerKey || p.entryRouterKey;
//        var content = {
//            mtype: Common.historyManager.types.module,
//            page: ++Common.historyManager.page,
//            data: p
//        };
//        window.history.pushState(content, "", "#" + Common.historyManager.page);
        require([routerPath, Common.getNlsPath(p.routerKey)], function(Router, nls) {
            Common.currentRouterKey = p.routerKey;
            $.localeResource[p.routerKey] = nls;
            if (window.Router) {
				var router = window.Router;
				window.Router = new Router();
				router = null;
			} else {
				window.Router = new Router();
			}
            window.Router.routerKey = p.routerKey;
			if (shareFlag) {
				Common.triggerRouter(Common.params.targetRouter);
			}
		});
	};
	
	/**
	 * 加载指定css文件
	 * @memberof Common
	 * @function
	 * @name loadStyleFiles
	 * @param (String) cssFilePath - css文件路径
	 * @param (Function) callback - 加载成功后执行的方法
	 * @returns (String) undefined
	 */
	Common.loadStyleFiles = function(cssFilePath,callback, args, isSync) {
	    // 修改加载CSS文件逻辑-guoyy
        var otherCss = new Array();
        var el = $("link[loadFlag]");
        $(cssFilePath).each(function(i,o){
            var isExist = false;
            el.each(function(j, t) {
                if(o==$(t).attr("href")){
                    isExist = true;
                }
            });
            if(!isExist)    otherCss.push(o);
        });
        var cssnum = document.styleSheets.length,count = 0;//在加载其他css之前初始化避免缓存瞬间加载完毕空等待问题
        //加载页面原来没有的CSS
        $(otherCss).each(function(i,o){
            $("link").eq(0).after("<link href=\""+o+"\" rel=\"stylesheet\" loadFlag=\"true\"/>");
        });
        if(isSync){//同步加载css后再执行callback
    		var ti = setInterval(function(){
    			count++;
    			if(document.styleSheets.length > cssnum || count>1000){
    				console.log(count);
    				clearInterval(ti);
    		        if(callback&&$.isFunction(callback)){
    		            callback(args);
    		        }
    		        count = 0;
    			}
    		}, 10);
        }else{
        	if(callback&&$.isFunction(callback)){
	            callback(args);
	        }
        }
	};

    Common.changeLocalizeResource = function(locale){
        $.locale = locale;
        require( ['nls/public/'+ $.locale, Common.getNlsPath(Common.currentRouterKey)], function(nls, rnls){
            $.localeResource[Common.currentRouterKey] = rnls;
            window.app.nls = nls;
            Common.changeLanguage('html');
        });
    };

    Common.changeLanguage = function(el){

        $(el).find("[nls]").each(function(i, item){
            var key = $(item).attr('nls');
            window.app && $(item).html($.localeResource[Common.currentRouterKey] && $.localeResource[Common.currentRouterKey][key] || window.app.nls[key], true);
        });
        $(el).find("[placeholder]").each(function(i, item){
            var key = $(item).attr('placeholderkey');
            if(!key){
                key = $(item).attr('placeholder');
                $(item).attr('placeholderkey', key);
            }
            if($.localeResource[Common.currentRouterKey] && $.localeResource[Common.currentRouterKey][key]){
                $.localeResource[Common.currentRouterKey][key] && $(item).attr('placeholder', $.localeResource[Common.currentRouterKey][key]);
            } else if(window.app && window.app.nls[key]){
                window.app.nls[key] && $(item).attr('placeholder', window.app.nls[key])
            }
        });
        $(el).find("[tips]").each(function(i, item){
            var keys = $(item).attr('tipkeys');
            if(!keys||keys=="undefined"||keys==""){
                keys = $(item).attr('tips');
                $(item).attr('tipkeys', keys);
            }
            keys = keys.split(',');
            var val = '';
            $.each(keys, function(i, key){
                val += (window.app &&  $.localeResource[Common.currentRouterKey] && $.localeResource[Common.currentRouterKey][key] || window.app.nls[key] || key) + ',';
            });
            $(item).attr('tips',val.substring(0, val.length-1));
        });
        $(el).find("[alt]").each(function(i, item){
            var key = $(item).attr('alt');
            window.app && $(item).attr('alt', $.localeResource[Common.currentRouterKey] && $.localeResource[Common.currentRouterKey][key] || window.app.nls[key] || key)
        });
    }

	/**
	 * 终端类型判断
	 * @memberof Common
	 * @name terminal
	 */
	Common.terminal = $.terminal;
	
	/**
	 * Ajax请求类
	 * @namespace Common.Ajax
	 * @name Common.Ajax
	 */
	Common.Ajax = {
		proccessing: 0,
		loadingMask: true,
        bottomTipEL: null,
		uuid:(new Date).valueOf() + "" + Math.ceil(Math.random() * 0x7fff),
		/**
		 * 请求参数组装
		 * @memberof Common.Ajax
		 * @function
		 * @name getCommPostParam
		 * @param (Model) model - Model实例
		 * @param (String) local - 本地标识，可选
		 * @private
		 */
		getCommPostParam: function(model, local) {
			var jsn = {};
            if(Common.urlParams.dev == 'IPAD'){
                jsn = {
                    header: {
                        "agent":"X-IPAD"/*"X-IOS"*/,
                        "version": "",
                        "device": "",
                        "platform": "",
                        "plugins": "",
                        "page": "",
                        "local": "zh_CN",
                        "uuid": this.uuid,
                        "ext": "8",
                        "cipherType":"0"
                    },
                    method: model.attributes.method,
                    params: $.extend(model.attributes.params,
                        {openId_log:Common.params.openId,pubId_log:Common.params.pubid})
                };
            }
            else if(Common.urlParams.dev == 'APAD'){
                jsn = {
                    header: {
                        "agent":"APAD",
                        "version": "1.0.0",
                        "device": "aPhone",
                        "platform": "android",
                        "plugins": "5",
                        "page": "6",
                        "local": "zh_CN",
                        "uuid": this.uuid,
                        "ext": "8",
                        "cipherType":"1"
                    },
                    method: model.attributes.method,
                    params: $.extend(model.attributes.params,
                        {openId_log:Common.params.openId,pubId_log:Common.params.pubid})
                };
            }else if(Common.terminal.ios){
				jsn = {
					header: {
						"agent":"X-IOS"/*"X-IOS"*/,
						"version": "",
						"device": "",
						"platform": "",
						"plugins": "",
						"page": "",
						"local": "zh_CN",
						"uuid": this.uuid,
						"ext": "8",
						"cipherType":"0"
					},
					method: model.attributes.method,
					params: $.extend(model.attributes.params,
						{openId_log:Common.params.openId,pubId_log:Common.params.pubid})
				};
			}
			else if(Common.terminal.android){
				jsn = {
					header: {
						"agent":"X-ANDR",
						"version": "1.0.0",
						"device": "aPhone",
						"platform": "android",
						"plugins": "5",
						"page": "6",
						"local": "zh_CN",
						"uuid": this.uuid,
						"ext": "8",
						"cipherType":"1"
					},
					method: model.attributes.method,
					params: $.extend(model.attributes.params,
						{openId_log:Common.params.openId,pubId_log:Common.params.pubid})
				};
			}else{
				jsn = {
					header: {
						"agent":"WEB15",
						"version": "1.0.0",
						"device": "aPhone",
						"platform": "android",
						"plugins": "5",
						"page": "6",
						"local": "zh_CN",
						"uuid": this.uuid,
						"ext": "8",
						"cipherType":"1"
					},
					method: model.attributes.method,
					params: $.extend(model.attributes.params,
						{openId_log:Common.params.openId,pubId_log:Common.params.pubid})
				};
			}

			//var agent = "WEB20";
			//if( model.attributes.changeAgent ){
			//	agent = !Common.params.agentInfo?'X-IPAD':Common.params.agentInfo;
			//};

			if (model.attributes.conversationId) {
				jsn.params.conversationId = model.attributes.conversationId;
				delete jsn.params.conversationId;
			}
			return jsn;
		},
        getOldRequestParams: function(model, locale){
            var header = {
                "agent": "WEB15",
                "bfw-ctrl": "json",
                "device": "",
                "ext": "",
                "local": "zh_CN",
                "page": "",
                "platform": "",
                "plugins": "",
                "version": "",
                "cipherType":"0"
            }
            var request = [];
            request[0] = {
                method:model.attributes.method,
                "id": "unique id",
                "conversationId":model.attributes.params && model.attributes.params.conversationId || "",
                "params": model.attributes.params || {},
            }
			delete request[0].params.conversationId;
			var c = 0;
			for(var f in request[0].params){
				c++;
			}
			if(c == 0){
				request[0].params = null;
			}
            return {"header": header, "request": request};
        },
        oldRequest: function(model, urlPerfix, async){
            Common.Ajax.proccessing++;
            if (!urlPerfix) {
                urlPerfix = Common.defaultDataPostUrl;
            }
            if (!model) {
                return null;
            }
            var url = Common.dataPostUrl[urlPerfix] + '?_locale=zh_CN&uuid=' + Math.random()*0xfffff;
            if(Common.urlParams.demo){
                url = "data/" + model.attributes.method + ".json";
            }
            var jsn = this.getOldRequestParams(model, 'zh_CN');
            return $.ajax({
                async: (async==undefined || async) ? true : false,
                url: url,
                type: Common.urlParams.demo ? 'get' : 'post',
                dataType: "json",
                timeout : 180000,
                "bfw-ctrl": "json",
                "contentType": "text/json",
                headers: {'X-id':4},
                data: JSON.stringify(jsn),
                beforeSend: function(x, settings) {
                    if((Common.Ajax.proccessing <= 1 || $("#maskbox").length < 1) && model.attributes.loadingMask){
                        Common.maskBox.show();
                    }
                },
                complete: function(x, status) {
                    if(status === "timeout"){
                        Common.log.error("request timeout, " + model.attributes.method + "交易返回超时");
                        if(model.attributes.ontimeout && $.isFunction(model.attributes.ontimeout)){
                            model.attributes.ontimeout();
                        }
                    }
                    setTimeout(function(){
                        if(--Common.Ajax.proccessing <= 0){
                            Common.maskBox.hide();
                        }
                    }, 50);
                },
                error: function(x, h, r) {
                    Common.showMessage(Common.Ajax.getMsgByCode("ajaxError"));
                }
            });
        },
		
		/**
		 * 发送Ajax的Post请求
		 * @memberof Common.Ajax
		 * @function
		 * @name postRequest
		 * @param (Model) model - Model实例
		 * @param (String) urlPerfix - 请求地址，可传入“BII”、“WBA”、“WPM”
		 * @param (bool) async - 是否同步
		 */
		postRequest: function(model, urlPerfix, async) {
			Common.Ajax.proccessing++;
			if (!urlPerfix) {
				urlPerfix = Common.defaultDataPostUrl;
			}
			if (!model) {
				return null;
			}

			var url = Common.dataPostUrl[urlPerfix] + '?_locale=zh_CN';
			if(Common.urlParams.demo){
				url = "data/" + model.attributes.method + ".json";
			}

            model.attributes.loadingMask = model.attributes.loadingMask == false ? false : true;
			//var jsn = "json=" + JSON.stringify(Common.Ajax.getCommPostParam(model)).replace(/"null"/g, '""').replace(/\+/g, "%2B").replace(/\&/g, "%26").replace(/\=/g, "%3D");
			var jsnStr  = JSON.stringify(Common.Ajax.getCommPostParam(model)).replace(/"null"/g, '""');
			Common.log.debug("request body:" + jsnStr , url);
			var jsn = 'json=' + encodeURIComponent(JSON.stringify(Common.Ajax.getCommPostParam(model)).replace(/"null"/g, '""'));
			return $.ajax({
				async: (async==undefined || async) ? true : false,
				url: url,
				type: Common.urlParams.demo ? 'get' : 'post',
				dataType: "json",
                timeout : 180000,
				data: jsn.replace(/\u2006/g," "), //替换iphone中文输入法产生的six-per-em space乱码问题
				beforeSend: function(x, settings) {
//                    if(Common.Ajax.bottomTipEL){
//                        Common.maskBox.showBottomTip(Common.Ajax.bottomTipEL);
//                    }else
                    if((Common.Ajax.proccessing <= 1 || $("#maskbox").length < 1) && model.attributes.loadingMask){
                        Common.maskBox.show();
                    }

					//Common.log.debug("request body:" + jsn , url);
				},
				complete: function(x, status) {
					if(status === "timeout"){
                        Common.log.error("request timeout, " + model.attributes.method + "交易返回超时");
                        Common.showAlert(window.app.nls.l_timeout, [window.app.nls.l_comfirm]);
						if(model.attributes.ontimeout && $.isFunction(model.attributes.ontimeout)){
							model.attributes.ontimeout();
						}
                    }
                    // Common.maskBox.hide();
					setTimeout(function(){
						if(--Common.Ajax.proccessing <= 0){
							Common.maskBox.hide();
						}
					}, 50);
				},
				error: function(x, h, r) {
					Common.showMessage(Common.Ajax.getMsgByCode("ajaxError"));
				}
			});
		},
		jsonpRequest: function(url, callback, errorback){
			return $.ajax({
				url: url,
				type: "GET",
				timeout : 30000,
				dataType: "jsonp",
				beforeSend: function(x, settings) {
					if(Common.Ajax.proccessing <= 1){
						Common.maskBox.show();
					}
					Common.log.debug("request jsonp url:" + url, url);
				},
				complete: function(x, status) {
					setTimeout(function(){
						if(--Common.Ajax.proccessing <= 0){
							Common.maskBox.hide();
						}
					}, 500);
                    if(status === "timeout"){
                        Common.log.error("请求url：" + url + "超时");
                        Common.showAlert(window.app.nls.l_timeout, [window.app.nls.l_comfirm]);
                    }
				},
				error: function(x, h, r) {
					if(errorback && typeof errorback === "function"){
						errorback(Common.Ajax.getMsgByCode("ajaxError"));
					}else{
						Common.showMessage(Common.Ajax.getMsgByCode("ajaxError"));
					}
				}
			}).then(callback);
		},
		
		/**
		 * Ajax请求返回的总处理方法，将分发到成功处理和异常处理
		 * @memberof Common.Ajax
		 * @function
		 * @name dataHandle
		 * @param (Object) data - json对象
		 * @param (Function) errorHandle - 后台异常时的处理方法
		 */
		dataHandle: function(data,errorHandle) {
			Common.log.debug("response body:" + JSON.stringify(data));
			if (data) {
				if (Common.Ajax.isSuccesful(data)) {
					var rst = data.result;
					if (rst != null) {
						return Common.Ajax.replaceNull(rst);
					}
				} else {
					// 需要重定向的错误
					errorMap = {
						"validation.session_invalid": window.app.nls.validation_session_invalid,
						"role.invalid_user": window.app.nls.role_invalid_user,
						"validation.resubmit_same_session": window.app.nls.validation_resubmit_same_session,
						"QA.authenticate.limit": window.app.nls.QA_authenticate_limit,
						"smc.token.lock": window.app.nls.smc_token_lock,
						"smc.token.false.lock": window.app.nls.smc_token_false_lock,
						"smc.token.true.lock": window.app.nls.smc_token_true_lock,
						"otp.token.lock": window.app.nls.otp_token_lock,
						"otp.token.false.lock": window.app.nls.otp_token_false_lock,
						"otp.token.true.lock": window.app.nls.otp_token_true_lock,
						"conversationMap.null": window.app.nls.conversationMap_null
					};
					errorsFilterMap={
						"validation.session_invalid": window.app.nls.validation_session_invalid,
						"validation.resubmit_same_session": window.app.nls.validation_resubmit_same_session,
						"role.invalid_user": window.app.nls.role_invalid_user,
						"conversationMap.null": window.app.nls.conversationMap_null
					}
					if(errorHandle&&errorHandle.errorsFilter){
						if(errorHandle.noLoginNetBank){
							if(errorHandle.errorFun&&$.isFunction(errorHandle.errorFun)){
								errorHandle.errorFun(data);
							}
						}else if (data && data.code && data.code in errorsFilterMap) {
							if(Common.currentRouterKey == 'BocAbroad_login'){
								return;
							}
							Common.params.targetRouter={};
                            if(errorHandle.errorFun&&$.isFunction(errorHandle.errorFun)){
                                errorHandle.errorFun(data);
                            }
							Common.triggerRouter({routerKey:"BocAbroad_login"});
							Common.currentRouterKey = 'BocAbroad_login';
						}else if(errorHandle.isExceptionError){
							if(errorHandle.errorFun&&$.isFunction(errorHandle.errorFun)){
								errorHandle.errorFun(data);
							}
						}else{
							Common.logoutToApp(data);//退出登录
						}
					}else{
						if (data && data.code && data.code in errorMap) {
								Common.logoutToApp(data);//退出登录
						} else {
							if(errorHandle&&errorHandle.errorFun&&$.isFunction(errorHandle.errorFun)){
								errorHandle.errorFun(data);
							}else{
								Common.showMessage(data.message || window.app.nls.l_network_error);
							}
						}
					}
				}
			}else{ //返回body为null时
				Common.showMessage(data.message || window.app.nls.l_network_error);
			}
		},
		
		/**
		 * 判断Ajax请求返回是否异常
		 * @memberof Common.Ajax
		 * @function
		 * @name isSuccesful
		 * @param (Object) data - json对象
		 * @returns (bool) 无异常返回true,异常时返回false
		 */
		isSuccesful: function(data) {
			return !data._isException_;
//            return data.response[0].status == "01";
		},
		
		/**
		 * 根据错误代码获取错误提示消息
		 * @memberof Common.Ajax
		 * @function
		 * @name getMsgByCode
		 * @param (String) errorCode - 错误代码
		 * @returns (String) 返回友好的错误提示消息
		 */
		getMsgByCode: function(errorCode) {
			var code = undefined;
			if (errorCode.code) {
				code = errorCode.code;
			} else {
				code = errorCode;
			}
			if (errorCode && Common.errorCode[code])
				return Common.errorCode[code] || "";
			else
				return errorCode ? window.app.nls.l_error_1 + errorCode.code : window.app.nls.l_error_2;
		},
		/**
		 * 空数据处理
		 * @memberof Common.Ajax
		 * @function
		 * @name replaceNull
		 * @param (Json) jsn - Json数据
		 * @returns (Json) 返回规范的Json数据
		 */
		replaceNull: function(jsn) {
			if (jsn) {
				for (var p in jsn) {
					if (jsn[p] == null)
						jsn[p] = "";
					if (typeof jsn[p] == 'object') {
						jsn[p] = Common.Ajax.replaceNull(jsn[p]);
					}
				}
			}
			return jsn;
		},
		/**
		 * 设置agent参数，请求头中发送的渠道标识
		 * @memberof Common.Ajax
		 * @function
		 * @name agent
		 * @param (String) tel - 电话号码?
		 * @returns (Json) 设置Common.params.agentInfo的值
		 */
		agent: function(tel) {
			if(!tel){
				Common.params.agentInfo = 'X-IPAD';
			}
			var reg = /^1\d{10,15}$/;
			if (reg.test(tel)) {
				Common.params.agentInfo = 'X-IOS';
			} else {
				Common.params.agentInfo =  'X-IPAD';
			}
		}
	};
	
	/**
	 * 登出方法
	 * @memberof Common
	 * @function
	 * @name logout
	 * @param (Json) data - 登出时的提示消息
	 * @example Common.logout({message:"您确认要退出吗？"});
	 */
	Common.logout = function(data){
		Common.showMessage(data.message,function(){
			Common.Ajax.postRequest({attributes: {method: "Logout",params: {}}}).then(function(resData) {
                window.Router && window.Router.destroy && window.Router.destroy();
                var oldRouter = Common.params.targetRouter;
            	Common.params.targetRouter = {};
            	Common.params.targetRouter.routerKey = 'login';
                Common.triggerRouter(Common.params.targetRouter, function(){
                	Common.params.targetRouter = oldRouter;
                });
			});	
		});
	};

    /**
     * 登出方法退出到原生APP
     * @memberof Common
     * @function
     * @name logout
     * @param (Json) data - 登出时的提示消息
     * @example Common.logout({message:"您确认要退出吗？"});
     */
    Common.logoutToApp = function(data){
        Common.showMessage(data.message,function(){
            try{
                if(Common.terminal.ios){
                    BOCWebViewJavascriptBridge.callHandler('bocCommeCoinBack','', function(response) {
                    });
                }else{
                    NATIVE_close();
                }
            }catch(e){}
        });
    };
	
	/**
	 * 确定提示框
	 * @memberof Common
	 * @function
	 * @name showMessage
	 * @param (String) message - 提示框消息
	 * @param (Function) callback - 关闭提示框时的回调方法
	 * @example Common.showMessage("您还有5次抽奖机会，点击确认继续", function(){ 
	 *	    //TODO
	 * });
	 */
	Common.showMessage = function(message,callback) {
		Common.showAlert(message, [window.app.nls.l_comfirm],callback);
	};

	/**
	 * 提示框
	 * @memberof Common
	 * @name showAlert
	 * @function
	 * @param (String) message - 提示框消息
	 * @param (String) Arr - 提示框按钮标签
	 * @param (Function) callback - 关闭提示框时的回调方法
	 * @param (String) result - 交易的成功或失败标记,成功为可以不传或者success，失败为fail
	 * @example Common.showAlert("您还有5次抽奖机会，点击确认继续", ["确定", "取消"], function(){
	 *	    //TODO
	 * });
	 */
	Common.showAlert = function(message, Arr,callback,result) {
		var height = document.body.scrollHeight+350;
		if (Arr.length > 0) {
			$('.alertcon').remove();
            $('.alertOuter').remove();
            var html = '<div class="alertOuter" style="height:' + height +'px;"></div>' +
                       '<div class="alertcon"> <p>' + message + "</p>" + '</div>';
            $(html).appendTo('body');
            var wid = $(document).width()*0.8;
            wid = wid > 400 ? 400 : wid;
            $('.alertcon').css({'width':wid+'px', 'left': ($(window).width()-wid)/2+'px'});
            var spans = '';
            if (Arr.length == 1) {
                spans = '<span class="obtn">' + Arr[0] + '</span>';
            } else {
                spans = '<span><em  class="obtn">' + Arr[0] + '</em><em class="cbtn">' + Arr[1] + '</em></span>';
            }
            $(spans).appendTo('.alertcon');
            function close(){
                $('.alertOuter').remove();
                $('.alertmasker').remove();
                $('.alertcon').remove();
            };
            $('.alertcon .obtn').on('click', function() {
                close();
                if(callback && $.isFunction(callback)){
                    callback();
                }else if($.isArray(callback) && $.isFunction(callback[0])){
                    callback[0]();
                }
                if($('.inCode').length>0){
                	$('.inCode').val('');
                }
            });
            $('.alertcon .cbtn').on('click', function() {
                close();
                callback && $.isArray(callback) && callback[1] && $.isFunction(callback[1]) && (callback[1]());
            });
		} else {
			var width = $(window).width(),
				widths = (width - 150) / 2 + 'px';
			var img = 'finish2.png';	//根据不同结果加载不同图片
			if("fail" == result){
				img = 'failure.png';
			}
			var html = '<div class="alertOuter alertOuters" style="height:' + height + 'px;"></div><div class="alert_out_con" style="left:' + widths + ';top:120px;"></div>' + '<div class="alert_out_tit" style="left:' + widths + ';top:120px;"><em><img src="images/' + img + '"></em><p>' + message + '</p></div>';
			$(html).appendTo('body');
			setTimeout(function() {
				$('.alertOuter').remove();
				$('.alert_out_con').remove();
				$('.alert_out_tit').remove();
			}, 3000);
		}
	};
    Common.popup = {
        single: function(message, okfunction){
            var height = document.body.scrollHeight+350;
			var str = window.app.nls.l_cancel;
			var strok = window.app.nls.l_comfirm;
            var html = '<div class="boc-popup" style="right: 0;">'+
                            '<div class="dlg_content_body">'+
                                    '<div class="boc-popup-c">' +
                                        '<span class="boc-popup-c-ui">' + (message || '删除后数据将不会复原，确认要删除吗？') + '</span>' +
                                    '</div>'+
                            '</div>'+
                            '<div class="boc-btn">' +
                                    '<div class="boc-cancel">'+str+'</div>'+
                                    '<img class="boc-btn-line" src="images/btn-line.png">'+
                                    '<div class="boc-sure">'+strok+'</div>'+
                            '</div>'+
                         '</div>'+
                        '<div class="alertOuter" style="height:' + height + 'px;z-index:1001;"></div>';
            $('body').append(html);
            $('.boc-btn .boc-cancel').on('click', function(e){
                $('.boc-popup').remove();
                $('.alertOuter').remove();
            });
            $('.boc-btn .boc-sure').on('click', function(e){
                $('.boc-popup').remove();
                $('.alertOuter').remove();
                okfunction && okfunction()
            });
        }
    }
	Common.popdown = {
		single: function(message, okfunction) {
			$('.boc-popdown').remove();
			var html = '<div class="boc-popdown"><div class="boc-center">'+message+'</div></div>';
			$('body').append(html);
		}
	}
//window.app.nls.l_close
    Common.fullLayer = {
        html: '<div class="fulllayer">' +
                '<div class="dlg_content_body"></div>' +
                '<div class="fullLayerMask"> </div>' +
              '</div>',

        onCancel: null,
        onSure: null,
        customerClass: '',
        /**
         * params: {view: view, onCancel: function(){}, onSure:function(){},closeByMask:true}
         * mod: 0/undefined--模态对话框，只出现一个; 1--可弹出多个的层
         */
        show: function(params, mod){
            params && $.extend(this, params);
            var _t = this, node;
            if(!mod){
                if( $('#fullModeDialog').length > 0){
                    node = $('#fullModeDialog');
					if(node.attr('removing')){
						node.remove();
						this.show(arguments);
					}
                    node.show();
//                    $('.fullLayerMask').show();
                }else{
                    node = $(this.html).attr('id', 'fullModeDialog');
                    $('body').append(node);
                    node.find('.nav-back').on('click', _t.cancel);
                }
            }else{
                node = $(this.html);
                $('body').append(node);
                node.find('.nav-back').on('click', _t.cancel);
            }
			node.addClass('fade-in');
            if(params.closeByMask){
                node.find('.fullLayerMask').on('click', _t.cancel).on('touchmove', function(e){
					e.preventDefault();                	
                });
            }

            if(params.classes){
                if(params.classes.indexOf('halfLayer') != -1){
                    node.addClass('halfLayer');
                    params.classes = params.classes.replace(/halfLayer/g, '');
                    node.find('.dlg_content_body').addClass(params.classes);
                }else{
                    node.find('.dlg_content_body').addClass(params.classes);
                    node.siblings().hide();
                }
            }else{
                node.siblings().hide();
            }
            if(_t.view && typeof _t.view == "object"){
                _t.view.setOper && _t.view.setOper('html');
                _t.view.initDomNode && _t.view.initDomNode(node.find('.dlg_content_body'));
                _t.view.load && _t.view.load();
            }else if(_t.view == 'string'){
                node.find('.dlg_content_body').html(_t.view);
            }
//            $(document).on('scroll', function(){
//                $('.fulllayer').css('top', $('body')[0].scrollTop + 'px');
//            });
        },
        close: function(e){
            var nd = e && $(e.target) || $('#fullModeDialog');
            while(nd && !nd.hasClass('fulllayer')){
                nd = nd.parent();
            }
			if(nd.hasClass('halfLayer')){
				nd.siblings().show();
                nd.remove();
			}else{
				nd.addClass('fade-out').attr('removing', 'true');
				nd.on('webkitAnimationEnd', function(){
                    nd.siblings().show();
					nd.remove();
				});
			}

			//setTimeout(function(){nd.remove()}, 150);
            //nd.remove();
//            $('.fullLayerMask').hide();
        },
        cancel: function(e){
            var nd = e && $(e.target) || $('#fullModeDialog');
            while(nd && !nd.hasClass('fulllayer')){
                nd = nd.parent();
            }
			if(nd.hasClass('halfLayer')){
                nd.siblings().show();
				nd.remove();
			}else{
				nd.addClass('fade-out').attr('removing', 'true');
				nd.on('webkitAnimationEnd', function(){
                    nd.siblings().show();
					nd.remove();
				});
			}
			//setTimeout(function(){nd.remove()}, 150);
            //nd.remove();
//            $('.fullLayerMask').hide();
            this.onCancel && this.onCancel();
        },
        sure: function(data, e){
            this.close(e);
            this.onSure && this.onSure(data);
        }
    }

	/**
	 * 确认提示框
	 * @memberof Common
	 * @function
	 * @name confirm
	 * @param (String) message - 提示框消息
	 * @param (String) Arr - 提示框按钮标签
	 * @param (Function) callback - 关闭提示框时的回调方法
	 * @example Common.confirm("您还有5次抽奖机会，点击确认继续", ["确定", "取消"], function(){ 
	 *     //TODO
	 * });
	 */
	Common.confirm = function(message, Arr,callback) {
		var height = document.body.scrollHeight+350;
		if (Arr.length > 0) {
			$('.alertcon').remove();
			$('.alertOuter').remove();
			var html = '<div class="alertOuter" style="height:' + height +'px; top:-240px;"></div>' + '<div class="alertcon" style="top:100px;"> <p>' + message + "</p>" + '</div>';
			$(html).appendTo('body');
			var spans = '';
			if (Arr.length == 1) {
				spans = '<span>' + Arr[0] + '</span>';
			} else {
				spans = '<span><em id="em1">' + Arr[0] + '</em><em id="em2">' + Arr[1] + '</em></span>';
			}
			$(spans).appendTo('.alertcon');
			if(Arr.length == 1){
				$('.alertcon span').on('click', function() {
					$('.alertOuter').remove();
					$('.alertcon').remove();
					if(callback&&$.isFunction(callback)){
						callback();
					}
				});
			}else{
				if(callback&&$.isFunction(callback)){
					$('#em2').on('click', function() {
						$('.alertOuter').remove();
						$('.alertcon').remove();
						callback();
					});
				}
				$('#em1').on('click', function() {
					$('.alertOuter').remove();
					$('.alertcon').remove();
					$('#maskbox').remove();
				});
			}
			
		} else {
			var width = $(window).width(),
				widths = (width - 150) / 2 + 'px';
			var html = '<div class="alertOuter alertOuters" style="height:' + height + ';"></div><div class="alert_out_con" style="left:' + widths + ';top:120px;"></div>' + '<div class="alert_out_tit" style="left:' + widths + ';top:120px;"><em><img src="images/finish2.png"></em><p>' + message + '</p></div>'
			$(html).appendTo('body');
			setTimeout(function() {
				$('.alertOuter').remove();
				$('.alert_out_con').remove();
				$('.alert_out_tit').remove();
			}, 3000);
		}
	};

	/**
	 * 正在加载提示框实例对象
	 * @namespace Common.maskBox
	 * @name Common.maskBox
	 */
	Common.maskBox = {
		adArr: [],
		/**
		 * 显示加载提示框
		 * @memberof Common.maskBox
		 * @function
		 * @name show
		 * @example Common.maskBox.show();
		 */
		initAdTips: function(callback){
			var _t=this;
			var js={		
				method:"queryLoadInfoList",
				header:{
					"agent":"WEIXIN",
					"version": "1.0",
					"device": "Apple,New Pad,MD368ZP",
					"platform": "Apple,iOS,5.1.1",
					"plugins": "",
					"page": "FF001",
					"local": "zh_CN",
					"ext": ""
				},	
				params:{
				  appid:Common.urlParams.appid,
				  menuEntrance:Common.urlParams.entrance,
				  openId_log:Common.params.openId,
				  pubId_log:Common.params.pubid
				}
			};
			var jsn = "json=" + JSON.stringify(js).replace(/"null"/g, '""').replace(/\+/g, "%2B").replace(/\&/g, "%26").replace(/\=/g, "%3D");
			var tit='';
			var url = Common.urlParams.demo ? "data/queryLoadInfoList.json" : '/WeiBankPublicModule/_bfwajax.do';
			$.ajax({
				url: url,
				type: Common.urlParams.demo ? 'get' : 'post',
				dataType:'json',
				data:jsn,
				success:function(data){console.log(data);
					for(var i=0;i<data.result.data.length;i++){
						_t.adArr.push(data.result.data[i].title);
					}
					if(callback&&$.isFunction(callback)){
						callback();
					}
				}
			});
		},
		show: function() {
			var _t=this;
			var width = $(window).width(),
			widths = (width - 50) / 2 + 'px';
			var html='';
			if(_t.adArr.length>0){
				var len=_t.adArr.length;
				html='<div id="maskbox">'
					+'<div class="maskbox_bg">'
					+'<div class="maskbox_con"></div>'
					+'<div class="maskbox_img_bg">'
						+'<span class="maskbox_img">'

						+'</span>'
						+'<em>'+_t.adArr[Math.round(Math.random()*(len-1))]+'</em>'
					+'</div>'
					+'</div>'
					+'<img id="loadingNew" src="images/new_loading.png" border="0" class="ui-loading"/>'
				+'</div>'
			}else{
				html='<div id="maskbox">'
						+'<div class="maskbox_bg">'
						+'<div class="maskbox_conss">'
						 	+'<div class="maskbox_cons">'
						 		+'</div>'
						 	+'</div>'
						+'</div>'
					+'<img id="loadingNew" src="images/new_loading.png" border="0" class="ui-loading"/>'
					+'</div>'

					//+'<div>'
			}
			$(html).appendTo('body');
			if($('#maskbox').children().hasClass('maskbox_img_bg')){
				if($('.maskbox_img_bg em').height()>20&&$('.maskbox_img_bg em').height()<=40){
					$('.maskbox_img_bg em').css({'padding-top':'10px'})
				}else if($('.maskbox_img_bg em').height()<=20){
					$('.maskbox_img_bg em').css({'padding-top':'20px'})
				}
			}
		},
        showBottomTip: function(el){
            $(el).append('<div class="bottomtip"><img src="images/loadding_small.png" id="smartLoading"><span>正在加载中...</span></div>');
        },
        removeTip: function(el){
            $('.bottomtip', el).remove();
        },
		/**
		 * 隐藏加载提示框
		 * @memberof Common.maskBox
		 * @function
		 * @name hide
		 * @example Common.maskBox.hide();
		 */
		hide: function() {
			$('#maskbox').remove();
		}
	};
//	Common.maskBox.initAdTips();
	
	/**
	 * 公共加密类
	 * @namespace Common.encrypt
	 * @name Common.encrypt
	 */
	Common.encrypt = {
		/**
		 * BASE64编码
		 * @memberof Common.encrypt
		 * @function
		 * @name encode64
		 * @param (String) input - 要加密的原文
		 * @returns (String) 编码后的密文
		 * @example Common.encrypt("输入要加密的原文");
		 */
		encode64: function(input) {
			var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";
			var output = "";
			var chr1, chr2, chr3 = "";
			var enc1, enc2, enc3, enc4 = "";
			var i = 0;
			do {
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
				output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);
				chr1 = chr2 = chr3 = "";
				enc1 = enc2 = enc3 = enc4 = "";
			} while (i < input.length);
			return output;
		}
	};
    /**
     * new Common.ScrollPagination({
     *    pageSize: 10,
     *    recordNum: 10000,
     *    el: $('#xxx').
     *    getPageData: function(pageSize, currentPage, startIdx, endIdx, callback){
     *       Model.interAction({
     *           function(data){
     *              appendIntoEL(data)
     *           }
     *       })
     *    }
     * })
     * @type {Class}
     */
	Common.ScrollPagination = $.extendCls({
		pageSize: 10,
		recordNum: null,
		totalPage: null,
		currentIdx: 0,
		currentPage: 1,
		el: null,
		context: null,
		rowClsName: "w_pagination_row",
		_constructor: function(obj){
			if(!obj.el){
				throw new Error("未传入滚动翻页的容器！");
			}
			if(!obj.getPageData){
				throw new Error("未传入获取数据的方法！");
			}
			var _t = this;
			$.extend(_t, obj);
            $(_t.el).css('overflow-y', 'auto').css('-webkit-overflow-scrolling', 'touch');
			$(_t.el).on("scroll", function(){
				var h=$(_t.el)[0].tagName == "body" ? $(window).height() : $(_t.el).height(), s=$(_t.el).scrollTop(), sh=$(_t.el).get(0).scrollHeight;
				h+s>=sh && (_t.onPaging());
			});
			_t.getPageData.apply(_t.context||this, [_t.pageSize, _t.currentPage, _t.currentIdx, _t.currentIdx+_t.pageSize-1, _t.setRecordNum]);
		},
		setRecordNum: function(num){
			this.recordNum = num;
			if(!this.recordNum){
				return false;
			}
			this.totalPage = Math.ceil(num/this.pageSize);
		},
		setTotalPage: function(num){
			this.totalPage = num;
			if(!this.totalPage){
				return false;
			}
			this.recordNum = this.pageSize*num;
		},
        showLoadingTipAtBtm: function(){
            //正在加载中...
            var _t = this;
			$(".bottomtip").remove();
			var str = window.app.nls.ovs_gy_loading;
            $(_t.el).append('<div class="bottomtip"><img src="images/loadding_small.png" id="smartLoading">'+str+'</div>');
		},
        showOverTipAtBtm: function(){
            var _t = this;
			$(".bottomtip").remove();
			var str = window.app.nls.ovs_gy_nomoreacontent;
            $(_t.el).append('<div class="bottomtip">'+str+'</div>');
            //setTimeout(function(){
			//    _t.removeTip();
			//}, 2000);
        },
        removeTip: function(){
            $(this.el).find('.bottomtip').remove();
        },
		onPaging: function(){
			var _t = this;
			if(!(typeof _t.recordNum === "undefined" || _t.recordNum === null || typeof _t.totalPage === "undefined" || _t.totalPage === null)){
				if(_t.recordNum <= 0 || _t.currentPage >= _t.totalPage){
					return false;
                    _t.showOverTipAtBtm();
				}
			}
			var curRowNum = $(".w_pagination_row", _t.el).length;
			_t.currentPage = Math.ceil(curRowNum/_t.pageSize) + 1;
			_t.currentIdx  = ++curRowNum;
            _t.showLoadingTipAtBtm();
			_t.getPageData.apply(_t.context||this, [_t.pageSize, _t.currentPage, _t.currentIdx, _t.currentIdx+_t.pageSize-1, _t.setRecordNum]);
		},
		//please overwride this function
		getPageData: function(pageSize, currentPage, startIdx, endIdx, callback){
			
		}
	});
	
	/**
	 * 集合类
	 * @namespace Common.Hashtable
	 * @name Common.Hashtable
	 */
	Common.Hashtable = function() {
		this._hash = new Object();
	};
	Common.Hashtable.prototype = {
		/**
		 * 长度
		 * @memberof Common.Hashtable
		 * @name length
		 * @type Number
		 * @example Common.Hashtable.length;
		 */
		length: 0,
		/**
		 * 添加子项
		 * @memberof Common.Hashtable
		 * @function
		 * @name add
		 * @param (String) key - key
		 * @param (Object) val - value
		 * @example Common.Hashtable.add("key", {"a": 1});
		 */
		add: function(key, val) {
			if (key !== undefined) {
				if (!this.contains(key)) {
					this._hash[key] = (val === undefined ? null : val);
					this.length++;
					return true;
				}
			};
			return false;
		},
		/**
		 * 清空集合
		 * @memberof Common.Hashtable
		 * @function
		 * @name clear
		 * @example Common.Hashtable.clear();
		 */
		clear: function() {
			for (var k in this._hash) {
				delete this._hash[k];
			};
			this.length = 0;
		},
		/**
		 * 查询子项
		 * @memberof Common.Hashtable
		 * @function
		 * @name contains
		 * @param (String) key - key
		 * @example Common.Hashtable.contains("aa");
		 */
		contains: function(key) {
			return this._hash[key] !== undefined;
		},
		/**
		 * 查询集合长度
		 * @memberof Common.Hashtable
		 * @function
		 * @name count
		 * @returns (Number) 集合长度
		 */
		count: function() {
			return this.length;
		},
		/**
		 * 检查集合是否为空
		 * @memberof Common.Hashtable
		 * @function
		 * @name isEmpty
		 * @returns (bool) 空返回true，否则false
		 * @example Common.Hashtable.isEmpty();
		 */
		isEmpty: function() {
			return this.length == 0;
		},
		/**
		 * 获取或设置子项的值
		 * @memberof Common.Hashtable
		 * @function
		 * @name item
		 * @param (String) key - key
		 * @param (Object) val - val 传入值时设置，不传入时取值
		 * @returns (Object) 取值时返回Object，设置时返回undefined
		 * @example 
		 * Common.Hashtable.item("aa"); 
		 * Common.Hashtable.item("aa", 11);
		 */
		item: function(key, val) {
			if (val) {
				this._hash[key] = val;
			};
			return this._hash[key];
		},
		
		/**
		 * 删除子项
		 * @memberof Common.Hashtable
		 * @function
		 * @name remove
		 * @param (String) key - key
		 * @example Common.Hashtable.remove("aa");
		 */
		remove: function(key) {
			delete this._hash[key];
			this.length--;
		}
	};

	/**
	 * 模块基类类定义，注意，类是需要new的
	 * 抽象了模块类的某些通用执行方式如EJSpath， loadStyleFiles等
	 * @namespace Common.Module
	 * @name Common.Module
	 * @class
	 */
	Common.Module = $.extendCls(function(){}, {
		EJSPATH: {},
		CSSPATH: [],
		isSync: false,
		_constructor: function(args){
			//根据isExcuteInit标志决定是否自动执行init方法
			if( !Common.params.targetRouter.isExcuteInit ){
				Common.loadStyleFiles(this.CSSPATH,$.proxy(this.init,this), args,this.isSync);
			}
		}
	});
	/**
	 * 从模块基类派生新模块，拥有模块通性：加载css文件并执行init方法
	 * @memberof Common.Module
	 * @function
	 * @name sub
	 * @function
	 * @example 
	 * Common.Module.sub({
	 *     EJSPATH: {
	 *         "_MAIN": "views/aroundQuery/aroundQuery.ejs"
	 *     },
	 *     CSSPATH: ["styles/aroundQuery.css"],
	 *     init: function(){
	 *         //TODO
	 *     }
	 * }
	 */
	Common.Module.sub = function(o){
		return $.extendCls(Common.Module, o);
	};
	
	/**
	 * 日志记录对象
	 *   1、可以在你需要的地方使用Common.log.debug,Common.log.info, Common.log.error进行记录日志
	 *   2、通过打开log.html查看已记录了的日志
	 * @namespace Common.log
	 * @name Common.log
	 */
	Common.log = {
		/**
		 * 日志记录等级
		 * @memberof Common.log
		 * @name mod
		 * @type bool
		 */
		logmod: 3,
		/**
		 * 日志等级定义
		 * @memberof Common.log
		 * @name def
		 * @type Object
		 */
		def: {"DEBUG": 3, "INFO": 2, "ERROR": 1, "NONE": 0},
		/**
		 * 最大日志记录数
		 * @memberof Common.log
		 * @name maxlength
		 * @type Number 
		 */
		maxlength: 1000,
		/**
		 * 记录debug信息
		 * @memberof Common.log
		 * @function
		 * @name debug
		 * @param (String) msg - 日志消息
		 * @example Common.log.debug("取pubid正常返回，puid=" + data.pubid);
		 */
		debug: function(msg, url){
			if(this.logmod >= this.def.DEBUG){
				this.add("DEBUG", msg, null, url);
				console && console.log(msg);
			}
		},
		/**
		 * 记录程序执行信息
		 * @memberof Common.log
		 * @function
		 * @name info
		 * @param (String) msg - 日志消息
		 * @example Common.log.info("用户已关注，准备进入指定模块。");
		 */
		info: function(msg){
			if(this.logmod >= this.def.INFO){
				this.add("INFO", msg);
				console && console.log(msg);
			}
		},
		/**
		 * 记录程序执行信息
		 * @memberof Common.log
		 * @function
		 * @name error
		 * @param (String) msg - 日志消息
		 * @param (String) stack - 错误堆栈
		 * @example Common.log.error("uncaught error: id parameter is needed.", "uncaught error at app.js line 10 char32.");
		 */
		error: function(msg, stack){
			if(this.logmod >= this.def.ERROR){
				this.add("ERROR", msg, stack);
                console && console.error(stack);
			}
		},
		/**
		 * 组织日志信息
		 * @memberof Common.log
		 * @function
		 * @name add
		 * @param (String) type - 日志类型
		 * @param (String) msg - 日志消息
		 * @param (String) stack - 错误堆栈
		 * @private
		 * @example Common.log.add("ERROR", "uncaught error: id parameter is needed.", "uncaught error at app.js line 10 char32.");
		 */
		add: function(type, msg, stack, url){
			var item = {
				date: new Date().toLocaleString(),
				type: type,
				msg: msg,
				stack: stack || "",
				url:url || ""
			}
			this.writeStore(item);
		},
		/**
		 * 组织日志信息
		 * @memberof Common.log
		 * @function
		 * @name writeStore
		 * @param (Object) item - 日志消息对象
		 * @private
		 * @example Common.log.writeStore({
		 *     date: new Date(), 
		 *     type: "INFO", 
		 *     msg: "用户已关注，准备进入指定模块。", 
		 *     stack: ""
		 * });
		 */
		writeStore: function(item){
			var log = (localStorage.getItem("log") || "").toObject();
			var len = 0;
			for(var f in log){
				log.hasOwnProperty(f) && len++;
			}
			//超出最大记录数后重置
			if(len >= this.maxlength){
				this.clear();
				len = 0;
			}
			log["item" + len] = item;
			localStorage.setItem("log", JSON.stringify(log));
		},
		clear: function(){
			localStorage.removeItem("log");
		}
	};

    Common.setPageStyle = function(opt){
        opt.footer && $('.wrap').removeClass('.wrap-justhead').addClass('.wrap-headfooter');
        opt.redHead && $('.wrap').addClass('.wrap-redHead');
        opt.goBack && ($('.page-head img').on('click', opt.goBack)) || ($('.page-head img').on('click', function(){
            //go back to native app;
        }));
    }
	
	return Common;
});