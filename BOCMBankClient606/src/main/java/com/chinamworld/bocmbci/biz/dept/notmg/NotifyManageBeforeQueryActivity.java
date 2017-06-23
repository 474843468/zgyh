package com.chinamworld.bocmbci.biz.dept.notmg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.NotMgNotifyListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;
import com.chinamworld.bocmbci.widget.adapter.SpinnerAdapter;

/**
 * @ClassName: NotifyManageBeforeQueryActivity
 * @Description: TODO
 * @author JiangWei
 * @date 2013-7-2 下午2:48:26
 */
public class NotifyManageBeforeQueryActivity extends DeptBaseActivity {
	private LayoutInflater inflater = null;
	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	/** 当前账户对应的详情内容 */
	private List<Map<String, String>> accountDetaiList;
	/** 存折存单关联数据 */
	private List<Map<String, Object>> volumesAndCdnumbers;
	/** 转出账户layout */
	// private LinearLayout accountLayout;
	/** 侧折册号列表 */
	private List<String> volumes;
	/** 存单号 */
	// private List<String> cdnums;
	/** 存折册号 */
	private String volumeNumber;
	/** 存单号 */
	private String cdNumber;
	private List<Map<String, Object>> listData;
	/** 当前存折列表选择位置 */
	// private int currentVolumePosition;
	/** 查询前layout */
	private RelativeLayout beforeQueryLayout;
	/** 查询后layout */
	private RelativeLayout afterQueryLayout;
	/** 底部listview Layout */
	private LinearLayout listContentLayout;
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
	/** 存折册号 */
	private TextView volumeNumberTv;
	/** 存折册号 */
	private TextView cdNumberTv;
	/** 组装数据中 当前选中的list */
	private List<Map<String, Object>> currentList;
	/** 排序button */
	private Button sortTextBtn;
	/** 横向滑动listitem */
	private CustomGallery gallery;
	/** 存单序号 */
	private Spinner banksheetSpinner;
	// 存折册号下拉列表
	private Spinner volumeSpinner;
	/** 下拉布局 */
	private LinearLayout popupLayout;
	/** 查询后布局 */
	private LinearLayout topupLayout;
	/** 存折册号adapter */
	private SpinnerAdapter volumeSpinnerAdapter;
	/** 存单序号adapter */
	private SpinnerAdapter cdSpinnerAdapter;
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
	private Animation animation_down;
	/** 过滤后listview的数据*/
//	private List<Map<String, Object>> otherData;
	private boolean isFirst = true;
	//@renh 仿spinner上的textview
	private TextView tvspinner_volume;
	private TextView tvspinner_cd;
	private int position = 0;//记录选择的是那一项
	private Dialog dialog;
	/** 首次弹出下拉框 */
	private boolean isFirstPop = true;
	/** listView头部布局*/
	private LinearLayout headerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.notify_manage);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.dept_notmg_query_before, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		// add by luqp 2016年3月31日 修改查询条件遮挡ListView
		popupLayout = (LinearLayout) view.findViewById(R.id.layout_notmg_pop);
		topupLayout = (LinearLayout) view.findViewById(R.id.layout_the_top);
