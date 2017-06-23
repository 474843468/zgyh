package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitValidAccountAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;

/** 套餐查询---详情页面 */
public class RemitQueryDetailActivity extends TranBaseActivity {
	private static final String TAG = "RemitQueryDetailActivity";
	private View view;
	/** 签约账户 */
	// private TextView tran_remit_account;
	/** 套餐属性 */
	private TextView remit_type_view;
	/** 自动续约套餐类型 */
	private TextView remit_extension_type_view;
	/** 收款付费起点金额布局 */
	private View ll_amount_layout;
	/** 优惠后费用 */
	// private TextView coupon_amount_view;
	/** 收款付费起点金额 */
	private TextView tran_remit_amount_view;
	/** 是否自动续约布局 */
	private View extension_flag_layout;
	/** 是否自动续约 */
	private TextView rg_extension_flag_view;
	/** 手机号码 */
	private TextView tran_remit_phone;
	/** 生效日期 */
	private TextView startDateText = null;
	/** 截止日期 */
	private TextView endDateText = null;
	/** 签约渠道 */
	private TextView channelNameText = null;
	/** 总笔数 */
	private TextView totalBalance = null;
	/** 已用笔数 */
	private TextView overBalance = null;
	/** 剩余笔数 */
	private TextView useBalance = null;
	/** 共享账户列表 */
	private ListView lv_shareAcc;
	/** 共享账户列表 */
	private LinearLayout ll_sharedAcc;
	/** 共享账户布局 */
	private LinearLayout remit_detail_sharedAccount;
	// private Button detailButton = null;
	/** 共享账户adapter */
	private RemitValidAccountAdapter adapter;
	/** 用户选择的签约账户 */
	private int position = -1;
	private Map<String, Object> dateMap = null;
	private String accuntNumber = null;
	/** 选中记录项 */
	private int selectPosition = -1;
	/** 初始化ScrollView */
	private ScrollView scrollview;
	// private String currentTime = null;
	// /** 是否自动续约 */
	// private String autoFlag = null;
	// private String dateTime = null;
	// private LinearLayoutForListView hasAccListView = null;
	// private RemitQueryDetailAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getString(R.string.trans_remit_menu_three));
		view = mInflater.inflate(R.layout.tran_remit_search_detail, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		toprightBtn();
		dateMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TRAN_REMIT_QUERY_RESULT);
		selectPosition = getIntent().getIntExtra(ConstantGloble.FOREX_customerSelectedPosition, -1);
		if (selectPosition < 0) {
			return;
		}
		accuntNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		// dateTime = getIntent().getStringExtra(Comm.DATETME);
		// currentTime = QueryDateUtils.getcurrentDate(dateTime);
		if (StringUtil.isNullOrEmpty(dateMap)) {
			return;
		}
		init();
		setValue();
		dealDate();
	}

	private void init() {
		// tran_remit_account = (TextView)
		// view.findViewById(R.id.tran_remit_account);
		// coupon_amount_view = (TextView)
		// view.findViewById(R.id.coupon_amount);
		remit_type_view = (TextView) view.findViewById(R.id.sp_remit_type);
		remit_extension_type_view = (TextView) view.findViewById(R.id.remit_extension_type);
		ll_amount_layout = view.findViewById(R.id.ll_extension_amount);
		tran_remit_amount_view = (TextView) view.findViewById(R.id.tran_remit_amount);
		extension_flag_layout = view.findViewById(R.id.ll_extension_flag);
		rg_extension_flag_view = (TextView) view.findViewById(R.id.yes_or_no);
		tran_remit_phone = (TextView) view.findViewById(R.id.tran_remit_phone);
		startDateText = (TextView) view.findViewById(R.id.tv_remit_valuedate);
		endDateText = (TextView) view.findViewById(R.id.tv_remit_enddate);
		channelNameText = (TextView) view.findViewById(R.id.tv_remit_channel);
		totalBalance = (TextView) view.findViewById(R.id.tv_remit_totalbalance);
		overBalance = (TextView) view.findViewById(R.id.tv_remit_overBalance);
		useBalance = (TextView) view.findViewById(R.id.tv_remit_useBalance);
		scrollview = (ScrollView) view.findViewById(R.id.remit_scrollView);
		remit_detail_sharedAccount = (LinearLayout) view.findViewById(R.id.remit_detail_sharedAccount);
		lv_shareAcc = (ListView) view.findViewById(R.id.lv_sharedAcc);
		// ll_sharedAcc = (LinearLayout) view.findViewById(R.id.ll_sharedAcc);
		// ll_sharedAcc.setOnClickListener(sharedAccOnclick);
		// detailButton = (Button)
		// findViewById(R.id.forex_query_deal_detailes_ok);
		// detailButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (adapter != null && adapter.isSelected()) {
		// /**汇款套餐明细结果页面*/
		// Intent intent = new Intent(RemitQueryDetailActivity.this,
		// RemitQueryMealDetaiActivity.class);
		// intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition,
		// position);
		// startActivity(intent);
		// } else {
		// // TODO 没有选择账号
		// }
		// }
		// });
	}

	// private OnClickListener sharedAccOnclick = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// /** 汇款套餐明细结果页面 */
	// Intent intent = new Intent(RemitQueryDetailActivity.this,
	// RemitQueryMealDetaiActivity.class);
	// intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, position);
	// startActivity(intent);
	// }
	// };

	/** 得到共享账户 */
	private void dealDate() {
		List<Map<String, String>> validAccountList = (List<Map<String, String>>) dateMap.get(Tran.TRAN_VALIDACCOUNTLIST_RES);
		adapter = new RemitValidAccountAdapter(this, validAccountList);
		lv_shareAcc.setAdapter(adapter);
		Utils.setListViewHeightBasedOnChildren(lv_shareAcc);
		adapter.setSelectPosition(0);
		lv_shareAcc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				// if (selectposition == position) {
				// return;
				// }else{
				// selectposition = position;
				// adapter.setSelectPosition(position);
				// }

				// //////////////////////////////
				Intent intent = new Intent(RemitQueryDetailActivity.this, RemitQueryMealDetaiActivity.class);
				intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, position);
				intent.putExtra(ConstantGloble.CURRENT_POSITION, selectPosition);
				startActivity(intent);
			}
		});
	}

	private void setValue() {
		// 赋值
		Map<String, String> PaySetMealEntity = (Map<String, String>) dateMap.get(Tran.TRAN_PAYSETMEALENTITY_RES);
		// tran_remit_account.setText(StringUtil.getForSixForString(accuntNumber));
		remit_type_view.setText(RemitSetMealProducDic.getKeyFromValue((String) PaySetMealEntity
				.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY)));
		String[] split = ((String) PaySetMealEntity.get(Tran.TRAN_SIGNTYPE_RES)).split("/");
		String amount = split[2];
		String mount = amount.substring(0, amount.lastIndexOf(".") + 3);
		String extension_type_str = String.format("%s笔/%s月-%s元", split[0], split[1], mount);
		remit_extension_type_view.setText(extension_type_str);

		/** 根据套餐属性 显示 (收款付费起点金额布局 共享账户 ) */
		if (RemitSetMealProducDic.getTagFromValue((String) PaySetMealEntity.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY))) {
			remit_detail_sharedAccount.setVisibility(View.GONE);
			ll_amount_layout.setVisibility(View.VISIBLE);
		} else {
			remit_detail_sharedAccount.setVisibility(View.VISIBLE);
			ll_amount_layout.setVisibility(View.GONE);
		}

		tran_remit_amount_view.setText(StringUtil.parseStringPattern(PaySetMealEntity.get(Tran.TRAN_ORIGNAMOUNT), 2));
		// 续约布局根据账号是否支持续约进行显示隐藏
		extension_flag_layout.setVisibility("Y".equalsIgnoreCase(PaySetMealEntity.get("extensionFlag")) ? View.VISIBLE
				: View.GONE);
		// 用户是否选择续约
		rg_extension_flag_view.setText("Y".equalsIgnoreCase(PaySetMealEntity.get("extensionFlag")) ? "是" : "否");
		tran_remit_phone.setText(PaySetMealEntity.get("mobile"));
		startDateText.setText(PaySetMealEntity.get("startDate"));
		endDateText.setText(PaySetMealEntity.get("endDate"));
		channelNameText.setText(PaySetMealEntity.get("channelName"));
		totalBalance.setText(PaySetMealEntity.get("totalbalance"));
		overBalance.setText(getBiShu(PaySetMealEntity.get("totalbalance"), PaySetMealEntity.get("useBalance")));
		useBalance.setText(PaySetMealEntity.get("useBalance"));
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		scrollview.smoothScrollTo(0, 0);
//	}

	/** 得到已用笔数 */
	private String getBiShu(String blance, String bb) {
		int b1 = Integer.valueOf(blance);
		int b2 = Integer.valueOf(bb);
		return String.valueOf(b1 - b2);
	}
}
