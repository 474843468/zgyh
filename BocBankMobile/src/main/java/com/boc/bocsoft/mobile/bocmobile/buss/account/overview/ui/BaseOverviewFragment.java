package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui.LossFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui.AccSmsNotifyHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * @author wangyang
 *         16/8/14 00:32
 *         账户概览界面基类
 */
public class BaseOverviewFragment<T extends BasePresenter> extends BaseAccountFragment<T> {

    public final static int REQUEST_CODE_REFRESH_FRAGMENT = 9527;

    /**
     * 跳转交易明细界面
     *
     * @param accountBean
     * @param detailType
     */
    protected void goTransDetailFragment(AccountBean accountBean, int detailType) {
        Bundle bundle = new Bundle();
        TransDetailFragment transDetailFragment = new TransDetailFragment();
        if (accountBean != null)
            bundle.putParcelable(TransDetailFragment.DETAIL_ACCOUNT_BEAN, accountBean);
        bundle.putInt(TransDetailFragment.DETAIL_TYPE, detailType);
        transDetailFragment.setArguments(bundle);
        start(transDetailFragment);
    }

    /**
     * 跳转交易详情界面
     *
     * @param listBean
     * @param detailType
     */
    protected void goTransDetailInfoFragment(TransDetailViewModel.ListBean listBean, int detailType) {
        TransDetailInfoFragment fragment = new TransDetailInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TransDetailFragment.DETAIL_INFO, listBean);
        bundle.putInt(TransDetailFragment.DETAIL_TYPE, detailType);
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 跳转冻结/挂失界面
     *
     * @param accountBean
     */
    protected void goLossFragment(AccountBean accountBean, boolean isSingleAccount) {
        LossFragment lossFragment = new LossFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSingleAccount", isSingleAccount);
        if (accountBean != null)
            bundle.putParcelable("AccountBean", accountBean);
        lossFragment.setArguments(bundle);
        start(lossFragment);
    }

    /**
     * 跳转账户动户通知
     *
     * @param accountBean
     */
    protected void goAccountNotifyFragment(AccountBean accountBean) {
        AccSmsNotifyHomeFragment notifyFragment = new AccSmsNotifyHomeFragment();
        if (accountBean != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AccSmsNotifyHomeFragment.NOTIFY_ACCOUNT, accountBean);
            notifyFragment.setArguments(bundle);
        }
        startForResult(notifyFragment,REQUEST_CODE_REFRESH_FRAGMENT);
    }

    /**
     * 获取医保账户
     *
     * @return
     */
    protected AccountBean getMedicalAccount() {
        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(AccountTypeUtil.getCurrentType());
        AccountBean medicalInsurance = null;
        for (AccountBean accountBean : accountBeans) {
            if (AccountTypeUtil.RESULT_TRUE_FINANCE_MEDICAL.equals(accountBean.getIsMedicalAccount())) {
                medicalInsurance = accountBean;
                break;
            }
        }
        return medicalInsurance;
    }

    /**
     * 获取别的界面跳转来的AccountBean
     * @return
     */
    protected AccountBean getAccountBean() {
        String accountId = getArguments().getString("accountId");

        for(AccountBean accountBean: ApplicationContext.getInstance().getChinaBankAccountList(null)){
            if(accountBean.getAccountId().equals(accountId))
                return accountBean;
        }
        return null;
    }

    protected void setFragmentResult(AccountBean accountBean){
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BEAN,accountBean);
        setFramgentResult(RESULT_OK,bundle);
    }

}
