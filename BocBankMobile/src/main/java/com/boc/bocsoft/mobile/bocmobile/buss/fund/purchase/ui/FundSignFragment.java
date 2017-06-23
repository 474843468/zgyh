package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model.FundbuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.presenter.FundPurchasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by zcy7065 on 2016/12/2.
 */
@SuppressLint("ValidFragment")
public class FundSignFragment extends MvpBussFragment<FundPurchaseContract.FundPurchasePresenter> implements FundPurchaseContract.SignContractView,View.OnClickListener {

    private View rootView;
    private Button btnNext;
    private CheckBox cbAgree;
    private SpannableString ssAgreement;
    private FundbuyModel signContractModel;

    @Override
    protected  View onCreateView(LayoutInflater mInflater){
        rootView = mInflater.inflate(R.layout.boc_fund_sign_eletronic_contract,null);
        return rootView;
    }


    public FundSignFragment(FundbuyModel model){
        this.signContractModel = model;
    }

    public void initView(){
        btnNext = (Button)rootView.findViewById(R.id.btn_next);
        cbAgree = (CheckBox) rootView.findViewById(R.id.cb_agree);
        ssAgreement = (SpannableString) rootView.findViewById(R.id.ss_agreement);
    }

    @Override
    public void setListener(){
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v == btnNext)
           getPresenter().signContractSubmit(signContractModel);
    }

    @Override
    public void signContractSubmit(FundbuyModel model) {
        signContractModel = model;
        super.titleLeftIconClick();
    }

    @Override
    protected FundPurchaseContract.FundPurchasePresenter initPresenter() {
        return new FundPurchasePresenter(this);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

}
