package com.chinamworld.bocmbci.biz.setting.accmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.setting.adapter.AccManagerListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 账户管家界面
 * 
 * @author xyl
 * 
 */
public class AccountManagerActivity extends AccountManagerBaseActivity {
	private static final String TAG = "AccountManagerActivity";
	/**
	 * 显示账户信息的listview
	 */
	private ListView listView;
	private ImageView footerImageView;
	private View footerView;
	/**
	 * 查询到用户的所有账户 传到listview适配器 的数据
	 */
	private List<Map<String, Object>> dataList;
	/** 请求conversationid标志:id=1,请求修改别名;id=2,请求处理取消关联 ；id=3 设定默认账户 */
	private int conid = 0;
	// 当前点击的位置
	private int position;
	/** 取消关联点击事件 */
	private OnItemClickListener onbanklistCancelRelationClickListener;
	/**
	 * 正常状态的点击事件
	 */
	private OnItemClickListener normalOnItemClickListener;
	private String tokenId;
	private AccManagerListAdapter adapter;
	/** 是否可以撤销 */
	private boolean canConsern;

	private String defaultAccStr;
	private String mobilNumStr;

	private String reSetAccId;
	private String reSetAccNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setLeftSelectedPosition("settingManager_1");
		BaseHttpEngine.showProgressDialogCanGoBack();
		queryAccounts();

	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("settingManager_1");
//	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if (conid == 2) {
			requestCancleAccRelation();
		} else if (conid == 3) {
			setDefaultAcc(mobilNumStr, reSetAccId, tokenId);
		}

	}

	@Override
	public void setDefaultAccCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.setDefaultAccCallback(resultObj);
		CustomDialog.toastShow(this,
				this.getString(R.string.set_setdefaultsuccess));
		adapter = new AccManagerListAdapter(this, dataList, 1,
				normalOnItemClickListener);
		footerImageView.setVisibility(View.GONE);
		listView.setAdapter(adapter);
		defaultAccStr = reSetAccNum;
		LogGloble.d("info", "setDefaultAccCallback");
		// defaultAccStr = (String) map.get("defaultAct");
		adapter.setDefaultAccStr(defaultAccStr);
	}

	/** 请求取消关联信息 */
	public void requestCancleAccRelation() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_CANCELACCRELATION_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(
				Acc.CANCELRELATION_ACC_ACCOUNTID,
				(String.valueOf(dataList.get(position).get(
						Acc.ACC_ACCOUNTID_RES))));
		paramsmap.put(Acc.CANCELRELATION_ACC_ACCOUNTNUMBER, String
				.valueOf(dataList.get(position).get(Acc.ACC_ACCOUNTNUMBER_RES)));
		paramsmap.put(Acc.CANCELRELATION_ACC_TOKEN, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"cancleAccRelationCallBack");
	}

	public void cancleAccRelationCallBack(Object resultObj) {
		CustomDialog.toastShow(this,
				this.getString(R.string.acc_cancelrelation_success));
		// 对列表内的数据进行刷新
		BaseHttpEngine.showProgressDialogCanGoBack();
		queryAccounts();

	}

	@Override
	public void queryAccountsCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryAccountsCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		dataList = getAllAccounts(map);
		adapter = new AccManagerListAdapter(this, dataList,
				AccManagerListAdapter.NORMAL, normalOnItemClickListener);
		footerImageView.setVisibility(View.GONE);
		listView.setAdapter(adapter);
		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		mobilNumStr = (String) loginMap.get(Comm.LOGINNAME);
		tabcontent.setPadding(
				getResources().getDimensionPixelSize(R.dimen.fill_margin_left),
				getResources().getDimensionPixelSize(R.dimen.fill_margin_top),
				getResources().getDimensionPixelSize(R.dimen.fill_margin_left),
				getResources().getDimensionPixelSize(
						R.dimen.common_bottom_padding_new));
		right.setText(getString(R.string.acc_main_right_btn));
		requestDefaultAccount(mobilNumStr);
		if (StringUtil.parseStrToBoolean((String) map
				.get(Setting.SET_QUERYACCOUNTS_SHOWFLG))) {// 如果返回是true
															// 是不可取消关联....
			canConsern = false;
			right.setVisibility(View.GONE);
		} else {
			canConsern = true;
			right.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 请求默认账户---新改接口   pwe
	 * @param mobile
	 */
	private void requestDefaultAccount(String mobile){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_QUERYDEFAULTACCT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Setting.MOBILE, mobile);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "defaultAccountCallBack");
		
	}
	
	@SuppressWarnings("unchecked")
	public void defaultAccountCallBack(Object resultObj) {
		Map<String, Object> mMap = getHttpTools().getResponseResult(resultObj);
		String defualtAcct = (String) mMap.get(Comm.ACCOUNTNUMBER);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(defualtAcct)) return;
		if (!mMap.get(Setting.STATUS).equals("01")) return;
		adapter.setDefaultAccStr(defualtAcct);
	}

	/*@Override
	public void queryDefaultAccCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryDefaultAccCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		defaultAccStr = String.valueOf(resultMap
				.get(Setting.SET_QUERYDEFAULTACCOUNT_ACCOUNTNUMBER));
		adapter.setDefaultAccStr(defaultAccStr);

	}*/

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		if(Setting.METHOD_QUERYDEFAULTACCT.equals(biiResponseBody.getMethod())){
			BiiHttpEngine.dissMissProgressDialog();
			return true;
		}

		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (biiResponseBody.getError().getCode()
					.equals(ErrorCode.SETTING_NODEFAULTACC_ERROR)) {
				return true;
			}

		}
		
		
		return super.httpRequestCallBackPre(resultObj);

	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.setting_accmanager_list,
				null);
		tabcontent.addView(childView);
		listView = (ListView) findViewById(R.id.listView1);
		footerView = mainInflater.inflate(
				R.layout.setting_accmanager_listfooter, null);
		View.OnClickListener footerOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (adapter.getFlag() == AccManagerListAdapter.NORMAL) {
//					startActivityForResult((new Intent(
//							AccountManagerActivity.this,
//							AccInputRelevanceAccountActivity.class)),
//							ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
					
					BusinessModelControl.gotoAccRelevanceAccount(AccountManagerActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
				}
			}
		};
		footerView.setOnClickListener(footerOnClickListener);
		footerImageView = (ImageView) footerView
				.findViewById(R.id.set_btn_gocancelrelation);
