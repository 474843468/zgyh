package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery.PsnCardBrandQueryParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery.PsnCardBrandQueryResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.service.EquickpayService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.CardBrandModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceSelectAccountContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/12/15.
 * 描述:选择主卡的账户列表presenter
 */
public class HceSelectAccountPresent extends RxPresenter implements HceSelectAccountContract.Presenter {

    private final HceSelectAccountContract.View mView;
    private final AccountService mAccountService;
    private final EquickpayService mEquickpayService;
    private final GlobalService mGlobalService;
    private List<String> unavailableHceAccountIds = new ArrayList<>();//  已经查询过的不支持的缓存

    public HceSelectAccountPresent(HceSelectAccountContract.View view) {
        this.mView = view;
        mAccountService = new AccountService();
        mEquickpayService = new EquickpayService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void queryAccountDetail(final AccountBean account) {
        if (isUnavailableForHce(account)) {
            mView.showErrorDialog("此卡不支持e闪付,请选择其它卡");
            return;
        }
        mView.showLoading();
        mAccountService.psnAccountQueryAccountDetail(new PsnAccountQueryAccountDetailParams(account.getAccountId()))
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.closeLoading();
                        mView.showErrorDialog("此卡不支持e闪付,请选择其它卡=====on error");
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                        queryCardBrand(account);
                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult accountDetailResult) {

                        if (!canApplyForHCE(accountDetailResult)) {
                            mView.showErrorDialog("此卡不支持e闪付,请选择其它卡=====on next");
                            unavailableHceAccountIds.add(account.getAccountId());
                            return;
                        }


                        // TODO: 2016/12/20 满足hce卡申请再查卡品牌
                        ToastUtils.show("====queryAccountDetail==========" + accountDetailResult.getAccountStatus());
                    }
                });
    }

    private boolean isUnavailableForHce(AccountBean account) {
        return unavailableHceAccountIds.contains(account.getAccountId());
    }

    private boolean canApplyForHCE(PsnAccountQueryAccountDetailResult accountDetailResult) {
        return accountDetailResult != null && "00".equals(accountDetailResult.getAccountStatus());
    }

    @Override
    public void queryCardBrand(final AccountBean account) {
        mView.showLoading();
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .flatMap(new Func1<String, Observable<PsnCardBrandQueryResult>>() {
                    @Override
                    public Observable<PsnCardBrandQueryResult> call(String conversationId) {
                        return mEquickpayService.psnCardBrandQuery(generateQueryBrandParams(account,conversationId));
                    }
                })
                .map(new Func1<PsnCardBrandQueryResult, CardBrandModel>() {
                    @Override
                    public CardBrandModel call(PsnCardBrandQueryResult result) {
                        return new CardBrandModel(result.getCardBrandId());
                    }
                })
                .compose(this.<CardBrandModel>bindToLifecycle())
                .compose(SchedulersCompat.<CardBrandModel>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<CardBrandModel>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.closeLoading();
                        LogUtil.d("=====queryCardBrand=======onerror================");
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(CardBrandModel cardBrandModel) {
                        LogUtil.d("=====queryCardBrand=======onNext================" +cardBrandModel.getCardBrandId());
                        if (cardBrandModel == null || cardBrandModel.getCardBrandId() == null) {
                            mView.showErrorDialog("此卡不支持申请云闪付");
                            return;
                        }
                        mView.onQueryCardBrandSuccess(account,cardBrandModel);
                    }
                });
    }

    // 查询卡品牌上送参数
    private PsnCardBrandQueryParams generateQueryBrandParams(AccountBean account, String conversationId) {
        return new PsnCardBrandQueryParams(account.getAccountNumber(), account.getAccountId(), conversationId);
    }


}
