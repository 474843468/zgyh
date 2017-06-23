package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model.MobileRemitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.presenter.MobileRemitPresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 汇往取款人确认页面
 * Created by liuweidong on 2016/7/26.
 */
public class MobileRemitConfirmFragment extends MvpBussFragment<MobileRemitContract.Presenter> implements SecurityVerity.VerifyCodeResultListener,
        MobileRemitContract.ResultView {
    private ConfirmInfoView confirmInfoView;
    private MobileRemitViewModel mViewModel;
    private AccountBean curAccountBean;// 当前账户
    private String defaultCombinName;// 默认安全因子
    private String defaultCombinID;
    private List<FactorListBean> defaultFactorList;
    private LinkedHashMap<String, String> datas = new LinkedHashMap<>();

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        Bundle bundle = getArguments();
        defaultCombinID = bundle.getString("DefaultCombinID");
        defaultCombinName = bundle.getString("DefaultCombinName");
        curAccountBean = bundle.getParcelable("AccountBean");
        mViewModel = (MobileRemitViewModel) bundle.getSerializable("MobileRemitViewModel");
        return confirmInfoView;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        defaultFactorList = copyToFactorListBean();
        String money = MoneyUtils.transMoneyFormat(mViewModel.getMoney(), mViewModel.getCurrencyCode());
        confirmInfoView.setHeadValue("汇款金额（人民币元）", money, false);
        for (int i = 0; i < collectionName().length; i++) {
            if (StringUtils.isEmptyOrNull(collectionValue()[i])) {
                continue;
            } else {
                datas.put(collectionName()[i], collectionValue()[i]);
            }
        }
        confirmInfoView.addData(datas);
        confirmInfoView.updateSecurity(defaultCombinName);// 设置默认的安全因子名称
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
//                if (defaultCombinID.equals(SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId())) {
//                    // 显示安全认证对话框
//                    if (SecurityVerity.getInstance().confirmFactor(defaultFactorList)) {
//                        SecurityVerity.getInstance().setConversationId(MobileRemitPresenter.conversationID);
//                        SecurityVerity.getInstance().showSecurityDialog(MobileRemitPresenter.randomID);
//                    }
//                } else {
                    // 汇往取款人预交易
                    showLoadingDialog(false);
                    getPresenter().mobileRemitConfirm(curAccountBean.getAccountId(),
                            SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
//                }
            }

            @Override
            public void onClickChange() {
                //显示安全认证选择对话框
                SecurityVerity.getInstance().selectSecurityType();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(MobileRemitConfirmFragment.this);
    }

    private List<FactorListBean> copyToFactorListBean() {
        List<FactorListBean> factorList = new ArrayList<FactorListBean>();
        if (mViewModel.getFactorList() != null) {
            for (int i = 0; i < mViewModel.getFactorList().size(); i++) {
                FactorListBean factorListBean = new FactorListBean();
                FactorListBean.FieldBean fieldBean = new FactorListBean.FieldBean();
                fieldBean.setName(mViewModel.getFactorList().get(i).getField().getName());
                fieldBean.setType(mViewModel.getFactorList().get(i).getField().getType());
                factorListBean.setField(fieldBean);
                factorList.add(factorListBean);
            }
        }
        return factorList;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_mobile_remit_confirm_title);
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
    protected MobileRemitContract.Presenter initPresenter() {
        return new MobileRemitPresenter(this, true);
    }

    /**
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[4];
        name[0] = getResources().getString(R.string.boc_transfer_mobile_withdraw_name);
        name[1] = getResources().getString(R.string.boc_transfer_mobile_withdraw_phone);
        name[2] = getResources().getString(R.string.boc_transfer_comment);
        name[3] = getResources().getString(R.string.boc_transfer_mobile_remit_account);
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String[] value = new String[4];
        value[0] = mViewModel.getPayeeName();
        value[1] = NumberUtils.formatMobileNumber(mViewModel.getPayeeMobile());
        value[2] = mViewModel.getRemark();
        value[3] = NumberUtils.formatCardNumber(curAccountBean.getAccountNumber());
        return value;
    }

    @Override
    public void setPresenter(MobileRemitContract.Presenter presenter) {

    }

    /**
     * 汇往取款人预交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void mobileRemitConfirmFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /**
     * 汇往取款人预交易成功
     */
    @Override
    public void mobileRemitConfirmSuccess() {
        closeProgressDialog();
        EShieldVerify.getInstance(getActivity()).setmPlainData(mViewModel.get_plainData());
        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(copyToFactorListBean())) {
            SecurityVerity.getInstance().setConversationId(MobileRemitPresenter.conversationID);
            SecurityVerity.getInstance().showSecurityDialog(MobileRemitPresenter.randomID);
        }
    }

    /**
     * 汇往取款人提交交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void mobileRemitResultFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /**
     * 汇往取款人提交交易成功
     */
    @Override
    public void mobileRemitResultSuccess() {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountBean", curAccountBean);
        BocCloudCenter.getInstance().updateLastAccountId(AccountType.ACC_TYPE_PHONE_DRAW, curAccountBean.getAccountId());

        MobileRemitResultFragment mobileRemitResultFragment = new MobileRemitResultFragment();
        mobileRemitResultFragment.setArguments(bundle);
        start(mobileRemitResultFragment);
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        // 设置更改的安全因子名称
        confirmInfoView.updateSecurity(bean.getName());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        // 汇往取款人提交交易
        showLoadingDialog(false);
        getPresenter().mobileRemitResult(curAccountBean.getAccountId(), randomNums,
                encryptPasswords, Integer.valueOf(factorId), mContext);
    }

    @Override
    public void onSignedReturn(String signRetData) {
        String[] randomNums = {signRetData};
        String[] encryptPasswords = {};
        // 汇往取款人提交交易
        showLoadingDialog(false);
        getPresenter().mobileRemitResult(curAccountBean.getAccountId(), randomNums,
                encryptPasswords, SecurityVerity.SECURITY_VERIFY_E_TOKEN, mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
