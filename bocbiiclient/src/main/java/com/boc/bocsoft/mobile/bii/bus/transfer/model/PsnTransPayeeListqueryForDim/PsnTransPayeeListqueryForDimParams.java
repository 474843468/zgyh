package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim;

/**
 * 查询收款人列表数据请求
 * Created by liuyang on 2016/6/23.
 */
public class PsnTransPayeeListqueryForDimParams {

    /**
     * 收款账户标志输入为空时（[]或[“”]）查全部
     0：国内跨行
     1：中行内（包含公司收款账户）
     2：信用卡(对私)
     3：二代支付
     4：跨境汇款收款人


     */
    /**
     * 是否定向收款人
     */
    private String isAppointed;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     *当前页
     */
    private String currentIndex;
    /**
     *每页显示条数
     */
    private String pageSize;
    /**
     *收款账户标志
     */
    private String[] bocFlag;

    public String getIsAppointed() {
        return isAppointed;
    }

    public void setIsAppointed(String isAppointed) {
        this.isAppointed = isAppointed;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
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

    public String[] getBocFlag() {
        return bocFlag;
    }

    public void setBocFlag(String[] bocFlag) {
        this.bocFlag = bocFlag;
    }
}
