package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 其他类型贷款进度查询View层数据模型
 * Created by liuzc on 2016/8/15
 */
public class OtherLoanAppliedQryListViewModel {
    /*
    上送数据
     */

    private String name = null; //姓名/企业名称
    private String appPhone = null; //联系电话
    private String appEmail = null; //Email地址
    private int pageSize;  //每页显示条数
    private int currentIndex; //当前页
    private boolean _refresh; //刷新标志
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

    public boolean is_refresh() {
        return _refresh;
    }

    public void set_refresh(boolean _refresh) {
        this._refresh = _refresh;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /*
    返回数据
     */
    private int recordNumber; //记录总数
    private List<ListBean> list;//历史还款信息列表

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String loanNumber; //贷款编号
        private String productCode; //产品编码
        private String productName; //产品名称
        private String name;	//姓名/企业名称
        private BigDecimal loanAmount;	//贷款金额
        private int loanTerm;	//贷款期限
        private String applyDate;	//申请日期
        private String currency;	//币种
        private String loanStatus;	//贷款状态
        private String refuseReason;	//拒绝原因

        public String getLoanNumber() {
            return loanNumber;
        }

        public void setLoanNumber(String loanNumber) {
            this.loanNumber = loanNumber;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(BigDecimal loanAmount) {
            this.loanAmount = loanAmount;
        }

        public int getLoanTerm() {
            return loanTerm;
        }

        public void setLoanTerm(int loanTerm) {
            this.loanTerm = loanTerm;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getLoanStatus() {
            return loanStatus;
        }

        public void setLoanStatus(String loanStatus) {
            this.loanStatus = loanStatus;
        }

        public String getRefuseReason() {
            return refuseReason;
        }

        public void setRefuseReason(String refuseReason) {
            this.refuseReason = refuseReason;
        }
    }
}
