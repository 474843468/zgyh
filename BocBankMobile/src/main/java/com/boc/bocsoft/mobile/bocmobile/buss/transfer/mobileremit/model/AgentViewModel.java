package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.ListModel;

import java.util.List;

/**
 * 代理点查询
 *
 * Created by liuweidong on 2016/8/13.
 */
public class AgentViewModel {
    private String prvIbkNum;
    /**
     * 每页显示条数
     */
    private int pageSize;
    /**
     * 当前记录索引
     */
    private int currentIndex;
    /**
     * 总记录数
     */
    private int recordNumber;
    private List<ListModel> list;

    public String getPrvIbkNum() {
        return prvIbkNum;
    }

    public void setPrvIbkNum(String prvIbkNum) {
        this.prvIbkNum = prvIbkNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListModel> getList() {
        return list;
    }

    public void setList(List<ListModel> list) {
        this.list = list;
    }
}
