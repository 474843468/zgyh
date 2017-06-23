package com.chinamworld.bocmbci.biz.finc.myfund;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 我的基金 短期理财到期日查询 详情页面
 * 
 * 
 * 
 */
public class FincFundDueDateDetailsActivity extends FincBaseActivity {

	/**短期理财到期日详情 主 view*/
	private View myFincView;

	/**短期理财产品界面控件*/
	private TextView dueFundCode;
	private TextView dueFundName;
	private TextView dueTypeColon;
	private TextView dueRealityShare;
	private TextView duetotalCount;
	private TextView dueRegisterDate;
	private TextView dueRedemptionDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();
		getViewValue();
		initOnClick();

	}

	/**初始化控件*/
	private void init(){
		setRightToMainHome();
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_due_date_query,
				null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_short_query));
		dueFundCode = (TextView) myFincView.findViewById(R.id.finc_fincCode);
		dueFundName = (TextView) myFincView.findViewById(R.id.finc_finc_name);
		dueTypeColon = (TextView) myFincView.findViewById(R.id.finc_feetype_colon);
		dueRealityShare = (TextView) myFincView.findViewById(R.id.finc_reality_share);
		duetotalCount = (TextView) myFincView.findViewById(R.id.finc_myfinc_totalCount);
		dueRegisterDate = (TextView) myFincView.findViewById(R.id.finc_myfinc_register_date);
		dueRedemptionDate = (TextView) myFincView.findViewById(R.id.finc_myfinc_redemption_date);

	}
	/**给控件赋值*/
	private void getViewValue(){
		int position = getIntent().getIntExtra("position", 0);
		String currency = getIntent().getStringExtra("currency");
		Map<String, Object> fundDueMap =fincControl.fincFundDueDateQuery.get(position);
		dueFundCode.setText((String) fundDueMap.get(Finc.FINC_FUNDCODE));
		dueFundName.setText((String) fundDueMap.get(Finc.FINC_FUNDNAME));
		dueTypeColon.setText
		(LocalData.fundfeeTypeCodeToStr.get((String) fundDueMap.get(Finc.FINC_FEETYPE)));
		dueRealityShare.setText(StringUtil.parseStringCodePattern
				(currency,((String) fundDueMap.get(Finc.FINC_MATURE_TOTAL)), 2));
		duetotalCount.setText(StringUtil.parseStringCodePattern
				(currency,((String) fundDueMap.get(Finc.FINC_FREE_QUTY)), 2));
		dueRegisterDate.setText((String) fundDueMap.get(Finc.FINC_QUTY_REGIST));
		dueRedemptionDate.setText((String) fundDueMap.get(Finc.FINC_ALLOW_REDEEM));
	}

	/**添加监听器*/
	private void initOnClick(){

	}

}
