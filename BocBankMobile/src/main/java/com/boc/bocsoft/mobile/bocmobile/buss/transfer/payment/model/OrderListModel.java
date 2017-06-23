package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnPaymentOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnReminderOrderListResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/6/28.
 */
public class OrderListModel {

    //当前付款列表索引位置
    private int currentPayIndex = 0;
    //当前收款列表索引位置
    private int currentReminderIndex = 0;
    //每页数据
    private String pageSize = ApplicationConst.PAGE_SIZE + "";
    //付款开始日期
    private String startPayDate = "2017/08/18";
    //付款结束日期
    private String endPayDate = "2017/08/20";
    //收款开始日期
    private String startReminderDate = "2017/08/18";
    //收款结束日期
    private String endReminderDate = "2017/08/20";
    //付款列表
    private List<ShowListBean> payList;
    //收款列表
    private List<ShowListBean> reminderList;
    //付款指令记录总数
    private int payListRecords;
    //收款指令记录总数
    private int reminderListRecords;

    public OrderListModel() {
        payList = new ArrayList<>();
        reminderList = new ArrayList<>();
    }

    public void convertFromPsnPaymentOrderListResult(PsnPaymentOrderListResult result) {
        int size = result.getPaymentRecordList().size();
        payListRecords = result.getRecordNum();
        currentPayIndex += size;
        for (int i = 0; i < size; i++) {

            LocalDate localDate = result.getPaymentRecordList().get(i).getCreateDate().toLocalDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = result.getPaymentRecordList().get(i - 1).getCreateDate().toLocalDate().format(DateFormatters.monthFormatter1);
            }

            if (tempTime.equals(formatTime)) {
                ShowListBean itemFirst = new ShowListBean();
                itemFirst.type = ShowListBean.CHILD;
                itemFirst.setTitleID(ShowListConst.TITLE_LEFT_2_RIGHT_1);
                itemFirst.setTime(localDate);
                PsnPaymentOrderListResult.PaymentRecordListBean listBean = result.getPaymentRecordList().get(i);
                itemFirst.setContentLeftBelowAgain(listBean.getNotifyId());
                itemFirst.setContentLeftAbove(listBean.getPayeeName());
                if (listBean.getStatus().equals("1")) {
                    itemFirst.setContentLeftBelow("未付");
                } else if (listBean.getStatus().equals("2")) {
                    itemFirst.setContentLeftBelow("已付");
                } else if (listBean.getStatus().equals("3")) {
                    itemFirst.setContentLeftBelow("已撤销");
                } else if (listBean.getStatus().equals("4")) {
                    itemFirst.setContentLeftBelow("状态未明");
                } else if (listBean.getStatus().equals("5")) {
                    itemFirst.setContentLeftBelow("过期未付");
                }
                itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(listBean.getRequestAmount(), "RMB"));
                payList.add(itemFirst);
            } else {
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_LEFT_2_RIGHT_1);
                        itemFirst.setTime(localDate);
                        PsnPaymentOrderListResult.PaymentRecordListBean listBean = result.getPaymentRecordList().get(i);
                        itemFirst.setContentLeftBelowAgain(listBean.getNotifyId());
                        itemFirst.setContentLeftAbove(listBean.getPayeeName());
                        if (listBean.getStatus().equals("1")) {
                            itemFirst.setContentLeftBelow("未付");
                        } else if (listBean.getStatus().equals("2")) {
                            itemFirst.setContentLeftBelow("已付");
                        } else if (listBean.getStatus().equals("3")) {
                            itemFirst.setContentLeftBelow("已撤销");
                        } else if (listBean.getStatus().equals("4")) {
                            itemFirst.setContentLeftBelow("状态未明");
                        } else if (listBean.getStatus().equals("5")) {
                            itemFirst.setContentLeftBelow("过期未付");
                        }
                        itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(listBean.getRequestAmount(), "RMB"));
                    }
                    payList.add(itemFirst);
                }
            }
        }
    }

    public void convertFromPsnReminderOrderListResult(PsnReminderOrderListResult result) {
        int size = result.getActiveReminderList().size();
        reminderListRecords = result.getRecordNum();
        currentReminderIndex += size;
        for (int i = 0; i < size; i++) {

            LocalDate localDate = result.getActiveReminderList().get(i).getCreateDate().toLocalDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = result.getActiveReminderList().get(i - 1).getCreateDate().toLocalDate().format(DateFormatters.monthFormatter1);
            }

            if (tempTime.equals(formatTime)) {
                ShowListBean itemFirst = new ShowListBean();
                itemFirst.type = ShowListBean.CHILD;
                itemFirst.setTitleID(ShowListConst.TITLE_LEFT_2_RIGHT_1);
                itemFirst.setTime(localDate);
                PsnReminderOrderListResult.ActiveReminderListBean listBean = result.getActiveReminderList().get(i);
                itemFirst.setContentLeftBelowAgain(listBean.getNotifyId());
                itemFirst.setContentLeftAbove(listBean.getPayerName());
                if (listBean.getStatus().equals("1")) {
                    itemFirst.setContentLeftBelow("未付");
                } else if (listBean.getStatus().equals("2")) {
                    itemFirst.setContentLeftBelow("已付");
                } else if (listBean.getStatus().equals("3")) {
                    itemFirst.setContentLeftBelow("已撤销");
                } else if (listBean.getStatus().equals("4")) {
                    itemFirst.setContentLeftBelow("状态未明");
                } else if (listBean.getStatus().equals("5")) {
                    itemFirst.setContentLeftBelow("过期未付");
                }
                itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(listBean.getRequestAmount(), "RMB"));
                reminderList.add(itemFirst);
            } else {
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_LEFT_2_RIGHT_1);
                        itemFirst.setTime(localDate);
                        PsnReminderOrderListResult.ActiveReminderListBean listBean = result.getActiveReminderList().get(i);
                        itemFirst.setContentLeftBelowAgain(listBean.getNotifyId());
                        itemFirst.setContentLeftAbove(listBean.getPayerName());
                        if (listBean.getStatus().equals("1")) {
                            itemFirst.setContentLeftBelow("未付");
                        } else if (listBean.getStatus().equals("2")) {
                            itemFirst.setContentLeftBelow("已付");
                        } else if (listBean.getStatus().equals("3")) {
                            itemFirst.setContentLeftBelow("已撤销");
                        } else if (listBean.getStatus().equals("4")) {
                            itemFirst.setContentLeftBelow("状态未明");
                        } else if (listBean.getStatus().equals("5")) {
                            itemFirst.setContentLeftBelow("过期未付");
                        }
                        itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(listBean.getRequestAmount(), "RMB"));
                    }
                    reminderList.add(itemFirst);
                }
            }
        }
    }

    private void clearPayCache() {
        currentPayIndex = 0;
        payListRecords = 0;
        payList.clear();
    }

    private void clearReminderCache() {
        currentReminderIndex = 0;
        reminderListRecords = 0;
        reminderList.clear();
    }

    public void setPayDateRange(String start, String end) {
        setStartPayDate(start);
        setEndPayDate(end);
        clearPayCache();
    }

    public void setReminderDateRange(String start, String end) {
        setStartReminderDate(start);
        setEndReminderDate(end);
        clearReminderCache();
    }

    public int getPayListRecords() {
        return payListRecords;
    }

    public void setPayListRecords(int payListRecords) {
        this.payListRecords = payListRecords;
    }

    public int getReminderListRecords() {
        return reminderListRecords;
    }

    public void setReminderListRecords(int reminderListrecords) {
        this.reminderListRecords = reminderListrecords;
    }

    public int getCurrentPayIndex() {
        return currentPayIndex;
    }

    public void setCurrentPayIndex(int currentPayIndex) {
        this.currentPayIndex = currentPayIndex;
    }

    public int getCurrentReminderIndex() {
        return currentReminderIndex;
    }

    public void setCurrentReminderIndex(int currentReminderIndex) {
        this.currentReminderIndex = currentReminderIndex;
    }

    public List<ShowListBean> getPayList() {
        return payList;
    }

    public void setPayList(List<ShowListBean> payList) {
        this.payList = payList;
    }

    public List<ShowListBean> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<ShowListBean> reminderList) {
        this.reminderList = reminderList;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartPayDate() {
        return startPayDate;
    }

    public void setStartPayDate(String startPayDate) {
        this.startPayDate = startPayDate;
    }

    public String getEndPayDate() {
        return endPayDate;
    }

    public void setEndPayDate(String endPayDate) {
        this.endPayDate = endPayDate;
    }

    public String getStartReminderDate() {
        return startReminderDate;
    }

    public void setStartReminderDate(String startReminderDate) {
        this.startReminderDate = startReminderDate;
    }

    public String getEndReminderDate() {
        return endReminderDate;
    }

    public void setEndReminderDate(String endReminderDate) {
        this.endReminderDate = endReminderDate;
    }
}
