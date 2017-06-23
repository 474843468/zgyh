package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils;

import android.support.annotation.NonNull;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayChangePass.QRPayChangePassParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment.QRPayDoPaymentParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment.QRPayDoPaymentResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo.QRPayGetConfirmInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo.QRPayGetConfirmInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota.QRPayGetPayQuotaResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo.QRPayGetPayeeInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetRelativedAcctList.QRPayGetRelativedAcctListResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo.QRPayGetTransInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord.QRPayGetTransRecordParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord.QRPayGetTransRecordResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeService.QRPayOpenPassFreeServiceParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre.QRPayOpenPassFreeServicePreParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre.QRPayOpenPassFreeServicePreResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPass.QRPaySetPassParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre.QRPaySetPassPreParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre.QRPaySetPassPreResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.model.QRPayGetPayeeInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayTransInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.model.QRPayPaymentRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRPayScanPaymentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PassFreeInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PayQuotaViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayChangePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayFreePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPaySetPwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView.FreePassViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口Result转换界面Model/Model转换数据
 * Created by wangf on 2016/8/29.
 */
public class QrCodeModelUtil {


    /**
     * 获取二维码接口 -- 返回数据 -- 生成View层二维码Model
     *
     * @param qrCodeResult
     * @return
     */
    public static QRPayGetQRCodeViewModel generateGetQrCodeViewModel(QRPayGetQRCodeResult qrCodeResult) {
        QRPayGetQRCodeViewModel qrCodeViewModel = new QRPayGetQRCodeViewModel();
        qrCodeViewModel.setSeqNo(qrCodeResult.getSeqNo());
        qrCodeViewModel.setLifeTime(qrCodeResult.getLifeTime());
        qrCodeViewModel.setGetConfirmInfoFreq(qrCodeResult.getGetConfirmInfoFreq());
        return qrCodeViewModel;
    }


    /**
     * 查询反扫后的交易确认通知 -- 返回数据 -- 生成View层交易确认信息Model
     * @param infoResult
     * @return
     */
    public static QRPayGetQRCodeViewModel generateGetConfirmInfoViewModel(QRPayGetConfirmInfoResult infoResult, QRPayGetConfirmInfoParams params){
        QRPayGetQRCodeViewModel viewModel = new QRPayGetQRCodeViewModel();
        viewModel.setMerchantNo(infoResult.getMerchantNo());
        viewModel.setMerchantName(infoResult.getMerchantName());
        viewModel.setMerchantType(infoResult.getMerchantType());
        viewModel.setAmount(infoResult.getAmount());
        viewModel.setSettleKey(infoResult.getSettleKey());
        viewModel.setResStatus(infoResult.getResStatus());
        viewModel.setConfirmInfoConversationID(params.getConversationId());
        return viewModel;
    }


    /**
     * 查询反扫支付交易信息 -- 返回数据 -- 生成View层交易信息Model
     * @param infoResult
     * @return
     */
    public static QRPayTransInfoViewModel generateGetTransInfoViewModel(QRPayGetTransInfoResult infoResult){
        QRPayTransInfoViewModel viewModel = new QRPayTransInfoViewModel();
        viewModel.setTranTime(infoResult.getTranTime());
        viewModel.setTranStatus(infoResult.getTranStatus());
        viewModel.setErrCode(infoResult.getErrCode());
        viewModel.setErrMsg(infoResult.getErrMsg());
        viewModel.setTranAmount(infoResult.getTranAmount());
        viewModel.setVoucherNum(infoResult.getVoucherNum());
        viewModel.setTranRemark(infoResult.getTranRemark());
        return viewModel;
    }



    /**
     * 获取安全因子接口 -- 返回数据 -- 生成View层安全因子Model
     *
     * @param psnGetSecurityFactorResult
     * @return
     */
    public static SecurityViewModel generateSecurityFactorViewModel(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
        SecurityViewModel viewModel = new SecurityViewModel();

        if (psnGetSecurityFactorResult.get_defaultCombin() != null) {
            CombinBean defaultCombin = new CombinBean();
            defaultCombin.setName(psnGetSecurityFactorResult.get_defaultCombin().getName());
            defaultCombin.setId(psnGetSecurityFactorResult.get_defaultCombin().getId());
            viewModel.set_defaultCombin(defaultCombin);
        }

        List<CombinBean> list = new ArrayList<CombinBean>();
        for (CombinListBean item : psnGetSecurityFactorResult.get_combinList()) {
            CombinBean bean = new CombinBean();
            bean.setId(item.getId());
            bean.setName(item.getName());
            list.add(bean);
        }
        viewModel.set_combinList(list);

        return viewModel;
    }


