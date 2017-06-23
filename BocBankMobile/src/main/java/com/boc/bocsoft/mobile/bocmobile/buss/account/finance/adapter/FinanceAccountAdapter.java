package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.AccountListItemView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * @author wangyang
 *         16/6/20 19:18
 *         电子现金账户概览界面
 */
public class FinanceAccountAdapter extends BaseListAdapter<FinanceModel> {

    public final int TYPE_OTHER = 0;

    public final int TYPE_SELF = 1;

    public FinanceAccountAdapter(Context context) {
        super(context);
    }

    /**
     * 更新账户信息--查询账户余额后,单条更新
     */
    public void updateItemView(ListView listView, FinanceModel financeModel) {
        //FinanceModel hashCode与equals 对比字段为 accountId
        int position = getDatas().indexOf(financeModel);
        if (position < 0)
            return;

        //更新单挑View
        getDatas().set(position, financeModel);

        if (isVisibleItem(listView, position)) {
            int index = position - listView.getFirstVisiblePosition();
            AccountListItemView itemView = (AccountListItemView) listView.getChildAt(index);
            itemView.setData(ModelUtil.generateAccountListItemViewModel(getItem(position)));
        }
    }

    /**
     * 根据position获取账户列表
     *
     * @param position
     * @return
     */
    public FinanceModel getFinanceModel(int position) {
        if (position < 0 || getDatas() == null)
            return null;
        return getDatas().get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_OTHER:
                convertView = mInflater.inflate(R.layout.boc_button_select_finance, null, false);
                return convertView;
            case TYPE_SELF:
                if (convertView == null || convertView.getTag() == null)
                    convertView = mInflater.inflate(R.layout.boc_fragment_finance_item, null, false);
                break;
        }

        FinanceViewHolder financeViewHolder = new FinanceViewHolder(convertView);

        FinanceModel financeModel = getDatas().get(position);

        //生成CardInfo数据Model
        financeViewHolder.accountItemView.setData(ModelUtil.generateAccountListItemViewModel(financeModel));
        financeViewHolder.accountItemView.setArrowVisible(false);

        //修改状态
        if (getDatas().get(position).getSupplyBalance() == null)
            financeViewHolder.accountItemView.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
        setOnClickChildViewInItemItf(position, financeModel, financeViewHolder.accountItemView.findViewById(R.id.iv_loading));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1)
            return TYPE_OTHER;
        else
            return TYPE_SELF;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    /**
     * 获取某个只含有finanICNumber账号的Model,在列表中对应的账户Model
     *
     * @param financeModel
     * @return
     */
    public FinanceModel getFinanceModel(FinanceModel financeModel) {
        int index = getDatas().indexOf(financeModel);
        return index < 0 ? null : getDatas().get(index);
    }

    public class FinanceViewHolder {

        //账户Cardview控件
        AccountListItemView accountItemView;

        public FinanceViewHolder(View itemView) {
            this.accountItemView = (AccountListItemView) itemView;
            itemView.setTag(this);
        }
    }

}
