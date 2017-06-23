package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.util.Map;

/**
 * 固定期限型产品参考收益
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForFixationInfo extends BaseReferProfitInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 收益日期 */
	public String edate;
	/** 产品性质 */
	public String kind;
	/** 持有份额 */
//	public String holdQuantity;
	/** 总收益 */
	public String totalprofit;
	/** 未到账收益 */
	public String unpayprofit;
	/** 已到账收益 */
	public String payprofit;

	public ReferenceProfitForFixationInfo(Map<String, Object> map) {
		super(map);
		edate = (String) map.get("edate");
		kind = (String) map.get("kind");
//		holdQuantity = (String) map.get("holdQuantity");
		totalprofit = (String) map.get("totalprofit");
		unpayprofit = (String) map.get("unpayprofit");
		payprofit = (String) map.get("payprofit");
		prodName = (String) map.get("proname");
	}
}
