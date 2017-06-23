package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.view.View;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.AddAndChangDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.ChangeState;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailProductQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignPreViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.AddNewBailConfirmPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Fragment：双向宝-账户管理-新增保证金账户确认页面
 * Created by zhx on 2016/12/12
 */
public class AddNewBailConfirmFragment extends BaseConfirmFragment<VFGSignPreViewModel, VFGSignSubmitViewModel> {
    private static VFGBailProductQueryViewModel.VFGBailProduct mVFGBailProduct;
    private static int fromWhere; // 1表示来自账户管理首页，2表示来自变更交易账户
    private AddAndChangDialog addAndChangDialog;
    private boolean isAddSuccess = false; // 添加保证金是否成功

    public static AddNewBailConfirmFragment newInstance(
            VFGSignPreViewModel vfgSignPreViewModel, VerifyBean verifyBean, VFGBailProductQueryViewModel.VFGBailProduct vfgBailProduct, int from) {
        Bundle args = getBundleForNew(vfgSignPreViewModel, verifyBean);
        AddNewBailConfirmFragment addNewBailConfirmFragment =
                new AddNewBailConfirmFragment();
        addNewBailConfirmFragment.setArguments(args);

        mVFGBailProduct = vfgBailProduct;
        fromWhere = from;
        return addNewBailConfirmFragment;
    }

    @Override
    protected void setConfirmViewData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("资金账户", NumberUtils.formatCardNumber(mFillInfoBean.getAccountNumber()));
        map.put("保证金名称", mVFGBailProduct.getBailCName() + "\n" + mVFGBailProduct.getBailEName());
        map.put("结算币种", PublicCodeUtils.getCurrency(mActivity, mFillInfoBean.getSettleCurrency()));

        // 斩仓比例
        BigDecimal liquidationRatio = mVFGBailProduct.getLiquidationRatio();
        int liquidationRatioInt = (int) liquidationRatio.doubleValue();
        map.put("斩仓比例", liquidationRatioInt + "%");

        // 报警比例
        BigDecimal warnRatio = mVFGBailProduct.getWarnRatio();
        int warnRatioInt = (int) warnRatio.doubleValue();
        map.put("报警比例", warnRatioInt + "%");

        // 交易所需保证金比例
        BigDecimal needMarginRatio = mVFGBailProduct.getNeedMarginRatio();
        double needMarginRatioInt = needMarginRatio.doubleValue();
        map.put("交易所需保证金比例", needMarginRatioInt + "%");

        // 开仓充足率
        BigDecimal openRate = mVFGBailProduct.getOpenRate();
        int openRateInt = (int) openRate.doubleValue();
        map.put("开仓充足率", openRateInt + "%");

        confirmInfoView.addData(map, false);
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums,
                                      String[] encryptPasswords) {
        if (fromWhere == 1) {
            showLoadingDialog(false);
        } else {
            addAndChangDialog = new AddAndChangDialog(mActivity);
            addAndChangDialog.setOnCloseListener(new AddAndChangDialog.OnCloseListener() {
                @Override
                public void onClose() {
                    if (isAddSuccess) {
                        popToAndReInit(AccountManagementFragment.class);
                    }
                }
            });
            addAndChangDialog.setCancelable(false);
            addAndChangDialog.show();
        }
        mSubmitBean.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        mSubmitBean.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        int type = Integer.parseInt(factorId);
        switch (type) {
            case SecurityVerity.SECURITY_VERIFY_SMS:
                mSubmitBean.setSmc(encryptPasswords[0]);
                mSubmitBean.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_TOKEN:
                mSubmitBean.setOtp(encryptPasswords[0]);
                mSubmitBean.setOtp_RC(randomNums[0]);

                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:
                mSubmitBean.setOtp(encryptPasswords[0]);
                mSubmitBean.setOtp_RC(randomNums[0]);
                mSubmitBean.setSmc(encryptPasswords[1]);
                mSubmitBean.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:
                mSubmitBean.setSmc(encryptPasswords[0]);
                mSubmitBean.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(mContext,
                        SecurityVerity.getInstance().getRandomNum());
                mSubmitBean.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                mSubmitBean.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                mSubmitBean.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
                break;
            default:
                break;
        }
        getPresenter().submit(mFillInfoBean, mSubmitBean);
    }

    public void onSubmitFail(BiiResultErrorException biiResultErrorException) {
        ToastUtils.show(biiResultErrorException.getErrorMessage());

        if (fromWhere == 1) {
            closeProgressDialog();
        } else {
            if (addAndChangDialog != null) {
                addAndChangDialog.setAddBailSate(ChangeState.FAIL); // 显示添加保证金为失败状态
                addAndChangDialog.setChangeTradeSate(ChangeState.FAIL); // 显示变更交易账户为失败状态
                addAndChangDialog.setTitle("操作失败");
                addAndChangDialog.getOKBtn().setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSubmitSuccess(VFGSignSubmitViewModel vfgSignSubmitViewModel) {
        //<editor-fold desc="如果新增的保证金属于当前交易账户，回到账户管理首页时，提示转入资金">
        AccountManagementFragment fragment = findFragment(AccountManagementFragment.class);
        if (fragment != null) {
            fragment.setNewBailAccount(vfgSignSubmitViewModel);
        }
        //</editor-fold>

        if (fromWhere == 1) {
            closeProgressDialog();
            ToastUtils.show("签约成功");

            popToAndReInit(AccountManagementFragment.class);
        } else {
            isAddSuccess = true;
            addAndChangDialog.setAddBailSate(ChangeState.SUCCESS);
            //<editor-fold desc="开始设置交易账户">
            VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel = new VFGSetTradeAccountViewModel();
            vfgSetTradeAccountViewModel.setAccountId(mFillInfoBean.getAccountId());
            AddNewBailConfirmPresenter presenter = (AddNewBailConfirmPresenter) getPresenter();
            presenter.vFGSetTradeAccount(vfgSetTradeAccountViewModel);
            //</editor-fold>
        }
    }

    @Override
    protected BaseConfirmContract.Presenter<VFGSignPreViewModel> initPresenter() {
        return new AddNewBailConfirmPresenter(this);
    }

    public void vFGSetTradeAccountFail(BiiResultErrorException biiResultErrorException) {
        ToastUtils.show(biiResultErrorException.getErrorMessage());
        addAndChangDialog.setTitle("添加保证金成功");
        addAndChangDialog.setChangeTradeSate(ChangeState.FAIL);
        addAndChangDialog.getOKBtn().setVisibility(View.VISIBLE);
    }

    public void vFGSetTradeAccountSuccess(VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel) {
        addAndChangDialog.setTitle("操作成功");
        addAndChangDialog.setChangeTradeSate(ChangeState.SUCCESS);
        addAndChangDialog.getOKBtn().setVisibility(View.VISIBLE);
    }
}