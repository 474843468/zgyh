package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.presenter;

import android.text.TextUtils;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatementBalanceQuery.PsnFundStatementBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatementBalanceQuery.PsnFundStatementBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnPersionalTransDetailQuery.PsnPersionalTransDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnPersionalTransDetailQuery.PsnPersionalTransDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.PersionaltrsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.StatementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui.StatementContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/23.
 * 基金对账单网络请求处理类
 */
public class StatementPresenter extends RxPresenter implements StatementContract.Presenter {

    /**对账单主页view*/
    private StatementContract.StatementView mStatementView;
    private StatementContract.PersionalTransView mPersionalTransView;
    /**网络请求服务类*/
    private FundService mFundService;

    /**
     * 持仓构造方法
     */
    public StatementPresenter (StatementContract.StatementView statementView){
        mStatementView = statementView;
        mStatementView.setPresenter(this);
        mFundService = new FundService();
    }

    /**
     * 交易流水
     */
    public StatementPresenter (StatementContract.PersionalTransView persionalTransView) {
        mPersionalTransView = persionalTransView;
        mPersionalTransView.setPresenter(this);
        mFundService = new FundService();
    }


    /**
     * 053持仓信息请求
     * @param fundStatementTime 查询时间
     */
    @Override
    public void queryFundStatement(String fundStatementTime) {
        PsnFundStatementBalanceQueryParams params = new PsnFundStatementBalanceQueryParams();
        if (!TextUtils.isEmpty(fundStatementTime)) {
            params.setFundStatementTime(fundStatementTime);
        }
        mFundService.psnFundStatementBalanceQuery(params)
                .compose(this.<PsnFundStatementBalanceQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundStatementBalanceQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundStatementBalanceQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mStatementView.fundStatementFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundStatementBalanceQueryResult psnFundStatementBalanceQueryResult) {
                        StatementModel statementModel= builtFundStatementList(psnFundStatementBalanceQueryResult);
                        mStatementView.fundStatementSuccess(statementModel);
                    }
                });
    }

    /**
     * 059交易流水网络请求
     * @param startDate 开始时间
     * @param endDate 结束时间
     */
    @Override
    public void queryPersionalTrans(String startDate, String endDate) {
        PsnPersionalTransDetailQueryParams params = new PsnPersionalTransDetailQueryParams();
        if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
            params.setEndDate(endDate);
            params.setStartDate(startDate);
        }
        mFundService.psnPersionalTransDetailQuery(params)
                .compose(this.<List<PsnPersionalTransDetailQueryResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnPersionalTransDetailQueryResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnPersionalTransDetailQueryResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mStatementView.fundPersionalTransFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnPersionalTransDetailQueryResult> psnPersionalTransDetailQueryResult) {
                        List<PersionaltrsModel> persionalList = builtPersionalTransList(psnPersionalTransDetailQueryResult);
                        mStatementView.fundPersionalTransSuccess(persionalList);
                    }
                });
    }


    /**
     * 持仓信息数据转换
     * @param result bii数据
     */
    private StatementModel builtFundStatementList(PsnFundStatementBalanceQueryResult  result) {
        StatementModel statementModel = null;
        if (result != null) {
            statementModel = BeanConvertor.toBean(result, new StatementModel());
        }
        return statementModel;
    }

    /**
     * 交易流水数据转换
     * @param result 返回参数
     */
    private List<PersionaltrsModel> builtPersionalTransList(List<PsnPersionalTransDetailQueryResult> result) {
        List<PersionaltrsModel> persionaltrsList = new ArrayList<PersionaltrsModel>();
        if (result != null && result.size() > 0) {
            for (PsnPersionalTransDetailQueryResult persionalList : result) {
                PersionaltrsModel persionaltrsModel = BeanConvertor.toBean(persionalList, new PersionaltrsModel());
                persionaltrsList.add(persionaltrsModel);
            }
        }
        return persionaltrsList;
    }
}
