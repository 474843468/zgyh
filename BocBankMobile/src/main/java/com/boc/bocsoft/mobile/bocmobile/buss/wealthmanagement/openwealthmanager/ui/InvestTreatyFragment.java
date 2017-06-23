package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountRegistFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessChoiceFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.model.OpenWealthModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.presenter.OpenWealthPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolSelectFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthBundleData;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 开通投资理财 - 三大步骤
 * Created by wangtong on 2016/9/19.
 * Modified by liuweidong on 2016/11/16.
 */
public class InvestTreatyFragment extends MvpBussFragment<OpenWealthPresenter> implements OpenWealthManagerContact.OpenStatusView {
    public static final String TAG = "InvestTreatyFragment";
    private View rootView;
    private LinearLayout llOpenHint;// 理财开通提示
    private ListView listView;
    private TextView btnNext;// 下一步
    private ItemAdapter mAdapter;

    private BussFragment toFragment;// 从哪个模块跳转过来
    private WealthDetailsBean detailsBean;// 明细接口响应数据（购买）
    private DetailsRequestBean requestBean;// 明细页其他数据（购买）
    private boolean[] mNeedOpened;// 根据模块需求显示需要的开通
    private boolean[] mOpenStatus;// 判断三个步骤开通的状态
    private boolean isAllOpened = false;// 判断是否都开通 的状态

    public InvestTreatyFragment() {
    }

