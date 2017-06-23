package com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.bean.ListHorizontalBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 水平列表
 *
 * Created by liuweidong on 2016/6/14.
 */
public class ListHorizontalAdapter extends BaseAdapter {
    private Context mContext;
    private List<ListHorizontalBean> mList = new ArrayList<>();

    public ListHorizontalAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<ListHorizontalBean> list){
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_fragment_list_horizontal_item, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_key);
            holder.txtValue = (TextView) convertView.findViewById(R.id.txt_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtName.setText(mList.get(position).getName());
        holder.txtValue.setText(mList.get(position).getValue());
        return convertView;
    }

    private class ViewHolder {
        private TextView txtName;
        private TextView txtValue;
    }
}
