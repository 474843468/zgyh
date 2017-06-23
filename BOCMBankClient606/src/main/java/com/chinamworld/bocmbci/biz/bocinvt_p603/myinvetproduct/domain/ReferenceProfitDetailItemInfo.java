package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.math.BigDecimal;
import java.util.Map;

import android.text.TextUtils;

/**
 * 业绩基准及日积月累产品的收益详情信息
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitDetailItemInfo extends BaseReferenceProfitDetailInfo{

	private static final long serialVersionUID = 1L;
	/** 计息开始 */
	public String intsdate;
	/** 计息截止 */
	public String intedate;
	/** 计息周期 */
	public String interestDate;
	/** 付息状态 */
	public String payflag;
	public ReferenceProfitDetailItemInfo(Map<String, Object> mapInfo) {
		this.proname = (String) mapInfo.get("proname");
		String payprofitStr = (String) mapInfo.get("payprofit");
		if (!TextUtils.isEmpty(payprofitStr))
			this.payprofit = new BigDecimal(payprofitStr);
		this.payflag = (String) mapInfo.get("payflag");
		this.intsdate = (String) mapInfo.get("intsdate");
		this.intedate = (String) mapInfo.get("intedate");
		interestDate = intsdate + "-" + intedate;
	}

}
