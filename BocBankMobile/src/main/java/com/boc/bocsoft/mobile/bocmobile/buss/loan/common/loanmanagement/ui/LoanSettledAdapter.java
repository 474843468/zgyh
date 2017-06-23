package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/10/31.
 * 已结清贷款适配器
 */
public class LoanSettledAdapter extends BaseAdapter {
    /**上下文*/
    private Context mContext;
    /**052贷款产品*/
    private List<LoanAccountListModel.PsnLOANListEQueryBean> mLoanAccountList;

    public LoanSettledAdapter( Context context) {
        mContext = context;
        mLoanAccountList = new ArrayList<LoanAccountListModel.PsnLOANListEQueryBean>();

    }

    public void setLoanAccountList (List<LoanAccountListModel.PsnLOANListEQueryBean> loanSaccountList) {
        mLoanAccountList = loanSaccountList;
    }


    @Override
    public int getCount() {

        return mLoanAccountList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLoanAccountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHodler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.boc_item_loan_hostory_record, null);
            ViewFinder finder = new ViewFinder(convertView);
            viewHodler = new ViewHolder(finder);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHolder) convertView.getTag();
        }

        String loanType = PublicCodeUtils.getLoanTypeName(mContext,
                mLoanAccountList.get(position).getLoanType());
        //贷款名称
        viewHodler.tvTodate.setText(loanType);
        //日期
        viewHodler.tvPeriod.setText(mLoanAccountList.get(position).getLoanDate());
        //金额名称
        viewHodler.tvCurrency.setText(getCurrency(mLoanAccountList.get(position).getCurrencyCode()));
        if ("R".equals(mLoanAccountList.get(position).getCycleType())) {
            viewHodler.tvMoney.setText(MoneyUtils.transMoneyFormat(mLoanAccountList.get(position).getLoanCycleAppAmount(),
                    mLoanAccountList.get(position).getCurrencyCode()));
        } else {
            viewHodler.tvMoney.setText(MoneyUtils.transMoneyFormat(mLoanAccountList.get(position).getLoanCycleAdvVal(),
                    mLoanAccountList.get(position).getCurrencyCode()));
        }
        return convertView;
    }

    /**
     * 币种格式化
     */
    private String getCurrency(String  currency) {
        String mCurrency = null;
        if (!TextUtils.isEmpty(currency)) {
            if (currency.equals("CNY")) {
                mCurrency = mContext.getString(R.string.boc_loan_mcurrency);
            } else {
                mCurrency = PublicCodeUtils.getCurrencyWithLetter(mContext, currency);
            }
        }
        return  mCurrency;
    }

     class ViewHolder {
        protected TextView tvTodate;
        protected TextView tvPeriod;
        protected TextView tvMoney;
        protected TextView tvCurrency;

        public ViewHolder(ViewFinder finder) {
            tvTodate = (TextView) finder.find(R.id.tv_todate);
            tvPeriod = (TextView) finder.find(R.id.tv_period);
            tvMoney = (TextView) finder.find(R.id.tv_money);
            tvCurrency = (TextView) finder.find(R.id.tv_currency);
        }
    }
}
