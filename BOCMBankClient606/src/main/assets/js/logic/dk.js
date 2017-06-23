PUB_METHOD_Object.fn_dehk_init = function() {
	$("#dehk_btnCalc").click(dehk_compute);
	$("#dehk_dLine").change(dehk_setRate);
}

function dehk_setRate() {
	var year = parseFloat($("#dehk_dLine").val(), 10);
	$("#dehk_edLoanRate").val(getDateRate(year,2));
}

function dehk_compute() {
	var edInitLoan = $("#dehk_edInitLoan").val();
	var dLine = $("#dehk_dLine").val();
	var edLoanRate = $("#dehk_edLoanRate").val();
	var formula = $("#dehk_formula").val();
	var datas = [["贷款金额", edInitLoan, "number"],
	             ["贷款期限", dLine, "number"],
	             ["贷款利率", edLoanRate,"number"],
	             ["还款方式", formula, null]];
	
	if(checkData(datas))
		return;
	
	edInitLoan = parseFloat($.trim(edInitLoan), 10);
	dLine = parseInt($.trim(dLine), 10)*12;
	edLoanRate = parseFloat($.trim(edLoanRate), 10)/1200;
	formula = $.trim(formula);
	
	switch(formula) {
	case "1" : 
		dehk_dengebenxi(edInitLoan, edLoanRate, dLine);
		break;
	case "2" :
		dehk_dengebenjin(edInitLoan, edLoanRate, dLine);
		break;
	case "3" :
		dehk_yicixingfuqing(edInitLoan, edLoanRate, dLine);
		break;
	default :
		dehk_dengebenxi(edInitLoan, edLoanRate, dLine);
		break;
	}
}

function dehk_dengebenxi(initLoan, loanRate, dLine) {
	var str;
	var repayPI;//偿还本息
	var repayInterest;//偿还利息
	var repayPrincipal;//偿还本金
	var surplusPrincipal = initLoan;//剩余本金
	var repayPISum = 0,repayInterestSum = 0,repayPrincipalSum = 0;
	repayPI = (surplusPrincipal*loanRate*Math.pow((1 + loanRate),dLine))/(Math.pow((1 + loanRate),dLine) - 1);
	var data_table = $("#dehk_table-column-toggle");
	var tBody = $("#dehk_table-column-body");
	tBody.children().remove();
	for(var i=1; i<=parseInt(dLine, 10); i++) {
		repayPISum += repayPI;
		repayInterest = surplusPrincipal * loanRate;
		repayInterestSum += repayInterest;
		tBody.append("<tr>");
		tBody.append("<th style=\"padding:0px\"  class=\"label-center\">" + i + "</th>");
		tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayPI) + "</td>");
		tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayInterest) + "</td>");
		repayPrincipal = repayPI - repayInterest;
		repayPrincipalSum += repayPrincipal;
		tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayPI - repayInterest) + "</td>");
		surplusPrincipal = surplusPrincipal - repayPrincipal;
		if(surplusPrincipal < 0) {
			surplusPrincipal = 0;
		}
		tBody.append("</tr>");
	}
	tBody.append("<tr>");
	tBody.append("<th style=\"padding:0px\"  class=\"label-center\">合计</th>");
	tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayPISum) + "</td>");
	tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayInterestSum) + "</td>");
	tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayPrincipalSum) + "</td>");
	tBody.append("</tr>");
}

