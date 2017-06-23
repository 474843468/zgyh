package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.JzTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.RankTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWanFenTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWeekTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldRateTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui.adapter.FundDetailHisRecordAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

import java.io.Serializable;

/**
 * 从详情页进入的历史记录页面（历史净值、历史收益率等）
 * Created by liuzc on 2016/12/29.
 */
public class FundDetailHisRecordFragment extends BussFragment {
    protected TextView tvTitle1;
    protected TextView tvTitle2;
    protected TextView tvTitle3;
    protected TextView tvTitle4;
    protected ListView lsvHisRecord;
    private View rootView;

    private Serializable kLineData = null; //图表数据

    private FundDetailHisRecordAdapter adapter = null;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_detail_hisrecord, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle1 = (TextView) rootView.findViewById(R.id.tvTitle1);
        tvTitle2 = (TextView) rootView.findViewById(R.id.tvTitle2);
        tvTitle3 = (TextView) rootView.findViewById(R.id.tvTitle3);
        tvTitle4 = (TextView) rootView.findViewById(R.id.tvTitle4);
        lsvHisRecord = (ListView) rootView.findViewById(R.id.lsvHisRecord);

        adapter = new FundDetailHisRecordAdapter(getContext());
        lsvHisRecord.setAdapter(adapter);
    }

    @Override
    public void initData() {
        super.initData();

        kLineData = (Serializable)getArguments().getSerializable(DataUtils.KEY_FUND_HISTORY_DATA);

        if(kLineData != null){
            updateViews();

            adapter.setkLineData(kLineData);
            adapter.notifyDataSetChanged();
        }
        else{
            //为空情况，一般不会出现
        }
    }

    /**
     * 更新标题
     */
    private void updateViews(){
        String titleName = "--";

        tvTitle1.setText(getString(R.string.boc_fundInvest_endDate));
        if(kLineData instanceof JzTendencyViewModel){
            titleName = getString(R.string.boc_fund_historyjz);
            tvTitle2.setText(String.format("%s(%s)",
                    getString(R.string.boc_fund_field_dwjz),
                    getString(R.string.boc_fund_money_unit)));
            tvTitle3.setText(String.format("%s(%s)",
                    getString(R.string.boc_fund_field_yieldjz),
                    getString(R.string.boc_fund_money_unit)));
            tvTitle4.setText(getString(R.string.boc_fund_field_curpercentdiff));
        }
        else if(kLineData instanceof YieldRateTendencyViewModel){
            titleName = String.format("%s%s", getString(R.string.boc_fund_history),
                    getString(R.string.boc_fund_yield_shouyilv));

            tvTitle2.setText(getString(R.string.boc_fund_bjj));
            tvTitle3.setText(getString(R.string.boc_fund_szzs));
            tvTitle4.setText(getString(R.string.boc_fund_yjjz));
        }
        else if(kLineData instanceof RankTendencyViewModel){
            tvTitle2.setText(getString(R.string.boc_fund_history_cate_rank));
            tvTitle3.setVisibility(View.GONE);
            tvTitle4.setVisibility(View.GONE);
        }
        else if(kLineData instanceof YieldOfWeekTendencyViewModel){
            tvTitle2.setText(getString(R.string.boc_fund_yield_of_week));
            tvTitle3.setVisibility(View.GONE);
            tvTitle4.setVisibility(View.GONE);
        }
        else if(kLineData instanceof YieldOfWanFenTendencyViewModel){
            tvTitle2.setText(getString(R.string.boc_fund_yield_of_wanfen));
            tvTitle3.setVisibility(View.GONE);
            tvTitle4.setVisibility(View.GONE);
        }

        updateTitleValue(titleName);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_history_data);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

}
