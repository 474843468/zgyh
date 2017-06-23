package com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheckold;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.16 【SA9206】远程开户单笔联网核查接口SV2007
 * 
 * @author gwluo
 * 
 */
public class MAORemoteOpenAccOnLineCheckOldParams extends MAOPBaseParamsModel {
	private static final String INTERFACE_URL = "remoteOpenAccOnLineCheckOld";
	private final static String ORGIDT = "orgIdt";
	private final static String FLAG = "flag";
	private final static String BANKCODE = "bankCode";
	private final static String BUSINESSTYPE = "businessType";
	private final static String ID = "id";
	private final static String NAME = "name";
	private final static String HANDORMAC = "handorMac";
	private final static String REMARK1 = "remark1";
	/**
	 * 网点机构号 String X(5) 必输
	 */
	public String orgIdt;
	/**
	 * 渠道标识 String X(7) 必输 渠道标识 Bancslink：0000001 网银：0000002 银医合作：0000003
	 * 自助发卡：0000004 IC卡POS渠道：0000007 财富管理：0000005 POS渠道：0000008 VTM渠道： 0000009
	 */
	public String flag;
	/**
	 * 核查机构对应的中行机构号 String X(12) 必输上送中行机构号
	 */
	public String bankCode;
	/**
	 * 业务类型 String X(3) 必输 附录1
	 */
	public String businessType;
	/**
	 * 身份证号码 String X(18) 必输
	 */
	public String id;
	/**
	 * 姓名 String X(30) 必输
	 */
	public String name;
	/**
	 * 手工/仪器识别 String X(1) 必输 0：仪器；1：手工
	 */
	public String handorMac;
	/**
	 * 备注 String X(100) 非必输 后补空格
	 */
	public String remark1;

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() +INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ORGIDT, orgIdt);
		jsonObject.put(FLAG, flag);
		jsonObject.put(BANKCODE, bankCode);
		jsonObject.put(BUSINESSTYPE, businessType);
		jsonObject.put(ID, id);
		jsonObject.put(NAME, name);
		jsonObject.put(HANDORMAC, handorMac);
		jsonObject.put(REMARK1, remark1);
		return jsonObject.toString();
	}
}
