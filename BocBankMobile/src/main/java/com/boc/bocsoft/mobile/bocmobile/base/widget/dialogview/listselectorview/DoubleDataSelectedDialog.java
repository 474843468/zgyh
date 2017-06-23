package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 两列数据选择对话框
 * Created by wangtong on 2016/10/26.
 */
public class DoubleDataSelectedDialog extends BaseDialog {

    private View rootView;
    private RadioButton btn_left;
    private RadioButton btn_right;
    private ListView listView;
    private CurencyAdapter adapter;
    private OnItemSelectDialogClicked listener;
    private List<String> leftList;
    private List<String> rightList;
    private int leftOrRight = 0;


    public interface OnItemSelectDialogClicked {
        /**
         * 当条目点击时调用
         * @param leftOrRight 0表示左边,1表示右边
         * @param index
         */
        public void onListItemClicked(int leftOrRight, int index);
    }

    public DoubleDataSelectedDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext, R.layout.boc_double_data_select_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        btn_left = (RadioButton) rootView.findViewById(R.id.btn_left);
        btn_right = (RadioButton) rootView.findViewById(R.id.btn_right);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new CurencyAdapter();
        listView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
    }

    /**
     * 设置标题
     * @param leftTitle 左标题
     * @param rightTitle 右标题
     */
    public void setTitle(String leftTitle, String rightTitle){
        btn_left.setText(leftTitle);
        btn_right.setText(rightTitle);
    }

    /**
     * 显示对话框
     * @param leftList
     * @param rightList
     */
    public void showDialog(List<String> leftList, List<String> rightList) {
        this.leftList = leftList;
        this.rightList = rightList;
        adapter.setData(leftList);
        show();
    }

    @Override
    protected void setListener() {
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftOrRight = 0;
                adapter.setData(leftList);
                adapter.notifyDataSetChanged();
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftOrRight = 1;
                adapter.setData(rightList);
                adapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onListItemClicked(leftOrRight, position);
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
