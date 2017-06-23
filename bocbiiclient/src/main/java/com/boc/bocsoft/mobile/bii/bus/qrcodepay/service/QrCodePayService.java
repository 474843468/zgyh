package com.boc.bocsoft.mobile.bii.bus.qrcodepay.service;

import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayChangePass.QRPayChangePassParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayChangePass.QRPayChangePassResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayChangePass.QRPayChangePassResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayClosePassFreeService.QRPayClosePassFreeServiceParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayClosePassFreeService.QRPayClosePassFreeServiceResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayClosePassFreeService.QRPayClosePassFreeServiceResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment.QRPayDoPaymentParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment.QRPayDoPaymentResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment.QRPayDoPaymentResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoScannedPayment.QRPayDoScannedPaymentParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoScannedPayment.QRPayDoScannedPaymentResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoScannedPayment.QRPayDoScannedPaymentResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer.QRPayDoTransferParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer.QRPayDoTransferResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer.QRPayDoTransferResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo.QRPayGetConfirmInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo.QRPayGetConfirmInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo.QRPayGetConfirmInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetDefaultCard.QRPayGetDefaultCardParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetDefaultCard.QRPayGetDefaultCardResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetDefaultCard.QRPayGetDefaultCardResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota.QRPayGetPayQuotaParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota.QRPayGetPayQuotaResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota.QRPayGetPayQuotaResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo.QRPayGetPayeeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo.QRPayGetPayeeInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo.QRPayGetPayeeInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayerResult.QRPayGetPayerResultParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayerResult.QRPayGetPayerResultResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayerResult.QRPayGetPayerResultResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetRelativedAcctList.QRPayGetRelativedAcctListParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetRelativedAcctList.QRPayGetRelativedAcctListResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetRelativedAcctList.QRPayGetRelativedAcctListResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo.QRPayGetTransInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo.QRPayGetTransInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo.QRPayGetTransInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord.QRPayGetTransRecordParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord.QRPayGetTransRecordResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord.QRPayGetTransRecordResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayIsPassSet.QRPayIsPassSetParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayIsPassSet.QRPayIsPassSetResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayIsPassSet.QRPayIsPassSetResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeService.QRPayOpenPassFreeServiceParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeService.QRPayOpenPassFreeServiceResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeService.QRPayOpenPassFreeServiceResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre.QRPayOpenPassFreeServicePreParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre.QRPayOpenPassFreeServicePreResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre.QRPayOpenPassFreeServicePreResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetDefaultCard.QRPaySetDefaultCardParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetDefaultCard.QRPaySetDefaultCardResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetDefaultCard.QRPaySetDefaultCardResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPass.QRPaySetPassParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPass.QRPaySetPassResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPass.QRPaySetPassResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre.QRPaySetPassPreParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre.QRPaySetPassPreResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre.QRPaySetPassPreResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceIsOpen.QRServiceIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceIsOpen.QRServiceIsOpenResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceIsOpen.QRServiceIsOpenResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceOpen.QRServiceOpenParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceOpen.QRServiceOpenResponse;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceOpen.QRServiceOpenResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import java.util.List;

import rx.Observable;

/**
 * Created by wangf on 2016/8/29.
 */
public class QrCodePayService {

    /**
     * 获取二维码
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetQRCodeResult> qRPayGetQRCode(QRPayGetQRCodeParams params) {
        return BIIClient.instance.post("QRPayGetQRCode", params, QRPayGetQRCodeResponse.class);
    }

    /**
     * 设置/修改默认卡
     *
     * @param params
     * @return
     */
    public Observable<QRPaySetDefaultCardResult> qRPaySetDefaultCard(QRPaySetDefaultCardParams params) {
        return BIIClient.instance.post("QRPaySetDefaultCard", params, QRPaySetDefaultCardResponse.class);
    }

