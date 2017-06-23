package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.InvestTreatyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter.ProtocolPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.utils.ProtocolConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;

import java.util.Collections;

/**
 * 投资协议申请（选择投资协议页面）
 * Created by wangtong on 2016/10/24.
 * Modified by liuweidong on 2016/11/4.
 */
public class ProtocolSelectFragment extends MvpBussFragment<ProtocolPresenter> implements ProtocolContact.ProtocolView {

    public static final String PROTOCOL_PURCHASE = "protocol_purchase";
    private View rootView;
    private RecyclerView recyclerView;
    private ProtocolAdapter mAdapter;// 适配器
    private LinearLayoutManager linearLayoutManager;

    private ProtocolModel mViewModel;// ui层model
    public ProtocolPresenter mPresenter;// 网络请求
//    private Map<String, String> protocols;// 协议列表

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_protocol, null);
        return rootView;
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mTitleBarView.setRightButton(mContext.getString(R.string.boc_wealth_protocol_help));
    }

    @Override
    public void initData() {
        mViewModel = getArguments().getParcelable(PROTOCOL_PURCHASE);// 获取详情页传递的数据
        Collections.sort(mViewModel.getProtocolList());
        /*初始化RecyclerView*/
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ProtocolAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected ProtocolPresenter initPresenter() {
        mPresenter = new ProtocolPresenter(this);
        return mPresenter;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return mContext.getString(R.string.boc_wealth_protocol_select);
    }

    @Override
    protected void titleRightIconClick() {
        ProtocolSelectHelpFragment fragment = new ProtocolSelectHelpFragment();
        start(fragment);// 跳转帮助页面
    }

    public ProtocolModel getViewModel() {
        return mViewModel;
    }

    /**
     * 智能协议详情
     *
     * @param details
     */
    @Override
    public void queryTreatyDetailSuccess(ProtocolIntelligentDetailsBean details) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolSmartFragment.DETAILS, details);
        bundle.putParcelable(ProtocolSmartFragment.MODEL, mViewModel);
        ProtocolSmartFragment fragment = new ProtocolSmartFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 周期性产品续约协议签约/签约初始化
     */
    @Override
    public void psnXpadSignInitSuccess(PsnXpadSignInitBean initBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE, mViewModel);
        bundle.putParcelable(ProtocolPeriodContinueFragment.SIGNINIT, initBean);
        ProtocolPeriodContinueFragment fragment = new ProtocolPeriodContinueFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * RecyclerView的适配器
     */
    class ProtocolAdapter extends RecyclerView.Adapter<ProtocolAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = View.inflate(getContext(), R.layout.boc_layout_protocol_select, null);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final InvestTreatyBean item = mViewModel.getProtocolList().get(position);
            viewHolder.txtName.setText(item.getAgrName());
            if (item.getAgrType().equals("1")) {
                viewHolder.txtTitle.setText("智能投资");
            } else if (item.getAgrType().equals("2")) {
                viewHolder.txtTitle.setText("定时定额投资");
                viewHolder.txtContent.setText(getString(R.string.boc_protocol_purchase_content_4));
            } else if (item.getAgrType().equals("3")) {
                viewHolder.txtTitle.setText("周期滚续投资");
                viewHolder.txtContent.setText(getString(R.string.boc_protocol_purchase_content_5));
            } else if (item.getAgrType().equals("4")) {
                viewHolder.txtTitle.setText("余额理财投资");
                viewHolder.txtContent.setText(getString(R.string.boc_protocol_purchase_content_7));
            }
            if ("1".equals(item.getInstType())) {
                viewHolder.txtContent.setText(getString(R.string.boc_protocol_purchase_content_0, ProtocolConvertUtils.convertPeriodAgr(item.getPeriodAgr())));
            } else if ("2".equals(item.getInstType())) {
                viewHolder.txtContent.setText(getString(R.string.boc_protocol_purchase_content_1, ProtocolConvertUtils.convertPeriodAgr(item.getPeriodAgr()),item.getPeriodBal()));
            } else if ("3".equals(item.getInstType())) {
                viewHolder.txtContent.setText(getString(R.string.boc_protocol_purchase_content_2, ProtocolConvertUtils.convertPeriodAgr(item.getPeriodAgr()),ProtocolConvertUtils.convertPeriodAgr(item.getPeriodPur())));
            } else if ("4".equals(item.getInstType())) {
                viewHolder.txtContent.setText(getString(R.string.boc_protocol_purchase_content_3, ProtocolConvertUtils.convertPeriodAgr(item.getPeriodAgr()),ProtocolConvertUtils.convertPeriodAgr(item.getPeriodPur())));
            }
            if (position > 0) {
                InvestTreatyBean lastItem = mViewModel.getProtocolList().get(position - 1);
                if (item.getAgrType().equals(lastItem.getAgrType())) {
                    viewHolder.txtTitle.setVisibility(View.GONE);
                } else {
                    viewHolder.txtTitle.setVisibility(View.VISIBLE);
                }
            } else {
                viewHolder.txtTitle.setVisibility(View.VISIBLE);
            }

            viewHolder.llParentContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.selectedProtocol = item;
                    if (item.getAgrType().equals("1")) {// 智能投资协议
                        getPresenter().queryTreatyDetail();// 进入时先调用接口
                    } else if (item.getAgrType().equals("2")) {// 定时定额投资
                        FixedtimeFixedAmountInvestFragment toFragment = new FixedtimeFixedAmountInvestFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE, mViewModel);
                        toFragment.setArguments(bundle);
                        start(toFragment);
                    } else if (item.getAgrType().equals("3")) {// 周期滚续投资
                        if ("7".equals(item.getInstType())) { // 周期滚续协议
                            getPresenter().psnXpadSignInit();
                        } else if ("8".equals(item.getInstType())) {// 业绩基准周期滚续（购买）
                            start(new PurchaseFragment());
                        }
                    } else if (item.getAgrType().equals("4")) {// 余额理财投资
                        ProtocolBalanceInvestFragment1 toFragment = new ProtocolBalanceInvestFragment1();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE, mViewModel);
                        toFragment.setArguments(bundle);
                        start(toFragment);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mViewModel.getProtocolList().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitle;
            LinearLayout llParentContent;
            TextView txtName;
            TextView txtContent;

            public ViewHolder(View itemView) {
                super(itemView);
                txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
                llParentContent = (LinearLayout) itemView.findViewById(R.id.ll_parent_content);
                txtName = (TextView) itemView.findViewById(R.id.txt_name);
                txtContent = (TextView) itemView.findViewById(R.id.txt_content);
            }
        }
    }
}
