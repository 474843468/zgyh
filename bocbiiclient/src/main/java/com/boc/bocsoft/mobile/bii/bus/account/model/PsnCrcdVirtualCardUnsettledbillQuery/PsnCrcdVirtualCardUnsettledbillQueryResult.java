package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillQuery;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.CrcdTransaction;

import java.util.List;

/**
 * @author wangyang
 *         16/7/26 17:36
 *         虚拟银行卡未出账单查询
 */
public class PsnCrcdVirtualCardUnsettledbillQueryResult {

    /** 记录数 */
    private int recordNumber;
    /** 交易列表 */
    private List<CrcdTransaction> crcdTransactionList;

    public List<CrcdTransaction> getCrcdTransactionList() {
        return crcdTransactionList;
    }

    public void setCrcdTransactionList(List<CrcdTransaction> crcdTransactionList) {
        this.crcdTransactionList = crcdTransactionList;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }


}
