package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金定期定额申请查询 详情页面
 * 
 * @author 浮动盈亏
 * 
 */
public class FundFdykDetailsActivity extends FincBaseActivity {
	private static final String TAG = "FundFdykDetailsActivity";
	private TextView fundNameTv;
	private TextView tradCodeTv;
	private Button confirmBtn;
	private String fundNameStr;
	private String fundCodeStr;
	private TextView tradCurrencyTv;
	private TextView startCostTv;
	private TextView hsAmountTv;
	private TextView trAmountTv;
	private TextView resultFloatTv;//已实现盈亏
	private TextView endFloatTv;//持仓盈亏
	private TextView middleFloatTv;//总盈亏
	private TextView endCostCostTv;
	private String tradCurrencyStr,cashFlagCode;
	private String startCostStr;
	private String endCostCostStr;
	private String hsAmountStr;
	private String trAmountStr;
	private String resultFloatStr;
	private String endFloatStr;
	private String middleFloatStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();

	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_fdyk_details, null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_title_fdyk);
		fundNameTv = (TextView) childView.findViewById(R.id.finc_fundname_tv);
		tradCodeTv = (TextView) childView.findViewById(R.id.finc_fundcode_tv);
		tradCurrencyTv = (TextView) childView.findViewById(R.id.finc_currency_tv);
		startCostTv = (TextView) childView.findViewById(R.id.finc_startCost_tv);
		endCostCostTv = (TextView) childView.findViewById(R.id.finc_endCost_tv);
		hsAmountTv = (TextView) childView.findViewById(R.id.finc_hsAmount_tv);
		trAmountTv = (TextView) childView.findViewById(R.id.finc_trAmount_tv);
		resultFloatTv = (TextView) childView.findViewById(R.id.finc_resultFloat_tv);
		endFloatTv = (TextView) childView.findViewById(R.id.finc_endFloat_tv);
		middleFloatTv = (TextView) childView.findViewById(R.id.finc_middleFloat_tv);
		confirmBtn = (Button) childView.findViewById(R.id.finc_confirm);
		confirmBtn.setOnClickListener(this);
		initRightBtnForMain();
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundNameTv);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Map<String,Object> map = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(Finc.D_FDYKMAP);
		fundNameStr = StringUtil.valueOf1((String)map.get(Finc.FINC_FUNDNAME));
		fundCodeStr =  StringUtil.valueOf1((String)map.get(Finc.FINC_FUNDCODE));
		tradCurrencyStr =  StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_CURCENY));
		cashFlagCode =  StringUtil.valueOf1((String)map.get(Finc.FINC_CASHFLAG));
		startCostStr = StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_STARTCOST));
		endCostCostStr = StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_ENDCOST));
		hsAmountStr =  StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_HSAMOUNT));
		trAmountStr =  StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_TRAMOUNT));
		resultFloatStr =  StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_RESULTFLOAT));
		endFloatStr =  StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_ENDFLOAT));
		middleFloatStr =  StringUtil.valueOf1((String)map.get(Finc.FINC_FLOATPROFITANDLOSS_MIDDLEFLOAT));

		fundNameTv.setText(fundNameStr);
		tradCodeTv.setText(fundCodeStr);
		tradCurrencyTv.setText(FincControl.fincCurrencyAndCashFlag(tradCurrencyStr, cashFlagCode));
		startCostTv.setText(startCostStr);
		endCostCostTv.setText(endCostCostStr);
		trAmountTv.setText(StringUtil.parseStringPattern(trAmountStr, 2));
		FincControl.setTextColor(trAmountTv, this);	
		hsAmountTv.setText(StringUtil.parseStringPattern(hsAmountStr,2));
		FincControl.setTextColor(hsAmountTv, this);	
		
		/**add by fsm “已实现盈亏”“持仓盈亏”“总盈亏”三个字段动态着色（负的绿色，正的红色，零的黑色）*/
		resultFloatTv.setText(resultFloatStr);
		FincControl.setTextColorAboutYK(resultFloatTv, this);
		endFloatTv.setText(endFloatStr);
		FincControl.setTextColorAboutYK(endFloatTv, this);
		middleFloatTv.setText(middleFloatStr);
		FincControl.setTextColorAboutYK(middleFloatTv, this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm:// 确定按钮
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	public void finish() {
		if (BaseDroidApp.getInstanse().getBizDataMap()//
				.containsKey(Finc.D_FDYKMAP)) {
			BaseDroidApp.getInstanse().getBizDataMap()
			.remove(Finc.D_FDYKMAP);
		}
		super.finish();
	}


}
