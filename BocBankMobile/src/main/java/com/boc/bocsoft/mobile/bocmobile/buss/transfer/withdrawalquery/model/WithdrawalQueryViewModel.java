package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 取款查询View层数据模型
 * Created by wangf on 2016/6/20
 */
public class WithdrawalQueryViewModel implements Parcelable {

    /**
     * 取款查询上送数据项
     */

    /**
     * startDate : 2019/01/26
     * endDate : 2019/02/01
     * currentIndex : 0
     * pageSize : 10
     */

    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 当前页号
     */
    private int currentIndex;
    /**
     * 每页显示条数
     */
    private int pageSize;

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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

/**
 * 取款查询返回数据项
 */

    /**
     * list : [{"channel":"1","currencyCode":"001","transactionId":"10000000281348","cashRemit":null,"payeeName":"zlp","payeeMobile":"13456321203","tranDate":"2013/07/12","remitStatus":"A","remitNo":"123456780","agentName":"alias1","remitAmount":20000,"agentNum":"7"}]
     * recordNumber : 1
     * nickName : 长城借记卡
     * agentAcctNumber : 4563515000000185687
     */

    /**
     * 记录总数
     */
    private int recordNumber;
    /**
     * 别名
     */
    private String nickName;
    /**
     * 代理点收款账号
     */
    private String agentAcctNumber;

    /**
     * channel : 1
     * currencyCode : 001
     * transactionId : 10000000281348
     * cashRemit : null
     * payeeName : zlp
     * payeeMobile : 13456321203
     * tranDate : 2013/07/12
     * remitStatus : A
     * remitNo : 123456780
     * agentName : alias1
     * remitAmount : 20000
     * agentNum : 7
     */

    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAgentAcctNumber() {
        return agentAcctNumber;
    }

    public void setAgentAcctNumber(String agentAcctNumber) {
        this.agentAcctNumber = agentAcctNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        
        /**
         * 渠道标识
         */
        private String channel;
        /**
         * 取款币种
         */
        private String currencyCode;
        /**
         * 网银交易序号
         */
        private String transactionId;
        /**
         * 钞汇
         */
        private String cashRemit;
        /**
         * 收款人姓名
         */
        private String payeeName;
        /**
         * 收款人手机号
         */
        private String payeeMobile;
        /**
         * 取款日期
         */
        private LocalDateTime tranDate;
        /**
         * 汇款状态
         */
        private String remitStatus;
        /**
         * 汇款编号
         */
        private String remitNo;
        /**
         * 代理点名称
         */
        private String agentName;
        /**
         * 取款金额
         */
        private String remitAmount;
        /**
         * 代理点编号
         */
        private String agentNum;

        //机构号
        private String branchId;
        //附言
        private String remark;
        //汇出账号
        private String fromActNumber;

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getFromActNumber() {
            return fromActNumber;
        }

        public void setFromActNumber(String fromActNumber) {
            this.fromActNumber = fromActNumber;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
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

        public LocalDateTime getTranDate() {
            return tranDate;
        }

        public void setTranDate(LocalDateTime tranDate) {
            this.tranDate = tranDate;
        }

        public String getRemitStatus() {
            return remitStatus;
        }

        public void setRemitStatus(String remitStatus) {
            this.remitStatus = remitStatus;
        }

        public String getRemitNo() {
            return remitNo;
        }

        public void setRemitNo(String remitNo) {
            this.remitNo = remitNo;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getRemitAmount() {
            return remitAmount;
        }

        public void setRemitAmount(String remitAmount) {
            this.remitAmount = remitAmount;
        }

        public String getAgentNum() {
            return agentNum;
        }

        public void setAgentNum(String agentNum) {
            this.agentNum = agentNum;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.channel);
            dest.writeString(this.currencyCode);
            dest.writeString(this.transactionId);
            dest.writeString(this.cashRemit);
            dest.writeString(this.payeeName);
            dest.writeString(this.payeeMobile);
            dest.writeSerializable(this.tranDate);
            dest.writeString(this.remitStatus);
            dest.writeString(this.remitNo);
            dest.writeString(this.agentName);
            dest.writeString(this.remitAmount);
            dest.writeString(this.agentNum);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.channel = in.readString();
            this.currencyCode = in.readString();
            this.transactionId = in.readString();
            this.cashRemit = in.readParcelable(String.class.getClassLoader());
            this.payeeName = in.readString();
            this.payeeMobile = in.readString();
            this.tranDate = (LocalDateTime) in.readSerializable();
            this.remitStatus = in.readString();
            this.remitNo = in.readString();
            this.agentName = in.readString();
            this.remitAmount = in.readString();
            this.agentNum = in.readString();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeInt(this.currentIndex);
        dest.writeInt(this.pageSize);
        dest.writeInt(this.recordNumber);
        dest.writeString(this.nickName);
        dest.writeString(this.agentAcctNumber);
        dest.writeList(this.list);
    }

    public WithdrawalQueryViewModel() {
    }

    protected WithdrawalQueryViewModel(Parcel in) {
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.currentIndex = in.readInt();
        this.pageSize = in.readInt();
        this.recordNumber = in.readInt();
        this.nickName = in.readString();
        this.agentAcctNumber = in.readString();
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<WithdrawalQueryViewModel> CREATOR = new Parcelable.Creator<WithdrawalQueryViewModel>() {
        @Override
        public WithdrawalQueryViewModel createFromParcel(Parcel source) {
            return new WithdrawalQueryViewModel(source);
        }

        @Override
        public WithdrawalQueryViewModel[] newArray(int size) {
            return new WithdrawalQueryViewModel[size];
        }
    };
}
