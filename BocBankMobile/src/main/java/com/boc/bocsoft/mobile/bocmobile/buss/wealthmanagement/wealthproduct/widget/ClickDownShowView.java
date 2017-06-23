package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 下拉展示
 * Created by liuweidong on 2016/9/26.
 */
public class ClickDownShowView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private View rootView;
    private RelativeLayout rlParent;// 接收点击事件区域
    private TextView txtName;
    private ImageView img;
    private LinearLayout llContent;// 内容区容器
    private boolean isShow = true;// 是否显示内容区
    private String name;// 分类名称
    private String[] names;
    private String[] values;

    public ClickDownShowView(Context context) {
        this(context, null);
    }

    public ClickDownShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickDownShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ClickDownShowView);
        name = typedArray.getString(R.styleable.ClickDownShowView_txtName);
        typedArray.recycle();
        initView();
        initData();
        setListener();
    }

    private void initView() {
        rootView = View.inflate(mContext, R.layout.boc_view_click_down_show, this);
        rlParent = (RelativeLayout) rootView.findViewById(R.id.rl_parent);
        txtName = (TextView) rootView.findViewById(R.id.txt_name);
        img = (ImageView) rootView.findViewById(R.id.img);
        llContent = (LinearLayout) rootView.findViewById(R.id.ll_parent_content);
    }

    private void initData() {
        txtName.setText(name);
    }

    private void setListener() {
        rlParent.setOnClickListener(this);
    }

    /**
     * 设置数据
     *
     * @param names
     * @param values
     */
    public void setData(String[] names, String[] values) {
        this.names = names;
        this.values = values;
    }

    /**
     * 页面展示数据
     */
    private void showData() {
        llContent.removeAllViews();
        if (names == null) {
            return;
        }
        for (int i = 0; i < names.length; i++) {
            View view = View.inflate(mContext, R.layout.boc_fragment_list_horizontal_item, null);
            TextView txtName = (TextView) view.findViewById(R.id.txt_key);
            TextView txtValue = (TextView) view.findViewById(R.id.txt_value);
            txtName.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            txtValue.setTextAppearance(mContext, R.style.BocTextViewSmall);
            txtName.setText(names[i]);
            txtValue.setText(values[i]);
            llContent.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (isShow) {
            isShow = false;
            llContent.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.boc_view_icon_current_close);
            showData();
        } else {
            isShow = true;
            llContent.setVisibility(View.GONE);
            img.setImageResource(R.drawable.boc_view_icon_current_open);
        }
    }
}
