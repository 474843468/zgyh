package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.CrcdMenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui.BillInstallmentsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.ui.CrcdBillQueryYFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.ui.CrcdBillQueryNFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.adapter.CrcdHomeAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.presenter.CrcdPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui.RepaymentMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.utils.CrcdUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 信用卡首页
 * Created by liuweidong on 2016/11/22.
 */
public class CrcdHomeFragment extends MvpBussFragment<CrcdPresenter> implements CrcdContract.HomeView,
        View.OnClickListener, CardStackView.ItemExpandListener, CrcdHomeAdapter.ClickListener {

    public static final String CUR_ACCOUNT = "cur_account";
    public static final String CRCD_INFO = "crcd_info";

    private View rootView;
    private CardStackView cardStackView;// 卡列表
    /*没有信用卡显示view*/
    private LinearLayout llNoCard;
    private Button btnApply;// 申请信用卡
    private Button btnOpen;// 激活信用卡

    private CrcdHomeAdapter mAdapter;

    private List<AccountBean> mAccountList = new ArrayList<>();// 过滤的所有信用卡
    private List<CrcdModel> mList = new ArrayList<>();// 首页信用卡数据

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_credit_card, null);
        return rootView;
    }

    @Override
    public void initView() {
        cardStackView = (CardStackView) rootView.findViewById(R.id.card_stack_view);
        llNoCard = (LinearLayout) rootView.findViewById(R.id.ll_no_card);
        btnApply = (Button) rootView.findViewById(R.id.btn_apply);
        btnOpen = (Button) rootView.findViewById(R.id.btn_open);
    }

    @Override
    public void initData() {
        setCommonInfo();
        if (mAccountList.size() == 0) {// 没有信用卡
            cardStackView.setVisibility(View.GONE);
            llNoCard.setVisibility(View.VISIBLE);
        } else {// 存在信用卡
            mAdapter = new CrcdHomeAdapter(mContext);
            cardStackView.setAdapter(mAdapter);
            for (AccountBean accountBean : mAccountList) {
                CrcdModel item = new CrcdModel();
                item.setAccountBean(accountBean);
                mList.add(item);
            }
            mAdapter.updateData(mList);
        }
    }

    @Override
    public void setListener() {
        btnApply.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
        if (mAccountList.size() > 0) {
            cardStackView.setItemExpandListener(this);
            mAdapter.setClickListener(this);
        }
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_crcd_home_title);
    }

    @Override
    protected CrcdPresenter initPresenter() {
        return new CrcdPresenter(this);
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
        Bundle bundle = new Bundle();
        bundle.putInt(CrcdMenuFragment.MENU, CrcdMenuFragment.CREDIT_MORE_ACCOUNT);
        CrcdMenuFragment crcdMenuFragment = new CrcdMenuFragment();
        crcdMenuFragment.setArguments(bundle);
        start(crcdMenuFragment);
    }

    /**
     * 设置公共信息
     */
    private void setCommonInfo() {
        mTitleBarView.setBackgroundResource(R.color.boc_bg_light_blue);// 设置标题栏背景
        mAccountList = ApplicationContext.getInstance().getChinaBankAccountList(CrcdUtil.filterAccountType());

        // TODO: 2016/12/9 测试数据
//        AccountBean item1 = new AccountBean();
//        AccountBean item2 = new AccountBean();
//        AccountBean item3 = new AccountBean();
//        AccountBean item4 = new AccountBean();
//        AccountBean item5 = new AccountBean();
//        mAccountList.add(item1);
//        mAccountList.add(item2);
//        mAccountList.add(item3);
//        mAccountList.add(item4);
//        mAccountList.add(item5);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_apply) {// 申请信用卡
            // TODO: 2016/12/9 添加跳转
        } else if (i == R.id.btn_open) {// 激活信用卡

        }
    }

    @Override
    public void onClick(int id) {
        if (id == R.id.img_details) {// 详情
            getPresenter().queryCrcdPoint(mList.get(cardStackView.getSelectPosition()));
        } else if (id == R.id.bill_no) {// 未出账单
            CrcdBillQueryNFragment billQueryYFragment = new CrcdBillQueryNFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(CUR_ACCOUNT, mList.get(cardStackView.getSelectPosition()).getAccountBean());
            billQueryYFragment.setArguments(bundle);
            start(billQueryYFragment);
        } else if (id == R.id.bill_yes) {// 已出账单
            CrcdBillQueryYFragment fragment = CrcdBillQueryYFragment.newInstance(mList.get(cardStackView.getSelectPosition()).getAccountBean());
            start(fragment);
        } else if (id == R.id.btn_repayment) {// 还款
            Bundle bundle = new Bundle();
            bundle.putParcelable(CUR_ACCOUNT, mList.get(cardStackView.getSelectPosition()).getAccountBean());
            RepaymentMainFragment fragment = new RepaymentMainFragment();
            fragment.setArguments(bundle);
            start(fragment);
        } else if (id == R.id.btn_bill_period) {// 账单分期
            getPresenter().queryBillInput(mList.get(cardStackView.getSelectPosition()).getAccountBean().getAccountId());
        }
    }

    /**
     * 卡列表展开监听
     */
    @Override
    public void onItemExpand(boolean isExpanded) {
        if (isExpanded) {
            getPresenter().queryCrcdGeneralInfo(mList.get(cardStackView.getSelectPosition()));
        }
    }

    private void startDetailFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CRCD_INFO, mList.get(cardStackView.getSelectPosition()));
        CrcdDetailFragment fragment = new CrcdDetailFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 信用卡综合信息查询成功
     */
    @Override
    public void queryCrcdGeneralInfoSuccess() {
        getPresenter().queryCrcdBillsExist(mList.get(cardStackView.getSelectPosition()));
    }

    @Override
    public void queryCrcdPointFail() {
        startDetailFragment();
    }

    @Override
    public void queryCrcdPointSuccess() {
        startDetailFragment();
    }

    @Override
    public void queryCrcdBillsExistFail() {
        mAdapter.notifyItemChanged(cardStackView.getSelectPosition());
    }

    @Override
    public void queryCrcdBillsExistSuccess() {
        mAdapter.notifyItemChanged(cardStackView.getSelectPosition());
    }

    /**
     * 获取账单分期上下限成功
     */
    @Override
    public void queryBillInputSuccess(BigDecimal upLimit, BigDecimal lowLimit) {
        BillInstallmentModel billModel = new BillInstallmentModel();
        billModel.setUpInstmtAmount(upLimit);
        billModel.setLowInstmtAmount(lowLimit);
        start(BillInstallmentsFragment.getInstance(billModel, mList.get(cardStackView.getSelectPosition()).getAccountBean()));
    }

}