function dehk_dengebenjin(initLoan, loanRate, dLine) {
	var repayPI;//偿还本息
	var repayInterest;//偿还利息
	var repayPrincipal;//偿还本金
	var surplusPrincipal = initLoan;//剩余本金
	
	var repayPISum = 0,repayInterestSum = 0,repayPrincipalSum = 0;
	
	repayPrincipal = surplusPrincipal/dLine;
	var data_table = $("#dehk_table-column-toggle");
	var tBody = $("#dehk_table-column-body");
	tBody.children().remove();
	for(var i=1; i<=parseInt(dLine, 10); i++) {
		repayInterest = surplusPrincipal * loanRate;
		repayInterestSum += repayInterest;
		repayPI = repayPrincipal + repayInterest;
		repayPISum += repayPI;
		tBody.append("<tr>");
		tBody.append("<th style=\"padding:0px\" class=\"label-center\">" + i + "</th>");
		tBody.append("<td style=\"padding:0px\" class=\"label-center\">" + Round(repayPI) + "</td>");
		tBody.append("<td style=\"padding:0px\" class=\"label-center\">" + Round(repayInterest) + "</td>");
		repayPrincipal = repayPI - repayInterest;
		repayPrincipalSum += repayPrincipal;
		tBody.append("<td style=\"padding:0px\" class=\"label-center\">" + Round(repayPrincipal) + "</td>");
		surplusPrincipal = surplusPrincipal - repayPrincipal;
		if(surplusPrincipal < 0) {
			surplusPrincipal = 0;
		}
		tBody.append("</tr>");
	}
	
	tBody.append("<tr>");
	tBody.append("<th style=\"padding:0px\" class=\"label-center\">合计</th>");
	tBody.append("<td style=\"padding:0px\" class=\"label-center\">" + Round(repayPISum) + "</td>");
	tBody.append("<td style=\"padding:0px\" class=\"label-center\">" + Round(repayInterestSum) + "</td>");
	tBody.append("<td style=\"padding:0px\" class=\"label-center\">" + Round(repayPrincipalSum) + "</td>");
	tBody.append("</tr>");
}

function dehk_yicixingfuqing(initLoan, loanRate, dLine) {
	var repayPI;//偿还本息
	var repayInterest;//偿还利息
	var repayPrincipal;//偿还本金
	var surplusPrincipal = initLoan;//剩余本金
	var data_table = $("#dehk_table-column-toggle");
	var tBody = $("#dehk_table-column-body");
	tBody.children().remove();
	repayInterest = initLoan * loanRate * dLine
	repayPrincipal = initLoan;
	repayPI = repayInterest + parseInt(repayPrincipal, 10);
	tBody.append("<tr>");
	tBody.append("<th style=\"padding:0px\"  class=\"label-center\">合计</th>");
	tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayPI) + "</td>");
	tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayInterest) + "</td>");
	tBody.append("<td style=\"padding:0px\"  class=\"label-center\">" + Round(repayPrincipal) + "</td>");
	tBody.append("</tr>");
}

PUB_METHOD_Object.fn_grfd_init = function (){
	$("#grfd_btnCalc").click(grfd_compute);
	$("#grfd_calcaulator_type").change(grfd_changeElement);
	$("#grfd_mortgagePeriods").change(grfd_setRate);
	grfd_changeElement();
}

function grfd_setRate() {
	var year = parseFloat($("#grfd_mortgagePeriods").val(), 10);
	$("#grfd_mortgageRate").val(getListRate(year,3));
}

function grfd_compute() {
	var price = $("#grfd_price").val();//单价
	var area = $("#grfd_area").val();//面积
	var mortgageRatio = $("#grfd_mortgageRatio").val();//按揭比重
	var mortgagePeriods = $("#grfd_mortgagePeriods").val();//按揭年限
	var mortgageRate = $("#grfd_mortgageRate").val();//按揭利率
	var loanSum = $("#grfd_loanSum").val(); //贷款总额
	
	var c_type = $("#grfd_calcaulator_type").val();
	
	
	
	var datas;
	if(c_type == "2") { 
		datas = [["贷款总额", loanSum, "number"],
	             ["按揭年限", mortgagePeriods, "number"],
	             ["按揭利率", mortgageRate,"number"]];
	} else {
		datas = [["单价", price, "number"],
	             ["面积", area, "number"],
	             ["按揭比重", mortgageRatio, "number"],
	             ["按揭年限", mortgagePeriods, "number"],
	             ["按揭利率", mortgageRate,"number"]];
	}
	
	if(checkData(datas))
		return;
	
	mortgagePeriods = parseFloat($.trim(mortgagePeriods), 10);
	mortgageRate = parseFloat($.trim(mortgageRate), 10);
	price = parseFloat($.trim(price), 10);
	area = parseFloat($.trim(area), 10);
	mortgageRatio = parseFloat($.trim(mortgageRatio), 10);
	loanSum = parseFloat($.trim(loanSum), 10);
	
	_grfd_compute(loanSum, price, area, mortgageRatio,mortgagePeriods,mortgageRate, c_type);
}

