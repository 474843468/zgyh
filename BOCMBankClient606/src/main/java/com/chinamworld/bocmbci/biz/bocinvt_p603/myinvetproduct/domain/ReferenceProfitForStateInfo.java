package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;


/**
 * 参考收益收益状况信息
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForStateInfo extends BaseReferProfitInfo {

	private static final long serialVersionUID = 1L;
	public String unpayprofit;
	public String payprofit;

	public ReferenceProfitForStateInfo(
			ReferenceProfitForFixationInfo fixationInfo) {
		this.prodName = fixationInfo.prodName;
		this.edate = fixationInfo.edate;
		this.procur = fixationInfo.procur;
		this.unpayprofit = fixationInfo.unpayprofit;
		this.payprofit = fixationInfo.payprofit;
	}

	public ReferenceProfitForStateInfo(ReferenceProfitForCashInfo cashInfo) {
		this.prodName = cashInfo.prodName;
		this.edate = cashInfo.edate;
		this.procur = cashInfo.procur;
		this.unpayprofit = cashInfo.unpayprofit;
		this.payprofit = cashInfo.payprofit;
	}
}
