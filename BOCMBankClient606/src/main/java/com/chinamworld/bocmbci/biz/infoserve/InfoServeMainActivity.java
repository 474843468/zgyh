package com.chinamworld.bocmbci.biz.infoserve;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseApplication;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.adapter.MessageAdapter;
//import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.biz.push.MessageService;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.RUtil;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.CustomViewPager;

/**
 * 消息服务的入口类
 * 
 * @author xby
 * 
 */
/**
 * P041消息推送
 */
public class InfoServeMainActivity extends InfoServeBaseActivity implements OnClickListener {
	private static final String TAG = InfoServeMainActivity.class.getSimpleName();

//	private static final String[] CONTENT = new String[] { RUtil.getString(R.string.system_message),
//		RUtil.getString(R.string.new_message), RUtil.getString(R.string.vip_message) };

	private static final String[] CONTENT = new String[] { "系统通知", "最新消息", "特别提醒" };

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	/** 登录之后进入设置画面 */
	private final static int REQUEST_TO_SETTING_AFTER_LOGIN = 1001;
	/** 请求消息详情请求码 */
	private static final int REQUEST_MESSAGE_DETIAL = 102;
	/** 请求系统列表请求码 */
	private static final int REQUEST_SYSTEM_LIST = 103;
	/** 进入特殊消息请求码 */
	private static final int REQUEST_VIP_MESSAGE = 104;
	/** 特殊消息 */
	private PushMessage mIntentVipMessage = null;

	/** 系统消息按钮 */
	private View mSystemLayoutView;
	/** 最新消息按钮 */
	private View mNewLayoutView;
	/** 特殊消息按钮 */
	private View mVipLayoutView;
	private TextView mSystemView;
	private TextView mNewView;
	private TextView mVipView;
	// /** 系统消息指示器 */
	// private View mSystemIconView;
	/** 最新消息指示器 */
//	private View mNewIconView;
////	/** 最新消息指示器 */
//	private View mVipIconView;
	/** 消息展示列表 */
	private CustomViewPager mViewPager;
	private MyViewPageAdapter mMyViewPageAdapter;

	/** 消息集合 */
	private List<List<PushMessage>> mMessagesList;
	/** 系统消息集合 */
	private List<PushMessage> mSystemMessages;
	/** 最新消息集合 */
	private List<PushMessage> mVipMessages;
	/** 特殊消息集合 */
	private List<PushMessage> mNewMessages;

	private MessageService mMessageService;
	// private PushDevice mPushDevice;
	/** 当前选择的按钮ID */
	private int mSelectViewId = R.id.layout_new;
	/** 最后登陆设备id */
	private String mLastPushCif = null;
	
	//wuhan更多模块    消息提示/** 最新消息指示器 */
	TextView tv_system,tv_new,tv_vip;
	int vipMessageCount,newMessageCount,totalMessage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.infoserve));
		addView(R.layout.infoserver_main_activity);
		//wuhan更多模块
		ib_setting_btn.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InfoServeMainActivity.this.finish();
				
			}
		});
		
		Intent intent = getIntent();
		vipMessageCount =intent.getIntExtra("vipMessageCount", 0);
		newMessageCount = intent.getIntExtra("newMessageCount", 0);
		if(vipMessageCount!=0&&newMessageCount!=0){
			BaseApplication.vipMessageCount = vipMessageCount;
			BaseApplication.newMessageCount = newMessageCount;
		}
//		totalMessage = intent.getIntExtra("totalMessage", 0);
		
