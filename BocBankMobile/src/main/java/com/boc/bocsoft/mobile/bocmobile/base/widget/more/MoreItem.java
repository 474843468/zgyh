package com.boc.bocsoft.mobile.bocmobile.base.widget.more;

import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;

/**
 * @author wangyang
 *         2016/12/10 10:11
 */
public class MoreItem extends Item{

    private boolean isMarginTop;

    public MoreItem(String title, String moduleId) {
        super(title, moduleId);
    }

    public MoreItem(String title, String moduleId, boolean isMarginTop) {
        super(title, moduleId);
        this.isMarginTop = isMarginTop;
    }

    public boolean isMarginTop() {
        return isMarginTop;
    }

    public void setMarginTop(boolean marginTop) {
        isMarginTop = marginTop;
    }
}
