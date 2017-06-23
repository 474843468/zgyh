package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.LimitService;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitType;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.RxUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author wangyang
 *         2016/10/27 19:21
 *         跨行订购Presenter
 */
public class AcrossBankPresenter extends BaseAccountPresenter implements AcrossBankContract.AcrossBankPresenter {

    private LimitService limitService;

    private AcrossBankContract.AcrossBankView acrossBankView;

    public AcrossBankPresenter(AcrossBankContract.AcrossBankView acrossBankView) {
        this.limitService = new LimitService();
        this.acrossBankView = acrossBankView;
    }

    @Override
    public void queryService(final List<AccountBean> accountBeans) {
        Observable.just(accountBeans).compose(this.<List<AccountBean>>bindToLifecycle())
                .compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<AccountBean, Observable<PsnNcpayServiceChooseResult>>() {
                    @Override
                    public Observable<PsnNcpayServiceChooseResult> call(AccountBean accountBean) {
                        return limitService.psnNcpayServiceChoose(new PsnNcpayServiceChooseParams(accountBean.getAccountId(), LimitType.LIMIT_ACROSS_BANK));
                    }
                })).compose(SchedulersCompat.<PsnNcpayServiceChooseResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnNcpayServiceChooseResult>() {

                    private List<LimitModel> openModels = new ArrayList<>(),closeModels;

                    @Override
                    public void onNext(PsnNcpayServiceChooseResult result) {
                        if (result == null)
                            return;

                        LimitModel limitModel = ModelUtil.generateLimitModel(accountBeans, result);
                        if (limitModel.isOpen())
                            openModels.add(limitModel);
                    }

                    @Override
                    public void onCompleted() {
                        closeModels = ModelUtil.generateLimitModel(accountBeans,openModels);
                        acrossBankView.queryService(openModels,closeModels);
                    }
                });
    }
}
