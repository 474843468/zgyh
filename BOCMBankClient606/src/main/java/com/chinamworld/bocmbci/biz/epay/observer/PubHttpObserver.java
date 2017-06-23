package com.chinamworld.bocmbci.biz.epay.observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TQConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.constants.WithoutCardContants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PubHttpObserver implements HttpObserver {
	private HttpObserver observer;
	private Context context;
	private String conversationId;
	
	public static PubHttpObserver getInstance(HttpObserver observer, String type) {
		return new PubHttpObserver(observer, type);
	}
	
	private PubHttpObserver(HttpObserver observer, String type) {
		this.observer = observer;
		if(PubConstants.CONTEXT_BOM.equals(type))
			context = TransContext.getBomContext();
		else if(PubConstants.CONTEXT_WITHOUT_CARD.equals(type))
			context = TransContext.getWithoutCardContext();
		else if(PubConstants.CONTEXT_TQ.equals(type))
			context = TransContext.getTQTransContext();
		else if(PubConstants.CONTEXT_TREATY.equals(type)) 
			context = TransContext.getTreatyTransContext();
		setConversationId(context.getString(PubConstants.PUB_FIELD_CONVERSATION_ID, ""));
	}
	/**
	 * 获取返回数据
	 * @param resultObj
	 * @return
	 */
	public Object getResult(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		return biiResponseBody.getResult();
	}
	/**
	 * 根据账户类型查询用户
	 * @param accType
	 */
	public void req_queryAllAccount(Object accType, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(PubConstants.METHOD_QUERY_ALL_ACCOUNT);
		Map<String, Object> params = null;
		if(!StringUtil.isNullOrEmpty(accType)) {
			params = new HashMap<String, Object>();
			params.put("accountType", accType);
			biiRequestBody.setParams(params);
		}
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询账户详情
	 * @param index
	 */
	public void req_queryAccountDetail(String accountId, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(PubConstants.METHOD_QUERY_ACCOUNT_DETAIL);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询系统预设限额信息
	 */
	public void req_queryMaxQuota(String serviceId, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_QUERY_MAX_QUOTA);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PubConstants.PUB_FIELD_SERVICE_ID, serviceId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询用户自设限额
	 * @param callBackMethod
	 */
	public void req_queryCustMaxQuota(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA);
		biiRequestBody.setConversationId(conversationId);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 开通支付预交易
	 */
	public void req_openServicePre(Map<String, String> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(BomConstants.METHOD_OPEN_SERVICE_PRE);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 获取TokenID
	 * @param callBackMethod
	 */
	public void req_getToken(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_GET_TOKEN_ID);
		biiRequestBody.setConversationId(conversationId);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 开通手机银行支付服务
	 * @param params
	 * @param callBackMethod
	 */
	public void req_openBomPaymentService(Map<String, Object> params,
			String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_OPEN_PAYMENT_SERVICE);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询已开通账户列表
	 */
	public void req_queryDredgedAccountList(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 设置支付账户预交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_setBomPaymentServiceAccPre(Map< Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_SET_PAYMENT_SERVICE_ACC_PRE);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 设置支付限额预交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_setBomPaymentQuotaPre(Map<String, String> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_SET_MAX_QUOTA_PRE);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	/**
	 * 设置支付限额交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_setPaymentQuota(Map<String, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_SET_MAX_QUOTA);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	/**
	 * 设置支付账户
	 * @param params
	 * @param callbackMethod
	 */
	public void req_setBomPaymentServiceAcc(Map<String, Object> params,
			String callbackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_SET_PAYMENT_SERVICE_ACC);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callbackMethod);
	}
	
	/**
	 * 获取ConversationId
	 */
	public void req_getConversationId(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_GET_CONVERSTATION_ID);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 关闭支付服务
	 */
	public void req_closePaymentService(String tokenId, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_CLOSE_PAYMENT_SEVICE);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, tokenId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 关闭支付账户
	 * @param params
	 * @param callBackMethod
	 */
	public void req_closeAccPaymentService(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_CLOSE_ACC_PAYMENT_SERVICE);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 判断是否开通手机支付
	 * @param callBackMethod
	 */
	public void req_bomServiceOpenStatus(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_IS_OPENONLINE_PAYMENT);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 判断是否开通无卡支付
	 * @param accountId
	 * @param callBackMethod
	 */
	public void req_queryWithoutCardPayStatus(String accountId, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(WithoutCardContants.METHOD_IS_OPEN_NC_PAY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_ACCOUNT_ID, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 开通无卡支付预交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_setWCPaymentServiceAccPre(Map<String, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(WithoutCardContants.METHOD_OPEN_NC_PAY_PRE);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 开通无卡支付账户提交交易
	 * @param params
	 * @param string
	 */
	public void req_setWcPaymentServiceAcc(Map<String, Object> params,
			String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(WithoutCardContants.METHOD_OPEN_NC_PAY);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	/**
	 * 设置无卡支付账户余额预交易
	 * @param params
	 * @param string
	 */
	public void req_setWcPaymentQuotaPre(Map<String, String> params,
			String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(WithoutCardContants.METHOD_MODIFY_QUOTA_PRE);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	/**
	 * 获取系统时间
	 * @param callBackMethod
	 */
	public void req_getSystemTime(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(PubConstants.METHOD_GET_SYSTEM_TIME);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 修改无卡支付交易限额
	 * @param params
	 * @param string
	 */
	public void req_setWcPaymentQuota(Map<String, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(WithoutCardContants.METHOD_MODIFY_QUOTA);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 关闭无卡支付账户预交易
	 * @param curAccountId
	 * @param string
	 */
	public void req_closeWcAccountPre(String accountId, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(WithoutCardContants.METHOD_CLOSE_WC_ACC_PRE);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_PRE_FIELD_ACCOUNT_ID, accountId);
		if (EPayBaseActivity.serviceType == 1) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"1");
		} else if (EPayBaseActivity.serviceType == 2) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"2");
		}
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 关闭无卡支付账户提交交易
	 * @param params
	 * @param string
	 */
	public void req_closeWcAccPaymentService(Map<String, String> params,
			String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(WithoutCardContants.METHOD_CLOSE_WC_ACC);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询电子支付交易记录
	 * @param params
	 * @param callBackMethod
	 */
	public void req_queryBOMTransRecord(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询协议支付交易记录
	 * @param params
	 * @param callBackMethod
	 */
	public void req_queryTreatyTransRecord(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	

	/**
	 * 电子支付交易记录
	 * @param params
	 * @param callBackMethod
	 */
	public void req_queryEpayQueryAllTypeRecord(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(TQConstants.METHOD_QUERY_TREATY_TRANS_ALL_RECORD);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	/**
	 * 查询中银快付交易记录
	 * @param params
	 * @param callBackMethod
	 */
	public void req_queryZYTransRecord(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TQConstants.METHOD_QUERY_ZY_TRANS_RECORD);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	/**
	 * 查询无卡支付交易限额
	 * @param callBackMethod
	 */
	public void req_queryWithoutCardQuota(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(WithoutCardContants.METHOD_QUERY_WITHOUTCARD_QUOTA);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	/**
	 * 获取加密随机数
	 * @param callBackMethod
	 */
	public void req_randomKey(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setMethod(PubConstants.METHOD_GET_RANDOM_KEY);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询电子支付交易详情
	 * @param transactionId
	 * @param string
	 */
	public void req_queryBOMTransDetail(String transactionId, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL);
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_TRANSACTION_ID, transactionId);
		params.put("payType", "1");//支付类型 1-电子支付 3-理财支付
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询已签约协议支付商户
	 * @param index
	 * @param isRefresh
	 * @param callBackMethod
	 */
	public void req_queryTreatyRelations(int index, boolean isRefresh, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS);
		biiRequestBody.setConversationId(conversationId);
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX, index);
		params.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH, EpayUtil.getString(isRefresh, "true"));
		params.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, 10);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
//	/**
//	 * 查询已签约协议支付商户详情
//	 * @param index
//	 * @param isRefresh
//	 * @param callBackMethod
//	 */
//	public void req_queryTreatyRelationDetail(String agreementId, String callBackMethod) {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(TreatyConstants.METHOD_QUERY_TREATY_RELATION_DETAIL);
//		biiRequestBody.setConversationId(conversationId);
//		Map<Object, Object> params = new HashMap<Object, Object>();
//		params.put(TreatyConstants.METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_AGREEMENT_SEQ, agreementId);
//		biiRequestBody.setParams(params);
//		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
//	}
	/**
	 * 查询商户
	 * @param index
	 * @param callBackMethod
	 */
	public void req_queryMerchants(int index, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_QUERY_MERCHANTS);
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX, index);
		params.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, 10);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 查询银行端协议支付交易限额
	 * @param string
	 */
	public void req_queryTreatyQuota(String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_QUERY_TREATY_QUOTA);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 签约协议支付商户提交交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_addTreatyMerchant(Map<String, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_ADD_TREATY_MERCHANT);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 签约协议支付商户预交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_addTreatyMerchantPre(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 解约商户预交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_deleteMerchantRelationPre(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_DELETE_RELATIONS_PRE);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 解约商户提交交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_deleteMerchantRelation(Map<String, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_DELETE_RELATIONS);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 修改协议支付交易限额预交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_modifyTreatyMaxQuotaPre(Map<Object, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	/**
	 * 修改协议支付交易限额提交交易
	 * @param params
	 * @param callBackMethod
	 */
	public void req_modifyTreatyMaxQuota(Map<String, Object> params, String callBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(TreatyConstants.METHOD_MODIFY_MAX_QUOTA);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, observer, callBackMethod);
	}
	
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getConversationId() {
		return conversationId;
	}
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		return observer.httpRequestCallBackPre(resultObj);
	}
	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
		return observer.httpRequestCallBackAfter(resultObj);
	}
	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
		return observer.httpCodeErrorCallBackPre(code);
	}
	@Override
	public boolean httpCodeErrorCallBackAfter(String code) {
		return observer.httpCodeErrorCallBackAfter(code);
	}
	@Override
	public void commonHttpErrorCallBack(String code) {
		observer.commonHttpErrorCallBack(code);
	}
	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		observer.commonHttpResponseNullCallBack(requestMethod);
	}
	
}