//		popupLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dept_notmg_query_popwindow, null);

		currentPosition = this.getIntent().getIntExtra(CURRENT_POSITION, -1);
		tabcontent.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));
		// 初始化控件
		initanimation();
		init();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(isFirstPop){
			if(hasFocus){
//				PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
//				requestPsnMobileRemitQuery();
				isFirstPop = false;
			}
		}
	}

	/**
	 * @Title: initanimation
	 * @Description: 初始化动画
	 * @param
	 * @return void
	 */
	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(this, R.anim.scale_out);
		animation_down = new AnimationUtils().loadAnimation(this, R.anim.scale_in);
	}

	/** 初始化控件*/
	private void init() {
		initPopLayout();
		initQueryAfterLayout();
		BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
		if(activity != null)
			PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout, activity);
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
	}

	/** 初始化查询条件框*/
	private void initPopLayout() {
		initHeaderLayout();
		// 收起箭头
		ivPullUp = (LinearLayout) popupLayout.findViewById(R.id.img_arrow_up);// TODO
		//声明仿spinner的textview
		tvspinner_volume = (TextView)popupLayout.findViewById(R.id.volume_number_tv);
		tvspinner_cd = (TextView)popupLayout.findViewById(R.id.cd_number_tv);
		tvspinner_cd.setVisibility(View.VISIBLE);
		tvspinner_cd.setBackgroundResource(R.drawable.bg_spinner_default);
		tvspinner_volume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog = onCreateDialog(1);
				if(dialog != null && !dialog.isShowing()){
					dialog.show();
					tvspinner_volume.setClickable(false);
					dialog.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							tvspinner_volume.setClickable(true);
						}
					});
				}
			}
		});
		tvspinner_volume.setVisibility(View.VISIBLE);
		initQueryBeforeLayout();
		banksheetSpinner.setEnabled(false);
	}

	/** 查询后layout*/
	private void initQueryAfterLayout() {
		afterQueryLayout = (RelativeLayout) view.findViewById(R.id.dept_after_query_layout);

		// 下拉箭头
		ivPullDown = (LinearLayout) view.findViewById(R.id.img_arrow_down);
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
						// add by luqp 2016年3月31日 修改查询条件遮挡ListView
						popupLayout.setVisibility(View.VISIBLE);
						topupLayout.setVisibility(View.GONE);
					}
				});
				popupLayout.startAnimation(animation_down);
