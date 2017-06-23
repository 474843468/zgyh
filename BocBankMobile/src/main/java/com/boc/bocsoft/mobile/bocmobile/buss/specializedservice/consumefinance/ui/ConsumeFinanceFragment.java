package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.ConsumefinanceConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.adpter.LoanListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.module.LoanTypeItemModle;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.module.MyScrollView;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.module.consumeListview;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcy7065 on 2016/11/1.
 * 消费金融主界面
 */
public class ConsumeFinanceFragment extends BussFragment implements LoanListAdapter.ILoanListListener,MyScrollView.OnScrollListener{
    private List<LoanTypeItemModle> loantypes = new ArrayList<LoanTypeItemModle>();
    private  View viewConsumeFinance;
    private ImageView imgWhitebackIcon;
    private ImageView imgBgCloud;
    private MyScrollView svConsumeFinance;
    private consumeListview lvLoanType;
    private LoanListAdapter loanAdapter;
    private LinearLayout btnStudyloan;
    private LinearLayout btnCarloan;
    private LinearLayout btnAssetloan;
    private int screenWidth;
    private TextView tvConsumeFinanceDescrip;
    private  String strConsumeFinanceLoanDescrip,strpledgeLoanDescrip,strELoanDescrip,strRecyleLoanDescrip;
    private  String strpledgeLoanTitle,strELoanTitle,strRecyleLoanTitle;

    @Override
    protected  View onCreateView(LayoutInflater mInflater){
        viewConsumeFinance = mInflater.inflate(R.layout.fargment_consumefinance,null);
        return viewConsumeFinance;
    }

    @Override
    public void initView() {
        btnStudyloan = (LinearLayout)  viewConsumeFinance.findViewById(R.id.other_liuxue);
        btnCarloan = (LinearLayout)  viewConsumeFinance.findViewById(R.id.other_car);
        btnAssetloan = (LinearLayout)  viewConsumeFinance.findViewById(R.id.other_asset);
        lvLoanType =(consumeListview) viewConsumeFinance.findViewById(R.id.loanType_list);
        svConsumeFinance = (MyScrollView)viewConsumeFinance.findViewById(R.id.first_scrollview) ;
        imgWhitebackIcon = (ImageView) viewConsumeFinance.findViewById(R.id.left_white);
        tvConsumeFinanceDescrip =(TextView) viewConsumeFinance.findViewById(R.id.tv_info);
        imgBgCloud = (ImageView) viewConsumeFinance.findViewById(R.id.bg_consumefinance_cloud);
    }

    @Override
    public void initData() {
        loanAdapter =  new LoanListAdapter(getActivity(),R.layout.boc_consumefinance_itemloantype,loantypes);
        loanAdapter.setListener(this);
        svConsumeFinance.setOnScrollListener(this);

        strConsumeFinanceLoanDescrip = getResources().getString(R.string.consumefinance_loandescrip1);
        strpledgeLoanDescrip = getResources().getString(R.string.consumefinance_loaninfo_pledge);
        strELoanDescrip = getResources().getString(R.string.consumefinance_loaninfo_eloan);
        strRecyleLoanDescrip = getResources().getString(R.string.consumefinance_loaninfo_recyle);
        strpledgeLoanTitle = getResources().getString(R.string.consumefinance_loantype_pledge);
        strELoanTitle = getResources().getString(R.string.consumefinance_loantype_eloan);
        strRecyleLoanTitle = getResources().getString(R.string.consumefinance_loantype_recyle);
        SpannableString spann = new SpannableString(strConsumeFinanceLoanDescrip);
        spann.setSpan(new CharacterStyle() {
            @Override
            public void updateDrawState(TextPaint tp) {
                // tp.setColor(0xffff0000);
                tp.setFakeBoldText(true);
            }
        },23,31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvConsumeFinanceDescrip.setText(spann);

        initLoanTypeList();
        getScreenWidth();
        lvLoanType.setAdapter(loanAdapter);
        svConsumeFinance.smoothScrollTo(0,0);
    }

    @Override
    public void setListener() {

        btnStudyloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new StudyaboardLoanFragment());
            }
        });
        btnAssetloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new AssestloanFragment());
            }
        });
        btnCarloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new CarloanFragment());
            }
        });
        imgWhitebackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    private void initLoanTypeList(){
        LoanTypeItemModle first = new LoanTypeItemModle(R.drawable.boc_consumefinance_zhiya_icon,strpledgeLoanTitle,
                strpledgeLoanDescrip,"visable");
        loantypes.add(first);
        LoanTypeItemModle second = new LoanTypeItemModle(R.drawable.boc_consumefinance_eloan_icon,strELoanTitle,
                strELoanDescrip,"visable");
        loantypes.add(second);
        LoanTypeItemModle third = new LoanTypeItemModle(R.drawable.boc_consumefinance_xunhuan_icon,strRecyleLoanTitle,
                strRecyleLoanDescrip,"invisable");
        loantypes.add(third);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    public void getScreenWidth(){
        Resources res = this.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
    }

    @Override
    public void onClick(int position) {
        /*LoanManagerFragment loanManagerFragment = new LoanManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConsumefinanceConst.PAGE_CONSUEMFINANCE,"fromConsumeFinanceloan");
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
                bundle.putString(ConsumefinanceConst.PAGE_CONSUEMFINANCE,"fromConsumeFinanceloan");
                bundle.putBoolean(ConsumefinanceConst.IS_CONSUMEFINANCE, true);
                loanManagerFragment.setArguments(bundle);
                start(loanManagerFragment);
            }
        });


    }

    public void onScroll(int scroll){
        int height = imgBgCloud.getHeight();
        if (scroll > (height*0.86-ResUtils.dip2px(getContext(),(45-(45-23)/2)))){
            imgWhitebackIcon.setImageResource(R.drawable.boc_back_black);
        }else {
            imgWhitebackIcon.setImageResource(R.drawable.boc_back_white);
        }
    }


}
