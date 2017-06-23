package com.boc.bocsoft.mobile.bocmobile.base.widget.PullToRefreshSwipeMenuListView;


import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;


/**
 * Created by gengjunying on 2016/12/12.
 */
public interface SwipeMenuCreator {
    void create(SwipeMenu menu, HceCardListQueryViewModel data);
}
