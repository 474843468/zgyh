package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model;

import java.io.Serializable;

/**
 * I42-4.40 040产品详情查询封装结果
 * Created by Wan mengxin on 2016/10/26.
 */
public class PsnXpadProductDetailQueryResModel implements Serializable{

    //产品性质：0-结构性理财产品
    /***
     * 是否允许自助终端销售	String	0--否 1--是
     */
    private String sellAutoBanc;
    /**
     * 是否允许电话银行人工发布	String	0--否 1--是
     */
    private String publishByPeple;
    /**
     * 是否允许家居银行发布	String	0--否 1--是
     */
    private String publishHomeBanc;
    /**
     * 本金返还方式	String	0：实时返还
     * 1：T+N返还
     * 2：期末返还
     */
    private String redPaymentMode;
    /**
     * 剩余额度	BigDecimal
     */
    private double availamt;
    /**
     * 预计年收益率（%）	String
     */
    private String yearlyRR;
    /**
     * 产品到期日	String
     */
    private String prodEnd;
    /**
     * 付息频率	String	*D：天
     * W：周
     * M：月
     * Y：年
     * 表示代码数字
     * -1表示到期付息
     */
    private String couponpayFreq;
    /**
     * prodTimeLimit	产品期限	String	当为业绩基准产品时该字段有效；
     * <p>
     * 当为非业绩基准产品，且“产品期限特性”不是“无限开放式”时，该字段有效；
     * <p>
     * 其余情况该字段无效，请统一展示无固定期限
     */
    private String prodTimeLimit;
    /**
     * 是否允许电话银行自助发布	String	0--否 1--是
     */
    private String publishTelphone;
    /**
     * 产品起息日	String
     */
    private String prodBegin;
    /**
     * 购买周期开始	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日
     * 频率1M（每月）、1S(每季度)为数字【1…31】代表
     * *号……**号
     */
    private String bidPeriodStartDate;
    /**
     * 是否允许自助终端发布	String	0--否 1--是
     */
    private String publishAutoBanc;
    /**
     * 赎回起点份额	String
     */
    private String lowLimitAmount;
    /**
     * 产品名称	String
     */
    private String prodName;
    /**
     * 是否允许网上发布	String	0--否 1--是
     */
    private String publishOnline;
    /**
     * 产品期限特性	String	0：有限期封闭式
     * 1：有限半开放式
     * 2：周期性
     * 3：无限开放式
     * 4：春夏秋冬
     */
    private String productTermType;
    /**
     * 是否允许家居银行销售	String	0--否 1--是
     */
    private String sellHomeBanc;
    /**
     * 预计年收益率（%）(最大值)	String	不显示“%”，需要前端处理
     */
    private String rateDetail;
    /**
     * 追加认申购起点金额	String
     */
    private String addAmount;
    /**
     * 收益返还方式	String	1：T+N返还
     * 2：期末返还
     */
    private String profitMode;
    /**
     * 是否允许指定日期赎回	String	0：否, 1：是
     */
    private String appdatered;
    /**
     * 购买基数	String
     */
    private String baseAmount;
    /**
     * 产品性质	String	0:结构性理财产品
     * 1:类基金理财产品
     */
    private String productKind;
    /**
     * 销售起始日期	String
     */
    private String sellingStartingDate;
    /**
     * 是否允许短信指令交易	String	0-否 1-是
     */
    private String isSMS;
    /**
     * 收益返还T+N(天数)	String
     */
    private String profitDate;
    /**
     * 收益到帐日	String
     */
    private String interestDate;
    /**
     * 是否为业绩基准产品	String	0：非业绩基准产品
     * 1：业绩基准-锁定期转低收益
     * 2：业绩基准-锁定期后入账
     * 3：业绩基准-锁定期周期滚续
     */
    private String isLockPeriod;
    /**
     * 产品销售状态	String	1：在售产品、
     * 2：停售产品
     */
    private String status;
    /**
     * 到期本金付款延迟天数	String
     */
    private String datesPaymentOffset;
    /**
     * 是否允许挂单销售	String	0：不允许
     * 1：允许
     */
    private String outTimeOrder;
    /**
     * 产品适用的客户等级	String	0：普通客户及以上
     * 1：中银理财及以上
     * 2：财富管理及以上
     * 3：私人银行及以上
     */
    private String custLevelSale;
    /**
     * 是否允许微信银行发布	String	0--否 1--是
     */
    private String publishWeChat;
    /**
     * 产品风险级别	String	0：低风险产品
     * 1：中低风险产品
     * 2：中等风险产品
     * 3：中高风险产品
     * 4：高风险产品
     */
    private String prodRisklvl;
    /**
     * 购买开始日期	String	yyyyMMdd
     */
    private String bidStartDate;
    /**
     * 赎回开放规则	String	00：不允许赎回
     * 01：开放期赎回
     * 02：付息日赎回
     * 03：起息后每日赎回
     * 04：周期开放赎回
     */
    private String sellType;
    /**
     * 销售结束日期	String
     */
    private String sellingEndingDate;
    /**
     * 是否允许柜台	String	0--否 1--是
     */
    private String isBancs;
    /**
     * 赎回本金收益到账日	String
     */
    private String redPayDate;
    /**
     * 是否允许电话银行人工销售	String	0--否 1--是
     */
    private String sellTelByPeple;
    /**
     * 认购起点金额	String
     */
    private String subAmount;
    /**
     * 最低持有份额	String
     */
    private String limitHoldBalance;
    /**
     * 赎回周期开始	String
     */
    private String redEmperiodStart;
    /**
     * 购买周期结束	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日
     * 频率1M（每月）、1S(每季度)为数字【1…31】代表
     * *号……**号
     */
    private String bidPeriodEndDate;
    /**
     * 是否允许手机银行销售	String	0--否 1--是
     */
    private String sellMobile;
    /**
     * 产品工作开始时间	String	HH:mm:ss
     */
    private String startTime;
    /**
     * 挂单结束时间	String	HH:mm:ss
     */
    private String orderEndTime;
    /**
     * 产品币种	String	001：人民币元
     * 014：美元
     * 012：英镑
     * 013：港币
     * 028: 加拿大元
     * 029：澳元
     * 038：欧元
     * 027：日元
     */
    private String curCode;
    /**
     * 产品风险类别	String	1：保本固定收益、
     * 2：保本浮动收益、
     * 3：非保本浮动收益
     */
    private String prodRiskType;
    /**
     * 赎回结束日期	String	yyyyMMdd
     */
    private String redEmptionEndDate;
    /**
     * 产品类型	String	1：现金管理类产品
     * 2：净值开放类产品
     * 3：固定期限产品
     */
    private String productType;
    /**
     * 购买价格	BigDecimal
     */
    private String buyPrice;
    /**
     * 认购/申购撤单设置	String	0：不允许撤单
     * 1：只允许当日撤单
     * 2：允许撤单
     */
    private String isCanCancle;
    /**
     * 是否允许赎回申请	String	0：不允许
     * 1：允许
     */
    private String isRedask;
    /**
     * 是否允许电话银行自助销售	String	0--否 1--是
     */
    private String sellTelphone;
    /**
     * 购买结束日期	String	yyyy/mm/dd
     */
    private String bidEndDate;
    /**
     * 赎回开始日期	String	yyyyMMdd
     */
    private String redEmptionStartDate;
    /**
     * 赎回周期频率	String	D：天
     * W：周
     * M：月
     */
    private String redEmperiodfReq;
    /**
     * 允许节假日赎回	String	0：不允许
     * 1：允许
     */
    private String redEmptionHoliday;
    /**
     * 是否允许网上销售	String	0--否 1--是
     */
    private String sellOnline;
    /**
     * 本金返还T+N(天数) 	String
     */
    private String redPaymentDate;
    /**
     * 允许节假日购买	String	0：不允许
     * 1：允许
     */
    private String bidHoliday;
    /**
     * 赎回周期结束	String
     */
    private String redEmperiodEnd;
    /**
     * 是否允许手机银行发布	String	0--否 1--是
     */
    private String publishMobile;
    /**
     * 产品最大续约期数	String
     */
    private String maxPeriod;
    /**
     * 产品代码	String
     */
    private String prodCode;
    /**
     * 购买周期频率	String 	D：天
     * W：周
     * M：月
     */
    private String bidPeriodMode;
    /**
     * 节假日调整方式	String	0：unAdjust
     * 1：Following
     * 2：Modify following
     * 3：Forward
     * 4：Modify forward
     */
    private String dateModeType;
    /**
     * 是否周期性产品	String	0：否 ;1：是
     */
    private String periodical;
    /**
     * 是否允许微信银行销售	String	0--否 1--是
     */
    private String sellWeChat;
    /**
     * 购买开放规则	String	00：关闭
     * 01：开放期购买
     * 02：周期开放购买
     * 03:：起息后每日申购
     */
    private String buyType;
    /**
     * 产品工作结束时间	String	HH:mm:ss
     */
    private String endTime;
    /**
     * 本金到帐日	String
     */
    private String paymentDate;
    /**
     * 适用对象	String	0：有投资经验
     * 1：无投资经验
     */
    private String applyObj;
    /**
     * 购买交易类型	String	0：认购
     * 1：申购
     */
    private String transTypeCode;
    /**
     * 是否收益累计产品	String	0：否
     * 1：是
     */
    private String progressionflag;
    /**
     * 挂单开始时间	String	HH:mm:ss
     */
    private String orderStartTime;
    /**
     * 赎回申请提前天数	String	赎回开放期前n天
     */
    private String redaskDay;


//    产品性质：1-类基金理财产品
    /**
     * 收费方式{0:前端收费外扣法一
     * 1:前端收费外扣法二
     * 2:前端收费内扣法一
     * 3:前端收费内扣法二
     * 4:后端收费
     * }
     */
    private String feeMode;
    /**
     * 募集配售方式{0:先到先得
     * 1:全程比例配售
     * 2:末日比例配售
     * }
     */
    private String collectDistributeMode;
    /**
     * 业绩费提取比例
     */
    private String pfmcDrawScale;
    /**
     * 业绩费提取起点收益率
     */
    private String pfmcDrawStart;
    /**
     * 类基金挂单时间{yyyyMMdd}
     */
    private String fundOrderTime;
    /**
     * 转换手续费收取方式{0:收取手续费
     * 1:不收取手续费
     * }
     */
    private String changeFeeMode;
    /**
     * 是否允许产品转换{0:不允许
     * 1:允许
     * }
     */
    private String changePermit;
    /**
     * 份额成立T+N(天数)
     */
    private String balexecDays;
    /**
     * 不能转入产品ID
     */
    private String changeFromIssueid;
    /**
     * 销售人数上限{-1表示不设置销售人数上限}
     */
    private String sellNumMax;
    /**
     * 挂单交易单笔限额
     */
    private String ordSingleMax;
    /**
     * 挂单交易总限额
     */
    private String ordTotalMax;
    /**
     * 单日申购上限
     */
    private String dayPurchMax;
    /**
     * 单日单人申购上限
     */
    private String dayPerPurchMax;
    /**
     * 挂单赎回单笔限额
     */
    private String ordredSingleMax;
    /***
     * 挂单赎回总限额
     */
    private String ordredTotalMax;
    /***
     * 最新净值
     */
    private String price;
    /***
     * 净值日期{yyyyMMdd，针对净值型产品显示具体净值报价日期，其他产品均显示“-”表示不适用。}
     */
    private String priceDate;
    /**
     * 认购手续费{未维护手续费返回为空
     * <p>
     * 采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
     * 后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
     * }
     */
    private String subscribeFee;
    /**
     * 申购手续费{未维护手续费返回为空
     * 采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
     * 后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
     * }
     */
    private String purchFee;
    /**
     * 赎回手续费{未维护手续费返回为空
     * 采用“|”进行手续费区间分割，按持有区间升序排列，持有区间方式有：按天、按月、按年三种方式，一款产品只能有一种方式；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
     * 或3月(含)-10月，3%|10月(含)-35月，450.00|35月(含)以上，6%
     * 或
     * 3年(含)-10年，3%|10年(含)-35年，450.00|35年(含)以上，6%
     * <p>
     * }
     */
    private String redeemFee;


