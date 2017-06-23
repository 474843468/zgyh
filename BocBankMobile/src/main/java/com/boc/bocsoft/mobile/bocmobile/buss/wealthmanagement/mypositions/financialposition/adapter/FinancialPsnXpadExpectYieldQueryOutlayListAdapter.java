package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SpringPressageView.SpringProgressView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPsnXpadExpectYieldQueryFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.List;

/**
 * 4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay
 * Created by Administrator on 2016/11/23.
 */
public class FinancialPsnXpadExpectYieldQueryOutlayListAdapter  extends BaseAdapter {
    private Context mContext;
    private List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> listBeen;

    public FinancialPsnXpadExpectYieldQueryOutlayListAdapter
            (List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> listBeen, Context mContext) {
        this.listBeen = listBeen;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return listBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if(null==vh){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_expect_yield_progress,null);
            vh.text_days= (TextView) convertView.findViewById(R.id.text_days);
            vh.text_percent= (TextView) convertView.findViewById(R.id.text_percent);
            vh.spring_progress_view = (SpringProgressView)convertView.findViewById(R.id.spring_progress_view);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        PsnXpadExpectYieldQueryOutlayResModel.ListEntity model = listBeen.get(position);
        if(StringUtils.isEmptyOrNull(model.getMaxAmt())||"-1".equalsIgnoreCase(model.getMaxAmt())){
            vh.text_days.setText(MoneyUtils.transMoneyFormat(model.getMinAmt(),"001")+"及以上");
        }else{
            vh.text_days.setText(MoneyUtils.transMoneyFormat(model.getMinAmt(),"001")+" - "+MoneyUtils.transMoneyFormat(model.getMaxAmt(),"001"));
        }
        vh.text_percent.setText(MoneyUtils.transRatePercentTypeFormat(model.getRates()));
//        vh.text_percent.setText(+"%");
        LogUtils.i("rates =------->"+model.getRates());
        vh.spring_progress_view.setAdapterContext(String.valueOf(FinancialPsnXpadExpectYieldQueryFragment.max),model.getRates());
        return convertView;
    }
    private class ViewHolder{
        TextView text_days;
        TextView text_percent;
        SpringProgressView spring_progress_view;
    }
}
