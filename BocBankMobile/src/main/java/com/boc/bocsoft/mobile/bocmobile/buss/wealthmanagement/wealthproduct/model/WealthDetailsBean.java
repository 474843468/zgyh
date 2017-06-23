package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 理财产品详情
 * Created by liuweidong on 2016/9/24.
 */
public class WealthDetailsBean implements Parcelable {
    private boolean isLoginBeforeI;// 登录前接口

    private String productKind;// 产品性质 0:结构性理财产品1:类基金理财产品
    private String periodical;// 是否周期性产品
    private String productType;// 产品类型 1：现金管理类产品2：净值开放类产品3：固定期限产品
    private String prodName;// 产品名称
    private String prodCode;// 产品代码
    private String curCode;// 币种
    private String yearlyRR;// 预计年收益率（%）
    private String rateDetail;// 预计年收益率（%）(最大值)
    private String applyObj;// 适用对象
    private String status;// 产品销售状态
    private String prodRisklvl;// 产品风险等级
    private String prodRiskType;// 产品风险类别
    private String progressionflag;// 是否收益累计产品
    /*交易渠道*/
    private String isBancs;// 是否允许柜台
    private String isSMS;// 短信
    private String sellOnline;// 是否允许网上销售
    private String sellMobile;// 是否允许手机银行销售
    private String sellHomeBanc;// 是否允许家居银行销售
    private String sellAutoBanc;// 是否允许自助终端销售
    private String sellTelphone;// 是否允许电话银行自助销售
    private String sellTelByPeple;// 是否允许电话银行人工销售
    private String outTimeOrder;// 是否允许挂单销售
    private String sellWeChat;// 是否允许微信银行销售
    /*产品期限*/
    private String prodTimeLimit;// 产品期限
    private String productTermType;// 产品期限特性
    private String sellingStartingDate;// 销售起始日期
    private String sellingEndingDate;// 销售结束日期
    private String prodBegin;// 产品起息日
    private String prodEnd;// 产品到期日
    private String startTime;// 产品工作开始时间
    private String endTime;// 产品工作结束时间
    /*购买*/
    private String buyPrice;// 购买价格
    private String subAmount;// 认购起点金额
    private String addAmount;// 追加认申购起点金额
    private String buyType;// 购买开放规则
    private String bidStartDate;// 购买开始日期
    private String bidEndDate;// 购买结束日期
    private String bidHoliday;// 允许节假日购买
    private String bidPeriodMode;// 购买周期频率
    private String bidPeriodStartDate;// 购买周期开始
    private String bidPeriodEndDate;// 购买周期结束
    private String availamt;// 剩余额度
    private String baseAmount;// 购买基数
    private String isCanCancle;// 认购/申购撤单设置
    private String transTypeCode;
    private String remainCycleCount;// 剩余可购买最大期数.非周期性产品返回空（列表接口字段）
    private String orderStartTime;// 挂单开始时间
    private String orderEndTime;// 挂单结束时间
    /*赎回*/
    private String lowLimitAmount;// 赎回起点份额
    private String limitHoldBalance;// 最低持有份额
    private String sellType;// 赎回开放规则
    private String redEmptionStartDate;// 赎回开始日期
    private String redEmptionEndDate;// 赎回结束日期
    private String redEmptionHoliday;// 允许节假日赎回
    private String redEmperiodfReq;// 赎回周期频率
    private String redEmperiodStart;// 赎回周期开始
    private String redEmperiodEnd;// 赎回周期结束
    private String appdatered;// 是否允许指定日期赎回
    private String redPaymentMode;// 本金返还方式
    private String dateModeType;// 节假日调整方式
    private String redPaymentDate;// 本金返还T+N(天数)
    private String paymentDate;// 本金到帐日
    private String couponpayFreq;// 付息频率
    private String interestDate;// 收益到帐日
    private String profitMode;// 收益返还方式
    private String profitDate;// 收益返还T+N(天数)
    private String redPayDate;// 赎回本金收益到账日

