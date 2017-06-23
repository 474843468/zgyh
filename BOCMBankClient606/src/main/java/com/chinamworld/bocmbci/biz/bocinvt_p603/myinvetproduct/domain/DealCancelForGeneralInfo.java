package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 常规交易撤单确认信息
 * 
 * @author HVZHUNG
 *
 */
public class DealCancelForGeneralInfo implements Serializable {

	public String dealId;
	public String dealDate;
	public String productName;
	public String currency;
	public String dealType;
	public BigDecimal amount;
	public BigDecimal quantity;
	public String account;
	public String estimateDate;
}
