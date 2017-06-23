package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.finc_p606.util.FincUtil;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class FundLookHistoryValueActivity extends FincBaseActivity implements ICommonAdapter<Map<String, Object>>{
    private String historyActivitTtypeFlag = "1";//页面类型 1：历史净值 2：历史累计收益率 3：历史排名 4：七日年化收益 5：万份收益
    private ListView listview;
    private CommonAdapter<Map<String, Object>> adapter;
    public TextView finc_history_value_date,finc_history_value_unit,finc_history_value_total,finc_history_value_currPercentDiff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finc_look_history_value);
        historyActivitTtypeFlag=getIntent().getStringExtra("historyActivitTtypeFlag");
        setTitle(FincDataCenter.fincHistoryTypeStr.get(historyActivitTtypeFlag));
        listview = (ListView) findViewById(R.id.finc_history_value_listvew);
        finc_history_value_date = (TextView) findViewById(R.id.finc_history_value_date);
        finc_history_value_unit = (TextView) findViewById(R.id.finc_history_value_unit);
        finc_history_value_total = (TextView) findViewById(R.id.finc_history_value_total);
        finc_history_value_currPercentDiff = (TextView)findViewById(R.id.finc_history_value_currPercentDiff);
        //根据类型改变列表头显示内容
        switch (historyActivitTtypeFlag){
            case "2"://累计收益率
                finc_history_value_unit.setText("累计收益率(%)");
                finc_history_value_total.setText("业绩比较基准收益率(%)");
                String fhtype = StringUtil.valueOf1((String) fincControl.fundDetails.get(Finc.FINC_FNTYPE));
                finc_history_value_currPercentDiff.setText("01".equals(fhtype)?"区间存款利率(%)":"上证综指收益率(%)");
                break;
            case "3"://排名变化
                finc_history_value_unit.setText("排名");
                finc_history_value_total.setVisibility(View.GONE);
                finc_history_value_currPercentDiff.setVisibility(View.GONE);
                break;
            case "4"://七日年化收益率
                finc_history_value_unit.setText("七日年化收益率(%)");
                finc_history_value_total.setVisibility(View.GONE);
                finc_history_value_currPercentDiff.setVisibility(View.GONE);
                break;
            case "5"://万份收益
                finc_history_value_unit.setText("万份收益(元)");
                finc_history_value_total.setVisibility(View.GONE);
                finc_history_value_currPercentDiff.setVisibility(View.GONE);
                break;
        }
        adapter = new CommonAdapter<Map<String, Object>>(FundLookHistoryValueActivity.this,fincControl.historyList,this);
        listview.setAdapter(adapter);
    }

    @Override
    public View getView(int i, Map<String, Object> currentItem, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.finc_look_history_value_item, null);
            viewHolder.finc_history_value_date = (TextView) convertView.findViewById(R.id.finc_history_value_date);
            viewHolder.finc_history_value_unit = (TextView) convertView.findViewById(R.id.finc_history_value_unit);
            viewHolder.finc_history_value_total = (TextView) convertView.findViewById(R.id.finc_history_value_total);
            viewHolder.finc_history_value_currPercentDiff = (TextView) convertView.findViewById(R.id.finc_history_value_currPercentDiff);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (historyActivitTtypeFlag){
            case "1":
                viewHolder.finc_history_value_date.setText(DateUtils.DateFormatter((String)currentItem.get("jzTime")));
                viewHolder.finc_history_value_unit.setText(FincUtil.setvalue((String)currentItem.get("dwjz")));
                viewHolder.finc_history_value_total.setText(FincUtil.setvalue((String)currentItem.get("ljjz")));
                viewHolder.finc_history_value_currPercentDiff.setTextColor(getResources().getColor(R.color.fonts_black));
                String currPercentDiff = (String)currentItem.get("currPercentDiff");
                if(!StringUtil.isNullOrEmpty(currPercentDiff)&&Double.parseDouble(currPercentDiff)!= 0){
                    viewHolder.finc_history_value_currPercentDiff.setTextColor(Double.parseDouble(currPercentDiff)>0?getResources().getColor(R.color.boc_text_color_red)
                            :getResources().getColor(R.color.boc_text_color_green));
                }
                viewHolder.finc_history_value_currPercentDiff.setText(FincUtil.setCent((String)currentItem.get("currPercentDiff")));
                break;
            case "2":
                viewHolder.finc_history_value_date.setText(DateUtils.DateFormatter((String)currentItem.get("jzTime")));
                viewHolder.finc_history_value_unit.setText(FincUtil.setCent2((String)currentItem.get("ljYieldRate")));
                viewHolder.finc_history_value_total.setText(FincUtil.setCent2((String)currentItem.get("yjbjjzYieldRate")));
                String fhtype = StringUtil.valueOf1((String) fincControl.fundDetails.get(Finc.FINC_FNTYPE));
                viewHolder.finc_history_value_currPercentDiff.setText("01".equals(fhtype)?FincUtil.setCent2((String)currentItem.get("sectionDepositRate")):
                        FincUtil.setCent2((String)currentItem.get("szzjYieldRate")));
                break;
            case "3":
                viewHolder.finc_history_value_date.setText(DateUtils.DateFormatter((String)currentItem.get("jzTime")));
                String rankScore = (String)currentItem.get("rankScore");
                if(!StringUtil.isNullOrEmpty(rankScore)){
                    rankScore = StringUtil.append2Decimals(Double.parseDouble(rankScore)*100+"",2) ;
                }
                String rank = (String)currentItem.get("rank");
                String yieldSection = (String)currentItem.get("yieldSection");
                viewHolder.finc_history_value_unit.setText(rank+"/"+yieldSection);
                viewHolder.finc_history_value_total.setVisibility(View.GONE);
                viewHolder.finc_history_value_currPercentDiff.setVisibility(View.GONE);
                break;
            case "4":
                viewHolder.finc_history_value_date.setText(DateUtils.DateFormatter((String)currentItem.get("date")));
                viewHolder.finc_history_value_unit.setText(FincUtil.setvalue((String)currentItem.get("yieldOfWeek")));
                viewHolder.finc_history_value_total.setVisibility(View.GONE);
                viewHolder.finc_history_value_currPercentDiff.setVisibility(View.GONE);
                break;
            case "5":
                viewHolder.finc_history_value_date.setText(DateUtils.DateFormatter((String)currentItem.get("date")));
                viewHolder.finc_history_value_unit.setText(FincUtil.setvalue((String)currentItem.get("yieldOfTenThousand")));
                viewHolder.finc_history_value_total.setVisibility(View.GONE);
                viewHolder.finc_history_value_currPercentDiff.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }

    private class ViewHolder {

        public TextView finc_history_value_date,finc_history_value_unit,finc_history_value_total,finc_history_value_currPercentDiff;
    }
}
