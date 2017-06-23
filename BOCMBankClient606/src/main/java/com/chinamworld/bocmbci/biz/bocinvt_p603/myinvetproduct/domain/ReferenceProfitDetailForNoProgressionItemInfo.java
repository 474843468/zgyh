package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;


/**
 * 非收益累计产品收益详情
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitDetailForNoProgressionItemInfo extends
		BaseReferenceProfitDetailInfo {

	private static final long serialVersionUID = 1L;
	/** 计息开始 String Yyyy/MM/dd */
	public String intsdate;
	/** 计息截止 String Yyyy/MM/dd */
	public String intedate;
	/** 付息状态 String */
	public String payflag;

}
