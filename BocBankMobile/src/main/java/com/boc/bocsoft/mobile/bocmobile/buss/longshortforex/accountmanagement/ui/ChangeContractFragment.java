package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleNoticeDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.ChangeContractPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * Fragment：双向宝-账户管理-变更账户（从详情页进入）
 * Created by zhx on 2016/12/14
 */
public class ChangeContractFragment extends MvpBussFragment<ChangeContractPresenter> implements ChangeContractContact.View {
    private View mRootView;
    private TextView tv_old_account_number; // 原资金账户
    private TextView tv_margin_account_no; // 保证金账号
    private TextView tv_settle_currency; // 结算币种
    private EditChoiceWidget ecw_choose_new_account_number; // 选择“新资金账户”组件
    private LinearLayout ll_set_trade_account;
    private LinearLayout ll_container;
    private ImageView iv_is_set_trade_account;
    private TextView tv_next_step;
    private VFGBailListQueryViewModel.BailItemEntity bailItem; // 即“原资金账户”
    private boolean isSetTradeAccount = false; // 是否设置交易账户
    private VFGBailDetailQueryViewModel vfgBailDetailQueryViewModel; // 保证金账户详情ViewModel
    private VFGGetBindAccountViewModel vfgGetBindAccountViewModel; // 如果bailItem不是当前交易账户，这个是“新资金账户”
    private VFGFilterDebitCardViewModel.DebitCardEntity mDebitCardEntity; // 选择的借记卡账户,即“新资金账户”
    private boolean isCurrentAccount; // bailItem是否是当前交易账户
    private VFGFilterDebitCardViewModel vfgFilterDebitCardViewModel; // 过滤出符合条件的借记卡账户ViewModel

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_change_contract, null);
        return mRootView;
    }

    @Override
    public void initView() {
        tv_old_account_number = (TextView) mRootView.findViewById(R.id.tv_old_account_number);
        tv_margin_account_no = (TextView) mRootView.findViewById(R.id.tv_margin_account_no);
        tv_settle_currency = (TextView) mRootView.findViewById(R.id.tv_settle_currency);
        ecw_choose_new_account_number = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_choose_new_account_number);
        ll_set_trade_account = (LinearLayout) mRootView.findViewById(R.id.ll_set_trade_account);
        ll_container = (LinearLayout) mRootView.findViewById(R.id.ll_container);
        iv_is_set_trade_account = (ImageView) mRootView.findViewById(R.id.iv_is_set_trade_account);
        tv_next_step = (TextView) mRootView.findViewById(R.id.tv_next_step);

        ll_container.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        bailItem = getArguments().getParcelable("bailItem");
        vfgBailDetailQueryViewModel = getArguments().getParcelable("vFGBailDetailQueryViewModel");
        vfgGetBindAccountViewModel = getArguments().getParcelable("vfgGetBindAccountViewModel");

        // 根据bailItem判断
        isCurrentAccount = bailItem.isCurrentAccount();

        vfgFilterDebitCardViewModel = new VFGFilterDebitCardViewModel();
        showLoadingDialog("加载中...");
        getPresenter().vFGFilterDebitCard(vfgFilterDebitCardViewModel);
    }

    // 展示数据
    private void displayData() {
        // 原资金账户
        tv_old_account_number.setText(NumberUtils.formatCardNumber(bailItem.getAccountNumber()));
        // 保证金账号
        String marginAccountNo = bailItem.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == -1) {
            tv_margin_account_no.setText(NumberUtils.formatCardNumber(marginAccountNo));
        } else {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            tv_margin_account_no.setText(NumberUtils.formatCardNumber(substring));
        }
        // 结算币种
        tv_settle_currency.setText(PublicCodeUtils.getCurrency(mActivity, bailItem.getSettleCurrency()));

        // 额外的判断(如果bailItem是当前交易账户,并且借记卡账户数目为2，那么设置默认新资金账户)
        List<VFGFilterDebitCardViewModel.DebitCardEntity> cardList = vfgFilterDebitCardViewModel.getCardList();
        if (cardList != null) {
            if (cardList.size() == 2) {
                for (VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity : cardList) {
                    if (!bailItem.getAccountNumber().equals(debitCardEntity.getAccountNumber())) {
                        mDebitCardEntity = debitCardEntity;
                        ecw_choose_new_account_number.setChoiceTextContent(NumberUtils.formatCardNumber(debitCardEntity.getAccountNumber()));
                        break;
                    }
                }

                if (!isCurrentAccount) { // 如果只有两个借记卡，bailItem不是当前交易账户，那么mDebitCardEntity必是当前交易账户
                    ll_set_trade_account.setVisibility(View.GONE);
                }
            } else {
                if (!isCurrentAccount) { // 3个及以上，如果不是当前账户，那么默认选中当前账户
                    ll_set_trade_account.setVisibility(View.GONE);
                    for (VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity : cardList) {
                        if (!bailItem.getAccountNumber().equals(debitCardEntity.getAccountNumber())) {
                            mDebitCardEntity = debitCardEntity;
                            ecw_choose_new_account_number.setChoiceTextContent(NumberUtils.formatCardNumber(debitCardEntity.getAccountNumber()));
                            break;
                        }
                    }
                }
            }
        }



        ll_container.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {
        iv_is_set_trade_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSetTradeAccount = !isSetTradeAccount;

                iv_is_set_trade_account.setImageResource(isSetTradeAccount ? R.drawable.checked : R.drawable.unchecked);
            }
        });

        // 新资金账户
        ecw_choose_new_account_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //<editor-fold desc="进入选择新资金账户界面">
                ChooseLongshortAccountFragment toFragment = ChooseLongshortAccountFragment.newInstance(vfgGetBindAccountViewModel.getAccountNumber(), bailItem.getAccountNumber(), 2);
                startForResult(toFragment, 222);
                //</editor-fold>
            }
        });

        // 下一步
        tv_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查数据
                if (!checkData()) {
                    return;
                }

                // 如果勾选了“设置交易账户”，那么弹出对话框提示
                if (isSetTradeAccount) {
                    showSetTradeAccountConfirmDialog();
                } else {
                    enterChangeContractConfirmFragment();
                }
            }
        });
    }

    // 显示设置交易账户的确认对话框
    private void showSetTradeAccountConfirmDialog() {
        final TitleNoticeDialog dialog = new TitleNoticeDialog(mActivity);

        dialog.setTitle("设置双向宝交易账户为");
        dialog.isShowTitle(true);

        String accountTypeStr = PublicCodeUtils.changeaccounttypetoName(mDebitCardEntity.getAccountType()); // 账户类型
        String accountNumber = NumberUtils.formatCardNumberStrong(mDebitCardEntity.getAccountNumber()); // 账号
        String nickName = mDebitCardEntity.getNickName(); // 别名

        dialog.setNoticeContent(accountTypeStr + accountNumber + nickName);
        dialog.getBtnRight().setText("确认");
        dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_main_bg_color), getResources().getColor(R.color.boc_main_bg_color), getResources().getColor(R.color.boc_text_color_red), getResources().getColor(R.color.boc_text_color_red));
        dialog.setDialogBtnClickListener(new TitleNoticeDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                dialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                dialog.dismiss();
                enterChangeContractConfirmFragment();
            }
        });

        dialog.show();
    }

    // 进去确认页面
    private void enterChangeContractConfirmFragment() {
        // 进去确认页面
        ChangeContractConfirmFragment fragment = ChangeContractConfirmFragment.newInstance(mDebitCardEntity, vfgBailDetailQueryViewModel, bailItem, isSetTradeAccount, vfgGetBindAccountViewModel);
        start(fragment);
    }

    // 检查数据
    private boolean checkData() {
        if (mDebitCardEntity == null) {
            showErrorDialog("请选择新资金账户");
            return false;
        }

        return true;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        // 更新是否设置交易账户的显示
        isSetTradeAccount = false;
        iv_is_set_trade_account.setImageResource(isSetTradeAccount ? R.drawable.checked : R.drawable.unchecked);

        mDebitCardEntity = data.getParcelable("debitCardEntity");
        // 此处判断mDebitCardEntity是不是当前交易账户
        if (vfgGetBindAccountViewModel.getAccountNumber().equals(mDebitCardEntity.getAccountNumber())) {
            ll_set_trade_account.setVisibility(View.GONE);
        } else {
            ll_set_trade_account.setVisibility(View.VISIBLE);
        }
        ecw_choose_new_account_number.setChoiceTextContent(NumberUtils.formatCardNumber(mDebitCardEntity.getAccountNumber()));
    }

    @Override
    protected ChangeContractPresenter initPresenter() {
        return new ChangeContractPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return "变更账户";
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
    public void vFGFilterDebitCardSuccess(VFGFilterDebitCardViewModel viewModel) {
        closeProgressDialog();
        vfgFilterDebitCardViewModel = viewModel;
        displayData();

    }

    @Override
    public void vFGFilterDebitCardFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        displayData();
    }

    @Override
    public void setPresenter(ChangeContractContact.Presenter presenter) {

    }
}