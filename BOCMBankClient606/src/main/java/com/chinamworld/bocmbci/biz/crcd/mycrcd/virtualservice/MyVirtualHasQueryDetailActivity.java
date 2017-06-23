package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCrcdDetailActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟卡-----已出账单详情页面
 * 
 * @author huangyuchao
 * 
 */
public class MyVirtualHasQueryDetailActivity extends CrcdAccBaseActivity implements OnClickListener {

	private View view;

	private TextView finc_accNumber, finc_fenqidate, finc_miaoshus, finc_validatime, finc_remiannomoney, finc_nextdate,
			finc_huanluanmoney, finc_zhangdanriqi, finc_daoqiday;

	private Button lastButton, sureButton;
	String preix;

	int black;
	int gray;
	int white;
	private String virtualCardNo = null;
	Map<String, Object> detailMap = new HashMap<String, Object>();
	Map<String, Object> detailMap1 = new HashMap<String, Object>();

	String strCurrency1;
	String strCurrency2;

	LinearLayout ll_btnswitch;
	Button btn_renminbi, btn_waibi;

	Button shouButton, fuButton, btn_xinyonghuan, gohuiButton, moreButton;

	public static String cardType;

	public static String currentStrCurrency;
	/** 虚拟银行卡卡号 */
	TextView finc_virtualnumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_current_zhangdan_total));
		view = addView(R.layout.crcd_virtual_trans_details);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);

		black = this.getResources().getColor(R.color.black);
		gray = this.getResources().getColor(R.color.gray);
		white = this.getResources().getColor(R.color.white);
		virtualCardNo = getIntent().getStringExtra(Crcd.CRCD_VIRTUALCARDNO);
		init();
	}

	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public void init() {

		detailMap = MyVivtualQueryActivity.returnMap;
		detailMap1 = MyVivtualQueryActivity.returnMap1;

		finc_virtualnumber = (TextView) view.findViewById(R.id.finc_virtualnumber);

		strCurrency1 = LocalData.Currency.get(detailMap.get(Crcd.CRCD_CURRENCY));
		strCurrency2 = LocalData.Currency.get(detailMap1.get(Crcd.CRCD_CURRENCY));

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqidate);
		finc_miaoshus = (TextView) view.findViewById(R.id.finc_miaoshus);

		finc_validatime = (TextView) view.findViewById(R.id.finc_validatime);
		finc_remiannomoney = (TextView) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) view.findViewById(R.id.finc_nextdate);
		finc_huanluanmoney = (TextView) view.findViewById(R.id.finc_huanluanmoney);
		finc_zhangdanriqi = (TextView) view.findViewById(R.id.finc_zhangdanriqi);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_zhangdanriqi);
		finc_daoqiday = (TextView) view.findViewById(R.id.finc_daoqiday);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_daoqiday);
		finc_accNumber.setText(StringUtil.getForSixForString(MyVirtualBCListActivity.accountNumber));
		finc_virtualnumber.setText(StringUtil.getForSixForString(virtualCardNo));

		// 人民币完成
		renminbiFinish();

		lastButton = (Button) view.findViewById(R.id.lastButton);
		sureButton = (Button) view.findViewById(R.id.sureButton);
		shouButton = (Button) view.findViewById(R.id.shouButton);
		fuButton = (Button) view.findViewById(R.id.fuButton);
		btn_xinyonghuan = (Button) view.findViewById(R.id.btn_xinyonghuan);
		gohuiButton = (Button) view.findViewById(R.id.gohuiButton);
		moreButton = (Button) view.findViewById(R.id.moreButton);

		lastButton.setOnClickListener(this);
		sureButton.setOnClickListener(this);
		shouButton.setOnClickListener(this);
		fuButton.setOnClickListener(this);
		btn_xinyonghuan.setOnClickListener(this);
		gohuiButton.setOnClickListener(this);
		moreButton.setOnClickListener(this);

		ll_btnswitch = (LinearLayout) findViewById(R.id.ll_btnswitch);
		btn_renminbi = (Button) findViewById(R.id.btn_renminbi);
		btn_waibi = (Button) findViewById(R.id.btn_waibi);

		if (!StringUtil.isNullOrEmpty(strCurrency1) && StringUtil.isNullOrEmpty(strCurrency2)) {
			btn_renminbi.setVisibility(View.VISIBLE);
			btn_renminbi.setText(strCurrency1);
			btn_waibi.setVisibility(View.GONE);
		} else if (StringUtil.isNullOrEmpty(strCurrency1) && !StringUtil.isNullOrEmpty(strCurrency2)) {
			btn_renminbi.setVisibility(View.GONE);
			btn_waibi.setVisibility(View.VISIBLE);
			btn_waibi.setText(strCurrency2);
		} else if (!StringUtil.isNullOrEmpty(strCurrency1) && !StringUtil.isNullOrEmpty(strCurrency2)) {
			btn_renminbi.setVisibility(View.VISIBLE);
			btn_renminbi.setText(strCurrency1);
			btn_waibi.setVisibility(View.VISIBLE);
			btn_waibi.setText(strCurrency2);
		}

		cardType = MyVirtualBCListActivity.accountType;

		currentStrCurrency = strCurrency1;
		btn_renminbi.setOnClickListener(renminbiClick);
		btn_waibi.setOnClickListener(waibiClick);
	}

	public void showButton() {
		// 最近一个月才显示
		if (MyCrcdDetailActivity.isLastMonth) {
			// 长城
			if (GREATWALL.equals(cardType)) {
				if (!StringUtil.isNullOrEmpty(strCurrency1) && currentStrCurrency.equals(strCurrency1)) {
					sureButton.setVisibility(View.VISIBLE);
					moreButton.setVisibility(View.VISIBLE);

					shouButton.setVisibility(View.GONE);
					fuButton.setVisibility(View.GONE);
				} else if (!StringUtil.isNullOrEmpty(strCurrency2) && currentStrCurrency.equals(strCurrency2)) {
					sureButton.setVisibility(View.GONE);
					moreButton.setVisibility(View.GONE);

					shouButton.setVisibility(View.VISIBLE);
					fuButton.setVisibility(View.VISIBLE);
				}
			}
			// 中银
			else if (ZHONGYIN.equals(cardType)) {
				if (!StringUtil.isNullOrEmpty(strCurrency1) && currentStrCurrency.equals(strCurrency1)) {
					sureButton.setVisibility(View.VISIBLE);
					moreButton.setVisibility(View.VISIBLE);

					btn_xinyonghuan.setVisibility(View.GONE);
					gohuiButton.setVisibility(View.GONE);
				} else if (!StringUtil.isNullOrEmpty(strCurrency2) && currentStrCurrency.equals(strCurrency2)) {
					sureButton.setVisibility(View.GONE);
					moreButton.setVisibility(View.GONE);

					btn_xinyonghuan.setVisibility(View.VISIBLE);
					gohuiButton.setVisibility(View.VISIBLE);
				}
			}
			// 单外币
			else if (SINGLEWAIBI.equals(cardType)) {
				gohuiButton.setVisibility(View.VISIBLE);
			}
		}
		// 不是最近一个月，全部隐藏
		else {
			sureButton.setVisibility(View.GONE);
		}
	}

	OnClickListener renminbiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			btn_renminbi.setBackgroundResource(R.drawable.btn_tranoselect);
			btn_renminbi.setTextColor(black);
			btn_waibi.setBackgroundResource(R.drawable.btn_transelect);
			btn_waibi.setTextColor(white);
			currentStrCurrency = strCurrency1;
			renminbiFinish();
		}
	};

	OnClickListener waibiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			btn_renminbi.setBackgroundResource(R.drawable.btn_transelect);
			btn_renminbi.setTextColor(white);
			btn_waibi.setBackgroundResource(R.drawable.btn_tranoselect);
			btn_waibi.setTextColor(black);
			currentStrCurrency = strCurrency2;
			waibiFinish();
		}
	};

	public void renminbiFinish() {
		if (!StringUtil.isNullOrEmpty(detailMap)) {
			finc_fenqidate.setText(strCurrency1);
			finc_miaoshus.setText(StringUtil.parseStringCodePattern(String.valueOf(detailMap.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap.get(Crcd.CRCD_LASTBALANCE)), 2));
			finc_validatime.setText(StringUtil.parseStringCodePattern(
					String.valueOf(detailMap.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap.get(Crcd.CRCD_TOTALOUT)), 2));
			finc_remiannomoney.setText(StringUtil.parseStringCodePattern(
					String.valueOf(detailMap.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap.get(Crcd.CRCD_TOTALIN)), 2));
			finc_nextdate.setText(StringUtil.parseStringCodePattern(String.valueOf(detailMap.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap.get(Crcd.CRCD_CURRENTBALANCE)), 2));
			finc_huanluanmoney.setText(StringUtil.parseStringCodePattern(
					String.valueOf(detailMap.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap.get(Crcd.CRCD_MINPAYMENTAMOUNT)), 2));

			String billDate = String.valueOf(detailMap.get(Crcd.CRCD_BILLDATE));
			finc_zhangdanriqi.setText(StringUtil.valueOf1(billDate));

			String dueDate = String.valueOf(detailMap.get(Crcd.CRCD_DUEDATE));
			finc_daoqiday.setText(StringUtil.valueOf1(dueDate));

		}

	}

	public void waibiFinish() {
		if (!StringUtil.isNullOrEmpty(detailMap1)) {
			finc_fenqidate.setText(strCurrency2);
			finc_miaoshus.setText(StringUtil.parseStringCodePattern(String.valueOf(detailMap1.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap1.get(Crcd.CRCD_LASTBALANCE)), 2));

			finc_validatime.setText(StringUtil.parseStringCodePattern(
					String.valueOf(detailMap1.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap1.get(Crcd.CRCD_TOTALOUT)), 2));
			finc_remiannomoney.setText(StringUtil.parseStringCodePattern(
					String.valueOf(detailMap1.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap1.get(Crcd.CRCD_TOTALIN)), 2));
			finc_nextdate.setText(StringUtil.parseStringCodePattern(String.valueOf(detailMap1.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap1.get(Crcd.CRCD_CURRENTBALANCE)), 2));
			finc_huanluanmoney.setText(StringUtil.parseStringCodePattern(
					String.valueOf(detailMap1.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(detailMap1.get(Crcd.CRCD_MINPAYMENTAMOUNT)), 2));
			String billDate = String.valueOf(detailMap1.get(Crcd.CRCD_BILLDATE));
			finc_zhangdanriqi.setText(StringUtil.valueOf1(billDate));

			String dueDate = String.valueOf(detailMap1.get(Crcd.CRCD_DUEDATE));
			finc_daoqiday.setText(StringUtil.valueOf1(dueDate));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
