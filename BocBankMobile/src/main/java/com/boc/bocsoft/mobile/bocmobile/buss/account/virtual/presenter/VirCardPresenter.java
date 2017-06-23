package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit.PsnCrcdVirtualCardApplyInitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit.PsnCrcdVirtualCardApplyInitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardCancel.PsnCrcdVirtualCardCancelResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.PsnCrcdVirtualCardQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSendMessage.PsnCrcdVirtualCardSendMessageParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.PsnCrcdVirtualCardSettledbillQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillQuery.PsnCrcdVirtualCardUnsettledbillQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum.PsnCrcdVirtualCardUnsettledbillSumParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum.PsnCrcdVirtualCardUnsettledbillSumResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.VirtualService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.common.utils.RxUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author wangyang
 *         16/7/21 16:11
 *         虚拟银行卡业务逻辑处理
 */
public class VirCardPresenter extends BaseAccountPresenter implements VirtualCardContract.Presenter {

    /**
     * 回调界面View
     */
    private VirtualCardContract.VirCardView cardView;
    /**
     * 回调界面View
     */
    private VirtualCardContract.VirCardApplyView applyView;
    /**
     * 回调界面
     */
    private VirtualCardContract.VirBillView virBillView;

    private VirtualCardContract.VirCardCancelView virCardCancelView;

    private VirtualCardContract.VirCardUnsettledBillView unsettledBillView;

    /**
     * 服务Service
     */
    private VirtualService virtualService;

    public VirCardPresenter(VirtualCardContract.VirCardUnsettledBillView unsettledBillView) {
        this.unsettledBillView = unsettledBillView;
        virtualService = new VirtualService();
    }

    public VirCardPresenter(VirtualCardContract.VirCardView cardView) {
        super();
        this.cardView = cardView;
        virtualService = new VirtualService();
    }

    public VirCardPresenter(VirtualCardContract.VirCardApplyView virCardApplyView) {
        this.applyView = virCardApplyView;
        virtualService = new VirtualService();
    }

    public VirCardPresenter() {
        virtualService = new VirtualService();
    }

    public VirCardPresenter(VirtualCardContract.VirBillView virBillView) {
        this.virBillView = virBillView;
        virtualService = new VirtualService();
    }

    public VirCardPresenter(VirtualCardContract.VirCardCancelView virCardCancelView) {
        this.virCardCancelView = virCardCancelView;
        virtualService = new VirtualService();
    }