//				afterQueryLayout.invalidate();
//				PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout,
//						BaseDroidApp.getInstanse().getCurrentAct());
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
			}
		});
		listContentLayout = (LinearLayout) this.findViewById(R.id.dept_account_list_layout);
		initListViewHeaderView();
		listView = (ListView) view.findViewById(R.id.dept_notmg_querylist);
		initAfterLayout();
	}

	/** 初始化 存折号数据*/
	@SuppressWarnings("unchecked")
	private void initVolumeData() {
		// 返回数据里面 存折号和存单是没有关联的 重新组装数据
		// List<Map<String,Object>>
		accountDetaiList = (List<Map<String, String>>) DeptDataCenter.getInstance().getCurDetailContent().get(ConstantGloble.ACC_DETAILIST);
		volumesAndCdnumbers = new ArrayList<Map<String, Object>>();
		volumes = new ArrayList<String>();
		// 找出列表中所有不相同的存折册号
		for (int i = 0; i < accountDetaiList.size(); i++) {
			Map<String, String> detaimap = accountDetaiList.get(i);
			String volumeNumber = detaimap.get(Dept.VOLUME_NUMBER);
			if (volumes.size() > 0) {
				if (!volumes.contains(volumeNumber)) {
					volumes.add(volumeNumber);
				}
			} else {
				volumes.add(volumeNumber);
			}
		}
		
		// 讲存折册号 和 存单号 对应起来
		for (int i = 0; i < volumes.size(); i++) {
			ArrayList<Map<String, Object>> list = null;
			list = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapContent = null;
			for (int j = 0; j < accountDetaiList.size(); j++) {
				Map<String, String> detaimap = accountDetaiList.get(j);
				String volumeNumber = detaimap.get(Dept.VOLUME_NUMBER);
				String cdNumber = detaimap.get(Dept.CD_NUMBER);
				String convertType = detaimap.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
				String status = detaimap.get(Dept.STATUS);
				if (volumeNumber.equals(volumes.get(i)) && ConstantGloble.CONVERTTYPE_N.equals(convertType)
						&&(status.equals("00") || status.equals("V"))) {// 非约定转存才加
					mapContent = new HashMap<String, Object>();
					mapContent.put(ConstantGloble.VOL, cdNumber);
					mapContent.put(ConstantGloble.CONTENT, accountDetaiList.get(j));
					list.add(mapContent);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Dept.VOLUME_NUMBER, volumes.get(i));
			map.put(Dept.CD_NUMBER, list);
			volumesAndCdnumbers.add(map);
		}
	}

	/** 转出账户layout*/
	private void initHeaderLayout() {
		gallery = (CustomGallery) popupLayout.findViewById(R.id.viewPager);
		img_arrow_left = (ImageView) popupLayout.findViewById(R.id.img_arrow_left);
		img_arrow_right = (ImageView) popupLayout.findViewById(R.id.img_arrow_right);
		accountList = DeptDataCenter.getInstance().getAccountList();
		NotifyGalleryAdapter adapter = new NotifyGalleryAdapter(this, accountList);
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
			public void onItemSelected(AdapterView<?> arg0, View arg1, int currentPage, long arg3) {
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
				currentPosition = currentPage;
				//add by wjp 2013年8月10日16:14:27
				banksheetSpinner.setEnabled(false);
				if(!isFirst){
					DeptDataCenter.getInstance().setAccOutInfoMap(accountList.get(currentPage));
					String accountId = (String) accountList.get(currentPage).get(Comm.ACCOUNT_ID);
					requestQueryNotifyAccountDetail(accountId);
					tvspinner_volume.setText(R.string.forex_custoner_fix_volunber);
					tvspinner_cd.setVisibility(View.VISIBLE);
					tvspinner_volume.setClickable(false);
					volumeNumber = "";
					if(listData != null){
						listData.clear();
						refreshListView(listData);
					}
					tvspinner_volume.setBackgroundResource(R.drawable.bg_spinner_default);
					tvspinner_volume.setClickable(false);
					ivPullUp.setClickable(false);
					initAfterLayout();
				}else{
					//加载完成之后 改标记  第一次进来不需要通讯区查详情
					isFirst = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/** 查询条件laytou*/
	private void initQueryBeforeLayout() {
		initVolumeData();
		beforeQueryLayout = (RelativeLayout) popupLayout.findViewById(R.id.dept_before_query_layout);
		initBeforeLayout(beforeQueryLayout);
	}

	/**
	 * 查询前layout
	 * @param view
	 */
	private void initBeforeLayout(View view) {
		// 存折册号下拉列表
		volumeSpinner = (Spinner) view.findViewById(R.id.dept_volume_number_spinner);
		volumeSpinner.setVisibility(View.GONE);
		// Spinner 存单号
		banksheetSpinner = (Spinner) view.findViewById(R.id.dept_cd_number_spinner);
		String strVolume = this.getResources().getString(R.string.forex_custoner_fix_volunber);
		volumeSpinnerAdapter = new SpinnerAdapter(context, volumes, strVolume);
		volumeSpinner.setAdapter(volumeSpinnerAdapter);
		volumeSpinner.setOnItemSelectedListener(volumeSpinnerListener);
//		volumeSpinner.setOnTouchListener(volumeSpinnerTouchListener);
//		banksheetSpinner.setOnTouchListener(cdSpinnerTouchListener);
		banksheetSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				@SuppressWarnings("unchecked")
				Map<String, Object> content = (Map<String, Object>) currentList.get(position).get(ConstantGloble.CONTENT);
				DeptDataCenter.getInstance().setCurDetailContent(content);
				cdNumber = (String) content.get(Dept.CD_NUMBER);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				@SuppressWarnings("unchecked")
				Map<String, Object> content = (Map<String, Object>) currentList.get(0).get(ConstantGloble.CONTENT);
				DeptDataCenter.getInstance().setCurDetailContent(content);
				cdNumber = (String) content.get(Dept.CD_NUMBER);
			}
		});

		// 查询按钮
		Button queryBtn = (Button) view.findViewById(R.id.dept_btnQuery);
		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(listData != null){
					listData.clear();
					refreshListView(listData);
				}
				if (volumeNumber != null && !volumeNumber.equals("") && cdSpinnerAdapter.isSelected()) {
					// 查询通知详情
					String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
					requestQueryNotify(accountId, volumeNumber, cdNumber);

					// TODO 改变存折和存单号 显示
					volumeNumberTv.setText(volumeNumber);
					cdNumberTv.setText(cdNumber);
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(NotifyManageBeforeQueryActivity.this.getResources().getString(R.string.choose_volume_and_cd));
				}

			}
		});

		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				// =========================================================
				animation_down.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// add by luqp 2016年3月31日 修改查询条件遮挡ListView
						popupLayout.setVisibility(View.GONE);
						topupLayout.setVisibility(View.VISIBLE);
					}
				});
				popupLayout.startAnimation(animation_down);
				// ========================================================
			}
		});
		ivPullUp.setClickable(false);
	}

	/**
	 * 查询后layout
	 * @param view
	 */
	private void initAfterLayout() {
		accOutInfoMap = DeptDataCenter.getInstance().getAccOutInfoMap();

		TextView accNoTv = (TextView) view.findViewById(R.id.dept_query_no_tv);
		String strAccNo = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accNoTv.setText(StringUtil.getForSixForString(strAccNo));
		volumeNumberTv = (TextView) view.findViewById(R.id.dept_query_volumenumber_tv);
		cdNumberTv = (TextView) view.findViewById(R.id.dept_query_cdnumber_tv);
		// 查询结果页面初始化
		sortTextBtn = (Button) view.findViewById(R.id.sort_text);
		sortTextBtn.setText(LocalData.deptSortMap[0]);
	}

	/** 刷新通知存款 列表*/
	private void refreshListView(List<Map<String, Object>> listData) {
		NotMgNotifyListAdapter nlAdapter = new NotMgNotifyListAdapter(context, listData);
		listView.setAdapter(nlAdapter);
		listView.setOnItemClickListener(listViewItemClickListener);
	}

	/**
	 * 初始化listview头视图view
	 * @returns
	 */
	private void initListViewHeaderView() {
		headerLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dept_notmg_list_item, null);
		TextView cdNumberTv = (TextView) headerLayout.findViewById(R.id.dept_cd_number_tv);
		TextView currencyTv = (TextView) headerLayout.findViewById(R.id.dept_type_tv);
		TextView availableBalanceTv = (TextView) headerLayout.findViewById(R.id.dept_avaliable_balance_tv);
		ImageView goDetailIv = (ImageView) headerLayout.findViewById(R.id.dept_notify_detail_iv);
		String strCdNumber = this.getResources().getString(R.string.notice_no_message);
		cdNumberTv.setText(strCdNumber);
		String strSaveType = this.getResources().getString(R.string.promise_checkout_day_tv);
		currencyTv.setText(strSaveType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, currencyTv);
		String strAvaiableBalance = this.getResources().getString(R.string.notify_status);
		availableBalanceTv.setText(strAvaiableBalance);
		goDetailIv.setVisibility(View.INVISIBLE);
		headerLayout.setClickable(false);
		listContentLayout.addView(headerLayout, 0);
	}

	/** listView条目点击事件*/
	private OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			DeptDataCenter.getInstance().setCurNotifyDetail(listData.get(position));
			Intent intent = new Intent();
			intent.putExtra(Dept.VOLUME_NUMBER, volumeNumber);
			intent.putExtra(Dept.CD_NUMBER, cdNumber);
			intent.setClass(NotifyManageBeforeQueryActivity.this, NotifyManageInfoConfirmActivity.class);
			startActivityForResult(intent, 100);
		}
	};

	/** 通讯返回*/
	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case QUERY_NOTIFY_ACCOUNT_DETAIL_CALLBACK:// 查询通知详情返回
			listData = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
			if(StringUtil.isNullOrEmpty(listData)){
				//提示
				ivPullUp.setClickable(false);
				headerLayout.setVisibility(View.GONE);
				String message = this.getString(R.string.dept_query_no_data);
				BaseDroidApp.getInstanse().showInfoMessageDialog(message);
				break;
			}
			headerLayout.setVisibility(View.VISIBLE);
			// 收起动画
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
					topupLayout.setVisibility(View.VISIBLE);
				}
			});
			popupLayout.startAnimation(animation_up);
			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			listContentLayout.setVisibility(View.VISIBLE);
			ivPullUp.setClickable(true);

			// 通知编号list
			List<String> noticeIdList = new ArrayList<String>();
			for (int i = 0; i < listData.size(); i++) {
				String noticeNo = (String) listData.get(i).get(Dept.NOTIFY_ID);
				if (!StringUtil.isNullOrEmpty(noticeNo)) {
					noticeIdList.add(noticeNo);
				}
			}
			DeptDataCenter.getInstance().setNoticeIdList(noticeIdList);
