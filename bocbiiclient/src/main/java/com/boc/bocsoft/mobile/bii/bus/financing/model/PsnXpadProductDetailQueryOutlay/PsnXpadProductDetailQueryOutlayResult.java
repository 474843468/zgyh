package com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay;

/**
 * 4.52  PsnXpadProductDetailQueryOutlay登录前查询理财产品详情
 *
 *  结果 包含 结构性 和 类基金
 */
public class PsnXpadProductDetailQueryOutlayResult {

  private String prodCode; //产品代码	String
  private String prodName; //产品名称	String
  private String curCode; //产品币种	String	001：人民币元 014：美元 012：英镑013：港币028: 加拿大元029：澳元038：欧元027：日元
  private String yearlyRR; //预计年收益率（%）	String
  private String buyPrice; //购买价格	BigDecimal
  private String prodTimeLimit;
  //产品期限	String	当为业绩基准产品时该字段有效；当为非业绩基准产品，且“产品期限特性”不是“无限开放式”时，该字段有效；其余情况该字段无效，请统一展示无固定期限
  private String applyObj; //适用对象	String	0：有投资经验1：无投资经验
  private String status; //产品销售状态	String	1：在售产品、2：停售产品
  private String prodRisklvl; //产品风险级别	String	0：低风险产品1：中低风险产品2：中等风险产品3：中高风险产品4：高风险产品
  private String prodRiskType; //产品风险类别	String	1：保本固定收益、2：保本浮动收益、3：非保本浮动收益
  private String sellingStartingDate;  //销售起始日期	String
  private String sellingEndingDate;//销售结束日期	String
  private String prodBegin;//产品起息日	String
  private String prodEnd;//产品到期日	String
  private String isBancs;//是否允许柜台	String	0--否 1--是
  private String isSMS;//是否允许短信指令交易	String	0-否 1-是
  private String sellOnline;//是否允许网上销售	String	0--否 1--是
  private String publishOnline;//是否允许网上发布	String	0--否 1--是
  private String sellMobile;//是否允许手机银行销售	String	0--否 1--是
  private String publishMobile;//是否允许手机银行发布	String	0--否 1--是
  private String sellHomeBanc;//是否允许家居银行销售	String	0--否 1--是
  private String publishHomeBanc;//是否允许家居银行发布	String	0--否 1--是
  private String sellAutoBanc;//是否允许自助终端销售	String	0--否 1--是
  private String publishAutoBanc;//是否允许自助终端发布	String	0--否 1--是
  private String sellTelphone;//是否允许电话银行自助销售	String	0--否 1--是
  private String publishTelphone;//是否允许电话银行自助发布	String	0--否 1--是
  private String sellTelByPeple;//是否允许电话银行人工销售	String	0--否 1--是
  private String publishByPeple;//是否允许电话银行人工发布	String	0--否 1--是
  private String outTimeOrder;//是否允许挂单销售	String	0：不允许1：允许
  private String paymentDate;//本金到帐日	String
  private String couponpayFreq;//付息频率	String	*D：天 *W：周 *M：月 *Y：年 *表示代码数字 -1表示到期付息
  private String interestDate; //收益到帐日	String
  private String subAmount;//认购起点金额	String
  private String addAmount;//追加认申购起点金额	String
  private String isCanCancle;//认购/申购撤单设置	String	0：不允许撤单 1：只允许当日撤单2：允许撤单
  private String buyType;//购买开放规则	String	00：关闭 01：开放期购买02：周期开放购买03:：起息后每日申购
  private String bidStartDate;//购买开始日期	String	Yyyy/MM/dd
  private String bidEndDate;//购买结束日期	String	Yyyy/MM/dd
  private String bidHoliday;//允许节假日购买	String	0：不允许 1：允许
  private String bidPeriodMode;//购买周期频率	String 	1W：周 1M：月 1S：季
  private String bidPeriodStartDate;
  //购买周期开始	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日 频率1M（每周）、1S(每季度)为数字【1…31】代表 **号……**号
  private String bidPeriodEndDate;
  //购买周期结束	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日 频率1M（每周）、1S(每季度)为数字【1…31】代表 **号……**号
  private String lowLimitAmount;//赎回起点份额	String
  private String LimitHoldBalance;//最低持有份额	String
  private String sellType;//赎回开放规则	String	00：不允许赎回 01：开放期赎回 02：付息日赎回 03：起息后每日赎回04：周期开放赎回
  private String redEmptionHoliday;//允许节假日赎回	String	0：不允许1：允许
  private String redEmptionStartDate;  //赎回开始日期	String	yyyy/MM/dd
  private String redEmptionEndDate;//赎回结束日期	String	yyyy/MM/dd
  private String redEmperiodfReq;//赎回周期频率	String	1W：周 1M：月 1S：季
  private String redEmperiodStart;
  //赎回周期开始	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日 频率1M（每周）、1S(每季度)为数字【1…31】代表 **号……**号
  private String redEmperiodEnd;
  //赎回周期结束	String	频率1W（每周）该字段内容为数字【1…7】代表周一…….周日 频率1M（每周）、1S(每季度)为数字【1…31】代表 **号……**号
  private String redPaymentMode;//本金返还方式	String	0：实时返还 1：T+N返还 2：期末返还
  private String redPaymentDate;//本金返还T+N(天数) 	String
  private String profitMode;//收益返还方式	String	1：T+N返还 2：期末返还
  private String profitDate;//收益返还T+N(天数)	String
  private String redPayDate;//赎回本金收益到账日	String
  private String dateModeType;
  //节假日调整方式	String	0：unAdjust 1：Following 2：Modify following 3：Forward 4：Modify forward
  private String progressionflag;//是否收益累计产品	String	0：否 1：是
  private String sellWeChat;//是否允许微信银行销售	String	0--否 1--是
  private String publishWeChat;//是否允许微信银行发布	String	0--否 1--是
  private String transTypeCode;//购买交易类型	String	0：认购 1：申购
  private String periodical;//是否周期性产品	String	0：否 1：是
  private String baseAmount;//购买基数	String
  private String productType;//产品类型	String	1：现金管理类产品 2：净值开放类产品 3：固定期限产品
  private String productTermType;//产品期限特性	String	0：有限期封闭式 1：有限半开放式2：周期性3：无限开放式4：春夏秋冬
  private String productKind; //产品性质	String	0:结构性理财产品 1:类基金理财产品
  private String availamt;//剩余额度	BigDecimal
  private String appdatered;//是否允许指定日期赎回	String	0：否 1：是
  private String startTime;//产品工作开始时间	String	HH:mm:ss
  private String endTime;//产品工作结束时间	String	HH:mm:ss
  private String orderStartTime;//挂单开始时间	String	HH:mm:ss
  private String orderEndTime;//挂单结束时间	String	HH:mm:ss
  private String isRedask;//是否允许赎回申请	String	0：不允许1：允许
  private String redaskDay;//赎回申请提前天数	String	赎回开放期前n天
  private String custLevelSale;

