package com.chinamworld.bocmbci.base.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;

/**
 * 短信验证码接收器 用于填充验证码
 * @author xby
 */
public class SmsReceiver extends BaseReceiver {
	private SipBox smsSipBox;
	/** 短信校验码输入框的同级控件个数 */
	private static final int SIPBOX_FAMILY_COUNT = 3;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (!intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			return;
		}
		RelativeLayout parentLayout = (RelativeLayout) BaseDroidApp
				.getInstanse().getCurrentAct().findViewById(R.id.rltotal);
		if (parentLayout != null) {
			getSipBox(parentLayout);
		}
		String smsCode = getSmsCode(intent);
		// TODO 填充短信验证码
	}

	/**
	 * 获取短信验证码
	 * 
	 * @param intent
	 */
	private String getSmsContent(Intent intent) {
		String smsContent = "";
		// TODO 增加截取短信验证码的处理
		// 判断是系统短信；
			// 不再往下传播；this.abortBroadcast();
			StringBuffer sb = new StringBuffer();
			String sender = null;// 发送者
			String content = null;// 内容
			String sendtime = null;// 发送时间
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				// 通过pdus获得接收到的所有短信消息，获取短信内容；
				Object[] pdus = (Object[]) bundle.get("pdus");
				// 构建短信对象数组；
				SmsMessage[] mges = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					// 获取单条短信内容，以pdu格式存,并生成短信对象；
					mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				for (SmsMessage mge : mges) {
					sb.append("短信来自：" + mge.getDisplayOriginatingAddress()
							+ "\n");
					sb.append("短信内容：" + mge.getMessageBody());

					sender = mge.getDisplayOriginatingAddress();// 获取短信的发送者
					smsContent = mge.getMessageBody();// 获取短信的内容
					
					Date date = new Date(mge.getTimestampMillis());
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					sendtime = format.format(date);// 获取短信发送时间；

				}
				// Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG)
				// .show();
			}

		return smsContent;
	}

	/**
	 * 获取短信验证码输入框
	 * 
	 * @param viewGroup
	 *            外层的容器
	 */
	private void getSipBox(ViewGroup viewGroup) {
		if (viewGroup != null) {
			int childCount = viewGroup.getChildCount();
			for (int i = 0; i < childCount; i++) {
				View item = viewGroup.getChildAt(i);
				if (isSmsSipbox(item, childCount)) {
					setKeytoSipbox((SipBox) item);
				}
				if (hasChildView((ViewGroup) item)) {
					getSipBox((ViewGroup) item);
				} else {
					continue;
				}
			}
		}
	}

	/**
	 * 判定是否是短信校验码的输入框 目前判断条件： 1，SipBox控件 2，同组控件个数3
	 * 
	 * @param item
	 * @param childCount
	 * @return
	 */
	private boolean isSmsSipbox(View item, int childCount) {
		if (item instanceof SipBox) {
			if (childCount == SIPBOX_FAMILY_COUNT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否包含子控件
	 * 
	 * @param view
	 * @return
	 */
	private boolean hasChildView(View view) {
		if (view instanceof ViewGroup) {
			int childCount = ((ViewGroup) view).getChildCount();
			if (childCount > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 给短信校验码输入框设置信息
	 * 
	 * @param smsSipBox
	 */
	private void setKeytoSipbox(SipBox smsSipBox) {
		this.smsSipBox = smsSipBox;
	}
	
	/**
	 * 获取短信验证码(6位数字)
	 * @param intent
	 * @return
	 */
	private String getSmsCode(Intent intent){
		String smsContent = getSmsContent(intent);
		String smsCode = "";
		//TODO 添加截取短信的逻辑代码
		return smsCode;
	}

}
