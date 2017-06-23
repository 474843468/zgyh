package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model;

import android.os.Parcel;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.NotConvert;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 11:29
 * 描述：
 */
public class PaymentSignViewModel implements BaseFillInfoBean {
    /**
     * 1:签约
     * 2：修改
     * 3：解约
     */
    private int signType;
    private String conversationId;

    /**
     * 额度编号/贷款账号
     * 当“额度/账户签约标识”上送为F时，此字段上送额度编号；
     * 当“额度/账户签约标识”上送为A时，此字段上送贷款账号
     */
    private String quoteOrActNo;
    /**
     * 额度/账户签约标识
     * 取值范围：
     * 额度为F
     * 账户为A
     */
    private String quoteFlag;

    /**
     * 签约类型
     * 01：WLCF
     * 02：PLCF
     * 03: 账户签约
     */
    private String quoteType;

    /**
     * 还款账户
     */
    private String payAccount;
    /**
     * 签约账户Id
     */
    private String signAccountId;
    /**
     * 签约账户
     */
    private String signAccountNum;

    /**
     * 原账户
     * 若不修改签约账户，原签约账户与签约账户相同
     */
    private String oldsignAccount;

    /**
     * 最小放款金额
     * 前端固定送：1000.00
     */
    private String minLoanAmount;
    /**
     * 最小放款金额的币种
     * 001：人民币元
     */
    private String minLoanAmtCur;

    /**
     * 用款偏好
     * 贷款：BOR
     * 存款：DEP
     */
    private String usePref;
    /**
     * 签约期限
     * 当“额度/账户签约标识”上送为F时，此字段必选；
     * 01：1月
     * 02：2月
     * 03：3月
     * 04：4月
     * 05：5月
     * 06：6月
     * 07：7月
     * 08：8月
     * 09：9月
     * 10：10月
     * 11：11月
     * 12：12月
     */
    private String signPeriod;
    /**
     * 还款方式
     * 当“额度/账户签约标识”上送为F时，此字段必选；
     * 当“额度/账户签约标识”上送为F时，此字段必选；
     * 1 一次性还本付息
     * 2 按月还息到期一次性还本
     * 3 按月等额本息
     * 4 按月等额本金；
     * 签约期限1-3个月，上送1：到期一次性还本付息 ；签约期限4-12个月，上送2：按月付息 到期还本
     */
    private String repayFlag;

    /**
     * 还款周期
     * 当“额度/账户签约标识”上送为F时，此字段必选；
     * 签约期限1-3个月，还款周期显示为“到期时”上送值“98”；
     * 签约期限4-12个月，还款周期显示“按月”上送值“01”
     */
    private String payCycle;
    /**
     * 产品大类
     * 当“额度/账户签约标识”上送为F时，此字段必选；
     * 中银E贷送固定值：1160
     */
    private String productBigType;
    /**
     * 产品子类
     * 当“额度/账户签约标识”上送为F时，此字段必选；
     * 中银E贷送固定值：1001
     */
    private String productCatType;
    /**
     * 安全工具组合
     */
    private String _combinId;
    /**
     * 安全工具名称
     */
    @NotConvert
    private String combinName;

    private String rate;

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    @Override
    public String getConversationId() {
        return conversationId;
    }

    @Override
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuoteOrActNo() {
        return quoteOrActNo;
    }

    public void setQuoteOrActNo(String quoteOrActNo) {
        this.quoteOrActNo = quoteOrActNo;
    }

    public String getQuoteFlag() {
        return quoteFlag;
    }

