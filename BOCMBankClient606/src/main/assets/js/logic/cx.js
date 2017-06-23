PUB_METHOD_Object.fn_zczq_init = function() {
	var name = $("#select_menu").val();
	if(name == "zczq") {
		addItemToSelect("zczq_saveTerm", zczq_saveTerm_names, zczq_saveTerm_values);
		$("#zczq_edFullRate").val(zczq_save_rates[0]);
	} else {
		addItemToSelect(name + "_saveTerm", 
				[zczq_saveTerm_names[2],zczq_saveTerm_names[4],zczq_saveTerm_names[5]], 
				[zczq_saveTerm_values[2],zczq_saveTerm_values[4],zczq_saveTerm_values[5]]);
		$("#" + name + "_edFullRate").val(zclq_save_rates[0]);
	}
}

function zczq_setRate() {
	var rates;
	var saveTermValues;
	var name = $("#select_menu").val();
	if(name == "zczq") { //设置定期存期
		rates = zczq_save_rates;
		saveTermValues = zczq_saveTerm_values;
	} else { 
		saveTermValues = [zczq_saveTerm_values[2],zczq_saveTerm_values[4],zczq_saveTerm_values[5]];
		rates = zclq_save_rates;
	}
	
	var saveTerm = $("#" + name + "_saveTerm").val();
	for(var i=0; i<rates.length; i++) {
		if(saveTermValues[i] == saveTerm){
			$("#" + name + "_edFullRate").val(rates[i]);
			break;
		}
	}
}

function zczq_compute() {
	var initSaveSum,term,yRate,sumCount,taxSum;
	initSaveSum = $("#zczq_edInitSaveSum").val();
	term = $("#zczq_saveTerm").val();
	yRate = $("#zczq_edFullRate").val();
	
	var datas = [["初始存入金额", initSaveSum, "number"],
	             ["储蓄存期", term, "number"],
	             ["年利率(%)", yRate,"number"]];
	
	if(checkData(datas)) {
		return;
	}
	initSaveSum = $.trim(initSaveSum);
	term = $.trim(term);
	yRate = $.trim(yRate) / 100;
	
	taxSum = Round(initSaveSum * term * yRate);
	sumCount = Round(taxSum * 1.00 + parseFloat(initSaveSum, 10));
	
	$("#zczq_edCapitalSum").empty().append(taxSum).append(createElement("font", ["size","color","style"], ["2px","#a1a3a6","font-weight:normal"], " 元"));
	$("#zczq_edFullSum").empty().append(sumCount).append(createElement("font", ["size","color","style"], ["2px","#a1a3a6","font-weight:normal"], " 元"));
}

function zclq_compute() {
	var initSaveSum,term,yRate,frequency,sumCount=0,taxSum=0;
	initSaveSum = $("#zclq_edInitSaveSum").val();
	term = $("#zclq_saveTerm").val();
	yRate = $("#zclq_edFullRate").val();
	frequency = $("#zclq_frequency").val();
	
	var datas = [["初始存入金额", initSaveSum, "number"],
	             ["储蓄存期", term, "number"],
	             ["年利率(%)", yRate,"number"],
	             ["支取频次", frequency, "number"]];
	
	if(checkData(datas)) {
		return;
	}
	initSaveSum = $.trim(initSaveSum);
	frequency = $.trim(frequency);
	term = $.trim(term);
	yRate = $.trim(yRate) / 1200;
	sumCount = parseFloat(initSaveSum, 10);
	var withOutCount = parseInt(term*12/frequency,10);
	var eachWithoutAmount = initSaveSum / withOutCount;
	for(var i=0; i<withOutCount; i++) {
		taxSum += initSaveSum * yRate * frequency;
		initSaveSum -= eachWithoutAmount;
	}
	
	sumCount += taxSum*1.00;
	
	$("#zclq_eachAmount").empty().append(Round(eachWithoutAmount)).append(createUnitElement("元"));
	$("#zclq_edCapitalSum").empty().append(Round(taxSum*1.00)).append(createUnitElement("元"));
	$("#zclq_edFullSum").empty().append(Round(sumCount)).append(createUnitElement("元"));
}

