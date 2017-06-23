package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.presenter.PurchasePresenter;

/**
 * @author wangyang
 *         2016/12/30 14:59
 *         选择平仓交易
 */
public class SelectClosePositionFragment extends BaseAccountFragment<PurchasePresenter>{

    private PinnedSectionListView lvTrans;

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_purchase_select_close_position_title);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_purchase_select_close_position,null);
    }

    @Override
    public void initView() {
        lvTrans = (PinnedSectionListView) mContentView.findViewById(R.id.lv_trans);
    }
}
