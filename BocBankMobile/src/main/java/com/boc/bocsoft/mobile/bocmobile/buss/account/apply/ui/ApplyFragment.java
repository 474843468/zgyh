package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui;


import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountApplyFragment;


/**
 * 申请定期/活期账户Fragment
 * Created by liuyang on 2016/6/3.
 */
public class ApplyFragment extends BaseAccountApplyFragment{

    private final int BTN_APPLY_REGULAR = 1;
    private final int BTN_APPLY_CURRENT = 2;

    /**
     * 修改标题文字
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_apply_account_title);
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        super.initView();

        addModule(getString(R.string.boc_apply_regular),getString(R.string.boc_apply_regular_text),BTN_APPLY_REGULAR);
//        addModule(getString(R.string.boc_apply_current),getString(R.string.boc_apply_current_text),BTN_APPLY_CURRENT);
    }

    @Override
    protected void onClick(int tag) {
        switch (tag){
            case BTN_APPLY_REGULAR:
                //定期一本通
                start(new ServiceBureauFragment(false,ApplyAccountModel.APPLY_TYPE_REGULAR));
                break;
//            case BTN_APPLY_CURRENT:
//                //活期一本通
//                start(new ServiceBureauFragment(false,ApplyAccountModel.APPLY_TYPE_CURRENT));
//                break;
        }
    }
}
