package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         2016/12/20 13:52
 */
public enum TabType {

    CREATE, FIRST_OPEN_FLAT, DELETE, NONE;

    public void setChecked(RadioGroup rgTab) {
        switch (this) {
            case CREATE:
                ((RadioButton) rgTab.getChildAt(0)).setChecked(true);
                break;
            case FIRST_OPEN_FLAT:
                ((RadioButton) rgTab.getChildAt(1)).setChecked(true);
                break;
            case DELETE:
                ((RadioButton) rgTab.getChildAt(2)).setChecked(true);
                break;
        }
    }

    public TabType getTabType(RadioGroup rgTab, int checkedId) {
        int index = -1;
        for (int i = 0; i < rgTab.getChildCount(); i++) {
            if (checkedId == rgTab.getChildAt(i).getId()) {
                index = i;
                break;
            }
        }

        switch (index) {
            case 0:
                return CREATE;
            case 1:
                return FIRST_OPEN_FLAT;
            case 2:
                return DELETE;
        }
        return null;
    }

    /**
     * 获取交易方式列表
     *
     * @param context
     * @return
     */
    public List<String> getTransList(Context context) {
        List transList = new ArrayList();
        switch (this) {
            case CREATE:
            case NONE:
                transList.add(TransType.PRICE_IMMEDIATELY.getTitle(context));
                transList.add(TransType.LIMIT_IMMEDIATELY.getTitle(context));
                transList.add(TransType.ENTRUST_PROFIT.getTitle(context));
                transList.add(TransType.ENTRUST_STOP.getTitle(context));
                transList.add(TransType.ENTRUST_CHOICE.getTitle(context));
                transList.add(TransType.ENTRUST_ADD.getTitle(context));
                transList.add(TransType.ENTRUST_SERIAL.getTitle(context));
                break;
            case FIRST_OPEN_FLAT:
                transList.add(TransType.PRICE_IMMEDIATELY.getTitle(context));
                transList.add(TransType.LIMIT_IMMEDIATELY.getTitle(context));
                transList.add(TransType.ENTRUST_PROFIT.getTitle(context));
                transList.add(TransType.ENTRUST_STOP.getTitle(context));
                transList.add(TransType.ENTRUST_CHOICE.getTitle(context));
                transList.add(TransType.ENTRUST_ADD.getTitle(context));
                transList.add(TransType.ENTRUST_SERIAL.getTitle(context));
                transList.add(TransType.ENTRUST_PURSUIT_STOP.getTitle(context));
                break;
            case DELETE:
                transList.add(TransType.PRICE_IMMEDIATELY.getTitle(context));
                transList.add(TransType.LIMIT_IMMEDIATELY.getTitle(context));
                break;
        }
        return transList;
    }

    /**
     * 获得选择交易类型
     * @param context
     * @param item
     * @return
     */
    public TransType getTransType(Context context, String item) {
        if (item.equals(TransType.PRICE_IMMEDIATELY.getTitle(context)))
            return TransType.PRICE_IMMEDIATELY;
        if (item.equals(TransType.LIMIT_IMMEDIATELY.getTitle(context)))
            return TransType.LIMIT_IMMEDIATELY;
        if (item.equals(TransType.ENTRUST_PROFIT.getTitle(context)))
            return TransType.ENTRUST_PROFIT;
        if (item.equals(TransType.ENTRUST_STOP.getTitle(context)))
            return TransType.ENTRUST_STOP;
        if (item.equals(TransType.ENTRUST_CHOICE.getTitle(context)))
            return TransType.ENTRUST_CHOICE;
        if (item.equals(TransType.ENTRUST_ADD.getTitle(context)))
            return TransType.ENTRUST_ADD;
        if (item.equals(TransType.ENTRUST_SERIAL.getTitle(context)))
            return TransType.ENTRUST_SERIAL;
        if (item.equals(TransType.ENTRUST_PURSUIT_STOP.getTitle(context)))
            return TransType.ENTRUST_PURSUIT_STOP;
        return null;
    }
}
