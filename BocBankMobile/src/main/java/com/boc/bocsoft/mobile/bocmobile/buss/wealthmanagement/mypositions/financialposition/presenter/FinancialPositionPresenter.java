package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

/**
 * 中银理财-我的持仓-持仓列表-Presenter
 * Created by yx on 2016/9/16.
 */
public class FinancialPositionPresenter extends RxPresenter implements FinancialPositionContract.FinancialpositionPresenter {

    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 公共service
     */
    private GlobalService globalService;

    /**
     * 持仓列表-接口回调
     */
    private FinancialPositionContract.FinancialPositionView mFinancialPositionView;
    /***
     * 持仓列表-model 转换工具类
     */
    private FinancialPositionCodeModeUtil mFinancialPositionCodeModeUtil;


    /**
     * 构造器，做初始化
     *
     * @param mFinancialPositionView
     */
    public FinancialPositionPresenter(FinancialPositionContract.FinancialPositionView mFinancialPositionView) {
        this.mFinancialPositionView = mFinancialPositionView;
        mWealthManagementService = new WealthManagementService();
        globalService = new GlobalService();
    }

    /**
     * 初始化
     */
    private void init() {

    }

    @Override
    public void getPSNCreatConversation() {
        PSNCreatConversationParams mParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(mParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String s) {
                        mFinancialPositionView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialPositionView.obtainConversationFail();
                    }
                });
    }

    @Override
    public void getPsnXpadProductBalanceQuery() {
        PsnXpadProductBalanceQueryParams params = new PsnXpadProductBalanceQueryParams();
        mWealthManagementService.psnXpadProductBalanceQuery(params)
                .compose(this.<List<PsnXpadProductBalanceQueryResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnXpadProductBalanceQueryResult>>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnXpadProductBalanceQueryResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialPositionView.obtainPsnXpadProductBalanceFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnXpadProductBalanceQueryResult> psnXpadProductBalanceQueryResults) {
                        mFinancialPositionView.obtainPsnXpadProductBalanceSuccess(mFinancialPositionCodeModeUtil.transverterPsnXpadProductBalanceQueryResModel(psnXpadProductBalanceQueryResults));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }

    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery
     *
     * @param productCode  产品代码
     * @param ibknum{省行联行号 String	O
     *                     返回项需展示(剩余额度、工作时间、挂单时间)，此项必输
     *                     根据PsnXpadAccountQuery接口的返回项进行上送}
     * @param productKind  产品性质
     */
    @Override
    public void getPsnXpadProductDetailQuery(String productCode, String ibknum, String productKind) {

        PsnXpadProductDetailQueryParams params = new PsnXpadProductDetailQueryParams();
        params.setProductCode(productCode);
        params.setIbknum(ibknum);
        params.setProductKind(productKind);
        mWealthManagementService.psnXpadProductDetailQuery(params)
                .compose(this.<PsnXpadProductDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProductDetailQueryResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadProductDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialPositionView.obtainPsnXpadProductDetailQueryFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadProductDetailQueryResult mPsnXpadProductDetailQueryResult) {
                        mFinancialPositionView.obtainPsnXpadProductDetailQuerySuccess(mFinancialPositionCodeModeUtil.transverterPsnXpadProductDetailQueryResModel(mPsnXpadProductDetailQueryResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }

    /**
     * I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery
     *
     * @param xpadAccountSatus 账户状态	String	O	0：停用 1：可用 不输代表查询全部
     * @param queryType        查询类型	String	必输	0：查询所有已登记的理财账户  1、查询所有已登记并且关联到网银的理财账户
     */
    @Override
    public void getPsnXpadAccountQuery(String xpadAccountSatus, String queryType) {
        PsnXpadAccountQueryParams params = new PsnXpadAccountQueryParams();
        params.setXpadAccountSatus(xpadAccountSatus);
        params.setQueryType(queryType);
        mWealthManagementService.psnXpadAccountQuery(params)
                .compose(this.<PsnXpadAccountQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialPositionView.obtainPsnXpadProductDetailQueryFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult mPsnXpadAccountQueryResult) {
                        mFinancialPositionView.obtainPsnXpadAccountQuerySuccess(mFinancialPositionCodeModeUtil.transverterPsnXpadAccountQueryResModel(mPsnXpadAccountQueryResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }


}
