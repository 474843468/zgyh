package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview.AccountCurrentHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview.ButtonModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinanceContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinancePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.MoreAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         16/6/23 15:49
 *         电子账户详情
 */
@SuppressLint("ValidFragment")
public class FinanceTransferDetailFragment extends BaseAccountFragment<FinancePresenter> implements FinanceContract.FinanceAccountTransferView, AccountCurrentHead.BtnCallback, View.OnClickListener, TitleAndBtnDialog.DialogBtnClickCallBack, AccountCurrentHead.ImgShowSumCallback {

    /**
     * 账户信息--按钮--充值
     */
    private final int HEAD_BUTTON_TRANSFER = 1;
    /**
     * 账户信息--按钮--新建/删除签约关系
     */
    private final int HEAD_BUTTON_SIGN = 2;

    /**
     * 账户信息
     */
    private FinanceModel financeModel;
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
     * 解除签约对话框
     */
    private TitleAndBtnDialog cancelDialog;
    /**
     * 是否从账户概览进入
     */
    private boolean isOverView;

    /**
     * 初始化传入账户信息
     *
     * @param financeModel
     */
    public FinanceTransferDetailFragment(FinanceModel financeModel, boolean isOverView) {
        this.financeModel = financeModel;
        this.isOverView = isOverView;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_finance_account_transfer_detail, null);
    }

    @Override
    protected String getTitleValue() {
        return financeModel == null ? "" : NumberUtils.formatCardNumber(financeModel.getFinanICNumber());
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
    }

    @Override
    public void setListener() {
        btnMore.setOnClickListener(this);
        currentHead.setOnclick(this);
        currentHead.setImgShowSumCallback(this);
    }

    @Override
    public void initData() {
        //初始化头部账户信息
        initHead();

        tranDetail.setAdapter();

        //查询交易明细
        showLoadingDialog();
        getPresenter().queryAllOfFinance(financeModel);
    }

    @Override
    protected FinancePresenter initPresenter() {
        return new FinancePresenter(this);
    }

    /**
     * 初始化账户详情头部信息
     */
    private void initHead() {
        currentHead.setVisibility(View.VISIBLE);
        //设置标题
        currentHead.setHeadTitle(getString(R.string.boc_finance_account_balance_title));
        //设置币种,余额
        if (financeModel.getSupplyBalance() != null)
            currentHead.setDataSum(PublicCodeUtils.getCurrency(getActivity(), financeModel.getCurrency()), financeModel.getSupplyBalance().toString());
        //隐藏账户列表按钮
        currentHead.imgShow(false);

        //生成充值,帮助按钮
        initHeadBtn(false);
    }

    /**
     * 根据绑定关系,生成头部账户信息按钮
     *
     * @param isSign
     */
    private void initHeadBtn(boolean isSign) {
        List<ButtonModel> btnModels = new ArrayList<>();

        //生成充值按钮
        btnModels.add(new ButtonModel(HEAD_BUTTON_TRANSFER, getString(R.string.boc_finance_account_recharge), R.drawable.boc_finance_transfer));

        //如果为纯电子现金账户,生成新建/删除签约关系按钮
        if (financeModel.isFinanceAccount()) {
            String name = isSign ? getString(R.string.boc_finance_account_transfer_detail_sign_cancel) : getString(R.string.boc_finance_account_transfer_detail_sign_build);
            int resId = isSign ? R.drawable.boc_finance_sign_cancel : R.drawable.boc_finance_sign_create;

            btnModels.add(new ButtonModel(HEAD_BUTTON_SIGN, name, resId));
        }

        //设置按钮
        currentHead.setButton(btnModels);
    }

    /**
     * 头部账户信息按钮点击事件
     *
     * @param tag
     */
    @Override
    public void onClickListener(int tag) {
        switch (tag) {
            //充值按钮
            case HEAD_BUTTON_TRANSFER:
                start(new FinanceTransferSelfFragment(financeModel));
                break;
            //新建/删除签约按钮
            case HEAD_BUTTON_SIGN:
                if (financeModel.isSign())
                    cancelSign();
                else
                    start(new SelectFinanceAccountFragment(SelectFinanceAccountFragment.SELECT_ACCOUNT_SIGN, financeModel));
                break;
        }
    }

    /**
     * 弹出确认解除签约关系框
     */
    private void cancelSign() {
        if (cancelDialog == null) {
            cancelDialog = new TitleAndBtnDialog(getActivity());
            cancelDialog.setBtnName(new String[]{getString(R.string.boc_common_cancel), getString(R.string.boc_common_sure)});
            cancelDialog.setTitle(getString(R.string.boc_finance_account_transfer_detail_sign_dialog_title));
            cancelDialog.setNoticeContent(getString(R.string.boc_finance_account_transfer_detail_sign_dialog_content) + NumberUtils.formatCardNumber(financeModel.getBankAccountNumber()));
            cancelDialog.setTitleBackground(getResources().getColor(R.color.boc_common_cell_color));
            cancelDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            cancelDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            cancelDialog.setDialogBtnClickListener(this);
            cancelDialog.isShowTitle(true);
        }
        cancelDialog.show();
    }

    /**
     * 跳转全部交易名称界面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_more) {
            //跳转全部交易明细界面
            TransDetailFragment detailFragment = new TransDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(TransDetailFragment.DETAIL_ACCOUNT_BEAN, ModelUtil.generateIncomeAccountBean(financeModel));
            bundle.putInt(TransDetailFragment.DETAIL_TYPE, TransDetailFragment.DETAIL_ACCOUNT_TYPE_FINANCE);
            detailFragment.setArguments(bundle);
            start(detailFragment);
        }
    }

    @Override
    protected void titleRightIconClick() {
        if (isOverView)
            start(new MoreAccountFragment(ModelUtil.generateAccountBean(financeModel), null));
        else
            start(new FinanceDetailInfoFragment(financeModel));
    }

    /**
     * 取消签约对话框,取消按钮
     *
     * @param view
     */
    @Override
    public void onLeftBtnClick(View view) {
        cancelDialog.dismiss();
    }

    /**
     * 取消签约对话框,确认按钮
     *
     * @param view
     */
    @Override
    public void onRightBtnClick(View view) {
        cancelDialog.dismiss();

        //取消签约
        showLoadingDialog(false);
        getPresenter().psnFinanceICSignCancel(financeModel);
    }

    @Override
    public boolean onBack() {
        setFragmentResult();
        return super.onBack();
    }

    @Override
    public boolean onBackPress() {
        setFragmentResult();
        return super.onBackPress();
    }

    private void setFragmentResult() {
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountId(financeModel.getAccountId());
        accountBean.setAccountType(financeModel.getFinanICType());
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BEAN, accountBean);
        setFramgentResult(RESULT_OK, bundle);
    }

    @Override
    public void psnFinanceAccountDetail(FinanceModel financeModel) {
        if (financeModel == null) {
            currentHead.setVisibility(View.GONE);
            return;
        }
        this.financeModel = financeModel;
        initHead();
    }

    /**
     * 最近交易详情回调
     *
     * @param detailModels
     * @param transactionList
     */
    @Override
    public void psnFinanceTransferDetail(List<TransDetailModel> detailModels, List<TransactionBean> transactionList) {
        //没有数据
        if (transactionList == null || transactionList.isEmpty()) {
            currentHead.setVisibility(View.VISIBLE);
            currentHead.showCenterListTitle(true, getString(R.string.boc_finance_account_transfer_detail_hint_no_data));
            return;
        }

        //缓存TransDetailModel数据,设置TransactionAdapter数据
        tranDetail.setData(transactionList);
        currentHead.showListTitle(true, getString(R.string.boc_finance_account_transfer_detail_hint));
        requestFocus(currentHead);
    }

    /**
     * 查询签约关系回调
     *
     * @param bankAccountId
     */
    @Override
    public void psnFinanceSignQuery(String bankAccountId) {
        //设置绑定银行账户Id,设置头部按钮
        financeModel.setBankAccountNumber(bankAccountId);
        initHeadBtn(financeModel.isSign());
    }

    /**
     * 取消签约成功回调
     */
    @Override
    public void cancelSignSuccess() {
        //置空绑定银行卡Id,设置头部按钮
        Toast.makeText(getActivity(), getString(R.string.boc_finance_account_sign_cancel_success), Toast.LENGTH_SHORT).show();
        financeModel.setBankAccountId(null);
        financeModel.setBankAccountNumber(null);
        initHeadBtn(false);
        closeProgressDialog();
    }
}
