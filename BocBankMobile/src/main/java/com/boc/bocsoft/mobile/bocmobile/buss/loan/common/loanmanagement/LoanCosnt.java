package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by huixiaobo 2016/9/20.
 */
public class LoanCosnt {
    /**贷款产品对象*/
    public static String LOAN_DATA = "PsnLOANListEQueryBean";
    /**系统当前时间*/
    public static String LOAN_ENDDATE = "endDate";
    /**逾期信息*/
    public static String LOAN_OVERDUE = "overdue";
    /**还款账号*/
    public static String LOAN_REPYNUM = "cardNum";
    /*进入还款记录页面显示titlebar**/
    public static String IS_SHOW_TITLE_BAR = "isshowtitlebar";
    /**贷款当前时间*/
    public static String LOAN_END_DATE = "loan_end_date";
    public static String LOAN_ACCOUTLIST_MODEL = "loanaccountlistmodel";


    /**
     * 还款期限单位: 年、月、日
     */
    public static final String LOAN_PERIOD_UNIT_YEAR = "Y";
    public static final String LOAN_PERIOD_UNIT_MONTH = "M";
    public static final String LOAN_PERIOD_UNIT_DAY = "D";

    /**
     * 获取还款周期描述
     * @param code
     * @return
     */
    public static String getLoanUnitPeriodUnitDesc(Context context, String code){
        if(StringUtils.isEmpty(code)){
            return "";
        }
        code = code.trim();
        if(code.equals(LOAN_PERIOD_UNIT_YEAR)){
            return context.getResources().getString(R.string.boc_year);
        }
        else if(code.equals(LOAN_PERIOD_UNIT_MONTH)){
            return context.getResources().getString(R.string.boc_month);
        }
        else if(code.equals(LOAN_PERIOD_UNIT_DAY)){
            return context.getResources().getString(R.string.boc_day);
        }

        return "";
    }


}
