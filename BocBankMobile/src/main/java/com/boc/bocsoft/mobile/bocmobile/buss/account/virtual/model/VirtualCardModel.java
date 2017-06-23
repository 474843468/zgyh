package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/7/21 14:37
 *         虚拟银行卡Model
 */
@SuppressLint("ParcelCreator")
public class VirtualCardModel extends AccountBean implements Serializable{

    /**
     * 状态--有效
     */
    public static final String STATUS_VALID = "1";
    /**
     * 状态--过期
     */
    public static final String STATUS_INVALID = "0";

    /**
     * 网上银行
     */
    private final String CHANNEL_WSYH = "1";
    /**
     * 手机银行
     */
    private final String CHANNEL_SJYH = "2";
    /**
     * 家居银行
     */
    private final String CHANNEL_JJYH = "4";

    /** 虚拟卡accountId */
    private String virAccountId;
    /**
     * 生效日期
     */
    private LocalDate startDate;
    /**
     * 失效日期
     */
    private LocalDate endDate;
    /**
     * 单笔交易限额
     */
    private BigDecimal signleLimit;
    /**
     * 累计交易限额
     */
    private BigDecimal totalLimit;
    /**
     * 已累计交易限额
     */
    private BigDecimal atotalLimit;
    /**
     * 最大交易限额
     */
    private BigDecimal maxSingleLimit;

    private String lastUpdateUser;

    private String lastUpdates;

    private boolean isUpdateLimitSuccess;

    public String getLastUpdates() {
        return lastUpdates;
    }

    public void setLastUpdates(String lastUpdates) {
        this.lastUpdates = lastUpdates;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public BigDecimal getMaxSingleLimit() {
        return maxSingleLimit;
    }

    public void setMaxSingleLimit(BigDecimal maxSingleLimit) {
        this.maxSingleLimit = maxSingleLimit;
    }

    /**
     * 建卡渠道 4 家居银行,2 手机银行, 1 网上银行
     */
    private String channel;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSignleLimit() {
        return signleLimit;
    }

    public void setSignleLimit(BigDecimal signleLimit) {
        this.signleLimit = signleLimit;
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getAtotalLimit() {
        return atotalLimit;
    }

    public void setAtotalLimit(BigDecimal atotalLimit) {
        this.atotalLimit = atotalLimit;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 获取申请渠道
     *
     * @return
     */
    public String getChannelString() {
        switch (channel) {
            case CHANNEL_WSYH:
                return "网上银行";
            case CHANNEL_SJYH:
                return "手机银行";
            case CHANNEL_JJYH:
                return "家居银行";
        }
        return null;
    }

    /**
     * 获取状态
     * @return
     */
    public String getStatus() {
        if (STATUS_VALID.equals(getAccountStatus()))
            return "有效";
        else
            return "已销户";
    }

    public boolean isApplySuccess() {
        return !StringUtils.isEmptyOrNull(getAccountIbkNum());
    }

    public String getVirAccountId() {
        return virAccountId;
    }

    public void setVirAccountId(String virAccountId) {
        this.virAccountId = virAccountId;
    }

    public boolean isUpdateLimitSuccess() {
        return isUpdateLimitSuccess;
    }

    public void setUpdateLimitSuccess(boolean updateLimitSuccess) {
        isUpdateLimitSuccess = updateLimitSuccess;
    }
}
