package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 基金-账户管理-TA账户列表List的适配器
 * Created by lyf7084 on 2016/11/29.
 */
public class TaAccListAdapter extends BaseAdapter {

    private Context mContext;
    private TaAccountModel model;

    public TaAccListAdapter(Context context) {
        mContext = context;
    }

    public void setTaAccountModel(TaAccountModel value) {
        model = value;
        notifyDataSetChanged();
    }

    //    获得List列项数
    @Override
    public int getCount() {
        return null == model ? 0 : model.getTaAccountList().size();
    }

    //    获得item内容
    @Override
    public Object getItem(int position) {
        return null == model ? null : model.getTaAccountList().get(position);
    }

    //    获得itemId
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        convertView的初始化
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_taaccount_list_item, null);
            holder = new ViewHolder();
            holder.tvTaAccountNo = (TextView) convertView.findViewById(R.id.tvTaAccountNo);
            holder.tvFundRegName = (TextView) convertView.findViewById(R.id.tvFundRegName);
            holder.tvAccountStatus = (TextView) convertView.findViewById(R.id.tvAccountStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaAccountModel.TaAccountBean entity = model.getTaAccountList().get(position);  // 获得当前位置item数据

        String fundRegName = entity.getFundRegName();
        holder.tvFundRegName.setText(fundRegName);
        String taAccountNo = entity.getTaAccountNo();
        holder.tvTaAccountNo.setText(taAccountNo);
        //账户状态 00：正常; 01：销户处理中;02：取消处理中;03：冻结
        String accountStatus = entity.getAccountStatus();
        if("00".equals(accountStatus)){
            holder.tvAccountStatus.setVisibility(View.INVISIBLE);
        }
        else if("01".equals(accountStatus)){
            holder.tvAccountStatus.setVisibility(View.VISIBLE);
            holder.tvAccountStatus.setText("销户处理中");
            holder.tvAccountStatus.setTextColor(
                    mContext.getResources().getColor(R.color.boc_text_color_money_count));
        }
        else if("02".equals(accountStatus)){
            holder.tvAccountStatus.setVisibility(View.VISIBLE);
            holder.tvAccountStatus.setText("取消处理中");
            holder.tvAccountStatus.setTextColor(
                    mContext.getResources().getColor(R.color.boc_text_color_money_count));
        }
        else if("03".equals(accountStatus)){
            holder.tvAccountStatus.setVisibility(View.VISIBLE);
            holder.tvAccountStatus.setText("冻结");
            holder.tvAccountStatus.setTextColor(
                    mContext.getResources().getColor(R.color.boc_text_color_red));
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView tvTaAccountNo;
        public TextView tvFundRegName;
        public TextView tvAccountStatus;
    }
}