package com.chinamworld.bocmbci.biz.acc.lossreport;

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
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.LossBankAccountAdapter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 临时挂失主页面
 * 
 * @author wangmengmeng
 * 
 */
public class AccDebitCardLossActivity extends AccBaseActivity {
	/** 账户列表信息页 */
	private View view;
	/** 账户列表 */
	private ListView lvBankAccountList;
	/** 确定按钮 */
	private Button btnConfirm;
	/** 选中记录项 */
	public int selectposition = -1;

	/*	*//** 挂失期限 */
	/*
	 * public RadioGroup rg_lossDays;
	 *//** 5天 */
	/*
	 * public RadioButton rb_lossDays_1;
	 *//** 长期 */
	/*
	 * public RadioButton rb_lossDays_2; public String lossDays;
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_lossreport_title));
		gonerightBtn();

		setLeftSelectedPosition("accountManager_3");
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		// 请求所有借记卡账户列表
		requestAccBankAccountList();
	}

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_debit_step1), this.getString(R.string.acc_debit_step2),
						this.getString(R.string.acc_debit_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		lvBankAccountList = (ListView) view.findViewById(R.id.acc_accountlist);

		btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
		/*
		 * rg_lossDays = (RadioGroup) view.findViewById(R.id.radioGroup_choose);
		 * rb_lossDays_1 = (RadioButton)
		 * view.findViewById(R.id.radio_lossDays_1); rb_lossDays_2 =
		 * (RadioButton) view.findViewById(R.id.radio_lossDays_2);
		 * rg_lossDays.setOnCheckedChangeListener(this);
		 * rb_lossDays_1.setChecked(true);
		 */

	}

	/*
	 * @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
	 * switch (checkedId) { case R.id.radio_lossDays_1: lossDays =
	 * lossDaysList.get(0); break; case R.id.radio_lossDays_2: lossDays =
	 * lossDaysList.get(1); break; default: break; }
	 * 
	 * }
	 */

	/** 请求所有借记卡账户列表信息 */
	public void requestAccBankAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		// 传递能作为转出账户的列表
		paramslist.add(AccBaseActivity.accountTypeList.get(3));
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		HttpManager.requestBii(biiRequestBody, this, "accBankAccountListCallBack");
	}

	/**
	 * 请求所有借记卡账户列表回调
	 * 
	 * @param resultObj
	 */
	public void accBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		bankAccountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		BaseHttpEngine.dissMissProgressDialog();
		if (bankAccountList == null || bankAccountList.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(this.getString(R.string.acc_null_lossact),
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							finish();
						}
					});
			return;
		}
		// 添加布局
		view = addView(R.layout.acc_losereport_list);
		// 初始化界面
		init();
		// 都已请求，进行列表赋值
		final LossBankAccountAdapter adapter = new LossBankAccountAdapter(AccDebitCardLossActivity.this,
				bankAccountList);
		lvBankAccountList.setAdapter(adapter);
		lvBankAccountList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (selectposition == position) {
					return;
				} else {
					selectposition = position;
					adapter.setSelectedPosition(position);
				}
			}
		});
		// 确定按钮监听
		btnConfirm.setOnClickListener(confirmClickListener);
	}

	/*
	 * @Override public void requestCommConversationIdCallBack(Object resultObj)
	 * { super.requestCommConversationIdCallBack(resultObj); // 请求安全因子组合id
	 * requestGetSecurityFactor(lossReportServiceId); }
	 */

	/*
	 * @Override public void requestGetSecurityFactorCallBack(Object resultObj)
	 * { super.requestGetSecurityFactorCallBack(resultObj);
	 * BaseDroidApp.getInstanse().showSeurityChooseDialog( new OnClickListener()
	 * {
	 * 
	 * @Override public void onClick(View v) { // 进行临时挂失预交易
	 * requestLossReportConfirm(); } });
	 * 
	 * }
	 */

	/**
	 * 请求临时挂失预交易
	 */
	/*
	 * public void requestLossReportConfirm() { BiiRequestBody biiRequestBody =
	 * new BiiRequestBody();
	 * biiRequestBody.setMethod(Acc.ACC_LOSSREPORTCONFIRM_API);
	 * biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
	 * .getBizDataMap().get(ConstantGloble.CONVERSATION_ID)); Map<String,
	 * String> map = new HashMap<String, String>();
	 * map.put(Acc.LOSSCONFIRM_ACTNUM_REQ,
	 * String.valueOf(bankAccountList.get(selectposition).get(
	 * Acc.ACC_ACCOUNTNUMBER_RES))); map.put(Acc.LOSSCONFIRM_COMBINID_REQ,
	 * BaseDroidApp.getInstanse() .getSecurityChoosed());
	 * map.put(Acc.LOSSCONFIRM_LOSSDAYS_REQ, lossDays);
	 * biiRequestBody.setParams(map); HttpManager.requestBii(biiRequestBody,
	 * this, "requestLossReportConfirmCallBack"); }
	 *//**
	 * 请求临时挂失预交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	/*
	 * public void requestLossReportConfirmCallBack(Object resultObj) {
	 * BiiResponse biiResponse = (BiiResponse) resultObj; List<BiiResponseBody>
	 * biiResponseBodys = biiResponse.getResponse(); BiiResponseBody
	 * biiResponseBody = biiResponseBodys.get(0); // 通讯结束,关闭通讯框
	 * BaseHttpEngine.dissMissProgressDialog(); Map<String, Object>
	 * lossReportmap = (Map<String, Object>) (biiResponseBody .getResult()); if
	 * (StringUtil.isNullOrEmpty(lossReportmap)) { return; }
	 * AccDataCenter.getInstance().setConfirmResult(lossReportmap); //
	 * 进入临时挂失确认信息页面 Intent intent = new Intent(AccDebitCardLossActivity.this,
	 * AccLossReportConfirmActivity.class);
	 * intent.putExtra(Acc.LOSSCONFIRM_LOSSDAYS_REQ, lossDays);
	 * startActivity(intent); }
	 */

	/** 确定按钮点击监听事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (selectposition == -1) {
				// 代表没有选择账户就点击了下一步
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						AccDebitCardLossActivity.this.getString(R.string.acc_loss_maintitle_error));
				return;
			}
			AccDataCenter.getInstance().setLossReportMap(bankAccountList.get(selectposition));
			/*
			 * requestCommConversationId(); BaseHttpEngine.showProgressDialog();
			 */
			// 进入临时挂失输入信息页面
			Intent intent = new Intent(AccDebitCardLossActivity.this, AccLossReportInputActivity.class);
			startActivity(intent);
		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};

}
