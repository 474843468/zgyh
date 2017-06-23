package com.chinamworld.bocmbci.biz.acc.dialogActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccountIsPayrollDetailActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdGuashiInfoActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class MyCardDetailDoalogActivity  extends BaseActivity{
	/** 账户列表信息页  信用卡详情 */
	private RelativeLayout view;
	private RelativeLayout rl_bank;
	private int detailPosition = 0;
	/** 请求回来的账户列表信息 */
	protected List<Map<String, Object>> bankAccountList;
	/** 选择的账户详情 */
	protected Map<String, Object> chooseBankAccount;
	/**账户类型*/
	private String accountType;
	
	private String accountId;
	private String  accountIbkNum=null;
	private String  nickName=null;
	private TextView mycrcd_product_name_value,acc_accountnickname_value,mycrcd_accnumber,mycrcd_state_value,
	card_currency1,card_currency1_value,card_currency2,card_currency2_value;
	
	private Button btn_one,btn_many;
	
	String[] morelist;
	// 购汇还款账户币种
	private String currType = null;
	private FrameLayout fl_acc_nickname;
	private LinearLayout ll_nickname;
	private Button btn_updatenickname;
	private EditText et_acc_accountnickname;
	private String nickname;
	private boolean gouhuiclick = false;
	private String tokenId;
	private String haveNotRepayAmout = null; // 本期未还款金额
	//工资单查询
	private LinearLayout payroll_query;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BaseDroidApp.getInstanse().setDialogAct(true);
		bankAccountList = AccDataCenter.getInstance().getBankAccountList();
		
		detailPosition = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		chooseBankAccount = bankAccountList.get(detailPosition);	
		accountType = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTTYPE_RES);
		accountId = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES);
		 accountIbkNum=(String) chooseBankAccount.get(Tran.ACCOUNTIBKNUM_RES);		
			nickName= (String) chooseBankAccount.get(Crcd.CRCD_NICKNAME_RES);
		
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_mycrcd_new_detail, null);
		rl_bank.removeAllViews();
		rl_bank.addView(view);
		init();
		initdata();
	}
	
	private void init() {
		mycrcd_product_name_value=(TextView)findViewById(R.id.mycrcd_product_name_value);
		acc_accountnickname_value=(TextView)findViewById(R.id.acc_accountnickname_value);
		mycrcd_accnumber=(TextView)findViewById(R.id.mycrcd_accnumber);
		mycrcd_state_value=(TextView)findViewById(R.id.mycrcd_state_value);
		card_currency1=(TextView)findViewById(R.id.card_currency1);
		card_currency1_value=(TextView)findViewById(R.id.card_currency1_value);
		card_currency2=(TextView)findViewById(R.id.card_currency2);
		card_currency2_value=(TextView)findViewById(R.id.card_currency2_value);
		/**工资卡账户查询*/
		payroll_query = (LinearLayout)findViewById(R.id.acc_payroll_query);
		if(AccDataCenter.getInstance().getIsPayrollAccount() != null && AccDataCenter.getInstance().getIsPayrollAccount()){
			payroll_query.setVisibility(View.VISIBLE);
			payroll_query.setOnClickListener(payrollQueryClick);
		}	
		btn_one=(Button)findViewById(R.id.btn_one);
		btn_many=(Button)findViewById(R.id.btn_many);
		ImageView img_exit = (ImageView)findViewById(R.id.img_exit_accdetail);
		img_exit.setOnClickListener(exitAccDetailClick);
		
		
		 fl_acc_nickname = (FrameLayout)findViewById(R.id.fl_nickname);
		 ll_nickname = (LinearLayout)findViewById(R.id.ll_nickname);
		 btn_updatenickname = (Button) findViewById(R.id.btn_update_nickname);
		 et_acc_accountnickname = (EditText)findViewById(R.id.et_acc_nickname);
		 EditTextUtils.setLengthMatcher(MyCardDetailDoalogActivity.this, et_acc_accountnickname, 20);
	}
	/** 账户工资卡查询监听器 */
	protected OnClickListener payrollQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MyCardDetailDoalogActivity.this, AccountIsPayrollDetailActivity.class);
			startActivity(intent);
			finish();
		}
	};
	private void initdata() {
		Map<String, Object> result =TranDataCenter.getInstance().getCrcdGeneralInfo();
		List<Map<String, Object>> actlist = (List<Map<String, Object>>) result.get(Crcd.CRCD_ACTLIST);
		mycrcd_product_name_value.setText(String.valueOf(result.get(Crcd.CRCD_PRODUCTNAME)));
		acc_accountnickname_value.setText((String) chooseBankAccount.get(Crcd.CRCD_NICKNAME_RES));
		mycrcd_accnumber.setText(StringUtil.getForSixForString(String.valueOf(result.get(Crcd.CRCD_ACCTNUM))));
		if(StringUtil.isNullOrEmpty(result.get(Crcd.CRCD_CARSTATUS))){
			mycrcd_state_value.setText("正常");	
		}else{
			mycrcd_state_value.setText(carStatusTypegMap.get(String.valueOf(result.get(Crcd.CRCD_CARSTATUS))));	
		}
		if (actlist.size() == 1) {
			String currencyCode=(String)actlist.get(0).get(Crcd.CRCD_CURRENCY_RES);
			card_currency1.setText(LocalData.Currency.get(currencyCode));	
			String availableBalance = (String) actlist.get(0).get(Crcd.CRCD_REALTIMEBALANCE);		
			String flag = (String)actlist.get(0).get(Crcd.CRCD_RTBALANCEFLAG);
			flag = flagConvert(flag);
			if("0".equals((String)actlist.get(0).get(Crcd.CRCD_RTBALANCEFLAG))){
				
				btn_one.setText(getResources().getString(R.string.card_payment_tittle));	
			}else{
				btn_one.setText(getResources().getString(R.string.card_payment_in));	
			}
			card_currency1_value.setText(flag +StringUtil.parseStringCodePattern(currencyCode,availableBalance, 2));
		}
		if (actlist.size() > 1) {
			String currencyCode=(String)actlist.get(0).get(Crcd.CRCD_CURRENCY_RES);
			card_currency1.setText(LocalData.Currency.get(currencyCode)+"：");	
			String availableBalance = (String) actlist.get(0).get(Crcd.CRCD_REALTIMEBALANCE);		
			String flag = (String)actlist.get(0).get(Crcd.CRCD_RTBALANCEFLAG);
			flag = flagConvert(flag);
			card_currency1_value.setText(flag +StringUtil.parseStringCodePattern(currencyCode,availableBalance, 2));
			
			
			String currencyCode1=(String)actlist.get(1).get(Crcd.CRCD_CURRENCY_RES);
			card_currency2.setText(LocalData.Currency.get(currencyCode1)+"：");	
			String availableBalance1 = (String) actlist.get(1).get(Crcd.CRCD_REALTIMEBALANCE);		
			String flag1 = (String)actlist.get(1).get(Crcd.CRCD_RTBALANCEFLAG);
			flag1 = flagConvert(flag1);
			card_currency2_value.setText(flag1 +StringUtil.parseStringCodePattern(currencyCode1,availableBalance1, 2));
			
			
			if("0".equals((String)actlist.get(0).get(Crcd.CRCD_RTBALANCEFLAG))||"0".equals((String)actlist.get(1).get(Crcd.CRCD_RTBALANCEFLAG))){
				
				btn_one.setText(getResources().getString(R.string.card_payment_tittle));	
			}else{
				btn_one.setText(getResources().getString(R.string.card_payment_in));	
			}
		}
		btn_one.setOnClickListener(btn_oneListener);
		morelist=new String [] { 

				getResources().getString(R.string.card_guashi1),
				getResources().getString(R.string.card_account_search),
		};	
		btn_many.setOnClickListener(btn_manyListener);	
		PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				btn_many, morelist, btn_manyListener);	
		
		ImageView img_update_accnickname = (ImageView) findViewById(R.id.img_acc_update_nickname);
		img_update_accnickname.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fl_acc_nickname.setVisibility(View.VISIBLE);
				ll_nickname.setVisibility(View.GONE);
				BaseActivity activity = (BaseActivity) BaseDroidApp.getInstanse()
						.getCurrentAct();
				if(activity != null)
					activity.upSoftInput();
				et_acc_accountnickname.setText(acc_accountnickname_value.getText().toString());
				et_acc_accountnickname.setSelection(et_acc_accountnickname.length());
			}
		});
		btn_updatenickname.setOnClickListener(updatenicknameClick);
	}
	
	
	/** 修改账户别名监听事件 */
	 OnClickListener updatenicknameClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText et_acc_accountnickname = (EditText) view
					.findViewById(R.id.et_acc_nickname);
			nickname = et_acc_accountnickname.getText().toString().trim();
			String strNickName=(String) chooseBankAccount.get(Crcd.CRCD_NICKNAME_RES);
			if (nickname.equals(strNickName)) {
				closeInput(et_acc_accountnickname);
				fl_acc_nickname.setVisibility(View.GONE);
				ll_nickname.setVisibility(View.VISIBLE);
				return;
			}
			// 以下为验证
			// 账户别名
			RegexpBean reb = new RegexpBean(
					MyCardDetailDoalogActivity.this
							.getString(R.string.nickname_regex),
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
	
	/** 自动还款状态 */
	final OnClickListener btn_manyListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			btn_manyListenerClick(text, v);

		}
	};
	
	/** 挂失补卡 */
	final OnClickListener searchClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			String nick = (String) chooseBankAccount.get(Crcd.CRCD_NICKNAME_RES);			
			String accountNumber = (String) chooseBankAccount
					.get(Crcd.CRCD_ACCOUNTNUMBER_RES);			
			String strAccountType = LocalData.AccountType.get(accountType);
			Intent intent = new Intent(MyCardDetailDoalogActivity.this,
					CrcdGuashiInfoActivity.class);
			intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			intent.putExtra(Crcd.CRCD_NICKNAME_RES, nick);
			intent.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, strAccountType);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
			finish();

		}
	};
	

	protected void btn_manyListenerClick(String text, View v) {
		if (text.equals(getResources().getString(R.string.card_guashi1))) {
					
			searchClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.card_account_search))) {
					
			BaseDroidApp.getInstanse().getCurrentAct().setResult(100);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}
		
			
	}
	
	private String flagConvert(String flag) {
		if ("0".equals(flag)) {
			flag = "欠款";
		} else if ("1".equals(flag)) {
			flag = "存款";
		} else if ("2".equals(flag)) {
			flag = "";
		}

		return flag;
	}
	public  Map<String, String> carStatusTypegMap = new HashMap<String, String>() {
		{
			put("ACCC", "销户销卡");
			put("ACTP", "卡未激活");
			put("CANC", "卡片取消");
			put("DCFR", "欺诈拒绝");
			put("FRAU", "欺诈冻结");
			put("FUSA", "首次使用超限");
			put("LOST", "挂失");
			put("LOCK", "锁卡");
			put("PIFR", "没收卡");			
			put("PINR", "密码输入错误超次");			
			put("QCPB", "卡预销户");			
			put("REFR", "欺诈卡，需联系发卡行");			
			put("STOL", "偷窃卡");			
			put("STPP", "止付");			
			put("BFRA", "分行冻结");
			put("CFRA", "催收冻结");
			put("SFRA", "客服冻结");
//			put("", "正常");
		}
	};
	
	/** 退出账户详情监听事件 */
	protected OnClickListener exitAccDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};

	final OnClickListener btn_oneListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			Map<String, Object> result =TranDataCenter.getInstance().getCrcdGeneralInfo();
			
			// 信用卡详情，跳转到转账页面
			result.put(Crcd.CRCD_ACCOUNTTYPE_RES, accountType);
			result.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
			result.put(Tran.ACCOUNTIBKNUM_RES, accountIbkNum);
			result.put(Crcd.CRCD_NICKNAME_RES, nickName);
			
			TranDataCenter.getInstance().setAccInInfoMap(result);
			TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(null);
			List<Map<String, Object>> actlist = (List<Map<String, Object>>) result.get(Crcd.CRCD_ACTLIST);
			
			for (int i = 0; i < actlist.size(); i++) {

				if (!"001".equals(String.valueOf(actlist.get(i).get(
						Crcd.CRCD_CURRENCY_RES)))) {
					// 得到外币 账单信息
					haveNotRepayAmout = (String) actlist.get(i).get(
							Crcd.CRCD_HAVENOTREPAYAMOUT); // 本期未还款金额
					currType = (String) actlist.get(i).get(Crcd.CRCD_CURRENCY); // 本期未还款金额
				}
			}
			if (!StringUtil.isNull(haveNotRepayAmout)
					&& Double.parseDouble(haveNotRepayAmout) > 0) {

				// 外币有欠款 请求购汇信息
				BaseHttpEngine.showProgressDialog();
				gouhuiclick = true;
				requestCommConversationId();
			} else {
				// 无欠款 直接到转账界面
				Intent intent = new Intent(MyCardDetailDoalogActivity.this,
						TransferManagerActivity1.class);
				TranDataCenter.getInstance().setModuleType(ConstantGloble.ACC_MANAGE_TYPE);
				intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
						ConstantGloble.CREDIT_TO_TRAN);
				startActivity(intent);
			}

		}
	};
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);		
			// 转账还款统一
			// 查询信用卡购汇还款信息
		
			if (gouhuiclick) {
				requesPsnCrcdQueryForeignPayOff();
			} else {
				// 获取TokenId
				pSNGetTokenId();
			}
	}
	
	
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
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求修改账户别名
		requestModifyAccountAlias(tokenId);
	}
	
	/** 请求修改账户别名 */
	public void requestModifyAccountAlias(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_MODIFYACCOUNT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTID_REQ,
				String.valueOf(chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES)));
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
	 *            返回结果
	 */
	public void modifyAccountAliasCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,
				this.getString(R.string.acc_modifyAccountAlias));
		// 修改显示的账户别名
		chooseBankAccount.put(Acc.ACC_NICKNAME_RES, nickname);
		AccDataCenter.getInstance().setBankAccountList(bankAccountList);
		fl_acc_nickname.setVisibility(View.GONE);
		ll_nickname.setVisibility(View.VISIBLE);
		acc_accountnickname_value.setText(nickname);
		

	}
	/**
	 * 信用卡查询购汇还款信息 sunh
	 */
	public void requesPsnCrcdQueryForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYDOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);// 转入账户标识 accountId
		map.put(Crcd.CRCD_CURRTYPE_REQ, currType);// 购汇还款账户币种 currType
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requesPsnCrcdQueryForeignPayOffCallBack");
	}

	
	/**
	 * 信用卡查询购汇还款信息返回 sunh
	 * 
	 * @param resultObj
	 */
	public void requesPsnCrcdQueryForeignPayOffCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);
		TranDataCenter.getInstance().setModuleType(ConstantGloble.ACC_MANAGE_TYPE);
		Intent intent = new Intent(MyCardDetailDoalogActivity.this, TransferManagerActivity1.class);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.CREDIT_TO_TRAN);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
		finish();
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
