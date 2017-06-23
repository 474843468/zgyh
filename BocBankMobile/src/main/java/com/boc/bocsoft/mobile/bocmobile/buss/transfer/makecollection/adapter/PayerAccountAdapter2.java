package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;

import java.util.List;

/**
 * Adapter：付款账户列表(MeMakeCollectionFragment)，我要收款-操作结果-界面用到的
 * Created by zhx on 2016/8/18
 */
public class PayerAccountAdapter2 extends BaseAdapter {
    Context context;
    List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> testBeanList;

    public PayerAccountAdapter2(Context context, List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> testBeanList) {
        this.context = context;
        this.testBeanList = testBeanList;
    }

    public void setTestBeanList(List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> testBeanList) {
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
    public PsnBatchTransActCollectionVerifyViewModel.PayerEntity getItem(int position) {
        return testBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_operate_result_payer, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        PsnBatchTransActCollectionVerifyViewModel.PayerEntity testBean = getItem(position);
        viewHolder.tv_payer_item.setText(testBean.getPayerName() + "(" + testBean.getPayerMobile() + ")");
        return convertView;
    }

    static class ViewHolder {
        TextView tv_payer_item;

        public ViewHolder(View convertView) {
            tv_payer_item = (TextView) convertView.findViewById(R.id.tv_payer_item);
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