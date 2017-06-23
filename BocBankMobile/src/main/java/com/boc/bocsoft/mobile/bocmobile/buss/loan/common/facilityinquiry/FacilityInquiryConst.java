package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * Created by liuzc on 2016/8/18.
 */
public class FacilityInquiryConst {
    public final static String KEY_FACILITY_INQUIRY_BEAN = "facility_inquiry_bean";
    public final static String KEY_FACILITY_USE_REC_BEAN = "facility_use_record_bean";
    public final static String KEY_REPAYOVER_FLAG = "repay_over_flag";

    /**
     * 数值四舍五入得到decimalCount位小数
     * @param value
     * @param decimalCount
     * @return
     */
    public static  String getStrWithFormatDecimal(String value, int decimalCount){
        if(StringUtils.isEmptyOrNull(value)){
            return "";
        }

        BigDecimal bdValue = new BigDecimal(value);
        return bdValue.setScale(decimalCount, BigDecimal.ROUND_HALF_UP).toString();
    }
}
