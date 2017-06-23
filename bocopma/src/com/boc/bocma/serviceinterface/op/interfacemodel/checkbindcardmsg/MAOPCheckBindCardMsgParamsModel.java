package com.boc.bocma.serviceinterface.op.interfacemodel.checkbindcardmsg;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 增加卡短信验证参数类
 *
 */
public class MAOPCheckBindCardMsgParamsModel extends MAOPBaseParamsModel {

    private static final String INTERFACE_URL = "checkmobleinfo";
    
    private static final String USER_ID_KEY = "userid";
    private static final String MOBILE_NUMBER_KEY = "mobleno";
    private static final String CARD_NUMBER_KEY = "cardno";
    private static final String CARD_PURP_KEY = "cardpurp";
    private static final String CAROTH_NAME_KEY = "carothname";
    private static final String MSG_FLAG_KEY = "msgflag";
    private static final String CHECK_CODE_KEY = "chkcode";
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, userId);
        body.put(MOBILE_NUMBER_KEY, mobileNumber);
        body.put(CARD_NUMBER_KEY, cardNumber);
        body.put(CARD_PURP_KEY, cardPurpose);
        body.put(CAROTH_NAME_KEY, carothName);
        body.put(MSG_FLAG_KEY, msgFlag);
        body.put(CHECK_CODE_KEY, checkCode);
        return body.toString();
    }
    
    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param cardNumber 卡号 左对齐右补空格
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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
     * @param checkCode 短信验证码, 
     */
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    /**
     * @param mobileNumber 手机号
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @param msgFlag 交易概要 
     * PD0001–增加卡流程, PD0002– 修改手机号流程
     */
    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    private String userId;
    private String mobileNumber;
    private String cardNumber;
    private String cardPurpose;
    private String carothName;
    private String msgFlag;
    private String checkCode;

}
