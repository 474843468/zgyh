package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview.AccountCurrentHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview.ButtonModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui.FinanceTransferDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.AccountInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         16/8/11 23:03
 *         活期账户界面
 */
@SuppressLint("ValidFragment")
public class CurrentFragment extends BaseOverviewFragment<OverviewPresenter> implements OverviewContract.CurrentView, View.OnClickListener, AccountCurrentHead.BtnCallback, AccountCurrentHead.ImgShowSumCallback, AccountCurrentHead.ButtonOtherCallback, TransactionView.ClickListener, AccountCurrentHead.ImgMessageCallback {

    /**
     * 账户信息--按钮--转账
     */
    private final int HEAD_BUTTON_TRANSFER = 1;
    /**
     * 账户信息--按钮--买理财
     */
    private final int HEAD_BUTTON_BUY = 2;
    /**
     * 账户信息--按钮--存定期
     */
    private final int HEAD_BUTTON_REGULAR = 3;

    /**
     * 头部账户信息
     */
    private AccountCurrentHead currentHead;
    /**
     * 交易明细列表
     */
    private TransactionView tranDetail;
    /**
     * 按钮-更多
     */
    private Button btnMore;
    /**
     * 账户信息
     */
    private AccountBean accountBean;
    private AccountInfoBean accountInfoBean;
    /**
     * 交易明细无数据提示
     */
    private TextView tvTrans;
    /**
     * 额度信息
     */
    private List<AccountListItemViewModel.CardAmountViewModel> cardModels;

    public CurrentFragment() {
    }

    public CurrentFragment(AccountListItemViewModel itemViewModel) {
        this.accountBean = itemViewModel.getAccountBean();

        if (itemViewModel.getCardAmountViewModelList() != null && !itemViewModel.getCardAmountViewModelList().isEmpty())
            this.cardModels = itemViewModel.getCardAmountViewModelList();

        this.accountInfoBean = ModelUtil.generateAccountInfoBean(itemViewModel);
    }

    @Override
    protected OverviewPresenter initPresenter() {
        return new OverviewPresenter(this);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_overview_current, null);
    }

    @Override
    protected String getTitleValue() {
        if (accountBean == null)
            accountBean = getAccountBean();
        return NumberUtils.formatCardNumber(accountBean.getAccountNumber());
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    public void initView() {
        currentHead = (AccountCurrentHead) mContentView.findViewById(R.id.head_account);
        tranDetail = (TransactionView) mContentView.findViewById(R.id.tran_detail);
        btnMore = (Button) mContentView.findViewById(R.id.btn_more);
        tvTrans = (TextView) mContentView.findViewById(R.id.tv_trans);
    }

    @Override
    public void setListener() {
        btnMore.setOnClickListener(this);
        currentHead.setOnclick(this);
        currentHead.setImgShowSumCallback(this);
        currentHead.setImgMessageOnclick(this);
        tranDetail.setListener(this);
    }

    @Override
    public void initData() {
        tranDetail.setAdapter();
    }

    @Override
    public void reInit() {
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (isCurrentFragment()) {
            //查询账户详情,交易明细
            showLoadingDialog();
            if (cardModels != null)
                initHead();

            getPresenter().queryAccountDetailAndTransaction(accountBean);
        }
    }

    private void initHead() {
        currentHead.setVisibility(View.VISIBLE);
        //设置标题
        currentHead.setHeadTitle(getString(R.string.boc_overview_detail_balance));

        //设置额度列表
        currentHead.setDetails(cardModels);

        //隐藏账户列表按钮
        if (cardModels != null && cardModels.size() > 1)
            currentHead.imgShow(true);
        else
            currentHead.imgShow(false);

        //生成转账,买理财,存定期按钮
        List<ButtonModel> btnModels = new ArrayList<>();
        btnModels.add(new ButtonModel(HEAD_BUTTON_TRANSFER, getString(R.string.boc_overview_detail_transfer)));
        if (!ApplicationConst.ACC_TYPE_BOCINVT.equals(accountBean.getAccountType())) {
            btnModels.add(new ButtonModel(HEAD_BUTTON_BUY, getString(R.string.boc_overview_detail_buy)));
            btnModels.add(new ButtonModel(HEAD_BUTTON_REGULAR, getString(R.string.boc_overview_detail_regular)));
        }
        currentHead.setButton(btnModels);

        if (AccountTypeUtil.RESULT_TRUE_FINANCE_MEDICAL.equals(accountBean.getIsECashAccount()))
            currentHead.showFinancePanel(this);
        if (AccountTypeUtil.RESULT_TRUE_FINANCE_MEDICAL.equals(accountBean.getIsMedicalAccount()))
            currentHead.showMedicalPanel(this);
    }

    @Override
    protected void titleRightIconClick() {
        start(new MoreAccountFragment(accountBean, accountInfoBean));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_finance) {
            start(new FinanceTransferDetailFragment(ModelUtil.generateFinanceModel(accountBean), false));
        } else if (v.getId() == R.id.tv_medical) {
            start(new MedicalInsuranceFragment(accountBean));
        } else if (v.getId() == R.id.btn_more) {
            goTransDetailFragment(accountBean, TransDetailFragment.DETAIL_ACCOUNT_TYPE_COMMON);
        } else {
            showErrorDialog(getString(R.string.boc_overview_detail_balance_hint));
        }
    }

    @Override
    public void onItemClickListener(int position) {
        goTransDetailInfoFragment(getPresenter().getTransDetail(position), TransDetailFragment.DETAIL_ACCOUNT_TYPE_COMMON);
    }

    @Override
    public void onClickListener(int tag) {
        switch (tag) {
            case HEAD_BUTTON_TRANSFER:
                TransRemitBlankFragment bussFragment = new TransRemitBlankFragment();
                Bundle bundle = new Bundle();
                bundle.putString(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUNTMANAGEMENT, accountBean.getAccountId());
                bussFragment.setArguments(bundle);
                start(bussFragment);
                break;
            case HEAD_BUTTON_BUY:
                start(new WealthProductFragment(true));
                break;
            case HEAD_BUTTON_REGULAR:
                ModuleActivityDispatcher.dispatch(mActivity, ModuleCode.MODEUL_DEPTSTORAGE);
                break;
        }
    }

    @Override
    public void queryAccountDetail(AccountListItemViewModel model, AccountInfoBean accountInfoBean) {
        if (model != null) {
            cardModels = model.getCardAmountViewModelList();
            this.accountInfoBean = accountInfoBean;
            initHead();
        }else{
            accountBean.setAccountStatus(null);
        }
    }

    @Override
    public void queryAccountTransDetail(List<TransactionBean> transactionBeans) {
        if (transactionBeans == null) {
            tvTrans.setVisibility(View.VISIBLE);
            return;
        }

        tvTrans.setVisibility(View.GONE);
        currentHead.showListTitle(true, getString(R.string.boc_trans_detail_title_data));
        tranDetail.setData(transactionBeans);
        requestFocus(currentHead);
    }

    @Override
    public boolean onBack() {
        setFragmentResult(accountBean);
        return super.onBack();
    }

    @Override
    public boolean onBackPress() {
        setFragmentResult(accountBean);
        return super.onBackPress();
    }
}