    /**
     * 业绩费提取比例{当产品性质为：1:类基金理财产品  时返回该字段}
     */
    private String pfmcdrawScale;
    /**
     * 业绩费提取起点收益率{当产品性质为：1:类基金理财产品  时返回该字段}
     */
    private String pfmcdrawStart;
    /**
     * 产品销售状态{1：在售产品、
     * 2：停售产品
     * }
     */
    private String Status;
    /**
     * 最低持有份额
     */
    private String LimitHoldBalance;

    public String getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(String feeMode) {
        this.feeMode = feeMode;
    }

    public String getCollectDistributeMode() {
        return collectDistributeMode;
    }

    public void setCollectDistributeMode(String collectDistributeMode) {
        this.collectDistributeMode = collectDistributeMode;
    }

    public String getPfmcDrawScale() {
        return pfmcDrawScale;
    }

    public void setPfmcDrawScale(String pfmcDrawScale) {
        this.pfmcDrawScale = pfmcDrawScale;
    }

    public String getPfmcDrawStart() {
        return pfmcDrawStart;
    }

    public void setPfmcDrawStart(String pfmcDrawStart) {
        this.pfmcDrawStart = pfmcDrawStart;
    }

    public String getFundOrderTime() {
        return fundOrderTime;
    }

    public void setFundOrderTime(String fundOrderTime) {
        this.fundOrderTime = fundOrderTime;
    }

