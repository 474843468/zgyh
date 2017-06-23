package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanBranchBean;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * Created by XieDu on 2016/7/27.
 */
public class BranchSelectAdapter extends BaseListAdapter<OnLineLoanBranchBean> {

    public BranchSelectAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OnLineLoanBranchBean item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_item_branch_select, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvBranchName.setText(item.getDeptName());
        viewHolder.tvBranchAddress.setText(item.getDeptAddr());
        return convertView;
    }

    private class ViewHolder {

        TextView tvBranchName;
        TextView tvBranchAddress;

        ViewHolder(View itemView) {
            tvBranchName = (TextView) itemView.findViewById(R.id.tv_branch_name);
            tvBranchAddress = (TextView) itemView.findViewById(R.id.tv_branch_address);
        }
    }
}
