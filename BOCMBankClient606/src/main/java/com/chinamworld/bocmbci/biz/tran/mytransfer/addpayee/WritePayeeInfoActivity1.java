package com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.order.OrderMainActivity;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenSacnResultActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 新增收款人 他行信息填写
 * 
 * @author
 * 
 */
public class WritePayeeInfoActivity1 extends TranBaseActivity implements
OnClickListener, OnCheckedChangeListener {
	private static final String TAG = WritePayeeInfoActivity1.class
			.getSimpleName();
	/** 下一步 */
	private Button mNextBtn = null;
	/** 上一步 */
	private Button mLastBtn = null;
	/** 查询开户行名称 */
	private Button kBankBtn = null;

	/** 收款人姓名 */
	private EditText payeeNameEt = null;
	/** 收款人账户 */
	private EditText accNumEt = null;
	/** 重输收款人账户 */
	// private EditText accNumAgainEt = null;
	/** 收款人手机号 */
	private EditText payeeMobileEt = null;
	/** 开户行银行名称 */
	private TextView inBankNameEt = null;
	/** 账户所属银行 */
	private Spinner accInBankSp = null;
	/** 账户所属银行 */
	private String accInKBank = null;
	/** 账户所属银行 */
	// private String bankName = "";
	private String toOrgName = "";
	/** 账户所属银行 省行联行号 */
	private String orgNameCnapsCode = "";
	/* 开户行名称信息的容器，根据账户所属银行的值，是否显示 */
	private View containerV;
	/* 提示信息，根据账户所属银行的值，是否显示 */
	private TextView messageTV;
	// R.layout.tran_payee_other_bank_write_info_activity对应的容器
	private View inflaterView;
	/** 账户账号 */
	private String bocAccNum;

	private Map<String, Object> accInInfoMap;

	/** 底部layout */
	private LinearLayout footLayout;
	/** 左侧菜单 */
	private Button showBtn;
	/** 卡类型 */
	// private String accountType;

	private TextView againInputTv;
	private boolean isfrommanage = false;
	/** 行内 */
	private int BANKIN = 10;
	/** 跨行 */
	private int BANKOTHER = 11;
	private int FLAG = 0;
	/** 收款行行号 */
	private String payeeBankNum = "";
	/** 账户类型 */
	private String toAccountType = "";

	/** 跨行转账——转账方式 */
	private RadioGroup radio_tran_type;
	// 实时到账
	private RadioButton rb_shishi;
	// 普通转账
	private RadioButton rb_common;
	//转账银行类型选择    中行  他行
	private RadioGroup radioGroupForTranTypeAccount;
	//选择中行
	private RadioButton radio_zh_account;
	//选择他行
	private RadioButton radio_other_account;
	//实时转账  普通转账  LinearLayout
	private LinearLayout ll_trantype;
	/** 实时转账 */
	// 银行选择
	private View container_bank;
	//	/** 实时——所属银行 */
	//	private TextView tv_acc_bank;
	private String shishiBankName;
	/** 实时——所属银行查询 */
	private TextView btn_query_bank;
	/** 普通转账 */
	private View ll_bankname;
	/**实时到账提示语*/
	private View radioShishiPrompt;
	/** 新增收款人标记转出账户是否为长城信用卡 */
//	private boolean TRANOUTISGREATWALL = false;
	private List<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_addNewPayee_head));
		inflaterView = mInflater.inflate(
				R.layout.tran_payee_other_bank_write_info_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(inflaterView);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.close));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		setLeftButtonPopupGone();
		isfrommanage = this.getIntent().getBooleanExtra(ISFROMMANAGE, false);
		// 标记新增收款人 查询网点
		if (isfrommanage) {
			psnVestOrgFlag = VEST_ADD_PAYEE;
//			TRANOUTISGREATWALL = false;
		} else {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map = TranDataCenter.getInstance().getAccOutInfoMap();
//			String tranOutType = (String) map.get(Tran.ACCOUNTTYPE_RES);
//			if (!StringUtil.isNullOrEmpty(tranOutType)) {
//				if (tranOutType.equals(ConstantGloble.ZHONGYIN)) {
//					TRANOUTISGREATWALL = true;
//				}
//			}
			psnVestOrgFlag = VEST_ADD_NEW_PAYEE;
		}
		setupView();
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.add_new_payee));
		StepTitleUtils.getInstance().setTitleStep(2);
		container_bank = (View) findViewById(R.id.container_bank);
		ll_bankname = (View) findViewById(R.id.ll_bankname);
		//		tv_acc_bank = (TextView) findViewById(R.id.tv_acc_bank);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		//				tv_acc_bank);
		mLastBtn = (Button) inflaterView
				.findViewById(R.id.btn_last_payee_other_bank_write);
		mNextBtn = (Button) inflaterView
				.findViewById(R.id.btn_next_payee_other_bank_write);
		kBankBtn = (Button) inflaterView
				.findViewById(R.id.btn_query_kbank_othbank_write);
		btn_query_bank = (TextView) findViewById(R.id.btn_query_bank);
		// 收款人姓名为30个中文字符或60个英文字符
		payeeNameEt = (EditText) inflaterView
				.findViewById(R.id.et_name_payee_other_bank_write);
		EditTextUtils.setLengthMatcher(this, payeeNameEt, 60);
		accNumEt = (EditText) inflaterView
				.findViewById(R.id.et_accnum_payee_other_bank_write);
		EditTextUtils.setBankCardNumAddSpace(accNumEt);
		againInputTv = (TextView) inflaterView
				.findViewById(R.id.again_input_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				againInputTv);
		payeeMobileEt = (EditText) inflaterView
				.findViewById(R.id.et_mobile_payee_other_bank_write);
		inBankNameEt = (TextView) inflaterView
				.findViewById(R.id.et_acc_bankname_payee_other_bank_write);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				WritePayeeInfoActivity1.this, inBankNameEt);
		accInBankSp = (Spinner) inflaterView
				.findViewById(R.id.sp_accbank_payee_other_bank_write);
		containerV = inflaterView.findViewById(R.id.container);
		messageTV = (TextView) inflaterView
				.findViewById(R.id.payee_tip_message_tv);
		// 默认是中国银行
		accInKBank = LocalData.kBankList.get(0);
		/*
		 * 默认中国银行 containerV和messageTV不显示，当用户选择其他银行在显示
		 */
		containerV.setVisibility(View.GONE);
		messageTV.setVisibility(View.GONE);
