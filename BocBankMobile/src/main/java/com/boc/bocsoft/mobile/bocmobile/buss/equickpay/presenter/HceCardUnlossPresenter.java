package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseSubmit.PsnHCEQuickPassLiftLoseSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseSubmit.PsnHCEQuickPassLiftLoseSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseVerify.PsnHCEQuickPassLiftLoseVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseVerify.PsnHCEQuickPassLiftLoseVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify.PsnHCEQuickPassQuotaSettingVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify.PsnHCEQuickPassQuotaSettingVerifyResult;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.SubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by gengjunying on 2016/12/22.
 * 描述:
 */
public class HceCardUnlossPresenter extends HceBaseConfirmPresenter<PsnHCEQuickPassLiftLoseSubmitResult> {

    public HceCardUnlossPresenter(HceBaseConfirmContract.View view) {
        super(view);
    }

    @Override
    protected String getServiceId() {
        return ServiceIdCodeConst.SERVICE_ID_HCE_LIFT_LOSE;
    }

    @Override
    protected Observable<VerifyBean> verifyRequest(HceTransactionViewModel viewModel) {
        return mEquickpayService.PsnHCEQuickPassLiftLoseVerify(generateVerifyParams(viewModel))
                .map(new Func1<PsnHCEQuickPassLiftLoseVerifyResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(PsnHCEQuickPassLiftLoseVerifyResult result) {
                        return BeanConvertor.toBean(result,new VerifyBean());
                    }
                });
    }

    @Override
    protected Observable<PsnHCEQuickPassLiftLoseSubmitResult> submitRequest(HceTransactionViewModel viewModel, SubmitModel submitModel) {
        return mEquickpayService.PsnHCEQuickPassLiftLoseSubmit(generateSubmitParams(viewModel,submitModel));
    }



    private PsnHCEQuickPassLiftLoseVerifyParams generateVerifyParams(HceTransactionViewModel viewModel) {
        PsnHCEQuickPassLiftLoseVerifyParams psnHCEQuickPassLiftLoseVerifyParams = new PsnHCEQuickPassLiftLoseVerifyParams();

        psnHCEQuickPassLiftLoseVerifyParams.setMasterCardNo(viewModel.getMasterCardNo());
        psnHCEQuickPassLiftLoseVerifyParams.set_combinId(viewModel.get_combinId());
        psnHCEQuickPassLiftLoseVerifyParams.setConversationId(viewModel.getConversationId());
        psnHCEQuickPassLiftLoseVerifyParams.setSlaveCardNo(viewModel.getSlaveCardNo());

        // TODO: 2016/12/20
        return psnHCEQuickPassLiftLoseVerifyParams;
    }

    private PsnHCEQuickPassLiftLoseSubmitParams generateSubmitParams(HceTransactionViewModel viewModel, SubmitModel submitModel) {
        PsnHCEQuickPassLiftLoseSubmitParams params = new PsnHCEQuickPassLiftLoseSubmitParams();

        BeanConvertor.toBean(viewModel, params);
        BeanConvertor.toBean(submitModel, params);

        // TODO: 2016/12/19
        return params;
    }

}
