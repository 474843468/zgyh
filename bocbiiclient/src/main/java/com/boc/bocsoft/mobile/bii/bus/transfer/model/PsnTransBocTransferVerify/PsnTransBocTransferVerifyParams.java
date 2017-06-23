package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify;

/**
 * Created by xdy on 2016/6/29.
 */
public class PsnTransBocTransferVerifyParams {

    /**
     * 转出账户Id
     */
    private String fromAccountId;
    /**
     * 收款人账户号码
     */
    private String payeeActno;
    /**
     * 执行类型
     * 0：立即执行1：预约日期执行2：预约周期执行
     */
    private String executeType;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 附言备注
     */
    private String remark;
    private String currency;
    /**
     * 收款人名
     */
    private String payeeName;
    /**
     * 转账金额
     * 必须是按币种格式化的金额，例如人民币：58,355.00
     */
    private String amount;
    /**
     * 汇款用途，8个汉字以内
     */
    private String remittanceInfo;
    /**
     * 执行日期
     * 预约日期执行必填，其他可以不填
     * 格式:yyyy/mm/dd
     */
    private String executeDate;
    /**
     * 起始日期
     * 预约周期执行：不填。其他可以不填
     * 格式:yyyy/mm/dd
     */
    private String startDate;
    /**
     * 结束日期
     * 预约周期执行：不填。其他可以不填
     * 格式:yyyy/mm/dd
     */
    private String endDate;
    /**
     * 周期  W：单周；D：双周；M：月
     */
    private String cycleSelect;
    private String payeeId;
    /**
     * 安全因子组合Id
     */
    private String _combinId;

    private String conversationId;

    private String token;

    private String serviceId;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCycleSelect() {
        return cycleSelect;
    }

    public void setCycleSelect(String cycleSelect) {
        this.cycleSelect = cycleSelect;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
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