function _grfd_compute(loanSum, price, area, mortgageRatio,mortgagePeriods,mortgageRate, type) {
	mortgagePeriods = mortgagePeriods * 12;
	mortgageRate = mortgageRate / 1200;
	var houseLoanSum = 0;
	var loanSum_d = 0;
	var repaySum = 0;
	var interestSum = 0;
	var firstTermPay = 0;
	
	
	if("1" == type) {
		houseLoanSum = Round(price * area);
		loanSum = houseLoanSum * mortgageRatio / 100;
		firstTermPay = Round(houseLoanSum * (1 - mortgageRatio / 100));
	} else {
		houseLoanSum = "#";
		firstTermPay = "#";
	}
	loanSum_d = loanSum;
	var eachMonthPay = (loanSum * mortgageRate * Math.pow((1 + mortgageRate),mortgagePeriods))/(Math.pow((1+mortgageRate), mortgagePeriods) - 1);
//	for(var i=0;i<mortgagePeriods;i++){
//		interestSum += loanSum * mortgageRate;
//		loanSum -= eachMonthPay;
//	}
	interestSum=eachMonthPay*mortgagePeriods-loanSum;
	repaySum = loanSum_d + interestSum;
	
	$("#grfd_eachMonthPay").empty().append(Round(eachMonthPay)).append(createUnitElement("元"));
	$("#grfd_houseLoanSum").empty().append(houseLoanSum).append(createUnitElement("元"));
	$("#grfd_loanSum_d").empty().append(Round(loanSum_d)).append(createUnitElement("元"));
	$("#grfd_repaySum").empty().append(Round(repaySum)).append(createUnitElement("元"));
	$("#grfd_firstTermPay").empty().append(firstTermPay).append(createUnitElement("元"));
	$("#grfd_interestSum").empty().append(Round(interestSum)).append(createUnitElement("元"));
}

function grfd_changeElement() {
	var type = $("#grfd_calcaulator_type").val();
	if(type == "2") {//根据面积、单价
		$("#grfd_calcaulator_type_li_4").addClass("ui-first-child");
		$("#grfd_calcaulator_type_li_4").show();
		$("#grfd_calcaulator_type_li_1").removeClass("ui-first-child").hide();
		$("#grfd_calcaulator_type_li_2").hide();
		$("#grfd_calcaulator_type_li_3").hide();
	} else {
		$("#grfd_calcaulator_type_li_4").removeClass("ui-first-child").hide();
		$("#grfd_calcaulator_type_li_1").show().addClass("ui-first-child");
		$("#grfd_calcaulator_type_li_2").show();
		$("#grfd_calcaulator_type_li_3").show();
	}
}

PUB_METHOD_Object.fn_tqhk_init = function() {
	$("#tqhk_btnCalc").click(tqhk_compute);
	$("#tqhk_oldRepayTerm").change(tqhk_setRate);
	$("#tqhk_repayType").change(tqhk_changeElement);
	$("#tqhk_firstRepayDate").val(getDate().substring(0, 7));
	$("#tqhk_predictRepayDate").val(getDate().substring(0, 7));
	tqhk_changeElement();
}

function tqhk_setRate() {
	var year = parseFloat($("#tqhk_oldRepayTerm").val(), 10);
	$("#tqhk_loanRate").val(getDateRate(year,2));
}

