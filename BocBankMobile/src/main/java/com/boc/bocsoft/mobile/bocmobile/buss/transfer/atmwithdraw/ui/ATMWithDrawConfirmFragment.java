package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.presenter.ATMWithDrawPresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ATM无卡取款确认信息页面
 * Created by liuweidong on 2016/7/19.
 */
public class ATMWithDrawConfirmFragment extends MvpBussFragment<ATMWithDrawPresenter> implements SecurityVerity.VerifyCodeResultListener,
        ATMWithDrawContract.ResultView {
    private ConfirmInfoView confirmInfoView;
    private ATMWithDrawViewModel mViewModel;
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
        mViewModel = (ATMWithDrawViewModel) bundle.getSerializable("ATMWithDrawViewModel");
        return confirmInfoView;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        defaultFactorList = copyToFactorListBean();
        String money = MoneyUtils.transMoneyFormat(mViewModel.getMoney(), mViewModel.getCurrencyCode());
        confirmInfoView.setHeadValue(getResources().getString(R.string.boc_transfer_atm_draw_query_details_amount), money, false);
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
//                    if (SecurityVerity.getInstance().confirmFactor(defaultFactorList)) {// 显示安全认证对话框
//                        SecurityVerity.getInstance().setConversationId(ATMWithDrawPresenter.conversationID);
//                        SecurityVerity.getInstance().showSecurityDialog(ATMWithDrawPresenter.randomID);
//                    }
//                } else {// ATM无卡取款预交易
                    showLoadingDialog(false);
                    getPresenter().atmWithDrawConfirm(curAccountBean.getAccountId(),
                            SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId(), mViewModel.getMoney().toString());
//                }
            }

            @Override
            public void onClickChange() {//显示安全认证选择对话框
                SecurityVerity.getInstance().selectSecurityType();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(ATMWithDrawConfirmFragment.this);
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
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected ATMWithDrawPresenter initPresenter() {
        return new ATMWithDrawPresenter(this, true);
    }

    /**
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[3];
        name[0] = getResources().getString(R.string.boc_transfer_atm_draw_phone);
        name[1] = getResources().getString(R.string.boc_transfer_comment);
        name[2] = getResources().getString(R.string.boc_transfer_atm_draw_account);
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String[] value = new String[3];
        value[0] = NumberUtils.formatMobileNumber(ApplicationContext.getInstance().getUser().getMobile());
        value[1] = mViewModel.getFurInf();
        value[2] = NumberUtils.formatCardNumber(curAccountBean.getAccountNumber());
        return value;
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
    public void onSecurityTypeSelected(CombinListBean bean) {
        // 设置更改的安全因子名称
        confirmInfoView.updateSecurity(bean.getName());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        // ATM无卡取款提交交易
        showLoadingDialog(false);
        getPresenter().atmWithDrawResult(curAccountBean.getAccountId(), randomNums,
                encryptPasswords, Integer.valueOf(factorId), mContext, DeviceInfoUtils.getDevicePrint(getActivity()));
    }

    @Override
    public void onSignedReturn(String signRetData) {
        String[] randomNums = {signRetData};
        String[] encryptPasswords = {};
        // ATM无卡取款提交交易`
        showLoadingDialog(false);
        getPresenter().atmWithDrawResult(curAccountBean.getAccountId(), randomNums,
                encryptPasswords, SecurityVerity.SECURITY_VERIFY_E_TOKEN, mContext, DeviceInfoUtils.getDevicePrint(getActivity()));
    }

    @Override
    public void queryAccountDetailsSuccess(ATMWithDrawViewModel viewModel) {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountBean", curAccountBean);
        bundle.putBoolean(ATMWithDrawResultFragment.isSuccess, true);
        ATMWithDrawResultFragment fragment = new ATMWithDrawResultFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void queryAccountDetailsFail() {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountBean", curAccountBean);
        bundle.putBoolean(ATMWithDrawResultFragment.isSuccess, false);
        ATMWithDrawResultFragment fragment = new ATMWithDrawResultFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * ATM无卡取款预交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void atmWithDrawConfirmFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * ATM无卡取款预交易成功
     */
    @Override
    public void atmWithDrawConfirmSuccess() {
        closeProgressDialog();
        EShieldVerify.getInstance(getActivity()).setmPlainData(mViewModel.get_plainData());
        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(copyToFactorListBean())) {
            SecurityVerity.getInstance().setConversationId(ATMWithDrawPresenter.conversationID);
            SecurityVerity.getInstance().showSecurityDialog(ATMWithDrawPresenter.randomID);
        }
    }

    /**
     * ATM无卡取款提交失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void atmWithDrawResultFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * ATM无卡取款提交成功
     */
    @Override
    public void atmWithDrawResultSuccess() {
        BocCloudCenter.getInstance().updateLastAccountId(AccountType.ACC_TYPE_NOCARD_DRAW, curAccountBean.getAccountId());
        getPresenter().queryAccountDetails(curAccountBean.getAccountId());// 查询账户详情
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
