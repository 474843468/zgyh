package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationSubmit.PsnHCEQuickPassActivationSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationSubmit.PsnHCEQuickPassActivationSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationVerify.PsnHCEQuickPassActivationVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationVerify.PsnHCEQuickPassActivationVerifyResult;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.SubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/12/19.
 * 描述: 激活确认界面
 */
public class HceActivateConfirmPresenter extends HceBaseConfirmPresenter<PsnHCEQuickPassActivationSubmitResult> {
    
    public HceActivateConfirmPresenter(HceBaseConfirmContract.View view) {
        super(view);
    }

    @Override
    protected String getServiceId() {
        return ServiceIdCodeConst.SERVICE_ID_HCE_ACTIVATION;
    }

    @Override
    protected Observable<VerifyBean> verifyRequest(HceTransactionViewModel viewModel) {
        return mEquickpayService.psnHCEQuickPassActivationVerify(generateVerifyParams(viewModel))
                .map(new Func1<PsnHCEQuickPassActivationVerifyResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(PsnHCEQuickPassActivationVerifyResult result) {
                          return BeanConvertor.toBean(result,new VerifyBean());
                    }
                });
    }

    @Override
    protected Observable<PsnHCEQuickPassActivationSubmitResult> submitRequest(HceTransactionViewModel viewModel, SubmitModel submitModel) {
        return mEquickpayService.psnHCEQuickPassActivationSubmit(generateSubmitParams(viewModel,submitModel));
    }

    private PsnHCEQuickPassActivationVerifyParams generateVerifyParams(HceTransactionViewModel viewModel) {
        PsnHCEQuickPassActivationVerifyParams verifyParams = new PsnHCEQuickPassActivationVerifyParams();
        verifyParams.setMasterCardNo(viewModel.getMasterCardNo());
        verifyParams.setConversationId(viewModel.getConversationId());
        verifyParams.set_combinId(viewModel.get_combinId());
        verifyParams.setDeviceNo(viewModel.getDeviceNo());
        verifyParams.setSlaveCardNo(viewModel.getSlaveCardNo());
        return verifyParams;
    }

    private PsnHCEQuickPassActivationSubmitParams generateSubmitParams(HceTransactionViewModel viewModel, SubmitModel submitModel) {
        PsnHCEQuickPassActivationSubmitParams params = new PsnHCEQuickPassActivationSubmitParams();
        BeanConvertor.toBean(viewModel, params);
        BeanConvertor.toBean(submitModel, params);
        return params;
    }


}
