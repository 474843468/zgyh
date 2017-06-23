package com.chinamworld.bocmbci.biz;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivitySwitcher;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiApi;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Login.CustLevelType;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.audio.AudioKeyManager;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDataCenter;
import com.chinamworld.bocmbci.biz.main.DragController;
import com.chinamworld.bocmbci.biz.main.DragController.DragListener;
import com.chinamworld.bocmbci.biz.main.DragLayer;
import com.chinamworld.bocmbci.biz.main.DragPagerAdapter;
import com.chinamworld.bocmbci.biz.main.DragView;
import com.chinamworld.bocmbci.biz.main.Item;
import com.chinamworld.bocmbci.biz.main.MainSetting;
import com.chinamworld.bocmbci.biz.main.view.DraggableGridView;
import com.chinamworld.bocmbci.biz.main.view.OnRearrangeListener;
import com.chinamworld.bocmbci.biz.more.MoreMenuActivity;
import com.chinamworld.bocmbci.biz.plps.order.CharacterParser;
import com.chinamworld.bocmbci.biz.push.MessageService;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.CardWelcomGuideUtil;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class MainActivity extends BaseActivity {

	protected static final String TAG = MainActivity.class.getSimpleName();
	/** 行 */
	public static  int ROW = 3;
	/** 列 */
	public static int COLUMN = 4;
	private ViewPager mPagerView;
	private DragLayer mDragLayer;
	private DragController mDragController;
	private DragPagerAdapter adapter;
	private List<Item> lists;
	private List<Item> dgvLists;
	private TextView mBtnLogin;
	//private String openNameLable;
	// private CirclePageIndicator mPageIndicator;
	public static boolean isSigned = false;
	/** 绘制底部圆点layout */
	private LinearLayout pointLayout;
	private Button mBtnLoginInfo;
	private Button mBtnLoginNew;
	private PopupWindow loginInfoPopup;
	

	//更多按钮 wuhan
	private Button mBtnMore;
	private TextView tv_prompt;
	private MessageService mMessageService;
	static int vipMessageCount = 0;
	static int newMessageCount = 0;

	private boolean fast_login=false;
	
	//替换成垂直滑动，所需变量
	private DraggableGridView dgv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity2);

		SharedPreUtils.getInstance().addOrModifyInt(ConstantGloble.TIME_OUT, 0);
		// 初始化弹出框
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		// 初始化控件
		findView();
		
		initVertical();

		//wuhan更多消息
		tv_prompt.setVisibility(View.INVISIBLE);
		mMessageService = MessageService.getInstance(this);
		PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();

		mMessageService.getMessages(pushDevice.getDeviceType(), pushDevice.getDeviceId(), this,
				"requestMessagesCallback");

		
		if (!SystemConfig.IS_SELECT_INTER) {//当没打开地址显示
			new Handler().postDelayed(new Runnable() {//因为从上个页面延时200毫秒，所以只能在这里添加延迟200毫秒添加引导页

				@Override
				public void run() {
					CardWelcomGuideUtil.showMainWelcomGuid(MainActivity.this);

				}
			}, 200);
		}
		Thread pinYinThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				CharacterParser.initChinesePinYin(MainActivity.this);
			}
		});
		pinYinThread.start();
	}
	

	private void initVertical(){
		dgv = ((DraggableGridView)findViewById(R.id.dgv));
		dgv.removeAllViews();
		if(BaseDroidApp.getInstanse().isLogin()){
			dgvLists = MainSetting.initItem(this,R.string.online_open_an_account);
		}
		else{
			dgvLists = MainSetting.initItem(this);
		}

		String order = getMainChildOrder();
        if(order != null && !"".equals(order))
        {
        	String[] childs = order.split(",");
        	orderInitView(childs);
        	
        }else{
        	initView();
        }
        
        setListeners();
	}
	
	static boolean isShowSelect = true;