//		if (TRANOUTISGREATWALL) {
//			list.add(LocalData.kBankList.get(0));
//		} else {
			list = LocalData.kBankList;
//		}

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.custom_spinner_item, list);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accInBankSp.setAdapter(adapter1);
		accInBankSp.setOnItemSelectedListener(onItemSelectedListener);
//		if (TRANOUTISGREATWALL) {
//			accInBankSp.setEnabled(false);
//			accInBankSp.setBackgroundResource(R.drawable.bg_spinner_default);
//		} else {
			accInBankSp.setEnabled(true);
			accInBankSp.setBackgroundResource(R.drawable.bg_spinner);
//		}
		radio_tran_type = (RadioGroup) findViewById(R.id.radioGroupForTranType);
		radioShishiPrompt = findViewById(R.id.radio_shishi_prompt);
		rb_shishi = (RadioButton) findViewById(R.id.radio_shishi);
		rb_common = (RadioButton) findViewById(R.id.radio_common);
		radioGroupForTranTypeAccount = (RadioGroup) findViewById(R.id.radioGroupForTranTypeAccount);
		radio_zh_account = (RadioButton) findViewById(R.id.radio_zh_account);
		radio_other_account = (RadioButton) findViewById(R.id.radio_other_account);
		ll_trantype = (LinearLayout) findViewById(R.id.ll_trantype);
		radio_tran_type.setOnCheckedChangeListener(this);
		radioGroupForTranTypeAccount.setOnCheckedChangeListener(this);
		rb_shishi.setChecked(true);
		radio_zh_account.setChecked(true);
		mNextBtn.setOnClickListener(this);
		mLastBtn.setOnClickListener(this);
		kBankBtn.setOnClickListener(this);
		btn_query_bank.setOnClickListener(this);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		//				btn_query_bank);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			toOrgName = data
			.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
			orgNameCnapsCode = data.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
			if (rb_shishi.isChecked()) {
				btn_query_bank.setVisibility(View.VISIBLE);
				btn_query_bank.setText(toOrgName);
			} else {
				inBankNameEt.setVisibility(View.VISIBLE);
				inBankNameEt.setText(toOrgName);
			}

			break;
		case 105:
			setResult(105);
			finish();
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_query_bank:
			BaseHttpEngine.showProgressDialog();
			// 查询所属银行——实时    常用银行
			PsnOtherBankQueryForTransToAccount("HOT");
			break;
			// 查询开户银行名称
		case R.id.btn_query_kbank_othbank_write:
			toOrgName = inBankNameEt.getText().toString().trim();
			// 查询开户行
			requestQueryExternalBank(0, 10);
			break;
		case R.id.btn_last_payee_other_bank_write:
			finish();
			break;
			// 确定按钮
		case R.id.btn_next_payee_other_bank_write:
			//他行转账
			if(radio_other_account.isChecked()){
				//选择 实时转账
				if (rb_shishi.isChecked()) {
					String bocPayeeName = payeeNameEt.getText().toString().trim();
					bocAccNum = accNumEt.getText().toString().trim();
					String bocPayeeMobile = payeeMobileEt.getText().toString()
							.trim();
					shishiBankName = btn_query_bank.getText().toString().trim();
					String errorInfo = null;
					if ("请选择".equals(shishiBankName)) {
						errorInfo = getResources().getString(
								R.string.shishibank_name_error1);
						BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
						return;
					}
					if (StringUtil.isNullOrEmpty(bocPayeeName)) {
						errorInfo = getResources().getString(
								R.string.payee_name_error);
						BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);

						return;
					}
					if (StringUtil.isNullOrEmpty(bocAccNum)) {
						errorInfo = getResources().getString(
								R.string.payee_accnum_error);
						BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
						return;
					} else {
						// TODO 账户的正则 待确定
					}
					if(StringUtil.isNullOrEmpty(bocPayeeMobile)){
//						BaseHttpEngine.showProgressDialog();
//						requestCommConversationId();
					}else{
						ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
						RegexpBean reMobile = new RegexpBean(WritePayeeInfoActivity1.this.getString(R.string.trans_remit_phone_nolabel),bocPayeeMobile, "shoujiH_1");
						lists.add(reMobile);
						if (RegexpUtils.regexpData(lists)) {
//							BaseHttpEngine.showProgressDialog();
//							requestCommConversationId();
						}else{
							return;
						}
					}
					bocAccNum = bocAccNum.replace(" ", "");
					accInInfoMap = new HashMap<String, Object>();
					accInInfoMap.put(Comm.ACCOUNTNUMBER, bocAccNum);
					accInInfoMap.put(Tran.EBPSQUERY_PAYEEACTNO_REQ, bocAccNum);
					accInInfoMap.put(Comm.ACCOUNT_NAME, bocPayeeName);
					accInInfoMap.put(Tran.EBPSQUERY_PAYEEACTNAME_REQ, bocPayeeName);
					accInInfoMap.put(Tran.TRANS_BANKNAME_RES, shishiBankName);
					accInInfoMap.put(Tran.EBPSQUERY_PAYEEBANKNAME_REQ,
							shishiBankName);
					accInInfoMap.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ,
							bocPayeeMobile);
					accInInfoMap.put(Tran.MANAGE_PAYEELIST_BANKNAME_RES, toOrgName);
					accInInfoMap.put(Tran.MANAGE_PAYEELIST_CNAPSCODE_RES,
							orgNameCnapsCode);
					accInInfoMap.put(Tran.MANAGE_PAYEELIST_ADDRESS_RES, toOrgName);
					accInInfoMap.put(Tran.EBPSQUERY_PAYEECNAPS_REQ,
							orgNameCnapsCode);
					accInInfoMap.put(Tran.TRANS_MOBILE_RES, bocPayeeMobile);
					accInInfoMap
					.put(Tran.EBPSQUERY_PAYEEMOBILE_REQ, bocPayeeMobile);
					TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
					FLAG = BANKOTHER;
					if (isfrommanage) {
						// TODO 从收款人管理进来
						requestCommConversationId();
						BiiHttpEngine.showProgressDialog();
					} else {
						Intent intent = new Intent();
						intent.putExtra(ISSHISHITYPE, true);
						setResult(103, intent);// 跨行转账结果码
						finish();
					}

				} else {
					String bocPayeeName = payeeNameEt.getText().toString().trim();
					bocAccNum = accNumEt.getText().toString().trim();
					String bocPayeeMobile = payeeMobileEt.getText().toString()
							.trim();
					toOrgName = inBankNameEt.getText().toString().trim();
					/*
					 * 如果账户银行是中行，则不检查开户行的值，否则校验开户行的值
					 */
					String errorInfo = null;
//					if (!LocalData.kBankList.get(0).equals(accInKBank)) {
						if (containerV.isShown()) {
							if (StringUtil.isNullOrEmpty(toOrgName)) {
								errorInfo = getResources().getString(
										R.string.inbank_name_error);
								BaseDroidApp.getInstanse().showInfoMessageDialog(
										errorInfo);
								return;
							}
						}

//					}
					if (StringUtil.isNullOrEmpty(bocPayeeName)) {
						errorInfo = getResources().getString(
								R.string.payee_name_error);
						BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);

						return;
					}
					if (StringUtil.isNullOrEmpty(bocAccNum)) {
						errorInfo = getResources().getString(
								R.string.payee_accnum_error);
						BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
						return;
					} else {
						// TODO 账户的正则 待确定
					}
					if(StringUtil.isNullOrEmpty(bocPayeeMobile)){
//						BaseHttpEngine.showProgressDialog();
//						requestCommConversationId();
					}else{
						ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
						RegexpBean reMobile = new RegexpBean(WritePayeeInfoActivity1.this.getString(R.string.trans_remit_phone_nolabel),bocPayeeMobile, "shoujiH_1");
						lists.add(reMobile);
						if (RegexpUtils.regexpData(lists)) {
//							BaseHttpEngine.showProgressDialog();
//							requestCommConversationId();
						}else{
							return;
						}
					}
					bocAccNum = bocAccNum.replace(" ", "");
					accInInfoMap = new HashMap<String, Object>();
					accInInfoMap.put(Comm.ACCOUNTNUMBER, bocAccNum);
					accInInfoMap.put(Comm.ACCOUNT_NAME, bocPayeeName);
					accInInfoMap.put(Tran.TRANS_MOBILE_RES, bocPayeeMobile);
					// 如果选择的是中行 按照行内转账处理
