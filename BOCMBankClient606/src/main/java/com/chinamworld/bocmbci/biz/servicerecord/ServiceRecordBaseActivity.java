package com.chinamworld.bocmbci.biz.servicerecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class ServiceRecordBaseActivity extends BaseActivity implements OnClickListener, OnItemClickListener{
	protected ActivityTaskManager activityTaskManager = ActivityTaskManager
			.getInstance();
	/**
	 * 返回按钮
	 */
	protected Button back;
	/**
	 * 右边按钮
	 */
	protected Button right;
	/**
	 * 主页面布局
	 */
	protected LinearLayout tabcontent;
	/**
	 * 获取文件
	 */
	protected LayoutInflater mainInflater;
	protected OnClickListener rightBtnOnClickListenerForTrade;
	/** p604服务记录查询 */
	public static final String Svr_Rec_Type = "SvrRecType";	// 业务种类
	public static final String RESULLT_LIST = "resultList";// 查询结果列表
	public static final String CREATE_TIME = "CreatTime";//交易日期
	public static final String AMOUNT = "Amount";//金额
	public static final String PSN_SVR_SERVICEREC_QUERYDETAIL = "PsnSVRServiceRecQueryDetail";//服务记录查询接口
	public static final String FINC_CURRENTINDEX = "currentIndex";// 当前页 int
	public static final String FINC_PAGESIZE = "pageSize";// 每页显示条数 int
	public static final String FINC_REFRESH = "_refresh";// 刷新标志
	public static final String FINC_STARTDATE = "startDate";
	public static final String FINC_ENDDATE = "endDate";
	//缤纷生活地址
	public static final String LIVE_URL="http://t.cn/RGOOggm";
	//中银易商地址
	public static final String SHOP_URL="http://open.boc.cn/apps/download/299";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GetPhoneInfo.initActFirst(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_more_activity_layout);
		initPulldownBtn();
		initFootMenu();
//		initLeftSideList(this, LocalData.fincLeftListData);// TODO 改图片
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainInflater = LayoutInflater.from(this);
		Button btn_show = (Button) findViewById(R.id.btn_show);
		btn_show.setVisibility(View.GONE);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(this);
		right = (Button) findViewById(R.id.ib_top_right_btn);
	}

	
	public void setRightToMainHome(){
		right.setVisibility(View.VISIBLE);
		right.setText(getString(R.string.go_main));
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activityTaskManager.removeAllActivity();
			}
		});
	}

//	/**
//	 * 获取tocken
//	 */
//	public void requestPSNGetTokenId() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		biiRequestBody.setParams(null);
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestPSNGetTokenIdCallback");
//	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
//	public void requestPSNGetTokenIdCallback(Object resultObj) {
//	}


	/**
	 * 初始化设定账户布局
	 */
	protected void settingbaseinit() {
		setContentView(R.layout.biz_activity_layout_withnofooter);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.setPadding(
				getResources().getDimensionPixelSize(R.dimen.fill_margin_left),
				getResources().getDimensionPixelSize(R.dimen.fill_margin_top),
				getResources().getDimensionPixelSize(R.dimen.fill_margin_top),
				getResources().getDimensionPixelSize(
						R.dimen.common_bottom_padding_new));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainInflater = LayoutInflater.from(this);
		back = (Button) findViewById(R.id.ib_back);
		right = (Button) findViewById(R.id.ib_top_right_btn);
		back.setOnClickListener(this);
//		setLeftButtonPopupGone();

	}
//?
//	@Override
//	protected void onResume() {
//		super.onResume();
//		// 把这个Activity放到Activity集中管理
//		ActivityTaskManager.getInstance().addActivit(this);
//	}



	/**
	 * 
	 * @param text1
	 * @param text2
	 * @param text3
	 * @return
	 */
