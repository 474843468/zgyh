package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/6/30.
 */
public class PaymentOperatorModel implements Parcelable {

    private String operatorTitle;

    //优惠后金额
    private String discountAmount;

    //交易序号
    private String transferNum;

    //交易币种
    private String trfCur;

    //转账金额
    private String transAmount;

    private String payeeMobel;
    private String payeeName;
    private String tips;
    private String payeeLbk;
    private String payerAccount;
    private String payerName;
    private String payeeAccNum;//收款账号
    private String notifyId;//指令序号

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayeeAccNum() {
        return payeeAccNum;
    }

    public void setPayeeAccNum(String payeeAccNum) {
        this.payeeAccNum = payeeAccNum;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    private BigDecimal remainAmount;

    public String getPayeeLbk() {
        return payeeLbk;
    }

    public void setPayeeLbk(String payeeLbk) {
        this.payeeLbk = payeeLbk;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getPayeeMobel() {
        return payeeMobel;
    }

    public void setPayeeMobel(String payeeMobel) {
        this.payeeMobel = payeeMobel;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    private List<OperatorBean> resultList;

    private List<OperatorButton> bottomButtonList;

    public List<OperatorButton> getBottomButtonList() {
        return bottomButtonList;
    }

    public void setBottomButtonList(List<OperatorButton> bottomButtonList) {
        this.bottomButtonList = bottomButtonList;
    }

    public String getTrfCur() {
        return trfCur;
    }

    public void setTrfCur(String trfCur) {
        this.trfCur = trfCur;
    }

    public String getOperatorTitle() {
        return operatorTitle;
    }

    public void setOperatorTitle(String operatorTitle) {
        this.operatorTitle = operatorTitle;
    }

    public List<OperatorBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<OperatorBean> resultList) {
        this.resultList = resultList;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(String transferNum) {
        this.transferNum = transferNum;
    }

    public static class OperatorBean implements Parcelable {

        public String name;
        public String value;

        public OperatorBean() {

        }

        public OperatorBean(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.value);
        }

        protected OperatorBean(Parcel in) {
            this.name = in.readString();
            this.value = in.readString();
        }

        public static final Creator<OperatorBean> CREATOR = new Creator<OperatorBean>() {
            @Override
            public OperatorBean createFromParcel(Parcel source) {
                return new OperatorBean(source);
            }

            @Override
            public OperatorBean[] newArray(int size) {
                return new OperatorBean[size];
            }
        };
    }

    public static class OperatorButton implements Parcelable {
        public String name;
        public MyListener listener;


        public OperatorButton() {

        }

        public OperatorButton(String name, MyListener listener) {
            this.name = name;
            this.listener = listener;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeParcelable(this.listener, flags);
        }

        protected OperatorButton(Parcel in) {
            this.name = in.readString();
            this.listener = in.readParcelable(MyListener.class.getClassLoader());
        }

        public static final Creator<OperatorButton> CREATOR = new Creator<OperatorButton>() {
            @Override
            public OperatorButton createFromParcel(Parcel source) {
                return new OperatorButton(source);
            }

            @Override
            public OperatorButton[] newArray(int size) {
                return new OperatorButton[size];
            }
        };
    }

    public static class MyListener implements View.OnClickListener, Parcelable {
        String id;
        @Override
        public void onClick(View v) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
        }

        public MyListener() {
        }

        protected MyListener(Parcel in) {
            this.id = in.readString();
        }

        public static final Creator<MyListener> CREATOR = new Creator<MyListener>() {
            @Override
            public MyListener createFromParcel(Parcel source) {
                return new MyListener(source);
            }

            @Override
            public MyListener[] newArray(int size) {
                return new MyListener[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.operatorTitle);
        dest.writeList(this.resultList);
        dest.writeList(this.bottomButtonList);
    }

    public PaymentOperatorModel() {
    }

    protected PaymentOperatorModel(Parcel in) {
        this.operatorTitle = in.readString();
        this.resultList = new ArrayList<OperatorBean>();
        in.readList(this.resultList, OperatorBean.class.getClassLoader());
        this.bottomButtonList = new ArrayList<OperatorButton>();
        in.readList(this.bottomButtonList, OperatorButton.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaymentOperatorModel> CREATOR = new Parcelable.Creator<PaymentOperatorModel>() {
        @Override
        public PaymentOperatorModel createFromParcel(Parcel source) {
            return new PaymentOperatorModel(source);
        }

        @Override
        public PaymentOperatorModel[] newArray(int size) {
            return new PaymentOperatorModel[size];
        }
    };
}
