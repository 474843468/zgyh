package com.chinamworld.bocmbci.biz.quickOpen.quickopen;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.QuickOpen;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.quickOpen.QuickOpenDataCenter;
import com.chinamworld.bocmbci.biz.quickOpen.QuickOpenUtils;
import com.chinamworld.bocmbci.biz.quickOpen.StockThirdQuickOpenBaseActivity;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银国际证券开户确认页
 * 
 * @author Zhi
 */
public class StockThirdQuickOpenConfirmActivity extends StockThirdQuickOpenBaseActivity implements OnClickListener {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 下一步按钮 */
	private Button btnNext;
	/** 用户选择的卡信息 */
	private Map<String, Object> cardInfo;
	/** 随机数 */
	private String random;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.quick_open_comfirm);
		setTitle(R.string.quickOpen_title_open);
		cardInfo = QuickOpenDataCenter.getInstance().getListCommonQueryAllChinaBankAccount().get(getIntent().getIntExtra(POSITION, 0));
		initView();
	}
	
	private void initView() {
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setText(getResources().getString(R.string.next));
		btnNext.setOnClickListener(this);
		
		Map<String, Object> map = QuickOpenDataCenter.getInstance().getMapStockThirdQueryCustInfoExtend();
		((TextView) findViewById(R.id.tv_name)).setText((String) map.get(QuickOpen.CUSTNAME));
		((TextView) findViewById(R.id.tv_gender)).setText(QuickOpenUtils.getGender((String) map.get(QuickOpen.GENDER)));
		((TextView) findViewById(R.id.tv_identitytype)).setText("身份证");
		((TextView) findViewById(R.id.tv_identityactnum)).setText((String) map.get(QuickOpen.IDENTIFYNUMBER));
		((TextView) findViewById(R.id.tv_mobile)).setText((String) map.get(QuickOpen.MOBILENO));
		((TextView) findViewById(R.id.tv_acc)).setText(StringUtil.getForSixForString((String) cardInfo.get(Comm.ACCOUNTNUMBER)));
		
		findViewById(R.id.layout_sms).setVisibility(View.GONE);
		findViewById(R.id.layout_otp).setVisibility(View.GONE);
		findViewById(R.id.sip_usbkey).setVisibility(View.GONE);
		findViewById(R.id.tv_submitHint).setVisibility(View.GONE);
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {
		BaseHttpEngine.showProgressDialog();
		requestGetSecurityFactor("PB185");
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ----------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
			}
		});
	};
	
	public void requestPSNGetRandomCallBack(Object resultObj) {
		random = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(random)) {
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(QuickOpen.STOCKACCOUNTNO, cardInfo.get(Comm.ACCOUNTNUMBER));
		params.put(QuickOpen.CUSTNAME, QuickOpenDataCenter.getInstance().getMapStockThirdQueryCustInfoExtend().get(QuickOpen.CUSTNAME));
		params.put(Safety.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		requestHttp(QuickOpen.PSNSTOCKTHIRDQUICKOPENPRE, "requestPsnStockThirdQuickOpenPreCallBack", params, true);
	}
	
	@SuppressWarnings("unchecked")
	public void requestPsnStockThirdQuickOpenPreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		QuickOpenDataCenter.getInstance().setMapStockThirdQuickOpenPre(result);
		startActivityForResult(new Intent(this, StockThirdQuickOpenSubmitActivity.class)
		.putExtra(RANDOM, random)
		.putExtra(POSITION, getIntent().getIntExtra(POSITION, 0)), 4);
	}
}
