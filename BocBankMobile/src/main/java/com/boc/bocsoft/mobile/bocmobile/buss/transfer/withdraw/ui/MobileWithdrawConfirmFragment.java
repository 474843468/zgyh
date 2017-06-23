package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.CFCAEditTextView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.model.MobileWithdrawViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.presenter.MobileWithdrawPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * 代理点取款确认页面
 * Created by liuweidong on 2016/7/14.
 */
public class MobileWithdrawConfirmFragment extends MvpBussFragment<MobileWithdrawContract.Presenter> implements MobileWithdrawContract.ResultView, CFCAEditTextView.SecurityEditCompleteListener {
    private View rootView;
    private CFCAEditTextView cfcaEditTextView;// 密码框
    private ConfirmInfoView confirmInfoView;// 确认信息
    private MobileWithdrawViewModel mViewModel;// 页面数据
    private LinkedHashMap<String, String> datas = new LinkedHashMap<>();

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_transfer_mobile_withdraw_confirm, null);
        mViewModel = (MobileWithdrawViewModel) getArguments().getSerializable("MobileWithdrawViewModel");
        return rootView;
    }

    @Override
    public void initView() {
        confirmInfoView = (ConfirmInfoView) rootView.findViewById(R.id.confirm_view);
        ViewGroup headView = (ViewGroup) View.inflate(mContext, R.layout.boc_fragment_transfer_mobile_withdraw_confirm_head, null);
        cfcaEditTextView = (CFCAEditTextView) headView.findViewById(R.id.cfca_edittext);
        confirmInfoView.addContentView(headView);
        confirmInfoView.isShowSecurity(false);
    }

    @Override
    public void initData() {
        // 取款金额
        String money = MoneyUtils.transMoneyFormat(String.valueOf(mViewModel.getRemitAmount()),
                mViewModel.getCurrencyCode());
        confirmInfoView.setHeadValue(getResources().getString(R.string.boc_transfer_atm_draw_query_details_amount), money, true);
        for (int i = 0; i < collectionName().length; i++) {
            if (StringUtils.isEmptyOrNull(collectionValue()[i])) {
                continue;
            } else {
                datas.put(collectionName()[i], collectionValue()[i]);
            }
        }
        confirmInfoView.addData(datas, false, false);
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                // 取款密码校验
                if (cfcaEditTextView.getCFCA().getText().toString().isEmpty()) {
                    showErrorDialog(getResources().getString(R.string.boc_transfer_message_withdraw_empty));
                    return;
                } else if (cfcaEditTextView.getCFCA().getText().toString().length() != 6) {
                    showErrorDialog(getResources().getString(R.string.boc_transfer_message_password_length));
                    return;
                }

                cfcaEditTextView.setCFCARandom(MobileWithdrawPresenter.randomID);
                // 汇款解付
                showLoadingDialog(false);
                getPresenter().queryMobileWithdraw(cfcaEditTextView.getEncryptPassword(),
                        cfcaEditTextView.getEncryptRandomNum(), SecurityVerity.getInstance(getActivity()).getCfcaVersion());
            }

            @Override
            public void onClickChange() {

            }
        });

        cfcaEditTextView.setSecurityEditCompleListener(this);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_mobile_withdraw_confirm_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected MobileWithdrawContract.Presenter initPresenter() {
        return new MobileWithdrawPresenter(this);
    }

    /**
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[4];
        name[0] = getResources().getString(R.string.boc_transfer_mobile_withdraw_num);
        name[1] = getResources().getString(R.string.boc_transfer_mobile_withdraw_name);
        name[2] = getResources().getString(R.string.boc_transfer_mobile_withdraw_phone);
        name[3] = getResources().getString(R.string.boc_transfer_comment);
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String[] value = new String[4];
        value[0] = mViewModel.getRemitNo();
        value[1] = mViewModel.getPayeeName();
        value[2] = NumberUtils.formatMobileNumber(mViewModel.getPayeeMobile());
        value[3] = mViewModel.getRemark();
        return value;
    }

    @Override
    public void queryMobileWithdrawFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    public void queryMobileWithdrawSuccess() {
        closeProgressDialog();
        MobileWithdrawResultFragment mobileWithdrawResultFragment = new MobileWithdrawResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MobileWithdrawViewModel", mViewModel);
        mobileWithdrawResultFragment.setArguments(bundle);
        start(mobileWithdrawResultFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNumCompleted(String random, String num, String mVersion) {

    }

    @Override
    public void onErrorMessage(boolean isShow) {

    }

    @Override
    public void onCompleteClicked(String inputString) {
        cfcaEditTextView.hiddenCfcaKeybard();
    }
}
