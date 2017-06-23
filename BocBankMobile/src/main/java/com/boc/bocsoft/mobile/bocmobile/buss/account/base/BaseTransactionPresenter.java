package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.buss.account.SecurityModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author wangyang
 *         16/8/19 20:33
 *         交易逻辑基类
 */
public class BaseTransactionPresenter extends BaseAccountPresenter implements TransactionPresenter {

    /**
     * 交易回调界面
     */
    private BaseTransactionView transactionView;

    protected String random;

    public BaseTransactionPresenter(BaseView baseView) {
        super();
        if (baseView instanceof BaseTransactionView)
            this.transactionView = (BaseTransactionView) baseView;
    }

    /**
     * 获取安全因子
     *
     * @param serviceId
     * @param view
     */
    @Override
    public void getSecurityCombin(final String serviceId, final BaseTransactionView view) {
        getConversation().flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
            @Override
            public Observable<PsnGetSecurityFactorResult> call(String conversationId) {
                setConversationId(conversationId);
                PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
                mSecurityFactorParams.setConversationId(conversationId);
                //硬件绑定服务码
                mSecurityFactorParams.setServiceId(serviceId);
                return getGlobalService().psnGetSecurityFactor(mSecurityFactorParams);
            }
        })
                .compose(this.<PsnGetSecurityFactorResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void onNext(PsnGetSecurityFactorResult result) {
                        if (result == null) {
                            onResultError("获取安全因子失败!");
                            return;
                        }

                        //回调界面
                        if (view != null)
                            view.psnCombinSuccess(ModelUtil.generateSecurityFactorModel(result));
                    }
                });
    }

    /**
     * 获取随机数
     *
     * @return
     */
    @Override
    public void getRandom(final BaseTransactionView view) {
        //获取会话,调用请求获取Token
        getConversation().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        setConversationId(conversationId);
                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(conversationId);
                        return getGlobalService().psnGetRandom(params);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String random) {
                        BaseTransactionPresenter.this.random=random;
                        //回调界面
                        if (view != null)
                            view.psnGetRandomSuccess(random, getConversationId());
                    }
                });
    }

    @Override
    public void preTransactionSuccess(SecurityModel securityModel) {
        if (transactionView == null) {
            throw new RuntimeException("please init baseTransactionView");
        }
        EShieldVerify.getInstance(((BussFragment)transactionView).getActivity()).setmPlainData(securityModel.getPlainData());
        transactionView.psnPreTransactionSuccess(securityModel);
    }
}
