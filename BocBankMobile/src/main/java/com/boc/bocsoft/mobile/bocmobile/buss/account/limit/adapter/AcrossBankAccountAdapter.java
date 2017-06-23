package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * 跨行订购账户列表适配器
 * Created by zhx on 2016/10/14
 */
public class AcrossBankAccountAdapter extends BaseListAdapter<LimitModel> {


    public AcrossBankAccountAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_item_across_bank_account, null);
        }

        ViewHolder holder = ViewHolder.getHolder(convertView);
        holder.btnAccount.setData(getItem(position));

        return convertView;
    }


    static class ViewHolder {
        SelectAccountButton btnAccount;

        public ViewHolder(View convertView) {
            btnAccount = (SelectAccountButton) convertView.findViewById(R.id.btn_account);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
