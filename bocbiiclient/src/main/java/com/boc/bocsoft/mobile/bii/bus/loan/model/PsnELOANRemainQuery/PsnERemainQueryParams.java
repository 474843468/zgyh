package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery;

/**
 * Created by huixiaobo on 2016/6/20.
 *剩余还款信息查询上传参数
 */
public class PsnERemainQueryParams {

    //创建交易会话后的id
    private String conversationId;
    /**贷款账号*/
    private String actNum;
    /**每页显示条数*/
    private String pageSize;
   /**当前页*/
    private String currentIndex;
    /**刷新标志 true：重新查询结果(在交易改变查询条件时是需要重新查询的,currentIndex需上送0)
     * false:不需要重新查询，使用缓存中的结果*/
    private String _refresh;

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

    @Override
    public String toString() {
        return "PsnERemainQueryParams{" +
                "conversationId='" + conversationId + '\'' +
                ", actNum='" + actNum + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", currentIndex='" + currentIndex + '\'' +
                ", _refresh='" + _refresh + '\'' +
                '}';
    }
}
