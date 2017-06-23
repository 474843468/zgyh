package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;


import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/15.
 * 未结清记录
 */
public class RepayRecordAdapter extends BaseAdapter {
    /**context*/
    private Context mContext;
    /**传值*/
    private List<EloanAccountListModel.PsnLOANListEQueryBean> mEloanList;
    /**未逾期布局*/
    public static final int ELOAN_RECORD = 0;
    /**逾期布局*/
    public static final int ELOAN_OVERDE = 1;

    public RepayRecordAdapter(Context context) {
    	mContext = context;
        mEloanList = new ArrayList<EloanAccountListModel.PsnLOANListEQueryBean>();
        
    }

    /*
    * 显示未结清列表数据
    */
    public void setRepayRcdData(List<EloanAccountListModel.PsnLOANListEQueryBean> eloanList) {
        mEloanList = eloanList;
    }


    @Override
    public int getCount() {
        return mEloanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEloanList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (mEloanList.get(position).getOverDueStat().equals("1")
                || mEloanList.get(position).getOverDueStat().equals("01") ) {
            return ELOAN_OVERDE;
        } else {
            return ELOAN_RECORD;
        }

    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
    	ViewHodler viewHodler = null;
    	
        if (convertView == null) {
            if (type == ELOAN_RECORD) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.boc_edrawingrecord_item, parent, false);
            } else  if (type == ELOAN_OVERDE ){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.boc_settlerecord_item, parent, false);
            }

            ViewFinder finder = new ViewFinder(convertView);
            viewHodler = new ViewHodler(finder);
            convertView.setTag(viewHodler);
            
        } else {
        	viewHodler =(ViewHodler)convertView.getTag();
        }
        
        viewHodler.loanApplyDateTv.setText(mEloanList.get(position).getLoanDate());
        viewHodler.loanApplyAmountTv.setText(mContext.getString(R.string.boc_eloan_draw_pagename)+ MoneyUtils.transMoneyFormat(mEloanList.get(position).getLoanCycleAdvVal(),"001"));

        if (type == ELOAN_OVERDE) {
            viewHodler.loanCycleLifeTermTv.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
            viewHodler.loanCycleLifeTermTv.setText(mContext.getString(R.string.boc_overduehit));
        } else  if (type == ELOAN_RECORD){
            viewHodler.thisIssueRepayDateTv.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_common_gray));
            viewHodler.thisIssueRepayDateTv.setText(mContext.getString(R.string.boc_details_date)+ mEloanList.get(position).getThisIssueRepayDate());
            viewHodler.darwDateilTv.setText(mContext.getString(R.string.boc_repay));
            viewHodler.thisIssueRepayAmountTv.setText(MoneyUtils.transMoneyFormat(mEloanList.get(position).getThisIssueRepayAmount(), "001"));
        }
        return convertView;
    }
    
    
    public class ViewHodler {
    	
    	private TextView loanApplyDateTv;
        private TextView loanApplyAmountTv;
        private TextView thisIssueRepayDateTv;
        private TextView darwDateilTv;
        private TextView thisIssueRepayAmountTv;
        private TextView loanCycleLifeTermTv;
        
        public ViewHodler(ViewFinder finder) {
        	 loanApplyDateTv = finder.find( R.id.loanApplyDateTv);
             loanApplyAmountTv = finder.find(R.id.loanApplyAmountTv);
             thisIssueRepayDateTv = finder.find(R.id.thisIssueRepayDateTv);
             darwDateilTv = finder.find(R.id.darwDateilTv);
             thisIssueRepayAmountTv = finder.find(R.id.thisIssueRepayAmountTv);
             loanCycleLifeTermTv = finder.find(R.id.loanCycleLifeTermTv);
        }
    }

}
