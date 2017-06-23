package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model;

/**
 * 作者：XieDu
 * 创建时间：2016/8/19 21:27
 * 描述：
 */
public interface IPledgeParamsData {

    /**
     * @return 浮动比
     */
    String getFloatingRate();

    /**
     * @return 浮动值
     */
    String getFloatingValue();


    /**
     * @return 会话Id
     */
    String getConversationId();

    /**
     * @return 贷款期限上限
     */
    String getLoanPeriodMax();

    /**
     * @return 贷款期限下限
     */
    String getLoanPeriodMin();

    /**
     * @return 单笔限额上限
     */
    String getSingleQuotaMax();

    /**
     * @return 单笔限额下限
     */
    String getSingleQuotaMin();
}
