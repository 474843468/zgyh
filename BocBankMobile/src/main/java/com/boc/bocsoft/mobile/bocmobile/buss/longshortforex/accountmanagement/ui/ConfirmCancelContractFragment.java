package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BailStateDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BailTransferConfirmDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGCancelContractViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGQueryMaxAmountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.ConfirmCancelContractPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.math.BigDecimal;

/**
 * Fragment：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/11/23
 */
public class ConfirmCancelContractFragment extends MvpBussFragment<ConfirmCancelContractPresenter> implements ConfirmCancelContractContact.View {
    private View mRootView;
    private TextView tv_account_number;
    private TextView tv_margin_account_no;
    private TextView tv_settle_currency;
    private TextView tv_balance;
    private VFGBailListQueryViewModel.BailItemEntity bailItem;
    private VFGBailDetailQueryViewModel vFGBailDetailQueryViewModel;
    private TextView tv_ok;
    private VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel;
    private String settleCurrency; // 币种
    private int currentTransferRemit = 0; // 0表示人民币，1表示钞，2表示汇
    private BailStateDialog bailStateDialog;
    private boolean isRemitNeedTransfer = false; // 汇是否需要转出
    private boolean isNeedTransfer = false; // 是否需要转出
    private boolean isHasTransferFail = false; // 是否有转出失败
    private boolean isCancelContractSuccess = false; // 是否解约成功
    private BaseMobileActivity curActivity;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_vfg_cancel_contract, null);
        return mRootView;
    }

    @Override
    public void initView() {
        tv_balance = (TextView) mRootView.findViewById(R.id.tv_balance);
        tv_account_number = (TextView) mRootView.findViewById(R.id.tv_account_number);
        tv_margin_account_no = (TextView) mRootView.findViewById(R.id.tv_margin_account_no);
        tv_settle_currency = (TextView) mRootView.findViewById(R.id.tv_settle_currency);
        tv_ok = (TextView) mRootView.findViewById(R.id.tv_ok);


    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        bailItem = arguments.getParcelable("bailItem");
        vFGBailDetailQueryViewModel = arguments.getParcelable("vFGBailDetailQueryViewModel");

        // 账户余额
        BigDecimal marginFund = bailItem.getMarginFund();
        if (marginFund != null) {
            tv_balance.setText(MoneyUtils.transMoneyFormat(marginFund, bailItem.getSettleCurrency()));
        }

        tv_account_number.setText(NumberUtils.formatCardNumber(bailItem.getAccountNumber()));// 资金账户
        String marginAccountNo = bailItem.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == 0) {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            tv_margin_account_no.setText(NumberUtils.formatCardNumber(substring));
        } else {
            tv_margin_account_no.setText(NumberUtils.formatCardNumber(marginAccountNo)); // 保证金账号
        }
        tv_settle_currency.setText(PublicCodeUtils.getCurrency(mActivity, bailItem.getSettleCurrency())); // 结算币种

    }

    @Override
    public void setListener() {
        curActivity = (BaseMobileActivity) ActivityManager.getAppManager().currentActivity();
        curActivity.setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
            @Override
            public void onEnterBtnClick() {
                if (isCancelContractSuccess) {
                    popToAndReInit(AccountManagementFragment.class);
                }
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BigDecimal cashBanlance = bailItem.getCashBanlance();
                final BigDecimal remitBanlance = bailItem.getRemitBanlance();

                if (cashBanlance == null) {
                    // TODO 为空的情况待处理（非当前账户会为空）
                    showLoadingDialog("加载中...", false);
                    doCancelContract();
                    return;
                }

                if (cashBanlance.doubleValue() > 0 || remitBanlance.doubleValue() > 0) { // 到时把这个值改为>就可以了
                    isNeedTransfer = true;
                    final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mActivity);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dialog.getTvNotice().getLayoutParams();
                    layoutParams.height = 260;
                    dialog.setTitle("");
                    dialog.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    dialog.getTvNotice().setTypeface(Typeface.DEFAULT_BOLD);
                    dialog.getBtnLeft().setTypeface(Typeface.DEFAULT_BOLD);
                    dialog.getBtnRight().setTypeface(Typeface.DEFAULT_BOLD);
                    dialog.setCancelable(false);
                    dialog.setNoticeContent("由于保证金账户解约后不可回退，请您将保证金账户余额全部转出后，再进行解约。");
                    dialog.getBtnRight().setText("余额转出并解约");
                    dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_main_bg_color), getResources().getColor(R.color.boc_main_bg_color), getResources().getColor(R.color.boc_text_color_red), getResources().getColor(R.color.boc_text_color_red));
                    dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                        @Override
                        public void onLeftBtnClick(View view) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onRightBtnClick(View view) {
                            dialog.dismiss();

                            BailTransferConfirmDialog bailTransferConfirmDialog = new BailTransferConfirmDialog(mActivity);
                            bailTransferConfirmDialog.setAccount(NumberUtils.formatCardNumber(bailItem.getAccountNumber()));
                            bailTransferConfirmDialog.setCancelable(false);
                            bailTransferConfirmDialog.setAmount(bailItem.getSettleCurrency(), bailItem.getCashBanlance().toString(), bailItem.getRemitBanlance().toString());
                            bailTransferConfirmDialog.setOnConfirmListener(new BailTransferConfirmDialog.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    showLoadingDialog("加载中...", false);
                                    bailStateDialog = new BailStateDialog(mActivity); // 转出状态的对话框
                                    bailStateDialog.setOnConfirmListener(new BailStateDialog.OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            if (isCancelContractSuccess && isHasTransferFail == false) { // 如果解约成功，那么跳转到“账户管理”首页面
                                                ToastUtils.show("解约成功");
                                                popToAndReInit(AccountManagementFragment.class);
                                            }
                                        }
                                    });
                                    // 转出金额的操作
                                    VFGBailTransferViewModel vfgBailTransferViewModel = new VFGBailTransferViewModel();
                                    settleCurrency = bailItem.getSettleCurrency();
                                    vfgBailTransferViewModel.setCurrencyCode(settleCurrency); // 币种
                                    if ("001".equals(settleCurrency)) {
                                        currentTransferRemit = 0; // 当前转出操作的是人民币
                                        vfgBailTransferViewModel.setCashRemit("0");
                                        vfgBailTransferViewModel.setAmount(cashBanlance.toString());
                                    } else { // 先转出汇，再转出钞
                                        if (remitBanlance.doubleValue() > 0) {
                                            currentTransferRemit = 2; // 当前转出操作的是汇
                                            vfgBailTransferViewModel.setCashRemit("2");
                                            vfgBailTransferViewModel.setAmount(remitBanlance.toString());
                                            isRemitNeedTransfer = true;
                                        } else {
                                            currentTransferRemit = 1; // 当前转出操作的是钞
                                            vfgBailTransferViewModel.setCashRemit("1");
                                            vfgBailTransferViewModel.setAmount(cashBanlance.toString());
                                        }
                                    }
                                    vfgBailTransferViewModel.setFundTransferDir("O"); // "I"表示银行资金账户转证券保证金账户
                                    getPresenter().vFGBailTransfer(vfgBailTransferViewModel);
                                }
                            });
                            bailTransferConfirmDialog.show();
                        }
                    });
                    dialog.show();
                } else {
                    showLoadingDialog("加载中...", false);
                    doCancelContract();
                }
            }
        });
    }

    // 进行解约的操作
    private void doCancelContract() {
        VFGCancelContractViewModel vfgCancelContractViewModel = new VFGCancelContractViewModel();
        vfgCancelContractViewModel.setSettleCurrency(bailItem.getSettleCurrency());
        vfgCancelContractViewModel.setMarginAccountNo(bailItem.getMarginAccountNo());
        vfgCancelContractViewModel.setAccountNumber(bailItem.getAccountNumber());
        getPresenter().vFGCancelContract(vfgCancelContractViewModel);
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
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
    protected ConfirmCancelContractPresenter initPresenter() {
        return new ConfirmCancelContractPresenter(this);
    }

    @Override
    public void vFGCancelContractSuccess(VFGCancelContractViewModel vfgCancelContractViewModel) {
        isCancelContractSuccess = true;
        closeProgressDialog();
        if (isNeedTransfer) {
            bailStateDialog.updateCancelContractState(0);
            bailStateDialog.show();
        } else {
            popToAndReInit(AccountManagementFragment.class);
            ToastUtils.show("解约成功");
        }
    }

    @Override
    public void vFGCancelContractFail(BiiResultErrorException biiResultErrorException) {
        isCancelContractSuccess = false;
        closeProgressDialog();
        if (isNeedTransfer) {
            bailStateDialog.updateCancelContractState(1);
            bailStateDialog.show();
        } else {
            ToastUtils.show("解约失败");
        }
    }

    @Override
    public void vFGQueryMaxAmountSuccess(VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel) {
        //        closeProgressDialog();
    }

    @Override
    public void vFGQueryMaxAmountFail(BiiResultErrorException biiResultErrorException) {
        //        closeProgressDialog();
    }

    @Override
    public void vFGBailTransferSuccess(VFGBailTransferViewModel vfgBailTransferViewModel) {
        handleTransferReturn(0);
    }

    @Override
    public void vFGBailTransferFail(BiiResultErrorException biiResultErrorException) {
        isHasTransferFail = true;
        handleTransferReturn(1);
    }

    // 处理转出返回:0表示成功，1表示失败
    private void handleTransferReturn(int state) {
        BigDecimal cashBanlance = bailItem.getCashBanlance();
        BigDecimal remitBanlance = bailItem.getRemitBanlance();
        String currency = PublicCodeUtils.getCurrency(mActivity, bailItem.getSettleCurrency());

        switch (currentTransferRemit) {
            case 0: // 人民币
                String renminbin = MoneyUtils.transMoneyFormat(bailItem.getCashBanlance(), bailItem.getSettleCurrency());
                bailStateDialog.updateBailState1(renminbin + " 元", state);
                bailStateDialog.updateBailState2("", 2);

                // 此处进行解约操作
                doCancelContract();
                break;
            case 2: // 汇
                String hui = MoneyUtils.transMoneyFormat(bailItem.getRemitBanlance(), bailItem.getSettleCurrency());
                bailStateDialog.updateBailState1(hui + " " + currency + "/汇", state);
                if (bailItem.getCashBanlance().doubleValue() > 0) {
                    currentTransferRemit = 1;
                    VFGBailTransferViewModel viewModel = new VFGBailTransferViewModel();
                    settleCurrency = bailItem.getSettleCurrency();
                    viewModel.setCurrencyCode(settleCurrency); // 币种
                    viewModel.setCashRemit("1");
                    viewModel.setFundTransferDir("O"); // "I"表示银行资金账户转证券保证金账户
                    viewModel.setAmount(remitBanlance.toString());
                    getPresenter().vFGBailTransfer(viewModel);
                } else {
                    bailStateDialog.updateBailState2("", 2);
                    // 此处进行解约操作
                    doCancelContract();
                }

                break;
            case 1: // 钞
                String chao = MoneyUtils.transMoneyFormat(cashBanlance, bailItem.getSettleCurrency());
                if (!isRemitNeedTransfer) {
                    bailStateDialog.updateBailState1("", 2);
                }
                bailStateDialog.updateBailState2(chao + " " + currency + "/钞", state);
                // 此处进行解约操作
                doCancelContract();
                break;
        }
    }

    @Override
    public void setPresenter(ConfirmCancelContractContact.Presenter presenter) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        curActivity.setErrorDialogClickListener(null);
    }
}
