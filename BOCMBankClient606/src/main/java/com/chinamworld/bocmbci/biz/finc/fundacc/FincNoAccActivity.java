package com.chinamworld.bocmbci.biz.finc.fundacc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 没有可用的贵金属账户
 * 
 * @author xyl
 * 
 */
public class FincNoAccActivity extends FincBaseActivity {
	private static final String TAG = "FincNoAccActivity";
	/**
	 * 添加新关联账户按钮
	 */
	private Button addNewAcc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View v = mainInflater.inflate(R.layout.finc_novalidacc, null);
		tabcontent.addView(v);
		addNewAcc = (Button) findViewById(R.id.prms_acc_addnewacc);
		addNewAcc.setVisibility(View.GONE);// 屏蔽自助关联
		setTitle(getResources().getString(R.string.prms_title_accsetingconfirm));
		addNewAcc.setOnClickListener(this);
		right.setText(getResources().getString(R.string.close));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_acc_addnewacc:
			// TODO
//			startActivityForResult((new Intent(this,
//					AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			
			if(BusinessModelControl.gotoAccRelevanceAccount(this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null)){
				finish();	
			}

		
			break;

		default:
			break;
		}
	}

	@Override
	public void queryAccListCallback(Object resultObj) {
		super.queryAccListCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fundAccList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(fincControl.fundAccList)) {
			return;
		} else {
			finish();
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			BaseHttpEngine.showProgressDialog();
			queryAccList();
			break;
		case RESULT_CANCELED:

			break;
		default:
			break;
		}
	}

}
