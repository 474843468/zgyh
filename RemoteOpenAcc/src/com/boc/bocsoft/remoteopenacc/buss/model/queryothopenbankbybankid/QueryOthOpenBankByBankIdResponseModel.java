package com.boc.bocsoft.remoteopenacc.buss.model.queryothopenbankbybankid;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryothopenbankbybankid.MAOQueryOthOpenBankByBankIdResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 1.2.15 【SA9205】非农补卡BIN信息查询开户行接口
 * 
 * @author gwluo
 * 
 */
public class QueryOthOpenBankByBankIdResponseModel implements
		BaseResultModel<QueryOthOpenBankByBankIdResponseModel> {
	/**
	 * 他行开户行名称 String (50) Y
	 */
	public String othCardBankName;
	/**
	 * 总行机构号 String (16) Y
	 */
	public String othCardTopCnaps;
	/**
	 * 返回码 String(7) Y 详见附录
	 */
	public String responseCode;
	/**
	 * 返回消息 String(100) Y 返回具体的错误信息
	 */
	public String responseMsg;

	@Override
	public QueryOthOpenBankByBankIdResponseModel parseResultModel(
			Object resultModel) {
		MAOQueryOthOpenBankByBankIdResponseModel result = (MAOQueryOthOpenBankByBankIdResponseModel) resultModel;
		othCardBankName = result.othCardBankName;
		othCardTopCnaps = result.othCardTopCnaps;
		responseCode = result.responseCode;
		responseMsg = result.responseMsg;
		return this;
	}
}
