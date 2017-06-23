package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadHisTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter.HistoryTransPresenter;

/**
 * Fragment：中银理财-交易查询-历史交易页面
 * Created by zhx on 2016/9/7
 */
public class HistoryTransFragment extends BussFragment implements HistoryTransContact.View {

    private View rootView;
    private HistoryTransPresenter historyTransPresenter;
    private XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_history_trans, null);

        Log.e("ljljlj", "001");
        return rootView;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        historyTransPresenter = new HistoryTransPresenter(this);

        //        XpadHisTradStatusViewModel viewModel = generateViewModel();
        //        historyTransPresenter.psnXpadHisTradStatus(viewModel);

//        xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
//        historyTransPresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);

        // 默认进行：历史常规交易状况查询
//        historyTransPresenter.psnXpadHisTradStatus(generateXpadHisTradStatusViewModel());
    }

    private XpadHisTradStatusViewModel generateViewModel() {
        XpadHisTradStatusViewModel viewModel = new XpadHisTradStatusViewModel();

        viewModel.setCurrentIndex("0");
       // viewModel.setStartDate("2019/01/26");
        viewModel.setAccountKey("97483dc7-885f-4f45-a2ad-e60f38e87573");
        viewModel.setAccountType("119");
        viewModel.setPageSize("15");
        viewModel.setXpadProductCurCode("000");
       // viewModel.setEndDate("2019/02/01");
        viewModel.setIbknum("40740");
        viewModel.set_refresh("true");

        return viewModel;
    }

    @Override
    public void setListener() {
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void psnXpadHisTradStatusSuccess(XpadHisTradStatusViewModel xpadHisTradStatusViewModel) {

    }

    @Override
    public void psnXpadHisTradStatusFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void psnXpadRecentAccountQuerySuccess(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {

        // 默认进行：历史常规交易状况查询
        historyTransPresenter.psnXpadHisTradStatus(generateXpadHisTradStatusViewModel());
    }

    private XpadHisTradStatusViewModel generateXpadHisTradStatusViewModel() {
        XpadHisTradStatusViewModel viewModel = new XpadHisTradStatusViewModel();

        viewModel.setAccountKey("2136f49a-afa8-4fdd-b1d5-86aa9a1a8c9d");
        viewModel.setXpadProductCurCode("000");
        //viewModel.setStartDate("2000/01/26");
        //viewModel.setEndDate("2019/02/01");
        viewModel.setPageSize("15");
        viewModel.setCurrentIndex("0");
        viewModel.set_refresh("true");
        viewModel.setAccountType("119");
        viewModel.setIbknum("43016");

        return viewModel;
    }

    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void setPresenter(HistoryTransContact.Presenter presenter) {

    }
}
