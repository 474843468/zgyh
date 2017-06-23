package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayerResult;

/**
 * 不区分当日交易和历史交易，统一入口查询。60s后开始轮询。
 * 前端可主动发起对被扫二维码的确认查询，反扫后ICCD拦截交易，
 * 等待网银验证客户的结果，前端通过此接口主动查询是否开始对客户进行验证。
 * Created by fanbin on 16/10/8.
 */
public class QRPayGetPayerResultParams {
     private String conversationId;
}