    @Override
    public void psnVirtualVircardListQuery(final AccountBean accountBean) {
        getConversation().flatMap(new Func1<String, Observable<PsnCrcdVirtualCardQueryResult>>() {
            @Override
            public Observable<PsnCrcdVirtualCardQueryResult> call(String conversationId) {
                return virtualService.psnCrcdVirtualCardQuery(ModelUtil.generateQueryVirCardParams(conversationId, accountBean.getAccountId()));
            }
        }).compose(this.<PsnCrcdVirtualCardQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdVirtualCardQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardQueryResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardQueryResult result) {
                        cardView.virtualCardListQuery(ModelUtil.generateVirCardModelList(accountBean, result));
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        cardView.virtualCardListQuery(null);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        if (biiResultErrorException.getErrorMessage().contains("您还未申请虚拟卡")){
                            cardView.virtualCardListQuery(null);
                            return;
                        }
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardApplyInit(final AccountBean creditAccountBean) {
        virtualService.psnCrcdVirtualCardApplyInit(new PsnCrcdVirtualCardApplyInitParams(creditAccountBean.getAccountId()))
                .compose(this.<PsnCrcdVirtualCardApplyInitResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdVirtualCardApplyInitResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardApplyInitResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardApplyInitResult result) {
                        applyView.initApplyVirtual(ModelUtil.generateVirtualModel(creditAccountBean, result));
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardSendMessage(VirtualCardModel model) {
        virtualService.psnCrcdVirtualCardSendMessage(new PsnCrcdVirtualCardSendMessageParams(model.getVirAccountId(), model.getAccountIbkNum()))
                .compose(this.<String>bindToLifecycle()).compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        ToastUtils.show("短信已发送");
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardUnsettledbillSum(final VirtualCardModel model) {
        virtualService.psnCrcdVirtualCardUnsettledbillSum(new PsnCrcdVirtualCardUnsettledbillSumParams(model.getAccountName(), model.getAccountIbkNum()))
                .compose(this.<List<PsnCrcdVirtualCardUnsettledbillSumResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCrcdVirtualCardUnsettledbillSumResult>>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<List<PsnCrcdVirtualCardUnsettledbillSumResult>>() {
                    @Override
                    public void onNext(List<PsnCrcdVirtualCardUnsettledbillSumResult> results) {
                        List<VirtualBillModel> models = ModelUtil.generateVirtualBillModels(results);
                        virBillView.queryUnsettledbillSum(models);

                        //判断是否有未出账单
                        if (models.isEmpty())
                            psnCrcdVirtualCardSettledbillQuery(model, false);
                        else
                            psnCrcdVirtualCardSettledbillQuery(model, true);
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardUnsettledbillQuery(final VirtualCardModel model, final int currentPage) {
        getConversation().flatMap(new Func1<String, Observable<PsnCrcdVirtualCardUnsettledbillQueryResult>>() {
            @Override
            public Observable<PsnCrcdVirtualCardUnsettledbillQueryResult> call(String conversationId) {
                return virtualService.psnCrcdVirtualCardUnsettledbillQuery(ModelUtil.generateUnsettledBillParams(conversationId, model, currentPage));
            }
        }).compose(this.<PsnCrcdVirtualCardUnsettledbillQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdVirtualCardUnsettledbillQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardUnsettledbillQueryResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardUnsettledbillQueryResult result) {
                        if (result != null && result.getRecordNumber() > 0)
                            unsettledBillView.queryUnsettledBill(ModelUtil.generateBillTransModels(result.getCrcdTransactionList()), result.getRecordNumber());
                        else
                            unsettledBillView.queryUnsettledBill(null, 0);
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardSettledbillQuery(final VirtualCardModel model, boolean isHadUnsettled) {
        //生成已出账单Map,方便后面按照账单日期排序
        Map<LocalDate, List<VirtualBillModel>> map = ModelUtil.generateVirtualCardSettledMap(isHadUnsettled);

        //并发账单月份列表请求,获取已出账单
        Observable.just(ModelUtil.generateVirtualCardSettledDate(map.keySet()))
                .subscribeOn(Schedulers.io())
                .compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<String, Observable<List<PsnCrcdVirtualCardSettledbillQueryResult>>>() {
                    @Override
                    public Observable<List<PsnCrcdVirtualCardSettledbillQueryResult>> call(String date) {
                        return virtualService.psnCrcdVirtualCardSettledbillQuery(ModelUtil.generateVirtualCardSettledbillQueryParams(model, date));
                    }
                }))
                .compose(this.<List<PsnCrcdVirtualCardSettledbillQueryResult>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new VirtualSettledbillSubscriber(map));
    }

    @Override
    public void psnCrcdVirtualCardCancel(final VirtualCardModel model) {
        getToken().flatMap(new Func1<String, Observable<PsnCrcdVirtualCardCancelResult>>() {
            @Override
            public Observable<PsnCrcdVirtualCardCancelResult> call(String token) {
                return virtualService.psnCrcdVirtualCardCancel(ModelUtil.generateVirtualCardCancelParams(getConversationId(), token, model));
            }
        }).compose(this.<PsnCrcdVirtualCardCancelResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdVirtualCardCancelResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardCancelResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardCancelResult result) {
                        if (result != null)
                            virCardCancelView.psnCrcdVirtualCardCancel(true);
                    }
                });
    }

    /**
     * 已出账单回调逻辑处理
     *
     * @author wangyang
     * @time 16/9/3 22:21
     */
    private class VirtualSettledbillSubscriber extends BaseAccountSubscriber<List<PsnCrcdVirtualCardSettledbillQueryResult>> {

        //已出账单Map
        private Map<LocalDate, List<VirtualBillModel>> map;

        public VirtualSettledbillSubscriber(Map<LocalDate, List<VirtualBillModel>> map) {
            this.map = map;
        }

        @Override
        public void commonHandleException(BiiResultErrorException biiResultErrorException) {
        }

        @Override
        public void onNext(List<PsnCrcdVirtualCardSettledbillQueryResult> results) {
            if (results == null || results.isEmpty())
                return;

            //生成VirtualBillModel,并保存至Map
            List<VirtualBillModel> models = ModelUtil.generateVirtualBillModel(results);
            map.put(models.get(0).getBillDate(), models);
        }

        @Override
        public void onCompleted() {
            List<List<VirtualBillModel>> models = new ArrayList<>();

            //将所有账单保存至集合
            for (List<VirtualBillModel> virModels : map.values()) {
                if (virModels != null && !virModels.isEmpty())
                    models.add(virModels);
            }

            if (models.isEmpty())
                virBillView.querySettledbill(null);
            else
                //回调界面
                virBillView.querySettledbill(models);
        }
    }

}
