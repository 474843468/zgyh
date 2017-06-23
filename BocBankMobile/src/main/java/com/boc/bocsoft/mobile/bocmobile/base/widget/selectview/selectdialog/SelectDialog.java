package com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selectdialog;

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
 * Created by wangtong on 2016/9/18.
 */
public class SelectDialog extends BaseDialog {

    protected TextView title;
    private View rootView;
    private ListView listView;
    protected SelectAdapter adapter;
    private OnItemSelectDialogClicked listener;

    public interface OnItemSelectDialogClicked {
        void onListItemClicked(int index);
    }

    public SelectDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext, R.layout.boc_purchase_select_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        listView = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new SelectAdapter();
        listView.setAdapter(adapter);
        title = (TextView) rootView.findViewById(R.id.title);
    }

    @Override
    protected void initData() {

    }

    public void setTitle(String titleString) {
        title.setVisibility(View.VISIBLE);
        title.setText(titleString);
    }

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

    public void setListener(OnItemSelectDialogClicked listener) {
        this.listener = listener;
    }

    /**
     * 列表适配器
     */
    public class SelectAdapter extends BaseAdapter {

        private List<String> data;

        public SelectAdapter() {
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
