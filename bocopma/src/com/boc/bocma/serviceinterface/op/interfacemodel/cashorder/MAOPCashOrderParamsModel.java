package com.boc.bocma.serviceinterface.op.interfacemodel.cashorder;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 现钞预约参数类
 */
public class MAOPCashOrderParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "ooforder";

    private static final String USER_ID_KEY = "userid";
    private static final String CARD_TYPE_KEY = "credtype";
    private static final String CARD_NUMBER_KEY = "credno";
    private static final String CUSTOMER_NUMBER_KEY = "custnum";
    private static final String CUSTOMER_NAME_KEY = "custname";
    private static final String SEX_KEY = "sex";
    private static final String BIRTHDAY_KEY = "birth";
    private static final String CUSTOMER_GRADE_KEY = "custgrade";
    private static final String MOBILE_KEY = "mobile";
    private static final String ORDER_DATE_KEY = "orderdate";
    private static final String ORDER_TIME_KEY = "ordertime";
    private static final String BUSINESS_TIME_KEY = "bustype";
    private static final String CURRENCY_KEY = "currency";
    private static final String AMOUNT_KEY = "amount";
    private static final String FILLER_KEY = "filler";
    private static final String LANGUAGE_TYPE_KEY = "lantype";
    private static final String BRANCH_NUMBER = "sitno";
    
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, userId);
        body.put(CARD_TYPE_KEY, cardType);
        body.put(CARD_NUMBER_KEY, cardNumber);
        body.put(CUSTOMER_NUMBER_KEY, customerNumber);
        body.put(CUSTOMER_NAME_KEY, customerName);
        body.put(SEX_KEY, sex);
        body.putOpt(BIRTHDAY_KEY, birth);
        body.putOpt(CUSTOMER_GRADE_KEY, customerGrade);
        body.putOpt(MOBILE_KEY, mobile);
        body.putOpt(ORDER_DATE_KEY, orderDate);
        body.putOpt(ORDER_TIME_KEY, orderTime);
        body.putOpt(BUSINESS_TIME_KEY, businessType);
        body.putOpt(CURRENCY_KEY, currency);
        body.putOpt(AMOUNT_KEY, amount);
        body.putOpt(FILLER_KEY, filler);
        body.putOpt(LANGUAGE_TYPE_KEY, language);
        body.put(BRANCH_NUMBER, branchNumber);
        return body.toString();
    }

    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param cardType 卡类型
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * @param cardNumber 卡（介质号码）
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @param customerNumber 客户号
     */
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     * @param customerName 客户姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @param sex 性别 01：男 02：女
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @param birth 客户生日 格式：YYYYMMDD
     */
    public void setBirth(String birth) {
        this.birth = birth;
    }

    /**
     * @param customerGrade 客户等级
     */
    public void setCustomerGrade(String customerGrade) {
        this.customerGrade = customerGrade;
    }

    /**
     * @param mobile 预约手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @param orderDate 预约日期 格式：YYYYMMDD
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @param orderTime 预约时段 例如时段8：30~13:15, 取值为08301315
     */
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * @param businessType 特殊业务类型
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * @param currency 币种
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @param amount 金额
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @param filler 特殊业务备注信息
     */
    public void setFiller(String filler) {
        this.filler = filler;
    }

    /**
     * @param language 语种 CN 简体中文 EN 英语
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    /**
     * @param branchNumber 网点机构号
     */
    public void setBranchNumber(String branchNumber) {
        this.branchNumber = branchNumber;
    }
    
    private String userId;
    private String cardType;
    private String cardNumber;
    private String customerNumber;
    private String customerName;
    private String sex;
    private String birth;
    private String customerGrade;
    private String mobile;
    private String orderDate;
    private String orderTime;
    private String businessType;
    private String currency;
    private String amount;
    private String filler;
    private String language;
    private String branchNumber;
}
