PUB_METHOD_Object.fn_jjzh_init = function () {
	$("#jjzh_btnCalc").click(jjzh_compute);
}

function jjzh_compute() {
	
	var outFundFaceValue = $("#jjzh_outFundFaceValue").val(); //转出基金净值
	var outFundShare = $("#jjzh_outFundShare").val(); //转出基金份额
	var outFundRedemptionsRate = $("#jjzh_outFundRedemptionsRate").val();//转出基金赎回费率
	var inFundFaceValue = $("#jjzh_inFundFaceValue").val();//转入基金净值
	var outBalanceRate = $("#jjzh_outBalanceRate").val();//转出补差费率
	
	var datas = [["转出基金净值", outFundFaceValue, "number"],
	             ["转出基金份额", outFundShare, "number"],
	             ["转出基金赎回费率", outFundRedemptionsRate,"number"],
	             ["转入基金净值", inFundFaceValue,"number"],
	             ["转出补差费率", outBalanceRate,"number"]];
	
	if(checkData(datas))
		return;
	
	outFundFaceValue = parseFloat($.trim(outFundFaceValue), 10);
	outFundShare = parseFloat($.trim(outFundShare), 10);
	outFundRedemptionsRate = parseFloat($.trim(outFundRedemptionsRate), 10)/100;
	inFundFaceValue = parseFloat($.trim(inFundFaceValue), 10);
	outBalanceRate = parseFloat($.trim(outBalanceRate), 10)/100;
	
	var inFundShare = 0; //转入份额
	var outFundRedemptionAmount = 0; //认购费用
	var subscribeBalance = 0; //认购利息
	
	inFundShare = (outFundShare * outFundFaceValue * (1 - outFundRedemptionsRate)/(1 + outBalanceRate) + 0) / inFundFaceValue; 
	outFundRedemptionAmount = outFundShare * outFundFaceValue * outFundRedemptionsRate;
	subscribeBalance = (outFundShare * outFundFaceValue * (1 - outFundRedemptionsRate) / (1 + outBalanceRate)) * outBalanceRate
	
	$("#jjzh_inFundShare").empty().append(Round(inFundShare)).append(createUnitElement("份"));
	$("#jjzh_outFundRedemptionAmount").empty().append(Round(outFundRedemptionAmount)).append(createUnitElement("元"));
	$("#jjzh_subscribeBalance").empty().append(Round(subscribeBalance)).append(createUnitElement("元"));
}


PUB_METHOD_Object.fn_rg_init = function() {
	$("#rg_btnCalc").click(rg_compute);
	$("#rg_saveRate").val(hqcx_rate);
}

function rg_compute() {
	
	var initFaceValue = $("#rg_initFaceValue").val(); //基金初始面值(元)
	var saveRate = $("#rg_saveRate").val(); //银行活期利率(%)
	var subscriptionRate = $("#rg_subscriptionRate").val();//认购费率(%)
	var subscriptionAmount = $("#rg_subscriptionAmount").val();//认购金额
	
	var datas = [["认购金额", subscriptionAmount, "number"],
	             ["认购费率", subscriptionRate, "number"],
	             ["银行活期利率", saveRate,"number"],
	             ["基金初始面值", initFaceValue,"number"]];
	
	if(checkData(datas))
		return;
	
	subscriptionAmount = parseFloat($.trim(subscriptionAmount), 10);
	subscriptionRate = parseFloat($.trim(subscriptionRate), 10)/100;
	saveRate = parseFloat($.trim(saveRate), 10)/1200;
	initFaceValue = parseFloat($.trim(initFaceValue), 10);
	
	//净认购金额
	var netAmount = subscriptionAmount / (1 + subscriptionRate);
	var subscriptionShare = 0; //认购份额
	var subscriptionExpense = 0; //认购费用
	var subscriptionInterest = 0; //认购利息
	
	subscriptionInterest = subscriptionAmount * saveRate;
	subscriptionExpense = Math.ceil(subscriptionAmount - netAmount);
	subscriptionShare = parseInt((subscriptionInterest + netAmount)/initFaceValue, 10);
	
	$("#rg_subscriptionShare").empty().append(Round(subscriptionShare)).append(createUnitElement("份"));
	$("#rg_subscriptionExpense").empty().append(Round(subscriptionExpense)).append(createUnitElement("元"));
	$("#rg_subscriptionInterest").empty().append(Round(subscriptionInterest)).append(createUnitElement("元"));
}

PUB_METHOD_Object.fn_sg_init = function() {
	$("#sg_btnCalc").click(sg_compute);
}

function sg_compute() {
	
	var fundNetFaceValue = $("#sg_fundNetFaceValue").val(); //基金净值
	var subscribeAmount = $("#sg_subscribeAmount").val(); //申购金额
	var subscribeRate = $("#sg_subscribeRate").val();//申购费率
	
	var datas = [["申购金额", subscribeAmount, "number"],
	             ["基金份额净值", fundNetFaceValue, "number"],
	             ["申购费率", subscribeRate,"number"]];
	
	if(checkData(datas))
		return;
	
	subscribeAmount = parseFloat($.trim(subscribeAmount), 10);
	fundNetFaceValue = parseFloat($.trim(fundNetFaceValue), 10);
	subscribeRate = parseFloat($.trim(subscribeRate), 10)/100;
	
	var netAmount = subscribeAmount / (1 + subscribeRate);
	
	var subscribeExpense = subscribeAmount - netAmount; //申购费用
	var subscribeShare = netAmount / fundNetFaceValue; //申购份额
	
	$("#sg_subscribeExpense").empty().append(Round(subscribeExpense)).append(createUnitElement("元"));
	$("#sg_subscribeShare").empty().append(Round(subscribeShare)).append(createUnitElement("份"));
}

PUB_METHOD_Object.fn_sh_init = function init() {
	$("#sh_btnCalc").click(sh_compute);
}

function sh_compute() {
	
	var fundNetFaceValue = $("#sh_fundNetFaceValue").val(); //基金份额净值
	var redemptionShare = $("#sh_redemptionShare").val(); //赎回份数
	var redemptionRate = $("#sh_redemptionRate").val();//赎回费率(%)
	
	var datas = [["基金份额净值", fundNetFaceValue, "number"],
	             ["赎回份数", redemptionShare, "number"],
	             ["赎回费率", redemptionRate,"number"]];
	
	if(checkData(datas))
		return;
	
	fundNetFaceValue = parseFloat($.trim(fundNetFaceValue), 10);
	redemptionShare = parseFloat($.trim(redemptionShare), 10);
	redemptionRate = parseFloat($.trim(redemptionRate), 10)/100;
	
	var redemptionExpense = 0; //赎回费用
	var redemptionAmount = 0; //赎回总额
	
	redemptionAmount = redemptionShare * fundNetFaceValue;
	redemptionExpense = redemptionAmount * redemptionRate;
	
	$("#sh_redemptionAmount").empty().append(Round(redemptionAmount)).append(createUnitElement("元"));
	$("#sh_redemptionExpense").empty().append(Round(redemptionExpense)).append(createUnitElement("元"));
}
