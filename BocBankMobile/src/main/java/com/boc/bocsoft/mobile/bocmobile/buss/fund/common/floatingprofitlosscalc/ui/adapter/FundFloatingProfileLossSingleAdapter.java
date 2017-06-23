package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.timeaxis.TimeAxis;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossSingleBean;

import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/3.
 */

public class FundFloatingProfileLossSingleAdapter extends BaseAdapter {


    private List<FundFloatingProfileLossSingleBean> beans;

    private Context mContext;
    public FundFloatingProfileLossSingleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<FundFloatingProfileLossSingleBean> getBeans() {
        return beans;
    }

    public void setBeans(List<FundFloatingProfileLossSingleBean> beans) {
        this.beans = beans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            TimeAxis timeAxis = new TimeAxis(mContext);
            timeAxis.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_50px),0
                    ,mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,135);
            timeAxis.setLayoutParams(params);
            convertView = timeAxis;
        }
        displayItem(position,convertView);
        return convertView;
    }

    private void displayItem(int position,View timeAxis){
        TimeAxis timeAxisDisplay = (TimeAxis)timeAxis;
        if (position == 0 || beans.get(position).isShowUpAxis() == false){
            timeAxisDisplay.setTopAxisColor(R.color.boc_common_cell_color);
        }else{
            timeAxisDisplay.setTopAxisColor(R.color.boc_yellow_end);
        }
        if (position == beans.size()-1 || beans.get(position).isShowDownAxis() == false){
            timeAxisDisplay.setBottomAxisColor(R.color.boc_common_cell_color);
        }else{
            timeAxisDisplay.setBottomAxisColor(R.color.boc_yellow_end);
        }
        if (!StringUtil.isNullOrEmpty(beans.get(position).getDespContent())){
            timeAxisDisplay.setDespView(beans.get(position).getDespContent());
            timeAxisDisplay.setValueTextColor(R.color.boc_text_money_color_red);
        }else{
            timeAxisDisplay.setValueTextColor(R.color.boc_text_color_common_gray);
            timeAxisDisplay.setDespView("");
        }
        timeAxisDisplay.setTitleView(beans.get(position).getTitleContent());
        timeAxisDisplay.setValueView(beans.get(position).getValueContent());
    }

}
