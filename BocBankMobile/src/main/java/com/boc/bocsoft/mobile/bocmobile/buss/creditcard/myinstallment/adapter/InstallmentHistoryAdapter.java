package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.widget.ProgressView;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;


/**
 * Created by yangle on 2016/11/21.
 * 分期记录列表适配器
 */
public class InstallmentHistoryAdapter extends BaseListAdapter<InstallmentRecordModel> {

    public InstallmentHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.boc_item_dividedpayhistory_record, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder  = (ViewHolder) convertView.getTag();
        }
        //  绑定数据
        viewHolder.bindData(getItem(position));
        return convertView;
    }


    public class ViewHolder {

        private final ProgressView mProgress;
        private final TextView mProgressContent;
        private final TextView mInstallmentDate;
        private final TextView mAccomplished;
        private final TextView mInstallmentAmount;
        private final TextView mTvInstmtCategory;
        public ViewHolder(View itemView) {
            mTvInstmtCategory = (TextView) itemView.findViewById(R.id.tv_instmt_category);
            mProgress = (ProgressView) itemView.findViewById(R.id.installment_progress);
            mProgressContent = (TextView) itemView.findViewById(R.id.installment_progress_content);
            mInstallmentDate = (TextView) itemView.findViewById(R.id.installment_date);
            mAccomplished = (TextView) itemView.findViewById(R.id.installment_accomplished);
            mInstallmentAmount = (TextView) itemView.findViewById(R.id.installment_amount);
        }

        public void bindData(InstallmentRecordModel recordModel) {
            mTvInstmtCategory.setText(recordModel.getInstmtCategory());
            int incomeCountForShow = recordModel.isAccomplished() ? recordModel.getInstmtCount() : recordModel.getIncomeTimeCount();
            String progressContent = mContext.getString(R.string.boc_crcd_myinstallment_progress_content, incomeCountForShow, recordModel.getInstmtCount());
            mProgress.setProgressAndMaxValue(incomeCountForShow,recordModel.getInstmtCount());
            setPartialBold(mProgressContent, progressContent);
            mInstallmentDate.setText(recordModel.getInstmtDate());
            mInstallmentAmount.setText(recordModel.getAmountMoneyFormat());
            mAccomplished.setText(recordModel.isAdvanceAccomplished() ? "已提前结清" : "已完成");
            mAccomplished.setVisibility(recordModel.isAccomplished() ? View.VISIBLE : View.GONE);
        }

        // 如"4/12 前半部分4加粗
        private void setPartialBold(TextView installmentCount, String text) {
            String regex = "/";
            if (TextUtils.isEmpty(text) || !text.contains(regex)) {
                throw new IllegalArgumentException("wrong text format");
            }
            String[] split = text.split(regex);
            SpannableString spannableString = new SpannableString(text);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(boldSpan,0,split[0].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            installmentCount.setText(spannableString);
        }

    }



}
