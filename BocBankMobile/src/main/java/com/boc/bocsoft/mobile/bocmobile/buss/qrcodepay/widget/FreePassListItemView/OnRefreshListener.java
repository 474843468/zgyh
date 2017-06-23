package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView;

import android.view.View;

/**
 * Created by wangf on 2016/7/8.
 */
public interface OnRefreshListener {
    void onRefresh(int position, FreePassViewModel item);
    void onClickCheckBox(int position, FreePassViewModel item, View childView);
}