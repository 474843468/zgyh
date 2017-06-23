package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery;

/**
 * Created by huixiaobo on 2016/6/16.
 * 查询客户签约中银E贷额度列表上送参数
 */
public class PsnEQuoteDetailQueryParams {

    /** 额度/账户*/
    private String quoteNo;
    /** 额度/账户签约标识 取值范围：额度为F 账户为A 如果上送额度/账户项，则额度/账户签约标识项为必选项*/
    private String quoteFlag;
    /**功能  01：只上送客户号，输出最多4条记录  02：上送客户号和额度/账户签约标识，最多输出4条记录 03：上送客户号和额度/账户和额度/账户签约标识，输出一条记录*/
    private String option;

    public String getQuoteNo() {
        return quoteNo;
    }

    public String getQuoteFlag() {
        return quoteFlag;
    }

    public String getOption() {
        return option;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public void setQuoteFlag(String quoteFlag) {
        this.quoteFlag = quoteFlag;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "PsnEQuoteDetailQueryParams{" +
                "quoteNo='" + quoteNo + '\'' +
                ", quoteFlag='" + quoteFlag + '\'' +
                ", option='" + option + '\'' +
                '}';
    }
}
