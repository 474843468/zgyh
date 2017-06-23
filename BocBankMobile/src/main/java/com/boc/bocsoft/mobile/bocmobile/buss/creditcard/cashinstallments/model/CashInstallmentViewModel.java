package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.model;

import android.os.Parcel;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cry7096 on 2016/11/27.
 */
public class CashInstallmentViewModel implements BaseFillInfoBean {

    /**
     * fromAccountId : 转出账户ID
     * toAccountId : 存入账户ID
     * toCardNo : 存入卡号
     * fromCardNo : 转出卡号
     * currency : 币种
     * divAmount : 分期金额
     * divPeriod : 分期期数
     * chargeMode : 手续费收取方式
     * divCharge : 分期手续费
     * divRate : 分期手续费率
     * firstCharge : 首期手续费金额
     * perCharge : 后期每期手续费金额
     * firstPayAmount : 分期后每期应还金额-首期
     * perPayAmount : 分期后每期应还金额-后每期
     * _combinId : 安全因子组合id安全因子组合id
     * conversationId : 回话ID
     * _certDN : CA的DN值
     * smcTrigerInterval : 安全因子数组
     * _plainData : CA加签数据XML报文
     * factorList : 安全因子列表
     * token : 防重机制
     * activ : 200102017-版本控制-前端控件加密默认就上送这两个字段？
     * state : TUFDPTZjLTBiLTg0LWE3LTE1LWUzO0lQPTIyLjExLjI2LjY1O0RJU0tJRD0wMDAwNDgyMztCT0NH
     * VUlEPXs0MEUyQkZENy01RkM4LTQ5RkUtQjRFOS0yOEQzNTFBNDg5QUR9O1NUQVRFQ09ERT0wNDkw
     * MDAwMTs=   前端控件加密默认就上送这两个字段？
     * Smc : 手机交易码
     * Smc_RC : V4IABa5YQCMCWZwRjx0wNkvyxWdtWRMYmme1dZVYzTVzcZlzKJWpUk+cl/tvcZMjZCP3mNnuT+Nr
     * SjhEAlKGUdnwOGx8hC36qPSqSKUeS1wRjaVgk/dc/GVZuuNmRdU+NCSwthkOw8AFGV9gtPPXLA==
     * Otp : 动态口令
     * _signedData : CA认证生成的密文
     * transactionId : 网银交易序列号
     * availableBalance : 可用额度上限
     */

    private String fromAccountId;
    private String toAccountId;
    private String toCardNo;
    private String fromCardNo;
    private String currency;
    private String divAmount;
    private String divPeriod;
    private String chargeMode;
    private String divCharge;
    private String divRate;
    private String firstCharge;
    private String perCharge;
    private String firstPayAmount;
    private String perPayAmount;
    private String conversationId;
    private String _certDN;
    private String smcTrigerInterval;
    private String _plainData;
    private List<FactorListBean> factorList;
    private String token;
    private String activ;
    private String state;
    private String Smc;
    private String Smc_RC;
    private String Otp;
    private String _signedData;
    private String CombinName;
    private String _combinId;
    private String	deviceInfo	;
    private String  deviceInfo_RC;
    private String devicePrint;
    private String transactionId;
    private String availableBalance;

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    @Override
    public void setCombinName(String combinName) {
        CombinName = combinName;
    }

    @Override
    public String getCombinName() {

        return CombinName;
    }

    public String getToken() {
        return token;
    }

    public String getActiv() {
        return activ;
    }

    public String getState() {
        return state;
    }

    public String getSmc() {
        return Smc;
    }

    public String get_signedData() {
        return _signedData;
    }

