package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.ListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配器
 * <p/>
 * Created by liuweidong on 2016/8/13.
 */
public class ListVerticalAdapter extends BaseAdapter {
    private Context mContext;
    private List<ListModel> mList;

    public ListVerticalAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    public void setData(List<ListModel> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ListModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_fragment_list_vertical_item, null);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txtValue = (TextView) convertView.findViewById(R.id.txt_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(mList.get(position).getName());
        viewHolder.txtValue.setText(mList.get(position).getValue());
        return convertView;
    }

    private class ViewHolder {
        private TextView txtName;
        private TextView txtValue;
    }
}
