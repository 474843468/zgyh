package com.boc.bocma.serviceinterface.op.business.financemanager;

import android.content.Context;

import com.boc.bocma.exception.MAOPException;
import com.boc.bocma.serviceinterface.op.MAOPBaseInterface;
import com.boc.bocma.serviceinterface.op.OnOPResultCallback;
import com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount.MAOPApplyOnlineOpenAccountParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount.MAOPApplyOnlineOpenAccountResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.bindcardwithoutcheck.MAOPBindCardWithoutCheckModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.bindcardwithoutcheck.MAOPBindCardWithoutCheckParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.cashorder.MAOPCashOrderModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.cashorder.MAOPCashOrderParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.checkbindcardmsg.MAOPCheckBindCardMsgModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.checkbindcardmsg.MAOPCheckBindCardMsgParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity.MAOPCheckPersonIdentityParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity.MAOPCheckPersonIdentityResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.creditcardbalance.MAOPCreditCardBalanceModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.creditcardbalance.MAOPCreditCardBalanceParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.debitcardbalance.MAOPDebitCardBalanceModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.debitcardbalance.MAOPDebitCardBalanceParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.debitcardtranshistory.MAOPDebitCardTransHistoryModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.debitcardtranshistory.MAOPDebitCardTransHistoryParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.encryptquery.MAOPEncryptQueryModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.encryptquery.MAOPEncryptQueryParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.findversion.MAOPFindVersionModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.findversion.MAOPFindVersionParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num.MAOPGetRandomNumParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num.MAOPGetRandomNumResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.keywordupload.MAOPKeywordUploadModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.keywordupload.MAOPKeywordUploadParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.notbillquery.MAOPNotBillQueryModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.notbillquery.MAOPNotBillQueryParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.privateclientinfoquery.MAOPPrivateClientInfoQueryModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.privateclientinfoquery.MAOPPrivateClientInfoQueryParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryboundcardlist.MAOPQueryBoundCardListModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryboundcardlist.MAOPQueryBoundCardListParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querydebitcard.MAOPQueryDebitCardModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querydebitcard.MAOPQueryDebitCardParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccattr.MAOPQueryElecAccAttrParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccattr.MAOPQueryElecAccAttrResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank.MAOPQueryElecAccOpeningBankParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank.MAOPQueryElecAccOpeningBankResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querykeywords.MAOPQueryKeywordsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querykeywords.MAOPQueryKeywordsParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querynearbybranches.MAOPQueryNearbyBranchesModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querynearbybranches.MAOPQueryNearbyBranchesParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress.MAOPQueryOpenAccountProgressParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress.MAOPQueryOpenAccountProgressResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopeningbank.MAOPQueryOpeningBankParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopeningbank.MAOPQueryOpeningBankResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queuequery.MAOPQueueQueryModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queuequery.MAOPQueueQueryParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode.MAOPSendMobileIdentifyCodeParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode.MAOPSendMobileIdentifyCodeResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.sendmsgonbindcard.MAOPSendMsgOnBindCardModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.sendmsgonbindcard.MAOPSendMsgOnBindCardParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.uploadapptoken.MAOPUploadAppTokenModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.uploadapptoken.MAOPUploadAppTokenParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.uploadidcard.MAOPUploadidcardParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.uploadidcard.MAOPUploadidcardResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.verifycardnumber.MAOPVerifyCardNumberModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.verifycardnumber.MAOPVerifyCardNumberParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.yetbillquery.MAOPYetBillQueryModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.yetbillquery.MAOPYetBillQueryParamsModel;

/**
 * 财务管家所有接口调用暂时都通过该类调用
 *
 */
public class MAOPFinanceManagerInterface extends MAOPBaseInterface {

    public MAOPFinanceManagerInterface(Context context) {
        super(context);
    }
    
    private static MAOPFinanceManagerInterface sInstance = null;
    
