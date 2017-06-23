package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.infoserve.adapter.IntentSpannable;
import com.chinamworld.bocmbci.biz.infoserve.adapter.IntentSpannable.IntentMatchFilter;
import com.chinamworld.bocmbci.biz.infoserve.adapter.IntentSpannable.SpanItemResult;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 投保声明界面
 * 
 * @author Zhi
 */
public class CarSafetyMustKnow extends SafetyBaseActivity {

	/** 主显示视图 */
	private View mMainView;
	/** 投保声明内容 */
	private String content;
	/** 交强险费率浮动告知单描述 */
	private String jqxNotify;
	/** 商业险费率浮动 */
	private String bizNotify;
	/** 交强险条款地址 */
	private String jqxMustKnow = "http://www.bankofchina.com/bocins/cusser/cs4/201501/P020150128647677063933.pdf";
	/** 商业险条款地址 */
	private String bizMustKnow = "http://www.bankofchina.com/bocins/cusser/cs4/201401/P020140107634387630249.pdf";
	@SuppressWarnings({"rawtypes" })
	private HttpHandler mHttpHandler;
	private long httpsize;
	private long filesize;
	
	private String mFileName;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_mustKnow);
		mLeftButton.setVisibility(View.GONE);
		mMainView = addView(R.layout.safety_carsafety_mustknow);
		
		if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get("bizList"))) {
			mMainView.findViewById(R.id.tv_bizMustKnow).setVisibility(View.GONE);
		}
		
		mMainView.findViewById(R.id.tv_jqxMustKnow).setOnClickListener(mustKnowDetilListener);
		mMainView.findViewById(R.id.tv_bizMustKnow).setOnClickListener(mustKnowDetilListener);
		// 取出投保声明信息
		content = (String) SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy().get(Safety.INSUREDECLARATION);
		jqxNotify = (String) SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy().get(Safety.JQXNOTIFY);
		bizNotify = (String) SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy().get(Safety.BIZNOTIFY);
//		jqxNotify = jqxNotify.replaceAll("\\[b\\]", "\\<b\\>");
//		jqxNotify = jqxNotify.replaceAll("\\[/b\\]", "\\</b\\>");
//		bizNotify = bizNotify.replaceAll("\\[b\\]", "\\<b\\>");
//		bizNotify = bizNotify.replaceAll("\\[/b\\]", "\\</b\\>");
		
//		jqxNotify = jqxNotify.replaceAll("\\\n", "\\<br\\>");
//		bizNotify = bizNotify.replaceAll("\\\n", "\\<br\\>");
		
		content = content.replaceAll("\\[biz\\]", "~@");
		content = content.replaceAll("\\[/biz\\]", "~@");
		content = content.replaceAll("\\[jqx\\]", "~@");
		content = content.replaceAll("\\[/jqx\\]", "~@");
//		TextView tv = (TextView) mMainView.findViewById(R.id.tv_mustKnow);
//		tv.setText(Html.fromHtml(jqxNotify));
		setTextContent(process(content));
		mMainView.findViewById(R.id.btnAgree).setOnClickListener(agreeListener);
		mMainView.findViewById(R.id.btnDisAgree).setOnClickListener(agreeListener);
	}
	
	/** 接受不接受按钮监听 */
	OnClickListener agreeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.btnAgree:
				intent.putExtra("agree", "1");
				break;

			case R.id.btnDisAgree:
				intent.putExtra("agree", "0");
				break;
			}
			setResult(1, intent);
			finish();
		}
	};
	
	/** 保险条款有关的监听 */
	OnClickListener mustKnowDetilListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!checkPDFapps()) {
				String msg = getString(R.string.safety_nopdfapps);
				BaseDroidApp.getInstanse().showInfoMessageDialog(msg);
				return;
			}
			switch (v.getId()) {
			case R.id.tv_jqxMustKnow:
				mFileName = "jqx";
				downLoadPDFfile(jqxMustKnow);
				break;

			case R.id.tv_bizMustKnow:
				mFileName = "biz";
				downLoadPDFfile(bizMustKnow);
				break;
			}
		}
	};
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(1, new Intent().putExtra("agree", "2"));
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//////////////////添加链接-----------------------------------------------
	private Spannable process(String content) {
		final IntentSpannable is = new IntentSpannable();
		String regex = "~@.*?~@"; 
		is.setContent(content, regex, new IntentMatchFilter() {
			@Override
			public SpanItemResult match(String srcContent) {
				String replaceFirst = srcContent.replaceFirst("~@", "");
				String replaceAll = replaceFirst.replaceFirst("~@", "");
				MyClickableSpan myClickableSpan = new MyClickableSpan(replaceAll);
				return is.new SpanItemResult(replaceAll, myClickableSpan);
			}
		});
		return is.process();
	}

	private void setTextContent(Spannable spannable) {
		TextView tv = (TextView) mMainView.findViewById(R.id.tv_mustKnow);
		tv.setText(spannable);
		addLinkMovementMethod(tv);
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
		private String url;
		public MyClickableSpan(String url) {
			super(url);
			this.url = url;
		}

		@Override
		public void onClick(View widget) {
			if (url.contains("商业险")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(bizNotify,Gravity.LEFT);
			} else if (url.contains("交强险")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(jqxNotify,Gravity.LEFT);
			} else {
				((TextView) widget).setTextColor(getResources().getColor(R.color.red));
				// 加粗
				TextPaint tp = ((TextView) widget).getPaint();
				tp.setFakeBoldText(true); 
			}
		}
	}	
	//////////////////添加链接end--------------------------------------------
	
	/**
	 * 下载pdf文件
	 * @param url
	 */
	private void downLoadPDFfile(final String url) {
		// 保存文件路劲
		String sdcardpath = android.os.Environment.getExternalStorageDirectory() + "/" + "BOCMB_PDF";
		final String fileName = sdcardpath + File.separator + mFileName + ".pdf";
		File file = new File(sdcardpath);
		if (!file.exists()) {
			try {
				file.mkdirs();
				if (!file.mkdirs()) CustomDialog.toastInCenter(getApplicationContext(), getApplicationContext().getString(R.string.save_dimen_to_nosdk));
				return;
			} catch (Exception e) {
			}
		}
		FinalHttp fh = null;
		if (fh == null) {
			fh = new FinalHttp();
		}
		
		mHttpHandler = fh.download(url, fileName, false, new AjaxCallBack<File>() {
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
			public void onFailure(Throwable t, int errorNo,  String strMsg) {
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
							+ mFileName + ".pdf";
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
	
	@Override
	protected void onDestroy() {
		if (mHttpHandler != null) {
			mHttpHandler.stop();
		}
		super.onDestroy();
	}
}
