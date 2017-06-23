package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery;

import java.util.List;

/**
 * 客户投资智能协议查询
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadCapacityQueryResult {

    /**
     * list : [{"accNo":"4563510100892788955","agrCode":"0000006","agrName":"周期滚续投资","agrType":"3","cashRemit":"00","custAgrCode":"1000000001","execDate":"2015/07/21","memo":"我的未来不是梦","proCur":"001","proId":"1234567890","proName":"产品1","productKind":"1"},{"accNo":"4563510100892788955","agrCode":"0000001","agrName":"余额理财投资","agrType":"4","custAgrCode":"2000000001","execDate":"2017/12/25","memo":"协议已经到期","proCur":"014","proId":"2222222222","proName":"产品4","productKind":"0"},{"accNo":"4563510100892788955","agrCode":"0000001","agrName":"定时定额投资","agrType":"2","custAgrCode":"2000000001","execDate":"2017/12/25","memo":"协议已经到期","proCur":"014","proId":"2222222222","proName":"产品2","productKind":"0"}]
     * recordNumber : 67
     */

    private int recordNumber;
    /**
     * accNo : 4563510100892788955
     * agrCode : 0000006
     * agrName : 周期滚续投资
     * agrType : 3
     * cashRemit : 00
     * custAgrCode : 1000000001
     * execDate : 2015/07/21
     * memo : 我的未来不是梦
     * proCur : 001
     * proId : 1234567890
     * proName : 产品1
     * productKind : 1
     */

    private List<CapacityQueryBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<CapacityQueryBean> getList() {
        return list;
    }

    public void setList(List<CapacityQueryBean> list) {
        this.list = list;
    }

    public static class CapacityQueryBean {
        private String accNo;
        private String accountKey;
        private String agrCode;
        private String agrName;
        private String agrType;
        private String cashRemit;
        private String custAgrCode;
        private String execDate;
        private String memo;
        private String proCur;
        private String proId;
        private String proName;
        private String productKind;

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public String getAccNo() {
            return accNo;
        }

        public void setAccNo(String accNo) {
            this.accNo = accNo;
        }

        public String getAgrCode() {
            return agrCode;
        }

        public void setAgrCode(String agrCode) {
            this.agrCode = agrCode;
        }

        public String getAgrName() {
            return agrName;
        }

        public void setAgrName(String agrName) {
            this.agrName = agrName;
        }

        public String getAgrType() {
            return agrType;
        }

        public void setAgrType(String agrType) {
            this.agrType = agrType;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getCustAgrCode() {
            return custAgrCode;
        }

        public void setCustAgrCode(String custAgrCode) {
            this.custAgrCode = custAgrCode;
        }

        public String getExecDate() {
            return execDate;
        }

        public void setExecDate(String execDate) {
            this.execDate = execDate;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getProCur() {
            return proCur;
        }

        public void setProCur(String proCur) {
            this.proCur = proCur;
        }

        public String getProId() {
            return proId;
        }

        public void setProId(String proId) {
            this.proId = proId;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public String getProductKind() {
            return productKind;
        }

        public void setProductKind(String productKind) {
            this.productKind = productKind;
        }
    }
}
