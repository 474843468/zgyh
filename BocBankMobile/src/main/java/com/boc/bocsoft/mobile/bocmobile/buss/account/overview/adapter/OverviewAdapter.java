package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.AccountListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;

/**
 * @author wangyang
 *         16/8/11 00:27
 *         账户概览Adapter
 */
public class OverviewAdapter extends AccountListAdapter {

    private ListView listView;

    public OverviewAdapter(ListView listView, Context context) {
        super(context);
        this.listView = listView;
    }

    public void updateItem(AccountListItemViewModel model) {
        int position = getItemPosition(model);
        if (position < 0)
            return;

        onLoadAmountInfoFinished(position);
        getDatas().set(position, model);
        updateItemView(listView, position);
    }

    public void updateItemNickName(AccountBean accountBean) {
        if (AccountTypeUtil.getRegularType().contains(accountBean.getAccountType()))
            accountBean.setAccountStatus("");

        AccountListItemViewModel model = new AccountListItemViewModel();
        model.setAccountBean(accountBean);

        int position = getItemPosition(model);
        if (position < 0)
            return;

        getItem(position).getAccountBean().setNickName(accountBean.getNickName());
        updateItemView(listView, position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountBean accountBean = getItem(position).getAccountBean();
        if (AccountTypeUtil.getRegularType().contains(accountBean.getAccountType()))
            accountBean.setAccountStatus("");
        return super.getView(position, convertView, parent);
    }
}
