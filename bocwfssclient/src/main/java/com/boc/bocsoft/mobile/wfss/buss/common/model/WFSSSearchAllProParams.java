package com.boc.bocsoft.mobile.wfss.buss.common.model;

import com.boc.bocsoft.mobile.wfss.buss.common.WFSSBaseParamsModel;
import com.boc.bocsoft.mobile.wfss.common.globle.WFSSGlobalConst;

/**
 * 4.1 全局搜索
 * Created by gwluo on 2016/11/5.
 */

public class WFSSSearchAllProParams extends WFSSBaseParamsModel {

    private String key;//	关键字	最长50

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getModuleName() {
        return WFSSGlobalConst.WFSS_COMM;
    }

    @Override
    public String getMethodName() {
        return "searchAllPro";
    }
}
