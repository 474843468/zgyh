package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellDetailQuery;

/**
 * Created by huixiaobo on 2016/11/17.
 * 055定赎申请明细查询—上送参数
 */
public class PsnFundScheduledSellDetailQueryParams {

    /**定赎申请日期*/
    private String fundScheduleDate;
    /**定赎序号*/
    private String scheduleSellNum;

    public String getFundScheduleDate() {
        return fundScheduleDate;
    }

    public void setFundScheduleDate(String fundScheduleDate) {
        this.fundScheduleDate = fundScheduleDate;
    }

    public String getScheduleSellNum() {
        return scheduleSellNum;
    }

    public void setScheduleSellNum(String scheduleSellNum) {
        this.scheduleSellNum = scheduleSellNum;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledSellDetailQueryParams{" +
                "fundScheduleDate='" + fundScheduleDate + '\'' +
                ", scheduleSellNum='" + scheduleSellNum + '\'' +
                '}';
    }
}
