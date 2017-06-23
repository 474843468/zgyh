package com.chinamworld.bocmbci.biz.bocnet;

import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bocnet.cashacct.CashAcctActivity;
import com.chinamworld.bocmbci.biz.bocnet.creditcard.CreditCardAcountActivity;
import com.chinamworld.bocmbci.biz.bocnet.debitcard.DebitCardAcountActivity;
import com.chinamworld.bocmbci.biz.login.LoginDataCenter;
import com.chinamworld.bocmbci.biz.login.reg.RegisteVerifyActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 开户送电子银行UI基类
 * 
 * @author panwe
 * 
 */
public class BocnetBaseActivity extends BaseActivity implements OnClickListener{
	private final int REQUEST_REGISTE_CODE = 1004;
	private final int INDEX_COMMACCT = 0;
	private final int INDEX_CASHACCT = 1;
	private LinearLayout mBodyLayout;
	public Button mLeftButton, mRightButton;
	public String systemTime;
	private boolean isOnlyExit = true;
	public boolean isHaveExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		setLeftMenu();
		initPulldownBtn();
		initFootMenu();
		initView();
	}

	 private void initView() {
		 mBodyLayout = (LinearLayout) findViewById(R.id.sliding_body);
		 findViewById(R.id.ib_top_right_btn).setVisibility(View.GONE);
		 mLeftButton = (Button) findViewById(R.id.ib_back);
		 mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		 mLeftButton.setOnClickListener(this);
	 }
	 
	 /**
	  * 隐藏左上按钮
	  */
	 public void setLeftButtonGone(){
		 mLeftButton.setVisibility(View.GONE);
	 }
	 
	 public void setRightButton(String text, OnClickListener onClickListener){
		 mRightButton.setText(text);
		 mRightButton.setVisibility(View.VISIBLE);
		 mRightButton.setOnClickListener(onClickListener);
	 }
	 
	 public OnClickListener exitClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			requestBocnetLogout(true);
		}
	};
	 
	 /**
	  * 初始化布局
	  * @param resource
	  */
	 public void inflateLayout(int resource){
			View v = View.inflate(this, resource, null);
			mBodyLayout.addView(v);
	}
	
	 /**
	  * 初始化左侧菜单
	  */
	private void setLeftMenu(){
		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();
		if(StringUtil.isNullOrEmpty(loginInfo)) return;
		String isHaveEleCashAcct = (String) loginInfo.get(Bocnet.ISHAVEELECASHACCT);
		if (StringUtil.isNull(isHaveEleCashAcct)) return;
		if (!StringUtil.parseStrToBoolean(isHaveEleCashAcct)) {
			setLeftBtnGone();
		}
//		if (isHaveEleCashAcct.equals(Bocnet.UNHAVEELECASHACCT)) {
//			setLeftBtnGone();
//		}
		if (StringUtil.parseStrToBoolean(isHaveEleCashAcct)) {
			initLeftSideList(this, LocalData.bocnetLeftListData);
		}
//		if (isHaveEleCashAcct.equals(Bocnet.HAVEELECASHACCT)) {
//			initLeftSideList(this, LocalData.bocnetLeftListData);
//		}
	}
	
	/**
	 * 手机银行登录/注册引导
	 * L-引导登录
	 * R-引导注册
	 * @param btn
	 */
	public void initBankingFlagBtn(Button btn){
		if (btn == null) return;
		if(BocnetDataCenter.getInstance().getLoginInfo() == null) return;
		String eBankingFlag = (String) BocnetDataCenter.getInstance().getLoginInfo().get(Bocnet.EBANKINGFLAG);
		if (StringUtil.isNull(eBankingFlag)) return;
		if (eBankingFlag.equals(Bocnet.EBANKINGFLAG_L)) {
			btn.setText(getString(R.string.bocnet_login_mobile_bank));
		}
		if (eBankingFlag.equals(Bocnet.EBANKINGFLAG_R)) {
			btn.setText(getString(R.string.self_reg_title));
		}
	}
	
	/**
	 * 手机银行登录/注册引导
	 * L-引导登录
	 * R-引导注册
	 */
	String eBankingFlag;
	public void eBankingFlag(){
		eBankingFlag = (String) BocnetDataCenter.getInstance().getLoginInfo().get(Bocnet.EBANKINGFLAG);
		if (StringUtil.isNull(eBankingFlag)) return;
		if (eBankingFlag.equals(Bocnet.EBANKINGFLAG_L)) {
			requestBocnetLogout(false);
		}
		if (eBankingFlag.equals(Bocnet.EBANKINGFLAG_R)) {
			BaseDroidApp.getInstanse().setMainItemAutoClick(false);
			requestBocnetLogout(false);
		}
	}
	 
	/**
	* 隐藏左侧菜单
	*/
	public void setLeftBtnGone() {
		findViewById(R.id.btn_show).setVisibility(View.GONE);
	}
	
	public int getLeftBtnVisible(){
		return findViewById(R.id.btn_show).getVisibility();
	}
	
	/**
	 * 左侧菜单
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllActivityExceptOne(
				"LoginActivity");
		Intent mIntent = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("bocnet_1")){
			if(BocnetDataCenter.getInstance().isDebitCard())
				mIntent.setClass(this, DebitCardAcountActivity.class);
			else
				mIntent.setClass(this, CreditCardAcountActivity.class);
		}
		else if(menuId.equals("bocnet_2")){
			mIntent.setClass(this, CashAcctActivity.class);
		}
		context.startActivity(mIntent);
		return true;
		
//		ActivityTaskManager.getInstance().removeAllActivityExceptOne(
//				"LoginActivity");
//		Intent mIntent = new Intent();
//		switch (clickIndex) {
//		case INDEX_COMMACCT:
////			mIntent.setAction(BocnetDataCenter.getInstance().getIntentAction());
//			if(BocnetDataCenter.getInstance().isDebitCard())
//				mIntent.setClass(this, DebitCardAcountActivity.class);
//			else
//				mIntent.setClass(this, CreditCardAcountActivity.class);
//			break;
//
//		case INDEX_CASHACCT:
//			mIntent.setClass(this, CashAcctActivity.class);
//			break;
//		}
//		startActivity(mIntent);
	}
	
	/**
	 * 设置日期
	 */
	public OnClickListener bocnetChooseDateClick = new OnClickListener() {
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
					BocnetBaseActivity.this, new OnDateSetListener() {

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
	
	/**
	 * 登录后创建会话id
	 */
	public void requestConversation() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.CONVERSATION);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "conversationCallBack");
	}
	
	public void conversationCallBack(Object resultObj) {
		BocnetDataCenter.getInstance().setConversationId((String) HttpTools.getResponseResult(resultObj));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_back) {
			finish();
		}
	}
	
	/**
	 * 请求系统时间
	 */
	public void requestSystemTime() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODSYSTEMTIME);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "systemTimeCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void systemTimeCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		systemTime = (String) result.get(Bocnet.SYSTEMTIME);
		BocnetDataCenter.getInstance().setSystemTime(systemTime);
	}
	
	/**
	 * 安全退出     
	 */
	private void requestBocnetLogout(boolean isOnlyExit){
		BaseHttpEngine.showProgressDialog();
		this.isOnlyExit = isOnlyExit;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETLOGOUT);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "bocnetLogoutCallBack");
	}
	
	public void bocnetLogoutCallBack(Object resultObj) {
		isHaveExit = true;
		if(isOnlyExit){
			ActivityTaskManager.getInstance().removeAllActivity();
//			finish();
			return;
		}
		if (eBankingFlag.equals(Bocnet.EBANKINGFLAG_L)) {
//			Intent intent1 = new Intent();
			ActivityTaskManager.getInstance().removeAllActivity();
//			intent1.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoginActivity.class);
//			startActivityForResult(intent1, ConstantGloble.ACTIVITY_RESULT_CODE);
			BaseActivity.getLoginUtils(BocnetBaseActivity.this).exe(new LoginTask.LoginCallback() {

				@Override
				public void loginStatua(boolean isLogin) {

				}
			});
		}
		if (eBankingFlag.equals(Bocnet.EBANKINGFLAG_R)) {
			requestLoginPreConversationId();
		}
	}

	@Override
	public void requestLoginPreConversationIdCallBack(Object resultObj) {//requestLoginPreConversationIdCallBack
		super.requestLoginPreConversationIdCallBack(resultObj);
		String registerConversationId = (String) BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.get(ConstantGloble.LOGIN_PRECONVERSATIONID);
		LoginDataCenter.getInstance().setConversationId(registerConversationId);
		requestRegisterRandomNumber();
	}
	
	/**
	 * 请求自助注册随机数
	 */
	private void requestRegisterRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance().getConversationId());
		HttpManager.requestBii(biiRequestBody, this, "requestRegisterRandomNumberCallBack");
	}
	
	public void requestRegisterRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		String randomNumber = HttpTools.getResponseResult(resultObj);
		LoginDataCenter.getInstance().setRandomNumber(randomNumber);
		ActivityTaskManager.getInstance().removeAllActivityExceptOne(
				"LoginActivity");
		Intent intent  = new Intent(this, RegisteVerifyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
		intent.putExtra(BocnetDataCenter.ModleName, true);
		startActivity(intent);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
	
}
