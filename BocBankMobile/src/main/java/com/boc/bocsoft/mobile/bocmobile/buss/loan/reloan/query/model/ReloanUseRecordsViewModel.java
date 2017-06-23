package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * 个人循环贷款用款记录数据模型
 * Created by liuzc on 2016/8/20
 */
public class ReloanUseRecordsViewModel {
    /*
    上送数据
     */
    /**查询开始日期 yyyy/MM/dd*/
    private String startDate;
    /**查询结束日期 yyyy/MM/dd*/
    private String endDate;
    /**贷款账号*/
    private String loanActNum;
    /**每页显示条数 该字段最大值为10（后台系统CTIS最大支持每页返回10条记录）*/
    private String pageSize;
    /**当前页 第一页送1，第二页送2，第三页送3，以此类推*/
    private String currentIndex;

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLoanActNum() {
        return loanActNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    /*
    返回数据
     */
    /**有款列表*/
    private List<PsnLOANUseRecordsQueryBean> result;

    public List<PsnLOANUseRecordsQueryBean> getResult() {
        return result;
    }

    public void setResult(List<PsnLOANUseRecordsQueryBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "PsnLOANUseRecordsQueryResult{" +
                "result=" + result +
                '}';
    }

    public class PsnLOANUseRecordsQueryBean implements Parcelable {
        /**用款日期*/
        private String loanApplyDate;
        /**用款金额*/
        private String loanApplyAmount;
        /**用款流水号*/
        private String loanApplyId;
        /**总笔数*/
        private Integer totnumq;
        /**返回记录数*/
        private Integer retnum;
        /**交易描述信息 = channel*/
        private String channel;
        /**交易币种*/
        private String currencyCode;
        /**商户:贷款用途*/
        private String merchant;

        public String getLoanApplyDate() {
            return loanApplyDate;
        }


        public String getLoanApplyAmount() {
            return loanApplyAmount;
        }

        public String getLoanApplyId() {
            return loanApplyId;
        }

        public Integer getTotnumq() {
            return totnumq;
        }

        public Integer getRetnum() {
            return retnum;
        }

        public void setLoanApplyDate(String loanApplyDate) {
            this.loanApplyDate = loanApplyDate;
        }

        public void setLoanApplyAmount(String loanApplyAmount) {
            this.loanApplyAmount = loanApplyAmount;
        }

        public void setLoanApplyId(String loanApplyId) {
            this.loanApplyId = loanApplyId;
        }

        public void setTotnumq(Integer totnumq) {
            this.totnumq = totnumq;
        }

        public void setRetnum(Integer retnum) {
            this.retnum = retnum;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getChannel() {
            return channel;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getMerchant() {
            return merchant;
        }

        public void setMerchant(String merchant) {
            this.merchant = merchant;
        }

        @Override
        public String toString() {
            return "PsnLOANUseRecordsQueryResult{" +
                    "loanApplyDate='" + loanApplyDate + '\'' +
                    ", loanApplyAmount='" + loanApplyAmount + '\'' +
                    ", loanApplyId='" + loanApplyId + '\'' +
                    ", channle='" + channel + '\'' +
                    ", merchant='" + merchant + '\'' +
//                    ", currencyCode='" + currencyCode + '\'' +
                    ", totnumq=" + totnumq +
                    ", retnum=" + retnum +
                    '}';
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.loanApplyDate);
            parcel.writeString(this.loanApplyAmount);
            parcel.writeString(this.loanApplyId);
            parcel.writeString(this.channel);
            parcel.writeString(this.merchant);
//            parcel.writeString(this.currencyCode);
            parcel.writeInt(this.retnum);
            parcel.writeInt(this.retnum);

        }

        public  PsnLOANUseRecordsQueryBean(){

        }

        protected  PsnLOANUseRecordsQueryBean(Parcel in) {
            this.loanApplyDate = in.readString();
            this.loanApplyAmount = in.readString();
            this.loanApplyId = in.readString();
            this.channel = in.readString();
            this.merchant = in.readString();
//            this.currencyCode = in.readString();
            this.retnum = in.readInt();
            this.retnum = in.readInt();
        }

        public  final Parcelable.Creator<ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean> CREATOR =
                new Parcelable.Creator<ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean>() {
                    @Override
                    public ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean createFromParcel(
                            Parcel source) {return new ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean(source);}

                    @Override
                    public ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean[] newArray(
                            int size) {return new ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean[size];}
                };


    }
}
