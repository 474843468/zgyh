package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 剩余还款记录View层数据模型
 * Created by liuzc on 2016/8/12
 */
public class RepayRemainViewModel {
    /*
    上送数据
     */

    /**
     * actNum : 00000102811811890
     * pageSize : 10
     * currentIndex : 0
     * _refresh : false
     * conversationId : 15d1fb69-45f1-446c-b9e2-f85bb71cee74
     */
    private String actNum; //贷款账号
    private String pageSize; //每页显示条数
    private String currentIndex; //当前页
    private String _refresh; //刷新标志
    private String conversationId; //会话ID

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /*
    返回数据
     */
    private int recordNumber; //记录总数
    private List<ListBean> list;//剩余还款信息列表

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        /**
         * repayDate : 2015/05/06
         * remainAmount : 949.91
         * remainCapital : 245.74
         * loanId : 1
         * remainInterest : 704.17
         */

        private String repayDate; //还款日期
        private String remainAmount; //剩余金额
        private String remainCapital; //剩余本金
        private String loanId; //期号
        private String remainInterest; //剩余利息

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getRemainAmount() {
            return remainAmount;
        }

        public void setRemainAmount(String remainAmount) {
            this.remainAmount = remainAmount;
        }

        public String getRemainCapital() {
            return remainCapital;
        }

        public void setRemainCapital(String remainCapital) {
            this.remainCapital = remainCapital;
        }

        public String getLoanId() {
            return loanId;
        }

        public void setLoanId(String loanId) {
            this.loanId = loanId;
        }

        public String getRemainInterest() {
            return remainInterest;
        }

        public void setRemainInterest(String remainInterest) {
            this.remainInterest = remainInterest;
        }
    }
}
