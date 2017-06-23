package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.TermlyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

import org.threeten.bp.LocalDate;

import java.util.Collections;
import java.util.List;

/**
 * 定期一本通adapter
 * Created by niuguobin on 2016/6/15.
 */
public class OverviewTermlyAdapter extends BaseListAdapter<TermlyViewModel> {

    public OverviewTermlyAdapter(Context context) {
        super(context);
    }

    @Override
    public void setDatas(List<TermlyViewModel> datas) {
        //到期日排序
        Collections.sort(datas);
        super.setDatas(datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_view_account_termly, null);
            holder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
            holder.tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        switch (mList.get(position).getStatus()) {
            case TermlyViewModel.STATUS_NORMAL://正常
                holder.iv_status.setVisibility(View.GONE);
                break;
            case TermlyViewModel.STATUS_CANCEL://已销户
                holder.iv_status.setVisibility(View.VISIBLE);
                holder.iv_status.setImageResource(R.drawable.boc_account_status_cancelled);
                break;
            case TermlyViewModel.STATUS_BD://不动
                holder.iv_status.setVisibility(View.GONE);
                break;
            case TermlyViewModel.STATUS_LOSS://挂失
                holder.iv_status.setVisibility(View.VISIBLE);
                holder.iv_status.setImageResource(R.drawable.boc_account_status_reportloss);
                break;
            case TermlyViewModel.STATUS_FREEZE://冻结
                holder.iv_status.setVisibility(View.VISIBLE);
                holder.iv_status.setImageResource(R.drawable.boc_account_status_frozen);
                break;
            case TermlyViewModel.STATUS_OTHER://其他
                holder.iv_status.setVisibility(View.GONE);
                break;
        }

        String type = mList.get(position).getType();
        switch (type){
            case AccountTypeUtil.REGULAR_TYPE_ZCZQ:
                holder.tv_type.setText(PublicCodeUtils.getDepositReceiptType(mContext, mList.get(position).getType()) + " (" + PublicCodeUtils.getCdPeriod(mContext, mList.get(position).getCdPeriod()) + ")");
                break;
            case AccountTypeUtil.REGULAR_TYPE_TZCK:
                String period = PublicCodeUtils.getCdPeriodDay(mContext, mList.get(position).getCdPeriod());
                period = period.replaceAll("通知存款","");
                holder.tv_type.setText(PublicCodeUtils.getDepositReceiptType(mContext, mList.get(position).getType()) + " (" + period + ")");
                break;
            default:
                holder.tv_type.setText(PublicCodeUtils.getDepositReceiptType(mContext, mList.get(position).getType()));
                break;
        }

        LocalDate interestEndDate = mList.get(position).getInterestEndDate();
        if (interestEndDate != null) {
            holder.tv_time.setText("到期日" + interestEndDate.format(DateFormatters.dateFormatter1));
        } else {
            holder.tv_time.setText("到期日 " + mContext.getResources().getString(R.string.boc_overview_detail_regular_info_no_start_end_date));
        }

        if (mList.get(position).getType().equals(AccountTypeUtil.REGULAR_TYPE_DHLB))
            holder.tv_time.setText("到期日 " + mContext.getResources().getString(R.string.boc_overview_detail_regular_info_no_start_end_date));

        holder.tv_sum.setText(MoneyUtils.transMoneyFormat(mList.get(position).getAvailableBalance(), mList.get(position).getCurrencyCode()));

        String cashRemit = AccountUtils.getCashRemit(mList.get(position).getCashRemit());
        if (StringUtils.isEmptyOrNull(cashRemit))
            holder.tv_currency.setText(PublicCodeUtils.getCurrency(mContext, mList.get(position).getCurrencyCode()));
        else
            holder.tv_currency.setText(PublicCodeUtils.getCurrency(mContext, mList.get(position).getCurrencyCode()) + "/" + cashRemit);

        setOnClickChildViewInItemItf(position, mList.get(position), convertView);

        return convertView;
    }

    public class ViewHolder {
        public ImageView iv_status;
        public TextView tv_type, tv_time, tv_sum, tv_currency;
    }
}
