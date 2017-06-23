package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ImageLoaderUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * @author wangyang
 *         16/7/21 14:23
 *         虚拟银行卡列表Adapter
 */
public class VirtualCardAdapter extends BaseListAdapter<VirtualCardModel> {

    public VirtualCardAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.boc_item_vircard_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        VirtualCardModel model = getItem(position);
        viewHolder.tvNumber.setText(NumberUtils.formatCardNumber2(model.getAccountIbkNum()));
        viewHolder.tvDate.setText(mContext.getString(R.string.boc_virtual_account_list_item_date, model.getEndDate().format(
                DateFormatters.dateFormatter1)));
        setCardStatusPic(viewHolder,model.getAccountStatus());

        return convertView;
    }

    private void setCardStatusPic(ViewHolder holder, String status) {
        if (VirtualCardModel.STATUS_VALID.equals(status))
            holder.ivStatus.setImageDrawable(null);
        else
            ImageLoaderUtils.getBIIImageLoader().load(R.drawable.boc_account_status_cancelled).into(holder.ivStatus);
    }

    private class ViewHolder {
        /**
         * 卡号,失效日期
         */
        private TextView tvNumber, tvDate;
        /**
         * 状态
         */
        private ImageView ivStatus;

        public ViewHolder(View itemView) {
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);

            ivStatus = (ImageView) itemView.findViewById(R.id.iv_status);
        }
    }
}
