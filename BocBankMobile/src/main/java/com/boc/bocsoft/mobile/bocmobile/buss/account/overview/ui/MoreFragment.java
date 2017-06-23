package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.more.BaseMoreFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui.ServiceBureauFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui.FinanceAccountViewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui.LossFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui.AccSmsNotifyHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview.VirtualCardListFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;

import java.util.List;

/**
 * 首页更多
 * Created by niuguobin on 2016/6/13.
 */
@SuppressLint("ValidFragment")
public class MoreFragment extends BaseMoreFragment {
    /**
     * 医保账户
     */
    private AccountBean medicalInsurance;

    public MoreFragment(AccountBean medicalInsurance) {
        super();
        this.medicalInsurance = medicalInsurance;
    }

    @Override
    protected List<Item> getItems() {
        return ModelUtil.generateOverviewMoreItems();
    }

    @Override
    public void onClick(String id) {
        switch (id) {
            //申请定期活期
            case ModuleCode.MODULE_ACCOUNT_0300:
                if (ApplicationContext.getInstance().getChinaBankAccountList(null).size() < AccountUtils.ACCOUNT_BALANCE_SUM)
                    start(new ServiceBureauFragment(false, ApplyAccountModel.APPLY_TYPE_REGULAR));
                else
                    showErrorDialog(getString(R.string.boc_apply_quota));
                break;
            //交易明细
            case ModuleCode.MODULE_ACCOUNT_0200:
                goTransDetailFragment(null, TransDetailFragment.DETAIL_ACCOUNT_TYPE_ALL);
                break;
            //冻结挂失
            case ModuleCode.MODULE_ACCOUNT_0500:
                goLossFragment(null, false);
                break;
            case ModuleCode.MODULE_ACCOUNT_0400:
                goAccountNotifyFragment(null);
                break;
            //我的电子现金账户
            case ModuleCode.MODULE_ACCOUNT_0700:
                //我的电子现金账户
                start(new FinanceAccountViewFragment());
                break;
            //虚拟银行卡
            case ModuleCode.MODULE_ACCOUNT_0900:
                start(new VirtualCardListFragment());
                break;
            //医保账户
            case ModuleCode.MODULE_ACCOUNT_1000:
                if (medicalInsurance == null) {
                    showErrorDialog(getString(R.string.boc_overview_more_medical_no_data_hint));
                    break;
                }
                start(new MedicalInsuranceFragment(medicalInsurance));
                break;
        }
    }

    /**
     * 跳转交易明细界面
     *
     * @param accountBean
     * @param detailType
     */
    private void goTransDetailFragment(AccountBean accountBean, int detailType) {
        Bundle bundle = new Bundle();
        TransDetailFragment transDetailFragment = new TransDetailFragment();
        if (accountBean != null)
            bundle.putParcelable(TransDetailFragment.DETAIL_ACCOUNT_BEAN, accountBean);
        bundle.putInt(TransDetailFragment.DETAIL_TYPE, detailType);
        transDetailFragment.setArguments(bundle);
        start(transDetailFragment);
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
        startForResult(notifyFragment,BaseOverviewFragment.REQUEST_CODE_REFRESH_FRAGMENT);
    }

    @Override
    public boolean onBack() {
        setFramgentResult(RESULT_OK, null);
        return super.onBack();
    }

    @Override
    public boolean onBackPress() {
        setFramgentResult(RESULT_OK, null);
        return super.onBackPress();
    }
}
