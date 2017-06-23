package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资协议申请（ui model）
 * Created by wangtong on 2016/10/24.
 */
public class ProtocolModel implements Parcelable {
    /*页面传值*/
    private List<WealthAccountBean> accountList = new ArrayList<>();
    private String proId;// 产品代码
    private String productKind;// 产品性质
    private String prodName;// 产品名称
    private String curCode;// 币种
    private String subAmount;// 认申购起点金额
    private String addAmount;// 追加认申购起点金额
    private String lowLimitAmount;// 赎回起点份额
    private String remainCycleCount;// 最大购买期数
    private String periodical;// 是否周期性产品
    private String price;
    private String priceDate;
    private String yearlyRR;
    private String rateDetail;
    private String prodTimeLimit;
    private String isLockPeriod;
    private String productTermType;
    /*产品投资协议查询（响应参数）*/
    private String recordNumber;
    private List<InvestTreatyBean> protocolList = new ArrayList<>();
    // 当前点击的投资协议
    public InvestTreatyBean selectedProtocol;
    /*智能投资确认页（请求参数）*/
    private String agrCode;// 协议编号
    private String charCode;// 钞汇类型
    private String amountType;// 投资金额模式 0:定额 1:不定额
    private String minAmount;// 账户保留余额
    private String maxAmount;// 最大购买金额
    private String unit;// 赎回份额
    private String isControl;// 是否不限期
    private String totalPeriod;// 购买期数/赎回期数
    private String amount;// 单期购买金额/单次购买金额
    private ProtocolIntelligentConfirmBean confirmBean;// 响应返回
    /*周期滚续投资确认页（请求参数）*/
    private String accountId;//账户ID
    private String accountNum;//账号
    private String serialName;//产品系列名称
    private String serialCode;//产品系列编号
    private String xpadCashRemit;//钞汇标识
    private String amountTypeCode;//基础金额模式
    private String baseAmount;//基础金额
    private String dealCode;//后台交易id
    private String token;
    private PsnXpadSignInitBean signInitBean;
    private PsnXpadSignResultBean signResultBean;
    /*风险匹配（响应参数）*/
    private String riskMatch;// 风险匹配结果
    private String proRisk;// 产品风险级别
    private String custRisk;// 客户风险等级
    private String isPeriod;// 是否周期性产品
    private String productId;// 产品代码
    private String digitId;// 产品数字代码
    private String riskMsg;// 风险揭示信息

