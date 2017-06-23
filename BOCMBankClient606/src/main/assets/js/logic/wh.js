
var wbdh_gExchangeRate;

PUB_METHOD_Object.fn_wbdh_init = function() {
	addItemToSelect("wbdh_convertedMoneyCur", foreign_currency_names, foreign_currency_values);
	addItemToSelect("wbdh_initMoneyCur", foreign_currency_names, foreign_currency_values);
	$("#wbdh_btnCalc").click(wbdh_compute);
	$("#wbdh_initMoneyCur").change(wbdh_setRate);
	$("#wbdh_convertedMoneyCur").change(wbdh_setRate);
	$("#wbdh_exchangeRate").val(1);
}

function wbdh_compute() {
	
	var initMoney = $("#wbdh_initMoney").val();
	var exchangeRate = $("#wbdh_exchangeRate").val();
	
	var datas = [["欲兑换货币汇率", exchangeRate, "number"],
	             ["现持有货币数量", initMoney, "number"]];
	if(checkData(datas))
		return;
	
	initMoney = parseFloat($.trim(initMoney));
	exchangeRate = parseFloat($.trim(exchangeRate));
	
	$("#wbdh_convertedAmount").empty().append(Round(initMoney*exchangeRate)).append(createUnitElement("元"));
}

function wbdh_setRate() {
	var initMoneyCur = $("#wbdh_initMoneyCur").val();
	var convertedMoneyCur = $("#wbdh_convertedMoneyCur").val();
	var exchangeRate;
	switch(initMoneyCur) {
	case foreign_currency_values[0] : //人民币
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[0], convertedMoneyCur);
		break;
	case foreign_currency_values[1] : //港币
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[1], convertedMoneyCur);
		break;
	case foreign_currency_values[2] : //美元
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[2], convertedMoneyCur);
		break;
	case foreign_currency_values[3] : //英镑
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[3], convertedMoneyCur);
		break;
	case foreign_currency_values[4] : //澳大利亚元
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[4], convertedMoneyCur);
		break;
	case foreign_currency_values[5] : //日元
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[5], convertedMoneyCur);
		break;
	case foreign_currency_values[6] : //加拿大元
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[6], convertedMoneyCur);
		break;
	default :
		exchangeRate = wbdh_getExchangeRate(foreign_currency_rates[0], convertedMoneyCur);
		break;
	}
	wbdh_gExchangeRate = exchangeRate;
	$("#wbdh_exchangeRate").val(Round(wbdh_gExchangeRate));
}

function wbdh_getExchangeRate(initMoneyCur, convertedMoneyCur) {
	switch(convertedMoneyCur) {
	case foreign_currency_values[0] : //人民币
		return initMoneyCur/foreign_currency_rates[0];
		break;
	case foreign_currency_values[1] : //港币
		return initMoneyCur/foreign_currency_rates[1];
		break;
	case foreign_currency_values[2] : //美元
		return initMoneyCur/foreign_currency_rates[2];
		break;
	case foreign_currency_values[3] : //英镑
		return initMoneyCur/foreign_currency_rates[3];
		break;
	case foreign_currency_values[4] : //澳大利亚元
		return initMoneyCur/foreign_currency_rates[4];
		break;
	case foreign_currency_values[5] : //日元
		return initMoneyCur/foreign_currency_rates[5];
		break;
	case foreign_currency_values[6] : //加拿大元
		return initMoneyCur/foreign_currency_rates[6];
		break;
	default :
		return initMoneyCur/foreign_currency_rates[0];
		break;
	}
}