function lczq_compute() {
	var initSaveSum,term,yRate,sumCount=0,taxSum=0;
	initSaveSum = $("#lczq_edInitSaveSum").val();
	term = $("#lczq_saveTerm").val();
	yRate = $("#lczq_edFullRate").val();
	eachMonthIn = $("#lczq_eachMonthIn").val();
	
	var datas = [["初始存入金额", initSaveSum, "number"],
	             ["储蓄存期", term, "number"],
	             ["年利率(%)", yRate,"number"],
	             ["每月存入金额", eachMonthIn, "number"]];
	
	if(checkData(datas)) {
		return;
	}
	
	initSaveSum = parseFloat($.trim(initSaveSum));
	term = parseInt($.trim(term),10);
	yRate = $.trim(yRate) / 1200; //月利率
	eachMonthIn = parseFloat($.trim(eachMonthIn),10);
	
	sumCount = initSaveSum + eachMonthIn;
	
	var month = term * 12;
	
	taxSum = (month + 1)/2*month*eachMonthIn*yRate;
	
	$("#lczq_edCapitalSum").empty().append(Round(taxSum)).append(createUnitElement("元"));
	$("#lczq_edFullSum").empty().append(Round(eachMonthIn * month + taxSum)).append(createUnitElement("元"));
}

function cbqx_compute() {
	var initDeposit = $("#cbqx_edInitSaveSum").val();
	var saveTerm = $("#cbqx_saveTerm").val();
	var withdrawalCount = $("#cbqx_withdrawalCount").val();
	var rate = $("#cbqx_edFullRate").val();
	
	var datas = [["初始存入金额", initDeposit, "number"],
	             ["储蓄存期", saveTerm, "number"],
	             ["年利率(%)", rate,"number"],
	             ["支取频次", withdrawalCount,"number"]];
	
	if(checkData(datas))
		return;
	
	initDeposit = parseFloat($.trim(initDeposit), 10);
	saveTerm = parseFloat($.trim(saveTerm), 10)*12;
	withdrawalCount = parseInt($.trim(withdrawalCount), 10);
	rate = parseFloat($.trim(rate), 10)/(saveTerm / withdrawalCount*100);
	var taxSum = initDeposit * rate * saveTerm / withdrawalCount;
	
	$("#cbqx_edCapitalSum").empty().append(Round(taxSum)).append(createUnitElement("元"));
	$("#cbqx_eachTax").empty().append(Round(taxSum/withdrawalCount)).append(createUnitElement("元"));
	$("#cbqx_edFullSum").empty().append(Round(initDeposit + taxSum * 1.00)).append(createUnitElement("元"));
}

PUB_METHOD_Object.fn_dhlb_init = function dhlb_init() {
	var name = $("#select_menu").val();
	$("#" + name + "_btnCalc").click(dhlb_compute);
	$("#" + name + "_startDate").val(getDate());
	$("#" + name + "_endDate").val(getDate(1));
	
	if(name == "dhlb") {
		dhlb_setRate();
	} else {
		$("#" + name + "_edFullRate").val(hqcx_rate);
	}
}

function dhlb_compute() {
	var name = $("#select_menu").val();
	var initDeposit = $("#" + name + "_edInitSaveSum").val();
	var startDate = $("#" + name + "_startDate").val();
	var endDate = $("#" + name + "_endDate").val();
	var rate = $("#" + name + "_edFullRate").val();
	
	var datas = [["初始存入金额", initDeposit, "number"],
	             ["初始存入日期", startDate, "date"],
	             ["年利率", rate,"number"],
	             ["提取日期", endDate,"date"]];
	
	if(checkData(datas)) //校验参数合法性
		return;
	
	if(startBeforeEnd(startDate, endDate)) {
		return;
	}
	
	initDeposit = parseFloat($.trim(initDeposit),10);
	rate = parseFloat($.trim(rate), 10);
	startDate = $.trim(startDate);
	endDate = $.trim(endDate);
	//获取存储年份
	var period = parseFloat(getPeriod(startDate,endDate), 10);
	var tax = initDeposit * rate /100 * period;
	var sum = initDeposit + tax * 1.00;
	$("#" + name + "_edFullInterest").empty().append(Round(tax)).append(createUnitElement("元"));
	$("#" + name + "_edFullSum").empty().append(Round(sum)).append(createUnitElement("元"));
}

function dhlb_setRate() {
	var startDate = $("#dhlb_startDate").val();
	var endDate = $("#dhlb_endDate").val();
	var datas = [["初始存入日期", startDate, "date"],
	             ["提取日期", endDate,"date"]];
	
	if(checkData(datas)) //校验参数合法性
		return;
	
	startDate = $.trim(startDate);
	endDate = $.trim(endDate);
	$("#dhlb_edFullRate").val(getDateRate(getPeriod(startDate, endDate), 1));
}