    public String getOtp() {
        return Otp;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public static class FactorListBean {
        /**
         * name : 安全因子名称
         * type : 安全因子类型
         */
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public void setFromCardNo(String fromCardNo) {
        this.fromCardNo = fromCardNo;
    }

    public String getFromCardNo() {
        return fromCardNo;
    }

    public void setFirstCharge(String firstCharge) {
        this.firstCharge = firstCharge;
    }

    public void setPerCharge(String perCharge) {
        this.perCharge = perCharge;
    }

    public String getFirstCharge() {
        return firstCharge;
    }

    public String getPerCharge() {
        return perCharge;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToCardNo() {
        return toCardNo;
    }

    public void setToCardNo(String toCardNo) {
        this.toCardNo = toCardNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDivAmount() {
        return divAmount;
    }

    public void setDivAmount(String divAmount) {
        this.divAmount = divAmount;
    }

    public String getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(String divPeriod) {
        this.divPeriod = divPeriod;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getDivCharge() {
        return divCharge;
    }

    public void setDivCharge(String divCharge) {
        this.divCharge = divCharge;
    }

    public String getDivRate() {
        return divRate;
    }

    public void setDivRate(String divRate) {
        this.divRate = divRate;
    }

    public String getFirstPayAmount() {
        return firstPayAmount;
    }

    public void setFirstPayAmount(String firstPayAmount) {
        this.firstPayAmount = firstPayAmount;
    }

    public String getPerPayAmount() {
        return perPayAmount;
    }

    public void setPerPayAmount(String perPayAmount) {
        this.perPayAmount = perPayAmount;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fromAccountId);
        dest.writeString(this.toAccountId);
        dest.writeString(this.toCardNo);
        dest.writeString(this.fromCardNo);
        dest.writeString(this.currency);
        dest.writeString(this.divAmount);
        dest.writeString(this.divPeriod);
        dest.writeString(this.chargeMode);
        dest.writeString(this.divCharge);
        dest.writeString(this.divRate);
        dest.writeString(this.firstCharge);
        dest.writeString(this.perCharge);
        dest.writeString(this.firstPayAmount);
        dest.writeString(this.perPayAmount);
        dest.writeString(this.conversationId);
        dest.writeString(this._certDN);
        dest.writeString(this.smcTrigerInterval);
        dest.writeString(this._plainData);
        dest.writeList(this.factorList);
        dest.writeString(this.token);
        dest.writeString(this.activ);
        dest.writeString(this.state);
        dest.writeString(this.Smc);
        dest.writeString(this.Smc_RC);
        dest.writeString(this.Otp);
        dest.writeString(this._signedData);
        dest.writeString(this.CombinName);
        dest.writeString(this._combinId);
    }

    public CashInstallmentViewModel() {
    }

    protected CashInstallmentViewModel(Parcel in) {
        this.fromAccountId = in.readString();
        this.toAccountId = in.readString();
        this.toCardNo = in.readString();
        this.fromCardNo = in.readString();
        this.currency = in.readString();
        this.divAmount = in.readString();
        this.divPeriod = in.readString();
        this.chargeMode = in.readString();
        this.divCharge = in.readString();
        this.divRate = in.readString();
        this.firstCharge = in.readString();
        this.perCharge = in.readString();
        this.firstPayAmount = in.readString();
        this.perPayAmount = in.readString();
        this.conversationId = in.readString();
        this._certDN = in.readString();
        this.smcTrigerInterval = in.readString();
        this._plainData = in.readString();
        this.factorList = new ArrayList<FactorListBean>();
        in.readList(this.factorList, FactorListBean.class.getClassLoader());
        this.token = in.readString();
        this.activ = in.readString();
        this.state = in.readString();
        this.Smc = in.readString();
        this.Smc_RC = in.readString();
        this.Otp = in.readString();
        this._signedData = in.readString();
        this.CombinName = in.readString();
        this._combinId = in.readString();
    }

    public static final Creator<CashInstallmentViewModel> CREATOR = new Creator<CashInstallmentViewModel>() {
        @Override
        public CashInstallmentViewModel createFromParcel(Parcel source) {
            return new CashInstallmentViewModel(source);
        }

        @Override
        public CashInstallmentViewModel[] newArray(int size) {
            return new CashInstallmentViewModel[size];
        }
    };
}
