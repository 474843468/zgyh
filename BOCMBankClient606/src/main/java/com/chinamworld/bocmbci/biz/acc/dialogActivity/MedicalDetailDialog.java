package com.chinamworld.bocmbci.biz.acc.dialogActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.inflaterview.InflaterViewDialog;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/** 医保账户弹出框 */
public class MedicalDetailDialog extends BaseActivity {
	private RelativeLayout rl_bank;
	private String nickname;
	/** 选择的medicalAccountList */
	private Map<String, Object> bankmap;
	/** 账户详情及余额 */
	private Map<String, Object> callbackmap;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	/** 第一个弹出框View */
	private RelativeLayout icAccountDetailView;// 第一个
	private int detailPosition = 0;
	/** 请求回来的medicalAccountList列表 */
	protected List<Map<String, Object>> medicalAccountList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		medicalAccountList = AccDataCenter.getInstance()
				.getMedicalAccountList();
		callbackmap = AccDataCenter.getInstance().getMedicalbackmap();
		detailPosition = this.getIntent().getIntExtra(
				ConstantGloble.ACC_POSITION, 0);
		bankmap = medicalAccountList.get(detailPosition);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		icAccountDetailView = (RelativeLayout) inflaterdialog
				.initMedicalAccountMessageDialogView(MedicalDetailDialog.this,
						bankmap, callbackmap, exitAccDetailClick,
						updateNicknameClick);
		rl_bank.removeAllViews();
		rl_bank.addView(icAccountDetailView);
	}

	/** 修改账户别名监听事件 */
	public OnClickListener updateNicknameClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText et_acc_accountnickname = (EditText) rl_bank
					.findViewById(R.id.et_acc_nickname);
			nickname = et_acc_accountnickname.getText().toString().trim();
			TextView acc_accountnickname_value = (TextView) rl_bank
					.findViewById(R.id.acc_accountnickname_value);
			if (nickname.equals(acc_accountnickname_value.getText().toString()
					.trim())) {
				// 重新展示账户详情窗口
				closeInput(et_acc_accountnickname);
				InflaterViewDialog inflaterdialog = new InflaterViewDialog(
						MedicalDetailDialog.this);
				RelativeLayout icActDetailView = (RelativeLayout) inflaterdialog
						.initMedicalAccountMessageDialogView(
								MedicalDetailDialog.this, bankmap, callbackmap,
								exitAccDetailClick, updateNicknameClick);
				rl_bank.removeAllViews();
				rl_bank.addView(icActDetailView);
				return;
			}
			// 以下为验证
			// 账户别名
			RegexpBean reb = new RegexpBean(
					MedicalDetailDialog.this.getString(R.string.nickname_regex),
					nickname, "nickname");

			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				closeInput(et_acc_accountnickname);
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}

		}
	};

	/**
	 * 请求conversationid回调
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		// 修改账户别名
		pSNGetTokenId();

	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求修改账户别名
		requestModifyAccountAlias(tokenId);
	}

	/** 请求tokenid */
	public void requestTokenId(int i) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		if (i == 0) {
			BiiHttpEngine.showProgressDialog();
		}
		HttpManager.requestBii(biiRequestBody, this, "requestTokenIdCallBack");
	}

	/** 请求tokenid回调 */
	public void requestTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
	}

	/** 请求修改账户别名 */
	public void requestModifyAccountAlias(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_MODIFYACCOUNT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTID_REQ,
				String.valueOf(bankmap.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTNICKNAME_REQ, nickname);
		paramsmap.put(Acc.MODIFY_ACC_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"modifyAccountAliasCallBack");
	}

	/**
	 * 请求修改账户别名回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void modifyAccountAliasCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String modifyResult = (String) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,
				this.getString(R.string.acc_modifyAccountAlias));
		// 修改显示的账户别名
		bankmap.put(Acc.ACC_NICKNAME_RES, nickname);
		// 重新展示账户详情窗口
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		RelativeLayout accDetailView = (RelativeLayout) inflaterdialog
				.initMedicalAccountMessageDialogView(MedicalDetailDialog.this,
						bankmap, callbackmap, exitAccDetailClick,
						updateNicknameClick);
		rl_bank.removeAllViews();
		rl_bank.addView(accDetailView);
	}

	/** 退出账户详情监听事件 */
	protected OnClickListener exitAccDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AccDataCenter.getInstance().setMedicalAccountList(
					medicalAccountList);
			setResult(RESULT_OK);
			finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		BaseDroidApp.getInstanse().setDialogAct(true);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
