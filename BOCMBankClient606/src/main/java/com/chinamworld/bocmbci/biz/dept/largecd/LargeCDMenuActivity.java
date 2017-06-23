package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.largecd.sign.LargeSignAccountListActivity;
import com.chinamworld.bocmbci.biz.dept.largecd.sign.LargeSignCannotRelationActivity;
import com.chinamworld.bocmbci.biz.dept.largecd.sign.LargeSignNoAccordActivity;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 大额存单菜单
 * @author luqp 2016年1月28日10:05:32
 */
public class LargeCDMenuActivity extends DeptBaseActivity implements OnClickListener {
	private Context context = this;
	private static final String TAG = "LargeCDMenuActivity";
	private LinearLayout tabcontent;
	private View view;
	/** 新增大额存单 */
	private LinearLayout menu_add;
	/** 支取大额存单 */
	private LinearLayout menu_redeem;

	private Class<?> clazz;
	/** 查询账户信息 */
	Map<String, Object> signedAcc = null;

	/** 关联提示 */
	private TextView prompt = null;
	/** 关联提示 根据是否能签约显示 能自动关联显示, 不能自动关联的不显示. */
	private LinearLayout relationAcc = null;
	/** 账户 */
	private TextView accNumber = null;
	/** 账户类型*/
	private TextView accType ;
	/** 如果  账户+账户类型 显示不全弹出提示popupWindow*/
	private LinearLayout accNumberPopupWindow;
	/** 签约关联 */
	private String linkAcctFlag = null;
	/** 该账户尚未关联至电子银行，请到柜台办理网银账户关联。 */
	private LinearLayout ll_prompt = null;
	/** 签约多个账户布局*/
	private LinearLayout ll_multiple = null;
	/** 签约多个账户TV*/
	private TextView tv_multiple;
	// 用户须知 add by luqp 2016年9月5日 ============
	/** 账户详情list*/
	private List<Map<String, Object>> accDetailList = null;
	/** Spinner 选择账户*/
	private List<String> flushList = null;
	/** 已签约已关联账户 */
	private Spinner capitalAccountList;
	/** 用户选择的账户 详情*/
	public Map<String, Object> accDataPosition = null;
	/** 已签约已关联账户 */
	public List<Map<String ,Object>> agencyAssociatedAccountList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("deptStorageCash_4");
		setTitle(getString(R.string.large_cd_menu_title));
		// add lqp 2015年12月14日11:58:50 判断是否开通投资理财!
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {

			@Override
			public void SuccessCallBack(Object param) {
				// add lqp 查询账户信息 2016年1月26日13:59:16
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestPsnLargeCDSignedAccQry();
			}
		},null);
	}

	/**
	 * 查询大额存单签约账号 回调
	 * @param resultObj
	 */
	public void requestPsnLargeCDSignedAccQryCallBack(Object resultObj) {
		super.requestPsnLargeCDSignedAccQryCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		signedAcc = (Map<String, Object>) biiResponseBody.getResult();
		if (signedAcc == null || signedAcc.isEmpty()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.large_cd_no_signed_error));
			return;
		}
		// add by 2016年9月2日  资金账户列表 ==============================================================
		accDetailList = (List<Map<String, Object>>) signedAcc.get(CONTRACT_ACCOUT_LIST);
		if (StringUtil.isNullOrEmpty(accDetailList)) {
			initNoSigned();
			return;
		}
		agencyAssociatedAccountList = new ArrayList<Map<String ,Object>>();
		for (int i = 0; i < accDetailList.size(); i++) {
			Map<String ,Object> accMap = accDetailList.get(i);
			String linkAcctFlags = (String) accMap.get(Dept.LINK_ACCT_FLAG); //签约标示
			if ((ConstantGloble.LARGE_CD_SIGNED_REL).equals(linkAcctFlags)){
				agencyAssociatedAccountList.add(accMap);
			}
		}
		// 已签约已关联账户为空
		if (StringUtil.isNullOrEmpty(agencyAssociatedAccountList)) {
			initNoSigned();
			return;
		}
		DeptDataCenter.getInstance().setLargeSelectAccountList(agencyAssociatedAccountList);
		flushList = new ArrayList<String>(); // 资金账户选择集合
		for (int i = 0; i< agencyAssociatedAccountList.size(); i++){
			String numberStr = StringUtil.getForSixForString((String) agencyAssociatedAccountList.get(i).get(Dept.ACCOUNT_NUMBER)); // 格式化账户
			String accountType = LocalData.AccountType.get(agencyAssociatedAccountList.get(i).get(Dept.LargeSign_accountType)); // 账户类型
			flushList.add(numberStr + " " + accountType);
		}
		// ==================================================end==========================================
		// 返回按钮
		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(LargeCDMenuActivity.this, MainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();
			}
		});

		initSelfLinkYes();
	}


	/** 未签约页面 */
	private void initNoSigned() {
		ibRight.setVisibility(View.GONE);
		LayoutInflater inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = inflater.inflate(R.layout.large_sign_not_signed, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		prompt = (TextView) view.findViewById(R.id.prompt);

		String head = "您尚未签约大额存单资金账户,请点击";
		String middle = "“这里”";
		String end = "进行签约。";
		SpannableString sp = new SpannableString(head + middle + end);
		TextViewNoSigned myStringSpan = new TextViewNoSigned();
		sp.setSpan(myStringSpan, head.length(), head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		prompt.setText(sp);
		prompt.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/** 点击这里 请求账户列表跳转Activity */
	private class TextViewNoSigned extends ClickableSpan {

		@Override
		public void onClick(View widget) {
			// 请求账户列表
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestQueryAllChinaBankAccount();
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.blue)); // 设置字体颜色
			ds.setUnderlineText(true); // 设置下划线 true显示下划线 false为不显示下划线.
		}
	}

	/** 请求账户列表 回调 */
	public void requestQueryAllChinaBankAccountCallBack(Object resultObj) {
		super.requestQueryAllChinaBankAccountCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> accList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(accList)) { // 账户列表为空
			Intent intent = new Intent();
			intent.setClass(context, LargeSignNoAccordActivity.class);
			startActivity(intent);
			return;
		}
		DeptDataCenter.getInstance().setLargeSignAccountList(accList);
		Intent intent = new Intent();
		intent.setClass(context, LargeSignAccountListActivity.class);
		startActivity(intent);
	}

	/** 已签约 能自助关联 & 已签约已关联 */
	private void initSelfLinkYes() {
		ibRight.setVisibility(View.GONE);
		LayoutInflater inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = inflater.inflate(R.layout.large_cd_menu, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		menu_add = (LinearLayout) view.findViewById(R.id.large_cd_menu_add_ll);
		menu_redeem = (LinearLayout) view.findViewById(R.id.large_cd_menu_redeem_ll);
		menu_add.setOnClickListener(this);
		menu_redeem.setOnClickListener(this);

		// add by luqp 2016年9月2日 大额存单 资金账户列表
		capitalAccountList = (Spinner) view.findViewById(R.id.account_list);
		ArrayAdapter<String> paymentAccountAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, flushList);
		paymentAccountAdapter.setDropDownViewResource(R.layout.epay_spinner_list_item);
		capitalAccountList.setAdapter(paymentAccountAdapter);
		capitalAccountList.setSelection(0);
		capitalAccountList.setOnItemSelectedListener(itemSelectClick);
		// add by luqp 2016年8月29日 签约多个账户布局
		ll_multiple = (LinearLayout) view.findViewById(R.id.large_cd_not_signed_ll);
		ll_multiple.setVisibility(View.VISIBLE);
		tv_multiple = (TextView) view.findViewById(R.id.tv_multiple);
		tv_multiple.setText(this.getResources().getString(R.string.least_sign_multiple_account));  // 三级菜单
		ll_multiple.setOnClickListener(this);

		relationAcc = (LinearLayout) view.findViewById(R.id.ll_acc);
		relationAcc.setVisibility(View.VISIBLE);
		prompt = (TextView) view.findViewById(R.id.prompt);
	}

	/** 账户监听事件 */
	private AdapterView.OnItemSelectedListener itemSelectClick = new AdapterView.OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// add by luqp 2016年9月7日 用户选择账户
			accDataPosition = agencyAssociatedAccountList.get(position);
			DeptDataCenter.getInstance().setSignedAcc(accDataPosition); // 将账户保存到集合
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	private LargeCDMenuActivity getActivity() {
		return this;
	}

	/** 已签约不能自动关联提示 */
	private void initSelfLinkNo() {
		ibRight.setVisibility(View.GONE);
		LayoutInflater inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = inflater.inflate(R.layout.large_sign_not_accord, null);
		ll_prompt = (LinearLayout) view.findViewById(R.id.ll_prompt);
		ll_prompt.setVisibility(View.VISIBLE);
		accNumber = (TextView) view.findViewById(R.id.tv_acc_number);
		String number = (String) signedAcc.get(Dept.ACCOUNT_NUMBER);
		accNumber.setText(StringUtil.getForSixForString(number));
		tabcontent.removeAllViews();
		tabcontent.addView(view);
	}

	/** 通讯返回 */
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		switch (flag) {
		case QUERY_ALL_ACCOUNT_CALLBACK:// 查询所有账户返回 刷新界面
			Intent intent = new Intent();
			intent.setClass(context, LargeSignAccountListActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/** 设置点击事件 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.large_cd_menu_add_ll: // 新增大额存单
				// add by luqp 2016年3月10日 修改  新增大额存单点击事件
				clazz = LargeCDAvailableQryActivity.class;
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestCommConversationId();
			break;
		case R.id.large_cd_menu_redeem_ll: // 支取大额存单
				// add by luqp 2016年3月3日 修改 支取大额存单点击事件
				clazz = LargeCDPurchasedQryActivity.class;
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestCommConversationId();
			break;
		case R.id.large_cd_not_signed_ll: // add by luqp 2016年8月29日 签约多个账户
					// add by luqp 2016年8月29日 签约多个账户
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestQueryAllChinaBankAccount(); 	// 请求账户列表
			break;
		default:
			break;
		}
//		refreshPsnLargeCDSignedAcc(); // 是否能自动关联能自动关联弹出提示 点击链接跳转关联账户页面
	}

	/** 点击 新增大额存单 支取大额存单时提示 */
	public void refreshPsnLargeCDSignedAcc() {
//		String selfLinkFlag = String.valueOf(signedAcc.get(Dept.SELF_LINK_FLAG));\
		String selfLinkFlag = String.valueOf(accDataPosition.get(Dept.SELF_LINK_FLAG));  // 是否能自助关联标识
		if ((ConstantGloble.SELF_LINK_YES).equalsIgnoreCase(selfLinkFlag)) { // 能自助关联
			// 自助关联弹出框
			String head = "请您先进行";
			String middle = "账户自助关联";
			SpannableString sp = new SpannableString(head + middle);
			relationAccTextView myRelation = new relationAccTextView();
			sp.setSpan(myRelation, head.length(), head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 提示框 文字超链接
			BaseDroidApp.getInstanse().showLargeDialog(sp, exitClick);
		} else if ((ConstantGloble.SELF_LINK_NO).equalsIgnoreCase(selfLinkFlag)) { // 不能自助关联
			Intent intent = new Intent();
			intent.setClass(this, LargeSignCannotRelationActivity.class);
			startActivity(intent);
		}
	}

	/** 跳转Activity */
	private class relationAccTextView extends ClickableSpan {

		@Override
		public void onClick(View widget) {
//			Intent intent = new Intent();
//			intent.setClass(context, AccInputRelevanceAccountActivity.class);
//			startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			BusinessModelControl.gotoAccRelevanceAccount(LargeCDMenuActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.blue)); // 设置字体颜色
			ds.setUnderlineText(true); // 设置下划线 true显示下划线 false为不显示下划线.
		}
	}

	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE:
			switch (resultCode) {
			case RESULT_OK:
				// getSignedAccQry();
				requestPsnLargeCDSignedAccQry();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (clazz != null) {
			Intent intent = new Intent(LargeCDMenuActivity.this, clazz);
			intent.putExtra("dateTime", dateTime);
			startActivity(intent);
		}
	}
}