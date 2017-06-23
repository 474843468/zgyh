package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 可购买大额存单详情
 * 
 * @author liuh
 * 
 */
public class LargeCDDetailActivity extends DeptBaseActivity {

	private LinearLayout tabcontent;
	private View view;
	/** 产品编号TextView */
	private TextView productCodeTv;
	/** 发售开始日期TextView */
	private TextView beginSellDateTv;
	/** 发售结束日期TextView */
	private TextView endSellDateTv;
	/** 产品剩余金额TextView */
	private TextView surplusAmount;
	/** 利率TextView */
	private TextView rateTv;
	/** 起购金额TextView */
	private TextView beginMoneyTv;
	/** 存期TextView */
	private TextView saveDateTv;
	/** 可用额度TextView */
	private TextView availableLimitTv;
	/** 购买Button */
	private Button buyBtn;

	/** 起购金额 */
	private String beginMoney;
	/** 大额存单详情 */
	private Map<String, Object> result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();

		if (!getIntentData()) {
			setViews();
			setListeners();
		} else {
			finish();
		}
	}

	private void setListeners() {
		buyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LargeCDDetailActivity.this, LargeCDBuyActivity.class);
				startActivity(intent);
			}
		});
	}

	private void setViews() {
		productCodeTv = (TextView) view.findViewById(R.id.tv_large_cd_product_code);
		beginSellDateTv = (TextView) view.findViewById(R.id.tv_large_cd_begin_sell_date);
		endSellDateTv = (TextView) view.findViewById(R.id.tv_large_cd_end_sell_date);
		rateTv = (TextView) view.findViewById(R.id.tv_large_cd_rate_two);
		beginMoneyTv = (TextView) view.findViewById(R.id.tv_large_cd_begin_money_two);
		saveDateTv = (TextView) view.findViewById(R.id.tv_large_cd_save_date);
		availableLimitTv = (TextView) view.findViewById(R.id.tv_large_cd_available_limit);
		buyBtn = (Button) view.findViewById(R.id.btn_buy_large_cd);

		//////////////////////////////////////////////////////////////////////////////
		// add by 2016年3月2日 产品剩余金额 修改为产品剩余金额
		surplusAmount = (TextView) view.findViewById(R.id.tv_large_cd_surplus_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, surplusAmount);
		//////////////////////////////////////////////////////////////////////////////
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rateTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, beginMoneyTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, availableLimitTv);

		String productCode = StringUtil.valueOf1((String) result.get(Dept.PRODUCT_CODE));
		String beginSellDate = StringUtil.valueOf1((String) result.get(Dept.SELL_START_DATE));
		String endSellDate = StringUtil.valueOf1((String) result.get(Dept.SELL_END_DATE));
		String rate = StringUtil.valueOf1((String) result.get(Dept.RATE));
		beginMoney = StringUtil.valueOf1((String) result.get(Dept.BEGIN_MONEY));
		// 基期
		String periodType = StringUtil.valueOf1((String) result.get(Dept.PERIOD_TYPE));
		// 可用额度
		String availableLimit = StringUtil.valueOf1((String) result.get(Dept.AVAIL_QUOTA));

		productCodeTv.setText(productCode);
		beginSellDateTv.setText(beginSellDate);
		endSellDateTv.setText(endSellDate);
		rateTv.setText(rate+"%"); // add by luqp 2016年3月4日 追加%号
		beginMoneyTv.setText(StringUtil.parseStringPattern(beginMoney, 2));

		if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_MONTH)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.month));
		} else if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_DAY)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.day));
		} else {
			saveDateTv.setText("-");
		}

		availableLimitTv.setText(StringUtil.parseStringPattern(availableLimit, 2));
		//////////////////////////////////////////////////////////////////////////////
		// add by 2016年3月2日 产品剩余金额 修改为产品剩余金额
		surplusAmount.setText(StringUtil.parseStringPattern(availableLimit, 2));
		//////////////////////////////////////////////////////////////////////////////
	}

	private boolean getIntentData() {
		DeptDataCenter data = DeptDataCenter.getInstance();
		result = (Map<String, Object>) data.getAvailableDetial();
		return StringUtil.isNullOrEmpty(result);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_add_title));

		LayoutInflater inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = inflater.inflate(R.layout.large_cd_available_detail, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}

	}

}
