package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;

import java.util.List;

/**
 * Adapter：HCE 卡列表适配器
 * Created by gengjunying on 2016/12/13
 */
public class HceCardListAdapter extends BaseAdapter {
    private Context context;
    private List<HceCardListQueryViewModel> list;

    public HceCardListAdapter(Context context, List<HceCardListQueryViewModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<HceCardListQueryViewModel> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public HceCardListQueryViewModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_equickpay_hcecard, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        HceCardListQueryViewModel HceCardItemEntity = getItem(position);
        viewHolder.textview_hce_account.setText(HceCardItemEntity.getSlaveCardNo()); // 借记卡卡号


        return convertView;
    }

    static class ViewHolder {
        TextView textview_hce_account;


        public ViewHolder(View convertView) {

            textview_hce_account = (TextView) convertView.findViewById(R.id.textview_hce_account);
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
