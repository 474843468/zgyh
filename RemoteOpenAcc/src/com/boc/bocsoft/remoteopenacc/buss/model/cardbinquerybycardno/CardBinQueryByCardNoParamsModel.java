package com.boc.bocsoft.remoteopenacc.buss.model.cardbinquerybycardno;

import com.boc.bocma.serviceinterface.op.interfacemodel.cardbinquerybycardno.MAOCardBinQueryByCardNoParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 1.2.14 【SA9180】查询非农补卡BIN信息接口
 * 
 * @author gwluo
 * 
 */
public class CardBinQueryByCardNoParamsModel implements BaseParamsModel {
	/**
	 * 他行卡卡号 String (32) Y 纯数字
	 */
	public String cardNo;

	@Override
	public MAOCardBinQueryByCardNoParamsModel transformParamsModel() {
		MAOCardBinQueryByCardNoParamsModel params = new MAOCardBinQueryByCardNoParamsModel();
		params.cardNo = cardNo;
		return params;
	}

}
