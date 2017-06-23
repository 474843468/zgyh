package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
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
 * Time：2016/11/25 15:55.
 * Created by lk7066 on 2016/11/25.
 * It's used to 信用卡自动还款首页面（其中包括已开通页面和未开通页面）
 */

/**
 * 接口顺序：先查自动还款设置信息，后查询币种
 * 另：如果关闭自动还款，先加载完界面之后，调用还款设置接口
 * */

/**
 * 加载界面元素时，为方便处理，设置四个状态值
 * 开通状态，mCrcdStatus：0，未开通；1，已开通
 * 币种状态，mCurrencyStatus：0，单外币；1，单人民币；2，多币种
 * 还款金额，mPaymentModeStatus：1，全额还款；2，最低额还款
 * 还款方式，mPaymentWay：0，均以人民币还款；1，人民币和外币分别以相应账户还款；2，外币账户还款
 * */

/**
 * 此处如果是单外币账户，那么它的本币其实就是外币，所以本币不是指人民币
 * 即：单美元账户，它的本币就是美元，注意此处本币含义，不可与人民币概念混淆
 * */

public class AutoCrcdPaymentMainFragment extends MvpBussFragment<AutoCrcdPaymentContract.AutoCrcdPaymentPresenter> implements AutoCrcdPaymentContract.AutoCrcdPaymentView{

    //bundle传送键值
    public static final String CRCD_AUTOPAY_ACCOUNT_ID = "CRCD_ACCOUNT_ID";
    public static final String CRCD_AUTOPAY_ACCOUNT_NO = "CRCD_ACCOUNT_NO";
    //前页面传值
    private String crcdAccountId;
    private String crcdAccountNo;

    private View mRootView = null;
    private AutoCrcdPaymentContract.AutoCrcdPaymentPresenter mPresenter;
    Bundle bundle = new Bundle();

    private AutoCrcdPayModel autoCrcdPayModel;
    //协议弹出框
    private JumpToDialog jumpToDialog;
    //未开通页面 四个选择框所占用的空间，金额，方式，本币账户，外币账户
    private LinearLayout infoPaymentMoney, infoPaymentway, infoLocalAccount, infoForeignAccount;//上下两个框
    //未开通页面 确认按钮
    private Button btnYes;
    //未开通页面 下方提示语
    private TextView tvAutoPayInfo;
    //未开通页面 四个选择框，金额，方式，本币账户，外币账户
    private EditChoiceWidget autoPaymentMoney, autoPaymentEditWay, autoPaymentLocalAccount, autoPaymentForeignAccount;//每行元素
    //未开通页面 金额和方式选择弹出框
    private SelectStringListDialog mPaymentMoney, mPaymentWay;
    //未开通页面 选择账户标识，不同的选择框跳转不同的账户选择列表
    private int accountChoiceStatus = 0;  //还款账户选择，1人民币账户，2外币账户
    //未开通页面 还款方式列表，不同币种的还款方式不同
    private List<String> autoPayWay = new ArrayList<String>();
    //未开通页面 账户选择可以选择的账户类型
    private ArrayList<String> accoutType;

    //已开通页面 四个内容显示控件，金额，方式，本币账户，外币账户
    private DetailRow autoPaymentAmount, autoPaymentWay, autoPayLocalCurrency, autoPayForeignCurrency;
    //已开通页面 修改按钮，关闭按钮
    private AutoCrcdPaymentButton autoPaymentExchange, autoPaymentCancel;
    //已开通页面 每条detailrow中间的间隔，根据不同情况控制不同间隔的显示，金额-View1-方式-View2-本币账户-View3-外币账户
    private View view1, view2, view3;

    //币种查询结果，用来区分单本币和单外币情况，这两个币种返回的字段值都在第一币种，所以需要区分
    private String mCurrency1= "";

    private AutoCrcdPaymentResultFragment resultFragment;

