package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择弹窗
 * Created by wangtong on 2016/10/26.
 */
public class SelectedDialogNew extends BaseDialog {

    private View rootView;
    private TextView txtTitle;
    private ListView listView;
    private CurencyAdapter adapter;
    private OnItemSelectDialogClicked listener;

    public interface OnItemSelectDialogClicked {
        public void onListItemClicked(int index);
    }

    public SelectedDialogNew(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext, R.layout.boc_purchase_select_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) rootView.findViewById(R.id.title);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new CurencyAdapter();
        listView.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    /**
     * 设置弹窗标题值
     *
     * @param value
     */
    public void setTitle(String value) {
        txtTitle.setText(value);
    }

    /**
     * 显示对话框
     *
     * @param list
     */
    public void showDialog(List<String> list) {
        adapter.setData(list);
        show();
    }

    @Override
    protected void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onListItemClicked(position);
                }
                dismiss();
            }
        });
    }

    /**
     * 设置监听回调
     *
     * @param listener
     */
    public void setListener(OnItemSelectDialogClicked listener) {
        this.listener = listener;
    }

    /**
     * 列表适配器
     */
    class CurencyAdapter extends BaseAdapter {

        private List<String> data;

        public CurencyAdapter() {
            data = new ArrayList<>();
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getContext(), R.layout.boc_item_security_type, null);
            TextView textView = (TextView) convertView.findViewById(R.id.item_name);
            textView.setText(data.get(position));
            return convertView;
        }
    }
}
