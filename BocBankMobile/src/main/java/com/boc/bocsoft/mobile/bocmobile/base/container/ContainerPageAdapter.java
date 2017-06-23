package com.boc.bocsoft.mobile.bocmobile.base.container;

import android.view.View;
import android.view.ViewGroup;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/12/16 19:48
 * 描述：容器的pageAdapter
 */
public class ContainerPageAdapter extends BasePagerAdapter<IContainer> {

    public ContainerPageAdapter(List<IContainer> mContainers) {
        this.mContainers = mContainers;
    }

    private List<IContainer> mContainers = null;
    private List<String> mTitles = null;

    public ContainerPageAdapter(List<String> titles, List<IContainer> containers) {
        mTitles = titles;
        mContainers = containers;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        IContainer iContainer = (IContainer) object;
        if (iContainer != mCurrentPrimaryItem) { // 支持懒加载
            iContainer.setUserVisibleHint(true);
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    protected View getViewFromItem(IContainer item) {
        return item.getView();
    }

    @Override
    public IContainer getItem(int position) {
        return PublicUtils.isEmpty(mContainers) ? null : mContainers.get(position);
    }

    @Override
    public int getCount() {
        return PublicUtils.isEmpty(mContainers) ? 0 : mContainers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PublicUtils.isEmpty(mTitles) ? "" : mTitles.get(position);
    }
}
