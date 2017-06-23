package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;


/**
 * 选择编辑组件
 * Created by wangfan on 2016/5/24.
 */
public class EditChoiceWidget extends RelativeLayout {

    /*** 选择item左侧的图标 */
    private ImageView mChoiceLeftIcon;
    /*** 选择item的名称*/
    private TextView mChoiceNameTextView;
    /*** 选择item的内容*/
    private SpannableString mChoiceContentTextView;
    /*** 选择item的右侧箭头*/
    private ImageView mChoiceWidgetArrowImageView;
    /*** 自动缩小字体的TextView*/
    private AutoFitTextView mChoiceContentAutoTextView;

    /*** 底部的线*/
    private View mChoiceBottomLine;
    /*** 属性中定义的item的名称 */
    private String strChoiceName;
    /*** 属性中定义的item的名称是否加粗*/
    private boolean isChoiceNameBold;
    /*** 属性中定义的item的左侧图标是否显示*/
    private boolean isVisibilityLeftIcon;
    /*** 属性中定义的item的左侧图标资源ID*/
    private int leftIconImageId;
    ;
    /*** 属性中定义的item的内容*/
    private String strChoiceContent;
    /*** 默认字体大小*/
    private float defaultTextSize;


    private View contentView;

    public EditChoiceWidget(Context context) {
        this(context, null);
    }

