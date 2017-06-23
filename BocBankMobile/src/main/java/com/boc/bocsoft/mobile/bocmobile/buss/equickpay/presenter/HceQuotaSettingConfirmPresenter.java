package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify.PsnHCEQuickPassQuotaSettingVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify.PsnHCEQuickPassQuotaSettingVerifyResult;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.SubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/12/20.
 * 描述:设置修改限额
 */
public class HceQuotaSettingConfirmPresenter extends HceBaseConfirmPresenter<PsnHCEQuickPassQuotaSettingSubmitResult> {

    public HceQuotaSettingConfirmPresenter(HceBaseConfirmContract.View view) {
        super(view);
    }

    @Override
    protected String getServiceId() {
        return ServiceIdCodeConst.SERVICE_ID_HCE_QUOTA_SETTING;
    }

    @Override
    protected Observable<VerifyBean> verifyRequest(HceTransactionViewModel viewModel) {
        return mEquickpayService.psnHCEQuickPassQuotaSettingVerify(generateVerifyParams(viewModel))
                .map(new Func1<PsnHCEQuickPassQuotaSettingVerifyResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(PsnHCEQuickPassQuotaSettingVerifyResult result) {
                        return BeanConvertor.toBean(result,new VerifyBean());
                    }
                });
    }

    @Override
    protected Observable<PsnHCEQuickPassQuotaSettingSubmitResult> submitRequest(HceTransactionViewModel viewModel, SubmitModel submitModel) {
        return mEquickpayService.psnHCEQuickPassQuotaSettingSubmit(generateSubmitParams(viewModel,submitModel));
    }

    private PsnHCEQuickPassQuotaSettingVerifyParams generateVerifyParams(HceTransactionViewModel viewModel) {
        PsnHCEQuickPassQuotaSettingVerifyParams params = new PsnHCEQuickPassQuotaSettingVerifyParams();
        BeanConvertor.toBean(viewModel, params);
        return params;
    }

    private PsnHCEQuickPassQuotaSettingSubmitParams generateSubmitParams(HceTransactionViewModel viewModel, SubmitModel submitModel) {
        PsnHCEQuickPassQuotaSettingSubmitParams params = new PsnHCEQuickPassQuotaSettingSubmitParams();
        BeanConvertor.toBean(viewModel, params);
        BeanConvertor.toBean(submitModel, params);
        return params;
    }

}
