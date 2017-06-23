package com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsDeleteCommonUsedPaymentList;

import java.util.List;

/**
 *  eyding 2016/08/01
 */
public class PsnPlpsDeleteCommonUsedPaymentListParams {

  private String conversationId;

  private List<FlowFileBean> flowFileIdList;
  private String token;

  public void setToken(String token) {
    this.token = token;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public void setFlowFileIdList(List<FlowFileBean> flowFileIdList) {
    this.flowFileIdList = flowFileIdList;
  }

  public static class FlowFileBean{
    /**
     * 	流程文件ID
     */
    private String flowFileId;

    public void setFlowFileId(String flowFileId) {
      this.flowFileId = flowFileId;
    }
  }
}
