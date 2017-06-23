package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;



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
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 中银理财-组合购买，我的声明页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyComboBuyStateActivity extends BocInvtBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_combo_buy_choose_agreement);
//		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化
		TextView total_agree = (TextView) findViewById(R.id.total_agree);
		rb_yes_deal_agreement = (RadioButton) findViewById(R.id.rb_yes_deal_agreement);
		rb_no_bocdeal_agreement = (RadioButton) findViewById(R.id.rb_no_bocdeal_agreement);
		rb_yes_descri_agreement = (RadioButton) findViewById(R.id.rb_yes_descri_agreement);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes);
//		Button btn_last = (Button) findViewById(R.id.btn_last);
		Button btn_next = (Button) findViewById(R.id.btn_next);
		//赋值
		total_agree.setText(getClickableSpan(total_agree));
		total_agree.setMovementMethod(LinkMovementMethod.getInstance());
		btn_next.setOnClickListener(btn_next_onclick);
	}
	
	private OnClickListener btn_next_onclick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (rb_yes_deal_agreement.isChecked() && rb_yes_descri_agreement.isChecked()) {
				// 协议、说明书  都阅读过了
				if (rb_yes.isChecked()) {
					Intent intent = new Intent(ProductQueryAndBuyComboBuyStateActivity.this,ProductQueryAndBuyComboBuyActivity.class);
					startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY);
				}else {//如果被组合产品总市值超过组合买入金额，没有选择同意继续交易
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							ProductQueryAndBuyComboBuyStateActivity.this
									.getString(R.string.bocinvt_error_combo_buy));
				}
			} else if (!rb_yes_deal_agreement.isChecked()
					&& rb_yes_descri_agreement.isChecked()) {
				BaseDroidApp
						.getInstanse()
						.showInfoMessageDialog(
								ProductQueryAndBuyComboBuyStateActivity.this
										.getString(R.string.bocinvt_error_noread_total));
			} else if (!rb_yes_descri_agreement.isChecked()
					&& rb_yes_deal_agreement.isChecked()) {
				BaseDroidApp
						.getInstanse()
						.showInfoMessageDialog(
								ProductQueryAndBuyComboBuyStateActivity.this
										.getString(R.string.bocinvt_error_noread_des));
			} else if (!rb_yes_descri_agreement.isChecked()&&!rb_yes_deal_agreement.isChecked()&&rb_yes.isChecked()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ProductQueryAndBuyComboBuyStateActivity.this
								.getString(R.string.bocinvt_error_noread_total_des));
			} else if (!rb_yes_descri_agreement.isChecked()&&!rb_yes_deal_agreement.isChecked()&&!rb_yes.isChecked()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ProductQueryAndBuyComboBuyStateActivity.this
								.getString(R.string.bocinvt_error_noread));
			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_yes_deal_agreement.setChecked(true);
			} 
			if (requestCode==BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY) {
				setResult(RESULT_OK);
				finish();
			}
			break;
		case RESULT_CANCELED:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_no_bocdeal_agreement.setChecked(true);
			}
			break;
		default:
			break;
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
				Intent intent = new Intent(ProductQueryAndBuyComboBuyStateActivity.this,
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
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
//		detailMap=BociDataCenter.getInstance().getDetailMap();
//		accound_map=BocInvestControl.accound_map;
//		GuarantyProductList_map=BocInvestControl.GuarantyProductList_map;
//	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("组合购买");
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
	private RadioButton rb_yes;

}
