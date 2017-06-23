package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;
import com.boc.bocsoft.mobile.framework.widget.listview.ListAdapterHelper;
import java.util.Arrays;

/**
 * 账户列表适配器
 * Created by XieDu on 2016/7/7.
 */
public class AccountRecycleViewAdapter extends
        BaseRecycleViewAdapter<AccountListItemViewModel, AccountRecycleViewAdapter.AccountViewHolder> {
    private boolean amountInfoVisible = true;
    private boolean[] loadAmountInfo;

    public AccountRecycleViewAdapter(Context context) {
        super(context);
    }

    /**
     * 设置是否显示额度区域，默认显示
     *
     * @param visible 是否显示
     */
    public void setAmountInfoVisible(boolean visible) {
        amountInfoVisible = visible;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        AccountListItemView accountListItemView = new AccountListItemView(mContext);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        accountListItemView.setLayoutParams(lp);
        return new AccountViewHolder(accountListItemView);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder accountViewHolder, int position) {
        AccountListItemViewModel viewModel = getItem(position);
        accountViewHolder.accountItemView.setAmountInfoVisible(amountInfoVisible);
        accountViewHolder.accountItemView.setData(viewModel);
        if (loadAmountInfo!=null&&loadAmountInfo[position]) {
            accountViewHolder.accountItemView.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
        }
        setOnClickChildViewInItemItf(position, viewModel,
                accountViewHolder.accountItemView.ivLoading);
    }

    public class AccountViewHolder extends BaseRecycleViewAdapter.BaseViewHolder {

        //账户Cardview控件
        private AccountListItemView accountItemView;

        public AccountViewHolder(AccountListItemView itemView) {
            super(itemView);
            accountItemView = itemView;
        }
    }

    /**
     * 设置局部加载按钮刷新点击监听
     *
     * @param onRefreshListener 刷新监听
     */
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        setOnClickChildViewInItemItf(
                new ListAdapterHelper.OnClickChildViewInItemItf<AccountListItemViewModel>() {
                    @Override
                    public void onClickChildViewInItem(int position, AccountListItemViewModel item,
                            View childView) {
                        if (childView.getId() == R.id.iv_loading) {
                            ((PartialLoadView) childView).setLoadStatus(
                                    PartialLoadView.LoadStatus.LOADING);
                            onRefreshListener.onRefresh(position, item);
                        }
                    }
                });
    }

    /**
     * 设置为初始化余额信息状态
     */
    public void loadAmountInfo() {
        if (loadAmountInfo == null) {
            loadAmountInfo = new boolean[getItemCount()];
        }
        Arrays.fill(loadAmountInfo, true);
    }

    /**
     * 加载余额完毕
     */
    public void onLoadAmountInfoFinished(int position) {
        if (loadAmountInfo != null) {
            loadAmountInfo[position] = false;
        }
    }
}
