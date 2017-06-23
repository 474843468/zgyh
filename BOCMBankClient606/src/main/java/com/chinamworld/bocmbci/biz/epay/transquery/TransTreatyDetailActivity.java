package com.chinamworld.bocmbci.biz.epay.transquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TQConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.transquery.adapter.OtherPayInfoAdapter;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 支付交易查询-协议支付详情页面
 * 
 * @author Administrator
 * 
 */
public class TransTreatyDetailActivity extends EPayBaseActivity {

	private View tranDetail;

	private TextView tv_pay_time;
	private TextView tv_order_no;
	private TextView tv_agreement_seq;
	private TextView tv_merchant_name;
	private TextView tv_pay_card_no;
	private TextView tv_pay_card_type;
	private TextView tv_cur_code;
	private TextView tv_order_amount;
	private TextView tv_order_note;
	private TextView tv_order_status;
	private TextView tv_pay_type;
	private TextView tv_order_type;
	private TextView tv_zhongduan;
	private TextView tv_fenqishu;

	private RelativeLayout rl_pay_content;
	private RelativeLayout rl_back_content;

	private ViewPager vp_pay_content;
	private ViewPager vp_back_content;

	private LinearLayout pay_page_dot;
	private LinearLayout back_page_dot;

	private Button bt_ensure;
	private Context tqTransContext;

	private List<Object> orderPayInfoList;

	private String payTime;
	private String orderNo;
	private String agreementSeq;
	private String merchantName;
	private String holderMerId;
	private String payCardNo;
	private String payCardType;
	private String curCode;
	private String orderAmount;
	private String orderNote;
	
	private String tranStatus;//交易状态
	private String orderStatus;
	private String payType;
	private String orderType;
	private String zhongduan;
	private String fenqishu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		tqTransContext = TransContext.getTQTransContext();
		tranDetail = LayoutInflater.from(this).inflate(
				R.layout.epay_tq_treaty_detail, null);

