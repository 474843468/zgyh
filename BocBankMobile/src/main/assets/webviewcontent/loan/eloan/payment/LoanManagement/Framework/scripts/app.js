"use strict";
require(['./appconfig'], function(config){
    require.config(config);
    var urlparams = (function(){
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
        var params = JSON.parse(search);
        return params;
    })();
    var locale = (urlparams._locale ? urlparams._locale : (navigator.language == 'zh-CN' ? 'zh_cn' : 'en_us')).toLowerCase();
    require(["zepto", "common", "model","routesUtil", "nls_public_" + locale, 'stackCtrl'], function($, Common, Model,RoutesUtil, nls, stackCtrl){
        var args = arguments;
        var cls = $.extendCls({
            /**
             * 构造函数，默认执行
             * @memberof APP
             * @name _constructor
             * @constructs
             */
            _constructor: function(){
                this.loaded = false;
                this.nls = nls;
                this.onError();
                this.urlParams = Common.urlParams;
                this.parseRoute();
                this.loaded = true;
                this.load();
                var _t = this;

                var aopHandler = {
                    "after": function(res, args) {
                        if(!args || !args[1]){
                            Common.changeLanguage($.lastInserts());
                        }
                        return res;
                    }
                }
                $.funAop($.fn, {
                    "html": aopHandler,
                    "append": aopHandler
                });
            },
            /**
             * 页面资源加载完成后需要做的事情
             * @memberof APP
             * @name load
             * @function
             */
            load: function(){
                var _t = this;
                require(["domReady", "ejs"], function (domReady) {
                    domReady(function () {
                        _t.vStack = new stackCtrl('.stacks');
                        if(Common.terminal.ios){
                            //$('html').css('margin-top', '20px');
                            //alert("ios");
                            // $('html').addClass('ios');
                            //require(["cordova_ios"], function(cordova){
                                _t.routeToModule();
                            //});
                        } else if (Common.terminal.android) {
                            //alert("android");
                            //require(["cordova_android"], function(){
                                _t.routeToModule();
                            //});
                        }else{
                            _t.routeToModule();
                        }
                        //else{
                        //    require(["cordova_android"], function(){});
                        //}


                    });
                });
            },
            /**
             * 根据传入的参数，解析模块路由
             * @memberof APP
             * @name parseRoute
             * @function
             */
            parseRoute: function(){
                var entryRouterKey, _w_p = this.urlParams, _c_p = Common.params;
                this.shareFlag = false;
                this.routerPath = "";
                if(!_w_p || !_w_p.entrance){
                    throw new Error("Application address configuration errors, you must have an 'entrance' parameter");
                }
                entryRouterKey = _w_p.entrance;
                _c_p.openId = _w_p.openid || "";
                _c_p.pubid = _w_p.pubid || "";
                _c_p.Code = _w_p.code || "";
                _c_p.appid = _w_p.appid || "";

                //閼惧嘲褰噐outerPath
                this.routerPath = Common.getRouterPath(entryRouterKey);
                if (typeof this.routerPath === "undefined") {
                    throw new Error("Router key '" + entryRouterKey + "' is not defined");
                }

                // 解析url后面的参数
                _c_p.targetRouter.entryRouterKey = entryRouterKey || "";
                _c_p.targetRouter.excuteFun =  _w_p.shareFun || "";
                _c_p.targetRouter.funArgs =  _w_p.shareArgs || "";
                _c_p.targetRouter.funArgs = _c_p.targetRouter.funArgs.replace(/\[|\]/g, "").split("_");
                _c_p.targetRouter.routerKey = _w_p.target || _w_p.sharekey || _w_p.shareKey || "";
                if(_w_p.sharekey || _w_p.shareKey ){
                    this.shareFlag = true;
                    _c_p.targetRouter.shareFlag = true;
                }
            },
            /**
             * 有code时，验证code，获取openid成功后route到指定模块
             * @memberof APP
             * @name routeToModule
             * @function
             */
            routeToModule: function(){
                var _t = this;
                Common.Router(_t.routerPath, _t.shareFlag);
            },
            /**
             * 定义当页面发送错误时的处理
             * @memberof APP
             * @name onError
             * @function
             */
            onError: function(){
                require(["common"],function(Common){
                    require.onError = function(err){
                        if(err.requireType === "timeout"){
                            $("#maskbox").remove();
                            Common.showAlert('当前访问人数过多，请点击确定重试', ['确定'],function(){
                                window.location.reload();
                            });
                        }
                        throw err;
                    }
                });
                window.onerror = function(msg, file, lineNum, charNum, errorObj){
                    Common.log.error(msg, errorObj && errorObj.stack || "");
                }
            }
        });
        $.funAop(cls, {
            _constructor: {
                before: function(){
                    config.onAppInitBefore && config.onAppInitBefore.apply(window.app, args);
                },
                after: function(){
                    config.onAppInitAfter && config.onAppInitAfter.apply(window.app, args);
                }
            },
            routeToModule: {
                before: function(){
                    config.onRouteBefore && config.onRouteBefore.apply(window.app, args);
                },
                after: function(){
                    config.onRouteAfter && config.onRouteAfter.apply(window.app, args);
                }
            }
        });
        window.app = new cls();
        Common.changeLanguage('body');
    });
});