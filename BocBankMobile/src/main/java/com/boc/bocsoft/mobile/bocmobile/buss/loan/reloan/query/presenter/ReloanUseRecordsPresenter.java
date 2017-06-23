package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.presenter;


import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery.PsnLOANUseRecordsQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery.PsnLOANUseRecordsQueryResultBean;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.model.ReloanUseRecordsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanUseRecordsContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.util.LinkedList;
import java.util.List;

/**
 * 分次动用贷款-用款记录查询 通信逻辑处理
 * Created by liuzc on 2016/8/16.
 */
public class ReloanUseRecordsPresenter extends RxPresenter implements ReloanUseRecordsContract.Presenter {

    private ReloanUseRecordsContract.View mContractView;
    private PsnLoanService mLoanService;

    /**
     * 查询转账记录service
     */

    public ReloanUseRecordsPresenter(ReloanUseRecordsContract.View loanOnlineQryView){
        mContractView = loanOnlineQryView;
        mContractView.setPresenter(this);
        mLoanService = new PsnLoanService();
    }

    @Override
    public void queryUseRecords(ReloanUseRecordsViewModel viewModel) {
        mLoanService.psnLOANUseRecordsQueryList(buildParamsFromView(viewModel))
                .compose(this.<List<PsnLOANUseRecordsQueryResultBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnLOANUseRecordsQueryResultBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnLOANUseRecordsQueryResultBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnLOANUseRecordsQueryResultBean> psnLOANOverdueResult) {
                        ReloanUseRecordsViewModel viewModel = buildViewModelFromResult(psnLOANOverdueResult);
                        mContractView.queryUseRecordsSuccess(viewModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryUseRecordsFail(biiResultErrorException);
                    }
                });
    }



    //根据屏幕数据构造请求参数
    private PsnLOANUseRecordsQueryParams buildParamsFromView(ReloanUseRecordsViewModel viewModel){
        PsnLOANUseRecordsQueryParams result = new PsnLOANUseRecordsQueryParams();

        result.setStartDate(viewModel.getStartDate());
        result.setEndDate(viewModel.getEndDate());
        result.setLoanActNum(viewModel.getLoanActNum());
        result.setPageSize(viewModel.getPageSize());
        result.setCurrentIndex(viewModel.getCurrentIndex());

        return result;
    }

    //根据网络返回结果构造屏幕显示数据
    private ReloanUseRecordsViewModel buildViewModelFromResult(List<PsnLOANUseRecordsQueryResultBean> qryResult){
        ReloanUseRecordsViewModel result = new ReloanUseRecordsViewModel();

        List<ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean> viewList = new LinkedList<>();
        List<PsnLOANUseRecordsQueryResultBean> returnList = qryResult;
        if(returnList != null){
            ReloanUseRecordsViewModel viewModel = new ReloanUseRecordsViewModel();
            for(int i = 0; i < returnList.size(); i ++){
                ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean viewBean =
                        viewModel.new PsnLOANUseRecordsQueryBean();
                PsnLOANUseRecordsQueryResultBean returnBean = returnList.get(i);

                viewBean.setLoanApplyAmount(returnBean.getLoanApplyAmount());
                viewBean.setLoanApplyDate(returnBean.getLoanApplyDate());
                viewBean.setLoanApplyId(returnBean.getLoanApplyId());
                viewBean.setRetnum(returnBean.getRetnum());
                viewBean.setTotnumq(returnBean.getTotnumq());
                viewBean.setChannel(returnBean.getChannel());
                viewBean.setMerchant(returnBean.getMerchant());
                viewList.add(viewBean);
            }
        }
        result.setResult(viewList);

        return result;
    }
}
