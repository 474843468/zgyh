package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.ProductTotalActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 份额转换声明页面
 * 
 * @author HVZHUNG
 *
 */
public class QuantityTransitionStatementActivity extends BocInvtBaseActivity {

	/** 协议书 */
	private RadioGroup rg_agreement;
	/** 协议书按钮“是” */
	private RadioButton rb_agreement_positive;//是
	/** 协议书按钮“否” */
	private RadioButton rb_agreement_negative;//否
	/** 说明书 */
	private RadioGroup rg_specification;
	/** 说明书 按钮“是”*/
	private RadioButton rb_specification_positive;//是
	/** 说明书 按钮“否”*/
	private RadioButton rb_specification_negative;//否
	/** */
	private TextView total_agree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.bocinvt_holding_detail_lot_transform);
		getBackgroundLayout().setRightButtonText(null);
		setContentView(R.layout.bocinvt_quantity_transition_statement_activity_p603);
		initView();
		if (savedInstanceState == null) {
			rg_agreement.check(R.id.rb_agreement_negative);
			rg_specification.check(R.id.rb_specification_negative);

		}
	}

	private void initView() {
		rg_agreement = (RadioGroup) findViewById(R.id.rg_agreement);
		rg_specification = (RadioGroup) findViewById(R.id.rg_specification);
		rb_agreement_positive = (RadioButton) findViewById(R.id.rb_agreement_positive);
		rb_agreement_negative = (RadioButton) findViewById(R.id.rb_agreement_negative);
		rb_specification_positive = (RadioButton) findViewById(R.id.rb_specification_positive);
		rb_specification_negative = (RadioButton) findViewById(R.id.rb_specification_negative);
		total_agree = (TextView) findViewById(R.id.total_agree);
		total_agree.setText(getClickableSpan(total_agree));
		total_agree.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_pre:
			finish();
			break;
		case R.id.bt_next:
			if(rb_agreement_positive.isChecked()&&rb_specification_negative.isChecked()){//说明书没有选中
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.bocinvt_error_noread_des));
				return;
			}else if(rb_agreement_negative.isChecked()&&rb_specification_positive.isChecked()){//协议书没有选中
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.bocinvt_error_noread_total));
				return;
			}else if(rb_agreement_negative.isChecked()&&rb_specification_negative.isChecked()){//两个都没有选中
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.bocinvt_error_noread));
				return;
			}
			Intent intent = new Intent(this, QuantityTransitionActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	/**
	 * 协议书设置
	 * @param tv
	 * @return
	 */
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
				Intent intent = new Intent(QuantityTransitionStatementActivity.this,
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_agreement_positive.setChecked(true);
			} 
			break;
		case RESULT_CANCELED:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				rb_agreement_negative.setChecked(true);
			}
			break;
		default:
			break;
		}
	}
}
