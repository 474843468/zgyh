package com.chinamworld.bocmbci.biz.finc.finc_p606.selectview;


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
     * 分类包含的内容集合
     */
    List<Content> list;
    /**
     * 默认Id
     */
    String defaultId;

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
