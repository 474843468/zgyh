package com.chinamworld.bocmbci.biz.safety;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.safety.safetyhistorytran.SafetyHoldHisQueryActivity;
import com.chinamworld.bocmbci.biz.safety.safetyhold.SafetyHoldProductQueryActivity;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;
import com.chinamworld.bocmbci.biz.safety.safetytemp.SafetyTempProductListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保险UI基类
 * 
 * @author panwe
 * 
 */
public class SafetyBaseActivity extends BaseActivity {
	/** 跳转标识名 */
	public String JUMPFLAG = "jumpFlag";
	/** 用户选择险别的下标名 intent使用 */
	public String SELECTPOSITION = "selectPosition";
	@SuppressWarnings("rawtypes")
	private HttpHandler mHttpHandler;
	private long httpsize;
	private long filesize;
	/** 主页面 */
	private LinearLayout mBodyLayout;
	/** 右按钮 */
	public Button mRightButton;
	/** 左按钮 */
	public Button mLeftButton;
	/** 弹出二级菜单按钮*/
	public Button btn_show;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnOnClickListener;
	private final int SAFETY_PRODUCT = 0;
	private final int SAFETY_TEMP = 1;
	private final int SAFETY_HOLD = 2;
	private final int SAFETY_HISTORYTRAN = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_606_layout);
		initLeftSideList(this, LocalData.safetyLeftListData);
		initPulldownBtn();
		initFootMenu();
		init();
	}

	private void init() {
		mBodyLayout = (LinearLayout) findViewById(R.id.sliding_body);
		mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		mLeftButton = (Button) findViewById(R.id.ib_back);
		btn_show = (Button) findViewById(R.id.btn_show);
		mRightButton.setOnClickListener(toMainOnClickListener);
		mLeftButton.setOnClickListener(backOnClickListener);
	}

	protected void addView(View v) {
		mBodyLayout.addView(v);
	}

	/**
	 * add by fsm
	 * 
	 * @param resource
	 *            引入布局id
	 */
	protected View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		mBodyLayout.removeAllViews();
		addView(view);
		return view;
	}

	/**** 左侧菜单点击事件 **/
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		SafetyDataCenter.getInstance().clearAllData();
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		String menuId = menuItem.MenuID;
		Intent mIntent = new Intent();
		if(menuId.equals("safety_1")){
			// 保险产品查询
			mIntent.setClass(this, SafetyProductListActivity.class);
		}
		else if(menuId.equals("safety_2")){
			mIntent.setClass(this, SafetyTempProductListActivity.class);
			// 暂存保单
		}
		else if(menuId.equals("safety_3")){
			// 持有保单
			mIntent.setClass(this, SafetyHoldProductQueryActivity.class);
		}
		else if(menuId.equals("safety_4")){
			// 历史交易查询
			mIntent.setClass(this, SafetyHoldHisQueryActivity.class);
		}
		context.startActivity(mIntent);
		return true;
		
//		SafetyDataCenter.getInstance().clearAllData();
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
//		Intent mIntent = new Intent();
//		switch (clickIndex) {
//		case SAFETY_PRODUCT:
//			// 保险产品查询
//			mIntent.setClass(this, SafetyProductListActivity.class);
//			break;
//
//		case SAFETY_TEMP:
//			mIntent.setClass(this, SafetyTempProductListActivity.class);
//			// 暂存保单
//			break;
//
//		case SAFETY_HOLD:
//			// 持有保单
//			mIntent.setClass(this, SafetyHoldProductQueryActivity.class);
//			break;
//
//		case SAFETY_HISTORYTRAN:
//			// 历史交易查询
//			mIntent.setClass(this, SafetyHoldHisQueryActivity.class);
//			break;
//		}
//		startActivity(mIntent);
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
	
	/** 隐藏底部tab */
	public void setBottomTabGone() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
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
			if ((SafetyBaseActivity.this instanceof SafetyProductListActivity && !SafetyDataCenter.getInstance().isSaveToThere && !SafetyDataCenter.getInstance().isHoldToThere)
					|| SafetyBaseActivity.this instanceof SafetyTempProductListActivity
					|| SafetyBaseActivity.this instanceof SafetyHoldProductQueryActivity
					|| SafetyBaseActivity.this instanceof SafetyHoldHisQueryActivity) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
				SafetyDataCenter.getInstance().clearAllData();
