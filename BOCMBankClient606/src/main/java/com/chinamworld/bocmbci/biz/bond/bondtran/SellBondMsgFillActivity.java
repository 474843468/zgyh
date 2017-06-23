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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;

/**
 * 卖债券信息填写页
 * 
 * @author panwe
 * 
 */
public class SellBondMsgFillActivity extends BondBaseActivity implements
		OnClickListener {

	/** 主布局 **/
	private View mainView;
	/** 交易面额 */
	private EditText edTranMoney;
	/** 可用面额 */
	private TextView tvAvalable;
	/** 下一步按钮 */
	private Button btnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_sell_msgfill, null);
		addView(mainView);
		BondDataCenter.getInstance().addActivity(this);
		setTitle(this.getString(R.string.bond_tran_title));
		init();
	}

	private void init() {
		TextView tvBondType = (TextView) mainView
				.findViewById(R.id.tv_bond_type);
		TextView tvBondName = (TextView) mainView
				.findViewById(R.id.tv_bond_name);
		TextView tvEncy = (TextView) mainView.findViewById(R.id.tv_bizhong);
		tvAvalable = (TextView) mainView.findViewById(R.id.tv_available);

		Map<String, Object> bondDetail = BondDataCenter.getInstance()
				.getMyBondDetailMap();
		tvBondType.setText(commSetText(BondDataCenter.bondType_cc
				.get(bondDetail.get(Bond.BOND_TYPE))));
		tvBondName.setText(commSetText((String) bondDetail
				.get(Bond.BOND_SHORTNAME)));
		tvAvalable.setText((String) bondDetail.get(Bond.MYBOND_AVAFACE));
		tvEncy.setText("人民币元");

		edTranMoney = (EditText) mainView.findViewById(R.id.et_tranmoney);

		btnNext = (Button) mainView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String tranAmount = edTranMoney.getText().toString();
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean reb1 = new RegexpBean(
				(getString(R.string.bond_tran_money)).substring(0, 4),
				tranAmount, "hundredMultiple");
		lists.add(reb1);
		if (RegexpUtils.regexpDate(lists)) {
			requestSellConfirm();
		}
	}

	/** 卖出一交易请求 **/
	private void requestSellConfirm() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_SELL_CONFIRM);
		Map<String, Object> dataMap = BondDataCenter.getInstance()
				.getMyBondDetailMap();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(Bond.BOND_CODE, dataMap.get(Bond.BOND_CODE));
		param.put(Bond.SELL_CONFIRM_TRANMONEY, edTranMoney.getText().toString());
		param.put(Bond.BOND_TYPE, dataMap.get(Bond.BOND_TYPE));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "sellConfirmCallBack");
	}

	/** 卖出预交易返回 */
	public void sellConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_tran_error));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(this, SellBondConfirmActivity.class);
		it.putExtra(TRANAMOUNT, edTranMoney.getText().toString());
		it.putExtra(TRANPRICE, (String) result.get(Bond.SELL_CONFIRM_TRANPRICE));
		it.putExtra(AMOUNT, (String) result.get(Bond.SELL_CONFIRM_TRANAMOUT));
		it.putExtra(SEQ, (String) result.get(Bond.SELL_COONFIRM_TRANSEQ));
		this.startActivity(it);
	}
}
