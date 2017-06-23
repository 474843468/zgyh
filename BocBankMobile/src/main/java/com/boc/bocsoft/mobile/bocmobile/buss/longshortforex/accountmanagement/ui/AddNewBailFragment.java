package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter.BailProductAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailProductQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignPreViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.AddNewBailPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：双向宝-账户管理-新增保证金账户
 * Created by zhx on 2016/12/8
 */
public class AddNewBailFragment extends MvpBussFragment<AddNewBailPresenter> implements AddNewBailContact.View {
    private View mRootView;
    private ListView lv_list;
    private SelectAgreementView sav_read;
    private TextView tv_next;
    private VFGBailProductQueryViewModel vfgBailProductQueryViewModel;
    private BailProductAdapter mAdapter;
    private EditChoiceWidget ecw_choose_debit_card;
    private TextView tv_notice_choose_bail_product;
    private TextView tv_notice_no_bail_product_for_choose;
    private VFGGetBindAccountViewModel vfgGetBindAccountViewModel;
    private VFGSignPreViewModel vfgSignPreViewModel;
    private VFGFilterDebitCardViewModel.DebitCardEntity mDebitCardEntity; // 选择的借记卡账户
    private boolean isNeedRequestBindAccount = false; // 是否需要调用接口请求“当前交易账户”
    private List<VFGBailListQueryViewModel.BailItemEntity> bailItemList;
    private List<String> filterCurrenyList = new ArrayList<String>(); // 过滤的币种列表(过滤保证金产品是否可选)
    private int fromWhere;
    private boolean isHasToAdd = false; // 是否有可添加的保证金产品

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_add_new_bail, null);
        return mRootView;
    }

    @Override
    public void initView() {
        lv_list = (ListView) mRootView.findViewById(R.id.lv_list);

        View new_bail_list_header = View.inflate(mActivity, R.layout.layout_add_new_bail_list_header, null);
        ecw_choose_debit_card = (EditChoiceWidget) new_bail_list_header.findViewById(R.id.ecw_choose_debit_card);
        tv_notice_choose_bail_product = (TextView) new_bail_list_header.findViewById(R.id.tv_notice_choose_bail_product);
        tv_notice_no_bail_product_for_choose = (TextView) new_bail_list_header.findViewById(R.id.tv_notice_no_bail_product_for_choose);
        View new_bail_list_footer = View.inflate(mActivity, R.layout.layout_add_new_bail_list_footer, null);
        sav_read = (SelectAgreementView) new_bail_list_footer.findViewById(R.id.sav_read);
        tv_next = (TextView) new_bail_list_footer.findViewById(R.id.tv_next);
        lv_list.addHeaderView(new_bail_list_header);
        lv_list.addFooterView(new_bail_list_footer);
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        // 1表示账户管理首页，2表示变更交易账户
        fromWhere = arguments.getInt("fromWhere", 1);
        mDebitCardEntity = arguments.getParcelable("mDebitCardEntity");
        vfgGetBindAccountViewModel = arguments.getParcelable("vfgGetBindAccountViewModel");
        bailItemList = arguments.getParcelableArrayList("bailItemList");

        sav_read.setAgreement("本人（甲方）已仔细阅读并理解《中国银行股份有限公司个人“双向外汇宝、账户贵金属”买卖交易协议》，并完全同意和接受协议书全部条款和内容，愿意履行和承担该协议书中约定的权利和义务。");

        if (fromWhere == 1) {
            showLoadingDialog("加载中...");

            if (vfgGetBindAccountViewModel == null || TextUtils.isEmpty(vfgGetBindAccountViewModel.getAccountNumber())) {
                isNeedRequestBindAccount = true;
            } else {
                ecw_choose_debit_card.setChoiceTextContent(NumberUtils.formatCardNumberStrong(vfgGetBindAccountViewModel != null ? vfgGetBindAccountViewModel.getAccountNumber() : "请选择"));
            }

            if (isNeedRequestBindAccount) { // 现在改到“管理”，这里肯定会请求
                vfgGetBindAccountViewModel = new VFGGetBindAccountViewModel();
                getPresenter().vFGGetBindAccount(vfgGetBindAccountViewModel);
            }
        } else {
            ecw_choose_debit_card.setChoiceTextContent(NumberUtils.formatCardNumberStrong(mDebitCardEntity.getAccountNumber()));
            ecw_choose_debit_card.setArrowImageGone(false);

            showLoadingDialog("加载中...");
            getPresenter().vFGGetBindAccount(vfgGetBindAccountViewModel);
        }
    }

    // 查询可签约保证金产品
    private void queryBailProduct() {
        vfgBailProductQueryViewModel = new VFGBailProductQueryViewModel();
        vfgBailProductQueryViewModel.setCurrentIndex("0");
        vfgBailProductQueryViewModel.setPageSize("16");
        vfgBailProductQueryViewModel.set_refresh("true");
        getPresenter().vFGBailProductQuery(vfgBailProductQueryViewModel);
    }

    @Override
    public void setListener() {
        if (fromWhere == 1) {
            ecw_choose_debit_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChooseLongshortAccountFragment toFragment = ChooseLongshortAccountFragment.newInstance(vfgGetBindAccountViewModel.getAccountNumber(), vfgGetBindAccountViewModel.getAccountNumber(), 1);
                    startForResult(toFragment, 222);
                }
            });
        }
        sav_read.setOnClickContractListener(new SelectAgreementView.OnClickContractListener() {
            @Override
            public void onClickContract(int index) {
                String customerName = ((ApplicationContext) (mActivity.getApplicationContext())).getUser().getCustomerName();
                String url = "file:///android_asset/webviewcontent/longshortforex/accountmanagement/notice.html";
                start(ContractFragment.newInstance(url, customerName));
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkData()) {
                    return;
                }

                int currentSelectIndex = mAdapter.getCurrentSelectIndex();
                showLoadingDialog("操作中...", false);
                vfgSignPreViewModel = new VFGSignPreViewModel();

                if (fromWhere == 1) { // 1表示来自首页
                    if (mDebitCardEntity != null) {
                        vfgSignPreViewModel.setAccountId(mDebitCardEntity.getAccountId());
                        vfgSignPreViewModel.setAccountNumber(mDebitCardEntity.getAccountNumber());
                    } else {
                        vfgSignPreViewModel.setAccountId(vfgGetBindAccountViewModel.getAccountId());
                        vfgSignPreViewModel.setAccountNumber(vfgGetBindAccountViewModel.getAccountNumber());
                    }
                } else { // 2表示来自变更交易账户
                    vfgSignPreViewModel.setAccountId(mDebitCardEntity.getAccountId());
                    vfgSignPreViewModel.setAccountNumber(mDebitCardEntity.getAccountNumber());
                }

                VFGBailProductQueryViewModel.VFGBailProduct product = mAdapter.getItem(currentSelectIndex);
                vfgSignPreViewModel.setBailName(product.getBailCName());
                vfgSignPreViewModel.setBailNo(product.getBailNo());
                vfgSignPreViewModel.setSettleCurrency(product.getSettleCurrency());

                getPresenter().setActivity(mActivity);
                getPresenter().vFGSignPre(vfgSignPreViewModel);
            }
        });

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }

                VFGBailProductQueryViewModel.VFGBailProduct item = mAdapter.getItem(position - 1);
                String settleCurrency = item.getSettleCurrency();

                if (!filterCurrenyList.contains(settleCurrency)) {
                    mAdapter.setCurrentSelectIndex(position - 1);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDebitCardEntity = data.getParcelable("debitCardEntity");

        if (vfgGetBindAccountViewModel.getAccountNumber().equals(mDebitCardEntity.getAccountNumber())) {
            ecw_choose_debit_card.setChoiceTextName("交易账户");
        } else {
            ecw_choose_debit_card.setChoiceTextName("资金账户");
        }

        //<editor-fold desc="生成过滤币种列表:再次">
        filterCurrenyList.clear();
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : bailItemList) {
            if (mDebitCardEntity.getAccountNumber().equals(bailItemEntity.getAccountNumber())) {
                filterCurrenyList.add(bailItemEntity.getSettleCurrency());
            }
        }
        //</editor-fold>

        //<editor-fold desc="判断是否有可添加的保证金产品:再次">
        isHasToAdd = false;
        for (VFGBailProductQueryViewModel.VFGBailProduct product : vfgBailProductQueryViewModel.getList()) {
            if (!filterCurrenyList.contains(product.getSettleCurrency())) {
                isHasToAdd = true;
                break;
            }
        }

        //<editor-fold desc="排序，把不能选的放在下面，再次">
        List<VFGBailProductQueryViewModel.VFGBailProduct> sortedList = new ArrayList<VFGBailProductQueryViewModel.VFGBailProduct>();
        for (VFGBailProductQueryViewModel.VFGBailProduct product : vfgBailProductQueryViewModel.getList()) {
            if (!filterCurrenyList.contains(product.getSettleCurrency())) {
                sortedList.add(product);
            }
        }

        for (VFGBailProductQueryViewModel.VFGBailProduct product : vfgBailProductQueryViewModel.getList()) {
            if (filterCurrenyList.contains(product.getSettleCurrency())) {
                sortedList.add(product);
            }
        }
        //</editor-fold>

        if (isHasToAdd) {
            tv_notice_choose_bail_product.setVisibility(View.VISIBLE);
            tv_notice_no_bail_product_for_choose.setVisibility(View.GONE);
        } else {
            tv_notice_choose_bail_product.setVisibility(View.GONE);
            tv_notice_no_bail_product_for_choose.setVisibility(View.VISIBLE);
        }
        //</editor-fold>

        // 刷新列表
        mAdapter.setList(sortedList);
        mAdapter.setFilterCurrenyList(filterCurrenyList);
        mAdapter.notifyDataSetChanged();

        ecw_choose_debit_card.setChoiceTextContent(NumberUtils.formatCardNumber(mDebitCardEntity.getAccountNumber()));
    }

    // 检查数据
    private boolean checkData() {
        if (!isHasToAdd) {
            showErrorDialog("暂无可添加保证金产品");
            return false;
        }

        // 是否勾选要签约的保证金
        int currentSelectIndex = mAdapter.getCurrentSelectIndex();
        if (currentSelectIndex == -1) {
            showErrorDialog("请勾选要签约的保证金");
            return false;
        }

        // 是否勾选协议
        if (!sav_read.isSelected()) {
            showErrorDialog("请阅知勾选产品相关协议后进行下一步操作");
            return false;
        }

        return true;
    }

    @Override
    protected String getTitleValue() {
        return "新增保证金账户";
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
    protected AddNewBailPresenter initPresenter() {
        return new AddNewBailPresenter(this);
    }

    @Override
    public void vFGBailProductQuerySuccess(VFGBailProductQueryViewModel viewModel) {
        closeProgressDialog();
        setAdapterOrNotify();
    }

    // 设置适配器或者刷新列表
    private void setAdapterOrNotify() {
        List<VFGBailProductQueryViewModel.VFGBailProduct> list = vfgBailProductQueryViewModel.getList();

        //<editor-fold desc="生成过滤币种列表:初始">
        filterCurrenyList.clear();
        for (VFGBailListQueryViewModel.BailItemEntity bailItemEntity : bailItemList) {
            switch (fromWhere) {
                case 1: // 1表示来自账户管理首页
                    if (vfgGetBindAccountViewModel.getAccountNumber().equals(bailItemEntity.getAccountNumber())) {
                        filterCurrenyList.add(bailItemEntity.getSettleCurrency());
                    }
                    break;
                case 2: // 2表示来自变更交易账户
                    if (mDebitCardEntity.getAccountNumber().equals(bailItemEntity.getAccountNumber())) {
                        filterCurrenyList.add(bailItemEntity.getSettleCurrency());
                    }
                    break;
            }

        }
        //</editor-fold>

        //<editor-fold desc="判断是否有可添加的保证金产品:初始">
        isHasToAdd = false;
        for (VFGBailProductQueryViewModel.VFGBailProduct product : list) {
            if (!filterCurrenyList.contains(product.getSettleCurrency())) {
                isHasToAdd = true;
                break;
            }
        }

        if (isHasToAdd) {
            tv_notice_choose_bail_product.setVisibility(View.VISIBLE);
            tv_notice_no_bail_product_for_choose.setVisibility(View.GONE);
        } else {
            tv_notice_choose_bail_product.setVisibility(View.GONE);
            tv_notice_no_bail_product_for_choose.setVisibility(View.VISIBLE);
        }
        //</editor-fold>

        //<editor-fold desc="排序，把不能选的保证金产品放在下面，首次">
        List<VFGBailProductQueryViewModel.VFGBailProduct> sortedList = new ArrayList<VFGBailProductQueryViewModel.VFGBailProduct>();
        for (VFGBailProductQueryViewModel.VFGBailProduct product : list) {
            if (!filterCurrenyList.contains(product.getSettleCurrency())) {
                sortedList.add(product);
            }
        }

        for (VFGBailProductQueryViewModel.VFGBailProduct product : list) {
            if (filterCurrenyList.contains(product.getSettleCurrency())) {
                sortedList.add(product);
            }
        }
        //</editor-fold>


        if (mAdapter == null) {
            mAdapter = new BailProductAdapter(mActivity, sortedList);
            mAdapter.setFilterCurrenyList(filterCurrenyList);
            lv_list.setAdapter(mAdapter);
        } else {
            mAdapter.setList(sortedList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void vFGBailProductQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void vFGSignPreSuccess(VerifyBean verifyBean) {
        closeProgressDialog();
        AddNewBailConfirmFragment fragment = AddNewBailConfirmFragment.newInstance(vfgSignPreViewModel, verifyBean, vfgBailProductQueryViewModel.getList().get(mAdapter.getCurrentSelectIndex()), fromWhere);
        start(fragment);
    }

    @Override
    public void vFGSignPreFail(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void vFGGetBindAccountSuccess(VFGGetBindAccountViewModel vfgGetBindAccountViewModel) {
        if (fromWhere == 1) {
            ecw_choose_debit_card.setChoiceTextContent(NumberUtils.formatCardNumberStrong(vfgGetBindAccountViewModel != null ? vfgGetBindAccountViewModel.getAccountNumber() : "请选择"));
        }
        // 开始查询保证金账户列表
        VFGBailListQueryViewModel vfgBailListQueryViewModel = new VFGBailListQueryViewModel();
        getPresenter().vFGBailListQuery(vfgBailListQueryViewModel);
    }

    @Override
    public void vFGGetBindAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void vFGBailListQuerySuccess(VFGBailListQueryViewModel vfgBailListQueryViewModel) {
        bailItemList = vfgBailListQueryViewModel.getEntityList();

        queryBailProduct();
    }

    @Override
    public void vFGBailListQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(AddNewBailContact.Presenter presenter) {
    }
}