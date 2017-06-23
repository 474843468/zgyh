package com.chinamworld.llbt.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/28.
 */
public class PullRefreshListModel<T> {

    private int currentIndex;
    private int pageSize;
    private int recordNumber;
    private List<T> list = new ArrayList<>();

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
