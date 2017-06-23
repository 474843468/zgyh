package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * 输入框组件
 * Created by wangfan on 2016/5/24.
 */
public class EditClearWidget extends LinearLayout {

    /*** 输入框组件的标题*/
    private TextView mTitleTextView;
    /*** 输入框组件的EditText*/
    private ClearEditText mContentEditText;
    /*** 输入框组件左侧的ImageView*/
    private ImageView mClearEditImageView;
    /*** 输入框组件右侧的ImageView*/
    private ImageView mClearEditRightImageView;
    /*** 输入框组件右侧的TextView*/
    private TextView mClearEditRightTextView;

    private boolean isShowWidgetImage;
    private boolean isShowWidgetRightImage;
    private boolean isNameTextBold;
    private String strEditClearName;
    private String strEditClearHint;
    private int editClearImageId;
    private int editClearRightImageId;

    private EditRightTextViewOnClick rightTextViewOnClick;
    private EditRightImageOnClick rightImageOnClick;
    private EditClearWidgetOnFocusCallback onFocusCallback;
    private ClearEditTextWatcher clearEditTextWatcher;


    public EditClearWidget(Context context) {
        this(context,null);
    }

    public EditClearWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditClearWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    /**
     * 初始化页面控件
     */
    private void initView(Context context, AttributeSet attrs) {
        View contentView = View.inflate(getContext(), R.layout.boc_clear_edit_item, this);
        mTitleTextView = (TextView) contentView.findViewById(R.id.clear_edit_name);
        mContentEditText = (ClearEditText) contentView.findViewById(R.id.clear_edit_context);
        mClearEditImageView = (ImageView) contentView.findViewById(R.id.clear_edit_image);
        mClearEditRightImageView = (ImageView) contentView.findViewById(R.id.clear_edit_right_image);
        mClearEditRightTextView = (TextView) contentView.findViewById(R.id.clear_edit_right_text);

        initDefault();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.editClearWidget);
        isShowWidgetImage = a.getBoolean(R.styleable.editClearWidget_isShowWidgetImage, false);
        isShowWidgetRightImage = a.getBoolean(R.styleable.editClearWidget_isShowWidgetRightImage, false);
        isNameTextBold = a.getBoolean(R.styleable.editClearWidget_isNameTextBold, false);
        strEditClearName = a.getString(R.styleable.editClearWidget_editClearName);
        String hint = a.getString(R.styleable.editClearWidget_editClearHint);
        if (hint!=null) strEditClearHint=hint;
        editClearImageId = a.getResourceId(R.styleable.editClearWidget_editClearImage, 0);
        editClearRightImageId = a.getResourceId(R.styleable.editClearWidget_editClearRightImage, 0);
        a.recycle();

