package com.boc.bocsoft.mobile.bocmobile.buss.fund.utils;

import android.content.Context;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by taoyongzhen on 2016/11/22.
 */

public class DataUtils {

    //047(046)基金历史（在途）交易类型码
    public static final String TRANS_TYPE_ALL = "0";//全部：0
    public static final String TRANS_TYPE_PURCHASE = "020,022,120,122,130";// 购买
    public static final String TRANS_TYPE_REDDEM = "024,124,142,198,098";// 赎回
    public static final String TRANS_TYPE_FUNDSCHEDULEDBUY = "039,059,060,061,062,063,064,139";//基金定投
    public static final String TRANS_TYPE_SET_BONUS = "129,029";//设置分红方式
    public static final String TRANS_TYPE_FUND_CONVERT = "036,038,138,037,136,137";// 基金转换
    public static final String TRANS_TYPE_BONUS = "611,612";//基金分红：6
    public static final String TRANS_TYPE_FUND_ACCOUNT_MANAGEMENT = "108,008,002,102,009,109";//基金账户管理


    public static final String KEY_FUND_ID = "fundID"; //基金ID
    public static final String KEY_BINDING_INFO = "binding_info"; //基金绑定的账户信息
    public static final String KEY_FUND_COMPANY = "fund_company"; //基金公司
    public static final String KEY_ACCOUNT_CURRENT_TAB_INDEX = "currentTabIndex"; // 当前账户管理Tab位置
    public static final String KEY_New_ACCOUNT_ID = "newAccountId"; // 变更新的资金帐户
    public static final String KEY_INVESTMENT_OPEN_STATE = "investment_open_state"; //开通投资服务状态
    public static final String KEY_FUND_HISTORY_DATA = "fundHistoryData"; //基金历史数据

    public static final int INVESTMENT_OPEN_STATE_UNCHECK = 0; //开通投资服务状态： 未检查
    public static final int INVESTMENT_OPEN_STATE_NOTOPEN = 1; //开通投资服务状态： 未开通
    public static final int INVESTMENT_OPEN_STATE_OPEN = 2; //开通投资服务状态： 已开通


    public static String getTransTypeDes(Context mcontext, String type) {
        String describetion = "";
        if (StringUtil.isNullOrEmpty(type)) {
            return describetion;
        }
        if (TRANS_TYPE_ALL.equals(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_all);
        } else if (TRANS_TYPE_PURCHASE.contains(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_purchase);
        } else if (TRANS_TYPE_REDDEM.contains(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_reddem);
        } else if (TRANS_TYPE_FUNDSCHEDULEDBUY.contains(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_fundscheduledbuy);
        } else if (TRANS_TYPE_SET_BONUS.contains(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_set_bonus);
        } else if (TRANS_TYPE_FUND_CONVERT.contains(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_fund_convert);
        } else if (TRANS_TYPE_BONUS.contains(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_bonus);
        } else if (TRANS_TYPE_FUND_ACCOUNT_MANAGEMENT.contains(type)) {
            describetion = mcontext.getString(R.string.boc_fund_trans_type_fund_account_management);
        }
        return describetion;

    }

    //基金交易类型为：设置分红方式，对应的描述字段：设置分红方式(现金分红)，即fundTranType（bonusType）的组合
    public static String getSetBonusSDes(Context mContext, String type) {
        String bunudsTypeDes = getBonusTypeDes(mContext, type);
        String result = mContext.getString(R.string.boc_fund_trans_type_set_bonus);
        if (StringUtil.isNullOrEmpty(bunudsTypeDes) == false) {
            result = result + "(" + bunudsTypeDes + ")";
        }
        return result;
    }

    public static String getTransTypeDes(Context mcontext, String transType, String bonusType) {
        String transTypeDes = getTransTypeDes(mcontext, transType);
        if (TRANS_TYPE_SET_BONUS.equals(transType)) {
            transTypeDes = getSetBonusSDes(mcontext, bonusType);
        }
        return transTypeDes;
    }

    public static final String BONUSTYPE_DEFAULT = "0";//默认
    public static final String BONUSTYPE_CASH = "1";//现金分红
    public static final String BONUSTYPE_BONUS = "2";//再投资分红

    public static String getBonusTypeDes(Context mContext, String type) {
        String describetion = "";
        if (StringUtil.isNullOrEmpty(type)) {
            return describetion;
        }
        if (BONUSTYPE_DEFAULT.equals(type)) {
            describetion = mContext.getString(R.string.boc_fund_bonustype_default);
        } else if (BONUSTYPE_CASH.equals(type)) {
            describetion = mContext.getString(R.string.boc_fund_bonustype_cash);
        } else if (BONUSTYPE_BONUS.equals(type)) {
            describetion = mContext.getString(R.string.boc_fund_bonustype_bonus);
        }
        return describetion;
    }



