package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 购买产品选择产品页面
 * 
 * @author wangmengmeng
 * 
 */
public class BuyProductChooseActivity extends BociBaseActivity implements
		OnCheckedChangeListener {
	private static final String TAG = "BuyProductChooseActivity";
	/** 购买产品页面 */
	private View view;
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
//	private Map<String, Object> chooseMap;
	/** 产品代码输入框 */
	private EditText et_prodname;
	/** 产品名称显示框—默认隐藏 */
	private TextView tv_prodname;
	/** 阅读总协议的radio */
	private RadioGroup rg_deal_agreement;
	private RadioButton rb_yes_deal_agreement;
	private RadioButton rb_no_deal_agreement;
	private TextView tv_total_agree;
	/** 产品说明书的radio */
	private RadioGroup rg_description_agreement;
	private RadioButton rb_yes_des;
//	private RadioButton rb_no_des;
//	private TextView tv_des_agree;
	private boolean isFastBuy;
	/** 下一步按钮 */
	private Button btn_next;
	/**是否从其他功能模块跳转过来*/
	private boolean isFlag;
	/**是否从投资协议申请跳转过来*/
	private String IsFromAgreeApply;
	/**用户点击协议列表的item*/
	private Map<String, Object> map_listview_choose;
	/** 产品名称显示框—默认隐藏,p603改成协议名称 */
	private TextView tv_prodName_agreement;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IsFromAgreeApply = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.IsFromAgreeApply);
		// 为界面标题赋值
		if("1".equals(IsFromAgreeApply)){
			setTitle(this.getString(R.string.bocinvt_apply_agree_title));
		}else{
			setTitle(this.getString(R.string.boci_buy_title));
		}
		// 添加布局
		map_listview_choose=BocInvestControl.map_listview_choose;
		view = addView(R.layout.bocinvt_buyproduct_agreement_activity);
		setText(this.getString(R.string.go_main));
		isFastBuy = this.getIntent().getBooleanExtra(
				ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
		// 界面初始化
		getIntentDate();
		if (isFlag) {
			requestPsnXpadProductDetailQuery();
		}else {
			init_1();
		}
		
	}
	private void init_1(){
		init();
		rg_deal_agreement.setOnCheckedChangeListener(this);
		rg_description_agreement.setOnCheckedChangeListener(this);

		if (isFastBuy) {
			// 无产品
			setBackBtnClick(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent = new Intent(BuyProductChooseActivity.this,
							QueryProductActivity.class);
					startActivity(intent);
					finish();
				}
			});
		} else {
			setBackBtnClick(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setResult(RESULT_CANCELED);
					finish();
				}
			});
		}

		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};
	private String productkind;
	private String productCode;

	@SuppressWarnings("unchecked")
	private void init() {
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_buy_step1),
						this.getResources().getString(
								R.string.bocinvt_buy_step2),
						this.getResources().getString(
								R.string.bocinvt_buy_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		TextView tv_prodName = (TextView) view.findViewById(R.id.tv_prodName);
		et_prodname = (EditText) view.findViewById(R.id.et_prodName_agreement);
		tv_prodname = (TextView) view.findViewById(R.id.tv_prodName_agreement);
		tv_prodName_agreement = (TextView) view.findViewById(R.id.tv_prodName_agreement);
		tv_total_agree = (TextView) view.findViewById(R.id.total_agree);
//		tv_des_agree = (TextView) view.findViewById(R.id.des_agree);
		rg_deal_agreement = (RadioGroup) view
				.findViewById(R.id.rg_deal_agreement);
		rg_description_agreement = (RadioGroup) view
				.findViewById(R.id.rg_description_agreement);

		rb_yes_deal_agreement = (RadioButton) view
				.findViewById(R.id.rb_yes_deal_agreement);
		rb_no_deal_agreement = (RadioButton) view
				.findViewById(R.id.rb_no_bocdeal_agreement);
		rb_yes_des = (RadioButton) view
				.findViewById(R.id.rb_yes_descri_agreement);
//		rb_no_des = (RadioButton) view
//				.findViewById(R.id.rb_no_descri_agreement);

		if (isFastBuy) {
			// 无产品
			tv_prodName.setText(this.getString(R.string.prodCode));
			et_prodname.setVisibility(View.VISIBLE);
			tv_prodname.setVisibility(View.GONE);
		} else {
			detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
					.getBizDataMap()
					.get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
			if("1".equals(IsFromAgreeApply)){
				tv_prodName.setVisibility(View.GONE);
				et_prodname.setVisibility(View.GONE);
				tv_prodName_agreement.setText(String.valueOf(map_listview_choose.get(BocInvestControl.AGRNAME)));
				tv_prodName_agreement.setGravity(Gravity.CENTER);
				tv_prodName_agreement.setVisibility(View.VISIBLE);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_prodName_agreement);
			}else{
			// 有产品信息
			tv_prodName.setText(this.getString(R.string.prodName));
			et_prodname.setVisibility(View.GONE);
			tv_prodname.setVisibility(View.VISIBLE);
//			chooseMap = BociDataCenter.getInstance().getChoosemap();
			tv_prodname.setText(String.valueOf(detailMap
					.get(BocInvt.BOCI_PRODNAME_RES)));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tv_prodname);
		}
		}
		tv_total_agree.setText(getClickableSpan(tv_total_agree));
		tv_total_agree.setMovementMethod(LinkMovementMethod.getInstance());
		btn_next = (Button) view.findViewById(R.id.btn_next_agreement);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFastBuy) {
					// 无产品信息 需请求
					if (StringUtil.isNullOrEmpty(et_prodname.getText()
							.toString().trim())) {
						// 必须输入代码信息才能查询
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										BuyProductChooseActivity.this
												.getString(R.string.bocinvt_error_noinput));
						return;
					}
				}
				if (rb_yes_deal_agreement.isChecked() && rb_yes_des.isChecked()) {
					// 都阅读过了
					if (isFastBuy) {
						// 无产品信息 需请求
						requestFast();
					} else {
						// 有产品信息
//						if (detailMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES).equals("1")) {
//							// 周期性产品——签约
//							requestCommConversationId();
//							BaseHttpEngine.showProgressDialog();
//						} else {
////							// 如果是非周期性产品
//						P603不分周期型理财产品和非周期性产品，分为净值型理财产品和非净值型理财产品
							toBuyProductInputActivity();
//							BaseHttpEngine.showProgressDialog();
//							requestSystemDateTime();
//						}
					}
				} else if (!rb_yes_deal_agreement.isChecked()
						&& rb_yes_des.isChecked()) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									BuyProductChooseActivity.this
											.getString(R.string.bocinvt_error_noread_total));
				} else if (!rb_yes_des.isChecked()
						&& rb_yes_deal_agreement.isChecked()) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									BuyProductChooseActivity.this
											.getString(R.string.bocinvt_error_noread_des));
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							BuyProductChooseActivity.this
									.getString(R.string.bocinvt_error_noread));
				}
			}
		});
	}
	/**
	 * 请求产品详情
	 */
	private void requestPsnXpadProductDetailQuery(){
		HashMap<String, Object> map_parms = new HashMap<String, Object>();
		map_parms.put(BocInvestControl.PRODUCTKIND, productkind);
		map_parms.put(BocInvestControl.PRODUCTCODE, productCode);
		map_parms.put("ibknum",((Map<String, Object>)BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP)).get("ibkNumber"));
		BaseHttpEngine.showProgressDialogCanGoBack();
		getHttpTools().requestHttp(BocInvt.PRODUCTDETAILQUERY, "requestPsnXpadProductDetailQueryCallBack", map_parms, true);
	}
	/**
	 * 请求产品详情 回调
	 */
	@SuppressWarnings("static-access")
	public void requestPsnXpadProductDetailQueryCallBack(Object resultObj){
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Map<String, Object> responseDeal = getHttpTools().getResponseResult(resultObj);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST, responseDeal);
		init_1();
		BaseHttpEngine.dissMissProgressDialog();
	}
