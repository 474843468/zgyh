package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductquerylist;

import java.util.List;

/**
 * I42-4.12 012持有产品查询与赎回  PsnXpadHoldProductQueryList  响应Model
 * Created by cff on 2016/9/7.
 */
public class PsnXpadHoldProductQueryListResModel {
    private List<PsnXpadHoldProductQueryListResListModel> listModel;

    public List<PsnXpadHoldProductQueryListResListModel> getListModel() {
        return listModel;
    }

    public void setListModel(List<PsnXpadHoldProductQueryListResListModel> listModel) {
        this.listModel = listModel;
    }

}
