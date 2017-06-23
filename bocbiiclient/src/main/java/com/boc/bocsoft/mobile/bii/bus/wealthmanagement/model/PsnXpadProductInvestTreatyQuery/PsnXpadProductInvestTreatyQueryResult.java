package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery;

import java.util.List;

/**
 * 产品投资协议查询--响应
 * Created by wangtong on 2016/10/24.
 */
public class PsnXpadProductInvestTreatyQueryResult {
    private int recordNumber;
    private List<ListBean> list;

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
        private String periodBal;// 份额持有天数
        private String isNeedPur;// 购买选项 0：期初购买1：协议不购买
        private String isNeedRed;// 赎回选项 0：到期赎回1：协议不赎回
        private String kind;// 产品性质 0:结构性理财产品1:类基金理财产品
        private String agrCode;// 产品协议编号
        private String agrName;// 产品协议名称
        private String agrType;// 协议类型 1：智能投资2：定时定额投资3：周期滚续投资4：余额理财投资
        private String proid;// 产品ID
        private String proNam;// 产品名称
        private String instType;// 投资方式 1:周期连续协议2:周期不连续协议3:多次购买协议4:多次赎回协议5:定时定额投资6:余额理财投资7:周期滚续协议8:业绩基准周期滚续
        private String periodAgr;// 当协议类型为智能投资，那么该字段返回相应周期频率。
        private String periodPur;// 剩余可购买期数频率 如果协议类型为智能投资，那么该字段返回相应期数频率，否则返回空
        private String proCur;// 产品币种

        public String getPeriodBal() {
            return periodBal;
        }

        public void setPeriodBal(String periodBal) {
            this.periodBal = periodBal;
        }

        public String getIsNeedPur() {
            return isNeedPur;
        }

        public void setIsNeedPur(String isNeedPur) {
            this.isNeedPur = isNeedPur;
        }

        public String getIsNeedRed() {
            return isNeedRed;
        }

        public void setIsNeedRed(String isNeedRed) {
            this.isNeedRed = isNeedRed;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
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

        public String getProid() {
            return proid;
        }

        public void setProid(String proid) {
            this.proid = proid;
        }

        public String getProNam() {
            return proNam;
        }

        public void setProNam(String proNam) {
            this.proNam = proNam;
        }

        public String getInstType() {
            return instType;
        }

        public void setInstType(String instType) {
            this.instType = instType;
        }

        public String getPeriodAgr() {
            return periodAgr;
        }

        public void setPeriodAgr(String periodAgr) {
            this.periodAgr = periodAgr;
        }

        public String getPeriodPur() {
            return periodPur;
        }

        public void setPeriodPur(String periodPur) {
            this.periodPur = periodPur;
        }

        public String getProCur() {
            return proCur;
        }

        public void setProCur(String proCur) {
            this.proCur = proCur;
        }
    }
}