//					if (LocalData.kBankList.get(0).equals(accInKBank)) {
//						// 查询卡类型
//						// requestQueryCardType();
//						TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
//						FLAG = BANKIN;
//						reqeustQueryAccountVestOrg();
//
//					} else {
						// 如果选择的是其他行 按照跨行转账处理
						if (containerV.isShown()) {
							accInInfoMap.put(Tran.TRANS_ADDRESS_RES, toOrgName);
							accInInfoMap.put(Tran.TRANS_CNAPSCODE_RES,
									orgNameCnapsCode);
						} else {
							accInInfoMap.put(Tran.TRANS_ADDRESS_RES, "");
							accInInfoMap.put(Tran.TRANS_CNAPSCODE_RES, "");
						}
						accInInfoMap.put(ISHAVEBANKNAME, true);
						accInInfoMap.put(ISHAVEADDRESS, true);
						accInInfoMap.put(Tran.TRANS_BANKNAME_RES, accInKBank);
						accInInfoMap.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ,
								bocPayeeMobile);
						TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
						FLAG = BANKOTHER;
						if (isfrommanage) {
							// 从收款人管理进来
							requestCommConversationId();
							BiiHttpEngine.showProgressDialog();
						} else {
							Intent intent = new Intent();
							if (containerV.isShown()) {
								intent.putExtra(ISSHISHITYPE, false);
							} else {
								intent.putExtra(ISSHISHITYPE, true);
							}
							setResult(103, intent);// 跨行转账结果码
							finish();
						}

					}
