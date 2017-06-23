package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 10:52
 * 描述：支付签约预交易
 */
public class PsnLoanPaymentSignVerifyParams {

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
     * 签约账户Id
     */
    private String signAccountId;
    /**
     * 签约账户
     */
    private String signAccountNum;
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
     * 签约期限1-3个月，上送1：一次性还本付息 ；签约期限4-12个月，上送2：按月付息到期还本
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
}
