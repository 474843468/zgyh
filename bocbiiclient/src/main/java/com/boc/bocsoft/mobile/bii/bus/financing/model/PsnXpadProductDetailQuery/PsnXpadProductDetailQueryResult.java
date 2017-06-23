package com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQuery;

/**
 * 4.40 040产品详情查询 PsnXpadProductDetailQuery 登陆后
 */
public class PsnXpadProductDetailQueryResult {






  //-------




  private String prodCode			       ; //产品代码	String
  private String prodName			       ; //产品名称	String
  private String curCode				       ; //产品币种	String	001：人民币元 014：美元012：英镑013：港币028: 加拿大元029：澳元038：欧元027：日元
  private String yearlyRR			       ; //预计年收益率（%）	String
  private String buyPrice			       ; //购买价格	BigDecimal
  private String prodTimeLimit	       ; //产品期限	String	当为业绩基准产品时该字段有效； 当为非业绩基准产品，且“产品期限特性”不是“无限开放式”时，该字段有效；其余情况该字段无效，请统一展示无固定期限
  private String applyObj			       ; //适用对象	String	0：有投资经验1：无投资经验
  private String Status				       ; //产品销售状态	String	1：在售产品、2：停售产品
  private String prodRisklvl		       ; //产品风险级别	String	0：低风险产品1：中低风险产品2：中等风险产品3：中高风险产品4：高风险产品
  private String prodRiskType	       ; //产品风险类别	String	1：保本固定收益、2：保本浮动收益、3：非保本浮动收益
  private String sellingStartingDate		;//销售起始日期	String
  private String sellingEndingDate			;//销售结束日期	String
  private String prodBegin							;//产品起息日	String
  private String prodEnd								;//产品到期日	String
  private String isBancs								;//是否允许柜台	String	0--否 1--是
  private String isSMS									;//是否允许短信指令交易	String	0-否 1-是
  private String sellOnline						;//是否允许网上销售	String	0--否 1--是
  private String publishOnline					;//是否允许网上发布	String	0--否 1--是
  private String sellMobile						;//是否允许手机银行销售	String	0--否 1--是
  private String publishMobile					;//是否允许手机银行发布	String	0--否 1--是
  private String sellHomeBanc					;//是否允许家居银行销售	String	0--否 1--是
  private String publishHomeBanc				;//是否允许家居银行发布	String	0--否 1--是
  private String sellAutoBanc					;//是否允许自助终端销售	String	0--否 1--是
  private String publishAutoBanc				;//是否允许自助终端发布	String	0--否 1--是
  private String sellTelphone					;//是否允许电话银行自助销售	String	0--否 1--是
  private String publishTelphone				;//是否允许电话银行自助发布	String	0--否 1--是
  private String sellTelByPeple				;//是否允许电话银行人工销售	String	0--否 1--是
  private String publishByPeple				;//是否允许电话银行人工发布	String	0--否 1--是
  private String outTimeOrder					;//是否允许挂单销售	String	0：不允许1：允许
  private String paymentDate						;//本金到帐日	String
  private String couponpayFreq					;//付息频率	String	*D：天*W：周*M：月*Y：年*表示代码数字-1表示到期付息
  private String interestDate					;//收益到帐日	String
  private String subAmount							;//认购起点金额	String
  private String addAmount							;//追加认申购起点金额	String
  private String isCanCancle						;//认购/申购撤单设置	String	0：不允许撤单1：只允许当日撤单2：允许撤单
  private String buyType								;//购买开放规则	String	00：关闭01：开放期购买02：周期开放购买03:：起息后每日申购
  private String bidStartDate					;//购买开始日期	String	yyyy/mm/dd
  private String bidEndDate						;//购买结束日期	String	yyyy/mm/dd
  private String bidHoliday						;//允许节假日购买	String	0：不允许1：允许
  private String bidPeriodMode					;//购买周期频率	String 	D：天W：周M：月
  private String bidPeriodStartDate		;//购买周期开始	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日 频率1M（每月）、1S(每季度)为数字【1…31】代表**号……**号
  private String bidPeriodEndDate			;//购买周期结束	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日 频率1M（每月）、1S(每季度)为数字【1…31】代表**号……**号
  private String lowLimitAmount				;//赎回起点份额	String
  private String limitHoldBalance			;//最低持有份额	String
  private String sellType							;//赎回开放规则	String	00：不允许赎回 01：开放期赎回02：付息日赎回 03：起息后每日赎回 04：周期开放赎回
  private String redEmptionHoliday			;//允许节假日赎回	String	0：不允许 1：允许
  private String redEmptionStartDate		;//赎回开始日期	String	yyyyMMdd
  private String redEmptionEndDate			;//赎回结束日期	String	yyyyMMdd
  private String redEmperiodfReq				;//赎回周期频率	String	D：天 W：周 M：月
  private String redEmperiodStart			;//赎回周期开始	String
  private String redEmperiodEnd				;//赎回周期结束	String
  private String redPaymentMode				;//本金返还方式	String	0：实时返还 1：T+N返还 2：期末返还
  private String redPaymentDate				;//本金返还T+N(天数) 	String
  private String profitMode						;//收益返还方式	String	1：T+N返还 2：期末返还
  private String profitDate						;//收益返还T+N(天数)	String
  private String redPayDate						;//赎回本金收益到账日	String
  private String dateModeType					;//节假日调整方式	String	0：unAdjust 1：Following 2：Modify following 3：Forward 4：Modify forward
  private String progressionflag				;//是否收益累计产品	String	0：否1：是
  private String sellWeChat						;//是否允许微信银行销售	String	0--否 1--是
  private String publishWeChat					;//是否允许微信银行发布	String	0--否 1--是
  private String transTypeCode					;//购买交易类型	String	0：认购 1：申购
  private String periodical						;//是否周期性产品	String	0：否1：是
  private String baseAmount						;//购买基数	String
  private String productType						;//产品类型	String	1：现金管理类产品 2：净值开放类产品3：固定期限产品
  private String productTermType				;//产品期限特性	String	0：有限期封闭式 1：有限半开放式2：周期性 3：无限开放式4：春夏秋冬
  private String productKind						;//产品性质	String	0:结构性理财产品 1:类基金理财产品
  private String availamt							;//剩余额度	BigDecimal
  private String appdatered						;//是否允许指定日期赎回	String	0：否 1：是
  private String isLockPeriod					;//是否为业绩基准产品	String	0：非业绩基准产品1：业绩基准-锁定期转低收益  2：业绩基准-锁定期后入账  3：业绩基准-锁定期周期滚续
  private String startTime							;//产品工作开始时间	String	HH:mm:ss
  private String endTime								;//产品工作结束时间	String	HH:mm:ss
  private String orderStartTime				;//挂单开始时间	String	HH:mm:ss
  private String orderEndTime					;//挂单结束时间	String	HH:mm:ss
  private String isRedask							;//是否允许赎回申请	String	0：不允许1：允许
  private String redaskDay							;//赎回申请提前天数	String	赎回开放期前n天
  private String maxPeriod							;//产品最大续约期数	String
  private String rateDetail						;//预计年收益率（%）(最大值)	String	不显示“%”，需要前端处理
  private String datesPaymentOffset		;//到期本金付款延迟天数	String
  private String custLevelSale					;//产品适用的客户等级	String	0：普通客户及以上1：中银理财及以上2：财富管理及以上3：私人银行及以上




