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
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPsnXpadExpectYieldQueryFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.List;

/**
 * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery
 * Created by Administrator on 2016/11/11.
 */
public class FinancialPsnXpadExpectYieldQueryListAdapter  extends BaseAdapter {
    private Context mContext;
    private List<PsnXpadExpectYieldQueryResModel.ListEntity> listBeen;

    public FinancialPsnXpadExpectYieldQueryListAdapter(List<PsnXpadExpectYieldQueryResModel.ListEntity> listBeen, Context mContext) {
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
        PsnXpadExpectYieldQueryResModel.ListEntity model = listBeen.get(position);
        if(StringUtils.isEmptyOrNull(model.getMaxAmt())||"-1".equalsIgnoreCase(model.getMaxAmt())){
            vh.text_days.setText(model.getMinAmt()+"及以上");
        }else{
            vh.text_days.setText(model.getMinAmt()+" - "+model.getMaxAmt());
        }
        vh.text_percent.setText(MoneyUtils.transRatePercentTypeFormat(model.getRates()));
//        vh.text_percent.setText(+"%");
        LogUtils.i("rates =------->"+model.getRates());

        /**修改开始*/
//        float max;
//        for(int i = 0;i<listBeen.size();i++){
//            max = Float.valueOf(listBeen.get(0).getRates());
//            if(max <= Float.valueOf(listBeen.get(i).getRates())){
//                max = Float.valueOf(listBeen.get(i).getRates());
//            }
//            vh.spring_progress_view.setAdapterContext(String.valueOf(max),model.getRates());
//        }
        /**修改结束*/
        vh.spring_progress_view.setAdapterContext(String.valueOf(FinancialPsnXpadExpectYieldQueryFragment.max),model.getRates());
        return convertView;
    }
    private class ViewHolder{
        TextView text_days;
        TextView text_percent;
        SpringProgressView spring_progress_view;
    }
}
