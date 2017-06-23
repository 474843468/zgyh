package com.boc.bocsoft.remoteopenacc.buss.model.checkpersonidentity;

import android.util.Log;

import com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity.MAOPCheckPersonIdentityResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 1.2.1 【SA9178】开户前身份验证返回model
 * 
 * @author fb
 * @version p601 增加 serviceResponse，responseCode，responseMsg by lgw 15.12.3
 */
public class CheckPersonIdentityResponseModel implements
		BaseResultModel<CheckPersonIdentityResponseModel> {

	public String uuid; // 票值信息
	public String serviceResponse;// jsonObject其中包含responseCode和responseMsg
	/**
	 * X(7) 0000000:交易成功 9999999:系统异常 1000001:必输项不能为空 1000002:数据校验失败
	 * 1000003:查询结果为空 当返回码为1000083时，需要调用重新发送接口。
	 */
	public String responseCode;

	/**
	 * X(100) 必输
	 */
	public String responseMsg;
	/**
	 * 二类账户账号 X(32)
	 */
	public String card2nd;
	/**
	 * 三类账户账号 X(32)
	 */
	public String card3rd;
	/**
	 * 绑定卡卡号 X(32)
	 */
	public String cardOth;
	/**
	 * 原业务类型 X(2)
	 */
	public String openTypePre;
	/**
	 * 待重发二（二、三同时）类账户开户申请uuid X(32)
	 */
	public String reUuid1;
	/**
	 * 待重发三类账户开户申请uuid X(32)
	 */
	public String reUuid2;
	/**
	 * 跳转页面 String(1) N 2身份证照片上传 3绑定卡信息
	 */
	public String pageType;
	/**
	 * 客户姓名 String(26) N 如果是中行客户需要给前端返回姓名
	 */
	public String name;
	/**
	 * 影像ID String(32) Y 分配的影像ID
	 */
	public String piCid;
	/**
	 * 影像操作类型 String(1) Y 影像操作类型 A新增 E修改
	 */
	public String operType;

	// /**
	// * 跳转标识 X(1) 0身份证照片上传 1绑定卡信息
	// */
	// public String pageFlag;
	// /**
	// * 中行客户标识 X(1) 0中行客户 1非中行客户
	// */
	// public String bocFlag;

	@Override
	public CheckPersonIdentityResponseModel parseResultModel(Object resultModel) {
		MAOPCheckPersonIdentityResponseModel result = (MAOPCheckPersonIdentityResponseModel) resultModel;
		uuid = result.uuid;
		responseCode = result.responseCode;
		serviceResponse = result.serviceResponse;
		responseCode = result.responseCode;
		responseMsg = result.responseMsg;
		card2nd = result.card2nd;
		card3rd = result.card3rd;
		cardOth = result.cardOth;
		openTypePre = result.cardOth;
		reUuid1 = result.reUuid1;
		reUuid2 = result.reUuid2;
		pageType = result.pageType;
		name = result.name;
		piCid = result.piCid;
		operType = result.operType;
		// pageFlag = result.getPageFlag();
		// bocFlag = result.getBocFlag();
		Log.d("feib", "------------->uuid02p  " + uuid);
		return this;
	}
}
