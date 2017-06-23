package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm.PsnCrcdDividedPayConsumeConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultParams;
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
 * 创建时间：2016/11/23.
 * 用途：信用卡消费分期Model
 */
public class ConsumeInstallmentModel implements Serializable, Parcelable , BaseFillInfoBean {
    /**
     * 预交易请求
     */
    private int accountId;      //账户标识
    private String currencyCode; //币种
    private BigDecimal amount;   //分期金额
    private int divPeriod;       //分期期数
    private String chargeMode;   //手续费收取方式
    private int transId;         //交易序号
    private String mainAcctId;   //账户id
    private int sequence;        //周期号
    private String crcdFinalFour; //信用卡卡号后四位

    private String conversationId;//会话id
    private String _combinId;  //安全因子组合id




    /**
     * 用于保存安全因子列表及默认安全工具
     */
    private SecurityFactorModel securityFactorModel;//请求安全因子返回结果


    /**
     * 预交易请求结果
     */
    private BigDecimal firstInAmount; //分期后首期应还
    private BigDecimal instmtCharge; //手续费
    private BigDecimal restPerTimeInAmount; //分期后每期应还
    private String smcTrigerInterval;//手机验证码有效时间
    private String _plainData;//CA加签数据XML报文
    private String _certDN;//CA的DN值
    @ListItemType(instantiate = FactorBean.class)
    private List<FactorBean> factorList;//安全因子数组

    /**
     * 提交交易需要的其他信息
     *  private String _signedData;
     private String deviceInfo;
     private String Smc;
     private String devicePrint;
     private String Smc_RC;
     private String Otp_RC;
     private String deviceInfo_RC;
     private String state;
     private String activ;
     *
     */
    private String token;//防重标识
    private String Smc;//手机交易码
    private String Otp;//动态口令
    private String Smc_RC;//手机交易码
    private String Otp_RC;//动态口令
    private String activ;//	控件取值	string
    private String	state;//	控件取值	string
    private String	deviceInfo	;//
    private String  deviceInfo_RC;
    private String	devicePrint	;//
    private String _signedData;// CA认证生成的密文
    private String acctNum;//信用卡卡号

    private String CombinName;

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
     * 数据项的get与set
     */

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

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public String getMainAcctId() {
        return mainAcctId;
    }

