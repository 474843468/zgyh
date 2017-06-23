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
 * 个人 - 安全设定
 * Created by eyding on 16/8/3.
 */
public class SecuritySettingFragment extends BussFragment implements View.OnClickListener {

  private LinearLayout llRoot;

  private ArrayList<Item> allMenuList;//菜单
  @Override protected View onCreateView(LayoutInflater mInflater) {
    return mInflater.inflate(R.layout.fragment_security_setting,null);
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
        /*((ImageView) itemView.findViewById(R.id.iv_icon)).setImageResource(
            getResId(item.getIconId()));*/
        tag++;

        if( ModuleCode.MODULE_SETTING_MANAGER_0100.equals(item.getModuleId())){
          //投资理财开管
          String title = item.getTitle();
          if(title!=null){
            String[] split = title.split(",");
            if(split.length>0 && split[0]!=null && split[0].length()>0){
              ((TextView) itemView.findViewById(R.id.tv_name)).setText(split[0]);
            }

          }
        }
      }
    }


  }

  private String[] ALL_MENU = new String[]{
      "settingManager_0301",//退出时间设定
      "settingManager_0302",//预留信息
      "settingManager_0303",//修改登录密码
      ModuleCode.MODULE_SETTING_MANAGER_0100,//开通投资理财开关
      "settingManager_0304",//中银E盾 - 查询信息
      "settingManager_0307",//中银E盾 - 修改密码
      "settingManager_0305",//管理绑定设备
      "settingManager_0306",//安全工具设置
  };
  @Override public void initData() {

  }

  @Override public void setListener() {

  }

  private boolean isNeedClose = false;
  @Override public void onResume() {
    super.onResume();
    if(isNeedClose){
      getActivity().finish();
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

/*  @Override protected void titleLeftIconClick() {
    mActivity.onBackPressed();
  }*/

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
    return "安全设置";
  }


 /* private int getResId(String name){
    return ApplicationContext.getAppContext().getResources().getIdentifier(name,"drawable",ApplicationContext.getAppContext().getPackageName());
  }*/
}
