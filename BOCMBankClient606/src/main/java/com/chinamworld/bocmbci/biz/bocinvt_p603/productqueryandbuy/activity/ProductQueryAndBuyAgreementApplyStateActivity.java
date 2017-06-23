package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.ProductTotalActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.XpadApplyAgreement;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财-投资协议申请，我的申明页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyAgreementApplyStateActivity extends BocInvtBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_agreement_apply_state);
		map_listview_choose=BocInvestControl.map_listview_choose;
//		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化
		TextView total_agree = (TextView) findViewById(R.id.total_agree);
		TextView tv_title_1 = (TextView) findViewById(R.id.tv_title_1);
		rb_yes_deal_agreement = (RadioButton) findViewById(R.id.rb_yes_deal_agreement);
		rb_no_bocdeal_agreement = (RadioButton) findViewById(R.id.rb_no_bocdeal_agreement);
		rb_yes_descri_agreement = (RadioButton) findViewById(R.id.rb_yes_descri_agreement);
//		rb_no_descri_agreement = (RadioButton) findViewById(R.id.rb_no_descri_agreement);
//		Button btn_last = (Button) findViewById(R.id.btn_last);
		Button btn_next = (Button) findViewById(R.id.btn_next);
		//赋值
		total_agree.setText(getClickableSpan(total_agree));
		total_agree.setMovementMethod(LinkMovementMethod.getInstance());
		tv_title_1.setText(String.valueOf(map_listview_choose.get(BocInvestControl.AGRNAME))/*getInstType(map_listview_choose.get(BocInvestControl.INSTTYPE).toString())*/);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_title_1);
//		btn_last.setOnClickListener(backClickListener);
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rb_yes_deal_agreement.isChecked() && rb_yes_descri_agreement.isChecked()) {
					// 都阅读过了 TODO 进入协议基本信息页面
//					Object obj_instType = map_listview_choose.get(BocInvestControl.INSTTYPE);
//					if (StringUtil.isNullOrEmpty(obj_instType)) {
//						BaseDroidApp.getInstanse().showInfoMessageDialog("协议投资方式取值为空，无法进入下一步");
//						return;
//					}else {
//						switch (Integer.parseInt((String)obj_instType)) {
//						case 1:{//智能协议，周期连续协议
							Intent intent = new Intent(ProductQueryAndBuyAgreementApplyStateActivity.this,ProductQueryAndBuyAgreementApplyAgreementMessageActivity.class);
							startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//						}break;
//						case 2:{//智能协议，周期不连续协议
//							Intent intent = new Intent(ProductQueryAndBuyAgreementApplyStateActivity.this,ProductQueryAndBuyAgreementApplyAgreementMessageActivity.class);
//							startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//						}break;
//						case 3:{//智能协议，多次购买协议 
//							Intent intent = new Intent(ProductQueryAndBuyAgreementApplyStateActivity.this,ProductQueryAndBuyAgreementApplyAgreementMessageActivity.class);
//							startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//						}break;
//						case 4:{//智能协议，多次赎回协议
//							Intent intent = new Intent(ProductQueryAndBuyAgreementApplyStateActivity.this,ProductQueryAndBuyAgreementApplyAgreementMessageActivity.class);
//							startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//						}break;
//						case 8:{//非智能协议，业绩基准周期滚续(走购买流程，跳转到购买流程页)
////							BaseDroidApp.getInstanse().showInfoMessageDialog("该类型协议应进入购买流程");
//						}break;
//						default:{//非智能协议,5:定时定额投资6:余额理财投资7:周期滚续协议(走P603之前老流程)
////							BaseHttpEngine.showProgressDialog();
////							requestSystemDateTime();
//						}break;
//						}
//					}
				} else if (!rb_yes_deal_agreement.isChecked()
						&& rb_yes_descri_agreement.isChecked()) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									ProductQueryAndBuyAgreementApplyStateActivity.this
											.getString(R.string.bocinvt_error_noread_total));
				} else if (!rb_yes_descri_agreement.isChecked()
						&& rb_yes_deal_agreement.isChecked()) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									ProductQueryAndBuyAgreementApplyStateActivity.this
											.getString(R.string.bocinvt_error_noread_des));
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							ProductQueryAndBuyAgreementApplyStateActivity.this
									.getString(R.string.bocinvt_error_noread));
				}
			
			}
		});
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(ProductQueryAndBuyAgreementApplyStateActivity.this,XpadApplyAgreement.class)
		.putExtra(ConstantGloble.DATE_TIEM, dateTime), BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
	}
	/**
	 * 返回数据中 "协议类型"
	 * @return
	 */
	private String getInstType(String str){
		switch (Integer.parseInt(str)) {
		case 1:
			return "周期连续协议";
		case 2:
			return "周期不连续协议";
		case 3:
			return "多次购买协议";
		case 4:
			return "多次赎回协议";
		case 5:
			return "定时定额投资";
		case 6:
			return "余额理财投资";
		case 7:
			return "周期滚续协议";
		case 8:
			return "业绩基准周期滚续";
		default:
			return "";
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
				Intent intent = new Intent(ProductQueryAndBuyAgreementApplyStateActivity.this,
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
	/**
	 * 初始化数据
	 */
//	private void initDate(){
////		chooseMap = BociDataCenter.getInstance().getChoosemap();
////		detailMap=BociDataCenter.getInstance().getDetailMap();
////		accound_map=BocInvestControl.accound_map;
////		result_agreement=BocInvestControl.result_agreement;
//		map_listview_choose=BocInvestControl.map_listview_choose;
//	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("投资协议申请");
		getBackgroundLayout().setLeftButtonText("返回");
		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
	}
	/**
	 * 监听事件，返回
	 */
	private OnClickListener backClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	private RadioButton rb_yes_deal_agreement;
	private RadioButton rb_no_bocdeal_agreement;
	private RadioButton rb_yes_descri_agreement;
//	private RadioButton rb_no_descri_agreement;
	private Map<String, Object> map_listview_choose;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_yes_deal_agreement.setChecked(true);
			}
			if (requestCode==BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY) {
				setResult(RESULT_OK);
				finish();
			}
			break;
		case RESULT_CANCELED:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_no_bocdeal_agreement.setChecked(true);
			}
			break;
		case 105:
			break;
		default:
			break;
		}
	}

}