    public void setMainAcctId(String mainAcctId) {
        this.mainAcctId = mainAcctId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getCrcdFinalFour() {
        return crcdFinalFour;
    }

    public void setCrcdFinalFour(String crcdFinalFour) {
        this.crcdFinalFour = crcdFinalFour;
    }

    @Override
    public String getCombinName() {
        return CombinName;
    }

    @Override
    public void setCombinName(String combinName) {
        CombinName = combinName;
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

    public BigDecimal getFirstInAmount() {
        return firstInAmount;
    }

    public void setFirstInAmount(BigDecimal firstInAmount) {
        this.firstInAmount = firstInAmount;
    }

    public BigDecimal getInstmtCharge() {
        return instmtCharge;
    }

    public void setInstmtCharge(BigDecimal instmtCharge) {
        this.instmtCharge = instmtCharge;
    }

    public BigDecimal getRestPerTimeInAmount() {
        return restPerTimeInAmount;
    }

    public void setRestPerTimeInAmount(BigDecimal restPerTimeInAmount) {
        this.restPerTimeInAmount = restPerTimeInAmount;
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

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public List<FactorBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorBean> factorList) {
        this.factorList = factorList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public SecurityFactorModel getSecurityFactorModel() {
        return securityFactorModel;
    }

    public void setSecurityFactorModel(SecurityFactorModel securityFactorModel) {
        this.securityFactorModel = securityFactorModel;
    }

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }



    public PsnCrcdDividedPayConsumeConfirmParams getConfirmParams() {
        /**
         *    private int accountId;
         private String currencyCode;
         private BigDecimal amount;
         private int divPeriod;
         private String chargeMode;
         private int transId;
         private String mainAcctId;
         private int sequence;
         private String crcdFinalFour;
         private String _combinId;
         private String conversationId;
         */
        PsnCrcdDividedPayConsumeConfirmParams params = new PsnCrcdDividedPayConsumeConfirmParams();


        return BeanConvertor.toBean(this, params);
    }

    public PsnCrcdDividedPayConsumeResultParams getResultParams() {
        PsnCrcdDividedPayConsumeResultParams params = new PsnCrcdDividedPayConsumeResultParams();

        return BeanConvertor.toBean(this, params);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.accountId);
        dest.writeString(this.currencyCode);
        dest.writeSerializable(this.amount);
        dest.writeInt(this.divPeriod);
        dest.writeString(this.chargeMode);
        dest.writeInt(this.transId);
        dest.writeString(this.mainAcctId);
        dest.writeInt(this.sequence);
        dest.writeString(this.crcdFinalFour);
        dest.writeString(this.conversationId);
        dest.writeString(this._combinId);
        dest.writeSerializable(this.securityFactorModel);
        dest.writeSerializable(this.firstInAmount);
        dest.writeSerializable(this.instmtCharge);
        dest.writeSerializable(this.restPerTimeInAmount);
        dest.writeString(this.smcTrigerInterval);
        dest.writeString(this._plainData);
        dest.writeString(this._certDN);
        dest.writeList(this.factorList);
        dest.writeString(this.token);
        dest.writeString(this.Smc);
        dest.writeString(this.Otp);
        dest.writeString(this.Smc_RC);
        dest.writeString(this.Otp_RC);
        dest.writeString(this.activ);
        dest.writeString(this.state);
        dest.writeString(this.deviceInfo);
        dest.writeString(this.deviceInfo_RC);
        dest.writeString(this.devicePrint);
        dest.writeString(this._signedData);
        dest.writeString(this.acctNum);
    }

    public ConsumeInstallmentModel() {
    }

    protected ConsumeInstallmentModel(Parcel in) {
        this.accountId = in.readInt();
        this.currencyCode = in.readString();
        this.amount = (BigDecimal) in.readSerializable();
        this.divPeriod = in.readInt();
        this.chargeMode = in.readString();
        this.transId = in.readInt();
        this.mainAcctId = in.readString();
        this.sequence = in.readInt();
        this.crcdFinalFour = in.readString();
        this.conversationId = in.readString();
        this._combinId = in.readString();
        this.securityFactorModel = (SecurityFactorModel) in.readSerializable();
        this.firstInAmount = (BigDecimal) in.readSerializable();
        this.instmtCharge = (BigDecimal) in.readSerializable();
        this.restPerTimeInAmount = (BigDecimal) in.readSerializable();
        this.smcTrigerInterval = in.readString();
        this._plainData = in.readString();
        this._certDN = in.readString();
        this.factorList = new ArrayList<FactorBean>();
        in.readList(this.factorList, FactorListBean.class.getClassLoader());
        this.token = in.readString();
        this.Smc = in.readString();
        this.Otp = in.readString();
        this.Smc_RC = in.readString();
        this.Otp_RC = in.readString();
        this.activ = in.readString();
        this.state = in.readString();
        this.deviceInfo = in.readString();
        this.deviceInfo_RC = in.readString();
        this.devicePrint = in.readString();
        this._signedData = in.readString();
        this.acctNum = in.readString();
    }

    public static final Parcelable.Creator<ConsumeInstallmentModel> CREATOR = new Parcelable.Creator<ConsumeInstallmentModel>() {
        @Override
        public ConsumeInstallmentModel createFromParcel(Parcel source) {
            return new ConsumeInstallmentModel(source);
        }

        @Override
        public ConsumeInstallmentModel[] newArray(int size) {
            return new ConsumeInstallmentModel[size];
        }
    };
}
