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

import java.util.List;

/**
 * Created by huixioabo on 2016/7/1.
 * 还款首页显示10条还款记录
 */
public class RepayAdapter extends BaseAdapter{
    /**context*/
    private Context mContext;
    /**获取布局*/
    private LayoutInflater mInflater;
    /**未逾期布局*/
    public static final int ELOAN_RECORD = 0;
    /**逾期布局*/
    public static final int ELOAN_OVERDE = 1;

    /**传值*/
    private List<EloanAccountListModel.PsnLOANListEQueryBean> mEloanList;

    public RepayAdapter(Context context, List<EloanAccountListModel.PsnLOANListEQueryBean> eloanList) {
    	mContext = context;
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
        if (mEloanList.get(position).getOverDueStat()!= null &&
                mEloanList.get(position).getOverDueStat().equals("1")
                || mEloanList.get(position).getOverDueStat().equals("01")) {
            return ELOAN_OVERDE;
        } else {
            return ELOAN_RECORD;
        }
       // return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
    	ViewHodler hodler = null;
        if (convertView == null) {
            if (type == ELOAN_RECORD) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.boc_edrawingrecord_item, parent, false);
            } else if(type == ELOAN_OVERDE) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.boc_settlerecord_item, parent, false);
            }
            ViewFinder finder = new ViewFinder(convertView);
            hodler = new ViewHodler(finder);
            convertView.setTag(hodler);
            
            
        } else {
        	hodler = (ViewHodler) convertView.getTag();
        }
        
        hodler.loanApplyDateTv.setText(mEloanList.get(position).getLoanDate());
        hodler.loanApplyAmountTv.setText(mContext.getString(R.string.boc_eloan_draw_pagename) + MoneyUtils.transMoneyFormat(mEloanList.get(position).getLoanCycleAdvVal(), "001"));

        if (type == ELOAN_OVERDE) {
            hodler.loanCycleLifeTermTv.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
            hodler.loanCycleLifeTermTv.setText(mContext.getString(R.string.boc_overduehit));

        } else  if (type == ELOAN_RECORD){
            hodler.thisIssueRepayDateTv.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_common_gray));
            hodler.thisIssueRepayDateTv.setText(mContext.getString(R.string.boc_details_date)+ mEloanList.get(position).getThisIssueRepayDate());
            hodler.darwDateilTv.setText(mContext.getString(R.string.boc_repay));
            hodler.thisIssueRepayAmountTv.setText( MoneyUtils.transMoneyFormat(mEloanList.get(position)
                    .getThisIssueRepayAmount(),"001"));
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
    		 loanApplyDateTv = finder.find(R.id.loanApplyDateTv);
             loanApplyAmountTv = finder.find(R.id.loanApplyAmountTv);
             thisIssueRepayDateTv = finder.find(R.id.thisIssueRepayDateTv);
             darwDateilTv = finder.find(R.id.darwDateilTv);
             thisIssueRepayAmountTv = finder.find(R.id.thisIssueRepayAmountTv);
            loanCycleLifeTermTv = finder.find(R.id.loanCycleLifeTermTv);
    	}
    	
    }
}