    public static synchronized MAOPFinanceManagerInterface getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MAOPFinanceManagerInterface(context);
        }
        return sInstance;
    }
    
    /**
     * 根据UserId获取已绑定卡信息
     */
    public void getBoundCardList(MAOPQueryBoundCardListParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPQueryBoundCardListModel.class, callback);
    }
    
    /**
     * 根据UserId获取已绑定卡信息
     */
    public MAOPQueryBoundCardListModel getBoundCardListSynchronous(MAOPQueryBoundCardListParamsModel params) throws MAOPException {
        return executeSynchronous(params, new MAOPQueryBoundCardListModel());
    }
    
    /**
     * 根据userid获取所有借记卡列表
     */
    public void getDebitCardList(MAOPQueryDebitCardParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPQueryDebitCardModel.class, callback);
    }

    /**
     * 根据userid获取所有借记卡列表
     * @throws MAOPException 
     */
    public MAOPQueryDebitCardModel getDebitCardListSynchronous(MAOPQueryDebitCardParamsModel params) throws MAOPException {
        return executeSynchronous(params, new MAOPQueryDebitCardModel());
    }
    
    /**
     * 新增用户卡资料（无需验证）
     */
    public void bindNewCardWithoutCheck(MAOPBindCardWithoutCheckParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPBindCardWithoutCheckModel.class, callback);
    }
    
    /**
     * 新增用户卡资料（无需验证）
     */
    public MAOPBindCardWithoutCheckModel bindNewCardWithoutCheckSynchronous(MAOPBindCardWithoutCheckParamsModel params) throws MAOPException {
        return executeSynchronous(params, new MAOPBindCardWithoutCheckModel());
    }
    
    /**
     * 验证卡号 
     */
    public void verifyCardNumber(MAOPVerifyCardNumberParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPVerifyCardNumberModel.class, callback);
    }
    
    /**
     * 对私客户信息查询
     */
    public void privateClientInfoQuery(MAOPPrivateClientInfoQueryParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPPrivateClientInfoQueryModel.class, callback);
    }
    
    /**
     * 增加卡时发送短信验证码
     * @param params
     * @param callback
     */
    public void sendCheckMsgOnBindCard(MAOPSendMsgOnBindCardParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPSendMsgOnBindCardModel.class, callback);
    }
    
    /**
     * 验证增加卡的短信验证码
     * @param params
     * @param callback
     */
    public void checkBindCardMsg(MAOPCheckBindCardMsgParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPCheckBindCardMsgModel.class, callback);
    }
    
    /**
     * 批量查询卡余额
     */
    public void batchQueryDebitCardBalance(MAOPDebitCardBalanceParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPDebitCardBalanceModel.class, callback);
    }
    
    public MAOPDebitCardBalanceModel batchQueryDebitCardBalanceSynchronous(MAOPDebitCardBalanceParamsModel params) 
            throws MAOPException {
        return executeSynchronous(params, new MAOPDebitCardBalanceModel());
    }
    /**
     * 查询信用卡卡余额
     */
    public MAOPCreditCardBalanceModel batchQueryCreditCardBalanceSynchronous(MAOPCreditCardBalanceParamsModel params) 
            throws MAOPException {
        return executeSynchronous(params, new MAOPCreditCardBalanceModel());
    }
    
    /**
     * 批量查询卡交易流水
     */
    public void batchQueryDebitCardTransHistory(MAOPDebitCardTransHistoryParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPDebitCardTransHistoryModel.class, callback);
    }
    
    public MAOPDebitCardTransHistoryModel batchQueryDebitCardTransHistorySynchronous(MAOPDebitCardTransHistoryParamsModel params) throws MAOPException {
        return executeSynchronous(params, new MAOPDebitCardTransHistoryModel());
    }
    
    /**
     * 已出账单查询
     */
    public void yetBillQuery(MAOPYetBillQueryParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPYetBillQueryModel.class, callback);
    }
    
    public MAOPYetBillQueryModel yetBillQuerySynchronous(MAOPYetBillQueryParamsModel params) throws MAOPException {
        return executeSynchronous(params, new MAOPYetBillQueryModel());
    }
    
    /**
     * 未出账单查询
     */
    public void notBillQuery(MAOPNotBillQueryParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPNotBillQueryModel.class, callback);
    }
    
    public MAOPNotBillQueryModel notBillQuerySynchronous(MAOPNotBillQueryParamsModel params) throws MAOPException {
        return executeSynchronous(params, new MAOPNotBillQueryModel());
    }
    
    /**
     * 新增用户卡资料（无需验证）
     */
    public void bindCardWithoutCheck(MAOPBindCardWithoutCheckParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPBindCardWithoutCheckModel.class, callback);
    }
    
    /**
     * 关键字查询
     */
    public void keywordsQuery(MAOPQueryKeywordsParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPQueryKeywordsModel.class, callback);
    }
    
    /**
     * 关键字查询
     */
    public MAOPQueryKeywordsModel keywordQuerySynchronous(MAOPQueryKeywordsParamsModel params) 
            throws MAOPException {
        return executeSynchronous(params, new MAOPQueryKeywordsModel());
    }
    
    /**
     * 关键字上送
     */
    public void keywordUpload(MAOPKeywordUploadParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPKeywordUploadModel.class, callback);
    }
    
    /**
     * 根据用户ID生成128位长度的字符串
     */
    public void encryptQuery(MAOPEncryptQueryParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPEncryptQueryModel.class, callback);
    }
    
    /**
     * 检查版本 
     */
    public void findVersion(MAOPFindVersionParamsModel params,OnOPResultCallback callback){
    	execute(params, MAOPFindVersionModel.class, callback);
    }

    /**
     * 查询附近网点
     */
    public void queryNearbyBranches(MAOPQueryNearbyBranchesParamsModel params, OnOPResultCallback callback){
        execute(params, MAOPQueryNearbyBranchesModel.class, callback);
    }
    
    /**
     * 现钞预约（大额预约和外币兑换）
     */
    public void cashOrder(MAOPCashOrderParamsModel params, OnOPResultCallback callback) {
        execute(params, MAOPCashOrderModel.class, callback);
    }
    
    /**
     * 预约取号-排队情况查询
     */
    public void queueQuery(MAOPQueueQueryParamsModel params,  OnOPResultCallback callback) {
        execute(params, MAOPQueueQueryModel.class, callback);
    }
    
    /**
     * FA0019上传通过容器登陆获取到的token
     * @param params
     * @param callback
     */
    public void apptokenQuerySynchronous(MAOPUploadAppTokenParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPUploadAppTokenModel.class, callback);
    }
    
    /*************************远程开户相关*******************************/
    /**
     * 开户前校验接口
     */
    public void checkpersonidentity(MAOPCheckPersonIdentityParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPCheckPersonIdentityResponseModel.class, callback);
    }
    
    /**
     * 开户行模糊查询
     */
    public void queryopeningbank(MAOPQueryOpeningBankParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPQueryOpeningBankResponseModel.class, callback);    	
    }
    
    /**
     * 电子账户归属地查询
     */
    public void queryelecaccattr(MAOPQueryElecAccAttrParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPQueryElecAccAttrResponseModel.class, callback); 
    	
    }
    
    /**
     * 电子账户开户行查询
     */
    public void queryelecaccopeningbank(MAOPQueryElecAccOpeningBankParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPQueryElecAccOpeningBankResponseModel.class, callback); 	
    }
    
    /**
     * 在线开户申请
     */
    public void applyonlineopenaccount(MAOPApplyOnlineOpenAccountParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPApplyOnlineOpenAccountResponseModel.class, callback); 
    	
    }
    
    /**
     * 查询开户进度
     */
    public void queryopenaccountprogress(MAOPQueryOpenAccountProgressParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPQueryOpenAccountProgressResponseModel.class, callback); 
    	
    }
    
    /**
     * 发送手机验证码
     */
    public void sendmobileidentifycode(MAOPSendMobileIdentifyCodeParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPSendMobileIdentifyCodeResponseModel.class, callback);
    	
    }
    
    /**
     * 生成服务器随机数
     */
    public void get_random_num(MAOPGetRandomNumParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPGetRandomNumResponseModel.class, callback);
    	
    }
    /**
     * 身份证图像上传
     */
    public void uploadidcard(MAOPUploadidcardParamsModel params, OnOPResultCallback callback){
    	execute(params,MAOPUploadidcardResponseModel.class, callback);
    	
    }
    
    
    		
}
