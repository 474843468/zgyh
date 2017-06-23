package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm.PsnCrcdDividedPayBillSetConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lq7090
 * 创建时间：2016/12/9.
 * 用途：信用卡账单分期Model
 */
public class BillInstallmentModel implements Serializable, Parcelable, BaseFillInfoBean {

    private String acctNum;//信用卡卡号

    public String getAcctNum() {
        return acctNum;
    }

    /**
     * 用于保存安全因子列表及默认安全工具
     */
    private SecurityFactorModel securityFactorModel;//请求安全因子返回结果

    public SecurityFactorModel getSecurityFactorModel() {
        return securityFactorModel;
    }

    public void setSecurityFactorModel(SecurityFactorModel securityFactorModel) {
        this.securityFactorModel = securityFactorModel;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    //可分期金额上限
    private BigDecimal upInstmtAmount;
    //可分期金额下限
    private BigDecimal lowInstmtAmount;



    private int accountId;
    private String currencyCode;
    private BigDecimal amount;
    private int divPeriod;
    private String _combinId;
    private String chargeMode;
    private BigDecimal minimalAmountNow;
    private String smcTrigerInterval;

    @ListItemType(instantiate = FactorBean.class)
    private List<FactorBean> factorList;

    private BigDecimal lowAmt;
    private BigDecimal instmtCharge;
    private BigDecimal firstInAmount;
    private BigDecimal restPerTimeInAmount;
    private BigDecimal restAmount;
    private BigDecimal minAmount;
    private String crcdFinalFour;
    private String Smc;
    private String Otp;
    private String token;
    private String _plainData;
    private String _certDN;
    private String _signedData;
    private String conversationId;
    private String Smc_RC;//手机交易码
    private String Otp_RC;//动态口令
    private String deviceInfo;//设备信息
    private String deviceInfo_RC;
    private String devicePrint;//
    private String activ;//	控件取值	string
    private String state;//	控件取值	string
    private String CombinName;


    public BigDecimal getUpInstmtAmount() {
        return upInstmtAmount;
    }

    public void setUpInstmtAmount(BigDecimal upInstmtAmount) {
        this.upInstmtAmount = upInstmtAmount;
    }

    public BigDecimal getLowInstmtAmount() {
        return lowInstmtAmount;
    }

    public void setLowInstmtAmount(BigDecimal lowInstmtAmount) {
        this.lowInstmtAmount = lowInstmtAmount;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(int divPeriod) {
        this.divPeriod = divPeriod;
    }

    @Override
    public void setCombinName(String combinName) {
        CombinName = combinName;
    }

    @Override
    public String getCombinName() {

        return CombinName;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public BigDecimal getMinimalAmountNow() {
        return minimalAmountNow;
    }

    public void setMinimalAmountNow(BigDecimal minimalAmountNow) {
        this.minimalAmountNow = minimalAmountNow;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public List<FactorBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorBean> factorList) {
        this.factorList = factorList;
    }

    public BigDecimal getLowAmt() {
        return lowAmt;
    }

    public void setLowAmt(BigDecimal lowAmt) {
        this.lowAmt = lowAmt;
    }

    public BigDecimal getInstmtCharge() {
        return instmtCharge;
    }

    public void setInstmtCharge(BigDecimal instmtCharge) {
        this.instmtCharge = instmtCharge;
    }

    public BigDecimal getFirstInAmount() {
        return firstInAmount;
    }

    public void setFirstInAmount(BigDecimal firstInAmount) {
        this.firstInAmount = firstInAmount;
    }

    public BigDecimal getRestPerTimeInAmount() {
        return restPerTimeInAmount;
    }

    public void setRestPerTimeInAmount(BigDecimal restPerTimeInAmount) {
        this.restPerTimeInAmount = restPerTimeInAmount;
    }

    public BigDecimal getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(BigDecimal restAmount) {
        this.restAmount = restAmount;
    }

    public BigDecimal getMinAmount() {
        return minimalAmountNow;
    }//此处用于传预交易获取的异名相同字段

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public String getCrcdFinalFour() {
        return crcdFinalFour;
    }

    public void setCrcdFinalFour(String crcdFinalFour) {
        this.crcdFinalFour = crcdFinalFour;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    /**
     * 用于从Model中获取预交易请求数据
     *
     * @return params
     */

    public PsnCrcdDividedPayBillSetResultParams getResultParams() {
        PsnCrcdDividedPayBillSetResultParams params = new PsnCrcdDividedPayBillSetResultParams();
        return BeanConvertor.toBean(this, params);


    }

    /**
     * 用于从Model中获取提交交易数据
     *
     * @return params
     */
    public PsnCrcdDividedPayBillSetConfirmParams getConfirmParams() {
        PsnCrcdDividedPayBillSetConfirmParams params = new PsnCrcdDividedPayBillSetConfirmParams();
        return BeanConvertor.toBean(this, params);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.acctNum);
        dest.writeSerializable(this.securityFactorModel);
        dest.writeSerializable(this.upInstmtAmount);
        dest.writeSerializable(this.lowInstmtAmount);
        dest.writeInt(this.accountId);
        dest.writeString(this.currencyCode);
        dest.writeSerializable(this.amount);
        dest.writeInt(this.divPeriod);
        dest.writeString(this._combinId);
        dest.writeString(this.chargeMode);
        dest.writeSerializable(this.minimalAmountNow);
        dest.writeString(this.smcTrigerInterval);
        dest.writeList(this.factorList);
        dest.writeSerializable(this.lowAmt);
        dest.writeSerializable(this.instmtCharge);
        dest.writeSerializable(this.firstInAmount);
        dest.writeSerializable(this.restPerTimeInAmount);
        dest.writeSerializable(this.restAmount);
        dest.writeSerializable(this.minAmount);
        dest.writeString(this.crcdFinalFour);
        dest.writeString(this.Smc);
        dest.writeString(this.Otp);
        dest.writeString(this.token);
        dest.writeString(this._plainData);
        dest.writeString(this._certDN);
        dest.writeString(this._signedData);
        dest.writeString(this.conversationId);
        dest.writeString(this.Smc_RC);
        dest.writeString(this.Otp_RC);
        dest.writeString(this.deviceInfo);
        dest.writeString(this.deviceInfo_RC);
        dest.writeString(this.devicePrint);
        dest.writeString(this.activ);
        dest.writeString(this.state);
    }

    public BillInstallmentModel() {
    }

    protected BillInstallmentModel(Parcel in) {
        this.acctNum = in.readString();
        this.securityFactorModel = (SecurityFactorModel) in.readSerializable();
        this.upInstmtAmount = (BigDecimal) in.readSerializable();
        this.lowInstmtAmount = (BigDecimal) in.readSerializable();
        this.accountId = in.readInt();
        this.currencyCode = in.readString();
        this.amount = (BigDecimal) in.readSerializable();
        this.divPeriod = in.readInt();
        this._combinId = in.readString();
        this.chargeMode = in.readString();
        this.minimalAmountNow = (BigDecimal) in.readSerializable();
        this.smcTrigerInterval = in.readString();
        this.factorList = new ArrayList<FactorBean>();
        in.readList(this.factorList, FactorListBean.class.getClassLoader());
        this.lowAmt = (BigDecimal) in.readSerializable();
        this.instmtCharge = (BigDecimal) in.readSerializable();
        this.firstInAmount = (BigDecimal) in.readSerializable();
        this.restPerTimeInAmount = (BigDecimal) in.readSerializable();
        this.restAmount = (BigDecimal) in.readSerializable();
        this.minAmount = (BigDecimal) in.readSerializable();
        this.crcdFinalFour = in.readString();
        this.Smc = in.readString();
        this.Otp = in.readString();
        this.token = in.readString();
        this._plainData = in.readString();
        this._certDN = in.readString();
        this._signedData = in.readString();
        this.conversationId = in.readString();
        this.Smc_RC = in.readString();
        this.Otp_RC = in.readString();
        this.deviceInfo = in.readString();
        this.deviceInfo_RC = in.readString();
        this.devicePrint = in.readString();
        this.activ = in.readString();
        this.state = in.readString();
    }

    public static final Parcelable.Creator<BillInstallmentModel> CREATOR = new Parcelable.Creator<BillInstallmentModel>() {
        @Override
        public BillInstallmentModel createFromParcel(Parcel source) {
            return new BillInstallmentModel(source);
        }

        @Override
        public BillInstallmentModel[] newArray(int size) {
            return new BillInstallmentModel[size];
        }
    };
}
