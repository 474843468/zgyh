package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanBranchQry;

import java.util.List;

/**
 * Created by XieDu on 2016/7/26.
 */
public class PsnOnLineLoanBranchQryResult {

    /**
     * recordNumber : 54
     * list : [{"deptID":"153","deptName":"中国银行北京王府井支行","deptAddr":"北京市东城区东方广场W2座103号","deptPhone":"010-58671234"},{"deptID":"126","deptName":"中国银行天通苑支行","deptAddr":"北京市昌平区东小口本五区19号","deptPhone":"010-58671453"}]
     */

    private String recordNumber;
    /**
     * deptID : 153
     * deptName : 中国银行北京王府井支行
     * deptAddr : 北京市东城区东方广场W2座103号
     * deptPhone : 010-58671234
     */

    private List<ListEntity> list;

    public String getRecordNumber() { return recordNumber;}

    public void setRecordNumber(String recordNumber) { this.recordNumber = recordNumber;}

    public List<ListEntity> getList() { return list;}

    public void setList(List<ListEntity> list) { this.list = list;}

    public static class ListEntity {
        /**
         * 网点编号
         */
        private String deptID;
        /**
         * 网点名称
         */
        private String deptName;
        /**
         * 网点地址
         */
        private String deptAddr;
        /**
         * 网点电话
         */
        private String deptPhone;

        public String getDeptID() { return deptID;}

        public void setDeptID(String deptID) { this.deptID = deptID;}

        public String getDeptName() { return deptName;}

        public void setDeptName(String deptName) { this.deptName = deptName;}

        public String getDeptAddr() { return deptAddr;}

        public void setDeptAddr(String deptAddr) { this.deptAddr = deptAddr;}

        public String getDeptPhone() { return deptPhone;}

        public void setDeptPhone(String deptPhone) { this.deptPhone = deptPhone;}
    }
}