    /**
     * 查询默认卡
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetDefaultCardResult> qRPayGetDefaultCard(QRPayGetDefaultCardParams params) {
        return BIIClient.instance.post("QRPayGetDefaultCard", params, QRPayGetDefaultCardResponse.class);
    }

    /**
     * 查询是否设置支付密码
     *
     * @param params
     * @return
     */
    public Observable<QRPayIsPassSetResult> qRPayIsPassSet(QRPayIsPassSetParams params) {
        return BIIClient.instance.post("QRPayIsPassSet", params, QRPayIsPassSetResponse.class);
    }

    /**
     * 设置支付密码预交易
     *
     * @param params
     * @return
     */
    public Observable<QRPaySetPassPreResult> qRPaySetPassPre(QRPaySetPassPreParams params) {
        return BIIClient.instance.post("QRPaySetPassPre", params, QRPaySetPassPreResponse.class);
    }

    /**
     * 设置支付密码
     *
     * @param params
     * @return
     */
    public Observable<QRPaySetPassResult> qRPaySetPass(QRPaySetPassParams params) {
        return BIIClient.instance.post("QRPaySetPass", params, QRPaySetPassResponse.class);
    }

//    /**
//     * 修改支付密码预交易
//     *
//     * @param params
//     * @return
//     */
//    public Observable<QRPayChangePassPreResult> qRPayChangePassPre(QRPayChangePassPreParams params) {
//        return BIIClient.instance.post("QRPayChangePassPre", params, QRPayChangePassPreResponse.class);
//    }

    /**
     * 修改支付密码
     *
     * @param params
     * @return
     */
    public Observable<QRPayChangePassResult> qRPayChangePass(QRPayChangePassParams params) {
        return BIIClient.instance.post("QRPayChangePass", params, QRPayChangePassResponse.class);
    }

//    /**
//     * 重置支付密码预交易
//     *
//     * @param params
//     * @return
//     */
//    public Observable<QRPayResetPassPreResult> qRPayResetPassPre(QRPayResetPassPreParams params) {
//        return BIIClient.instance.post("QRPayResetPassPre", params, QRPayResetPassPreResponse.class);
//    }

//    /**
//     * 重置支付密码
//     *
//     * @param params
//     * @return
//     */
//    public Observable<QRPayResetPassResult> qRPayResetPass(QRPayResetPassParams params) {
//        return BIIClient.instance.post("QRPayResetPass", params, QRPayResetPassResponse.class);
//    }

    /**
     * 查询小额免密信息
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetPassFreeInfoResult> qRPayGetPassFreeInfo(QRPayGetPassFreeInfoParams params) {
        return BIIClient.instance.post("QRPayGetPassFreeInfo", params, QRPayGetPassFreeInfoResponse.class);
    }

    /**
     * 开通小额免密服务预交易
     *
     * @param params
     * @return
     */
    public Observable<QRPayOpenPassFreeServicePreResult> qRPayOpenPassFreeServicePre(QRPayOpenPassFreeServicePreParams params) {
        return BIIClient.instance.post("QRPayOpenPassFreeServicePre", params, QRPayOpenPassFreeServicePreResponse.class);
    }

    /**
     * 开通小额免密服务提交交易
     *
     * @param params
     * @return
     */
    public Observable<QRPayOpenPassFreeServiceResult> qRPayOpenPassFreeService(QRPayOpenPassFreeServiceParams params) {
        return BIIClient.instance.post("QRPayOpenPassFreeService", params, QRPayOpenPassFreeServiceResponse.class);
    }

    /**
     * 关闭小额免密服务
     *
     * @param params
     * @return
     */
    public Observable<QRPayClosePassFreeServiceResult> qRPayClosePassFreeService(QRPayClosePassFreeServiceParams params) {
        return BIIClient.instance.post("QRPayClosePassFreeService", params, QRPayClosePassFreeServiceResponse.class);
    }

