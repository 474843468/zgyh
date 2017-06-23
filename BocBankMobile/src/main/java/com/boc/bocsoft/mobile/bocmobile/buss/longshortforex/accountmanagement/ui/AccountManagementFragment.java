package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter.BailAccountAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailAccountInfoListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.AccountManagementPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment：双向宝-账户管理
 * Created by zhx on 2016/11/21
 */
public class AccountManagementFragment extends MvpBussFragment<AccountManagementPresenter> implements AccountManagementContact.View {
    private View mRootView;
    private ListView lv_list;
    private VFGBailListQueryViewModel vfgBailListQueryViewModel;
    private BailAccountAdapter mAdapter;
    private VFGGetBindAccountViewModel vfgGetBindAccountViewModel; // 双向宝交易账户ViewModel
    private VFGBailAccountInfoListQueryViewModel vfgBailAccountInfoListQueryViewModel; // 双向宝保证金账户基本信息多笔查询ViewModel
    private TextView tv_add_new_bail_account;
    private VFGSignSubmitViewModel newBaiAccount; // 新增的账户
    private VFGFilterDebitCardViewModel vfgFilterDebitCardViewModel; // 借记卡列表ViewModel

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_long_short_account_management, null);
        return mRootView;
    }

    @Override
    public void initView() {
        lv_list = (ListView) mRootView.findViewById(R.id.lv_list);
        tv_add_new_bail_account = (TextView) mRootView.findViewById(R.id.tv_add_new_bail_account);
    }

    @Override
    public void initData() {
        showLoadingDialog("加载中...");
        vfgBailListQueryViewModel = new VFGBailListQueryViewModel();
        getPresenter().vFGBailListQuery(vfgBailListQueryViewModel);
    }

    public void setNewBailAccount(VFGSignSubmitViewModel newBailAccount) {
        this.newBaiAccount = newBailAccount;
    }

    @Override
    public void reInit() {
        // 清空列表
        mAdapter.getList().clear();
        mAdapter.notifyDataSetChanged();
        // 显示加载对话框
        showLoadingDialog("加载中...");
        // 重新请求列表
        vfgBailListQueryViewModel = new VFGBailListQueryViewModel();
        getPresenter().vFGBailListQuery(vfgBailListQueryViewModel);
    }

    @Override
    public void setListener() {
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("bailItem", mAdapter.getItem(position));
                bundle.putBoolean("isAccountNumberSizeBiggerThanZero", mAdapter.isAccountNumberSizeBiggerThanZero());
                bundle.putParcelable("vfgGetBindAccountViewModel", vfgGetBindAccountViewModel);
                BailAccountDetailFragment toFragment = new BailAccountDetailFragment();
                toFragment.setArguments(bundle);
                start(toFragment);
            }
        });

        // 进入新增保证金账户页面
        tv_add_new_bail_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewBailFragment addNewBailFragment = new AddNewBailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("vfgGetBindAccountViewModel", vfgGetBindAccountViewModel);
                bundle.putParcelableArrayList("bailItemList", (ArrayList<VFGBailListQueryViewModel.BailItemEntity>) vfgBailListQueryViewModel.getEntityList());
                addNewBailFragment.setArguments(bundle);
                start(addNewBailFragment);
            }
        });
    }

    @Override
    protected AccountManagementPresenter initPresenter() {
        return new AccountManagementPresenter(this);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    /**
     * 头部右侧标题设置
     */
    @Override
    protected View getTitleBarView() {
        TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
        //账户设置 点击事件
        titleBarView.setRightButton("管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("Menu", AccountManagementMenuFragment.LONG_SHORT_FOREX_ACCOUNT_MANAGE);
                AccountManagementMenuFragment fragment = new AccountManagementMenuFragment();
                fragment.setArguments(bundle);
                start(fragment);
            }
        });
        return titleBarView;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "账户管理";
    }

    @Override
    public void vFGBailListQuerySuccess(VFGBailListQueryViewModel vfgBailListQueryViewModel) {
        vfgGetBindAccountViewModel = new VFGGetBindAccountViewModel();
        getPresenter().vFGGetBindAccount(vfgGetBindAccountViewModel);
    }

    // 设置适配器并刷新列表
    private void setAdapterAndNotify() {
        List<VFGBailListQueryViewModel.BailItemEntity> entityList = vfgBailListQueryViewModel.getEntityList();
        Collections.sort(entityList);

        // 判断是否是当前签约账户下的保证金账户
        String currentAccountNumber = vfgGetBindAccountViewModel.getAccountNumber();
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : entityList) {
            if (currentAccountNumber.equals(bailItemEntity.getAccountNumber())) {
                bailItemEntity.setCurrentAccount(true);
            }
        }

        //<editor-fold desc="赋值PsnVFGBailListQuery (021接口)缺少的字段">
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : entityList) {
            if (bailItemEntity.isCurrentAccount()) {
                for (VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo bailAccountInfo : vfgBailAccountInfoListQueryViewModel.getList()) {
                    if (bailAccountInfo.getMarginAccountNo().endsWith(bailItemEntity.getMarginAccountNo())) {
                        BeanConvertor.toBean(bailAccountInfo, bailItemEntity);

                        VFGBailListQueryViewModel.BailItemEntity.SettleCurrencyEntity currencyEntity = new VFGBailListQueryViewModel.BailItemEntity.SettleCurrencyEntity();
                        BeanConvertor.toBean(bailAccountInfo.getSettleCurrency2(), currencyEntity);

                        bailItemEntity.setSettleCurrency2(currencyEntity);

                        break;
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="把当前交易账户放在最上面">
        List<VFGBailListQueryViewModel.BailItemEntity> sortedEntityList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : entityList) {
            if (bailItemEntity.isCurrentAccount()) {
                sortedEntityList.add(0, bailItemEntity);
            } else {
                sortedEntityList.add(bailItemEntity);
            }
        }
        //</editor-fold>

        if (mAdapter == null) {
            mAdapter = new BailAccountAdapter(mActivity, sortedEntityList, this);
            lv_list.setAdapter(mAdapter);
        } else {
            mAdapter.setList(sortedEntityList);
            mAdapter.notifyDataSetChanged();
        }

        // 如果新增的保证金账户属于当前交易账户，那么提示“转入保证金”
        if (newBaiAccount != null) {
            if (vfgGetBindAccountViewModel.getAccountNumber().equals(newBaiAccount.getAccountNumber())) {
                final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mActivity);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dialog.getTvNotice().getLayoutParams();
                layoutParams.height = 260;
                dialog.setTitle("");
                dialog.setNoticeContent("保证金账户添加成功，为保证交易顺利进行，请转入资金后再进行交易");
                dialog.getBtnLeft().setText("暂不转入");
                dialog.getBtnRight().setText("转入保证金账户");
                dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_main_bg_color), getResources().getColor(R.color.boc_main_bg_color), getResources().getColor(R.color.boc_text_color_red), getResources().getColor(R.color.boc_text_color_red));

                dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {

                    @Override
                    public void onLeftBtnClick(View view) {
                        newBaiAccount = null;
                        dialog.dismiss();
                    }

                    @Override
                    public void onRightBtnClick(View view) {
                        dialog.dismiss();
                        newBaiAccount = null;
                    }
                });

                dialog.show();
            } else { // 不是当前交易账户时，直接置为null
                newBaiAccount = null;
            }
        }
    }

    @Override
    public void vFGBailListQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

        if (mAdapter != null) {
            mAdapter.getList().clear();
            mAdapter.notifyDataSetChanged();
        }

        newBaiAccount = null;
    }

    @Override
    public void vFGGetBindAccountSuccess(VFGGetBindAccountViewModel vfgGetBindAccountViewModel) {
        vfgBailAccountInfoListQueryViewModel = new VFGBailAccountInfoListQueryViewModel();
        getPresenter().vFGBailAccountInfoListQuery(vfgBailAccountInfoListQueryViewModel);
    }

    @Override
    public void vFGGetBindAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void vFGBailAccountInfoListQuerySuccess(VFGBailAccountInfoListQueryViewModel viewModel) {
        VFGFilterDebitCardViewModel vfgFilterDebitCardViewModel = new VFGFilterDebitCardViewModel();
        getPresenter().vFGFilterDebitCard(vfgFilterDebitCardViewModel);
    }

    @Override
    public void vFGBailAccountInfoListQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        newBaiAccount = null;
    }

    @Override
    public void vFGFilterDebitCardSuccess(VFGFilterDebitCardViewModel viewModel) {
        vfgFilterDebitCardViewModel = viewModel;
        closeProgressDialog();
        setAdapterAndNotify();
        tv_add_new_bail_account.setVisibility(View.VISIBLE); // 显示新增保证金的按钮
    }

    @Override
    public void vFGFilterDebitCardFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void setPresenter(AccountManagementContact.Presenter presenter) {
    }
}