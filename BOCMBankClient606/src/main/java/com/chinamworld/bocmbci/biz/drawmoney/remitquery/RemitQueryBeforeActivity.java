package com.chinamworld.bocmbci.biz.drawmoney.remitquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.dept.notmg.NotifyGalleryAdapter;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;

/**
 * @ClassName: RemitQueryBeforeActivity
 * @Description: 汇款查询条件页面
 * @author JiangWei
 * @date 2013-7-22 上午9:56:45
 */
public class RemitQueryBeforeActivity extends DrawBaseActivity {

	private LayoutInflater inflater = null;
	private Context context = this;

	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	/** 底部listview Layout */
	private LinearLayout listContentLayout;

	/** 排序的layout */
	private LinearLayout ll_sort;
	/**  */
	private ImageView img_sort_icon;
	/** 排序button */
	private Button sortTextBtn;

	/** 左右滑动控件的左箭头 */
	private ImageView img_arrow_left;
	/** 左右滑动控件的右箭头 */
	private ImageView img_arrow_right;

	private ListView listView;
	/** 当前选择list条目 */
	private int currentPosition;
	/** 下拉箭头 */
	private LinearLayout ivPullDown;
	/** 收起箭头 */
	private LinearLayout ivPullUp;
	/** 列表数据的adapter */
	private RemitQueryResultListAdapter adapter;

	/** 横向滑动listitem */
	private CustomGallery gallery;
	/** 开始日期str */
	private String startDate;
	/** 结束日期str */
	private String endDate;
	/** 开始日期 */
	private TextView textStartDate;
	/** 结束日期 */
	private TextView textEndDate;
	/**到期日期*/
	private String dueDate;
	/** 查询后的日期范围 */
	private TextView queryDateContent;
	private TextView accoutNumberText;
	/** 下拉布局 */
	private View popupLayout;
	/** 所有账户列表 */
	private List<Map<String, Object>> accountList;
	/** 当期系统时间 */
	private String currentTime;
	/** 首次弹出下拉框 */
	private boolean isFirst = true;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	/** 总条目数 */
	private int totalCount = 0;
	/** listview的更多条目 */
	private View viewFooter;
	private Button btnMore;
	private String pageSize = "10";

