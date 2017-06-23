package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.servOpen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceListActivity;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 电子支付-阅读协议页
 * 
 */
public class ServOpenAgreementActivity extends EPayBaseActivity {

	private String tag = "EPayServOpenAgreementActivity";

	private View epayServiceOpenAgreement;

	private TextView tv_first_part;

	private Button bt_agree;
	private Button bt_cancel;
	
	private PubHttpObserver httpObserver;
	private Context bomTransContext;

	private int isQccRedirct = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_BOM);
		bomTransContext = TransContext.getBomContext();
		getTransData();
		epayServiceOpenAgreement = LayoutInflater.from(this).inflate(R.layout.epay_bom_service_open_agreement, null);
		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(epayServiceOpenAgreement);
		super.onCreate(savedInstanceState);
		// // 初始化导航条
		// EpayPubUtil.initStepBar(this, 1, new String[] { "阅读协议", "选择账户", "填写信息" });
		initCurPage();
	}

	private void initCurPage() {
		tv_first_part = (TextView) epayServiceOpenAgreement.findViewById(R.id.tv_first_part);
		tv_first_part.setText(EpayUtil.getLoginInfo("customerName"));
		bt_cancel = (Button) epayServiceOpenAgreement.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		bt_agree = (Button) epayServiceOpenAgreement.findViewById(R.id.bt_agree);
		bt_agree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				switch (isQccRedirct) {
				case 1 :
					Intent intent = new Intent();
					intent.setClass(ServOpenAgreementActivity.this, ServOpenMsgInputActivity.class);
					startActivityForResult(intent, 0);
					break;
				default :
					BiiHttpEngine.showProgressDialog();
					httpObserver.req_queryAllAccount(EpayUtil.getAccTypeList(), "queryAllAccountCallback");
					break;
				}

			}
		});
	}
	
	/**
	 * 查询账户回调方法
	 * @param resultObj
	 */
	public void queryAllAccountCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		List<Object> resultList = EpayUtil.getList(result);
		
		BiiHttpEngine.dissMissProgressDialog();
		if(resultList.isEmpty()) {
			
//			StringBuffer sb = new StringBuffer("没有可供开通电子支付的账户!是否立即关联新账户？\n支持开通电子支付功能的卡类型有：");
			StringBuffer sb = new StringBuffer(getText(R.string.no_open_electronic_payment_is_correlation).toString());
//			String temp = "";
//			for(Object accTypeStr : EpayUtil.getAccTypeList()) {
//				String accTypeName = LocalData.AccountType.get(accTypeStr);
//				if(!temp.equals(accTypeName)) {
//					temp = accTypeName;
//					sb.append("“").append(accTypeName).append("”、");
//				}
//			}
			
			BaseDroidApp.getInstanse().showErrorDialog(null , /*sb.substring(0, sb.length() - 1)*/sb.toString(), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					
					switch (PublicTools.getInt(v.getTag(), 0)) {
					case CustomDialog.TAG_SURE:
//						goRelevanceAccount();
						finish();
						break;
					case CustomDialog.TAG_CANCLE : 
						finish();
						break;
					}
				}
			});
			return;
		}
		
		bomTransContext.setData(PubConstants.CONTEXT_FIELD_ALL_ACCLIST, resultList);
		Intent intent = new Intent();
		intent.setClass(ServOpenAgreementActivity.this, ServOpenAccountSelectActivity.class);
		startActivityForResult(intent, 0);
	}
	

	private void getTransData() {
		Intent intent = getIntent();
		isQccRedirct = intent.getIntExtra("excuteType", 0);
		switch (isQccRedirct) {
		case 0:
			return;
		case 1:
			// Bundle bd = intent.getBundleExtra("qccRedirct");
			// Object selectedAcc = bd.get("selectedAcc");
			List<Object> selectedAccList = new ArrayList<Object>();
			// selectedAccList.add(selectedAcc);
			Map<String, Object> map = VirtualBCServiceListActivity.getBankSetupMap();
			selectedAccList.add(map);

			TransContext.getBomContext().setData("excuteType", isQccRedirct);
			TransContext.getBomContext().setData(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST, selectedAccList);
			break;
		default:
			break;
		}
	}
}
