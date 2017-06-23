package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter;


import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincAccountList.PsnFincAccountListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincAccountList.PsnFincAccountListResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail.PsnQueryTaAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail.PsnQueryTaAccountDetailResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.ChangeCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TransAccountCancelReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TransAccount.TransAccountContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TransAccount.TransAccountView;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class TransAccountPresenter extends RxPresenter implements TransAccountContract.Presenter {

    private TransAccountView mContractView;
    private FundService mFundService;
    private GlobalService mGlobalService;
    //接口请求参数model
    private TransAccountCancelReqModel request;

    public TransAccountPresenter(TransAccountView view) {
        mContractView = view;
        mContractView.setPresenter(this);
        mFundService = new FundService();
        mGlobalService = new GlobalService();
    }

    // 查询080 PsnFincQueryQccBalance查询资金账户余额(QCC)
    void queryQccBalance() {

    }

    ;


    // 提交048 PsnCancelFundAccount基金交易账户销户
    /**
     * 创建会话
     */
    private String conversationId;
    private String tokenId;

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    /**
     * 获取Token
     *
     * @return
     */
    public Observable<String> getToken() {
        //根据ConversationId生成Token
        return getConversation().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String conversationResult) {
                conversationId = conversationResult;
                PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                params.setConversationId(conversationResult);
                return mGlobalService.psnGetTokenId(params);
            }
        });
    }

    /**
     * 获取会话,如果已经存在会话ID,则直接返回不用请求
     *
     * @return
     */
    public Observable<String> getConversation() {
        if (!StringUtils.isEmpty(conversationId))
            return Observable.just(conversationId);

        //生成ConversationId
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        return mGlobalService.psnCreatConversation(psnCreatConversationParams);
    }

    @Override
    public void transAccCancelSubmit() {

        getToken().flatMap(new Func1<String, Observable<PsnCancelFundAccountResult>>() {
            @Override
            public Observable<PsnCancelFundAccountResult> call(String token) {
                PsnCancelFundAccountParams request = new PsnCancelFundAccountParams();
                tokenId = token;
                request.setConversationId(conversationId);
                request.setToken(tokenId);
                return mFundService.PsnCancelFundAccount(request);
            }
        })
                .compose(SchedulersCompat.<PsnCancelFundAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCancelFundAccountResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCancelFundAccountResult result) {
                        //成功调用
                        mContractView.transAccCancelSubmitSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        mContractView.transAccCancelSubmitFail(e);
                    }
                });

    }

    // 查询076 PsnFincAccountList获取资金账号列表
    @Override
    public void queryCardList() {
        PsnFincAccountListParams params = new PsnFincAccountListParams();
        mFundService.PsnFincAccountList(params)
                .compose(this.<List<PsnFincAccountListResultBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnFincAccountListResultBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnFincAccountListResultBean>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryCardListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnFincAccountListResultBean> result) {
                        // TODO:conversationID
                        ChangeCardModel viewModel = new ChangeCardModel();
                        // 为viewModel中的list赋值
                        List<ChangeCardModel.CardListBean> list = new LinkedList<ChangeCardModel.CardListBean>();
                        // 循环读取result列表中的每一项，赋值到list中
                        for (PsnFincAccountListResultBean resBean : result) {
                            ChangeCardModel.CardListBean bean = viewModel.new CardListBean();
                            bean.setNickName(resBean.getNickName());
                            bean.setAccountNumber(resBean.getAccountNumber());
                            bean.setAccountType(resBean.getAccountType());
                            bean.setAccountName(resBean.getAccountName());
                            bean.setAccountId(resBean.getAccountId()+"");
                            list.add(bean);
                        }
                        viewModel.setcardList(list);

                        //页面回调方法，viewModel是把服务端返回的数据解析到view层
                        mContractView.queryCardListSuccess(viewModel);
                    }
                });
    }

    @Override
    public void queryTaAccList() {
        PsnQueryTaAccountDetailParams params = new PsnQueryTaAccountDetailParams();
        mFundService.PsnQueryTaAccountDetail(params)
                .compose(this.<List<PsnQueryTaAccountDetailResultBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnQueryTaAccountDetailResultBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnQueryTaAccountDetailResultBean>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryTaAccListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnQueryTaAccountDetailResultBean> result) {
                        TaAccountModel viewModel = new TaAccountModel();
                        // 为viewModel中的list赋值
                        List<TaAccountModel.TaAccountBean> list = new LinkedList<TaAccountModel.TaAccountBean>();
                        // 循环读取result列表中的每一项，赋值到list中
                        for (PsnQueryTaAccountDetailResultBean resBean : result) {
                            TaAccountModel.TaAccountBean bean = viewModel.new TaAccountBean();
                            bean.setTaAccountNo(resBean.getTaAccountNo());
                            bean.setFundRegName(resBean.getFundRegName());
                            bean.setFundRegCode(resBean.getFundRegCode());
                            bean.setAccountStatus(resBean.getFundAccount());
                            list.add(bean);
                        }
                        viewModel.setTaAccountList(list);

                        //页面回调方法，viewModel是把服务端返回的数据解析到view层
                        mContractView.queryTaAccListSuccess(viewModel);
                    }
                });
    }

}