  /****/
  /**
   * 收费方式	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * 0:前端收费外扣法一
   * 1:前端收费外扣法二
   * 2:前端收费内扣法一
   * 3:前端收费内扣法二
   * 4:后端收费
   */
  private String feeMode;//
  /**
   * 募集配售方式	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * 0:先到先得
   * 1:全程比例配售
   * 2:末日比例配售
   */
  private String collectDistributeMode;
  /**
   * 业绩费提取比例	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String pfmcdrawScale;
  /**
   * 业绩费提取起点收益率	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String pfmcdrawStart;
  /**
   * 类基金挂单时间	String	当产品性质为：1:类基金理财产品  时返回该字段
   * yyyyMMdd
   */
  private String fundOrderTime;
  /**
   * 转换手续费收取方式	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * 0:收取手续费
   * 1:不收取手续费
   */
  private String changeFeeMode;
  /**
   * 是否允许产品转换	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * 0:不允许
   * 1:允许
   */
  private String changePermit;
  /**
   * 份额成立T+N(天数)	String	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String balexecDays;
  /**
   * 不能转入产品ID	String	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String changeFromIssueid;
  /**
   * 销售人数上限	String	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String sellNumMax;
  /**
   * 挂单交易单笔限额	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String ordSingleMax;
  /**
   * 挂单交易总限额	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String ordTotalMax;
  /**
   * 单日申购上限	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String dayPurchMax;
  /**
   * 单日单人申购上限	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String dayPerPurchMax;
  /**
   * 单日单人申购上限	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String ordredSingleMax;
  /**
   * 挂单赎回总限额	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String ordredTotalMax;
  /**
   * 最新净值	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
   */
  private String price;
  /**
   * 净值日期	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * yyyyMMdd
   */
  private String priceDate;
  /**
   * 认购手续费	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * 采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   * 300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
   * 后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
   */
  private String subscribeFee;
  /**
   * 申购手续费	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * 采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   * 300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
   * 后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
   */
  private String purchFee;//
  /**
   * 赎回手续费	String	当产品性质为：1:类基金理财产品  时返回该字段
   *
   * 采用“|”进行手续费区间分割，按持有区间升序排列，持有区间方式有：按天、按月、按年三种方式，一款产品只能有一种方式；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
   * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
   * 或
   * 3月(含)-10月，3%|10月(含)-35月，450.00|35月(含)以上，6%
   * 或
   * 3年(含)-10年，3%|10年(含)-35年，450.00|35年(含)以上，6%
   */
  private String redeemFee;//
  /****/



