package com.chinamworld.bocmbci.biz.infoserve;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.push.MessageService;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 消息服务设置--辅助功能
 * 
 * @author wanbing
 * 
 */
public class InfoServeAssistFunctionActivity extends InfoServeBaseActivity {

	private RelativeLayout llyt_clear_new_message;
	private RelativeLayout llyt_clear_vip_message;
	private RelativeLayout llyt_clear_all;
	private MessageService mMessageService;
	private PushDevice mPushDevice;
	private boolean isSucced;
	private String messageName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.infoserve_assist_function));
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();
			}
		});
		btn_right.setVisibility(View.GONE);
		addView(R.layout.service_assist_function);
		mMessageService = MessageService.getInstance(this);
		mPushDevice = PushManager.getInstance(this).getPushDevice();
		initView();
	}

	private void initView() {
		llyt_clear_new_message = (RelativeLayout) findViewById(R.id.llyt_clear_new_message);
		llyt_clear_vip_message = (RelativeLayout) findViewById(R.id.llyt_clear_vip_message);
		llyt_clear_all = (RelativeLayout) findViewById(R.id.llyt_clear_all);
		llyt_clear_new_message.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				clearNewMessages();
			}
		});
		llyt_clear_vip_message.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				clearVipMessages();
			}
		});
		llyt_clear_all.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				clearAllMessages();
			}
		});
	}

	/**
	 * 清空最新消息
	 */
	private void clearNewMessages() {

		messageName = getString(R.string.new_message);
		isSucced = mMessageService.deleteLocalMessagesByType(new MessageType[] { MessageType.New });
		if (mPushDevice == null) {
			showOptResultInfo();
			return;
		}
		List<PushMessage> newMesssages = InfoServeDataCenter.getInstance().getNewMessages();
		if (newMesssages == null || newMesssages.size() == 0) {
			showOptResultInfo();
			return;
		}
		List<String> contentIds = new ArrayList<String>();
		for (PushMessage message : newMesssages) {
			if (message != null && message.getContentId() != null) {
				contentIds.add(message.getContentId());
			}
		}
		BiiHttpEngine.showProgressDialog();
		InfoServeDataCenter.getInstance().clearCacheNewMessages();
		mMessageService.deleteMessage(mPushDevice, contentIds, this, "requestDeleteMessageCallback");
	}

	public void requestDeleteMessageCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		showOptResultInfo();
	}

	/**
	 * 清空特别提醒
	 */
	private void clearVipMessages() {

		messageName = getString(R.string.vip_message);
		isSucced = mMessageService.deleteLocalMessagesByType(new MessageType[] { MessageType.Vip });
		if (mPushDevice == null) {
			showOptResultInfo();
			return;
		}
		List<PushMessage> vipMesssages = InfoServeDataCenter.getInstance().getVipMessages();
		if (vipMesssages == null || vipMesssages.size() == 0) {
			showOptResultInfo();
			return;
		}
		List<String> contentIds = new ArrayList<String>();
		for (PushMessage message : vipMesssages) {
			if (message != null && message.getContentId() != null) {
				contentIds.add(message.getContentId());
			}
		}
		BiiHttpEngine.showProgressDialog();
		InfoServeDataCenter.getInstance().clearCacheVipMessages();
		mMessageService.deleteMessage(mPushDevice, contentIds, this, "requestDeleteMessageCallback");
	}

	/**
	 * 全部清空
	 */
	private void clearAllMessages() {
		isSucced = mMessageService.deleteLocalMessagesByType(new MessageType[] { MessageType.New });
		List<PushMessage> vipMessages = InfoServeDataCenter.getInstance().getVipMessages();
		if (vipMessages != null && !vipMessages.isEmpty()) {
			isSucced |= mMessageService.removeLocalMessages(vipMessages);
		}

		messageName = "全部消息";
		if (mPushDevice == null) {
			showOptResultInfo();
			return;
		}
		List<PushMessage> newMesssages = InfoServeDataCenter.getInstance().getNewMessages();
//		List<PushMessage> vipMesssages = InfoServeDataCenter.getInstance().getVipMessages();
		List<String> contentIds = new ArrayList<String>();
		if (newMesssages != null && newMesssages.size() > 0) {
			for (PushMessage message : newMesssages) {
				if (message != null && message.getContentId() != null) {
					contentIds.add(message.getContentId());
				}
			}
		}
		if (vipMessages != null && vipMessages.size() > 0) {
			for (PushMessage message : vipMessages) {
				if (message != null && message.getContentId() != null) {
					contentIds.add(message.getContentId());
				}
			}
		}
		if (contentIds == null || contentIds.size() == 0) {
			showOptResultInfo();
			return;
		}

		BiiHttpEngine.showProgressDialog();
		InfoServeDataCenter.getInstance().clearCacheAllMessages();
		mMessageService.deleteMessage(mPushDevice, contentIds, this, "requestDeleteMessageCallback");
	}

	/**
	 * 显示操作结果
	 */
	private void showOptResultInfo() {
		if (isSucced) {
			CustomDialog.toastInCenter(this, messageName + getString(R.string.clear_cache_success));
		} else {
			CustomDialog.toastInCenter(this, messageName + getString(R.string.clear_cache_failed));
		}
	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		super.commonHttpResponseNullCallBack(requestMethod);
		if (Push.PNS004.equals(requestMethod)) {
			// 删除消息返回为空
			showOptResultInfo();
		}
	}
	
}
