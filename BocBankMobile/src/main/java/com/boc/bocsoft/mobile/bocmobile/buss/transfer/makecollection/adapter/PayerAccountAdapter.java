package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;

import java.util.List;

/**
 * Adapter：付款账户列表(MeMakeCollectionFragment)，我要收款界面用到的
 * Created by zhx on 2016/7/11
 */
public class PayerAccountAdapter extends BaseAdapter {
    Context context;
    List<QueryPayerListViewModel.ResultBean> testBeanList;

    public PayerAccountAdapter(Context context, List<QueryPayerListViewModel.ResultBean> testBeanList) {
        this.context = context;
        this.testBeanList = testBeanList;
    }

    public void setTestBeanList(List<QueryPayerListViewModel.ResultBean> testBeanList) {
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
    public QueryPayerListViewModel.ResultBean getItem(int position) {
        return testBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_payer, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        QueryPayerListViewModel.ResultBean testBean = getItem(position);
        viewHolder.tv_item.setText(testBean.getPayerName());
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
