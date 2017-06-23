package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran;

/**
 * Created by taoyongzhen on 2016/11/21.
 * 046 PsnFundQueryTransOntran基金在途交易查询
 */

public class PsnFundQueryTransOntranParams {

    /**当前页数索引：从0开始*/
    private String currentIndex;
    /**当前页数索引：从0开始*/
    private String pageSize;
    /**是否刷新*/
    private String _refresh;
    /**回话id*/
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getRefresh() {
        return _refresh;
    }

    public void setRefresh(String _refresh) {
        this._refresh = _refresh;
    }
}