//				Intent intent = new Intent(SafetyBaseActivity.this, SecondMainActivity.class);
//				startActivity(intent);
//				goToMainActivity();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				finish();
			} else {
				finish();
			}
		}
	};

	/** 返回主页面 */
	public OnClickListener toMainOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			SafetyDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};


	/** 设置日期 */
	public OnClickListener SafetyChooseDateClick = new OnClickListener() {
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
					SafetyBaseActivity.this, new OnDateSetListener() {

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

	/** 获取账户列表 --公共接口 */
	public void requestBankAcctList(List<String> accountList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, accountList);
		getHttpTools().requestHttp(Comm.QRY_ALL_BANK_ACCOUNT, "bankAccListCallBack", params);
	}

	/**
	 * 账户列表返回处理
	 * 
	 * @param resultObj
	 */
	public void bankAccListCallBack(Object resultObj) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> cardList = this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(cardList)) {
			BaseDroidApp.getInstanse().showMessageDialog("没有可用的缴费账户", null);
			return;
		}
		// 临时存储账户列表
		SafetyDataCenter.getInstance().setAcctList(cardList);
	}
	
	/**
	 * 隐藏左按钮
	 */
	public void setLeftTopGone(){
		mLeftButton.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 隐藏右按钮
	 */
	public void setRightTopGone(){
		mRightButton.setVisibility(View.INVISIBLE);
	}
	
//	/**
//	 * 隐藏左侧菜单栏
//	 */
//	public void goneLeftView() {
//		// 隐藏左侧菜单
//		LinearLayout slidingTab = (LinearLayout) findViewById(R.id.sliding_tab);
//		Button btn_hide = (Button) findViewById(R.id.btn_hide);
//		Button btn_fill_show = (Button) findViewById(R.id.btn_fill_show);
//		slidingTab.setVisibility(View.GONE);
//		btn_show.setVisibility(View.GONE);
//		setLeftButtonPopupGone();
//		btn_hide.setVisibility(View.GONE);
//		btn_fill_show.setVisibility(View.GONE);
//	}
	
	/**
	 * 隐藏底部菜单
	 */
	public void goneBottomMenu(){
		((LinearLayout)findViewById(R.id.foot_layout)).setVisibility(View.GONE);
	}
	
	/**
	 * 查看保险产品明细
	 * @param url
	 */
	public void productDescription(String url){
		if (!checkPDFapps()) {
			String msg = getString(R.string.safety_nopdfapps);
			BaseDroidApp.getInstanse().showInfoMessageDialog(msg);
			return;
		}
		downLoadPDFfile(url);
	}

	/**
	 * 下载pdf文件
	 * @param url
	 */
	private void downLoadPDFfile(final String url) {
		// 保存文件路劲
		String sdcardpath = android.os.Environment
				.getExternalStorageDirectory() + "/" + "BOCMB_PDF";
		final String fileName = sdcardpath + File.separator + "safety_pdf" + ".pdf";
		File file = new File(sdcardpath);
		if (!file.exists()) {
			try {
				file.mkdirs();
				if (!file.mkdirs())
					CustomDialog.toastInCenter(getApplicationContext(),
							getApplicationContext().getString(R.string.save_dimen_to_nosdk));
				return;
			} catch (Exception e) {
			}
		}
		FinalHttp fh = null;
		if (fh == null) {
			fh = new FinalHttp();
		}
		mHttpHandler = fh.download(url, fileName, false,
				new AjaxCallBack<File>() {

					@Override
					public void onStart() {
						super.onStart();
						BaseHttpEngine.showProgressDialog();
					}

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(File t) {
						if (t != null) {
							// 调用手机pdf软件
							BaseHttpEngine.dissMissProgressDialog();
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.addCategory(Intent.CATEGORY_DEFAULT); 
							intent.setDataAndType(Uri.fromFile(t), "application/pdf");
							startActivity(intent);
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						if (errorNo == 416) {// maybe you have download complete
							try {
								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											URL mUrl = new URL(url);
											HttpURLConnection urlcon = (HttpURLConnection) mUrl
													.openConnection();
											// 根据响应获取文件大小
											httpsize = urlcon
													.getContentLength();
											File file = new File(fileName);
											if (file != null) {
												filesize = file.length();
											}
										} catch (Exception e) {
										} finally {
											fialueHandler.sendEmptyMessage(0);
										}
									}
								}).start();
							} catch (Exception e) {
							}
						} else {
						}
					}
				});
	}
	
	Handler fialueHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if ((httpsize == filesize) && httpsize > 0) {
					// 保存文件路劲
					String sdcardpath = android.os.Environment
							.getExternalStorageDirectory()
							+ "/"
							+ "pdf_test";
					final String fileName = sdcardpath + File.separator
							+ "BOCMB_PDF" + ".pdf";
					File file = new File(fileName);
					// 调用手机pdf软件
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addCategory(Intent.CATEGORY_DEFAULT); 
					intent.setDataAndType(Uri.fromFile(file), "application/pdf");
					startActivity(intent);
				}
			} catch (Exception e) {
			} finally {
			}
		}

	};
	
	/**
	 * 检测手机是否安装PDF软件
	 * @return
	 */
	public boolean checkPDFapps(){
		Intent intent = new Intent(Intent.ACTION_VIEW); 
	    intent.addCategory(Intent.CATEGORY_DEFAULT); 
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    Uri uri = Uri.fromFile(new File("http://www.bocmb/defualt.pdf")); 
	    intent.setDataAndType(uri, "application/pdf"); 
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (!StringUtil.isNullOrEmpty(list)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 添加引导页
	 * @param key
	 */
	public void showCardPriceGuid(String key) {
//		int showTime = SharedPreUtils.getInstance().getInt(key, 0);
//		if (showTime < 3) {
			final PopupWindow popupWindow = new PopupWindow(
					SafetyBaseActivity.this);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
			popupWindow.setHeight(LayoutValue.SCREEN_HEIGHT);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setFocusable(true);
			popupWindow.setAnimationStyle(R.style.popuwindow_in_out_Animation_forCard);
			View view = LayoutInflater.from(SafetyBaseActivity.this).inflate(
					R.layout.safety_customer_guide, null);
			View layout = view.findViewById(R.id.layout);
			LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH,
					LayoutValue.SCREEN_WIDTH * 266 / 640);
			lp.setMargins(0, LayoutValue.SCREEN_WIDTH / 3, 0, 0);
			lp.addRule(Gravity.CENTER_HORIZONTAL);
			layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popupWindow.dismiss();
				}
			});
			popupWindow.setContentView(view);
			popupWindow.showAtLocation(this.getWindow().getDecorView(),
					Gravity.CENTER, 0, 0);
//			showTime++;
//			SharedPreUtils.getInstance().addOrModifyInt(key, showTime);
//		}
	}

	@Override
	protected void onDestroy() {
		if (mHttpHandler != null) {
			mHttpHandler.stop();
		}
		super.onDestroy();
	}
	
	/**
	 * 登录前为请求网络提供统一的请求方法
	 * 
	 * @param requestMethod
	 *            要请求的接口
	 * @param responseMethod
	 *            请求成功后的回调方法
	 * @param params
	 *            参数列表，子类准备此参数
	 * @param needConversationId
	 *            是否需要ConversationId
	 */
	public void requestHttpOutlay(String requestMethod, String responseMethod, Map<String, Object> params, boolean needConversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(requestMethod);
		biiRequestBody.setParams(params);
		// 如果需要ConversationId
		if (needConversationId)
			biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID));
		HttpManager.requestOutlayBii(biiRequestBody, this, responseMethod);
	}
	@Override
	protected void onResume() {
		super.onResume();
		boolean login = BaseDroidApp.getInstanse().isLogin();
		onResumeFromLogin(login);
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
