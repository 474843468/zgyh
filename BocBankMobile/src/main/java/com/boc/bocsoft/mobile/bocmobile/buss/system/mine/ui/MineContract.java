package com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.model.MineMenuVo;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import java.util.List;

/**
 * 个人协议
 * Created by eyding on 16/7/29.
 */
public class MineContract {


  public interface View extends BaseView<BasePresenter>{
    void startLoading(String loading);
    void endLoading();

    void updateUINoLogin();
    void updateUILogin();

    /**
     * 退出后回调
     */
    void endLogOut();
  }

  public interface Presenter extends BasePresenter {

    /**
     * 登出操作
     */
    void logOut();

    /**
     * 是否登录
     */
    boolean isLogin();

    /**
     * 更新登录状态 ， 登录状态刷新后需要刷新UI
     */
    void uptateLoginState();

    List<MineMenuVo> getMenus();

  }

}
