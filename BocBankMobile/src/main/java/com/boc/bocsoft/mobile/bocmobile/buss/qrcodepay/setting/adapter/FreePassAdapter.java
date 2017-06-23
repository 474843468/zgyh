package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView.FreePassListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView.FreePassViewModel;

/**
 * 小额免密adapter
 * @author wangf
 */
public class FreePassAdapter extends FreePassListAdapter {

    private ListView listView;

    public FreePassAdapter(ListView listView, Context context) {
        super(context);
        this.listView = listView;
    }

    public void updateItem(FreePassViewModel model) {
        int position = getItemPosition(model);
        if (position < 0)
            return;

        onLoadAmountInfoFinished(position);
        getDatas().set(position, model);
        updateItemView(listView, position);
    }

    public void updateItemNickName(AccountBean accountBean) {
        FreePassViewModel model = new FreePassViewModel();
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
        return super.getView(position, convertView, parent);
    }
}
