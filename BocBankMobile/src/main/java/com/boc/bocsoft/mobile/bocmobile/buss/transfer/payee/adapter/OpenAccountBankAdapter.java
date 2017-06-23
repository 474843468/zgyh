package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransQueryExternalBankInfoViewModel;

import java.util.List;

/**
 * 开户银行列表适配器
 * Created by zhx on 2016/7/22
 */
public class OpenAccountBankAdapter extends BaseAdapter {
    private Context context;
    List<PsnTransQueryExternalBankInfoViewModel.OpenBankBean> viewList;

    public OpenAccountBankAdapter(Context context, List<PsnTransQueryExternalBankInfoViewModel.OpenBankBean> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        if (viewList != null && viewList.size() > 0) {
            return viewList.size();
        }
        return 0;
    }

    @Override
    public PsnTransQueryExternalBankInfoViewModel.OpenBankBean getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.list_item_open_account_bank, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);
        PsnTransQueryExternalBankInfoViewModel.OpenBankBean openBankBean = getItem(position);
        holder.tv_bank_name.setText(openBankBean.getBankName());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_bank_name;

        public ViewHolder(View convertView) {
            tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
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