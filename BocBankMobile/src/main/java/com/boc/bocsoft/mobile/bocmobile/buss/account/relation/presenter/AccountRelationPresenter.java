package com.boc.bocsoft.mobile.bocmobile.buss.account.relation.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author wangyang
 *         16/7/7 11:10
 *         取消关联业务逻辑
 */
public class AccountRelationPresenter extends BaseTransactionPresenter implements AccountRelationContract.Presenter {

    private AccountRelationContract.AccountRelationView accountRelationView;

    private AccountService accountService;

    public AccountRelationPresenter(AccountRelationContract.AccountRelationView accountRelationView) {
        super(accountRelationView);
        this.accountRelationView = accountRelationView;
        accountService = new AccountService();
    }

    @Override
    public void cancelAccountRelation(final AccountBean accountBean) {
        getToken().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String token) {
                return accountService.psnSVRCancelAccRelation(ModelUtil.generateRelationParams(accountBean, getConversationId(), token));
            }
        }).compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        clearConversation();
                        updateChinaBankAccountList(result);
                    }
                });
    }


    @Override
    public void afterUpdateChinaBankAccountList(Object object) {
        accountRelationView.cancelAccountRelation();
    }
}
