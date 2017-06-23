package com.chinamworld.bocmbci.biz.finc.fundprice;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 登记基金它账户成功也米娜
 * @author xyl
 *
 */
public class FincFundTaSettingSuccessActivity  extends FincBaseActivity{
	private static final String TAG = "FincFundTaSettingSuccessActivity";
	/**
	 * 基金交易账户
	 */
	private TextView fundAccTextView;
	/***
	 * 基金它账户显示
	 */
	private TextView fundTaAccTextView;
	/**
	 * 选择的公司
	 */
	private TextView fundCompanyTextView;
	/**
	 * 用户输入的基金他账户
	 */
	private String fundTaAccStr;
	private String fundCompanyNameStr;
	/**
	 *  下一步按钮
	 */
	private Button confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		initData();
	}


	/**
	 * 初始化布局
	 *@Author xyl
	 */
	private void init(){
		View childview = mainInflater.inflate(R.layout.finc_fundtasetting_success,null);
		tabcontent.addView(childview);
		//title 和步骤条
		setTitle(R.string.finc_title_registfundTA);
		StepTitleUtils.getInstance().initTitldStep(this, fincControl.getStepsForRegistFundTa());
		StepTitleUtils.getInstance().setTitleStep(3);
		fundAccTextView= (TextView) childview.findViewById(R.id.finc_fundacc);
		
		///add by fsm
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundAccTextView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, 
				((TextView)findViewById(R.id.finc_fundcompany_textview_alert)));
				
		fundCompanyTextView = (TextView) childview.findViewById(R.id.finc_fundcompany_textview);
		fundTaAccTextView = (TextView) childview.findViewById(R.id.finc_fundtaacc_textview);
		confirm = (Button) childview.findViewById(R.id.finc_confirm);
		//基金账户显示
		if(!StringUtil.isNullOrEmpty(fincControl.invAccId)){
			fundAccTextView.setText(fincControl.invAccId);
		}
		confirm.setOnClickListener(this);
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
	}
	private void initData(){
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		fundCompanyNameStr = (String) extras.get(Finc.I_FUNDCOMPANYNAME);
		fundTaAccStr = extras.getString(Finc.I_FUNDTAACC);
		fundCompanyTextView.setText(fundCompanyNameStr);
		fundTaAccTextView.setText(fundTaAccStr);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundCompanyTextView);
//		if(!StringUtil.isNullOrEmpty(fincControl.accId)){
//			fundAccTextView.setText(StringUtil.getForSixForString(fincControl.accNum));
//		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm://确定
			setResult(RESULT_OK);  
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
