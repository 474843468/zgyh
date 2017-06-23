package com.chinamworld.bocmbci.biz.dept;

import java.util.List;

/**
 * 申请定活期账户回调
 * @see ApplyForAnAccountListResult
 * @author luqp 2016/11/23
 */
public class ApplyForAnAccountListResult {
    private List<AccountBean> rows;

    public List<AccountBean> getArrayList() {
        return rows;
    }

    public static class AccountBean {

        private String ApplyStatus; //申请账户状态 0：开户失败 1：开户成功
        private String LinkStatus; //关联网银状态 0：关联网银失败 1：关联网银成功
        private String AccountNum; //新开户的账号

        public String getApplyStatus() {
            return ApplyStatus;
        }

        public void setApplyStatus(String applyStatus) {
            ApplyStatus = applyStatus;
        }

        public String getLinkStatus() {
            return LinkStatus;
        }

        public void setLinkStatus(String linkStatus) {
            LinkStatus = linkStatus;
        }

        public String getAccountNum() {
            return AccountNum;
        }

        public void setAccountNum(String accountNum) {
            AccountNum = accountNum;
        }
    }
}
