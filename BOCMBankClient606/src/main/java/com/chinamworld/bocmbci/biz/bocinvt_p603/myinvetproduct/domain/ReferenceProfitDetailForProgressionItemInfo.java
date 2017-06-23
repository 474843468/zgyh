package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.math.BigDecimal;
import java.util.Map;

import android.text.TextUtils;

/**
 * 收益累进型产品的收益详情信息
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitDetailForProgressionItemInfo extends
		BaseReferenceProfitDetailInfo {

	private static final long serialVersionUID = 1L;
	/** 起息日期 String Yyyy/MM/dd */
	public String startdate;
	/** 持有份额 Bigdicmal */
	public BigDecimal balunit;
	/** 持有天数 String */
	public String baldays;
	/** 预计年收益率 Bigdicmal */
	public BigDecimal exyield;
	/** 下一档收益率剩余天数 Bigdicmal */
	public String nextdays;

	public ReferenceProfitDetailForProgressionItemInfo(Map<String, Object> mapInfo) {
		this.startdate = (String) mapInfo.get("startdate");
		String balunitStr = (String) mapInfo.get("balunit");
		if (!TextUtils.isEmpty(balunitStr))
			this.balunit = new BigDecimal(balunitStr);
		this.baldays = (String) mapInfo.get("baldays");
		String exyieldStr = (String) mapInfo.get("exyield");
		if (!TextUtils.isEmpty(exyieldStr))
			this.exyield = new BigDecimal(exyieldStr);
		this.nextdays = (String) mapInfo.get("nextdays");
		String payprofitStr = (String) mapInfo.get("payprofit");
		if (!TextUtils.isEmpty(payprofitStr))
			this.payprofit = new BigDecimal(payprofitStr);
	}
}
