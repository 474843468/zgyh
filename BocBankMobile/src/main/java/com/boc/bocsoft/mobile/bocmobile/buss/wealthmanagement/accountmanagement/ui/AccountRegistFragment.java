package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.adapter.RegistAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.XpadAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.presenter.AccountMainPersenter;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 理财——账户登记界面
 * Created by Wan mengxin on 2016/9/17.
 */
@SuppressLint("ValidFragment")
public class AccountRegistFragment extends MvpBussFragment<AccountMainPersenter> implements AccountContract.RegistView {

    private View registView;
    private LinearLayout canbeReisgtedView;//有可登记帐号
    private LinearLayout notBeRegisted;//无可登记帐号
    private ListView registAccount;//可登记帐号列表
    private AccountModel model;//用户详情数据模型
    private List<XpadAccountModel> mList;//登记页显示条目数据集合
    private RegistAdapter registAdapter;
    private List<AccountModel.padAllAcountDetail> acountList;
    private Class<? extends BussFragment> fromClass;
    private boolean mark = true;

    public AccountRegistFragment(Class<? extends BussFragment> clazz, AccountModel model) {
        this.fromClass = clazz;
        this.model = model;
    }

    public AccountRegistFragment(Class<? extends BussFragment> clazz) {
        this.fromClass = clazz;
    }

    protected View onCreateView(LayoutInflater mInflater) {
        registView = View.inflate(mContext, R.layout.boc_fragment_xpad_account_regist, null);
        return registView;
    }

    @Override
    public void initView() {
        canbeReisgtedView = (LinearLayout) registView.findViewById(R.id.flCanbeRegisted);
        registAccount = (ListView) registView.findViewById(R.id.lvRegistAccount);
        notBeRegisted = (LinearLayout) registView.findViewById(R.id.noResult);
    }

    @Override
    public void initData() {
        if (model == null) {
            mark = false;
            model = new AccountModel();
        }
        registAdapter = new RegistAdapter(mContext);
        registAccount.setAdapter(registAdapter);
        showLoadingDialog(true);
        getPresenter().psnXpadReset();
    }

    @Override
    protected String getTitleValue() {
        return mContext.getResources().getString(R.string.account_regist_detail);
    }


    @Override
    public void setListener() {
        //点击登记操作
        registAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XpadAccountModel item = registAdapter.getItem(position);
                String accountId = item.getAccountId();
                showLoadingDialog(false);
                getPresenter().psnXpadResult(accountId);
            }
        });
    }

    @Override
    public AccountModel getModel() {
        return model;
    }

    /**
     * 判断借口是否返回数据，即是否有未绑定的卡
     */
    @Override
    public void psnXpadResetSuccess(List<PsnXpadAccountQueryResult.XPadAccountEntity> mlist) {
        closeProgressDialog();
        acountList = model.getAllAcountList();
        //有则去筛选，无则显示无结果页面
        if (!PublicUtils.isEmpty(acountList)) {
            initItem(mlist);
        } else {
            notBeRegisted.setVisibility(View.VISIBLE);
            canbeReisgtedView.setVisibility(View.GONE);
        }
    }

    @Override
    public void psnXpadResetFailed() {
        closeProgressDialog();
    }

    /**
     * 如果绑定账户集合为空或不存在 则直接给适配器设置set接口返回的帐号集合信息
     * 如果绑定账户集合不为空 则遍历set接口与accountQuery接口
     * 取其不一样的账户存入新集合 并将其作为显示数据
     **/
    private void initItem(List<PsnXpadAccountQueryResult.XPadAccountEntity> resultlist) {
        notBeRegisted.setVisibility(View.GONE);
        canbeReisgtedView.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();

        //无绑定帐号情况(获取所有理财账户，装入新集合并设置给adapter)
        if (PublicUtils.isEmpty(resultlist)) {
            for (int i = 0; i < acountList.size(); i++) {
                XpadAccountModel item = new XpadAccountModel();
                item.setAccountId(acountList.get(i).getAccountId());
                item.setAccountNumber(acountList.get(i).getAccountNumber());
                item.setAccountIbkNum(acountList.get(i).getAccountIbkNum());
                item.setNickName(acountList.get(i).getNickName());
                item.setAccountType(acountList.get(i).getAccountType());
                mList.add(item);
            }
            registAdapter.setDatas(mList);

            /**
             * 当前账户有绑定账户，需要将已绑定的帐号与查询列表返回数据一一对比，将相同数据剔除，将剩下的作为显示数据
             * （获取已绑定账号id以及所有账号id，如果一直则剔除，将剩余集合数据设置给adapter）
             */
        } else {
            for (AccountModel.padAllAcountDetail detail : acountList) {
                String returnedId = detail.getAccountId();
                boolean mark = true;
                for (PsnXpadAccountQueryResult.XPadAccountEntity entity : resultlist) {
                    String bindedId = entity.getAccountId();
                    if (returnedId.equals(bindedId)) {
                        mark = false;
                        break;
                    }
                }

                if (mark) {
                    XpadAccountModel item = new XpadAccountModel();
                    item.setAccountId(detail.getAccountId());
                    item.setAccountNumber(detail.getAccountNumber());
                    item.setAccountIbkNum(detail.getAccountIbkNum());
                    item.setNickName(detail.getNickName());
                    item.setAccountType(detail.getAccountType());
                    mList.add(item);
                }
            }
            if (!PublicUtils.isEmpty(mList)) {
                registAdapter.setDatas(mList);
            } else {
                notBeRegisted.setVisibility(View.VISIBLE);
                canbeReisgtedView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void psnXpadResultSuccess() {
        closeProgressDialog();
        if (mark) {
            showToast(mContext.getResources().getString(R.string.account_xpad_regist));
            popToAndReInit(fromClass);
        } else {
            popToAndReInit(fromClass);
        }
    }

    @Override
    public void psnXpadResultFailed() {
        closeProgressDialog();
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
    public void setPresenter(AccountContract.Presenter presenter) {

    }

    @Override
    protected AccountMainPersenter initPresenter() {
        return new AccountMainPersenter(this);
    }
}
