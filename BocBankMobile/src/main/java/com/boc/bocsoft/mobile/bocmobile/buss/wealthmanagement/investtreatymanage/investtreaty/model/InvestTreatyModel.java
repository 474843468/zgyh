package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model;

import java.io.Serializable;
import java.util.List;

/**
 * 协议查询
 * Created by guokai on 2016/9/12.
 */
public class InvestTreatyModel implements Serializable{

    /**
     * 上送字段
     */

    /**
     * 协议类型
     * 0：全部
     * 1：智能投资
     * 2：定时定额投资
     * 3：周期滚续投资
     * 4：余额理财投资
     */
    private String agrType;
    /**
     * 协议状态
     * 0：全部
     * 1：正常
     * 2：失效
     */
    private String agrState;
    /**
     * 页面大小
     */
    private String pageSize;
    /**
     * 当前页索引
     */
    private String currentIndex;
    /**
     * 是否重新查询
     * true：重新查询结果(在交易改变查询条件时是需要重新查询的,
     * currentIndex需上送0)
     * false:不需要重新查询，使用缓存中的结果
     */
    private String _refresh;

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getAgrState() {
        return agrState;
    }

    public void setAgrState(String agrState) {
        this.agrState = agrState;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 返回的字段
     */

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
        private String accNo;//银行帐号
        private String accountKey;//账号缓存标识
        private String agrCode;//产品协议

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        private String agrName;//协议名称
        private String agrType;//协议类型
        private String cashRemit;
        private String custAgrCode;//客户协议代码
        private String execDate;//投资日期
        private String memo;//失败原因
        private String proCur;//产品币种
        private String proId;//产品代码
        private String proName;//产品名称
        private String productKind;//产品性质

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
