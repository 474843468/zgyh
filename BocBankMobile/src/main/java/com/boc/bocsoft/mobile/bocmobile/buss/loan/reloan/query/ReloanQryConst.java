package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by liuzc on 2016/8/20.
 */
public class ReloanQryConst {
    public final static String KEY_STATUS_QRY_RESULT_BEAN = "key_status_query_result";
    public final static String KEY_ACCOUNT_NUMBER = "key_account_number";
    public final static String KEY_CURRENCY = "key_currency";
    public final static String KEY_RELOAD_USER_RECORD_BEAN = "key_reload_user_record_bean";
    public final static String KEY_MIN_USEMONEY_AMOUNT = "key_min_use_money_amount";

    public final static String SHOW_DETAIL = "show_detail";

    /**逾期状态 00：正常 01：逾期*/
    public final static String OVERDUE_STATUS_OVER = "01";
    /**账户可执行的交易 1“advance”可以执行提前还款 2、none”，不可执行提前还款*/
    public final static String TRANSFLAG_ADVANCE = "advance";

    /**可循环贷款标示 1循环（能在线上循环用款）*/
    public final static String CYCLE_FLAT_NORMAL = "1";
}