	private int theChoosePosition = 0;
	private RelativeLayout layout_the_top;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.remitout_query);
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.remitout_query_before, null);

		tabcontent.addView(view);

		setTitle(R.string.remitout_query_title);
		currentPosition = this.getIntent().getIntExtra(CURRENT_POSITION, -1);
		currentTime = this.getIntent().getStringExtra(CURRENT_DATETIME);

		tabcontent.setPadding(
				0,
				0,
				0,
				getResources().getDimensionPixelSize(
						R.dimen.common_bottom_padding_new));
	
		initConditionView();
		initView();
		// BaseHttpEngine.showProgressDialog();
	}

	private void initConditionView() {
//		popupLayout = (LinearLayout) LayoutInflater.from(this).inflate(
//				R.layout.remitout_query_condition, null);
		popupLayout = findViewById(R.id.remitout_query_condition_id);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout,
//				BaseDroidApp.getInstanse().getCurrentAct());

		gallery = (CustomGallery) findViewById(R.id.viewPager);
		img_arrow_left = (ImageView) findViewById(R.id.img_arrow_left);
		img_arrow_right = (ImageView) findViewById(R.id.img_arrow_right);
		accountList = DrawMoneyData.getInstance().getScreenAccountList();
		NotifyGalleryAdapter adapter = new NotifyGalleryAdapter(this,
				accountList);
		gallery.setAdapter(adapter);
		gallery.setSelection(currentPosition);
		if (accountList.size() == 1) {
			img_arrow_left.setVisibility(View.GONE);
			img_arrow_right.setVisibility(View.GONE);
		} else if (currentPosition == 0) {
			img_arrow_left.setVisibility(View.GONE);
			img_arrow_right.setVisibility(View.VISIBLE);
		} else if (currentPosition == accountList.size() - 1) {
			img_arrow_left.setVisibility(View.VISIBLE);
			img_arrow_right.setVisibility(View.GONE);
		} else {
			img_arrow_left.setVisibility(View.VISIBLE);
			img_arrow_right.setVisibility(View.VISIBLE);
		}
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int currentPage, long arg3) {
				if (accountList.size() == 1) {
					img_arrow_left.setVisibility(View.GONE);
					img_arrow_right.setVisibility(View.GONE);
				} else if (currentPage == 0) {
					img_arrow_left.setVisibility(View.GONE);
					img_arrow_right.setVisibility(View.VISIBLE);
				} else if (currentPage == accountList.size() - 1) {
					img_arrow_left.setVisibility(View.VISIBLE);
					img_arrow_right.setVisibility(View.GONE);
				} else {
					img_arrow_left.setVisibility(View.VISIBLE);
					img_arrow_right.setVisibility(View.VISIBLE);
				}
				// currentPosition = currentPage;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		sortTextBtn = (Button) findViewById(R.id.sort_text);
		sortTextBtn.setText(LocalData.filterRemitStatus[0]);
		ll_sort = (LinearLayout) findViewById(R.id.dept_ll_sort);
		// ll_sort.setVisibility(View.VISIBLE);

		img_sort_icon = (ImageView) findViewById(R.id.img_sort_icon);
		img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
		// 初始化排序框
		PopupWindowUtils.getInstance().setOnPullSelecterListener(this, ll_sort,
				LocalData.filterRemitStatus, null, sortClick);
		accoutNumberText = (TextView) findViewById(R.id.dept_query_volumenumber_tv);
		queryDateContent = (TextView) findViewById(R.id.dept_query_cdnumber_tv);

		// 收起箭头
		ivPullUp = (LinearLayout) popupLayout.findViewById(R.id.img_arrow_up);
		initQueryBeforeLayout();
	
	
	}

	private void initView() {
		initQueryAfterLayout();
		ivPullUp.setClickable(false);
		popupLayout.setVisibility(View.VISIBLE);
		layout_the_top.setVisibility(View.GONE);
		listContentLayout.setVisibility(View.GONE);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (isFirst) {
			if (hasFocus) {
				if (listData != null && !listData.isEmpty()) {
//					PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
					popupLayout.setVisibility(View.GONE);
					layout_the_top.setVisibility(View.VISIBLE);
					listContentLayout.setVisibility(View.VISIBLE);
				} else {
//					PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
					popupLayout.setVisibility(View.VISIBLE);
					layout_the_top.setVisibility(View.GONE);
					listContentLayout.setVisibility(View.GONE);
				}
				// requestPsnMobileRemitQuery();
				isFirst = false;
			}
		}

	}

	/**
	 * @Title: init
	 * @Description: 初始化view和控件
	 * @param
	 * @return void
	 */
	private void init() {
//		popupLayout = (LinearLayout) LayoutInflater.from(this).inflate(
//				R.layout.remitout_query_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout,
//				BaseDroidApp.getInstanse().getCurrentAct());
//
//		gallery = (CustomGallery) popupLayout.findViewById(R.id.viewPager);
//		img_arrow_left = (ImageView) popupLayout
//				.findViewById(R.id.img_arrow_left);
//		img_arrow_right = (ImageView) popupLayout
//				.findViewById(R.id.img_arrow_right);
//		accountList = DrawMoneyData.getInstance().getScreenAccountList();
//		NotifyGalleryAdapter adapter = new NotifyGalleryAdapter(this,
//				accountList);
//		gallery.setAdapter(adapter);
//		gallery.setSelection(currentPosition);
//		if (accountList.size() == 1) {
//			img_arrow_left.setVisibility(View.GONE);
//			img_arrow_right.setVisibility(View.GONE);
//		} else if (currentPosition == 0) {
//			img_arrow_left.setVisibility(View.GONE);
//			img_arrow_right.setVisibility(View.VISIBLE);
//		} else if (currentPosition == accountList.size() - 1) {
//			img_arrow_left.setVisibility(View.VISIBLE);
//			img_arrow_right.setVisibility(View.GONE);
//		} else {
//			img_arrow_left.setVisibility(View.VISIBLE);
//			img_arrow_right.setVisibility(View.VISIBLE);
//		}
//		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int currentPage, long arg3) {
//				if (accountList.size() == 1) {
//					img_arrow_left.setVisibility(View.GONE);
//					img_arrow_right.setVisibility(View.GONE);
//				} else if (currentPage == 0) {
//					img_arrow_left.setVisibility(View.GONE);
//					img_arrow_right.setVisibility(View.VISIBLE);
//				} else if (currentPage == accountList.size() - 1) {
//					img_arrow_left.setVisibility(View.VISIBLE);
//					img_arrow_right.setVisibility(View.GONE);
//				} else {
//					img_arrow_left.setVisibility(View.VISIBLE);
//					img_arrow_right.setVisibility(View.VISIBLE);
//				}
//				// currentPosition = currentPage;
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//
//			}
//		});
//
//		sortTextBtn = (Button) findViewById(R.id.sort_text);
//		sortTextBtn.setText(LocalData.filterRemitStatus[0]);
//		ll_sort = (LinearLayout) findViewById(R.id.dept_ll_sort);
//		// ll_sort.setVisibility(View.VISIBLE);
//
//		img_sort_icon = (ImageView) findViewById(R.id.img_sort_icon);
//		img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
//		// 初始化排序框
//		PopupWindowUtils.getInstance().setOnPullSelecterListener(this, ll_sort,
//				LocalData.filterRemitStatus, null, sortClick);
//		accoutNumberText = (TextView) findViewById(R.id.dept_query_volumenumber_tv);
//		queryDateContent = (TextView) findViewById(R.id.dept_query_cdnumber_tv);
//
//		// 收起箭头
//		ivPullUp = (LinearLayout) popupLayout.findViewById(R.id.img_arrow_up);
//		initQueryBeforeLayout();
//		initQueryAfterLayout();
//		ivPullUp.setClickable(false);
		// excuseQuery();
	}

	/**
	 * 查询后layout
	 */
	private void initQueryAfterLayout() {
		// 下拉箭头
		ivPullDown = (LinearLayout) findViewById(R.id.img_arrow_down);
		ivPullDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout,
//						BaseDroidApp.getInstanse().getCurrentAct());
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
				popupLayout.setVisibility(View.VISIBLE);
				layout_the_top.setVisibility(View.GONE);
				listContentLayout.setVisibility(View.VISIBLE);
			}
		});

		//查询布局
		layout_the_top = (RelativeLayout) this.findViewById(R.id.layout_the_top);
		listContentLayout = (LinearLayout) this
				.findViewById(R.id.dept_account_list_layout);//结果布局
		LinearLayout headerLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.dept_notmg_list_item, null);
		TextView cdNumberTv = (TextView) headerLayout
				.findViewById(R.id.dept_cd_number_tv);
		TextView currencyTv = (TextView) headerLayout
				.findViewById(R.id.dept_type_tv);
		TextView availableBalanceTv = (TextView) headerLayout
				.findViewById(R.id.dept_avaliable_balance_tv);
		ImageView goDetailIv = (ImageView) headerLayout
				.findViewById(R.id.dept_notify_detail_iv);
		String strCdNumber = this.getResources().getString(
				R.string.remitout_date);
		cdNumberTv.setText(strCdNumber);
		String strSaveType = this.getResources().getString(
				R.string.get_remit_name_title);
		// currencyTv.setText(strSaveType);
		currencyTv.setVisibility(View.INVISIBLE);
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		// currencyTv);
		// String strAvaiableBalance = this.getResources().getString(
		// R.string.remitout_state);
		availableBalanceTv.setText(strSaveType);
		goDetailIv.setVisibility(View.INVISIBLE);
		headerLayout.setClickable(false);
		listContentLayout.addView(headerLayout, 0);
		listView = (ListView) findViewById(R.id.dept_notmg_querylist);
		viewFooter = inflater.inflate(R.layout.acc_load_more, null);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
 		btnMore.setBackgroundColor(Color.TRANSPARENT);