    //未开通布局
    FrameLayout viewNotOpen;
    //已开通布局
    RelativeLayout viewOpened;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        showLoadingDialog();

        crcdAccountId = getArguments().getString(CRCD_AUTOPAY_ACCOUNT_ID);//前页面传入accountID
        crcdAccountNo = getArguments().getString(CRCD_AUTOPAY_ACCOUNT_NO);//前页面传入accountNumber

        mRootView = mInflater.inflate(R.layout.fragment_autopayment_main, null);
        //已开通布局
        viewOpened = (RelativeLayout) mRootView.findViewById(R.id.view_autopay_opened);
        //未开通布局
        viewNotOpen = (FrameLayout) mRootView.findViewById(R.id.view_autopay_notopen);
        autoCrcdPayModel = new AutoCrcdPayModel();
        return mRootView;
    }

    @Override
    public void initView(){
        /*未开通*/
        infoPaymentMoney = (LinearLayout) mRootView.findViewById(R.id.lyt_info_paymentmoney);
        infoPaymentway = (LinearLayout) mRootView.findViewById(R.id.lyt_info_paymentway);
        infoLocalAccount = (LinearLayout) mRootView.findViewById(R.id.lyt_info_localaccount);
        infoForeignAccount = (LinearLayout) mRootView.findViewById(R.id.lyt_info_foreignaccount);
        btnYes = (Button) mRootView.findViewById(R.id.btn_autocard_yes);
        tvAutoPayInfo = (TextView) mRootView.findViewById(R.id.txt_autocard_info);
        tvAutoPayInfo.setTextColor(getResources().getColor(R.color.boc_text_color_gray));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writingNotNull()){
                    //协议弹出框
                    jumpToDialog = new JumpToDialog(getContext());
                    jumpToDialog.setBtnText(getResources().getString(R.string.boc_crcd_autopayment_jumptodialogbtn));
                    jumpToDialog.setJumpToData(getResources().getString(R.string.boc_crcd_autopayment_jumptodialogtext));
                    jumpToDialog.enterBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpToDialog.dismiss();
                            AutoCrcdPaymentConfirmFragment autoCrcdPaymentConfirmFragment = new AutoCrcdPaymentConfirmFragment();
                            statusToExchange();
                            bundle.putParcelable("AutoPayConfirm", autoCrcdPayModel);
                            bundle.putInt("AutoStatus", 1);
                            autoCrcdPaymentConfirmFragment.setArguments(bundle);
                            start(autoCrcdPaymentConfirmFragment);
                        }
                    });
                    jumpToDialog.show();
                }
            }
        });

        /*已开通*/
        autoPaymentAmount = (DetailRow) mRootView.findViewById(R.id.autopay_amount);
        autoPaymentWay = (DetailRow) mRootView.findViewById(R.id.autopay_paymentway);
        autoPayLocalCurrency = (DetailRow) mRootView.findViewById(R.id.autopay_localcurrency);
        autoPayForeignCurrency = (DetailRow) mRootView.findViewById(R.id.autopay_foreigncurrency);
        //三个间隔
        view1 = mRootView.findViewById(R.id.view1);
        view2 = mRootView.findViewById(R.id.view2);
        view3 = mRootView.findViewById(R.id.view3);

        autoPaymentExchange = (AutoCrcdPaymentButton) mRootView.findViewById(R.id.exchange_autopay);
        autoPaymentCancel = (AutoCrcdPaymentButton) mRootView.findViewById(R.id.cancel_autopay);

        autoPaymentExchange.updateText(getResources().getString(R.string.boc_crcd_autopay_exchange));
        autoPaymentCancel.updateText(getResources().getString(R.string.boc_crcd_autopay_cancel));
        autoPaymentCancel.updateTextColor(R.color.boc_text_color_red);

        autoPaymentExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //这里应该跳转至exchangeFragment，修改设置
                AutoCrcdPaymentExchangeFragment exchangeFragment = new AutoCrcdPaymentExchangeFragment();
                statusToExchange();
                bundle.putParcelable("AutoPayment", autoCrcdPayModel);
                exchangeFragment.setArguments(bundle);
                start(exchangeFragment);
            }
        });

        autoPaymentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MessageDialog dialog = new MessageDialog(getActivity());
                dialog.setCanceledOnTouchOutside(false);
                dialog.showDialog(getResources().getString(R.string.boc_crcd_autopay_confirmdialogtext));
                dialog.setLeftButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.setRightButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultFragment = new AutoCrcdPaymentResultFragment();
                        //取消的时候上送0，-1，-1
                        //由于直接跳转至结果页面，所以需要将上送字段值先写入
                        autoCrcdPayModel.setRepayType("0");
                        autoCrcdPayModel.setAutoRepayMode("-1");
                        autoCrcdPayModel.setRepayCurSel("-1");
                        getPresenter().setCrcdPaymentWay(autoCrcdPayModel);
                        bundle.putParcelable("AutoPayResult", autoCrcdPayModel);
                        bundle.putInt("ResultStatus", 1);
                        resultFragment.setArguments(bundle);
                        dialog.cancel();
                        showLoadingDialog(false);
                    }
                });

            }
        });

    }

    /**
     * 将相应字段写入到model中，防止页面当中不操作，直接跳转下一个页面时，相应字段为空值
     * 该方法也是将该页面值写入相应上送字段
     * 后续页面其实也有该操作，有点多余，先保留
     * */
    public void statusToExchange(){

        if(1 == autoCrcdPayModel.getmCrcdStatus()){//已开通
            autoCrcdPayModel.setRepayType("1");
        } else {//未开通
            autoCrcdPayModel.setRepayType("0");
        }

        if(1 == autoCrcdPayModel.getmPaymentModeStatus()){//全额还款
            autoCrcdPayModel.setAutoRepayMode("FULL");
        } else if(2 == autoCrcdPayModel.getmPaymentModeStatus()){//最低额还款
            autoCrcdPayModel.setAutoRepayMode("MINP");
        } else {//主动还款
            autoCrcdPayModel.setAutoRepayMode("-1");
        }

        if(0 == autoCrcdPayModel.getmPaymentWay()){//人民币还款
            autoCrcdPayModel.setRepayCurSel("0");
            autoCrcdPayModel.setRepayAcctId(autoCrcdPayModel.getLocalAccountId());
        } else if(1 == autoCrcdPayModel.getmPaymentWay()){//混合还款，各还各的
            autoCrcdPayModel.setRepayCurSel("1");
            autoCrcdPayModel.setRmbRepayAcctId(autoCrcdPayModel.getLocalAccountId());
            autoCrcdPayModel.setForeignRepayAcctId(autoCrcdPayModel.getForeignAccountId());
        } else if(2 == autoCrcdPayModel.getmPaymentWay()){//外币还款
            autoCrcdPayModel.setRepayCurSel("2");
            autoCrcdPayModel.setSignForeignCurrencyAcctId(autoCrcdPayModel.getLocalAccountId());
        } else {//主动还款
            autoCrcdPayModel.setRepayCurSel("-1");
        }

    }

    /**
     * 判断选择框内否有选择
     * */
    public boolean writingNotNull(){
        if(1 != autoCrcdPayModel.getmCurrencyStatus() && autoPaymentEditWay.getChoiceContentTextView().getText().equals(getResources().getString(R.string.boc_crcd_autopay_choose))){
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

    /**
     * 页面元素初始化
     */
    public void initViewVisible(){
        /**
         * 未开通初始化界面
         * */
        tvAutoPayInfo.setText(getResources().getString(R.string.boc_crcd_autopay_tvinfo));

        tvAutoPayInfo.setVisibility(View.VISIBLE);//下方语言提示可见

        if(1 == autoCrcdPayModel.getmCurrencyStatus()){//单本币
            infoPaymentway.setVisibility(View.GONE);//还款方式去除
        } else {
            infoPaymentway.setVisibility(View.VISIBLE);//还款方式可见
        }
        infoForeignAccount.setVisibility(View.GONE);//外币账户隐藏

        /**
         * 已开通初始化界面
         * */
        autoPaymentAmount.updateData(getResources().getString(R.string.boc_crcd_autopay_paymentamount), getautoPaymentAmount(autoCrcdPayModel.getmPaymentModeStatus()));
        if(1 == autoCrcdPayModel.getmCurrencyStatus()){
            autoPaymentWay.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        } else {
            autoPaymentWay.updateData(getResources().getString(R.string.boc_crcd_autopay_paymentway), getautoPaymentWay(autoCrcdPayModel.getmPaymentWay()));//开通状态下，需要判断是否有还款账户来判定还款方式

        }

        if(1 == autoCrcdPayModel.getmPaymentWay()){
            autoPayLocalCurrency.updateData(getResources().getString(R.string.boc_crcd_autopay_paymentrmbaccount), getAutoPaymentNnmber(autoCrcdPayModel.getLocalCurrencyPaymentAccountNo()));
            autoPayForeignCurrency.updateData(getResources().getString(R.string.boc_crcd_autopay_paymentforeignaccount), getAutoPaymentNnmber(autoCrcdPayModel.getForeignCurrencyAccountNo()));
        } else {
            view3.setVisibility(View.GONE);
            autoPayLocalCurrency.updateData(getResources().getString(R.string.boc_crcd_autopay_paymentaccount), getAutoPaymentNnmber(autoCrcdPayModel.getLocalCurrencyPaymentAccountNo()));
            autoPayForeignCurrency.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData(){
        getPresenter().queryCrcdPaymentWay(crcdAccountId);
        mPresenter = new AutoCrcdPaymentPresenter(this);
        autoCrcdPayModel.setAccountId(crcdAccountId);
        autoCrcdPayModel.setAccountNumber(crcdAccountNo);
    }

    private String getautoPaymentAmount(int amount){//显示还款金额
        //1，全额还款，2，最低额还款
        if(1 == amount)
            return getResources().getString(R.string.boc_crcd_autopay_full);
        else if(2 == amount)
            return getResources().getString(R.string.boc_crcd_autopay_minp);
        else
            return getResources().getString(R.string.boc_crcd_autopay_full);
    }

    private String getautoPaymentWay(int currency){//显示还款方式
        //0，人民币还款，1，混合方式还款，2，外币还款
        if(0 == currency){
            return getResources().getString(R.string.boc_crcd_autopay_rmbrepay);
        } else if(1 == currency) {
            return getResources().getString(R.string.boc_crcd_autopay_bothrepay);
        } else if(2 == currency) {
            return getResources().getString(R.string.boc_crcd_autopay_foreignrepay);
        } else {
            return "";
        }
    }

    private String getAutoPaymentNnmber(String number){//显示还款账号
        return NumberUtils.formatCardNumber(number);
    }

    private int getPaymentStatus(String mLocalPayment){
        if(mLocalPayment.equals("0"))
            return 0;  //未开通
        else
            return 1;  //已开通
    }

    private int getPaymentMode(String mLocalMode){
        if(mLocalMode.equals("FULL"))
            return 1;  //全额还款
        else
            return 2;  //最低额还款
    }

    //根据查询结果加载不同的页面，未开通和已开通
    private void startFragment(){
        if(0 == autoCrcdPayModel.getmCrcdStatus()){//未开通
            closeProgressDialog();
            viewOpened.setVisibility(View.GONE);
            viewNotOpen.setVisibility(View.VISIBLE);
            initViewVisible();//初始化元素
            LoadAutoPaymentInfo();//点击操作,各选择编辑框
        } else {//已开通
            closeProgressDialog();
            viewOpened.setVisibility(View.VISIBLE);
            viewNotOpen.setVisibility(View.GONE);
            initViewVisible();//初始化元素
        }
    }

    @Override
    protected AutoCrcdPaymentContract.AutoCrcdPaymentPresenter initPresenter() {
        return new AutoCrcdPaymentPresenter(this);
    }

    @Override
    public void setPresenter(AutoCrcdPaymentContract.AutoCrcdPaymentPresenter presenter) {

    }

    /**
     * 还款方式查询成功回调
     * */
    @Override
    public void crcdPaymentWaySuccess(PsnCrcdQueryCrcdPaymentWayResult mPaymentWayResult){

        //根据查询的本币还款方式，设定开通状态
        autoCrcdPayModel.setmCrcdStatus(getPaymentStatus(mPaymentWayResult.getLocalCurrencyPayment()));

        if(0 == autoCrcdPayModel.getmCrcdStatus()){//未开通
            //仅设置还款金额一项，其余选项为空结果，即其余选择框为“请选择”状态
            autoCrcdPayModel.setmPaymentModeStatus(1);//设置为全额还款状态
        } else if(1 == autoCrcdPayModel.getmCrcdStatus()){//已开通
            //本币还款方式，设定还款方式状态
            autoCrcdPayModel.setmPaymentModeStatus(getPaymentMode(mPaymentWayResult.getLocalCurrencyPaymentMode()));

            if(mPaymentWayResult.getForeignCurrencyAccountNo() != null){//多币种状态

                autoCrcdPayModel.setForeignCurrencyAccountCurrency(mPaymentWayResult.getForeignCurrencyAccountCurrency());

                //如果多币种还款方式为“均以人民币来还”，那么查询结果外币对应值也有，所以需要查询还款币种来确定多币种的还款方式
                if(mPaymentWayResult.getForeignCurrencyAccountCurrency().equals("001")){
                    //设定还款方式状态
                    autoCrcdPayModel.setmPaymentWay(0); //均以人民币还款，此处必须查询的是多币种外币还款账户的币种，这是个坑
                    //将查询结果中的卡号转换为Id
                    autoCrcdPayModel.setForeignCurrencyAccountNo(mPaymentWayResult.getForeignCurrencyAccountNo());
                    autoCrcdPayModel.setForeignAccountId(getPaymentAccountId(mPaymentWayResult.getForeignCurrencyAccountNo()));
                    autoCrcdPayModel.setLocalCurrencyPaymentAccountNo(mPaymentWayResult.getLocalCurrencyPaymentAccountNo());
                    autoCrcdPayModel.setLocalAccountId(getPaymentAccountId(mPaymentWayResult.getLocalCurrencyPaymentAccountNo()));
                } else {
                    //设定还款方式状态
                    autoCrcdPayModel.setmPaymentWay(1); //人民币与外币结欠分别以相应账户还款
                    //将查询结果中的卡号转换为Id
                    autoCrcdPayModel.setForeignCurrencyAccountNo(mPaymentWayResult.getForeignCurrencyAccountNo());
                    autoCrcdPayModel.setForeignAccountId(getPaymentAccountId(mPaymentWayResult.getForeignCurrencyAccountNo()));
                    autoCrcdPayModel.setLocalCurrencyPaymentAccountNo(mPaymentWayResult.getLocalCurrencyPaymentAccountNo());
                    autoCrcdPayModel.setLocalAccountId(getPaymentAccountId(mPaymentWayResult.getLocalCurrencyPaymentAccountNo()));
                }

            } else {//单币种状态

                //人民币和外币的字段值都在接口返回结果的本币字段值，所以需要查询币种区分
                autoCrcdPayModel.setLocalCurrencyPaymentAccountNo(mPaymentWayResult.getLocalCurrencyPaymentAccountNo());
                autoCrcdPayModel.setLocalAccountId(getPaymentAccountId(mPaymentWayResult.getLocalCurrencyPaymentAccountNo()));

                if(mPaymentWayResult.getLocalCurrencyAccountCurrency().equals("001")){//人民币
                    autoCrcdPayModel.setmPaymentWay(0);//所有结欠均以人民币还款
                } else {
                    autoCrcdPayModel.setmPaymentWay(2);//外币结欠以相应账户还款
                }

            }

        }

        //查询币种
        getPresenter().queryCrcdCurrency(crcdAccountNo);
    }

    @Override
    public void crcdPaymentWayFailed(BiiResultErrorException exception){

    }

    /**
     * 币种查询成功回调
     * */
    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQueryResult){
        if(2 == mCurrencyQueryResult.getCurrencyList().size()){
            //如果返回结果字段长度为2，说明第二币种有值，即为多币种
            autoCrcdPayModel.setmCurrencyStatus(2);//多币种
        } else {

            //第一币种是本币字段，需要区分人民币还是外币
            mCurrency1 = mCurrencyQueryResult.getCurrency1().getCode();

            if(mCurrency1.equals("001")){
                autoCrcdPayModel.setmCurrencyStatus(1);//单本币
            } else {
                autoCrcdPayModel.setmCurrencyStatus(0);//单外币
            }
        }

        startFragment();
    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception){

    }

    /**
     * 还款方式设定成功回调
     * 用于已开通页面关闭自动还款按钮请求接口
     * */
    @Override
    public void setCrcdPaymentWaySuccess(PsnCrcdPaymentWaySetupResult mPaymentWaySetResult){
        closeProgressDialog();
        start(resultFragment);
    }

    @Override
    public void setCrcdPaymentWayFailed(BiiResultErrorException exception){

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_autopay_title);
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

    //点击四个选择编辑框，点击操作
    public void LoadAutoPaymentInfo(){

        //还款金额选择
        if(true) {
            autoPaymentMoney = new EditChoiceWidget(mContext);
            autoPaymentMoney.setChoiceTitleBold(true);
            autoPaymentMoney.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentamount));
            autoPaymentMoney.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_full));

            autoPaymentMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectMoney();
                }
            });
            infoPaymentMoney.addView(autoPaymentMoney);
        }

        //还款方式选择
        if(true) {
            autoPaymentEditWay = new EditChoiceWidget(mContext);
            autoPaymentEditWay.setChoiceTitleBold(true);
            autoPaymentEditWay.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentway));
            autoPaymentEditWay.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));

            autoPaymentEditWay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectWay();
                }
            });
            infoPaymentway.addView(autoPaymentEditWay);
        }

        //人民币账户选择
        if(true) {
            autoPaymentLocalAccount = new EditChoiceWidget(mContext);
            autoPaymentLocalAccount.setChoiceTitleBold(true);
            autoPaymentLocalAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentaccount));
            autoPaymentLocalAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));

            autoPaymentLocalAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountChoiceStatus = 1;  //选择人民币账户
                    getSelectAccount();
                }
            });
            infoLocalAccount.addView(autoPaymentLocalAccount);
        }

        //外币账户选择
        if(true) {
            autoPaymentForeignAccount = new EditChoiceWidget(mContext);
            autoPaymentForeignAccount.setChoiceTitleBold(true);
            autoPaymentForeignAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentforeignaccount));
            autoPaymentForeignAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));

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

    /**
     * 还款金额选择弹框
     * */
    private void selectMoney() {
        if (mPaymentMoney == null) {
            mPaymentMoney = new SelectStringListDialog(mContext);
            mPaymentMoney.setListData(AutoCrcdPaymentConst.autoPayMoney);
            mPaymentMoney.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    autoPaymentMoney.setChoiceTextContent(model);
                    autoCrcdPayModel.setmPaymentModeStatus(position + 1);
                    mPaymentMoney.dismiss();
                }
            });
        }
        mPaymentMoney.show();
    }

    /**
     * 还款方式选择弹框
     * */
    private void selectWay() {

        if (mPaymentWay == null) {
            mPaymentWay = new SelectStringListDialog(mContext);

            if(0 == autoCrcdPayModel.getmCurrencyStatus()){//单外币
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_rmbrepay));
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_foreignrepay));
            } else if(1 == autoCrcdPayModel.getmCurrencyStatus()){//单本币
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_rmbrepay));
            } else {//多币种
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_rmbrepay));
                autoPayWay.add(getResources().getString(R.string.boc_crcd_autopay_bothrepay));
            }

            mPaymentWay.setListData(autoPayWay);
            mPaymentWay.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    autoPaymentEditWay.setChoiceTextContent(model);
                    //mWay = String.valueOf(position + 1);
                    if(autoPayWay.get(position).equals(getResources().getString(R.string.boc_crcd_autopay_rmbrepay))){
                        autoCrcdPayModel.setmPaymentWay(0);
                    }
                    if(autoPayWay.get(position).equals(getResources().getString(R.string.boc_crcd_autopay_bothrepay))){
                        autoCrcdPayModel.setmPaymentWay(1);
                    }
                    if(autoPayWay.get(position).equals(getResources().getString(R.string.boc_crcd_autopay_foreignrepay))){
                        autoCrcdPayModel.setmPaymentWay(2);
                    }

                    //此处设置不仅仅是隐藏与否，还需要设置一个账户时，显示“还款账户”，两个账户时，显示“还人民币账户”，“还外币账户”
                    if(autoPayWay.get(position).equals(getResources().getString(R.string.boc_crcd_autopay_bothrepay))){
                        //如果选择的还款方式为人民币和外币以相应账户还款，那么“还外币账户”一栏显示
                        infoForeignAccount.setVisibility(View.VISIBLE);
                        autoPaymentLocalAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentrmbaccount));
                        autoPaymentLocalAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));
                        autoPaymentForeignAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));
                    } else {
                        //其余情况，“还外币账户”一栏隐藏
                        infoForeignAccount.setVisibility(View.GONE);
                        autoPaymentLocalAccount.setChoiceTextName(getResources().getString(R.string.boc_crcd_autopay_paymentaccount));
                        autoPaymentLocalAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));
                        autoPaymentForeignAccount.setChoiceTextContent(getResources().getString(R.string.boc_crcd_autopay_choose));
                    }

                    mPaymentWay.dismiss();
                }
            });
        }
        mPaymentWay.show();
    }

    /**
     * 账户选择返回结果
     * */
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
                showErrorDialog("账户选择返回失败，请重新选择");
            }
        }
    }

    /**
     * @return 展示需要显示账户里类型
     */
    public ArrayList<String> getFilteredAccountType() {
        accoutType = new ArrayList<>();
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

    /**
     * 跳转选择账户页面
     * */
    private void getSelectAccount(){  //获取所有账户，去除自身的信用卡
        ArrayList<AccountBean> list = new ArrayList<>();
        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(getFilteredAccountType());

        for(int i = 0; i<accountBeans.size(); i++){

            if(accountBeans.get(i).getAccountNumber().equals(crcdAccountNo)){
                accountBeans.remove(accountBeans.get(i));
            }

        }
        list.addAll(accountBeans);

        SelectAccoutFragment fragment = SelectAccoutFragment.newInstanceWithData(list);
        startForResult(fragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);

    }

    /**
     * 初始化界面根据返回的accountnumber来获取accountID
     * 还款方式查询结果都是卡号，但是还款方式设定需要上送ID
     * */
    private String getPaymentAccountId(String accountNumber){
        List<AccountBean> accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(accoutType);
        for(int i = 0; i<accountBeanList.size(); i++){
            if(accountBeanList.get(i).getAccountNumber().equals(accountNumber));
            return accountBeanList.get(i).getAccountId();
        }
        return "";
    }

}
