package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 我的基金 分红方式 填写页面
 * 
 * @author 宁焰红
 * 
 */
public class MyFincBoundsTypeSubmitActivity extends FincBaseActivity {
	private final String TAG = "MyFincBoundsTypeSubmitActivity";
	/** 分红方式view */
	private View myFincView = null;
	/** 基金代码 */
	private TextView fincCodeText = null;
	/** 基金名称 */
	private TextView fincNameText = null;
	/** 分红方式 */
	private RadioGroup radioGroup = null;
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	/**分红方式*/
	private TextView bonusTypeText=null;
	/** 分红方式代码 0: 默认1: 现金2: 红利再投资*/
	private String foundTypeCode = "1";
	/**现金分红*/
	private RadioButton type1RadioButton=null;
	/** 红利再投资*/
	private RadioButton type2RadioButton=null;
	/**下一步*/
	private Button nextButton=null;
	
	private String oldTypeCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initOnClick();initRightBtnForMain();
		
	}

	private void init() {
		myFincView = mainInflater.inflate(
				R.layout.finc_myfinc_bondstype_submit, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_bound));
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		radioGroup = (RadioGroup) findViewById(R.id.finc_bounds_type);
		type1RadioButton=(RadioButton) findViewById(R.id.finc_myfinc_bounds1);
		type2RadioButton=(RadioButton) findViewById(R.id.finc_myfinc_bounds2);
		bonusTypeText=(TextView) findViewById(R.id.finc_boundsType);
		nextButton=(Button) findViewById(R.id.nextButton);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.finc_myfinc_foundOne),
						this.getResources().getString(R.string.finc_top_two),
						this.getResources().getString(R.string.finc_myfinc_acc3) });
		StepTitleUtils.getInstance().setTitleStep(1);
	}

	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			foundCode = intent.getStringExtra(Finc.FINC_FUNDCODE_REQ);
			foundName = intent.getStringExtra(Finc.FINC_FUNDNAME_REQ);
			oldTypeCode=intent.getStringExtra(Finc.FINC_BOUNDSTYPE_REQ);
			bonusTypeText.setText(LocalData.bonusTypeMap.get(oldTypeCode));
			fincCodeText.setText(foundCode);
			fincNameText.setText(foundName);
			/**601 修改  默认值与现有分红方式相反，比如现在分红方式是现金分红，那么修改界面的默认值就应该是红利再投； */
			if(oldTypeCode.equals(LocalData.bonusTypeList.get(0))){
				type1RadioButton.setChecked(true);
				foundTypeCode = LocalData.bonusTypeList.get(1);
			}else if(LocalData.bonusTypeList.get(1).equals(oldTypeCode)){
				type2RadioButton.setChecked(true);
				foundTypeCode = LocalData.bonusTypeList.get(2);
			}else if(LocalData.bonusTypeList.get(2).equals(oldTypeCode)){
				type1RadioButton.setChecked(true);
				foundTypeCode =LocalData.bonusTypeList.get(1);
			}
		
			
		}
	}

	private void initOnClick() {
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.finc_myfinc_bounds1:// 现金分红
					foundTypeCode=LocalData.bonusTypeList.get(1);
					break;
				case R.id.finc_myfinc_bounds2:// 红利再投资
					foundTypeCode = LocalData.bonusTypeList.get(2);
					break;
				
				default:
					break;
				}
			}
		});
		//下一步
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(oldTypeCode.equals(foundTypeCode)){
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_boundtypes_equel_error));
					return;
				}
				Intent intent=new Intent(MyFincBoundsTypeSubmitActivity.this,MyFincBoundsTypeConfirmActivity.class);
				intent.putExtra(Finc.FINC_FUNDCODE_REQ, foundCode);
				intent.putExtra(Finc.FINC_FUNDNAME_REQ, foundName);
				intent.putExtra(ConstantGloble.FINC_FOUNDTYPECODE_KEY, foundTypeCode);				
				startActivityForResult(intent, 1);
			}
		});
	}
	  @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;

			default:
				break;
			}
		}
}