    /**
     * 1:成功: 修改分红方式；基金转换结果成功
     9000:非工作时间  修改分红方式；基金转换结果
     E135:未作风险评估
     E136:风险等级不匹配
     */
    public static String ALTER_FLAG_SUCCESS = "1";
    public static String ALTER_FLAG_NIGHT = "9000";
    public static String NO_RISK_EVALUATION = "E135";
    public static String NO_MATCH_RISK_EVALUATION="E136";




    /**
     * 在途交易查询 购买/定期定额申请查询 定投  :元
     * 在途交易查询 赎回/定期定额申请查询 定赎  ：份
     * 基金分红： 现金分红-元；红利再投资-份
     *
     * @return
     */
    public static String getFundUnit(Context mContext, String transType, String bonusType, String currencyCode) {
        if (TRANS_TYPE_PURCHASE.contains(transType) || STATUSDDAPPLY_TRANSTYPE_SCHEDULEDBUY.equals(transType)) {
            return getCurrencyDescByLetter(mContext, currencyCode);
        }
        if (TRANS_TYPE_REDDEM.contains(transType) || STATUSDDAPPLY_TRANSTYPE_SCHEDULEDSELL.equals(transType)) {
            return mContext.getString(R.string.boc_fund_bonus_unit);
        }
        if (TRANS_TYPE_BONUS.contains(transType)) {
            if (BONUSTYPE_CASH.equals(bonusType)) {
                return getCurrencyDescByLetter(mContext, currencyCode);
            } else {
                return mContext.getString(R.string.boc_fund_bonus_unit);
            }

        } else {
            return getCurrencyDescByLetter(mContext, currencyCode);
        }

    }

    /**
     * 获取币种描述
     *
     * @param context
     * @param "002"等
     * @return
     */
    public static String getCurrencyDescByLetter(Context context, String code) {
        if (code != null && code.equals("CNY")) {
            return context.getString(R.string.boc_fund_money_unit);
        }

        String result = PublicCodeUtils.getCurrency(context, code);
        if (StringUtils.isEmptyOrNull(result) || result.equals("-")) {
            result = PublicCodeUtils.getCurrencyWithLetter(context, code);
        }
        if (context.getString(R.string.boc_loan_mcurrency).equals(result)) {
            result = context.getString(R.string.boc_fund_money_unit);
        }
        return result;
    }


    public static String getCashFlagDes(Context mContet, String cashFlag) {
        if (StringUtil.isNullOrEmpty(cashFlag)) {
            return "";
        }
        if (cashFlag.equals(CASHFLAG_CAS)) {
            return mContet.getString(R.string.boc_fund_position_cash_cas);
        }
        if (cashFlag.equals(CASHFLAG_TRN)) {
            return mContet.getString(R.string.boc_fund_position_cash_trn);
        }
        return "";
    }

    //人民币--->"";其他正常
    public static String getCurrencyDesWithoutRMB(Context context, String code){
        String des = getCurrencyDescByLetter(context,code);
        if (StringUtil.isNullOrEmpty(des)){
            return "";
        }
        if (des.contains(context.getString(R.string.boc_fund_money_unit))){
            return "";
        }
        return des;
    }

    private static final String TRANSSTATUS_SUCCESS = "0";// 0：成功
    private static final String TRANSSTATUS_PART_SUCCESS = "1";//1：部分成功
    private static final String TRANSSTATUS_FAIL = "2";//2：失败

    public static String getTransStatusDes(Context mcontext, String type) {
        if (StringUtil.isNullOrEmpty(type)) {
            return mcontext.getString(R.string.boc_fund_trans_status_fail);
        }
        if (TRANSSTATUS_PART_SUCCESS.equals(type)) {
            return mcontext.getString(R.string.boc_fund_trans_status_party_success);
        } else if (TRANSSTATUS_SUCCESS.equals(type)) {
            return mcontext.getString(R.string.boc_fund_trans_status_success);

        }
        if (TRANSSTATUS_FAIL.equals(type)) {
            return mcontext.getString(R.string.boc_fund_trans_status_fail);
        }
        return mcontext.getString(R.string.boc_fund_trans_status_fail);
    }

    /**
     * 购买: transAmount + 元
     * 赎回：transCount + 份
     * 基金分红： 现金分红 transAmount+ 元；红利再投资-transCount + 份
     *
     * @return true: transAmount; false:transCount
     */
    public final static String FLAG_AMOUNT = "1";
    public final static String FLAG_COUNT = "2";
    public final static String FLAG_STATUST = "3";

