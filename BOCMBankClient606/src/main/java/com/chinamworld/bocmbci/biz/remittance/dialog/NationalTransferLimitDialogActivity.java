package com.chinamworld.bocmbci.biz.remittance.dialog;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 查询剩余额度弹出框
 * 
 * @author Zhi
 */
public class NationalTransferLimitDialogActivity extends BaseActivity {

	private RelativeLayout rl_bank;
	/**折合成美元*/
	private TextView tvConvertShow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		rl_bank.removeAllViews();
		rl_bank.addView(initView());
	}
	
	private View initView() {
		View view = LayoutInflater.from(this).inflate(R.layout.remittance_info_input_query_transfer_limit_dialog, null);
		Map<String, Object> map = RemittanceDataCenter.getInstance().getMapPsnQueryNationalTransferLimit();
		((TextView) view.findViewById(R.id.tv_currency)).setText("美元");;
//		((TextView) view.findViewById(R.id.tv_currency)).setText(LocalData.Currency.get(getIntent().getStringExtra("CURRENCY")));
		((TextView) view.findViewById(R.id.tv_totalLimit)).setText(StringUtil.parseStringPattern((String) map.get(Remittance.TOTALLIMIT), 2));
		((TextView) view.findViewById(R.id.tv_usedLimit)).setText(StringUtil.parseStringPattern((String) map.get(Remittance.USEDLIMIT), 2));
		((TextView) view.findViewById(R.id.tv_remainingLimit)).setText(StringUtil.parseStringPattern((String) map.get(Remittance.REMAININGLIMIT), 2));
//		((TextView) view.findViewById(R.id.tv_convertRemainLimit)).setText("折合" + StringUtil.parseStringCodePattern(getIntent().getStringExtra("CURRENCY"), (String) map.get(Remittance.CONVERTREMAINLIMIT), 2) + LocalData.Currency.get(getIntent().getStringExtra("CURRENCY")));
		tvConvertShow=(TextView) view.findViewById(R.id.tv_convertRemainLimit);
		if(!getIntent().getStringExtra("CURRENCY").equals("014")){
			tvConvertShow.setVisibility(View.VISIBLE);
			tvConvertShow.setText("折合"+StringUtil.parseStringCodePattern(getIntent().getStringExtra("CURRENCY"),(String)map.
					get(Remittance.CONVERTREMAINLIMIT) , 2)+LocalData.Currency.get(getIntent().getStringExtra("CURRENCY")));
			
		}
		view.findViewById(R.id.btnClose).setOnClickListener(closeListener);
		view.findViewById(R.id.img_exit_accdetail).setOnClickListener(closeListener);
		
		return view;
	}
	
	private OnClickListener closeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
