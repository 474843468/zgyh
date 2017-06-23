package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.AddNewCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.CardBrandModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter.HceAddNewCardPresent;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceactivecard.HceActivateConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceUtil;

import java.util.ArrayList;

/**
 * Created by yangle on 2016/12/14.
 * 描述: 申请新卡界面
 */
public class HceAddNewCarFragment extends MvpBussFragment<HceAddNewCardContract.Present> implements HceAddNewCardContract.View, View.OnClickListener, SelectAgreementView.OnClickContractListener {

    private static final String TAG = HceAddNewCarFragment.class.getSimpleName();
   private View mRootView;
    private EditChoiceWidget mSelectCardWidget;// 选择卡的view
    private LinearLayout mContentView; // 选择卡列表下面的容器View
    private EditMoneyInputWidget mCardBrandWidget;//卡品牌
    private EditMoneyInputWidget mSingleQuotaEditText;//单笔限额
    private EditMoneyInputWidget mPerDayQuotaEditText;//每日限额
    private Button mBtnNext;
    private AddNewCardViewModel mAddCardModel; // 界面model
   private SelectAgreementView mSelectAgreeView;// 协议组件

    public static BussFragment newInstance() {
        return new HceAddNewCarFragment();
    }
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mRootView =  mInflater.inflate(R.layout.boc_fragment_hce_add_card, null);
    }

    @Override
    public void initView() {
        super.initView();

        mSelectCardWidget = mViewFinder.find(R.id.select_master_card);
        mContentView = (LinearLayout) mRootView.findViewById(R.id.content_view);
        mCardBrandWidget = (EditMoneyInputWidget) mRootView.findViewById(R.id.card_brand);
        setMoneyInputWidgetEditable(mCardBrandWidget,false);
        mSingleQuotaEditText = (EditMoneyInputWidget) mRootView.findViewById(R.id.quota_single);
        mPerDayQuotaEditText = (EditMoneyInputWidget) mRootView.findViewById(R.id.quota_per_day);

        mSelectAgreeView = (SelectAgreementView) mRootView.findViewById(R.id.select_agreement_view);
        mBtnNext = (Button) mRootView.findViewById(R.id.btn_next);
    }

    private void showDebitView() {
        setMoneyInputWidgetEditable(mSingleQuotaEditText,false);
        setMoneyInputWidgetEditable(mPerDayQuotaEditText,false);
        mPerDayQuotaEditText.setVisibility(View.VISIBLE);
    }

    private void showCreditView() {
        mPerDayQuotaEditText.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        mSelectCardWidget.setChoiceTextContent("请选择");
        mPerDayQuotaEditText.setmContentMoneyEditText("5000.00");
        mSingleQuotaEditText.setmContentMoneyEditText("2000.00");
        mSelectAgreeView.setAgreement("《中国银行股份有限公司移动支付（借记应用）服务协议》");
        // 当前界面viewModel
        mAddCardModel = new AddNewCardViewModel();
    }

    @Override
    public void setListener() {
        super.setListener();
        mSelectCardWidget.setOnClickListener(this);
        mSelectAgreeView.setOnClickContractListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClickContract(int index) {
        start(ContractFragment.newInstance(getContractUrl()));
    }

    private String getContractUrl() {
        // TODO: 2016/12/14 url？？
        return "file:///android_asset/webviewcontent/crcd/CashInstallmentNotice.html";
    }

    @Override
    protected String getTitleValue() {
        return "申请新卡";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 跳转至选择账户列表
        if (id == R.id.select_master_card) {
            startForResult(HceSelectAccountFragment.newInstance(getNeededCardTypeList(),mAddCardModel),SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            return;
        }

        // 申请请求
        if (id == R.id.btn_next) {
            if (!checkModel(getModel())) {
                return;
            }
            getPresenter().applyHce();
            return;
        }
    }

    private boolean checkModel(AddNewCardViewModel model) {
        if (model == null) {
            throw new NullPointerException("AddNewCardViewModel == null");
        }

        if (TextUtils.isEmpty(model.getMasterCardNo())) {
            showError("未选择卡!");
            return false;
        }

        if (TextUtils.isEmpty(model.getCardBrandModel().getAppTypeName())) {
            showError("卡品牌不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(model.getSingleQuota())) {
            showError("交易限额不能为空");
            return false;
        }

        // 判断单笔限额须0~1000之间
        double singleQuota = Double.parseDouble(model.getSingleQuota());
        if (singleQuota > 1000 || singleQuota <= 0) {
            showError("交易限额不能超过1000元");
            return false;
        }

        if (!model.isAgree()) {
            ToastUtils.show("请勾选协议");
            return false;
        }

        return true;
    }

    @NonNull
    private HceTransactionViewModel getFakeData() {
        HceTransactionViewModel arguments = new HceTransactionViewModel();
        arguments.setMasterCardNo("1234567890123");
        arguments.setSlaveCardNo("slavercardno25614");
        arguments.setCardBrandModel(new CardBrandModel("256156"));
        arguments.setSingleQuota("2000");
        arguments.setPerDayQuota("5000");
        arguments.setFrom(HceTransactionViewModel.From.APPLY);
        return arguments;
    }

    //  获取满足hce的卡类型的列表
    private ArrayList<String> getNeededCardTypeList() {
        ArrayList<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        types.add(ApplicationConst.ACC_TYPE_ZHONGYIN);// 中银信用卡
        types.add(ApplicationConst.ACC_TYPE_GRE);// 长城信用卡
        types.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);// 单外币信用卡
        return types;
    }

    @Override
    public AddNewCardViewModel getModel() {
        mAddCardModel.setDeviceNo(HceUtil.getDeviceId(mContext));
        mAddCardModel.setSingleQuota("500");
        mAddCardModel.setAgree(mSelectAgreeView.isSelected());
        return mAddCardModel;
    }

    @Override
    protected HceAddNewCardContract.Present initPresenter() {
        return new HceAddNewCardPresent(this);
    }

    @Override
    public void showLoading() {
        super.showLoadingDialog();
    }

    @Override
    public void closeLoading() {
        super.closeProgressDialog();
    }

    @Override
    public void showError(String msg) {
        super.showErrorDialog(msg);
    }

    @Override
    public void applyHceSuccess(HceTransactionViewModel transactionViewModel) {
        // 申请成功跳转至确认信息界面
        start(HceActivateConfirmFragment.newInstance(transactionViewModel));
    }

    @Override
    public void applyHceFailure() {
        // 申请失败Toast提示,跳转到卡列表首页
        ToastUtils.show("本地化失败,,跳转到卡列表首页");
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        LogUtil.i("=========onFragmentResult==================");
        if (requestCode == SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT && data != null) {
            mAddCardModel = data.getParcelable(HceSelectAccountFragment.SELECTED_NEW_CARD_SUPPORT_HCE);
            // 设置选择卡的账号
            updateMasterCardView();
            showApplyContentView();
        }
    }

    private void updateMasterCardView() {
        mSelectCardWidget.setChoiceTextContent(mAddCardModel.getMasterCardNoFormat());
        mCardBrandWidget.setmContentMoneyEditText(mAddCardModel.getCardBrandModel().getCardBrandName());
    }

    private void setMoneyInputWidgetEditable(EditMoneyInputWidget editMoneyInputWidget, boolean isEditable) {
        MoneyInputTextView contentMoneyEditText = editMoneyInputWidget.getContentMoneyEditText();
        contentMoneyEditText.setClickable(isEditable);
        contentMoneyEditText.setClickable(isEditable);
    }

    private void showApplyContentView() {
        mContentView.setVisibility(View.VISIBLE);
        if (mAddCardModel.isCreditCard()) {
            showCreditView();
        } else {
            showDebitView();
        }
    }

    private void hideApplyContentView() {
        mContentView.setVisibility(View.GONE);
    }

}
