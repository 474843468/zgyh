package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择弹窗（下面有个自定义，可以输入）
 * Created by zhx on 2016/11/09
 */
public class SelectedDialogWithSelfInput extends Dialog {

    private View rootView;
    private TextView txtTitle;
    private ListView listView;
    private CurencyAdapter adapter;
    private OnItemSelectDialogClicked listener;
    private EditText edit_content;
    private TextView tv_self_define;
    private List<String> mList = null;
    private Context mContext = null;

    public interface OnItemSelectDialogClicked {
        public void onListItemClicked(int index);
    }

    public interface OnBottomViewClick {
        public void onBottomViewClick(String str);
    }

    private OnBottomViewClick onBottomViewClick;

    public void setOnBottomViewClick(OnBottomViewClick onBottomViewClick) {
        this.onBottomViewClick = onBottomViewClick;
    }

    public EditText getEditContent() {
        return edit_content;
    }

    public SelectedDialogWithSelfInput(Context context) {
        super(context);
        mContext = context;

        //        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        rootView = View.inflate(context, R.layout.boc_purchase_select_dialog1, null);

        initView();
        setListener();
    }

    protected void initView() {
        txtTitle = (TextView) rootView.findViewById(R.id.title);
        listView = (ListView) rootView.findViewById(R.id.list_view);

        edit_content = (EditText) rootView.findViewById(R.id.edit_content);
        tv_self_define = (TextView) rootView.findViewById(R.id.tv_self_define);
        adapter = new CurencyAdapter();
        listView.setAdapter(adapter);
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
        this.mList = list;
        adapter.setData(list);
        show();

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setContentView(rootView);
    }

    protected void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    if (position < mList.size()) {
                        listener.onListItemClicked(position);
                        dismiss();
                    }
                }
            }
        });

        tv_self_define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_content.setVisibility(View.VISIBLE);
                if (onBottomViewClick != null) { // 当点击bottomView的时候，把bottomView中的值传回去
                    onBottomViewClick.onBottomViewClick(edit_content.getText().toString());
                }
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
