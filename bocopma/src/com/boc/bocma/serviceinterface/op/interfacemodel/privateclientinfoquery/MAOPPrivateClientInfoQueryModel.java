package com.boc.bocma.serviceinterface.op.interfacemodel.privateclientinfoquery;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPPrivateClientInfoQueryModel extends MAOPBaseResponseModel {
    
    private static final String CUSTOMER_ID_KEY = "cusid";
    private static final String CUSTOMER_TYPE_KEY = "custype";
    private static final String CUSTOMER_SON_TYPE_KEY = "cussontype";
    private static final String PASS_TYPE_KEY = "passtype";
    private static final String PASS_NUMBER_KEY = "passno";
    private static final String FIRST_NAME_KEY = "firstname";
    private static final String LAST_NAME_KEY = "lastname";
    private static final String NATIONAL_KEY = "national";
    private static final String SEX_KEY = "sex";
    private static final String CARD_TYPE_KEY = "cardtype";
    private static final String MOBLE_NUMBER_KEY = "mobleno";
    
    
    private MAOPPrivateClientInfoQueryModel(JSONObject jsonResponse) throws JSONException {
        cusId = jsonResponse.optString(CUSTOMER_ID_KEY);
        cusType = jsonResponse.optString(CUSTOMER_TYPE_KEY);
        cusSonType = jsonResponse.optString(CUSTOMER_SON_TYPE_KEY);
        passType = jsonResponse.optString(PASS_TYPE_KEY);
        passNo = jsonResponse.optString(PASS_NUMBER_KEY);
        firstName = jsonResponse.optString(FIRST_NAME_KEY);
        lastName = jsonResponse.optString(LAST_NAME_KEY);
        national = jsonResponse.optString(NATIONAL_KEY);
        sex = jsonResponse.optInt(SEX_KEY);
        cardType = jsonResponse.optString(CARD_TYPE_KEY);
        mobileNo = jsonResponse.optString(MOBLE_NUMBER_KEY);
    }
    
    public static final Creator<MAOPPrivateClientInfoQueryModel> CREATOR = new Creator<MAOPPrivateClientInfoQueryModel>() {
        @Override
        public MAOPPrivateClientInfoQueryModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPPrivateClientInfoQueryModel(jsonResponse);
        }
    };
    
    private String cusId;
    private String cusType;
    private String cusSonType;
    private String passType;
    private String passNo;
    private String firstName;
    private String lastName;
    private String national;
    private int sex = -1;
    private String cardType;
    private String mobileNo;
    
    /**
     * 客户号
     */
    public String getCusId() {
        return cusId;
    }

    /**
     * 客户类型
     * 无值返空，有则选填如下： 01–个人用户，02–企业用户 
     */
    public String getCusType() {
        return cusType;
    }

    /**
     * 客户子类型
     * @return
     */
    public String getCusSonType() {
        return cusSonType;
    }

    /**
     * 证件类型
     * @return
     */
    public String getPassType() {
        return passType;
    }

    /**
     * 证件号码
     * @return
     */
    public String getPassNo() {
        return passNo;
    }

    /**
     * 客户姓
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 客户名
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 国籍
     * @return
     */
    public String getNational() {
        return national;
    }

    /**
     * 性别
     * @return 默认值为-1，1-男, 0-女
     */
    public int getSex() {
        return sex;
    }

    /**
     * 卡类型 选填如下：01-借记卡，02-贷记卡 （信用卡）
     * @return
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * 手机号
     * @return
     */
    public String getMobileNo() {
        return mobileNo;
    }

    
}
