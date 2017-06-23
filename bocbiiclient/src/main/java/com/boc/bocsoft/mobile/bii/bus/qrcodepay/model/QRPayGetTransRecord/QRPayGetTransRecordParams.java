package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord;

/**
 * 交易记录查询
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetTransRecordParams {

    //会话id
    private String conversationId;
    //交易类型
    private String type;
    //账户流水号
    private String actSeq;
    //查询起始日期
    private String startDate;
    //查询结束日期
    private String endDate;
    //是否重新查询结果
    private String _refresh;
    //页面大小
    private int pageSize;
    //当前页索引
    private int currentIndex;
    //交易状态  0:全部 1:成功 2:失败 3:银行处理中
    private String tranStatus;



    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
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

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }
}
