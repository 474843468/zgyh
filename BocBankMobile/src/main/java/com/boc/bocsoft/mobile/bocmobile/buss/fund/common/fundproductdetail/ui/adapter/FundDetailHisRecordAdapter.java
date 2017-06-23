package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.JzTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.RankTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWanFenTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWeekTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldRateTendencyViewModel;

import java.io.Serializable;

/**
 * 历史净值、历史累计收益等页面listview的适配器
 * Created by liuzc on 2016/12/30.
 */
public class FundDetailHisRecordAdapter extends BaseAdapter{
    private Context mContext = null;
    private Serializable kLineData = null;

    public FundDetailHisRecordAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        if(kLineData == null){
            return 0;
        }

        if((kLineData instanceof JzTendencyViewModel) &&
                (((JzTendencyViewModel) kLineData).getItems() != null)){
            return ((JzTendencyViewModel) kLineData).getItems().size();
        }
        else if((kLineData instanceof YieldRateTendencyViewModel) &&
                ((YieldRateTendencyViewModel) kLineData).getItems() != null){
            return ((YieldRateTendencyViewModel) kLineData).getItems().size();
        }
        else if((kLineData instanceof RankTendencyViewModel) &&
                ((RankTendencyViewModel) kLineData).getItems() != null){
            return ((RankTendencyViewModel) kLineData).getItems().size();
        }
        else if((kLineData instanceof YieldOfWeekTendencyViewModel) &&
                (((YieldOfWeekTendencyViewModel) kLineData).getItems() != null)){
            return ((YieldOfWeekTendencyViewModel) kLineData).getItems().size();
        }
        else if((kLineData instanceof YieldOfWanFenTendencyViewModel) &&
                (((YieldOfWanFenTendencyViewModel) kLineData).getItems() != null)){
            return ((YieldOfWanFenTendencyViewModel) kLineData).getItems().size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if((kLineData instanceof JzTendencyViewModel) &&
                (((JzTendencyViewModel) kLineData).getItems() != null)){
            return ((JzTendencyViewModel) kLineData).getItems().get(position);
        }
        else if((kLineData instanceof YieldRateTendencyViewModel) &&
                ((YieldRateTendencyViewModel) kLineData).getItems() != null){
            return ((YieldRateTendencyViewModel) kLineData).getItems().get(position);
        }
        else if((kLineData instanceof RankTendencyViewModel) &&
                ((RankTendencyViewModel) kLineData).getItems() != null){
            return ((RankTendencyViewModel) kLineData).getItems().get(position);
        }
        else if((kLineData instanceof YieldOfWeekTendencyViewModel) &&
                (((YieldOfWeekTendencyViewModel) kLineData).getItems() != null)){
            return ((YieldOfWeekTendencyViewModel) kLineData).getItems().get(position);
        }
        else if((kLineData instanceof YieldOfWanFenTendencyViewModel) &&
                (((YieldOfWanFenTendencyViewModel) kLineData).getItems() != null)){
            return ((YieldOfWanFenTendencyViewModel) kLineData).getItems().get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_item_fund_detail_hisrecord, null);

            viewHolder.tvValue1 = (TextView)convertView.findViewById(R.id.tvValue1);
            viewHolder.tvValue2 = (TextView)convertView.findViewById(R.id.tvValue2);
            viewHolder.tvValue3 = (TextView)convertView.findViewById(R.id.tvValue3);
            viewHolder.tvValue4 = (TextView)convertView.findViewById(R.id.tvValue4);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.tvValue1.setVisibility(View.VISIBLE);
        viewHolder.tvValue2.setVisibility(View.VISIBLE);
        viewHolder.tvValue3.setVisibility(View.VISIBLE);
        viewHolder.tvValue4.setVisibility(View.VISIBLE);

        if((kLineData instanceof JzTendencyViewModel) &&
                (((JzTendencyViewModel) kLineData).getItems() != null)){
            JzTendencyViewModel.FundList curItem = ((JzTendencyViewModel) kLineData).getItems().get(position);
            viewHolder.tvValue1.setText(curItem.getJzTime());
            viewHolder.tvValue2.setText(curItem.getDwjz());
            viewHolder.tvValue3.setText(curItem.getLjjz());
            viewHolder.tvValue4.setText(curItem.getChangeOfDay());
        }
        else if((kLineData instanceof YieldRateTendencyViewModel) &&
                ((YieldRateTendencyViewModel) kLineData).getItems() != null){
            YieldRateTendencyViewModel.FundList curItem =
                    ((YieldRateTendencyViewModel) kLineData).getItems().get(position);
            viewHolder.tvValue1.setText(curItem.getJzTime());
            viewHolder.tvValue2.setText(curItem.getLjYieldRate());
            viewHolder.tvValue3.setText(curItem.getSzzjYieldRate());
            viewHolder.tvValue4.setText(curItem.getYjbjjzYieldRate());
        }
        else if((kLineData instanceof RankTendencyViewModel) &&
                ((RankTendencyViewModel) kLineData).getItems() != null){
            RankTendencyViewModel.FundList curItem = ((RankTendencyViewModel) kLineData).getItems().get(position);
            viewHolder.tvValue1.setText(curItem.getJzTime());
            viewHolder.tvValue2.setText(String.format("%s/%s", curItem.getRank(), curItem.getSimilarCount()));
            viewHolder.tvValue3.setVisibility(View.GONE);
            viewHolder.tvValue4.setVisibility(View.GONE);
        }
        else if((kLineData instanceof YieldOfWeekTendencyViewModel) &&
                (((YieldOfWeekTendencyViewModel) kLineData).getItems() != null)){
            YieldOfWeekTendencyViewModel.FundList curItem = ((YieldOfWeekTendencyViewModel) kLineData).getItems().get(position);
            viewHolder.tvValue1.setText(curItem.getDate());
            viewHolder.tvValue2.setText(curItem.getYieldOfWeek());
            viewHolder.tvValue3.setVisibility(View.GONE);
            viewHolder.tvValue4.setVisibility(View.GONE);
        }
        else if((kLineData instanceof YieldOfWanFenTendencyViewModel) &&
                (((YieldOfWanFenTendencyViewModel) kLineData).getItems() != null)){
            YieldOfWanFenTendencyViewModel.FundList curItem = ((YieldOfWanFenTendencyViewModel) kLineData).getItems().get(position);
            viewHolder.tvValue1.setText(curItem.getDate());
            viewHolder.tvValue2.setText(curItem.getYieldOfTenThousand());
            viewHolder.tvValue3.setVisibility(View.GONE);
            viewHolder.tvValue4.setVisibility(View.GONE);
        }

        return convertView;
    }


    public void setkLineData(Serializable kLineData) {
        this.kLineData = kLineData;
    }

    class ViewHolder{
        TextView tvValue1;
        TextView tvValue2;
        TextView tvValue3;
        TextView tvValue4;
    }
}
