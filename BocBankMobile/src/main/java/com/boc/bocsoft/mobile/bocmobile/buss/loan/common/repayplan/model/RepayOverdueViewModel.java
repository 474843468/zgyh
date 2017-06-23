package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 逾期还款记录View层数据模型
 * Created by liuzc on 2016/8/12
 */
public class RepayOverdueViewModel {
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

    private String actNum;  //贷款账号
    private String pageSize;  //每页显示条数
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

    public String is_refresh() {
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
    /**
     * overdueAmountSum : 800
     * overdueCapitalSum : 100000
     * overdueForfeitSum : 660
     * overdueInterestSum : 1160
     * overdueIssueSum : 24
     */

    private String overdueAmountSum; //累计逾期未还款总额
    private String overdueCapitalSum; //累计逾期本金
    private String overdueInterestSum;//累计逾期利息
    private String overdueIssueSum; //累计逾期未还次数
    private List<ListBean> list;//逾期还款信息列表


    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getOverdueAmountSum() {
        return overdueAmountSum;
    }

    public void setOverdueAmountSum(String overdueAmountSum) {
        this.overdueAmountSum = overdueAmountSum;
    }

    public String getOverdueCapitalSum() {
        return overdueCapitalSum;
    }

    public void setOverdueCapitalSum(String overdueCapitalSum) {
        this.overdueCapitalSum = overdueCapitalSum;
    }

    public String getOverdueInterestSum() {
        return overdueInterestSum;
    }

    public void setOverdueInterestSum(String overdueInterestSum) {
        this.overdueInterestSum = overdueInterestSum;
    }

    public String getOverdueIssueSum() {
        return overdueIssueSum;
    }

    public void setOverdueIssueSum(String overdueIssueSum) {
        this.overdueIssueSum = overdueIssueSum;
    }

    public static class ListBean {

        /**
         * overdueAmount : 10000
         * overdueCapital : 600000
         * overdueInterest : 2460
         * pymtDate : 2011/03/18
         */

        private String overdueAmount;
        private String overdueCapital;
        private String overdueInterest;
        private String pymtDate;

        public String getOverdueAmount() {
            return overdueAmount;
        }

        public void setOverdueAmount(String overdueAmount) {
            this.overdueAmount = overdueAmount;
        }

        public String getOverdueCapital() {
            return overdueCapital;
        }

        public void setOverdueCapital(String overdueCapital) {
            this.overdueCapital = overdueCapital;
        }

        public String getOverdueInterest() {
            return overdueInterest;
        }

        public void setOverdueInterest(String overdueInterest) {
            this.overdueInterest = overdueInterest;
        }

        public String getPymtDate() {
            return pymtDate;
        }

        public void setPymtDate(String pymtDate) {
            this.pymtDate = pymtDate;
        }
    }
}
