package com.boc.bocsoft.mobile.bii.bus.login.service;

import com.boc.bocsoft.mobile.bii.bus.login.PsnAccBocnetQryLoginInfo.PsnAccBocnetQryLoginInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.PsnAccBocnetQryLoginInfo.PsnAccBocnetQryLoginInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.login.PsnAccBocnetQryLoginInfo.PsnAccBocnetQryLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.LoginWP7.LoginWP7Params;
import com.boc.bocsoft.mobile.bii.bus.login.model.LoginWP7.LoginWP7Response;
import com.boc.bocsoft.mobile.bii.bus.login.model.LoginWP7.LoginWP7Result;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PSNCreatConversationLoginPre.PSNCreatConversationLoginPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PSNCreatConversationLoginPre.PSNCreatConversationLoginPreResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PSNGetRandomLoginPre.PSNGetRandomLoginPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PSNGetRandomLoginPre.PSNGetRandomLoginPreResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetCreateConversationPre.PsnAccBocnetCreateConversationPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetCreateConversationPre.PsnAccBocnetCreateConversationPreResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetGetRandomPre.PsnAccBocnetGetRandomPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetGetRandomPre.PsnAccBocnetGetRandomPreResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetLogin.PsnAccBocnetLoginParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetLogin.PsnAccBocnetLoginResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetLogin.PsnAccBocnetLoginResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCardType.PsnAccBocnetQryCardTypeParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCardType.PsnAccBocnetQryCardTypeResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCardType.PsnAccBocnetQryCardTypeResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCrcdPoint.PsnAccBocnetQryCrcdPointParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCrcdPoint.PsnAccBocnetQryCrcdPointResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCrcdPoint.PsnAccBocnetQryCrcdPointResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryDebitDetail.PsnAccBocnetQryDebitDetailParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryDebitDetail.PsnAccBocnetQryDebitDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryDebitDetail.PsnAccBocnetQryDebitDetailResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQueryGeneralInfo.PsnAccBocnetQueryGeneralInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQueryGeneralInfo.PsnAccBocnetQueryGeneralInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQueryGeneralInfo.PsnAccBocnetQueryGeneralInfoResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnCommonQueryOprLoginInfo.PsnCommonQueryOprLoginInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnCommonQueryOprLoginInfo.PsnCommonQueryOprLoginInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnCommonQueryOprLoginInfo.PsnCommonQueryOprLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnRedirectEzuc.PsnRedirectEzucParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnRedirectEzuc.PsnRedirectEzucResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnRedirectEzuc.PsnRedirectEzucResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.PsnSvrRegisterDevicePreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.PsnSvrRegisterDevicePreResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.PsnSvrRegisterDevicePreResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnWMSQueryWealthRank.PsnWMSQueryWealthRankParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnWMSQueryWealthRank.PsnWMSQueryWealthRankResponse;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import rx.Observable;

/**
 * Created by feibin on 2016/5/19.
 * 登录service
 */
public class PsnLoginService {


    /**
     * 获取登录前会话
     *added by feibin 20160617
     * @param params
     * @return
     */
    public Observable<String> pSNCreatConversationLoginPre(PSNCreatConversationLoginPreParams params) {

        return BIIClient.instance.post("PSNCreatConversationLoginPre", params, PSNCreatConversationLoginPreResponse.class);
    }

    /**
     * 获取登录前随机数
     *added by feibin 20160607
     * @param params
     * @return
     */
    public Observable<String> pSNGetRandomLoginPre(PSNGetRandomLoginPreParams params) {
        return BIIClient.instance.post("PSNGetRandomLoginPre", params, PSNGetRandomLoginPreResponse.class);
    }

    /**
     * 手机号登录
     *added by feibin 20160615
     * @param params
     * @return
     */
    public Observable<LoginWP7Result> loginWP7(LoginWP7Params params) {
        return BIIClient.instance.post("LoginWP7", params, LoginWP7Response.class);
    }

    /**
     * 获取客户等级
     * added by feibin 20160610
     * @return
     */
    public Observable<String> psnWMSQueryWealthRank(PsnWMSQueryWealthRankParams params) {
        return BIIClient.instance.post("PsnWMSQueryWealthRank", params, PsnWMSQueryWealthRankResponse.class);
    }

    /**
     * 获取操作员信息
     * added by feibin 20160612
     * @return
     */
    public Observable<PsnCommonQueryOprLoginInfoResult> psnCommonQueryOprLoginInfo (PsnCommonQueryOprLoginInfoParams params) {
        return BIIClient.instance.post("PsnCommonQueryOprLoginInfo", params, PsnCommonQueryOprLoginInfoResponse.class);
    }
    /**
     * 设备注册预交易
     * added by feibin 20160625
     * @return
     */
    public Observable<PsnSvrRegisterDevicePreResult> psnSvrRegisterDevicePre (PsnSvrRegisterDevicePreParams params) {
        return BIIClient.instance.post("PsnSvrRegisterDevicePre", params, PsnSvrRegisterDevicePreResponse.class);
    }

