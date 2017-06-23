package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import com.boc.bocsoft.mobile.framework.widget.listview.ListAdapterHelper;
import java.util.Arrays;

/**
 * 账户列表适配器
 * Created by XieDu on 2016/7/7.
 */
public class AccountListAdapter extends BaseListAdapter<AccountListItemViewModel> {
    private boolean amountInfoVisible = true;
    private boolean[] loadAmountInfo;
    private boolean arrowVisible = false;
    private boolean medicalEcashVisible = true;

    public AccountListAdapter(Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountListItemView accountListItemView =
                convertView == null ? new AccountListItemView(mContext)
                        : (AccountListItemView) convertView;
        accountListItemView.setArrowVisible(arrowVisible);
        accountListItemView.setMedicalEcashVisible(medicalEcashVisible);
        AccountListItemViewModel viewModel = getItem(position);
        accountListItemView.setAmountInfoVisible(amountInfoVisible);
        accountListItemView.setData(viewModel);
        if (loadAmountInfo != null && loadAmountInfo[position]) {
            accountListItemView.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
        }
        setOnClickChildViewInItemItf(position, viewModel, accountListItemView.ivLoading);
        return accountListItemView;
    }

    /**
     * 更新某一条的视图
     */
    public void updateItemView(ListView listView, int position) {
        onLoadAmountInfoFinished(position);
        if (isVisibleItem(listView, position)) {
            int index = position - listView.getFirstVisiblePosition();
            AccountListItemView itemView = (AccountListItemView) listView.getChildAt(index);
            itemView.setData(getItem(position));
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
            loadAmountInfo = new boolean[getCount()];
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

    public void setArrowVisible(boolean arrowVisible) {
        this.arrowVisible = arrowVisible;
    }

    public void setMedicalEcashVisible(boolean visible) {
        medicalEcashVisible = visible;
    }
}
