package com.chinamworld.bocmbci.biz.gatherinitiative.commenpayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.RegCode;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;

/**
 * @ClassName: ModifyPhoneNumberActivity
 * @Description: 修改手机号页面
 * @author JiangWei
 * @date 2013-9-1下午10:00:40
 */
public class ModifyPhoneNumberActivity extends GatherBaseActivity {
	/** 付款人客户号 */
	private TextView textCustomerId;
	/** 付款人类型 */
	private TextView textPayerType;
	/** 付款人姓名 */
	private TextView textPayerName;
	/** 付款人旧手机号 */
	private TextView textOldNumber;
	/** 付款人手机号 */
	private TextView textPayerNumber;
	/** 输入新手机号 */
	private EditText editNewNumber;

	/** 付款人客户号str */
	private String strCustomerId;
	/** 付款人类型str */
	private String strPayerType;
	/** 付款人姓名str */
	private String strPayerName;
	/** 付款人旧手机号str */
	private String strOldNumber;
	/** 付款人手机号str */
	private String strPayerNumber;
	/** 输入新手机号str */
	private String strNewNumber;
	/** 取消按钮 */
	private Button btnCancel;

	private Button btnNext;
	private TextView textTitle1;
	private TextView textTitle2;
	private LinearLayout layoutOldNumber;
	private LinearLayout layoutPayerNumber;
	private LinearLayout layoutEditNumber;
	private Map<String, Object> mapInfo;
	private String tokenId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.modify_commen_payer_phone);
		View view = LayoutInflater.from(this).inflate(R.layout.gather_modify_phone_number_activity, null);
		tabcontent.addView(view);
		setLeftButtonPopupGone();
		((LinearLayout) findViewById(R.id.rl_menu)).setVisibility(View.GONE);
		ibBack.setVisibility(View.GONE);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setText(R.string.close);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
					overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
				}
			});
		}
		btnCancel = (Button) findViewById(R.id.btn_cancle);
		if (btnCancel != null) {
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
					overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
				}
			});
		}

		mapInfo = GatherInitiativeData.getInstance().getPayerInfo();
		init();
	}

	private void init() {
		textTitle1 = (TextView) this.findViewById(R.id.second_title_tv_1);
		textTitle2 = (TextView) this.findViewById(R.id.second_title_tv_2);
		textCustomerId = (TextView) this.findViewById(R.id.payer_customer_number);
		textPayerType = (TextView) this.findViewById(R.id.payer_type);
		textPayerName = (TextView) this.findViewById(R.id.payer_name);
		textOldNumber = (TextView) this.findViewById(R.id.old_phone_number);
		textPayerNumber = (TextView) this.findViewById(R.id.payer_phone);
		editNewNumber = (EditText) this.findViewById(R.id.new_phone_number);

		layoutOldNumber = (LinearLayout) this.findViewById(R.id.layout_payer_old_phone);
		layoutPayerNumber = (LinearLayout) this.findViewById(R.id.layout_payer_phone);
		layoutEditNumber = (LinearLayout) this.findViewById(R.id.layout_edit_phone);
		btnNext = (Button) this.findViewById(R.id.next_btn);

		strCustomerId = (String) mapInfo.get(GatherInitiative.PAYER_CUSTOMER_ID);
		strPayerType = (String) mapInfo.get(GatherInitiative.IDENTIFY_TYPE);
		strPayerName = (String) mapInfo.get(GatherInitiative.PAYER_NAME);
		strOldNumber = (String) mapInfo.get(GatherInitiative.PAYER_MOBILE);
		textCustomerId.setText(strCustomerId);
		textPayerType.setText(LocalData.payerType.get(strPayerType));
		textPayerName.setText(strPayerName);
		textOldNumber.setText(strOldNumber);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				strNewNumber = editNewNumber.getText().toString().trim();
				// 验证 手机
				RegexpBean moblieRegex = new RegexpBean(getString(R.string.new_phone), strNewNumber,
						RegCode.LONG_MOBILE);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(moblieRegex);
				if (RegexpUtils.regexpDate(lists)) {
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}
			}
		});
	}

	private void showSuccessView() {
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setResult(RESULT_OK);
					finish();
					overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
				}
			});
		}
		textTitle1.setText(R.string.finish_modify_phone_success);
		textTitle1.setVisibility(View.GONE);
		textTitle2.setVisibility(View.VISIBLE);
		layoutOldNumber.setVisibility(View.GONE);
		layoutEditNumber.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);

		textPayerNumber.setText(strNewNumber);
		layoutPayerNumber.setVisibility(View.VISIBLE);
		btnNext.setText(R.string.finish);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
				overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnTransActModifyPayerMobile();
	}

	private void requestPsnTransActModifyPayerMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_MODIFY_PAYER_MOBILE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GatherInitiative.PAYER_ID, (String) mapInfo.get(GatherInitiative.PAYER_ID));
		map.put(GatherInitiative.PAYER_MOBILE, strNewNumber);
		map.put(GatherInitiative.TOKEN, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransActModifyPayerMobileCallback");
	}

	public void requestPsnTransActModifyPayerMobileCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		showSuccessView();
	}

}
