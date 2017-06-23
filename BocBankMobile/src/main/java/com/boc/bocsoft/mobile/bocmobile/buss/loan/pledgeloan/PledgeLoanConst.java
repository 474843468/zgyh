package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan;

import android.support.v4.util.ArrayMap;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import java.util.ArrayList;

/**
 * 作者：XieDu
 * 创建时间：2016/8/16 19:04
 * 描述：
 */
public class PledgeLoanConst {

    //收款账户、付款账户支持借记卡、活期一本通、普通活期
    public static ArrayList<String> typeList = new ArrayList<>();

    static {
        typeList.add(ApplicationConst.ACC_TYPE_BRO);
        typeList.add(ApplicationConst.ACC_TYPE_RAN);
        typeList.add(ApplicationConst.ACC_TYPE_ORD);
    }

    public static ArrayMap<String, String> checkPayeeResultMap = new ArrayMap<>();

    static {
        checkPayeeResultMap.put("01", "符合业务条件");
        checkPayeeResultMap.put("02", "贷款账户与收款账户币种不一致");
        checkPayeeResultMap.put("03", "收款账户为钞户");
        checkPayeeResultMap.put("04", ApplicationContext.getInstance().getString(R.string.boc_eloan_payment_check_account_signed_invest));
    }

    public static ArrayMap<String, String> checkPayerResultMap = new ArrayMap<>();

    static {
        checkPayerResultMap.put("01", "符合业务条件");
        checkPayerResultMap.put("02", "贷款账户与还款账户币种不一致");
        checkPayerResultMap.put("03", "还款账户为钞户");
    }

    /**
     * 还款方式
     * B：只还利息
     */

    public static final String LONETYPE_B = "B";
    /**
     * *F：等额本息
     */
    public static final String LONETYPE_F = "F";
    /**
     * G：等额本金
     */
    public static final String LONETYPE_G = "G";
    /**
     * *N：协议还款
     */
    public static final String LONETYPE_N = "N";
}
