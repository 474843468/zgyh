package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota.PsnDebitCardQryQuotaParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota.PsnDebitCardQryQuotaResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.account.service.LimitService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.QuotaModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

/**
 * @author wangyang
 *         2016/10/13 03:49
 *         交易限额
 */
public class LimitPresenter extends BaseAccountPresenter implements LimitContract.LimitPresenter{

    private LimitContract.LimitView limitView;

    private LimitContract.QuotaView quotaView;

    private AccountService accountService;

    private LimitService limitService;

    public LimitPresenter(LimitContract.LimitView limitView) {
        this.limitView = limitView;
        limitService = new LimitService();
    }

    public LimitPresenter(LimitContract.QuotaView quotaView) {
        this.quotaView = quotaView;
        accountService = new AccountService();
    }

    @Override
    public void queryLimit(final AccountBean accountBean, final String serviceType) {
        limitService.psnNcpayServiceChoose(new PsnNcpayServiceChooseParams(accountBean.getAccountId(),serviceType))
                .compose(this.<PsnNcpayServiceChooseResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnNcpayServiceChooseResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnNcpayServiceChooseResult>() {
                    @Override
                    public void onNext(PsnNcpayServiceChooseResult result) {
                        limitView.queryLimit(ModelUtil.generateLimitModel(accountBean,result,serviceType));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }
                });
    }

    @Override
    public void queryAllQuota(final QuotaModel quotaModel){
        accountService.psnDebitCardQryQuota(new PsnDebitCardQryQuotaParams(quotaModel.getAccountId()))
                .compose(this.<PsnDebitCardQryQuotaResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDebitCardQryQuotaResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnDebitCardQryQuotaResult>() {
                    @Override
                    public void onNext(PsnDebitCardQryQuotaResult result) {
                        quotaView.queryAllQuota(ModelUtil.generateQuotaModel(quotaModel,result));
                    }
                });
    }
}
