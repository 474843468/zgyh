package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.ConsumefinanceConst;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * Created by zcy7065 on 2016/11/2.
 * 汽车贷介绍页面
 */
public class CarloanFragment extends BussFragment {
    private ImageView imgBlackbackIcon;
    private Button btnApplyLoan;
    private  View mRootView;
  /*  @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState){
        mRootView = inflater.inflate(R.layout.fragment_studyloan,container,false);
        return mRootView;
    }
*/

    protected  View onCreateView(LayoutInflater mInflater){
        mRootView = mInflater.inflate(R.layout.fragment_carloan,null);
        return mRootView;
    }

    @Override
    public void initView(){
        View title = mRootView.findViewById(R.id.firsttitle);
        btnApplyLoan = (Button)mRootView.findViewById(R.id.car_btn) ;
        imgBlackbackIcon = (ImageView)title.findViewById(R.id.left_black);
    }

    @Override
    public void setListener() {
        //title左侧返回按钮
        imgBlackbackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
        btnApplyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*LoanManagerFragment loanManagerFragment = new LoanManagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ConsumefinanceConst.PAGE_CONSUEMFINANCE,"fromCarloan");
                bundle.putBoolean(ConsumefinanceConst.IS_CONSUMEFINANCE, true);
                loanManagerFragment.setArguments(bundle);
                start(loanManagerFragment);*/
                if(ApplicationContext.isLogin()){
                    LoanManagerFragment loanManagerFragment = new LoanManagerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(ConsumefinanceConst.PAGE_CONSUEMFINANCE,"fromConsumeFinanceloan");
                    bundle.putBoolean(ConsumefinanceConst.IS_CONSUMEFINANCE, true);
                    loanManagerFragment.setArguments(bundle);
                    start(loanManagerFragment);
                    return;
                }
                ModuleActivityDispatcher.startToLogin(ActivityManager.getAppManager().currentActivity(), new LoginCallback() {
                    @Override
                    public void success() {
                        LoanManagerFragment loanManagerFragment = new LoanManagerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(ConsumefinanceConst.PAGE_CONSUEMFINANCE,"fromCarloan");
                        bundle.putBoolean(ConsumefinanceConst.IS_CONSUMEFINANCE, true);
                        loanManagerFragment.setArguments(bundle);
                        start(loanManagerFragment);
                    }
                });

            }
        });

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }


}
