package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.TestBean;

import java.util.List;

/**
 * Adapter：选择收款帐户（底部的）
 * Created by zhx on 2016/8/10
 */
public class BottomPayerAccountAdapter extends BaseAdapter {
    private Context context;
    private List<TestBean> testBeanList;

    public BottomPayerAccountAdapter(Context context, List<TestBean> testBeanList) {
        this.context = context;
        this.testBeanList = testBeanList;
    }

    @Override
    public int getCount() {
        if (testBeanList != null) {
            return testBeanList.size();
        }
        return 0;
    }

    @Override
    public TestBean getItem(int position) {
        return testBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_payer_bottom, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        TestBean testBean = getItem(position);
        viewHolder.tv_item.setText(testBean.name);
        return convertView;
    }

    static class ViewHolder {
        TextView tv_item;

        public ViewHolder(View convertView) {
            tv_item = (TextView) convertView.findViewById(R.id.tv_item);
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
