package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ProgressStateListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.StateItemAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter.SelectBailAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.ChangeState;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.ChangeTask;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGChangeContractViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.SelectBailPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：双向宝-账户管理-选择要迁移的保证金列表界面
 * Created by zhx on 2016/12/19
 */
public class SelectBailFragment extends MvpBussFragment<SelectBailPresenter> implements SelectBailContact.View {
    private View mRootView;
    private ListView lv_list;
    private ListView lv_list_not_transfer;
    private TextView tv_just_change_trade_account;
    private TextView tv_transfer_and_change_trade;
    private LinearLayout ll_bottom_list;
    private List<VFGBailListQueryViewModel.BailItemEntity> oldList; // “原交易账户”保证金列表
    private List<VFGBailListQueryViewModel.BailItemEntity> newList; // “新交易账户”保证金列表
    private List<String> filterCurrenyList; // 得到过滤的币种列表
    private List<ChangeTask> taskList; // 要变更的任务列表
    private List<VFGBailListQueryViewModel.BailItemEntity> selectList; // 选中的保证金列表
    private VFGFilterDebitCardViewModel.DebitCardEntity mDebitCardEntity; // 选择的借记卡账户(对应“新交易账户”)
    private String currentTradeAccountNo; // 当前交易账户(即“原交易账户”)
    private ProgressStateListDialog mProgressDialog;
    private StateItemAdapter stateItemAdapter;
    private boolean isTradeChangeSuccess = false; // 交易账户是否变更成功,默认为false
    private boolean isAllChangeContractFail = true; // 保证金变更签约账户是不是全部失败,默认全部失败
    private int failAccount;
    private List<VFGBailListQueryViewModel.BailItemEntity> topList; // 可迁移的保证金列表
    private List<VFGBailListQueryViewModel.BailItemEntity> bottomList; // 不可迁移的保证金列表
    private int operateType = 0; // 操作类型：0表示“不迁移，继续变更”，1表示“迁移，且变更”


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_select_bail, null);
        return mRootView;
    }

    @Override
    public void initView() {
        lv_list = (ListView) mRootView.findViewById(R.id.lv_list);
        lv_list_not_transfer = (ListView) mRootView.findViewById(R.id.lv_list_not_transfer);
        tv_just_change_trade_account = (TextView) mRootView.findViewById(R.id.tv_just_change_trade_account);
        tv_transfer_and_change_trade = (TextView) mRootView.findViewById(R.id.tv_transfer_and_change_trade);
        ll_bottom_list = (LinearLayout) mRootView.findViewById(R.id.ll_bottom_list);
    }

    @Override
    public void initData() {
        oldList = getArguments().getParcelableArrayList("oldList");
        newList = getArguments().getParcelableArrayList("newList");
        filterCurrenyList = getArguments().getStringArrayList("filterCurrenyList");
        mDebitCardEntity = getArguments().getParcelable("mDebitCardEntity");
        currentTradeAccountNo = getArguments().getString("currentTradeAccountNo");
        taskList = new ArrayList<ChangeTask>();
        selectList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();

        //<editor-fold desc="清空保证金的选中">
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : oldList) {
            bailItemEntity.setChecked(false);
        }
        //</editor-fold>

        //<editor-fold desc="得到topList和bottomList列表">
        topList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();
        bottomList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : oldList) {
            if (!filterCurrenyList.contains(bailItemEntity.getSettleCurrency())) {
                topList.add(bailItemEntity);
            }
        }

        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : oldList) {
            if (filterCurrenyList.contains(bailItemEntity.getSettleCurrency())) {
                bottomList.add(bailItemEntity);
            }
        }
        //</editor-fold>

        //<editor-fold desc="显示列表">
        if (topList.size() == 0) {
            lv_list.setVisibility(View.GONE);
        } else {
            SelectBailAdapter bopAdapter = new SelectBailAdapter(mActivity, topList);
            bopAdapter.setFilterCurrenyList(filterCurrenyList);
            lv_list.setAdapter(bopAdapter);
        }

        if (bottomList.size() == 0) {
            ll_bottom_list.setVisibility(View.GONE);
        } else {
            SelectBailAdapter bottomAdapter = new SelectBailAdapter(mActivity, bottomList);
            bottomAdapter.setFilterCurrenyList(filterCurrenyList);
            lv_list_not_transfer.setAdapter(bottomAdapter);
        }
        //</editor-fold>
    }

    @Override
    public void setListener() {
        // 点击"不迁移,继续变更"按钮
        tv_just_change_trade_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateType = 0;
                int size = newList.size();
                if (size == 0) {
                    showErrorDialog("您选择的新交易账户下没有保证金,请迁移保证金并变更");
                } else {
                    showLoadingDialog("加载中...", false);
                    //<editor-fold desc="开始设置交易账户">
                    VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel = new VFGSetTradeAccountViewModel();
                    vfgSetTradeAccountViewModel.setAccountId(mDebitCardEntity.getAccountId());
                    getPresenter().vFGSetTradeAccount(vfgSetTradeAccountViewModel);
                    //</editor-fold>
                }
            }
        });

        // 点击"迁移,且变更"按钮
        tv_transfer_and_change_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateType = 1;
                //<editor-fold desc="得到选中的保证金列表(通过过滤当前交易账户的保证金列表即oldList得到)">
                selectList.clear(); // 清空选中的列表
                for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : topList) {
                    if (bailItemEntity.isChecked()) {
                        selectList.add(bailItemEntity);
                    }
                }
                //</editor-fold>

                if (!checkData()) {
                    return;
                }

                // 生成Task列表
                taskList.clear();
                for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : selectList) {
                    ChangeTask changeTask = new ChangeTask();
                    changeTask.setAccountNumber(bailItemEntity.getAccountNumber());
                    changeTask.setMarginAccountNo(bailItemEntity.getMarginAccountNo());
                    changeTask.setSettleCurrency(bailItemEntity.getSettleCurrency());
                    changeTask.setAccountId(mDebitCardEntity.getAccountId());
                    taskList.add(changeTask);
                }

                // 显示进度对话框
                mProgressDialog = new ProgressStateListDialog(mActivity);
                stateItemAdapter = new StateItemAdapter(mActivity, taskList);
                mProgressDialog.setAdapter(stateItemAdapter);
                mProgressDialog.setOnCloseListener(new ProgressStateListDialog.OnCloseListener() {
                    @Override
                    public void onClose() {
                        //<editor-fold desc="点击完成按钮，如果变更交易账户成功，那么跳转到AccountManagementFragment。否则留在本页面">
                        mProgressDialog.dismiss();
                        if (!isAllChangeContractFail) {
                            popToAndReInit(AccountManagementMenuFragment.class);
                        }
                        //</editor-fold>
                    }
                });
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                //<editor-fold desc="开始变更保证金账户">
                for (ChangeTask changeTask : taskList) {
                    VFGChangeContractViewModel vfgChangeContractViewModel = new VFGChangeContractViewModel();
                    vfgChangeContractViewModel.setOldAccountNumber(currentTradeAccountNo); // 原借记卡卡号
                    vfgChangeContractViewModel.setAccountId(mDebitCardEntity.getAccountId()); // 变更后借记卡账户标识
                    vfgChangeContractViewModel.setSettleCurrency(changeTask.getSettleCurrency()); // 结算货币
                    vfgChangeContractViewModel.setMarginAccountNo(changeTask.getMarginAccountNo()); // 保证金账号
                    getPresenter().vFGChangeContract(vfgChangeContractViewModel);
                }
                //</editor-fold>
            }
        });
    }

    // 检查数据
    private boolean checkData() {
        //<editor-fold desc="判断是否有可以变更的保证金产品">
        boolean isHasChangeBail = false; // 是否有可以变更的保证金产品
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : oldList) {
            if (!filterCurrenyList.contains(bailItemEntity.getSettleCurrency())) {
                isHasChangeBail = true;
                break;
            }
        }

        if (!isHasChangeBail) {
            showErrorDialog("没有可以迁移的保证金");
            return false;
        }
        //</editor-fold>

        if (selectList.size() == 0) {
            showErrorDialog("请选择要迁移的保证金");
            return false;
        }

        return true;
    }

    @Override
    protected String getTitleValue() {
        return "原保证金账户";
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
    protected SelectBailPresenter initPresenter() {
        return new SelectBailPresenter(this);
    }

    @Override
    public void vFGChangeContractSuccess(VFGChangeContractViewModel vfgChangeContractViewModel) {
        isAllChangeContractFail = false;
        //<editor-fold desc="回调中更新任务状态">
        for (ChangeTask changeTask : taskList) {
            if (changeTask.getMarginAccountNo().equals(vfgChangeContractViewModel.getMarginAccountNo())) {
                changeTask.setState(ChangeState.SUCCESS);
                break;
            }
        }
        //</editor-fold>
        handleChangeContractTaskItemFinish();
    }

    @Override
    public void vFGChangeContractFail(VFGChangeContractViewModel viewModel, BiiResultErrorException biiResultErrorException) {
        ToastUtils.show(biiResultErrorException.getErrorMessage());
        //<editor-fold desc="回调中更新任务状态">
        for (ChangeTask changeTask : taskList) {
            if (changeTask.getMarginAccountNo().equals(viewModel.getMarginAccountNo())) {
                changeTask.setState(ChangeState.FAIL);
                break;
            }
        }
        //</editor-fold>
        handleChangeContractTaskItemFinish();
    }

    // 处理“变更签约账户”单个任务的完成
    private void handleChangeContractTaskItemFinish() {
        stateItemAdapter.notifyDataSetChanged();

        int doingCount;
        synchronized (SelectBailFragment.class) {
            doingCount = 0;
            for (ChangeTask changeTask : taskList) {
                if (ChangeState.DOING == changeTask.getState()) {
                    doingCount++;
                }
            }
        }

        if (doingCount == 0) {
            // 此处要知道是不是全部变更失败
            if (!isAllChangeContractFail) { // 如果有保证金变更成功的
                //<editor-fold desc="开始设置交易账户">
                VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel = new VFGSetTradeAccountViewModel();
                vfgSetTradeAccountViewModel.setAccountId(mDebitCardEntity.getAccountId());
                getPresenter().vFGSetTradeAccount(vfgSetTradeAccountViewModel);
                //</editor-fold>
            } else {
                mProgressDialog.setBottomSate(3);
                mProgressDialog.getOKBtn().setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void vFGSetTradeAccountSuccess(VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel) {
        switch (operateType) { // 操作类型：0表示“不迁移，继续变更”，1表示“迁移，且变更”
            case 0: // 不迁移，继续变更
                closeProgressDialog();
                ToastUtils.show("变更交易账户成功");
                popToAndReInit(AccountManagementMenuFragment.class);
                break;
            case 1: // 迁移，且变更
                isTradeChangeSuccess = true; // 把“交易账户是否变更成功”标记置为true
                mProgressDialog.setBottomSate(2); // 更新界面显示：变更交易账户成功
                mProgressDialog.getOKBtn().setVisibility(View.VISIBLE);

                //<editor-fold desc="统计变更保证金失败的条数，并更新对话框标题显示">
                failAccount = 0;
                for (ChangeTask changeTask : taskList) {
                    if (3 == changeTask.getState()) { // 注：3表示变更失败
                        failAccount++;
                    }
                }

                if (failAccount > 0) {
                    mProgressDialog.getTvTitle().setText(failAccount + "笔保证金变更失败");
                } else {
                    mProgressDialog.getTvTitle().setText("变更成功");
                }
                //</editor-fold>
                break;
        }
    }

    @Override
    public void vFGSetTradeAccountFail(BiiResultErrorException biiResultErrorException) {
        switch (operateType) {
            case 0: // 操作类型:0表示“不迁移，继续变更”
                showErrorDialog(biiResultErrorException.getErrorMessage());
                break;
            case 1: // 操作类型：1表示“迁移，且变更”
                ToastUtils.show(biiResultErrorException.getErrorMessage());
                mProgressDialog.setBottomSate(3); // 更新界面显示：变更交易账户失败
                mProgressDialog.getTvTitle().setText("变更失败");
                mProgressDialog.getOKBtn().setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setPresenter(SelectBailContact.Presenter presenter) {
    }
}