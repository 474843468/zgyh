package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

/**
 * 转账记录 退汇信息View层数据模型
 */
public class RemitReturnInfoViewModel implements Parcelable {


    /**
     * 退汇交易信息上送数据项
     */

    /**
     * transId : 1913189
     */

    //网银交易序号
    private String transId;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }


    /**
     * 退汇交易信息返回数据项
     */

    //退汇交易转入账户
    private String payerActno;
    //退汇交易转出账户
    private String payeeActno;
    //退汇金额
    private String reexchangeAmount;
    //退汇原因
    private String reexchangeInfo;
    //附言
    private String remittanceInfo;
    //退汇入账日期
    private LocalDate reexchangeDate;
    //退汇交易状态
    private String reexchangeStatus;
    //转账详情的ViewModel
    private TransferRecordDetailInfoViewModel detailInfoViewModel;


    public TransferRecordDetailInfoViewModel getDetailInfoViewModel() {
        return detailInfoViewModel;
    }

    public void setDetailInfoViewModel(TransferRecordDetailInfoViewModel detailInfoViewModel) {
        this.detailInfoViewModel = detailInfoViewModel;
    }

    public String getPayerActno() {
        return payerActno;
    }

    public void setPayerActno(String payerActno) {
        this.payerActno = payerActno;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getReexchangeAmount() {
        return reexchangeAmount;
    }

    public void setReexchangeAmount(String reexchangeAmount) {
        this.reexchangeAmount = reexchangeAmount;
    }

    public String getReexchangeInfo() {
        return reexchangeInfo;
    }

    public void setReexchangeInfo(String reexchangeInfo) {
        this.reexchangeInfo = reexchangeInfo;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public LocalDate getReexchangeDate() {
        return reexchangeDate;
    }

    public void setReexchangeDate(LocalDate reexchangeDate) {
        this.reexchangeDate = reexchangeDate;
    }

    public String getReexchangeStatus() {
        return reexchangeStatus;
    }

    public void setReexchangeStatus(String reexchangeStatus) {
        this.reexchangeStatus = reexchangeStatus;
    }

    public RemitReturnInfoViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transId);
        dest.writeString(this.payerActno);
        dest.writeString(this.payeeActno);
        dest.writeString(this.reexchangeAmount);
        dest.writeString(this.reexchangeInfo);
        dest.writeString(this.remittanceInfo);
        dest.writeSerializable(this.reexchangeDate);
        dest.writeString(this.reexchangeStatus);
        dest.writeParcelable(this.detailInfoViewModel, flags);
    }

    protected RemitReturnInfoViewModel(Parcel in) {
        this.transId = in.readString();
        this.payerActno = in.readString();
        this.payeeActno = in.readString();
        this.reexchangeAmount = in.readString();
        this.reexchangeInfo = in.readString();
        this.remittanceInfo = in.readString();
        this.reexchangeDate = (LocalDate) in.readSerializable();
        this.reexchangeStatus = in.readString();
        this.detailInfoViewModel = in.readParcelable(TransferRecordDetailInfoViewModel.class.getClassLoader());
    }

    public static final Creator<RemitReturnInfoViewModel> CREATOR = new Creator<RemitReturnInfoViewModel>() {
        @Override
        public RemitReturnInfoViewModel createFromParcel(Parcel source) {
            return new RemitReturnInfoViewModel(source);
        }

        @Override
        public RemitReturnInfoViewModel[] newArray(int size) {
            return new RemitReturnInfoViewModel[size];
        }
    };
}