//	private void showSelectInter() {
//		// *************************开始 打开所有地址
//		if (SystemConfig.IS_SELECT_INTER && isShowSelect) {
//			isShowSelect = false;
//			View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.address_input, null);
//			Spinner sp = (Spinner) view.findViewById(R.id.findpwd_sp_idcty);
//			final LinkedHashMap<String, String> inters = LocalData.addressApi;
//			final String[] data = new String[inters.size()];
//			inters.keySet().toArray(data);
//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.dept_spinner, data);
//			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			sp.setAdapter(adapter);
//			sp.setOnItemSelectedListener(new OnItemSelectedListener() {
//				@Override
//				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//					BiiApi.BASE_HTTP_URL = inters.get(data[position]);
//					BiiApi.BASE_URL = BiiApi.BASE_HTTP_URL;
//					BiiApi.BASE_API_URL = BiiApi.BASE_URL + BiiApi.BASE_API;
//					BiiApi.FINCADDRESS = "http://22.188.130.127:9080/EBankingInfoSvc/";
//					if (position == 9) {// 公网地址
//						Comm.AQUIRE_IMAGE_CODE = BiiApi.BASE_HTTP_URL + "ImageValidation/validation.gif";
//					}
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> arg0) {
//
//				}
//			});
//			final Button btnAddress = (Button) view.findViewById(R.id.btnConfirm);
//			btnAddress.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					BaseDroidApp.getInstanse().dismissErrorDialog();
//					// finish();
//					CardWelcomGuideUtil.showMainWelcomGuid(BaseDroidApp.getInstanse().getCurrentAct());
//				}
//			});
//			BaseDroidApp.getInstanse().showAddressDialog(view);
//
//		}
//	}

	protected void onResume() {
		super.onResume();
		initVertical();
		BaseDroidApp.getInstanse().setMainAct(this);
		if ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA) != null) {
			// 除了登录客户信息外清楚所有其他数据
			Map<String, Object> loginInfoMap = new HashMap<String, Object>();
			loginInfoMap.putAll((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.BIZ_LOGIN_DATA));
			if (BaseDroidApp.getInstanse().getActfive() == null||BaseDroidApp.getInstanse().getActfive()instanceof SecondMainActivity){
				BaseDroidApp.getInstanse().getBizDataMap().clear();	
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BIZ_LOGIN_DATA, loginInfoMap);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BIZ_LOGIN_DATA, loginInfoMap);
			} 
			
		
		}
		if(!fast_login){
			LayoutValue.LEWFTMENUINDEX = LayoutValue.DefaultMenu;	
		}
		
		
		
		if (!BaseDroidApp.getInstanse().isLogin()) {
			mBtnLogin.setText(R.string.login);
		} else {
			mBtnLogin.setText(R.string.logout);
		}
		showLoginBtnOrInfoBtn();
		// 显示选择地址框
//		showSelectInter();
		
//		
//		if(mtype!=null&&!mtype.equals("")){
//			requestCloseLoginPreOutlayConversationId(mtype);
//			mtype=null;
//			LogGloble.e("mtype==清空=", mtype );
//		}
//		
		//------------------------ 放到onNewIntent代码快中
//		if (getIntent().getBooleanExtra(ConstantGloble.FAST_LOGIN, false)) {// 快捷方式的跳转
//			startFastActivity(BaseDroidApp.getInstanse().getFastItemCHoosed(),
//					getIntent().getIntExtra(ConstantGloble.FAST_INDEX, 0));
//		}
		//-------------------------
//		CardWelcomGuideUtil.showMainWelcomGuid(this);
		
		//必读消息，测试用
//		if (BaseDroidApp.getInstanse().isLogin()) {
//			toInfoServeDetialActivityAfterLogin(/*bundle*/);
//		}
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getBooleanExtra(ConstantGloble.FAST_LOGIN, false)) {// 快捷方式的跳转
			fast_login=true;
			startFastActivity(BaseDroidApp.getInstanse().getFastItemCHoosed(),
					intent.getIntExtra(ConstantGloble.FAST_INDEX, 0));
		}else{
			fast_login=false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		dgv.cancelAnimation();
		saveMainChildOrder(dgv.getChildOrder());
		
		if (adapter != null) {
			if (adapter.isAnimaing()) {
				stopAdapterAnimation(false);
			}
		}
		if (!BaseDroidApp.getInstanse().isLogin()) {
			mBtnLogin.setText(R.string.login);
		} else {
			mBtnLogin.setText(R.string.logout);
		}
		showLoginBtnOrInfoBtn();
		
	}


	private void findView() {
		findViewById(R.id.ib_back).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_title)).setText(R.string.titletext);
		mBtnLogin = (TextView) findViewById(R.id.ib_top_right_btn);
		pointLayout = (LinearLayout) this.findViewById(R.id.point_layout);
		mBtnLoginInfo = (Button) findViewById(R.id.ib_login_info_btn);
		mBtnLoginNew = (Button) findViewById(R.id.ib_login_btn);
		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = mBtnLogin.getText().toString();
				if (getApplication().getText(R.string.finish).toString().equals(text)) {// 完成
					// 保存配置
					dgv.cancelAnimation();
					saveMainChildOrder(dgv.getChildOrder());
					
					stopAdapterAnimation(true);
					mBtnLogin.setText(BaseDroidApp.getInstanse().isLogin() ? R.string.logout : R.string.login);
					showLoginBtnOrInfoBtn();
				} else {// 登陆 | 安全退出
					if (BaseDroidApp.getInstanse().isLogin()) {
						exitLogin();
					} else {
						openLoginActivity();
					}
				}
			}
		});
		mBtnLoginInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA) == null) {
					// 请求登录信息
					requestForLoginInfo();
				} else {
					showLoginInfoPopup();
				}
			}
		});
		mBtnLoginNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				BaseHttpEngine.showProgressDialog();