//				}
			}else{
				//中行转账
				String bocPayeeName = payeeNameEt.getText().toString().trim();
				String bocPayeeMobile = payeeMobileEt.getText().toString().trim();
				bocAccNum = accNumEt.getText().toString().trim();
				
				if (StringUtil.isNullOrEmpty(bocPayeeName)) {
					String errorInfo = getResources().getString(
							R.string.payee_name_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);

					return;
				}
				if (StringUtil.isNullOrEmpty(bocAccNum)) {
					String errorInfo = getResources().getString(
							R.string.payee_accnum_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
					return;
				}
				if(StringUtil.isNullOrEmpty(bocPayeeMobile)){
//					BaseHttpEngine.showProgressDialog();
//					requestCommConversationId();
				}else{
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					RegexpBean reMobile = new RegexpBean(WritePayeeInfoActivity1.this.getString(R.string.trans_remit_phone_nolabel),bocPayeeMobile, "shoujiH_1");
					lists.add(reMobile);
					if (RegexpUtils.regexpData(lists)) {
//						BaseHttpEngine.showProgressDialog();
//						requestCommConversationId();
					}else{
						return;
					}
				}
				bocAccNum = bocAccNum.replace(" ", "");
				accInInfoMap = new HashMap<String, Object>();
				accInInfoMap.put(Comm.ACCOUNTNUMBER, bocAccNum);
				accInInfoMap.put(Comm.ACCOUNT_NAME, bocPayeeName);
				accInInfoMap.put(Tran.TRANS_MOBILE_RES, bocPayeeMobile);
				// 如果选择的是中行 按照行内转账处理
					// 查询卡类型
		
					TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
					FLAG = BANKIN;
					reqeustQueryAccountVestOrg();
			}
			break;
		default:
			break;
		}

	}

	private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long id) {
			accInKBank = list.get(position);
			inBankNameEt.setVisibility(View.GONE);
			inBankNameEt.setText(null);
//			if (position == 0) {
//				containerV.setVisibility(View.GONE);
//				messageTV.setVisibility(View.GONE);
//			} else {
				containerV.setVisibility(View.VISIBLE);
				messageTV.setVisibility(View.VISIBLE);
//			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			accInKBank = list.get(0);
		}
	};

	/**
	 * 转账银行开户行查询
	 */
	public void requestQueryExternalBank(int currentIndex, int pageSize) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.QUERYEXTERNALBANKINFO);
		Map<String, String> map = new HashMap<String, String>();
		// 银行行号
		map.put(Tran.PAYEE_TOBANKCODE_REQ,
				LocalData.kBankListMap.get(accInKBank));
		// 银行名称
		map.put(Tran.PAYEE_BANKNAME_REQ, "");
		// 当前页
		map.put(Tran.PAYEE_CURRENTINDEX_REQ, currentIndex + "");
		// 每页显示条数
		map.put(Tran.PAYEE_PAGESIZE_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryExternalBankCallBack");
	}

	/**
	 * 转账银行开户行查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 * @return tokenId
	 */
	@SuppressWarnings("unchecked")
	public void queryExternalBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result.get(Tran.RECORD_NUMBER);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(ConstantGloble.LIST);
		TranDataCenter.getInstance().setExternalBankList(queryExternalBankList);
		// 跳转到下个列表页面
		Intent intent = new Intent(WritePayeeInfoActivity1.this,
				QueryExternalBankActivity1.class);
		//		Intent intent = new Intent(WritePayeeInfoActivity1.this,
		//		OrderMainActivity.class);


		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		intent.putExtra(Tran.PAYEE_BANKNAME_REQ, accInKBank);
		startActivityForResult(intent, ConstantGloble.PAYEE_OTHERBANK);
	}

	/**
	 * 查询开户行网点
	 */
	private void reqeustQueryAccountVestOrg() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNQUERY_ACCOUNT_VESTORG);
		Map<String, String> map = new HashMap<String, String>();
		// 银行行号
		map.put(Comm.ACCOUNTNUMBER, bocAccNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"reqeustQueryAccountVestOrgCallBack");
	}

	/**
	 * 查询开户网点返回
	 * 
	 * @param resultObj
	 */
	public void reqeustQueryAccountVestOrgCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
		.getResult();
		if (!StringUtil.isNullOrEmpty(result)) {
			accInInfoMap.put(Tran.ACCOUNTIBKNUM_RES,
					result.get(Tran.BANCS_BRANCHNUMBER));
			accInInfoMap.put(Tran.TRANS_BANKNAME_RES,
					result.get(Tran.BANCS_BRANCHNAME));
			TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
			
			
			if (isfrommanage) {
				// 继续调用新增收款人接口
				// 调用根据账户查询类型接口
				requestPsnQueryActTypeByActNum();
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				setResult(102);// 行内转账结果码
				finish();
			}

		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// TODO 提示待确认
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		if (FLAG == BANKIN) {
			requestPsnTransBocAddPayee(tokenId);
		} else if (FLAG == BANKOTHER) {
			requestPsnTransNationalAddPayee(tokenId);
		}

	}

	/**
	 * 行内保存常用收款人
	 */
	private void requestPsnQueryActTypeByActNum() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNQUERYACTTYPEBYACTNUM_API);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.QUERYBYNUM_ACCOUNTNUMBER_REQ, bocAccNum);
		map.put(Tran.QUERYBYNUM_TONAME_REQ, payeeNameEt.getText().toString()
				.trim());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnQueryActTypeByActNumCallBack");
	}

	public void requestPsnQueryActTypeByActNumCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		payeeBankNum = (String) map.get(Tran.QUERYBYNUM_IBKNUMBER_RES);
		toAccountType = (String) map.get(Tran.QUERYBYNUM_ACCOUNTTYPE_RES);
		requestCommConversationId();
		
		
		
	}

	/**
	 * 行内保存常用收款人
	 */
	private void requestPsnTransBocAddPayee(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNTRANSBOCADDPAYEE_INTERFACE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.BOCADDPAYEE_PAYEENAME_TOACCOUNTID_REQ, bocAccNum);
		map.put(Tran.BOCADDPAYEE_PAYEENAME_REQ, payeeNameEt.getText()
				.toString().trim());
		map.put(Tran.BOCADDPAYEE_PAYEEBANKNUM_REQ, payeeBankNum);
		map.put(Tran.BOCADDPAYEE_TOACCOUNTTYPE_REQ, toAccountType);
		map.put(Tran.BOCADDPAYEE_PAYEEMOBILE_REQ, payeeMobileEt.getText()
				.toString().trim());
		map.put(Tran.BOCADDPAYEE_TOKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransBocAddPayeeCallBack");
	}

	public void requestPsnTransBocAddPayeeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();	
