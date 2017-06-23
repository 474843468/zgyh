package com.boc.bocsoft.mobile.framework.widget.imageLoader.itf;

import com.boc.bocsoft.mobile.framework.widget.imageLoader.ImageRequestHolder;

/**
 * Created by XieDu on 2016/5/31.
 */
public interface RequestProcessor extends RequestManager {
    public void processRequest(ImageRequestHolder requestHolder);
}
