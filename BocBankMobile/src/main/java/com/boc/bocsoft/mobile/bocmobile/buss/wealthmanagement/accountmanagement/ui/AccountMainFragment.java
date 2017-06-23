package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.adapter.MainAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.XpadAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.presenter.AccountMainPersenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenStatusI;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 理财——账户管理主界面
 * Created by Wan mengxin on 2016/9/17.
 */
@SuppressLint("ValidFragment")
public class AccountMainFragment extends MvpBussFragment<AccountMainPersenter> implements AccountContract.MainView {

    private View mainView;
    private TextView accountLv;//账户风险评级
    private TextView bindNum;//账户已绑定账户数量
    private ListView bindAccount;//已绑定账户列表
    public AccountModel model;//用户详情数据模型
    private MainAdapter mainAdapter;
    private LinearLayout havaBindAccount;
    private LinearLayout noBindAccount;
    private TextView tvRegist;
    private View footView;//脚布局
    private RelativeLayout userLv;
    private List<XpadAccountModel> mList;
    private boolean mark = false;
    private Class<? extends BussFragment> fromClass;

    public AccountMainFragment(Class<? extends BussFragment> clazz) {
        this.fromClass = clazz;
    }

    protected View onCreateView(LayoutInflater mInflater) {
        mainView = View.inflate(mContext, R.layout.boc_fragment_xpad_account_main, null);
        return mainView;
    }

    @Override
    public void reInit() {
        showLoadingDialog(true);
        WealthProductFragment.getInstance().setCall(new OpenStatusI() {
            @Override
            public void openSuccess() {
                getPresenter().psnInvtEvaluationInit();
            }

            @Override
            public void openFail(ErrorDialog dialog) {
                getPresenter().psnInvtEvaluationInit();
            }
        });
        WealthProductFragment.getInstance().requestOpenStatus();

        userLv.setVisibility(View.GONE);
        havaBindAccount.setVisibility(View.GONE);
        noBindAccount.setVisibility(View.GONE);
        initView();
    }

    @Override
    public void initView() {
        userLv = (RelativeLayout) mainView.findViewById(R.id.userLV);
        accountLv = (TextView) mainView.findViewById(R.id.tvAccountLevel);
        bindNum = (TextView) mainView.findViewById(R.id.tvBindNum);
        bindAccount = (ListView) mainView.findViewById(R.id.lvBindAccount);
        tvRegist = (TextView) mainView.findViewById(R.id.tvRegist);
        havaBindAccount = (LinearLayout) mainView.findViewById(R.id.havaBindAccount);
        noBindAccount = (LinearLayout) mainView.findViewById(R.id.noBindAccount);

        footView = View.inflate(mContext, R.layout.boc_fragment_list_account_footview, null);
    }

    @Override
    public void initData() {
        model = new AccountModel();
        mainAdapter = new MainAdapter(mContext);
        bindAccount.addFooterView(footView);
        bindAccount.setAdapter(mainAdapter);
        showLoadingDialog(true);
        getPresenter().psnInvtEvaluationInit();
    }