		super.setType(1);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_TRANS_QUERY);
		super.setContentView(tranDetail);
		super.onCreate(savedInstanceState);

		// 初始化当前页面内容
		initCurPage();
	}

	private void initCurPage() {
		tv_pay_time = (TextView) tranDetail.findViewById(R.id.tv_pay_time);
		tv_order_no = (TextView) tranDetail.findViewById(R.id.tv_order_no);
		tv_agreement_seq = (TextView) tranDetail
				.findViewById(R.id.tv_agreement_seq);
		tv_merchant_name = (TextView) tranDetail
				.findViewById(R.id.tv_merchant_name);
		tv_pay_card_no = (TextView) tranDetail
				.findViewById(R.id.tv_pay_card_no);
		tv_pay_card_type = (TextView) tranDetail
				.findViewById(R.id.tv_pay_card_type);
		tv_cur_code = (TextView) tranDetail.findViewById(R.id.tv_cur_code);
		tv_order_amount = (TextView) tranDetail
				.findViewById(R.id.tv_order_amount);
		tv_order_note = (TextView) tranDetail.findViewById(R.id.tv_order_note);
		tv_order_status = (TextView) tranDetail
				.findViewById(R.id.tv_order_status);
		tv_pay_type = (TextView) tranDetail.findViewById(R.id.tv_pay_type);
		tv_order_type = (TextView) tranDetail.findViewById(R.id.tv_order_type);
		tv_zhongduan = (TextView) tranDetail.findViewById(R.id.tv_zhongduan);
		tv_fenqishu = (TextView) tranDetail.findViewById(R.id.tv_fenqishu);

		rl_pay_content = (RelativeLayout) tranDetail
				.findViewById(R.id.rl_pay_content);
		rl_back_content = (RelativeLayout) tranDetail
				.findViewById(R.id.rl_back_content);

		vp_pay_content = (ViewPager) tranDetail
				.findViewById(R.id.vp_pay_content);
		vp_back_content = (ViewPager) tranDetail
				.findViewById(R.id.vp_back_content);

		pay_page_dot = (LinearLayout) tranDetail
				.findViewById(R.id.pay_page_dot);
		back_page_dot = (LinearLayout) tranDetail
				.findViewById(R.id.back_page_dot);

		bt_ensure = (Button) tranDetail.findViewById(R.id.bt_ensure);
		bt_ensure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getTransData();
	}

	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		Map<Object, Object> selectedResult = tqTransContext
				.getMap(TQConstants.PUB_SELECTED_RESULT);

		orderNo = EpayUtil
				.getString(
						selectedResult
								.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_NO),
						"");
		payCardNo = EpayUtil.getString(selectedResult.get("cardNo"), "");
		payCardType = EpayUtil.getString(selectedResult.get("cardType"), "");
		payTime = EpayUtil.getString(selectedResult.get("payTime"), "");
		curCode = EpayUtil.getString(selectedResult.get("currencyCode"), "");
		orderAmount = EpayUtil.getString(selectedResult.get("orderAmount"), "");
		payType = EpayUtil.getString(selectedResult.get("payMethod"), "");
		orderStatus = EpayUtil.getString(selectedResult.get("orderStatus"), "");
		
		tranStatus= EpayUtil.getString(selectedResult.get("tranStatus"), "");
		orderType = EpayUtil.getString(selectedResult.get("tranCode"), "");
		zhongduan = EpayUtil.getString(selectedResult.get("teminalFlag"), "");
		agreementSeq = EpayUtil.getString(selectedResult.get("transactionId"),
				"");
		merchantName = EpayUtil.getString(selectedResult.get("merchantName"),
				"");
		fenqishu = EpayUtil.getString(selectedResult.get("planNumber"), "");
		orderNote = EpayUtil.getString(selectedResult.get("remark"), "");

		holderMerId = EpayUtil
				.getString(
						selectedResult
								.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_HOLDER_MERID),
						"");

		orderPayInfoList = EpayUtil
				.getList(selectedResult
						.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_PAY_INFO_LIST));
		// if(!orderPayInfoList.isEmpty()) {
		// initOtherInfo();
		// }
		initDisplay();
	}

	private void initOtherInfo() {
		List<Map<Object, Object>> iPayInfoList = new ArrayList<Map<Object, Object>>();
		List<Map<Object, Object>> backInfoList = new ArrayList<Map<Object, Object>>();

		// 获取支付信息/退货信息
		for (int i = 0; i < orderPayInfoList.size(); i++) {
			Map<Object, Object> payInfo = EpayUtil.getMap(orderPayInfoList
					.get(i));

			boolean backFlag = EpayUtil
					.getBoolean(payInfo
							.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_BACK_FLAG));

			if (backFlag) {
				backInfoList.add(payInfo);
			} else {
				iPayInfoList.add(payInfo);
			}
		}
		// 如果支付信息列表不为空
		if (!iPayInfoList.isEmpty()) {
			ArrayList<View> iPayInfoViews = new ArrayList<View>();
			final ArrayList<TextView> payDotList = new ArrayList<TextView>();
			ArrayList<String> titles = new ArrayList<String>();
			for (int i = 0; i < iPayInfoList.size(); i++) { // 初始View列表
				Map<Object, Object> info = iPayInfoList.get(i);
				View payDetail = LayoutInflater.from(this).inflate(
						R.layout.epay_tq_pay_detail_item, null);

				TextView tv_pay_transaction_id = (TextView) payDetail
						.findViewById(R.id.tv_pay_transaction_id);
				tv_pay_transaction_id
						.setText(EpayUtil.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRANSACTION_ID),
								""));

				TextView tv_pay_return_time = (TextView) payDetail
						.findViewById(R.id.tv_pay_return_time);
				tv_pay_return_time
						.setText(EpayUtil.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_RETURN_TIME),
								""));

				TextView tv_pay_tran_amount = (TextView) payDetail
						.findViewById(R.id.tv_pay_tran_amount);
				String amount = EpayUtil
						.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRANA_MOUNT),
								"");
				tv_pay_tran_amount.setText(StringUtil.parseStringPattern(
						amount, 2));

				TextView tv_pay_currency = (TextView) payDetail
						.findViewById(R.id.tv_pay_currency);
				String currency = EpayUtil
						.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_CURRENCY),
								"");
				tv_pay_currency.setText(LocalData.Currency.get(currency));

				TextView tv_pay_tran_status = (TextView) payDetail
						.findViewById(R.id.tv_pay_tran_status);
				String status = EpayUtil
						.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRAN_STATUS),
								"");
				tv_pay_tran_status.setText(TQConstants.OTHER_PAY_STATUS
						.get(status));
				titles.add((i + 1) + "");
				iPayInfoViews.add(payDetail);
			}
			// 创建ViewPager 适配器
			OtherPayInfoAdapter payInfoAdapter = new OtherPayInfoAdapter(
					titles, iPayInfoViews, this);
			// 判断支付信息View列表数量是否大于 1
			if (1 < iPayInfoViews.size()) {

				for (int i = 0; i < iPayInfoViews.size(); i++) {
					TextView img = new TextView(this);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							10, 10);
					params.setMargins(3, 0, 3, 0);
					if (i == 0)
						img.setBackgroundResource(R.drawable.current_page);
					else
						img.setBackgroundResource(R.drawable.other_page);
					img.setLayoutParams(params);
					pay_page_dot.addView(img);
					payDotList.add(img);
				}

			} else {
				// 隐藏支付页面
				pay_page_dot.setVisibility(View.GONE);
			}

			vp_pay_content.setAdapter(payInfoAdapter);
			vp_pay_content
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							for (int i = 0; i < payDotList.size(); i++) {
								if (i == position)
									payDotList.get(i).setBackgroundResource(
											R.drawable.current_page);
								else
									payDotList.get(i).setBackgroundResource(
											R.drawable.other_page);
							}
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {
						}

						@Override
						public void onPageScrollStateChanged(int arg0) {
						}
					});

			rl_pay_content.setVisibility(View.VISIBLE);

		} else {// 如果支付信息列表为空
			rl_pay_content.setVisibility(View.GONE);
		}

		if (!backInfoList.isEmpty()) {// 判读退货信息列表是否为空
			ArrayList<View> backInfoViews = new ArrayList<View>();
			final ArrayList<TextView> backDotViews = new ArrayList<TextView>();

			for (int i = 0; i < backInfoList.size(); i++) {
				Map<Object, Object> info = backInfoList.get(i);
				View backDetail = LayoutInflater.from(this).inflate(
						R.layout.epay_tq_back_detail_item, null);

				TextView tv_back_transaction_id = (TextView) backDetail
						.findViewById(R.id.tv_back_transaction_id);
				tv_back_transaction_id
						.setText(EpayUtil.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRANSACTION_ID),
								""));

				TextView tv_back_return_time = (TextView) backDetail
						.findViewById(R.id.tv_back_return_time);
				tv_back_return_time
						.setText(EpayUtil.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_RETURN_TIME),
								""));

				TextView tv_back_tran_amount = (TextView) backDetail
						.findViewById(R.id.tv_back_tran_amount);
				String amount = EpayUtil
						.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRANA_MOUNT),
								"");
				tv_back_tran_amount.setText(StringUtil.parseStringPattern(
						amount, 2));

				TextView tv_back_currency = (TextView) backDetail
						.findViewById(R.id.tv_back_currency);
				String currency = EpayUtil
						.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_CURRENCY),
								"");
				tv_back_currency.setText(LocalData.Currency.get(currency));

				TextView tv_back_tran_status = (TextView) backDetail
						.findViewById(R.id.tv_back_tran_status);
				String status = EpayUtil
						.getString(
								info.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRAN_STATUS),
								"");
				tv_back_tran_status.setText(TQConstants.OTHER_PAY_STATUS
						.get(status));

				backInfoViews.add(backDetail);
			}

			OtherPayInfoAdapter backInfoAdapter = new OtherPayInfoAdapter(
					backInfoViews, this);

			if (1 < backInfoViews.size()) {

				for (int i = 0; i < backInfoViews.size(); i++) {
					TextView img = new TextView(this);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							10, 10);
					params.setMargins(3, 0, 3, 0);
					if (i == 0)
						img.setBackgroundResource(R.drawable.current_page);
					else
						img.setBackgroundResource(R.drawable.other_page);
					img.setLayoutParams(params);
					back_page_dot.addView(img);
					backDotViews.add(img);
				}

			} else {
				back_page_dot.setVisibility(View.GONE);
			}

			vp_back_content.setAdapter(backInfoAdapter);
			vp_back_content
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							for (int i = 0; i < backDotViews.size(); i++) {
								if (i == position)
									backDotViews.get(i).setBackgroundResource(
											R.drawable.current_page);
								else
									backDotViews.get(i).setBackgroundResource(
											R.drawable.other_page);
							}
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {
						}

						@Override
						public void onPageScrollStateChanged(int arg0) {
						}
					});

			rl_back_content.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 设置页面显示内容
	 */
	private void initDisplay() {
		if (!StringUtil.isNull(payTime) && payTime.length() >= 14) {
			payTime = payTime.substring(0, 4) + "/" + payTime.substring(4, 6)
					+ "/" + payTime.substring(6, 8) + " "
					+ payTime.substring(8, 10) + ":"
					+ payTime.substring(10, 12) + ":"
					+ payTime.substring(12, 14);
		}
		tv_pay_time.setText(payTime);
		tv_order_no.setText(orderNo);
		tv_agreement_seq.setText(agreementSeq);
		tv_merchant_name.setText(merchantName);
		tv_pay_card_no.setText(StringUtil.getForSixForString(payCardNo));
		tv_pay_card_type.setText(LocalData.AccountType.get(payCardType));
		tv_cur_code.setText(LocalData.Currency.get(curCode));
		tv_order_amount.setText(StringUtil.parseStringPattern(orderAmount, 2));
		tv_order_note.setText(orderNote);
//		tv_order_status
//				.setText(TQConstants.OTHER_PAY_STATUS.get(orderStatus));
		
		tv_order_status
		.setText(TQConstants.OTHER_PAY_STATUS.get(tranStatus));
		
		tv_pay_type.setText(TQConstants.PAY_TYPES.get(payType));
		tv_order_type.setText(TQConstants.ORDER_TYPES.get(orderType));
		tv_zhongduan.setText(TreatyConstants.TREATY_AGENT.get(zhongduan));
		if(StringUtil.isNull(fenqishu)){
			tv_fenqishu.setText("一次性全额支付");
		} else {
		 tv_fenqishu.setText(fenqishu);
		}

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_pay_time);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_order_no);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_agreement_seq);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_merchant_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_pay_card_no);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_pay_card_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_cur_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_order_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_order_note);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_order_status);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_fenqishu);
	}

}
