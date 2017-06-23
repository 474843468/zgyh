package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitSharedAccConfirmAdapter;
import com.chinamworld.bocmbci.utils.Dictionary;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.LinearListView;

/**
 * 套餐签约成功信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitSetMealSuccessActivity extends TranBaseActivity {
	/** 成功信息页面 */
	private View view;
	/** 签约账户 */
	private TextView tran_remit_account;
	/** 套餐属性 */
	private TextView remit_type_view;
	/** 自动续约套餐类型 */
	private TextView remit_extension_type_view;
	/** 收款付费起点金额布局 */
	private View ll_amount_layout;
	/** 优惠后费用 */
	private TextView coupon_amount_view;
	/** 收款付费起点金额 */
	private TextView tran_remit_amount_view;
	/** 是否自动续约布局 */
	private View extension_flag_layout;
	/** 是否自动续约 */
	private TextView rg_extension_flag_view;
	/** 手机号码 */
	private TextView tran_remit_phone;
	/** 生效日期 */
	private TextView tv_remit_valuedate;
	/** 共享账户列表 */
	private LinearListView lv_shareAcc;
	/** 下一步 */
	private Button nextButton;

	/** 共享账户 */
	private List<Map<String, String>> shareAccList = new ArrayList<Map<String, String>>();
	/** 汇款笔数套餐签约交易数据 */
	private Map<String, Object> remitinputMap;
	/** 选择的签约账户 */
	private Map<String, Object> chooseMap;
	/** 优惠后费用 */
	private String afterThePreferentialFee;
	/** 套餐类型 */
	private Dictionary<String, String, Map<String, Object>> remitSetMealTypeResDic;
	/**是否上传共享账户*/
	private boolean isShowShareAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_one));
		toprightBtn();
		back.setVisibility(View.GONE);
		// 添加布局
		view = addView(R.layout.tran_remit_setmeal_success);
		afterThePreferentialFee = getIntent().getStringExtra(Tran.AfterThePreferentialFee_preCommissionCharge);
		isShowShareAccount  = getIntent().getBooleanExtra("isShowShareAccount", false);
		shareAccList = TranDataCenter.getInstance().getShareAccountList();
		remitSetMealTypeResDic = TranDataCenter.getInstance().getMealTypeResDic();
		chooseMap = TranDataCenter.getInstance().getAtmChooseMap();
		remitinputMap = TranDataCenter.getInstance().getShareInputMap();
		dateTime = (String) remitinputMap.get(Tran.TRAN_REMIT_APP_EFFECTTIVEDATE_REQ);
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {

		tran_remit_account = (TextView) view.findViewById(R.id.tran_remit_account);
		remit_type_view = (TextView) view.findViewById(R.id.sp_remit_type);
		remit_extension_type_view = (TextView) view.findViewById(R.id.remit_extension_type);
		coupon_amount_view = (TextView) view.findViewById(R.id.coupon_amount);
		ll_amount_layout = view.findViewById(R.id.ll_extension_amount);
		tran_remit_amount_view = (TextView) view.findViewById(R.id.tran_remit_amount);
		extension_flag_layout = view.findViewById(R.id.ll_extension_flag);
		rg_extension_flag_view = (TextView) view.findViewById(R.id.yes_or_no);
		tran_remit_phone = (TextView) view.findViewById(R.id.tran_remit_phone);
		tv_remit_valuedate = (TextView) view.findViewById(R.id.tv_remit_valuedate);
		lv_shareAcc = (LinearListView) view.findViewById(R.id.lv_sharedAcc);
		nextButton = (Button) view.findViewById(R.id.remit_input_next_btn);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,(TextView)findViewById(R.id.tran_remit_amount_lable));
		
		nextButton.setOnClickListener(nextListener);

		// 赋值
		String account = (String) chooseMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		tran_remit_account.setText(StringUtil.getForSixForString(account));
		remit_type_view.setText(RemitSetMealProducDic.getKeyFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ)));
		String extension_type_str = remitSetMealTypeResDic.getKeyFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCTTYPE_REQ));
		remit_extension_type_view.setText(extension_type_str);
		String charge = this.getIntent().getStringExtra(Tran.TRAN_REMIT_APP_FINALCOMMISSIONCHARGE_RES);
		/** 优惠后的费用*/
//		coupon_amount_view.setText(StringUtil.parseStringPattern(afterThePreferentialFee, 2));
		
		/** 优惠后的费用 */
		if (!StringUtil.isNull(afterThePreferentialFee)) {
			coupon_amount_view.setText(StringUtil.parseStringPattern(afterThePreferentialFee, 2));		
		}else{
			coupon_amount_view.setText("0.00");
		}
		
		ll_amount_layout.setVisibility(RemitSetMealProducDic.getTagFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ)) ? View.VISIBLE : View.GONE);
		if (remitinputMap.get(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ) != null && !remitinputMap.get(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ).equals("0")) {
			tran_remit_amount_view.setText(StringUtil.parseStringPattern((String) remitinputMap.get(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ),2));
		}else{
			tran_remit_amount_view.setText("0.00");
		}
		
		
		// 续约布局根据账号是否支持续约进行显示隐藏
		Map<String, Object> tag2 = (Map<String, Object>) remitSetMealTypeResDic.getTagFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCTTYPE_REQ));
		String flag = (String) tag2.get(Tran.MealTypeQuery_remitSetMealautoFlag);
		extension_flag_layout.setVisibility("Y".equalsIgnoreCase(flag) ? View.VISIBLE : View.GONE);
		// 用户是否选择续约
		rg_extension_flag_view.setText(extensionTypeFlag.get((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_EXTENSIONFLAG_REQ)));
		tran_remit_phone.setText((String) remitinputMap.get(Tran.TRAN_REMIT_APP_PHONENUMBER_REQ));
		tv_remit_valuedate.setText((String) remitinputMap.get(Tran.TRAN_REMIT_APP_EFFECTTIVEDATE_REQ));

		LinearLayout ll_sharedAcc = (LinearLayout) view.findViewById(R.id.ll_sharedAcc);
		/** 共享账户列表 (根据返回结果反显 付款方套餐 并且不等于空显示 收款方套餐和双向套餐则不显示) */
		if (RemitSetMealProducDic.getTagFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ))) {
				ll_sharedAcc.setVisibility(View.GONE);
		} else {
			if (!StringUtil.isNullOrEmpty(shareAccList) && isShowShareAccount) {
				RemitSharedAccConfirmAdapter adapter = new RemitSharedAccConfirmAdapter(RemitSetMealSuccessActivity.this,
						shareAccList);
				lv_shareAcc.setAdapter(adapter);
				ll_sharedAcc.setVisibility(View.VISIBLE);
			} else {
				ll_sharedAcc.setVisibility(View.GONE);
			}
		}
	}

	OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent(RemitSetMealSuccessActivity.this, RemitThirdMenu.class);
			startActivity(intent);
		}
	};

	@Override
	public void onBackPressed() {
	}
}
