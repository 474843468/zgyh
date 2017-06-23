package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 中银理财产品的持仓信息
 * 
 * @author HVZHUNG
 *
 */
public class BOCProductForHoldingInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 产品代码 */
	public String prodCode;
	/** 资金账号 */
	public String bancAccount;
	/** 客户理财账户 */
	public String xpadAccount;
	/** 产品名称 */
	public String prodName;
	/**
	 * 产品币种 <br/>
	 * 001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元
	 */
	public BOCCurrency curCode;
	/** 预计年收益率 */
	public String yearlyRR;
	/** 产品起息日期 */
	public String prodBegin;
	/**
	 * 产品到期日<br/>
	 * 无限开放式产品返回“-1”表示“无限期”
	 */
	public String prodEnd;
	/** 持有份额 */
	public String holdingQuantity;

	/** 可用份额 */
	public String availableQuantity;

	/** 是否可赎回 */
	public String canRedeem;
	/** 是否允许部分赎回 */
	public String canPartlyRedeem;
	/** 是否可修改分红方式 */
	public String canChangeBonusMode;
	/** 当前分红方式 */
	public String currentBonusMode;
	/** 最低持有份额 */
	public String lowestHoldQuantity;
	/** 赎回起点金额 */
	public String redeemStartingAmount;

	/** 汇钞标识人民币类型 */
	public static final String CASH_REMIT_RMB = "00";
	/** 汇钞标识-钞 */
	public static final String CASH_REMIT_CASH = "01";
	/** 汇钞标识-汇 */
	public static final String CASH_REMIT_REMIT = "02";
	/**
	 * 会钞标识<br/>
	 * 1：钞 2：汇 0：人民币
	 * */
	public String cashRemit;
	/**
	 * 
	 * 是否收益累计产品<br/>
	 * 0：否 1：是
	 * 
	 * 
	 * */
	public String progressionflag;
	/** 赎回价格 */
	public String sellPrice;
	/** 现金管理型 */
	public static final String PROD_TYPE_CASH = "1";
	/** 净值型 */
	public static final String PROD_TYPE_VALUE = "2";
	/** 固定期限 */
	public static final String PROD_TYPE_FIXATION = "3";
	/**
	 * 产品类型 1:现金管理理财产品 2:净值开放理财产品 3：固定期限
	 */
	public String issueType;
	/**
	 * 产品性质 <br/>
	 * 0:结构性理财产品 1:类基金理财产品
	 * 
	 * 
	 * 
	 * */
	public String productKind;
	/** 参考收益 */
	public String expProfit;
	/** 单位净值 */
	public String price;
	/** 净值日期 */
	public String priceDate;
	/** 参考市值 */
	public String expAmt;
	/**
	 * 产品期限特性<br/>
	 * 00-有限期封闭式 01-有限期半开放式 02-周期连续 03-周期不连续 04-无限开放式 05-春夏秋冬 06-其它
	 * 
	 * */
	public String termType;
	/** 是否可追加购买 */
	public boolean canAddBuy;

	/** standardPro 值为此时，当前产品为非业绩基准型产品 */
	public static final String STANDARD_NO = "0";
	/** 业绩基准型产品 p603更改为 业绩基准-锁定期转低收益 */
	public static final String STANDARD_YES_1 = "1";
	/** 业绩基准型产品 p603新增 业绩基准-锁定期后入账 */
	public static final String STANDARD_YES_2 = "2";
	/** 业绩基准型产品 p603新增 业绩基准-锁定期周期滚续 */
	public static final String STANDARD_YES_3 = "3";
	/**
	 * 是否为业绩基准产品<br/>
	 * 0：否 1：是 业绩基准产品允许查看份额明细
	 * 
	 * */
	public String standardPro;
	/** 是否可投资协议管理 */
	public String canAgreementMange;
	/**
	 * 产品期限 <br/>
	 * 固定期限类产品有效
	 */
	public String productTerm;
	/**
	 * 是否可指定日期赎回 <br/>
	 * 0：否 1：是
	 */
	public String allowAssignDate;
	/** 账号缓存标识 */
	public String accountKey;
	/** 份额面值 */
	public String shareValue;

	/** 现金管理类产品-日积月累型 */
	public static final int CASH_PRO_TYPE_MULTIPLYING = 0;
	/** 现金管理类产品-收益累进型 */
	public static final int CASH_PRO_TYPE_PROGRESSION = 1;
	/** 现金管理类产品-与时聚金 */
	public static final int CASH_PRO_TYPE_GATHER = 2;
	/**
	 * 现金管理类产品类型<br/>
	 * 产品为现金管理类产品时有效<br/>
	 * 0：日积月累 1：收益累进 2：与时聚金
	 */
	public String cashProType = "-1";

	/** 交易手续费 p603 */
	public String charge;
	/** 业绩报酬，浮动管理费 p603 */
	public String reward;
	/** 是否允许撤单 p603 */
	public String enableRevoke;

	/** 资金账号缓存标识 */
	public String bancAccountKey;
	/** 当前期数 */
	public String currPeriod;
	/** 总期数 */
	public String totalPeriod;

	/** 预计年收益率最大值 */
	public String yearlyRRMax;

	/** 业绩型 持仓交易流水号   */
	public String tranSeq;


	/**
	 * 转换份额
	 */
	public String transferUnit;
	
	
	/**
	 * psnXpadShareTransitionCommit  接口返回数据
	 */
	public Map<String,Object>  psnXpadShareTransitionCommitResponseMap;
	
	/**
	 * PsnXpadShareTransitionVerify  接口返回数据
	 */
	public Map<String,Object>  PsnXpadShareTransitionVerifyResponseMap;

	@Override
	public String toString() {
		return "BOCProductInfo [prodCode=" + prodCode + ", bancAccount="
				+ bancAccount + ", xpadAccount=" + xpadAccount + ", prodName="
				+ prodName + ", curCode=" + curCode + ", yearlyRR=" + yearlyRR
				+ ", prodBegin=" + prodBegin + ", prodEnd=" + prodEnd
				+ ", holdingQuantity=" + holdingQuantity
				+ ", availableQuantity=" + availableQuantity + ", canRedeem="
				+ canRedeem + ", canPartlyRedeem=" + canPartlyRedeem
				+ ", canChangeBonusMode=" + canChangeBonusMode
				+ ", currentBonusMode=" + currentBonusMode
				+ ", lowestHoldQuantity=" + lowestHoldQuantity
				+ ", redeemStartingAmount=" + redeemStartingAmount
				+ ", cashRemit=" + cashRemit + ", progressionflag="
				+ progressionflag + ", sellPrice=" + sellPrice
				+ ", productKind=" + productKind + ", expProfit=" + expProfit
				+ ", price=" + price + ", priceDate=" + priceDate + ", expAmt="
				+ expAmt + ", termType=" + termType + ", canAddBuy="
				+ canAddBuy + ", standardPro=" + standardPro
				+ ", canAgreementMange=" + canAgreementMange + ", productTerm="
				+ productTerm + ", allowAssignDate=" + allowAssignDate
				+ ", accountKey=" + accountKey + ", shareValue=" + shareValue
				+ ", cashProType=" + cashProType + "]";
	}
}
