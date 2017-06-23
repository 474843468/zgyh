package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.ChangeTask;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * Adapter：进度状态列表适配器
 * Created by zhx on 2016/12/19
 */
public class StateItemAdapter extends BaseAdapter {
    private Context context;
    private List<ChangeTask> taskList;

    public StateItemAdapter(Context context, List<ChangeTask> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        if (taskList != null) {
            return taskList.size();
        }
        return 0;
    }

    @Override
    public ChangeTask getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.dialog_progress_state_list_item, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        ChangeTask item = getItem(position);
        // 币种
        viewHolder.tv_currency.setText(PublicCodeUtils.getCurrency(context, item.getSettleCurrency()));
        // 保证金账号
        String marginAccountNo = item.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == 0) {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            viewHolder.tv_margin_account_no.setText(NumberUtils.formatCardNumber(substring));
        } else {
            viewHolder.tv_margin_account_no.setText(NumberUtils.formatCardNumber(marginAccountNo));
        }
        // 状态
        int state = item.getState();
        switch (state) {
            case 1: // 1表示正在变更
                viewHolder.pb_state.setVisibility(View.VISIBLE);
                viewHolder.iv_state.setVisibility(View.GONE);
                break;
            case 2: // 2表示变更成功
                viewHolder.pb_state.setVisibility(View.GONE);
                viewHolder.iv_state.setVisibility(View.VISIBLE);

                viewHolder.iv_state.setImageResource(R.drawable.ic_success);
                break;
            case 3: // 3表示变更失败
                viewHolder.pb_state.setVisibility(View.GONE);
                viewHolder.iv_state.setVisibility(View.VISIBLE);

                viewHolder.iv_state.setImageResource(R.drawable.ic_fail);
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView iv_state;
        ProgressBar pb_state;
        TextView tv_currency;
        TextView tv_margin_account_no;

        public ViewHolder(View convertView) {
            iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
            pb_state = (ProgressBar) convertView.findViewById(R.id.pb_state);
            tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
            tv_margin_account_no = (TextView) convertView.findViewById(R.id.tv_margin_account_no);
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
