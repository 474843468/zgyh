package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.Fragment;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

public abstract class LifeInsuranceBaseFragment extends Fragment {

	protected View mMainView;
	/** 用户选择险别的下标名 intent使用 */
	public String SELECTPOSITION = "selectPosition";

	protected abstract void findView();

	protected abstract void viewSet();

	public abstract boolean submit();

	/** 为时间显示控件添加系统时间，且增加选择时间事件，支持设置最大最小日期 */
	protected void setShowDateView(TextView tv, String dateStr, String maxDateStr, String minDateStr) {
		final String maxDate = maxDateStr;
		final String minDate = minDateStr;
		if (StringUtil.isNull(dateStr)) {
			tv.setText(SafetyDataCenter.getInstance().getSysTime());
		} else {
			tv.setText(dateStr);
		}
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				TextView tv = (TextView) v;
				String time = tv.getText().toString();
				int Year = 0;
				int Month = 0;
				int Day = 0;
				Year = Integer.parseInt(time.substring(0, 4));
				Month = Integer.parseInt(time.substring(5, 7));
				Day = Integer.parseInt(time.substring(8, 10));
				
				// 第二个参数为用户选择设置按钮后的响应事件
				// 最后的三个参数为缺省显示的年度，月份，及日期信息
				DatePickerDialog dialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						StringBuilder date = new StringBuilder();
						date.append(String.valueOf(year));
						date.append("/");
						int month = monthOfYear + 1;
						date.append(((month < 10) ? ("0" + month) : (month + "")));
						date.append("/");
						date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
						// 为日期赋值
						((TextView) v).setText(String.valueOf(date));
					}
				}, Year, Month - 1, Day);
				
				if (!StringUtil.isNull(maxDate)) {
					Calendar cal = Calendar.getInstance();
					
					cal.set(Integer.parseInt(maxDate.substring(0, 4)), Integer.parseInt(maxDate.substring(5, 7)), Integer.parseInt(maxDate.substring(8, 10)));
					long times = cal.getTimeInMillis();
					dialog.getDatePicker().setMaxDate(times);
				}
				
				if (!StringUtil.isNull(minDate)) {
					Calendar cal = Calendar.getInstance();
					
					cal.set(Integer.parseInt(minDate.substring(0, 4)), Integer.parseInt(minDate.substring(5, 7)), Integer.parseInt(minDate.substring(8, 10)));
					long times = cal.getTimeInMillis();
					dialog.getDatePicker().setMinDate(times);
				}
				
				dialog.show();
			}
		});
	}

	/** 根据spinner返回spinner选择的字符串 */
	public String getSpSelectItemStr(Spinner sp) {
		return sp.getSelectedItem().toString();
	}
	
	/**
	 * 当控件内容校验不通过时变更控件背景为红色<br>
	 * 支持EditText、Spinner、TextView做成的日期选择控件
	 * 
	 * @param view
	 */
	protected void changeViewBg(View view) {
		if (view == null)
			return;
		ViewUtils.scrollShow(view);
		if (view instanceof EditText) {
			changeEditTextBg((EditText) view);
		} else if (view instanceof Spinner) {
			changeSpinnerBg((Spinner) view);
		} else if (view instanceof TextView) {
			changeTextViewDateBg((TextView) view);
		}
//		requestFocus(view);
	}

//	private void requestFocus(View v) {
//		 v.setFocusable(true);
//         v.setFocusableInTouchMode(true);
//         v.requestFocus();
//         v.requestFocusFromTouch();
//	}

	/** 控制输入出错的EditText控件背景 */
	protected void changeEditTextBg(final EditText et) {
		et.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_for_dittext_red));
		((EditText) et).addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				et.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_for_edittext));
			}
		});
	}

	/** 控制输入出错的Spinner控件背景 */
	protected void changeSpinnerBg(Spinner sp) {
		sp.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_red));
	}

	/** 控制输入出错的TextView日期控件背景 */
	protected void changeTextViewDateBg(final TextView tv) {
		tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_red));
		((TextView) tv).addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			}
		});
	}

	/** 设置日期，注册日期控件的点击事件 */
	protected OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int Year = 0;
			int Month = 0;
			int Day = 0;
			Year = Integer.parseInt(time.substring(0, 4));
			Month = Integer.parseInt(time.substring(5, 7));
			Day = Integer.parseInt(time.substring(8, 10));
			
			Calendar cal = Calendar.getInstance();
			cal.set(Year, Month, Day);
			long times = cal.getTimeInMillis();
			
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// 为日期赋值
					((TextView) v).setText(String.valueOf(date));
				}
			}, Year, Month - 1, Day);
			dialog.getDatePicker().setMinDate(times);
			dialog.show();
		}
	};
}
