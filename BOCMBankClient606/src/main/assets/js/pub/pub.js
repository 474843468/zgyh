window.addEventListener("load",pub_init,true);

var PUB_METHOD_Object = new Object();

function clear_input() {
	$("input[type=\"text\"]").val("");
}

function clear_result() {
	$("span[name=\"result\"]").text("");
	
}

function pub_init() {
	setExplain();
	setMenu();
}

function createUnitElement(value) {
	return createElement("font", ["size","color","style"], ["2px","#a1a3a6","font-weight:normal"], " " + value);
}

function select_menu() {
	clear_result();
	clear_input();
	var type = getCurPageListName()[0];
	if(type == 1) {
		// 清空计算结果
		$("#dehk_table-column-body").empty();
	}
	var subMenuValues = menu_values[type][1];
	var cur_id = $("#select_menu").val();
	for(var i=0;i<subMenuValues.length;i++) {
		if(subMenuValues[i] != cur_id) {
			$("#"+subMenuValues[i]).hide();
		}
	}
	$("#" + cur_id).show();
	var fn = $("#" + cur_id).attr("fn");
	return PUB_METHOD_Object[fn]();
}

/**
 * 设置免责说明
 * 
 * @return
 */
function setExplain() {
// var title = $("#explain_title");
	var content = $("#explain_content");
// title.empty();
	content.empty();
// title.text(pub_explain[0]);
	for(var i=1;i<pub_explain.length;i++) {
		var f_p = createElement("p", null, null, null);
		var font = createElement("font", ["color","size","style"], ["#a0a0a0","2px","font-weight:normal"], pub_explain[i]);
		f_p.append(font);
		content.append(f_p);
	}
}

/**
 * 设置panel菜单
 * 
 * @param menuName
 * @param menuValue
 * @param subNames
 * @param subValues
 * @return
 */
function setMenu() {
	var type = getCurPageListName()[0];
	var menu = $("#select_menu");
		var menusName;
		var subValues;
		var subNames;
		switch(type) {
		case 0 : 
			menusName = menu_names[0];
			subValues = menu_values[0][1];
			subNames = menu_values[0][0];
			break;
		case 1 : 
			menusName = menu_names[1];
			subValues = menu_values[1][1];
			subNames = menu_values[1][0];
			break;
		case 2 :
			menusName = menu_names[1];
			subValues = menu_values[2][1];
			subNames = menu_values[2][0];
			break;
		case 3 :
			//去除外汇计算器菜单 直接初始化
			PUB_METHOD_Object["fn_wbdh_init"]();
//			menusName = menu_names[1];
//			subValues = menu_values[3][1];
//			subNames = menu_values[3][0];
			return;
		default :
			menusName = menu_names[0];
			subValues = menu_values[0][1];
			subNames = menu_values[0][0];
			break;
		}
		
		for(var i=0;i<subNames.length;i++){
			var a = createElement("option", ["value"],[subValues[i]], subNames[i]);
			menu.append(a);
		}
		
		menu.selectmenu("refresh");
		menu.get(0).selectedIndex = 0;
		menu.change(select_menu);
		select_menu();
}

/**
 * 创建html元素
 * 
 * @param elementName
 *            元素名称
 * @param attrNames
 *            属性名称
 * @param attrValues
 *            属性值
 * @param value
 * @return
 */
function createElement(elementName, attrNames, attrValues, value) {
	var e = $("<" + elementName + "/>");
	if(attrNames != null && attrValues != null) {
		for(var i=0; i<attrNames.length; i++) {
			e.attr(attrNames[i], attrValues[i]);
		}
	}
	if(null != value && "" != value) {
		e.text(value);
	}
	return e;
}

/**
 * 四舍五入,保留2位小数
 * 
 * @param i
 * @param digit
 * @return
 */
function Round(i, digit) {
	var p = 1;
	if(!digit) {
		p = 100;
	} else {
		if(digit != 0) {
			p = Math.pow(10,digit);
		}
	}
	return Math.round(i * p) / p;
}
/**
 * 判断是否为数字
 * 
 * @param value
 * @return
 */
function isNumber(value) {
	var patrn=/^-?\d+\.{0,}\d{0,}$/;
	if(parseInt(value, 10) < 0) {
		return false;
	}
	
	return patrn.test(value);
}

/**
 * 是否为yyyy/mm/dd日期
 * 
 * @param value
 * @return
 */
function isData(value) {
	value = value.replace(/-/g, "/");
	var patrn= /[0-9]{4}\/[0-9]{2}\/[0-9]{2}/;
	return patrn.test(value);
}
/**
 * 是否为yyyy/mm日期
 * 
 * @param value
 * @return
 */
function isMonth(value) {
	value = value.replace(/-/g, "/");
	var patrn= /[0-9]{4}\/[0-9]{2}/;
	return patrn.test(value);
}

/**
 * 动态添加select选项
 * 
 * @param select_id
 * @param optionNames
 * @param optionValues
 * @return
 */
