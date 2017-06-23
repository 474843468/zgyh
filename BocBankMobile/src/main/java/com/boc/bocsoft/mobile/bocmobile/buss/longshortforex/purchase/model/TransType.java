package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * @author wangyang
 *         2016/12/23 09:47
 *         交易类型
 */
public enum TransType {

    /**
     * 市价即时,限价即时,获利委托,止损委托,二选一委托,追加委托,连环委托,追击止损委托
     */
    PRICE_IMMEDIATELY(3, R.string.boc_purchase_trans_type_price_immediately), LIMIT_IMMEDIATELY(3,R.string.boc_purchase_trans_type_limit_immediately),
    ENTRUST_PROFIT(3,R.string.boc_purchase_trans_type_entrust_profit), ENTRUST_STOP(3,R.string.boc_purchase_trans_type_entrust_stop),
    ENTRUST_CHOICE(3,R.string.boc_purchase_trans_type_entrust_choice), ENTRUST_ADD(3,R.string.boc_purchase_trans_type_entrust_add),
    ENTRUST_SERIAL(3,R.string.boc_purchase_trans_type_entrust_serial),ENTRUST_PURSUIT_STOP(3,R.string.boc_purchase_trans_type_entrust_pursuit_stop);

    private int refreshFrequency;

    private int titleId;

    TransType(int refreshFrequency,int titleId) {
        this.refreshFrequency = refreshFrequency;
        this.titleId = titleId;
    }

    public int getRefreshFrequency() {
        return refreshFrequency;
    }

    public String getTitle(Context context){
        return context.getResources().getString(titleId);
    }

}