//		CustomDialog.toastShow(this,
//				this.getString(R.string.new_add_payee_success));
//		setResult(105);
		Intent intent =new Intent(WritePayeeInfoActivity1.this, AddPayeeSuccess.class);
		intent.putExtra(Tran.BOCADDPAYEE_PAYEENAME_REQ, payeeNameEt.getText().toString().trim());
		intent.putExtra(Tran.SAVEEBPS_PAYEEACTNO_REQ, bocAccNum);
		intent.putExtra(Tran.SAVEEBPS_PAYEEMOBILE_REQ,payeeMobileEt.getText().toString().trim());
		intent.putExtra(Tran.ADD_NEW_PAYEE_ORG_NAME, "");
		intent.putExtra(Tran.DIR_ADDPAYEE_BANKNAME_RES, "");
		startActivityForResult(intent, 1);
//		finish();
	}

	/**
	 * 跨行保存常用收款人
	 */
	private void requestPsnTransNationalAddPayee(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		if (!rb_shishi.isChecked()) {
			// 普通转账
			biiRequestBody.setMethod(Tran.PSNTRANS_NATIONAL_ADDPAYEE);
			map.put(Tran.BOCADDPAYEE_PAYEENAME_TOACCOUNTID_REQ, bocAccNum);
			map.put(Tran.BOCADDPAYEE_PAYEENAME_REQ, payeeNameEt.getText()
					.toString().trim());
			map.put(Tran.TRANS_CNAPSCODE_RES, orgNameCnapsCode);
			map.put(Tran.TRANS_BANKNAME_RES, accInKBank);
			map.put(Tran.NATIONALADDPAYEE_TOORGNAME_REQ, toOrgName);
			map.put(Tran.BOCADDPAYEE_PAYEEMOBILE_REQ,
					TranDataCenter.getInstance().getAccInInfoMap()
					.get(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ));
			map.put(Tran.BOCADDPAYEE_TOKEN_REQ, tokenId);
		} else {
			// 实时转账
			biiRequestBody.setMethod(Tran.PSNEBPSREALTIMEPAYMENTSAVEPAYEE_API);
			map.put(Tran.SAVEEBPS_PAYEEACTNO_REQ, bocAccNum);
			map.put(Tran.SAVEEBPS_PAYEENAME_REQ, payeeNameEt.getText()
					.toString().trim());
			map.put(Tran.SAVEEBPS_PAYEEORGNAME_REQ, toOrgName);
			map.put(Tran.SAVEEBPS_PAYEEBANKNAME_REQ, toOrgName);
			map.put(Tran.SAVEEBPS_PAYEECNAPS_REQ, orgNameCnapsCode);
			map.put(Tran.SAVEEBPS_PAYEEMOBILE_REQ,
					TranDataCenter.getInstance().getAccInInfoMap()
					.get(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ));
			map.put(Tran.SAVEEBPS_TOKEN_REQ, tokenId);
		}

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransNationalAddPayeeCallBack");
	}

	public void requestPsnTransNationalAddPayeeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
