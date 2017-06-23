package com.chinamworld.bocmbci.biz.prms.myaccount;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/** 
 *  贵金属持仓 账户信息 DialogActivity
 * @author Administrator
 *
 */
public class PrmsAccDetailDialogActivity extends PrmsBaseActivity  {

	private Button reSetBtn;
	private TextView accNumTextView;
	private TextView nickNameTv;
	private TextView accTypeTv;
	private ImageView concernBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		init();
		initData();
		initLayoutParams();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.prms_accbalance_dialog, null);
		setContentView(contentView);
		accNumTextView = (TextView) contentView.findViewById(R.id.textView1);
		nickNameTv = (TextView) contentView.findViewById(R.id.prms_accalias);
		accTypeTv = (TextView) contentView.findViewById(R.id.prms_acctype);
		reSetBtn = (Button) contentView.findViewById(R.id.reset);
		concernBtn = (ImageView) contentView
				.findViewById(R.id.img_exit_accdetail);

		reSetBtn.setOnClickListener(this);
		concernBtn.setOnClickListener(this);

	}

	/**
	 * 为布局 赋值
	 */
	private void initData() {
		nickNameTv.setText(PrmsControl.getInstance().accMessage
				.get(Prms.QUERY_PRMSACC_ACCOUNTNICKNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				BaseDroidApp.getInstanse().getCurrentAct(), nickNameTv);
		accTypeTv.setText(LocalData.AccountType.get(PrmsControl.getInstance().accMessage
				.get(Prms.PRMS_ACCOUNTTYPE)));
		accNumTextView.setText(StringUtil.getForSixForString(PrmsControl
				.getInstance().accNum));
	}

	/**
	 * 设定弹出框布局属性
	 */
	private void initLayoutParams() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
//		lp.width = LayoutValue.SCREEN_WIDTH * 19 / 20;
//		lp.height = LayoutParams.WRAP_CONTENT;
		lp.width =LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reset:
			BaseHttpEngine.showProgressDialog();
			queryPrmsAccs();
			break;
		case R.id.img_exit_accdetail:
			finish();
			break;
		default:
			break;
		}

	}
	@Override
	public void queryPrmsAccsCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryPrmsAccsCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.prms_noprmsAcc_error));
			startActivityForResult(new Intent(this, PrmsNoAccActivity.class),
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			return;
		} else {
			prmsControl.prmsAccList = (List<Map<String, String>>) (biiResponseBody
					.getResult());
			startActivityForResult(new Intent(this, PrmsAccSettingActivity.class),
					ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);
			
		}
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