    /**
     * 设置支付密码预交易接口 -- 上送数据 -- 构建BII层上送参数
     * @param pwdViewModel
     * @return
     */
    public static QRPaySetPassPreParams buildSetPayPwdPreBiiParams(QRPaySetPwdViewModel pwdViewModel){
        QRPaySetPassPreParams passPreParams = new QRPaySetPassPreParams();
        passPreParams.set_combinId(pwdViewModel.get_combinId());
        passPreParams.setPassword(pwdViewModel.getPassword());
        passPreParams.setPassword_RC(pwdViewModel.getPassword_RC());
        passPreParams.setPasswordConform(pwdViewModel.getPasswordConform());
        passPreParams.setPasswordConform_RC(pwdViewModel.getPasswordConform_RC());
        
        passPreParams.setState(pwdViewModel.getState());
        passPreParams.setActiv(pwdViewModel.getActiv());
        
        return passPreParams;
    }


    /**
     * 设置支付密码预交易接口 -- 返回数据 -- 生成View层预交易Model
     *
     * @param qrPaySetPassPreResult
     * @return
     */
    public static QRPaySetPwdViewModel generateSetPwdPreViewModel(QRPaySetPassPreResult qrPaySetPassPreResult) {
        QRPaySetPwdViewModel viewModel = new QRPaySetPwdViewModel();
        viewModel.set_certDN(qrPaySetPassPreResult.get_certDN());
        viewModel.set_plainData(qrPaySetPassPreResult.get_plainData());
        viewModel.setSmcTrigerInterval(qrPaySetPassPreResult.getSmcTrigerInterval());

        List<FactorBean> list = new ArrayList<FactorBean>();
        if (qrPaySetPassPreResult.getFactorList() != null) {
            for (QRPaySetPassPreResult.FactorListBean item : qrPaySetPassPreResult.getFactorList()) {
                FactorBean factorBean = new FactorBean();
                FactorBean.FieldBean bean = new FactorBean.FieldBean();
                bean.setName(item.getField().getName());
                bean.setType(item.getField().getType());
                factorBean.setField(bean);
                list.add(factorBean);
            }
        }
        viewModel.setFactorList(list);
        return viewModel;
    }


    /**
     * 设置支付密码提交交易接口 -- 上送数据 -- 构建BII层上送参数
     *
     * @param viewModel
     * @return
     */
    public static QRPaySetPassParams buildSetPayPwdBiiParams(QRPaySetPwdViewModel viewModel) {
        QRPaySetPassParams setPassParams = new QRPaySetPassParams();
        setPassParams.setSmc(viewModel.getSmc());
        setPassParams.setSmc_RC(viewModel.getSmc_RC());
        setPassParams.setOtp(viewModel.getOtp());
        setPassParams.setOtp_RC(viewModel.getOtp_RC());
        setPassParams.set_signedData(viewModel.get_signedData());
        setPassParams.setDeviceInfo(viewModel.getDeviceInfo());
        setPassParams.setDeviceInfo_RC(viewModel.getDeviceInfo_RC());

        setPassParams.setPassword(viewModel.getPassword());
        setPassParams.setPassword_RC(viewModel.getPassword_RC());
        setPassParams.setPasswordConform(viewModel.getPasswordConform());
        setPassParams.setPasswordConform_RC(viewModel.getPasswordConform_RC());
        setPassParams.setPassType(viewModel.getPassType());

        setPassParams.setState(viewModel.getState());
        setPassParams.setActiv(viewModel.getActiv());

        return setPassParams;
    }


