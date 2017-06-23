package com.boc.bocsoft.mobile.bii.bus.global.service;

import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNBmpsCreatConversation.PSNBmpsCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNBmpsCreatConversation.PSNBmpsCreatConversationResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryOprIp.PsnCommonQueryOprIpParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryOprIp.PsnCommonQueryOprIpResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCustomerCombinInfo.PsnCustomerCombinInfoParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCustomerCombinInfo.PsnCustomerCombinInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCustomerCombinInfo.PsnCustomerCombinInfoResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnMobileIsSignedAgent.PsnMobileIsSignedAgentParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnMobileIsSignedAgent.PsnMobileIsSignedAgentResponse;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnMobileIsSignedAgent.PsnMobileIsSignedAgentResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnSendSMSCodeToMobile.PsnSendSMSCodeParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnSendSMSCodeToMobile.PsnSendSMSCodeResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSetMobileInfo.PsnSetMobileInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSetMobileInfo.PsnSetMobileInfoResponse;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;
import java.util.List;
import rx.Observable;

/**
 * Created by feibin on 2016/6/7.
 * 公共service
 */
public class GlobalService {


    /**
     * 获取会话
     *added by feibin 20160603
     * @param params
     */
    public  Observable<String> psnCreatConversation(PSNCreatConversationParams params) {
        return BIIClient.instance.post("PSNCreatConversation", params, PSNCreatConversationResponse.class);
    }

    /**
     * 获取随机数
     *added by feibin 20160603
     * @param params
     */
    public  Observable<String> psnGetRandom(PSNGetRandomParams params) {
        return BIIClient.instance.post("PSNGetRandom", params, PSNGetRandomResponse.class);
    }

    /**
     * 检查本机是否绑定
     *added by feibin 20160612
     */
    public Observable<CurrentDeviceCheckResult> currentDeviceCheck(CurrentDeviceCheckParams params) {
        return BIIClient.instance.post("CurrentDeviceCheck", params, CurrentDeviceCheckResponse.class);
    }

    /**
     * 获取安全因子
     *added by feibin 20160615
     * @param params
     */
    public  Observable<PsnGetSecurityFactorResult> psnGetSecurityFactor(PsnGetSecurityFactorParams params) {
        return BIIClient.instance.post("PsnGetSecurityFactor", params, PsnGetSecurityFactorResponse.class);
    }

    /**
     * 查询中行所有帐户列表
     *
     * @param params
     */
    public Observable<List<PsnCommonQueryAllChinaBankAccountResult>> psnCommonQueryAllChinaBankAccount(PsnCommonQueryAllChinaBankAccountParams params) {
        return BIIClient.instance.post("PsnCommonQueryAllChinaBankAccount", params, PsnCommonQueryAllChinaBankAccountResponse.class);
    }


    /**
     * 获取Token
     *
     * @param params
     */
    public  Observable<String> psnGetTokenId(PSNGetTokenIdParams params) {
        return BIIClient.instance.post("PSNGetTokenId", params, PSNGetTokenIdResponse.class);
    }

    /**
     * 发送手机验证码
     *
     * @param params
     */
    public static Observable<String> psnSendSMSCodeToMobile(PsnSendSMSCodeParams params) {
        return BIIClient.instance.post("PsnSendSMSCodeToMobile", params, PsnSendSMSCodeResponse.class);
    }

    /**
     * 获取服务器时间
     *
     * @param params
     */
    public static Observable<PsnCommonQuerySystemDateTimeResult> psnCommonQuerySystemDateTime(PsnCommonQuerySystemDateTimeParams params) {
        return BIIClient.instance.post("PsnCommonQuerySystemDateTime", params, PsnCommonQuerySystemDateTimeResponse.class);
    }

    /**
     * 登录后获取客户合并后信息
     *
     * @param params
     */
    public  Observable<PsnCustomerCombinInfoResult> psnCustomerCombinInfo(PsnCustomerCombinInfoParams params) {
        return BIIClient.instance.post("PsnCustomerCombinInfo", params, PsnCustomerCombinInfoResponse.class);
    }

    /**
     * BMPS登录前创建交易会话
     *
     * @param params
     */
    public  Observable<String> psnBmpsCreatConversation(PSNBmpsCreatConversationParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(), "PSNBmpsCreatConversation", params, PSNBmpsCreatConversationResponse.class);
    }

    /**
     * I43-4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询
     *
     * @param params
     */
    public  Observable<List<PsnGetAllExchangeRatesOutlayResult>> psnGetAllExchangeRatesOutlay(PsnGetAllExchangeRatesOutlayParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(), "PsnGetAllExchangeRatesOutlay", params, PsnGetAllExchangeRatesOutlayResponse.class);
    }
    /**
     * 消息推送硬件绑定
     *
     * @param params
     */
    public  Observable<String> psnSetMobileInfo(PsnSetMobileInfoParams params) {
        return BIIClient.instance.post( "PsnSetMobileInfo", params, PsnSetMobileInfoResponse.class);
    }
    /**
     * 查询IP地址
     *
     * @param params
     */
    public  Observable<String> psnCommonQueryOprIp(PsnCommonQueryOprIpParams params) {
        return BIIClient.instance.post("PsnCommonQueryOprIp", params, PsnCommonQueryOprIpResponse.class);
    }

    /**
     *I12 4.12 012代理点签约判断: PsnMobileIsSignedAgent
     *
     * @param params 参数
     * @return 是否签约结果
     */
    public  Observable<PsnMobileIsSignedAgentResult> psnMobileIsSignedAgent(PsnMobileIsSignedAgentParams params) {
        return BIIClient.instance.post("PsnMobileIsSignedAgent", params, PsnMobileIsSignedAgentResponse.class);
    }

}
