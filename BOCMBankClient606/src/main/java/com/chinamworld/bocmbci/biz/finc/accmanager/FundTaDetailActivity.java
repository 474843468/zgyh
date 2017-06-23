package com.chinamworld.bocmbci.biz.finc.accmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.List;
import java.util.Map;

/**
 *
 * 基金Ta账户详情 页面
 *
 * @author xyl
 *
 */
public class FundTaDetailActivity extends FincBaseActivity {
	private static final String TAG = "FincFundTaSettingConfirmActivity";
	/** 注册登记基金公司,基金TA账户,账户状态,是否持仓,是否有在途交易 */
	private TextView taCompanyTv, taAccNumTv, taAccStateTv, isPostionTv,
			isTransTv;
	/** 注册登记基金公司,基金TA账户,账户状态,是否持仓,是否有在途交易 */
	private String taCompany, taCompanyCode, taAccNum, taAccState, isPostion,
			isTrans;
	/** 取消关联按钮,销户按钮 */
	private Button conserRelationBtn, disChargeBtn;
	private View btnView;

	private int flag;
	/** 取消关联,销户 */
	private static final int CONSERNRELATION = 1, DISCHARGE = 2;
	private boolean changeBtnView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		fincControl.addActivit(this);
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		switch (flag) {
		case CONSERNRELATION:
			consernFundTaAccRelation(taAccNum,
					Finc.C_FUNDTA_CONSERRELATION_TRANSTYPE, taCompanyCode,
					tokenId);
			break;
		case DISCHARGE:
			consernFundTaAccRelation(taAccNum,
					Finc.C_FUNDTA_DISCHARGE_TRANSTYPE, taCompanyCode, tokenId);
			break;

		default:
			break;
		}

	}

	@Override
	public void consernFundTaAccRelationCallback(Object resultObj) {
		super.consernFundTaAccRelationCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// List<Map<String, String>> resultList = (List<Map<String, String>>)
		// biiResponseBody
		// .getResult();
		StringBuffer sb = new StringBuffer();
		sb.append(getString(R.string.finc_fundta_consernAndedisCharge_info1));
		switch (flag) {
		case CONSERNRELATION:
			sb.append(conserRelationBtn.getText().toString());
			break;
		case DISCHARGE:
			sb.append(disChargeBtn.getText().toString());
			break;
		}
		sb.append(getString(R.string.finc_fundta_consernAndedisCharge_info2));
		BaseDroidApp.getInstanse().showMessageDialog(sb.toString(),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FundTaDetailActivity.this.setResult(RESULT_OK);
						FundTaDetailActivity.this.finish();
					}
				});
	}

	/**
	 * 初始化布局
	 *
	 * @Author xyl
	 */
	private void initView() {
		View childview = mainInflater
				.inflate(R.layout.finc_fundta_detail, null);
		tabcontent.addView(childview);
		setTitle(R.string.fincn_accmanner_fundTaacc);
		disChargeBtn = (Button) findViewById(R.id.finc_fundta_discharge_btn);
		conserRelationBtn = (Button) findViewById(R.id.finc_fundta_cancelrelation_btn);
		taCompanyTv = (TextView) findViewById(R.id.finc_setfundcompany_Tv);
		taAccNumTv = (TextView) findViewById(R.id.finc_fundtaacc_tv);
		taAccStateTv = (TextView) findViewById(R.id.acc_state_tv);
		isPostionTv = (TextView) findViewById(R.id.fincn_ifposition_tv);
		isTransTv = (TextView) findViewById(R.id.fincn_ifTrans_tv);
		btnView = findViewById(R.id.btn_layout);
		ViewUtils.initBtnParamsTwoLeft(conserRelationBtn, this);
		ViewUtils.initBtnParamsTwoRight(disChargeBtn, this);
	}

	private void init() {
		initView();
		initListenner();
		initData();


	}

	private void initListenner() {
		conserRelationBtn.setOnClickListener(this);
		disChargeBtn.setOnClickListener(this);
	}

	private void initData() {
		Map<String, Object> map = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap().get(Finc.D_FUNDTADAIL);
		taCompany = (String) map.get(Finc.QUERYTAACCDETAILLIST_FUNDREGNAME);
		taCompanyCode = (String) map.get(Finc.QUERYTAACCDETAILLIST_FUNDREGCODE);
		taAccNum = (String) map.get(Finc.QUERYTAACCDETAILLIST_TAACCOUNTNO);
		taAccState = (String) map.get(Finc.QUERYTAACCDETAILLIST_ACCOUNTSTATUS);
		isPostion = (String) map.get(Finc.QUERYTAACCDETAILLIST_ISPOSITION);
		isTrans = (String) map.get(Finc.QUERYTAACCDETAILLIST_ISTRANS);

		taCompanyTv.setText(taCompany);
		taAccNumTv.setText(taAccNum);
		taAccStateTv.setText(LocalData.fincTaAccTypeMap.get(taAccState));
		isPostionTv.setText(getisTransOrisPosition(isPostion));
		isTransTv.setText(getisTransOrisPosition(isTrans));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				taCompanyTv);
		changeBtnView(taAccState, isPostion, isTrans);
	}

	private String getisTransOrisPosition(String str) {
		if (StringUtil.parseStrToBoolean(str)) {
			return getString(R.string.yes);
		} else {
			return getString(R.string.no);
		}

	}

	/**
	 * 根据 账户状态 是否有持仓 是否有在途交易 判断按钮是否显示
	 *
	 * @param taAccState
	 *            Ta账户状态
	 * @param isPostion
	 *            是否有持仓
	 * @param isTrans
	 *            是有在途交易
	 * @return 按钮是否显示
	 */
	private void changeBtnView(String taAccState, String isPostion,
			String isTrans) {
//		账户状态正常 是否持仓 N 是否在途交易 N 都满足显示
		if(taAccState.equals("00")&&isPostion.equals("N")&&isTrans.equals("N")){
		}else{
			conserRelationBtn.setVisibility(View.GONE);//取消关联
			disChargeBtn.setVisibility(View.GONE);//注销
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_fundta_discharge_btn:// 销户
			flag = DISCHARGE;
			BaseDroidApp.getInstanse().showErrorDialog(
					getString(R.string.finc_fundta_disCharge_info),
					R.string.cancle, R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								requestCommConversationId();
								BaseHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
			break;
		case R.id.finc_fundta_cancelrelation_btn:// 取消关联
			flag = CONSERNRELATION;
			BaseDroidApp.getInstanse().showErrorDialog(
					getString(R.string.finc_fundta_consernRelation_info),
					R.string.cancle, R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								requestCommConversationId();
								BaseHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
	}
}