    public String getChangeFeeMode() {
        return changeFeeMode;
    }

    public void setChangeFeeMode(String changeFeeMode) {
        this.changeFeeMode = changeFeeMode;
    }

    public String getChangePermit() {
        return changePermit;
    }

    public void setChangePermit(String changePermit) {
        this.changePermit = changePermit;
    }

    public String getBalexecDays() {
        return balexecDays;
    }

    public void setBalexecDays(String balexecDays) {
        this.balexecDays = balexecDays;
    }

    public String getChangeFromIssueid() {
        return changeFromIssueid;
    }

    public void setChangeFromIssueid(String changeFromIssueid) {
        this.changeFromIssueid = changeFromIssueid;
    }

    public String getSellNumMax() {
        return sellNumMax;
    }

    public void setSellNumMax(String sellNumMax) {
        this.sellNumMax = sellNumMax;
    }

    public String getOrdSingleMax() {
        return ordSingleMax;
    }

    public void setOrdSingleMax(String ordSingleMax) {
        this.ordSingleMax = ordSingleMax;
    }

    public String getOrdTotalMax() {
        return ordTotalMax;
    }

    public void setOrdTotalMax(String ordTotalMax) {
        this.ordTotalMax = ordTotalMax;
    }

    public String getDayPurchMax() {
        return dayPurchMax;
    }

