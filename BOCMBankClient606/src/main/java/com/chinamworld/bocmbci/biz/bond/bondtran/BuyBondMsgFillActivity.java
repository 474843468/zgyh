package com.chinamworld.bocmbci.biz.bond.bondtran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 买债券信息填写页
 * 
 * @author panwe
 * 
 */
public class BuyBondMsgFillActivity extends BondBaseActivity {

	/** 主布局 **/
	private View mainView;
	/** 债券名称 */
	private Spinner spBondName;
	/** 债券类型 */
//	private RadioButton radBtn1;
	/** 币种 */
	private TextView tvEney;
	/** 交易面额 */
	private EditText edTranPrice;
	/** 当前显示条目 */
	private int mPostion;
	/** 交易类型 */
	private String transType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_buy_msgfill, null);
		addView(mainView);
		BondDataCenter.getInstance().addActivity(this);
		setTitle(this.getString(R.string.bond_tran_title));
		init();
	}

	private void init() {
		mPostion = getIntent().getIntExtra(POSITION, 0);
		transType = getIntent().getStringExtra(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE);
		spBondName = (Spinner) mainView.findViewById(R.id.sp_bondName);
//		radBtn1 = (RadioButton) mainView.findViewById(R.id.rabtn1);
		tvEney = (TextView) mainView.findViewById(R.id.tv_bizhong);
		edTranPrice = (EditText) mainView.findViewById(R.id.et_tranmoney);
		tvEney.setText("人民币元");
		spInit();
		Button btnNext = (Button) mainView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String price = edTranPrice.getText().toString();
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean reb1 = new RegexpBean(
						(getString(R.string.bond_tran_money)).substring(0, 4),
						price, "hundredMultiple");
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					// 获取会话id
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}
			}
		});
	}

	/** 初始化债券名称下拉框 */
	private void spInit() {
		// 债券
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter<ArrayList<String>> bondNameAdapter = new ArrayAdapter(
				this, R.layout.spinner_item, BondDataCenter.getInstance()
						.getNameList());
		bondNameAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spBondName.setAdapter(bondNameAdapter);
		spBondName.setSelection(mPostion);
	}

	/** conversationid回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		// 发送安全因子请求
		requestGetSecurityFactor(Bond.SERVICECODE_SELL);
	}

	/*** 安全因子返回结果 ***/
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 请求预交易
						requestBondBuyConfirm();
					}
				});
	}

	/** 买入预交易 */
	private void requestBondBuyConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BUY_CONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(Bond.BUY_CONFIRM_COMBIN, BaseDroidApp.getInstanse()
				.getSecurityChoosed());
		param.put(Bond.BOND_CODE, BondDataCenter.getInstance().getBondList()
				.get(spBondName.getSelectedItemPosition()).get(Bond.BOND_CODE));
		param.put(Bond.SELL_CONFIRM_TRANMONEY, edTranPrice.getText().toString());
		param.put(Bond.BOND_RESULT_BONDACC, BondDataCenter.getInstance()
				.getAccMap().get(Bond.INVESTACCOUNT));
		param.put(Bond.BONDBUY_RESULT_ACCID, BondDataCenter.getInstance()
				.getAccMap().get(Bond.ACCOUNTID));
		param.put(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE, transType);
		param.put(Bond.BOND_TYPE, BondDataCenter.bondType_re.get(1));
		param.put(Bond.BONDBUY_RESULT_DATE, QueryDateUtils
				.getcurrentDate(BondDataCenter.getInstance().getSysTime()));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "bondBuyConfirmCallBack");
	}

	/** 买入预交易返回 */
	public void bondBuyConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_tran_error));
			return;
		}
		BondDataCenter.getInstance().setBuyConfirmResult(result);
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(this, BuyBondConfirmActivity.class);
		it.putExtra(POSITION, spBondName.getSelectedItemPosition());
		it.putExtra(TRANAMOUNT, edTranPrice.getText().toString());
		it.putExtra(TRANTYPE, transType);
		startActivity(it);
	}
}
