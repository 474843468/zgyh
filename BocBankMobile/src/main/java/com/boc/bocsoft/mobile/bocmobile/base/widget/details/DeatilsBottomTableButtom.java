package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 详情底部按钮
 * Created by niuguobin on 2016/6/3.
 */
public class DeatilsBottomTableButtom extends LinearLayout {

    private TextView tvDelete;

    private Context mContext;
    private View root_view;
    private String textTitle;
    private int textColor;
    private BtnCallback btnCallback;

    public DeatilsBottomTableButtom(Context mContext) {
        this(mContext, null, 0);
    }

    public DeatilsBottomTableButtom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeatilsBottomTableButtom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.detailsButton);
        textTitle = a.getString(R.styleable.detailsButton_textTitle);
        textColor = a.getInt(R.styleable.detailsButton_textColor, 0);
        a.recycle();
        initView();
    }

    private void initView() {
        root_view = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_bottom_table_button, this);
        tvDelete = (TextView) root_view.findViewById(R.id.tv_delete);
        tvDelete.setText(textTitle);
        //tvDelete.setTextColor(mContext.getResources().getColor(textColor));
        root_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCallback != null) {
                    btnCallback.onClickListener();
                }
            }
        });
    }

    /**
     * 设置文字和颜色
     * @param text
     * @param color
     */
    public DeatilsBottomTableButtom setButtonText(String text, int color) {
        tvDelete.setVisibility(VISIBLE);
        tvDelete.setText(text);
        tvDelete.setTextColor(color);
        return this;
    }

    /**
     * 改变文字
     * @param text
     */
    public DeatilsBottomTableButtom updateText(String text) {
        tvDelete.setVisibility(VISIBLE);
        tvDelete.setText(text);
        return this;
    }

    /**
     * 改变颜色
     * @param color
     */
    public DeatilsBottomTableButtom updateColor(int color) {
        tvDelete.setVisibility(VISIBLE);
        tvDelete.setTextColor(color);
        return this;
    }

    /**
     * 底部按钮回调函数
     */
    public interface BtnCallback {
        public void onClickListener();
    }

    /**
     * 底部按钮点击事件
     * @param btnCallback
     */
    public void setOnclick(BtnCallback btnCallback) {
        this.btnCallback = btnCallback;
    }

}
