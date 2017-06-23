package com.chinamworld.bocmbci.biz.bond.bondtran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 卖债券信息确认页
 * 
 * @author panwe
 * 
 */
public class SellBondConfirmActivity extends BondBaseActivity {

	/** 主布局 */
	private View mainView;
	/** 债券名 */
	private TextView tvBondName;
	/** 债券类型 */
	private TextView tvBondType;
	/** 币种 */
	private TextView tvEncy;
	/** 交易面额 */
	private TextView tvTranMoney;
	/** 交易价格 */
	private TextView tvPrice;
	/** 交易金额 */
	private TextView tvAmount;
	/** 确定按钮 */
	private Button btnConfirm;
	/** 接受上一页面数据 */
	private String tranAmount;
	private String price;
	private String amount;
	private String querySeq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_sell_confirm, null);
		addView(mainView);
		setTitle(this.getString(R.string.bond_tran_title));
		initData();
		init();
	}
	
	private void initData(){
		Intent intent = getIntent();
		tranAmount = intent.getStringExtra(TRANAMOUNT);
		price = intent.getStringExtra(TRANPRICE);
		amount = intent.getStringExtra(AMOUNT);
		querySeq = intent.getStringExtra(SEQ);
	}

	private void init() {
		tvBondName = (TextView) mainView.findViewById(R.id.tv_bond_name);
		tvBondType = (TextView) mainView.findViewById(R.id.tv_bond_type);
		tvEncy = (TextView) mainView.findViewById(R.id.tv_bizhong);
		tvTranMoney = (TextView) mainView.findViewById(R.id.tv_money1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvTranMoney);
		tvPrice = (TextView) mainView.findViewById(R.id.tv_money2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				 tvPrice);
		tvAmount = (TextView) mainView.findViewById(R.id.tv_money3);

		btnConfirm = (Button) mainView.findViewById(R.id.btnConfirm);

		setData();
	}

	private void setData() {
		Map<String, Object> dataMap = BondDataCenter.getInstance()
				.getMyBondDetailMap();
		tvBondType.setText(commSetText(BondDataCenter.bondType_cc.get(dataMap.get(Bond.BOND_TYPE))));
		tvBondName.setText(commSetText((String) dataMap.get(Bond.MYBOND_SHORTNAME)));
		tvTranMoney.setText((tranAmount));

		String str_tem=StringUtil.parseStringPattern(price, 2);
		String str_tem_1=str_tem+"人民币元/每100元面额";
		SpannableStringBuilder str_span = new SpannableStringBuilder(str_tem_1);
		if (!StringUtil.isNullOrEmpty(str_tem)){
			str_span.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.fonts_pink)),0,str_tem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			str_span.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.fonts_black)),str_tem.length(),str_tem_1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		tvPrice.setText(str_span);
		tvAmount.setText(StringUtil.parseStringPattern(amount,2));
		tvEncy.setText("人民币元");
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 获取会话id
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}
	
	/** conversationid回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		// 发送获取token请求
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	/*** token返回   */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestSellResult(token);
	}

	/**
	 * 卖出结果请求
	 * 
	 * @param token
	 */
	private void requestSellResult(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_SELL_RESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> dataMap = BondDataCenter.getInstance()
				.getMyBondDetailMap();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(Bond.BOND_BUYRESULT_TOKEN, token);
		param.put(Bond.BOND_CODE, dataMap.get(Bond.BOND_CODE));
		param.put(Bond.SELL_CONFIRM_TRANAMOUT, amount);
		param.put(Bond.SELL_CONFIRM_TRANPRICE, price);
		param.put(Bond.SELL_CONFIRM_TRANMONEY, tranAmount);
		param.put(Bond.SELL_COONFIRM_TRANSEQ, querySeq);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "sellResultCallBack");
	}

	/** 卖出结果返回 */
	public void sellResultCallBack(Object resultObj) {
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
		Intent it = new Intent(SellBondConfirmActivity.this, SellBondResultActivity.class);
		it.putExtra(TRANAMOUNT, tranAmount);
		it.putExtra(AMOUNT, (String)result.get(Bond.SELL_CONFIRM_TRANAMOUT));
		it.putExtra(TRANPRICE, (String)result.get(Bond.SELL_CONFIRM_TRANPRICE));
		it.putExtra(SEQ, (String)result.get(Bond.SELL_RESULT_TRANID));
		SellBondConfirmActivity.this.startActivity(it);
		SellBondConfirmActivity.this.finish();
	}
}