//			otherData = new ArrayList<Map<String, Object>>();
//			//只保留 a w o 三种状态
//			for (int i = 0; i < listData.size(); i++) {
//				String notifyStatus = (String) listData.get(i).get(Dept.NOTIFY_STATUS);
//				if (notifyStatus.equals("A") || notifyStatus.equals("W") || notifyStatus.equals("O")) {
////					listData.remove(i);
//					otherData.add(listData.get(i));
//				}
//			}
			refreshListView(listData);
			break;
		case QUERY_ACCOUNT_DETAIL_CALLBACK:// 查询通知存款详情返回
			if(listData != null){
				listData.clear();
				refreshListView(listData);
			}
			if(checkCdNumber()){
				tvspinner_volume.setClickable(true);
				tvspinner_volume.setBackgroundResource(R.drawable.bg_spinner);
				ivPullUp.setClickable(true);
				initQueryBeforeLayout();
			}else{
				tvspinner_volume.setVisibility(View.VISIBLE);
				tvspinner_volume.setBackgroundResource(R.drawable.bg_spinner_default);
				tvspinner_volume.setClickable(false);
				ivPullUp.setClickable(false);
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.slide_no_cdnumber));
			}
		default:
			break;
		}
	}

	private OnItemSelectedListener volumeSpinnerListener = new OnItemSelectedListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// currentVolumePosition = position;
			volumeNumber = volumes.get(position);
			currentList = (List<Map<String, Object>>) volumesAndCdnumbers.get(position).get(Dept.CD_NUMBER);
			// 从新组装一个cdnumberlist
			List<String> cdlist = new ArrayList<String>();
			for (int i = 0; i < currentList.size(); i++) {
				String vol = (String) currentList.get(i).get(ConstantGloble.VOL);
				cdlist.add(vol);
			}
			String strCdNumber = context.getResources().getString(R.string.cd_number);
			cdSpinnerAdapter = new SpinnerAdapter(context, cdlist, strCdNumber);
			banksheetSpinner.setAdapter(cdSpinnerAdapter);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// currentVolumePosition = 0;
			volumeNumber = volumes.get(0);
		}
	};

	/** 排序点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				sortTextBtn.setText(LocalData.deptSortMap[0]);
				// sortIcon.setBackgroundResource(R.drawable.icon_paixu_time);
				// 时间排序
				List<Map<String, Object>> list = datesort(listData);
				refreshListView(list);
				break;
			case R.id.tv_text2:
				// 正常在前
				sortTextBtn.setText(LocalData.deptSortMap[1]);
				// sortIcon.setBackgroundResource(R.drawable.icon_paixu_shouru);
				List<Map<String, Object>> normalList = normalsort(listData);
				refreshListView(normalList);
				break;
			case R.id.tv_text3:
				// 已逾期在前
				sortTextBtn.setText(LocalData.deptSortMap[2]);
				// sortIcon.setBackgroundResource(R.drawable.icon_paixu_zhichu);
				List<Map<String, Object>> listOver = oversort(listData);
				refreshListView(listOver);
				break;
			case R.id.tv_text4:
				// 已结清在前
				sortTextBtn.setText(LocalData.deptSortMap[3]);
				// sortIcon.setBackgroundResource(R.drawable.icon_paixu_zhichu);
				List<Map<String, Object>> pList = psort(listData);
				refreshListView(pList);
				break;
			default:
				break;
			}
		}
	};

	/** 时间排序 */
	private List<Map<String, Object>> datesort(List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> object1, Map<String, Object> object2) {
					return (((String) object2.get(Dept.DRAW_DATE)).compareTo((String) object1.get(Dept.DRAW_DATE)));
				}
			});
		}
		return list;
	}

	/** 正常在前排序 */
	private List<Map<String, Object>> normalsort(List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> object1, Map<String, Object> object2) {
					return (((String) object1.get(Dept.NOTIFY_STATUS)).compareTo((String) object2.get(Dept.NOTIFY_STATUS)));
				}
			});
		}
		return list;
	}

	/** 已逾期在前排序 */
	private List<Map<String, Object>> oversort(List<Map<String, Object>> list) {
		List<Map<String, Object>> newList = null;
		if (newList == null) {
			newList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (map.get(Dept.NOTIFY_STATUS).equals(PASS_STATUS)) {
					newList.add(0, map);
				} else {
					newList.add(map);
				}
			}
		}
		return newList;
	}

	/** 已结清在前排序 */
	private List<Map<String, Object>> psort(List<Map<String, Object>> list) {
		List<Map<String, Object>> newList = null;
		if (newList == null) {
			newList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (map.get(Dept.NOTIFY_STATUS).equals(NOTIFY_STATUS_P)) {
					newList.add(0, map);
				} else {
					newList.add(map);
				}
			}
		}
		return newList;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 100) {
			String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
			requestQueryNotify(accountId, volumeNumber, cdNumber);
		}
	}

	private OnTouchListener volumeSpinnerTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			volumeSpinnerAdapter.setSelected(true);
			((BaseAdapter) volumeSpinnerAdapter).notifyDataSetChanged();