    /**
     * 修改支付密码接口 -- 上送数据 -- 构建BII层上送参数
     * @param pwdViewModel
     * @return
     */
    public static QRPayChangePassParams buildChangePayPwdBiiParams(QRPayChangePwdViewModel pwdViewModel){
        QRPayChangePassParams passParams = new QRPayChangePassParams();
        passParams.setOldPass(pwdViewModel.getOldPass());
        passParams.setOldPass_RC(pwdViewModel.getOldPass_RC());
        passParams.setNewPass(pwdViewModel.getNewPass());
        passParams.setNewPass_RC(pwdViewModel.getNewPass_RC());
        passParams.setNewPass2(pwdViewModel.getNewPass2());
        passParams.setNewPass2_RC(pwdViewModel.getNewPass2_RC());
        passParams.setPassType(pwdViewModel.getPassType());

        passParams.setState(pwdViewModel.getState());
        passParams.setActiv(pwdViewModel.getActiv());

        return passParams;
    }


//    /**
//     * 重置支付密码预交易接口 -- 上送数据 -- 构建BII层上送参数
//     * @param pwdViewModel
//     * @return
//     */
//    public static QRPayResetPassPreParams buildResetPayPwdPreBiiParams(QRPayResetPwdViewModel pwdViewModel){
//        QRPayResetPassPreParams passPreParams = new QRPayResetPassPreParams();
//        passPreParams.set_combinId(pwdViewModel.get_combinId());
//
//        return passPreParams;
//    }
//
//
//    /**
//     * 重置支付密码预交易接口 -- 返回数据 -- 生成View层预交易Model
//     *
//     * @param qrPayResetPassPreResult
//     * @return
//     */
//    public static QRPayResetPwdViewModel generateResetPwdPreViewModel(QRPayResetPassPreResult qrPayResetPassPreResult) {
//        QRPayResetPwdViewModel viewModel = new QRPayResetPwdViewModel();
//        viewModel.set_certDN(qrPayResetPassPreResult.get_certDN());
//        viewModel.set_plainData(qrPayResetPassPreResult.get_plainData());
//        viewModel.setSmcTrigerInterval(qrPayResetPassPreResult.getSmcTrigerInterval());
//
//        List<FactorBean> list = new ArrayList<FactorBean>();
//        if (qrPayResetPassPreResult.getFactorList() != null) {
//            for (QRPayResetPassPreResult.FactorListBean item : qrPayResetPassPreResult.getFactorList()) {
//                FactorBean factorBean = new FactorBean();
//                FactorBean.FieldBean bean = new FactorBean.FieldBean();
//                bean.setName(item.getField().getName());
//                bean.setType(item.getField().getType());
//                factorBean.setField(bean);
//                list.add(factorBean);
//            }
//        }
//        viewModel.setFactorList(list);
//        return viewModel;
//    }
//
//
//    /**
//     * 重置支付密码提交交易接口 -- 上送数据 -- 构建BII层上送参数
//     *
//     * @param viewModel
//     * @return
//     */
//    public static QRPayResetPassParams buildResetPayPwdBiiParams(QRPayResetPwdViewModel viewModel) {
//        QRPayResetPassParams resetPassParams = new QRPayResetPassParams();
//        resetPassParams.setSmc(viewModel.getSmc());
//        resetPassParams.setSmc_RC(viewModel.getSmc_RC());
//        resetPassParams.setOtp(viewModel.getOtp());
//        resetPassParams.setOtp_RC(viewModel.getOtp_RC());
//        resetPassParams.set_signedData(viewModel.get_signedData());
//
//        resetPassParams.setPassword(viewModel.getPassword());
//        resetPassParams.setPassword_RC(viewModel.getPassword_RC());
//        resetPassParams.setPasswordConform(viewModel.getPasswordConform());
//        resetPassParams.setPasswordConform_RC(viewModel.getPasswordConform_RC());
//        resetPassParams.setPassType(viewModel.getPassType());
//
//        resetPassParams.setState(viewModel.getState());
//        resetPassParams.setActiv(viewModel.getActiv());
//
//        return resetPassParams;
//    }



