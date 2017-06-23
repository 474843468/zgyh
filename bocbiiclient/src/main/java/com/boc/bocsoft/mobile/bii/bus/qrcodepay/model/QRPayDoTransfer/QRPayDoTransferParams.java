package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer;

/**
 * 转账交易（新增）
 * Created by fanbin on 16/10/8.
 */
public class QRPayDoTransferParams {
    //网银账户流水号   不上送则去默认银行卡
    private String actSeq;
    //支付密码 控件加密结果1，小额免密时无需上送，否则必须上送
    private String password;
    //支付密码 控件加密结果2，小额免密时无需上送，否则必须上送
    private String password_RC;
    //转账金额 浮点数：整数部分最多13位，小数部分固定为两位。
    private String tranAmount;
    //二维码流水号
    private String qrNo;
    //付款方附言
    private String payerComments;
    //收款方卡号
    private String payeeAccNo;
    //收款方姓名
    private String payeeName;
    //防重令牌
    private String token;
    private String conversationId;
    private String activ;
    private String state;
    private String passType;

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_RC() {
        return password_RC;
    }

    public void setPassword_RC(String password_RC) {
        this.password_RC = password_RC;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount= tranAmount;
    }

    public String getQrNo() {
        return qrNo;
    }

    public void setQrNo(String qrNo) {
        this.qrNo = qrNo;
    }

    public String getPayerComments() {
        return payerComments;
    }

    public void setPayerComments(String payerComments) {
        this.payerComments = payerComments;
    }

    public String getPayeeAccNo() {
        return payeeAccNo;
    }

    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }
}
