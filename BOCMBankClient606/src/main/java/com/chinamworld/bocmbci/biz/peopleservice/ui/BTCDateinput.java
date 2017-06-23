package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.UnitTransition;

public class BTCDateinput extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	private static final String TAG = BTCDateinput.class.getSimpleName();
	/** 全局变量对象 */
	private BTCCMWApplication cmwApplication;
	
	private EditText textView;
	/** 当前选中的时间 */
	private Calendar  curCalendar;
	/** 时间选择器 */
	private DatePickerDialog dialog ;
	private int element_size;
	String params_name =null;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCDateinput(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Dateinput;
		//this.context = context;
		activityManager = BTCActivityManager.getInstance();
	}
	
	/** 创建时间选择器  */
	@SuppressWarnings("deprecation")
	private void createDatePicker(View view,final String date){
//		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy/MM/dd");       
//		Date  curDate = new Date(date);//获取当前时间       
//		String str = formatter.format(curDate);     
		
		View view1 = LayoutInflater.from(context).inflate(R.layout.peopleservice_dateinput_layout, null);
		textView = (EditText)view1.findViewById(R.id.textview);
		textView.setTextSize(15);
		textView.setTextColor(context.getResources().getColor(R.color.black));
		textView.setPadding(20, 0, 0, 0);
		textView.setFocusable(false);
		view1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
		textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UnitTransition.dip2px(context,40),1));
		textView.setText(date);
		textView.setOnClickListener( new OnClickListener() {
		@Override
		public void onClick(final View v) {
			EditText tv = (EditText) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					context, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month): (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							textView.setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	});
		if (element_size>1) {
			((ViewGroup) view).addView(view1);
		}else {
			LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			((LinearLayout) view).setGravity(Gravity.CENTER_VERTICAL);
			((ViewGroup) view).addView(view1);
		}
		
	}
	
	@Override
	public Object drawLable( Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		params_name = params.get("name");
		List<BTCElement> childElements = btcElement.getChildElements();
		BTCElement parentElement = btcElement.getParentElement();
		element_size = parentElement.getChildElements().size();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		String dateSystem = BTCCMWApplication.flowFileLangMap.get("_SYSTEMDATETIME").toString();//2017/11/09 
		dateSystem=dateSystem.substring(0, dateSystem.indexOf(" "));
		//wuhan
		if(params.containsKey(BTCLableAttribute.LABEL)){
			String label=params.get(BTCLableAttribute.LABEL);
			TextView text = new TextView(context);
			text.setTextSize(15);
			text.setTextColor(context.getResources().getColor(R.color.black));
			if(BTCCMWApplication.flowFileLangMap.containsKey(label)&&BTCCMWApplication.flowFileLangMap.get(label)!=null){
				text.setText(BTCCMWApplication.flowFileLangMap.get(label).toString());
			}else{
				text.setText(label);
			}
			/*by dl*/
			LinearLayout.LayoutParams childParams =null;
				if (label==null||label.equals("")) {
					childParams = new LinearLayout.LayoutParams(
							 0,
							ViewGroup.LayoutParams.WRAP_CONTENT);
				}else{
					childParams = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.WRAP_CONTENT,1);
				}
			/*by dl  end*/
			((ViewGroup) view).addView(text,childParams);	
		}
		
		createDatePicker(view,dateSystem);
		
		/*by dl*/
		LinearLayout.LayoutParams childParams =null;
			childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		/*by dl  end*/

		// 为输入框设置id
		String names = params.get(BTCLableAttribute.NAME);//TIME_BEGIN
		int hashcode = names.hashCode();
		hashcode = hashcode > 0 ? hashcode : -hashcode;
		textView.setId(hashcode);
		Map<String, Object> msg = new HashMap<String, Object>();
		
		if(params.containsKey(BTCLableAttribute.BINDTARGET)){
			String tag=params.get(BTCLableAttribute.BINDTARGET);
			msg.put(BTCLableAttribute.BINDTARGET, tag);
//			edittext.setTag(tag);
//			textView.setTag(tag);
			
		}
		if (params.containsKey(BTCLableAttribute.USEPROMPT)) {
			String useprompt = params.get(BTCLableAttribute.USEPROMPT);
			msg.put(BTCLableAttribute.USEPROMPT, useprompt);
		}
		
		if (params.containsKey(BTCLableAttribute.PROMPT)) {
			String prompt = params.get(BTCLableAttribute.PROMPT);
			msg.put(BTCLableAttribute.PROMPT, prompt);
		}
		
		if(params.containsKey("checkrule")){
			String checkrule = params.get("checkrule");
			msg.put("checkrule", checkrule);
		}
		
		String periodtype="";
		int	periodnum=0;
		String isdefault;
		if(params.containsKey("periodtype")){
//			Y:年 M:月 D:天 时间跨度类型
			periodtype = params.get("periodtype");
		}
		
		if(params.containsKey("periodnum")){
//			时间跨值 ，可正可负 ，正值表示想未来推算， 负值表示向过去推算
			try{
				periodnum = Integer.parseInt(params.get("periodnum").replace("+", "")) ;
			}
			catch (Exception e){
			}
		}
		
		Date  dates = new Date(dateSystem);
		curCalendar = Calendar.getInstance();
		curCalendar.setTime(dates);
		//isdefault,periodtype,periodnum进行判断
		if(params.containsKey("isdefault")){
			isdefault = params.get("isdefault");
			if(isdefault.equals("true")){
//				是否需要默认值，如果periodtype和periodnum不配置，默认值为当前系统日期；
				if(!"".equals(periodtype)&&periodtype!=null){
//					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					String date =df.format(new Date());
					String date = "";

					if("Y".equals(periodtype)){
						curCalendar.add(Calendar.YEAR, periodnum);
					//	year = year+1;
					}else if("M".equals(periodtype)){
						curCalendar.add(Calendar.MONTH, periodnum);
					//	month = month +1;
					}else if("D".equals(periodtype)){
						curCalendar.add(Calendar.DATE, periodnum);
					//	day = day +1;
					}
					//date = year+":"+month+":"+day;
//					int num = Integer.parseInt(periodnum);
					
					//edittext.setText(date);
					
				}else{//默认值为当-前系统日期
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String date =df.format(new Date());
					//edittext.setText(date);
					
				}
			}
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String dateStr = df.format(curCalendar.getTime());
		textView.setText(dateStr);
		textView.setTag(msg);
		if (params.containsKey(BTCLableAttribute.CHECKRULE)) {

			Object checkrule = params.get(BTCLableAttribute.CHECKRULE);
			activityManager.setWidgetcheckrule(textView.getId(), checkrule);
		}
		activityManager.putWidgetId(names, hashcode);
		
		if(dbMap!=null){
			dbMap.put(names, String.valueOf(hashcode));
		}else{
			// 把当前输入框的id号放到全局变量中
			activityManager.putWidgetId(names, hashcode);
		}
		
		return super.drawLable(dbMap, view);
	}
	
}