  private String isLockPeriod;//     是否为业绩基准产品	String	0：非业绩基准产品1：业绩基准-锁定期转低收益  2：业绩基准-锁定期后入账  3：业绩基准-锁定期周期滚续
  private String maxPeriod;//			产品最大续约期数	String
  private String rateDetail;//			预计年收益率（%）(最大值)	String	不显示“%”，需要前端处理

  public String getProdCode() {
    return prodCode;
  }

  public String getProdName() {
    return prodName;
  }

  public String getCurCode() {
    return curCode;
  }

  public String getYearlyRR() {
    return yearlyRR;
  }

  public String getBuyPrice() {
    return buyPrice;
  }

  public String getProdTimeLimit() {
    return prodTimeLimit;
  }

  public String getApplyObj() {
    return applyObj;
  }

  public String getStatus() {
    return status;
  }

  public String getProdRisklvl() {
    return prodRisklvl;
  }

  public String getProdRiskType() {
    return prodRiskType;
  }

  public String getSellingStartingDate() {
    return sellingStartingDate;
  }

  public String getSellingEndingDate() {
    return sellingEndingDate;
  }

  public String getProdBegin() {
    return prodBegin;
  }

  public String getProdEnd() {
    return prodEnd;
  }

  public String getIsBancs() {
    return isBancs;
  }

  public String getIsSMS() {
    return isSMS;
  }

  public String getSellOnline() {
    return sellOnline;
  }

  public String getPublishOnline() {
    return publishOnline;
  }

  public String getSellMobile() {
    return sellMobile;
  }

  public String getPublishMobile() {
    return publishMobile;
  }

  public String getSellHomeBanc() {
    return sellHomeBanc;
  }

  public String getPublishHomeBanc() {
    return publishHomeBanc;
  }

  public String getSellAutoBanc() {
    return sellAutoBanc;
  }

  public String getPublishAutoBanc() {
    return publishAutoBanc;
  }

  public String getSellTelphone() {
    return sellTelphone;
  }

  public String getPublishTelphone() {
    return publishTelphone;
  }

  public String getSellTelByPeple() {
    return sellTelByPeple;
  }

  public String getPublishByPeple() {
    return publishByPeple;
  }

  public String getOutTimeOrder() {
    return outTimeOrder;
  }

  public String getPaymentDate() {
    return paymentDate;
  }

  public String getCouponpayFreq() {
    return couponpayFreq;
  }

  public String getInterestDate() {
    return interestDate;
  }

