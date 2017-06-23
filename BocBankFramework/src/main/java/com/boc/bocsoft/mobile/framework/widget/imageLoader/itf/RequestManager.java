package com.boc.bocsoft.mobile.framework.widget.imageLoader.itf;

import android.widget.ImageView;

/**
 * Created by XieDu on 2016/5/30.
 */
public interface RequestManager {

    /**
     * 取消请求
     *
     * @param view imageview
     */
    public void cancelRequest(ImageView view);


    /**
     * 暂停所有请求
     */
    public void pauseRequests();

    /**
     * 根据标签继续请求
     */
    public void resumeRequests();

}
