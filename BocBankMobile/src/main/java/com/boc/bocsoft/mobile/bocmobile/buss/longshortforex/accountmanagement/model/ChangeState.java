package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

/**
 * 变更状态：变更保证金签约账户交易
 * 或者变更交易账户
 * Created by zhx on 2016/12/22
 */
public final class ChangeState {
    /**
     * 正在进行中
     */
    public static final int DOING = 1;

    /**
     * 变更成功
     */
    public static final int SUCCESS = 2;

    /**
     * 变更失败
     */
    public static final int FAIL = 3;
}
