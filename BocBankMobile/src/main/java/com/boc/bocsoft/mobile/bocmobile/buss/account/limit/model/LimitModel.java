package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/10/11 20:54
 *         限额Model
 */
public class LimitModel extends AccountBean implements Parcelable {

    public static final String STATUS_OPEN = "Y";

    public static final String STATUS_CLOSED = "N";

    /**
     * 日限额
     */
    private BigDecimal quota;
    /**
     * 服务类型 1：无卡在线支付,2：代扣交易,3: 订购交易,5: 境外磁条交易,6：小额免密免签消费,7：免密或凭签名消费
     */
    private String serviceType;
    /**
     * 交易类型
     */
    private String operateType = "开通";
    /**
     * 交易日期
     */
    private String tranDate;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 当前交易限额
     */
    private String currentQuota;
    /**
     *
     */
    private LimitType limitType;
    /**
     * 卡品牌 V: VISA卡,M: Master Card卡,U: 银联卡
     */
    private String cardBrand;
    /**
     * 核心预留手机号
     */
    private String cifMobile;

    public LimitModel() {
    }

    protected LimitModel(Parcel in) {
        setAccountId(in.readString());
        setAccountNumber(in.readString());
        setAccountStatus(in.readString());
        serviceType = in.readString();
        operateType = in.readString();
        tranDate = in.readString();
        customerName = in.readString();
        currentQuota = in.readString();
        cardBrand = in.readString();
        cifMobile = in.readString();
    }

    public static final Creator<LimitModel> CREATOR = new Creator<LimitModel>() {
        @Override
        public LimitModel createFromParcel(Parcel in) {
            return new LimitModel(in);
        }

        @Override
        public LimitModel[] newArray(int size) {
            return new LimitModel[size];
        }
    };

    public LimitType getLimitType() {
        return limitType;
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCurrentQuota() {
        return currentQuota;
    }

    public void setCurrentQuota(String currentQuota) {
        this.currentQuota = currentQuota;
    }

    public String getCifMobile() {
        return cifMobile;
    }

    public void setCifMobile(String cifMobile) {
        this.cifMobile = cifMobile;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getStatusString() {
        switch (getAccountStatus()) {
            case STATUS_OPEN:
                return "已开通";
            case STATUS_CLOSED:
                return "未开通";
        }
        return null;
    }

    public boolean isOpen() {
        return STATUS_OPEN.equals(getAccountStatus());
    }

    public boolean isUnion() {
        return "U".equals(getCardBrand());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getAccountId());
        dest.writeString(getAccountNumber());
        dest.writeString(getAccountStatus());
        dest.writeString(serviceType);
        dest.writeString(operateType);
        dest.writeString(tranDate);
        dest.writeString(customerName);
        dest.writeString(currentQuota);
        dest.writeString(cardBrand);
        dest.writeString(cifMobile);
    }
}