  //产品性质：1-类基金理财产品

  private String LimitHoldBalance			;//最低持有份额	String
  private String feeMode								;//收费方式	String	0:前端收费外扣法一
  private String collectDistributeMode	;//募集配售方式	String	0:先到先得1:全程比例配售 2:末日比例配售
  private String pfmcDrawScale					;//业绩费提取比例	BigDecimal
  private String pfmcDrawStart					;//业绩费提取起点收益率	BigDecimal
  private String fundOrderTime					;//类基金挂单时间	String	yyyyMMdd
  private String changeFeeMode					;//转换手续费收取方式	String	0:收取手续费1:不收取手续费
  private String changePermit					;//是否允许产品转换	String	0:不允许1:允许
  private String balexecDays						;//份额成立T+N(天数)	String
  private String changeFromIssueid			;//不能转入产品ID	String
  private String sellNumMax						;//销售人数上限	String	-1表示不设置销售人数上限
  private String ordSingleMax					;//挂单交易单笔限额	BigDecimal
  private String ordTotalMax						;//挂单交易总限额	BigDecimal
  private String dayPurchMax						;//单日申购上限	BigDecimal
  private String dayPerPurchMax				;//单日单人申购上限	BigDecimal
  private String ordredSingleMax				;//挂单赎回单笔限额	BigDecimal
  private String ordredTotalMax				;//挂单赎回总限额	BigDecimal
  private String price									;//单位净值	BigDecimal
  private String priceDate							;//净值日期	String	yyyyMMdd，针对净值型产品显示具体净值报价日期，其他产品均显示“-”表示不适用。

