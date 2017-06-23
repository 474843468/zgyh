package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview.AccountCurrentHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.XpadAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.presenter.AccountMainPersenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 理财——账户 绑定/解绑 管理明细页面
 * 根据传递的不同标签显示不同的界面
 * Created by Wan mengxin on 2016/9/17.
 */
@SuppressLint("ValidFragment")
public class AccountDetailFragment extends MvpBussFragment<AccountMainPersenter> implements AccountContract.DetailView {

    private View detailView;
    private AccountCurrentHead accountMsg;//账户详情头部插件（余额、币种相关内容）
    private DetailTableRow tradeAccount;//理财交易账户
    private DetailTableRow accountType;//账户类型
    private DetailTableRow isEbankAccount;//是否为电子银行关联账户
    private TextView tvUnRegist;//取消登记
    private TextView tvTrans;//资金划转
    private TextView tvCanle;//解除
    private AccountModel model;//用户详情数据模型
    private boolean isOFA;//是否是专属账户
    private LinearLayout xpadOnlyAccount;//专属账户信息
    private LinearLayout xpadNomalAccount;//普通账户信息
    private List<AccountModel.AccountDetaiListBean> detaiList;
    private DetailTableRow customername;//专属账户名
    private DetailTableRow bankAccount;//专属账户卡
    private DetailTableRow openState;//开通状态
    private TitleAndBtnDialog cancelDialog;//确认取消登记
    private TitleAndBtnDialog againCancelDialog;//确认解除绑定
    private XpadAccountModel item;
    private ImageView ivZone;

    public AccountDetailFragment(AccountModel model, boolean isOFA, XpadAccountModel item) {
        this.model = model;
        this.isOFA = isOFA;
        this.item = item;
    }

    protected View onCreateView(LayoutInflater mInflater) {
        detailView = View.inflate(mContext, R.layout.boc_fragment_xpad_account_detail, null);
        return detailView;
    }

    @Override
    public void initView() {
        accountMsg = (AccountCurrentHead) detailView.findViewById(R.id.accountMsg);
        ivZone = (ImageView) detailView.findViewById(R.id.ivZone);

        xpadNomalAccount = (LinearLayout) detailView.findViewById(R.id.xpadNomalAccount);
        tradeAccount = (DetailTableRow) detailView.findViewById(R.id.tradeAccount);
        accountType = (DetailTableRow) detailView.findViewById(R.id.accountType);
        isEbankAccount = (DetailTableRow) detailView.findViewById(R.id.isEbankAccount);

        xpadOnlyAccount = (LinearLayout) detailView.findViewById(R.id.xpadOnlyAccount);
        customername = (DetailTableRow) detailView.findViewById(R.id.customername);
        bankAccount = (DetailTableRow) detailView.findViewById(R.id.bankAccount);
        openState = (DetailTableRow) detailView.findViewById(R.id.openState);

        tvUnRegist = (TextView) detailView.findViewById(R.id.tvUnRegist);
        tvTrans = (TextView) detailView.findViewById(R.id.tvTrans);
        tvCanle = (TextView) detailView.findViewById(R.id.tvCanle);
    }

    @Override
    public void reInit() {
        accountMsg.setVisibility(View.GONE);
        ivZone.setVisibility(View.GONE);
        xpadOnlyAccount.setVisibility(View.GONE);
        xpadNomalAccount.setVisibility(View.GONE);
        tvUnRegist.setVisibility(View.GONE);
        tvTrans.setVisibility(View.GONE);
        tvCanle.setVisibility(View.GONE);
        initView();
        initData();
    }

    @Override
    public void initData() {
        accountMsg.setHeadTitle(mContext.getResources().getString(R.string.account_xpad_canable_remaint));
        accountMsg.isShowImgMessage(false);
        updateView();
    }

