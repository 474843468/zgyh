/**
 * 日历控件，支持年月日和年月模式 date_mode="year-month"
 * @namespace Widgets
 * @name SelectWidget
 * @class
 * @requires zepto
 */
define("DatepickerWidget", ["zepto"], function($){
    $._widget_renders = $._widget_renders || {};
    var render = function(wdgtpl){
        var args = $.analyzeWidgetProperties(wdgtpl);
        var html = new EJS({url:"views/wgt_tmpl/datepicker.ejs"}).render({
            classes: args.classes,
            style: args.style,
            id: args.id,
            name: args.name,
            disabled: args.disabled,
            required: args.required,
            validator: args.validator,
            regExp: args.regExp,
            value: args.value,
            minyear: args.minyear || 1900,
            maxyear: args.maxyear || 2030,
            date_mode: args.date_mode
        }, true);
        return html;
    }
    $._widget_renders["DatepickerWidget"] = render;
    /**
     * 监听插入事件，支持插入一段EJS字符串时，自动绑定其中的日历控件
     */
    function aophandle(el){
        if(!$.isTmplContainsCtrl(el, 'DatepickerWidget')){
            return el;
        }
        $(el).find(".datepicker_widget").each(function(i, item){
            if($(item).attr("binded")){
                return el;
            }
            initDatePickerWidget(item);
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
        append: {
            after : function(el) {
                return aophandle(el);
            }
        }
    }, true);

    function initDatePickerWidget(widget){
        widget = $(widget);
        if(widget.attr("date_mode") == "year-month"){
            widget.find(".datepicker_sel_container").addClass("datepicker_yearmonth_container");
            widget.find(".selected_date_area .selected_date, .selected_date_area .date_separator").empty();
        }
        var h, h1=32, precount;
        widget.find(".select_table-view").on("touchstart mousedown", function(e){
            var touch= e.touches && e.touches[0] || $(e.target), p = {
                x: touch.pageX || e.pageX,
                y: touch.pageY || e.pageY
            }, t=$(e.target).parent();
            t.attr("scrolling", 'true');
            t.attr("x", p.x).attr("y", p.y);
            e.stopPropagation();
        }).on('touchmove mousemove', function(e){
            e.preventDefault();
            var touch= e.touches && e.touches[0] || $(e.target) , p = {
                x: touch.pageX || e.pageX,
                y: touch.pageY || e.pageY
            }, t = $(e.target).parent();
            if(t.attr("scrolling") !== 'true'){return false;}
            var x_dis = Math.abs(p.x - t.attr("x")),
                y_dis = Math.abs(p.y - t.attr("y")),
                direct = (p.y - t.attr("y")) > 0 ? 1 : -1,
                old_dis = 1*(t.attr("old_dis")),
                new_dis = old_dis + Math.ceil(y_dis/h1)*h1*direct;
            new_dis = new_dis > 0 ? (new_dis<precount*h1?new_dis:precount*h1) : ((Math.abs(new_dis) + h) > (t.height()+(Math.ceil(h/h1)-precount-1)*h1) ? (h - (t.height()+(Math.ceil(h/h1)-precount-1)*h1)) : new_dis);
            new_dis = Math.ceil(new_dis/h1)*h1; //矫正
            if(y_dis > x_dis && y_dis>20){ //纵向滑动
                t.attr("x", p.x).attr("y", p.y);
                t.attr("old_dis",new_dis);
                t.css("-webkit-transform", "translateY(" + new_dis + "px)");
                e.stopPropagation();
                setSelectedStyle(t);
            }
        }).on("touchend mouseup", function(e){
            e.preventDefault();
            var t = $(e.target).parent();
            t.attr("scrolling", "false");
        }).on('wheel', function(e){
            var y_dis = h1,direct = e.wheelDeltaY > 0 ? 1 : -1,t = $(e.currentTarget),old_dis = 1*(t.attr("old_dis")),
                new_dis = old_dis + Math.ceil(y_dis/h1)*h1*direct;
            new_dis = new_dis > 0 ? (new_dis<precount*h1?new_dis:precount*h1) : ((Math.abs(new_dis) + h) > (t.height()+(Math.round(h/h1)-precount-1)*h1) ? (h - (t.height()+(Math.round(h/h1)-precount-1)*h1)) : new_dis);
            t.attr("old_dis",new_dis);
            t.css("-webkit-transform", "translateY(" + new_dis + "px)");
            e.stopPropagation();
            setSelectedStyle(t);
        });
        function setSelectedStyle(tab){
            var ctop = widget.find(".datepicker_sel_container").offset().top;
            $(tab).children().removeClass("selected").each(function(i, item){
                if(Math.abs($(item).offset().top - ctop  - precount * h1) <= 10){
                    $(item).addClass("selected");
                    var cpn = $(item).parent().parent()[0], txt = $(item).text();
                    if(cpn.className.indexOf("year_container") != -1){
                        widget.find(".selected_year").text(txt);
                    }else if(cpn.className.indexOf("month_container") != -1){
                        widget.find(".selected_month").text(txt);
                    }else{
                        widget.find(".selected_date").text(txt);
                    }
                }
            });
        }
        function _movetoSelectedItem(selector, val){
            widget.find(selector).each(function(i, item){
                if($(item).text() == val){
                    $(item).parent().attr("old_dis",(precount - i)*h1);
                    $(item).parent().css("-webkit-transform", "translateY(" + (precount - i)*h1 + "px)");
                }
            });
        }
        function setSelectedValue(e){
            var defaultvalue = $(e.target).val();
            //如果点击时，输入框中无值
            if(!defaultvalue){
                //控件有指定初始值用初始日期，未指定默认为今天
                defaultvalue = $(e.target).parent().attr("default") || new Date();
            }
            typeof defaultvalue == "string" ? (defaultvalue = new Date(defaultvalue)) : "";
            _movetoSelectedItem(".year_container .select_table-view-cell", defaultvalue.getFullYear());
            _movetoSelectedItem(".month_container .select_table-view-cell", defaultvalue.getMonth()+1);
            _movetoSelectedItem(".date_container .select_table-view-cell", defaultvalue.getDate());
        }
        widget.find(".choosedatebtn").on("touchstart click", function(e){
            var t=$(e.target), input = t.parent().parent().parent().find("input"),
                valuenode = t.siblings("span");
            input.val(valuenode.text().replace(/\s/g, ''));
            $(".datepicker_widget .popup_widget, .datepicker_widget .selectoverlay").hide();
        });
        widget.find("input").on("touchstart click", function(e){
            e.stopPropagation();
            widget.find(".selectoverlay, .date_picker_container").show();
            h = widget.find(".datepicker_sel_container").height(), precount = Math.ceil(Math.round(h/h1)/2)-1;
            setSelectedValue(e);
            widget.find(".datepicker_sel_container .select_table-view").each(function(i, item){setSelectedStyle(item)});
        });
    }

    $.extend($.fn, {
        bindAsDatePicker: function(params){
            var t = $(this), p= t.parent();
            params = $.extend($.analyzeWidgetProperties(t[0].outerHTML), params);
            var html = new EJS({url:"views/wgt_tmpl/datepicker.ejs"}).render({
                classes: params.classes || '',
                style: params.style || '',
                id: params.id || '',
                name: params.name || '',
                disabled: params.disabled || '',
                required: params.required || false,
                validator: params.validator || '',
                regExp: params.regExp || '',
                value: params.value || '',
                minyear: params.minyear || 1900,
                maxyear: params.maxyear || 2030,
                date_mode: params.date_mode
            }, true);
            var newt = $(html).insertBefore(t);
            t.remove();
            initDatePickerWidget(newt);
            return newt;
        }
    })
});