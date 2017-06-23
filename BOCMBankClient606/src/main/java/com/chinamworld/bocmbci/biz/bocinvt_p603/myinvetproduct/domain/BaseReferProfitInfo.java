package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 参考收益汇总信息信息基类
 * 
 * @author HVZHUNG
 *
 */
public class BaseReferProfitInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 产品信息 */
	// public BOCProductForHoldingInfo productInfo;
	/** 产品名称 */
	public String prodName;
	/** 产品到期日 */
	public String edate;
	/** 产品币种 */
	public String procur;

	public BaseReferProfitInfo(Map<String, Object> map) {
		/** 产品名称 */
		prodName = (String) map.get("prodName");
		/** 产品到期日 */
		edate = (String) map.get("edate");
		/** 产品币种 */
		procur = (String) map.get("procur");
	}

	public BaseReferProfitInfo() {

	}

	public static BaseReferProfitInfo newInstance(Map<String, Object> map) {
		BaseReferProfitInfo instance = null;
		String kindStr = (String) map.get("kind");
		if ("0".equals(kindStr)) {
			instance = new ReferenceProfitForCashInfo(map);
		} else if ("1".equals(kindStr)) {
			instance = new ReferenceProfitForValueInfo(map);
		} else if ("2".equals(kindStr)) {
			instance = new ReferenceProfitForFixationInfo(map);
		} else {
			throw new IllegalAccessError("参数错误，没有相关字段");
		}
		return instance;
	}
}
