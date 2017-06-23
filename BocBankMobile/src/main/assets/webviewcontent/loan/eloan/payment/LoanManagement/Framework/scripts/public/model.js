/**
 * 框架model对象，负责封装所有收发请求
 * @namespace Model
 * @name Model
 * @requires zepto, common
 */
define('model', ['zepto', 'common', 'routesUtil'], function($, Common, routesUtil) {
	var Model = function(method, loadingMask, params, conversationId,changeAgent, ontimeout) {
		this.attributes = {
			method: null,
			conversationId: null,
			params: null,
			changeAgent:null,
            loadingMask: true,
			ontimeout: null
		};
		this.init(method, loadingMask, params, conversationId,changeAgent, ontimeout);
	};

	Model.prototype.init = function(method, loadingMask, params, conversationId,changeAgent, ontimeout) {
		if (method) {
			this.attributes.method = method;
		} else {
			throw new Error("The Method Attributes Of Model Undefined...");
		}
        if(loadingMask != undefined){
            this.attributes.loadingMask = loadingMask;
        }
		if (conversationId) {
			this.attributes.conversationId = conversationId;
		};
		if (params) {
			this.attributes.params = params;
		};
		if(changeAgent){
			this.attributes.changeAgent = changeAgent;
		}
		if(ontimeout){
			this.attributes.ontimeout = ontimeout;
		}
		return this;
	};

	Model.prototype.pagination = function(i, pageSize, refresh) {
		if (!this.attributes.params) {
			this.attributes.params = {};
		}
		if (pageSize) {
			this.attributes.params.pageSize = pageSize + "";
		}
		if (refresh !== undefined) {
			if (refresh) {
				this.attributes.params._refresh = "true";
			} else {
				this.attributes.params._refresh = "false";
			}
		} else {
			this.attributes.params._refresh = "false";
		}
		this.attributes.params.currentIndex = (i - 1) * this.attributes.params.pageSize + "";
		return this;
	};
	/**
	 * 接口请求
	 * @memberof Model
	 * @function
	 * @name interaction
	 * @param {Object} modelData - 请求数据
	 * @param {Object} url - 请求地址，可以传入"BII","WBK","WPM"等简写地址(注："WPM"以不再使用 2014.11.19)
	 * @param {Object} callback - 接口成功返回时的回调函数
	 * @param {Object} errorHandle - 接口异常时的回调函数
	 * @param {Object} async - 是否同步
	 * @example
	 * var reqdata = {
	 *     method : "subscribeStatus",
	 *     params : {
	 *         queryflag: "0",
	 *         id: this.urlParams.id
	 *     }
	 * }
	 * Model.interaction(reqdata,"WPM", function(data){
	 *     //获取到appid
	 *     var appid = data.appid;
	 *     Common.params.pubid = data.pubid;
	 *     window.location.reload();
	 * }, {errorFun: function(err){
	 *     Common.Router(_t.routerPath, _t.shareFlag);
	 * }});
	 */
	Model.interaction = function(modelData, url, callback, errorHandle, ontimeout, async) {
		ontimeout = ontimeout || (errorHandle && errorHandle.errFun) || null;
		return Common.Ajax.postRequest(new Model(modelData.method, modelData.loadingMask, modelData.params, modelData.conversationId,modelData.changeAgent, ontimeout), url, async).then(function(resData) {
			console.log(resData);
            var dataHandle = Common.Ajax.dataHandle(resData,errorHandle);
			//alert("modelData.params.extParams" + modelData.params.extParams  + "index" + modelData.params.extParams.index);
			if(modelData.params && modelData.params.extParams != null){
				dataHandle.extParams = modelData.params.extParams;
			}
			var success = Common.Ajax.isSuccesful(resData);
			if (success && callback && $.isFunction(callback)) {
				callback(dataHandle);
			}
		}).fail(function(err){
			if(errorHandle && errorHandle.errorFun){
				errorHandle.errorFun(Common.Ajax.getMsgByCode("ajaxError"));
			}else{
				Common.showMessage(Common.Ajax.getMsgByCode("ajaxError"));
			}
		});
	};
    Model.serviceRequest = function(mdl, callback, errorHandle, async){
        service = routesUtil.services[mdl.service];
        $.extend(mdl, {method: service.method})
        Model.interaction(mdl, service.url, function(data){
            if(service.dataRestruct){
                data = service.dataRestruct(data)
            }
            callback(data);
        }, errorHandle, errorHandle, async);
    };

    Model.postReqsWaitRes = function(mdls, callback, errorHandle){
        var datastore = {}, itvl;
        for(var i in mdls){
            mdls[i].reqStatu = false;
            var alias = mdls[i].alias||mdls[i].service;
            Model.serviceRequest(mdls[i], function(data){
                var d = {};
                d[alias] = data;
                $.extend(datastore, d);
                mdls[i].reqStatu = true;
            }, {errorFun: function(err){
                clearInterval(itvl);
                errorHandle && errorHandle.errorFun && errorHandle.errorFun(err) || Common.showMessage(Common.Ajax.getMsgByCode("ajaxError"));
            }});
        }
        itvl = setInterval(function(){
            var allDone = true;
            for(var i in mdls){
                if(!mdls[i].reqStatu){
                    allDone = false;
                }
            }
            if(allDone){
                clearInterval(itvl);
                callback && callback(datastore);
            }
        }, 100)
    }

    Model.postRequests = function(mdls, callback, errorHandle){
        var ontimeout = ontimeout || (errorHandle && errorHandle.errFun) || null;
        var models = [];
        for(var i in mdls){
            var modelData = mdls[i];
            service = routesUtil.services[mdl.service];
            $.extend(modelData, {method: service.method});
            models.push(Common.Ajax.oldRequest(new Model(modelData.method, modelData.loadingMask, modelData.params, modelData.conversationId,modelData.changeAgent, ontimeout), service.url, false))
        }
        return $.when(models).then(function(resData) {
            console.log(resData);
            var dataHandle = Common.Ajax.dataHandle(resData,errorHandle);
            //alert("modelData.params.extParams" + modelData.params.extParams  + "index" + modelData.params.extParams.index);
            if(modelData.params.extParams != null){
                dataHandle.extParams = modelData.params.extParams;
            }
            var success = Common.Ajax.isSuccesful(resData);
            if (success && callback && $.isFunction(callback)) {
                callback(dataHandle);
            }
        }).fail(function(err){
            if(errorHandle && errorHandle.errorFun){
                errorHandle.errorFun(Common.Ajax.getMsgByCode("ajaxError"));
            }else{
                Common.showMessage(Common.Ajax.getMsgByCode("ajaxError"));
            }
        });
    }

    Model.request = function(service, data, success, onerror, ontimeout){
        Model.interaction({
            method: service.method,
            params: service.params
        },service.url, function(data){
            success(service.dataRestruct && service.dataRestruct(data) || data);
        }, function(error){
            onerror && onerror(error);
        }, ontimeout);
    }

    Model.formSubmit = function(form, otherdata, success, onerror){

    }

	/**
	 * 接口请求
	 * @memberof Model
	 * @function
	 * @name slidingScreen
	 * @param {Object} modelData - 请求数据
	 * @param {Object} url - 请求地址，可以传入"BII","WBK","WPM"等简写地址(注："WPM"以不再使用 2014.11.19)
	 * @param {Object} callback - 接口成功返回时的回调函数
	 * @param {Object} errorHandle - 接口异常时的回调函数
	 * @param {Object} async - 是否同步
	 * @example
	 * var requestData = {
	 *     method:RoutesUtil.method.WBA.PsnCrcdQueryBilledTransDetail,
	 *     params : {
	 *         accountId:crcdCustomerInfo.creditcardId,
	 *         billDate:crcdCustomerInfo.billDate,
	 *         billCardType:crcdCustomerInfo.billCardType,
	 *         acctHash:crcdCustomerInfo.acctHash,
	 *         pageNo:_t.pageNo,
	 *         primary:_t.primary,
	 *         lineNum:pageSize
	 *     },
	 * };
	 * Model.interaction(requestData,"WBA",function(data){		
	 *     if(data){	
	 *         _t.slidingScreen({
	 *             requestData: requestData,
	 *             pageNo:(parseInt(_t.pageNo) + 1).toString(),
	 *             primary:data.crcdBilledQueryInfo.primary,
	 *             lineNum:pageSize,
	 *             pageCount:data.crcdBilledQueryInfo.sumNo,
	 *             callback: function(data2) {
	 * 	               _t._renderResultPage(data2.crcdBilledQueryInfo,false,pageSize);
	 *             }
	 *         },"WBA");
	 *         _t._renderResultPage(data.crcdBilledQueryInfo,true,pageSize);
	 *     }
	 * });
	 */
	Model.slidingScreen = function(iscrollParams, url) {
		$(window).scroll(function() {
			if (iscrollParams.point.iscroll) {
				var totalheight = parseFloat($(window).height()) + parseFloat($(window).scrollTop()),
					height = $(document).height();
				if (height <= totalheight) {
					var pageSize = iscrollParams.pageSize ? iscrollParams.pageSize : 10;
					if(iscrollParams.pageCount>1 && iscrollParams.pageCount != iscrollParams.currentIndex){ //只有一页或已经是最后一页不在查询
						iscrollParams.point.currentIndex = iscrollParams.currentIndex = iscrollParams.currentIndex + 1;
						Common.Ajax.oldRequest(new Model(iscrollParams.requestData.method, true, iscrollParams.requestData.params, iscrollParams.requestData.conversationId).pagination(iscrollParams.currentIndex, iscrollParams.pageSize, iscrollParams._refresh), url).then(function(resData) {
							var dataHandle = Common.Ajax.dataHandle(resData);
							var success = Common.Ajax.isSuccesful(resData);
							if (success && iscrollParams.callback && $.isFunction(iscrollParams.callback)) {
								iscrollParams.callback(dataHandle);
							}
						});
					}
				}
			}
		});
	};

	return Model;
});