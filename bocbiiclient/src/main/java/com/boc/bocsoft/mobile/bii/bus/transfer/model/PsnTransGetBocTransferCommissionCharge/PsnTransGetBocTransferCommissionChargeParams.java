package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge;

/**
 * 中行内转账费用试算
 * 说明：关联账户转账、中行内转账、中行内定向转账、主动收款-付款，
 * 手机号转账、在线批量转账，以上交易均调用此接口进行手续费试算
 * Created by WM on 2016/6/12.
 */
public class PsnTransGetBocTransferCommissionChargeParams {


    /**
     * 服务id
     * 关联账户转账：PB021
     * 中行内转账：PB031
     * 中行内定向转账：PB033
     * 主动收款-付款：PB037C
     * 手机号转账：PB035
     */
    private String conversationId;

    private String serviceId;
    /**
     * 转出账户id
     */
    private String fromAccountId;
    /**
     * 转入账户id
     * 仅当关联账户转账时，上送此值，数字，非空
     */
    private String toAccountId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 小于等于18位，必须是按币种格式化的金额，例如人民币：58,355.00
     */
    private String amount;
    /**
     * 最多25个汉字或50个字符
     */
    private String remark;
    /**
     * 钞汇标识
     * 仅当关联账户转账、主动收款-付款时，上送此值，且此时为必输项
     * -：00
     * 现钞：01
     * 现汇：02
     */
    private String cashRemit;

    /**
     * 指令序号
     * 仅当主动收款-付款时，上送此值
     */
    private String notifyId;


    /**
     * 收款人账号
     * 仅当中行内转账、中行内定向转账、手机号转账时，送此值
     */
    private String payeeActno;
    /**
     * 转入账户名称
     * 仅当中行内转账、中行内定向转账、手机号转账时，送此值
     */
    private String payeeName;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
}