    public EditChoiceWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditChoiceWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

    }


    /**
     * 初始化页面控件
     */
    private void initView(Context context, AttributeSet attrs) {
        contentView = View.inflate(getContext(), R.layout.boc_choice_date_item, this);
        mChoiceLeftIcon = (ImageView) contentView.findViewById(R.id.choice_data_left_icon);
        mChoiceNameTextView = (TextView) contentView.findViewById(R.id.choice_data_name);
        mChoiceContentTextView = (SpannableString) contentView.findViewById(R.id.choice_data_context);
        mChoiceWidgetArrowImageView = (ImageView) contentView.findViewById(R.id.choice_data_arrow);
        mChoiceContentAutoTextView = (AutoFitTextView) contentView.findViewById(R.id.choice_data_auto_context);
        mChoiceBottomLine = contentView.findViewById(R.id.choice_line);
        mChoiceBottomLine.setVisibility(View.GONE);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.editChoiceWidget);
        strChoiceName = a.getString(R.styleable.editChoiceWidget_editChoiceName);
        strChoiceContent = a.getString(R.styleable.editChoiceWidget_editChoiceContent);
        isChoiceNameBold = a.getBoolean(R.styleable.editChoiceWidget_editChoiceNameBold, false);
        isVisibilityLeftIcon = a.getBoolean(R.styleable.editChoiceWidget_isVisibilityLeftIcon, false);
        leftIconImageId = a.getResourceId(R.styleable.editChoiceWidget_leftIconImageId, 0);
        a.recycle();

        initViewData();
    }

    /**
     * 初始化页面数据，数据从自定义属性中获得
     */
    private void initViewData() {
        mChoiceNameTextView.setText(strChoiceName);
        mChoiceContentTextView.setText(strChoiceContent);
        mChoiceNameTextView.getPaint().setFakeBoldText(isChoiceNameBold);

        setLeftIconVisibility(isVisibilityLeftIcon);
        if (isVisibilityLeftIcon) {
            setLeftIconImage(leftIconImageId);
        }
    }


    // -------------------- 选择组件 左侧图标  使用方法 start -------------------

    /**
     * 获取 左侧图标 的ImageView
     *
     * @return
     */
    public ImageView getLeftIconImageView() {
        return mChoiceLeftIcon;
    }

    /**
     * 设置左侧图标是否显示
     *
     * @param isVisibility
     */
    public void setLeftIconVisibility(boolean isVisibility) {
        if (isVisibility) {
            mChoiceLeftIcon.setVisibility(View.VISIBLE);
        } else {
            mChoiceLeftIcon.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左侧图标的icon
     *
     * @param imageId
     */
    public void setLeftIconImage(int imageId) {
        mChoiceLeftIcon.setImageResource(imageId);
    }

    // -------------------- 选择组件 左侧图标  使用方法 end -------------------

    // -------------------- 选择组件 左侧Title  使用方法 start -------------------

    /**
     * 获取 Title 的TextView
     *
     * @return
     */
    public TextView getChoiceNameTextView() {
        return mChoiceNameTextView;
    }

    /**
     * 设置 Title 的名称
     *
     * @param strName
     */
    public void setChoiceTextName(String strName) {
        if (strName == null) {
            return;
        }
        mChoiceNameTextView.setText(strName);
    }

    /**
     * 获取 Title 的名称
     *
     * @return
     */
    public String getChoiceTextName() {
        return mChoiceNameTextView.getText().toString();
    }

    /**
     * 设置 Title 是否加粗
     *
     * @param isBold
     */
    public void setChoiceTitleBold(boolean isBold) {
        mChoiceNameTextView.getPaint().setFakeBoldText(isBold);
    }

    /**
     * 设置标题为根据内容大小显示
     */
    public void setNameWidth() {
        ViewGroup.LayoutParams lp = mChoiceNameTextView.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        mChoiceNameTextView.setLayoutParams(lp);
    }

    // -------------------- 选择组件 左侧Title  使用方法 end -------------------

    // -------------------- 选择组件 内容区域  使用方法 start -------------------

    /**
     * 获取 Content 的TextView
     *
     * @return
     */
    public SpannableString getChoiceContentTextView() {
        return mChoiceContentTextView;
    }


    /**
     * 设置 Content 的内容
     *
     * @param strContent
     */
    public void setChoiceTextContent(String strContent) {
        if (strContent == null) {
            return;
        }
        mChoiceContentTextView.setText(strContent);
    }

    public void setChoiceTextColor(int color) {
        mChoiceContentTextView.setTextColor(color);
    }

    /**
     * 设置 Content 的提示内容
     *
     * @param hint
     */
    public void setChoiceTextContentHint(String hint) {
        if (hint == null) {
            return;
        }
        mChoiceContentTextView.setHintTextColor(getResources().getColor(R.color.boc_text_color_gray));
        mChoiceContentTextView.setHint(hint);
    }

    /**
     * 获取 Content 的内容
     *
     * @return
     */
    public String getChoiceTextContent() {
        return mChoiceContentTextView.getText().toString();
    }

    // -------------------- 选择组件 内容区域  使用方法 end -------------------

    // -------------------- 选择组件 右侧箭头  使用方法 start -------------------

    /**
     * 设置内容区域可显示的最大行数（多余部分用省略号显示）
     * @param maxLines
     */
    public void setChoiceContentMaxLines(int maxLines){
        mChoiceContentTextView.setMaxLines(maxLines);
    }


    // -------------------- 选择组件 内容区域  使用方法 end -------------------

    // -------------------- 选择组件 右侧箭头  使用方法 start -------------------

    /**
     * 获取 右侧箭头 的ImageView
     *
     * @return
     */
    public ImageView getChoiceWidgetArrowImageView() {
        return mChoiceWidgetArrowImageView;
    }

    /**
     * 隐藏右侧箭头
     */
    public void setArrowImageGone(boolean isVisible) {
        if (isVisible) {
            mChoiceWidgetArrowImageView.setVisibility(VISIBLE);
        } else {
            mChoiceWidgetArrowImageView.setVisibility(GONE);
        }
    }


    // -------------------- 选择组件 右侧箭头  使用方法 end -------------------

    // -------------------- 选择组件 可自动缩放字体的内容区域  使用方法 start -------------------


    /**
     * 设置是否显示自动缩放字体的TextView
     *
     * @param isVisibility
     */
    public void isVisibilityChoiceAutoTextView(boolean isVisibility) {
        if (isVisibility) {
            defaultTextSize = mChoiceContentAutoTextView.getTextSize();
            mChoiceContentAutoTextView.setVisibility(View.VISIBLE);
            mChoiceContentTextView.setVisibility(View.GONE);
        } else {
            mChoiceContentAutoTextView.setVisibility(View.GONE);
            mChoiceContentTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置自动缩放字体的TextView的内容
     *
     * @param strContent
     */
    public void setChoiceAutoContent(String strContent) {
        if (strContent == null) {
            return;
        }
        mChoiceContentAutoTextView.setDefaultTextSize(defaultTextSize);
        mChoiceContentAutoTextView.setText(strContent);
    }

    /**
     * 获取自动缩放字体的TextView的内容
     *
     * @return
     */
    public String getChoiceAutoTextContent() {
        return mChoiceContentAutoTextView.getText().toString();
    }

    /**
     * 获取 自动缩放 的TextView
     *
     * @return
     */
    public AutoFitTextView getChoiceAutoTextView() {
        return mChoiceContentAutoTextView;
    }

    // -------------------- 选择组件 可自动缩放字体的内容区域  使用方法 end -------------------

    // --------------------------------- 其他功能 --------------------------------


    /**
     * 设置底部的线是否显示
     *
     * @param isVisibility
     */
    public void setBottomLineVisibility(boolean isVisibility) {
        if (isVisibility) {
            mChoiceBottomLine.setVisibility(View.VISIBLE);
        } else {
            mChoiceBottomLine.setVisibility(View.GONE);
        }
    }


    /**
     * 设置条目背景颜色
     */
//    public void setBgColor(int colorId){
//        contentView.setBackgroundColor(colorId);
//    }




}
