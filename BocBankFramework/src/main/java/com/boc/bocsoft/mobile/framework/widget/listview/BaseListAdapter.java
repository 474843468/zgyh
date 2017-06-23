package com.boc.bocsoft.mobile.framework.widget.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> mList = new ArrayList<T>();

    protected LayoutInflater mInflater;

    protected Context mContext;

    protected ListAdapterHelper.OnClickChildViewInItemItf<T> onClickChildViewInItemItf;

    public BaseListAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
    }

    /**
     * 对外部提供的设置点击列表项中子view监听器的方法
     */
    public void setOnClickChildViewInItemItf(
            ListAdapterHelper.OnClickChildViewInItemItf<T> OnClickChildViewInItemItf) {
        this.onClickChildViewInItemItf = OnClickChildViewInItemItf;
    }

    /**
     * 给列表项的某个子视图绑定点击监听器。adapter的getView中使用。
     */
    protected void setOnClickChildViewInItemItf(int position, T item, View childView) {
        ListAdapterHelper.setOnClickChildViewInItemItf(position, item, childView,
                onClickChildViewInItemItf);
    }

    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    public T getItem(int position) {
        return PublicUtils.isEmpty(mList) ? null : mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setDatas(List<T> datas) {
        mList = datas;
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mList;
    }

    public int getItemPosition(T item) {
        return mList.indexOf(item);
    }
    /**
     * pisition位置的item是否位于可见区域
     */
    public boolean isVisibleItem(ListView listView, int position) {
        return position >= listView.getFirstVisiblePosition()
                && position <= listView.getLastVisiblePosition();
    }
}