//				sendGetActivityAction();
				openLoginActivity();
			}
		});

		mPagerView = (ViewPager) findViewById(R.id.main_vp);
		mDragLayer = (DragLayer) findViewById(R.id.dragview);
		mDragController = new DragController(this, mDragLayer);
		mDragLayer.setDragController(mDragController);
		lists = MainSetting.loadMainItem(this);
		ROW = (int) Math.ceil(lists.size()/4.0);
		// 适配
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int windowWidth = wm.getDefaultDisplay().getWidth();
		int windowHeight = wm.getDefaultDisplay().getHeight();
		if (windowWidth == 540 && windowHeight == 960) {
			MarginLayoutParams layoutParams = (MarginLayoutParams) mPagerView.getLayoutParams();
			System.out.println(layoutParams);
			layoutParams.topMargin = (int) getResources().getDimension(R.dimen.dp_five_zero);
			mPagerView.setLayoutParams(layoutParams);
		}

		mPagerView.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				refreshPageView();
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		
		//wuhan 初始化更多按钮
		mBtnMore = (Button) this.findViewById(R.id.ib_top_right_more_btn);
		tv_prompt = (TextView) findViewById(R.id.tv_prompt);
		mBtnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//跳转到更多页面
				openMoreActivity();
			}
		});
		
		setPagerView();
		refreshPageView();
	}

	private void setPagerView() {
		adapter = new DragPagerAdapter(this, mDragController, lists, mPagerView, ROW, COLUMN);
		adapter.setOnDragListener(new DragListener() {

			@Override
			public void onDraging(DragView view, Object dragInfo, MotionEvent ev) {

			}

			@Override
			public void onDragStart(View view, Object dragInfo) {
				mBtnLogin.setText(R.string.finish);
				showLoginBtnOrInfoBtn();
			}

			@Override
			public void onDragEnd(DragView view, Object dragInfo) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDragEdge(Orientation orientation) {
				// TODO Auto-generated method stub

			}
		});
		adapter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!adapter.isAnimaing()) {
					Item item = (Item) v.getTag();
					if (item != null) {
						String itemName = item.getName();
						openActivity(itemName);
					}
				}
			}
		});
		mPagerView.setAdapter(adapter);
		// mPageIndicator.setCurrentItem(mPagerView.getCurrentItem());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		if (resultCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
			initFootMenu();
		}
		if (resultCode == RESULT_OK) {// 登录成功返回
			ActivityTaskManager.getInstance().removeAllActivity();
			ActivityTaskManager.getInstance().removeAllSecondActivity();

			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_CODE_ZHIFU) {// 支付登录返回页面
				Utils.startPayPlui(this);
				return;
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (adapter.isAnimaing()) {
			stopAdapterAnimation(false);
			if (!BaseDroidApp.getInstanse().isLogin()) {
				mBtnLogin.setText(R.string.login);
			} else {
				mBtnLogin.setText(R.string.logout);
			}
			showLoginBtnOrInfoBtn();
		} else {
			if (!BaseDroidApp.isExit) {
				BaseDroidApp.isExit = true;
				CustomDialog.toastInCenter(getApplicationContext(), getResources().getString(R.string.exit_first_info));
				// 利用handler延迟发送更改状态信息
				BaseDroidApp.getInstanse().mHandlerExit.sendEmptyMessageDelayed(0, SystemConfig.EXIT_BETWEEN_TIME);
			} else {
				exitApp();
			}
		}
	}

	private void stopAdapterAnimation(boolean isSave) {
		if (adapter != null) {
			if (isSave) {
				try {
					adapter.stopAnimation();
					MainSetting.saveMainItem(MainActivity.this, lists);
				} catch (Exception e) {
					LogGloble.e(TAG, e.getMessage(), e);
				}
			} else {
				try {
					adapter.stopAnimation();
					lists = MainSetting.loadMainItem(this);
				} catch (Exception e) {
					LogGloble.e(TAG, e.getMessage(), e);
				}
			}
			int currentItem = mPagerView.getCurrentItem();
			setPagerView();
			mPagerView.setCurrentItem(currentItem);
			// mPageIndicator.setCurrentItem(currentItem);
		}
	}

	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissErrorDialog();
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE:// 确定
				exitApp();
				break;
			}
		}
	};

	private void exitLogin() {
		// P501音频key
//		boolean connected = mAudioKeyManager.isConnected();
//		if (connected) {
//			BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.safe_exit_device_tips), cancelListener);
//		} else {
		String message = MainActivity.this.getResources().getString(R.string.exit_confirm);
		BaseDroidApp.getInstanse().showErrorDialog(null, message, new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissErrorDialog();
				switch ((Integer) v.getTag()) {
				case CustomDialog.TAG_SURE:// 确定
					// P501音频key
					boolean connected = AudioKeyManager.getInstance().isConnected();
					if (connected) {
						BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.safe_exit_device_tips),R.string.cancle,R.string.quit, 
								new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										switch ((Integer) v.getTag()) {
										case CustomDialog.TAG_CANCLE:
											BaseDroidApp.getInstanse().dismissMessageDialog();
											break;
										case CustomDialog.TAG_SURE:
											logout();
											break;
										}
									}
								});

					} else {
						logout();
						initVertical();
					}
				}
			}
		});
