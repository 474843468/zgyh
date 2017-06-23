package com.chinamworld.bocmbci.biz.finc.finc_p606.selectview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.llbt.userwidget.SlipMenu.SlipDrawerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 依据分类进行筛选
 * Created by liuweidong on 2016/5/23.
 */
public class SelectTypeView extends LinearLayout implements View.OnClickListener {

    public static final String SELECT_DEFAULT_ID = "10001";

    private Context mContext;
    private SlipDrawerLayout drawerLayout;

    private ListView listView;
    private ListViewAdapter adapter;
    private Button btnLeft;// 布局下方左边按钮
    private Button btnRight;// 布局下发右边按钮
    private TextView txtCancel;// 取消按钮
    private SelectListener listener;// 筛选的监听接口
    private List<SelectTypeData> list;// 默认的数据集合
    public static boolean isRadio;// 单选或多选
    public static int numColumns;// 列数
    public static int otherNumColumns;// 特殊item的列数 没有特殊列时传入任意数都可
    public static String otherNumColumnName;// 特殊item的名称 没有特殊列时传入“”
    private LinearLayout companyLayout;//基金公司布局
    private TextView companyTv;

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
        View view = View.inflate(mContext, R.layout.finc_select_type, this);
        listView = (ListView) view.findViewById(R.id.listview);
        btnLeft = (Button) view.findViewById(R.id.btn_left);
        btnRight = (Button) view.findViewById(R.id.btn_right);
        txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
        companyLayout = (LinearLayout) view.findViewById(R.id.company_layout);
        companyTv = (TextView) view.findViewById(R.id.company_tv);
        adapter = new ListViewAdapter(mContext);
        listView.setAdapter(adapter);
    }

    private void setListener(){
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        companyLayout.setOnClickListener(this);
    }

    public ListViewAdapter getAdapter(){
        return adapter;
    }

    /**
     * ListView设置数据
     *
     * @param list
     */
    public void setData(List<SelectTypeData> list,int otherNumColumns,String otherNumColumnName) {
        this.list = list;
        this.otherNumColumns = otherNumColumns;
        this.otherNumColumnName = otherNumColumnName;
        adapter.setData(list,otherNumColumns);
        adapter.notifyDataSetChanged();
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

    public ListView getListView(){
        return listView;
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
        } else if (i == R.id.txt_cancel) {// 取消
            drawerLayout.toggle();
        }else if(i == R.id.company_layout){//基金公司
            if (listener != null)
                listener.companyOnClick();
        }
    }

    private List<String> getSelectId() {
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
        companyTv.setText("全部");
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
     * 筛选的监听接口
     */
    public interface SelectListener {
        void onClick(List<String> selectIds);
        void companyOnClick();
    }
}
