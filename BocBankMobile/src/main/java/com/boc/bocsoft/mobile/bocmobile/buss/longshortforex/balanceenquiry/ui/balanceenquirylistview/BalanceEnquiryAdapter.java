package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.balanceenquirylistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 *  *余额查询公共组件适配器
 * Created by wjk7118 on 2016/11/29.
 */
public class BalanceEnquiryAdapter extends BaseListAdapter<BalanceEnquiryBean>{
    public BalanceEnquiryAdapter(Context context) {
        super(context);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        BalanceEnquiryBean viewModel = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_balance_enquiry_list, null);
            viewHolder.tvSettleCurrency=(TextView)convertView.findViewById(R.id.tvSettleCurrency);
            viewHolder.tvCurrentLimit=(TextView)convertView.findViewById(R.id.tvCurrentLimit);
            viewHolder.tvCurrentRemit=(TextView)convertView.findViewById(R.id.tvCurrentRemit);
            viewHolder.tvCurrentRemitNumber=(TextView)convertView.findViewById(R.id.tvCurrentRemitNumber);
            viewHolder.tvCurrentMoney=(TextView)convertView.findViewById(R.id.tvCurrentMoney);
            viewHolder.tvCurrentMoneyNumber=(TextView)convertView.findViewById(R.id.tvCurrentMoneyNumber);
            viewHolder.normallCurrentShow=(LinearLayout)convertView.findViewById(R.id.normallCurrentShow);
            viewHolder.ll_renminbiCurrent=(LinearLayout)convertView.findViewById(R.id.ll_renminbiCurrent);
            viewHolder.tvRenminbiCurrentRemitNumber=(TextView)convertView.findViewById(R.id.tvRenminbiCurrentRemitNumber);
            viewHolder.normallShow=(LinearLayout)convertView.findViewById(R.id.normallShow);
            viewHolder.tvLimit=(TextView)convertView.findViewById(R.id.tvLimit);
            viewHolder.tvRemit=(TextView)convertView.findViewById(R.id.tvRemit);
            viewHolder.tvRemitNumber=(TextView)convertView.findViewById(R.id.tvRemitNumber);
            viewHolder.tvMoney=(TextView)convertView.findViewById(R.id.tvMoney);
            viewHolder.tvMoneyNumber=(TextView)convertView.findViewById(R.id.tvMoneyNumber);
            viewHolder.ll_renminbi=(LinearLayout)convertView.findViewById(R.id.ll_renminbi);
            viewHolder.tvRenminbiRemitNumber=(TextView)convertView.findViewById(R.id.tvRenminbiRemitNumber);
            viewHolder.tvSingle=(TextView)convertView.findViewById(R.id.tvSingle);
            viewHolder.tvSingleAvailable=(TextView)convertView.findViewById(R.id.tvSingleAvailable);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String currencyCode1= viewModel.getCurrenyA();
        String currencyCode2=viewModel.getCurrenyB();

        if ("001".equals(currencyCode1)){
            viewHolder.normallCurrentShow.setVisibility(View.GONE);
            viewHolder.ll_renminbiCurrent.setVisibility(View.VISIBLE);
            viewHolder.normallShow.setVisibility(View.GONE);
            viewHolder.ll_renminbi.setVisibility(View.VISIBLE);
        }else {
            int num=viewModel.getNumber();
            for (int i = 0; i < num; i++) {
                if (currencyCode1.equals(currencyCode2) && (currencyCode2!= null)) {
                    viewHolder.normallCurrentShow.setVisibility(View.VISIBLE);
                    viewHolder.ll_renminbiCurrent.setVisibility(View.GONE);
                    viewHolder.normallShow.setVisibility(View.VISIBLE);
                    viewHolder.ll_renminbi.setVisibility(View.GONE);
                }
                else{
                    viewHolder.normallCurrentShow.setVisibility(View.GONE);
                    viewHolder.ll_renminbiCurrent.setVisibility(View.VISIBLE);
                    viewHolder.tvSingle.setVisibility(View.VISIBLE);
                    viewHolder.normallShow.setVisibility(View.GONE);
                    viewHolder.ll_renminbi.setVisibility(View.VISIBLE);
                    viewHolder.tvSingleAvailable.setVisibility(View.VISIBLE);
                }
            }
        }

        //第一行信息
        viewHolder.tvSettleCurrency.setText(viewModel.getFirstLineInfo());

        //第二行信息
        viewHolder.tvCurrentLimit.setText(viewModel.getSecLineLeftInfo());
        viewHolder.tvCurrentRemit.setText(viewModel.getSecLineRighAboFInfo());
        viewHolder.tvCurrentRemitNumber.setText(viewModel.getSecLineRighAboSInfo());
        viewHolder.tvCurrentMoney.setText(viewModel.getSecLineRighBeloFInfo());
        viewHolder.tvCurrentMoneyNumber.setText(viewModel.getSecLineRighBeloSInfo());
        viewHolder.tvRenminbiCurrentRemitNumber.setText(viewModel.getSecRenminbi());
        viewHolder.tvSingle.setText(viewModel.getSecSingle());



        //第三行信息
        viewHolder.tvLimit.setText(viewModel.getThrLineLeftInfo());
        viewHolder.tvRemit.setText(viewModel.getThrLineRighAboFInfo());
        viewHolder.tvRemitNumber.setText(viewModel.getThrLineRighAboSInfo());
        viewHolder.tvMoney.setText(viewModel.getThrLineRighBeloFInfo());
        viewHolder.tvMoneyNumber.setText(viewModel.getThrLineRighBeloSInfo());
        viewHolder.tvRenminbiRemitNumber.setText(viewModel.getThrRenminbi());
        viewHolder.tvSingleAvailable.setText(viewModel.getThrSingle());

        return convertView;

        }
        private class ViewHolder {
            TextView tvSettleCurrency;//结算币种
            TextView tvCurrentLimit; //当前额度
            LinearLayout normallCurrentShow;//人民币以外的当前额度线性布局
            TextView tvCurrentRemit; //现汇
            TextView tvCurrentRemitNumber;//现汇金额
            TextView tvCurrentMoney; //现钞
            TextView tvCurrentMoneyNumber;//现钞金额
            LinearLayout ll_renminbiCurrent;//单行显示当前额度线性布局
            TextView tvRenminbiCurrentRemitNumber;//人民币当前额度
            TextView tvSingle;//单行当前额度现钞现汇标识
            LinearLayout normallShow;//人民币以外的可用额度的线性布局
            TextView tvLimit;//可用额度
            TextView tvRemit;//可用现汇
            TextView tvRemitNumber;//可用现汇金额
            TextView tvMoney;//可用现钞
            TextView tvMoneyNumber;//可用现钞金额
            LinearLayout ll_renminbi;//单行显示可用额度线性布局
            TextView tvRenminbiRemitNumber;//人民币可用额度
            TextView tvSingleAvailable;//单行可用额度现钞现汇标识

        }
    }
