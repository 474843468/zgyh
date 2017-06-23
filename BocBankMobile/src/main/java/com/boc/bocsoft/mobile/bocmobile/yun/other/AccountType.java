package com.boc.bocsoft.mobile.bocmobile.yun.other;

/**
 * Created by dingeryue on 2016年10月25.
 * 云备份 - 使用的最近账号类型
 */

public interface AccountType {
    //我要转账、二维码转账、手机号转账、无卡取款、主动收款和手机取款
    /**
     * 我要转账
     */
    String ACC_TYPE_TRANSREMIT = "tran01";
    /**
     * 二维码转账
     */
    String ACC_TYPE_QRCODETRANS = "tran03";

    /**
     * 手机号转账
     */
    String ACC_TYPE_PHONETRANS = "tran02";

    /**
     * 无卡取款
     */
    String ACC_TYPE_NOCARD_DRAW = "tran05";

    /**
     * 主动收款
     */
    String ACC_TYPE_PAYEE = "tran04";

    /**
     * 手机取款
     */
    String ACC_TYPE_PHONE_DRAW = "tran06";

    /**
     * 信用卡还款 本币
     */
    String ACC_TYPE_CRCD_LOCAL_REPAY = "crdt01";

    /**
     * 信用卡还款 外币
     */
    String ACC_TYPE_CRCD_FOREIGN_REPAY = "crdt02";
    /**
     * 结构汇-购汇
     */
    String ACC_TYPE_FESS_BUY_EXCHANGE = "fess01";
    /**
     * 结构汇-结汇
     */
    String ACC_TYPE_FESS_SELL_EXCHANGE = "fess02";
}
