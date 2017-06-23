package com.chinamworld.bocmbci.biz.infoserve;


/**
 * 消息服务的详细内容展示类
 * 
 * @author xby
 *   注释此类 因 dex方法超出限制  sunh 
 */
public class InfoServeDetialActivity1 extends InfoServeBaseActivity {
//
//	private static final String TAG = InfoServeDetialActivity1.class.getSimpleName();
//
//	private MessageService mMessageService;
//	private PushDevice mPushDevice;
//
//	private PushMessage mPushMessage;
//	private Intent mLastIntent;;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		addView(R.layout.service_info_detial);
//		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
//		initView();
//
//		mMessageService = MessageService.getInstance(this);
//		mPushDevice = PushManager.getInstance(this).getPushDevice();
//		Intent intent = getIntent();
//		// 处理分支
//		boolean processSrc = processSrc(intent);
//		if (!processSrc) {
//			mPushMessage = (PushMessage) intent.getSerializableExtra(Push.INTENT_MESSAGE);
//			if (mPushMessage == null) {
//				finish();
//				return;
//			}
//			if (mPushMessage.getMessageType() == MessageType.System) {
//				refreshView(mPushMessage);
//			} else {
//				if (!mPushMessage.isReaded()) {
//					// 未读
//					if (mPushDevice == null) {
//						finish();
//						return;
//					}
//				}
//				requestMessage();
//			}
//		}
//	}
//
//	private boolean processSrc(Intent intent) {
//		if (intent != null) {
//			String src = intent.getStringExtra(Push.INTENT_SRC);
//			if (Push.INTENT_NOTIFICATION.equals(src)) {
//				// 处理从通知栏过来的
//				boolean nofitionReset = intent.getBooleanExtra(Push.INTENT_RESET, false);
//				if (nofitionReset) {
//					InfoServeDataCenter.getInstance().resetNotification();
//				}
//				PushMessage msg = (PushMessage) getIntent().getSerializableExtra(Push.INTENT_MESSAGE);
//				if (msg != null && msg.getMessageType() != null) {
//					mLastIntent = intent;
//					mPushMessage = msg;
//					requestMessage();
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	private void requestMessage() {
//		if (mPushMessage.getMessageType() == MessageType.System || mPushMessage.isReaded()) {
//			refreshView(mPushMessage);
//		} else {
//			if (mPushMessage.getMessageType() == MessageType.Vip) {
//				BiiHttpEngine.showProgressDialogCanGoBack();
//				mMessageService.getTicketForMessage(mPushMessage.getContentId(), this, "requestTicketCallback");
//			} else {
//				BiiHttpEngine.showProgressDialogCanGoBack();
//				mMessageService.getNewMessage(mPushDevice, mPushMessage.getContentId(), this, "requestMessageCallback");
//			}
//		}
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		processSrc(intent);
//	}
//
//	@Override
//	public void onBackPressed() {
//		if (mLastIntent != null) {
//			goInfoServeMainActivity();
//		}
//		super.onBackPressed();
//	}
//
//	private void goInfoServeMainActivity() {
//		String src = mLastIntent.getStringExtra(Push.INTENT_SRC);
//		if (Push.INTENT_NOTIFICATION.equals(src)) {
//			// 如果从通知栏刷新列表
//			PushMessage msg = (PushMessage) mLastIntent.getSerializableExtra(Push.INTENT_MESSAGE);
//			if (msg != null) {
//				Intent intent = new Intent(this, InfoServeMainActivity.class);
//				intent.putExtra(Push.INTENT_SRC, Push.INTENT_NOTIFICATION);
//				intent.putExtra(Push.INTENT_MESSAGE, msg);
//				startActivity(intent);
//			}
//		}
//	}
//
//	public void requestMessageCallback(Object resultObj) {
//		BiiHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, String> resultMap = (Map<String, String>) biiResponseBody.getResult();
//		if (resultMap != null && !resultMap.isEmpty()) {
//			String contentId = resultMap.get(Push.PNS002_ContentID_RESULT);
//			String content = resultMap.get(Push.PNS002_Content_RESULT);
//			//
//			if (contentId != null && contentId.equals(mPushMessage.getContentId())) {
//				mPushMessage.setContentId(contentId);
//				mPushMessage.setContent(content);
//				refreshView(mPushMessage);
//				// 先刷新在设置为已读
//				mPushMessage.setReaded(true);
//				mPushMessage.setDeviceID(mPushDevice.getDeviceId());
//				mMessageService.updateLocalMessage(mPushMessage);
//
//				Intent resultIntent = new Intent();
//				resultIntent.putExtra(Push.INTENT_DEVICE_ID, mPushMessage.getDeviceID());
//				resultIntent.putExtra(Push.INTENT_CONTENT_ID, mPushMessage.getContentId());
//				resultIntent.putExtra(Push.INTENT_TYPE, mPushMessage.getMessageType());
//				resultIntent.putExtra(Push.INTENT_CONTENT, mPushMessage.getContent());
//				setResult(Activity.RESULT_OK, resultIntent);
//			}
//		}
//	}
//
//	public void requestTicketCallback(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, String> resultMap = (Map<String, String>) biiResponseBody.getResult();
//		if (resultMap != null && !resultMap.isEmpty()) {
//			String ticketResult = resultMap.get(Push.Ticket);
//			if (ticketResult != null) {
//				mMessageService.getVipMessage(mPushDevice, mPushMessage.getContentId(), ticketResult, this,
//						"requestMessageCallback");
//			}
//		}
//	}
//
//	boolean isPageFinished = false;
//
//	public void refreshView(PushMessage message) {
//		// TextView tvTitle = (TextView) findViewById(R.id.info_title);
//		WebView webview = (WebView) findViewById(R.id.detial_info);
//		// TextView tvTime = (TextView) findViewById(R.id.info_time);
//		webview.setBackgroundColor(0); // make the background transparent
//		webview.setBackgroundResource(R.drawable.infoserve_bg);
//		View newView = findViewById(R.id.info_new);
//		webview.setScrollBarStyle(0);
//		webview.setWebViewClient(new WebViewClient() {
//
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				LogGloble.i(TAG, "url:" + url);
//				return true;
//			}
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				// PageFinished完成后,进行标记
//				// 第二次启动activity
//				if (isPageFinished) {
//					if (url.contains("#") && mPushMessage.getMessageType() == MessageType.Vip) {
//						openQueryProductActivity();
//					} else {
//						return;
//					}
//				}
//				isPageFinished = true;
//				super.onPageFinished(view, url);
//			}
//
//		});
//
//		String time = null;
//		try {
//			if (message.getMessageType() != MessageType.System) {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//				// tvTime.setText(sdf.format(new Date(message.getDateTime())));
//				// tvTime.setVisibility(View.VISIBLE);
//				time = sdf.format(new Date(message.getDateTime()));
//			} else {
//				// tvTime.setVisibility(View.GONE);
//			}
//
//		} catch (Exception e) {
//			LogGloble.e(TAG, e.getMessage(), e);
//		}
//		// tvTitle.setText(message.getNotification());
//		if (message.getMessageType() == MessageType.Vip) {
//			// 加标签[~@点击这里~@]
//			final IntentSpannable is = new IntentSpannable();
//			String regex = "~@.*?~@";
//			is.setContent(message.getContent(), regex, new IntentMatchFilter() {
//				@Override
//				public SpanItemResult match(String srcContent) {
//					String replaceFirst = srcContent.replaceFirst("~@", "<a href='#'>");
//					String replaceAll = replaceFirst.replaceFirst("~@", "</a>");
//					MyClickableSpan myClickableSpan = new MyClickableSpan(replaceAll);
//					return is.new SpanItemResult(replaceAll, myClickableSpan);
//				}
//			});
//			String str = is.process().toString();
//			webview.loadDataWithBaseURL("", loadData(message.getNotification(), time, str), "text/html", "utf-8", "");
//		} else {
//			webview.loadDataWithBaseURL("", loadData(message.getNotification(), time, message.getContent()),
//					"text/html", "utf-8", "");
//
//		}
//		newView.setVisibility(message.isReaded() ? View.INVISIBLE : View.VISIBLE);
//
//		findViewById(R.id.layout_root).setVisibility(View.VISIBLE);
//	}
//
//	private String loadData(String title, String time, String content) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<html>");
//		sb.append("<head>");
//		sb.append("<meta http-equiv='Content-Type' content='application/xhtml+xml; charset=UTF-8' />");
//		sb.append("<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;' name='viewport' />");
//		sb.append("<title></title>");
//		sb.append("</head>");
//		sb.append("<body style='font-size:15pt;word-wrap:break-word;word-break:break-all;'>");
//		// margin: top right bottom left
//		sb.append("<div style='margin:30px 10px 10px 10px;'>");// div——root
//		sb.append("<div style='font-size:15pt;text-align:center;margin:0px 10px 10px 10px'>");
//		sb.append(title);
//		sb.append("</div>");
//		if (!TextUtils.isEmpty(time)) {
//			sb.append("<div style='color:#9a9a9a;font-size:13pt;text-align:right;'>");
//			sb.append(time);
//			sb.append("</div>");
//		}
//		sb.append("<div style='font-size:14pt;margin:10px 0px 0px 0px;'>");
//		sb.append(content);
//		sb.append("</div>");
//		int h = LayoutValue.SCREEN_HEIGHT / 10; // 适配
//		sb.append("<div style='height:" + h + "px'></div>");
//		sb.append("</div>"); // div——root end
//		sb.append("</body></html>");
//		return sb.toString();
//	}
//
//	// private String loadData(String text) {
//	// StringBuilder sb = new StringBuilder();
//	// sb.append("<html><head><meta http-equiv='Content-Type' content='application/xhtml+xml; charset=UTF-8' /><title></title></head>"
//	// +
//	// "<body style='font-size:12pt;word-wrap:break-word;word-break:break-all;'>");
//	// sb.append(text);
//	// int h = LayoutValue.SCREEN_HEIGHT / 10; // 适配
//	// sb.append("<div style='height:" + h + "px'></body></html>");
//	// return sb.toString();
//	// }
//
//	private static final void addLinkMovementMethod(TextView t) {
//		MovementMethod m = t.getMovementMethod();
//
//		if ((m == null) || !(m instanceof LinkMovementMethod)) {
//			if (t.getLinksClickable()) {
//				t.setMovementMethod(LinkMovementMethod.getInstance());
//			}
//		}
//	}
//
//	class MyClickableSpan extends URLSpan {
//
//		public MyClickableSpan(String url) {
//			super(url);
//		}
//
//		@Override
//		public void onClick(View widget) {
//			openQueryProductActivity();
//		}
//	}
//
//	public void openQueryProductActivity() {
//		Intent intent = new Intent(this, QueryProductActivity.class);
//		startActivity(intent);
//	}
//
//	private void initView() {
//		setTitle(getString(R.string.infoserve_info));
//		btn_right.setVisibility(View.GONE);
//		back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (mLastIntent != null) {
//					goInfoServeMainActivity();
//				}
//				finish();
//			}
//		});
//	}
//
}
