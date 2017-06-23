package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class BaseReferenceProfitDetailInfo implements Serializable {

	/** 产品名称 String */
	public String proname;
	/** 参考收益 Bigdicmal */
	public BigDecimal payprofit;
	/** 保留字段 String */
	public String extfield;

}
