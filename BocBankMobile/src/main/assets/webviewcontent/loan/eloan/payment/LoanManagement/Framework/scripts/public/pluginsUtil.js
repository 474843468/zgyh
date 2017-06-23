/**
 * Created by xianwei on 2015/12/5.
 */
define('pluginsUtil', ['zepto', 'dateUtil'], function($, DateUtil) {

    var pluginsUtil = {};

    /**
    * 默认cfca参数
    * @type {{cipher: boolean, cipherType: number, randomeKey_S: string, outputValueType: string, passwordRegularExpression: string, passwordMinLength: string, passwordMaxLength: string}}
    */
    var defaultCFCAConfig = {
        cipher:true,
        cipherType:1,
        randomeKey_S:'',
        outputValueType:'2',
        passwordRegularExpression:'',
        passwordMinLength:"1",
        passwordMaxLength:"6"
    };

    /**
    *  默认日期选择参数
    * @type {{type: number, select: {options: *[]}, picker: {single: {min: string, max: string}, scope: {min: string, max: string, mindef: string, maxdef: string}}, format: string}}
    */
    var selectDateParams = {
        // 0 : 选择单个日期, 1 选择时间范围有自定义选择 2 选择时间范围无自定义选择
        type:0,
        select:{
            options:[]
        },
        picker: {
            single: {
                min: '',
                max: '',
                def: ''
            },
            scope: {
                min: '',
                max: '',
                mindef: '',
                maxdef: '',
                check:{
                    interval:"3",
                    unit:'M',
                    msg:window.app.nls.ovs_gy_datebetweenthreemonth
                }
            },
            format: 'yyyy/MM/dd'
        }
    };

    /**
     * 生成默认options选项
     */
    function createDefaultOptions(currentDate, format, count){

        var date = null;

        if (format != null) {
            data = DateUtil.parse(currentDate, format);
        } else {
            format = "yyyy/MM/dd";
            data =  DateUtil.parse(currentDate, 'yyyy/MM/dd');
        }
        var tmp = DateUtil.addDays(data, count);
        var countDate =  DateUtil.addDays(data, 1);
        var week = DateUtil.addDays(countDate, -7);
        var oneMonth = DateUtil.addMonths(countDate, -1);
        var threeMonth = DateUtil.addMonths(countDate, -3);


        var currentDateString = DateUtil.format(data, format);
        var tmpString = DateUtil.format(tmp, format);
        var weekString = DateUtil.format(week, format);
        var oneMonthString = DateUtil.format(oneMonth, format);
        var threeMonthString = DateUtil.format(threeMonth, format);

        var options = [

            {name:window.app.nls.ovs_ma_td_today, value:currentDateString + ',' + currentDateString},
            {name:window.app.nls.ovs_ma_td_nearlyaweek, value:weekString + ','+ tmpString},
            {name:window.app.nls.ovs_ma_td_nearlyamonth, value:oneMonthString + ','+ tmpString},
            {name:window.app.nls.ovs_ma_td_closetomarch, value:threeMonthString + ','+ tmpString}
        ];
        return options;
    }

    /**
     * {cipher:false, randomeKey_S:'222111''}
     * @param config
     */
    pluginsUtil.createCFCAConfig = function(params){
        var result = {};
        $.extend(result, defaultCFCAConfig);
        $.extend(result, params);
        return result;
    };

    /**
     * 生成单个日期选择参数
     * {min:'2015/12/12',max:'2016/1/12'，def:''} format = 'yyy/MM/dd'
     */
    pluginsUtil.createSigleDateSelectParams = function(params, format){
        var result = {};
        $.extend(result, selectDateParams);
        result.type = 0;
        $.extend(result.picker.single, params);
        if (format != null && format != "") {
            result.picker.format = format;
        }
        return result;
    };

    /**
     * 选择日期返回，有自定义  scope:{
     *           min:'',
     *           max:'',
     *           mindef:'',
     *           maxdef:'',
     *       }
     * @param params
     * @returns {{}}
     */
    pluginsUtil.createLimitDataSelectParams = function(currentDate, params, format){
        var result = {};
        $.extend(result, selectDateParams);
        result.type = 1;
        var options = createDefaultOptions(currentDate, format, 0);
        result.select.options = options;
        if (format) {
            result.picker.format = format;
        }
        $.extend(result.picker.scope, params);
        return result;
    }

    /**
     * 选择日期返回，有自定义  scope:{
     *           min:'',
     *           max:'',
     *           mindef:'',
     *           maxdef:'',
     *       }
     * @param params
     * @returns {{}}
     */
    pluginsUtil.createBeforeTodayLimitDataSelectParams = function(currentDate, params, format){
        var result = {};
        $.extend(result, selectDateParams);
        result.type = 1;
        var options = createDefaultOptions(currentDate, format, -1);
        result.select.options = options;
        $.extend(result.picker.scope, params);
        return result;
    }

    return pluginsUtil;
});