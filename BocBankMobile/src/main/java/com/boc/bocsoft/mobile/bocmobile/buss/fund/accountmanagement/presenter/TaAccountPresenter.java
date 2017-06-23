package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail.PsnQueryTaAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail.PsnQueryTaAccountDetailResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccount.TaAccountContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

/**
 * 基金-账户管理-TA子页面Presenter
 * Created by lyf7084 on 2016/11/30.
 */

public class TaAccountPresenter extends RxPresenter implements TaAccountContract.Presenter {

    private TaAccountContract.View mContractView;
    private FundService mFundService;

    public TaAccountPresenter(TaAccountContract.View view) {
        mContractView = view;

//        mContractView.setPresenter(this);
        mFundService = new FundService();
    }


    @Override
    public void queryTaAccList() {
        PsnQueryTaAccountDetailParams params = new PsnQueryTaAccountDetailParams();
        mFundService.PsnQueryTaAccountDetail(params)
                .compose(this.<List<PsnQueryTaAccountDetailResultBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnQueryTaAccountDetailResultBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnQueryTaAccountDetailResultBean>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryTaAccListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnQueryTaAccountDetailResultBean> result) {
                        TaAccountModel viewModel = new TaAccountModel();
                        // 为viewModel中的list赋值
                        List<TaAccountModel.TaAccountBean> list = new LinkedList<TaAccountModel.TaAccountBean>();
                        // 循环读取result列表中的每一项，赋值到list中
                        for (PsnQueryTaAccountDetailResultBean resBean : result) {
                            TaAccountModel.TaAccountBean bean = viewModel.new TaAccountBean();
                            bean.setTaAccountNo(resBean.getTaAccountNo());
                            bean.setFundRegName(resBean.getFundRegName());
                            bean.setFundRegCode(resBean.getFundRegCode());
                            bean.setAccountStatus(resBean.getAccountStatus());
                            bean.setIsPosition(resBean.getIsPosition());
                            bean.setIsTrans(resBean.getIsTrans());
                            list.add(bean);
                        }
                        viewModel.setTaAccountList(list);

                        //页面回调方法，viewModel是把服务端返回的数据解析到view层
                        mContractView.queryTaAccListSuccess(viewModel);
                    }
                });
    }

}