    private String custLevelSale;// 产品适用的客户等级 0：普通客户及以上1：中银理财及以上2：财富管理及以上3：私人银行及以上
    /**
     * 结构性理财产品（非净值 结构性）
     */
    private String isLockPeriod = "";// 是否为业绩基准产品
    private String datesPaymentOffset;// 到期本金付款延迟天数（登录后）
    private String maxPeriod;// 产品最大续约期数
    /**
     * 类基金理财产品（净值 类基金）
     */
    private String price;// 单位净值
    private String priceDate;// 净值日期
    private String subscribeFee;// 认购手续费
    private String purchFee;// 申购手续费
    private String redeemFee;// 赎回手续费
    private String fundOrderTime;// 类基金挂单时间
    private String pfmcdrawStart;// 业绩费提取起点收益率
    private String pfmcdrawScale;// 业绩费提取比例

    public static Creator<WealthDetailsBean> getCREATOR() {
        return CREATOR;
    }

    public boolean isLoginBeforeI() {
        return isLoginBeforeI;
    }

    public void setLoginBeforeI(boolean loginBeforeI) {
        isLoginBeforeI = loginBeforeI;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getPeriodical() {
        return periodical;
    }

    public void setPeriodical(String periodical) {
        this.periodical = periodical;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
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
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getProgressionflag() {
        return progressionflag;
    }

    public void setProgressionflag(String progressionflag) {
        this.progressionflag = progressionflag;
    }

    public String getMaxPeriod() {
        return maxPeriod;
    }

    public void setMaxPeriod(String maxPeriod) {
        this.maxPeriod = maxPeriod;
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

    public String getSellMobile() {
        return sellMobile;
    }

    public void setSellMobile(String sellMobile) {
        this.sellMobile = sellMobile;
    }

    public String getSellHomeBanc() {
        return sellHomeBanc;
    }

    public void setSellHomeBanc(String sellHomeBanc) {
        this.sellHomeBanc = sellHomeBanc;
    }

    public String getSellAutoBanc() {
        return sellAutoBanc;
    }

    public void setSellAutoBanc(String sellAutoBanc) {
        this.sellAutoBanc = sellAutoBanc;
    }

    public String getSellTelphone() {
        return sellTelphone;
    }

    public void setSellTelphone(String sellTelphone) {
        this.sellTelphone = sellTelphone;
    }

    public String getSellTelByPeple() {
        return sellTelByPeple;
    }

    public void setSellTelByPeple(String sellTelByPeple) {
        this.sellTelByPeple = sellTelByPeple;
    }

    public String getOutTimeOrder() {
        return outTimeOrder;
    }

    public void setOutTimeOrder(String outTimeOrder) {
        this.outTimeOrder = outTimeOrder;
    }

    public String getSellWeChat() {
        return sellWeChat;
    }

    public void setSellWeChat(String sellWeChat) {
        this.sellWeChat = sellWeChat;
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

    public String getCustLevelSale() {
        return custLevelSale;
    }

    public void setCustLevelSale(String custLevelSale) {
        this.custLevelSale = custLevelSale;
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

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
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

    public String getRedEmptionHoliday() {
        return redEmptionHoliday;
    }

    public void setRedEmptionHoliday(String redEmptionHoliday) {
        this.redEmptionHoliday = redEmptionHoliday;
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

    public String getAppdatered() {
        return appdatered;
    }

    public void setAppdatered(String appdatered) {
        this.appdatered = appdatered;
    }

    public String getRedPaymentMode() {
        return redPaymentMode;
    }

    public void setRedPaymentMode(String redPaymentMode) {
        this.redPaymentMode = redPaymentMode;
    }

    public String getDateModeType() {
        return dateModeType;
    }

    public void setDateModeType(String dateModeType) {
        this.dateModeType = dateModeType;
    }

    public String getRedPaymentDate() {
        return redPaymentDate;
    }

    public void setRedPaymentDate(String redPaymentDate) {
        this.redPaymentDate = redPaymentDate;
    }

    public String getProductTermType() {
        return productTermType;
    }

    public void setProductTermType(String productTermType) {
        this.productTermType = productTermType;
    }

    public String getRateDetail() {
        return rateDetail;
    }

    public void setRateDetail(String rateDetail) {
        this.rateDetail = rateDetail;
    }

    public String getAvailamt() {
        return availamt;
    }

    public void setAvailamt(String availamt) {
        this.availamt = availamt;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getIsCanCancle() {
        return isCanCancle;
    }

    public void setIsCanCancle(String isCanCancle) {
        this.isCanCancle = isCanCancle;
    }

    public String getTransTypeCode() {
        return transTypeCode;
    }

    public void setTransTypeCode(String transTypeCode) {
        this.transTypeCode = transTypeCode;
    }

    public String getRemainCycleCount() {
        return remainCycleCount;
    }

    public void setRemainCycleCount(String remainCycleCount) {
        this.remainCycleCount = remainCycleCount;
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

    public String getIsLockPeriod() {
        return isLockPeriod;
    }

    public void setIsLockPeriod(String isLockPeriod) {
        this.isLockPeriod = isLockPeriod;
    }

    public String getDatesPaymentOffset() {
        return datesPaymentOffset;
    }

    public void setDatesPaymentOffset(String datesPaymentOffset) {
        this.datesPaymentOffset = datesPaymentOffset;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public String getFundOrderTime() {
        return fundOrderTime;
    }

    public void setFundOrderTime(String fundOrderTime) {
        this.fundOrderTime = fundOrderTime;
    }

    public String getPfmcdrawStart() {
        return pfmcdrawStart;
    }

    public void setPfmcdrawStart(String pfmcdrawStart) {
        this.pfmcdrawStart = pfmcdrawStart;
    }

    public String getPfmcdrawScale() {
        return pfmcdrawScale;
    }

    public void setPfmcdrawScale(String pfmcdrawScale) {
        this.pfmcdrawScale = pfmcdrawScale;
    }

    public WealthDetailsBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isLoginBeforeI ? (byte) 1 : (byte) 0);
        dest.writeString(this.productKind);
        dest.writeString(this.periodical);
        dest.writeString(this.productType);
        dest.writeString(this.prodName);
        dest.writeString(this.prodCode);
        dest.writeString(this.curCode);
        dest.writeString(this.yearlyRR);
        dest.writeString(this.rateDetail);
        dest.writeString(this.applyObj);
        dest.writeString(this.status);
        dest.writeString(this.prodRisklvl);
        dest.writeString(this.prodRiskType);
        dest.writeString(this.progressionflag);
        dest.writeString(this.isBancs);
        dest.writeString(this.isSMS);
        dest.writeString(this.sellOnline);
        dest.writeString(this.sellMobile);
        dest.writeString(this.sellHomeBanc);
        dest.writeString(this.sellAutoBanc);
        dest.writeString(this.sellTelphone);
        dest.writeString(this.sellTelByPeple);
        dest.writeString(this.outTimeOrder);
        dest.writeString(this.sellWeChat);
        dest.writeString(this.prodTimeLimit);
        dest.writeString(this.productTermType);
        dest.writeString(this.sellingStartingDate);
        dest.writeString(this.sellingEndingDate);
        dest.writeString(this.prodBegin);
        dest.writeString(this.prodEnd);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.buyPrice);
        dest.writeString(this.subAmount);
        dest.writeString(this.addAmount);
        dest.writeString(this.buyType);
        dest.writeString(this.bidStartDate);
        dest.writeString(this.bidEndDate);
        dest.writeString(this.bidHoliday);
        dest.writeString(this.bidPeriodMode);
        dest.writeString(this.bidPeriodStartDate);
        dest.writeString(this.bidPeriodEndDate);
        dest.writeString(this.availamt);
        dest.writeString(this.baseAmount);
        dest.writeString(this.isCanCancle);
        dest.writeString(this.transTypeCode);
        dest.writeString(this.remainCycleCount);
        dest.writeString(this.orderStartTime);
        dest.writeString(this.orderEndTime);
        dest.writeString(this.lowLimitAmount);
        dest.writeString(this.limitHoldBalance);
        dest.writeString(this.sellType);
        dest.writeString(this.redEmptionStartDate);
        dest.writeString(this.redEmptionEndDate);
        dest.writeString(this.redEmptionHoliday);
        dest.writeString(this.redEmperiodfReq);
        dest.writeString(this.redEmperiodStart);
        dest.writeString(this.redEmperiodEnd);
        dest.writeString(this.appdatered);
        dest.writeString(this.redPaymentMode);
        dest.writeString(this.dateModeType);
        dest.writeString(this.redPaymentDate);
        dest.writeString(this.paymentDate);
        dest.writeString(this.couponpayFreq);
        dest.writeString(this.interestDate);
        dest.writeString(this.profitMode);
        dest.writeString(this.profitDate);
        dest.writeString(this.redPayDate);
        dest.writeString(this.custLevelSale);
        dest.writeString(this.isLockPeriod);
        dest.writeString(this.datesPaymentOffset);
        dest.writeString(this.maxPeriod);
        dest.writeString(this.price);
        dest.writeString(this.priceDate);
        dest.writeString(this.subscribeFee);
        dest.writeString(this.purchFee);
        dest.writeString(this.redeemFee);
        dest.writeString(this.fundOrderTime);
        dest.writeString(this.pfmcdrawStart);
        dest.writeString(this.pfmcdrawScale);
    }

    protected WealthDetailsBean(Parcel in) {
        this.isLoginBeforeI = in.readByte() != 0;
        this.productKind = in.readString();
        this.periodical = in.readString();
        this.productType = in.readString();
        this.prodName = in.readString();
        this.prodCode = in.readString();
        this.curCode = in.readString();
        this.yearlyRR = in.readString();
        this.rateDetail = in.readString();
        this.applyObj = in.readString();
        this.status = in.readString();
        this.prodRisklvl = in.readString();
        this.prodRiskType = in.readString();
        this.progressionflag = in.readString();
        this.isBancs = in.readString();
        this.isSMS = in.readString();
        this.sellOnline = in.readString();
        this.sellMobile = in.readString();
        this.sellHomeBanc = in.readString();
        this.sellAutoBanc = in.readString();
        this.sellTelphone = in.readString();
        this.sellTelByPeple = in.readString();
        this.outTimeOrder = in.readString();
        this.sellWeChat = in.readString();
        this.prodTimeLimit = in.readString();
        this.productTermType = in.readString();
        this.sellingStartingDate = in.readString();
        this.sellingEndingDate = in.readString();
        this.prodBegin = in.readString();
        this.prodEnd = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.buyPrice = in.readString();
        this.subAmount = in.readString();
        this.addAmount = in.readString();
        this.buyType = in.readString();
        this.bidStartDate = in.readString();
        this.bidEndDate = in.readString();
        this.bidHoliday = in.readString();
        this.bidPeriodMode = in.readString();
        this.bidPeriodStartDate = in.readString();
        this.bidPeriodEndDate = in.readString();
        this.availamt = in.readString();
        this.baseAmount = in.readString();
        this.isCanCancle = in.readString();
        this.transTypeCode = in.readString();
        this.remainCycleCount = in.readString();
        this.orderStartTime = in.readString();
        this.orderEndTime = in.readString();
        this.lowLimitAmount = in.readString();
        this.limitHoldBalance = in.readString();
        this.sellType = in.readString();
        this.redEmptionStartDate = in.readString();
        this.redEmptionEndDate = in.readString();
        this.redEmptionHoliday = in.readString();
        this.redEmperiodfReq = in.readString();
        this.redEmperiodStart = in.readString();
        this.redEmperiodEnd = in.readString();
        this.appdatered = in.readString();
        this.redPaymentMode = in.readString();
        this.dateModeType = in.readString();
        this.redPaymentDate = in.readString();
        this.paymentDate = in.readString();
        this.couponpayFreq = in.readString();
        this.interestDate = in.readString();
        this.profitMode = in.readString();
        this.profitDate = in.readString();
        this.redPayDate = in.readString();
        this.custLevelSale = in.readString();
        this.isLockPeriod = in.readString();
        this.datesPaymentOffset = in.readString();
        this.maxPeriod = in.readString();
        this.price = in.readString();
        this.priceDate = in.readString();
        this.subscribeFee = in.readString();
        this.purchFee = in.readString();
        this.redeemFee = in.readString();
        this.fundOrderTime = in.readString();
        this.pfmcdrawStart = in.readString();
        this.pfmcdrawScale = in.readString();
    }

    public static final Creator<WealthDetailsBean> CREATOR = new Creator<WealthDetailsBean>() {
        @Override
        public WealthDetailsBean createFromParcel(Parcel source) {
            return new WealthDetailsBean(source);
        }

        @Override
        public WealthDetailsBean[] newArray(int size) {
            return new WealthDetailsBean[size];
        }
    };
}