//		listView.addFooterView(footerView); 屏蔽自助关联
		setTitle(getResources().getString(R.string.set_title_accnamager));
		right.setOnClickListener(this);
		right.setText(getResources().getString(R.string.acc_main_right_btn));
		right.setVisibility(View.VISIBLE);
		normalOnItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AccountManagerActivity.this.position = position;
				Map<String, Object> map = dataList.get(position);
				reSetAccId = (String) map
						.get(Setting.SET_QUERYACCOUNTS_ACCOUNTID);
				reSetAccNum = (String) map
						.get(Setting.SET_QUERYACCOUNTS_ACCOUNTNUMBER);
				Boolean isCanSetDefaultAcc = false;
				if (map.get(Setting.SET_QUERYACCOUNTS_ACCOUNTTYPE).equals(
						ConstantGloble.GREATWALL)
						|| map.get(Setting.SET_QUERYACCOUNTS_ACCOUNTTYPE)
								.equals(ConstantGloble.ACC_TYPE_BRO)) {
					isCanSetDefaultAcc = true;
				}
				OnClickListener onClickListenner = new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch ((Integer) v.getTag()) {
						case CustomDialog.TAG_EDIT_ALIAS:
							BaseDroidApp.getInstanse().dismissMessageDialog();
							Intent intent = new Intent();
							intent.setClass(AccountManagerActivity.this,
									EditAccAliasActivity.class);
							Map<String, Object> map = dataList
									.get(AccountManagerActivity.this.position);
							final String accId = (String) map
									.get(Setting.SET_QUERYACCOUNTS_ACCOUNTID);// 自己
							final String nickName = (String) map
									.get(Setting.SET_QUERYACCOUNTS_NICKNAME);
							final String accType = (String) map
									.get(Setting.SET_QUERYACCOUNTS_ACCOUNTTYPE);
							final String accNum = (String) map
									.get(Setting.SET_QUERYACCOUNTS_ACCOUNTNUMBER);
							final String accName = (String) map
									.get(Setting.SET_QUERYACCOUNTS_ACCOUNTNAME);
							intent.putExtra(Setting.I_ACCID, accId);
							intent.putExtra(Setting.I_ACCALIAS, nickName);
							intent.putExtra(Setting.I_ACCTYPE, accType);
							intent.putExtra(Setting.I_ACCNAME, accName);
							intent.putExtra(Setting.I_ACCNUM,
									StringUtil.getForSixForString(accNum));
							AccountManagerActivity.this.startActivity(intent);
							break;
						/*case CustomDialog.TAG_SET_DEFAULTNUM:
							AccountManagerActivity.this.conid = 3;
							BaseDroidApp.getInstanse().dismissMessageDialog();
							StringBuffer stringbuffer = new StringBuffer();
							stringbuffer.append(AccountManagerActivity.this
									.getString(R.string.set_setmorenacc1));
							stringbuffer
									.append(StringUtil
											.getForSixForString((String) AccountManagerActivity.this.dataList
													.get(AccountManagerActivity.this.position)
													.get(Setting.SET_QUERYACCOUNTS_ACCOUNTNUMBER)));
							stringbuffer.append(AccountManagerActivity.this
									.getString(R.string.set_setmorenacc2));
							BaseDroidApp.getInstanse().showErrorDialog(
									stringbuffer.toString(), R.string.cancle,
									R.string.confirm, new OnClickListener() {
										@Override
										public void onClick(View v) {
											switch (Integer.parseInt(v.getTag()
													+ "")) {
											case CustomDialog.TAG_SURE:
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
												requestCommConversationId();
												BaseHttpEngine
														.showProgressDialog();
												break;
											case CustomDialog.TAG_CANCLE:
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
												conid = 0;

												break;
											}
										}
									});
							break;*/
						case CustomDialog.TAG_COSERN_CONNECTION:
							AccountManagerActivity.this.conid = 2;
							BaseDroidApp.getInstanse().dismissMessageDialog();
							BaseDroidApp
									.getInstanse()
									.showErrorDialog(
											AccountManagerActivity.this
													.getResources()
													.getString(
															R.string.acc_cancelrelation_msg),
											R.string.consern, R.string.confirm,
											new OnClickListener() {
												@Override
												public void onClick(View v) {
													switch (Integer.parseInt(v
															.getTag() + "")) {
													case CustomDialog.TAG_CANCLE:
														// 取消操作
														BaseDroidApp
																.getInstanse()
																.dismissErrorDialog();
														conid = 0;
														break;

													case CustomDialog.TAG_SURE:
														// 确定取消关联
														BaseDroidApp
																.getInstanse()
																.dismissErrorDialog();
														conid = 2;
														requestCommConversationId();
														BaseHttpEngine
																.showProgressDialog();
														break;
													}
												}
											});
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissMessageDialog();
							break;

						default:
							break;
						}

					}
				};
				BaseDroidApp.getInstanse().setAccManagerMenuDialog(
						onClickListenner, isCanSetDefaultAcc, canConsern);

			}
		};
		onbanklistCancelRelationClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				AccountManagerActivity.this.position = position;
				conid = 2; // 弹出对话框,是否取消
				BaseDroidApp
						.getInstanse()
						.showErrorDialog(
								AccountManagerActivity.this
										.getString(R.string.acc_cancelrelation_msg),
								R.string.cancle, R.string.confirm,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										switch (Integer.parseInt(v.getTag()
												+ "")) {
										case CustomDialog.TAG_SURE:
											// 确定取消关联
											conid = 2;
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											requestCommConversationId();
											BaseHttpEngine.showProgressDialog();
											break;
										case CustomDialog.TAG_CANCLE:
											// 取消操作
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											conid = 0;

											break;
										}
									}
								});
			}
		};
		BaseHttpEngine.showProgressDialogCanGoBack();
	}

	/**
	 * 处理账户管家查询 所得的Map 拿到所有的账号信息
	 * 
	 * @Author xyl
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	private List<Map<String, Object>> getAllAccounts(Map<String, Object> map) {
//		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
//		List<Map<String, Object>> currentList = (List<Map<String, Object>>) map
//				.get(Setting.SET_QUERYACCOUNTS_CURRENTLIST);// 个人活期账户列表
//		
//		List<Map<String, Object>> termList = (List<Map<String, Object>>) map
//				.get(Setting.SET_QUERYACCOUNTS_TERMLIST);// 定期
//		
//		List<Map<String, Object>> creditCardList = (List<Map<String, Object>>) map
//				.get(Setting.SET_QUERYACCOUNTS_CREDITCARDLIST);// 信用卡
//		
//		List<Map<String, Object>> ecashAccountList = (List<Map<String, Object>>) map
//				.get(Setting.SET_QUERYACCOUNTS_ECASHACCOUNTLIST);// 电子
//		
//		if (!StringUtil.isNullOrEmpty(currentList)) {
//			returnList.addAll(currentList);
//		}
//		if (!StringUtil.isNullOrEmpty(termList)) {
//			returnList.addAll(termList);
//		}
//		if (!StringUtil.isNullOrEmpty(creditCardList)) {
//			returnList.addAll(creditCardList);
//		}
//		if (!StringUtil.isNullOrEmpty(ecashAccountList)) {
//			returnList.addAll(ecashAccountList);
//		}
//		return returnList;
//	}
	//2014年8月21日 16:20:10  pwe
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getAllAccounts(Map<String, Object> map) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> currentList = (List<Map<String, Object>>) map
				.get(Setting.SET_QUERYACCOUNTS_CURRENTLIST);
		if (!StringUtil.isNullOrEmpty(currentList)) {// 个人活期账户列表
			for (int i = 0; i < currentList.size(); i++) {
				if (!currentList.get(i).get(Comm.ACCOUNT_TYPE).equals(Setting.ACCTTYPT_YHT)) {
					returnList.add(currentList.get(i));
				}
			}
		}
		if (!StringUtil.isNullOrEmpty(map
				.get(Setting.SET_QUERYACCOUNTS_TERMLIST))) {// 定期
			List<Map<String, Object>> termList = (List<Map<String, Object>>) map
					.get(Setting.SET_QUERYACCOUNTS_TERMLIST);
			for (int i = 0; i < termList.size(); i++) {
				if (!termList.get(i).get(Comm.ACCOUNT_TYPE).equals(Setting.ACCTTYPT_YHT)) {
					returnList.add(termList.get(i));
				}
			}
		}
		if (!StringUtil.isNullOrEmpty(map
				.get(Setting.SET_QUERYACCOUNTS_CREDITCARDLIST))) {// 信用卡
			List<Map<String, Object>> creditCardList = (List<Map<String, Object>>) map
					.get(Setting.SET_QUERYACCOUNTS_CREDITCARDLIST);
			for (int i = 0; i < creditCardList.size(); i++) {
				if (!creditCardList.get(i).get(Comm.ACCOUNT_TYPE).equals(Setting.ACCTTYPT_YHT)) {
					returnList.add(creditCardList.get(i));
				}
			}
		}
		List<Map<String, Object>> ecashAccountList = (List<Map<String, Object>>) map
				.get(Setting.SET_QUERYACCOUNTS_ECASHACCOUNTLIST);
		if (!StringUtil.isNullOrEmpty(ecashAccountList)) {// 电子
			for (int i = 0; i < ecashAccountList.size(); i++) {
				if (!ecashAccountList.get(i).get(Comm.ACCOUNT_TYPE).equals(Setting.ACCTTYPT_YHT)) {
					returnList.add(ecashAccountList.get(i));
				}
			}
		}
		return returnList;
	}
	

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_top_right_btn:
			if (adapter.getFlag() == AccManagerListAdapter.NORMAL) {
				tabcontent.setPadding(
						getResources().getDimensionPixelSize(
								R.dimen.fill_margin_left),
						getResources().getDimensionPixelSize(
								R.dimen.fill_margin_top),
						0,
						getResources().getDimensionPixelSize(
								R.dimen.common_bottom_padding_new));
				footerImageView.setVisibility(View.INVISIBLE);
				adapter = new AccManagerListAdapter(this, dataList,
						AccManagerListAdapter.REMOVECONNECTION,
						onbanklistCancelRelationClickListener);
				listView.setAdapter(adapter);
				adapter.setDefaultAccStr(defaultAccStr);
				right.setText(getString(R.string.finish));
			} else {
				tabcontent.setPadding(
						getResources().getDimensionPixelSize(
								R.dimen.fill_margin_left),
						getResources().getDimensionPixelSize(
								R.dimen.fill_margin_top),
						getResources().getDimensionPixelSize(
								R.dimen.fill_margin_left),
						getResources().getDimensionPixelSize(
								R.dimen.common_bottom_padding_new));
				right.setText(getString(R.string.acc_main_right_btn));
				adapter = new AccManagerListAdapter(this, dataList,
						AccManagerListAdapter.NORMAL, normalOnItemClickListener);
				listView.setAdapter(adapter);
				adapter.setDefaultAccStr(defaultAccStr);
				footerImageView.setVisibility(View.GONE);
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 刷新转出账户界面
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE) {
				BaseHttpEngine.showProgressDialogCanGoBack();
				queryAccounts();
			}
		}

	}

}