//	@Override
//	public void requestSystemDateTimeCallBack(Object resultObj) {
//		if (StringUtil.isNullOrEmpty(resultObj)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		super.requestSystemDateTimeCallBack(resultObj);
//		BocInvestControl.SYSTEM_DATE=dateTime;
//		toBuyProductInputActivity();
//		BaseHttpEngine.dissMissProgressDialog();
//	}
	private void toBuyProductInputActivity(){
		Intent intent = new Intent(BuyProductChooseActivity.this,BuyProductInputActivity.class);
		intent.putExtra("isFlag", isFlag);
		startActivityForResult(intent, ACTIVITY_BUY_CODE);
	}
	private void getIntentDate(){
		Intent intent = getIntent();
		if (StringUtil.isNullOrEmpty(intent)) {
			return;
		}
		productkind = intent.getStringExtra(BocInvestControl.PRODUCTKIND);
		productCode = intent.getStringExtra(BocInvestControl.PRODUCTCODE);
		if (!StringUtil.isNullOrEmpty(productkind)&&!StringUtil.isNullOrEmpty(productCode)) {
			isFlag=true;
		}
	}

	private SpannableString getClickableSpan(final TextView tv) {
		final SpannableString sp = new SpannableString(
				this.getString(R.string.boc_deal));
		sp.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				sp.setSpan(
						new ForegroundColorSpan(getResources().getColor(
								R.color.red)), 6, 26,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				tv.setText(sp);
				Intent intent = new Intent(BuyProductChooseActivity.this,
						ProductTotalActivity.class);
				startActivityForResult(intent,
						ConstantGloble.ACTIVITY_RESULT_CODE);
				overridePendingTransition(R.anim.push_up_in,
						R.anim.no_animation);
			}
		}, 6, 26, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 6, 26,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return sp;
	}

