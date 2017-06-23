package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyModel;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * Created by guokai on 2016/9/12.
 */
public class InvestTreatyAdapter extends BaseListAdapter {

    private boolean state = true;

    public InvestTreatyAdapter(Context context) {
        super(context);
    }

    public void setState(boolean state){
        this.state = state;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.boc_fragment_list_invest_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.tvValue = (TextView) convertView.findViewById(R.id.txt_value);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.txt_number);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        InvestTreatyModel.CapacityQueryBean item = (InvestTreatyModel.CapacityQueryBean) getItem(position);
        holder.tvName.setText(item.getAgrName());
        holder.tvValue.setText("["+ PublicCodeUtils.getCurrency(mContext, item.getProCur())+"]"+item.getProName());
        if (state) {
            String accno = item.getAccNo().replace(item.getAccNo().substring(4, item.getAccNo().length() - 4), "******");
            holder.tvNumber.setText(accno);
        }else{
            holder.tvNumber.setText(item.getMemo());
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvName;
        public TextView tvValue;
        public TextView tvNumber;
    }
}