//		btnMore.setVisibility(View.GONE);
		//setButtonMore(false);
		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestPsnMobileRemitQuery();
			}
		});

		initAfterLayout();
	}
	
	private void setButtonMore(boolean isShow){
		if(isShow){
			if(listView.getFooterViewsCount() <=0 ){
				listView.addFooterView(viewFooter);
			}
		}else{
			if(listView.getFooterViewsCount()>0){
				listView.removeFooterView(viewFooter);
			}
		}
	}

	/**
	 * 查询条件laytou
	 */
	private void initQueryBeforeLayout() {
		textStartDate = (TextView) popupLayout
				.findViewById(R.id.volume_number_tv);
		textEndDate = (TextView) popupLayout.findViewById(R.id.cd_number_tv);
		textStartDate.setOnClickListener(chooseDateClick);
		textEndDate.setOnClickListener(chooseDateClick);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(currentTime);
		textStartDate.setText(startThreeDate);
		// 系统当前时间格式化
		String currenttime = QueryDateUtils.getcurrentDate(currentTime);
		// 初始结束时间赋值
		textEndDate.setText(currenttime);
		// 查询按钮
		Button queryBtn = (Button) popupLayout.findViewById(R.id.dept_btnQuery);
		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseQuery();
			}
		});

		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				popupLayout.setVisibility(View.GONE);
				layout_the_top.setVisibility(View.VISIBLE);
				listContentLayout.setVisibility(View.VISIBLE);
			}
		});
		startDate = startThreeDate;
		endDate = currenttime;
		initAfterLayout();
	}

	/**
	 * @Title: excuseQuery
	 * @Description: 执行查询操作
	 * @param
	 * @return void
	 */
	private void excuseQuery() {
		int currentPositionPre = gallery.getSelectedItemPosition();
		String startDatePre = textStartDate.getText().toString().trim();
		String endDatePre = textEndDate.getText().toString().trim();
		if (QueryDateUtils.compareDateOneYear(startDatePre, currentTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					RemitQueryBeforeActivity.this
							.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(endDatePre, currentTime)) {
			// 结束日期在服务器日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					RemitQueryBeforeActivity.this
							.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startDatePre, endDatePre)) {
			// 开始日期在结束日期之前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					RemitQueryBeforeActivity.this
							.getString(R.string.acc_query_errordate));
			return;
		}
		if (!QueryDateUtils.compareDateThree(startDatePre, endDatePre)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_check_start_end_date));
			return;
		}
		if (StringUtil.isNullOrEmpty(accountList.get(currentPositionPre))) {
			return;
		}
		currentPosition = currentPositionPre;
		startDate = startDatePre;
		endDate = endDatePre;

		mCurrentIndex = 0;
		listData.clear();
		requestPsnMobileRemitQuery();
	}

	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {
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
					RemitQueryBeforeActivity.this, new OnDateSetListener() {

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

	/**
	 * 查询后layout
	 * 
	 * @param view
	 */
	private void initAfterLayout() {
		// 查询结果页面初始化
		String acountNO = (String) accountList.get(currentPosition).get(
				Comm.ACCOUNTNUMBER);
		accoutNumberText.setText(StringUtil.getForSixForString(String
				.valueOf(acountNO)));
		queryDateContent.setText(startDate + "-" + endDate);
	}

	/**
	 * 刷新通知存款 列表
	 */
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new RemitQueryResultListAdapter(context, listData);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(listViewItemClickListener);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * listView条目点击事件
	 */
	private OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			requestPsnMobileRemitDetailsQuery(position);
			theChoosePosition = position;
		}
	};

	/**
	 * @Title: requestPsnMobileRemitQuery
	 * @Description: 发送“汇款查询”接口请求
	 * @param
	 * @return void
	 */
	public void requestPsnMobileRemitQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_REMIT_QUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Comm.ACCOUNT_ID,
				accountList.get(currentPosition).get(Comm.ACCOUNT_ID));
		map.put(DrawMoney.START_DATE, startDate);
		map.put(DrawMoney.END_DATE, endDate);
		map.put(DrawMoney.CURRENT_INDEX, mCurrentIndex);
		// if(mCurrentIndex == 0){
		// map.put(DrawMoney.REFRESH, true);
		// }else{
		// map.put(DrawMoney.REFRESH, false);
		// }
		map.put(DrawMoney.PAGE_SIZE, pageSize);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileRemitQueryCallback");
	}

	/**
	 * @Title: requestPsnMobileRemitQueryCallback
	 * @Description: “汇款查询”接口请求的回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnMobileRemitQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) map.get(DrawMoney.RECORD_NUMBER);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) map
				.get(ConstantGloble.LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getResources().getString(R.string.no_list_data));
			listData.clear();
			showQueryResultView(false);
			return;
		} else {
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			popupLayout.setVisibility(View.GONE);
			layout_the_top.setVisibility(View.VISIBLE);
			listContentLayout.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		if (listData.size() >= totalCount) {
			//btnMore.setVisibility(View.GONE);
			setButtonMore(false);
		} else {
			mCurrentIndex += Integer.parseInt(pageSize);
			//btnMore.setVisibility(View.VISIBLE);
			setButtonMore(true);
		}
		DrawMoneyData.getInstance().setQueryCallBackList(listData);
		showQueryResultView(true);
	}

	/**
	 * @Title: requestPsnMobileRemitDetailsQuery
	 * @Description: 请求“汇款详情查询”接口
	 * @param
	 * @return void
	 */
	private void requestPsnMobileRemitDetailsQuery(int position) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_REMIT_DETAILS_QUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNullOrEmpty(listData)
				|| position > listData.size() - 1
				|| StringUtil.isNullOrEmpty(listData.get(position))) {
			return;
		}
		Map<String, Object> itemData = listData.get(position);
		map.put(DrawMoney.TRANSACTION_ID,
				itemData.get(DrawMoney.TRANSACTION_ID));
		map.put(DrawMoney.REMIT_STATUS, itemData.get(DrawMoney.REMIT_STATUS));
		map.put(DrawMoney.TRAN_DATE, itemData.get(DrawMoney.TRAN_DATE));
		map.put(DrawMoney.REMIT_NO, itemData.get(DrawMoney.REMIT_NO));
		map.put(DrawMoney.FROM_ACT_NUMBER,
				itemData.get(DrawMoney.FROM_ACT_NUMBER));
		map.put(DrawMoney.BRANCH_ID, itemData.get(DrawMoney.BRANCH_ID));
		map.put(DrawMoney.FROM_ACT_TYPE, itemData.get(DrawMoney.FROM_ACT_TYPE));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileRemitDetailsQueryCallback");
	}

	/**
	 * @Title: requestPsnMobileRemitDetailsQueryCallback
	 * @Description: TODO 请求“汇款详情查询”接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnMobileRemitDetailsQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		String remitStatusStr = (String) map.get(DrawMoney.REMIT_STATUS);
		//有效期至
		String dueDate = (String) map.get(DrawMoney.DUE_DATE);
		
		if ("B".equals(remitStatusStr)) {
			DrawMoneyData.getInstance().setQueryResultDetail(
					listData.get(theChoosePosition));
		} else {
			DrawMoneyData.getInstance().setQueryResultDetail(map);
		}
		Intent intent = new Intent(RemitQueryBeforeActivity.this,
				RemitQueryInfoActivity.class);
		intent.putExtra("channel", (String)listData.get(theChoosePosition).get(DrawMoney.CHANNEL));
		intent.putExtra("dueDate", dueDate);
		startActivityForResult(intent, 101);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 101 && resultCode == RESULT_OK) {
			// excuseQuery();
			// requestCommConversationId();
			mCurrentIndex = 0;
			listData.clear();
			requestPsnMobileRemitQuery();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		excuseQuery();
	}

	/**
	 * @Title: showQueryResultView
	 * @Description: 展示查询后的页面
	 * @param
	 * @return void
	 */
	private void showQueryResultView(boolean isDissPop) {
		if (isDissPop) {
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			popupLayout.setVisibility(View.GONE);
			layout_the_top.setVisibility(View.VISIBLE);
			listContentLayout.setVisibility(View.VISIBLE);
			queryDateContent.setText(startDate + "-" + endDate);
			ivPullUp.setClickable(true);
			initAfterLayout();
			refreshListView(listData);
		} else {
			listContentLayout.setVisibility(View.VISIBLE);
			queryDateContent.setText(startDate + "-" + endDate);
			ivPullUp.setClickable(false);
			initAfterLayout();
			refreshListView(listData);
		}

	}

	/** 排序点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				sortTextBtn.setText(LocalData.filterRemitStatus[0]);
				refreshListView(listData);
				break;
			case R.id.tv_text2:
				sortTextBtn.setText(LocalData.filterRemitStatus[1]);
				List<Map<String, Object>> otherData = new ArrayList<Map<String, Object>>();
				// 汇款成功
				for (int i = 0; i < listData.size(); i++) {
					String remitStatus = (String) listData.get(i).get(
							DrawMoney.REMIT_STATUS);
					if (remitStatus.equals("A")) {
						otherData.add(listData.get(i));
					}
				}
				refreshListView(otherData);
				break;
			case R.id.tv_text3:
				sortTextBtn.setText(LocalData.filterRemitStatus[2]);
				List<Map<String, Object>> otherData2 = new ArrayList<Map<String, Object>>();
				// 汇款失败
				for (int i = 0; i < listData.size(); i++) {
					String remitStatus = (String) listData.get(i).get(
							DrawMoney.REMIT_STATUS);
					if (remitStatus.equals("B")) {
						otherData2.add(listData.get(i));
					}
				}
				refreshListView(otherData2);
				break;
			default:
				break;
			}
		}
	};

}
