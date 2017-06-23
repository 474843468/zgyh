package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.SubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.presenter.PayAdvancePresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangle on 2016/11/21.
 * 提前结清入账
 */
public class PayAdvanceFragment extends MvpBussFragment<PayAdvancePresenter> implements PayAdvanceContract.View,DetailTableRowButton.BtnCallback, View.OnClickListener, SecurityVerity.VerifyCodeResultListener {
    public static final String RECORD_MODEL = "record_model";
    public static final String SECURITY_MODEL = "security_model";
    private View mRootView;
    private DetailTableRowButton mBtnSecurity;
    private Button mBtnOk;
    private SecurityVerity mSecurityVerity;
    private boolean mIsSecurityChanged = false;
    private  InstallmentRecordModel mRecordModel;
    private SecurityViewModel mSecurityModel;
    private SubmitModel mSubmitModel;

    public static PayAdvanceFragment newInstance(InstallmentRecordModel recordModel, SecurityViewModel securityModel) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RECORD_MODEL,recordModel);
        arguments.putParcelable(SECURITY_MODEL, securityModel);
        PayAdvanceFragment advanceFragment = new PayAdvanceFragment();
        advanceFragment.setArguments(arguments);
        return advanceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mRecordModel = arguments.getParcelable(RECORD_MODEL);
        mSecurityModel = arguments.getParcelable(SECURITY_MODEL);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_creditcard_payadvance, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        mBtnSecurity = (DetailTableRowButton) mRootView.findViewById(R.id.btn_security);
        mBtnOk = (Button) mRootView.findViewById(R.id.btn_ok);

        mSecurityVerity = SecurityVerity.getInstance(getActivity());
    }

    @Override
    public void initData() {
        super.initData();
        //  获取默认安全因子
        CombinListBean combinListBean = mSecurityVerity.getDefaultSecurityFactorId(new SecurityFactorModel(copyOfSecurityCombin(mSecurityModel)));
        updateSecurityAndSaveId(combinListBean);
        //  初始化提交交易上送相关model
        mSubmitModel = new SubmitModel();
    }

    private void updateSecurityAndSaveId(CombinListBean combinListBean) {
        updateSecurity(combinListBean.getName());
        mRecordModel.set_combinId(combinListBean.getId());
    }

    @Override
    public void setListener() {
        super.setListener();
        mBtnSecurity.setOnclick(this);
        mBtnOk.setOnClickListener(this);
        mSecurityVerity.setSecurityVerifyListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_crcd_myinstallment_pay_advance);
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
    public void onClickListener() {
        // 更改安全认证方式
        mSecurityVerity.selectSecurityType();
    }

    @Override
    public void onClick(View v) {
        // 点击确认时 先请求预交易
        getPresenter().psnCrcdDividedPayAdvanceConfirm(mRecordModel);
    }

    @Override
    public void payAdvanceConfirmSuccess(VerifyBean verifyBean, String random) {
        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyBean.get_plainData());
        // 预交易成功后再次确认安全因子
        if (mSecurityVerity.confirmFactor(copyFactorList(verifyBean.getFactorList()))) {
            // 显示安全工具dialog
            mSecurityVerity.setConversationId(mRecordModel.getConversationId());
            mSecurityVerity.showSecurityDialog(random);
        }

    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        mIsSecurityChanged = !mRecordModel.get_combinId().equals(bean.getId());
        if (mIsSecurityChanged) {
            updateSecurityAndSaveId(bean);
        }
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        // 加密完成后回调
        mSubmitModel.reset();
        mSubmitModel.setState(mSecurityVerity.SECURITY_VERIFY_STATE);
        mSubmitModel.setActiv(mSecurityVerity.getCfcaVersion());
        int type = Integer.parseInt(factorId);
        switch (type) {
            case SecurityVerity.SECURITY_VERIFY_SMS:
                mSubmitModel.setSmc(encryptPasswords[0]);
                mSubmitModel.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_TOKEN:
                mSubmitModel.setOtp(encryptPasswords[0]);
                mSubmitModel.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:
                mSubmitModel.setOtp(encryptPasswords[0]);
                mSubmitModel.setOtp_RC(randomNums[0]);
                mSubmitModel.setSmc(encryptPasswords[1]);
                mSubmitModel.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:
                mSubmitModel.setSmc(encryptPasswords[0]);
                mSubmitModel.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(mContext, mSecurityVerity.getRandomNum());
                mSubmitModel.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                mSubmitModel.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                mSubmitModel.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
                break;
            default:
                break;
        }
        getPresenter().psnCrcdDividedPayAdvanceResult(mRecordModel,mSubmitModel);
    }

    @Override
    public void onSignedReturn(String signRetData) {
        //  CA 认证
        mSubmitModel.set_signedData(signRetData);
        getPresenter().psnCrcdDividedPayAdvanceResult(mRecordModel, mSubmitModel);
    }

    @Override
    public void payAdvanceResultSuccess() {
        // 跳转至结果界面
        start(PayAdvanceResultFragment.newInstance(mRecordModel));
    }

    @Override
    public void showLoading() {
        super.showLoadingDialog();
    }

    @Override
    public void closeLoading() {
        super.closeProgressDialog();
    }

    @Override
    protected PayAdvancePresenter  initPresenter() {
        return new PayAdvancePresenter(this);
    }

    @Override
    public void setPresenter(PayAdvanceContract.Presenter presenter) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSecurityVerity = null;
        mSubmitModel = null;
        mRecordModel = null;
    }

    public void updateSecurity(String name) {
        mBtnSecurity.addTextBtn(getString(R.string.security_title), name, getContext().getString(R.string.security_change_verify), getResources().getColor(R.color.boc_main_button_color));
    }


    public static PsnGetSecurityFactorResult copyOfSecurityCombin(SecurityViewModel securityViewModel) {
        PsnGetSecurityFactorResult result = new PsnGetSecurityFactorResult();
        // 保存默认安全因子
        if (securityViewModel.get_defaultCombin() != null) {
            CombinListBean defaultCombin = new CombinListBean();
            defaultCombin.setId(securityViewModel.get_defaultCombin().getId());
            defaultCombin.setName(securityViewModel.get_defaultCombin().getName());
            defaultCombin.setSafetyFactorList(securityViewModel.get_defaultCombin().getSafetyFactorList());
            result.set_defaultCombin(defaultCombin);
        }
        // 保存安全因子列表
        List<CombinListBean> combinList = new ArrayList<CombinListBean>();
        for (int i = 0; i < securityViewModel.get_combinList().size(); i++) {
            CombinListBean item = new CombinListBean();
            item.setId(securityViewModel.get_combinList().get(i).getId());
            item.setName(securityViewModel.get_combinList().get(i).getName());
            item.setSafetyFactorList(securityViewModel.get_combinList().get(i).getSafetyFactorList());
            combinList.add(item);
        }
        result.set_combinList(combinList);
        return result;
    }

    public static List<FactorListBean> copyFactorList(List<FactorBean> factorList) {
        List<FactorListBean> result = new ArrayList<>();
        for (FactorBean factorBean : factorList) {
            result.add(BeanConvertor.fromBean(factorBean, new FactorListBean()));
        }
        return result;
    }

}
