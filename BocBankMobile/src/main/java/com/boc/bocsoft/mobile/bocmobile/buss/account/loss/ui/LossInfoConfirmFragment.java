package com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.model.LossViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.presenter.LossPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 临时挂失确认信息页面
 * Created by liuweidong on 2016/6/13.
 */
public class LossInfoConfirmFragment extends MvpBussFragment<LossContract.Presenter> implements LossContract.LossView,
        SecurityVerity.VerifyCodeResultListener {
    private ConfirmInfoView confirmInfoView;
    private String defaultCombinName;// 默认安全因子名称
    private AccountBean curAccountBean;// 当前账户信息
    private LossViewModel mLossViewModel;// 挂失信息页面数据
    private String[] key = {};
    private String[] value = {};
    private LinkedHashMap<String, String> datas = new LinkedHashMap<>();

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        Bundle bundle = getArguments();
        curAccountBean = bundle.getParcelable("AccountBean");// 当前账户信息
        mLossViewModel = (LossViewModel) bundle.getSerializable("LossViewModel");// 挂失信息
        defaultCombinName = bundle.getString("DefaultCombinName");
        return confirmInfoView;
    }

    @Override
    public void initView() {
//        confirmInfoView.isShowHeadView(false);
    }

    @Override
    public void initData() {
        changeConfirmData();
        for (int i = 0; i < key.length; i++) {
            datas.put(key[i], value[i]);
        }
        confirmInfoView.addData(datas);
        // 设置默认的安全因子名称
        confirmInfoView.updateSecurity(defaultCombinName);
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                showLoadingDialog(false);
                switch (curAccountBean.getAccountType()) {
                    case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                        getPresenter().debitCardLossConfirm(curAccountBean.getAccountNumber(),
                                mLossViewModel, SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
                        break;
//                case ApplicationConst.ACC_TYPE_ZHONGYIN:// 信用卡
//                case ApplicationConst.ACC_TYPE_GRE:
//                case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
//                    mLossPresenter.creditCardLossConfirm(Integer.valueOf(curAccountBean.getAccountId()), curSelectedCombin.getId());
//                    break;
                    default:// 活期一本通（其他）
                        getPresenter().accountLossConfirm(curAccountBean.getAccountNumber(),
                                SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
                        break;
                }
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
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(LossInfoConfirmFragment.this);
    }

    @Override
    protected LossContract.Presenter initPresenter() {
        return new LossPresenter(this);
    }

    /**
     * 依据卡类型修改确认列表信息
     */
    private void changeConfirmData() {
        if (curAccountBean != null) {
            switch (curAccountBean.getAccountType()) {
                case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                    if (mLossViewModel.getAccFlozenFlag().equals(LossFragment.LOSS_TYPE_YES)) {
                        key = new String[]{"账户", "挂失有效期", "冻结主账户", "冻结有效期"};
                        value = new String[]{NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()),
                                setLossDays(mLossViewModel.getLossDays()), setFreeze(mLossViewModel.getAccFlozenFlag()),
                                setLossDays(mLossViewModel.getLossDays())};
                    } else {
                        key = new String[]{"账户", "挂失有效期", "冻结主账户"};
                        value = new String[]{NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()),
                                setLossDays(mLossViewModel.getLossDays()), setFreeze(mLossViewModel.getAccFlozenFlag())};
                    }
                    break;
//                case ApplicationConst.ACC_TYPE_ZHONGYIN:// 信用卡
//                case ApplicationConst.ACC_TYPE_GRE:
//                case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
//                    if (mLossViewModel.getSelectLossType().equals(LossFragment.LOSS_TYPE_NORMAL)) {
//                        key = new String[]{"账户", "挂失类型", "挂失费用"};
//                        value = new String[]{NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()),
//                                setLossType(mLossViewModel.getSelectLossType()), "人民币 "
//                                + MoneyUtils.transMoneyFormat(mLossViewModel.getLossFee(), mLossViewModel.getLossFeeCurrency())};
//                    } else {
//                        key = new String[]{"账户", "挂失类型", "邮寄方式", "邮寄地址", "挂失费用", "补卡费用"};
//                        value = new String[]{NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()),
//                                setLossType(mLossViewModel.getSelectLossType()),
//                                mLossViewModel.getMailAddressType(), mLossViewModel.getMailAddress(), "人民币 "
//                                + MoneyUtils.transMoneyFormat(mLossViewModel.getLossFee(), mLossViewModel.getLossFeeCurrency()), "人民币 "
//                                + MoneyUtils.transMoneyFormat(mLossViewModel.getReportFee(), mLossViewModel.getReportFeeCurrency())};
//                    }
//                    break;
                default:// 活期一本通（其他）
                    key = new String[]{"账户", "冻结有效期"};
                    value = new String[]{NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()),
                            setLossDays(mLossViewModel.getLossDays())};
                    break;
            }
        }
    }

    /**
     * 挂失或冻结有效期
     *
     * @param id
     * @return
     */
    private String setLossDays(String id) {
        String temp = "";
        switch (id) {
            case LossFragment.LOSS_DAY_5:
                temp = "5日";
                break;
            case LossFragment.LOSS_DAY_FOREVER:
                temp = "长期";
                break;
        }
        return temp;
    }

    /**
     * 是否冻结
     *
     * @param id
     * @return
     */
    private String setFreeze(String id) {
        String temp = "";
        switch (id) {
            case LossFragment.LOSS_TYPE_YES:
                temp = "是";
                break;
            case LossFragment.LOSS_TYPE_NO:
                temp = "否";
                break;
        }
        return temp;
    }

    /**
     * 信用卡挂失类型
     *
     * @param id
     * @return
     */
    private String setLossType(String id) {
        String temp = "";
        switch (id) {
            case LossFragment.LOSS_TYPE_NORMAL:
                temp = "挂失";
                break;
            case LossFragment.LOSS_TYPE_REAPPLY_CARD:
                temp = "挂失及补卡";
                break;
        }
        return temp;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_fragment_confirm_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    private List<FactorListBean> copyToFactorListBean() {
        List<FactorListBean> factorList = new ArrayList<FactorListBean>();
        if (mLossViewModel.getFactorList() != null) {
            for (int i = 0; i < mLossViewModel.getFactorList().size(); i++) {
                FactorListBean factorListBean = new FactorListBean();
                FactorListBean.FieldBean fieldBean = new FactorListBean.FieldBean();
                fieldBean.setName(mLossViewModel.getFactorList().get(i).getField().getName());
                fieldBean.setType(mLossViewModel.getFactorList().get(i).getField().getType());
                factorListBean.setField(fieldBean);
                factorList.add(factorListBean);
            }
        }
        return factorList;
    }

    /**
     * 借记卡预交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void debitCardLossConfirmFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 借记卡预交易成功
     */
    @Override
    public void debitCardLossConfirmSuccess() {
        closeProgressDialog();
        EShieldVerify.getInstance(getActivity()).setmPlainData(mLossViewModel.get_plainData());
        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(copyToFactorListBean())) {
            SecurityVerity.getInstance().setConversationId(LossPresenter.conversationID);
            SecurityVerity.getInstance().showSecurityDialog(LossPresenter.randomID);
        }
    }

    /**
     * 借记卡挂失失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void debitCardLossResultFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 借记卡挂失成功
     */
    @Override
    public void debitCardLossResultSuccess(LossViewModel lossViewModel) {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountBean", curAccountBean);// 账户信息
        bundle.putSerializable("LossViewModel", mLossViewModel);
        LossResultFragment lossResultFragment = new LossResultFragment();
        lossResultFragment.setArguments(bundle);
        start(lossResultFragment);
    }

    /**
     * 活期一本通预交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void accountLossConfirmFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 活期一本通预交易成功
     */
    @Override
    public void accountLossConfirmSuccess() {
        closeProgressDialog();
        EShieldVerify.getInstance(getActivity()).setmPlainData(mLossViewModel.get_plainData());
        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(copyToFactorListBean())) {
            SecurityVerity.getInstance().setConversationId(LossPresenter.conversationID);
            SecurityVerity.getInstance().showSecurityDialog(LossPresenter.randomID);
        }
    }

    /**
     * 活期一本通提交失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void accountLossResultFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 活期一本通提交成功
     */
    @Override
    public void accountLossResultSuccess() {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountBean", curAccountBean);// 账户信息
        bundle.putSerializable("LossViewModel", mLossViewModel);
        LossResultFragment lossResultFragment = new LossResultFragment();
        lossResultFragment.setArguments(bundle);
        start(lossResultFragment);
    }

    /**
     * 信用卡预交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void creditCardLossConfirmFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 信用卡预交易成功
     */
    @Override
    public void creditCardLossConfirmSuccess() {
        closeProgressDialog();
        EShieldVerify.getInstance(getActivity()).setmPlainData(mLossViewModel.get_plainData());
        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(copyToFactorListBean())) {
            SecurityVerity.getInstance().setConversationId(LossPresenter.conversationID);
            SecurityVerity.getInstance().showSecurityDialog(LossPresenter.randomID);
        }
    }

    /**
     * 信用卡提交失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void creditCardLossResultFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        LossResultFragment lossResultFragment = new LossResultFragment();
        lossResultFragment.setArguments(bundle);
        start(lossResultFragment);
    }

    /**
     * 信用卡提交成功
     */
    @Override
    public void creditCardLossResultSuccess() {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        LossResultFragment lossResultFragment = new LossResultFragment();
        lossResultFragment.setArguments(bundle);
        start(lossResultFragment);
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        confirmInfoView.updateSecurity(bean.getName());// 设置更改的安全因子名称
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        if (curAccountBean != null) {
            showLoadingDialog(false);
            switch (curAccountBean.getAccountType()) {
                case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                    getPresenter().debitCardLossResult(curAccountBean, randomNums, encryptPasswords,
                            Integer.valueOf(factorId), mContext);
                    break;
//                case ApplicationConst.ACC_TYPE_ZHONGYIN:// 信用卡
//                case ApplicationConst.ACC_TYPE_GRE:
//                case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
//                    // 信用卡提交网络请求
//                    mLossPresenter.creditCardLossResult(curAccountBean.getAccountId(), randomNums, encryptPasswords,
//                            Integer.valueOf(factorID), mContext);
//                    break;
                default:// 活期一本通（其他）
                    getPresenter().accountLossResult(curAccountBean, randomNums, encryptPasswords,
                            Integer.valueOf(factorId), mContext);
                    break;
            }
        }
    }

    @Override
    public void onSignedReturn(String signRetData) {
        String[] randomNums = {signRetData};
        String[] encryptPasswords = {};
        if (curAccountBean != null) {
            showLoadingDialog(false);
            switch (curAccountBean.getAccountType()) {
                case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                    getPresenter().debitCardLossResult(curAccountBean, randomNums, encryptPasswords,
                            SecurityVerity.SECURITY_VERIFY_E_TOKEN, mContext);
                    break;
                default:// 活期一本通（其他）
                    getPresenter().accountLossResult(curAccountBean, randomNums, encryptPasswords,
                            SecurityVerity.SECURITY_VERIFY_E_TOKEN, mContext);
                    break;
            }
        }
    }
}