  /**
   *
   * 认购手续费	String	未维护手续费返回为空

   采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
   后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
   purchFee	申购手续费	String	未维护手续费返回为空
   采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
   后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
   redeemFee	赎回手续费	String	未维护手续费返回为空
   采用“|”进行手续费区间分割，按持有区间升序排列，持有区间方式有：按天、按月、按年三种方式，一款产品只能有一种方式；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
   或
   3月(含)-10月，3%|10月(含)-35月，450.00|35月(含)以上，6%
   或
   3年(含)-10年，3%|10年(含)-35年，450.00|35年(含)以上，6%
   */
  private String subscribeFee					;//

  public String getProdCode() {
    return prodCode;
  }

  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

  public String getProdName() {
    return prodName;
  }

  public void setProdName(String prodName) {
    this.prodName = prodName;
  }

  public String getCurCode() {
    return curCode;
  }

  public void setCurCode(String curCode) {
    this.curCode = curCode;
  }

  public String getYearlyRR() {
    return yearlyRR;
  }

  public void setYearlyRR(String yearlyRR) {
    this.yearlyRR = yearlyRR;
  }

  public String getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(String buyPrice) {
    this.buyPrice = buyPrice;
  }

  public String getProdTimeLimit() {
    return prodTimeLimit;
  }

  public void setProdTimeLimit(String prodTimeLimit) {
    this.prodTimeLimit = prodTimeLimit;
  }

  public String getApplyObj() {
    return applyObj;
  }

  public void setApplyObj(String applyObj) {
    this.applyObj = applyObj;
  }

  public String getStatus() {
    return Status;
  }

  public void setStatus(String status) {
    Status = status;
  }

  public String getProdRisklvl() {
    return prodRisklvl;
  }

  public void setProdRisklvl(String prodRisklvl) {
    this.prodRisklvl = prodRisklvl;
  }

  public String getProdRiskType() {
    return prodRiskType;
  }

  public void setProdRiskType(String prodRiskType) {
    this.prodRiskType = prodRiskType;
  }

  public String getSellingStartingDate() {
    return sellingStartingDate;
  }

  public void setSellingStartingDate(String sellingStartingDate) {
    this.sellingStartingDate = sellingStartingDate;
  }

  public String getSellingEndingDate() {
    return sellingEndingDate;
  }

  public void setSellingEndingDate(String sellingEndingDate) {
    this.sellingEndingDate = sellingEndingDate;
  }

  public String getProdBegin() {
    return prodBegin;
  }

  public void setProdBegin(String prodBegin) {
    this.prodBegin = prodBegin;
  }

  public String getProdEnd() {
    return prodEnd;
  }

  public void setProdEnd(String prodEnd) {
    this.prodEnd = prodEnd;
  }

  public String getIsBancs() {
    return isBancs;
  }

  public void setIsBancs(String isBancs) {
    this.isBancs = isBancs;
  }

  public String getIsSMS() {
    return isSMS;
  }

  public void setIsSMS(String isSMS) {
    this.isSMS = isSMS;
  }

  public String getSellOnline() {
    return sellOnline;
  }

  public void setSellOnline(String sellOnline) {
    this.sellOnline = sellOnline;
  }

  public String getPublishOnline() {
    return publishOnline;
  }

  public void setPublishOnline(String publishOnline) {
    this.publishOnline = publishOnline;
  }

  public String getSellMobile() {
    return sellMobile;
  }

  public void setSellMobile(String sellMobile) {
    this.sellMobile = sellMobile;
  }

  public String getPublishMobile() {
    return publishMobile;
  }

  public void setPublishMobile(String publishMobile) {
    this.publishMobile = publishMobile;
  }

  public String getSellHomeBanc() {
    return sellHomeBanc;
  }

  public void setSellHomeBanc(String sellHomeBanc) {
    this.sellHomeBanc = sellHomeBanc;
  }

  public String getPublishHomeBanc() {
    return publishHomeBanc;
  }

  public void setPublishHomeBanc(String publishHomeBanc) {
    this.publishHomeBanc = publishHomeBanc;
  }

  public String getSellAutoBanc() {
    return sellAutoBanc;
  }

  public void setSellAutoBanc(String sellAutoBanc) {
    this.sellAutoBanc = sellAutoBanc;
  }

  public String getPublishAutoBanc() {
    return publishAutoBanc;
  }

  public void setPublishAutoBanc(String publishAutoBanc) {
    this.publishAutoBanc = publishAutoBanc;
  }

