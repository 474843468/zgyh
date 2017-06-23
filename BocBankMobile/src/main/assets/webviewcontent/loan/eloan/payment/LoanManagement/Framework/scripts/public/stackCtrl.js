define('stackCtrl', ['zepto', 'common'], function($, common){
    var stackCtrl = function(el){
        this.el = el;
        this.currentPane = null;
        $(el).children().hide().first().show();
        this.onshows = [];
    }
    stackCtrl.prototype = {
        add: function(pane, opt){
            $(this.currentPane).hide();
            var idx = null;
            if(opt && opt.onshow){
                var idx = this.onshows.length;
                this.onshows.push(opt.onshow);
            }
            this.currentPane = $(pane).appendTo($(this.el)).addClass('stackpane fade-in');
            idx != undefined && idx != null && this.currentPane.attr('onshow_idx', idx);
            var content = {
                mtype: common.historyManager.types.stackview,
                page: ++common.historyManager.page
            };
            window.history.pushState(content, "", "#" + common.historyManager.page);
//            this.currentPane = pane;
            return this;
        },
        setAsPane: function(node){
            this.currentPane = $(node);
            var content = {
                mtype: common.historyManager.types.stackview,
                page: ++common.historyManager.page
            };
            window.history.pushState(content, "", "#" + common.historyManager.page);
        },
        next: function(){
            $(this.currentPane).hide().next().show();
            this.currentPane = $(this.currentPane).next();
            this.trigOnShow();
            window.history.pushState('', "", "#" + ++common.historyManager.page);
            return this;
        },
        prev: function(){
            var obj = $(this.currentPane);
            if(obj.prev().length > 0){
                obj.prev().show();
                this.currentPane = obj.prev();
                obj.remove();
                this.trigOnShow();
            }
            return this;
        },
        trigOnShow: function(){
            var idx = this.currentPane.attr('onshow_idx');
            if(idx != null && idx != undefined){
                this.onshows[idx]();
            }
        },
        getFormVals: function(){
            var vals = {}
            $(this.currentPane).find("input, textarea, select").forEach(function(item, i){
                var type = $(item).attr('type'), widget_type = $(item).attr('widget_type');
                if(type && (type == 'radio' || type == 'checkbox')){
                    if(type == 'radio' && $(item).attr('checked')){
                        vals[$(item).attr('name')] = $(item).val();
                    }else if(type == 'checkbox' && $(item).attr('checked')){
                        if(!vals[$(item).attr('name')]){
                            vals[$(item).attr('name')] = [];
                        }
                        vals[$(item).attr('name')][vals[$(item).attr('name')].length] = $(item).val();
                    }
                }else if(widget_type){
                    $.extend(vals, $(item).boc_password());
                }else{
                    vals[$(item).attr('name')] = $(item).val();
                }

            });
            return vals;
        },
        findPane: function(selector){
            return $(this.el).find(selector);
        }
    }
    $.fn.stackCtrl = function(){
        return new stackCtrl(this);
    }
    return stackCtrl;
});