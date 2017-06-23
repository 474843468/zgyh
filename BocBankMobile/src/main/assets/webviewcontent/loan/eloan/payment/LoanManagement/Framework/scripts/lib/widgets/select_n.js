/**
 * 自定义下拉框小部件
 * @namespace Widgets
 * @name SelectWidget
 * @class
 * @requires zepto
 */
define("SelectWidget", ["zepto"], function($){
    $._widget_renders = $._widget_renders || {};
    var SelectWidgetRender = function(wdgtpl){
        var options = [], optarr = wdgtpl.match(/<option[^>]*>[^<>]*<\/option>/g);
        $.each(optarr, function(i, opt){
            options.push({
                val: (opt.match(/value=('|")[^<>'"]*('|")/) ? opt.match(/value=('|")[^<>'"]*('|")/)[0] : "").replace(/value=('|")/, "").replace(/'|"/, ""),
                txt: opt.replace(/<[^<>]*>/g, "")
            });
        });
        var args = $.analyzeWidgetProperties(wdgtpl);
        var html = new EJS({url:"views/wgt_tmpl/select_n.ejs"}).render({
            classes: args.classes,
            style: args.style,
            id: args.id,
            name: args.name,
            disabled: args.disabled,
            required: args.required,
            validator: args.validator,
            size: args.size,
            options: options
        }, true);
        return html;
    }
    $._widget_renders["SelectWidget"] = SelectWidgetRender;
    
//    $(document).on("click", function(e){
//        $(".select_widget .popup_widget, .select_widget .selectoverlay").hide();
//    });
    $.BocSelect = {
        showSelectOptions:  function(e){
            $(".select_options_container, .selectoverlay").hide();
            $(e.target).parents(".select_widget").children(".select_options_container, .selectoverlay").show();
            e.stopPropagation();
        },
        selectOption: function(e){
            var p = $(e.target).parent().parent().parent(),
                val = $(e.target).siblings(".value").attr("value"),
                txt = $(e.target).siblings(".value").text(),
                oval = p.find("input").val();

//            input.val(valuenode.text().replace(/\s/g, ''));

            $(".select_widget .popup_widget, .select_widget .selectoverlay").hide();
            p.find(".selected_text").find("span").text(txt);
            p.find(".selected_text").find("input").val(val).attr("label", txt);
            var id = p.attr("id");
            $.BocSelect.changeEvts[id] && $.BocSelect.changeEvts[id].apply(null, [val, txt, oval]);
            $(".selectoverlay").hide();
            e.stopPropagation();
        },
        changeEvts: []
    }
    /**
     * 绑定下拉菜单的change事件
     * @memberof SelectWidget
     * @name bindSelectChange
     * @param (Function) callback - 事件响应函数
     * @function
     * @returns (Object) SelectWidget
     * @example
     * $("#myselect").bindSelectChange(function(val, txt, oldval){
     *     //val  --> 新选项的值
     *     //txt  --> 新选项的文字
     *     //oldval --> 旧选项的值
     * });
     */
    $.fn.bindSelectChange = function(callback){
        var id = $(this).attr("id");
        if(!id){
            throw new Error("添加onchange事件的select必须带有ID");
        }
        $.BocSelect.changeEvts[id] = callback;
        return this;
    }
    /**
     * 下拉菜单添加选项
     * @memberof SelectWidget
     * @name addOptions
     * @param (Object) Function - 选项数组
     * @function
     * @returns (Object) SelectWidget
     * @example
     * $("#myselect").addOptions([
     *     {val: "1", txt: "工资卡1"},
     *     {val: "2", txt: "工资卡2"},
     *     {val: "3", txt: "工资卡3"}
     * ]);
     */
    $.fn.addOptions = function(options){
        var s = "";
        for(var i=0; i<options.length; i++){
            if(options[i].selected){
                $(this).find(".selected_text span").html(options[i].txt);
                $(this).find(".selected_text input").val(options[i].val).attr(options[i].txt);
            }
            s += '<li class="select_table-view-cell selec_widget_opt" value="' + options[i].val + '" onclick="$.BocSelect.selectOption(event)">' + options[i].txt + '</li>';
        }
        $(this).find("ul").append(s);
        return this;
    }
    /**
     * 清空下拉菜单原有选项
     * @memberof SelectWidget
     * @name clearSelect
     * @function
     * @returns (Object) SelectWidget
     */
    $.fn.clearSelect = function(){
        $(this).find(".selected_text span").html("");
        $(this).find("ul").html("");
        return this;
    }
    /**
     * 获取或设置下拉框当前选项的值
     * @memberof SelectWidget
     * @name addOptions
     * @param (String) val - 选项值
     * @function
     * @returns (String/Object) SelectWidget.value/SelectWidget
     * @example
     * $("#myselect").selectOpt(); //returns "1";
     * $("#myselect").selectOpt("1"); //下拉框被选中了值为1的选项，并返回下拉框本身
     */
    $.fn.selectOpt = function(val){
        var sel = $(this);
        if(!val){
            return sel.find("input").val();
        }
        sel.find("input").val(val);
        sel.find("li").each(function(i, opt){
           if($(opt).attr("value") == val){
               sel.find(".selected_text span").text($(opt).text());
               sel.find("input").attr("label", $(opt).text());
           }
        });
        return sel;
    }

    /**
     * 监听插入事件，支持插入一段EJS字符串时，自动绑定其中的日历控件
     */
    function aophandle(el){
        if(!$.isTmplContainsCtrl(el, 'SelectWidget')){
            return el;
        }
        $(el).find(".select_widget").each(function(i, item){
            if($(item).attr("binded")){
                return el;
            }
            initSelectWidget(item);
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

    function initSelectWidget(widget){
        widget = $(widget);
        var h, h1=36, precount;
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
                old_dis = 1*(t.attr("old_dis")) || 0,
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
            var ctop = widget.find(".options_content_container").offset().top;
            $(tab).children().removeClass("selected").each(function(i, item){
                if(Math.abs($(item).offset().top - ctop  - precount * h1) <= 10){
                    $(item).addClass("selected");
                    var cpn = $(item).parent().parent()[0], txt = $(item).text(), val = $(item).attr("value");
                    widget.find(".value").text(txt);
                    widget.find(".value").attr("value", val);
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
            var defaultvalue = "";
            widget.find("li").each(function(i, item){
                defaultvalue = i == 0 ? $(item).text() : ($(item).attr("selected") ? $(item).text() : defaultvalue);
            });
            //如果点击时，输入框中无值
            _movetoSelectedItem(".select_table-view-cell", defaultvalue);
        }
        widget.find(".cancelbtn").on("touchstart click", function(e){
            widget.find(".popup_widget, .selectoverlay").hide();
        });
        widget.find(".choosedatebtn").on("touchstart click", function(e){
            $.BocSelect.selectOption(e);
//            var t=$(e.target), input = t.parent().parent().parent().find("input"),
//                valuenode = t.siblings("span");
//            input.val(valuenode.text().replace(/\s/g, ''));
//            $(".select_widget .popup_widget, .select_widget .selectoverlay").hide();
        });
        widget.find(".selected_text, .select_wgt_icon").on("touchstart click", function(e){
            e.stopPropagation();
            $.BocSelect.showSelectOptions(e);
            h = widget.find(".options_content_container").height(), precount = Math.ceil(Math.round(h/h1)/2);
            setSelectedValue(e);
            widget.find(".options_content_container .select_table-view").each(function(i, item){setSelectedStyle(item)});
        });
    }
});
