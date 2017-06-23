package com.chinamworld.bocmbci.biz.blpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.blpt.adapter.BillCommonusedAdapter;
import com.chinamworld.bocmbci.biz.blpt.adapter.BillPaySignAadapter;
import com.chinamworld.bocmbci.biz.blpt.bdlocation.BDLocationCenter;
import com.chinamworld.bocmbci.biz.blpt.sign.BillHadAppliedActivity;
import com.chinamworld.bocmbci.biz.blpt.sign.BillPaymentSignMsgAddActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 账单支付主界面
 * 
 * @author panwe
 * 
 */
public class BillPaymentMainActivity extends BillPaymentBaseActivity implements
		OnCheckedChangeListener {

	private BillPaymentMainActivity instance;
	private BDLocationCenter bdLocationCenter;
	/** 主布局 */
	private View viewContent;
	/** 顶部table */
	private RadioGroup mRadioGroup;
	private RadioButton topBtn1, topBtn3;
	/** 签约列表 */
	private SwipeListView lvSign;
	/** 常用列表 */
	private SwipeListView lvNomaul;
	/** 是否需要签约状态 0--不需要签约 1--需要签约 */
	private final String SIGN = "1";
	/** 签约、非签约列表 */
	private List<Map<String, Object>> resultList;
	/** 常用项列表 */
	private List<Map<String, Object>> commonList = null;
	private List<Map<String, Object>> provinceList;
	/** 当前省份 */
	private String province;
	/** 当前城市 */
	private String city;
	private String cityDispName;
	private String locaCity;
	/**
	 * 跳转列表标记 1--签约 、2--非签约 、3--常用
	 */
	private final int TAG_SING = 1;
	private final int TAG_COMMON = 3;
	public static final int TAG_FORMAIN = 4;
	private int tag;
	/** listview 点击条目index */
	private int mPosition;
	/** 常用项删除控制 **/
	private boolean isdelete = true;
	/** 常用项删除条目对应position **/
	private int commonPostion;
	/** 常用项adapter */
	private BillCommonusedAdapter commonAdapter;
	private BillPaySignAadapter signAdapter;
	/** 定位Handler **/
	private MyHandler mHandler;
	/** 定位线程 */
	private LoctionThread loction;

	public static final int OPEN_GPS = 0;
	private boolean isfinishcurpag;
	private boolean isToprovince;
	private boolean locationSU;
	private int locaProIndex = -1;
	private int provIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		setTitle(this.getString(R.string.blpt_main_title));
		setRightBtnClick(rightBtnClick);
		viewContent = View.inflate(this, R.layout.blpt_main_list, null);
		btnBack.setOnClickListener(rightBtnBackmainClick);
		btnBack.setVisibility(View.GONE);
		addView(viewContent);
		getDataForIntent();
		instance = this;
		ViewsStup();
		setRightBtnText();
		ctrolViewVisible();
		showMessDialog();
	}
	/**
	 * 页面增加信息提示
	 */
	private void showMessDialog(){
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int with=metrics.widthPixels;
		int height=metrics.heightPixels;
		String str_tem="缩进";
		String str_mess_1="账单缴付已升级到民生缴费功能。账单缴付目前仍可使用，后续即将下线，敬请知悉。";
		SpannableStringBuilder span_1 = new SpannableStringBuilder(str_tem+str_mess_1);
		span_1.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.transparent_00)), 0, str_tem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		View view = LayoutInflater.from(this).inflate(R.layout.bill_payment_main_mess_layout, (ViewGroup)viewContent,false);
		TextView tv_mess_1 = getViewById(view, R.id.tv_mess_1);
		TextView tv_btn_sure = getViewById(view, R.id.tv_btn_sure);
		tv_mess_1.setText(span_1);
		
		final CustomDialog dialog = new CustomDialog(this,R.style.Theme_Dialog);
		dialog.setCancelable(false);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.width=with*7/10;
		lp.height=height*1/4;
		window.setAttributes(lp);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		dialog.show();
		
		tv_btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
	}
	
	private void getDataForIntent(){
		tag = getIntent().getIntExtra(Blpt.KEY_TAG, TAG_FORMAIN);
		city = getIntent().getStringExtra(Blpt.KEY_CITY);
		cityDispName = getIntent().getStringExtra(Blpt.CITY_DISNAME);
		province = getIntent().getStringExtra(Blpt.KEY_PROVICESHORTNAME);
	}

	private void ctrolViewVisible() {
		// 定位城市
		if (tag == TAG_FORMAIN) {
			gonRightBtn();
			// 判断是否开通GPS定位
			if (getIsOpenGps()) {
				locationCtty();
			} else {
				locationSU = false;
				getBillProvince(true);
			}
		}
		if (tag == TAG_SING) {
			topBtn1.setChecked(true);
			getbillsignList(province, city);
		} else if (tag == TAG_COMMON) {
			topBtn3.setChecked(true);
			getCommonused();
		}
	}

	/** 设置右上角按钮文字 */
	private void setRightBtnText() {
		if (topBtn3.isChecked()) {
			setText(this.getString(R.string.delete));
		} else {
			if (!StringUtil.isNullOrEmpty(cityDispName)) {
				setText(cityDispName);
			}
		}
	}

	/*** 定位城市    */
	private void locationCtty() {
		mHandler = new MyHandler();
		loction = new LoctionThread();
		loction.run();
		String message = BillPaymentMainActivity.this.getResources()
				.getString(R.string.blpt_location_ing);
		BaseDroidApp.getInstanse()
				.createLocationDialog(0, "", message, onclick);
	}

	/** 初始化控件 */
	private void ViewsStup() {
		mRadioGroup = (RadioGroup) viewContent.findViewById(R.id.radioGroup);
		topBtn1 = (RadioButton) viewContent.findViewById(R.id.blpt_topbtn1);
		topBtn3 = (RadioButton) viewContent.findViewById(R.id.blpt_topbtn3);
		mRadioGroup.setOnCheckedChangeListener(this);

		lvSign = (SwipeListView) viewContent.findViewById(R.id.blpt_lv_sign);
		lvNomaul = (SwipeListView) viewContent
				.findViewById(R.id.blpt_lv_nomaul);
		lvSign.setSwipeListViewListener(swipeListViewListener);
		lvNomaul.setSwipeListViewListener(swipeListViewListener);
		lvSign.setLastPositionClickable(true);
		lvSign.setAllPositionClickable(true);
		lvNomaul.setLastPositionClickable(true);
		lvNomaul.setAllPositionClickable(true);

		signAdapter = new BillPaySignAadapter(instance, resultList);
		lvSign.setAdapter(signAdapter);
	}

	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {

		@Override
		public void onStartOpen(int position, int action, boolean right) {
			if (action == 0) {
				toPayMentInfo(position);
			}
		}
		
		@Override
		public void onClickFrontView(int position) {
			super.onClickFrontView(position);
			toPayMentInfo(position);
		}
	};
	
	private void toPayMentInfo(int position){
		mPosition = position;
		if (topBtn1.isChecked()) {
			Map<String, Object> map = resultList.get(position);
			getSignOrNOsign((String) map.get(Blpt.BILL_MERCHANTID),
					(String) map.get(Blpt.BILL_JNUM), province);

		} else if (topBtn3.isChecked()) {
			Map<String, Object> map = commonList.get(position);
			getSignOrNOsign(
					(String) map.get(Blpt.BILL_COMMON_MERCHANTID),
					(String) map.get(Blpt.BILL_COMMON_JNUM), province);
		}
	}

	/** 右上角按钮，跳转城市页面 */
	private OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 判断当前显示的列表为哪个项
			if (topBtn3.isChecked()) {
				if (isdelete) {
					setText(instance.getString(R.string.blpt_delete_finsh));
					BillCommonusedAdapter mAdapter = new BillCommonusedAdapter(
							instance, commonList, true);
					mAdapter.setImageViewClick(imOnclick);
					lvNomaul.setAdapter(mAdapter);
					lvNomaul.setLastPositionClickable(false);
					lvNomaul.setAllPositionClickable(false);
					lvNomaul.setSwipeListViewListener(null);
					isdelete = false;
				} else {
					setText(instance.getString(R.string.delete));
					BillCommonusedAdapter mAdapter = new BillCommonusedAdapter(
							instance, commonList, false);
					mAdapter.setImageViewClick(imOnclick);
					lvNomaul.setAdapter(mAdapter);
					lvNomaul.setLastPositionClickable(true);
					lvNomaul.setAllPositionClickable(true);
					lvNomaul.setSwipeListViewListener(swipeListViewListener);
					isdelete = true;
				}
			} else {
				isfinishcurpag = false;
				isToprovince = true;
				getBillProvince(false);
			}
		}
	};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		// 签约
		case R.id.blpt_topbtn1:
			btnRight.setClickable(true);
			lvNomaul.setVisibility(View.GONE);
			setText(cityDispName);
			getbillsignList(province, city);
			break;
		// 常用
		case R.id.blpt_topbtn3:
			lvSign.setVisibility(View.GONE);
			btnRight.setVisibility(View.GONE);
			lvNomaul.setLastPositionClickable(true);
			lvNomaul.setAllPositionClickable(true);
			lvNomaul.setSwipeListViewListener(swipeListViewListener);
			getCommonused();
			break;
		}
	}

	/**
	 * 获取签约、非签约缴费类型
	 * 
	 * @param provinvce
	 *            省简称
	 * @param city
	 *            城市
	 */
	private void getbillsignList(String provinvce, String city) {
		btnBack.setVisibility(View.VISIBLE);
		mRadioGroup.setVisibility(View.VISIBLE);
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_BILL);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.BILL_PROVINCE, provinvce);
		map.put(Blpt.BILL_CITY, city);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "billListCallBack");
	}

	/** 签约、非签约返回处理 */
	public void billListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		resultList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.blpt_common_error)); return;
		}
		lvSign.setVisibility(View.VISIBLE);
		signAdapter.setData(resultList);
	}

	/**
	 * 判断签约非签约请求
	 * 
	 * @param merid
	 *            商户号
	 * @param jnum
	 *            业务编号
	 * @param prvcname
	 *            省简称
	 */
	private void getSignOrNOsign(String merid, String jnum, String prvcname) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_SIGNORNO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.SIGNORNO_MERCHANTID, merid);
		map.put(Blpt.SIGNORNO_JNUM, jnum);
		map.put(Blpt.SIGNORNO_PRVCNAME, prvcname);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "signOrnotCallBack");
	}

	/**
	 * 判断是否签约返回结果
	 * 
	 * @param resultObj
	 */
	public void signOrnotCallBack(Object resultObj) {
		String state = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (!StringUtil.isNull(state)) {
			toPayMent(state);
		}
	}
	
	/*** 跳往缴费   */
	private void toPayMent(String state){
		/** 状态 0--不需要签约 1--需要签约 */
		if (topBtn3.isChecked()) {
			Map<String, Object> map = commonList.get(mPosition);
			// 存储信息
			BlptUtil.getInstance().bimsgSave(instance,
					(String) map.get(Blpt.BILL_COMMONPTIONNAME),
					(String) map.get(Blpt.BILL_COMMON_DISPNAME),
					(String) map.get(Blpt.CITY_DISNAME),
					(String) map.get(Blpt.BILL_COMMON_MERCHANTID),
					(String) map.get(Blpt.BILL_COMMON_JNUM), province);

			Intent it = new Intent();
			if (!StringUtil.isNullOrEmpty(state) && state.equals(SIGN)) {
				it.putExtra("ISVISIBLE", false);
				it.setClass(instance, BillHadAppliedActivity.class);
			} else {
				it.setClass(instance, BillPaymentSignMsgAddActivity.class);
				Bundle b = new Bundle();
				b.putInt(Blpt.KEY_TAG, TAG_COMMON);
				it.putExtra(Blpt.KEY_BUNDLE, b);
			}
			instance.startActivity(it);
		} else {
			Map<String, Object> map = resultList.get(mPosition);
			// 存储信息
			BlptUtil.getInstance().bimsgSave(instance,
					(String) map.get(Blpt.BILL_MASTERNAME),
					(String) map.get(Blpt.BILL_DISNAME),
					(String) map.get(Blpt.CITY_DISNAME),
					(String) map.get(Blpt.BILL_MERCHANTID),
					(String) map.get(Blpt.BILL_JNUM), province);
			Intent it = new Intent();
			if (!StringUtil.isNullOrEmpty(state) && state.equals(SIGN)) {
				it.putExtra("ISVISIBLE", true);
				it.setClass(instance, BillHadAppliedActivity.class);
			} else {
				it.setClass(instance, BillPaymentSignMsgAddActivity.class);
				Bundle b = new Bundle();
				b.putInt(Blpt.KEY_TAG, TAG_SING);
				it.putExtra(Blpt.KEY_BUNDLE, b);
			}
			instance.startActivity(it);
		}
	}

	/** 请求常用缴费项    */
	private void getCommonused() {
		mRadioGroup.setVisibility(View.VISIBLE);
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_COMMOMUSED);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "commonBillCallBack");
	}

	/**
	 * 缴费常用项返回结果
	 * 
	 * @param resultObj
	 */
	public void commonBillCallBack(Object resultObj) {
		commonList = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(commonList)) {
			btnRight.setClickable(false);
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.blpt_common_error)); return;
		}
		lvNomaul.setVisibility(View.VISIBLE);
		setText(instance.getString(R.string.delete));
		btnRight.setClickable(true);
		commonAdapter = new BillCommonusedAdapter(instance, commonList, false);
		lvNomaul.setAdapter(commonAdapter);
	}

	/***
	 * 常用项删除请求
	 * 
	 * @param token
	 * @param index
	 */
	private void requestDeleteCommonBill(String token, int index) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.EMTHOD_COMMON_DELETE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.COMMON_DELETE_MERCHANTID, (String) commonList.get(index)
				.get(Blpt.COMMON_DELETE_MERCHANTID));
		map.put(Blpt.COMMON_DELETE_JNUM,
				(String) commonList.get(index).get(Blpt.BILL_COMMON_JNUM));
		map.put(Blpt.COMMON_DELETE_PRVNAME, province);
		map.put(Blpt.COMMON_DELETE_TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"deleteCommonPostionCallBack");
	}

	/** 删除常用项返回结果 **/
	public void deleteCommonPostionCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