  public String getSellTelphone() {
    return sellTelphone;
  }

  public void setSellTelphone(String sellTelphone) {
    this.sellTelphone = sellTelphone;
  }

  public String getPublishTelphone() {
    return publishTelphone;
  }

  public void setPublishTelphone(String publishTelphone) {
    this.publishTelphone = publishTelphone;
  }

  public String getSellTelByPeple() {
    return sellTelByPeple;
  }

  public void setSellTelByPeple(String sellTelByPeple) {
    this.sellTelByPeple = sellTelByPeple;
  }

  public String getPublishByPeple() {
    return publishByPeple;
  }

  public void setPublishByPeple(String publishByPeple) {
    this.publishByPeple = publishByPeple;
  }

  public String getOutTimeOrder() {
    return outTimeOrder;
  }

  public void setOutTimeOrder(String outTimeOrder) {
    this.outTimeOrder = outTimeOrder;
  }

  public String getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }

  public String getCouponpayFreq() {
    return couponpayFreq;
  }

  public void setCouponpayFreq(String couponpayFreq) {
    this.couponpayFreq = couponpayFreq;
  }

  public String getInterestDate() {
    return interestDate;
  }

  public void setInterestDate(String interestDate) {
    this.interestDate = interestDate;
  }

  public String getSubAmount() {
    return subAmount;
  }

  public void setSubAmount(String subAmount) {
    this.subAmount = subAmount;
  }

  public String getAddAmount() {
    return addAmount;
  }

  public void setAddAmount(String addAmount) {
    this.addAmount = addAmount;
  }

  public String getIsCanCancle() {
    return isCanCancle;
  }

  public void setIsCanCancle(String isCanCancle) {
    this.isCanCancle = isCanCancle;
  }

  public String getBuyType() {
    return buyType;
  }

  public void setBuyType(String buyType) {
    this.buyType = buyType;
  }

  public String getBidStartDate() {
    return bidStartDate;
  }

  public void setBidStartDate(String bidStartDate) {
    this.bidStartDate = bidStartDate;
  }

  public String getBidEndDate() {
    return bidEndDate;
  }

  public void setBidEndDate(String bidEndDate) {
    this.bidEndDate = bidEndDate;
  }

  public String getBidHoliday() {
    return bidHoliday;
  }

  public void setBidHoliday(String bidHoliday) {
    this.bidHoliday = bidHoliday;
  }

  public String getBidPeriodMode() {
    return bidPeriodMode;
  }

  public void setBidPeriodMode(String bidPeriodMode) {
    this.bidPeriodMode = bidPeriodMode;
  }

  public String getBidPeriodStartDate() {
    return bidPeriodStartDate;
  }

  public void setBidPeriodStartDate(String bidPeriodStartDate) {
    this.bidPeriodStartDate = bidPeriodStartDate;
  }

  public String getBidPeriodEndDate() {
    return bidPeriodEndDate;
  }

  public void setBidPeriodEndDate(String bidPeriodEndDate) {
    this.bidPeriodEndDate = bidPeriodEndDate;
  }

  public String getLowLimitAmount() {
    return lowLimitAmount;
  }

  public void setLowLimitAmount(String lowLimitAmount) {
    this.lowLimitAmount = lowLimitAmount;
  }

  public String getLimitHoldBalance() {
    return limitHoldBalance;
  }

  public void setLimitHoldBalance(String limitHoldBalance) {
    this.limitHoldBalance = limitHoldBalance;
  }

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

  public String getSellType() {
    return sellType;
  }

  public void setSellType(String sellType) {
    this.sellType = sellType;
  }

  public String getRedEmptionHoliday() {
    return redEmptionHoliday;
  }

  public void setRedEmptionHoliday(String redEmptionHoliday) {
    this.redEmptionHoliday = redEmptionHoliday;
  }

  public String getRedEmptionStartDate() {
    return redEmptionStartDate;
  }

  public void setRedEmptionStartDate(String redEmptionStartDate) {
    this.redEmptionStartDate = redEmptionStartDate;
  }

  public String getRedEmptionEndDate() {
    return redEmptionEndDate;
  }

  public void setRedEmptionEndDate(String redEmptionEndDate) {
    this.redEmptionEndDate = redEmptionEndDate;
  }

  public String getRedEmperiodfReq() {
    return redEmperiodfReq;
  }

  public void setRedEmperiodfReq(String redEmperiodfReq) {
    this.redEmperiodfReq = redEmperiodfReq;
  }

  public String getRedEmperiodStart() {
    return redEmperiodStart;
  }

  public void setRedEmperiodStart(String redEmperiodStart) {
    this.redEmperiodStart = redEmperiodStart;
  }

  public String getRedEmperiodEnd() {
    return redEmperiodEnd;
  }

  public void setRedEmperiodEnd(String redEmperiodEnd) {
    this.redEmperiodEnd = redEmperiodEnd;
  }

  public String getRedPaymentMode() {
    return redPaymentMode;
  }

  public void setRedPaymentMode(String redPaymentMode) {
    this.redPaymentMode = redPaymentMode;
  }

  public String getRedPaymentDate() {
    return redPaymentDate;
  }

  public void setRedPaymentDate(String redPaymentDate) {
    this.redPaymentDate = redPaymentDate;
  }

  public String getProfitMode() {
    return profitMode;
  }

  public void setProfitMode(String profitMode) {
    this.profitMode = profitMode;
  }

  public String getProfitDate() {
    return profitDate;
  }

  public void setProfitDate(String profitDate) {
    this.profitDate = profitDate;
  }

  public String getRedPayDate() {
    return redPayDate;
  }

  public void setRedPayDate(String redPayDate) {
    this.redPayDate = redPayDate;
  }

  public String getDateModeType() {
    return dateModeType;
  }

  public void setDateModeType(String dateModeType) {
    this.dateModeType = dateModeType;
  }

  public String getProgressionflag() {
    return progressionflag;
  }

  public void setProgressionflag(String progressionflag) {
    this.progressionflag = progressionflag;
  }

  public String getSellWeChat() {
    return sellWeChat;
  }

  public void setSellWeChat(String sellWeChat) {
    this.sellWeChat = sellWeChat;
  }

  public String getPublishWeChat() {
    return publishWeChat;
  }

  public void setPublishWeChat(String publishWeChat) {
    this.publishWeChat = publishWeChat;
  }

  public String getTransTypeCode() {
    return transTypeCode;
  }

  public void setTransTypeCode(String transTypeCode) {
    this.transTypeCode = transTypeCode;
  }

  public String getPeriodical() {
    return periodical;
  }

  public void setPeriodical(String periodical) {
    this.periodical = periodical;
  }

  public String getBaseAmount() {
    return baseAmount;
  }

  public void setBaseAmount(String baseAmount) {
    this.baseAmount = baseAmount;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getProductTermType() {
    return productTermType;
  }

  public void setProductTermType(String productTermType) {
    this.productTermType = productTermType;
  }

  public String getProductKind() {
    return productKind;
  }

  public void setProductKind(String productKind) {
    this.productKind = productKind;
  }

  public String getAvailamt() {
    return availamt;
  }

  public void setAvailamt(String availamt) {
    this.availamt = availamt;
  }

  public String getAppdatered() {
    return appdatered;
  }

  public void setAppdatered(String appdatered) {
    this.appdatered = appdatered;
  }

  public String getIsLockPeriod() {
    return isLockPeriod;
  }

  public void setIsLockPeriod(String isLockPeriod) {
    this.isLockPeriod = isLockPeriod;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getOrderStartTime() {
    return orderStartTime;
  }

  public void setOrderStartTime(String orderStartTime) {
    this.orderStartTime = orderStartTime;
  }

  public String getOrderEndTime() {
    return orderEndTime;
  }

  public void setOrderEndTime(String orderEndTime) {
    this.orderEndTime = orderEndTime;
  }

  public String getIsRedask() {
    return isRedask;
  }

  public void setIsRedask(String isRedask) {
    this.isRedask = isRedask;
  }

  public String getRedaskDay() {
    return redaskDay;
  }

  public void setRedaskDay(String redaskDay) {
    this.redaskDay = redaskDay;
  }

  public String getMaxPeriod() {
    return maxPeriod;
  }

  public void setMaxPeriod(String maxPeriod) {
    this.maxPeriod = maxPeriod;
  }

  public String getRateDetail() {
    return rateDetail;
  }

  public void setRateDetail(String rateDetail) {
    this.rateDetail = rateDetail;
  }

  public String getDatesPaymentOffset() {
    return datesPaymentOffset;
  }

  public void setDatesPaymentOffset(String datesPaymentOffset) {
    this.datesPaymentOffset = datesPaymentOffset;
  }

  public String getCustLevelSale() {
    return custLevelSale;
  }

  public void setCustLevelSale(String custLevelSale) {
    this.custLevelSale = custLevelSale;
  }
}

