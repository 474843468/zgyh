package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.InvestTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetDetailVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view.PieChartView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view.PieInfoView;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.util.List;

/**
 * 资产汇总
 * Created by eyding on 16/8/11.
 */
public class InvestSummarizeFragment extends BussFragment {


  private PieChartView viewPieChart;//饼图
  private TextView tvTotal;//总资产
  private PieInfoView viewPieInfo;

  @Override protected View onCreateView(LayoutInflater mInflater) {
    return mInflater.inflate(R.layout.boc_fragment_invest_summarize,null);
  }

  @Override public void beforeInitView() {

  }

  @Override public void initView() {
    viewPieChart =   mViewFinder.find(R.id.view_pie);
    tvTotal = mViewFinder.find(R.id.tv_total);
    viewPieInfo = mViewFinder.find(R.id.view_pieinfo);

    ViewGroup.LayoutParams layoutParams = viewPieChart.getLayoutParams();
    if(layoutParams == null){
      layoutParams = new LinearLayout.LayoutParams(-1,-2);
    }
    layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels * 0.75f);
  }

  @Override public void initData() {

    Bundle arguments = getArguments();
     List<AssetDetailVo>  detailVos=  arguments.getParcelableArrayList("data");
    String total = arguments.getString("total", "0.00");
    setTotalInfo(total);
    //TODO 资产汇总 信息处理

    //资产vo 转 pievo
    InvestTools tools = new InvestTools();

    //demo
    //detailVos = demoIt();

    viewPieChart.setData(tools.buildPieItems(detailVos));
    viewPieChart.setSelect(-1);

    viewPieInfo.setData(tools.buildInfoItems(detailVos));
  }





  @Override public void onDestroy() {
    super.onDestroy();
  }


  private void setTotalInfo(String money){
    tvTotal.setText("投资总资产:"+money+"元");
  }

  @Override public void setListener() {

    viewPieInfo.setListener(new PieInfoView.PieInfoListener() {
      @Override public void onItemClick(int pos, PieInfoView.InfoData infoData) {
        LogUtils.d("dding","---->>target:"+infoData.target);
        if(StringUtils.isEmptyOrNull(infoData.target))return;
        //TODO
        ModuleActivityDispatcher.dispatch(mActivity, infoData.target);
      }
    });
  }

  @Override protected String getTitleValue() {
    return "投资总资产";
  }

  @Override protected boolean getTitleBarRed() {
    return false;
  }

  @Override protected boolean isDisplayRightIcon() {
    return false;
  }

//  @Override protected void titleLeftIconClick() {
//   pop();
//  }


  private  List<AssetDetailVo>  demoIt( ){
    AssetVo assetVo = new AssetVo();
    if(assetVo == null) return null;
    assetVo.setXpadAmt("400");
    assetVo.setTpccAmt("5000");
    assetVo.setMetalAmt("600");
    assetVo.setJxjAmt("700");
    assetVo.setActGoldAmt("1000");
    assetVo.setFundAmt("800");
    assetVo.setIbasAmt("900");
    assetVo.setForexAmt("1200");
    assetVo.setBondAmt("1500");
    assetVo.setAutd("300");
    List<AssetDetailVo> detailVos = new InvestTools().buildDetailVo(assetVo);
    setTotalInfo(InvestTools.getTotalMoneyFormat(InvestTools.getTotalMoney(detailVos)));
   return detailVos;
  }
}
