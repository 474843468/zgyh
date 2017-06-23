package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadprogressquery.PsnXpadProgressQueryResModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.List;

/**
 * 中信理财 - 持仓详情（日积月累）收益率查询列表详情
 * Created by cff on 2016/9/23.
 */
public class FinancialTypeProgressQueryAdapter extends BaseAdapter{
    private Context mContext;
    //收益率列表数据
    private List<PsnXpadProgressQueryResModel.ListBean> progressList;
    //适配器构造器
    public FinancialTypeProgressQueryAdapter(Context mContext){
        super();
        this.mContext=mContext;
    }

    /**
     * 为适配器设置数据
     */
    public void setProfirDate(List<PsnXpadProgressQueryResModel.ListBean> progressList){
        this.progressList=progressList;
    }

    @Override
    public int getCount() {
        return progressList == null?0:progressList.size();
    }

    @Override
    public Object getItem(int position) {
        return progressList.get(position);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_financial_progress,null);
            vh.progress_hoddays= (TextView) convertView.findViewById(R.id.progress_hoddays);
            vh.progress_yearrr= (TextView) convertView.findViewById(R.id.progress_yearrr);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        PsnXpadProgressQueryResModel.ListBean model=progressList.get(position);
        //持有天数
        if (!StringUtils.isEmptyOrNull(model.getMaxDays())&&!"-1".equalsIgnoreCase(model.getMaxDays())) {
            vh.progress_hoddays.setText(model.getMinDays()+"-"+model.getMaxDays()+"天");
        }else{
            vh.progress_hoddays.setText(model.getMaxDays()+"天及以上");
        }
       //预期年化收益率
        vh.progress_yearrr.setText(model.getYearlyRR());
        return convertView;
    }

    private class ViewHolder{
        TextView progress_hoddays;
        TextView progress_yearrr;
    }

}
