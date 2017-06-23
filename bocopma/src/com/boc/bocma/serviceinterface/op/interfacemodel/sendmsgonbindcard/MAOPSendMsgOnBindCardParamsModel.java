package com.boc.bocma.serviceinterface.op.interfacemodel.sendmsgonbindcard;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 增加卡及短信发送参数类
 */
public class MAOPSendMsgOnBindCardParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "unlogin/sendchit";
    
    private static final String USER_ID_KEY = "userid";
    private static final String CUS_NAME_KEY = "cusname";
    private static final String CARD_NUMBER_KEY = "cardno";
    private static final String ID_TYPE_KEY = "idtype";
    private static final String CARD_PURP_KEY = "cardpurp";
    private static final String CAROTH_NAME_KEY = "carothname";
    private static final String ID_NUMBER = "idno";
    private static final String CHECK_MSG_KEY = "checkmsg";
    private static final String MOBILE_NUMBER_KEY = "mobleno";
    private static final String MSG_FLAG_KEY = "msgflag";
    private static final String CUS_ID_KEY = "cusid";
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, userId);
        body.put(CUS_NAME_KEY, cusName);
        body.put(CARD_NUMBER_KEY, cardNumber);
        body.put(ID_TYPE_KEY, idType);
        body.put(CARD_PURP_KEY, cardPurpose);
        body.put(CAROTH_NAME_KEY, carothName);
        body.put(ID_NUMBER, idNumber);
        body.put(CHECK_MSG_KEY, checkMsg);
        body.put(MOBILE_NUMBER_KEY, mobileNumber);
        body.put(MSG_FLAG_KEY, msgFlag);
        body.put(CUS_ID_KEY, cusId);
        return body.toString();
    }
    
    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param cusName 客户姓名
     */
    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    /**
     * @param cardNumber 卡号
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @param idType 证件类型
     */
    public void setIdType(String idType) {
        this.idType = idType;
    }

    /**
     * @param cardPurpose 卡用途
     * 增加卡时必输，其它可输 01-查询, 02-支付
     */
    public void setCardPurpose(String cardPurpose) {
        this.cardPurpose = cardPurpose;
    }

    /**
     * @param carothName 卡别名
     */
    public void setCarothName(String carothName) {
        this.carothName = carothName;
    }

    /**
     * @param idNumber 证件号码
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    /**
     * @param checkMsg 增加卡时是否发送短信验证码, 
     * 增加卡时必输，其它可输。Y-是 N-否
     */
    public void setCheckMsg(String checkMsg) {
        this.checkMsg = checkMsg;
    }

    /**
     * @param mobileNumber 手机号
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @param msgFlag 交易概要 
     * PD0001–增加卡流程, PD0002– 修改手机号流程, PD0003 - 新增用户注册验证短信
     */
    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    /**
     * @param cusId 核心客户号
     */
    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    private String userId;
    private String cusName;
    private String cardNumber;
    private String idType;
    private String cardPurpose;
    private String carothName;
    private String idNumber;
    private String checkMsg;
    private String mobileNumber;
    private String msgFlag;
    private String cusId;

}
