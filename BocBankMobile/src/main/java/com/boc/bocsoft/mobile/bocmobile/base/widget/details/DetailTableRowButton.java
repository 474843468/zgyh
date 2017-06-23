package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * 详情组件item项，带右侧按钮
 * Created by niuguobin on 2016/5/23.
 */
public class DetailTableRowButton extends LinearLayout {

    protected TextView tvName;
    protected TextView tvValue;
    protected TextView tvBtn;
    protected ImageView ivBtn;
    protected LinearLayout llBtn, llContent;
    private View viewLine;

    private BtnCallback btnCallback;

    public DetailTableRowButton(Context context) {
        this(context, null, 0);
    }

    public DetailTableRowButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailTableRowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.boc_view_detail_table_row_btn, this);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvValue = (TextView) view.findViewById(R.id.tv_value);
        tvBtn = (TextView) view.findViewById(R.id.tv_btn);
        ivBtn = (ImageView) view.findViewById(R.id.iv_btn);
        llBtn = (LinearLayout) view.findViewById(R.id.ll_btn);
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);

        // 分隔线
        viewLine = view.findViewById(R.id.view_line);
    }

    /**
     * 改变组件内容
     *
     * @param name
     * @param value
     */
    public void updateData(String name, String value) {
        tvName.setText(name);
        tvValue.setText(value);
    }

    /**
     * @param value
     */
    public void setTxtValue(String value) {
        tvValue.setText(value);
    }

    /**
     * 隐藏分隔线
     */
    public void hideDividerLine() {
        viewLine.setVisibility(View.GONE);
    }

    /**
     * 添加详情item,显示文字按钮
     *
     * @param title
     * @param value
     * @param text
     */
    public void addTextBtn(String title, String value, String text) {
        addTextBtn(title, value, text, 0, null);
    }

    /**
     * 添加详情item,显示文字按钮
     *
     * @param title:左侧详情标题
     * @param value:右侧详情数据
     * @param text:按钮文字
     * @param textColor:按钮文字颜色
     */
    public void addTextBtn(String title, String value, String text, int textColor) {
        addTextBtn(title, value, text, textColor, null);
    }

    /**
     * 添加详情item,显示文字按钮
     *
     * @param title:左侧详情标题
     * @param value:右侧详情数据
     * @param text:按钮文字
     * @param textColor:按钮文字颜色
     * @param btnLayoutParams:文字按钮LayoutParams
     */
    public void addTextBtn(String title, String value, String text, int textColor, LayoutParams btnLayoutParams) {
        ivBtn.setVisibility(View.GONE);
        if (btnLayoutParams != null && ResUtils.getScreenWidth(getContext()) <= 480)
            tvBtn.setLayoutParams(btnLayoutParams);
        tvBtn.setVisibility(View.VISIBLE);
        tvName.setText(title);
        tvValue.setText(value);
        if (textColor != 0)
            tvBtn.setTextColor(textColor);
        tvBtn.setText(text);
        llBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCallback != null) {
                    btnCallback.onClickListener();
                }
            }
        });
    }

    /**
     * 添加详情item,显示图片按钮
     *
     * @param title：左侧详情标题
     * @param value：右侧详情数据
     * @param bgImg：图片按钮背景图，,图片来自Drawable
     */
    public void addImgBtn(String title, String value, int bgImg) {
        setVisibility(VISIBLE);
        tvBtn.setVisibility(View.GONE);
        ivBtn.setVisibility(View.VISIBLE);
        tvName.setText(title);
        tvValue.setText(value);
        ivBtn.setImageResource(bgImg);
        llBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCallback != null)
                    btnCallback.onClickListener();
            }
        });
    }

    /**
     * 添加详情item，文字按钮和图片按钮同时显示
     *
     * @param title：左侧详情标题
     * @param value：右侧详情数据
     * @param text：按钮文字
     * @param textColor：文字按钮字体颜色
     * @param textBgColor：文字按钮背景颜色
     * @param bgImg：图片按钮背景图，,图片来自Drawable
     */
    public void addTextAndImgBtn(String title, String value, String text, int textColor, int textBgColor, int bgImg) {
        tvBtn.setVisibility(View.VISIBLE);
        ivBtn.setVisibility(View.VISIBLE);
        tvName.setText(title);
        tvValue.setText(value);
        tvBtn.setBackgroundColor(textBgColor);
        if (textColor != 0)
            tvBtn.setTextColor(textColor);
        tvBtn.setText(text);
        tvBtn.setBackgroundColor(textColor);
        ivBtn.setImageResource(bgImg);
    }

    /**
     * 文字按钮回调函数
     */
    public interface BtnCallback {
        void onClickListener();
    }

    /**
     * 文字按钮点击事件
     *
     * @param btnCallback
     */
    public void setOnclick(BtnCallback btnCallback) {
        this.btnCallback = btnCallback;
    }

    /**
     * 设置数字下面的分割线的左右margging值
     * 设置边距时调用
     *
     * @param left
     * @param right
     */
    public void setDividerMargin(int left, int right) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewLine.getLayoutParams();
        lp.setMargins(left, 0, right, 0);
        viewLine.setVisibility(VISIBLE);
    }

    public void setBodyHeight(int height) {
        if (height > 0)
            llContent.getLayoutParams().height = height;
    }

    public void clearValueWeight() {
        LinearLayout.LayoutParams params = ((LayoutParams) tvValue.getLayoutParams());
        params.width = LayoutParams.WRAP_CONTENT;
        params.weight = -1;
        tvValue.setLayoutParams(params);
    }
}
