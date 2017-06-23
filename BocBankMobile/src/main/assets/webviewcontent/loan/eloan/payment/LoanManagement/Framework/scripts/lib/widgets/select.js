/**
 * 自定义下拉框小部件
 * @namespace Widgets
 * @name SelectWidget
 * @class
 * @requires zepto
 */
define("SelectWidget", ["zepto"], function($){
    $._widget_renders = $._widget_renders || [];
    var SelectWidgetRender = function(wdgtpl){
        var options = [], optarr = wdgtpl.match(/<option[^>]*>[^<>]*<\/option>/g);
        $.each(optarr, function(i, opt){
            options.push({
                val: (opt.match(/value=('|")[^<>'"]*('|")/) ? opt.match(/value=('|")[^<>'"]*('|")/)[0] : "").replace(/value=('|")/, "").replace(/'|"/, ""),
                txt: opt.replace(/<[^<>]*>/g, "")
            });
        });
        var args = $.analyzeWidgetProperties(wdgtpl);
        var html = new EJS({url:"views/wgt_tmpl/select.ejs"}).render({
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
    
    $(document).on("click", function(e){
        $(".select_widget .popup_widget, .select_widget .selectoverlay").hide();
    });
    $.BocSelect = {
        showSelectOptions:  function(e){
            $(".select_options_container, .selectoverlay").hide();
            if($.isMinorScreen()){
                $(e.target).parents(".select_widget").children(".select_options_container, .selectoverlay").show();
            }else{
                $(e.target).parents(".select_widget").children(".select_options_container").show();
            }
            e.stopPropagation();
        },
        selectOption: function(e){
            var p = $(e.target).parent().parent(), 
                val = $(e.target).attr("value"),
                txt = $(e.target).text(),
                oval = p.siblings("input").val();
            p.siblings(".selected_text").find("span").text(txt);
            p.siblings(".selected_text").find("input").val(val).attr("label", txt);
            var id = p.parent().attr("id");
            $.BocSelect.changeEvts[id] && $.BocSelect.changeEvts[id].apply(null, [val, txt, oval]);
            p.hide();
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
           if($(opt).val() == val){
               sel.find("span").text($(opt).text());
           }
        });
        return sel;
    }
});
