package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirCardPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirtualCardContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.VirtualBillItemView;

import java.util.List;

/**
 * @author wangyang
 *         16/9/7 00:07
 *         未出账账单
 */
@SuppressLint("ValidFragment")
public class VirtualUnsettledBillFragment extends BaseAccountFragment<VirCardPresenter> implements VirtualCardContract.VirCardUnsettledBillView, TransactionView.ClickListener,
        PullToRefreshLayout.OnLoadListener {

    private PullToRefreshLayout flRefresh;

    private TransactionView transDetail;

    private VirtualBillItemView itemTotalIn, itemTotalOut;

    private TextView tvDetail;

    private VirtualCardModel model;

    private List<VirtualBillModel> models;

    private int currentPage;

    public VirtualUnsettledBillFragment(VirtualCardModel model, List<VirtualBillModel> data) {
        this.model = model;
        this.models = data;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_virtual_account_detail_bill);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_unsettled, null);
    }

    @Override
    protected VirCardPresenter initPresenter() {
        return new VirCardPresenter(this);
    }

    @Override
    public void initView() {
        flRefresh = (PullToRefreshLayout) mContentView.findViewById(R.id.fl_refresh);
        transDetail = (TransactionView) mContentView.findViewById(R.id.trans_detail);
        transDetail.setAdapter();

        View headView = View.inflate(mContext,R.layout.boc_fragment_virtual_unsettled_header,null);
        transDetail.addHeaderView(headView);
        itemTotalIn = (VirtualBillItemView) mContentView.findViewById(R.id.item_in);
        itemTotalOut = (VirtualBillItemView) mContentView.findViewById(R.id.item_out);
        tvDetail = (TextView) mContentView.findViewById(R.id.tv_detail);
    }

    @Override
    public void setListener() {
        transDetail.setListener(this);
        flRefresh.setOnLoadListener(this);
    }

    @Override
    public void initData() {
        itemTotalIn.setUnsettled(models, true);
        itemTotalOut.setUnsettled(models, false);

        loadData();
    }

    private void loadData() {
        currentPage++;
        showLoadingDialog();
        getPresenter().psnCrcdVirtualCardUnsettledbillQuery(model, currentPage);
    }

    @Override
    public void onItemClickListener(int position) {
        start(new VirtualTransDetailFragment(model.getAccountIbkNum(), models.get(0).getTransModels().get(position)));
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        loadData();
    }

    @Override
    public void queryUnsettledBill(List<VirtualBillTransModel> transModels, int recordNumber) {
        closeProgressDialog();

        if (currentPage > 0) {
            if (transModels == null || recordNumber <= transModels.size())
                flRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            else
                flRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        }

        if (recordNumber == 0 && transDetail.getAdapter().getCount() == 0) {
            tvDetail.setText(getString(R.string.boc_trans_detail_title_no_data));
            return;
        }

        models.get(0).setTransModels(transModels);
        transDetail.setData(ModelUtil.generateTransactionBean(getContext(), transModels, true));
    }
}
