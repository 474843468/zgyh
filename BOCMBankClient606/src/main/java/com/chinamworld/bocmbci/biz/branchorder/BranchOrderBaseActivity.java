package com.chinamworld.bocmbci.biz.branchorder;

import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.chsliver.ZhongYinMainActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.mapabc.bc.activity.BCMapabcActivity;

/**
 * 网点排队预约UI基类
 * @author panwe
 *
 */
public class BranchOrderBaseActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener{
	private LinearLayout mBodyLayout;
	/** 右按钮 */
	public Button mRightButton;
	/** 左按钮 */
	public Button mLeftButton;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnOnClickListener;
	/** 关闭标志*/
	protected final static int RESULT_CLOSE = -100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		setLeftBtnGone();
		initPulldownBtn();
		initFootMenu();
		setupView();
//		setFootMenuGone();
	}
	
	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setRightText(String title) {
		mRightButton.setVisibility(View.VISIBLE);
		mRightButton.setText(title);
		mRightButton.setTextColor(Color.WHITE);
		mRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (rightBtnOnClickListener != null) {
					rightBtnOnClickListener.onClick(v);
				}
			}
		});
	}
	
	public void setLeftInvisible(){
		mLeftButton.setVisibility(View.INVISIBLE);
	}
	
	public void setRightInvisible(){
		mRightButton.setVisibility(View.INVISIBLE);
	}
	
	public void setPadding(int left, int top, int right, int bottom){
		mBodyLayout.setPadding(left, top, right, getResources().getDimensionPixelSize(R.dimen.fill_margin_top));
	}
	
	/**
	 * 右上角按钮clikListener
	 * 
	 * @param rightBtnClick
	 */
	public void setRightBtnClick(OnClickListener rightBtnOnClick) {
		this.rightBtnOnClickListener = rightBtnOnClick;
	}
	
	/** 返回事件 */
	public OnClickListener backOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	/**
	 * 初始化控件
	 */
	private void setupView(){
		mBodyLayout = (LinearLayout) findViewById(R.id.sliding_body);
		mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		mLeftButton = (Button) findViewById(R.id.ib_back);
		mRightButton.setOnClickListener(toMainOnClickListener);
		mLeftButton.setOnClickListener(backOnClickListener);
	}
	
	/**
	 * 隐藏左侧菜单
	 */
	protected void setLeftBtnGone() {
		findViewById(R.id.btn_show).setVisibility(View.GONE);
	}
	
	/**
	 * 隐藏底部菜单
	 */
	public void setFootMenuGone(){
		findViewById(R.id.menu_popwindow).setVisibility(View.GONE);
	}
	
	/**
	 * 初始化布局
	 */
	public void inflateLayout(int resource){
		View v = View.inflate(this, resource, null);
		mBodyLayout.addView(v);
	}
	
	/**
	 * 设置日期 
	 */
	public OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					BranchOrderBaseActivity.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};
	
	/** 返回主页面 */
	public OnClickListener toMainOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BranchOrderDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
//			BranchOrderBaseActivity.this.startActivity(new Intent(BranchOrderBaseActivity.this, ZhongYinMainActivity.class));
			goToMainActivity();
		}
	};
	
	public void setText(String string, TextView textView){
		if(textView != null){
			textView.setText(StringUtil.valueOf1(string));
		}
	}
	
	public String getNoColonStr(String str){
		if (StringUtil.isNull(str))
			return ConstantGloble.BOCINVT_DATE_ADD;
		if (str.contains("：") || str.contains(":"))
			return str.substring(0, str.length() - 1);
		return str;
	}
	
	public String addColonToStr(String str){
		if (StringUtil.isNull(str))
			return ConstantGloble.BOCINVT_DATE_ADD;
		if (str.contains("：") || str.contains(":"))
			return str;
		else
			return str + "：";
	}
	
	/**
	 * 请求系统时间
	 */
	public void requestSystemDateTimeForLoginPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_SYSTEM_TIME_FOR_LOGIN_PRE);
		HttpManager.requestBii(biiRequestBody, this,
				"requestSystemDateTimeForLoginPreCallBack");
	}
	
	public void requestSystemDateTimeForLoginPreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		dateTime = (String) resultMap.get(Comm.DATETME);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_top_right_btn:
//			BaseDroidApp.getInstanse().showInfoMessageDialog("我的周边服务尚未开始，敬请期待");
			Intent intent = new Intent(this,BCMapabcActivity.class);
			intent.putExtra("isLogin",BaseDroidApp.getInstanse().isLogin()); // 客户未登录，显示网点预约排队按钮
			BranchOrderDataCenter.isFromMain = true;
			BranchOrderDataCenter.isFromMyAround = true;
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.FourTask;
	}
}