  public String getSubAmount() {
    return subAmount;
  }

  public String getAddAmount() {
    return addAmount;
  }

  public String getIsCanCancle() {
    return isCanCancle;
  }

  public String getBuyType() {
    return buyType;
  }

  public String getBidStartDate() {
    return bidStartDate;
  }

  public String getBidEndDate() {
    return bidEndDate;
  }

  public String getBidHoliday() {
    return bidHoliday;
  }

  public String getBidPeriodMode() {
    return bidPeriodMode;
  }

  public String getBidPeriodStartDate() {
    return bidPeriodStartDate;
  }

  public String getBidPeriodEndDate() {
    return bidPeriodEndDate;
  }

  public String getLowLimitAmount() {
    return lowLimitAmount;
  }

  public String getLimitHoldBalance() {
    return LimitHoldBalance;
  }

  public String getSellType() {
    return sellType;
  }

  public String getRedEmptionHoliday() {
    return redEmptionHoliday;
  }

  public String getRedEmptionStartDate() {
    return redEmptionStartDate;
  }

  public String getRedEmptionEndDate() {
    return redEmptionEndDate;
  }

  public String getRedEmperiodfReq() {
    return redEmperiodfReq;
  }

  public String getRedEmperiodStart() {
    return redEmperiodStart;
  }

  public String getRedEmperiodEnd() {
    return redEmperiodEnd;
  }

  public String getRedPaymentMode() {
    return redPaymentMode;
  }

  public String getRedPaymentDate() {
    return redPaymentDate;
  }

  public String getProfitMode() {
    return profitMode;
  }

  public String getProfitDate() {
    return profitDate;
  }

  public String getRedPayDate() {
    return redPayDate;
  }

  public String getDateModeType() {
    return dateModeType;
  }

  public String getProgressionflag() {
    return progressionflag;
  }

  public String getSellWeChat() {
    return sellWeChat;
  }

  public String getPublishWeChat() {
    return publishWeChat;
  }

  public String getTransTypeCode() {
    return transTypeCode;
  }

  public String getPeriodical() {
    return periodical;
  }

  public String getBaseAmount() {
    return baseAmount;
  }

  public String getProductType() {
    return productType;
  }

  public String getProductTermType() {
    return productTermType;
  }

  public String getProductKind() {
    return productKind;
  }

  public String getAvailamt() {
    return availamt;
  }

