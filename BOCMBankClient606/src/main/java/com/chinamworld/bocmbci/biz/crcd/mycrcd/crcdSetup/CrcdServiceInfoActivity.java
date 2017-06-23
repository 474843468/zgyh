package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 填写调整设置信息
 * 
 * @author huangyuchao
 * 
 */
public class CrcdServiceInfoActivity extends CrcdBaseActivity {

	private View view;
	TextView tv_cardNumber;
	/** 币种 */
	TextView finc_accNumber;
	/** 短信提示 */
	EditText finc_accId;
	EditText finc_fincName;
	RadioGroup rg_select;
	RadioButton rb_passandqianming;
	RadioButton rg_qianming;
	/** 签名 密码+签名---名称 */
	static String passType;
	private String passTypeCode = null;
	/** 币种代码 */
	private String codeCode = null;
	Button sureButton;

	static String msgVerifyLimit;
	static String posVerifyLimit;
	private View posView = null;
	/** 消费设置详情页面----pos金额 */
	private String posMoney = null;
	private String accountId = null;
	private String accountNumber = null;
	private String postriggeramount = null;
	private String smstriggeramount = null;
	private String verifymodeflg = null;
	private  String strCurrencyCode = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_service_setup));
		view = addView(R.layout.crcd_service_setup_info);
		codeCode = getIntent().getStringExtra(Crcd.CRCD_CURRENCYCODE);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		postriggeramount= getIntent().getStringExtra(Crcd.CRCD_POSLIMITAMOUNT);
//		postriggeramount = StringUtil.parseStringPattern(postriggeramount, 2);
		smstriggeramount= getIntent().getStringExtra(Crcd.CRCD_SHORTMSGLIMITAMOUNT);
//		smstriggeramount = StringUtil.parseStringPattern(smstriggeramount, 2);
		verifymodeflg= getIntent().getStringExtra(Crcd.CRCD_POSFLAG);

		
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		init();

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_service_tiaozheng_setup),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);

		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(accountNumber));

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_accNumber);
		finc_accId = (EditText) view.findViewById(R.id.finc_accId);
		finc_fincName = (EditText) view.findViewById(R.id.finc_fincName);
		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		rb_passandqianming = (RadioButton) view.findViewById(R.id.rb_passandqianming);
		rg_qianming = (RadioButton) view.findViewById(R.id.rg_qianming);

		strCurrencyCode = LocalData.Currency.get(codeCode);
		finc_accNumber.setText(strCurrencyCode);
		finc_accId.setText(smstriggeramount);
		posMoney = postriggeramount;
		finc_fincName.setText(postriggeramount);
		posView = findViewById(R.id.pos_layout);
		// 0：签名,1：密码+签名
		if ("1".equals(verifymodeflg)) {
			// 显示POS
			rb_passandqianming.setChecked(true);
			passTypeCode = ConstantGloble.CRCD_STATUS_ONE;
			passType = getString(R.string.mycrcd_service_passandqianming);
			posView.setVisibility(View.VISIBLE);
		} else if ("0".equals(verifymodeflg)) {
			// 隐藏POS
			rg_qianming.setChecked(true);
			passType = getString(R.string.mycrcd_service_qianming);
			passTypeCode = ConstantGloble.CRCD_STATUS_ZERO;
			posView.setVisibility(View.GONE);
		}
		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkMessage();
			}
		});

		rg_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				// 0：签名 1：密码+签名

				case R.id.rb_passandqianming:
					passType = getString(R.string.mycrcd_service_passandqianming);
					passTypeCode = ConstantGloble.CRCD_STATUS_ONE;
					posView.setVisibility(View.VISIBLE);
					break;
				case R.id.rg_qianming:
					passType = getString(R.string.mycrcd_service_qianming);
					passTypeCode = ConstantGloble.CRCD_STATUS_ZERO;
					posView.setVisibility(View.GONE);
					break;
				}
			}
		});
	}

	private void checkMessage() {
		msgVerifyLimit = finc_accId.getText().toString();
		if (msgVerifyLimit.contains(",")) {
			msgVerifyLimit = msgVerifyLimit.replace(",", "");
		}
		if (LocalData.codeNoNumber.contains(codeCode)) {
			// 没有小数点
			RegexpBean reb1 = new RegexpBean(CrcdServiceInfoActivity.this.getString(R.string.mycrcd_sms_chufa_money),
					msgVerifyLimit, "xiaoFeiSetialAmount");// crcd_emg_jyMoney
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			// 验证
			if (RegexpUtils.regexpDate(lists)) {
				if (ConstantGloble.IS_EBANK_0.equals(passTypeCode)) {
					posVerifyLimit = posMoney;
					BaseHttpEngine.showProgressDialog();
					requestGetSecurityFactor(psnSersecurityId);
				} else if (ConstantGloble.IS_EBANK_1.equals(passTypeCode)) {
					checkPOS();
				}

			}
		} else {
			// 有小数点
			RegexpBean reb1 = new RegexpBean(CrcdServiceInfoActivity.this.getString(R.string.mycrcd_sms_chufa_money),
					msgVerifyLimit, "xiaoFeiAmount");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			// 验证
			if (RegexpUtils.regexpDate(lists)) {
				if (ConstantGloble.IS_EBANK_0.equals(passTypeCode)) {
					posVerifyLimit = posMoney;
					BaseHttpEngine.showProgressDialog();
					requestGetSecurityFactor(psnSersecurityId);
				} else if (ConstantGloble.IS_EBANK_1.equals(passTypeCode)) {
					checkPOS();
				}
			}
		}

	}

	private void checkPOS() {
		posVerifyLimit = finc_fincName.getText().toString();
		if (posVerifyLimit.contains(",")) {
			posVerifyLimit = posVerifyLimit.replace(",", "");
		}
		if (LocalData.codeNoNumber.contains(codeCode)) {
			RegexpBean reb2 = new RegexpBean(CrcdServiceInfoActivity.this.getString(R.string.mycrcd_pos_xiaofei_money),
					posVerifyLimit, "xiaoFeiSetialAmount");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			// 验证
			if (RegexpUtils.regexpDate(lists)) {
				// 请求安全因子组合id
				BaseHttpEngine.showProgressDialog();
				requestGetSecurityFactor(psnSersecurityId);
			}
		} else {
			RegexpBean reb2 = new RegexpBean(CrcdServiceInfoActivity.this.getString(R.string.mycrcd_pos_xiaofei_money),
					posVerifyLimit, "xiaoFeiAmount");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			// 验证
			if (RegexpUtils.regexpDate(lists)) {
				// 请求安全因子组合id
				BaseHttpEngine.showProgressDialog();
				requestGetSecurityFactor(psnSersecurityId);
			}
		}

	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 请求信用卡服务设置定制信息确认
				crcdServiceInfoConfirm();
			}
		});
	}

	public void crcdServiceInfoConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_SERVICESETCONFIRM);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_SETMSGVERIFYLIMIT, msgVerifyLimit);
		map.put(Crcd.CRCD_SETPOSVERIFYLIMIT, posVerifyLimit);
		map.put(Crcd.CRCD_SETPOSVERIFYMODE, passTypeCode);
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "crcdServiceInfoConfirmCallBack");
	}

	public static Map<String, Object> returnMap;

	public void crcdServiceInfoConfirmCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();

		Intent it = new Intent(CrcdServiceInfoActivity.this, CrcdServiceInfoConfirmActivity.class);
		it.putExtra(Crcd.CRCD_SETPOSVERIFYMODE, passTypeCode);
		it.putExtra(ConstantGloble.CRCD_CODE, codeCode);// 币种代码
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
