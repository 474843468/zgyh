package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.model.OpenWealthModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.presenter.OpenWealthPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;

/**
 * 关闭投资理财服务
 * Created by wangtong on 2016/10/21.
 * Modified by liuweidong on 2016/11/24
 */
@SuppressLint("ValidFragment")
public class CloseWealthManagerFragment extends MvpBussFragment<OpenWealthPresenter> implements OpenWealthManagerContact.CloseView {

    protected TextView btnClose;
    private View rootView = null;
    public OpenWealthModel mViewModel;
    public OpenWealthPresenter presenter;
    private Class<? extends BussFragment> fromeClass;

    public CloseWealthManagerFragment(Class<? extends BussFragment> clazz) {
        fromeClass = clazz;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_close_wealth_manager, null);
        return rootView;
    }

    @Override
    public void initView() {
        btnClose = (TextView) rootView.findViewById(R.id.btn_open);
    }

    @Override
    public void initData() {
        mViewModel = new OpenWealthModel();
        presenter = new OpenWealthPresenter(this);
    }

    @Override
    public void setListener() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.psnInvestmentManageCancel();
            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(fromeClass);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "开通投资理财";
    }

    @Override
    protected OpenWealthPresenter initPresenter() {
        return new OpenWealthPresenter(this);
    }

    @Override
    public boolean onBack() {
        popToAndReInit(fromeClass);
        return super.onBack();
    }

    @Override
    public void psnInvestmentManageCancelReturned() {
        boolean[] status = WealthProductFragment.getInstance().isOpenWealth();
        status[0] = false;
        WealthProductFragment.getInstance().setOpenWealth(status);
        popToAndReInit(fromeClass);
    }
}