  public String getAppdatered() {
    return appdatered;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public String getOrderStartTime() {
    return orderStartTime;
  }

  public String getOrderEndTime() {
    return orderEndTime;
  }

  public String getIsRedask() {
    return isRedask;
  }

  public String getRedaskDay() {
    return redaskDay;
  }

  public String getCustLevelSale() {
    return custLevelSale;
  }

  public String getFeeMode() {
    return feeMode;
  }

  public String getCollectDistributeMode() {
    return collectDistributeMode;
  }

  public String getPfmcdrawScale() {
    return pfmcdrawScale;
  }

  public String getPfmcdrawStart() {
    return pfmcdrawStart;
  }

  public String getFundOrderTime() {
    return fundOrderTime;
  }

  public String getChangeFeeMode() {
    return changeFeeMode;
  }

  public String getChangePermit() {
    return changePermit;
  }

  public String getBalexecDays() {
    return balexecDays;
  }

  public String getChangeFromIssueid() {
    return changeFromIssueid;
  }

  public String getSellNumMax() {
    return sellNumMax;
  }

  public String getOrdSingleMax() {
    return ordSingleMax;
  }

  public String getOrdTotalMax() {
    return ordTotalMax;
  }

  public String getDayPurchMax() {
    return dayPurchMax;
  }

  public String getDayPerPurchMax() {
    return dayPerPurchMax;
  }

  public String getOrdredSingleMax() {
    return ordredSingleMax;
  }

  public String getOrdredTotalMax() {
    return ordredTotalMax;
  }

  public String getPrice() {
    return price;
  }

  public String getPriceDate() {
    return priceDate;
  }

  public String getSubscribeFee() {
    return subscribeFee;
  }

  public String getPurchFee() {
    return purchFee;
  }

  public String getRedeemFee() {
    return redeemFee;
  }

  public String getIsLockPeriod() {
    return isLockPeriod;
  }

  public String getMaxPeriod() {
    return maxPeriod;
  }

  public String getRateDetail() {
    return rateDetail;
  }

  /*  public static class x2 extends XpadProdcutBean {

    *//**
     * 收费方式	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * 0:前端收费外扣法一
     * 1:前端收费外扣法二
     * 2:前端收费内扣法一
     * 3:前端收费内扣法二
     * 4:后端收费
     *//*
    private String feeMode;//
    *//**
     * 募集配售方式	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * 0:先到先得
     * 1:全程比例配售
     * 2:末日比例配售
     *//*
    private String collectDistributeMode;
    *//**
     * 业绩费提取比例	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String pfmcdrawScale;
    *//**
     * 业绩费提取起点收益率	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String pfmcdrawStart;
    *//**
     * 类基金挂单时间	String	当产品性质为：1:类基金理财产品  时返回该字段
     * yyyyMMdd
     *//*
    private String fundOrderTime;
    *//**
     * 转换手续费收取方式	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * 0:收取手续费
     * 1:不收取手续费
     *//*
    private String changeFeeMode;
    *//**
     * 是否允许产品转换	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * 0:不允许
     * 1:允许
     *//*
    private String changePermit;
    *//**
     * 份额成立T+N(天数)	String	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String balexecDays;
    *//**
     * 不能转入产品ID	String	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String changeFromIssueid;
    *//**
     * 销售人数上限	String	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String sellNumMax;
    *//**
     * 挂单交易单笔限额	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String ordSingleMax;
    *//**
     * 挂单交易总限额	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String ordTotalMax;
    *//**
     * 单日申购上限	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String dayPurchMax;
    *//**
     * 单日单人申购上限	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String dayPerPurchMax;
    *//**
     * 单日单人申购上限	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String ordredSingleMax;
    *//**
     * 挂单赎回总限额	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String ordredTotalMax;
    *//**
     * 最新净值	BigDecimal	当产品性质为：1:类基金理财产品  时返回该字段
     *//*
    private String price;
    *//**
     * 净值日期	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * yyyyMMdd
     *//*
    private String priceDate;
    *//**
     * 认购手续费	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * 采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
     * 后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
     *//*
    private String subscribeFee;
    *//**
     * 申购手续费	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * 采用“|”进行手续费区间分割，前段收费方式按金额区间段升序排列；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 300.00(含)-5000.00，23%|5000.00(含)-7000.00，780|7000.00(含)以上，28%
     * 后端收费方式按持有时间长短排序；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
     *//*
    private String purchFee;//
    *//**
     * 赎回手续费	String	当产品性质为：1:类基金理财产品  时返回该字段
     *
     * 采用“|”进行手续费区间分割，按持有区间升序排列，持有区间方式有：按天、按月、按年三种方式，一款产品只能有一种方式；费率方式为“按费率”，为n%，费率方式为“按金额”，为n，参考示例：
     * 3天(含)-10天，3%|10天(含)-35天，450.00|35天(含)以上，6%
     * 或
     * 3月(含)-10月，3%|10月(含)-35月，450.00|35月(含)以上，6%
     * 或
     * 3年(含)-10年，3%|10年(含)-35年，450.00|35年(含)以上，6%
     *//*
    private String redeemFee;//
  }*/

/*  *//**
   * 结构性
   *//*
  public static class x1 extends XpadProdcutBean {
    private String isLockPeriod;
    //     是否为业绩基准产品	String	0：非业绩基准产品1：业绩基准-锁定期转低收益  2：业绩基准-锁定期后入账  3：业绩基准-锁定期周期滚续
    private String maxPeriod;//			产品最大续约期数	String
    private String rateDetail;//			预计年收益率（%）(最大值)	String	不显示“%”，需要前端处理
  }*/
}

