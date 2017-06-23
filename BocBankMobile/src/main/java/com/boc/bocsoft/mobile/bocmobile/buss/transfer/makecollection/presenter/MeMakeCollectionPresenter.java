package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import android.app.Activity;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify.PsnTransActCollectionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify.PsnTransActCollectionVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.CombinListEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.MeMakeCollectionContact;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Contact：我要收款
 * Created by zhx on 2016/9/13
 */
public class MeMakeCollectionPresenter implements MeMakeCollectionContact.Presenter {
    private MeMakeCollectionContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private TransferService transferService;

    public static String conversationId = null;
    public CombinListBean combinListBean;
    public PsnGetSecurityFactorResult psnGetSecurityFactorResult;
    // 默认的安全因子
    public CombinListBean defaultCombin;

    public MeMakeCollectionPresenter(MeMakeCollectionContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        transferService = new TransferService();
    }


    @Override
    public void collectionVerify(final PsnTransActCollectionVerifyViewModel viewModel) {
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversation) {
                        MeMakeCollectionPresenter.this.conversationId = conversation;

                        PsnGetSecurityFactorParams psnGetSecurityFactorParams = new PsnGetSecurityFactorParams();
                        psnGetSecurityFactorParams.setConversationId(conversation);
                        psnGetSecurityFactorParams.setServiceId("PB037"); // 转账汇款->主动收款->主动收款预交易:PB037
                        return globalService.psnGetSecurityFactor(psnGetSecurityFactorParams);
                    }
                })
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnTransActCollectionVerifyResult>>() {
                    @Override
                    public Observable<PsnTransActCollectionVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        MeMakeCollectionPresenter.this.psnGetSecurityFactorResult = psnGetSecurityFactorResult; // 保存psnGetSecurityFactorResult

                        // ------start-------base on liuweidong
                        // 传递安全因子给组件
                        defaultCombin = SecurityVerity.getInstance(mActivity).
                                getDefaultSecurityFactorId(new SecurityFactorModel(PublicUtils.copyOfSecurityCombin(resultModelToViewModel(psnGetSecurityFactorResult))));
                        // ------end-------base on liuweidong

                        PsnTransActCollectionVerifyParams psnTransActCollectionVerifyParams = new PsnTransActCollectionVerifyParams();

                        BeanConvertor.toBean(viewModel, psnTransActCollectionVerifyParams);
                        psnTransActCollectionVerifyParams.setConversationId(conversationId); // 如果conversationId没有赋值，就会提示回话失效


                        MeMakeCollectionPresenter.this.combinListBean = generateCombinId(psnGetSecurityFactorResult);
                        psnTransActCollectionVerifyParams.set_combinId(combinListBean.getId());

                        viewModel.set_combinId(combinListBean.getId());
                        viewModel.set_combinName(combinListBean.getName());


                        CombinListEntity combinListEntity = new CombinListEntity();
                        combinListEntity.setId(combinListBean.getId());

                        return transferService.psnTransActCollectionVerify(psnTransActCollectionVerifyParams);
                    }
                })
                .compose(SchedulersCompat.<PsnTransActCollectionVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActCollectionVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.collectionVerifyFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActCollectionVerifyResult psnTransActCollectionVerifyResult) {
                        BeanConvertor.toBean(psnTransActCollectionVerifyResult, viewModel);
                        viewModel.setFactorList(psnTransActCollectionVerifyResult.getFactorList());
                        mView.collectionVerifySuccess(viewModel);
                    }
                });

    }

    /**
     * 将安全因子响应数据复制到ViewModel中
     *
     * @param psnGetSecurityFactorResult
     * @return
     */
    private SecurityViewModel resultModelToViewModel(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
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


    private Activity mActivity;

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 生成需要的CombinId
     *
     * @param psnGetSecurityFactorResult
     * @return
     */
    private CombinListBean generateCombinId(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
        SecurityFactorModel model = new SecurityFactorModel();

        model.setCombinList(psnGetSecurityFactorResult.get_combinList());
        model.setDefaultCombin(psnGetSecurityFactorResult.get_defaultCombin());

        CombinListBean combinListBean = SecurityVerity.getInstance(mActivity).getDefaultSecurityFactorId(model);
        return combinListBean;
    }

    @Override
    public void subscribe() {
        // TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        // TODO 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