function tqhk_compute() {
	var loanSum = $("#tqhk_loanSum").val(); //贷款总额(元)
	var oldRepayTerm = $("#tqhk_oldRepayTerm").val();//原还款期限(年)
	var loanRate = $("#tqhk_loanRate").val();//贷款利率(%)
	var firstRepayDate = $("#tqhk_firstRepayDate").val();//第一次还款时间
	var predictRepayDate = $("#tqhk_predictRepayDate").val();//预计提前还款时间
	var repayType = $("#tqhk_repayType").val();//还款方式
	var repeiedDispose = $("#tqhk_repeiedDispose").val();//还款后处理方式
	var datas = [["贷款总额", loanSum, "number"],
	             ["原还款期限", oldRepayTerm, "number"],
	             ["贷款利率", loanRate,"number"],
	             ["第一次还款时间", firstRepayDate,"month"],
	             ["预计提前还款时间", predictRepayDate,"month"],
	             ["还款方式", repayType,null],
	             ["还款后处理方式", repeiedDispose, null]];
	if(startBeforeEnd(firstRepayDate, predictRepayDate)) {
		alert("预计提前还款时间不能早于第一次还款时间!");
		return;
	}
	
	var prepaymentAmount = $("#tqhk_prepaymentAmount").val();
	
	if(checkData(datas))
		return;
	
	loanSum = parseFloat($.trim(loanSum), 10);
	oldRepayTerm = parseFloat($.trim(oldRepayTerm), 10)*12;
	loanRate = parseFloat($.trim(loanRate), 10)/1200;
	firstRepayDate = $.trim(firstRepayDate);
	predictRepayDate = $.trim(predictRepayDate);
	repayType = $.trim(repayType);
	repeiedDispose = $.trim(repeiedDispose);
	prepaymentAmount = parseFloat($.trim(prepaymentAmount), 10);
	
	switch(repayType) {
	case "1" : 
		tqhk_yicixingfuqing(loanSum, oldRepayTerm, loanRate, firstRepayDate, predictRepayDate, prepaymentAmount, repeiedDispose);
		break;
	case "2" : 
		tqhk_bufenhuankuan(loanSum, oldRepayTerm, loanRate, firstRepayDate, predictRepayDate, prepaymentAmount, repeiedDispose);
		break;
	default :
		tqhk_yicixingfuqing(loanSum, oldRepayTerm, loanRate, firstRepayDate, predictRepayDate, prepaymentAmount, repeiedDispose);
		break;
	}
}

function tqhk_changeElement() {
	var type = $("#tqhk_repayType").val();
	if(type == "1") {
		$("#tqhk_repayType_li_1").hide();
		$("#tqhk_repayType_li_2").hide();
		$("#tqhk_repay_fieldset_1").hide();
		$("#tqhk_repay_fieldset_2").hide();
		$("#tqhk_repay_fieldset_3").hide();
		$("#tqhk_repay_hr_1").hide();
		$("#tqhk_repay_hr_2").hide();
		$("#tqhk_repay_hr_3").hide();
		$("#tqhk_hide_last_li").addClass("ui-last-child");
	} else {
		$("#tqhk_repayType_li_1").show();
		$("#tqhk_repayType_li_2").show();
		$("#tqhk_repay_fieldset_1").show();
		$("#tqhk_repay_fieldset_2").show();
		$("#tqhk_repay_fieldset_3").show();
		$("#tqhk_repay_hr_1").show();
		$("#tqhk_repay_hr_2").show();
		$("#tqhk_repay_hr_3").show();
		$("#tqhk_hide_last_li").removeClass("ui-last-child");
	}
}

function tqhk_getMonthSpace(sMonth, eMonth) {
	var sArray,eArray;
	if(sMonth != null && "" != sMonth) {
		sArray = sMonth.split("-");
	} else {
		sArray = 0;
	}
	if(eMonth != null && "" != eMonth) {
		eArray = eMonth.split("-");
	} else {
		eArray = 0;
	}
	
	if(sArray == 0 || eArray == 0) {
		return 0;
	} else {
		return (parseInt(eArray[0], 10) - parseInt(sArray[0], 10)) * 12 + (parseInt(eArray[1], 10) - parseInt(sArray[1], 10));
	}
}

