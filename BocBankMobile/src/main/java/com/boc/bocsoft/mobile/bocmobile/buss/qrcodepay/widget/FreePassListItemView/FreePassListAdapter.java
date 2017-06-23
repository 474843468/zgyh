package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import com.boc.bocsoft.mobile.framework.widget.listview.ListAdapterHelper;

import java.util.Arrays;

/**
 * 小额免密列表适配器
 * Created by wangf on 2016/9/22.
 */
public class FreePassListAdapter extends BaseListAdapter<FreePassViewModel> {
    private boolean amountInfoVisible = true;
    private boolean[] loadAmountInfo;

    public FreePassListAdapter(Context context) {
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
        FreePassListItemView accountListItemView =
                convertView == null ? new FreePassListItemView(mContext): (FreePassListItemView) convertView;
        FreePassViewModel viewModel = getItem(position);


        //设置分组数据
        String accountType = viewModel.getAccountBean().getAccountType();
        int accountFlag = QrCodeUtils.judgeAccountIsCredit(accountType);
        int accountFlagTemp = -2;
        if (position > 0) {
            String accountTypeTemp = getItem(position - 1).getAccountBean().getAccountType();
            accountFlagTemp = QrCodeUtils.judgeAccountIsCredit(accountTypeTemp);
        }
        if (accountFlag == accountFlagTemp) {
            accountListItemView.isShowItemTitle(false);
        } else {// 设置标题
            accountListItemView.isShowItemTitle(true);
            if (QrCodeUtils.judgeAccountIsCredit(accountType) == 0){
                accountListItemView.setItemTitleText("信用卡");
            }else if(QrCodeUtils.judgeAccountIsCredit(accountType) == 1){
                accountListItemView.setItemTitleText("借记卡");
            }else {
                accountListItemView.setItemTitleText("");
            }
        }


        accountListItemView.setData(viewModel);
        if (loadAmountInfo!=null&&loadAmountInfo[position]) {
            accountListItemView.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
        }
        setOnClickChildViewInItemItf(position, viewModel, accountListItemView.ivLoading);
        setOnClickChildViewInItemItf(position, viewModel, accountListItemView.cbFree);
        return accountListItemView;
    }

    /**
     * 更新某一条的视图
     */
    public void updateItemView(ListView listView, int position) {
        onLoadAmountInfoFinished(position);
        if (isVisibleItem(listView, position)) {
            int index = position - listView.getFirstVisiblePosition();
            FreePassListItemView itemView = (FreePassListItemView) listView.getChildAt(index);
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
                new ListAdapterHelper.OnClickChildViewInItemItf<FreePassViewModel>() {
                    @Override
                    public void onClickChildViewInItem(int position, FreePassViewModel item,View childView) {
                        if (childView.getId() == R.id.iv_qrpay_loading) {
                            ((PartialLoadView) childView).setLoadStatus(PartialLoadView.LoadStatus.LOADING);
                            onRefreshListener.onRefresh(position, getItem(position));
                        }else if(childView.getId() == R.id.cb_qrpay_free){
                        	onRefreshListener.onClickCheckBox(position, getItem(position), childView);
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
}
