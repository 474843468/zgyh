package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ActionSheetDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.ChangeTradeAccountPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：双向宝-账户管理-变更交易账户（首页）
 * Created by zhx on 2016/12/19
 */
public class ChangeTradeAccountFragment extends MvpBussFragment<ChangeTradeAccountPresenter> implements ChangeTradeAccountContact.View {
    private static final int REQUEST_CODE_SELECT_ACCCOUNT = 218; // 选择资金账户
    private View mRootView;
    private EditChoiceWidget ecw_old_trade_account;
    private EditChoiceWidget ecw_new_trade_account;
    private TextView tv_next_step; // 下一步
    private List<VFGBailListQueryViewModel.BailItemEntity> list;
    private List<VFGBailListQueryViewModel.BailItemEntity> oldList; // “原交易账户”保证金列表
    private List<VFGBailListQueryViewModel.BailItemEntity> newList; // “新交易账户”保证金列表
    private String currentTradeAccountNo; // 当前交易账户(即“原交易账户”)
    private VFGFilterDebitCardViewModel.DebitCardEntity mDebitCardEntity; // 选择的借记卡账户(对应“新交易账户”)
    private List<String> filterCurrenyList; // 得到过滤的币种列表
    private VFGFilterDebitCardViewModel vfgFilterDebitCardViewModel; // 过滤出符合条件的借记卡账户ViewModel
    private VFGBailListQueryViewModel vfgBailListQueryViewModel;
    private VFGGetBindAccountViewModel vfgGetBindAccountViewModel;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_change_trade_account, null);
        return mRootView;
    }

    @Override
    public void initView() {
        ecw_old_trade_account = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_old_trade_account);
        ecw_old_trade_account.setArrowImageGone(false);
        ecw_old_trade_account.getChoiceContentTextView().setTextColor(mActivity.getResources().getColor(R.color.boc_text_color_common_gray));
        ecw_new_trade_account = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_new_trade_account);
        tv_next_step = (TextView) mRootView.findViewById(R.id.tv_next_step);
    }

    @Override
    public void initData() {
        filterCurrenyList = new ArrayList<String>();
        list = getArguments().getParcelableArrayList("list");
        oldList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();
        newList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();

        // 请求
        showLoadingDialog("加载中...");
        vfgBailListQueryViewModel = new VFGBailListQueryViewModel();
        getPresenter().vFGBailListQuery(vfgBailListQueryViewModel);
    }

    @Override
    public void setListener() {
        ecw_new_trade_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入“选择账户列表”界面
                ChooseLongshortAccountFragment toFragment = ChooseLongshortAccountFragment.newInstance(currentTradeAccountNo, currentTradeAccountNo, 3);
                startForResult(toFragment, REQUEST_CODE_SELECT_ACCCOUNT);
            }
        });

        // 点击"下一步"按钮
        tv_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkData()) {
                    return;
                }

                if (newList.size() == 0) {
                    new ActionSheetDialog(mActivity)
                            .builder()
                            .setTitle("您选择的资金账户下无保证金账户，您需要")
                            .setCancelable(false)
                            .setCanceledOnTouchOutside(false)
                            .addSheetItem("使用原保证金", ActionSheetDialog.SheetItemColor.Blue,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
                                            SelectBailFragment fragment = new SelectBailFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelableArrayList("oldList", (ArrayList<VFGBailListQueryViewModel.BailItemEntity>) oldList);
                                            bundle.putParcelableArrayList("newList", (ArrayList<VFGBailListQueryViewModel.BailItemEntity>) newList);
                                            bundle.putStringArrayList("filterCurrenyList", (ArrayList<String>) filterCurrenyList);
                                            bundle.putParcelable("mDebitCardEntity", mDebitCardEntity);
                                            bundle.putString("currentTradeAccountNo", currentTradeAccountNo);
                                            fragment.setArguments(bundle);
                                            start(fragment);
                                        }
                                    })
                            .addSheetItem("新增保证金", ActionSheetDialog.SheetItemColor.Blue,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
                                            AddNewBailFragment fragment = new AddNewBailFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("fromWhere", 2);
                                            bundle.putParcelable("mDebitCardEntity", mDebitCardEntity);
                                            fragment.setArguments(bundle);
                                            start(fragment);
                                        }
                                    })
                            .show();
                } else {
                    SelectBailFragment fragment = new SelectBailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("oldList", (ArrayList<VFGBailListQueryViewModel.BailItemEntity>) oldList);
                    bundle.putParcelableArrayList("newList", (ArrayList<VFGBailListQueryViewModel.BailItemEntity>) newList);
                    bundle.putStringArrayList("filterCurrenyList", (ArrayList<String>) filterCurrenyList);
                    bundle.putParcelable("mDebitCardEntity", mDebitCardEntity);
                    bundle.putString("currentTradeAccountNo", currentTradeAccountNo);
                    fragment.setArguments(bundle);
                    start(fragment);
                }
            }
        });
    }

    // 检查数据
    private boolean checkData() {
        if (mDebitCardEntity == null) {
            showErrorDialog("请选择新交易账户");
            return false;
        }

        return true;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        switch (requestCode) {
            case REQUEST_CODE_SELECT_ACCCOUNT: // 选择资金账户
                mDebitCardEntity = data.getParcelable("debitCardEntity");

                if (mDebitCardEntity != null) {
                    ecw_new_trade_account.setChoiceTextContent(NumberUtils.formatCardNumber(mDebitCardEntity.getAccountNumber()));
                }
                getFilterCurrencyList();

                //<editor-fold desc="得到新交易账户保证金列表,再次">
                newList.clear();
                for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : list) {
                    if (bailItemEntity.getAccountNumber().equals(mDebitCardEntity.getAccountNumber())) {
                        newList.add(bailItemEntity);
                    }
                }
                //</editor-fold>
                break;
        }
    }

    //<editor-fold desc="得到过滤的币种列表">
    private void getFilterCurrencyList() {
        filterCurrenyList.clear();
        String accountNumber = mDebitCardEntity.getAccountNumber();
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : list) {
            if (accountNumber.equals(bailItemEntity.getAccountNumber())) {
                filterCurrenyList.add(bailItemEntity.getSettleCurrency());
            }
        }
    }
    //</editor-fold>

    @Override
    protected String getTitleValue() {
        return "变更交易账户";
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
    protected ChangeTradeAccountPresenter initPresenter() {
        return new ChangeTradeAccountPresenter(this);
    }

    // 更新界面展示
    private void updateDisplay() {
        //<editor-fold desc="额外的判断(如果借记卡账户数目为2，那么设置默认新交易账户)">
        List<VFGFilterDebitCardViewModel.DebitCardEntity> cardList = vfgFilterDebitCardViewModel.getCardList();
        if (cardList != null) {
            if (cardList.size() == 2) {
                for (VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity : cardList) {
                    if (!currentTradeAccountNo.equals(debitCardEntity.getAccountNumber())) {
                        mDebitCardEntity = debitCardEntity;
                        // 显示新交易账户到界面上
                        ecw_new_trade_account.setChoiceTextContent(NumberUtils.formatCardNumber(mDebitCardEntity.getAccountNumber()));
                        // 得到过滤的币种列表
                        getFilterCurrencyList();

                        //<editor-fold desc="得到新交易账户保证金列表,首次">
                        newList.clear();
                        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : list) {
                            if (bailItemEntity.getAccountNumber().equals(mDebitCardEntity.getAccountNumber())) {
                                newList.add(bailItemEntity);
                            }
                        }
                        //</editor-fold>
                    }
                }
            }
        }
        //</editor-fold>
    }

    @Override
    public void vFGSetTradeAccountSuccess(VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel) {
        popToAndReInit(AccountManagementFragment.class);
    }

    @Override
    public void vFGSetTradeAccountFail(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void vFGBailListQuerySuccess(VFGBailListQueryViewModel vfgBailListQueryViewModel) {
        list = vfgBailListQueryViewModel.getEntityList();
        vfgGetBindAccountViewModel = new VFGGetBindAccountViewModel();
        getPresenter().vFGGetBindAccount(vfgGetBindAccountViewModel);
    }

    @Override
    public void vFGBailListQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void vFGGetBindAccountSuccess(VFGGetBindAccountViewModel viewModel) {
        //<editor-fold desc="得到原交易账户保证金列表">
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : list) {
            if (bailItemEntity.getAccountNumber().equals(vfgGetBindAccountViewModel.getAccountNumber())) {
                oldList.add(bailItemEntity);
            }
        }
        //</editor-fold>

        //<editor-fold desc="获取当前交易账号，并显示在界面上">
        currentTradeAccountNo = vfgGetBindAccountViewModel.getAccountNumber();
        ecw_old_trade_account.setChoiceTextContent(NumberUtils.formatCardNumber(currentTradeAccountNo));
        //</editor-fold>

        vfgFilterDebitCardViewModel = new VFGFilterDebitCardViewModel();
        getPresenter().vFGFilterDebitCard(vfgFilterDebitCardViewModel);
    }

    @Override
    public void vFGGetBindAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void vFGFilterDebitCardSuccess(VFGFilterDebitCardViewModel viewModel) {
        closeProgressDialog();
        updateDisplay();
    }

    @Override
    public void vFGFilterDebitCardFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(ChangeTradeAccountContact.Presenter presenter) {

    }
}
