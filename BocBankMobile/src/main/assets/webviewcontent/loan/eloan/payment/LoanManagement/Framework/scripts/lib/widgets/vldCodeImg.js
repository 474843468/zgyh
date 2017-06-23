/**
 * 图形验证码控件
 * @namespace Widgets
 * @name VldCodeImgWidget
 * @class
 * @requires zepto
 */
define("VldCodeImgWidget", ["zepto"], function($){
    $._widget_renders = $._widget_renders || {};
    var render = function(wdgtpl){
        var args = $.analyzeWidgetProperties(wdgtpl);
        var html = new EJS({url:"views/wgt_tmpl/vldcodeimg.ejs"}).render({
            classes: args.classes,
            style: args.style,
            id: args.id,
            name: args.name,
            disabled: args.disabled,
            required: args.required,
            validator: args.validator,
            vldurl: args.vldurl
        }, true);
        return html;
    }
    $._widget_renders["VldCodeImgWidget"] = render;

    /**
     * 监听插入事件，支持插入一段EJS字符串时，自动绑定其中的日历控件
     */
    function aophandle(el){
        if(!$.isTmplContainsCtrl(el, 'VldCodeImgWidget')){
            return el;
        }
        $(el).find(".vldcodeimg_widget").each(function(i, item){
            if($(item).attr("binded")){
                return el;
            }
            $(item).find('.refresh').on('click', $.proxy(refresh, item));
            $(item).attr("binded", "true");
        });
        return el;
    }
    $.funAop($.fn, {
        "html" : {
            after : function(el) {
                return aophandle(el);
            }
        },
        "append": {
            after : function(el) {
                return aophandle(el);
            }
        }
    }, true);

    $.fn = $.extend($.fn,{
        refreshVldCode: refresh,
        setDisabled: function(flag){
            flag && $(this).find('input').attr('disabled', true) || $(this).find('input').removeAttr("disabled");
        }
    });

    function refresh(){
        $(this).find('input').val('');
        $(this).find('img').attr('src', $(this).attr('vldurl').replace(/{random}/, Math.random()*0x7777777));
    }
});