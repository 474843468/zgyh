package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 到期产品查询
 * Created by zhx on 2016/9/5
 */
public class XpadDueProductProfitQueryViewModel {
    // 账户缓存标识（36位长度）
    private String accountKey;
    // 开始日期（yyyyMMDD）
    private String startDate;
    // 结束日期
    private String endDate;
    // 页面大小（每页显示15条）
    private String pageSize;
    // 当前页索引
    private String currentIndex;
    // 刷新标识（true：重新查询结果(在交易改变查询条件时是需要重新查询的,currentIndex需上送0)false:不需要重新查询，使用缓存中的结果）
    private String _refresh;
    // 币种（000-全部、001-人民币元、 014-美元、012-英镑、 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
    private String currency;
    private String conversationId;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
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

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

    private int recordNumber;
    private List<DueProductEntity> list;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<DueProductEntity> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<DueProductEntity> getList() {
        return list;
    }

    public static class DueProductEntity implements Parcelable {
        /**
         * amount : 1000.2343
         * proId : 675435
         * payRate : 4
         * procur : 001
         * proterm : 20
         * eDate : 2022/10/01
         * proname : aaaaaaaa
         * accno : 675411111111111111118677
         * buyMode : 01
         * payFlag : 0
         * kind : 0
         * payProfit : 1200.3
         */

        // 产品代码
        private String proId;
        // 产品到期日（yyyy/MM/dd）
        private LocalDate eDate;
        // 产品名称
        private String proname;
        // 币种（000：全部、001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        private String procur;
        // 钞汇标识（01：钞 02：汇 00：人民币）
        private String buyMode;
        // 产品期限（以天数表示的产品到期日期限，等于产品到日期减去产品起息日。）
        private String proterm;
        // 本金
        private String amount;
        // 收益率
        private String payRate;
        // 产品性质（0:结构理财产品 1:类基金理财产品）
        private String kind;
        // 支付标识（0：未付 1：已付(未付，前端显示“结算中”)2：非理财系统入账（前端实际收益和实际收益率显示为“-”））
        private String payFlag;
        // 账户
        private String accno;
        // 实际收益
        private String payProfit;

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setProId(String proId) {
            this.proId = proId;
        }

        public void setPayRate(String payRate) {
            this.payRate = payRate;
        }

        public void setProcur(String procur) {
            this.procur = procur;
        }

        public void setProterm(String proterm) {
            this.proterm = proterm;
        }

        public void setEDate(LocalDate eDate) {
            this.eDate = eDate;
        }

        public void setProname(String proname) {
            this.proname = proname;
        }

        public void setAccno(String accno) {
            this.accno = accno;
        }

        public void setBuyMode(String buyMode) {
            this.buyMode = buyMode;
        }

        public void setPayFlag(String payFlag) {
            this.payFlag = payFlag;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setPayProfit(String payProfit) {
            this.payProfit = payProfit;
        }

        public String getAmount() {
            return amount;
        }

        public String getProId() {
            return proId;
        }

        public String getPayRate() {
            return payRate;
        }

        public String getProcur() {
            return procur;
        }

        public String getProterm() {
            return proterm;
        }

        public LocalDate getEDate() {
            return eDate;
        }

        public String getProname() {
            return proname;
        }

        public String getAccno() {
            return accno;
        }

        public String getBuyMode() {
            return buyMode;
        }

        public String getPayFlag() {
            return payFlag;
        }

        public String getKind() {
            return kind;
        }

        public String getPayProfit() {
            return payProfit;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.proId);
            dest.writeSerializable(this.eDate);
            dest.writeString(this.proname);
            dest.writeString(this.procur);
            dest.writeString(this.buyMode);
            dest.writeString(this.proterm);
            dest.writeString(this.amount);
            dest.writeString(this.payRate);
            dest.writeString(this.kind);
            dest.writeString(this.payFlag);
            dest.writeString(this.accno);
            dest.writeString(this.payProfit);
        }

        public DueProductEntity() {
        }

        private DueProductEntity(Parcel in) {
            this.proId = in.readString();
            this.eDate = (LocalDate) in.readSerializable();
            this.proname = in.readString();
            this.procur = in.readString();
            this.buyMode = in.readString();
            this.proterm = in.readString();
            this.amount = in.readString();
            this.payRate = in.readString();
            this.kind = in.readString();
            this.payFlag = in.readString();
            this.accno = in.readString();
            this.payProfit = in.readString();
        }

        public static final Parcelable.Creator<DueProductEntity> CREATOR = new Parcelable.Creator<DueProductEntity>() {
            public DueProductEntity createFromParcel(Parcel source) {
                return new DueProductEntity(source);
            }

            public DueProductEntity[] newArray(int size) {
                return new DueProductEntity[size];
            }
        };
    }
}
