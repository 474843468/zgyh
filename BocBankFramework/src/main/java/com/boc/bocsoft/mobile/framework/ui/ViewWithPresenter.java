package com.boc.bocsoft.mobile.framework.ui;

/**
 * 作者：XieDu
 * 创建时间：2016/9/23 17:21
 * 描述：
 */
public interface ViewWithPresenter<P extends BasePresenter> {
    P getPresenter();

    void startPresenter();

    void stopPresenter();
}
