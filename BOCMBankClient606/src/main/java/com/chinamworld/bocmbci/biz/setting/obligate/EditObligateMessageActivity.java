package com.chinamworld.bocmbci.biz.setting.obligate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class EditObligateMessageActivity extends ObligateBaseActivity {
	public static final String TAG = "EditObligateMessageActivity";
	/**
	 * 预留头像 textview
	 */
	// private TextView obligateImageTextView;
	/**
	 * 显示预留信息的TextView
	 */
	private EditText obligateMessageEdit;
	/**
	 * 取消按钮
	 */
	private Button conernBtn;
	/**
	 * 确定按钮
	 */
	private Button comfirmBtn;
	// private TextView info2Tv;
	private WebView info2webView;
	/**
	 * 返回的预留信息
	 */
	private String obligateMessage;
	/**
	 * 修改后的message
	 */
	private String newObligateMessage;
	/**
	 * 记录是否正在被修改 true 处于编辑状态
	 */

	private LinearLayout editLayout;
	private LinearLayout showLayout;
	private TextView obligateMessageTv;
	private boolean isEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	setLeftSelectedPosition("settingManager_4");
		queryLoginInfo();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("settingManager_4");
//	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void editWelcomeInfoCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		biiResponseBody.getResult();
		CustomDialog.toastShow(this, getResources().getString(R.string.set_editobligatemessagesuccess));
		obligateMessage = newObligateMessage;
		isEdit = false;

		// TODO 添加的缓存
		Map loginData = (Map) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		if (loginData != null) {
			loginData.put(Login.LOGIN_HINT, obligateMessage);
		}
		refrushView();

		ModelBoc.onModifyWelcomeInfoSuccess(newObligateMessage);//预留信息修改成功后调用该方法

	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		editWelcomeInfo(newObligateMessage, tokenId);
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		right.setVisibility(View.GONE);
		tabcontent.setPadding(0, 0, 0, 0);
		View childView = mainInflater.inflate(R.layout.setting_editobligatemessage, null);
		editLayout = (LinearLayout) childView.findViewById(R.id.set_layout1_edit);
		showLayout = (LinearLayout) childView.findViewById(R.id.set_layout1_show);
		obligateMessageEdit = (EditText) childView.findViewById(R.id.set_obligatemessage_edit);
		obligateMessageTv = (TextView) childView.findViewById(R.id.set_obligatemessage_show);
		conernBtn = (Button) childView.findViewById(R.id.set_editobligateimage_concern);
		comfirmBtn = (Button) childView.findViewById(R.id.set_editobligatemessage_ok);
		initWebView2(childView);
		// StringBuffer sb = new StringBuffer();
		// sb.append(getString(R.string.set_obligatemessage_info2));
		// sb.append("[]^$~@#%&<>{}'\"。\n");
		// info2Tv.setText(sb.toString());
		// LogGloble.d("info",
		// "sb.toString()=="+sb.toString()+"ToDBC(sb.toString())=="+ToDBC(sb.toString()));
		// info2Tv.setText(Html.fromHtml(ToDBC(sb.toString())));
		// info2Tv.setText(ToDBC(sb.toString()));
		comfirmBtn.setOnClickListener(this);
		conernBtn.setOnClickListener(this);

		comfirmBtn.setClickable(false);

		EditTextUtils.setLengthMatcher(this, obligateMessageEdit, 60);
		setTitle(getString(R.string.set_obligatemessage_no));

		// Intent intent = getIntent();
		// obligateMessage = intent.getStringExtra(Setting.I_WELCOMEINFO);
		isEdit = false;
		refrushView();
		right.setVisibility(View.GONE);
		tabcontent.addView(childView);
	}

	/**
	 * 初始化WebView
	 */
	private void initWebView2(View parentView) {
		info2webView = (WebView) parentView.findViewById(R.id.set_obligatemessage_info2);
		ViewUtils.initWebView(info2webView);
		info2webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		info2webView.setLongClickable(false);
		info2webView.setEnabled(false);
		info2webView.setClickable(false);
		info2webView.setFocusable(false);
		info2webView.loadUrl("file:///android_asset/page/settingobligationinfo.html");
	}

	/***
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
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

	/**
	 * * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_editobligatemessage_ok:
			if (isEdit) {// 如果处于编辑状态
				String editText = obligateMessageEdit.getText().toString();
				if(!StringUtil.simpleCheckPreHint(editText)){
					StringBuffer sb = new StringBuffer();
					sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
					sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
					String editInfo = sb.toString();
					BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
					return;
				}
//				editText = editText.toLowerCase();
//				//去掉空格
//				editText = editText.replaceAll(" ", "");
//				//去掉换行符
//				editText = editText.replaceAll("\r|\n", "");
//				if(editText.contains("eval(")){
//					int evalLength = editText.indexOf("eval(");
//					int last = editText.indexOf(")", evalLength);
//					if(last>=0){
//						StringBuffer sb = new StringBuffer();
//						sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//						sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//						String editInfo = sb.toString();
//						BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//							}
//						});
//						return;
//					}
//				}
//				if(editText.contains("onload(")){
//					int onloadLength = editText.indexOf("onload(");
//					int last = editText.indexOf(")", onloadLength);
//					if(last>=0){
//						StringBuffer sb = new StringBuffer();
//						sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//						sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//						String editInfo = sb.toString();
//						BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//							}
//						});
//						return;
//					}
//				}
//				if(editText.contains("javascript:")||editText.contains("vbscript:")||editText.contains("src='")){
//					StringBuffer sb = new StringBuffer();
//					sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//					sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//					String editInfo = sb.toString();
//					BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							BaseDroidApp.getInstanse().dismissMessageDialog();
//						}
//					});
//					return;
//				}	
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean regex = new RegexpBean(getString(R.string.set_obligatemessage_no),
						StringUtil.trim(obligateMessageEdit.getText().toString()), "oblimessage");
				lists.add(regex);
				if (RegexpUtils.regexpDate(lists)) {
					newObligateMessage = StringUtil.trim(obligateMessageEdit.getText().toString());
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}
			} else {
				isEdit = true;
				refrushView();
			}
			break;
		case R.id.set_editobligateimage_concern:// 取消
			isEdit = false;
			refrushView();
			break;

		default:
			break;
		}
	}

	/**
	 * 更新页面 根据是否处于编辑状态
	 * 
	 * @param isEdit
	 */
	private void refrushView() {
		if (isEdit) {
			editLayout.setVisibility(View.VISIBLE);
			showLayout.setVisibility(View.GONE);
			obligateMessageEdit.setText(obligateMessage);
			if (obligateMessage != null) {
				obligateMessageEdit.setSelection(obligateMessage.length());
			}
			conernBtn.setVisibility(View.VISIBLE);
			comfirmBtn.setVisibility(View.VISIBLE);
			comfirmBtn.setText(getString(R.string.confirm));
			ViewUtils.initBtnParamsTwo(comfirmBtn, this);
		} else {
			editLayout.setVisibility(View.GONE);
			showLayout.setVisibility(View.VISIBLE);
			obligateMessageTv.setText(obligateMessage);
			conernBtn.setVisibility(View.GONE);
			comfirmBtn.setVisibility(View.VISIBLE);
			comfirmBtn.setText(getString(R.string.set_editobligatemessage));
			ViewUtils.initBtnParams(comfirmBtn, this);
		}
	}

	/**
	 * 查询欢迎信息
	 * 
	 * @Author xyl
	 */
	protected void queryLoginInfo() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_QUERYLOGININFO);
		// TODO commConverSationId
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryLoginInfoCallback");
	}

	/**
	 * 查询欢迎信息回调处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            返回的结果
	 */
	public void queryLoginInfoCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody.getResult();
		if (!StringUtil.isNullOrEmpty(resultMap)) {
			obligateMessage = resultMap.get(Setting.SET_QUERYLOGININFO_LOGINHINT);
		}
		if (obligateMessage == null) {
			obligateMessage = "";
		}
		comfirmBtn.setClickable(true);
		refrushView();
	}
}
