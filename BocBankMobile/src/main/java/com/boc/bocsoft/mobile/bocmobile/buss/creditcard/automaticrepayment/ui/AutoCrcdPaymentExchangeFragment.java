package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.model.AutoCrcdPayModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter.AutoCrcdPaymentContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter.AutoCrcdPaymentPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: liukai
 * Time：2016/11/25 16:51.
 * Created by lk7066 on 2016/11/25.
 * It's used to 信用卡自动还款修改设置页面
 */

public class AutoCrcdPaymentExchangeFragment extends MvpBussFragment<AutoCrcdPaymentContract.AutoCrcdPaymentPresenter> implements AutoCrcdPaymentContract.AutoCrcdPaymentView{

    private View rootView;
    //四个选择框所占空间
    private LinearLayout infoPaymentMoney, infoPaymentway, infoLocalAccount, infoForeignAccount;//上下两个框
    //确认按钮
    private Button btnYes;
    //下方提示语，目前该页面没有提示语，此处保留，防止后期修改添加
    private TextView tvAutoPayInfo;
    //四个选择框
    private EditChoiceWidget autoPaymentMoney, autoPaymentWay, autoPaymentLocalAccount, autoPaymentForeignAccount;//每行元素
    //点击选择框时弹框
    private SelectStringListDialog mPaymentMoney, mPaymentWay;
    //private String mMoney = "", mWay = "", mAccount = "";
    //还款方式列表
    private List<String> autoPayWay = new ArrayList<String>();
    private AutoCrcdPaymentContract.AutoCrcdPaymentPresenter mPresenter;
    private AutoCrcdPayModel autoCrcdPayModel;
    //不同还款账户选择标识
    private int accountChoiceStatus = 0;  //还款账户选择，1人民币账户，2外币账户

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        autoCrcdPayModel = new AutoCrcdPayModel();
        autoCrcdPayModel = getArguments().getParcelable("AutoPayment");
        rootView = mInflater.inflate(R.layout.fragment_autopayment_exchange, null);
        return rootView;
    }

    @Override
    public void initView() {
        infoPaymentMoney = (LinearLayout) rootView.findViewById(R.id.lyt_info_paymentmoney);
        infoPaymentway = (LinearLayout) rootView.findViewById(R.id.lyt_info_paymentway);
        infoLocalAccount = (LinearLayout) rootView.findViewById(R.id.lyt_info_localaccount);
        infoForeignAccount = (LinearLayout) rootView.findViewById(R.id.lyt_info_foreignaccount);
        btnYes = (Button) rootView.findViewById(R.id.btn_autocard_yes);
        tvAutoPayInfo = (TextView) rootView.findViewById(R.id.txt_autocard_info);
        tvAutoPayInfo.setTextColor(getResources().getColor(R.color.boc_text_color_gray));
        btnYes.setText(getResources().getString(R.string.boc_crcd_autopay_confirmrighttext));

        initViewVisible();//初始化元素
        LoadAutoPaymentInfo();//点击操作

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCrcdPaymentConfirmFragment autoCrcdPaymentConfirmFragment = new AutoCrcdPaymentConfirmFragment();
                Bundle bundle = new Bundle();
                statusToExchange();
                bundle.putParcelable("AutoPayConfirm", autoCrcdPayModel);
                bundle.putInt("AutoStatus", 0);
                autoCrcdPaymentConfirmFragment.setArguments(bundle);
                if(writingNotNull()){
                    start(autoCrcdPaymentConfirmFragment);
                }
            }
        });
    }

    public void statusToExchange(){
        if(1 == autoCrcdPayModel.getmCrcdStatus()){
            autoCrcdPayModel.setRepayType("1");
        } else {
            autoCrcdPayModel.setRepayType("0");
        }
        if(1 == autoCrcdPayModel.getmPaymentModeStatus()){
            autoCrcdPayModel.setAutoRepayMode("FULL");
        } else if(2 == autoCrcdPayModel.getmPaymentModeStatus()){
            autoCrcdPayModel.setAutoRepayMode("MINP");
        } else {
            autoCrcdPayModel.setAutoRepayMode("-1");
        }
        if(0 == autoCrcdPayModel.getmPaymentWay()){
            autoCrcdPayModel.setRepayCurSel("0");
            autoCrcdPayModel.setRepayAcctId(autoCrcdPayModel.getLocalAccountId());
        } else if(1 == autoCrcdPayModel.getmPaymentWay()){
            autoCrcdPayModel.setRepayCurSel("1");
            autoCrcdPayModel.setRmbRepayAcctId(autoCrcdPayModel.getLocalAccountId());
            autoCrcdPayModel.setForeignRepayAcctId(autoCrcdPayModel.getForeignAccountId());
        } else if(2 == autoCrcdPayModel.getmPaymentWay()){
            autoCrcdPayModel.setRepayCurSel("2");
            autoCrcdPayModel.setSignForeignCurrencyAcctId(autoCrcdPayModel.getLocalAccountId());
        } else {
            autoCrcdPayModel.setRepayCurSel("-1");
        }
    }

    public boolean writingNotNull(){
        if(1 != autoCrcdPayModel.getmCurrencyStatus() && autoPaymentWay.getChoiceContentTextView().getText().equals(getResources().getString(R.string.boc_crcd_autopay_choose))){
            //非单人民币的情况下，有还款方式，单人民币没有还款方式选择
            showErrorDialog(getResources().getString(R.string.boc_crcd_paymentway_errortext));
            return false;
        }
        if(autoPaymentLocalAccount.getChoiceTextContent().equals(getResources().getString(R.string.boc_crcd_autopay_choose))) {
            //判断本币账户输入框是否为空
            if(1 == autoCrcdPayModel.getmPaymentWay()){//混合方式
                showErrorDialog(getResources().getString(R.string.boc_crcd_paymentrmbaccount_errortext));
                return false;
            } else {
                showErrorDialog(getResources().getString(R.string.boc_crcd_paymentaccount_errortext));
                return false;
            }
        }
        if(1 == autoCrcdPayModel.getmPaymentWay() && autoPaymentForeignAccount.getChoiceTextContent().equals(getResources().getString(R.string.boc_crcd_autopay_choose))){
            //混合方式还款，判断外币账户输入框是否为空
            showErrorDialog(getResources().getString(R.string.boc_crcd_paymentforeignaccount_errortext));
            return false;
        }
        return true;//全不为空
    }

    public void initViewVisible(){
        if(1 == autoCrcdPayModel.getmCurrencyStatus()){
            infoPaymentway.setVisibility(View.GONE);
        } else {
            infoPaymentway.setVisibility(View.VISIBLE);
        }
        if(1 == autoCrcdPayModel.getmPaymentWay()){
            infoForeignAccount.setVisibility(View.VISIBLE);
        } else {
            infoForeignAccount.setVisibility(View.GONE);
        }
    }

    public String initPyamentMoney(){
        if(1 == autoCrcdPayModel.getmPaymentModeStatus()){
            return getResources().getString(R.string.boc_crcd_autopay_full);
        } else {
            return getResources().getString(R.string.boc_crcd_autopay_minp);
        }
    }

    public String initPaymentWay(){
        if(0 == autoCrcdPayModel.getmPaymentWay()){
            return getResources().getString(R.string.boc_crcd_autopay_rmbrepay);
        } else if(1 == autoCrcdPayModel.getmPaymentWay()){
            return getResources().getString(R.string.boc_crcd_autopay_bothrepay);
        }else {
            return getResources().getString(R.string.boc_crcd_autopay_foreignrepay);
        }
    }

    public String initPaymentForAccount(){
        if(1 == autoCrcdPayModel.getmPaymentWay()){
            return autoCrcdPayModel.getForeignCurrencyAccountNo();
        } else {
            return "";
        }
    }

    public void LoadAutoPaymentInfo(){

        if(true) {
            autoPaymentMoney = new EditChoiceWidget(mContext);
            autoPaymentMoney.setChoiceTitleBold(true);
            autoPaymentMoney.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentamount));
            autoPaymentMoney.setChoiceTextContent(initPyamentMoney());

            autoPaymentMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectMoney();
                }
            });
            infoPaymentMoney.addView(autoPaymentMoney);
        }

        if(true) {
            autoPaymentWay = new EditChoiceWidget(mContext);
            autoPaymentWay.setChoiceTitleBold(true);
            autoPaymentWay.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentway));
            autoPaymentWay.setChoiceTextContent(initPaymentWay());

            autoPaymentWay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectWay();
                }
            });
            infoPaymentway.addView(autoPaymentWay);
        }

        if(true) {
            autoPaymentLocalAccount = new EditChoiceWidget(mContext);
            autoPaymentLocalAccount.setChoiceTitleBold(true);
            if(1 == autoCrcdPayModel.getmPaymentWay()){
                autoPaymentLocalAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentrmbaccount));
            } else {
                autoPaymentLocalAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentaccount));
            }
            autoPaymentLocalAccount.setChoiceTextContent(NumberUtils.formatCardNumber(autoCrcdPayModel.getLocalCurrencyPaymentAccountNo()));

            autoPaymentLocalAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountChoiceStatus = 1;  //选择人民币账户
                    getSelectAccount();
                }
            });
            infoLocalAccount.addView(autoPaymentLocalAccount);
        }

        if(true) {
            autoPaymentForeignAccount = new EditChoiceWidget(mContext);
            autoPaymentForeignAccount.setChoiceTitleBold(true);
            autoPaymentForeignAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentforeignaccount));
            autoPaymentForeignAccount.setChoiceTextContent(NumberUtils.formatCardNumber(initPaymentForAccount()));

            autoPaymentForeignAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountChoiceStatus = 2;  //选择外币账户
                    getSelectAccount();
                }
            });
            infoForeignAccount.addView(autoPaymentForeignAccount);
        }
    }

    @Override
    public void initData() {
        mPresenter = new AutoCrcdPaymentPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_autopay_change);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    private void selectMoney() {
        if (mPaymentMoney == null) {
            mPaymentMoney = new SelectStringListDialog(mContext);
            mPaymentMoney.setListData(AutoCrcdPaymentConst.autoPayMoney);
            mPaymentMoney.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    autoPaymentMoney.setChoiceTextContent(model);
                    //mMoney = String.valueOf(position + 1);
                    mPaymentMoney.dismiss();
                    autoCrcdPayModel.setmPaymentModeStatus(position + 1);
                }
            });
        }

        mPaymentMoney.show();
    }

    private void selectWay() {
        if (mPaymentWay == null) {
            mPaymentWay = new SelectStringListDialog(mContext);
            if(0 == autoCrcdPayModel.getmCurrencyStatus()){//单外币
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_rmbrepay));
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_foreignrepay));
            } else {//多币种
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_rmbrepay));
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_bothrepay));
            }
            mPaymentWay.setListData(autoPayWay);
            mPaymentWay.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    autoPaymentWay.setChoiceTextContent(model);

                    if(autoPayWay.get(position).equals(getResources().getString(R.string.boc_crcd_autopay_bothrepay))){
                        infoForeignAccount.setVisibility(View.VISIBLE);
                        autoPaymentLocalAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentrmbaccount));
                        autoPaymentLocalAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));
                        autoPaymentForeignAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));

                    } else {
                        infoForeignAccount.setVisibility(View.GONE);
                        autoPaymentLocalAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentaccount));
                        autoPaymentLocalAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));
                        autoPaymentForeignAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));

                    }

                    mPaymentWay.dismiss();
                    if(autoPayWay.get(position).equals(getResources().getString(R.string.boc_crcd_autopay_rmbrepay))){
                        autoCrcdPayModel.setmPaymentWay(0);
                    } else if(autoPayWay.get(position).equals(getResources().getString(R.string.boc_crcd_autopay_bothrepay))) {
                        autoCrcdPayModel.setmPaymentWay(1);
                    } else {
                        autoCrcdPayModel.setmPaymentWay(2);
                    }

                }

            });

        }

        mPaymentWay.show();

    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {

        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
            AccountBean item = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);

            if (1 == accountChoiceStatus){
                autoPaymentLocalAccount.setChoiceTextContent(NumberUtils.formatCardNumber(item.getAccountNumber()));
                autoCrcdPayModel.setLocalCurrencyPaymentAccountNo(item.getAccountNumber());
                autoCrcdPayModel.setLocalAccountId(item.getAccountId());
            } else if (2 == accountChoiceStatus) {
                autoPaymentForeignAccount.setChoiceTextContent(NumberUtils.formatCardNumber(item.getAccountNumber()));
                autoCrcdPayModel.setForeignCurrencyAccountNo(item.getAccountNumber());
                autoCrcdPayModel.setForeignAccountId(item.getAccountId());
            } else {
                showErrorDialog("账户选择返回失败，请重新选择!");
            }

        }

    }

    /**
     * @return 展示需要显示账户里类型
     */
    public ArrayList<String> getFilteredAccountType() {

        ArrayList<String> accoutType = new ArrayList<>();

        if(1 == accountChoiceStatus){//选择人民币账户
            accoutType.add(ApplicationConst.ACC_TYPE_ORD);//101
            accoutType.add(ApplicationConst.ACC_TYPE_GRE);//104,只能还本币
            accoutType.add(ApplicationConst.ACC_TYPE_RAN);//188
            accoutType.add(ApplicationConst.ACC_TYPE_BRO);//119
        } else if(1 == autoCrcdPayModel.getmCurrencyStatus()){//单外币账户
            accoutType.add(ApplicationConst.ACC_TYPE_ORD);//101
            accoutType.add(ApplicationConst.ACC_TYPE_RAN);//188
            accoutType.add(ApplicationConst.ACC_TYPE_BRO);//119
        } else {
            accoutType.add(ApplicationConst.ACC_TYPE_ORD);//101
            accoutType.add(ApplicationConst.ACC_TYPE_RAN);//188
            accoutType.add(ApplicationConst.ACC_TYPE_BRO);//119
        }

        return accoutType;

    }

    private void getSelectAccount(){  //获取所有账户，去除自身的信用卡

        ArrayList<AccountBean> list = new ArrayList<>();
        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(getFilteredAccountType());

        accountBeans.size();

        for(int i = 0; i<accountBeans.size(); i++){
            if(accountBeans.get(i).getAccountNumber().equals(autoCrcdPayModel.getAccountNumber())){
                accountBeans.remove(accountBeans.get(i));
            }
        }

        list.addAll(accountBeans);

        SelectAccoutFragment fragment = SelectAccoutFragment.newInstanceWithData(list);
        startForResult(fragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
    }

    @Override
    protected AutoCrcdPaymentContract.AutoCrcdPaymentPresenter initPresenter() {
        return new AutoCrcdPaymentPresenter(this);
    }

    @Override
    public void setPresenter(AutoCrcdPaymentContract.AutoCrcdPaymentPresenter presenter) {

    }

    @Override
    public void crcdPaymentWaySuccess(PsnCrcdQueryCrcdPaymentWayResult mPaymentWayResult){

    }

    @Override
    public void crcdPaymentWayFailed(BiiResultErrorException exception){

    }

    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQueryResult){

    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception){

    }

    @Override
    public void setCrcdPaymentWaySuccess(PsnCrcdPaymentWaySetupResult mPaymentWaySetResult){

    }

    @Override
    public void setCrcdPaymentWayFailed(BiiResultErrorException exception){

    }

}