//		}
	}
	
	private void logout(){
		mBtnLogin.setText(R.string.login);
		showLoginBtnOrInfoBtn();
		// 发送通讯请求退出
		requestForLogout();
		CustomDialog.toastInCenter(MainActivity.this, getString(R.string.logoutsucess));
		BaseDroidApp.getInstanse().clientLogOut();
		// TODO P402
		ActivityTaskManager.getInstance().removeAllActivity();
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		
		// 关闭登录信息PopupWindow
		if (loginInfoPopup != null) {
			loginInfoPopup.dismiss();
			loginInfoPopup = null;
		}
	}

	private boolean isLogin() {
		boolean isLogin = BaseDroidApp.getInstanse().isLogin();
		if (!isLogin) {
//			Intent intent = new Intent(this, LoginActivity.class);
//			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
			BaseDroidApp.getInstanse().setMainItemAutoClick(true);
			BaseActivity.getLoginUtils(MainActivity.this).exe(new LoginTask.LoginCallback() {

				@Override
				public void loginStatua(boolean isLogin) {
					if(isLogin){
						ActivityTaskManager.getInstance().removeAllActivity();
						ActivityTaskManager.getInstance().removeAllSecondActivity();
					}
				}
			});
		}
		return isLogin;
	}

	private void openLoginActivity() {
//		Intent intent1 = new Intent();
//		intent1.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoginActivity.class);
//		startActivityForResult(intent1, ConstantGloble.ACTIVITY_RESULT_CODE);
		BaseActivity.getLoginUtils(MainActivity.this).exe(new LoginTask.LoginCallback() {

			@Override
			public void loginStatua(boolean isLogin) {
				if(isLogin){
					ActivityTaskManager.getInstance().removeAllActivity();
					ActivityTaskManager.getInstance().removeAllSecondActivity();
				}
			}
		});
	}

	private void openActivity(String itemName) {
		new ActivitySwitcher(this).openModule(itemName, false);
	}

	
	//wuhan
	private void openMoreActivity() {
		tv_prompt.setVisibility(View.INVISIBLE);
		Intent intent1 = new Intent();
		intent1.putExtra("vipMessageCount", vipMessageCount);
		intent1.putExtra("newMessageCount", newMessageCount);
		intent1.putExtra("totalMessage", totalMessage);
		intent1.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MoreMenuActivity.class);
		startActivity(intent1);
		vipMessageCount =0;
		newMessageCount = 0;
		totalMessage= 0;
	}

	public void refreshPageView() {
		pointLayout.removeAllViews();
		pointLayout.setVisibility(View.GONE);
		int imgWidth = getResources().getDimensionPixelSize(R.dimen.main_page_icon_height);
		LayoutParams lp = new LayoutParams(imgWidth, imgWidth);
		int gag = getResources().getDimensionPixelSize(R.dimen.dp_three);
		lp.setMargins(gag, gag, gag, gag);
		PagerAdapter pa = mPagerView.getAdapter();
		if (pa != null) {
			if (pa.getCount() > 1) {
				for (int page = 0; page < pa.getCount(); page++) {
					ImageView imgView = new ImageView(this);
					if (page == mPagerView.getCurrentItem()) {
						imgView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.current_page));
					} else {
						imgView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.other_page));
					}
					imgView.setLayoutParams(lp);
					pointLayout.addView(imgView);
				}
			}
		}
	}

	/**
	 * 判断登录按钮显示
	 */
	private void showLoginBtnOrInfoBtn() {
		String text = mBtnLogin.getText().toString();
		if (getApplication().getText(R.string.finish).toString().equals(text)) {
			// 完 成
			mBtnLogin.setVisibility(View.VISIBLE);
			mBtnLoginInfo.setVisibility(View.GONE);
			mBtnLoginNew.setVisibility(View.GONE);
			mBtnMore.setVisibility(View.GONE);
			tv_prompt.setVisibility(View.INVISIBLE);
		} else if (getApplication().getText(R.string.login).toString().equals(text)) {
			// 登录
			mBtnLoginNew.setVisibility(View.VISIBLE);
			mBtnLogin.setVisibility(View.GONE);
			mBtnLoginInfo.setVisibility(View.GONE);
			mBtnMore.setVisibility(View.VISIBLE);
			if(totalMessage!=0){
		 		tv_prompt.setVisibility(View.VISIBLE);
		 		tv_prompt.setText(totalMessage+"");
		 	}else{
		 		tv_prompt.setVisibility(View.INVISIBLE);
		 	}
		} else {
			mBtnLoginNew.setVisibility(View.GONE);
			mBtnLogin.setVisibility(View.GONE);
			mBtnLoginInfo.setVisibility(View.VISIBLE);
			mBtnMore.setVisibility(View.VISIBLE);
			if(totalMessage!=0){
		 		tv_prompt.setVisibility(View.VISIBLE);
		 		tv_prompt.setText(totalMessage+"");
		 	}else{
		 		tv_prompt.setVisibility(View.INVISIBLE);
		 	}
		}
	}

	/**
	 * 显示登录信息PopupWindow
	 */
	RelativeLayout layout ;
	private void showLoginInfoPopup() {
		if (BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA) == null) {
			return;
		}

		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);

		
		if (loginInfoPopup == null) {
			layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.login_info_popupwindow2,
					null);
			loginInfoPopup = new PopupWindow(layout,
					getWindow().getWindowManager().getDefaultDisplay().getWidth() * 6 / 7,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 姓名
		// TextView tvName = (TextView) layout.findViewById(R.id.tvname);
		// 称呼
		TextView tvCall = (TextView) layout.findViewById(R.id.tvcalled);
		// 预留信息
		TextView tvwelcominfo = (TextView) layout.findViewById(R.id.tvwelcominfo);
		// 上次登录成功的时间
		TextView tvloginday = (TextView) layout.findViewById(R.id.tvloginday);
		// 上次登录失败的时间
		TextView tvloginfail = (TextView) layout.findViewById(R.id.tvloginday_fail);
		// 退出
		/**601需求变更==变更成两个取消 确定按钮 没有退出弹出框 zxj*/
		//红色提示信息
		TextView textInfo = (TextView)layout.findViewById(R.id.textinfo);
		boolean connected = AudioKeyManager.getInstance().isConnected();
		if(connected){
			textInfo.setText(getString(R.string.safe_exit_device_tips));
		}else {
			textInfo.setText("是否确认退出手机银行？");
		}
		//取消按钮
		LinearLayout ly_cancle=(LinearLayout)layout.findViewById(R.id.ly_cancle);
		ly_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginInfoPopup.dismiss();
			}
		});
		//确定按钮
		LinearLayout rlyt_logout = (LinearLayout)layout.findViewById(R.id.ly_logout);
		final ImageView btn_logout = (ImageView) layout.findViewById(R.id.btn_logout);
		rlyt_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 退出登录
