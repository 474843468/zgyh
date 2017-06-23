package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailDetailQuery;

import java.math.BigDecimal;

/**
 * Result：查询保证金账户详情
 * Created by zhx on 2016/11/21
 */
public class PsnVFGBailDetailQueryResult {

    /**
     * marginAccountNo : 11111111111111111
     * settleCurrency : 333
     * needMarginRatio : 22222.44444
     * openRate : 22222.44444
     * warnRatio : 22222.44444
     * liquidationRatio : 22222.44444
     */
    // 保证金账号
    private String marginAccountNo;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;
    // 交易所需保证金比例
    private BigDecimal needMarginRatio;
    // 报警比例
    private BigDecimal warnRatio;
    // 斩仓比例
    private BigDecimal liquidationRatio;
    // 开仓充足率
    private BigDecimal openRate;

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public void setNeedMarginRatio(BigDecimal needMarginRatio) {
        this.needMarginRatio = needMarginRatio;
    }

    public void setOpenRate(BigDecimal openRate) {
        this.openRate = openRate;
    }

    public void setWarnRatio(BigDecimal warnRatio) {
        this.warnRatio = warnRatio;
    }

    public void setLiquidationRatio(BigDecimal liquidationRatio) {
        this.liquidationRatio = liquidationRatio;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public BigDecimal getNeedMarginRatio() {
        return needMarginRatio;
    }

    public BigDecimal getOpenRate() {
        return openRate;
    }

    public BigDecimal getWarnRatio() {
        return warnRatio;
    }

    public BigDecimal getLiquidationRatio() {
        return liquidationRatio;
    }
}