    /**
     * 购买模块跳转使用
     *
     * @param detailsBean
     * @param requestBean
     */
    public InvestTreatyFragment(WealthDetailsBean detailsBean, DetailsRequestBean requestBean) {
        this.detailsBean = detailsBean;
        this.requestBean = requestBean;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_investtreaty, null);
        return rootView;
    }

    @Override
    public void initView() {
        llOpenHint = (LinearLayout) rootView.findViewById(R.id.ll_open_hint);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        btnNext = (TextView) rootView.findViewById(R.id.btn_next);
    }

    @Override
    public void initData() {
        mAdapter = new ItemAdapter();
        listView.setAdapter(mAdapter);
        mOpenStatus = WealthProductFragment.getInstance().isOpenWealth();// 获取理财开通状态
        updateList();
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {// 下一步
            @Override
            public void onClick(View v) {
                if (toFragment instanceof ProtocolSelectFragment) {// 投资协议申请
                    WealthDetailsFragment.getInstance().queryInvestTreaty();
                } else if (toFragment instanceof PurchaseFragment) {// 购买
                    Log.i(TAG, "--------------------buy------------------");
                    getPresenter().queryFinanceAccountInfo();
                } else {
                    startWithPop(toFragment);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataHolder item = (DataHolder) mAdapter.getItem(position);
                if (getString(R.string.boc_wealth_open_service).equals(item.name)) {
                    if (!mOpenStatus[0]) {// 未开通
                        getPresenter().psnGetSecurityFactor();
                    }
                } else if (getString(R.string.boc_wealth_open_risk).equals(item.name)) {
                    if (!mOpenStatus[1]) {// 无效
                        if (!mOpenStatus[0]) {
                            Toast.makeText(mContext, getString(R.string.boc_wealth_open_service_hint), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (WealthProductFragment.getInstance().getRiskStatus() == 1) {
                            start(new RiskAssessFragment(InvestTreatyFragment.class));
                        } else {
                            start(new RiskAssessChoiceFragment(InvestTreatyFragment.class));
                        }
                    }
                } else if (getString(R.string.boc_wealth_open_account).equals(item.name)) {
                    if (!mOpenStatus[2]) {// 未登记
                        if (!mOpenStatus[0]) {
                            Toast.makeText(mContext, getString(R.string.boc_wealth_open_service_hint), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!mOpenStatus[1]) {
                            Toast.makeText(mContext, getString(R.string.boc_wealth_open_risk_hint), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        start(new AccountRegistFragment(InvestTreatyFragment.class));
                    }
                }
            }
        });
    }

    @Override
    protected OpenWealthPresenter initPresenter() {
        return new OpenWealthPresenter(this);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_wealth_open_title);
    }

    @Override
    public void reInit() {
        WealthProductFragment.getInstance().setCall(new OpenStatusI() {
            @Override
            public void openSuccess() {
                closeProgressDialog();
                mOpenStatus = WealthProductFragment.getInstance().isOpenWealth();
                updateList();
            }

            @Override
            public void openFail(ErrorDialog dialog) {
                closeProgressDialog();
            }
        });
        showLoadingDialog(false);
        WealthProductFragment.getInstance().requestOpenStatus();// 开通理财状态接口
    }

    /**
     * 设置需要的理财状态与即将跳转的页面（必须调用）
     *
     * @param needs
     * @param toFragment
     */
    public void setDefaultInvestFragment(boolean[] needs, BussFragment toFragment) {
        this.mNeedOpened = needs;
        this.toFragment = toFragment;
    }

    /**
     * 更新页面数据
     */
    private void updateList() {
        String[] allData = new String[]{getString(R.string.boc_wealth_open_service), getString(R.string.boc_wealth_open_risk), getString(R.string.boc_wealth_open_account)};
        List<DataHolder> data = new ArrayList<>();
        for (int i = 0; i < mNeedOpened.length; i++) {
            if (mNeedOpened[i]) {
                DataHolder item = new DataHolder();
                item.name = allData[i];
                item.isOpened = mOpenStatus[i];
                data.add(item);
            }
        }
        mAdapter.updateData(data);
        isAllOpened = WealthPublicUtils.isOpenAll(mNeedOpened, mOpenStatus);
        if (isAllOpened) {//只有 都开通的情况 才能 显示按钮
            llOpenHint.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
        } else {
            llOpenHint.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
        }
    }

    /**
     * 查询理财账户成功
     */
    @Override
    public void getAccountSuccess() {
        closeProgressDialog();
        if (detailsBean.isLoginBeforeI()) {

        } else {

        }
        if (toFragment instanceof PurchaseFragment) {// 购买
            WealthViewModel viewModel = WealthProductFragment.getInstance().getViewModel();
            WealthAccountBean accountBean = null;
            if (requestBean.getAccountBean() == null) {
                accountBean = viewModel.getAccountList().get(0);
            } else {
                accountBean = requestBean.getAccountBean();
            }
            PurchaseFragment fragment = PurchaseFragment.newInstance(WealthBundleData.buildBuyData(detailsBean,
                    accountBean), (ArrayList<WealthAccountBean>) viewModel.getAccountList());
            startWithPop(fragment);
        }
    }

    /**
     * 安全因子调用成功
     */
    @Override
    public void psnGetSecurityFactorReturned(OpenWealthModel viewModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(OpenWealthManagerFragment.VIEW_MODEL, viewModel);
        OpenWealthManagerFragment fragment = new OpenWealthManagerFragment(InvestTreatyFragment.class);
        fragment.setArguments(bundle);
        start(fragment);// 跳转到开通理财服务页面
    }

    class ItemAdapter extends BaseAdapter {

        private List<DataHolder> data;

        public ItemAdapter() {
            data = new ArrayList<>();
        }

        public void updateData(List<DataHolder> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getContext(), R.layout.boc_item_investtreaty, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView state = (TextView) convertView.findViewById(R.id.state);
            TextView inRing = (TextView) convertView.findViewById(R.id.in_ring);
            ImageView img = (ImageView) convertView.findViewById(R.id.choice_data_arrow);
            View outRing = convertView.findViewById(R.id.out_ring);

            DataHolder item = (DataHolder) getItem(position);
            name.setText(data.get(position).name);
            if (item.isOpened) {// true
                if (getString(R.string.boc_wealth_open_service).equals(item.name)) {
                    state.setText(getString(R.string.boc_wealth_open_service_y));
                    img.setVisibility(View.GONE);
                } else if (getString(R.string.boc_wealth_open_risk).equals(item.name)) {
                    state.setText(getString(R.string.boc_wealth_open_risk_y));
                    img.setVisibility(View.GONE);
                } else if (getString(R.string.boc_wealth_open_account).equals(item.name)) {
                    state.setText(getString(R.string.boc_wealth_open_account_y));
                    img.setVisibility(View.GONE);
                }
                state.setTextColor(getResources().getColor(R.color.boc_round_progressbar_green));
                outRing.setVisibility(View.INVISIBLE);// 去掉外层圈
            } else {// false
                if (getString(R.string.boc_wealth_open_service).equals(item.name)) {
                    state.setText(getString(R.string.boc_wealth_open_service_n));
                    img.setVisibility(View.VISIBLE);
                } else if (getString(R.string.boc_wealth_open_risk).equals(item.name)) {
                    state.setText(getString(R.string.boc_wealth_open_risk_n));
                    img.setVisibility(View.VISIBLE);
                } else if (getString(R.string.boc_wealth_open_account).equals(item.name)) {
                    state.setText(getString(R.string.boc_wealth_open_account_n));
                    img.setVisibility(View.VISIBLE);
                }
                state.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                if (position > 0) {
                    DataHolder holder = (DataHolder) getItem(position - 1);
                    if (!holder.isOpened) {
                        outRing.setVisibility(View.INVISIBLE);
                    }
                }
            }
            inRing.setText(position + 1 + "");
            return convertView;
        }
    }

    static class DataHolder {
        String name;// 理财状态名称
        boolean isOpened;// 开通状态
    }
}
