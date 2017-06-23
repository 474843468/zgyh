package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv.PsnCrcdApplyCashDivParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre.PsnCrcdApplyCashDivPreParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre.PsnCrcdApplyCashDivPreResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdGetCashDivCommissionCharge.PsnCrcdGetCashDivCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdGetCashDivCommissionCharge.PsnCrcdGetCashDivCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.model.CashInstallmentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui.CashInstallmentContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;

import rx.Observable;
import rx.functions.Func1;

public class CashInstallmentPresenter extends RxPresenter implements CashInstallmentContract.Presenter {

    private CashInstallmentContract.View mCashInstallmentFragmentView;
    private CrcdService crcdService;
    private String mConversationId;
    private GlobalService mGlobalService;
    final String[] temp = new String[1]; //存会话id

    public CashInstallmentPresenter(CashInstallmentContract.View view) {
        mCashInstallmentFragmentView = view;
        crcdService = new CrcdService();
        mGlobalService = new GlobalService();
    }

    /**
     * 进行费用试算
     * @param cashInstallmentViewModel View Model
     */
    @Override
    public void getCashDivCommissionCharge(final CashInstallmentViewModel cashInstallmentViewModel) {
        PsnCrcdGetCashDivCommissionChargeParams psnCrcdGetCashDivCommissionChargeParams = new PsnCrcdGetCashDivCommissionChargeParams();
        psnCrcdGetCashDivCommissionChargeParams.setAccountId(cashInstallmentViewModel.getFromAccountId());
        psnCrcdGetCashDivCommissionChargeParams.setCurrency(cashInstallmentViewModel.getCurrency());
        psnCrcdGetCashDivCommissionChargeParams.setDivAmount(cashInstallmentViewModel.getDivAmount());
        psnCrcdGetCashDivCommissionChargeParams.setDivPeriod(cashInstallmentViewModel.getDivPeriod());
        psnCrcdGetCashDivCommissionChargeParams.setChargeMode(cashInstallmentViewModel.getChargeMode());
        psnCrcdGetCashDivCommissionChargeParams.setDivRate(cashInstallmentViewModel.getDivRate());
        crcdService.psnCrcdGetCashDivCommissionCharge(psnCrcdGetCashDivCommissionChargeParams)
                .compose(this.<PsnCrcdGetCashDivCommissionChargeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdGetCashDivCommissionChargeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdGetCashDivCommissionChargeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mCashInstallmentFragmentView.onQueryCashDivCommissionChargeFail(cashInstallmentViewModel);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdGetCashDivCommissionChargeResult psnCrcdGetCashDivCommissionChargeResult) {
                        //分期手续费
                        cashInstallmentViewModel.setDivCharge(psnCrcdGetCashDivCommissionChargeResult.getDivCharge());
                        //首期-应还金额
                        cashInstallmentViewModel.setFirstPayAmount(psnCrcdGetCashDivCommissionChargeResult.getFirstPayAmount());
                        //以后每期-应还金额
                        cashInstallmentViewModel.setPerPayAmount(psnCrcdGetCashDivCommissionChargeResult.getPerPayAmount());
                        //分期手续费率
                        cashInstallmentViewModel.setDivRate(psnCrcdGetCashDivCommissionChargeResult.getDivRate());
                        //if ("1".equals(psnCrcdGetCashDivCommissionChargeResult.getChargeMode())) {
                        //首期手续费
                        cashInstallmentViewModel.setFirstCharge(psnCrcdGetCashDivCommissionChargeResult.getFirstCharge());
                        //后续每期手续费
                        cashInstallmentViewModel.setPerCharge(psnCrcdGetCashDivCommissionChargeResult.getPerCharge());
                        //}
                        mCashInstallmentFragmentView.onQueryCashDivCommissionChargeSuccess(cashInstallmentViewModel);
                    }
                });
    }

    /**
     * 请求安全因子列表
     *
     * @param serviceId 服务码
     */
    @Override
    public void qrySecurityFactor(final String serviceId) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String s) {
                        mConversationId = s;
                        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
                        params.setConversationId(mConversationId);
                        params.setServiceId(serviceId);
                        return mGlobalService.psnGetSecurityFactor(params);
                    }
                }).compose(this.<PsnGetSecurityFactorResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(
                            BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(
                            PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        SecurityFactorModel securityFactorModel = ModelUtil.generateSecurityFactorModel(
                                psnGetSecurityFactorResult);
                        mCashInstallmentFragmentView.onQrySecurityFactorSuccess(
                                securityFactorModel);
                    }
                });
    }

    /**
     * 点击下一步后的接口调用——手续费试算 + 预交易
     *
     * @param cashInstallmentViewModel
     */
    @Override
    public void qryCrcdApplyCashDivPre(final CashInstallmentViewModel cashInstallmentViewModel) {

        PsnCrcdApplyCashDivPreParams psnCrcdApplyCashDivPreParams =
                BeanConvertor.toBean(cashInstallmentViewModel, new PsnCrcdApplyCashDivPreParams());

        if (!StringUtils.isEmpty(cashInstallmentViewModel.getFirstPayAmount())) {
            //分期收取手续费
            if (cashInstallmentViewModel.getChargeMode().equals("1")) {
                BigDecimal firstPayAmountBD = new BigDecimal(cashInstallmentViewModel.getFirstPayAmount());
                BigDecimal perPayAmountBD = new BigDecimal(cashInstallmentViewModel.getPerPayAmount());
                BigDecimal firstPayChargeBD = new BigDecimal(cashInstallmentViewModel.getFirstCharge());
                BigDecimal perPayChargeBD = new BigDecimal(cashInstallmentViewModel.getPerCharge());

                psnCrcdApplyCashDivPreParams.setDivCharge(format(cashInstallmentViewModel.getDivCharge()));
                psnCrcdApplyCashDivPreParams.setFirstPayAmount(format(firstPayAmountBD.add(firstPayChargeBD).toString()));
                psnCrcdApplyCashDivPreParams.setPerPayAmount(format(perPayAmountBD.add(perPayChargeBD).toString()));
            }
            //一次性收取手续费 不需要求和 只需要进行小数点后去0转换
            else if (cashInstallmentViewModel.getChargeMode().equals("0")) {
                psnCrcdApplyCashDivPreParams.setDivCharge(format(cashInstallmentViewModel.getDivCharge()));
                psnCrcdApplyCashDivPreParams.setFirstPayAmount(format(cashInstallmentViewModel.getFirstPayAmount()));
                psnCrcdApplyCashDivPreParams.setPerPayAmount(format(cashInstallmentViewModel.getPerPayAmount()));
            }
        }
        psnCrcdApplyCashDivPreParams.setConversationId(mConversationId);
        psnCrcdApplyCashDivPreParams.set_combinId(cashInstallmentViewModel.get_combinId());

        crcdService.psnCrcdApplyCashDivPre(psnCrcdApplyCashDivPreParams)
                .compose(this.<PsnCrcdApplyCashDivPreResult>bindToLifecycle())
                .map(new Func1<PsnCrcdApplyCashDivPreResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(
                            PsnCrcdApplyCashDivPreResult psnCrcdApplyCashDivPreResult
                    ) {
                        return BeanConvertor.toBean(psnCrcdApplyCashDivPreResult, new VerifyBean());
                    }
                })
                .compose(SchedulersCompat.<VerifyBean>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<VerifyBean>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mCashInstallmentFragmentView.onCrcdApplyCashDivPreFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(VerifyBean verifyBean) {
                        mCashInstallmentFragmentView.onCrcdApplyCashDivPreSuccess(verifyBean, mConversationId);
                    }
                });
    }

    /**
     * 去掉小数点后0的数值转换
     *
     * @param input
     * @return
     */
    private static String format(String input) {
        if (input.indexOf(".") > 0) {
            String s = input.replaceAll("0+?$", "");
            return s.replaceAll("[.]$", "");
        }
        return input;
    }
}