package com.boc.bocsoft.mobile.bocmobile.base.widget.more;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * @author wangyang
 *         2016/11/29 10:48
 *         更多页基类
 */
public abstract class BaseMoreFragment<T extends BasePresenter> extends MvpBussFragment<T> implements MoreView.OnClickListener {

    private MoreView llMore;

    @Override
    protected final boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected final boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_common_more_title);
    }

    @Override
    protected final View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_more, null);
    }

    @Override
    public final void initView() {
        llMore = (MoreView) mContentView.findViewById(R.id.ll_more);
    }

    @Override
    public final void setListener() {
        llMore.setOnClickListener(this);
    }

    @Override
    public final void initData() {
        for (Item item : getItems()) {
            if (item instanceof MoreItem)
                llMore.addMoreView(item.getModuleId(), item.getTitle(), ((MoreItem) item).isMarginTop());
            else
                llMore.addMoreView(item.getModuleId(), item.getTitle());
        }
        loadData();
    }

    protected abstract List<? extends Item> getItems();

    @Override
    protected T initPresenter() {
        return null;
    }

    public void setPresenter(BasePresenter presenter) {

    }

    protected void loadData() {
    }

    public <T extends CharSequence>void setContentById(String id, T content) {
        llMore.setContentById(id,content);
    }

    public <T extends CharSequence>void setContentById(String id, T content,int textColor) {
        llMore.setContentById(id,content,textColor);
    }
}
