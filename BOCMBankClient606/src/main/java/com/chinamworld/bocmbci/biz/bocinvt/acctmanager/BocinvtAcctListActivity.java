package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.BocinvtAcctAdapter;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.NewInvtEvaluationActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferInputActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 
 * @author panwe 理财账户管理列表页面
 */
public class BocinvtAcctListActivity extends BociBaseActivity {
	private boolean isFirst = true;
	// 登记
	private boolean isAcctSet;
	// 列表
	private boolean isList;
	private BocinvtAcctListActivity instance;
	private View mView;
	private BocinvtAcctAdapter mAdapter;
	private int mPosition;
	private int viewId;
	/** 转出=1，转入=2 */
	private int outorInId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setLeftSelectedPosition("bocinvtManager_1");
		setTitle(R.string.fincn_title_accManager);
		setRightBtnClick(btnRightOnclick);
		setBackBtnClick(backBtnClick);
		setText(getString(R.string.boci_evaluation_title));
		BiiHttpEngine.showProgressDialogCanGoBack();
		requestPsnInvestmentisOpenBefore();
	}

	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent = new Intent(BocinvtAcctListActivity.this,
//					SecondMainActivity.class);
//			startActivity(intent);
			goToMainActivity();
			

		}
	};
	private void setUpView() {
		mView = addView(R.layout.bocinvt_acct_list);
		ListView mListView = (ListView) mView.findViewById(R.id.listview);
		mAdapter = new BocinvtAcctAdapter(this, BociDataCenter.getInstance()
				.getAcctList());
		mAdapter.setViewOnClick(itemClick);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void bocinvtAcctCallback(Object resultObj) {
		super.bocinvtAcctCallback(resultObj);
		// 登记
		if (isAcctSet) {
			isTask = false;
			List<String> acctype = new ArrayList<String>();
			acctype.add("119");
			acctype.add("188");
			requestBankAcctList(acctype);
			return;
		}
		// 已登记
		if (isOpenBefore && !StringUtil.isNullOrEmpty(investBinding)
				&& isevaluated) {
			if (isList) {
				BaseHttpEngine.dissMissProgressDialog();
				sortAcct(BociDataCenter.getInstance().getBocinvtAcctList());
				if (isFirst) {
					setUpView();
					isFirst = false;
					return;
				}
				mAdapter.setmList(BociDataCenter.getInstance().getAcctList());
				return;
			}
			isList = true;
			requestBociAcctList("1", "0");
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		showTaskDialog();
	}

	/**
	 * 按账户类型 119、188、190排序
	 * 
	 * @param list
	 */
	private void sortAcct(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
				return ((String) lhs.get(Comm.ACCOUNT_TYPE))
						.compareTo((String) rhs.get(Comm.ACCOUNT_TYPE));
			}
		});
		BociDataCenter.getInstance().setAcctList(list);
	}

	protected OnClickListener exitClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};

	/**
	 * 弹出任务框
	 */
	private void showTaskDialog() {
		InflaterViewDialog inflater = new InflaterViewDialog(instance);
		dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpenBefore,
				investBinding, isevaluated, manageOpenClick, invtBindingClick,
				invtEvaluationClick, exitClick);
		TextView tv = (TextView) dialogView2
				.findViewById(R.id.tv_acc_account_accountState);
		tv.setText(this.getString(R.string.bocinvt_query_title));
		BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView2);
	}

	private OnClickListener btnRightOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(BocinvtAcctListActivity.this,
					NewInvtEvaluationActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};

	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			isAcctSet = false;
			mPosition = position;
			Map<String, Object> map = BociDataCenter.getInstance()
					.getAcctList().get(mPosition);
			String acctType = (String) map.get(Comm.ACCOUNT_TYPE);
			String acctId = (String) map.get(Comm.ACCOUNT_ID);
			if (!StringUtil.isNull(acctType)) {
				if (acctType.equals("119") || acctType.equals("188")) {
					if (view.getId() == R.id.text2) {
						if (!StringUtil.isNull(acctId)) {
							showNotiDialog();
							return;
						}
//						Intent intent = new Intent(instance,
//								AccInputRelevanceAccountActivity.class);
//						if (acctType.equals("119")) {
//							intent.putExtra(Acc.ACC_ACCOUNTNUMBER_RES,
//									(String) map.get(BocInvt.ACCOUNTNO));
//						}
//						startActivityForResult(intent,
//								ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
						Map<String, Object>mapData=new HashMap<String, Object>();
						if (acctType.equals("119")) {
							mapData.put(Acc.ACC_ACCOUNTNUMBER_RES, map.get(BocInvt.ACCOUNTNO));
						}
						BusinessModelControl.gotoAccRelevanceAccount(instance, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, mapData);
						return;
					}
					return;
				}
				viewId = view.getId();
				requestAcctOF();
			}
		}
	};

	/**
	 * 登记账户
	 * 
	 * @param v
	 */
	public void buttonSetAcctOnclick(View v) {
		isAcctSet = true;
		BaseHttpEngine.showProgressDialog();
		requestBociAcctList("1", "0");
	}

	private void showNotiDialog() {
		BaseDroidApp.getInstanse().showErrorDialog("您确定要取消登记吗？",
				R.string.cancle, R.string.confirm, new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							BaseHttpEngine.showProgressDialog();
							requestCommConversationId();
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							break;
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 开通理财
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				isOpenBefore = true;
				// 已评估
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE
					&& BociDataCenter.getInstance().getI() != 1) {
				isevaluated = true;
				// 已登记
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
				investBinding = BociDataCenter.getInstance().getUnSetAcctList();
			}
			if (isOpenBefore && !StringUtil.isNullOrEmpty(investBinding)
					&& isevaluated) {
				isList = true;
				isAcctSet = false;
				BaseHttpEngine.showProgressDialog();
				requestBociAcctList("1", "0");
				return;
			}
			showTaskDialog();
		} else if (!isAcctSet && resultCode == RESULT_CANCELED) {
			if (!isOpenBefore || !isevaluated
					|| StringUtil.isNullOrEmpty(investBinding)) {
				showTaskDialog();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void requestInvtEvaCallback(Object resultObj) {
		Map<String, Object> responseMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(responseMap)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(BocInvt.BOCINVT_EVA_GENDER_REQ,
				(String.valueOf(responseMap
						.get(BocInvt.BOCIEVA_GENDER_RES))));
		inputMap.put(BocInvt.BOCINVT_EVA_RISKBIRTHDAY_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_BIRTHDAY_RES));
		inputMap.put(BocInvt.BOCINVT_EVA_ADDRESS_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_ADDRESS_RES));
		inputMap.put(BocInvt.BOCINVT_EVA_PHONE_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_PHONE_RES));
		inputMap.put(BocInvt.BOCINVT_EVA_RISKMOBILE_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_MOBILE_RES));
		inputMap.put(BocInvt.BOCINVT_EVA_POSTCODE_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_POSTCODE_RES));
		inputMap.put(BocInvt.BOCINVT_EVA_RISKEMAIL_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_EMAIL_RES));
		inputMap.put(BocInvt.BOCINVT_EVA_REVENUE_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_REVENUE_RES));
		inputMap.put(
				BocInvt.BOCINVT_EVA_CUSTNATIONALITY_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_CUSTNATIONALITY_RES));
		inputMap.put(BocInvt.BOCINVT_EVA_EDUCATION_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_EDUCATION_RES));
		inputMap.put(
				BocInvt.BOCINVT_EVA_OCCUPATION_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_OCCUPATION_RES));
		inputMap.put(
				BocInvt.BOCINVT_EVA_HASINVESTEXPERIENCE_REQ,
				(String) responseMap
						.get(BocInvt.BOCIEVA_HASINVESTEXPERIENCE_RES));
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_REQUESTMAP,inputMap);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOICNVT_ISBEFORE_RESULT, responseMap);
		String status = (String) responseMap.get(BocInvt.BOCIEVA_STATUS_RES);
		if (!StringUtil.isNull(status)
				&& status.equals(ConstantGloble.BOCINVT_EVA_SUC_STATUS)) {
			isevaluated = true;
		} else {
			isevaluated = false;
		}
		requestBociAcctList("1", "0");
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}

	/**
	 * 请求token
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestAccountCancel(tokenId);
	}

	/**
	 * 请求取消登记账户
	 * 
	 * @param tokenId
	 */
	private void requestAccountCancel(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.ACCOUNTCANCEL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> map = BociDataCenter.getInstance().getAcctList()
				.get(mPosition);
		param.put(Comm.TOKEN_REQ, tokenId);
		param.put(Comm.ACCOUNT_ID, map.get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "accountCancelCallBack");
	}

	public void accountCancelCallBack(Object resultObj) {
		isList = true;
		isAcctSet = false;
		requestBociAcctList("1", "0");
	}

	/**
	 * 网上专属理财账户状态查询
	 */
	private void requestAcctOF() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.OFAACCOUNTSTATEQUERY);
		biiRequestBody.setParams(null);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "acctOFCallBack");
	}

	@SuppressWarnings("unchecked")
	public void acctOFCallBack(Object resultObj) {

		BociDataCenter.getInstance().setAcctOFmap(
				(Map<String, Object>) HttpTools.getResponseResult(resultObj));
		if (viewId == R.id.text1) {//转账
			Map<String, Object> accmap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
			TranDataCenter.getInstance().setAccmap(accmap);
			String openStatus = (String) accmap.get(BocInvt.OPENSTATUS);
			
			if (!StringUtil.isNullOrEmpty(openStatus)
					&& openStatus.equals(BocInvt.OPENSTATUS_S)
					&& !StringUtil.isNullOrEmpty(getOFBankAccountInfo())) {
				// 已开通,有绑定账户——可以继续转账,需要请求详情
				Map<String, Object> accOutInfoMap = (Map<String, Object>) accmap
						.get(BocInvt.FINANCIALACCOUNT);
				String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
				requestForAccountDetail(accountId);
				outorInId = 1;
			} else if(!StringUtil.isNullOrEmpty(openStatus)
					&& openStatus.equals(BocInvt.OPENSTATUS_B)){//未绑定
				BiiHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"请点击“状态查询”，绑定用于转出资金的银行账户后再进行资金划转");
			}else if(!StringUtil.isNullOrEmpty(openStatus)
					&& openStatus.equals(BocInvt.OPENSTATUS_R)){//未关联
				BiiHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"请点击“状态查询”，将网上专属理财账户关联至电子银行后再进行资金划转");
			}else{
				BiiHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"网上专属理财账户不可以进行资金转出");
			}
		} else if (viewId == R.id.text2) {//账户详情
			BaseHttpEngine.dissMissProgressDialog();
			Map<String, Object> acctOFmap = BociDataCenter.getInstance()
					.getAcctOFmap();
			if (acctOFmap.get(BocInvt.OPENSTATUS).equals(BocInvt.OPENSTATUS_N)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"您尚未开通网上专属理财账户");
				return;
			}
			Intent intent = new Intent(instance,
					BocinvtAcctDetailActivity.class);
			startActivityForResult(intent, 1001);
		}
	}

	/**
	 * 查询其他卡（除信用卡）详情 PsnAccountQueryAccountDetail
	 * 
	 * @param accountId
	 */
	private void requestForAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.ACCOUNTDETAIL);
		paramsmap.put(Tran.ACC_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(paramsmap);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForAccountDetailCallBack");
	}

	/**
	 * 查询其他卡（除信用卡）详情 PsnAccountQueryAccountDetail 返回
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestForAccountDetailCallBack(Object resultObj) {
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if (outorInId == 1) {
			TranDataCenter.getInstance().setCurrOutDetail(resultMap);
			// 转入详情
			Map<String, Object> accInInfoMap = (Map<String, Object>) TranDataCenter
					.getInstance().getAccmap().get(BocInvt.BANKACCOUNT);
			String accountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
			outorInId = 2;
			requestForAccountDetail(accountId);
		} else if (outorInId == 2) {
			BaseHttpEngine.dissMissProgressDialog();
			TranDataCenter.getInstance().setCurrInDetail(resultMap);
			TranDataCenter.getInstance().setAccOutInfoMap(
					(Map<String, Object>) TranDataCenter.getInstance()
							.getAccmap().get(BocInvt.FINANCIALACCOUNT));
			TranDataCenter.getInstance().setAccInInfoMap(
					(Map<String, Object>) TranDataCenter.getInstance()
							.getAccmap().get(BocInvt.BANKACCOUNT));
			TranDataCenter.getInstance()
					.setModuleType(ConstantGloble.BOCI_TYPE);
			startActivity(new Intent(this, TransferInputActivity.class));
		}
	}
}
