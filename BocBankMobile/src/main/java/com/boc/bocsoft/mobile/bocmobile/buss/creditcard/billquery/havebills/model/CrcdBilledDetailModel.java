package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.util.List;

/**
 * 4.8 008查询信用卡已出账单交易明细PsnCrcdQueryBilledTransDetail
 * 接口响应参数
 *
 * 作者：xwg on 16/12/21 09:36
 * Modified by liuweidong on 2016/12/28.
 */
public class CrcdBilledDetailModel {
    private String sumNo;// 总页数
    private String pageNo;// 当前页
    private String primary;// 分页查询唯一号
    private String dealCount;// 总笔数
    @ListItemType(instantiate = TransListBean.class)
    private List<TransListBean> transList;

    public String getSumNo() {
        return sumNo;
    }

    public void setSumNo(String sumNo) {
        this.sumNo = sumNo;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getDealCount() {
        return dealCount;
    }

    public void setDealCount(String dealCount) {
        this.dealCount = dealCount;
    }

    public List<TransListBean> getTransList() {
        return transList;
    }

    public void setTransList(List<TransListBean> transList) {
        this.transList = transList;
    }

    public static class TransListBean implements Parcelable{
        private String acntType;// 账户类型
        private String dealDt;// 交易日期
        private String checkDt;// 记账日期
        private String dealCardId;// 卡号
        private String dealDesc;// 交易描述
        private String dealCcy;// 交易币种
        private String transactionProfileCode;// 交易码
        private String dealCnt;// 交易金额
        private String balCnt;// 结算金额
        private String loanSign;// 借贷记账标志 "DEBT"表示:借方(比如：网上消费这种就是借方)"CRED"表示:贷方"NMON"表示:非金融交易，CRED表示存入；DEBT和NMON表示支出

        public TransListBean() {
        }

        protected TransListBean(Parcel in) {
            acntType = in.readString();
            dealDt = in.readString();
            checkDt = in.readString();
            dealCardId = in.readString();
            dealDesc = in.readString();
            dealCcy = in.readString();
            transactionProfileCode = in.readString();
            dealCnt = in.readString();
            balCnt = in.readString();
            loanSign = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(acntType);
            dest.writeString(dealDt);
            dest.writeString(checkDt);
            dest.writeString(dealCardId);
            dest.writeString(dealDesc);
            dest.writeString(dealCcy);
            dest.writeString(transactionProfileCode);
            dest.writeString(dealCnt);
            dest.writeString(balCnt);
            dest.writeString(loanSign);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TransListBean> CREATOR = new Creator<TransListBean>() {
            @Override
            public TransListBean createFromParcel(Parcel in) {
                return new TransListBean(in);
            }

            @Override
            public TransListBean[] newArray(int size) {
                return new TransListBean[size];
            }
        };

        public String getAcntType() {
            return acntType;
        }

        public void setAcntType(String acntType) {
            this.acntType = acntType;
        }

        public String getDealDt() {
            return dealDt;
        }

        public void setDealDt(String dealDt) {
            this.dealDt = dealDt;
        }

        public String getCheckDt() {
            return checkDt;
        }

        public void setCheckDt(String checkDt) {
            this.checkDt = checkDt;
        }

        public String getDealCardId() {
            return dealCardId;
        }

        public void setDealCardId(String dealCardId) {
            this.dealCardId = dealCardId;
        }

        public String getDealDesc() {
            return dealDesc;
        }

        public void setDealDesc(String dealDesc) {
            this.dealDesc = dealDesc;
        }

        public String getDealCcy() {
            return dealCcy;
        }

        public void setDealCcy(String dealCcy) {
            this.dealCcy = dealCcy;
        }

        public String getTransactionProfileCode() {
            return transactionProfileCode;
        }

        public void setTransactionProfileCode(String transactionProfileCode) {
            this.transactionProfileCode = transactionProfileCode;
        }

        public String getDealCnt() {
            return dealCnt;
        }

        public void setDealCnt(String dealCnt) {
            this.dealCnt = dealCnt;
        }

        public String getBalCnt() {
            return balCnt;
        }

        public void setBalCnt(String balCnt) {
            this.balCnt = balCnt;
        }

        public String getLoanSign() {
            return loanSign;
        }

        public void setLoanSign(String loanSign) {
            this.loanSign = loanSign;
        }
    }
}