//		CustomDialog.toastShow(this,
//				this.getString(R.string.new_add_payee_success));
//		
		Intent intent =new Intent(WritePayeeInfoActivity1.this, AddPayeeSuccess.class);
		intent.putExtra(Tran.BOCADDPAYEE_PAYEENAME_REQ, payeeNameEt.getText().toString().trim());
		intent.putExtra(Tran.SAVEEBPS_PAYEEACTNO_REQ, bocAccNum);
		intent.putExtra(Tran.SAVEEBPS_PAYEEMOBILE_REQ,payeeMobileEt.getText().toString().trim());
		if(!rb_shishi.isChecked()){
			intent.putExtra(Tran.ADD_NEW_PAYEE_ORG_NAME, toOrgName);
			intent.putExtra(Tran.DIR_ADDPAYEE_BANKNAME_RES, accInKBank);
		}else{
			intent.putExtra(Tran.ADD_NEW_PAYEE_ORG_NAME, "");
			intent.putExtra(Tran.DIR_ADDPAYEE_BANKNAME_RES, shishiBankName);
		}
		
		
		
		startActivityForResult(intent, 1);
//		setResult(105);
//		finish();
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码

			if (Tran.PSNQUERY_ACCOUNT_VESTORG.equals(biiResponseBody
					.getMethod())) {// 查询开户行网店
				// 代表返回数据异常
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								BiiHttpEngine.dissMissProgressDialog();
								showTimeOutDialog(biiError.getMessage());
							} else if (biiError.getCode().equals(
									"not.support.yet")) {// 非会话超时错误拦截
								if (psnVestOrgFlag == VEST_ADD_NEW_PAYEE) {
									BiiHttpEngine.dissMissProgressDialog();
									setResult(102);// 行内转账结果码
									finish();
								} else if (psnVestOrgFlag == VEST_TWO_DIMEN_SCAN) {
									BiiHttpEngine.dissMissProgressDialog();
									Intent intent = getIntent();
									intent.setClass(this,
											TwoDimenSacnResultActivity.class);
									startActivity(intent);
								} else if (psnVestOrgFlag == VEST_ADD_PAYEE) {
									requestPsnQueryActTypeByActNum();
								}
							} else {
								return super.httpRequestCallBackPre(resultObj);
							}
						}
					}
					// }
					return true;
				}
				return false;// 没有异常

			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常

		return super.httpRequestCallBackPre(resultObj);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_shishi:
			ll_bankname.setVisibility(View.GONE);
			containerV.setVisibility(View.GONE);
			container_bank.setVisibility(View.VISIBLE);
			messageTV.setVisibility(View.GONE);
			radioShishiPrompt.setVisibility(View.VISIBLE);
			break;
		case R.id.radio_common: 
			creditCardPrompt();
			ll_bankname.setVisibility(View.VISIBLE);
			radioShishiPrompt.setVisibility(View.GONE);
				containerV.setVisibility(View.VISIBLE);
				messageTV.setVisibility(View.VISIBLE);
			container_bank.setVisibility(View.GONE);
			break;

		case R.id.radio_zh_account:
			rb_shishi.setChecked(true);
			ll_trantype.setVisibility(View.GONE);
			container_bank.setVisibility(View.GONE);
			radioShishiPrompt.setVisibility(View.GONE);
			ll_bankname.setVisibility(View.GONE);
			containerV.setVisibility(View.GONE);
			break;
		case R.id.radio_other_account:
			ll_trantype.setVisibility(View.VISIBLE);
			container_bank.setVisibility(View.GONE);
			if(rb_shishi.isChecked()){
				//如果选择的是实时到账
				radioShishiPrompt.setVisibility(View.VISIBLE);
				container_bank.setVisibility(View.VISIBLE);
			}else{
				radioShishiPrompt.setVisibility(View.GONE);
				container_bank.setVisibility(View.GONE);
				ll_bankname.setVisibility(View.VISIBLE);
				containerV.setVisibility(View.VISIBLE);
			}
			break;	
		default:
			break;
		}
	}

	private void creditCardPrompt(){
		Map<String, Object> OutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String errortxt=getResources().getString(R.string.out_acctypegre_error);
		if(!StringUtil.isNullOrEmpty(OutInfoMap)){
			if(ConstantGloble.ZHONGYIN.equals(OutInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES))){
				BaseDroidApp.getInstanse().showMessageDialog(errortxt, new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissMessageDialog();
								rb_shishi.setChecked(true);
								
							}
						});
			}
		}
	}

	/**
	 * 实时转账——所属银行模糊查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void psnEbpsQueryAccountOfBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result
				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
		TranDataCenter.getInstance().setShishiBankList(queryExternalBankList);
		// 跳转到下个列表页面
		Intent intent = new Intent(WritePayeeInfoActivity1.this,
				OrderMainActivity.class);
		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		intent.putExtra(ISSHISHITYPE, true);
		startActivityForResult(intent, ConstantGloble.CHOOSE_SHISHIBANK);
	}


	@Override
	public void PsnOtherBankQueryForTransToAccountCallBack(Object resultObj) {
		super.PsnOtherBankQueryForTransToAccountCallBack(resultObj);

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result
				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
		for(int i = 0;i<queryExternalBankList.size(); i++){
			queryExternalBankList.get(i).put("flag", "Y");
		}
		TranDataCenter.getInstance().setShishiBankList(queryExternalBankList);
		//查询其他银行
		PsnOtherBankQueryForTransToAccount2("OTHER");
	}

	@Override
	public void PsnOtherBankQueryForTransToAccountCallBack2(Object resultObj) {
		super.PsnOtherBankQueryForTransToAccountCallBack2(resultObj);

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		//		String recordNumber = (String) result
		//				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);

		List<Map<String, String>> ShishiBankList = TranDataCenter.getInstance().getShishiBankList();
		ShishiBankList.addAll(queryExternalBankList);
		TranDataCenter.getInstance().setShishiBankList(ShishiBankList);
		BaseHttpEngine.dissMissProgressDialog();
		// 跳转到下个列表页面
		Intent intent = new Intent(WritePayeeInfoActivity1.this,
				OrderMainActivity.class);
		//		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		intent.putExtra(ISSHISHITYPE, true);
		startActivityForResult(intent, ConstantGloble.CHOOSE_SHISHIBANK);
	}

}
