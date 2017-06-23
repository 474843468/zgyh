package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model;

/**
 * Created by huixiaobo on 2016/11/29.
 * 定投管理上送参数对象
 */
public class InvestParamsModel {
    /**当前页*/
    private String currentIndex;
    /**每页显示条数*/
    private String pageSize;
    /**基金代码*/
    private String fundCode;
    /**定投周期*/
    private String dtFlag;
    /**刷新标志*/
    private String _refresh;

    /**会话ID*/
    private String conversationId;

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

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getDtFlag() {
        return dtFlag;
    }

    public void setDtFlag(String dtFlag) {
        this.dtFlag = dtFlag;
    }

    public String is_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "PsnFundStatusDdApplyQueryParams{" +
                "currentIndex='" + currentIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", fundCode='" + fundCode + '\'' +
                ", dtFlag='" + dtFlag + '\'' +
                ", _refresh='" + _refresh + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
