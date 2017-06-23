package com.boc.bocsoft.remoteopenacc.buss.model.queryothopenbankbybankid;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryothopenbankbybankid.MAOQueryOthOpenBankByBankIdParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 1.2.15 【SA9205】非农补卡BIN信息查询开户行接口
 * 
 * @author gwluo
 * 
 */
public class QueryOthOpenBankByBankIdParamsModel implements BaseParamsModel {
	/**
	 * 总行行号 String (3) Y 纯数字，为接口13根据卡号查询卡bin信息返回的bankId
	 */
	public String othCardTopCnaps;

	@Override
	public MAOQueryOthOpenBankByBankIdParamsModel transformParamsModel() {
		MAOQueryOthOpenBankByBankIdParamsModel params = new MAOQueryOthOpenBankByBankIdParamsModel();
		params.othCardTopCnaps = othCardTopCnaps;
		return params;
	}

}