    /**
     * 正扫支付接口 -- 上送数据 -- 构建BII层上送参数
     *
     * @param paymentViewModel
     * @return
     */
    public static QRPayDoPaymentParams buildDoPaymentBiiParams(QRPayScanPaymentViewModel paymentViewModel) {
        QRPayDoPaymentParams doPaymentParams = new QRPayDoPaymentParams();
        doPaymentParams.setActSeq(paymentViewModel.getActSeq());
        doPaymentParams.setMerchantNo(paymentViewModel.getMerchantNo());
        doPaymentParams.setMerchantName(paymentViewModel.getMerchantName());
        doPaymentParams.setTerminalId(paymentViewModel.getTerminalId());
        doPaymentParams.setPassword(paymentViewModel.getPassword());
        doPaymentParams.setPassword_RC(paymentViewModel.getPassword_RC());
        doPaymentParams.setTranAmount(paymentViewModel.getTranAmount());
        doPaymentParams.setPassType(paymentViewModel.getPassType());
        doPaymentParams.setState(paymentViewModel.getState());
        doPaymentParams.setActiv(paymentViewModel.getActiv());
        return doPaymentParams;
    }


    /**
     * 正扫支付接口 -- 返回数据 -- 生成View层支付结果Model
     *
     * @param doPaymentResult
     * @return
     */
    public static QRPayScanPaymentViewModel generateDoPaymentViewModel(QRPayDoPaymentResult doPaymentResult) {
        QRPayScanPaymentViewModel paymentViewModel = new QRPayScanPaymentViewModel();
        paymentViewModel.setCurCode(doPaymentResult.getCurCode());
        paymentViewModel.setErrCode(doPaymentResult.getErrCode());
        paymentViewModel.setErrMsg(doPaymentResult.getErrMsg());
        paymentViewModel.setMerchantCatNo(doPaymentResult.getMerchantCatNo());
        paymentViewModel.setMerchantName(doPaymentResult.getMerchantName());
        paymentViewModel.setMerchantNo(doPaymentResult.getMerchantNo());
        paymentViewModel.setSettleKey(doPaymentResult.getSettleKey());
        paymentViewModel.setStatus(doPaymentResult.getStatus());
        paymentViewModel.setTranAmount(doPaymentResult.getTranAmount());
        paymentViewModel.setVoucherNo(doPaymentResult.getVoucherNo());
        paymentViewModel.setTranTime(doPaymentResult.getTranTime());
        paymentViewModel.setTranSeq(doPaymentResult.getTranSeq());
        return paymentViewModel;
    }


    /**
     * 交易记录查询 -- 上送数据 -- 构建BII层上送参数
     * @param paymentViewModel
     * @return
     */
    public static QRPayGetTransRecordParams buildPaymentRecordBiiParams(QRPayPaymentRecordViewModel paymentViewModel) {
        QRPayGetTransRecordParams transRecordParams = new QRPayGetTransRecordParams();
        transRecordParams.set_refresh(paymentViewModel.get_refresh());
        transRecordParams.setType(paymentViewModel.getType());
        transRecordParams.setActSeq(paymentViewModel.getActSeq());
        transRecordParams.setStartDate(paymentViewModel.getStartDate());
        transRecordParams.setEndDate(paymentViewModel.getEndDate());
        transRecordParams.setPageSize(paymentViewModel.getPageSize());
        transRecordParams.setCurrentIndex(paymentViewModel.getCurrentIndex());
        transRecordParams.setTranStatus(paymentViewModel.getTranStatus());

        return transRecordParams;
    }


    /**
     * 交易记录查询 -- 返回数据 -- 生成View层交易记录Model
     * @param recordResult
     * @return
     */
    public static QRPayPaymentRecordViewModel generatePaymentRecordViewModel(QRPayGetTransRecordResult recordResult) {
        QRPayPaymentRecordViewModel recordViewModel = new QRPayPaymentRecordViewModel();
        recordViewModel.setRecordNumber(recordResult.getRecordNumber());

        List<QRPayPaymentRecordViewModel.ListBean> listBeanList = new ArrayList<QRPayPaymentRecordViewModel.ListBean>();
        for (int i = 0; i < recordResult.getList().size(); i++){
            QRPayPaymentRecordViewModel.ListBean listBean = new QRPayPaymentRecordViewModel.ListBean();
            QRPayGetTransRecordResult.ListBean item = recordResult.getList().get(i);

            listBean.setType(item.getType());
            listBean.setTransferType(item.getTransferType());
            listBean.setTranSeq(item.getTranSeq());
            listBean.setTranTime(item.getTranTime());
            listBean.setTranStatus(item.getTranStatus());
            listBean.setTranAmount(item.getTranAmount());
            listBean.setMerchantNo(item.getMerchantNo());
            listBean.setMerchantName(item.getMerchantName());
            listBean.setMerchantCatNo(item.getMerchantCatNo());
            listBean.setTranRemark(item.getTranRemark());
            listBean.setPayerAccNo(item.getPayerAccNo());
            listBean.setPayerName(item.getPayerName());
            listBean.setPayerComments(item.getPayerComments());
            listBean.setVoucherNum(item.getVoucherNum());
            listBean.setPayeeAccNo(item.getPayeeAccNo());
            listBean.setPayeeName(item.getPayeeName());
            listBean.setPayeeComments(item.getPayeeComments());

            listBeanList.add(listBean);
        }
        recordViewModel.setList(listBeanList);
        return recordViewModel;
    }


