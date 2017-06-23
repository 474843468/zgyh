package com.boc.bocma.serviceinterface.op.interfacemodel.queuequery;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 预约取号-排队情况查询结果
 */
public class MAOPQueueQueryModel extends MAOPBaseResponseModel {
    /**
     * @return 对公一般客户等待人数
     */
    public String getCommonPublicWaitNumber() {
        return commonPublicWaitNumber;
    }

    /**
     * @return 对公一般客户柜台数
     */
    public String getCommonPublicCounterNumber() {
        return commonPublicCounterNumber;
    }

    /**
     * @return 对公VIP等待人数
     */
    public String getVipPubWaitNumber() {
        return vipPubWaitNumber;
    }

    /**
     * @return 对公VIP柜台数
     */
    public String getVipPubCounterNumber() {
        return vipPubCounterNumber;
    }

    /**
     * @return 对私一般客户等待人数
     */
    public String getCommonPrivateWaitNumber() {
        return commonPrivateWaitNumber;
    }

    /**
     * @return 对私一般客户柜台数
     */
    public String getCommonPrivateCounterNumber() {
        return commonPrivateCounterNumber;
    }

    /**
     * @return 对私VIP等待人数
     */
    public String getVipPriviateWaitNumber() {
        return vipPriviateWaitNumber;
    }

    /**
     * @return 对私VIP柜台数
     */
    public String getVipPrivateCounterNumber() {
        return vipPrivateCounterNumber;
    }

    /**
     * @return 网点介绍
     */
    public String getDescription() {
        return description;
    }

    private static final String COMMON_PUB_WAIT_NUM_KEY = "common_pub_wait_num";
    private static final String COMMON_PUB_COUNTER_NUM_KEY = "common_pub_counter_num";
    private static final String VIP_PUB_WAIT_NUM_KEY = "vip_pub_wait_num";
    private static final String VIP_PUB_COUNTER_NUM_KEY = "vip_pub_counter_num";
    private static final String COMMON_PRI_WAIT_NUM_KEY = "common_pri_wait_num";
    private static final String COMMON_PRI_COUNTER_NUM_KEY = "common_pri_counter_num";
    private static final String VIP_PRI_WAIT_NUM_KEY = "vip_pri_wait_num";
    private static final String VIP_PRI_COUNTER_NUM_KEY = "vip_pri_counter_num";
    private static final String DESCRIPTION_KEY = "description";

    private String commonPublicWaitNumber;
    private String commonPublicCounterNumber;
    private String vipPubWaitNumber;
    private String vipPubCounterNumber;
    private String commonPrivateWaitNumber;
    private String commonPrivateCounterNumber;
    private String vipPriviateWaitNumber;
    private String vipPrivateCounterNumber;
    private String description;
    
    public MAOPQueueQueryModel(JSONObject jsonResponse) {
        commonPublicWaitNumber = jsonResponse.optString(COMMON_PUB_WAIT_NUM_KEY);
        commonPublicCounterNumber = jsonResponse.optString(COMMON_PUB_COUNTER_NUM_KEY);
        vipPubWaitNumber = jsonResponse.optString(VIP_PUB_WAIT_NUM_KEY);
        vipPubCounterNumber = jsonResponse.optString(VIP_PUB_COUNTER_NUM_KEY);
        commonPrivateWaitNumber = jsonResponse.optString(COMMON_PRI_WAIT_NUM_KEY);
        commonPrivateCounterNumber = jsonResponse.optString(COMMON_PRI_COUNTER_NUM_KEY);
        vipPriviateWaitNumber = jsonResponse.optString(VIP_PRI_WAIT_NUM_KEY);
        vipPrivateCounterNumber = jsonResponse.optString(VIP_PRI_COUNTER_NUM_KEY);
        jsonResponse.optString(DESCRIPTION_KEY);
    }

    public static final Creator<MAOPQueueQueryModel> CREATOR = new Creator<MAOPQueueQueryModel>() {
        @Override
        public MAOPQueueQueryModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueueQueryModel(jsonResponse);
        }
        
    };
}
