package com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.List;


/**
 * 单选与多选组件
 * Created by liuweidong on 2016/5/25.
 */
public class SelectGridView extends GridView implements AdapterView.OnItemClickListener {
    private Context context;
    // 是否单选
    private boolean isRadio;
    // 适配器
    private GridViewAdapter adapter;
    // 数据集合
    private List<Content> list;
    // 事件监听
    private ClickListener listener;

    public SelectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = this.context.obtainStyledAttributes(attrs, R.styleable.SelectGridView);
        isRadio = typedArray.getBoolean(R.styleable.SelectGridView_radioTrue, false);
        typedArray.recycle();
        initView();
    }

    /**
     * 初始化信息
     */
    private void initView() {
        setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridViewAdapter(this.context);
        setAdapter(adapter);
        setOnItemClickListener(this);
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public GridViewAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置子条目的尺寸
     *
     * @param isUpdated
     * @param width
     * @param height
     */
    public void setItemSize(boolean isUpdated, int width, int height) {
        adapter.setItemSize(isUpdated, width, height);
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<Content> list) {
        this.list = list;
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isRadio) {
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    list.get(i).setSelected(true);
                } else {
                    list.get(i).setSelected(false);
                }
            }
        } else {
            if (position == 0 && ("全部").equals(list.get(0).getName())) {
                if (list.get(0).getSelected()) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelected(false);
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelected(true);
                    }
                }

            } else {
                list.get(position).setSelected(!list.get(position).getSelected());
            }
        }
        adapter.notifyDataSetChanged();
        if (listener != null) {
            listener.setItemClick(parent, view, position, id);
        }
    }

    public interface ClickListener {
        void setItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    /**
     * 设置组件的监听
     *
     * @param listener
     */
    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public List<Content> getList() {
        return list;
    }
}