//	public View getListHeaderView(int text1, int text2, int text3) {
//		View headerView = mainInflater.inflate(
//				R.layout.finc_query_history_list_header, null);
//		TextView tv1 = (TextView) headerView.findViewById(R.id.tv1);
//		TextView tv2 = (TextView) headerView.findViewById(R.id.tv2);
//		TextView tv3 = (TextView) headerView.findViewById(R.id.tv3);
//		tv1.setText(getString(text1));
//		tv2.setText(getString(text2));
//		tv3.setText(getString(text3));
//		return headerView;
//	}

	/** listView 的header view 适用于主页面 */
	protected void initListHeaderView(int text1ResID, int textRes2Id,
			int textRes3Id) {
		View v = findViewById(R.id.finc_listheader_layout);
		TextView headerView1 = (TextView) v.findViewById(R.id.list_header_tv1);
		TextView headerView2 = (TextView) v.findViewById(R.id.list_header_tv2);
		TextView headerView3 = (TextView) v.findViewById(R.id.list_header_tv3);
		headerView1.setText(text1ResID);
		headerView2.setText(textRes2Id);
		headerView3.setText(textRes3Id);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView3);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	/**
	 * 设置日期
	 */
//	public OnClickListener fincChooseDateClick = new OnClickListener() {
//		@Override
//		public void onClick(final View v) {
//			TextView tv = (TextView) v;
//			String time = tv.getText().toString();
//			int startYear = Integer.parseInt(time.substring(0, 4));
//			int startMonth = Integer.parseInt(time.substring(5, 7));
//			int startDay = Integer.parseInt(time.substring(8, 10));
//			// 第二个参数为用户选择设置按钮后的响应事件
//			// 最后的三个参数为缺省显示的年度，月份，及日期信息
//			DatePickerDialog dialog = new DatePickerDialog(
//					ServiceRecordBaseActivity.this, new OnDateSetListener() {
//
//						@Override
//						public void onDateSet(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							StringBuilder date = new StringBuilder();
//							date.append(String.valueOf(year));
//							date.append("/");
//							int month = monthOfYear + 1;
//							date.append(((month < 10) ? ("0" + month)
//									: (month + "")));
//							date.append("/");
//							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
//									: (dayOfMonth + "")));
//							// 为日期赋值
//							((TextView) v).setText(String.valueOf(date));
//						}
//					}, startYear, startMonth - 1, startDay);
//			dialog.show();
//		}
//	};

	/**
	 * params week 
	 * 根据week，获取对于的时间id
	 * */
//	protected String getValueByWeek(String week) {
//		String value = "01";
//		if("周一".equals(week)){
//			value = "01";
//		}else if("周二".equals(week)){
//			value = "02";
//		}else if("周三".equals(week)){
//			value = "03";
//		}else if("周四".equals(week)){
//			value = "04";
//		}else if("周五".equals(week)){
//			value = "05";
//		}
//		return value ;
//	}
	
	/**
	 * params week 
	 * 根据week，获取对于的时间id
	 * */
//	protected String getWeekByValue(String value) {
//		String week = "-";
//		if("01".equals(value)){
//			week = "周一";
//		}else if("02".equals(value)){
//			week = "周二";
//		}else if("03".equals(value)){
//			week = "周三";
//		}else if("04".equals(value)){
//			week = "周四";
//		}else if("05".equals(value)){
//			week = "周五";
//		}
//		return week ;
//	}
	/**
	 * 历史交易查询 
	 * 
	 * @param startDate
	 * @param endDate
	 * @param fundCode
	 * @param transType
	 * @param currentIndex
	 * @param pageSize
	 * @param flush
	 */
	public void PsnSVRServiceRecQueryDetail(String startDate, String endDate,
			String svrRecType, int currentIndex, int pageSize) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(PSN_SVR_SERVICEREC_QUERYDETAIL);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(FINC_CURRENTINDEX, String.valueOf(currentIndex));
		map.put(FINC_PAGESIZE, String.valueOf(pageSize));
		map.put(FINC_STARTDATE, startDate);
		map.put(FINC_ENDDATE, endDate);
		map.put(Svr_Rec_Type, svrRecType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"psnsvrservicerecquerydetailCallback");
	}

	/**
	 * 历史交易查询 
	 * 
	 * @param resultObj
	 */
	public void psnsvrservicerecquerydetailCallback(Object resultObj) {

	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
//			ServiceRecordBaseActivity.this.onBackPressed();
//			ActivityTaskManager.getInstance().removeAllActivity();
//			finish();
			ServiceRecordBaseActivity.this.finish();
			break;

		default:
			break;
		}

	}
	//判断安转包信息
	protected boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}

}