    /**
     * 查询支付限额
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetPayQuotaResult> qRPayGetPayQuota(QRPayGetPayQuotaParams params) {
        return BIIClient.instance.post("QRPayGetPayQuota", params, QRPayGetPayQuotaResponse.class);
    }

    /**
     * 查询反扫后的交易确认通知
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetConfirmInfoResult> qRPayGetConfirmInfo(QRPayGetConfirmInfoParams params) {
        return BIIClient.instance.post("QRPayGetConfirmInfo", params, QRPayGetConfirmInfoResponse.class);
    }

    /**
     * 查询反扫支付交易信息
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetTransInfoResult> qRPayGetTransInfo(QRPayGetTransInfoParams params) {
        return BIIClient.instance.post("QRPayGetTransInfo", params, QRPayGetTransInfoResponse.class);
    }

    /**
     * 反扫支付
     *
     * @param params
     * @return
     */
    public Observable<QRPayDoScannedPaymentResult> qRPayDoScannedPayment(QRPayDoScannedPaymentParams params) {
        return BIIClient.instance.post("QRPayDoScannedPayment", params, QRPayDoScannedPaymentResponse.class);
    }

    /**
     * 正扫支付
     *
     * @param params
     * @return
     */
    public Observable<QRPayDoPaymentResult> qRPayDoPayment(QRPayDoPaymentParams params) {
        return BIIClient.instance.post("QRPayDoPayment", params, QRPayDoPaymentResponse.class);
    }

    /**
     * 交易记录查询
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetTransRecordResult> qRPayGetTransRecord(QRPayGetTransRecordParams params) {
        return BIIClient.instance.post("QRPayGetTransRecord", params, QRPayGetTransRecordResponse.class);
    }

    /**
     * 开通二维码服务
     *
     * @param params
     * @return
     */
    public Observable<QRServiceOpenResult> qRServiceOpen(QRServiceOpenParams params) {
        return BIIClient.instance.post("QRServiceOpen", params, QRServiceOpenResponse.class);
    }

    /**
     * 查询客户是否开通二维码服务
     *
     * @param params
     * @return
     */
    public Observable<QRServiceIsOpenResult> qRServiceIsOpen(QRServiceIsOpenParams params) {
        return BIIClient.instance.post("QRServiceIsOpen", params, QRServiceIsOpenResponse.class);
    }

    /**
     * 收款结果查询
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetPayeeResultResult> qRPayGetPayeeResult(QRPayGetPayeeResultParams params) {
        return BIIClient.instance.post("QRPayGetPayeeResult", params, QRPayGetPayeeResultResponse.class);
    }

    /**
     * 转账交易（新增）
     *
     * @param params
     * @return
     */
    public Observable<QRPayDoTransferResult> qRPayDoTransfer(QRPayDoTransferParams params) {
        return BIIClient.instance.post("QRPayDoTransfer", params, QRPayDoTransferResponse.class);
    }

    /**
     * 查询收款人信息（新增）
     * @param params
     * @return
     */
    public Observable<QRPayGetPayeeInfoResult> qRPayGetPayeeInfo(QRPayGetPayeeInfoParams params){
        return BIIClient.instance.post("QRPayGetPayeeInfo",params, QRPayGetPayeeInfoResponse.class);
    }
    /**
     *  C2C付款结果查询（新增）
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetPayerResultResult> qRPayGetPayerResult(QRPayGetPayerResultParams params) {
        return BIIClient.instance.post("QRPayGetPayerResult", params, QRPayGetPayerResultResponse.class);
    }

    /**
     *  查询账户类别（新增）
     *
     * @param params
     * @return
     */
    public Observable<QRPayGetAccountCatalogResult> qRPayGetAccountCatalog(QRPayGetAccountCatalogParams params) {
        return BIIClient.instance.post("QRPayGetAccountCatalog", params, QRPayGetAccountCatalogResponse.class);
    }

    /**
     * 查询关联账户中的银联账户列表
     *
     * @param params
     */
    public Observable<List<QRPayGetRelativedAcctListResult>> qRPayGetRelativedAcctList(QRPayGetRelativedAcctListParams params) {
        return BIIClient.instance.post("QRPayGetRelativedAcctList", params, QRPayGetRelativedAcctListResponse.class);
    }

}