    public void setQuoteFlag(String quoteFlag) {
        this.quoteFlag = quoteFlag;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getSignAccountId() {
        return signAccountId;
    }

    public void setSignAccountId(String signAccountId) {
        this.signAccountId = signAccountId;
    }

    public String getSignAccountNum() {
        return signAccountNum;
    }

    public void setSignAccountNum(String signAccountNum) {
        this.signAccountNum = signAccountNum;
    }

    public String getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(String minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public String getMinLoanAmtCur() {
        return minLoanAmtCur;
    }

    public void setMinLoanAmtCur(String minLoanAmtCur) {
        this.minLoanAmtCur = minLoanAmtCur;
    }

    public String getUsePref() {
        return usePref;
    }

    public void setUsePref(String usePref) {
        this.usePref = usePref;
    }

    public String getSignPeriod() {
        return signPeriod;
    }

    public void setSignPeriod(String signPeriod) {
        this.signPeriod = signPeriod;
    }

    public String getRepayFlag() {
        return repayFlag;
    }

    public void setRepayFlag(String repayFlag) {
        this.repayFlag = repayFlag;
    }

    public String getPayTypeString() {
        return ApplicationContext.getInstance()
                                 .getString("1".equals(getRepayFlag())
                                         ? R.string.boc_eloan_payment_paytype_1
                                         : R.string.boc_eloan_payment_paytype_2);
    }

    public String getPayCycle() {
        return payCycle;
    }

    public void setPayCycle(String payCycle) {
        this.payCycle = payCycle;
    }

    public String getProductBigType() {
        return productBigType;
    }

    public void setProductBigType(String productBigType) {
        this.productBigType = productBigType;
    }

    public String getProductCatType() {
        return productCatType;
    }

    public void setProductCatType(String productCatType) {
        this.productCatType = productCatType;
    }

    @Override
    public String get_combinId() {
        return _combinId;
    }

    @Override
    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    @Override
    public String getCombinName() {
        return combinName;
    }

    @Override
    public void setCombinName(String combinName) {
        this.combinName = combinName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOldsignAccount() {
        return oldsignAccount;
    }

    public void setOldsignAccount(String oldsignAccount) {
        this.oldsignAccount = oldsignAccount;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.signType);
        dest.writeString(this.conversationId);
        dest.writeString(this.quoteOrActNo);
        dest.writeString(this.quoteFlag);
        dest.writeString(this.quoteType);
        dest.writeString(this.payAccount);
        dest.writeString(this.signAccountId);
        dest.writeString(this.signAccountNum);
        dest.writeString(this.oldsignAccount);
        dest.writeString(this.minLoanAmount);
        dest.writeString(this.minLoanAmtCur);
        dest.writeString(this.usePref);
        dest.writeString(this.signPeriod);
        dest.writeString(this.repayFlag);
        dest.writeString(this.payCycle);
        dest.writeString(this.productBigType);
        dest.writeString(this.productCatType);
        dest.writeString(this._combinId);
        dest.writeString(this.combinName);
        dest.writeString(this.rate);
    }

    public PaymentSignViewModel() {}

    protected PaymentSignViewModel(Parcel in) {
        this.signType = in.readInt();
        this.conversationId = in.readString();
        this.quoteOrActNo = in.readString();
        this.quoteFlag = in.readString();
        this.quoteType = in.readString();
        this.payAccount = in.readString();
        this.signAccountId = in.readString();
        this.signAccountNum = in.readString();
        this.oldsignAccount = in.readString();
        this.minLoanAmount = in.readString();
        this.minLoanAmtCur = in.readString();
        this.usePref = in.readString();
        this.signPeriod = in.readString();
        this.repayFlag = in.readString();
        this.payCycle = in.readString();
        this.productBigType = in.readString();
        this.productCatType = in.readString();
        this._combinId = in.readString();
        this.combinName = in.readString();
        this.rate = in.readString();
    }

    public static final Creator<PaymentSignViewModel> CREATOR =
            new Creator<PaymentSignViewModel>() {
                @Override
                public PaymentSignViewModel createFromParcel(
                        Parcel source) {return new PaymentSignViewModel(source);}

                @Override
                public PaymentSignViewModel[] newArray(
                        int size) {return new PaymentSignViewModel[size];}
            };
}
