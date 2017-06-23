package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 有列表的Dialog
 *
 * @param <T>
 * @author gwluo
 */
public class SimpleListViewDialog<T> extends TitleAndBtnDialog {
    private Context mContext;
    /***
     * listview
     */
    protected ListView mListView;
    /**
     * 当前布局view
     */
    protected View contentView;
    /**
     * 单选列表按钮 回调监听
     */
    protected OnSelectListener<T> onSelectListener;

    /**
     * @param context
     */
    public SimpleListViewDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View getContentView() {
        contentView = inflateView(R.layout.boc_dialog_listview);
        mListView = (ListView) contentView.findViewById(R.id.listview);

        return contentView;
    }

    private List<T> mData;

    public void setData(List<T> data) {
        mData = data;
        String key = "name";
        List<Map<String, T>> list = new ArrayList<>();
        for (T item : data) {
            Map<String, T> map = new LinkedHashMap<>();
            map.put(key, item);
            list.add(map);
        }
        String[] from = new String[]{key};
        int[] to = new int[]{R.id.tv_item_name};
        mListView.setAdapter(new SimpleAdapter(mContext, list, R.layout.boc_dialog_list_item, from, to));
    }

    @Override
    protected void setListener() {
        super.setListener();
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (onSelectListener != null) {
                    onSelectListener.onItemClick(position, mData.get(position));
                }
            }
        });
    }

    /**
     * 设置单选列表按钮 点击事件
     *
     * @param onSelectListener
     */
    public void setOnSelectListener(
            OnSelectListener<T> onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    /**
     * 选择一条的回调
     */
    public interface OnSelectListener<T> {
        void onItemClick(int position, T model);
    }
}
