package com.boc.bocsoft.mobile.bocmobile.base.container;

import android.view.View;

/**
 * 作者：XieDu
 * 创建时间：2016/12/17 15:09
 * 描述：容器接口
 */
public interface IContainer extends ViewLifeCycle {
    /**
     * 得到容器的view
     */
    View getView();

    /**
     * 设置进入可见或不可见状态时的操作。
     * ViewPager会调用此方法，但是别的场景下不会自动调用该方法，需要开发者自己控制。
     */
    void setUserVisibleHint(boolean isVisibleToUser);
}
