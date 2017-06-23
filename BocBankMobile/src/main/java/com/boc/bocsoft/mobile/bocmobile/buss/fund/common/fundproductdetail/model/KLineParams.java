package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 折线图、趋势图的请求参数
 * Created by lzc4524 on 2016/11/30.
 */
public class KLineParams {
    //周期
    public enum CycleFlag{
        cycle_OneWeek, //七日
        cycle_OneMonth,   //近一月
        cycle_TwoMonth, //近两月
        cycle_ThreeMonth, //近三月
        cycle_SixMonth, //近六月
        cycle_OneYear //近一年
    }

    //图表样式
    public enum LineType{
        jzTendency, //净值走势
        yieldRateTendency, //累计收益率
        rankTendency, //排名变化
        yieldOfWeekTendency, //七日年化收益率
        yieldOfWanfenTendency //万份收益率
    }

    private CycleFlag cycleFlag = CycleFlag.cycle_OneMonth; //当前图表的周期
    private LineType lineType = LineType.jzTendency; //当前图表的样式

    /**
     * 获取周期code
     * @return
     */
    public String getCycleFlagParamCode() {
        if (cycleFlag == CycleFlag.cycle_OneWeek) {
            return "s";
        }else if (cycleFlag == CycleFlag.cycle_OneMonth) {
            return "m";
        }
        else if (cycleFlag == CycleFlag.cycle_TwoMonth) {
            return "t";
        }
        else if (cycleFlag == CycleFlag.cycle_ThreeMonth) {
            return "q";
        }
        else if (cycleFlag == CycleFlag.cycle_SixMonth) {
            return "h";
        }
        else if (cycleFlag == CycleFlag.cycle_OneYear) {
            return "y";
        }
        return "m";
    }

    /**
     * 获取周期描述
     * @return
     */
    public String getCycleFlagParamDesc(Context context) {
        if(context == null){
            return "";
        }
        if (cycleFlag == CycleFlag.cycle_OneWeek) {
            return context.getResources().getString(R.string.boc_fund_week);
        }else if (cycleFlag == CycleFlag.cycle_OneMonth) {
            return context.getResources().getString(R.string.boc_fund_one_month);
        }
        else if (cycleFlag == CycleFlag.cycle_TwoMonth) {
            return context.getResources().getString(R.string.boc_fund_two_month);
        }
        else if (cycleFlag == CycleFlag.cycle_ThreeMonth) {
            return context.getResources().getString(R.string.boc_fund_three_month);
        }
        else if (cycleFlag == CycleFlag.cycle_SixMonth) {
            return  context.getResources().getString(R.string.boc_fund_six_month);
        }
        else if (cycleFlag == CycleFlag.cycle_OneYear) {
            return  context.getResources().getString(R.string.boc_fund_one_year);
        }
        return context.getResources().getString(R.string.boc_fund_one_month);
    }

    /**
     * 获取不同类型图表的记录标题
     * @return
     */
    public String getLineTypeRecordTitle(Context context){
        if(context == null){
            return "";
        }
        if(lineType == LineType.jzTendency){
            return context.getResources().getString(R.string.boc_fund_historyjz);
        } else if(lineType == LineType.yieldRateTendency){
            return context.getResources().getString(R.string.boc_fund_yield_shouyilv);
        } else if(lineType == LineType.rankTendency){
            return context.getResources().getString(R.string.boc_fund_rank_tendency);
        } else if(lineType == LineType.yieldOfWeekTendency){
            return context.getResources().getString(R.string.boc_fund_history_yield_of_week);
        } else if(lineType == LineType.yieldOfWanfenTendency){
            return context.getResources().getString(R.string.boc_fund_history_yield_of_wanfen);
        }

        return "";
    }

    public CycleFlag getCycleFlag() {
        return cycleFlag;
    }

    public void setCycleFlag(CycleFlag cycleFlag) {
        this.cycleFlag = cycleFlag;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

}
