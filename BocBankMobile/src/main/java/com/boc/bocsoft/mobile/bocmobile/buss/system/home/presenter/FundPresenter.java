package com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.fundqueryoutlay.FundQueryOutlayParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.FundContract;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 基金列表
 * Created by gwluo on 2016/8/29.
 */
public class FundPresenter extends RxPresenter implements FundContract.Presenter {
    FundContract.View mView;
    FundService mFundService;

    public FundPresenter(FundContract.View view) {
        mView = view;
        mFundService = new FundService();
    }

    @Override
    public void getFundList(final FundQueryOutlayParams params) {
        PsnFundQueryOutlayParams paramsCppy = new PsnFundQueryOutlayParams();
        paramsCppy.setPageSize(params.getPageSize() + "");
        paramsCppy.setCurrentIndex(params.getCurrentIndex() + "");
        paramsCppy.setSortField(params.getSortField());
        paramsCppy.setSortFlag(params.getSortFlag());
        paramsCppy.setCompany(params.getCompany());
        paramsCppy.setCurrencyCode(params.getCurrencyCode());
        paramsCppy.setFundInfo(params.getFundInfo());
        paramsCppy.setFundKind(params.getFundKind());
        paramsCppy.setFundState(params.getFundState());
        paramsCppy.setFundType(params.getFundType());
        paramsCppy.setRiskGrade(params.getRiskGrade());
        mFundService.psnFundQueryOutlay(paramsCppy)
                .compose(this.<PsnFundQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.updateFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryOutlayResult psnFundQueryOutlayResult) {
                        if (psnFundQueryOutlayResult != null) {
                            params.getList().clear();
                            params.setRecordNumber(Integer.valueOf(
                                    psnFundQueryOutlayResult.getRecordNumber()));
                            if (!PublicUtils.isEmpty(psnFundQueryOutlayResult.getList())) {
                                if (params.getCurrentIndex() == 0) {
                                    params.getList().clear();
                                }
                                for (PsnFundQueryOutlayResult.ListBean listBean : psnFundQueryOutlayResult
                                        .getList()) {
                                    FundBean fundBean = new FundBean();
                                    fundBean.setFundName(listBean.getFundName());
                                    fundBean.setFundCode(listBean.getFundCode());
                                    params.getList().add(fundBean);
                                }
                            }
                            mView.updateFundView(params);
                        }
                    }
                });
    }
}