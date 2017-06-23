package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyPeriodAgreementInputActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 接受协议页面
 * 
 * @author wangmengmeng
 * 
 */
public class AgreementChooseActivity extends BociBaseActivity implements
		OnCheckedChangeListener {
	/** 接受协议页面 */
	private View view;
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
	/** 产品代码输入框 */
	private EditText et_prodname;
	/** 产品名称显示框—默认隐藏,p603改成协议名称 */
	private TextView tv_prodName_agreement;
	/** 阅读总协议的radio */
	private RadioGroup rg_deal_agreement;
	private RadioButton rb_yes_deal_agreement;
	private RadioButton rb_no_deal_agreement;
	private TextView tv_total_agree;
	/** 产品说明书的radio */
	private RadioGroup rg_description_agreement;
	private RadioButton rb_yes_des;
	private RadioButton rb_no_des;
	private Map<String, Object> chooseMap;
	private Map<String, Object> accound_map;
	/**用户点击协议列表的item*/
	private Map<String, Object> map_listview_choose;
	/** 下一步按钮 */
	private Button btn_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_apply_agree_title));
		// 添加布局
		view = addView(R.layout.bocinvt_buyproduct_agreement_activity);
		setText(this.getString(R.string.go_main));
		chooseMap = BociDataCenter.getInstance().getChoosemap();
		accound_map=BocInvestControl.accound_map;
		map_listview_choose=BocInvestControl.map_listview_choose;
		// 界面初始化
		init();
		rg_deal_agreement.setOnCheckedChangeListener(this);
		rb_no_deal_agreement.setChecked(true);
		rg_description_agreement.setOnCheckedChangeListener(this);
		rb_no_des.setChecked(true);
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
		tv_prodName_agreement = (TextView) view.findViewById(R.id.tv_prodName_agreement);
		tv_total_agree = (TextView) view.findViewById(R.id.total_agree);
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
		rb_no_des = (RadioButton) view
				.findViewById(R.id.rb_no_descri_agreement);
		// 有产品信息
//		tv_prodName.setText(this.getString(R.string.prodName));
		tv_prodName.setVisibility(View.GONE);
		et_prodname.setVisibility(View.GONE);
//		tv_prodname.setVisibility(View.VISIBLE);
		detailMap = BociDataCenter.getInstance().getDetailMap();
//		tv_prodname.setText(String.valueOf(detailMap.get(BocInvt.BOCI_PRODNAME_RES)));
		tv_prodName_agreement.setText(String.valueOf(map_listview_choose.get(BocInvestControl.AGRNAME)));
		tv_prodName_agreement.setGravity(Gravity.CENTER);
		tv_prodName_agreement.setVisibility(View.VISIBLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_prodName_agreement);
		tv_total_agree.setText(getClickableSpan(tv_total_agree));
		tv_total_agree.setMovementMethod(LinkMovementMethod.getInstance());
		btn_next = (Button) view.findViewById(R.id.btn_next_agreement);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rb_yes_deal_agreement.isChecked() && rb_yes_des.isChecked()) {
					// 都阅读过了 TODO 进入确认信息页面
					requestSystemDateTime();
					BiiHttpEngine.showProgressDialog();
				} else if (!rb_yes_deal_agreement.isChecked()
						&& rb_yes_des.isChecked()) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									AgreementChooseActivity.this
											.getString(R.string.bocinvt_error_noread_total));
				} else if (!rb_yes_des.isChecked()
						&& rb_yes_deal_agreement.isChecked()) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									AgreementChooseActivity.this
											.getString(R.string.bocinvt_error_noread_des));
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							AgreementChooseActivity.this
									.getString(R.string.bocinvt_error_noread));
				}
			}
		});
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		//周期滚续产品单走流程，定时定额、余额理财走老的流程
		if (map_listview_choose.get("agrType").toString().equals("3")) {//协议类型:周期滚续协议
			Map<String, Object> parms_map=new HashMap<String, Object>();
			parms_map.put("productCode", detailMap.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES).toString());//产品代码
			parms_map.put("productName", detailMap.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES).toString());//产品名称
			parms_map.put("curCode", detailMap.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES).toString());//获取币种
			//parms_map.put("remainCycleCount", chooseMap.get("remainCycleCount").toString());//最大可购买期数
			parms_map.put("remainCycleCount", detailMap.get("maxPeriod").toString());//最大可购买期数
			parms_map.put("accountId", accound_map.get("accountId"));//账户标识
			BaseHttpEngine.showProgressDialog();
			getHttpTools().requestHttp(BocInvestControl.PSNXPADSIGNINIT, "requestPsnXpadSignInitCallBack", parms_map, false);
		}else {//定时定额协议、余额理财协议
			// 投资协议申请接受协议页面
			Intent intent = new Intent(this, XpadApplyAgreement.class);
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
//			startActivity(intent);
			startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
		}
	}
	
	/**
	 * 请求 周期性产品续约协议签约/签约初始化 回调
	 */
	@SuppressWarnings("static-access")
	public void requestPsnXpadSignInitCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_BUYINIT_MAP, getHttpTools().getResponseResult(resultObj));
		startActivityForResult(
				new Intent(AgreementChooseActivity.this,ProductQueryAndBuyPeriodAgreementInputActivity.class),
				BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);//周期滚续协议申请
//		startActivity(new Intent(AgreementChooseActivity.this,ProductQueryAndBuyPeriodAgreementInputActivity.class));
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
				Intent intent = new Intent(AgreementChooseActivity.this,
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
			if (requestCode==BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY) {
				setResult(RESULT_OK);
				finish();
			}
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_yes_deal_agreement.setChecked(true);
			}
			break;
		case RESULT_CANCELED:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_no_deal_agreement.setChecked(true);
			}
			break;
		default:
			break;
		}
	}
}