function tqhk_yicixingfuqing(loanSum, oldRepayTerm, loanRate, firstRepayDate, predictRepayDate, prepaymentAmount, repeiedDispose) {
	var monthSpace = tqhk_getMonthSpace(firstRepayDate, predictRepayDate); //以还款期数
	var oldEachMonthPay = (loanSum * loanRate * Math.pow(1 + loanRate, oldRepayTerm))/(Math.pow(1 + loanRate, oldRepayTerm) - 1);
	var repaiedAmountSum = oldEachMonthPay * monthSpace; //以付还款总额
	var loanSumTemp = parseFloat(loanSum, 10);
	var repaiedInterestSum = 0; //以付利息金额
	var oldRepaiedInterestSum = 0; //原利息总额
	var economizeInterest;//节省利息总额
	
	for(var i=1;i<=oldRepayTerm;i++){
		var benjin = loanSumTemp * loanRate*Math.pow((1 + loanRate), (i - 1))/(Math.pow(1 + loanRate, oldRepayTerm) - 1);
		oldRepaiedInterestSum += (oldEachMonthPay - benjin);//计算原利息总额
	}
	
	var payableInterest = 0; //应付利息
	var loanSumTemp_2 = loanSumTemp;
	for(var j=1;j<=monthSpace;j++) {
		var benjin = loanSumTemp * loanRate*Math.pow((1 + loanRate), (j - 1))/(Math.pow(1 + loanRate, oldRepayTerm) - 1);
		payableInterest += oldEachMonthPay - benjin;
		loanSumTemp_2 -= benjin;
	}
	
	repaiedInterestSum = payableInterest;
	
	payableInterest += loanSumTemp_2 * loanRate;
	
	economizeInterest = oldRepaiedInterestSum - payableInterest;
	
	var curMonthPay = loanSumTemp_2 * (1 + loanRate) + oldEachMonthPay;
	
	var oldLastTerm = tqhk_getLastPayTerm(1 ,firstRepayDate, predictRepayDate ,oldRepayTerm);
	
	$("#tqhk_oldEachMonthPay").empty().append(Round(oldEachMonthPay)).append(createUnitElement("元"));
	$("#tqhk_economizeInterest").empty().append(Round(economizeInterest)).append(createUnitElement("元"));
	$("#tqhk_repaiedInterestSum").empty().append(Round(repaiedInterestSum)).append(createUnitElement("元"));
	$("#tqhk_curMonthPay").empty().append(Round(curMonthPay)).append(createUnitElement("元"));
	$("#tqhk_repaiedAmountSum").empty().append(Round(repaiedAmountSum)).append(createUnitElement("元"));
	
}

function tqhk_getLastPayTerm(type ,firstRepayDate, predictRepayDate ,oldRepayTerm) {
	var lastTerm;
	if(type == 1) {
		var fArray = firstRepayDate.split("-");
		var month = (parseInt(fArray[1], 10) + parseInt(oldRepayTerm, 10)) % 12;
		var year = Math.floor((parseInt(fArray[1], 10) + parseInt(oldRepayTerm, 10)) / 12);
		month -= 1;
		if(month == 0) {
			year -= 1;
			month = "12";
		}
		lastTerm = (parseInt(fArray[0], 10) + year)+ "<font size=\"2px\" color=\"#a1a3a6\" style=\"font-weight:normal\"> 年 </font>" + month + "<font size=\"2px\" color=\"#a1a3a6\" style=\"font-weight:normal\"> 月</font>";
	} else {
		var fArray = firstRepayDate.split("-");
		var month = (parseInt(fArray[1], 10) + (oldRepayTerm - predictRepayDate)) % 12;
		var year = Math.floor((parseInt(fArray[1], 10) + (oldRepayTerm - predictRepayDate)) / 12);
		month -= 1;
		if(month == 0) {
			year -= 1;
			month = "12";
		}
		lastTerm = (parseInt(fArray[0], 10) + year)+ "<font size=\"2px\" color=\"#a1a3a6\" style=\"font-weight:normal\"> 年 </font>" + month + "<font size=\"2px\" color=\"#a1a3a6\" style=\"font-weight:normal\"> 月</font>";
	}
	
	return lastTerm;
}

