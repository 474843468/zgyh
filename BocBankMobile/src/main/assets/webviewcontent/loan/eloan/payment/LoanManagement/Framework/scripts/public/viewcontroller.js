define('viewcontroller',['zepto', 'common', 'model', 'bocejs', 'routesUtil', 'checkForm'], function($, common, model, ejs, routesUtil, checkForm){
    return $.extendCls({
        ejs: "",
        css: '',
        services: "",
        html: "",
        templateString: '',
        _constructor: function(options){
            this.el = '';
            this.domNode = null;
            this.eloper = 'append';
            this.datastore = {};
            this.id = "";
            this._interv= {};
            $.extend(this, options);
            if(this.data){
                this.datastore['forienArgus'] = this.data;
            }
            if(!this.id){
                throw new Error("未指定视图id");
            }
            this.initDomNode();
            var _t = this;


            $.each(['appendTo', 'insertBefore', 'insertAfter'], function(idx, item){
                _t[item] = function(nd, node){
                    $(node)[item](nd);
                    _t.bindEvts();
                }
            });
            $.each(['append','before','after'], function(idx, item){
                _t[item] = function(nd, node){
                    $(nd)[item](node);
                    _t.bindEvts();
                }
            });


        },
        initDomNode: function(el){
            el && (this.el = el);
            if(!this.el){
                return false;
            }
            $(this.el)[this.eloper]("<div id=\"" + this.id + "\" class='customer-view " + (this.className || '') + "'></div>");
            this.domNode = $(this.el).find("#" + this.id);
        },
        setOper: function(oper){
            this.eloper = oper;
        },
        load: function(){
            var data = this.data || {}, _t = this;
            if($.isArray(data)){
                data = {data: data};
            }
            if(!this.domNode){
                throw new Error("未初始化根节点");
            }
            if(_t.services){
                for(var s=0; s<_t.services.length; s++){
                    _t.serviceQuery(_t.services[s]);
                }
                var iv = setInterval(function(){
                    var servicesReady = true;
                    for(var s=0;s<_t.services.length; s++){
                        if(!_t.datastore[_t.services[s]] || _t.datastore[_t.services[s]] == 'querying'){
                            servicesReady = false;
                            continue;
                        }
                        if(_t.datastore[_t.services[s]] == 'error'){
                            clearInterval(iv);
                            continue;
                        }
                    }
                    if(servicesReady){
                        clearInterval(iv);
//                        var data = {};
                        for(var d in _t.datastore){
                            $.extend(data,_t.datastore[d]);
                        }
                        _t.domNode.html(new ejs({url: _t.ejs}).render(data));
                        _t.formInView(_t.el);
                        _t.bindEvts();
                        _t.onLoadFinish(data);
                    }
                }, 50);
            }else{
                _t.ejs && _t.domNode.html(new ejs({url: _t.ejs}).render(data));
                _t.templateString && _t.domNode.html(new ejs({element: {
                    id: 'viewtemp' + parseInt(Math.random()*0xffff),
                    value: _t.templateString
                }}).render(data));
                _t.html && _t.domNode.html(_t.html);
                _t.formInView(_t.el);
                _t.bindEvts();
                _t.onLoadFinish(data);
            }
        },
        onLoadFinish: function(){
        		var _t = this, f = this;
        		if($(f).attr('onLoadFinish'))
        			_t[$(f).attr('onLoadFinish')]();
        },
        formInView: function(el){
            $(el).find('form').each(function(i, item){
               for(var i=0;i<item.elements.length; i++){
                   var p = item.elements[i];
                   if(p.tagName == 'BUTTON'){
                       continue;
                   }
                   if(!$(p).attr('name') && !$(p).attr('id')){
                       throw new Error('表单内元素未赋值name属性');
                   }
               };
            });
            var _t = this;
            $(el).find('form').on('submit', function(e){
                if(!checkForm.checkAll(_t.el)){
                    return false;
                }
                var vals = _t._getFormValues(this), f=this;
                var service = $(this).attr('service');
                if(service){
                    service = routesUtil.services[service];
                    model.interaction({
                        method: service.method,
                        params: $(f).attr('serviceFormParams') ?  _t[$(f).attr('serviceFormParams')]():vals
                    },service.url, function(data){
                        _t.datastore[service.method] = service.dataRestruct && service.dataRestruct(data) || data;
                        if($(f).attr('afterSubmit')){
                            _t[$(f).attr('afterSubmit')](data);
                        }
                    }, {errorFun: function(error){
                        if($(f).attr('onSubError')){
                            _t[$(f).attr('onSubError')](error);
                        }
                    }});
                }
            });
        },
        _getFormValues: function(form){
            var vals = {};
            for(var i=0;i<form.elements.length; i++){
                var p = form.elements[i];
                if(p.tagName == 'BUTTON'){
                    continue;
                }
                var key  = $(p).attr('id') || $(p).attr('name');
                var val = $(p).val();
                if(p.tagName == 'INPUT' && $(p).attr('widget_type') == 'password'){
                    var ps = $(p).boc_password();
                    $.extend(vals, ps);
                    continue;
                }
                if(p.disabled){
                    continue;
                }
                if(p.tagName == 'INPUT' && p.type == 'radio'){
                    if(p.checked){
                        vals[key] = val;
                    }
                    continue;
                }
                vals[key] = val;
            }
            var data = $(form).attr('data');
            data = data && data.toObject() || {};
            return $.extend(vals, data);
        },
        bindEvts: function(){
            var _t = this;
            $(_t.domNode).find("[def_event]").each(function(i, item){
                var evts = $(item).attr('def_event').toObject();
                for(var e in evts){
                    $(item).unbind(e).on(e, $.proxy(_t[evts[e]], _t));
                }
            });
        },
        serviceQuery: function(service){
            var biiServices = routesUtil.services, _t=this,data = this.data || {};
            for(var f=0; service.depends && f<service.depends.length; f++){
                var s = service.depends[f];
                if(!_t.datastore[s]){
                    _t.serviceQuery(s);
                }else if(_t.datastore[s] != "querying"){
                }
            }
            _t._interv[service] = setInterval(function(){
                var dataComplete = true;
                for(var s=0; s<biiServices[service].depends.length; s++){
                    s = biiServices[service].depends[s];
                    if(!_t.datastore[s] || _t.datastore[s] == 'querying'){
                        dataComplete = false;
                    }else if(_t.datastore[s] == 'error'){
                    	dataComplete = false;
                    	clearInterval(_t._interv[service]);
                    }
                }
                if(dataComplete){
                    clearInterval(_t._interv[service]);
                    if(_t.datastore[service] == "querying"){
                        return;
                    }
                    _t.datastore[service] = 'querying';
                    var params = data || {}, val=_t.datastore;
                    for(var f in biiServices[service].params){
                        var v = biiServices[service].params[f];
                        if(v){
                        	v = v + "";
                            v = v.split('.');
                            for(var i=0; i< v.length; i++){
                                val = val[v[i]];
                            }
                            params[f] = val;
                            val=_t.datastore;
                        }
                    }
                    model.interaction({
                        method: biiServices[service].method,
                        params: params
                    },biiServices[service].url, function(data){
                        _t.datastore[service] = biiServices[service].dataRestruct && biiServices[service].dataRestruct(data) || data;
                    }, {errorFun: function(err){
                        _t.datastore[service] = 'error';
                        var errorsFilterMap={
                            "validation.session_invalid": '',
                            "validation.resubmit_same_session": '',
                            "role.invalid_user": '',
                            "conversationMap.null": ''
                        }
                        if(err.code in errorsFilterMap){
                            common.params.targetRouter.routerKey = common.currentRouterKey;
                        }
                    }, errorsFilter: true});
                }
            }, 50);
        },
        getViewData: function(){
            var data = {}, _t = this;
            $(this.el).find('form').each(function(i, item){
                $.extend(data, _t._getFormValues(item));
            })
            return data;
        },
        //通过点击事件,获取拥有cls这一class的最近的父元素
        //如果事件所在的当前元素便拥有该class,则直接返回
        getEvtObj: function(e, cls){
            var obj = $(e.target || e.srcElement);
            while(obj.length > 0){
                if(obj.hasClass(cls)){
                    return obj;
                }
                obj = obj.parent();
            }
            return null;
        },
        destroy: function(){
            $(this.domNode).remove();
            this.datastore = null;
            for(var i in this._interv){
                clearInterval(this._interv[i]);
            }
            this._interv = null;
        }
    });
});