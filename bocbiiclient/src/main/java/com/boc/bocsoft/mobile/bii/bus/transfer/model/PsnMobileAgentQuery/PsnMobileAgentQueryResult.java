package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileAgentQuery;

import java.util.List;

/**
 * Created by liuweidong on 2016/7/26.
 */
public class PsnMobileAgentQueryResult {

    private String recordNumber;

    private List<ListBean> list;

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String agentName;
        private String agentAddress;

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getAgentAddress() {
            return agentAddress;
        }

        public void setAgentAddress(String agentAddress) {
            this.agentAddress = agentAddress;
        }
    }
}
