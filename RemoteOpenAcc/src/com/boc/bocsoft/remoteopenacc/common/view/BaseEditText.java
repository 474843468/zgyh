package com.boc.bocsoft.remoteopenacc.common.view;

import android.content.Context;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView.OnEditorActionListener;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.common.util.BocroaUtils;

/**
 * 有删除内容按钮的EditText
 * 
 * @author gwluo
 * 
 */
public class BaseEditText extends LinearLayout {
	/**
	 * 设置默认提示文字
	 * 
	 * @param hintText
	 */
	public void setHint(String hintText) {
		editText.setHint(hintText);
	}

	/**
	 * 获取到EditText
	 * 
	 * @return
	 */
	public EditText getEditText() {
		return editText;
	}

	public Editable getText() {
		return editText.getText();
	}

	/**
	 * 设置显示内容
	 * 
	 * @param text
	 */
	public void setText(String text) {
		editText.setText(text);
	}

	private Context mContext;
	private EditText editText;
	private ImageView imageView;

	public BaseEditText(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public BaseEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}

	private void init() {
		init(null);
	}

	private void init(AttributeSet attrs) {
		initLayout();
		initEditText(attrs);
		addView(editText);
		initImageView();
		View view = new View(mContext);
		view.setLayoutParams(new LayoutParams(BocroaUtils.dip2px(mContext, 5),
				1));
		addView(view);
		addView(imageView);
		editText.addTextChangedListener(editTextWatcher);
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				handEditFocusChange(hasFocus);
			}

		});
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editText.getText().toString().length() == 0) {
					// 跳转图片识别
					if (mCallBack != null) {
						mCallBack.onJump2PicDescern();
					}
				} else {
					editText.setText("");
				}
			}
		});
	}

	private void initImageView() {
		imageView = new ImageView(mContext);
		MarginLayoutParams marginLayoutParams = new MarginLayoutParams(
				BocroaUtils.dip2px(mContext, 18), BocroaUtils.dip2px(mContext,
						18));
		imageView.setBackgroundResource(R.drawable.bocroa_gray_clear_icon);
		// marginLayoutParams.leftMargin = BocroaUtils.dip2px(mContext,
		// 5);//不起作用？？
		imageView.setLayoutParams(marginLayoutParams);
		imageView.setVisibility(View.GONE);
	}

	private void initLayout() {
		android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		setGravity(Gravity.CENTER_VERTICAL);
		setOrientation(LinearLayout.HORIZONTAL);
		// setBackgroundResource(R.drawable.bocroa_input_white_gray_bg);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		try {
			super.onRestoreInstanceState(state);// 此处会发生异常
		} catch (Exception e) {
			state = null;
		}
	}

	private void initEditText(AttributeSet attrs) {
		editText = new EditText(mContext, attrs);
		LayoutParams editParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		MarginLayoutParams marginLayoutParams = new MarginLayoutParams(0,
				LayoutParams.MATCH_PARENT);
		marginLayoutParams.rightMargin = 30;
		editParams.weight = 1.0f;
		editText.setLayoutParams(editParams);
		editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		editText.setTextAppearance(mContext,
				R.style.bocroa_style_step_item_edit);
		// editText.setTextAppearance(mContext, R.style.base_edit_text_style);
		editText.setSingleLine(true);
		editText.setPadding(BocroaUtils.dip2px(mContext, 6), 0, 0, 0);
		editText.setBackgroundResource(0);
		BocroaUtils.setEditTextCourse(editText);
	}

	public void setEditTextGravity(int gravity) {
		editText.setGravity(gravity);
	}

	/**
	 * 设置密码隐藏显示
	 * 
	 * @param isPassword
	 */
	public void setEditTextPasswordType(Boolean isPassword) {
		if (isPassword) {
			editText.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			// editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);//lgw
			// 2015.10.28 由于要使用SDK 2.3.3(Android 10) 编译
		}
	}

	private TextWatcher editTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (mTextWatcher != null) {
				mTextWatcher.onTextChanged(s, start, before, count);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (mTextWatcher != null) {
				mTextWatcher.beforeTextChanged(s, start, count, after);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (mTextWatcher != null) {
				mTextWatcher.afterTextChanged(s);
			}
			if (!editText.isFocused()) {
				return;
			}
			handImageVisiable();
		}

	};
	private int picId = 0;

	/**
	 * 设置右侧显示图片
	 * 
	 * @param res
	 */
	public void setEditImage(int res) {
		picId = res;
		imageView.setBackgroundResource(res);
		imageView.setVisibility(View.VISIBLE);
	}

	private void handImageVisiable() {
		if (BocroaUtils.isEmpty(editText.getText().toString())) {
			isShowPic();
		} else {
			if (editText.isEnabled()) {// 输入后不可修改内容的情况
				imageView.setVisibility(View.VISIBLE);
				imageView
						.setBackgroundResource(R.drawable.bocroa_gray_clear_icon);
			}
		}
	}

	private void isShowPic() {
		if (picId == 0) {
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setBackgroundResource(picId);
		}
	}

	private void handEditFocusChange(boolean hasFocus) {
		if (hasFocus) {
			handImageVisiable();
		} else {
			imageView.setVisibility(View.GONE);
		}
	}

	public void setOnEditFocusChangeListener(
			final OnFocusChangeListener editFocusChange) {
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				handEditFocusChange(hasFocus);
				editFocusChange.onFocusChange(v, hasFocus);
			}
		});
	}

	private TextWatcher mTextWatcher;

	public void addEditTextWatcher(TextWatcher watcher) {
		mTextWatcher = watcher;
	}

	public void setEditEnable(Boolean isEnabled) {
		editText.setEnabled(isEnabled);
	}

	public void setEditImeOptions(int imeActionSearch) {
		editText.setImeOptions(imeActionSearch);
	}

	public void setEditImeActionLabel(CharSequence label, int actionId) {
		editText.setImeActionLabel(label, actionId);
	}

	public void setOnEditorActionListener(
			OnEditorActionListener onEditorActionListener) {
		editText.setOnEditorActionListener(onEditorActionListener);
	}

	public void setTransformationMethod(ReplacementTransformationMethod method) {
		editText.setTransformationMethod(method);
	}

	public BaseEditCallBack mCallBack;

	public void setBaseEditCallBack(BaseEditCallBack callBack) {
		mCallBack = callBack;
	}

	public interface BaseEditCallBack {
		public void onJump2PicDescern();
	}

	/**
	 * 设置最大字符数
	 * 
	 * @param i
	 */
	public void setMaxLenth(int i) {
		// 中英文长度一样
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(i) });
		// editText.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// int lenth = BocroaUtils.getStringLength(s.toString());
		// if (lenth > i) {
		// editText.setText(BocroaUtils.getAppointLengthString(
		// s.toString(), i));
		// editText.setSelection(editText.getText().toString()
		// .length());
		// }
		// }
		// });
	}
}
