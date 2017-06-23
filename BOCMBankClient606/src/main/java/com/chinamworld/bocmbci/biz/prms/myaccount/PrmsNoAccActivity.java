package com.chinamworld.bocmbci.biz.prms.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.TextViewUtil;

/**
 * 没有可用的贵金属账户
 * 
 * @author xyl
 * 
 */
public class PrmsNoAccActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsQueryDealDetailsActivity";
	/**
	 * 添加新关联账户按钮
	 */
	private Button addNewAcc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		settingbaseinit();
		View v = mainInflater.inflate(R.layout.prms_novalidacc, null);
		tabcontent.addView(v);
		addNewAcc = (Button) findViewById(R.id.prms_acc_addnewacc);
		addNewAcc.setVisibility(View.GONE);//屏蔽自助关联
		setTitle(getResources().getString(R.string.prms_title_accsetingconfirm));
		addNewAcc.setOnClickListener(this);
		right.setText(getString(R.string.switch_off));
		right.setOnClickListener(this);
		back.setVisibility(View.GONE);
//		SpannableString ss = new SpannableString(getString(R.string.prms_acc_noacc_info));
//		ImageSpan image = new ImageSpan(this, R.drawable.forex_no_acc);
//		ss.setSpan(image, 0, 1, 0); //TODO  学习
		TextView tv = (TextView)findViewById(R.id.textView1);
		TextViewUtil.getInstance(this).ShowImageViewOnStart(tv, R.drawable.forex_no_acc);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_acc_addnewacc:
//			startActivityForResult((new Intent(this,
//					AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			
			BusinessModelControl.gotoAccRelevanceAccount(this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			break;
		case R.id.ib_top_right_btn:
			finish();
			break;
		default:
			break;
		}
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}

	
}
