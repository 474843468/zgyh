package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment;

import android.content.Context;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.HashMap;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class RiskAssessmentUtils {

    public static final int RISK_ASSESS_ACCOUNT = 1;//账户管理
    public static final int RISK_ASSESS_CHOICE = 2;//评估答题
    private static Context mcontext = ApplicationContext.getAppContext();
    // 等级----名称描述
    private static HashMap<String, String> riskLevel2Name = new HashMap();
    //等级---详细描述
    private static HashMap<String,String> riskLevel2Descripiton = new HashMap<>();
    static {
        riskLevel2Name.put("1", mcontext.getString(R.string.boc_risk_assess_keep));
        riskLevel2Name.put("2", mcontext.getString(R.string.boc_risk_assess_steady));
        riskLevel2Name.put("3", mcontext.getString(R.string.boc_risk_assess_balance));
        riskLevel2Name.put("4", mcontext.getString(R.string.boc_risk_assess_grow));
        riskLevel2Name.put("5", mcontext.getString(R.string.boc_risk_assess_progress));

    }



    public static String getDescByLevel(String riskLevel){
        if (StringUtil.isNullOrEmpty(riskLevel)) {
            return ApplicationContext.getAppContext().getString(R.string.boc_risk_assess_not);
        }
        if (riskLevel2Name.containsKey(riskLevel) == false){
            return ApplicationContext.getAppContext().getString(R.string.boc_risk_assess_not);
        }
        return riskLevel2Name.get(riskLevel);

    }


}
