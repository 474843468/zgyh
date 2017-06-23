package com.chinamworld.bocmbci.biz.acc.lossreport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 临时挂失输入信息页面
 * 
 * @author luqipeng
 * 
 */
public class AccLossReportInputActivity extends AccBaseActivity implements OnCheckedChangeListener {
	/** 临时挂失确认信息页 */
	private View view;
	/** 账户类型 */
	private TextView acc_input_type;
	/** 账号 */
	private TextView acc_input_actnum;
	/** 账户别名 */
	private TextView acc_loss_nicknames;
	/** 挂失有效期布局 */
	private RadioGroup rg_inputDays;
	/** 5天 */
	public RadioButton acc_input_penthemeron;
	/** 长期 */
	public RadioButton acc_input_longTerm;
	/** 是否冻结主卡布局 */
	private RadioGroup radioGroup_input_mainCard;
	/** 是否同时冻结主卡 (是) */
	public RadioButton acc_intpu_freeze_is;
	/** 是否同时冻结主卡 (否) */
	public RadioButton acc_intpu_freeze_no;
	/***/
	public View input_date_layout;
	/** 挂失期限 */
	private TextView acc_input_ifFreeze = null;

	/** 确定 */
	private Button btn_input_confirm;
	/** 进行临时挂失账户信息 */
	private Map<String, Object> lossReportMap;
	
	/** 加密控件里的随机数 */
	protected String randomNumber;

