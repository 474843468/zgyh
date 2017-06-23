package com.boc.bocsoft.mobile.bocmobile.base.container;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者：XieDu
 * 创建时间：2016/12/16 20:40
 * 描述：pageAdapter基类
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {

    private SparseArray<T> mItems = new SparseArray<>();
    protected T mCurrentPrimaryItem = null;

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T item = mItems.get(position);
        if (item == null) {
            item = getItem(position);
            mItems.put(position, item);
        }
        // 通过item得到将要被add到viewpager中的view
        View view = getViewFromItem(item);
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view);
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(getViewFromItem((T) object));
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentPrimaryItem = (T) object;
    }

    /**
     * 这里要实现一个从item拿到view的规则
     *
     * @param item 包含view的item对象
     * @return item中的view对象
     */
    protected abstract View getViewFromItem(T item);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return getViewFromItem((T) object) == view;
    }

    public abstract T getItem(int position);
}