        setViewData();
        setViewListener();
    }

    private void initDefault() {
        strEditClearHint=getResources().getString(R.string.boc_common_input_hint);
    }

    /**
     * 设置View点击事件的监听
     */
    private void setViewListener() {
        mContentEditText.setClearEditTextFocusListener(new ClearEditText.ClearEditTextFocusCallBack() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusCallback != null){
                    onFocusCallback.onFocusChange(v, hasFocus);
                }
            }
        });

        mContentEditText.setClearEditTextWatcherListener(new ClearEditText.ClearEditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (clearEditTextWatcher != null){
                    clearEditTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (clearEditTextWatcher != null){
                    clearEditTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (clearEditTextWatcher != null){
                    clearEditTextWatcher.afterTextChanged(s);
                }
            }
        });


        mClearEditRightImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightImageOnClick == null){
                    return;
                }
                rightImageOnClick.onClick();
            }
        });

        mClearEditRightTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightTextViewOnClick != null){
                    rightTextViewOnClick.onClick(v);
                }
            }
        });
    }

    /**
     * 初始化页面数据，数据从自定义属性中获取
     */
    private void setViewData() {

        mContentEditText.setHint(strEditClearHint);
        if (isShowWidgetImage) {
            showEditWidgetImage(isShowWidgetImage);
            setClearEditImage(editClearImageId);
        } else {
            mTitleTextView.setText(strEditClearName);
        }
        if (isShowWidgetRightImage) {
            showEditWidgetRightImage(isShowWidgetRightImage);
            setClearEditRightImage(editClearRightImageId);
        }
        if (isNameTextBold) {
            mTitleTextView.getPaint().setFakeBoldText(true);
        }
    }


    // -------------------- 输入框组件 左侧图标  使用方法 start -------------------

    /**
     * 获取输入框组件 左侧图标 的ImageView
     *
     * @return
     */
    public ImageView getClearEditImageView() {
        return mClearEditImageView;
    }

    /**
     * 是否显示输入框组件 左侧图标
     *
     * @param isShowImage
     */
    public void showEditWidgetImage(boolean isShowImage) {
        if (isShowImage) {
            mTitleTextView.setVisibility(View.GONE);
            mClearEditImageView.setVisibility(View.VISIBLE);
        } else {
            mTitleTextView.setVisibility(View.VISIBLE);
            mClearEditImageView.setVisibility(View.GONE);
        }
    }

    /**
     * 判断输入框组件 左侧图标 是否显示出来
     *
     * @return
     */
    public boolean isShowEditWidgetImage() {
        if (View.VISIBLE == mClearEditImageView.getVisibility()) {
            return true;
        } else if (View.GONE == mClearEditImageView.getVisibility()) {
            return false;
        }
        return false;
    }

    /**
     * 设置输入框组件 左侧图标 的icon
     *
     * @param imageId
     */
    public void setClearEditImage(int imageId) {
        mClearEditImageView.setImageResource(imageId);
    }

    // -------------------- 输入框组件 左侧图标  使用方法 end -------------------
    // -------------------- 输入框组件 Title  使用方法 start -------------------

    /**
     * 设置Title是否加粗
     * @param isBold
     */
    public void setChoiceTitleBold(boolean isBold){
        mTitleTextView.getPaint().setFakeBoldText(isBold);
    }

    /**
     * 获取输入框组件 Title 的TextView
     *
     * @return
     */
    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    /**
     * 设置输入框组件的Title
     *
     * @param strTitle 组件的Title
     */
    public void setEditWidgetTitle(String strTitle) {
        if (strTitle == null) {
            return;
        }
        mTitleTextView.setText(strTitle);
    }

    /**
     * 获取输入框组件的Title
     *
     * @return 组件的Title
     */
    public String getEditWidgetTitle() {
        return mTitleTextView.getText().toString();
    }

    /**
     * 判断输入框组件的Title是否显示出来
     *
     * @return
     */
    public boolean isShowEditWidgetTitle() {
        if (View.VISIBLE == mTitleTextView.getVisibility()) {
            return true;
        } else if (View.GONE == mTitleTextView.getVisibility()) {
            return false;
        }
        return false;
    }

    // -------------------- 输入框组件 Title  使用方法 end -------------------


    // -------------------- 输入框组件 可输入内容部分  使用方法 start -------------------

    /**
     * 设置允许输入的最大数量
     * @param iAllNumPermit
     */
    public void setAllNumPermit(int iAllNumPermit){
        mContentEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(iAllNumPermit)});
    }

    /**
     * 获取输入框组件内容的EditText
     *
     * @return
     */
    public ClearEditText getContentEditText() {
        return mContentEditText;
    }

    /**
     * 设置输入框组件的内容
     *
     * @param strContent
     */
    public void setEditWidgetContent(String strContent) {
        if (strContent == null) {
            return;
        }
        mContentEditText.setText(strContent);
    }

    /**
     * 设置输入文字局右显示
     */
    public void setEditWidgetContentToRight() {
        mContentEditText.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
    }

    /**
     * 设置输入框组件的提示信息
     *
     * @param strHint
     */
    public void setEditWidgetHint(String strHint) {
        if (strHint == null) {
            return;
        }
        mContentEditText.setHint(strHint);
    }


    /**
     * 获取输入框组件的Content
     *
     * @return
     */
    public String getEditWidgetContent() {
        return mContentEditText.getText().toString();
    }

    // -------------------- 输入框组件 可输入内容部分  使用方法 end -------------------


    // -------------------- 输入框组件 右侧图标  使用方法 start -------------------

    /**
     * 获取输入框组件右侧图标的ImageView
     *
     * @return
     */
    public ImageView getClearEditRightImageView() {
        return mClearEditRightImageView;
    }

    /**
     * 是否显示输入框组件右侧的图标
     *
     * @param isShowImage
     */
    public void showEditWidgetRightImage(boolean isShowImage) {
        if (isShowImage) {
            mClearEditRightImageView.setVisibility(View.VISIBLE);
        } else {
            mClearEditRightImageView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置输入框组件右侧image的图片
     *
     * @param imageId
     */
    public void setClearEditRightImage(int imageId) {
        mClearEditRightImageView.setImageResource(imageId);
    }

    // -------------------- 输入框组件 右侧图标  使用方法 end -------------------
    // -------------------- 输入框组件 右侧TextView  使用方法 start -------------------

    /**
     * 设置输入框组件右侧TextView
     *
     * @param str
     */
    public void setClearEditRightTextView(String str) {
        if (str == null || str.isEmpty()){
            return;
        }
        mClearEditRightTextView.setText(str);
        mClearEditRightTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置输入框组件右侧TextView的颜色
     */
    public void setRightTextViewColor(int colorId){
        mClearEditRightTextView.setTextColor(getResources().getColor(colorId));
    }


    // -------------------- 输入框组件 右侧TextView  使用方法 end -------------------


    // --------------------------------- 其他功能 --------------------------------

    /**
     * 为右侧Textview设置点击事件的回调
     * @param textViewCallBack
     */
    public void setRightTextViewCallBack(EditRightTextViewOnClick textViewCallBack){
        this.rightTextViewOnClick = textViewCallBack;
    }

    /**
     * 为右侧图标设置点击事件的回调
     * @param imageCallBack
     */
    public void setRightImageCallBack(EditRightImageOnClick imageCallBack){
        this.rightImageOnClick = imageCallBack;
    }

    /**
     * 为输入框的焦点设置回调
     * @param listener
     */
    public void setEditClearWidgetOnFocusListener(EditClearWidgetOnFocusCallback listener){
        this.onFocusCallback = listener;
    }

    /**
     * 设置TextWatcher的回调
     * @param watcherListener
     */
    public void setClearEditTextWatcherListener(ClearEditTextWatcher watcherListener){
        this.clearEditTextWatcher = watcherListener;
    }

    /**
     * 点击事件的接口
     */
    public interface EditRightImageOnClick{
        void onClick();
    }

    /**
     * 点击事件的接口
     */
    public interface EditRightTextViewOnClick{
        void onClick(View v);
    }

    /**
     * 焦点变化的接口
     */
    public interface EditClearWidgetOnFocusCallback{
        void onFocusChange(View v, boolean hasFocus);
    }

    /**
     * TextWatcher的接口
     */
    public interface ClearEditTextWatcher{
        void onTextChanged(CharSequence s, int start, int before, int count);
        void beforeTextChanged(CharSequence s, int start, int count,int after);
        void afterTextChanged(Editable s);
    }

}
