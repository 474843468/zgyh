package com.chinamworld.bocmbci.biz.finc.finc_p606.selectview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chinamworld.bocmbci.R;

import java.util.List;


/**
 * Created
 * <p>
 * <p>
 * 单选与多选组件
 */
public class SelectGridView2 extends GridView implements AdapterView.OnItemClickListener {
    private Context mContext;
    // 是否单选
    private boolean isRadio;
    // 适配器
    private GridViewAdapter adapter;
    // 数据集合
    private List<Content> list;

    public SelectGridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SelectGridView);
        isRadio = typedArray.getBoolean(R.styleable.SelectGridView_radioTrue, false);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridViewAdapter(mContext);
        setAdapter(adapter);
        setOnItemClickListener(this);
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
     * GridView设置数据
     *
     * @param list
     */
    public void setData(List<Content> list) {
        this.list = list;
        isRadio = SelectTypeView.isRadio;
        setNumColumns(SelectTypeView.numColumns);
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
    /**
     * GridView设置数据
     *
     * @param list
     */
    public void setData2(List<Content> list) {
        this.list = list;
        isRadio = SelectTypeView.isRadio;
        setNumColumns(SelectTypeView.otherNumColumns);
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
            if (position == 0) {
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
    }

}
