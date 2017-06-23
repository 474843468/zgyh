package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanAppliedQry;

/**
 * 查询贷款记录列表请求参数
 * Created by liuzc on 2016/8/16.
 */
public class PsnOnLineLoanAppliedQryParams {
    private String name = null; //姓名/企业名称
    private String appPhone = null; //联系电话
    private String appEmail = null; //Email地址
    private String pageSize;  //每页显示条数
    private String currentIndex; //当前页
    private String _refresh; //刷新标志
    private String conversationId; //会话ID

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppPhone() {
        return appPhone;
    }

    public void setAppPhone(String appPhone) {
        this.appPhone = appPhone;
    }

    public String getAppEmail() {
        return appEmail;
    }

    public void setAppEmail(String appEmail) {
        this.appEmail = appEmail;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
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
}
