package com.boc.bocsoft.remoteopenacc.buss.model.cardbinquerybycardno;

import com.boc.bocma.serviceinterface.op.interfacemodel.cardbinquerybycardno.MAOCardBinQueryByCardNoResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 1.2.14 【SA9180】查询非农补卡BIN信息接口
 * 
 * @author gwluo
 * 
 */
public class CardBinQueryByCardNoResponseModel implements
		BaseResultModel<CardBinQueryByCardNoResponseModel> {
	/**
	 * 是否农补卡标识 String (1) 1-农补卡
	 */
	public String agCardFlag;
	/**
	 * 银行行号 String (32) Y
	 */
	public String bankId;
	/**
	 * 绑定账号类型 String (2) Y
	 */
	public String bindType;
	/**
	 * 开户行 String(128) Y
	 */
	public String openBank;
	/**
	 * 错误码 String (9)
	 */
	public String errCode;
	/**
	 * 错误原因 String(100)
	 */
	public String errReason;
	/**
	 * 返回码 String(7) Y 详见附录
	 */
	public String responseCode;
	/**
	 * 返回消息 String(100) Y 返回具体的错误信息
	 */
	public String responseMsg;

	@Override
	public CardBinQueryByCardNoResponseModel parseResultModel(Object resultModel) {
		MAOCardBinQueryByCardNoResponseModel result = (MAOCardBinQueryByCardNoResponseModel) resultModel;
		agCardFlag = result.agCardFlag;
		bankId = result.bankId;
		bindType = result.bindType;
		openBank = result.openBank;
		errCode = result.errCode;
		errReason = result.errReason;
		responseCode = result.responseCode;
		responseMsg = result.responseMsg;
		return this;
	}

}