//	/** 购买初始化 */
//	public void requestBuyInit(int i) {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADPRODUCTBUYINIT_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		if (i == 1) {
//			// 不用显示通讯框
//		} else {
//			BiiHttpEngine.showProgressDialog();
//		}
//		HttpManager.requestBii(biiRequestBody, this, "requestBuyInitCallBack");
//	}

//	/** 购买初始化回调 */
//	public void requestBuyInitCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//
//		Map<String, Object> buyInitMap = (Map<String, Object>) biiResponseBody
//				.getResult();
//		if (StringUtil.isNullOrEmpty(buyInitMap)) {
//			return;
//		}
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOCINVT_BUYINIT_MAP, buyInitMap);
//		// 结束通讯,隐藏通讯框
//		BiiHttpEngine.dissMissProgressDialog();
//		// 如果是非周期性产品
//		Intent intent = new Intent(BuyProductChooseActivity.this,
//				BuyProductInputActivity.class);
//		startActivityForResult(intent, ACTIVITY_BUY_CODE);
//
//	}

	/** 请求周期性产品签约初始化 */
	@SuppressWarnings("unchecked")
	public void requestXpadSignInit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADSIGNINIT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCINVT_SIGNINIT_PRODUCTCODE_REQ,
				String.valueOf(detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES)));
		paramsmap.put(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_REQ,
				String.valueOf(detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES)));
		paramsmap.put(BocInvt.BOCINVT_SIGNINIT_CURCODE_REQ,
				String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)));
		// 上送最大可购买期数
		paramsmap.put(
				BocInvt.BOCINVT_SIGNINIT_REMAINCYCLECOUNT_REQ,
				BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.BOCINVT_REMAINCYCLECOUNT_STRING));
		paramsmap.put(Comm.ACCOUNT_ID, ((Map<String, Object>)BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP)).get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestXpadSignInitCallBack");
	}

	/** 请求周期性产品签约初始化 */
	@SuppressWarnings("unchecked")
	public void requestXpadSignInitCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> initMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(initMap)) {
			// 结束通讯,隐藏通讯框
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		// 结束通讯,隐藏通讯框
		BiiHttpEngine.dissMissProgressDialog();
		// 储存周期性签约初始化返回结果
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_SIGNINIT_MAP, initMap);
//		Intent intent = new Intent(BuyProductChooseActivity.this,
//				BuyProductInputActivity.class);
//		startActivityForResult(intent, ACTIVITY_BUY_CODE);
		toBuyProductInputActivity();
	}

	/** 请求快速查询 */
	@SuppressWarnings("unchecked")
	public void requestFast() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.FAST_QUERY_REQUEST_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_FAST_XPADPRODUCTCODE_REQ, et_prodname
				.getText().toString().trim());
		Map<String, Object> investBindingInfo = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
		paramsmap.put(BocInvt.BOCI_FAST_ACCOUNTID_REQ,
				investBindingInfo.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestFastCallBack");
	}

	/** 请求快速查询回调 */
	@SuppressWarnings("unchecked")
	public void requestFastCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> fastMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(fastMap)) {
			// 结束通讯,隐藏通讯框
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					BuyProductChooseActivity.this
							.getString(R.string.bocinve_error_nullproduct));
			return;
		}
		detailMap = fastMap;
		// 储存剩余最大可购买期数
		BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.BOCINVT_REMAINCYCLECOUNT_STRING,
						(String) detailMap
								.get(BocInvt.BOCI_REMAINCYCLECOUNT_RES));
		// 储存产品详情
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST, detailMap);

		if (Boolean.valueOf(String.valueOf(detailMap
				.get(BocInvt.BOCI_DETAILPERIODICAL_RES)))) {
			String periodical_con = (String) detailMap
					.get(BocInvt.BOCI_DETAILPERIODICAL_RES);
			// 签约
			if (LocalData.boci_StatusMap.get(
					(String) detailMap.get(BocInvt.BOCI_DETAILSTATUS_RES))
					.equalsIgnoreCase(LocalData.bocinvtXpadStatus.get(1))
					&& periodical_con.equals("1")) {
			} else {
				BiiHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						BuyProductChooseActivity.this
								.getString(R.string.bocinvt_error_contract));
				return;
			}
			// 周期性产品——签约
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();

		} else {
			String periodical = (String) detailMap
					.get(BocInvt.BOCI_DETAILPERIODICAL_RES);
			boolean isperiodical = Boolean.valueOf(periodical);
			if (LocalData.boci_StatusMap.get(
					(String) detailMap.get(BocInvt.BOCI_DETAILSTATUS_RES))
					.equalsIgnoreCase(LocalData.bocinvtXpadStatus.get(1))
					&& !isperiodical) {
			} else {
				BiiHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						BuyProductChooseActivity.this
								.getString(R.string.bocinvt_error_buy));
				return;
			}

			// 非周期性产品——购买
			BiiHttpEngine.dissMissProgressDialog();
//			// 如果是非周期性产品
//			Intent intent = new Intent(BuyProductChooseActivity.this,
//					BuyProductInputActivity.class);
//			startActivityForResult(intent, ACTIVITY_BUY_CODE);
			toBuyProductInputActivity();
		}

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestXpadSignInit();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_yes_deal_agreement:
			break;
		case R.id.rb_no_bocdeal_agreement:
			break;
		case R.id.rb_yes_descri_agreement:
			break;
		case R.id.rb_no_descri_agreement:
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_yes_deal_agreement.setChecked(true);
			} else {
				setResult(RESULT_OK);
				finish();
			}
			break;
		case RESULT_CANCELED:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_no_deal_agreement.setChecked(true);
			}
			break;
		case 105:
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isFastBuy) {
				// 无产品
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(BuyProductChooseActivity.this,
						QueryProductActivity.class);
				startActivity(intent);
				finish();
			} else {
				setResult(RESULT_CANCELED);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
