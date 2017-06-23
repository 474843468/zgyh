package com.boc.bocsoft.mobile.bocmobile.buss.system.mine.presenter;

import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutResult;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.model.MineMenuVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui.MineContract;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人Presenter
 * Created by eyding on 16/7/29.
 */
public class MinePresenter implements MineContract.Presenter {

  private MineContract.View mineView;
  private RxLifecycleManager lifecycleManager;


  private final static String[] ALL_MENU = new String[] {

      //"settingManager_0100",//开通投资理财服务
      "settingManager_0700",//我的资产
      "settingManager_0200",//限额设置
      "settingManager_0300",//安全设置
      "settingManager_0400",//我的支付
      "settingManager_0500",//服务记录
      "settingManager_0600",//更多中行应用
      ModuleCode.MODULE_SETTING_MANAGER_0800//在线开户
  };


  public MinePresenter(MineContract.View view){
    this.mineView = view;
    lifecycleManager = new RxLifecycleManager();
  }

  @Override public void logOut() {
    mineView.startLoading("");

    PsnLoginService loginService = new PsnLoginService();
    loginService.logout(new LogoutParams()).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<LogoutResult>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            mineView.endLoading();
            actionAfterLogout();
            LogUtils.d("dding","-----退出失败:"+throwable.getMessage());
          }

          @Override public void onNext(LogoutResult logoutResult) {
            LogUtils.d("dding","-----退出成功");
            mineView.endLoading();
           actionAfterLogout();
          }
        });
  }

  private void actionAfterLogout(){
    mineView.endLogOut();
    ApplicationContext.getInstance().logout();
    uptateLoginState();
  }

  @Override public boolean isLogin() {
    User user = ApplicationContext.getInstance().getUser();
    return user!=null && user.isLogin();
  }

  @Override public void uptateLoginState() {
    if(isLogin()){
      mineView.updateUILogin();
    }else{
      mineView.updateUINoLogin();
    }
  }

  public List<MineMenuVo> getMenus(){

    ArrayList<Item> allMenuList =
        ApplicationContext.getInstance().getMenu().filterMenuItem(ALL_MENU);
    List<MineMenuVo> list = new ArrayList<>();

    for(Item item:allMenuList){
      MineMenuVo mineMenuVo = new MineMenuVo();

      String[] split = item.getTitle().split(",");
      if(split == null){
        split=new String[]{"",""};
      }
      mineMenuVo.icon(getResId(item.getIconId()))
          .target(item.getModuleId())
          .titleNoLogin(split[0])
          .titleLogin(split[1])
          .needLogin("1".equals(item.getLogin()));

      list.add(mineMenuVo);
    }
    return list;
  }

  @Override public void subscribe() {


  }

  @Override public void unsubscribe() {

    if(lifecycleManager != null){
      lifecycleManager.onDestroy();
    }
  }

  private int getResId(String name){
    return ApplicationContext.getAppContext().getResources().getIdentifier(name,"drawable",ApplicationContext.getAppContext().getPackageName());
  }
}
