package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery;

import java.util.List;

/**
 * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
 * Created by cff on 2016/9/22.
 */
public class PsnXpadReferProfitDetailQueryResModel {
    /**
     * 总笔数
     */
    private String recordNumber;
    /**
     * 产品信息
     */
    private List<QueryModel> list;

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<QueryModel> getList() {
        return list;
    }

    public void setList(List<QueryModel> list) {
        this.list = list;
    }

    /**
     * 详情数据
     */
    public static class QueryModel{
        /**
         * 产品名称
         */
        private String proname;
        /**
         * 参考收益
         */
        private String payprofit;
        /**
         * 保留字段
         */
        private String extfield;
        //是否收益累计产品：0-否

        /**
         * 计息开始
         */
        private String intsdate;
        /**
         * 计息截止
         */
        private String intedate;
        /**
         * 付息状态
         */
        private String payflag;
        //是否收益累计产品：1-是
        /**
         * 起息日期
         */
        private String startdate;
        /**
         * 持有份额
         */
        private String balunit;
        /**
         * 持有天数
         */
        private String baldays;
        /**
         * 预计年收益率
         */
        private String exyield;
        /**
         * 下一档收益率剩余天数
         */
        private String nextdays;

        public String getProname() {
            return proname;
        }

        public void setProname(String proname) {
            this.proname = proname;
        }

        public String getPayprofit() {
            return payprofit;
        }

        public void setPayprofit(String payprofit) {
            this.payprofit = payprofit;
        }

        public String getExtfield() {
            return extfield;
        }

        public void setExtfield(String extfield) {
            this.extfield = extfield;
        }

        public String getIntsdate() {
            return intsdate;
        }

        public void setIntsdate(String intsdate) {
            this.intsdate = intsdate;
        }

        public String getIntedate() {
            return intedate;
        }

        public void setIntedate(String intedate) {
            this.intedate = intedate;
        }

        public String getPayflag() {
            return payflag;
        }

        public void setPayflag(String payflag) {
            this.payflag = payflag;
        }

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getBalunit() {
            return balunit;
        }

        public void setBalunit(String balunit) {
            this.balunit = balunit;
        }

        public String getBaldays() {
            return baldays;
        }

        public void setBaldays(String baldays) {
            this.baldays = baldays;
        }

        public String getExyield() {
            return exyield;
        }

        public void setExyield(String exyield) {
            this.exyield = exyield;
        }

        public String getNextdays() {
            return nextdays;
        }

        public void setNextdays(String nextdays) {
            this.nextdays = nextdays;
        }
    }
}