    public List<WealthAccountBean> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<WealthAccountBean> accountList) {
        this.accountList = accountList;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
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

    public String getLowLimitAmount() {
        return lowLimitAmount;
    }

    public void setLowLimitAmount(String lowLimitAmount) {
        this.lowLimitAmount = lowLimitAmount;
    }

    public String getRemainCycleCount() {
        return remainCycleCount;
    }

    public void setRemainCycleCount(String remainCycleCount) {
        this.remainCycleCount = remainCycleCount;
    }

    public String getPeriodical() {
        return periodical;
    }

    public void setPeriodical(String periodical) {
        this.periodical = periodical;
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

    public String getYearlyRR() {
        return yearlyRR;
    }

    public void setYearlyRR(String yearlyRR) {
        this.yearlyRR = yearlyRR;
    }

    public String getRateDetail() {
        return rateDetail;
    }

    public void setRateDetail(String rateDetail) {
        this.rateDetail = rateDetail;
    }

    public String getProdTimeLimit() {
        return prodTimeLimit;
    }

    public void setProdTimeLimit(String prodTimeLimit) {
        this.prodTimeLimit = prodTimeLimit;
    }

    public String getIsLockPeriod() {
        return isLockPeriod;
    }

    public void setIsLockPeriod(String isLockPeriod) {
        this.isLockPeriod = isLockPeriod;
    }

    public String getProductTermType() {
        return productTermType;
    }

    public void setProductTermType(String productTermType) {
        this.productTermType = productTermType;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<InvestTreatyBean> getProtocolList() {
        return protocolList;
    }

    public void setProtocolList(List<InvestTreatyBean> protocolList) {
        this.protocolList = protocolList;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ProtocolIntelligentConfirmBean getConfirmBean() {
        return confirmBean;
    }

    public void setConfirmBean(ProtocolIntelligentConfirmBean confirmBean) {
        this.confirmBean = confirmBean;
    }

    public InvestTreatyBean getSelectedProtocol() {
        return selectedProtocol;
    }

    public void setSelectedProtocol(InvestTreatyBean selectedProtocol) {
        this.selectedProtocol = selectedProtocol;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getSerialName() {
        return serialName;
    }

    public void setSerialName(String serialName) {
        this.serialName = serialName;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getAmountTypeCode() {
        return amountTypeCode;
    }

    public void setAmountTypeCode(String amountTypeCode) {
        this.amountTypeCode = amountTypeCode;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRiskMatch() {
        return riskMatch;
    }

    public void setRiskMatch(String riskMatch) {
        this.riskMatch = riskMatch;
    }

    public String getProRisk() {
        return proRisk;
    }

    public void setProRisk(String proRisk) {
        this.proRisk = proRisk;
    }

    public String getCustRisk() {
        return custRisk;
    }

    public void setCustRisk(String custRisk) {
        this.custRisk = custRisk;
    }

    public String getIsPeriod() {
        return isPeriod;
    }

    public void setIsPeriod(String isPeriod) {
        this.isPeriod = isPeriod;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDigitId() {
        return digitId;
    }

    public void setDigitId(String digitId) {
        this.digitId = digitId;
    }

    public String getRiskMsg() {
        return riskMsg;
    }

    public void setRiskMsg(String riskMsg) {
        this.riskMsg = riskMsg;
    }

    public PsnXpadSignInitBean getSignInitBean() {
        return signInitBean;
    }

    public void setSignInitBean(PsnXpadSignInitBean signInitBean) {
        this.signInitBean = signInitBean;
    }

    public PsnXpadSignResultBean getSignResultBean() {
        return signResultBean;
    }

    public void setSignResultBean(PsnXpadSignResultBean signResultBean) {
        this.signResultBean = signResultBean;
    }

    /**
     * 协议选择
     */

    //选择协议名称
    public String protocolName;
    /**
     * 公共
     */
    //钞汇类型
    public String cashRemit = "元";
    //投资期数
    public int totalPeriodNum = 12;
    /**
     * 定投，余额投资
     */
    //定投类型
    public String fixInvestType = "0";
    //定投金额
    public double fixInvestAmount;
    //定赎份额
    public double fixRedeemAmount;
    //定投频率
    public String fixInvestOfen = "1w";
    //定投开始日期
    public String fixInvestDate = "2016/10/27";
    //赎回触发金额
    public String redeemStartedValue;
    //购买触发金额
    public String buyStartedValue;

    //智能投资确认页结果
    public String confirmContent;
    //单期投资金额
    public String investPerPeriod;
    //投资金额模式
    public String investMoneyType;
    //账户保留余额
    public String accountLeastAmount = "";
    //最大购买金额
    public String maxInvestAmount = "";
    //投资总周期(天)
    public int totalPeriodDays;


    //风险匹配结果
    public String productRisk;
    //风险匹配结果
    public String customerRisk;
    //风险匹配结果
    public String riskMessage;
    //确认页数据
    public LinkedHashMap<String, String> confirmDetails = new LinkedHashMap<>();
    //结果页数据
    public Map<String, String> resultDetail = new HashMap<>();
    //只能投资结果页String
    public String secondTitle;

    public int getPeriodDays(String str) {
        int result = 0;
        String unit = str.substring(str.length() - 1, str.length());
        int value = Integer.parseInt(str.substring(0, str.length() - 1));
        if (unit.equals("d")) {
            result = value;
        } else if (unit.equals("w")) {
            result = value * 7;
        } else if (unit.equals("m")) {
            result = value * 30;
        } else if (unit.equals("y")) {
            result = value * 365;
        }
        return result;
    }

    public ProtocolModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.accountList);
        dest.writeString(this.proId);
        dest.writeString(this.productKind);
        dest.writeString(this.prodName);
        dest.writeString(this.curCode);
        dest.writeString(this.subAmount);
        dest.writeString(this.addAmount);
        dest.writeString(this.lowLimitAmount);
        dest.writeString(this.remainCycleCount);
        dest.writeString(this.periodical);
        dest.writeString(this.price);
        dest.writeString(this.priceDate);
        dest.writeString(this.yearlyRR);
        dest.writeString(this.rateDetail);
        dest.writeString(this.prodTimeLimit);
        dest.writeString(this.isLockPeriod);
        dest.writeString(this.productTermType);
        dest.writeString(this.recordNumber);
        dest.writeTypedList(this.protocolList);
        dest.writeParcelable(this.selectedProtocol, flags);
        dest.writeString(this.agrCode);
        dest.writeString(this.charCode);
        dest.writeString(this.amountType);
        dest.writeString(this.minAmount);
        dest.writeString(this.maxAmount);
        dest.writeString(this.unit);
        dest.writeString(this.isControl);
        dest.writeString(this.totalPeriod);
        dest.writeString(this.amount);
        dest.writeParcelable(this.confirmBean, flags);
        dest.writeString(this.accountId);
        dest.writeString(this.serialName);
        dest.writeString(this.serialCode);
        dest.writeString(this.xpadCashRemit);
        dest.writeString(this.amountTypeCode);
        dest.writeString(this.baseAmount);
        dest.writeString(this.dealCode);
        dest.writeString(this.token);
        dest.writeParcelable(this.signInitBean, flags);
        dest.writeParcelable(this.signResultBean, flags);
        dest.writeString(this.riskMatch);
        dest.writeString(this.proRisk);
        dest.writeString(this.custRisk);
        dest.writeString(this.isPeriod);
        dest.writeString(this.productId);
        dest.writeString(this.digitId);
        dest.writeString(this.riskMsg);
        dest.writeString(this.protocolName);
        dest.writeString(this.cashRemit);
        dest.writeInt(this.totalPeriodNum);
        dest.writeString(this.fixInvestType);
        dest.writeDouble(this.fixInvestAmount);
        dest.writeDouble(this.fixRedeemAmount);
        dest.writeString(this.fixInvestOfen);
        dest.writeString(this.fixInvestDate);
        dest.writeString(this.redeemStartedValue);
        dest.writeString(this.buyStartedValue);
        dest.writeString(this.confirmContent);
        dest.writeString(this.investPerPeriod);
        dest.writeString(this.investMoneyType);
        dest.writeString(this.accountLeastAmount);
        dest.writeString(this.maxInvestAmount);
        dest.writeInt(this.totalPeriodDays);
        dest.writeString(this.productRisk);
        dest.writeString(this.customerRisk);
        dest.writeString(this.riskMessage);
        dest.writeSerializable(this.confirmDetails);
        dest.writeInt(this.resultDetail.size());
        for (Map.Entry<String, String> entry : this.resultDetail.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.secondTitle);
    }

    protected ProtocolModel(Parcel in) {
        this.accountList = in.createTypedArrayList(WealthAccountBean.CREATOR);
        this.proId = in.readString();
        this.productKind = in.readString();
        this.prodName = in.readString();
        this.curCode = in.readString();
        this.subAmount = in.readString();
        this.addAmount = in.readString();
        this.lowLimitAmount = in.readString();
        this.remainCycleCount = in.readString();
        this.periodical = in.readString();
        this.price = in.readString();
        this.priceDate = in.readString();
        this.yearlyRR = in.readString();
        this.rateDetail = in.readString();
        this.prodTimeLimit = in.readString();
        this.isLockPeriod = in.readString();
        this.productTermType = in.readString();
        this.recordNumber = in.readString();
        this.protocolList = in.createTypedArrayList(InvestTreatyBean.CREATOR);
        this.selectedProtocol = in.readParcelable(InvestTreatyBean.class.getClassLoader());
        this.agrCode = in.readString();
        this.charCode = in.readString();
        this.amountType = in.readString();
        this.minAmount = in.readString();
        this.maxAmount = in.readString();
        this.unit = in.readString();
        this.isControl = in.readString();
        this.totalPeriod = in.readString();
        this.amount = in.readString();
        this.confirmBean = in.readParcelable(ProtocolIntelligentConfirmBean.class.getClassLoader());
        this.accountId = in.readString();
        this.serialName = in.readString();
        this.serialCode = in.readString();
        this.xpadCashRemit = in.readString();
        this.amountTypeCode = in.readString();
        this.baseAmount = in.readString();
        this.dealCode = in.readString();
        this.token = in.readString();
        this.signInitBean = in.readParcelable(PsnXpadSignInitBean.class.getClassLoader());
        this.signResultBean = in.readParcelable(PsnXpadSignResultBean.class.getClassLoader());
        this.riskMatch = in.readString();
        this.proRisk = in.readString();
        this.custRisk = in.readString();
        this.isPeriod = in.readString();
        this.productId = in.readString();
        this.digitId = in.readString();
        this.riskMsg = in.readString();
        this.protocolName = in.readString();
        this.cashRemit = in.readString();
        this.totalPeriodNum = in.readInt();
        this.fixInvestType = in.readString();
        this.fixInvestAmount = in.readDouble();
        this.fixRedeemAmount = in.readDouble();
        this.fixInvestOfen = in.readString();
        this.fixInvestDate = in.readString();
        this.redeemStartedValue = in.readString();
        this.buyStartedValue = in.readString();
        this.confirmContent = in.readString();
        this.investPerPeriod = in.readString();
        this.investMoneyType = in.readString();
        this.accountLeastAmount = in.readString();
        this.maxInvestAmount = in.readString();
        this.totalPeriodDays = in.readInt();
        this.productRisk = in.readString();
        this.customerRisk = in.readString();
        this.riskMessage = in.readString();
        this.confirmDetails = (LinkedHashMap<String, String>) in.readSerializable();
        int resultDetailSize = in.readInt();
        this.resultDetail = new HashMap<String, String>(resultDetailSize);
        for (int i = 0; i < resultDetailSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.resultDetail.put(key, value);
        }
        this.secondTitle = in.readString();
    }

    public static final Creator<ProtocolModel> CREATOR = new Creator<ProtocolModel>() {
        @Override
        public ProtocolModel createFromParcel(Parcel source) {
            return new ProtocolModel(source);
        }

        @Override
        public ProtocolModel[] newArray(int size) {
            return new ProtocolModel[size];
        }
    };
}
