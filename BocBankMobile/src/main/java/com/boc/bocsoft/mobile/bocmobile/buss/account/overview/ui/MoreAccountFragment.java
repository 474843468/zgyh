package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.ClipBoardWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.more.BaseMoreFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui.ServiceBureauFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.AccountLimitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui.LossFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui.AccSmsNotifyHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.AccountInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.relation.ui.AccountRelationCancelFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * @author wangyang
 *         16/8/13 21:45
 *         账户更多界面
 */
@SuppressLint("ValidFragment")
public class MoreAccountFragment extends BaseMoreFragment<OverviewPresenter> implements OverviewContract.MoreAccountView {

    //账户详情
    public final static String MORE_DETAIL = "more_detail";
    //申请定期账户
    public final static String MORE_APPLY = "more_apply";
    //冻结/挂失
    public final static String MORE_FROZEN = "more_frozen";
    //显示完整账号
    public final static String MORE_FULL = "more_full";
    //账户动户通知
    public final static String MORE_NOTIFY = "more_notify";
    //取消关联
    public final static String MORE_CANCEL = "more_cancel";
    //限额设置
    public final static String MORE_LIMIT = "more_limit";

    /**
     * 账户信息
     */
    private AccountBean accountBean;

    private AccountInfoBean accountInfoBean;

    public MoreAccountFragment(AccountBean accountBean, AccountInfoBean accountInfoBean) {
        this.accountBean = accountBean;
        this.accountInfoBean = accountInfoBean;
    }

    @Override
    protected OverviewPresenter initPresenter() {
        return new OverviewPresenter(this);
    }

    @Override
    protected List<Item> getItems() {
        return ModelUtil.generateAccountMoreItems(getResources(),accountBean);
    }

    @Override
    protected void loadData() {
        if (AccountTypeUtil.getCanNotify().contains(accountBean.getAccountType())) {
            showLoadingDialog();
            getPresenter().querySsm(accountBean.getAccountId());
        }
    }

    @Override
    public void onClick(String id) {
        switch (id){
            case MORE_DETAIL:
                start(new AccountDetailFragment(accountBean, accountInfoBean));
                break;
            case MORE_APPLY:
                start(new ServiceBureauFragment(accountBean, false, ApplyAccountModel.APPLY_TYPE_REGULAR));
                break;
            case MORE_FROZEN:
                goLossFragment(accountBean, true);
                break;
            case MORE_FULL:
                String cardNum = NumberUtils.formatCardNumber2(accountBean.getAccountNumber());
                new ClipBoardWidget(mContext, cardNum).show();
                break;
            case MORE_NOTIFY:
                goAccountNotifyFragment(accountBean);
                break;
            case MORE_CANCEL:
                start(new AccountRelationCancelFragment(accountBean));
                break;
            case MORE_LIMIT:
                start(new AccountLimitFragment(accountBean));
                break;
        }
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
    public void querySsm(boolean isOpen) {
        closeProgressDialog();

        if (isOpen)
            setContentById(MORE_NOTIFY,getString(R.string.boc_overview_account_more_status_service));
        else
            setContentById(MORE_NOTIFY,getString(R.string.boc_overview_account_more_status_not_service));
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if(requestCode != BaseOverviewFragment.REQUEST_CODE_REFRESH_FRAGMENT)
            return;

        if(resultCode == RESULT_OK)
            loadData();
    }
}