    public void setDayPurchMax(String dayPurchMax) {
        this.dayPurchMax = dayPurchMax;
    }

    public String getDayPerPurchMax() {
        return dayPerPurchMax;
    }

    public void setDayPerPurchMax(String dayPerPurchMax) {
        this.dayPerPurchMax = dayPerPurchMax;
    }

    public String getOrdredSingleMax() {
        return ordredSingleMax;
    }

    public void setOrdredSingleMax(String ordredSingleMax) {
        this.ordredSingleMax = ordredSingleMax;
    }

    public String getOrdredTotalMax() {
        return ordredTotalMax;
    }

    public void setOrdredTotalMax(String ordredTotalMax) {
        this.ordredTotalMax = ordredTotalMax;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(String priceDate) {
        this.priceDate = priceDate;
    }

    public String getSubscribeFee() {
        return subscribeFee;
    }

    public void setSubscribeFee(String subscribeFee) {
        this.subscribeFee = subscribeFee;
    }

    public String getPurchFee() {
        return purchFee;
    }

    public void setPurchFee(String purchFee) {
        this.purchFee = purchFee;
    }

    public String getRedeemFee() {
        return redeemFee;
    }

    public void setRedeemFee(String redeemFee) {
        this.redeemFee = redeemFee;
    }

    public String getPfmcdrawScale() {
        return pfmcdrawScale;
    }

    public void setPfmcdrawScale(String pfmcdrawScale) {
        this.pfmcdrawScale = pfmcdrawScale;
    }

    public String getPfmcdrawStart() {
        return pfmcdrawStart;
    }

    public void setPfmcdrawStart(String pfmcdrawStart) {
        this.pfmcdrawStart = pfmcdrawStart;
    }


//  =============================set()==方法段落=============

    /***
     * 是否允许自助终端销售	String	0--否 1--是
     */
    public void setSellAutoBanc(String sellAutoBanc) {
        this.sellAutoBanc = sellAutoBanc;
    }

    public void setPublishByPeple(String publishByPeple) {
        this.publishByPeple = publishByPeple;
    }

    public void setPublishHomeBanc(String publishHomeBanc) {
        this.publishHomeBanc = publishHomeBanc;
    }

    public void setRedPaymentMode(String redPaymentMode) {
        this.redPaymentMode = redPaymentMode;
    }

    public void setAvailamt(double availamt) {
        this.availamt = availamt;
    }

    public void setYearlyRR(String yearlyRR) {
        this.yearlyRR = yearlyRR;
    }

    public void setProdEnd(String prodEnd) {
        this.prodEnd = prodEnd;
    }

    public void setCouponpayFreq(String couponpayFreq) {
        this.couponpayFreq = couponpayFreq;
    }

    public void setProdTimeLimit(String prodTimeLimit) {
        this.prodTimeLimit = prodTimeLimit;
    }

    public void setPublishTelphone(String publishTelphone) {
        this.publishTelphone = publishTelphone;
    }

    public void setProdBegin(String prodBegin) {
        this.prodBegin = prodBegin;
    }

    public void setBidPeriodStartDate(String bidPeriodStartDate) {
        this.bidPeriodStartDate = bidPeriodStartDate;
    }

    public void setPublishAutoBanc(String publishAutoBanc) {
        this.publishAutoBanc = publishAutoBanc;
    }

    public void setLowLimitAmount(String lowLimitAmount) {
        this.lowLimitAmount = lowLimitAmount;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setPublishOnline(String publishOnline) {
        this.publishOnline = publishOnline;
    }

    public void setProductTermType(String productTermType) {
        this.productTermType = productTermType;
    }

    public void setSellHomeBanc(String sellHomeBanc) {
        this.sellHomeBanc = sellHomeBanc;
    }

    public void setRateDetail(String rateDetail) {
        this.rateDetail = rateDetail;
    }

    public void setAddAmount(String addAmount) {
        this.addAmount = addAmount;
    }

    public void setProfitMode(String profitMode) {
        this.profitMode = profitMode;
    }

    public void setAppdatered(String appdatered) {
        this.appdatered = appdatered;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public void setSellingStartingDate(String sellingStartingDate) {
        this.sellingStartingDate = sellingStartingDate;
    }

    public void setIsSMS(String isSMS) {
        this.isSMS = isSMS;
    }

    public void setProfitDate(String profitDate) {
        this.profitDate = profitDate;
    }

    public void setInterestDate(String interestDate) {
        this.interestDate = interestDate;
    }

    public void setIsLockPeriod(String isLockPeriod) {
        this.isLockPeriod = isLockPeriod;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDatesPaymentOffset(String datesPaymentOffset) {
        this.datesPaymentOffset = datesPaymentOffset;
    }

    public void setOutTimeOrder(String outTimeOrder) {
        this.outTimeOrder = outTimeOrder;
    }

    public void setCustLevelSale(String custLevelSale) {
        this.custLevelSale = custLevelSale;
    }

    public void setPublishWeChat(String publishWeChat) {
        this.publishWeChat = publishWeChat;
    }

    public void setProdRisklvl(String prodRisklvl) {
        this.prodRisklvl = prodRisklvl;
    }

    public void setBidStartDate(String bidStartDate) {
        this.bidStartDate = bidStartDate;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public void setSellingEndingDate(String sellingEndingDate) {
        this.sellingEndingDate = sellingEndingDate;
    }

    public void setIsBancs(String isBancs) {
        this.isBancs = isBancs;
    }

    public void setRedPayDate(String redPayDate) {
        this.redPayDate = redPayDate;
    }

    public void setSellTelByPeple(String sellTelByPeple) {
        this.sellTelByPeple = sellTelByPeple;
    }

    public void setSubAmount(String subAmount) {
        this.subAmount = subAmount;
    }

    public void setLimitHoldBalance(String limitHoldBalance) {
        this.limitHoldBalance = limitHoldBalance;
    }

    public void setRedEmperiodStart(String redEmperiodStart) {
        this.redEmperiodStart = redEmperiodStart;
    }

    public void setBidPeriodEndDate(String bidPeriodEndDate) {
        this.bidPeriodEndDate = bidPeriodEndDate;
    }

    public void setSellMobile(String sellMobile) {
        this.sellMobile = sellMobile;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setOrderEndTime(String orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public void setProdRiskType(String prodRiskType) {
        this.prodRiskType = prodRiskType;
    }

    public void setRedEmptionEndDate(String redEmptionEndDate) {
        this.redEmptionEndDate = redEmptionEndDate;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setIsCanCancle(String isCanCancle) {
        this.isCanCancle = isCanCancle;
    }

    public void setIsRedask(String isRedask) {
        this.isRedask = isRedask;
    }

    public void setSellTelphone(String sellTelphone) {
        this.sellTelphone = sellTelphone;
    }

    public void setBidEndDate(String bidEndDate) {
        this.bidEndDate = bidEndDate;
    }

    public void setRedEmptionStartDate(String redEmptionStartDate) {
        this.redEmptionStartDate = redEmptionStartDate;
    }

    public void setRedEmperiodfReq(String redEmperiodfReq) {
        this.redEmperiodfReq = redEmperiodfReq;
    }

    public void setRedEmptionHoliday(String redEmptionHoliday) {
        this.redEmptionHoliday = redEmptionHoliday;
    }

    public void setSellOnline(String sellOnline) {
        this.sellOnline = sellOnline;
    }

    public void setRedPaymentDate(String redPaymentDate) {
        this.redPaymentDate = redPaymentDate;
    }

    public void setBidHoliday(String bidHoliday) {
        this.bidHoliday = bidHoliday;
    }

    public void setRedEmperiodEnd(String redEmperiodEnd) {
        this.redEmperiodEnd = redEmperiodEnd;
    }

    public void setPublishMobile(String publishMobile) {
        this.publishMobile = publishMobile;
    }

    public void setMaxPeriod(String maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setBidPeriodMode(String bidPeriodMode) {
        this.bidPeriodMode = bidPeriodMode;
    }

    public void setDateModeType(String dateModeType) {
        this.dateModeType = dateModeType;
    }

    public void setPeriodical(String periodical) {
        this.periodical = periodical;
    }

    public void setSellWeChat(String sellWeChat) {
        this.sellWeChat = sellWeChat;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setApplyObj(String applyObj) {
        this.applyObj = applyObj;
    }

    public void setTransTypeCode(String transTypeCode) {
        this.transTypeCode = transTypeCode;
    }

    public void setProgressionflag(String progressionflag) {
        this.progressionflag = progressionflag;
    }

    public void setOrderStartTime(String orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public void setRedaskDay(String redaskDay) {
        this.redaskDay = redaskDay;
    }
    //  =============================get()==方法段落=============

    /***
     * 是否允许自助终端销售	String	0--否 1--是
     */
    public String getSellAutoBanc() {
        return sellAutoBanc;
    }

    /**
     * 是否允许电话银行人工发布	String	0--否 1--是
     */
    public String getPublishByPeple() {
        return publishByPeple;
    }

    /**
     * 是否允许家居银行发布	String	0--否 1--是
     */
    public String getPublishHomeBanc() {
        return publishHomeBanc;
    }

    /**
     * 本金返还方式	String	0：实时返还
     * 1：T+N返还
     * 2：期末返还
     */
    public String getRedPaymentMode() {
        return redPaymentMode;
    }

    /**
     * 剩余额度	BigDecimal
     */
    public double getAvailamt() {
        return availamt;
    }

    /**
     * 预计年收益率（%）	String
     */
    public String getYearlyRR() {
        return yearlyRR;
    }

    /**
     * 产品到期日	String
     */
    public String getProdEnd() {
        return prodEnd;
    }

    /**
     * 付息频率	String	*D：天
     * W：周
     * M：月
     * Y：年
     * 表示代码数字
     * -1表示到期付息
     */
    public String getCouponpayFreq() {
        return couponpayFreq;
    }

    /**
     * prodTimeLimit	产品期限	String	当为业绩基准产品时该字段有效；
     * <p>
     * 当为非业绩基准产品，且“产品期限特性”不是“无限开放式”时，该字段有效；
     * <p>
     * 其余情况该字段无效，请统一展示无固定期限
     */
    public String getProdTimeLimit() {
        return prodTimeLimit;
    }

    /**
     * 是否允许电话银行自助发布	String	0--否 1--是
     */
    public String getPublishTelphone() {
        return publishTelphone;
    }

    /**
     * 产品起息日	String
     */
    public String getProdBegin() {
        return prodBegin;
    }

    /**
     * 购买周期开始	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日
     * 频率1M（每月）、1S(每季度)为数字【1…31】代表
     * *号……**号
     */
    public String getBidPeriodStartDate() {
        return bidPeriodStartDate;
    }

    /**
     * 是否允许自助终端发布	String	0--否 1--是
     */
    public String getPublishAutoBanc() {
        return publishAutoBanc;
    }

    /**
     * 赎回起点份额	String
     */
    public String getLowLimitAmount() {
        return lowLimitAmount;
    }

    /**
     * 产品名称	String
     */
    public String getProdName() {
        return prodName;
    }

    /**
     * 是否允许网上发布	String	0--否 1--是
     */
    public String getPublishOnline() {
        return publishOnline;
    }

    /**
     * 产品期限特性	String	0：有限期封闭式
     * 1：有限半开放式
     * 2：周期性
     * 3：无限开放式
     * 4：春夏秋冬
     */
    public String getProductTermType() {
        return productTermType;
    }

    /**
     * 是否允许家居银行销售	String	0--否 1--是
     */
    public String getSellHomeBanc() {
        return sellHomeBanc;
    }

    /**
     * 预计年收益率（%）(最大值)	String	不显示“%”，需要前端处理
     */
    public String getRateDetail() {
        return rateDetail;
    }

    /**
     * 追加认申购起点金额	String
     */
    public String getAddAmount() {
        return addAmount;
    }

    /**
     * 收益返还方式	String	1：T+N返还
     * 2：期末返还
     */
    public String getProfitMode() {
        return profitMode;
    }

    /**
     * 是否允许指定日期赎回	String	0：否, 1：是
     */
    public String getAppdatered() {
        return appdatered;
    }

    /**
     * 购买基数	String
     */
    public String getBaseAmount() {
        return baseAmount;
    }

    /**
     * 产品性质	String	0:结构性理财产品
     * 1:类基金理财产品
     */
    public String getProductKind() {
        return productKind;
    }

    /**
     * 销售起始日期	String
     */
    public String getSellingStartingDate() {
        return sellingStartingDate;
    }

    /**
     * 是否允许短信指令交易	String	0-否 1-是
     */
    public String getIsSMS() {
        return isSMS;
    }

    /**
     * 收益返还T+N(天数)	String
     */
    public String getProfitDate() {
        return profitDate;
    }

    /**
     * 收益到帐日	String
     */
    public String getInterestDate() {
        return interestDate;
    }

    /**
     * 是否为业绩基准产品	String	0：非业绩基准产品
     * 1：业绩基准-锁定期转低收益
     * 2：业绩基准-锁定期后入账
     * 3：业绩基准-锁定期周期滚续
     */
    public String getIsLockPeriod() {
        return isLockPeriod;
    }

    /**
     * 产品销售状态	String	1：在售产品、
     * 2：停售产品
     */
    public String getStatus() {
        return status;
    }

    /**
     * 到期本金付款延迟天数	String
     */
    public String getDatesPaymentOffset() {
        return datesPaymentOffset;
    }

    /**
     * 是否允许挂单销售	String	0：不允许
     * 1：允许
     */
    public String getOutTimeOrder() {
        return outTimeOrder;
    }

    /**
     * 产品适用的客户等级	String	0：普通客户及以上
     * 1：中银理财及以上
     * 2：财富管理及以上
     * 3：私人银行及以上
     */
    public String getCustLevelSale() {
        return custLevelSale;
    }

    /**
     * 是否允许微信银行发布	String	0--否 1--是
     */
    public String getPublishWeChat() {
        return publishWeChat;
    }

    /**
     * 产品风险级别	String	0：低风险产品
     * 1：中低风险产品
     * 2：中等风险产品
     * 3：中高风险产品
     * 4：高风险产品
     */
    public String getProdRisklvl() {
        return prodRisklvl;
    }

    /**
     * 购买开始日期	String	yyyyMMdd
     */
    public String getBidStartDate() {
        return bidStartDate;
    }

    /**
     * 赎回开放规则	String	00：不允许赎回
     * 01：开放期赎回
     * 02：付息日赎回
     * 03：起息后每日赎回
     * 04：周期开放赎回
     */
    public String getSellType() {
        return sellType;
    }

    /**
     * 销售结束日期	String
     */
    public String getSellingEndingDate() {
        return sellingEndingDate;
    }

    /**
     * 是否允许柜台	String	0--否 1--是
     */
    public String getIsBancs() {
        return isBancs;
    }

    /**
     * 赎回本金收益到账日	String
     */
    public String getRedPayDate() {
        return redPayDate;
    }

    /**
     * 是否允许电话银行人工销售	String	0--否 1--是
     */
    public String getSellTelByPeple() {
        return sellTelByPeple;
    }

    /**
     * 最低持有份额	String
     */
    public String getSubAmount() {
        return subAmount;
    }

    /**
     * 最低持有份额	String
     */
    public String getLimitHoldBalance() {
        return limitHoldBalance;
    }

    /**
     * 最低持有份额	String
     */
    public String getRedEmperiodStart() {
        return redEmperiodStart;
    }

    /**
     * 购买周期结束	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日
     * 频率1M（每月）、1S(每季度)为数字【1…31】代表
     * *号……**号
     */
    public String getBidPeriodEndDate() {
        return bidPeriodEndDate;
    }

    /**
     * 是否允许手机银行销售	String	0--否 1--是
     */
    public String getSellMobile() {
        return sellMobile;
    }

    /**
     * 产品工作开始时间	String	HH:mm:ss
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 挂单结束时间	String	HH:mm:ss
     */
    public String getOrderEndTime() {
        return orderEndTime;
    }

    /**
     * 产品币种	String	001：人民币元
     * 014：美元
     * 012：英镑
     * 013：港币
     * 028: 加拿大元
     * 029：澳元
     * 038：欧元
     * 027：日元
     */
    public String getCurCode() {
        return curCode;
    }

    /**
     * 产品风险类别	String	1：保本固定收益、
     * 2：保本浮动收益、
     * 3：非保本浮动收益
     */
    public String getProdRiskType() {
        return prodRiskType;
    }

    /**
     * 赎回结束日期	String	yyyyMMdd
     */
    public String getRedEmptionEndDate() {
        return redEmptionEndDate;
    }

    /**
     * 产品类型	String	1：现金管理类产品
     * 2：净值开放类产品
     * 3：固定期限产品
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 购买价格	BigDecimal
     */
    public String getBuyPrice() {
        return buyPrice;
    }

    /**
     * 认购/申购撤单设置	String	0：不允许撤单
     * 1：只允许当日撤单
     * 2：允许撤单
     */
    public String getIsCanCancle() {
        return isCanCancle;
    }

    /**
     * 是否允许赎回申请	String	0：不允许
     * 1：允许
     */
    public String getIsRedask() {
        return isRedask;
    }

    /**
     * 是否允许电话银行自助销售	String	0--否 1--是
     */
    public String getSellTelphone() {
        return sellTelphone;
    }

    /**
     * 购买结束日期	String	yyyy/mm/dd
     */
    public String getBidEndDate() {
        return bidEndDate;
    }

    /**
     * 赎回开始日期	String	yyyyMMdd
     */
    public String getRedEmptionStartDate() {
        return redEmptionStartDate;
    }

    /**
     * 赎回周期频率	String	D：天
     * W：周
     * M：月
     */
    public String getRedEmperiodfReq() {
        return redEmperiodfReq;
    }

    /**
     * 允许节假日赎回	String	0：不允许
     * 1：允许
     */
    public String getRedEmptionHoliday() {
        return redEmptionHoliday;
    }

    /**
     * 是否允许网上销售	String	0--否 1--是
     */
    public String getSellOnline() {
        return sellOnline;
    }

    /**
     * 本金返还T+N(天数) 	String
     */
    public String getRedPaymentDate() {
        return redPaymentDate;
    }

    /**
     * 允许节假日购买	String	0：不允许
     * 1：允许
     */
    public String getBidHoliday() {
        return bidHoliday;
    }

    /**
     * 赎回周期结束	String
     */
    public String getRedEmperiodEnd() {
        return redEmperiodEnd;
    }

    /**
     * 是否允许手机银行发布	String	0--否 1--是
     */
    public String getPublishMobile() {
        return publishMobile;
    }

    /**
     * 产品最大续约期数	String
     */
    public String getMaxPeriod() {
        return maxPeriod;
    }

    /**
     * 产品代码	String
     */
    public String getProdCode() {
        return prodCode;
    }

    /**
     * 购买周期频率	String 	D：天
     * W：周
     * M：月
     */
    public String getBidPeriodMode() {
        return bidPeriodMode;
    }

    /**
     * 节假日调整方式	String	0：unAdjust
     * 1：Following
     * 2：Modify following
     * 3：Forward
     * 4：Modify forward
     */
    public String getDateModeType() {
        return dateModeType;
    }

    /**
     * 是否周期性产品	String	0：否 ;1：是
     */
    public String getPeriodical() {
        return periodical;
    }

    /**
     * 是否允许微信银行销售	String	0--否 1--是
     */
    public String getSellWeChat() {
        return sellWeChat;
    }

    /**
     * 购买开放规则	String	00：关闭
     * 01：开放期购买
     * 02：周期开放购买
     * 03:：起息后每日申购
     */
    public String getBuyType() {
        return buyType;
    }

    /**
     * 产品工作结束时间	String	HH:mm:ss
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 本金到帐日	String
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * 适用对象	String	0：有投资经验
     * 1：无投资经验
     */
    public String getApplyObj() {
        return applyObj;
    }

    /**
     * 购买交易类型	String	0：认购
     * 1：申购
     */
    public String getTransTypeCode() {
        return transTypeCode;
    }

    /**
     * 是否收益累计产品	String	0：否
     * 1：是
     */
    public String getProgressionflag() {
        return progressionflag;
    }

    /**
     * 挂单开始时间	String	HH:mm:ss
     */
    public String getOrderStartTime() {
        return orderStartTime;
    }

    /**
     * 赎回申请提前天数	String	赎回开放期前n天
     */
    public String getRedaskDay() {
        return redaskDay;
    }

}