//		String status = (String)BlptUtil.getInstance().httpResponseDeal(resultObj);
//		if (!StringUtil.isNullOrEmpty(status) && status.equals("01")) {
//			CustomDialog.toastShow(this,
//					this.getString(R.string.blpt_delete_success));
//			commonList.remove(commonPostion);
//			BillCommonusedAdapter mAdapter = new BillCommonusedAdapter(
//					instance, commonList, false);
//			lvNomaul.setAdapter(mAdapter);
//			lvNomaul.setLastPositionClickable(true);
//			lvNomaul.setAllPositionClickable(true);
//			lvNomaul.setSwipeListViewListener(swipeListViewListener);
//			setText(this.getString(R.string.delete));
//		} else {
//			CustomDialog.toastShow(this,
//					this.getString(R.string.blpt_delete_fild));
//		}
		CustomDialog.toastShow(this,
				this.getString(R.string.blpt_delete_success));
		commonList.remove(commonPostion);
		BillCommonusedAdapter mAdapter = new BillCommonusedAdapter(
				instance, commonList, false);
		lvNomaul.setAdapter(mAdapter);
		lvNomaul.setLastPositionClickable(true);
		lvNomaul.setAllPositionClickable(true);
		lvNomaul.setSwipeListViewListener(swipeListViewListener);
		setText(this.getString(R.string.delete));
	}

	/** 会话返回 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		requestDeleteCommonBill(tokenId, commonPostion);

	}

	/** 关闭定位   */
	private OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (loction.isAlive()) {
				loction.interrupt();
			}
			closeBDClient();
			BaseDroidApp.getInstanse().dismissErrorDialog();
			locationSU = false;
			getBillProvince(true);
		}
	};

	/** 常用项删除提示框点击事件 */
	private OnItemClickListener imOnclick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			commonPostion = position;
			// 弹出对话框,是否取消
			BaseDroidApp.getInstanse().showErrorDialog(
					instance.getString(R.string.blpt_common_delete_msg),
					R.string.cancle, R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								// 确定
								BaseDroidApp.getInstanse().dismissErrorDialog();
								// 获取会话id
								requestCommConversationId();
								BaseHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:
								// 取消
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			ActivityTaskManager.getInstance().removeAllActivity();
			BlptUtil.getInstance().clearAllData();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	public class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			getLocaInfo(b);
		}
	}
	
	private void getLocaInfo(Bundle b){
		if ((b.getString(Blpt.KEY_CITY)).contains("市")) {
			locaCity = b.getString(Blpt.KEY_CITY).substring(0, b.getString(Blpt.KEY_CITY).length()-1);
		} else {
			locaCity = b.getString(Blpt.KEY_CITY);
		}
		province = BlptUtil.getInstance().provZhNameToShortName(b.getString(Blpt.KEY_PROVICENAME));
		BaseDroidApp.getInstanse().dismissErrorDialog();
		if (StringUtil.isNull(locaCity)) {
			locationSU = false;
		}else{
			locationSU = true;
		}
		isToprovince = false;
		closeBDClient();
		getBillProvince(true);
	}

	/*** 定位线程   */
	class LoctionThread extends Thread {
		@Override
		public void run() {
			super.run();
			if (bdLocationCenter == null) {
				bdLocationCenter = new BDLocationCenter(mHandler, instance);
			}
			bdLocationCenter.getLocationInfo();
		}
	}

	/**
	 * 获取省份列表
	 */
	private void getBillProvince(boolean canGoBack) {
		if (canGoBack) {
			BaseHttpEngine.showProgressDialogCanGoBack();
		}else{BaseHttpEngine.showProgressDialog();}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_CODE_PROVINCE);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "provinceListCallBack");
	}

	/**
	 * 省份列表返回
	 * 
	 * @param resultObj
	 */
	public void provinceListCallBack(Object resultObj) {
		provinceList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(provinceList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showMessageDialog("您的银行卡所在地暂时不能缴费",
					errorClick); return;
		}
		if (isToprovince) {
			BaseHttpEngine.dissMissProgressDialog();
			BlptUtil.getInstance().setProvList(provinceList);
			startActivity(new Intent(instance, BillPaymentProvinceActivity.class)
			.putExtra(Blpt.KEY_TAG, TAG_SING));
			if (isfinishcurpag) {
				instance.finish();
			} return;
		} 
		if (locationSU) {
			compareProvince(provinceList);
		}else{
			if (locaProIndex == 0) {
				province = (String) provinceList.get(1).get(Blpt.PROVINCE_SNAME);
			}else{
				province = (String) provinceList.get(0).get(Blpt.PROVINCE_SNAME);
			}
			getBillCity(province);
		}
	}
	
	private void compareProvince(List<Map<String, Object>> list){
		boolean compareSU = false;
		for (int i = 0; i < list.size(); i++) {
			String proShortName = (String) list.get(i).get(Blpt.PROVINCE_SNAME);
			if (locationSU && proShortName.equals(province)) {
				locaProIndex = i;
				compareSU = true;break;
			}
		}
		if (!compareSU) {
			if (locaProIndex == 0) {
				province = (String) provinceList.get(1).get(Blpt.PROVINCE_SNAME);
			}else{
				province = (String) provinceList.get(0).get(Blpt.PROVINCE_SNAME);
			}
		}
		getBillCity(province);
	}
	
	private void initProvince(){
		if (provIndex < 2 && provIndex <provinceList.size()) {
			if (provIndex == locaProIndex) {
				provIndex = provIndex +1;
			}
			province = (String) provinceList.get(provIndex).get(Blpt.PROVINCE_SNAME);
			getBillCity(province);
			provIndex ++;
		}else{
			BiiHttpEngine.dissMissProgressDialog();
			if ((locationSU && provinceList.size() < 4) || (!locationSU && provinceList.size() < 3)) {
				BaseDroidApp.getInstanse().showMessageDialog("您的银行卡所在地暂时不能缴费",errorClick);
			}else{
				BaseDroidApp.getInstanse().showErrorDialog("请您选择缴费地区",
						R.string.cancle,R.string.confirm, toprovinceListener);
			}
		}
	}
	
	private void compareCity(List<Map<String, Object>> list){
		boolean compareSU = false;
		for (int i = 0; i < list.size(); i++) {
			String strcity;
			if (((String) list.get(i).get(Blpt.CITY_NAME)).contains("市")) {
				strcity = ((String) list.get(i).get(Blpt.CITY_NAME)).substring(0,
						((String) list.get(i).get(Blpt.CITY_NAME)).length()-1);
			} else {
				strcity = (String) list.get(i).get(Blpt.CITY_NAME);
			}
			if (locationSU && strcity.equals(locaCity)) {
				city = (String) list.get(i).get(Blpt.CITY_NAME);
				cityDispName = (String) list.get(i).get(Blpt.CITY_DISNAME);
				compareSU = true; break;
			} 
		}
		if (!compareSU) {
			if("HE".equalsIgnoreCase(province )){
				//河北省特殊处理，不显示第一个，而是显示石家庄市
				for(int i = 0; i <list.size(); i++){
					if(((String) list.get(i).get(Blpt.CITY_NAME)).contains("石家庄")){
						city = (String) list.get(i).get(Blpt.CITY_NAME);
						cityDispName = (String) list.get(i).get(Blpt.CITY_DISNAME);
						break;
					}
				} 
				if(city == null || cityDispName == null){
					city = (String) list.get(0).get(Blpt.CITY_NAME);
					cityDispName = (String) list.get(0).get(Blpt.CITY_DISNAME);
				}
			}else{
				city = (String) list.get(0).get(Blpt.CITY_NAME);
				cityDispName = (String) list.get(0).get(Blpt.CITY_DISNAME);
			} 
		}
		setText(cityDispName);
		topBtn1.setChecked(true);
	}

	/**
	 * 获取城市列表
	 */
	private void getBillCity(String sn) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_CODE_CITY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.CITY_SHORTNAME, sn);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "cityListCallBack");
	}

	/**
	 * 城市列表返回
	 * 
	 * @param resultObj
	 */
	public void cityListCallBack(Object resultObj) {
		List<Map<String, Object>> cityList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(cityList)) {
			initProvince();return;
		}
		if (locationSU) {
			compareCity(cityList);
		}else{
			if("HE".equalsIgnoreCase(province )){
				//河北省特殊处理，不显示第一个，而是显示石家庄市
				for(int i = 0; i <cityList.size(); i++){
					if(((String) cityList.get(i).get(Blpt.CITY_NAME)).contains("石家庄")){
						city = (String) cityList.get(i).get(Blpt.CITY_NAME);
						cityDispName = (String) cityList.get(i).get(Blpt.CITY_DISNAME);
						break;
					}
				} 
				if(city == null || cityDispName == null){
					city = (String) cityList.get(0).get(Blpt.CITY_NAME);
					cityDispName = (String) cityList.get(0).get(Blpt.CITY_DISNAME);
				}
			}else{
				city = (String) cityList.get(0).get(Blpt.CITY_NAME);
				cityDispName = (String) cityList.get(0).get(Blpt.CITY_DISNAME);
			}
			topBtn1.setChecked(true);
		}
	}
	
	/** 手动选择提示   **/
	private OnClickListener toprovinceListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (Integer.parseInt(v.getTag() + "")) {
			case CustomDialog.TAG_CANCLE:
				// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				instance.finish();
				break;
			case CustomDialog.TAG_SURE:
				// 确定
				isfinishcurpag = true;
				isToprovince = true;
				BaseDroidApp.getInstanse().dismissErrorDialog();
				getBillProvince(true);
				break;
			}
		}
	};

	private boolean getIsOpenGps() {
		boolean isOpen = false;
		LocationManager lm = (LocationManager) this
				.getSystemService(Service.LOCATION_SERVICE);
		isOpen = lm
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
		LogGloble.d(TAG, " GPS isOpen =="+isOpen);
		return isOpen;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeBDClient();
	}

	/*** 关闭百度定位服务 */
	private void closeBDClient() {
		if (bdLocationCenter == null) return;
		if (bdLocationCenter.mLocationClient != null) {
			bdLocationCenter.mLocationClient.unRegisterLocationListener(null);
			bdLocationCenter.mLocationClient.stop();
		}
	}

}
