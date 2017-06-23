package com.chinamworld.bocmbci.biz.infoserve;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.BocCommonTools;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.adapter.IntentSpannable;
import com.chinamworld.bocmbci.biz.infoserve.adapter.IntentSpannable.IntentMatchFilter;
import com.chinamworld.bocmbci.biz.infoserve.adapter.IntentSpannable.SpanItemResult;
import com.chinamworld.bocmbci.biz.push.MessageService;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息服务的详细内容展示类
 * 
 * @author xby
 * 
 */
public class InfoServeDetialActivity extends InfoServeBaseActivity {

	private static final String TAG = InfoServeDetialActivity.class
			.getSimpleName();

	private MessageService mMessageService;
	private PushDevice mPushDevice;

	private PushMessage mPushMessage;
	private Intent mLastIntent;
	String replaceAll = null;
	private List<Point> listpoint;
	private List<String> listurl;
	String text = "";
	TextView tvContent;
	Spannable sp;
	private String newContent;
	private String starttag = "<image>";
	private String endtag = "</image>";
	private String replaytag = "image";
	private boolean hasTag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.service_info_detial1);

		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		initView();
		mMessageService = MessageService.getInstance(this);
		mPushDevice = PushManager.getInstance(this).getPushDevice();
		listpoint = new ArrayList<InfoServeDetialActivity.Point>();
		listurl = new ArrayList<String>();
		Intent intent = getIntent();
		// 处理分支
		boolean processSrc = processSrc(intent);
		if (!processSrc) {
			mPushMessage = (PushMessage) intent
					.getSerializableExtra(Push.INTENT_MESSAGE);
			if (mPushMessage == null) {
				finish();
				return;
			}
			if (mPushMessage.getMessageType() == MessageType.System) {
				refreshView(mPushMessage);
			} else {
				if (!mPushMessage.isReaded()) {
					// 未读
					if (mPushDevice == null) {
						finish();
						return;
					}
				}
				requestMessage();
			}
		}
	}

	private boolean processSrc(Intent intent) {
		if (intent != null) {
			String src = intent.getStringExtra(Push.INTENT_SRC);
			if (Push.INTENT_NOTIFICATION.equals(src)) {
				// 处理从通知栏过来的
				boolean nofitionReset = intent.getBooleanExtra(
						Push.INTENT_RESET, false);
//				int notifiCacheNum=InfoServeDataCenter.getInstance().getCacheNotificationNumber();
//				InfoServeDataCenter.getInstance().setNotificationNumber(notifiCacheNum-1);
				if (nofitionReset) {
					InfoServeDataCenter.getInstance().resetNotification();
				}
				PushMessage msg = (PushMessage) getIntent()
						.getSerializableExtra(Push.INTENT_MESSAGE);
				if (msg != null && msg.getMessageType() != null) {
					mLastIntent = intent;
					mPushMessage = msg;
					requestMessage();
					return true;
				}
			}
		}
		return false;
	}

	private void requestMessage() {
		if (mPushMessage.getMessageType() == MessageType.System
				|| mPushMessage.isReaded()) {
			refreshView(mPushMessage);
		} else {
			if (mPushMessage.getMessageType() == MessageType.Vip) {
				BiiHttpEngine.showProgressDialogCanGoBack();
				mMessageService.getTicketForMessage(
						mPushMessage.getContentId(), this,
						"requestTicketCallback");
			} else {
				BiiHttpEngine.showProgressDialogCanGoBack();
				mMessageService.getNewMessage(mPushDevice,
						mPushMessage.getContentId(), this,
						"requestMessageCallback");
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		processSrc(intent);
	}

	@Override
	public void onBackPressed() {
		if (mLastIntent != null) {
			goInfoServeMainActivity();
		}
		super.onBackPressed();
	}

	private void goInfoServeMainActivity() {
		String src = mLastIntent.getStringExtra(Push.INTENT_SRC);
		if (Push.INTENT_NOTIFICATION.equals(src)) {
			// 如果从通知栏刷新列表
			PushMessage msg = (PushMessage) mLastIntent
					.getSerializableExtra(Push.INTENT_MESSAGE);
			if (msg != null) {
				new BocCommonTools().toMessagePage(1,"1");
//				Intent intent = new Intent(this, InfoServeMainActivity.class);
//				intent.putExtra(Push.INTENT_SRC, Push.INTENT_NOTIFICATION);
//				intent.putExtra(Push.INTENT_MESSAGE, msg);
//				startActivity(intent);
			}
		}
	}

	public void requestMessageCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (resultMap != null && !resultMap.isEmpty()) {
			String contentId = resultMap.get(Push.PNS002_ContentID_RESULT);
			String content = resultMap.get(Push.PNS002_Content_RESULT);
			//
			if (contentId != null
					&& contentId.equals(mPushMessage.getContentId())) {
				mPushMessage.setContentId(contentId);
				mPushMessage.setContent(content);
				refreshView(mPushMessage);
				// 先刷新在设置为已读
				mPushMessage.setReaded(true);
				mPushMessage.setDeviceID(mPushDevice.getDeviceId());
				mMessageService.updateLocalMessage(mPushMessage);

				Intent resultIntent = new Intent();
				resultIntent.putExtra(Push.INTENT_DEVICE_ID,
						mPushMessage.getDeviceID());
				resultIntent.putExtra(Push.INTENT_CONTENT_ID,
						mPushMessage.getContentId());
				resultIntent.putExtra(Push.INTENT_TYPE,
						mPushMessage.getMessageType());
				resultIntent.putExtra(Push.INTENT_CONTENT,
						mPushMessage.getContent());
				setResult(Activity.RESULT_OK, resultIntent);
			}
		}
	}

	public void requestTicketCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (resultMap != null && !resultMap.isEmpty()) {
			String ticketResult = resultMap.get(Push.Ticket);
			if (ticketResult != null) {
				mMessageService.getVipMessage(mPushDevice,
						mPushMessage.getContentId(), ticketResult, this,
						"requestMessageCallback");
			}
		}
	}

	boolean isPageFinished = false;

	public void refreshView(PushMessage message) {
		ScrollView sv = (ScrollView) findViewById(R.id.info_sv);
		TextView tvTitle = (TextView) findViewById(R.id.info_title);
		TextView tvTime = (TextView) findViewById(R.id.info_time);
		tvContent = (TextView) findViewById(R.id.info_content);
		
		View newView = findViewById(R.id.info_new);
		sv.setBackgroundColor(0);
		sv.setBackgroundResource(R.drawable.infoserve_bg);
		tvTitle.setText(message.getNotification());

		String time = null;
		try {
			if (message.getMessageType() != MessageType.System) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				// tvTime.setText(sdf.format(new Date(message.getDateTime())));
				// tvTime.setVisibility(View.VISIBLE);
				time = sdf.format(new Date(message.getDateTime()));
				tvTime.setText(time);
				tvTime.setVisibility(View.VISIBLE);
			} else {
				tvTime.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}

		if (message.getMessageType() != MessageType.System) {
			// 加标签[~@点击这里~@]
			final IntentSpannable is = new IntentSpannable();
			String regex1 = "http.*?@@";
			String regex2 = "<image>.*?</image>";
			newContent = message.getContent();
			donewmessage(message.getContent(), regex2);
			if(hasTag){
				newContent = newContent.replaceAll(starttag, "\r\n" + starttag)
						.replaceAll(endtag, endtag + "\r\n");	
			}
			
			is.setContent(newContent, regex1, new IntentMatchFilter() {
				@Override
				public SpanItemResult match(String srcContent) {
					String replaceFirst = srcContent.replaceFirst("http",
							"http");
					replaceAll = replaceFirst.replaceFirst("@@", "");
					MyClickableSpan myClickableSpan = new MyClickableSpan(
							replaceAll);
					return is.new SpanItemResult(replaceAll, myClickableSpan);
				}
			});

			sp = is.process();
			String str = is.process().toString();
			doImageSpan(str, regex2);
			
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int tw=dm.widthPixels-getResources().getDimensionPixelSize(R.dimen.dp_one_zero)*2-getResources().getDimensionPixelSize(R.dimen.fill_margin_left)*2;
			Drawable imageCodeDrawable = getResources().getDrawable(
					R.drawable.ic_launcher);
			float w=imageCodeDrawable.getIntrinsicWidth();
			float h=imageCodeDrawable.getIntrinsicHeight();
			for (Point po : listpoint) {	
				
				imageCodeDrawable.setBounds((int)(tw - w) / 2,0,(int)((tw - w)/ 2 + w),(int)h);
				sp.setSpan(new ImageSpan(imageCodeDrawable), po.start, po.end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			tvContent.setText(sp);
			if (listurl.size() > 0) {
				HttpManager.requestImage(listurl.get(0),
						ConstantGloble.GO_METHOD_GET, null, this,
						"imageCallBackMethod");
			}

		} else {
			tvContent.setText(ToDBC(message.getContent()));

		}

		addLinkMovementMethod(tvContent);
		newView.setVisibility(message.isReaded() ? View.INVISIBLE
				: View.VISIBLE);
		findViewById(R.id.layout_root).setVisibility(View.VISIBLE);
	}

	private void donewmessage(String c, String regex) {

		String content = new String(c);
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(content);
		if (matcher.find()) {
			hasTag=true;
			String srcGroup = matcher.group();
			String url = srcGroup.replaceFirst(starttag, "").replaceFirst(
					endtag, "");
			listurl.add(url);
			newContent = newContent.replaceFirst(url, replaytag);
			String replace = content.replaceFirst(srcGroup, "");
			donewmessage(replace, regex);
		}

	}

	@SuppressWarnings("deprecation")
	public void imageCallBackMethod(Object resultObj) {
		
		// 取消通讯框
		Bitmap bitmap = (Bitmap) resultObj;
		Drawable imageCodeDrawable = new BitmapDrawable(bitmap);
		float w=imageCodeDrawable.getIntrinsicWidth()*2;
		float h=imageCodeDrawable.getIntrinsicHeight()*2;
		int tw=tvContent.getWidth();
		if(w>tw){
			imageCodeDrawable.setBounds(0,0,tw,(int)((h/w)*tw));
		}else{
			imageCodeDrawable.setBounds((int)(tw - w) / 2,0,(int)((tw - w)/ 2 + w),(int)h);
		}
		sp.setSpan(new ImageSpan(imageCodeDrawable), listpoint.get(0).start,listpoint.get(0).end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		tvContent.setText(sp);
		listpoint.remove(0);
		listurl.remove(0);
		if (listurl.size() > 0) {
			HttpManager.requestImage(listurl.get(0),
					ConstantGloble.GO_METHOD_GET, null, this,
					"imageCallBackMethod");
		}
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	private static final void addLinkMovementMethod(TextView t) {
		MovementMethod m = t.getMovementMethod();

		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			if (t.getLinksClickable()) {
				t.setMovementMethod(LinkMovementMethod.getInstance());
			}
		}
	}

	class MyClickableSpan extends URLSpan {

		String Murl;

		public MyClickableSpan(String url) {
			super(url);
			Murl = url;
		}

		@Override
		public void onClick(View widget) {

			openQueryProductActivity(Murl);

		}
	}

	public void openQueryProductActivity(String Murl) {
		Intent intent = new Intent();
		intent.putExtra("extra", Murl);
		intent.setClass(this, InfoServeWebActivity.class);
		startActivity(intent);
	}

	private void initView() {
		setTitle(getString(R.string.infoserve_info));
		btn_right.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLastIntent != null) {
					goInfoServeMainActivity();
				}
				finish();
			}
		});
	}

	private void doImageSpan(String c, String regex) {
		String content = new String(c);
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(content);
		if (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			String srcGroup = matcher.group();
			listpoint.add(new Point(start, end));
			String replace = content.replaceFirst(srcGroup, getEmptyString(end
					- start));
			doImageSpan(replace, regex);
		}
	}

	public String getEmptyString(int leng) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < leng; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	class Point {
		int start, end;

		public Point(int x, int y) {
			this.start = x;
			this.end = y;
		}

		@Override
		public String toString() {
			return "Position [x=" + start + ", y=" + end + "]";
		}
	}
}
