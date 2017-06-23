package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyang
 *         16/7/26 16:03
 *         虚拟银行卡查询
 */
public class PsnCrcdVirtualCardQueryResult {

    private int recordNumber;
    /** 单笔交易限额最大值 */
    private BigDecimal maxSingLeamt;
    /** 虚拟卡列表 */
    private List<VirCardInfo> virCardList;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public BigDecimal getMaxSingLeamt() {
        return maxSingLeamt;
    }

    public void setMaxSingLeamt(BigDecimal maxSingLeamt) {
        this.maxSingLeamt = maxSingLeamt;
    }

    public List<VirCardInfo> getVirCardList() {
        return virCardList;
    }

    public void setVirCardList(List<VirCardInfo> virCardList) {
        this.virCardList = virCardList;
    }

}
