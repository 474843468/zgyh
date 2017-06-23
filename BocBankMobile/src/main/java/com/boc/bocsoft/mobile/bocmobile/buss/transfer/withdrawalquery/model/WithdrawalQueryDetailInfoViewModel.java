package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model;

/**
 * 取款查询 详情页面 View层数据模型
 * Created by wangf on 2016/6/20
 */
public class WithdrawalQueryDetailInfoViewModel {

    /**
     * 取款查询 详情上送数据项
     */
    //网银交易序号
    private String transactionId;
    //交易金额
    private String receiptAmount;
    //交易币种
    private String currencyCode;
    //收款人姓名
    private String payeeName;
    //收款人手机号
    private String payeeMobile;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    /**
     * 取款查询 详情返回数据项
     */

    /**
     * result : B
     */

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

//    //交易币种
//    private String currencyCode;
//    //钞汇
//    private String cashRemit;
//    //收款人姓名
//    private String payeeName;
//    //收款人手机号
//    private String payeeMobile;
//    //汇出户名
//    private String fromName;
//    //附言
//    private String remark;
//    //汇款日期
//    private LocalDate tranDate;
//    //汇款状态
//    private String remitStatus;
//    //汇款编号
//    private String remitNo;
//    //汇出账户
//    private String cardNo;
//    //代理点名称
//    private String agentName;
//    //取款日期
//    private LocalDate receiptDate;
//    //汇款金额
//    private String remitAmount;
//    //代理点编号
//    private String agentNum;
//    //渠道标识
//    private String channel;
//
//    public String getCurrencyCode() {
//        return currencyCode;
//    }
//
//    public void setCurrencyCode(String currencyCode) {
//        this.currencyCode = currencyCode;
//    }
//
//    public String getCashRemit() {
//        return cashRemit;
//    }
//
//    public void setCashRemit(String cashRemit) {
//        this.cashRemit = cashRemit;
//    }
//
//    public String getPayeeName() {
//        return payeeName;
//    }
//
//    public void setPayeeName(String payeeName) {
//        this.payeeName = payeeName;
//    }
//
//    public String getPayeeMobile() {
//        return payeeMobile;
//    }
//
//    public void setPayeeMobile(String payeeMobile) {
//        this.payeeMobile = payeeMobile;
//    }
//
//    public String getFromName() {
//        return fromName;
//    }
//
//    public void setFromName(String fromName) {
//        this.fromName = fromName;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public LocalDate getTranDate() {
//        return tranDate;
//    }
//
//    public void setTranDate(LocalDate tranDate) {
//        this.tranDate = tranDate;
//    }
//
//    public String getRemitStatus() {
//        return remitStatus;
//    }
//
//    public void setRemitStatus(String remitStatus) {
//        this.remitStatus = remitStatus;
//    }
//
//    public String getRemitNo() {
//        return remitNo;
//    }
//
//    public void setRemitNo(String remitNo) {
//        this.remitNo = remitNo;
//    }
//
//    public String getCardNo() {
//        return cardNo;
//    }
//
//    public void setCardNo(String cardNo) {
//        this.cardNo = cardNo;
//    }
//
//    public String getAgentName() {
//        return agentName;
//    }
//
//    public void setAgentName(String agentName) {
//        this.agentName = agentName;
//    }
//
//    public LocalDate getReceiptDate() {
//        return receiptDate;
//    }
//
//    public void setReceiptDate(LocalDate receiptDate) {
//        this.receiptDate = receiptDate;
//    }
//
//    public String getRemitAmount() {
//        return remitAmount;
//    }
//
//    public void setRemitAmount(String remitAmount) {
//        this.remitAmount = remitAmount;
//    }
//
//    public String getAgentNum() {
//        return agentNum;
//    }
//
//    public void setAgentNum(String agentNum) {
//        this.agentNum = agentNum;
//    }
//
//    public String getChannel() {
//        return channel;
//    }
//
//    public void setChannel(String channel) {
//        this.channel = channel;
//    }
//
//    public WithdrawalQueryDetailInfoViewModel() {
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.transactionId);
//        dest.writeString(this.receiptAmount);
//        dest.writeString(this.currencyCode);
//        dest.writeString(this.cashRemit);
//        dest.writeString(this.payeeName);
//        dest.writeString(this.payeeMobile);
//        dest.writeString(this.fromName);
//        dest.writeString(this.remark);
//        dest.writeSerializable(this.tranDate);
//        dest.writeString(this.remitStatus);
//        dest.writeString(this.remitNo);
//        dest.writeString(this.cardNo);
//        dest.writeString(this.agentName);
//        dest.writeSerializable(this.receiptDate);
//        dest.writeString(this.remitAmount);
//        dest.writeString(this.agentNum);
//        dest.writeString(this.channel);
//    }
//
//    protected WithdrawalQueryDetailInfoViewModel(Parcel in) {
//        this.transactionId = in.readString();
//        this.receiptAmount = in.readString();
//        this.currencyCode = in.readString();
//        this.cashRemit = in.readString();
//        this.payeeName = in.readString();
//        this.payeeMobile = in.readString();
//        this.fromName = in.readString();
//        this.remark = in.readString();
//        this.tranDate = (LocalDate) in.readSerializable();
//        this.remitStatus = in.readString();
//        this.remitNo = in.readString();
//        this.cardNo = in.readString();
//        this.agentName = in.readString();
//        this.receiptDate = (LocalDate) in.readSerializable();
//        this.remitAmount = in.readString();
//        this.agentNum = in.readString();
//        this.channel = in.readString();
//    }
//
//    public static final Creator<WithdrawalQueryDetailInfoViewModel> CREATOR = new Creator<WithdrawalQueryDetailInfoViewModel>() {
//        @Override
//        public WithdrawalQueryDetailInfoViewModel createFromParcel(Parcel source) {
//            return new WithdrawalQueryDetailInfoViewModel(source);
//        }
//
//        @Override
//        public WithdrawalQueryDetailInfoViewModel[] newArray(int size) {
//            return new WithdrawalQueryDetailInfoViewModel[size];
//        }
//    };
}
