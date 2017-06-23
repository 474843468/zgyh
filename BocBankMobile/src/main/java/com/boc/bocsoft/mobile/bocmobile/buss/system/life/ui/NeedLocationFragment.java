package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.ui.BaseFragment;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * Created by dingeryue on 2016年08月24.
 */
public class NeedLocationFragment extends BaseFragment {
  private LinearLayout ll_root;
  @Override protected View onCreateView(LayoutInflater mInflater) {

    ll_root = new LinearLayout(getActivity());
    ll_root.setOrientation(LinearLayout.VERTICAL);
    //ll_root.setBackgroundColor(0x88ffffff);

    return ll_root;
  }

  @Override public void beforeInitView() {

  }

  @Override public void initView() {

    TextView textView = new TextView(getActivity());
    textView.setTextColor(0xffffffff);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-1);
    params.topMargin = ResUtils.dip2px(getActivity(),45);
    ll_root.addView(textView,params);
    ll_root.setGravity(Gravity.CENTER);
    textView.setText("请先选择位置再使用该功能！");
    textView.setGravity(Gravity.CENTER);
    textView.setBackgroundColor(0x44000000);
    textView.setOnClickListener(null);
  }

  @Override public void initData() {

  }

  @Override public void setListener() {

  }

  @Override protected boolean isHaveTitleBarView() {
    return false;
  }

  @Override protected boolean getTitleBarRed() {
    return false;
  }

  @Override protected String getTitleValue() {
    return "提示";
  }

  @Override protected boolean isDisplayLeftIcon() {
    return false;
  }

  @Override protected boolean isDisplayRightIcon() {
    return false;
  }
}
