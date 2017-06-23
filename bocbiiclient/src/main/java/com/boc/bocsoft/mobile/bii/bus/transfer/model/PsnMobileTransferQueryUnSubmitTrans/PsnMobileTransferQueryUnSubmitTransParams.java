package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans;

/**
 * 手机号转账在途交易查询请求
 *
 * Created by liuweidong on 2016/6/22.
 */
public class PsnMobileTransferQueryUnSubmitTransParams {
    /**
     * 服务码:PB035
     */
    private String serviceId;
    /**
     * 开始日期 格式：yyyy/mm/dd
     */
    private String beiginDate;
    /**
     * 结束日期 格式：yyyy/mm/dd
     */
    private String endDate;
    /**
     * 状态 全部-送空
     01-	提交成功
     05-	已过期
     A-交易成功
     03B-交易失败，代表查询03和B状态的记录
     */
    private String status;
    /**
     * 当前页起始索引
     */
    private int currentIndex;
    /**
     * 页面显示记录条数
     */
    private int pageSize;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBeiginDate() {
        return beiginDate;
    }

    public void setBeiginDate(String beiginDate) {
        this.beiginDate = beiginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
}