    /**
     * 查询支付限额接口 -- 返回数据 -- 生成View层支付限额Model
     *
     * @param payQuotaResult
     * @return
     */
    public static PayQuotaViewModel generatePayQuotaViewModel(QRPayGetPayQuotaResult payQuotaResult) {
        PayQuotaViewModel quotaViewModel = new PayQuotaViewModel();
        quotaViewModel.setCreditCardTransQuota(payQuotaResult.getCreditCardTransQuota());
        quotaViewModel.setCreditCardDailyQuota(payQuotaResult.getCreditCardDailyQuota());
        quotaViewModel.setCreditCardMonthlyQuota(payQuotaResult.getCreditCardMonthlyQuota());
        quotaViewModel.setDebitCardTransQuota(payQuotaResult.getDebitCardTransQuota());
        quotaViewModel.setDebitCardDailyQuota(payQuotaResult.getDebitCardDailyQuota());
        quotaViewModel.setDebitCardMonthlyQuota(payQuotaResult.getDebitCardMonthlyQuota());

        //606
        quotaViewModel.setCardTransQuota(payQuotaResult.getCardTransQuota());
        quotaViewModel.setCardPayQuota(payQuotaResult.getCardPayQuota());
        quotaViewModel.setCardTransferQuota(payQuotaResult.getCardTransferQuota());

        return quotaViewModel;
    }


    /**
     * 查询小额免密信息接口 -- 返回数据 -- 生成View层小额免密信息Model
     *
     * @param freeInfoResult
     * @return
     */
    public static PassFreeInfoViewModel generatePassFreeInfoViewModel(QRPayGetPassFreeInfoResult freeInfoResult) {
        PassFreeInfoViewModel freeInfoViewModel = new PassFreeInfoViewModel();
        freeInfoViewModel.setCreditCardFlag(freeInfoResult.getCreditCardFlag());
        freeInfoViewModel.setDebitCardFlag(freeInfoResult.getDebitCardFlag());
        freeInfoViewModel.setCreditCardPassFreeAmount(freeInfoResult.getCreditCardPassFreeAmount());
        freeInfoViewModel.setDebitCardPassFreeAmount(freeInfoResult.getDebitCardPassFreeAmount());

        //606
        freeInfoViewModel.setPassFreeFlag(freeInfoResult.getPassFreeFlag());
        freeInfoViewModel.setPassFreeAmount(freeInfoResult.getPassFreeAmount());

        return freeInfoViewModel;
    }


    /**
     * 开通小额免密服务预交易接口 -- 上送数据 -- 构建BII层上送参数
     * @param freePwdViewModel
     * @return
     */
    public static QRPayOpenPassFreeServicePreParams buildOpenPassFreeServicePreBiiParams(QRPayFreePwdViewModel freePwdViewModel){
        QRPayOpenPassFreeServicePreParams passPreParams = new QRPayOpenPassFreeServicePreParams();
        passPreParams.set_combinId(freePwdViewModel.get_combinId());

        return passPreParams;
    }