//				exitLogin();
				// if (loginInfoPopup != null) {
				// loginInfoPopup.dismiss();
				// }
//				boolean connected = AudioKeyManager.getInstance().isConnected();
//				if (connected) {
////					BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.safe_exit_device_tips), cancelListener);
//					BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.safe_exit_device_tips),R.string.cancle,R.string.quit, 
//							new View.OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									switch ((Integer) v.getTag()) {
//									case CustomDialog.TAG_CANCLE:
//										BaseDroidApp.getInstanse().dismissMessageDialog();
//										break;
//									case CustomDialog.TAG_SURE:
//										logout();
//										break;
//									}
//									
//								}
//							});
//				} else {
					logout();
					initVertical();
//				}
			}
		});
		rlyt_logout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btn_logout.setPressed(true);
					break;
				case MotionEvent.ACTION_MOVE:
					btn_logout.setPressed(true);
					break;
				case MotionEvent.ACTION_UP:
					btn_logout.setPressed(false);
					break;
				case MotionEvent.ACTION_CANCEL:
					btn_logout.setPressed(false);
					break;
				}
				return false;
			}
		});
		String customerName = (String) loginMap.get(Login.CUSTOMER_NAME);
		String gender=LocalData.gender.get(loginMap.get(Login.GENDER));
		String custLevel = (String) loginMap.get(Login.custLevel);
		// 财富等级
		String customerID = (String) loginMap.get(Login.CUSTOMER_ID);
		if (Login.CustLevelType.isCustLevel(custLevel)) {
			String custlevelStr = CustLevelType.getCustLevelStr((String) loginMap.get(Login.custLevel));
			String str = String.format(getString(R.string.custlevel), custlevelStr, customerName, gender, customerID);
			tvCall.setText(str);
		} else {
			// 不区分贵宾和普通用户
			// if (Inves.CUMTOMCALLNAME.equals((String)
			// loginMap.get(Inves.SEGMENTID))) {
			// String str = String.format(getString(R.string.vipcall),
			// customerName, gender,customerID);
			// tvCall.setText(str);
			// } else {
			String str = String.format(getString(R.string.commoncall), customerName, gender, customerID);
			tvCall.setText(str);
			// }
		}
		tvwelcominfo.setText((String) loginMap.get(Inves.LOGINHINT));
		String loginDate = (String) loginMap.get(Inves.LASTLOGIN);
		if (null == loginDate || "".equals(loginDate) || " ".equals(loginDate)) {
			loginDate = "-";
		} else {
			loginDate = loginDate.replaceFirst("/", "年").replaceFirst("/", "月").replaceFirst(" ", "日 ");
		}
		tvloginday.setText(loginDate);
		String loginfaildate = (String) loginMap.get(Inves.LOGINERRORDATE);
		if (null == loginfaildate || "".equals(loginfaildate) || " ".equals(loginfaildate)) {
			loginfaildate = "-";
		} else {
			loginfaildate = loginfaildate.replaceFirst("/", "年").replaceFirst("/", "月").replaceFirst(" ", "日 ");
		}
		tvloginfail.setText(loginfaildate);

		loginInfoPopup.setBackgroundDrawable(new BitmapDrawable());
		loginInfoPopup.setOutsideTouchable(true);
		loginInfoPopup.setFocusable(true);
		loginInfoPopup.showAsDropDown(mBtnLoginInfo);
	}

	/**
	 * 请求登录信息
	 */
	private void requestForLoginInfo() {
	
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.LOGIN_INFO_API);
		HttpManager.requestBii(biiRequestBody, this, "requestForLoginInfoCallBack");
	}

	public void requestForLoginInfoCallBack(Object resultObj) {
	
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 当userStatus不为VERIFIED并且challengeQuestion不为空且长度大于0时，需弹出安全保护问题设置页面。
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		// 将登陆之后的信息保存到全局map中
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BIZ_LOGIN_DATA, resultMap);
		BiiHttpEngine.dissMissProgressDialog();
		showLoginInfoPopup();
	}
	
	
	
	//垂直滑动所需方法
	private void initView(){
    	LayoutInflater mInflater = LayoutInflater.from(this);
    	for(int i = 0; i < dgvLists.size(); i++){
    		final View myView = mInflater.inflate(R.layout.grid_item_img_text_3, null);
    		Button btn = (Button)myView.findViewById(R.id.grid_icon);
//    		image.setImageBitmap(BitmapFactory.decodeResource(getResources(), drawID[i]));
    		btn.setBackgroundDrawable(getResources().getDrawable(dgvLists.get(i).getDrawableResourcesId()));
    		myView.setTag(i);
    		btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openActivity2((Integer)myView.getTag());
				}
			});
    		TextView text = (TextView)myView.findViewById(R.id.grid_text);
    		text.setText(dgvLists.get(i).getName());
			dgv.addView(myView);
    	}
    }
    
    private void orderInitView(String[] indexs){
    	if(indexs.length != 0){
    		LayoutInflater mInflater = LayoutInflater.from(this);
    		
    		if(dgvLists.size() >= indexs.length){
    			for(int i = 0; i < dgvLists.size(); i++){
    				final View myView = mInflater.inflate(R.layout.grid_item_img_text_3, null);
    				Button image = (Button)myView.findViewById(R.id.grid_icon);
    				TextView text = (TextView)myView.findViewById(R.id.grid_text);
    				if(i < indexs.length){
    					image.setBackgroundDrawable(getResources().getDrawable(dgvLists.get(Integer.valueOf(indexs[i])).getDrawableResourcesId()));
    					text.setText(dgvLists.get(Integer.valueOf(indexs[i])).getName());
    					myView.setTag(Integer.valueOf(indexs[i]));
    					image.setOnClickListener(new OnClickListener() {
    						
    						@Override
    						public void onClick(View v) {
    							// TODO Auto-generated method stub
    							openActivity2((Integer)myView.getTag());
    						}
    					});
    				}else{
    					image.setBackgroundDrawable(getResources().getDrawable(dgvLists.get(i).getDrawableResourcesId()));
    					text.setText(dgvLists.get(i).getName());
    					myView.setTag(i);
    					image.setOnClickListener(new OnClickListener() {
    						
    						@Override
    						public void onClick(View v) {
    							// TODO Auto-generated method stub
    							openActivity2((Integer)myView.getTag());
    						}
    					});
    				}
    				dgv.addView(myView);
    			}
    		}else{
    			initView();
    		}
    		
    	}
    }
    
    private void saveMainChildOrder(String childOrder){
    	SharedPreferences sp = getSharedPreferences("Main", 0);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putString("childOrder", childOrder);
    	editor.commit();
    }
    
    private String getMainChildOrder(){
    	SharedPreferences sp = getSharedPreferences("Main", 0);
    	String order = sp.getString("childOrder", "");
    	return order;
    }
    
    private void setListeners()
    {
    	dgv.setOnRearrangeListener(new OnRearrangeListener() {
			
			@Override
			public void onRearrange(int oldIndex, int newIndex) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beginAnimation() {
				// TODO Auto-generated method stub
				mBtnLogin.setText(R.string.finish);
				showLoginBtnOrInfoBtn();
			}
		});
//    	dgv.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				int tag = (Integer)arg1.getTag();
//				openActivity2(tag);
//			}
//		});
    }
    
    private void openActivity2(int tag){
    	String childName = dgvLists.get(tag).getName();
    	openActivity(childName);
    }

    static int totalMessage = 0;
    
	public void requestMessagesCallback(Object resultObj) {
		try {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			String requestMethod = biiResponseBody.getMethod();
			if (Push.PNS001.equals(requestMethod)) {
				// new && vip
				BiiHttpEngine.dissMissProgressDialog();
//				doRequestNewAndVipMessageCallback(biiResponseBody);
				Map<String, Object> map = (Map<String, Object>) (biiResponseBody.getResult());
				LogGloble.i("info", "biiResponseBody.getResult()=="+biiResponseBody.getResult().toString());
				if (map == null) {
					return;
				}
				 
				 if (map.get(Push.PNS001_TotalMessage_RESULT) != null) {
				 totalMessage = Integer.valueOf((String)map.get(Push.PNS001_TotalMessage_RESULT));
				 	if(totalMessage!=0){
				 		tv_prompt.setVisibility(View.VISIBLE);
				 		tv_prompt.setText(totalMessage+"");
				 	}else{
				 		tv_prompt.setVisibility(View.INVISIBLE);
				 	}
//				 	setNewMessage(totalMessage);
				 }
				 
				
				if (map.get(Push.PNS001_VipMessageNum_RESULT) != null) {
					vipMessageCount = Integer.valueOf((String) map.get(Push.PNS001_VipMessageNum_RESULT));
				}
				
				if (map.get(Push.PNS001_NewMessageNum_RESULT) != null) {
					newMessageCount = Integer.valueOf((String) map.get(Push.PNS001_NewMessageNum_RESULT));
				}
				// 清除通知栏缓存
				InfoServeDataCenter.getInstance().resetNotification();

				
			} 
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
	}

	
//	public void setNewMessage(int total){
//		totalMessage = total;
//	}
	
	public static int getTotalMessage(){
		
		return totalMessage;
	}
	
	public static int getNewMessage() {

		return newMessageCount;
	}

	public static int getVipMessage() {

		return vipMessageCount;
	}
	
	/**
	 * 回调前拦截，主要是统一拦截错误信息给出提示
	 */
//	@Override
//	public boolean httpRequestCallBackPre(Object resultObj) {
//		boolean stopFlag = false;
//		if (resultObj instanceof BiiResponse) {
//			// Bii请求前拦截
//			stopFlag = doBiihttpRequestCallBackPre((BiiResponse) resultObj);
//		} else if (resultObj instanceof String) {
//
//		} else if (resultObj instanceof Map) {
//
//		} else if (resultObj instanceof Bitmap) {
//
//		} else if (resultObj instanceof File) {
//
//		} else {
//			// do nothing
//		}
//
//		return stopFlag;
//	}
	
	
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
//			if(body.getMethod().equals(Push.PNS001)){
				for (BiiResponseBody body : biiResponseBodyList) {
					if(body.getMethod().equals(Push.PNS001)){
						return false;
					}
					else if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						// 消除通信框
						BaseHttpEngine.dissMissProgressDialog();
						if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求

							return false;
						}
						BiiError biiError = body.getError();
						// 判断是否存在error
						if (biiError != null) {
							//过滤错误
							LocalData.Code_Error_Message.errorToMessage(body);
							
							
							if (biiError.getCode() != null) {
								if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
																							// 要重新登录
									// TODO 错误码是否显示"("+biiError.getCode()+") "
									BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													BaseDroidApp.getInstanse().dismissErrorDialog();
													ActivityTaskManager.getInstance().removeAllActivity();
//													Intent intent = new Intent();
//													intent.setClass(MainActivity.this, LoginActivity.class);
//													startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
													BaseActivity.getLoginUtils(MainActivity.this).exe(new LoginTask.LoginCallback() {

														@Override
														public void loginStatua(boolean isLogin) {
															if(isLogin){
																ActivityTaskManager.getInstance().removeAllActivity();
																ActivityTaskManager.getInstance().removeAllSecondActivity();
															}
														}
													});
												}
											});

								} else {// 非会话超时错误拦截
									BaseDroidApp.getInstanse().createDialog(biiError.getCode(), biiError.getMessage(),
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													BaseDroidApp.getInstanse().dismissErrorDialog();
													if (BaseHttpEngine.canGoBack) {
														finish();
														BaseHttpEngine.canGoBack = false;
													}
												}
											});
								}
								return true;
							}
							// 弹出公共的错误框
							BaseDroidApp.getInstanse().dismissErrorDialog();
							BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (BaseHttpEngine.canGoBack) {
										finish();
										BaseHttpEngine.canGoBack = false;
									}
								}
							});
						} else {
							BaseDroidApp.getInstanse().dismissErrorDialog();
//							// 避免没有错误信息返回时给个默认的提示
							BaseDroidApp.getInstanse().createDialog("", getResources().getString(R.string.request_error),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse().dismissErrorDialog();
											if (BaseHttpEngine.canGoBack) {
												finish();
												BaseHttpEngine.canGoBack = false;
											}
										}
									});
						}

						return true;
					}
				}
			
		}

		return false;
		
	}

	
	/**
	 * 因网络问题（超时，无网络等）引起的通信异常的默认回调( 返回码 不是200 )<br>
	 * 子类可重写进行特殊化处理<br>
	 * 
	 * @param 请求失败的接口名称
	 *            Method
	 */
	@Override
	public void commonHttpErrorCallBack(final String requestMethod) {
		if (Push.PNS001.equals(requestMethod)) {// 退出的请求 不做任何处理
			return;
		}
		super.commonHttpErrorCallBack(requestMethod);
	}


	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}


}
