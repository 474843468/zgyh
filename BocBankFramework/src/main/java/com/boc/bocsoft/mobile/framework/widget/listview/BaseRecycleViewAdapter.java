package com.boc.bocsoft.mobile.framework.widget.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/7.
 */
public abstract class BaseRecycleViewAdapter<T, A extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<A> {
    protected List<T> mList = new ArrayList<T>();

    protected LayoutInflater mInflater;

    protected Context mContext;

    private OnItemClickListener mOnItemClickListener;
    protected ListAdapterHelper.OnClickChildViewInItemItf<T> onClickChildViewInItemItf;

    public BaseRecycleViewAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
    }

    public void setOnClickChildViewInItemItf(
            ListAdapterHelper.OnClickChildViewInItemItf<T> onClickChildViewInItemItf) {
        this.onClickChildViewInItemItf = onClickChildViewInItemItf;
    }

    /**
     * 给列表项的某个子视图绑定点击监听器
     */
    protected void setOnClickChildViewInItemItf(int position, T item, View childView) {
        ListAdapterHelper.setOnClickChildViewInItemItf(position, item, childView,
                onClickChildViewInItemItf);
    }

    @Override
    public int getItemCount() {
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

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public int getItemPosition(T item) {
        return mList.indexOf(item);
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public BaseViewHolder(View itemView) {
            super(itemView);
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }
}
