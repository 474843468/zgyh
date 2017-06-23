package com.chinamworld.bocmbci.biz.drawmoney.drawfromagencey;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivitySwitcher;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.biz.drawmoney.remitout.AgencyQueryActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DrawInputInfoAcitivity
 * @Description: 代理点取款的初始页面
 * @author JiangWei
 * @date 2013-7-15 下午4:03:46
 */
public class DrawInputInfoAcitivity extends DrawBaseActivity {

	/** 汇款编号 */
	private EditText editRemitNumber;
	private String remitNoStr;
	/** 收款人手机号 */
	private EditText editPayeePhone;
	private String payeeMobileStr;
	/** 收款人姓名 */
	private EditText editPayeeName;
	private String payeeNameStr;

	/** 下一步按钮 */
	private Button btnNext;
	private String conversationId;
	public static boolean toClearOrNot = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.drawmoney_draw_input_info, null);
		tabcontent.addView(view);
		setTitle(R.string.draw_from_agency_title);
		boolean isFromShortCut = this.getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false);
		if(isFromShortCut){
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestDrawPsnMobileIsSignedAgent();
		}else{
			init();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (toClearOrNot) {
			editPayeeName.setText("");
			editPayeePhone.setText("");
			editRemitNumber.setText("");
			editRemitNumber.requestFocus();
			toClearOrNot = false;
		}
	}

	private void init() {
		editRemitNumber = (EditText) this.findViewById(R.id.edit_remit_no);
		editPayeePhone = (EditText) this.findViewById(R.id.edit_get_remit_phone);
		editPayeeName = (EditText) this.findViewById(R.id.edit_get_remit_name);
		EditTextUtils.setLengthMatcher(this, editPayeeName, 56);
		btnNext = (Button) this.findViewById(R.id.draw_input_next_btn);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseNext();
			}
		});
		Button rightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setText(R.string.agence_query);
		if (rightBtn != null) {
			rightBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(DrawInputInfoAcitivity.this,
							AgencyQueryActivity.class);
					startActivity(intent);
				}
			});
		}

	}

	/**
	 * @Title: excuseNext
	 * @Description:执行下一步
	 * @param
	 * @return void
	 */
	private void excuseNext() {
		remitNoStr = editRemitNumber.getText().toString();
		payeeMobileStr = editPayeePhone.getText().toString();
		payeeNameStr = editPayeeName.getText().toString();

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regRemitNo = new RegexpBean(this.getString(R.string.remitout_no_no_label), remitNoStr, "remitNO");
		lists.add(regRemitNo);
		RegexpBean regBankNum = new RegexpBean(this.getString(R.string.get_remit_phone_no_label), payeeMobileStr,
				"longMobile");
		lists.add(regBankNum);
		RegexpBean regName = new RegexpBean(this.getString(R.string.get_remit_name_no_label), payeeNameStr,
				"payeeNameDrawM");
		lists.add(regName);
		if (RegexpUtils.regexpDate(lists)) {
			BaseHttpEngine.showProgressDialog();
			requestPsnMobileWithdrawalDetails();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestForRandomNumber();
	}

	/**
	 * @Title: requestForRandomNumber
	 * @Description: 请求随机数
	 * @param
	 * @return void
	 */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * @Title: queryRandomNumberCallBack
	 * @Description: 请求随机数的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		DrawMoneyData.getInstance().setRandomNumber(randomNumber);

		Intent intent = new Intent(this, DrawConfirmActivity.class);
		startActivity(intent);
	}

	/**
	 * @Title: requestPsnMobileWithdrawalDetails
	 * @Description: 请求“汇款解付详细信息”接口
	 * @param
	 * @return void
	 */
	private void requestPsnMobileWithdrawalDetails() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_WITH_DRAWAL_DETAILS);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DrawMoney.PAYEE_MOBILE, payeeMobileStr);
		map.put(DrawMoney.PAYEE_NAME, payeeNameStr);
		map.put(DrawMoney.REMIT_NO, remitNoStr);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnMobileWithdrawalDetailsCallback");
	}

	/**
	 * @Title: requestPsnMobileWithdrawalDetailsCallback
	 * @Description: 请求“汇款解付详细信息”接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnMobileWithdrawalDetailsCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		DrawMoneyData.getInstance().setDrawInfo(result);
		requestCommConversationId();
	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case GET_IS_SIGNED_CALLBACK:
			if(ActivitySwitcher.isSigned){
				initLeftSideList(this, LocalData.DrawMoneyLeftList);
			}else{
				initLeftSideList(this, LocalData.DrawMoneyLeftListNoSigned);
			}
			BaseHttpEngine.dissMissProgressDialog();
			if (!ActivitySwitcher.isSigned) {
				BaseDroidApp.getInstanse().showMessageDialog(this.getResources().getString(R.string.no_sign_agency),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
//								ActivityTaskManager.getInstance().removeAllActivity();
//								Intent intent = new Intent();
//								intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//								BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
								goToMainActivity();
							}
						});
				break;
			}else{
				init();
			}
			break;
		default:
			break;
		}

	}
}