    public static String getAmountOrCount(String transType, String bonusType) {
        if (TRANS_TYPE_PURCHASE.contains(transType)) {
            return FLAG_AMOUNT;
        } else if (TRANS_TYPE_REDDEM.contains(transType)) {
            return FLAG_COUNT;
        } else if (TRANS_TYPE_SET_BONUS.contains(transType)) {
            return FLAG_STATUST;
        } else if (TRANS_TYPE_BONUS.contains(transType)) {
            if (BONUSTYPE_CASH.equals(bonusType)) {
                return FLAG_AMOUNT;
            } else {
                return FLAG_COUNT;
            }
        }
        return FLAG_AMOUNT;
    }

    //011  PsnFundStatusDdApplyQuery定期定额申请查询 交易类型transType “0”：定投 “1”：定赎

    private static final String STATUSDDAPPLY_TRANSTYPE_SCHEDULEDBUY = "0";
    private static final String STATUSDDAPPLY_TRANSTYPE_SCHEDULEDSELL = "1";

    public static String getStatusDdApplytranstypeDes(Context mContext, String type) {
        if (StringUtil.isNullOrEmpty(type)) {
            return "-";
        }
        if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDBUY.equals(type)) {
            return mContext.getString(R.string.boc_fund_statusddapply_transtype_scheduledbuy);
        } else if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDSELL.equals(type)) {
            return mContext.getString(R.string.boc_fund_statusddapply_transtype_scheduledsell);
        } else {
            return "-";
        }
    }

    /**
     * 定投：
     * 0：正常
     * 1：暂停
     * 2：主动撤销
     * 3：达到预设结束日期失效
     * 4：达到预设累计扣款金额失效
     * 5：达到预设累计扣款次数失效
     * 6：扣款三次失败失效
     * 定赎：
     * 0：正常
     * 1：暂停
     * 2：主动撤销
     * 3：达到预设结束日期失效
     * 4：达到预设累计赎回份额失效
     * 5：达到预设累计赎回次数失效
     * 6：余额不足失效
     *
     * @param mContext
     * @param type
     * @return
     */
    public static String getStatusDdRecordStatusDes(Context mContext, String type, String index) {
        if (StringUtil.isNullOrEmpty(index) || StringUtil.isNullOrEmpty(type)) {
            return "";
        }
        if ("0".equals(index)) {
            return mContext.getString(R.string.boc_fund_statusddapply_record_normal);
        }
        if ("1".equals(index)) {
            return mContext.getString(R.string.boc_fund_statusddapply_record_pause);
        }
        if ("2".equals(index)) {
            return mContext.getString(R.string.boc_fund_statusddapply_record_revoke);
        }
        if ("3".equals(index)) {
            return mContext.getString(R.string.boc_fund_statusddapply_record_outdate);
        }
        if ("4".equals(index)) {
            if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDBUY.equals(type)) {
                return mContext.getString(R.string.boc_fund_statusddapply_record_buy_amount_stale);
            } else if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDSELL.equals(type)) {
                return mContext.getString(R.string.boc_fund_statusddapply_record_sell_amount_stale);
            }

        }
        if ("5".equals(index)) {
            if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDBUY.equals(type)) {
                return mContext.getString(R.string.boc_fund_statusddapply_record_buy_count_stale);
            } else if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDSELL.equals(type)) {
                return mContext.getString(R.string.boc_fund_statusddapply_record_sell_count_stale);
            }
        }
        if ("6".equals(index)) {
            if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDBUY.equals(type)) {
                return mContext.getString(R.string.boc_fund_statusddapply_record_buy_fail_stale);
            } else if (STATUSDDAPPLY_TRANSTYPE_SCHEDULEDSELL.equals(type)) {
                return mContext.getString(R.string.boc_fund_statusddapply_record_sell_fail_stale);
            }
        }
        return "";
    }


    //cashFlag CAS代表钞TRN代表汇
    private static final String CASHFLAG_CAS = "CAS";
    private static final String CASHFLAG_TRN = "TRN";

    /**
     * 返回：元；美元/汇；美元/钞
     * @param mContext
     * @param currencyCode
     * @param cashFlag
     * @return
     */
    public static String getCurrencyAndCashFlagDes(Context mContext,String currencyCode,String cashFlag){
        String currcyDes = getCurrencyDescByLetter(mContext,currencyCode);
        if (StringUtil.isNullOrEmpty(currcyDes)){
            return "";
        }
        String cashDes = getCashFlagDes(mContext,cashFlag);
        if (StringUtil.isNullOrEmpty(cashDes) || mContext.getString(R.string.boc_fund_money_unit).equals(currcyDes)){
            return currcyDes;
        }
        return currcyDes + "/" + cashDes;
    }

    /**
     * 在前一个函数基础上，特殊处理人民币：返回人民币元，不在返回元
     * @param mContext
     * @param currencyCode
     * @param cashFlag
     * @return
     */
    public static String getCurrencyAndCashFlagDesSpecalRMB(Context mContext, String currencyCode, String cashFlag){
        String result = getCurrencyAndCashFlagDes(mContext,currencyCode,cashFlag);
        if (mContext.getString(R.string.boc_fund_money_unit).equals(result)){
            return mContext.getString(R.string.boc_loan_mcurrency);
        }
        return result;
    }


    // "true""y" "Y" "是"--true;"false" "n" "N" "否"--false
    public static boolean despToBoolean(String desp) {
        if (StringUtil.isNullOrEmpty(desp)) {
            return false;
        }
        if (desp.equalsIgnoreCase("true")){
            return true;
        }
        if (desp.equalsIgnoreCase("false")){
            return false;
        }
        if (desp.equalsIgnoreCase("y")) {
            return true;
        }
        if (desp.equalsIgnoreCase("n")) {
            return false;
        }
        if ("是".equals(desp)) {
            return true;
        }
        if ("否".equals(desp)) {
            return false;
        }
        return false;
    }



    // 基金持仓------>持仓详情
    public static final String FUND_POSITION_BEAN_KEY = "fund_position_bean_key";
    public static final String FUND_POSITION_PROFILELOSS_KEY = "fund_position_profileloss_key";

    public static final String FUND_NAME_KEY = "fund_name_key";
    public static final String FUND_CODE_KEY = "fund_code_key";
    public static final String FUND_COMPANY_KEY = "fund_company_key";
    public static final String FUND_CURRENCY_KEY = "fund_currency_key";
    //基金转换----->基金确认
    public static final String FUND_CONVERSION_RESULT_KEY= "fund_conversion_result_key";
    //投资类型：12 ----->基金
    public static final String INVT_TYPE_FUND_CODE = "12";

    public static final String FUND_INVEST_ENDDATE ="1";
    public static final String FUND_INVEST_ENDSUN ="2";
    public static final String FUND_INVEST_SUNAMT="3";

    //
    public static final String FUND_FLOAT_PROFILELESS_BEAN_KEY ="fund_float_profileless_bean_key";
    //列表页合计------>合计详情传值
    public static final String FUND_PROFILE_LOSS_TOTAL_BEAN_KEY = "fund_profile_loss_total_bean_key";
    //列表页时间值/持仓------->详情传值
    public static final String FUND_PROFILE_LOSS_TIME_HINT = "fund_profile_loss_time_hint";
    //持仓--- 详情 可选择；列表---详情：不可选择
    public static final String FUND_PROFILE_LOSS_TIME_SELECT_KEY = "fund_profile_loss_time_select_key";

    /**基金制定条件转换
     * @param endCode
     */
    public static String getEndFlag(String endCode) {
        String endFlag = "";
        if (StringUtils.isEmptyOrNull(endCode)) {
            return endFlag;
        }
        if (TRANS_TYPE_ALL.equals(endCode)) {
            endFlag = "空";
        } else if (FUND_INVEST_ENDDATE.equals(endCode)) {
            endFlag = "指定交易日期";
        } else if (FUND_INVEST_ENDSUN.equals(endCode)) {
            endFlag = "累计交易次数";
        } else if (FUND_INVEST_SUNAMT.equals(endCode)) {
            endFlag = "累计交易份额";
        }
        return endFlag;
    }

    /**赎回延续方式*/
    public static String getSellFlag(String sellcode) {
        String sellFlag = "";
        if (StringUtils.isEmptyOrNull(sellcode)) {
            return sellFlag;
        }
        if ("1".equals(sellcode)) {
            sellFlag = "顺延赎回";
        } else if ("2".equals(sellcode)) {
            sellFlag = "取消赎回";
        }
        return sellFlag;
    }
    /**赎回是否延续方式*/
    public static String getDtdsFlag (String sellflag) {
        String sellcode = "";
        if (StringUtils.isEmptyOrNull(sellflag)) {
            return sellcode;
        }
        if ("0".equals(sellflag)) {
            sellcode = "每月";
        } else if ("1".equals(sellflag)) {
            sellcode = "每周";
        }
        return sellcode;
    }


    /**基金定投选择周期
     * @param type
     */
    public static String getWeekDate(String type) {
        String weekdate = "";
        if (StringUtils.isEmptyOrNull(type)) {
            return weekdate;
        }
        if ("1".equals(type)) {
            weekdate = "周一";
        } else if ("2".equals(type)) {
            weekdate = "周二";
        } else if ("3".equals(type)) {
            weekdate = "周三";
        } else if ("4".equals(type)) {
            weekdate = "周四";
        } else if ("5".equals(type)) {
            weekdate = "周五";
        }
        return weekdate;
    }
    //数值 正负标志
    public static String NUM_POSITIVE_FLAG = "-";

    public static final int RISK_ASSESS_ACCOUNT = 1;//账户管理
    public static final int RISK_ASSESS_CHOICE = 2;//评估答题


}