function addItemToSelect(select_id, optionNames, optionValues) {
	var select = $("#" + select_id);
	select.empty();
	if(optionNames != null && optionValues != null) {
		for(var i=0; i<optionNames.length; i++) {
			var option = new Option(optionNames[i], optionValues[i]);
			select.append(option);
		}
	}
	select.get(0).selectedIndex = 0;
	select.selectmenu("refresh");
}

/**
 * 跳转页面
 * 
 * @param e
 * @return
 */
function refresh_window(e) {
	window.location.assign($(e).attr("href"));
	return false;
}

/**
 * 获取当期yyyy/mm/dd格式时间
 * 
 * @return
 */
function getDate(quantity) {
	if(!quantity) 
		quantity = 0;
	var date = new Date();
	var dateStr = (parseInt(date.getFullYear(), 10) + quantity) + "-" + formatDate(date.getMonth() + 1) + "-" + formatDate(date.getDate());
	return dateStr;
}
/**
 * 格式化日期为yyyy/mm/dd格式
 * 
 * @param str
 * @return
 */
function formatDate(str) {
	var temp = new String(str);
	if(temp.length >= 2) {
		return str;
	} else { 
		return "0" + str;
	}
}

function startBeforeEnd(startDate, endDate) {
	if((endDate.replace(/-/g,"") - startDate.replace(/-/g,"")) < 0) {
		alert("提取日期不能早于存款日期，请重新选择");
		return true;
	}
	return false;
}

/**
 * 校验参数合法性
 * 
 * @param datas
 * @return
 */
function checkData(datas) {
	if(datas != null) {
		for(var i=0; i<datas.length; i++) {
			var name = datas[i][0];
			var value = datas[i][1];
			var type = datas[i][2];
			if(!value) {
				alert("请输入" + name + "!");
				return true;
			}
			switch(type) {
			case "number" : 
				if(!isNumber(value)) {
					alert(name + "格式不正确，请输入正数数字");
					return true;
				}
				break;
			case "date" :
				if(!isData(value)) {
					alert(name + "格式不正确，日期格式为yyyy(年)/mm(月)/dd(日)");
					return true;
				}
				break;
			case "month" :
				if(!isMonth(value)) {
					alert(name + "格式不正确，日期格式为yyyy(年)/mm(月)");
					return true;
				}
				break;
			default :
				return false;
			}
		}
	}
}

/**
 * 根据日期间隔时间获取利率
 * 
 * @param startDate
 * @param endDate
 * @param type
 * @return
 */
function getDateRate(year, type) {
	switch(type) {
	case 1 : // 定活两便
		return getListRate(year) * 0.6;
	case 2 : // 人民币贷款
		return getListRate(year, 2);
	}
}

/**
 * 将间隔时间单位转换为年
 * 
 * @param startDate
 * @param endDate
 * @return
 */
function getPeriod(startDate, endDate) {
	
	var sDStr = startDate.split("-");
	var edStr = endDate.split("-");
	
	var sD = new Date();
	var eD = new Date();
	
	sD.setFullYear(sDStr[0], sDStr[1], sDStr[2]);
	eD.setFullYear(edStr[0], edStr[1], edStr[2]);
	
	var period =  ((eD.getTime() - sD.getTime())/(24*3600*1000))/365;
	return period;
}

/**
 * 根据时间获取定期利率
 * 
 * @param year
 * @return
 */
function getListRate(year, type) {
	var term_values;
	var term_rates;
	switch(type) {
	case 1 :  // 整存整取
		term_values = zczq_saveTerm_values;
		term_rates = zczq_save_rates;
		break;
	case 2 : // 贷款
		term_values = dk_loanTerm_values;
		term_rates = dk_loan_rates;
		break;
	case 3 : // 房贷
		term_values = dk_house_loanTerm_values;
		term_rates = dk_house_loanTerm_rates;
		break;
	default : 
		term_values = zczq_saveTerm_values;
		term_rates = zczq_save_rates;
		break;
	}
	
	var start = term_values[0];
	
	if(year < start)
		return term_rates[0];
	var rate = 0;
	for(var i=0; i<term_values.length; i++) {
		if(i == 0) {
			continue;
		}
		
		if(year >= parseFloat(term_values[i], 10)) {
			rate = term_rates[i];
		} else {
			return term_rates[i - 1];
		}
	}
	
	return rate;
}

/**
 * 获取当前浏览页面的目录
 * 
 * @return
 */
function getCurPageListName() {
	var pathName = window.location.pathname;
	var strArray;
	if(pathName != null && "" != pathName) {
		strArray = pathName.split("/");
	}
	var listName;
	var result = new Array();
	if(strArray != null) {
		listName = strArray[strArray.length - 1];
		listName = listName.substring(0, listName.length - 5)
	}
	
	if("cx" == listName) {
		result = [0,"cx"];
	} else if("dk" == listName) {
		result = [1,"dk"];
	} else if("jj" == listName) {
		result = [2,"jj"];
	} else if("wh" == listName) {
		result = [3,"wh"];
	}
	
	return result;
}
