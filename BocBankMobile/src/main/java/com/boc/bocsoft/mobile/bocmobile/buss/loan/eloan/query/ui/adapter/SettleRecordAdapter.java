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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/25.
 * 已结清记录
 */
public class SettleRecordAdapter extends BaseAdapter {
    /**ccontext*/
    private Context context;
    /**已结清列表*/
    private List<EloanAccountListModel.PsnLOANListEQueryBean>  mEloanList;

    public SettleRecordAdapter(Context context) {
        this.context = context;
        mEloanList = new ArrayList<EloanAccountListModel.PsnLOANListEQueryBean>();
    }

    /**
     *显示已结清数据列表
     * @param eloanList 结清数据
     */
    public void setSettleRcdData( List<EloanAccountListModel.PsnLOANListEQueryBean> eloanList) {
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	ViewHodler hodler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.boc_settlerecord_item, parent,false);
             ViewFinder finder = new ViewFinder(convertView);
             hodler = new ViewHodler(finder);
             
             convertView.setTag(hodler);
            
        } else {
        	hodler = (ViewHodler) convertView.getTag();
        }
        
        hodler.loanApplyDateTv.setText(mEloanList.get(position).getLoanDate());
        hodler.loanApplyAmountTv.setText(context.getString(R.string.boc_eloan_draw_pagename)+
                MoneyUtils.transMoneyFormat(mEloanList.get(position).getLoanCycleAdvVal(), "001") );
        hodler.loanCycleLifeTermTv.setText("共" +mEloanList.get(position).getLoanCycleLifeTerm() + "期");
        
        return convertView;
    }
    
    public class ViewHodler {
    	public TextView loanApplyDateTv;
    	public TextView loanApplyAmountTv;
    	public TextView loanCycleLifeTermTv;
    	
    	public ViewHodler(ViewFinder finder){
    		loanApplyDateTv = finder.find(R.id.loanApplyDateTv);
            loanApplyAmountTv = finder.find( R.id.loanApplyAmountTv);
            loanCycleLifeTermTv = finder.find(R.id.loanCycleLifeTermTv);
    	}
    }
}
