package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery;

/**
 * Created by huixiaobo on 2016/6/20.
 * 逾期还款信息查询上传参数
 */
public class PsnEOverdueQueryParams {

    //创建交易会话后的id
    private String conversationId;
    /**贷款账号*/
    private String actNum;
    /**每页显示条数*/
    private String pageSize;
    /**当前页*/
    private String currentIndex;
    /**刷新标志*/
    private String  _refresh;

    public String getConversationId() {
        return conversationId;
    }

    public String getActNum() {
        return actNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }
}
