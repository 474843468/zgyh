package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldAcountChoseActivity extends GoldBonusBaseActivity {
	/** 账户列表 */
	private ListView cardList;
	/** 当前选中账户的位置 */
	private int mCurrentPosition = -1;
	/** 列表adapter */
	private GoldbounsAccListAdapter goldbounsAccListAdapter;
	/** 长城借记卡 */
	protected static final String GREATWALL_CREDIT = "119";
	/** 活一本 */
	protected static final String HUOQIBENTONG = "188";
	/** 单选框 */
	private CheckBox acc_checkbox;
	/** 可点击协议 */
	private TextView agreement;
	/** 下一步 */
	private Button btnNext;
	// 查询所有账户的接口的返回集合
	private List<Map<String, Object>> result;
	// 账户标志
	private String accountId;
	// 账号
	private String accountNumber;
	// 适配器中的list
	private List<Map<String, Object>> resultList;
	private String isFitst;
	// 最先面提示信息，在第一次进入时候显示，账户变更不显示
	private LinearLayout alert_info;
	// 最上面的提示信息
	private TextView tv_financeic_choose_title;
	//本地保存的手机号码
	private String preferencePhone;
	//手机号输入框
	private EditText phonEditText;
	//新的的手机号码
	private String phoneString;
	private WebView webView;
	private TextView user_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isFitst = getIntent().getStringExtra("isFirst");
		Button back = (Button) findViewById(R.id.ib_back);
//		back.setVisibility(View.GONE);
//		//隐藏足bu'j
//		(findViewById(R.id.foot_layout)).setVisibility(View.GONE);
//		setResult(RESULT_CANCELED);
//		finish();
//		Button right = (Button) findViewById(R.id.ib_top_right_btn);
//		right.setText("关闭");
//		right.setVisibility(View.GONE);
//		getBackgroundLayout().setRightButtonNewText(null);
//		right.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				setResult(RESULT_CANCELED);
//				finish();
//			}
//		});
		
		if(isFitst.equals("2")){
//			setTitle(R.string.main_menu32);
			getBackgroundLayout().setTitleNewText(R.string.main_menu32);
			getBackgroundLayout().setRightButtonNewText(null);
		}else {
//			setTitle(R.string.goldbonus_account_manager);
			getBackgroundLayout().setTitleNewText(R.string.goldbonus_account_manager);
			getBackgroundLayout().setLeftButtomNewVisibility(View.GONE);
			getBackgroundLayout().setRightButtonNewText("关闭");
			getBackgroundLayout().setRightButtonNewClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		setContentView(R.layout.goldbouns_account_choose);
		if(isFitst.equals("2")){
			//2是第一次签约，显示输入手机号的布局
			((LinearLayout)findViewById(R.id.phoneNumberLayout)).setVisibility(View.VISIBLE);
			// 反显手机号
			SharedPreferences sharedPreferences = this.getSharedPreferences(
					ConstantGloble.CONFIG_FILE, MODE_PRIVATE);
//			preferencePhone = sharedPreferences.getString(Login.LOGIN_NAME,
//					null);
			Map<String, Object> resultMap=(Map<String, Object>) (BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.BIZ_LOGIN_DATA));
			preferencePhone=(String) resultMap.get(Login.REGISTER_MOBILE);
			phonEditText=(EditText) findViewById(R.id.input_phonenum);
			phonEditText.setText(preferencePhone);
		}else {
			((LinearLayout)findViewById(R.id.phoneNumberLayout)).setVisibility(View.GONE);
		}
		initView();
	}

	private void initView() {
		
		cardList = (ListView) findViewById(R.id.acc_accountlist);
		acc_checkbox = (CheckBox) findViewById(R.id.acc_checkbox);
		agreement = (TextView) findViewById(R.id.agreement);
		String head = "本人（客户）已仔细阅读并理解";
		String middle = "《中国银行股份有限公司个人积利金业务交易协议》";
		String end = "完全同意和接受协议书全部条款和内容，愿意履行和承担该协议书中约定的权利和义务。";
		SpannableString sp = new SpannableString(head + middle + end);
		TextViewNoSigned myStringSpan = new TextViewNoSigned();
		sp.setSpan(myStringSpan, head.length(), head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		agreement.setText(sp);
		agreement.setMovementMethod(LinkMovementMethod.getInstance());
		//		agreement.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				View dialogView = View.inflate(GoldAcountChoseActivity.this,
//						R.layout.goldbouns_agreement_message, null);
//				int l = getWindowManager().getDefaultDisplay().getWidth() / 20;
//				int t = getWindowManager().getDefaultDisplay().getHeight() / 10;
//				int r = getWindowManager().getDefaultDisplay().getWidth() * 19 / 20;
//				int b = getWindowManager().getDefaultDisplay().getHeight() * 9 / 10;
//				dialogView.layout(l, t, r, b);
//				((Button) dialogView.findViewById(R.id.confirm_button))
//						.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View arg0) {
//								// TODO Auto-generated method stub
//								BaseDroidApp.getInstanse().closeAllDialog();
//							}
//						});
//				BaseDroidApp.getInstanse().showDialog(dialogView);
//			}
//		});
		alert_info = (LinearLayout) findViewById(R.id.alert_info);
		tv_financeic_choose_title = (TextView) findViewById(R.id.tv_financeic_choose_title);
		if (isFitst.equals("2")) {
			alert_info.setVisibility(View.VISIBLE);
			tv_financeic_choose_title
					.setText(R.string.goldbonus_choose_account);
		} else {
			alert_info.setVisibility(View.GONE);
			tv_financeic_choose_title
					.setText(R.string.goldbonus_choose_account_new);
		}

//		requestPsnCommonQueryAllChinaBankAccount();
		resultList=GoldbonusLocalData.getInstance().requestPsnCommonQueryAllChinaBankAccountList;
		setListView(GoldbonusLocalData.getInstance().requestPsnCommonQueryAllChinaBankAccountList);
		btnNext = (Button) findViewById(R.id.btnNext);

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isFitst.equals("2")){
					phoneString=phonEditText.getText().toString().trim();
					//判断手机号是不是空
					if(StringUtil.isNull(phoneString)){
						BaseDroidApp.getInstanse().showInfoMessageDialog("请输入手机号码");
						return;
					}
					//判断手机号是不是11位
					if(phoneString.length()<11){
						BaseDroidApp.getInstanse().showInfoMessageDialog("手机号码由11位数字组成");
						return;
					}
					//保存手机号
					GoldbonusLocalData.getInstance().phoneNumber=phoneString;
				}
				
				if (mCurrentPosition < 0) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									GoldAcountChoseActivity.this
											.getString(R.string.choose_your_goldbouns_acc));
					return;
				}
				if (isFitst.equals("2")) {
					// 判断是否勾选服务协议
					if (acc_checkbox.isChecked()) {

						// 弹安全工具
						GoldbonusLocalData.getInstance().accountId = accountId;
						GoldbonusLocalData.getInstance().accountNumber = accountNumber;
						requestCommConversationId();

					} else {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getResources().getString(
											R.string.goldbonus_remind_message));

					}
				} else {
					GoldbonusLocalData.getInstance().accountIdNew = accountId;
					GoldbonusLocalData.getInstance().accountNumberNew = accountNumber;
					requestCommConversationId();
				}

			}
		});
		cardList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (((GoldbounsAccListAdapter.ViewHolder) arg1.getTag()).selected) {
					goldbounsAccListAdapter.setSelectedPosition(arg2);
					accountId = (String) (resultList.get(arg2).get("accountId"));
					GoldbonusLocalData.getInstance().accountIdBusi = accountId;
					accountNumber = (String) (resultList.get(arg2)
							.get("accountNumber"));
					mCurrentPosition = -1;
				} else {
					goldbounsAccListAdapter.setSelectedPosition(arg2);
					mCurrentPosition = arg2;
					accountId = (String) (resultList.get(arg2).get("accountId"));
					GoldbonusLocalData.getInstance().accountIdBusi = accountId;
					accountNumber = (String) (resultList.get(arg2)
							.get("accountNumber"));
					GoldbonusLocalData.getInstance().mCurrentPosition = mCurrentPosition;
				}

			}
		});

	}

	/**
	 * 查询用户的所有账户
	 */
	public void requestPsnCommonQueryAllChinaBankAccount() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		getHttpTools().requestHttp(GoldBonus.PSNCOMMONQUERYFILTEREDACCOUNTS,
				"requestPsnCommonQueryAllChinaBankAccountCallBack", paramMap,
				false);
	}

	/** 获取借记卡列表----回调 */
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		result = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		resultList = new ArrayList<Map<String, Object>>();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				if (GREATWALL_CREDIT.equals(result.get(i).get(
						Crcd.CRCD_ACCOUNTTYPE_RES))
						|| HUOQIBENTONG.equals(result.get(i).get(
								Crcd.CRCD_ACCOUNTTYPE_RES))) {
					resultList.add(result.get(i));
				}
			}
			for (int i = 0; i < result.size(); i++) {
				// 账号变更时重复账号不能显示
				if (getIntent().getStringExtra("isFirst").equals("1")) {
					if ((GoldbonusLocalData.getInstance().accountIdOld
							.equals(result.get(i).get(GoldBonus.ACCOUNTID)))) {
						resultList.remove(result.get(i));
					}
				}
			}
			setListView(resultList);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}

	}

	/**
	 * @Title: setListView
	 * @Description: 填充视图的数据
	 * @param
	 * @return void
	 */
	private void setListView(List<Map<String, Object>> resultList) {
		if (goldbounsAccListAdapter == null) {
			goldbounsAccListAdapter = new GoldbounsAccListAdapter(this,
					resultList);
			cardList.setAdapter(goldbounsAccListAdapter);
		} else {
			goldbounsAccListAdapter.setData(resultList);
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id/1为账户变更

		if (isFitst.equals("1")) {
			// 账户变更
			requestGetSecurityFactor("PB271");
		} else {
			requestGetSecurityFactor("PB270");
		}

	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 判断如果是第一次签约走3接口，变更账户走5接口,2为第一次签约，1为改变账户
						if (isFitst.equals("2")) {
							// 第一次选择交易账户，调用接口3，账户设定确认
							requestPsnGoldBonusRegisterAccountConfirm();
						} else {
							// 变更账户接口
							requestPsnGoldBonusModifyAccountConfirm(
									BaseDroidApp.getInstanse()
											.getSecurityChoosed(),
									GoldbonusLocalData.getInstance().accountIdOld,
									accountId, accountNumber);
						}

					}

				});

	}

	private void requestPsnGoldBonusRegisterAccountConfirm() {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("_combinId", BaseDroidApp.getInstanse()
				.getSecurityChoosed());// 安全因子
		paramMap.put("accountId", accountId);// 账户标识
		paramMap.put("accountNumber", accountNumber);// 账号
		paramMap.put("custPhoneNum", phoneString);//手机号
		getHttpTools().requestHttp(
				GoldBonus.PSNGOLDBONUSREGISTERACCOUNTCONFIRM,
				"requestPsnGoldBonusRegisterAccountConfirmCallBack", paramMap,
				true);

	}

	public void requestPsnGoldBonusRegisterAccountConfirmCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		GoldbonusLocalData.getInstance().RegisterAccountConfirmMap = resultMap;
		try {
			Intent intent = new Intent(GoldAcountChoseActivity.this,
					GoldbounsRegisterMessageActivity.class);
			intent.putExtra(
					"accountNumber",
					(String) resultList.get(mCurrentPosition).get(
							"accountNumber"));
			intent.putExtra("accountType",
					(String) resultList.get(mCurrentPosition)
							.get("accountType"));
			intent.putExtra("isFirst", "2");
			startActivity(intent);

		} catch (Exception e) {
			// TODO: handle exception
			LogGloble.e("asd", e + "");
		}

	}

	private void requestPsnGoldBonusModifyAccountConfirm(String _combinId,
			String accountIdOld, String accountId, String accountNumber) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("_combinId", _combinId);// 安全因子
		paramMap.put("oldAccountId", accountIdOld);// 老的账户标识
		paramMap.put("newAccountId", accountId);// 老的账户标识
		paramMap.put("newAccountNumber", accountNumber);// 账号
		getHttpTools().requestHttp(GoldBonus.PSNGOLDBONUSMODIFYACCOUNTCONFIRM,
				"requestPsnGoldBonusModifyAccountConfirmCallBack", paramMap,
				true);

	}

	public void requestPsnGoldBonusModifyAccountConfirmCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		GoldbonusLocalData.getInstance().PsnGoldBonusModifyAccountConfirmMap = resultMap;
		try {
			Intent intent = new Intent(GoldAcountChoseActivity.this,
					GoldbounsRegisterMessageActivity.class);
			intent.putExtra(
					"accountNumber",
					(String) resultList.get(mCurrentPosition).get(
							"accountNumber"));
			intent.putExtra("accountType",
					(String) resultList.get(mCurrentPosition)
							.get("accountType"));
			intent.putExtra("isFirst", "1");
			startActivity(intent);

		} catch (Exception e) {
			// TODO: handle exception
			LogGloble.e("asd", e + "");
		}

	}
	/** 点击这里 请求账户列表跳转Activity */
	private class TextViewNoSigned extends ClickableSpan {

		@Override
		public void onClick(View widget) {
			View dialogView = View.inflate(GoldAcountChoseActivity.this,
					R.layout.goldbouns_agreement_message, null);
			webView = (WebView) dialogView.findViewById(R.id.webView1);
//			user_name=(TextView) dialogView.findViewById(R.id.username);
//			user_name.setText((String)((Map<String, Object>) (BaseDroidApp.getInstanse().getBizDataMap()
//					.get(ConstantGloble.BIZ_LOGIN_DATA))).get(Login.CUSTOMER_NAME));
			readHtmlFromAssets("file:///android_asset/page/goldbouns_protocol.htm");
			int l = getWindowManager().getDefaultDisplay().getWidth() / 20;
			int t = getWindowManager().getDefaultDisplay().getHeight() / 10;
			int r = getWindowManager().getDefaultDisplay().getWidth() * 19 / 20;
			int b = getWindowManager().getDefaultDisplay().getHeight() * 9 / 10;
			dialogView.layout(l, t, r, b);
			((Button) dialogView.findViewById(R.id.confirm_button))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().closeAllDialog();
						}
					});
			BaseDroidApp.getInstanse().showDialog(dialogView);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.fonts_blue)); // 设置字体颜色
			ds.setUnderlineText(false); // 设置下划线 true显示下划线 false为不显示下划线.
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
	private void readHtmlFromAssets(String url) {
		ViewUtils.initWebView(webView);
		WebSettings webSettings = webView.getSettings();
		// webSettings.setLoadWithOverviewMode(true);
		// webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
		// webSettings.setLightTouchEnabled(true);
		// webSettings.setSupportZoom(true);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		webView.setClickable(false);
		webView.clearView();
		// webView.clearCache(true);
		// webView.setFocusable(false);
		webView.loadUrl(url);
	}


}
