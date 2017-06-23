package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView;

import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;

/**
 * Created by XieDu on 2016/7/8.
 */
public interface OnRefreshListener {
    void onRefresh(int position, AccountListItemViewModel item);
}