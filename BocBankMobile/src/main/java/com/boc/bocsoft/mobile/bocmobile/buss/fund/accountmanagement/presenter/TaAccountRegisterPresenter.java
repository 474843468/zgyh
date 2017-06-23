package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundRegCompanyList.PsnGetFundRegCompanyListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundRegCompanyList.PsnGetFundRegCompanyListResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.FundRegCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccountRegisterContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

public class TaAccountRegisterPresenter extends RxPresenter implements TaAccountRegisterContract.Presenter {

    private TaAccountRegisterContract.View mContractView;
    private FundService mFundService;
    private GlobalService mGlobalService;

    public TaAccountRegisterPresenter(TaAccountRegisterContract.View view) {
        mContractView = view;
        mFundService = new FundService();
        mGlobalService = new GlobalService();

    }

    @Override
    public void queryFundCoList() {
        PsnGetFundRegCompanyListParams params = new PsnGetFundRegCompanyListParams();
        mFundService.PsnGetFundRegCompanyList(params)
                .compose(this.<List<PsnGetFundRegCompanyListResultBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnGetFundRegCompanyListResultBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnGetFundRegCompanyListResultBean>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryFundCoListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnGetFundRegCompanyListResultBean> result) {
                        FundRegCompanyModel viewModel = new FundRegCompanyModel();
                        // 为viewModel中的list赋值
                        List<FundRegCompanyModel.RegCompanyBean> list = new LinkedList<FundRegCompanyModel.RegCompanyBean>();
                        // 循环读取result列表中的每一项，赋值到list中
                        for (PsnGetFundRegCompanyListResultBean resBean : result) {
                            FundRegCompanyModel.RegCompanyBean bean = viewModel.new RegCompanyBean();
                            bean.setFundRegCode(resBean.getFundRegCode());
                            bean.setFundRegName(resBean.getFundRegName());
                            list.add(bean);
                        }
                        viewModel.setList(list);

                        //页面回调方法，viewModel是把服务端返回的数据解析到view层
                        mContractView.queryFundCoListSuccess(viewModel);
                    }
                });
    }

}