package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zc7067 on 2016/9/26.
 * 历史查询——组合购买——详情
 * @des ${TODO}
 */
public class XpadQueryGuarantyProductResultModel {
    // 组合交易流水号
    private String tranSeq;
    // 账号缓存标识
    private String accountKey;
    // 省行联行号
    private String ibknum;
    // 账户类型（SY-活一本交易 CD-借记卡交易 MW-网上专属理财 GW-长城信用卡 必输）
    private String typeOfAccount;

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public String getIbknum() {
        return ibknum;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }
    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    private List<QueryGuarantyProductResultEntity> list;

    public List<QueryGuarantyProductResultEntity> getList() {
        return list;
    }

    public void setList(List<QueryGuarantyProductResultEntity> list) {
        this.list = list;
    }


    public static class QueryGuarantyProductResultEntity implements Parcelable {
        // 押品代码
        private String prodCode;
        // 押品名称
        private String prodName;
        // 币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        private String curCode;
        // 钞汇标识（1：钞 2：汇 0：人民币）
        private String cashRemit;
        // 押品份额
        private String freezeUnit;
        // 质押日期
        private String prodBegin;
        // 押品到期日
        private String prodEnd;
        // 组合购买状态（0：未成交 1：成交成功 2：成交失败）
        private String impawnPermit;

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public String getProdCode() {
            return prodCode;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getProdName() {
            return prodName;
        }

        public void setCurCode(String curCode) {
            this.curCode = curCode;
        }

        public String getCurCode() {
            return curCode;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setFreezeUnit(String freezeUnit) {
            this.freezeUnit = freezeUnit;
        }

        public String getFreezeUnit() {
            return freezeUnit;
        }

        public void setProdBegin(String prodBegin) {
            this.prodBegin = prodBegin;
        }

        public String getProdBegin() {
            return prodBegin;
        }

        public void setProdEnd(String prodEnd) {
            this.prodEnd = prodEnd;
        }

        public String getProdEnd() {
            return prodEnd;
        }

        public void setImpawnPermit(String impawnPermit) {
            this.impawnPermit = impawnPermit;
        }

        public String getImpawnPermit() {
            return impawnPermit;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.prodCode);
            dest.writeString(this.prodName);
            dest.writeString(this.curCode);
            dest.writeString(this.cashRemit);
            dest.writeString(this.freezeUnit);
            dest.writeString(this.prodBegin);
            dest.writeString(this.prodEnd);
            dest.writeString(this.impawnPermit);
        }

        public QueryGuarantyProductResultEntity() {
        }

        private QueryGuarantyProductResultEntity(Parcel in) {
            this.prodCode = in.readString();
            this.prodName = in.readString();
            this.curCode = in.readString();
            this.cashRemit = in.readString();
            this.freezeUnit = in.readString();
            this.prodBegin = in.readString();
            this.prodEnd = in.readString();
            this.impawnPermit = in.readString();
        }

        public static final Creator<QueryGuarantyProductResultEntity> CREATOR = new Creator<QueryGuarantyProductResultEntity>() {
            public QueryGuarantyProductResultEntity createFromParcel(Parcel source) {
                return new QueryGuarantyProductResultEntity(source);
            }

            public QueryGuarantyProductResultEntity[] newArray(int size) {
                return new QueryGuarantyProductResultEntity[size];
            }
        };

    }

}