	/** 临时挂失期限 */
	private String lossDays;
	// /** 上一步 */
	// private Button btn_input_last;
	/** 是否同时冻结主卡 */
	private String masterAccount;
	/** 是否冻结主卡 单选 */
	private TextView tv_acc_input_freeze;
	/** 是否冻结主卡 */
	private TextView tv_acc_freeze;
	/** 是否冻结主账户提示 选择是提示*/
	private TextView intpu_freeze_is;
	/** 是否冻结主账户提示 选择否提示*/
	private TextView intpu_freeze_no;
	private TextView acc_lossreport_input_prompt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_lossreport_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_lossreport_input);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);

		lossReportMap = AccDataCenter.getInstance().getLossReportMap();
		// 得到选择的账户信息
		// lossReportMap = AccDataCenter.getInstance().getLossReportMap();
		// confirmResult = AccDataCenter.getInstance().getConfirmResult();
		// lossDays =
		// this.getIntent().getStringExtra(Acc.LOSSCONFIRM_LOSSDAYS_REQ);
		// masterAccount =
		// this.getIntent().getStringExtra(Acc.LOSSCONFIRM_MASTER_ACCOUNT);
		// // 初始化界面
		init();
		
		
	}

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_debit_step1), this.getString(R.string.acc_debit_step2),
						this.getString(R.string.acc_debit_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		// 账户别名
		acc_loss_nicknames = (TextView) view.findViewById(R.id.tv_acc_input_actnum_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, acc_loss_nicknames);
		acc_loss_nicknames.setText(String.valueOf(lossReportMap.get(Acc.ACC_NICKNAME_RES)));

		acc_input_type = (TextView) view.findViewById(R.id.tv_acc_input_type_value);
		acc_input_actnum = (TextView) view.findViewById(R.id.tv_acc_input_actnum_value);
		input_date_layout = view.findViewById(R.id.ll_acc_input_date);
		acc_input_ifFreeze = (TextView) view.findViewById(R.id.tv_acc_input_ifFreeze);
		btn_input_confirm = (Button) view.findViewById(R.id.btnConfirm);
		intpu_freeze_is = (TextView) findViewById(R.id.intpu_freeze_is);
		intpu_freeze_no = (TextView) findViewById(R.id.intpu_freeze_no);
		acc_lossreport_input_prompt = (TextView) findViewById(R.id.acc_lossreport_input_prompt);
		btn_input_confirm.setOnClickListener(confirmClickListener);

		/** 有效期和是否冻结主账户的弹框设置 */
		tv_acc_input_freeze = (TextView) view.findViewById(R.id.tv_acc_input_freeze);
		tv_acc_freeze = (TextView) view.findViewById(R.id.tv_acc_freeze);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_acc_input_freeze);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_acc_freeze);
		/**冻结主卡时间 五天 长期*/
		rg_inputDays = (RadioGroup) view.findViewById(R.id.radioGroup_input_choose);
		acc_input_penthemeron = (RadioButton) view.findViewById(R.id.rb_acc_input_penthemeron);
		acc_input_longTerm = (RadioButton) view.findViewById(R.id.rb_acc_input_longTerm);
		rg_inputDays.setOnCheckedChangeListener(this);
		acc_input_penthemeron.setChecked(true);

		/** 是否冻结主卡 */
		radioGroup_input_mainCard = (RadioGroup) view.findViewById(R.id.radioGroup_input_mainCard);
		acc_intpu_freeze_is = (RadioButton) view.findViewById(R.id.rb_acc_intpu_freeze_is);
		acc_intpu_freeze_no = (RadioButton) view.findViewById(R.id.rb_acc_intpu_freeze_no);

		radioGroup_input_mainCard.setOnCheckedChangeListener((android.widget.RadioGroup.OnCheckedChangeListener) this);
		acc_intpu_freeze_is.setChecked(true);

		// ////////////////
		String loss_type = String.valueOf(lossReportMap.get(Acc.ACC_ACCOUNTTYPE_RES));
		acc_input_type.setText(LocalData.AccountType.get(loss_type));
		String acc_loss_number = String.valueOf(lossReportMap.get(Acc.ACC_ACCOUNTNUMBER_RES));
		acc_input_actnum.setText(StringUtil.getForSixForString(acc_loss_number));

		lossDays = lossDaysList.get(0);
		acc_input_ifFreeze.setText(lossDaysMap.get(lossDays));

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_acc_input_penthemeron:
			lossDays = lossDaysList.get(0);
			acc_input_ifFreeze.setText(lossDaysMap.get(lossDays));
			break;
		case R.id.rb_acc_input_longTerm:
			lossDays = lossDaysList.get(1);
			acc_input_ifFreeze.setText(lossDaysMap.get(lossDays));
			break;
		case R.id.rb_acc_intpu_freeze_is:
			masterAccount = masterAccountList.get(0);
			input_date_layout.setVisibility(View.VISIBLE);
			intpu_freeze_is.setVisibility(View.VISIBLE);
			acc_lossreport_input_prompt.setVisibility(View.VISIBLE);
			intpu_freeze_no.setVisibility(View.GONE);
			break;
		case R.id.rb_acc_intpu_freeze_no:
			masterAccount = masterAccountList.get(1);
			input_date_layout.setVisibility(View.INVISIBLE);
			intpu_freeze_is.setVisibility(View.GONE);
			intpu_freeze_no.setVisibility(View.VISIBLE);
			acc_lossreport_input_prompt.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	/** 上一步按钮点击事件 */
	OnClickListener goLastClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	/** 下一步按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// if
			// (confirmResult.containsKey(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES)) {
			// // 进入临时挂失确认页面
			// Intent intent = new Intent(AccLossReportInputActivity.this,
			// AccLossReportConfirmActivity.class);
			// } else {
			// BaseHttpEngine.showProgressDialog();
			// pSNGetTokenId();
			// }
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
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

	// ////////////////////////////////////////////////////////////

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
		requestGetSecurityFactor(lossReportServiceId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 进行临时挂失预交易
				requestLossReportConfirm();
			}
		});

	}

	/////////////////////////////////////////////
	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}
	
	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		//关闭通讯框
		BiiHttpEngine.dissMissProgressDialog();
		// // 加密控件设置随机数
		// sipBoxActiveCode.setRandomKey_S(randomNumber);
		// sipBoxSmc.setRandomKey_S(randomNumber);
		// 进入临时挂失确认信息页面
		Intent intent = new Intent(this, AccLossReportConfirmActivity.class);
		intent.putExtra(Acc.LOSSCONFIRM_LOSSDAYS_REQ, lossDays);
		intent.putExtra(Acc.RANDOMNUMBER, randomNumber);
		// 借记卡临时挂失优化,修改是否冻结主账户字段
		intent.putExtra(Acc.LOSSCONFIRM_ACC_FLOZENFLAG, acc_intpu_freeze_is.isChecked());
		startActivity(intent);		
	}
	
	/**
	 * 请求临时挂失预交易
	 */
	public void requestLossReportConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_LOSSREPORTCONFIRM_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Acc.LOSSCONFIRM_ACTNUM_REQ, String.valueOf(lossReportMap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		map.put(Acc.LOSSCONFIRM_COMBINID_REQ, BaseDroidApp.getInstanse().getSecurityChoosed());
		map.put(Acc.LOSSCONFIRM_LOSSDAYS_REQ, lossDays);
		// 借记卡临时挂失优化,修改是否冻结主账户字段
		map.put(Acc.LOSSCONFIRM_ACC_FLOZENFLAG, acc_intpu_freeze_is.isChecked() ? "Y" : "N");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestLossReportConfirmCallBack");
	}
	
	/**
	 * 请求临时挂失预交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestLossReportConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
	//	BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> lossReportmap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(lossReportmap)) {
			return;
		}
		AccDataCenter.getInstance().setConfirmResult(lossReportmap);
		
		//请求随机数
		requestForRandomNumber();
	}

}
