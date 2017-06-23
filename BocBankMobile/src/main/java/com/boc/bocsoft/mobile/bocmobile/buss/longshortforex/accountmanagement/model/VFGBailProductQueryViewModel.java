package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * ViewModel：查询可签约保证金产品
 * Created by zhx on 2016/12/8
 */
public class VFGBailProductQueryViewModel {
    // 起始序号
    private String currentIndex;
    // 每页数
    private String pageSize;
    // 刷新标志(true：刷新 false：不刷新 非必输)
    private String _refresh;

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    private int recordNumber;
    private List<VFGBailProduct> list;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<VFGBailProduct> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<VFGBailProduct> getList() {
        return list;
    }

    public static class VFGBailProduct {
        /**
         * bailNo : 3333
         * isSign : 3
         * settleCurrency : 333
         * needMarginRatio : null
         * canOpen : 3
         * bailEName : 3333333333333333
         * openRate : null
         * warnRatio : null
         * liquidationRatio : null
         * bailCName : 3333333333333333333333333333333333333333
         */
        // 保证金产品序号
        private String bailNo;
        // 保证金产品英文名称
        private String bailEName;
        // 保证金产品中文名称
        private String bailCName;
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
        // 允许开仓标识
        private String canOpen;
        // 是否可签约
        private String isSign;

        public String getBailNo() {
            return bailNo;
        }

        public void setBailNo(String bailNo) {
            this.bailNo = bailNo;
        }

        public String getBailEName() {
            return bailEName;
        }

        public void setBailEName(String bailEName) {
            this.bailEName = bailEName;
        }

        public String getBailCName() {
            return bailCName;
        }

        public void setBailCName(String bailCName) {
            this.bailCName = bailCName;
        }

        public String getSettleCurrency() {
            return settleCurrency;
        }

        public void setSettleCurrency(String settleCurrency) {
            this.settleCurrency = settleCurrency;
        }

        public BigDecimal getNeedMarginRatio() {
            return needMarginRatio;
        }

        public void setNeedMarginRatio(BigDecimal needMarginRatio) {
            this.needMarginRatio = needMarginRatio;
        }

        public BigDecimal getWarnRatio() {
            return warnRatio;
        }

        public void setWarnRatio(BigDecimal warnRatio) {
            this.warnRatio = warnRatio;
        }

        public BigDecimal getLiquidationRatio() {
            return liquidationRatio;
        }

        public void setLiquidationRatio(BigDecimal liquidationRatio) {
            this.liquidationRatio = liquidationRatio;
        }

        public BigDecimal getOpenRate() {
            return openRate;
        }

        public void setOpenRate(BigDecimal openRate) {
            this.openRate = openRate;
        }

        public String getCanOpen() {
            return canOpen;
        }

        public void setCanOpen(String canOpen) {
            this.canOpen = canOpen;
        }

        public String getIsSign() {
            return isSign;
        }

        public void setIsSign(String isSign) {
            this.isSign = isSign;
        }
    }
}
