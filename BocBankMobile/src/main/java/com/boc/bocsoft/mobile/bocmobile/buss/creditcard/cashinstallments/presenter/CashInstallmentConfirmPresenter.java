package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.presenter;

import android.support.annotation.NonNull;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv.PsnCrcdApplyCashDivParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv.PsnCrcdApplyCashDivResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre.PsnCrcdApplyCashDivPreParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre.PsnCrcdApplyCashDivPreResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.model.CashInstallmentViewModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.math.BigDecimal;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cry7096 on 2016/12/1.
 */
public class CashInstallmentConfirmPresenter extends BaseConfirmPresenter<CashInstallmentViewModel, PsnCrcdApplyCashDivResult> {

    private CrcdService mCrcdService = new CrcdService();
    public CashInstallmentConfirmPresenter(BaseConfirmContract.View<PsnCrcdApplyCashDivResult> view) {
        super(view);
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(CashInstallmentViewModel fillInfo) {
        PsnCrcdApplyCashDivPreParams params =
                BeanConvertor.toBean(fillInfo, new PsnCrcdApplyCashDivPreParams());
        if(!StringUtils.isEmpty(fillInfo.getFirstPayAmount())) {
            //分期收取手续费
            if (fillInfo.getChargeMode().equals("1")) {
                BigDecimal firstPayAmountBD = new BigDecimal(fillInfo.getFirstPayAmount());
                BigDecimal perPayAmountBD = new BigDecimal(fillInfo.getPerPayAmount());
                BigDecimal firstPayChargeBD = new BigDecimal(fillInfo.getFirstCharge());
                BigDecimal perPayChargeBD = new BigDecimal(fillInfo.getPerCharge());

                params.setDivCharge(format(fillInfo.getDivCharge()));
                params.setFirstPayAmount(format(firstPayAmountBD.add(firstPayChargeBD).toString()));
                params.setPerPayAmount(format(perPayAmountBD.add(perPayChargeBD).toString()));
            }
            //一次性收取手续费 不需要求和 只需要进行小数点后去0转换
            else if (fillInfo.getChargeMode().equals("0")) {
                params.setDivCharge(format(fillInfo.getDivCharge()));
                params.setFirstPayAmount(format(fillInfo.getFirstPayAmount()));
                params.setPerPayAmount(format(fillInfo.getPerPayAmount()));
            }
        }

        return mCrcdService.psnCrcdApplyCashDivPre(params)
                .map(new Func1<PsnCrcdApplyCashDivPreResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(
                            PsnCrcdApplyCashDivPreResult psnCrcdApplyCashDivPreResult) {
                        VerifyBean bean = BeanConvertor.toBean(psnCrcdApplyCashDivPreResult,
                                new VerifyBean());
                        return bean;
                    }
                });
    }

    @Override
    public void submit(CashInstallmentViewModel fillInfo, BaseSubmitBean submitBean) {
        //填充申请上送字段
        PsnCrcdApplyCashDivParams params =
                BeanConvertor.toBean(fillInfo, new PsnCrcdApplyCashDivParams());
        params = BeanConvertor.toBean(fillInfo, params);
        params = BeanConvertor.toBean(submitBean, params);

        if(!StringUtils.isEmpty(fillInfo.getFirstPayAmount())) {
            //分期收取手续费
            if (fillInfo.getChargeMode().equals("1")) {
                BigDecimal firstPayAmountBD = new BigDecimal(fillInfo.getFirstPayAmount());
                BigDecimal perPayAmountBD = new BigDecimal(fillInfo.getPerPayAmount());
                BigDecimal firstPayChargeBD = new BigDecimal(fillInfo.getFirstCharge());
                BigDecimal perPayChargeBD = new BigDecimal(fillInfo.getPerCharge());

                params.setDivCharge(format(fillInfo.getDivCharge()));
                params.setFirstPayAmount(format(firstPayAmountBD.add(firstPayChargeBD).toString()));
                params.setPerPayAmount(format(perPayAmountBD.add(perPayChargeBD).toString()));
            }
            //一次性收取手续费 不需要求和 只需要进行小数点后去0转换
            else if (fillInfo.getChargeMode().equals("0")) {
                params.setDivCharge(format(fillInfo.getDivCharge()));
                params.setFirstPayAmount(format(fillInfo.getFirstPayAmount()));
                params.setPerPayAmount(format(fillInfo.getPerPayAmount()));
            }
        }
        mCrcdService.psnCrcdApplyCashDiv(params)
                .compose(this.<PsnCrcdApplyCashDivResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdApplyCashDivResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdApplyCashDivResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdApplyCashDivResult psnCrcdApplyCashDivResult) {
                        mView.onSubmitSuccess(psnCrcdApplyCashDivResult);
                    }
                });
    }

    /**
     * 金额去掉小数点后的0
     * @param input
     * @return
     */
    private static String format(String input){
        if(input.indexOf(".")>0){
            String s = input.replaceAll("0+?$","");
            return s.replaceAll("[.]$","");
        }
        return input;
    }
}