    /**
     * 开通小额免密服务预交易接口 -- 返回数据 -- 生成View层预交易Model
     *
     * @param freeServicePreResult
     * @return
     */
    public static QRPayFreePwdViewModel generateOpenPassFreeServicePreViewModel(QRPayOpenPassFreeServicePreResult freeServicePreResult) {
        QRPayFreePwdViewModel viewModel = new QRPayFreePwdViewModel();
        viewModel.set_certDN(freeServicePreResult.get_certDN());
        viewModel.set_plainData(freeServicePreResult.get_plainData());
        viewModel.setSmcTrigerInterval(freeServicePreResult.getSmcTrigerInterval());

        List<FactorBean> list = new ArrayList<FactorBean>();
        if (freeServicePreResult.getFactorList() != null) {
            for (QRPayOpenPassFreeServicePreResult.FactorListBean item : freeServicePreResult.getFactorList()) {
                FactorBean factorBean = new FactorBean();
                FactorBean.FieldBean bean = new FactorBean.FieldBean();
                bean.setName(item.getField().getName());
                bean.setType(item.getField().getType());
                factorBean.setField(bean);
                list.add(factorBean);
            }
        }
        viewModel.setFactorList(list);
        return viewModel;
    }

    /**
     * 开通小额密码服务提交交易接口 -- 上送数据 -- 构建BII层上送参数
     *
     * @param viewModel
     * @return
     */
    public static QRPayOpenPassFreeServiceParams buildOpenPassFreeServiceBiiParams(QRPayFreePwdViewModel viewModel) {
        QRPayOpenPassFreeServiceParams freeServiceParams = new QRPayOpenPassFreeServiceParams();
        freeServiceParams.setActSeq(viewModel.getActSeq());
        freeServiceParams.setSmc(viewModel.getSmc());
        freeServiceParams.setSmc_RC(viewModel.getSmc_RC());
        freeServiceParams.setOtp(viewModel.getOtp());
        freeServiceParams.setOtp_RC(viewModel.getOtp_RC());
        freeServiceParams.set_signedData(viewModel.get_signedData());
        freeServiceParams.setDeviceInfo(viewModel.getDeviceInfo());
        freeServiceParams.setDeviceInfo_RC(viewModel.getDeviceInfo_RC());

        freeServiceParams.setState(viewModel.getState());
        freeServiceParams.setActiv(viewModel.getActiv());

        return freeServiceParams;
    }


    /** -------------------------- 小额免密 局部加载数据  封装  开始 ---------------------------- */

    /**
     * 生成有小额免密信息的 FreePassViewModel
     * @param accountBean
     * @param result
     * @return
     */
    public static FreePassViewModel generatePassFreeInfoViewModel(AccountBean accountBean, QRPayGetPassFreeInfoResult result) {
        FreePassViewModel model = getPassFreeInfoViewModel(accountBean);
        if (result == null){
            return model;
        }

        PassFreeInfoViewModel freeInfoViewModel = new PassFreeInfoViewModel();
        freeInfoViewModel.setDebitCardFlag(result.getDebitCardFlag());
        freeInfoViewModel.setCreditCardFlag(result.getCreditCardFlag());
        freeInfoViewModel.setDebitCardPassFreeAmount(result.getDebitCardPassFreeAmount());
        freeInfoViewModel.setCreditCardPassFreeAmount(result.getCreditCardPassFreeAmount());

        //606
        freeInfoViewModel.setPassFreeFlag(result.getPassFreeFlag());
        freeInfoViewModel.setPassFreeAmount(result.getPassFreeAmount());

        model.setFreeInfoViewModel(freeInfoViewModel);

        return model;
    }



    public static List<FreePassViewModel> generatePassFreeInfoListViewModels(List<AccountBean> accountList) {
        List<FreePassViewModel> models = new ArrayList<FreePassViewModel>();
        for (AccountBean accountBean : accountList) {
            FreePassViewModel model = getPassFreeInfoViewModel(accountBean);

            models.add(model);
        }
        return models;
    }


    @NonNull
    public static FreePassViewModel getPassFreeInfoViewModel(AccountBean accountBean) {
        FreePassViewModel model = new FreePassViewModel();
        model.setAccountBean(accountBean);

        return model;
    }

    /** ---------------------------- 小额免密 局部加载数据  封装  结束 ---------------------------- */

