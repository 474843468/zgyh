package com.chinamworld.bocmbci.biz.drawmoney.drawquery;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivitySwitcher;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.biz.drawmoney.remitquery.RemitQueryResultListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DrawQueryChooseCardAcitivity
 * @Description: 取款查询的初始页面
 * @author JiangWei
 * @date 2013-7-15 下午4:01:17
 */
public class DrawQueryAcitivity extends DrawBaseActivity {

	private LayoutInflater inflater = null;
	private Context context = this;

	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	/** 查询前layout */
	private RelativeLayout beforeQueryLayout;
	/** 查询后layout */
	private RelativeLayout afterQueryLayout;
	/** 底部listview Layout */
	private LinearLayout listContentLayout;

	private TextView queryDateContent;

	private ListView listView;
	/** 下拉箭头 */
	private LinearLayout ivPullDown;
	/** 收起箭头 */
	private LinearLayout ivPullUp;
	/** 排序button */
	private Button sortTextBtn;
	/** 开始日期str */
	private String startDate;
	/** 结束日期str */
	private String endDate;
	/** 代理点取款账户str */
	private String agencyAcountStr;
	/** 开始日期 */
	private TextView textStartDate;
	/** 结束日期 */
	private TextView textEndDate;
	/** 下拉布局 */
	private LinearLayout popupLayout;
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
	private Animation animation_down;
	/** listview的更多条目 */
	private View viewFooter;
	private Button btnMore;
	/** listview的adapter */
	private RemitQueryResultListAdapter adapter;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	/** 总条目数 */
	private int totalCount = 0;
	private String pageSize = "10";
	/** listview的头部 */
	LinearLayout headerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.remitout_query);
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.drawmoney_drawquery_layout, null);
		tabcontent.addView(view);
		setTitle(R.string.draw_query_title);

		((Button) findViewById(R.id.ib_top_right_btn)).setVisibility(View.GONE);

		popupLayout = (LinearLayout) view.findViewById(R.id.layout_notmg_pop);
		dateTime = this.getIntent().getStringExtra(CURRENT_DATETIME);

		tabcontent.setPadding(
				0,
				0,
				0,
				getResources().getDimensionPixelSize(
						R.dimen.common_bottom_padding_new));

		boolean isFromShortCut = this.getIntent().getBooleanExtra(
				ConstantGloble.COMEFROMFOOTFAST, false);
		if (isFromShortCut) {
			tabcontent.setVisibility(View.GONE);
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestDrawPsnMobileIsSignedAgent();
		} else {
			// 初始化控件
			initanimation();
			init();
			//excuseQuery();
		}
	}

	/**
	 * @Title: initanimation
	 * @Description: 初始化动画
	 * @param
	 * @return void
	 */
	@SuppressWarnings("static-access")
	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(this,
				R.anim.scale_out);
		animation_down = new AnimationUtils().loadAnimation(this,
				R.anim.scale_in);
	}

	/**
	 * @Title: init
	 * @Description: 初始化view和控件
	 * @param
	 * @return void
	 */
	private void init() {
		((LinearLayout) popupLayout.findViewById(R.id.top_layout))
				.setVisibility(View.GONE);
		// 收起箭头
		ivPullUp = (LinearLayout) popupLayout.findViewById(R.id.img_arrow_up);
		initQueryBeforeLayout();
		initQueryAfterLayout();
	}

	/**
	 * 查询后layout
	 */
	private void initQueryAfterLayout() {
		afterQueryLayout = (RelativeLayout) findViewById(R.id.dept_after_query_layout);
		//隐藏排序布局，调整位置
		findViewById(R.id.dept_ll_sort).setVisibility(View.GONE);
		// 下拉箭头
		ivPullDown = (LinearLayout) findViewById(R.id.img_arrow_down);
		ivPullDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				animation_down.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						popupLayout.setVisibility(View.VISIBLE);
					}
				});
				popupLayout.startAnimation(animation_down);
				afterQueryLayout.invalidate();
			}
		});
		listContentLayout = (LinearLayout) this
				.findViewById(R.id.dept_account_list_layout);
		initListViewHeaderView();
		listView = (ListView) findViewById(R.id.dept_notmg_querylist);
		viewFooter = inflater.inflate(R.layout.acc_load_more, null);
		listView.addFooterView(viewFooter);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		btnMore.setBackgroundColor(Color.TRANSPARENT);
		btnMore.setVisibility(View.GONE);
		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestPsnMobileWithdrawalQuery();
			}
		});

		initAfterLayout();
	}

	/**
	 * 查询条件laytou
	 */
	private void initQueryBeforeLayout() {
		beforeQueryLayout = (RelativeLayout) popupLayout
				.findViewById(R.id.dept_before_query_layout);

		((Spinner) beforeQueryLayout
				.findViewById(R.id.dept_volume_number_spinner))
				.setVisibility(View.GONE);
		((Spinner) beforeQueryLayout.findViewById(R.id.dept_cd_number_spinner))
				.setVisibility(View.GONE);
		textStartDate = (TextView) beforeQueryLayout
				.findViewById(R.id.volume_number_tv);
		textEndDate = (TextView) beforeQueryLayout
				.findViewById(R.id.cd_number_tv);
		textEndDate.setPadding(
				getResources().getDimensionPixelSize(R.dimen.btnpaddinglr), 0,
				0, 0);
		textStartDate.setVisibility(View.VISIBLE);
		textEndDate.setVisibility(View.VISIBLE);
		textStartDate.setOnClickListener(chooseDateClick);
		textEndDate.setOnClickListener(chooseDateClick);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		textStartDate.setText(startThreeDate);
		// 系统当前时间格式化
		String currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		textEndDate.setText(currenttime);

		// 查询按钮
		Button queryBtn = (Button) beforeQueryLayout
				.findViewById(R.id.dept_btnQuery);
		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseQuery();
			}
		});

		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animation_up.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						popupLayout.setVisibility(View.GONE);
					}
				});
				popupLayout.startAnimation(animation_up);

			}
		});
		ivPullUp.setClickable(false);
		startDate = startThreeDate;
		endDate = currenttime;

	}

	/**
	 * @Title: excuseQuery
	 * @Description: 执行查询操作
	 * @param
	 * @return void
	 */
	private void excuseQuery() {
		String startDatePre = textStartDate.getText().toString().trim();
		String endDatePre = textEndDate.getText().toString().trim();
		if (QueryDateUtils.compareDateOneYear(startDatePre, dateTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					DrawQueryAcitivity.this
							.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(endDatePre, dateTime)) {
			// 结束日期在服务器日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					DrawQueryAcitivity.this
							.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startDatePre, endDatePre)) {
			// 开始日期在结束日期之前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					DrawQueryAcitivity.this
							.getString(R.string.acc_query_errordate));
			return;
		}
		if (!QueryDateUtils.compareDateThree(startDatePre, endDatePre)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_check_start_end_date));
			return;
		}
		startDate = startDatePre;
		endDate = endDatePre;
		mCurrentIndex = 0;
		listData.clear();
		requestPsnMobileWithdrawalQuery();
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
					DrawQueryAcitivity.this, new OnDateSetListener() {

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
		LinearLayout firstLineLayout = (LinearLayout) findViewById(R.id.layout_first_line);
		LinearLayout secondLineLayout = (LinearLayout) findViewById(R.id.layout_second_line);
		TextView thirdLine = (TextView) findViewById(R.id.label_third_line);
		firstLineLayout.setVisibility(View.GONE);
		secondLineLayout.setVisibility(View.GONE);
		thirdLine.setText(R.string.remit_query_time);
		queryDateContent = (TextView) findViewById(R.id.dept_query_cdnumber_tv);
		queryDateContent.setText(startDate + "-" + endDate);
		sortTextBtn = (Button) findViewById(R.id.sort_text);
		sortTextBtn.setVisibility(View.INVISIBLE);
	}

	/**
	 * 刷新 列表
	 */
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new RemitQueryResultListAdapter(context, listData);
			adapter.setIsDraw(true);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(listViewItemClickListener);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}

	}

	/**
	 * 初始化listview头视图view
	 * 
	 * @return
	 */
	private void initListViewHeaderView() {
		headerLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.dept_notmg_list_item, null);
		TextView cdNumberTv = (TextView) headerLayout
				.findViewById(R.id.dept_cd_number_tv);
		TextView currencyTv = (TextView) headerLayout
				.findViewById(R.id.dept_type_tv);
		TextView availableBalanceTv = (TextView) headerLayout
				.findViewById(R.id.dept_avaliable_balance_tv);
		ImageView goDetailIv = (ImageView) headerLayout
				.findViewById(R.id.dept_notify_detail_iv);
		String strCdNumber = this.getResources().getString(
				R.string.get_remit_date_no_label);
		cdNumberTv.setText(strCdNumber);
		String strSaveType = this.getResources().getString(
				R.string.get_remit_name_title);
		currencyTv.setText(strSaveType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				currencyTv);
		String strAvaiableBalance = this.getResources().getString(
				R.string.remitout_no_no_label);
		availableBalanceTv.setText(strAvaiableBalance);
		goDetailIv.setVisibility(View.INVISIBLE);
		headerLayout.setClickable(false);
		listContentLayout.addView(headerLayout, 0);
	}

	/**
	 * listView条目点击事件
	 */
	private OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			DrawMoneyData.getInstance().setQueryResultDetail(
					listData.get(position));
			requestPsnMobileWithdrawalDetailsQuery(position);
		}
	};

	/**
	 * @Title: requestPsnMobileWithdrawalQuery
	 * @Description: 发送“汇款解付查询”接口请求
	 * @param
	 * @return void
	 */
	public void requestPsnMobileWithdrawalQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_WITH_DRAWAL_QUERY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DrawMoney.START_DATE, startDate);
		map.put(DrawMoney.END_DATE, endDate);
		map.put(DrawMoney.CURRENT_INDEX, mCurrentIndex);
		map.put(DrawMoney.PAGE_SIZE, pageSize);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileWithdrawalQueryCallback");
	}

	/**
	 * @Title: requestPsnMobileWithdrawalQueryCallback
	 * @Description: “汇款解付查询”接口请求的回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnMobileWithdrawalQueryCallback(Object resultObj) {
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
		agencyAcountStr = (String) map.get(DrawMoney.AGENT_ACCT_NUMBER);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) map
				.get(ConstantGloble.LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getResources().getString(R.string.no_list_data));
		}
		// if(StringUtil.isNullOrEmpty(resultList)){
		// resultList = (List<Map<String, Object>>) map.get("list");
		// }
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		DrawMoneyData.getInstance().setQueryCallBackList(listData);
		if (listData.size() >= totalCount) {
			btnMore.setVisibility(View.GONE);
		} else {
			mCurrentIndex += Integer.parseInt(pageSize);
			btnMore.setVisibility(View.VISIBLE);
		}
		if (listData != null && !listData.isEmpty()) {
			listView.setVisibility(View.VISIBLE);
			ivPullUp.setClickable(true);
			if (headerLayout != null) {
				headerLayout.setVisibility(View.VISIBLE);
			}
			if (mCurrentIndex == 0
					|| mCurrentIndex == Integer.parseInt(pageSize)) {
				showQueryResultView(true);
			} else {
				showQueryResultView(false);
			}
		} else {
			ivPullUp.setClickable(false);
			if (headerLayout != null) {
				headerLayout.setVisibility(View.GONE);
			}
			listView.setVisibility(View.GONE);
		}
		// listData = (List<Map<String, Object>>) map.get(ConstantGloble.LIST);
		// if(StringUtil.isNullOrEmpty(listData)){
		// listData = (List<Map<String, Object>>) map.get("List");
		// }

	}

	/**
	 * @Title: requestPsnMobileWithdrawalDetailsQuery
	 * @Description: 请求“汇款解付详情查询”接口
	 * @param
	 * @return void
	 */
	private void requestPsnMobileWithdrawalDetailsQuery(int position) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(DrawMoney.PSN_MOBILE_WITH_DRAWAL_DETAILS_QUERY);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNullOrEmpty(listData)
				|| position > listData.size() - 1
				|| StringUtil.isNullOrEmpty(listData.get(position))) {
			return;
		}
		Map<String, Object> itemData = listData.get(position);
		map.put(DrawMoney.TRANSACTION_ID,
				itemData.get(DrawMoney.TRANSACTION_ID));
		map.put(DrawMoney.RECEIPT_AMOUNT, itemData.get(DrawMoney.REMIT_AMOUNT));
		// map.put(DrawMoney.CURRENY_CODE,
		// itemData.get(DrawMoney.CURRENY_CODE));
		map.put(DrawMoney.CURRENY_CODE, "001");
		map.put(DrawMoney.PAYEE_MOBILE, itemData.get(DrawMoney.PAYEE_MOBILE));
		map.put(DrawMoney.PAYEE_NAME, itemData.get(DrawMoney.PAYEE_NAME));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileWithdrawalDetailsQueryCallback");
	}

	/**
	 * @Title: requestPsnMobileWithdrawalDetailsQueryCallback
	 * @Description: TODO 请求“汇款解付详情查询”接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnMobileWithdrawalDetailsQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String getRemitStatus = (String) biiResponseBody.getResult();
		// DrawMoneyData.getInstance().setQueryResultDetail(map);
		Intent intent = new Intent(DrawQueryAcitivity.this,
				DrawQueryInfoActivity.class);
		intent.putExtra(DrawMoney.AGENT_ACCT_NUMBER, agencyAcountStr);
		intent.putExtra(DrawMoney.REMIT_STATUS, getRemitStatus);
		startActivity(intent);
	}

	/**
	 * @Title: showQueryResultView
	 * @Description: 展示查询后的页面
	 * @param
	 * @return void
	 */
	private void showQueryResultView(boolean isFirstPage) {
		if (isFirstPage) {
			animation_up.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {

					popupLayout.setVisibility(View.GONE);
				}
			});
			popupLayout.startAnimation(animation_up);
			listContentLayout.setVisibility(View.VISIBLE);
			// 查询后显示 “代理点收款账户”
			((LinearLayout) findViewById(R.id.layout_second_line))
					.setVisibility(View.VISIBLE);
			TextView textLabel = (TextView) findViewById(R.id.label_second_line);
			TextView textAgencyAcount = (TextView) findViewById(R.id.dept_query_volumenumber_tv);
			textLabel.setText(R.string.remit_query_time);
			textAgencyAcount.setText(startDate + "-" + endDate);

			TextView textThirdLabel = (TextView) findViewById(R.id.label_third_line);
			TextView textThirdContent = (TextView) findViewById(R.id.dept_query_cdnumber_tv);
			textThirdLabel.setText(R.string.agence_payee_account);
			if (agencyAcountStr != null && !agencyAcountStr.equals("")) {
				textThirdContent.setText(StringUtil.getForSixForString(String
						.valueOf(agencyAcountStr)));
				textThirdContent.setVisibility(View.VISIBLE);
				textThirdLabel.setVisibility(View.VISIBLE);
			} else {
				textThirdContent.setVisibility(View.GONE);
				textThirdLabel.setVisibility(View.GONE);
			}

			// queryDateContent.setText(startDate + "-" + endDate);
			ivPullUp.setClickable(true);
			refreshListView(listData);
		} else {
			refreshListView(listData);
		}

	}

	/**
	 * @Title: requestSystemDateTimeForDrawQuery
	 * @Description:请求系统时间
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestSystemDateTimeForDrawQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_SYSTEM_TIME);
		HttpManager.requestBii(biiRequestBody, this,
				"requestSystemDateTimeForCallBack");
	}

	/**
	 * @Title: requestSystemDateTimeForCallBack
	 * @Description:请求系统时间的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	public void requestSystemDateTimeForCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		// 初始化控件
		tabcontent.setVisibility(View.VISIBLE);
		initanimation();
		init();
		excuseQuery();
	}

	/**
	 * 通讯返回
	 */
	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case 1:// 查询通知详情返回
				// 收起动画
			animation_up.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {

					popupLayout.setVisibility(View.GONE);
				}
			});
			popupLayout.startAnimation(animation_up);
			listContentLayout.setVisibility(View.VISIBLE);
			ivPullUp.setClickable(true);

			refreshListView(listData);
			break;
		case 2:// 查询通知存款详情返回
			initQueryBeforeLayout();
		case GET_IS_SIGNED_CALLBACK:
			// 加载左边菜单栏
			if (ActivitySwitcher.isSigned) {
				initLeftSideList(this, LocalData.DrawMoneyLeftList);
			} else {
				initLeftSideList(this, LocalData.DrawMoneyLeftListNoSigned);
			}
			if (!ActivitySwitcher.isSigned) {
				BaseDroidApp.getInstanse().showMessageDialog(
						this.getResources().getString(R.string.no_sign_agency),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
//								ActivityTaskManager.getInstance()
//										.removeAllActivity();
//								Intent intent = new Intent();
//								intent.setClass(BaseDroidApp.getInstanse()
//										.getCurrentAct(), MainActivity.class);
//								BaseDroidApp.getInstanse().getCurrentAct()
//										.startActivity(intent);
								goToMainActivity();
							}
						});
				break;
			} else {
				requestSystemDateTimeForDrawQuery();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 100) {
			// String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
			// requestQueryNotify(accountId, volumeNumber, cdNumber);
		}
	}

}
