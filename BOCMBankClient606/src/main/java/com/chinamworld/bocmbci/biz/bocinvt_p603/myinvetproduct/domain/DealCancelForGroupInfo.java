package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 常规交易撤单确认信息
 * 
 * @author HVZHUNG
 *
 */
public class DealCancelForGroupInfo implements Serializable {

	public String dealId;
	public String productName;
	public String currency;
	public String cashRemit;
	public BigDecimal amount;
	public BigDecimal quantity;
}
