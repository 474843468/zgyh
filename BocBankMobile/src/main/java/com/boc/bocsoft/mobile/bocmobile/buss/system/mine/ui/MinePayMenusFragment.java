package com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import java.util.ArrayList;

/**
 * 个人 - 我的支付
 * Created by eyding on 16/10/10.
 */
public class MinePayMenusFragment extends BussFragment implements View.OnClickListener {

  private LinearLayout llRoot;

  private ArrayList<Item> allMenuList;//菜单

  private String[] ALL_MENU = new String[]{
      ModuleCode.MODULE_SETTINGMANAGER_0401,//移动支付
      ModuleCode.MODULE_SETTINGMANAGER_0402,//支付交易查询
  };

  @Override protected View onCreateView(LayoutInflater mInflater) {
    return mInflater.inflate(R.layout.fragment_mine_pay,null);
  }

  @Override public void beforeInitView() {

  }

  @Override public void initView() {
    llRoot = mViewFinder.find(R.id.ll_root);

    allMenuList = ApplicationContext.getInstance().getMenu().filterMenuItem(ALL_MENU);

    View itemView;
    Integer tag = 0;

    int count = llRoot.getChildCount();

    for (int index = 0; index < count; index++) {
      itemView = llRoot.getChildAt(index);

      if (itemView instanceof LinearLayout) {
        Item item = allMenuList.get(tag);
        itemView.setTag(tag);
        itemView.setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.tv_name)).setText(item.getTitle());
        itemView.findViewById(R.id.iv_icon).setVisibility(View.GONE);
       /* ((ImageView) itemView.findViewById(R.id.iv_icon)).setImageResource(
            getResId(item.getIconId()));*/
        tag++;
      }
    }
  }

  @Override public void initData() {

  }

  @Override public void setListener() {

  }

  private boolean isNeedClose = false;
  @Override public void onResume() {
    super.onResume();
    if(isNeedClose){
      mActivity.onBackPressed();
      isNeedClose = false;
    }
  }

  @Override public void onClick(View v) {
    Integer index = (Integer) v.getTag();
    if(index == null){
      return;
    }
    //处理 下一个界面返回逻辑 ,为true 返回频道页面 此处暂定返回上一个界面非频道页
    isNeedClose = false;
    ModuleActivityDispatcher.dispatch((BussActivity) getActivity(),allMenuList.get(index).getModuleId());
  }

  @Override protected void titleLeftIconClick() {
    mActivity.onBackPressed();
  }

  @Override protected boolean isHaveTitleBarView() {
    return true;
  }

  @Override protected boolean isDisplayRightIcon() {
    return false;
  }

  @Override protected boolean getTitleBarRed() {
    return false;
  }

  @Override protected String getTitleValue() {
    return "我的支付";
  }


 /* private int getResId(String name){
    return ApplicationContext.getAppContext().getResources().getIdentifier(name,"drawable",ApplicationContext.getAppContext().getPackageName());
  }*/
}