//		ib_setting_btn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(InfoServeMainActivity.this,InfoServeSettingActivity.class);
//				startActivity(intent);
//				
//			}
//		});
		
		findView();

		PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();
		mMessageService = MessageService.getInstance(this);
		// 优化本地数据库，如果数据库过大进行删除
		mMessageService.optimizeLocalPushMessage(pushDevice != null ? pushDevice.getDeviceId() : null);
		InfoServeDataCenter.getInstance().resetNotification();

		mMessagesList = new ArrayList<List<PushMessage>>();
		mSystemMessages = new ArrayList<PushMessage>();
		mNewMessages = mMessageService.getLocalNewMessages();
		mVipMessages = mMessageService.getLocalVipMessages(pushDevice != null ? pushDevice.getDeviceId() : null);

		mMessagesList.add(mSystemMessages);
		mMessagesList.add(mNewMessages);
		mMessagesList.add(mVipMessages);

		InfoServeDataCenter.getInstance().setVipMessages(mVipMessages);
		InfoServeDataCenter.getInstance().setNewMessages(mNewMessages);
		refreshListView();

		// 处理分支
		boolean processSrc = processSrc(getIntent());
		if (!processSrc) {
			// 处理分支失败打开最新列表
			performClickMessageList(MessageType.New, PushManager.getInstance(this).getPushDevice() != null);
		}

		btn_right.setVisibility(View.GONE);
		ib_setting_btn.setVisibility(View.VISIBLE);
		ib_setting_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (isLogin(REQUEST_TO_SETTING_AFTER_LOGIN, true, R.string.need_login_infoserve_setting)) {
					goToInfoServeSettingActivity();
				}
			}
		});
		
		
		

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LogGloble.i(TAG, "onNewIntent");
		processSrc(intent);
	}

	/**
	 * 处理来路分支，比如来自通知栏
	 * 
	 * @param intent 意图
	 */
	private boolean processSrc(Intent intent) {
		if (intent != null) {
			String src = intent.getStringExtra(Push.INTENT_SRC);
			if (Push.INTENT_NOTIFICATION.equals(src)) {
				// 处理从通知栏过来的
				boolean nofitionReset = intent.getBooleanExtra(Push.INTENT_RESET, false);
				if (nofitionReset) {
					InfoServeDataCenter.getInstance().resetNotification();
				}
				PushMessage msg = (PushMessage) getIntent().getSerializableExtra(Push.INTENT_MESSAGE);
				if (msg != null && msg.getMessageType() != null) {
					performClickMessageList(msg.getMessageType(), true);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogGloble.i(TAG, "onResume");
		//wuhan
		
		// 由于启动模式为SingleTop，防止未登陆进入没有绑定机会
		// 登陆用户改变判断
		if (mLastPushCif == null) {
			PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();
			if (pushDevice != null) {
				mLastPushCif = PushDevice.deviceIdToCifId(pushDevice.getDeviceId());
			}
		}
		String loginCif = getLoginCif();
		if (loginCif != null && !loginCif.equals(mLastPushCif)) {
			mLastPushCif = loginCif;
			mVipMessages.clear();
			mIntentVipMessage = null;
			String msgId = PushDevice.cifIdToDeviceId(loginCif);
			if (msgId != null) {
				List<PushMessage> localVipMessages = mMessageService.getLocalVipMessages(msgId);
				for (PushMessage p : localVipMessages) {
					mVipMessages.add(p);
				}
			}
			refreshListView();
			InfoServeDataCenter.getInstance().setIsRefreshMessages(false);
		}

		refreshTable();
		
		
	}

	private String getLoginCif() {
		String cif = null;
		HashMap<String, Object> bizDataMap = BaseDroidApp.getInstanse().getBizDataMap();
		if (!StringUtil.isNullOrEmpty(bizDataMap)) {
			Map<String, Object> loginMap = (Map<String, Object>) bizDataMap.get(ConstantGloble.BIZ_LOGIN_DATA);
			if (!StringUtil.isNullOrEmpty(loginMap)) {
				cif = (String) loginMap.get(Login.REGISTER_CIF_NUMBER);
			}
		}
		return cif;
	}

	@Override
	public void onClick(View v) {
		boolean isRefresh = false;
		if (v.getId() == R.id.layout_system) {
			LogGloble.i(TAG, "onClick system");
			// 系统消息
			if (isLogin(REQUEST_SYSTEM_LIST, true, R.string.need_login_infoserve_system_content)) {
				mViewPager.setCurrentItem(0);
				isRefresh = true;
				BiiHttpEngine.showProgressDialog();
				requestPsnSvrGlobalMsgList();
			}
		} else if (v.getId() == R.id.layout_new) {
			LogGloble.i(TAG, "onClick new");
			//wuhan
			tv_new.setVisibility(View.GONE);
			BaseApplication.newMessageCount = 0;
			InfoServeDataCenter.getInstance().setUserShowNewMessageIndicator(false);
			// 最新消息
			mViewPager.setCurrentItem(1);
			isRefresh = true;
			PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();
			if (pushDevice != null) {
				BiiHttpEngine.showProgressDialog();
				mMessageService.getMessages(pushDevice.getDeviceType(), pushDevice.getDeviceId(), this,
						"requestMessagesCallback");
			}
		} else if (v.getId() == R.id.layout_vip) {
			LogGloble.i(TAG, "onClick vip");
//			wuhan
			tv_vip.setVisibility(View.GONE);
			BaseApplication.vipMessageCount = 0;
			InfoServeDataCenter.getInstance().setUserShowVipMessageIndicator(false);
			// 特殊消息
			mViewPager.setCurrentItem(2);
			isRefresh = true;
			PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();
			if (pushDevice != null) {
				BiiHttpEngine.showProgressDialog();
				mMessageService.getMessages(pushDevice.getDeviceType(), pushDevice.getDeviceId(), this,
						"requestMessagesCallback");
			}
		}
		if (isRefresh) {
			mSelectViewId = v.getId();
			refreshTable();
		}
	}

	private void refreshTable() {
		int selectid = mSelectViewId;
		boolean isShow = false;

//		for (PushMessage m : mNewMessages) {
//			if (!m.isReaded()) {
//				isShow = true;
//				break;
//			}
//		}
		//原来的
//		mNewIconView.setVisibility(isShow ? View.VISIBLE : View.GONE);
//		tv_new.setVisibility(isShow ? View.VISIBLE : View.GONE);
		//wuhan
//		if(mNewMessages.size()!=0){
//			tv_new.setVisibility(View.VISIBLE);
//			tv_new.setText(mNewMessages.size()+"");
//		}
		

		isShow = false;
//		for (PushMessage m : mVipMessages) {
//			if (!m.isReaded()) {
//				isShow = true;
//				break;
//			}
//		}
		
		//原来的
//		mVipIconView.setVisibility(isShow ? View.VISIBLE : View.GONE);
		
		//wuhan
//		if(mVipMessages.size()!=0){
//			tv_vip.setVisibility(View.VISIBLE);
//			tv_vip.setText(mVipMessages.size()+"");
//		}
		
		mSystemLayoutView.setBackgroundResource(R.drawable.button_left_selector);
		mNewLayoutView.setBackgroundResource(R.drawable.button_middle_selector);
		mVipLayoutView.setBackgroundResource(R.drawable.button_right_selector);
		if (selectid == R.id.layout_system) {
			// 系统消息
			mSystemLayoutView.setBackgroundResource(R.drawable.btn_3_left_red);
			mSystemView.setTextColor(getResources().getColor(R.color.white));
			mNewView.setTextColor(getResources().getColor(R.color.black));
			mVipView.setTextColor(getResources().getColor(R.color.black));
			moveArrowUp(mSystemLayoutView);
		} else if (selectid == R.id.layout_new) {
			// 最新消息
			tv_new.setVisibility(View.GONE);
			mNewLayoutView.setBackgroundResource(R.drawable.middle_three_red);
			mSystemView.setTextColor(getResources().getColor(R.color.black));
			mNewView.setTextColor(getResources().getColor(R.color.white));
			mVipView.setTextColor(getResources().getColor(R.color.black));
			moveArrowUp(mNewLayoutView);
		} else if (selectid == R.id.layout_vip) {
			// 特殊消息
			tv_vip.setVisibility(View.GONE);
			mVipLayoutView.setBackgroundResource(R.drawable.right_three_red);
			mSystemView.setTextColor(getResources().getColor(R.color.black));
			mNewView.setTextColor(getResources().getColor(R.color.black));
			mVipView.setTextColor(getResources().getColor(R.color.white));
			moveArrowUp(mVipLayoutView);
		}
		// 更新底部消息指示器
		updateMessageView();
	}

	private void moveArrowUp(View srcView) {
		int[] location = new int[2];
		srcView.getLocationInWindow(location);
		// 中心点
		int srcX = location[0] + srcView.getWidth() / 2;
		View arrowView = findViewById(R.id.arrow_up);
		arrowView.getLocationInWindow(location);
		final int arrowX = location[0] + arrowView.getWidth() / 2;
		if (Math.abs(srcX - arrowX) > 5) {
			View contentRootView = findViewById(R.id.content_root);
			int[] contentLocation = new int[2];
			contentRootView.getLocationInWindow(contentLocation);
			RelativeLayout.LayoutParams mp = (RelativeLayout.LayoutParams) arrowView.getLayoutParams();
			mp.leftMargin = srcX - contentLocation[0] - arrowView.getWidth() / 2;
			mp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
			arrowView.setLayoutParams(mp);
			arrowView.requestLayout();
		}
	}

	private class MyViewPageAdapter extends PagerAdapter {

		private List<List<PushMessage>> lists;

		public MyViewPageAdapter(List<List<PushMessage>> lists) {
			super();
			this.lists = lists;
		}

		public void setData(List<List<PushMessage>> messageList) {
			lists = messageList;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(InfoServeMainActivity.this, R.layout.list_item, null);
			ListView lv = (ListView) view.findViewById(R.id.list_message);
			List<? extends PushMessage> list = lists.get(position);
			MessageAdapter messageAdapter = new MessageAdapter(InfoServeMainActivity.this, list, lv);
			lv.setAdapter(messageAdapter);
			lv.setOnItemClickListener(new MyOnItemClickListener(list));
			container.addView(view);
			return view;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

	}

	private class MyOnItemClickListener implements OnItemClickListener {

		private List<? extends PushMessage> list;

		public MyOnItemClickListener(List<? extends PushMessage> list) {
			super();
			this.list = list;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (list.size() > position) {
				PushMessage baseMessage = list.get(position);
				mIntentVipMessage = baseMessage;
				if (baseMessage.isNeedLogin()
						&& !isLogin(REQUEST_VIP_MESSAGE, true, R.string.need_login_infoserve_vip_content)) {
					// 需要登陆没有登陆
					return;
				}
				goToInfoServeDetialActivity(baseMessage);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_TO_SETTING_AFTER_LOGIN) {
				goToInfoServeSettingActivity();
			} else if (requestCode == REQUEST_MESSAGE_DETIAL) {
				String msgId = data.getStringExtra(Push.INTENT_CONTENT_ID);
				String deviceId = data.getStringExtra(Push.INTENT_DEVICE_ID);
				MessageType msgType = (MessageType) data.getSerializableExtra(Push.INTENT_TYPE);
				String msgContent = data.getStringExtra(Push.INTENT_CONTENT);
				boolean isSuccessd = setMessageReaded(msgId, deviceId, msgType, msgContent, true);
				if (isSuccessd) {
					// 排序
					if (msgType == MessageType.New) {
						Collections.sort(mNewMessages, mPushMessageCompartor);
					} else if (msgType == MessageType.Vip) {
						Collections.sort(mVipMessages, mPushMessageCompartor);
					}
					// refreshTable();
					refreshListView();
				} else {
					performClickMessageList(msgType, true);
				}
			} else if (requestCode == REQUEST_SYSTEM_LIST) {
				// 登陆成功进入系统列表
				performClickMessageList(MessageType.System, true);
			} else if (requestCode == REQUEST_VIP_MESSAGE) {
				// 登陆成功进入消息详情
				if (mIntentVipMessage != null) {
					goToInfoServeDetialActivity(mIntentVipMessage);
				}
			}
		}
	}

	
	@Override
	public void onBackPressed() {
		if(!BaseDroidApp.isExit){
			BaseDroidApp.isExit = true;
			CustomDialog.toastInCenter(getApplicationContext(), getResources().getString(R.string.exit_first_info));
            // 利用handler延迟发送更改状态信息
			BaseDroidApp.getInstanse().mHandlerExit.sendEmptyMessageDelayed(0, SystemConfig.EXIT_BETWEEN_TIME);
		}else{
			exitApp();
		}
	}

	/**
	 * 删除消息
	 * 
	 * @param message
	 */
	public void deleteMessage(PushMessage message) {
		// 删除本地数据
		mMessageService.deleteLocalMessage(message);
		// 未读消息删除网络数据
		if (!message.isReaded()) {
			// BiiHttpEngine.showProgressDialog();
			PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();
			if (pushDevice != null) {
				mMessageService.deleteMessage(pushDevice, message.getContentId(), this, "requestDeleteMessageCallBack");
			}
		}
		// 删除成功
		CustomDialog.toastInCenter(this, getString(R.string.delete_success));
		refreshListView();
	}

	// ---------------------------------------------------------------------------------
	// request & callback

	/**
	 * 请求最新消息的列表请求
	 */
	private void requestPsnSvrGlobalMsgList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSNSVRGLOBALMSGLIST);
		HttpManager.requestBii(biiRequestBody, this, "requestMessagesCallback");
	}

	/**
	 * 删除消息返回
	 */
	public void requestDeleteMessageCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
	}

	public void requestMessagesCallback(Object resultObj) {
		try {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			String requestMethod = biiResponseBody.getMethod();
			if (Push.PNS001.equals(requestMethod)) {
				// new && vip
				BiiHttpEngine.dissMissProgressDialog();
				doRequestNewAndVipMessageCallback(biiResponseBody);

			} else if (Login.PSNSVRGLOBALMSGLIST.equals(requestMethod)) {
				// system
				BiiHttpEngine.dissMissProgressDialog();
				doSystemMessageCallback(biiResponseBody);
			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
	}

	private void doSystemMessageCallback(BiiResponseBody biiResponseBody) {
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (resultList != null) {
			mSystemMessages.clear();
			for (Map<String, String> map : resultList) {
				String notification = map.get(Login.SUBJECT);
				String content = map.get(Login.CONTENT);
				PushMessage vipMessage = new PushMessage();
				vipMessage.setNotification(notification);
				vipMessage.setContent(content);
				vipMessage.setMessageType(MessageType.System);
				mSystemMessages.add(vipMessage);
			}
			refreshTable();
			refreshListView();
		}
	}

	private void doRequestNewAndVipMessageCallback(BiiResponseBody biiResponseBody) {
		Map<String, Object> map = (Map<String, Object>) (biiResponseBody.getResult());
		if (map == null) {
			return;
		}
		// int totalMessage = 0;
		// if (map.get(Push.PNS001_TotalMessage_RESULT) != null) {
		// totalMessage = Integer.valueOf((String)
		// map.get(Push.PNS001_TotalMessage_RESULT));
		// }

		int vipMessageCount = 0;
		if (map.get(Push.PNS001_VipMessageNum_RESULT) != null) {
			vipMessageCount = Integer.valueOf((String) map.get(Push.PNS001_VipMessageNum_RESULT));
		}
		int newMessageCount = 0;
		if (map.get(Push.PNS001_NewMessageNum_RESULT) != null) {
			newMessageCount = Integer.valueOf((String) map.get(Push.PNS001_NewMessageNum_RESULT));
		}

		// 获取本地
		// 特殊消息 && 最新消息
		mVipMessages.clear();
		mNewMessages.clear();
		// 清除通知栏缓存
		InfoServeDataCenter.getInstance().resetNotification();

		PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();
		List<PushMessage> newMessages = mMessageService.getLocalNewMessages();
		List<PushMessage> vipMessages = mMessageService.getLocalVipMessages(pushDevice != null ? pushDevice
				.getDeviceId() : null);

		for (PushMessage pm : newMessages) {
			mNewMessages.add(pm);
		}
		for (PushMessage pm : vipMessages) {
			mVipMessages.add(pm);
		}

		if (vipMessageCount > 0) {
			List<Map<String, String>> vipLists = (List<Map<String, String>>) map.get(Push.PNS001_VipMessageList_RESULT);
			if (vipLists != null && !vipLists.isEmpty()) {
				for (Map<String, String> vip : vipLists) {
					try {
						String contentId = vip.get(Push.PNS001_ContentID_RESULT);
						String notification = vip.get(Push.PNS001_Notification_RESULT);
						String sourceId = vip.get(Push.PNS001_SourceID_RESULT);
						String contentStyle = vip.get(Push.PNS001_ContentStyle_RESULT);
						String applicationid = vip.get(Push.PNS001_ApplicationID_RESULT);
						String dateTime = vip.get(Push.PNS001_DateTime_RESULT);
						PushMessage vipMessage = new PushMessage();
						vipMessage.setContentId(contentId);
						vipMessage.setNotification(notification);
						vipMessage.setSourceId(sourceId);
						vipMessage.setContentStyle(contentStyle);
						vipMessage.setApplicationId(applicationid);
						vipMessage.setDateTime(sdf.parse(dateTime).getTime());
						mVipMessages.add(vipMessage);
					} catch (Exception e) {
						LogGloble.e(TAG, e.getMessage(), e);
					}
				}
			}
		}

		if (newMessageCount > 0) {
			List<Map<String, String>> newLists = (List<Map<String, String>>) map.get(Push.PNS001_NewMessageList_RESULT);
			if (newLists != null && !newLists.isEmpty()) {
				for (Map<String, String> vip : newLists) {
					try {
						String contentId = vip.get(Push.PNS001_ContentID_RESULT);
						String notification = vip.get(Push.PNS001_Notification_RESULT);
						String sourceId = vip.get(Push.PNS001_SourceID_RESULT);
						String contentStyle = vip.get(Push.PNS001_ContentStyle_RESULT);
						String applicationid = vip.get(Push.PNS001_ApplicationID_RESULT);
						String dateTime = vip.get(Push.PNS001_DateTime_RESULT);
						PushMessage newMessage = new PushMessage();
						newMessage.setContentId(contentId);
						newMessage.setNotification(notification);
						newMessage.setSourceId(sourceId);
						newMessage.setContentStyle(contentStyle);
						newMessage.setApplicationId(applicationid);
						newMessage.setDateTime(sdf.parse(dateTime).getTime());
						mNewMessages.add(newMessage);
					} catch (Exception e) {
						LogGloble.e(TAG, e.getMessage(), e);
					}
				}
			}
		}

		// 排序
		Collections.sort(mVipMessages, mPushMessageCompartor);
		Collections.sort(mNewMessages, mPushMessageCompartor);
		// 保存数据供辅助功能清除使用
		InfoServeDataCenter.getInstance().setVipMessages(mVipMessages);
		InfoServeDataCenter.getInstance().setNewMessages(mNewMessages);
		
		refreshTable();
		refreshListView();
	}

	private Comparator<PushMessage> mPushMessageCompartor = new Comparator<PushMessage>() {
		@Override
		public int compare(PushMessage object1, PushMessage object2) {
			long objTime1 = object1.getDateTime();
			long objTime2 = object2.getDateTime();
			if (!object1.isReaded() && !object2.isReaded()) {
				// 都为未读
			} else if (!object1.isReaded()) {
				// o1为未读
				return -1;
			} else if (!object2.isReaded()) {
				// o2为未读
				return 1;
			}
			// 都为以读或为未读
			if (objTime1 > objTime2) {
				return -1;
			} else if (objTime1 < objTime2) {
				return 1;
			}
			return 0;
		}
	};

	// ------------------------------------------------------------------------------------
	// private method

	/**
	 * 打开指定的消息列表
	 *
	 * @param 消息类型
	 * @param 是否显示消息框
	 */
	private void performClickMessageList(MessageType messageType, boolean isShowDialog) {
		if (messageType != null && isShowDialog) {
			BiiHttpEngine.showProgressDialog();
		}
		if (messageType == MessageType.New) {
			mNewLayoutView.performClick();
		} else if (messageType == MessageType.Vip) {
			mVipLayoutView.performClick();
		} else if (messageType == MessageType.System) {
			mSystemLayoutView.performClick();
		} else {
			LogGloble.e(TAG, "没有指定的刷新列表");
		}
	}

	// 设置PushMessage是否已读
	private boolean setMessageReaded(String msgId, String deviceId, MessageType type, String content, boolean isReaded) {
		List<PushMessage> list = null;
		if (type == MessageType.New) {
			list = mNewMessages;
		} else if (type == MessageType.Vip) {
			list = mVipMessages;
		}
		if (list != null) {
			for (PushMessage msg : list) {
				if (msgId.equals(msg.getContentId())) {
					msg.setDeviceID(deviceId);
					msg.setContent(content);
					msg.setReaded(true);
					return true;
				}
			}
		}
		return false;
	}

	private void refreshListView() {
		if (mMyViewPageAdapter == null) {
			mMyViewPageAdapter = new MyViewPageAdapter(mMessagesList);
			mViewPager.setAdapter(mMyViewPageAdapter);
		} else {
			mMyViewPageAdapter.setData(mMessagesList);
			mMyViewPageAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 判断是否已登录，未登录则跳到登录画面。
	 * 
	 * @return
	 */
	private boolean isLogin(final int requestCode, boolean isShowDialog, int resId) {
		boolean isLogin = BaseDroidApp.getInstanse().isLogin();
		if (!isLogin) {
			if (isShowDialog) {
				BaseDroidApp.getInstanse().showErrorDialog(null, getString(resId), new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (v.getTag() != null) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							switch ((Integer) v.getTag()) {
							case CustomDialog.TAG_SURE:// 确定
//								Intent intent = new Intent(InfoServeMainActivity.this, LoginActivity.class);
//								startActivityForResult(intent, requestCode);
								BaseActivity.getLoginUtils(InfoServeMainActivity.this).exe(new LoginTask.LoginCallback() {

									@Override
									public void loginStatua(boolean isLogin) {
										if(isLogin){
											if (requestCode == REQUEST_TO_SETTING_AFTER_LOGIN) {
												goToInfoServeSettingActivity();
											} else if (requestCode == REQUEST_SYSTEM_LIST) {
												// 登陆成功进入系统列表
												performClickMessageList(MessageType.System, true);
											} else if (requestCode == REQUEST_VIP_MESSAGE) {
												// 登陆成功进入消息详情
												if (mIntentVipMessage != null) {
													goToInfoServeDetialActivity(mIntentVipMessage);
												}
											}
										}
									}
								});
								break;
							}
						}
					}
				});
			} else {
//				Intent intent = new Intent(this, LoginActivity.class);
//				startActivityForResult(intent, requestCode);
				BaseActivity.getLoginUtils(InfoServeMainActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {
						if(isLogin){
							if (requestCode == REQUEST_TO_SETTING_AFTER_LOGIN) {
								goToInfoServeSettingActivity();
							} else if (requestCode == REQUEST_SYSTEM_LIST) {
								// 登陆成功进入系统列表
								performClickMessageList(MessageType.System, true);
							} else if (requestCode == REQUEST_VIP_MESSAGE) {
								// 登陆成功进入消息详情
								if (mIntentVipMessage != null) {
									goToInfoServeDetialActivity(mIntentVipMessage);
								}
							}
						}
					}
				});
			}
		}
		return isLogin;
	}

	/**
	 * 跳转到消息服务设置画面
	 */
	/**
	 * 查询版用户直接进入设置页面
	 */
	private void goToInfoServeSettingActivity() {
		Intent intent = new Intent(this, InfoServeSettingActivity.class);
		startActivity(intent);
		//------------------------------------------------------------------------------------
		//进入设置不需要查询理财产品
		/* 
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String segmentId = null;
		if (!StringUtil.isNullOrEmpty(returnMap)) {
			segmentId = (String) returnMap.get(Crcd.CRCD_SEGMENTID);
		}
		if (StringUtil.isNull(segmentId) || ConstantGloble.CRCD_TEN.equals(segmentId)) {
			Intent intent = new Intent();
			intent.setClass(this, InfoServeSettingActivity.class);
			startActivity(intent);
		} else {
			requestPsnXpadDueDateRemindQuery();
		}*/
	}

//	@Override
//	public void requestPsnXpadDueDateRemindQueryCallback(Object resultObj) {
//		super.requestPsnXpadDueDateRemindQueryCallback(resultObj);
//		Intent intent = new Intent();
//		intent.setClass(this, InfoServeSettingActivity.class);
//		startActivity(intent);
//	}

	private void goToInfoServeDetialActivity(PushMessage pushMessage) {
		// 如果是特殊消息
		if (pushMessage.getMessageType() == MessageType.Vip) {
			String loginCif = getLoginCif();
			String mdID = pushMessage.getDeviceID();
			String cifId = PushDevice.deviceIdToCifId(mdID);
			LogGloble.i(TAG, "loginCif : " + loginCif);
			LogGloble.i(TAG, "meg cifId : " + cifId);
			if (cifId != null && !cifId.equals(loginCif)) {
				performClickMessageList(pushMessage.getMessageType(), true);
				return;
			}
		}

		Intent intent = new Intent(getActivity(), InfoServeDetialActivity.class);
		intent.putExtra(Push.INTENT_MESSAGE, pushMessage);
		startActivityForResult(intent, REQUEST_MESSAGE_DETIAL);
	}

	private void findView() {
		mViewPager = (CustomViewPager) findViewById(R.id.pager);

		mSystemLayoutView = findViewById(R.id.layout_system);
		mNewLayoutView = findViewById(R.id.layout_new);
		mVipLayoutView = findViewById(R.id.layout_vip);
		
		mSystemView = (TextView) findViewById(R.id.btn_system);
		mNewView = (TextView) findViewById(R.id.btn_new);
		mVipView = (TextView) findViewById(R.id.btn_vip);

		mSystemLayoutView.setOnClickListener(this);
		mNewLayoutView.setOnClickListener(this);
		mVipLayoutView.setOnClickListener(this);

		// mSystemIconView = findViewById(R.id.view_system);
		//原来
//		mNewIconView = findViewById(R.id.view_new);
//		mVipIconView = findViewById(R.id.view_vip);
		
		//wuhan
		tv_new = (TextView) findViewById(R.id.tv_new);
		tv_vip = (TextView) findViewById(R.id.tv_vip);
		tv_system = (TextView) findViewById(R.id.tv_system);
//		mSystemIconView = findViewById(R.id.tv_system);
		
		if(BaseApplication.vipMessageCount!=0){
			tv_vip.setVisibility(View.VISIBLE);
			tv_vip.setText(BaseApplication.vipMessageCount+"");
		}
		
		if(BaseApplication.newMessageCount!=0){
			tv_new.setVisibility(View.VISIBLE);
			tv_new.setText(BaseApplication.newMessageCount+"");
		}
	}

	private InfoServeMainActivity getActivity() {
		return this;
	}

	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {
				if (Push.PNS001.equals(body.getMethod())) {
					// 最新消息或特殊消息列表为空不弹出对话框
					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						BiiHttpEngine.dissMissProgressDialog();
						return true;
					}
				} else {
					return super.doBiihttpRequestCallBackPre(response);
				}
			}
		}

		return false;
	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		// 去掉对话框
		// super.commonHttpErrorCallBack(requestMethod);
	}

	// *************************************************************************************

}
