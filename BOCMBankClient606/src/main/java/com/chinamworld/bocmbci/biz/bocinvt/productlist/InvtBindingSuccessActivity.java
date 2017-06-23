package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 登记账户成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class InvtBindingSuccessActivity extends BociBaseActivity {
	private static final String TAG = "InvtBindingSuccessActivity";
	/** 登记账户成功信息页 */
	private View view;
	/** 选择的资金账户信息 */
	private Map<String, Object> chooseBindingMap;
	/** 账户类型 */
	private TextView accTypeText;
	/** 账户别名 */
	private TextView accAlisText;
	/** 账户 */
	private TextView accNumberText;
	private Button lastButton;
	private Button sureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.boci_binding_title));
		// 右上角按钮赋值
		// setText(this.getResources().getString(R.string.close));
		// 添加布局
		view = addView(R.layout.bocinvt_binding_confirm);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		goneLeftView();
		gonerightBtn();
		// 初始化界面
		init();
	}

	private void init() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_rel_step1),
						this.getResources().getString(
								R.string.bocinvt_rel_step2),
						this.getResources().getString(
								R.string.bocinvt_rel_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		TextView binding_title = (TextView) view
				.findViewById(R.id.binding_title);
		binding_title.setText(this.getResources().getString(
				R.string.bocinvt_relevance_success_title));
		chooseBindingMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
		accTypeText = (TextView) view.findViewById(R.id.acc_type);
		accAlisText = (TextView) view.findViewById(R.id.acc_alias);
		accNumberText = (TextView) view.findViewById(R.id.acc_number);
		lastButton = (Button) view.findViewById(R.id.acc_last);
		sureButton = (Button) view.findViewById(R.id.acc_sure);
		sureButton.setText(this.getString(R.string.finish));
//		LinearLayout bocinvt_currency = (LinearLayout) view
//				.findViewById(R.id.bocinvt_currency);
//		List<Map<String, Object>> currencyList = (List<Map<String, Object>>) chooseBindingMap
//				.get(ConstantGloble.ACC_DETAILIST);
//		List<Map<String, Object>> accdetailList = new ArrayList<Map<String, Object>>();
//		if (currencyList == null || currencyList.size() == 0) {
//
//		} else {
//			for (int i = 0; i < currencyList.size(); i++) {
//				String currencyname = (String) currencyList.get(i).get(
//						Acc.DETAIL_CURRENCYCODE_RES);
//				// 过滤
//				if (StringUtil.isNull(LocalData.currencyboci.get(currencyname))) {
//					accdetailList.add(currencyList.get(i));
//				}
//			}
//		}
//		if (accdetailList == null || accdetailList.size() == 0) {
//
//		} else {
//			for (int i = 0; i < accdetailList.size(); i++) {
//				View currency_view = LayoutInflater.from(
//						InvtBindingSuccessActivity.this).inflate(
//						R.layout.bocinvt_currency_balance_forbinding, null);
//				bocinvt_currency.addView(currency_view, i);
//
//				TextView tv_acc_currencycode = (TextView) currency_view
//						.findViewById(R.id.acc_currencycode);
//				TextView tv_acc_accbookbalance = (TextView) currency_view
//						.findViewById(R.id.acc_bookbalance);
//				String currencyname = (String) accdetailList.get(i).get(
//						Acc.DETAIL_CURRENCYCODE_RES);
//				String cashRemit = (String) accdetailList.get(i).get(
//						Acc.DETAIL_CASHREMIT_RES);
//				PopupWindowUtils.getInstance().setOnShowAllTextListener(
//						BaseDroidApp.getInstanse().getCurrentAct(),
//						tv_acc_accbookbalance);
//				PopupWindowUtils.getInstance().setOnShowAllTextListener(
//						BaseDroidApp.getInstanse().getCurrentAct(),
//						tv_acc_currencycode);
//				if (LocalData.Currency.get(currencyname).equals(
//						ConstantGloble.ACC_RMB)) {
//					tv_acc_currencycode.setText(LocalData.Currency
//							.get(currencyname) + ConstantGloble.ACC_COLON);
//				} else {
//					tv_acc_currencycode.setText(LocalData.Currency
//							.get(currencyname)
//							+ ConstantGloble.ACC_STRING
//							+ LocalData.CurrencyCashremit.get(cashRemit)
//							+ ConstantGloble.ACC_COLON);
//				}
//				tv_acc_accbookbalance.setText(StringUtil
//						.parseStringCodePattern(
//								currencyname,
//								(String) accdetailList.get(i).get(
//										Acc.DETAIL_AVAILABLEBALANCE_RES), 2));
//			}
//		}
		String account_type = String.valueOf(chooseBindingMap
				.get(BocInvt.BOCINVT_BINDING_ACCOUNTTYPE_RES));
		accTypeText.setText(LocalData.AccountType.get(account_type.trim()));
		accAlisText.setText(String.valueOf(chooseBindingMap
				.get(BocInvt.BOCINVT_BINDING_NICKNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accAlisText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accTypeText);
		String acc_account_num = String.valueOf(chooseBindingMap
				.get(BocInvt.BOCINVT_BINDING_ACCOUNTNUMBER_RES));
		accNumberText.setText(StringUtil.getForSixForString(acc_account_num));
		lastButton.setVisibility(View.GONE);
		sureButton.setOnClickListener(sureBtnClick);
	}

	/** 确定按钮监听事件 */
	OnClickListener sureBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