function tqhk_bufenhuankuan(loanSum, oldRepayTerm, loanRate, firstRepayDate, predictRepayDate, prepaymentAmount, repeiedDispose) {
	var monthSpace = tqhk_getMonthSpace(firstRepayDate, predictRepayDate); //以还款期数
	var oldEachMonthPay = (loanSum * loanRate * Math.pow(1 + loanRate, oldRepayTerm))/(Math.pow(1 + loanRate, oldRepayTerm) - 1);
	var repaiedAmountSum = oldEachMonthPay * monthSpace; //以付还款总额
	var loanSumTemp = parseFloat(loanSum, 10);
	var repaiedInterestSum = 0; //以付利息金额
	var oldRepaiedInterestSum = 0; //原利息总额
	var economizeInterest = 0;//节省利息总额
	var oldRepaiedInterest = 0;
	for(var i=1;i<=oldRepayTerm;i++){
		var benjin = loanSumTemp * loanRate*Math.pow((1 + loanRate), (i - 1))/(Math.pow(1 + loanRate, oldRepayTerm) - 1);
		if(i <= monthSpace) {
			oldRepaiedInterest += (oldEachMonthPay - benjin);
		}
		oldRepaiedInterestSum += (oldEachMonthPay - benjin); 
	}
	
	var jieshengcunqi = 0;
	
	
	var nextMonthPay = 0;
	var newLastTerm;
	var yihuanbenji = repaiedAmountSum - oldRepaiedInterest;//已还本金
	var curMonthPay = prepaymentAmount + oldEachMonthPay;//当月还款
	var newLoanSum = (loanSum - yihuanbenji)*(1 + loanRate) - curMonthPay;//提前还款后总额
	
	if(prepaymentAmount > newLoanSum) {
		alert("部分还款金额已足够一次性付清贷款本息！");
		return;
	}
	
	var newRepayTerm = 0;
	if(repeiedDispose == "1") {//缩短存期
		jieshengcunqi = Math.ceil(prepaymentAmount / oldEachMonthPay);
		newRepayTerm = oldRepayTerm - jieshengcunqi - monthSpace - 1;//新存期
		nextMonthPay = (newLoanSum * loanRate * Math.pow(1 + loanRate, newRepayTerm))/(Math.pow(1 + loanRate, newRepayTerm) - 1);
		for(var k=monthSpace+1;k<=(oldRepayTerm - jieshengcunqi);k++) {
			var benjin = loanSumTemp * loanRate*Math.pow((1 + loanRate), (k - 1))/(Math.pow(1 + loanRate, oldRepayTerm) - 1);
			economizeInterest += (oldEachMonthPay - benjin);
		}
		oldLastTerm = tqhk_getLastPayTerm(1, firstRepayDate, predictRepayDate, oldRepayTerm);
		newLastTerm = tqhk_getLastPayTerm(2, firstRepayDate, jieshengcunqi, oldRepayTerm);
	} else {//减少月还款量
		newRepayTerm = oldRepayTerm - monthSpace - 1;//新存期
		nextMonthPay = (newLoanSum * loanRate * Math.pow(1 + loanRate, newRepayTerm))/(Math.pow(1 + loanRate, newRepayTerm) - 1);
		for(var k=1;k<=newRepayTerm;k++) {
			var benjin = newLoanSum * loanRate*Math.pow((1 + loanRate), (k - 1))/(Math.pow(1 + loanRate, newRepayTerm) - 1);
			economizeInterest += (nextMonthPay - benjin);
		}
		economizeInterest = oldRepaiedInterestSum - economizeInterest - oldRepaiedInterest - (loanSum - yihuanbenji) * loanRate;
		oldLastTerm = tqhk_getLastPayTerm(1, firstRepayDate, predictRepayDate, oldRepayTerm);
		newLastTerm = oldLastTerm;
	}
	
	$("#tqhk_oldEachMonthPay").empty().append(Round(oldEachMonthPay)).append(createUnitElement("元"));
	$("#tqhk_oldLastTerm").empty().append(oldLastTerm);
	$("#tqhk_repaiedAmountSum").empty().append(Round(repaiedAmountSum)).append(createUnitElement("元"));
	$("#tqhk_repaiedInterestSum").empty().append(Round(oldRepaiedInterest)).append(createUnitElement("元"));
	$("#tqhk_curMonthPay").empty().append(Round(curMonthPay)).append(createUnitElement("元"));
	$("#tqhk_nextMonthPay").empty().append(Round(nextMonthPay)).append(createUnitElement("元"));
	$("#tqhk_economizeInterest").empty().append(Round(economizeInterest)).append(createUnitElement("元"));
	$("#tqhk_newLastTerm").empty().append(newLastTerm);
}
