package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLukLoad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjunying on 2016/12/6.
 * HCE闪付卡LUK加载
 */
public class PsnHCEQuickPassLukLoadResult {

    private Integer recordNumber;
    List<String> keyInfoList = new ArrayList<>();

    public List<String> getKeyInfoList() {
        return keyInfoList;
    }

    public void setKeyInfoList(List<String> keyInfoList) {
        this.keyInfoList = keyInfoList;
    }

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }


}
