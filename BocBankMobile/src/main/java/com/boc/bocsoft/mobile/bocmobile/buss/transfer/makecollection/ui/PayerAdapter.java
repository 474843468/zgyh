package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器：付款人列表
 * Created by zhx on 2016/7/19
 */
public class PayerAdapter extends BaseAdapter {
    private String filterWord;
    private Context context;
    private List<QueryPayerListViewModel.ResultBean> testBeanList;
    private List<QueryPayerListViewModel.ResultBean> testBeanFilterList = new ArrayList<QueryPayerListViewModel.ResultBean>();


    public void setFilterWord(String filterWord) {
        this.filterWord = filterWord;
        notifyDataSetChanged();
    }

    public PayerAdapter(Context context, List<QueryPayerListViewModel.ResultBean> testBeanList) {
        this.context = context;
        this.testBeanList = testBeanList;
    }

    @Override
    public int getCount() {
        if (testBeanList != null && testBeanList.size() > 0) {
            if (!TextUtils.isEmpty(filterWord)) {
                testBeanFilterList.clear();

                for (QueryPayerListViewModel.ResultBean testBean : testBeanList) {
                    if (testBean.getPayerName().contains(filterWord)) {
                        testBeanFilterList.add(testBean);
                    }
                }

                return testBeanFilterList.size();
            }
            return testBeanList.size();
        }
        return 0;
    }

    public QueryPayerListViewModel.ResultBean updateItem(int position, View view) {
        getItem(position).setChecked(!getItem(position).isChecked());
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_is_choose);
        checkBox.setChecked(getItem(position).isChecked());

        return getItem(position);
    }

    @Override
    public QueryPayerListViewModel.ResultBean getItem(int position) {
        QueryPayerListViewModel.ResultBean testBean;
        if (!TextUtils.isEmpty(filterWord)) {
            testBean = testBeanFilterList.get(position);
        } else {
            testBean = testBeanList.get(position);
        }
        return testBean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_payer_account, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        QueryPayerListViewModel.ResultBean testBean = getItem(position);
        viewHolder.tv_payer_name.setText(testBean.getPayerName());
        viewHolder.cb_is_choose.setChecked(testBean.isChecked());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_payer_name;
        CheckBox cb_is_choose;

        public ViewHolder(View convertView) {
            tv_payer_name = (TextView) convertView.findViewById(R.id.tv_payer_name);
            cb_is_choose = (CheckBox) convertView.findViewById(R.id.cb_is_choose);
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