    @Override
    protected String getTitleValue() {
        return mContext.getResources().getString(R.string.account_main_title);
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
    public void setListener() {
        accountLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RiskAssessFragment riskFragment = new RiskAssessFragment(AccountMainFragment.class, RiskAssessFragment.RISK_ASSESS_ACCOUNT);
                start(riskFragment);
            }
        });

        tvRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountRegistFragment registFragment = new AccountRegistFragment(AccountMainFragment.class, model);
                start(registFragment);
            }
        });

        bindAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XpadAccountModel item = mainAdapter.getItem(position);
                start(new AccountDetailFragment(model, "190".equals(item.getAccountType()), item));
            }
        });

        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountRegistFragment registFragment = new AccountRegistFragment(AccountMainFragment.class,model);
                start(registFragment);
            }
        });
    }

    @Override
    public boolean onBack() {
        popToAndReInit(fromClass);
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(fromClass);
    }

    @Override
    public AccountModel getModel() {
        return model;
    }

    /**
     * 用户名下理财账户查询成功回调
     */
    @Override
    public void psnXpadAccountQuerySuccess(List<PsnXpadAccountQueryResult.XPadAccountEntity> resultList) {
        if (!PublicUtils.isEmpty(resultList)) {
            mList = new ArrayList<>();
            for (PsnXpadAccountQueryResult.XPadAccountEntity item : resultList) {
                mark = "190".equals(item.getAccountType());
                XpadAccountModel detail = new XpadAccountModel();
                detail.setAccountId(item.getAccountId());//账户Id
                detail.setAccountNumber(item.getAccountNo());//资金账号
                detail.setXpadAccount(item.getXpadAccount());//理财账户
                detail.setAccountType(item.getAccountType());//账户类型
                detail.setNickName(PublicCodeUtils.changeaccounttypetoName(item.getAccountType()));//账户名称
                mList.add(detail);
            }

            // 此处为有专属理财，查询专属账户状态
            if (mark) {
                getPresenter().psnOFAAccountStateQuery();

                // 此处为未开通专属理财
            } else {
                closeProgressDialog();
                initLevel();
                showlist(mList);
            }

        } else {
            closeProgressDialog();
            havaBindAccount.setVisibility(View.GONE);
            noBindAccount.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 用户名下理财账户查询成功回调
     */
    @Override
    public void psnXpadAccountQueryFailed() {
        closeProgressDialog();
    }

    /**
     * 理财专属账号状态查询成功回调
     * 循环遍历理财账户集合，如果账户类型为190，且开通状态非S，那么便将其从集合中移除，否则将账户集合设置给adapter
     */
    @Override
    public void psnOFAAccountStateQuerySuccess() {
        closeProgressDialog();
        initLevel();
        for (XpadAccountModel entity : mList) {
            if ("190".equals(entity.getAccountType()) && !"S".equals(model.getOpenStatus())) {
                mList.remove(entity);
            }
        }
        if (!PublicUtils.isEmpty(mList)) {
            showlist(mList);
        } else {
            havaBindAccount.setVisibility(View.GONE);
            noBindAccount.setVisibility(View.VISIBLE);
        }
    }

    private void showlist(List<XpadAccountModel> list) {
        havaBindAccount.setVisibility(View.VISIBLE);
        noBindAccount.setVisibility(View.GONE);
        mainAdapter.setDatas(list);
        bindNum.setText(list.size() + "条");
    }

    /**
     * 理财专属账号状态查询失败回调
     */
    @Override
    public void psnOFAAccountStateQueryFailed() {
        closeProgressDialog();
    }

    /**
     * 更新风险评估等级
     */
    private void initLevel() {
        if ("1".equals(model.getRiskLevel())) {
            userLv.setVisibility(View.VISIBLE);
            accountLv.setText(getResources().getString(R.string.account_xpad_baoshou));
        } else if ("2".equals(model.getRiskLevel())) {
            userLv.setVisibility(View.VISIBLE);
            accountLv.setText(getResources().getString(R.string.account_xpad_wenjian));
        } else if ("3".equals(model.getRiskLevel())) {
            userLv.setVisibility(View.VISIBLE);
            accountLv.setText(getResources().getString(R.string.account_xpad_pingheng));
        } else if ("4".equals(model.getRiskLevel())) {
            userLv.setVisibility(View.VISIBLE);
            accountLv.setText(getResources().getString(R.string.account_xpad_chengzhang));
        } else if ("5".equals(model.getRiskLevel())) {
            userLv.setVisibility(View.VISIBLE);
            accountLv.setText(getResources().getString(R.string.account_xpad_jinqu));
        }
    }

    @Override
    public void setPresenter(AccountContract.Presenter presenter) {
    }

    @Override
    protected AccountMainPersenter initPresenter() {
        return new AccountMainPersenter(this);
    }
}
