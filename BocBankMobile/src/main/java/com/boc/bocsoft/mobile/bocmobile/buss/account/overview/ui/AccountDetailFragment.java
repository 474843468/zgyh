package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.GlobalParams;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditDialogWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.AccountInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.List;

/**
 * @author wangyang
 *         16/8/14 00:24
 *         账户详情界面
 */
@SuppressLint("ValidFragment")
public class AccountDetailFragment extends BaseOverviewFragment<OverviewPresenter> implements OverviewContract.AccountDetailView, DetailTableRowButton.BtnCallback {

    /**
     * 详情组件
     */
    private DetailContentView dcvDetail;
    /**
     * 账户信息
     */
    private AccountBean accountBean;

    private AccountInfoBean accountInfoBean;

    public AccountDetailFragment(AccountBean accountBean, AccountInfoBean accountInfoBean) {
        super();
        this.accountBean = accountBean;
        this.accountInfoBean = accountInfoBean;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_detail);
    }

    @Override
    protected OverviewPresenter initPresenter() {
        return new OverviewPresenter(this);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.fragment_overview_account_detail, null);
    }

    @Override
    public void initView() {
        dcvDetail = (DetailContentView) mContentView.findViewById(R.id.dcv_detail);
    }

    @Override
    public void initData() {
        /**
         * 添加详情
         */
        int height = getResources().getDimensionPixelOffset(R.dimen.boc_button_height_96px);
        dcvDetail.addDetailRow(getString(R.string.boc_account_detail_type), PublicCodeUtils.getAccountType(mContext, accountBean.getAccountType()), height);
        dcvDetail.addImgBtnRow(true, getString(R.string.boc_account_detail_name), accountBean.getNickName(), R.drawable.boc_detail_revise, height, this);
        dcvDetail.addDetailRow(getString(R.string.boc_account_detail_number), NumberUtils.formatCardNumber(accountBean.getAccountNumber()), height);

        if (ApplicationConst.ACC_TYPE_ORD.equals(accountBean.getAccountType())) {
            dcvDetail.addDetailRow(getString(R.string.boc_account_detail_currency), PublicCodeUtils.getCurrency(mContext, accountBean.getCurrencyCode()), height);
            if (!ApplicationConst.CURRENCY_CNY.equals(accountBean.getCurrencyCode()) && accountInfoBean != null && !StringUtils.isEmptyOrNull(accountInfoBean.getCashRemit()))
                dcvDetail.addDetailRow(getString(R.string.boc_account_detail_cash_remit), AccountUtils.getCashRemitName(accountInfoBean.getCashRemit()), height);
        }

        if (accountInfoBean != null && !StringUtils.isEmptyOrNull(accountInfoBean.getAccOpenBank()))
            dcvDetail.addDetailRow(getString(R.string.boc_account_detail_open_bank), accountInfoBean.getAccOpenBank(), height);
        if (accountInfoBean != null && !StringUtils.isEmptyOrNull(accountInfoBean.getAccOpenDate()))
            dcvDetail.addDetailRow(getString(R.string.boc_account_detail_open_date), accountInfoBean.getAccOpenDate(), height);

        if (StringUtils.isEmptyOrNull(accountBean.getAccountStatus()))
            return;

        //最后一行不带分割线
        if (ApplicationConst.ACC_TYPE_ECASH.equals(accountBean.getAccountType()))
            dcvDetail.addDetailRowNotLine(getString(R.string.boc_account_detail_status), accountBean.getAccountStatus(), height);
        else
            dcvDetail.addDetailRowNotLine(getString(R.string.boc_account_detail_status), PublicCodeUtils.getFacilityChildStatusCode(mContext, accountBean.getAccountStatus()), height);
    }

    @Override
    public void updateAccountNickName(AccountBean accountBean) {
        closeProgressDialog();
        ToastUtils.show(getString(R.string.boc_account_detail_update_name_success));
        dcvDetail.getDetailTableRowButton().updateData(getString(R.string.boc_account_detail_name), accountBean.getNickName());
        List<AccountBean> list = ApplicationContext.getInstance().getChinaBankAccountList(null);
        list.set(list.indexOf(accountBean), accountBean);

        OverviewFragment fragment = findFragment(OverviewFragment.class);
        if (fragment != null)
            fragment.getAdapter().updateItemNickName(accountBean);
    }

    @Override
    public void onClickListener() {
        //弹出dialog修改别名
        final EditDialogWidget dialog = new EditDialogWidget(mContext, 20, true);
        dialog.setRightStyle(getResources().getColor(R.color.boc_text_color_red), getResources().getColor(R.color.boc_common_cell_color));
        dialog.setClearEditTextContent(accountBean.getNickName().trim());
        dialog.setEditDialogListener(new EditDialogWidget.EditDialogCallBack() {
            @Override
            public void onClick(String nickName) {
                nickName = getNickNameTrim(nickName);

                if (GlobalParams.isValidName(nickName)) {
                    showLoadingDialog(false);
                    getPresenter().updateAccountNickName(accountBean, nickName);
                } else if (StringUtils.isEmptyOrNull(nickName))
                    showErrorDialog(getString(R.string.boc_account_detail_update_name_error1));
                else
                    showErrorDialog(getString(R.string.boc_account_detail_update_name_error));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @NonNull
    private String getNickNameTrim(String nickName) {
        for (; ; ) {
            if (nickName.length() > 0 && nickName.charAt(0) == ' ')
                nickName = nickName.substring(1, nickName.length());
            else
                break;
        }

        for (; ; ) {
            if (nickName.length() > 0 && nickName.charAt(nickName.length() - 1) == ' ')
                nickName = nickName.substring(0, nickName.length() - 1);
            else
                break;
        }
        return nickName;
    }

}