//				cdSpinnerAdapter.setSelected(true);
//				((BaseAdapter) cdSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};

	private OnTouchListener cdSpinnerTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			cdSpinnerAdapter.setSelected(true);
			((BaseAdapter) cdSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};
	
	/** 仿spinner的弹出框*/
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog1 = null;
		switch (id) {
		case 1:
			Builder builder = new AlertDialog.Builder(this);
			final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
			builder.setSingleChoiceItems(volumes.toArray(new CharSequence[volumes.size()]), position, choiceListener);
			dialog1 = builder.create();
			break;
		}
		dialog1.setCanceledOnTouchOutside(false);
		return dialog1;
	}
	
	/**
	 * 弹出的列表的监听类
	 * @author 1
	 */
	private class ChoiceOnClickListener implements
		DialogInterface.OnClickListener {
		private int which = 0;
		@Override
		public void onClick(DialogInterface dialogInterface, int which) {
			banksheetSpinner.setEnabled(true);
			this.which = which;
			position = which;
			volumeNumber = volumes.get(position);
			tvspinner_volume.setText(volumeNumber);
			tvspinner_volume.setClickable(true);
			tvspinner_cd.setVisibility(View.GONE);
			currentList = (List<Map<String, Object>>) volumesAndCdnumbers.get(
					position).get(Dept.CD_NUMBER);
			// 从新组装一个cdnumberlist
			List<String> cdlist = new ArrayList<String>();
			for (int i = 0; i < currentList.size(); i++) {
				String vol = (String) currentList.get(i)
						.get(ConstantGloble.VOL);
				cdlist.add(vol);
			}
			String strCdNumber = context.getResources().getString(
					R.string.cd_number);
			cdSpinnerAdapter = new SpinnerAdapter(context, cdlist, strCdNumber);
			banksheetSpinner.setAdapter(cdSpinnerAdapter);
			cdSpinnerAdapter.setSelected(true);
			((BaseAdapter) cdSpinnerAdapter).notifyDataSetChanged();
			dialogInterface.cancel();
		}

		public int getWhich() {
			return which;
		}
	}
	
	/** 查看详情 返回数据 看有无非约定转存 并且有存单数据*/
	private boolean checkCdNumber() {
		// 如果通知存款返回详情里面 非约定存款数据 没有存单 不跳到下个页面 在本页面提示
		@SuppressWarnings("unchecked")
		List<Map<String, String>> accountDetaiList = (List<Map<String, String>>) DeptDataCenter
				.getInstance().getCurDetailContent()
				.get(ConstantGloble.ACC_DETAILIST);
		// 有非约定存款 并且存单序号不能空 就跳转到下个页面
		for (int i = 0; i < accountDetaiList.size(); i++) {
			Map<String, String> map = accountDetaiList.get(i);
			String convertType = map.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			// 如果是非约定转存并且存单序号不为空
			if (!StringUtil.isNullOrEmpty(convertType) && convertType.equals(
					ConstantGloble.CONVERTTYPE_N)
					&& !StringUtil.isNullOrEmpty(map.get(Dept.CD_NUMBER)) ) {
				if(((String)map.get(Dept.STATUS)).equals("V") || ((String)map.get(Dept.STATUS)).equals("00"))
				return true;		
			}
		}
		return false;
	}
}