    /**
     * 将View层的安全因子数组传递给安全组件
     * @param factorBeanList
     * @return
     */
    public static List<FactorListBean> copyToFactorListBean(List<FactorBean> factorBeanList) {
        List<FactorListBean> factorList = new ArrayList<FactorListBean>();
        for (int i = 0; i < factorBeanList.size(); i++) {
            FactorListBean factorListBean = new FactorListBean();
            FactorListBean.FieldBean fieldBean = new FactorListBean.FieldBean();
            fieldBean.setName(factorBeanList.get(i).getField().getName());
            fieldBean.setType(factorBeanList.get(i).getField().getType());
            factorListBean.setField(fieldBean);
            factorList.add(factorListBean);
        }
        return factorList;
    }
    /**
     * 查询收款结果接口 -- 返回数据 -- 生成View层支付限额Model(废弃)
     *QRPayGetPayeeResultResult
     * @param payQuotaResult
     * @return
     */
//    public static QRPayGetPayeeResultModel generatePayeeResultViewModel(QRPayGetPayeeResultResult payQuotaResult) {
//        QRPayGetPayeeResultModel qrPayGetPayeeResultModel = new QRPayGetPayeeResultModel();
////        qrPayGetPayeeResultModel.setTranStatus(payQuotaResult.getTranStatus());
////        qrPayGetPayeeResultModel.setCurrencyCode(payQuotaResult.getCurrencyCode());
////        qrPayGetPayeeResultModel.setAmount(payQuotaResult.getAmount());
////        qrPayGetPayeeResultModel.setVoucherNum(payQuotaResult.getVoucherNum());
////        qrPayGetPayeeResultModel.setPayerComments(payQuotaResult.getPayerComments());
//        return qrPayGetPayeeResultModel;
//    }


    /**
     * 查询收款人信息接口 -- 返回数据 -- 生成View层收款人信息Model
     * @param result
     * @return
     */
    public static QRPayGetPayeeInfoModel generatePayeeInfoViewModel(QRPayGetPayeeInfoResult result) {
        QRPayGetPayeeInfoModel model = new QRPayGetPayeeInfoModel();
        model.setTranAmount(result.getTranAmount());
        model.setPayeeActNam(result.getPayeeActNam());
        model.setPayeeActNum(result.getPayeeActNum());
        model.setPayeeComments(result.getPayeeComments());
        model.setPayeeIbkNam(result.getPayeeIbkNam());
        model.setPayeeIbkNum(result.getPayeeIbkNum());
        return model;
    }


    /**
     * 关联账户中的银联账户BII Model转换为View Model
     *
     * @param sourceData added by wangf on 2016-11-2 20:50:29
     */
    public static List<AccountBean> convertBIIAccount2ViewModel(List<QRPayGetRelativedAcctListResult> sourceData) {
        List<AccountBean> mAccountList = new ArrayList<AccountBean>();
        if (null != sourceData) {
            for (QRPayGetRelativedAcctListResult item : sourceData) {
                AccountBean convertItem = new AccountBean();
                convertItem.setAccountType(item.getAccountType());
                convertItem.setAccountIbkNum(item.getAccountIbkNum());
                convertItem.setAccountId(String.valueOf(item.getAccountId()));
                convertItem.setAccountName(item.getAccountName());
                convertItem.setAccountNumber(item.getAccountNumber());
                convertItem.setAccountStatus(item.getAccountStatus());
                convertItem.setBranchId(String.valueOf(item.getBranchId()));
                convertItem.setCardDescription(item.getCardDescription());
                convertItem.setCurrencyCode(item.getCurrencyCode());
                convertItem.setCurrencyCode2(item.getCurrencyCode2());
                convertItem.setCardDescriptionCode(item.getCardDescriptionCode());
                convertItem.setCustomerId(String.valueOf(item.getCustomerId()));
                convertItem.setBranchName(item.getBranchName());
                convertItem.setEcard(item.getEcard());
                convertItem.setHasOldAccountFlag(item.getHasOldAccountFlag());
                convertItem.setIsECashAccount(item.getIsECashAccount());
                convertItem.setIsMedicalAccount(item.getIsMedicalAccount());
                convertItem.setNickName(item.getNickName());
                convertItem.setVerifyFactor(item.getVerifyFactor());
                mAccountList.add(convertItem);
            }
        }
        return mAccountList;
    }
}
