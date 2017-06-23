package com.chinamworld.bocmbci.biz.tran.managetrans.managetranrecords;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class TransRecordTuihuiDetailActivity  extends ManageTransBaseActivity{

	
	private TextView  tv_payerActno;
	private TextView  tv_payeeActno;
	private TextView  tv_reexchangeAmount;
	private TextView  tv_reexchangeInfo;
	private TextView  tv_remittanceInfo;
	private TextView  tv_reexchangeDate;
	private TextView  tv_reexchangeState;
	
	private String feeCur;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_detail_tuihui));
		View view = mInflater.inflate(
				R.layout.tran_manage_records_tuihui_detail_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		feeCur=getIntent().getStringExtra(Tran.MANAGE_RECORDSDETAIL_FEECUR_RES);
		initview(view);
		displayTextView();
	}
	private void displayTextView() {
		Map<String, Object> detailMap = TranDataCenter.getInstance()
				.getRemitReturnInfo();
		String payerActno=(String)detailMap.get(Tran.MANAGE_REMITRETURN_PAYERACTNO_RES);
		String payeeActno=(String)detailMap.get(Tran.MANAGE_REMITRETURN_PAYEEACTNO_RES);
		String reexchangeAmount=(String)detailMap.get(Tran.MANAGE_REMITRETURN_REEXCHANGEAMOUNT_RES);
		String reexchangeInfo=(String)detailMap.get(Tran.MANAGE_REMITRETURN_REEXCHANGEINFO_RES);
		String remittanceInfo=(String)detailMap.get(Tran.MANAGE_REMITRETURN_REMITTANCEINFO_RES);
		String reexchangeDate=(String)detailMap.get(Tran.MANAGE_REMITRETURN_REEXCHANGEDATE_RES);
		String reexchangeState=(String)detailMap.get(Tran.MANAGE_REMITRETURN_REEXCHANGESTATE_RES);
		tv_payerActno.setText(StringUtil.getForSixForString(payerActno));	
		tv_payeeActno.setText(StringUtil.getForSixForString(payeeActno));
		tv_reexchangeAmount.setText(StringUtil.parseStringCodePattern(feeCur, reexchangeAmount, 2));		
		tv_reexchangeInfo.setText(reexchangeInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_reexchangeInfo);
		tv_remittanceInfo.setText(StringUtil.isNullChange(remittanceInfo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_remittanceInfo);
		tv_reexchangeDate.setText(reexchangeDate);
//		0表示未退汇
//		1表示已经退汇
		if("1".equals(reexchangeState)){
			tv_reexchangeState.setText(getResources().getString(R.string.tran_tuihui_detail_status_y));	
		}else{
			tv_reexchangeState.setText(getResources().getString(R.string.tran_tuihui_detail_status_n));
		}
		
		
	}
	private void initview(View v) {
		tv_payerActno=(TextView)v.findViewById(R.id.tv_payerActno);
		tv_payeeActno=(TextView)v.findViewById(R.id.tv_payeeActno);
		tv_reexchangeAmount=(TextView)v.findViewById(R.id.tv_reexchangeAmount);
		tv_reexchangeInfo=(TextView)v.findViewById(R.id.tv_reexchangeInfo);
		tv_remittanceInfo=(TextView)v.findViewById(R.id.tv_remittanceInfo);
		tv_reexchangeDate=(TextView)v.findViewById(R.id.tv_reexchangeDate);
		tv_reexchangeState=(TextView)v.findViewById(R.id.tv_reexchangeState);
		
	}
	
}
