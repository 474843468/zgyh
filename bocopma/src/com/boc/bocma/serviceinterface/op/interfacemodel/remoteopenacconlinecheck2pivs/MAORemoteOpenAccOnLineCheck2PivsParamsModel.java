package com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheck2pivs;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.17 【SA9207】远程开户单笔联网核查接口tv0001
 * 
 * @author gwluo
 * 
 */
public class MAORemoteOpenAccOnLineCheck2PivsParamsModel extends
		MAOPBaseParamsModel {
	private static final String INTERFACE_URL = "remoteOpenAccOnLineCheck2Pivs";
	private static final String SIAORGIDT = "siaOrgidt";
	private final static String SIACHANNELFLAG = "siaChannelFlag";
	private final static String IDNO = "idNo";
	private final static String IDTYPE = "idType";
	private final static String NAME = "name";
	private final static String PICVER = "picVer";
	private final static String PICID = "picId";
	/**
	 * 网点机构号 String X(5) 必输
	 */
	public String siaOrgidt;
	/**
	 * 渠道标识 X(7) Y 渠道标识 Bancslink：0000001 网银：0000002 银医合作：0000003 自助发卡：0000004
	 * IC卡POS渠道：0000007 财富管理：0000005 POS渠道：0000008 VTM渠道： 0000009
	 */
	public String siaChannelFlag;
	/**
	 * 证件号 X(32) 必填 后补空格
	 */
	public String idNo;
	/**
	 * 证件类型 X (2) 必填 后补空格
	 */
	public String idType;
	/**
	 * 客户姓名 X(160) 必填 后补空格
	 */
	public String name;
	/**
	 * 版本号 X(32) 必填 后补空格
	 */
	public String picVer;
	/**
	 * 影像ID X (32) 必填 后补空格
	 */
	public String picId;

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(SIAORGIDT, siaOrgidt);
		jsonObject.put(SIACHANNELFLAG, siaChannelFlag);
		jsonObject.put(IDNO, idNo);
		jsonObject.put(IDTYPE, idType);
		jsonObject.put(NAME, name);
		jsonObject.put(PICVER, picVer);
		jsonObject.put(PICID, picId);
		return jsonObject.toString();
	}
}
