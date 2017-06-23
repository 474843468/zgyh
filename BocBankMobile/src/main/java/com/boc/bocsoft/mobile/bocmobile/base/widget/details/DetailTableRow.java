package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;


/**
 * 单条详情item,不带分组和按钮
 * Created by niuguobin on 2016/5/23.
 */
public class DetailTableRow extends LinearLayout {

    private TextView tvName;
    private TextView tvValue;
    private View vLine;
    private View benrLine;
    private String textName;
    private String textValue;
    private boolean fullLine = true;
    private boolean lineVisibility = true;

    LinearLayout rootView, llBody;
    // 用于给整个view设置背景颜色使用
    private LinearLayout mRootView;
    private Context mContext;

    public DetailTableRow(Context context) {
        this(context, null, 0);
    }

    public DetailTableRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }

    public String getTextName() {
        return tvName.getText().toString();
    }

    public String getTextValue() {
        return tvValue.getText().toString();
    }

    public DetailTableRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.detailsTextView);
        textName = a.getString(R.styleable.detailsTextView_textName);
        textValue = a.getString(R.styleable.detailsTextView_textValue);
        fullLine = a.getBoolean(R.styleable.detailsTextView_fullLine, false);
        lineVisibility = a.getBoolean(R.styleable.detailsTextView_lineVisibility, true);
        a.recycle();
        initView();
    }

    private void initView() {
        rootView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_table_row, this);
        llBody = (LinearLayout) rootView.findViewById(R.id.layout_body);
        mRootView = (LinearLayout) rootView.findViewById(R.id.root_view);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvValue = (TextView) rootView.findViewById(R.id.tv_value);
        vLine = rootView.findViewById(R.id.v_line);
        benrLine = rootView.findViewById(R.id.benr_line);
        if (lineVisibility) {
            if (fullLine) {
                benrLine.setVisibility(View.GONE);
                vLine.setVisibility(View.VISIBLE);
            } else {
                benrLine.setVisibility(View.VISIBLE);
                vLine.setVisibility(View.GONE);
            }
        } else {
            benrLine.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        }
        updateTitle(textName);
        updateValue(textValue);
    }

    /**
     * 改变详情标题
     *
     * @param name：左侧标题
     */
    public void updateTitle(String name) {
        tvName.setText(name);
    }

    /**
     * 改变详情标题
     *
     * @param name：左侧标题 可以自定义 标题的样式
     */
    public void updateTitle(SpannableString name) {
        tvName.setText(name);
    }

    /**
     * 改变详情内容
     *
     * @param value：右侧数据
     */
    public void updateValue(String value) {
        if (StringUtils.isEmptyOrNull(value)) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        tvValue.setText(value);
    }

    public void updateValueAndColor(String value, int textColor) {
        if (StringUtils.isEmptyOrNull(value)) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        tvValue.setTextColor(textColor);
        tvValue.setText(value);
    }

    /**
     * 改变详情内容
     *
     * @param value：右侧数据
     * @param isNullShow：是否空数据的时候显示view
     * @author yx
     * @date 2016年11月16日 18:49:59
     */
    public void updateValue(String value, boolean isNullShow) {
        if (!isNullShow) {
            if (StringUtils.isEmptyOrNull(value)) {
                setVisibility(GONE);
                return;
            }
        }

        setVisibility(VISIBLE);
        if (!StringUtils.isEmptyOrNull(value))
            tvValue.setText(value);
    }

    /**
     * @param value
     * @param textColor
     * @param isNullShow 字符为空的时候 view是否显示
     * @author yx
     * @date 2016年11月16日 18:49:59
     */
    public void updateValueAndColor(String value, int textColor, boolean isNullShow) {
        if (!isNullShow) {
            if (StringUtils.isEmptyOrNull(value)) {
                setVisibility(GONE);
                return;
            }
        }
        setVisibility(VISIBLE);
        tvValue.setTextColor(textColor);
        tvValue.setText(value);
    }

    /**
     * 改变详情内容,并设置底部线条Margin
     *
     * @param value
     * @param left
     * @param right
     */
    public void updateWithLineMargin(String value, int left, int right) {
        if (StringUtils.isEmptyOrNull(value)) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        setLineMargging(left, right);
        tvValue.setText(value);
    }

    /**
     * 改变详情标题和内容
     *
     * @param name
     * @param value
     */
    public void updateData(String name, String value) {
        updateTitle(name);
        updateValue(value);
    }

    /**
     * 改变详情标题和内容
     *
     * @param name
     * @param value
     * @param isNullShow 是否空字符的时候显示整行view
     * @author yx
     * @date 2016年11月16日 18:53:20
     */
    public void updateData(String name, String value, boolean isNullShow) {
        updateTitle(name);
        updateValue(value, isNullShow);
    }

    /**
     * 改变详情标题和内容 可以自定义 标题的样式
     *
     * @param name
     * @param value
     */
    public void updateData(SpannableString name, String value) {
        updateTitle(name);
        updateValue(value);
    }

    /**
     * 每一行下面的分割线是否显示
     * 默认显示
     *
     * @param isVisible
     */
    public void setRowLineVisable(boolean isVisible) {
        if (isVisible) {
            vLine.setVisibility(View.VISIBLE);
        } else {
            vLine.setVisibility(View.GONE);
        }
    }

    public void isShowDividerLine(boolean isVisible) {
        if (isVisible) {
            benrLine.setVisibility(View.VISIBLE);
        } else {
            benrLine.setVisibility(View.GONE);
        }
    }

    /**
     * 设置数字下面的分割线的左右margging值
     * 设置边距时调用
     *
     * @param left
     * @param right
     */
    public void setLineMargging(int left, int right) {
        LayoutParams lp = new LayoutParams(vLine.getLayoutParams());
        lp.setMargins(left, 0, right, 0);
        vLine.setVisibility(GONE);
        benrLine.setVisibility(VISIBLE);
    }

    /**
     * 仅显示分隔线，并设置其margin
     *
     * @param left
     * @param right
     */
    public void showDividerlineWithMargin(int left, int right) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) benrLine.getLayoutParams();
        lp.setMargins(left, 0, right, 0);
        vLine.setVisibility(GONE);
        benrLine.setVisibility(VISIBLE);
    }

    /**
     * 修改 值 的字体颜色
     *
     * @param color 颜色值
     */
    public void setValueColor(int color) {
        tvValue.setTextColor(mContext.getResources().getColor(color));
    }

    public void setBodyHeight(int height) {
        if (height > 0)
            llBody.getLayoutParams().height = height;
    }

    /**
     * 设置 view 的背景颜色
     *
     * @param mBackgroundColor
     * @author yx
     * @date 2016-09-24 16:24:41
     */
    public void setRootViewBackground(int mBackgroundColor) {
        mRootView.setBackgroundResource(mBackgroundColor);
    }

    public void setValueEllipsize(TextUtils.TruncateAt truncateAt) {
        tvValue.setEllipsize(truncateAt);
    }

}
