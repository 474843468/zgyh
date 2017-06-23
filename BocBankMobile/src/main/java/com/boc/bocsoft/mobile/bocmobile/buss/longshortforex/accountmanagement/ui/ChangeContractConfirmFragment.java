package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ProgressStateListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.StateItemAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.ChangeState;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.ChangeTask;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGChangeContractViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.ChangeContractConfirmPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：双向宝-账户管理-变更账户确认页面
 * Created by zhx on 2016/12/14
 */
public class ChangeContractConfirmFragment extends MvpBussFragment<ChangeContractConfirmPresenter> implements ChangeContractConfirmContact.View {
    private View mRootView;
    private TextView tv_notice1; // “资金账户变更信息”提示语
    private TextView tv_old_account_number; // 原资金账户
    private TextView tv_old_account_number1; // 原资金账户
    private TextView tv_new_account_number; // 新资金账户
    private TextView tv_new_account_number1; // 新资金账户
    private TextView tv_margin_account_no; // 保证金账号
    private TextView tv_settle_currency; // 结算币种
    private TextView tv_ok;
    private LinearLayout ll_set_trade_account; // 当勾选“设置交易账户”时显示，否则隐藏
    private VFGFilterDebitCardViewModel.DebitCardEntity mDebitCardEntity; // 选择的借记卡账户,即“新资金账户”
    private VFGBailDetailQueryViewModel mVFGBailDetailQueryViewModel;
    private VFGBailListQueryViewModel.BailItemEntity mBailItem; // 即“原资金账户”
    private boolean isSetTradeAccount;// 是否设置交易账户
    private VFGGetBindAccountViewModel vfgGetBindAccountViewModel;
    private boolean isCurrentAccount; // bailItem是否是当前交易账户
    private List<ChangeTask> taskList; // 要变更的任务列表(这个页面其实只有1个，只是为了用ProgressStateListDialog)
    private StateItemAdapter stateItemAdapter; // 状态条目适配器(条目其实只有1个，只是为了用ProgressStateListDialog)
    private ProgressStateListDialog mProgressDialog;
    private boolean isChangeContractSuccess = false; // 保证金资金账户是否变更成功，默认是失败

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_change_contract_confirm, null);
        return mRootView;
    }

    @Override
    public void initView() {
        tv_notice1 = (TextView) mRootView.findViewById(R.id.tv_notice1);
        tv_old_account_number = (TextView) mRootView.findViewById(R.id.tv_old_account_number);
        tv_old_account_number1 = (TextView) mRootView.findViewById(R.id.tv_old_account_number1);
        tv_new_account_number = (TextView) mRootView.findViewById(R.id.tv_new_account_number);
        tv_new_account_number1 = (TextView) mRootView.findViewById(R.id.tv_new_account_number1);
        tv_margin_account_no = (TextView) mRootView.findViewById(R.id.tv_margin_account_no);
        tv_settle_currency = (TextView) mRootView.findViewById(R.id.tv_settle_currency);
        tv_ok = (TextView) mRootView.findViewById(R.id.tv_ok);
        ll_set_trade_account = (LinearLayout) mRootView.findViewById(R.id.ll_set_trade_account);


    }

    @Override
    public void initData() {
        mDebitCardEntity = getArguments().getParcelable("debitCardEntity");
        mBailItem = getArguments().getParcelable("bailItem");
        isCurrentAccount = mBailItem.isCurrentAccount();
        mVFGBailDetailQueryViewModel = getArguments().getParcelable("vfgBailDetailQueryViewModel");
        isSetTradeAccount = getArguments().getBoolean("isSetTradeAccount");
        vfgGetBindAccountViewModel = getArguments().getParcelable("vfgGetBindAccountViewModel");
        taskList = new ArrayList<ChangeTask>();

        displayData();
    }

    // 展示数据
    private void displayData() {
        // 原资金账户
        String oldAccountNumber = NumberUtils.formatCardNumber(mBailItem.getAccountNumber());
        tv_old_account_number.setText(oldAccountNumber);
        tv_old_account_number1.setText(oldAccountNumber);

        // 新资金账户
        String newAccountNumber = NumberUtils.formatCardNumber(mDebitCardEntity.getAccountNumber());
        tv_new_account_number.setText(newAccountNumber);
        tv_new_account_number1.setText(newAccountNumber);

        // 保证金账号
        String marginAccountNo = mBailItem.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == -1) {
            tv_margin_account_no.setText(NumberUtils.formatCardNumber(marginAccountNo));
        } else {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            tv_margin_account_no.setText(NumberUtils.formatCardNumber(substring));
        }
        // 结算币种
        tv_settle_currency.setText(PublicCodeUtils.getCurrency(mActivity, mBailItem.getSettleCurrency()));

        // 隐藏与显示一些提示信息
        tv_notice1.setVisibility(isSetTradeAccount ? View.VISIBLE : View.GONE);
        ll_set_trade_account.setVisibility(isSetTradeAccount ? View.VISIBLE : View.GONE);
    }

    public static ChangeContractConfirmFragment newInstance(VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity, VFGBailDetailQueryViewModel vfgBailDetailQueryViewModel, VFGBailListQueryViewModel.BailItemEntity bailItem, boolean isSetTradeAccount, VFGGetBindAccountViewModel vfgGetBindAccountViewModel) {
        ChangeContractConfirmFragment fragment = new ChangeContractConfirmFragment();
        Bundle args = new Bundle();

        args.putParcelable("debitCardEntity", debitCardEntity);
        args.putParcelable("vfgBailDetailQueryViewModel", vfgBailDetailQueryViewModel);
        args.putParcelable("bailItem", bailItem);
        args.putBoolean("isSetTradeAccount", isSetTradeAccount);
        args.putParcelable("vfgGetBindAccountViewModel", vfgGetBindAccountViewModel);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setListener() {
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 生成Task列表（其实只有1个Task）
                taskList.clear();
                ChangeTask changeTask = new ChangeTask();
                changeTask.setAccountNumber(mBailItem.getAccountNumber());
                changeTask.setMarginAccountNo(mBailItem.getMarginAccountNo());
                changeTask.setSettleCurrency(mBailItem.getSettleCurrency());
                changeTask.setAccountId(mDebitCardEntity.getAccountId());
                taskList.add(changeTask);

                if (isSetTradeAccount) {
                    // 显示进度对话框
                    mProgressDialog = new ProgressStateListDialog(mActivity);

                    stateItemAdapter = new StateItemAdapter(mActivity, taskList);
                    mProgressDialog.setAdapter(stateItemAdapter);
                    mProgressDialog.setOnCloseListener(new ProgressStateListDialog.OnCloseListener() {
                        @Override
                        public void onClose() {
                            mProgressDialog.dismiss();
                            if (isChangeContractSuccess) {
                                popToAndReInit(BailAccountDetailFragment.class);
                            }
                        }
                    });
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                } else {
                    showLoadingDialog("加载中...", false);
                }

                //<editor-fold desc="开始变更保证金签约账户">
                VFGChangeContractViewModel vfgChangeContractViewModel = new VFGChangeContractViewModel();
                vfgChangeContractViewModel.setOldAccountNumber(mBailItem.getAccountNumber()); // 原借记卡卡号
                vfgChangeContractViewModel.setAccountId(mDebitCardEntity.getAccountId()); // 变更后借记卡账户标识
                vfgChangeContractViewModel.setSettleCurrency(taskList.get(0).getSettleCurrency()); // 结算货币
                vfgChangeContractViewModel.setMarginAccountNo(taskList.get(0).getMarginAccountNo()); // 保证金账号
                getPresenter().vFGChangeContract(vfgChangeContractViewModel);
                //</editor-fold>
            }
        });
    }

    // 设置交易账户
    private void setTradeAccount() {
        VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel = new VFGSetTradeAccountViewModel();
        vfgSetTradeAccountViewModel.setAccountId(mDebitCardEntity.getAccountId());
        getPresenter().vFGSetTradeAccount(vfgSetTradeAccountViewModel);
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected ChangeContractConfirmPresenter initPresenter() {
        return new ChangeContractConfirmPresenter(this);
    }

    @Override
    public void vFGChangeContractSuccess(VFGChangeContractViewModel vfgChangeContractViewModel) {
        isChangeContractSuccess = true;
        if (isSetTradeAccount) {
            taskList.get(0).setState(ChangeState.SUCCESS);
            stateItemAdapter.notifyDataSetChanged();
            setTradeAccount();
        } else {
            closeProgressDialog();

            ChangeContractOperateResultFragment fragment = new ChangeContractOperateResultFragment();
            // // TODO: 2016/12/28
            Bundle bundle = new Bundle();
            bundle.putParcelable("mBailItem", mBailItem);
            bundle.putParcelable("mDebitCardEntity", mDebitCardEntity);
            bundle.putParcelable("mVFGBailDetailQueryViewModel", mVFGBailDetailQueryViewModel);

            fragment.setArguments(bundle);
            start(fragment);
        }


        //<editor-fold desc="跳转到详情时需要这个值更新(mBailItem.setAccountNumber)">
        mBailItem.setAccountNumber(mDebitCardEntity.getAccountNumber());
        //</editor-fold>
    }

    @Override
    public void vFGChangeContractFail(BiiResultErrorException biiResultErrorException) {
        if (isSetTradeAccount) {
            ToastUtils.show(biiResultErrorException.getErrorMessage());
            // 失败了就不再请求“重新设定双向宝交易账户”的接口
            taskList.get(0).setState(ChangeState.FAIL);
            stateItemAdapter.notifyDataSetChanged();

            mProgressDialog.getTvTitle().setText("变更失败");
            mProgressDialog.setBottomSate(ChangeState.FAIL);
            mProgressDialog.getOKBtn().setVisibility(View.VISIBLE);
        } else {
            closeProgressDialog();
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }

    }

    @Override
    public void vFGSetTradeAccountSuccess(VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel) {
        mProgressDialog.setBottomSate(ChangeState.SUCCESS);
        mProgressDialog.getTvTitle().setText("变更成功");
        mProgressDialog.getOKBtn().setVisibility(View.VISIBLE);

        //<editor-fold desc="跳转到详情时需要这个值更新(vfgGetBindAccountViewModel.setAccountNumber)">
        vfgGetBindAccountViewModel.setAccountNumber(mDebitCardEntity.getAccountNumber());
        //</editor-fold>
    }

    @Override
    public void vFGSetTradeAccountFail(BiiResultErrorException biiResultErrorException) {
        if (isSetTradeAccount) {
            ToastUtils.show(biiResultErrorException.getErrorMessage());
            mProgressDialog.setBottomSate(ChangeState.FAIL);
            mProgressDialog.getTvTitle().setText("资金账户变更成功"); // 暂定提示文字为“资金账户变更成功”
            mProgressDialog.getOKBtn().setVisibility(View.VISIBLE);
        } else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }

    }

    @Override
    public void setPresenter(ChangeContractConfirmContact.Presenter presenter) {
    }
}