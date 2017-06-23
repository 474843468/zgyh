package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 分期查询后的详情页
 * 
 * @author huangyuchao
 * 
 */
public class CrcdDividedDetailListDetail extends CrcdBaseActivity {

	/** 分期查询后的详情 */
	private View view;

	private TextView finc_accNumber, finc_fenqidate, finc_miaoshu, finc_bizhong, finc_fincName, finc_qinum,
			finc_fenqitype, finc_firstamount, finc_nextmoney, finc_hasrumoney, finc_remiannonum, finc_remiannomoney,
			finc_nextdate;

	private Button sureButton;
	protected static Map<String, Object> detailMap;

	String instalmentPlan;

	static String strChargeMode;
	static String strCurrCode;
	String conVersationId;
	private TextView moneyText = null;
	private View buttonView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_history_queryandjq));
		view = addView(R.layout.crcd_divided_info_detail);
		init();
	}

	/** 初始化界面 */
	public void init() {
		detailMap = CrcdDividedHistoryQueryDetail.dividedMap;
		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqidate);
		finc_miaoshu = (TextView) view.findViewById(R.id.finc_miaoshus);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_miaoshu);
		finc_bizhong = (TextView) view.findViewById(R.id.finc_bizhong);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_bizhong);
		finc_fincName = (TextView) view.findViewById(R.id.finc_fincName);
		finc_qinum = (TextView) view.findViewById(R.id.finc_qinum);
		finc_fenqitype = (TextView) view.findViewById(R.id.finc_fenqitype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqitype);
		finc_firstamount = (TextView) view.findViewById(R.id.finc_firstamount);
		finc_nextmoney = (TextView) view.findViewById(R.id.finc_nextmoney);
		finc_hasrumoney = (TextView) view.findViewById(R.id.finc_hasrumoney);
		finc_remiannonum = (TextView) view.findViewById(R.id.finc_remiannonum);
		finc_remiannomoney = (TextView) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) view.findViewById(R.id.finc_nextdate);
		finc_accNumber.setText(StringUtil.getForSixForString(CrcdDividedHistoryQueryList.accountNumber));
		finc_fenqidate.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMTDATE)));
		finc_miaoshu.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMEDESCRIPTION)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_miaoshu);
		strCurrCode = LocalData.Currency.get(String.valueOf(detailMap.get(Crcd.CRCD_CURRENCY)));
		moneyText = (TextView) findViewById(R.id.money);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);

		finc_bizhong.setText(strCurrCode);
		finc_fincName.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_AMOUNT)), 2));
		finc_qinum.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMTCOUNT)));

		strChargeMode = LocalData.chargeModeType.get(String.valueOf(detailMap.get(Crcd.CRCD_CHARGEMODE)));

		finc_fenqitype.setText(strChargeMode);
		finc_firstamount.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_FIRSTINAMOUNT)),
				2));
		finc_nextmoney
				.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_NEXTTIMEAMOUNT)), 2));
		finc_hasrumoney
				.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_INCOMEAMOUNT)), 2));
		finc_remiannonum.setText(String.valueOf(detailMap.get(Crcd.CRCD_RESTTIMECOUNT)));

		finc_remiannomoney.setText(StringUtil.parseStringPattern(
				String.valueOf(detailMap.get(Crcd.CRCD_RESTAMOUNT_RES)), 2));

		String nextincomedate = String.valueOf(detailMap.get(Crcd.CRCD_NEXTINCOMEDATE));

		finc_nextdate.setText(StringUtil.valueOf1(nextincomedate));
		instalmentPlan = String.valueOf(detailMap.get(Crcd.CRCD_INSTALMENTPLAN));
		buttonView = findViewById(R.id.button_layout);

		String accomplishDate = (String) detailMap.get(Crcd.CRCD_ACCOMPLISHDATE);
		String instmtFlag = (String) detailMap.get(Crcd.CRCD_INSTMTFLAG);
		if (StringUtil.isNull(accomplishDate)
				&& !StringUtil.isNull(instmtFlag)
				&& (ConstantGloble.CRCD_INSTMTFLAG_ONE.equals(instmtFlag) || ConstantGloble.CRCD_INSTMTFLAG_TWO
						.equals(instmtFlag))) {
			buttonView.setVisibility(View.VISIBLE);
		} else {
			buttonView.setVisibility(View.GONE);
		}

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conVersationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// 请求安全因子组合id
		requestGetSecurityFactor(psnDividedsecurityId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 账单开通或修改确认
				psnCrcdDividedPayAdvanceConfirm();
			}
		});
	}

	/** 提前结清入账确认 */
	public void psnCrcdDividedPayAdvanceConfirm() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conVersationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDIVIDEDPAYADVANCECONFIRM);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, CrcdDividedHistoryQueryList.accountId);
		map.put(Crcd.CRCD_INSTALMENTPLAN, instalmentPlan);
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdDividedPayAdvanceConfirmCallBack");
	}

	public static Map<String, Object> result;

	public void psnCrcdDividedPayAdvanceConfirmCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		result = (Map<String, Object>) biiResponseBody.getResult();
		Intent it = new Intent(CrcdDividedDetailListDetail.this, CrcdDividedAndPreviewConfirm.class);
		startActivity(it);
	}

}