    @Override
    public void setListener() {
        //取消登记
        tvUnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
        //资金划转
        tvTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransRemitBlankFragment bussFragment = new TransRemitBlankFragment();
                Bundle bundle = new Bundle();
                bundle.putString(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUTDETAILFRAGMENT, item.getAccountId());
                bussFragment.setArguments(bundle);
                start(bussFragment);
            }
        });
        //解除绑定
        tvCanle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                againCancel();
            }
        });

        accountMsg.setImgMessageOnclick(new AccountCurrentHead.ImgMessageCallback() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 弹出确认取消登记框（普通账户）
     */
    private void cancelDialog() {
        if (cancelDialog == null) {
            cancelDialog = new TitleAndBtnDialog(getActivity());
            cancelDialog.setTitle(mContext.getResources().getString(R.string.account_xpad_check_cancle));
            cancelDialog.setNoticeContent("理财交易账户：" + NumberUtils.formatCardNumberStrong(item.getAccountNumber()));
            cancelDialog.setBtnName(new String[]{"我再想想", "取消登记"});
            cancelDialog.setTitleBackground(getResources().getColor(R.color.boc_common_cell_color));
            cancelDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            cancelDialog.setRightBtnTextBgColor(
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            cancelDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    cancelDialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    cancelDialog.dismiss();
                    showLoadingDialog(false);
                    getPresenter().psnXpadAccountCancel(item.getAccountId());
                }
            });
            cancelDialog.isShowTitle(true);
        }
        cancelDialog.show();
    }

    /**
     * 弹出确认解除绑定登记框（专属理财）
     */
    private void againCancel() {
        if (againCancelDialog == null) {
            againCancelDialog = new TitleAndBtnDialog(getActivity());
            againCancelDialog.setTitle(mContext.getResources().getString(R.string.account_xpad_check_unbind));
            String state = "";
            if ("S".equals(model.getRiskLevel())) {
                state = getResources().getString(R.string.account_xpad_openstates_s);
            } else if ("B".equals(model.getRiskLevel())) {
                state = getResources().getString(R.string.account_xpad_openstates_b);
            } else if ("R".equals(model.getRiskLevel())) {
                state = getResources().getString(R.string.account_xpad_openstates_r);
            } else if ("N".equals(model.getRiskLevel())) {
                state = getResources().getString(R.string.account_xpad_openstates_n);
            }

            String accountNum = StringUtils.isEmpty(model.bankAccount.getAccountNumber()) ? model.mainAccount.getAccountNumber() : model.bankAccount.getAccountNumber();
            againCancelDialog.setNoticeContent("网上专属理财账户：" + NumberUtils.formatCardNumberStrong(model.financialAccount.getAccountNumber()) +
                    "\r\n银行账户：" + NumberUtils.formatCardNumberStrong(accountNum) + " " + state);
            againCancelDialog.setBtnName(new String[]{"取消", "确认"});
            againCancelDialog.setTitleBackground(getResources().getColor(R.color.boc_common_cell_color));
            againCancelDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            againCancelDialog.setRightBtnTextBgColor(
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            againCancelDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    againCancelDialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    againCancelDialog.dismiss();

                    String key = StringUtils.isEmpty(model.bankAccount.getAccountKey()) ? model.mainAccount.getAccountKey() : model.bankAccount.getAccountKey();
                    showLoadingDialog(false);
                    getPresenter().psnOFADisengageBind(key);
                }
            });
            againCancelDialog.isShowTitle(true);
        }
        againCancelDialog.show();
    }

    private void updateView() {
        if (isOFA) {
            //专属理财
            xpadOnlyAccount.setVisibility(View.VISIBLE);//账户信息
            xpadNomalAccount.setVisibility(View.VISIBLE);
            tvTrans.setVisibility(View.VISIBLE);//资金划转
            tvCanle.setVisibility(View.VISIBLE);//解除

            String name = ApplicationContext.getInstance().getUser().getCustomerName();
            customername.updateValue(name);

            if (model.bankAccount != null && !StringUtils.isEmptyOrNull(model.bankAccount.getAccountNumber())) {
                bankAccount.updateValue(NumberUtils.formatCardNumberStrong(model.bankAccount.getAccountNumber()));
            } else {
                bankAccount.updateValue(NumberUtils.formatCardNumberStrong(model.mainAccount.getAccountNumber()));
            }

            /** S-已开通
             ** B-账户未绑定
             ** R-账户未关联网银
             ** N-未开通 **/

            String s = model.getOpenStatus();
            if ("S".equals(s)) {
                openState.updateValue(mContext.getResources().getString(R.string.account_xpad_openstates_s));
            } else if ("B".equals(s)) {
                openState.updateValue(mContext.getResources().getString(R.string.account_xpad_openstates_b));
            } else if ("R".equals(s)) {
                openState.updateValue(mContext.getResources().getString(R.string.account_xpad_openstates_r));
            } else if ("N".equals(s)) {
                openState.updateValue(mContext.getResources().getString(R.string.account_xpad_openstates_n));
            }
            tradeAccount.updateValue(NumberUtils.formatCardNumberStrong(item.getAccountNumber()));
            accountType.updateValue(PublicCodeUtils.getAccountType(mContext, item.getAccountType()));
            String isE;
            if (StringUtils.isEmpty(item.getAccountId())) {
                isE = getResources().getString(R.string.account_xpad_mark_no);
            } else {
                isE = getResources().getString(R.string.account_xpad_mark_yes);
            }
            isEbankAccount.updateValue(isE);

            //查询当前账户余额
            String accountId = item.getAccountId();
            if (!StringUtils.isEmpty(accountId)) {
                showLoadingDialog(true);
                getPresenter().psnAccountQueryAccountDetail(accountId);
            }
        } else {
            //非专属理财
            xpadNomalAccount.setVisibility(View.VISIBLE);
            tradeAccount.updateValue(NumberUtils.formatCardNumberStrong(item.getAccountNumber()));
            accountType.updateValue(PublicCodeUtils.getAccountType(mContext, item.getAccountType()));
            String isE;
            if (StringUtils.isEmpty(item.getAccountId())) {
                isE = getResources().getString(R.string.account_xpad_mark_no);
            } else {
                isE = getResources().getString(R.string.account_xpad_mark_yes);
                tvUnRegist.setVisibility(View.VISIBLE);//取消登记
            }
            isEbankAccount.updateValue(isE);

            //查询当前账户余额
            String accountId = item.getAccountId();
            if (!StringUtils.isEmpty(accountId)) {
                showLoadingDialog(true);
                getPresenter().psnAccountQueryAccountDetail(accountId);
            }
        }
    }

    @Override
    public AccountModel getModel() {
        return model;
    }

    /**
     * 余额查询结果回调，如果查询成功则更新账户余额组件
     */
    @Override
    public void psnAccountQueryAccountDetailSuccess() {
        closeProgressDialog();
        updateAccountHeadinfo();
    }

    @Override
    public void psnAccountQueryAccountDetailFailed() {
        closeProgressDialog();
    }

    /**
     * 该方法为更新详情页，账户余额组件，获取余额查询结果，以查询结果判断是否显示该组件，显示内容
     */
    private void updateAccountHeadinfo() {
        if (!StringUtils.isEmpty(item.getAccountId())) {
            accountMsg.setVisibility(View.VISIBLE);
            detaiList = model.getAccountDetaiList();
            int size = detaiList.size();
            if (size > 1) {
                //多种余额显示下拉按钮
                accountMsg.imgShow(true);
                List<AccountListItemViewModel.CardAmountViewModel> list = new ArrayList<>();
                for (int i = 0; i < detaiList.size(); i++) {
                    AccountListItemViewModel.CardAmountViewModel detail = new AccountListItemViewModel.CardAmountViewModel();
                    detail.setCurrencyCode(detaiList.get(i).getCurrencyCode());
                    detail.setAccountType(detaiList.get(i).getType());
                    detail.setAmount(detaiList.get(i).getAvailableBalance() + "");
                    detail.setBookBalance(detaiList.get(i).getAvailableBalance() + "");
                    detail.setCashRemit(detaiList.get(i).getCashRemit());
                    list.add(detail);
                }
                accountMsg.setDetails(list, false);
            } else if (size == 1) {
                //单种余额显示下拉按钮
                accountMsg.imgShow(false);
                List<AccountListItemViewModel.CardAmountViewModel> list = new ArrayList<>();
                AccountListItemViewModel.CardAmountViewModel detail = new AccountListItemViewModel.CardAmountViewModel();
                detail.setCurrencyCode(detaiList.get(0).getCurrencyCode());
                detail.setCashRemit(detaiList.get(0).getCashRemit());
                detail.setAmount(detaiList.get(0).getAvailableBalance() + "");
                detail.setBookBalance(detaiList.get(0).getAvailableBalance() + "");
                detail.setAccountType(detaiList.get(0).getType());
                list.add(detail);
                accountMsg.setDetails(list, false);
            } else {
                accountMsg.setVisibility(View.GONE);
                ivZone.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 取消登记普通账户成功回调
     */
    @Override
    public void psnXpadAccountCancelSuccess() {
        closeProgressDialog();
        showToast(mContext.getResources().getString(R.string.account_xpad_cancle));
        popToAndReInit(AccountMainFragment.class);
    }

    /**
     * 取消登记普通账户失败回调
     */
    @Override
    public void psnXpadAccountCancelFailed() {
        closeProgressDialog();
    }

    /**
     * 解绑专属理财账户成功回调
     */
    @Override
    public void psnOFADisengageBindSuccess() {
        closeProgressDialog();
        showToast(mContext.getResources().getString(R.string.account_xpad_jiebang));
        popToAndReInit(AccountMainFragment.class);
    }

    /**
     * 解绑专属理财账户失败回调
     */
    @Override
    public void psnOFADisengageBindFailed() {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(AccountContract.Presenter presenter) {
    }

    @Override
    protected String getTitleValue() {
        return mContext.getResources().getString(R.string.account_msg_detail);
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
    protected AccountMainPersenter initPresenter() {
        return new AccountMainPersenter(this);
    }
}
