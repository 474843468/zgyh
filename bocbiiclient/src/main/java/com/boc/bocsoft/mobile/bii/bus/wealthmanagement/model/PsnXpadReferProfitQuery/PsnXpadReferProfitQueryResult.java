package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery;

import com.boc.bocsoft.mobile.bii.common.model.BIIResponse;

/**
 * PsnXpadReferProfitQuery  参考收益汇总查询   结果Model
 * Created by zn on 2016/9/19.
 */
public class PsnXpadReferProfitQueryResult {
    //    ============================产品性质：1-类基金理财产品=====字段==================================
    /**
     * 收益日期	String
     */
    private String profitdate;


    /**
     * 总盈亏	Bigdicmal
     */
    private String totalamt;
    /**
     * 已实现盈亏	Bigdicmal
     */
    private String amt;
    /**
     * 持仓盈亏	Bigdicmal
     */
    private String balamt;
//=========================================产品性质：0-结构性理财产品=========字段============================
    /**
     * 产品名称
     */
    private String proname;
    /**
     * 产品到期日	String	Yyyy/MM/dd
     */
    private String edate;
    /**
     * 产品币种	String	000：全部、
     * 001：人民币元
     * 014：美元
     * 012：英镑
     * 013：港币
     * 028: 加拿大元
     * 029：澳元
     * 038：欧元
     * 027：日元
     */
    private String procur;
    /**
     * 计息开始日期	String	YYYY/MM/DD
     * 收益累进产品为空
     */
    private String intsdate;
    /**
     * 计息截止日期/收益日期	String	YYYY/MM/DD
     * 收益累进产品为收益日期
     */
    private String intedate;
    /**
     * 是否收益累计产品	String	0：否
     * 1：是
     */
    private String progressionflag;
    /**
     * 产品性质	String	产品性质	String	0:结构性理财产品，1-类基金理财产品
     */
    private String kind;
    /**
     * 产品期限特性	String	00-有限期封闭式
     * 01-有限期半开放式
     * 02-周期连续
     * 03-周期不连续
     * 04-无限开放式
     * 05-春夏秋冬
     * 06-其它
     */
    private String termType;
    /**
     * 当期收益/持仓收益	Bigdicmal	收益累进产品为持仓收益
     */
    private String profit;
    /**
     * 历史累计收益	Bigdicmal
     */
    private String totalprofit;
    /**
     * 未到账收益	Bigdicmal	收益累进产品为0
     */
    private String unpayprofit;
    /**
     * 已到账收益	Bigdicmal	收益累进产品为0
     */
    private String payprofit;
    /**
     * 付息日	String	收益累进产品为0
     */
    private String paydate;
    /**
     * 赎回规则	String	0:否
     * 1:先进先出
     * 2:后进先出
     */
    private String redeemrule;
    /**
     * 本金返还方式	String	0:实时返还
     * 1:T+N返还
     * 2:期末返还
     */
    private String redpayamtmode;
    /**
     * 本金返还T+N(天数)	String
     */
    private String redpayamountdate;
    /**
     * 收益返还方式	String	1:T+N返还
     * 2:期末返还
     */
    private String redpayprofitmode;
    /**
     * 收益返还T+N(天数)	String
     */
    private String redpayprofitdate;
    /**
     * 保留字段	String
     */
    private String extfield;

//    =======================================================


    public String getProfitdate() {
        return profitdate;
    }

    public void setProfitdate(String profitdate) {
        this.profitdate = profitdate;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(String totalamt) {
        this.totalamt = totalamt;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getBalamt() {
        return balamt;
    }

    public void setBalamt(String balamt) {
        this.balamt = balamt;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getProcur() {
        return procur;
    }

    public void setProcur(String procur) {
        this.procur = procur;
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

    public String getProgressionflag() {
        return progressionflag;
    }

    public void setProgressionflag(String progressionflag) {
        this.progressionflag = progressionflag;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getTotalprofit() {
        return totalprofit;
    }

    public void setTotalprofit(String totalprofit) {
        this.totalprofit = totalprofit;
    }

    public String getUnpayprofit() {
        return unpayprofit;
    }

    public void setUnpayprofit(String unpayprofit) {
        this.unpayprofit = unpayprofit;
    }

    public String getPayprofit() {
        return payprofit;
    }

    public void setPayprofit(String payprofit) {
        this.payprofit = payprofit;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getRedeemrule() {
        return redeemrule;
    }

    public void setRedeemrule(String redeemrule) {
        this.redeemrule = redeemrule;
    }

    public String getRedpayamtmode() {
        return redpayamtmode;
    }

    public void setRedpayamtmode(String redpayamtmode) {
        this.redpayamtmode = redpayamtmode;
    }

    public String getRedpayamountdate() {
        return redpayamountdate;
    }

    public void setRedpayamountdate(String redpayamountdate) {
        this.redpayamountdate = redpayamountdate;
    }

    public String getRedpayprofitmode() {
        return redpayprofitmode;
    }

    public void setRedpayprofitmode(String redpayprofitmode) {
        this.redpayprofitmode = redpayprofitmode;
    }

    public String getRedpayprofitdate() {
        return redpayprofitdate;
    }

    public void setRedpayprofitdate(String redpayprofitdate) {
        this.redpayprofitdate = redpayprofitdate;
    }

    public String getExtfield() {
        return extfield;
    }

    public void setExtfield(String extfield) {
        this.extfield = extfield;
    }


}
