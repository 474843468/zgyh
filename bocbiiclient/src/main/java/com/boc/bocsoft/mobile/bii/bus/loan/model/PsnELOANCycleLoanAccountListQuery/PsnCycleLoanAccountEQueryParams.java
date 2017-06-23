package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery;

/**
 * Created by huixiaobo on 2016/6/16.
 * 个人循环列表查询上送参数
 */
public class PsnCycleLoanAccountEQueryParams {
    /**结清标示  Y：结清 N：非结清 空：全部*/
    private String eFlag;
    /**E贷标识 01：WLCF 02：PLCF 10：非签约额度 99：全部*/
    private String eLoanState;

    public String getFlag() {
        return eFlag;
    }

    public String geteLoanState() {
        return eLoanState;
    }

    public void seteLoanState(String eLoanState) {
        this.eLoanState = eLoanState;
    }

    public void setFlag(String flag) {
        this.eFlag = flag;
    }

    @Override
    public String toString() {
        return "PsnCycleLoanAccountEQueryParams{" +
                "flag='" + eFlag + '\'' +
                ", eLoanState='" + eLoanState + '\'' +
                '}';
    }
}
