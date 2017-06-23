package com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype;

import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;

import java.util.List;

/**
 * 依据分类进行筛选的数据
 *
 * Created by liuweidong on 2016/5/25.
 */
public class SelectTypeData {
    /**
     * 分类的标题
     */
    String title;
    /**
     * 默认Id
     */
    String defaultId;
    /**
     * 分类包含的内容集合
     */
    List<Content> list;

    public List<Content> getList() {
        return list;
    }

    public void setList(List<Content> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(String defaultId) {
        this.defaultId = defaultId;
    }
}