    /**
     * 设备注册交易
     * added by feibin 20160627
     * @return
     */
    public Observable<PsnSvrRegisterDeviceSubmitResult> psnSvrRegisterDeviceSubmit (PsnSvrRegisterDeviceSubmitParams params) {
        return BIIClient.instance.post("PsnSvrRegisterDeviceSubmit", params, PsnSvrRegisterDeviceSubmitResponse.class);
    }

    /**
     * 退出
     * added by feibin 20160715
     * @return
     */
    public Observable<LogoutResult> logout (LogoutParams params) {
        return BIIClient.instance.post("Logout", params, LogoutResponse.class);
    }

    /**
     * PsnAccBocnetCreateConversationPre登录前创建交易会话
     * added by feibin 20160715
     * @return
     */
    public Observable<String> psnAccBocnetCreateConversationPre (PsnAccBocnetCreateConversationPreParams params) {
        return BIIClient.instance.post("PsnAccBocnetCreateConversationPre", params, PsnAccBocnetCreateConversationPreResponse.class);
    }

    /**
     * PsnAccBocnetGetRandomPre登录前获取加密随机数
     * added by feibin 20160715
     * @return
     */
    public Observable<String> psnAccBocnetGetRandomPre (PsnAccBocnetGetRandomPreParams params) {
        return BIIClient.instance.post("PsnAccBocnetGetRandomPre", params, PsnAccBocnetGetRandomPreResponse.class);
    }

    /**
     * PsnAccBocnetQryCardType通过卡号查询卡类型,判断是否可以进行卡号登录
     * added by feibin 20160715
     * @return
     */
    public Observable<PsnAccBocnetQryCardTypeResult> psnAccBocnetQryCardType (PsnAccBocnetQryCardTypeParams params) {
        return BIIClient.instance.post("PsnAccBocnetQryCardType", params, PsnAccBocnetQryCardTypeResponse.class);
    }

    /**
     *  PsnAccBocnetLogin 使用卡号登录账户版网银
     * added by feibin 20160801
     * @return
     */
    public Observable<PsnAccBocnetLoginResult> psnAccBocnetLogin (PsnAccBocnetLoginParams params) {
        return BIIClient.instance.post("PsnAccBocnetLogin", params, PsnAccBocnetLoginResponse.class);
    }

    /**
     * PsnAccBocnetQryDebitDetail 查询借记卡账户详情
     * added by feibin 20160803
     * @return
     */
    public Observable<PsnAccBocnetQryDebitDetailResult> psnAccBocnetQryDebitDetail (PsnAccBocnetQryDebitDetailParams params) {
        return BIIClient.instance.post("PsnAccBocnetQryDebitDetail", params, PsnAccBocnetQryDebitDetailResponse.class);
    }

    /**
     * PsnAccBocnetQueryGeneralInfo查询信用卡综合信息
     * added by feibin 20160803
     * @return
     */
    public Observable<PsnAccBocnetQueryGeneralInfoResult> psnAccBocnetQueryGeneralInfo (PsnAccBocnetQueryGeneralInfoParams params) {
        return BIIClient.instance.post("PsnAccBocnetQueryGeneralInfo", params, PsnAccBocnetQueryGeneralInfoResponse.class);
    }

    /**
     * PsnAccBocnetQryCrcdPoint查询信用卡积分
     * added by feibin 20160803
     * @return
     */
    public Observable<PsnAccBocnetQryCrcdPointResult> psnAccBocnetQryCrcdPoint (PsnAccBocnetQryCrcdPointParams params) {
        return BIIClient.instance.post("PsnAccBocnetQryCrcdPoint", params, PsnAccBocnetQryCrcdPointResponse.class);
    }

    /**
     * PsnAccBocnetQryLoginInfo 查询卡号登录信息
     * added by feibin 20160803
     * @return
     */
    public Observable<PsnAccBocnetQryLoginInfoResult> psnAccBocnetQryLoginInfo (PsnAccBocnetQryLoginInfoParams params) {
        return BIIClient.instance.post("PsnAccBocnetQryLoginInfo", params, PsnAccBocnetQryLoginInfoResponse.class);
    }

    /**
     * PsnAccBocnetQryLoginInfo 查询卡号登录信息
     * added by feibin 20160803
     * @return
     */
    public Observable<PsnRedirectEzucResult> psnRedirectEzuc (PsnRedirectEzucParams params) {
        return BIIClient.instance.post("PsnRedirectEzuc", params, PsnRedirectEzucResponse.class);
    }

}
