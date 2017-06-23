package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 输入框组件 - 金额键盘
 * Created by wangfan on 2016/5/24.
 */
public class EditMoneyInputWidget extends LinearLayout
    implements TextWatcher, View.OnClickListener {

  /**** 输入框组件的标题 */
  private TextView mTitleTextView;
  /*** 输入框组件左侧的ImageView */
  private ImageView mClearEditImageView;
  /*** 输入框组件的EditText */
  private MoneyInputTextView mContentMoneyEditText;
  /**** 删除图标 */
  private TextView editMoneyDelImage;
  /**** 输入组件右侧TextView 文字 */
  private TextView clearEditMoneyChange;
  /*** 输入框组件右侧的ImageView */
  private ImageView mEditMoneyRightImageView;
  /*** 组件底部分割线 */
  private View mWidgetBottomLine;
  /**** 属性中定义的item的名称 */
  private String strChoiceName;
  /**** 属性中定义的item的名称是否加粗 */
  private boolean isChoiceNameBold;
  /*** 属性中定义的item的内容 */
  private String strChoiceContent;

  private KeyBoardDismissOrShowCallBack dismissOrShowCallBack;

  public Boolean isCanUse = false;

  /**
   * 是否显示右侧文字信息
   */
  private boolean isShowRightText;

  public EditMoneyInputWidget(Context context) {
    this(context, null);
  }

  public EditMoneyInputWidget(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public EditMoneyInputWidget(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context, attrs);
  }

  /**
   * 初始化页面控件
   */
  protected void initView(Context context, AttributeSet attrs) {

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.editChoiceWidget);
    strChoiceName = a.getString(R.styleable.editChoiceWidget_editChoiceName);
    strChoiceContent = a.getString(R.styleable.editChoiceWidget_editChoiceContent);
    isChoiceNameBold = a.getBoolean(R.styleable.editChoiceWidget_editChoiceNameBold, false);
    int left = a.getInt(R.styleable.editChoiceWidget_moneyInputMaxLeftNum, 13);
    int right = a.getInt(R.styleable.editChoiceWidget_moneyInputMaxRightNum, 2);
    a.recycle();

    View contentView = View.inflate(getContext(), R.layout.boc_clear_edit_moneyinput_item, this);
    mTitleTextView = (TextView) contentView.findViewById(R.id.clear_edit_money_name);
    mClearEditImageView = (ImageView) contentView.findViewById(R.id.clear_edit_money_image);
    mContentMoneyEditText =
        (MoneyInputTextView) contentView.findViewById(R.id.clear_edit_money_context);
    editMoneyDelImage = (TextView) contentView.findViewById(R.id.clear_edit_money_del_image);
    clearEditMoneyChange = (TextView) contentView.findViewById(R.id.clear_edit_money_change);
    mEditMoneyRightImageView = (ImageView) contentView.findViewById(R.id.clear_edit_right_image);
    mWidgetBottomLine = contentView.findViewById(R.id.choice_line);
    mWidgetBottomLine.setVisibility(View.GONE);

    mClearEditImageView.setVisibility(View.GONE);
    clearEditMoneyChange.setVisibility(View.GONE);
    mEditMoneyRightImageView.setVisibility(View.GONE);

    Drawable mClearDrawable = getResources().getDrawable(R.drawable.boc_btn_delete);
    mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth() + 10,
        mClearDrawable.getIntrinsicHeight() + 10);
    editMoneyDelImage.setCompoundDrawables(mClearDrawable,
        editMoneyDelImage.getCompoundDrawables()[1], editMoneyDelImage.getCompoundDrawables()[2],
        editMoneyDelImage.getCompoundDrawables()[3]);

    mContentMoneyEditText.addTextChangedListener(this);
    if (!isInEditMode()) {
      mContentMoneyEditText.setMaxLeftNumber(left);
      mContentMoneyEditText.setMaxRightNumber(right);
    }
    editMoneyDelImage.setOnClickListener(this);

    initViewData();
  }

  /**
   * 初始化页面数据，数据从属性中来
   */
  private void initViewData() {
    mTitleTextView.setText(strChoiceName);
    mTitleTextView.getPaint().setFakeBoldText(isChoiceNameBold);
    mContentMoneyEditText.setText(strChoiceContent);
    mContentMoneyEditText.setHint(getResources().getString(R.string.boc_common_input_hint));
    if (isInEditMode()) return;
    mContentMoneyEditText.setOnKeyBoardDismiss(new MoneyInputDialog.KeyBoardListener() {
      @Override public void onKeyBoardDismiss() {
        if (mContentMoneyEditText.getInputMoney().length() > 0) {
          setClearIconVisible(true);
        } else {
          setClearIconVisible(false);
        }
        if (dismissOrShowCallBack != null) {
          dismissOrShowCallBack.onKeyBoardDismiss();
        }
        if (inputCompleteListener != null) {
          inputCompleteListener.InputComplete(getContentMoney());
        }
      }

      @Override public void onKeyBoardShow() {
        setClearIconVisible(false);
        if (dismissOrShowCallBack != null) {
          dismissOrShowCallBack.onKeyBoardShow();
        }
      }
    });
  }

  // -------------------- 金额输入框 标题  使用方法 开始 -------------------

  /**
   * 设置金额输入组件的Title是否显示
   */
  public void setTitleTextViewVisibility(boolean isVisibility) {
    mTitleTextView.setVisibility(isVisibility ? VISIBLE : GONE);
  }

  /**
   * 判断金额输入框组件的Title是否显示出来
   */
  public boolean isShowEditWidgetTitle() {
    if (View.VISIBLE == mTitleTextView.getVisibility()) {
      return true;
    } else if (View.GONE == mTitleTextView.getVisibility()) {
      return false;
    }
    return false;
  }

  /**
   * 获取金额输入框组件标题的TextView
   */
  public TextView getTitleTextView() {
    return mTitleTextView;
  }

  /**
   * 设置金额输入框组件的Title
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
   * 获取金额输入框组件的Title
   *
   * @return 组件的Title
   */
  public String getEditWidgetTitle() {
    return mTitleTextView.getText().toString();
  }

  /**
   * 设置金额输入框组件的Title是否加粗
   */
  public void setTitleTextBold(boolean isBold) {
    mTitleTextView.getPaint().setFakeBoldText(isBold);
  }

  /**
   * 设置金额输入框组件的Title的文字颜色
   */
  public void setTitleTextColor(int colorId) {
    mTitleTextView.setTextColor(colorId);
  }

  // -------------------- 金额输入框 标题  使用方法 结束 -------------------

  // -------------------- 金额输入框 左侧图标  使用方法 开始 -------------------

  /**
   * 获取金额输入框组件图标的ImageView
   */
  public ImageView getClearEditImageView() {
    return mClearEditImageView;
  }

  /**
   * 是否显示金额输入框组件的图标
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
   * 判断金额输入框组件的image是否显示出来
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
   * 设置金额输入框组件image的图片
   */
  public void setClearEditImage(int imageId) {
    mClearEditImageView.setImageResource(imageId);
  }

  // -------------------- 金额输入框 左侧图标  使用方法 结束 -------------------

  // -------------------- 金额输入框 可输入内容View  使用方法 开始 -------------------

  /**
   * 获取金额输入框组件内容的EditText
   */
  public MoneyInputTextView getContentMoneyEditText() {
    return mContentMoneyEditText;
  }

  /**
   * 获取输入的金额
   */
  public String getContentMoney() {
    return mContentMoneyEditText.getInputMoney();
  }

  /**
   * 获取输入的金额
   */
  public BigDecimal getMoney() {
    if (StringUtils.isEmptyOrNull(mContentMoneyEditText.getInputMoney())) {
      return new BigDecimal(0.00);
    }

    return new BigDecimal(mContentMoneyEditText.getInputMoney());
  }

  /**
   * 设置输入框的初始数据
   */
  public void setmContentMoneyEditText(String money) {
    mContentMoneyEditText.setInputMoney(money);
  }

  /**
   * 设置输入框的提示信息
   */
  public void setContentHint(String str) {
    if (null == str || str.isEmpty()) {
      return;
    }
    mContentMoneyEditText.setHint(str);
  }

  /**
   * 设置金额输入组件的金额输入框的边距
   */
  public void setMoneyEditTextHaveMarginLeft(boolean haveMarginLeft) {
    if (!haveMarginLeft) {
      mContentMoneyEditText.setPadding(0, 0, 0, 0);
    }
  }

  /**
   * 　设置左边金额最大位数
   */
  public void setMaxLeftNumber(int maxLeftNumber) {
    mContentMoneyEditText.setMaxLeftNumber(maxLeftNumber);
  }

  /**
   * 　设置右边边金额最大位数
   */
  public void setMaxRightNumber(int maxRightNumber) {
    mContentMoneyEditText.setMaxRightNumber(maxRightNumber);
  }

  /**
   * 设置币种
   */
  public void setCurrency(String currency) {
    mContentMoneyEditText.setCurrency(currency);
  }

  /**
   * 设置金额输入组件内容的显示位置
   *
   * @param isLocationMarginLeft true 显示居左显示；  false  居右显示
   * @date 2016年11月7日 13:45:33
   * @author yx
   */
  public void setMoneyEditTextShowLocation(boolean isLocationMarginLeft) {
    if (!isLocationMarginLeft) {
      mContentMoneyEditText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    } else {
      mContentMoneyEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }
  }
  // ----------------------- 金额输入框 可输入内容View  使用方法 结束 ----------------------

  // ----------------------- 金额输入框 右侧TextView  使用方法 开始 ----------------------

  /**
   * 设置金额输入组件 右侧TextView 是否显示
   */
  public void setRightTextViewVisibility(boolean isVisibility) {
    clearEditMoneyChange.setVisibility(isVisibility ? VISIBLE : GONE);
  }

  /**
   * 获取金额输入框组件 右侧TextView 控件
   */
  public TextView getRightTextView() {
    return clearEditMoneyChange;
  }

  /**
   * 设置金额输入框组件 右侧TextView 的内容
   */
  public void setRightTextViewText(String strText) {
    if (strText == null) {
      return;
    }
    setRightTextViewVisibility(true);
    clearEditMoneyChange.setText(strText);
  }

  /**
   * 设置金额输入框组件 右侧文字信息 是否加粗
   */
  public void setRightTextBold(boolean isBold) {
    clearEditMoneyChange.getPaint().setFakeBoldText(isBold);
  }

  /**
   * 设置金额输入框组件 右侧文字信息 文字大小
   */
  public void setRightTextSize(float size) {
    clearEditMoneyChange.setTextSize(size);
  }

  /**
   * 设置金额输入框组件 右侧文字信息 的颜色
   */
  public void setRightTextColor(int colorId) {
    clearEditMoneyChange.setTextColor(colorId);
  }

  /**
   * 设置金额输入框组件 右侧文字信息 的点击事件
   */
  public void setRightTextViewOnClick(final RightTextClickListener rightTextClickListener) {
    clearEditMoneyChange.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (rightTextClickListener != null) {
          rightTextClickListener.onRightClick(v);
        }
      }
    });
  }

  /**
   * 设置右侧文字显示 - 借记卡交易限额的功能需要
   */
  public void setShowRightText(boolean isShowRightText,
      final RightViewClickListener clickListener) {
    this.isShowRightText = isShowRightText;
    if (!isShowRightText) {
      clearEditMoneyChange.setVisibility(GONE);
      return;
    }

    clearEditMoneyChange.setVisibility(View.VISIBLE);
    clearEditMoneyChange.setText("更改");
    mContentMoneyEditText.setClickable(false);
    clearEditMoneyChange.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if ("更改".equals(clearEditMoneyChange.getText().toString())) {
          clearEditMoneyChange.setText("取消");
          mContentMoneyEditText.setClickable(true);
          if (mContentMoneyEditText.getInputMoney().toString().length() > 0) {
            setClearIconVisible(true);
          } else {
            setClearIconVisible(false);
          }
        } else if ("取消".equals(clearEditMoneyChange.getText().toString())) {
          clearEditMoneyChange.setText("更改");
          mContentMoneyEditText.setClickable(false);
          setClearIconVisible(false);
        }

        if (clickListener != null) {
          clickListener.onRightClick("取消".equals(clearEditMoneyChange.getText().toString()));
        }
      }
    });
  }

  // ----------------------- 金额输入框 右侧TextView  使用方法 结束 ----------------------

  // -------------------- 金额输入框 右侧图标  使用方法 start -------------------

  /**
   * 获取金额输入框右侧图标的ImageView
   */
  public ImageView getEditMoneyRightImageView() {
    return mEditMoneyRightImageView;
  }

  /**
   * 是否显示输入框组件右侧的图标
   */
  public void showEditMoneyRightImageView(boolean isShowImage) {
    if (isShowImage) {
      mEditMoneyRightImageView.setVisibility(View.VISIBLE);
    } else {
      mEditMoneyRightImageView.setVisibility(View.GONE);
    }
  }

  /**
   * 设置金额输入框右侧image的图片
   */
  public void setEditMoneyRightImage(int imageId) {
    mEditMoneyRightImageView.setImageResource(imageId);
  }

  /**
   * 设置金额输入框右侧image图片的点击事件
   */
  public void setRightImageViewOnClick(final RightImageViewClickListener rightImageViewOnClick) {
    mEditMoneyRightImageView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (rightImageViewOnClick != null) {
          rightImageViewOnClick.onRightImageClick(v);
        }
      }
    });
  }

  // -------------------- 输入框组件 右侧图标  使用方法 end -------------------

  // --------------------------------- 其他功能 --------------------------------

  /**
   * 设置清除图标的显示与隐藏
   */
  public void setClearIconVisible(boolean visible) {
    if (visible) {
      editMoneyDelImage.setVisibility(View.VISIBLE);
    } else {
      editMoneyDelImage.setVisibility(View.GONE);
    }
  }

  /**
   * 设置底部的分割线是否显示
   */
  public void setBottomLineVisibility(boolean isVisibility) {
    if (isVisibility) {
      mWidgetBottomLine.setVisibility(View.VISIBLE);
    } else {
      mWidgetBottomLine.setVisibility(View.GONE);
    }
  }

  public void setScrollView(View scrollView) {
    mContentMoneyEditText.setScrollView(scrollView);
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    if (moneyInputTextWatcher != null) {
      moneyInputTextWatcher.beforeTextChanged(s, start, count, after);
    }
  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    if (s.length() == 0) {
      setClearIconVisible(false);
    }
    if (mMoneyChangeListenerInterface != null) {
      mMoneyChangeListenerInterface.setOnMoneyChangeListener(s.toString());
    }
    if (moneyInputTextWatcher != null) {
      moneyInputTextWatcher.onTextChanged(s, start, before, count);
    }
  }

  @Override public void afterTextChanged(Editable s) {
    if (moneyInputTextWatcher != null) {
      moneyInputTextWatcher.afterTextChanged(s);
    }
  }

  @Override public void onClick(View v) {
    mContentMoneyEditText.clearText();
    if (inputCompleteListener != null) {
      inputCompleteListener.InputComplete(getContentMoney());
    }
  }

  /**
   * 绑定键盘消失与显示的监听
   */
  public void setOnKeyBoardListener(KeyBoardDismissOrShowCallBack listener) {
    dismissOrShowCallBack = listener;
  }

  //键盘消失与显示的监听
  public interface KeyBoardDismissOrShowCallBack {
    void onKeyBoardDismiss();

    void onKeyBoardShow();
  }

  public interface MoneyChangeListenerInterface {
    void setOnMoneyChangeListener(String str);
  }

  private MoneyChangeListenerInterface mMoneyChangeListenerInterface;

  public void setMoneyChangeListener(MoneyChangeListenerInterface mMoneyChangeListenerInterface) {
    this.mMoneyChangeListenerInterface = mMoneyChangeListenerInterface;
  }

  public interface RightImageViewClickListener {
    void onRightImageClick(View v);
  }

  public interface RightViewClickListener {
    void onRightClick(boolean isUpdateMode);
  }

  public interface RightTextClickListener {
    void onRightClick(View v);
  }

  private MoneyInputTextWatcher moneyInputTextWatcher;

  public interface MoneyInputTextWatcher {
    void beforeTextChanged(CharSequence s, int start, int count, int after);

    void onTextChanged(CharSequence s, int start, int before, int count);

    void afterTextChanged(Editable s);
  }

  public void setMoneyInputTextWatcherListener(MoneyInputTextWatcher moneyInputTextWatcher) {
    this.moneyInputTextWatcher = moneyInputTextWatcher;
  }

  private MoneyInputCompleteListener inputCompleteListener;

  /** 绑定输入完成时的监听 */
  public void setMoneyInputCompleteListener(MoneyInputCompleteListener listener) {
    this.inputCompleteListener = listener;
  }

  /** 输入完成时的监听，此监听会在键盘收起或点击清除图标时执行 */
  public interface MoneyInputCompleteListener {
    void InputComplete(String inputMoney);
  }
}
