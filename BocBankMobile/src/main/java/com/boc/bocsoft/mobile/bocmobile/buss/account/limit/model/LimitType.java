package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * @author wangyang
 *         2016/10/18 11:23
 *         限额设置类
 */
public enum LimitType {

    SMALL(R.string.boc_account_limit_small_title, R.string.boc_account_limit_small_limit),
    PASSWORD(R.string.boc_account_limit_password_title, R.string.boc_account_limit_password_limit),
    BORDER(R.string.boc_account_limit_border_title, R.string.boc_account_limit_border_limit),
    ACROSS(R.string.boc_account_across_bank_open, R.string.boc_account_across_bank_quota);

    /**
     * 跨行订购
     */
    public static final String LIMIT_ACROSS_BANK = "3";
    /**
     * 小额凭签名免密
     */
    public static final String LIMIT_PASSWORD = "4";
    /**
     * 境外磁条交易
     */
    public static final String LIMIT_BORDER = "5";

    private int titleId;

    private int limitTitleId;

    LimitType(int titleId, int limitTitleId) {
        this.titleId = titleId;
        this.limitTitleId = limitTitleId;
    }

    public String getTitle(Context context) {
        return context.getString(titleId);
    }

    public String getLimitTitle(Context context) {
        return context.getString(limitTitleId);
    }

    public String getServiceType() {
        switch (this) {
            case PASSWORD:
            case SMALL:
                return LIMIT_PASSWORD;
            case BORDER:
                return LIMIT_BORDER;
            case ACROSS:
                return LIMIT_ACROSS_BANK;
        }
        return null;
    }
}
