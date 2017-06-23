package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.base.widget.more.BaseMoreFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;

import java.util.List;

/**
 * @author wangyang
 *         16/8/13 21:45
 *         账户更多界面
 */
@SuppressLint("ValidFragment")
public class MoreAccountFragment extends BaseMoreFragment{

    //账户详情
    public static final String MORE_DETAIL = "more_detail";
    //注销
    public static final String MORE_CANCEL = "more_cancel";

    private VirtualCardModel model;

    public MoreAccountFragment(VirtualCardModel model) {
        this.model = model;
    }

    @Override
    protected List<Item> getItems() {
        return ModelUtil.generateVirtualAccountMoreItems(getResources());
    }

    @Override
    public void onClick(String id) {
        switch (id){
            case MORE_DETAIL:
                /**账户详情*/
                start(new VirtualAccountDetailFragment(model));
                break;
            case MORE_CANCEL:
                /**注销*/
                start(new VirtualAccountCancelFragment(model));
                break;
        }
    }
}
