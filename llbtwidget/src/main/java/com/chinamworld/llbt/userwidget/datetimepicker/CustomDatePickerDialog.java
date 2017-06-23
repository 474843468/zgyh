package com.chinamworld.llbt.userwidget.datetimepicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Date;

/**
 * 自定义日期选择框
 * 支持只显示年月
 * @author yuht
 *
 */
public class CustomDatePickerDialog extends DatePickerDialog {

	private boolean isShowDay = true;
	private int y,m,d;
	private DatePicker dp;
	 public CustomDatePickerDialog(Context context,
								   OnDateSetListener callBack, int year, int monthOfYear,
								   int dayOfMonth) {
	        super(context, callBack, year, monthOfYear, dayOfMonth);  
	        y = year;
	        m = monthOfYear;
	        d = dayOfMonth;
	        isShowDay = true;
	    }  

	 public CustomDatePickerDialog(Context context,
								   OnDateSetListener callBack, int year, int monthOfYear) {
	        super(context, callBack, year, monthOfYear, 1);  
	        y = year;
	        m = monthOfYear;
	        d = 1;
	        isShowDay  = false;
	        setTitle( y + "年" + (monthOfYear + 1) + "月");
	    }


	@Override
	public void show() {
		super.show();
		if(isShowDay == true)
			return;
		DatePicker dp = findDatePicker((ViewGroup) getWindow().getDecorView());
		if (dp != null) {
			try{
				((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
			}
			catch (Exception e){

			}

		}
	}

	/**
	    * 设置
	    * @param maxDate
	    * @param minDate
	    */
		public void setDateLimit(Date maxDate, Date minDate){
			if(maxDate != null)
			 	this.getDatePicker().setMaxDate(maxDate.getTime());
			if(minDate != null)
			 this.getDatePicker().setMinDate(minDate.getTime());
		}
	 
	    @Override
	    public void onDateChanged(DatePicker view, int year, int month, int day) {
	        super.onDateChanged(view, year, month, day);
			if(isShowDay == false && dp != null)
	        	setTitle(year + "年" + (month + 1) + "月");
	    }  
	    
	    
	   
	    
		private DatePicker findDatePicker(ViewGroup group) {
	        if (group != null) {  
	            for (int i = 0, j = group.getChildCount(); i < j; i++) {  
	                View child = group.getChildAt(i);
	                if (child instanceof DatePicker) {
	                    return (DatePicker) child;
	                } else if (child instanceof ViewGroup) {
	                    DatePicker result = findDatePicker((ViewGroup) child);
	                    if (result != null)  
	                        return result;  
	                }  
	            }  
	        }  
	        return null;  
	    }   
	    
}
