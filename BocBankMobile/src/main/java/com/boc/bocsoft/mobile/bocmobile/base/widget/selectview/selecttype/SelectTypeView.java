package com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;

import java.util.ArrayList;
import java.util.List;

/**
 * 依据分类进行筛选
 * Created by liuweidong on 2016/5/23.
 */
public class SelectTypeView extends LinearLayout implements View.OnClickListener {

    public final static String DEFAULT_ID = "9527";

    private Context mContext;
    private SlipDrawerLayout drawerLayout;
    private ListView listView;
    private ListViewAdapter adapter;
    private Button btnLeft;// 布局下方左边按钮
    private Button btnRight;// 布局下发右边按钮
    private TextView txtCancel;// 取消按钮
    private LinearLayout llParent;
    private SelectListener listener;// 筛选的监听接口
    private List<SelectTypeData> list;// 默认的数据集合

    private LinearLayout llyChioiceArea; //请选择区域
    private EditChoiceWidget edtcwSelect; //请选择条目

    public static boolean isRadio;// 单选或多选
    public static int numColumns;// 列数

    public SelectTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        setListener();
    }

    public void setDrawerLayout(SlipDrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    /**
     * 初始化信息
     */
    private void initView() {
        View view = View.inflate(mContext, R.layout.boc_select_type, this);
        llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        listView = (ListView) view.findViewById(R.id.listview);
        btnLeft = (Button) view.findViewById(R.id.btn_left);
        btnRight = (Button) view.findViewById(R.id.btn_right);
        txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
        adapter = new ListViewAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setFocusable(false);
        llyChioiceArea = (LinearLayout)view.findViewById(R.id.llyChioiceArea);
        edtcwSelect = (EditChoiceWidget)view.findViewById(R.id.edtcwSelect);
    }

    private void setListener() {
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }

    public ListViewAdapter getAdapter(){
        return adapter;
    }

    public LinearLayout getParentView(){
        return llParent;
    }

    /**
     * ListView设置数据
     *
     * @param list
     */
    public void setData(List<SelectTypeData> list) {
        this.list = list;
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    public boolean isEmpty(){
        return list == null || list.isEmpty();
    }

    public void setSelectView(boolean isRadio, int numColumns) {
        this.isRadio = isRadio;
        this.numColumns = numColumns;
    }

    /**
     * 设置选择框的大小
     *
     * @param width
     * @param height
     */
    public void setSelectSize(boolean isUpdate, int width, int height) {
        adapter.setItemSize(isUpdate, width, height);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            if (listener != null)
                listener.onClick(getSelectId());
            drawerLayout.toggle();
        } else if (i == R.id.btn_left) {// 重置
            reset();
            if (listener != null)
                listener.resetClick();
        } else if (i == R.id.txt_cancel) {// 取消
            drawerLayout.toggle();
        }
    }

    public List<String> getSelectId() {
        List<String> selectList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getList().size(); j++) {
                //是否选中
                Content content = list.get(i).getList().get(j);
                if (content.getSelected()) {
                    selectList.add(content.getContentNameID());
                    if (isRadio)
                        break;
                }
            }
        }
        return selectList;
    }

    private void reset() {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getList().size(); j++) {
                Content content = list.get(i).getList().get(j);
                if (!isRadio) {
                    content.setSelected(false);
                    continue;
                }

                if (list.get(i).getDefaultId().equals(content.getContentNameID()))
                    content.setSelected(true);
                else
                    content.setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public String getDefaultId(int position){
        return list.get(position).getDefaultId();
    }

    /**
     * 筛选组件设置监听
     *
     * @param listener
     */
    public void setClickListener(SelectListener listener) {
        this.listener = listener;
    }

    /**
     * 显示请选择条目
     */
    public void showSelectArea(String title, String content){
        llyChioiceArea.setVisibility(View.VISIBLE);
        edtcwSelect.setChoiceTextName(title);
        edtcwSelect.setChoiceTextContent(content);
    }

    /**
     * 筛选的监听接口
     */
    public interface SelectListener {
        void onClick(List<String> selectIds);

        void resetClick();
    }
}
