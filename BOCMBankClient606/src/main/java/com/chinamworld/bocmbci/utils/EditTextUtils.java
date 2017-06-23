package com.chinamworld.bocmbci.utils;

import android.app.Activity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;

/**
 * 输入框的工具类
 * 
 * @author xby
 * 
 *         2013-5-31
 */
public class EditTextUtils {

	/**
	 * 添加位数限制（中英混合，中文占有两个字符）
	 * 
	 * @param act
	 * @param et
	 *            要添加限制的Edittext
	 * @param length
	 *            限制的位数
	 */
	public static void setLengthMatcher(Activity act, EditText et, int length) {
		et.addTextChangedListener(new TextWatcherLimit(act, et, length));
	}
	
	/**
	 * 小写改为大些
	 */
	public static int EditUtil_ActionType_UpperCase = 1;

	/**
	 * 监听EditText，根据type事件进行处理
	 * @param EditText
	 * @param type
	 */
	public static void addActionListener(final EditText editText, final int type) {

		editText.addTextChangedListener(new TextWatcher() {
			boolean isUser = false;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (EditUtil_ActionType_UpperCase == type) {
					if (!isUser) {
						isUser = true;
						String str = s.toString();
						int selectionStart = editText.getSelectionStart();
						editText.setText(str.toUpperCase());
						editText.setSelection(selectionStart);
						isUser = false;
					}
				}
			}
		}); 
	}

	/**
	 * 添加输入过滤
	 * 
	 * @param act
	 * @param et
	 *            要添加限制的Edittext
	 * @param match
	 *            正则表达式
	 */
	public static void setTextMatcher(Activity act, EditText et, String match) {
		et.addTextChangedListener(new FilterWatcherLimit(act, et, match));
	}

	/**
	 * 将金额输入框与中文大写金额文本显示框进行关联
	 * 
	 * @param numInput
	 *            金额输入框
	 * @param chineseShowTV
	 *            中文大写反显文本框
	 */
	public static void relateNumInputToChineseShower(final EditText numInput,
			final TextView chineseShowTV) {
	
		final Trans2RMB tr = new Trans2RMB();
		if (numInput != null && chineseShowTV != null) {
			numInput.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (numInput.hasFocus() == false) {
						chineseShowTV.setVisibility(View.GONE);
					} else {
						if (StringUtil.isNullOrEmpty(numInput.getText()
								.toString().trim())) {
							chineseShowTV.setVisibility(View.GONE);
						} else {
							chineseShowTV.setVisibility(View.VISIBLE);	
						}

						
					}
				}
			});

			numInput.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (!StringUtil.isNullOrEmpty(numInput.getText().toString()
							.trim())) {
						chineseShowTV.setText(tr.getFormatedZHNumber(numInput));
						chineseShowTV.setVisibility(View.VISIBLE);
						View scrollView = null;
						ViewParent parent = numInput.getParent();
						while(true){
							if(parent instanceof ScrollView) {
								scrollView = (ScrollView)parent;
								break;
							}
							if(parent instanceof ViewGroup == false){
								break;
							}
							parent = ((ViewGroup)parent).getParent();
						}
						if(scrollView == null)
							return;								
						scrollView.scrollTo(0, 10000);
					} else {
						chineseShowTV.setVisibility(View.GONE);
					}

				}
			});
		}
	}
	

	/**
	 * 将金额输入框与中文大写金额文本显示框进行关联
	 * 如果是跨行转账  金额超过5万元 提示信息
	 * @param numInput
	 *            金额输入框
	 * @param chineseShowTV
	 *            中文大写反显文本框
	 */
	public static void relateNumInputToChineseShower(final EditText numInput,
			final TextView chineseShowTV, final boolean isotherBank) {
		final Trans2RMB tr = new Trans2RMB();
		if (numInput != null && chineseShowTV != null) {
			numInput.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (numInput.hasFocus() == false) {
						chineseShowTV.setVisibility(View.GONE);
						//如果是跨行转账 金额大于5万元 提示信息
						if(isotherBank){
							String money = numInput.getText().toString().trim();
							if(!StringUtil.isNull(money)){
								double dmoney = Double.parseDouble(money);
								if(dmoney > 50000){
									BaseDroidApp.getInstanse().showMessageDialog("转账金额不可大于5万元 ", new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											numInput.getText().clear();
											BaseDroidApp.getInstanse().dismissMessageDialog();
										}
									});
								}
							}
						}
						
					} else {
						if (StringUtil.isNullOrEmpty(numInput.getText()
								.toString().trim())) {
							chineseShowTV.setVisibility(View.GONE);
						} else {
							chineseShowTV.setVisibility(View.VISIBLE);
						}
					}
				}
			});

			numInput.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (!StringUtil.isNullOrEmpty(numInput.getText().toString()
							.trim())) {
						chineseShowTV.setText(tr.getFormatedZHNumber(numInput));
						chineseShowTV.setVisibility(View.VISIBLE);
					} else {
						chineseShowTV.setVisibility(View.GONE);
					}

				}
			});
		}
	}

	/**
	 * 设置银行卡输入框每四位加一空格<br/>
	 * 
	 * @param mEditText
	 *            要设置的银行卡输入框
	 */
	public static void setBankCardNumAddSpace(final EditText mEditText) {
		mEditText.addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;

			int location = 0;// 记录光标的位置
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == ' ') {
						konggeNumberB++;
					}
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3
						|| isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isChanged) {
					location = mEditText.getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == ' ') {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}

					index = 0;
					int konggeNumberC = 0;
					while (index < buffer.length()) {
						// 每四位加一个空格 add by xby
						if ((index + 1) % 5 == 0) {
							buffer.insert(index, ' ');
							konggeNumberC++;
						}
						index++;
					}

					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}

					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}

					mEditText.setText(str);
					Editable etable = mEditText.getText();
					Selection.setSelection(etable, location);
					isChanged = false;
				}
			}
		});
	}

}
