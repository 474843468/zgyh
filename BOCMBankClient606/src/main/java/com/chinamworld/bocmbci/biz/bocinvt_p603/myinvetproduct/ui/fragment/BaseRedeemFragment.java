package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.Trans2ChineseNumber;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财产品赎回 页面基类
 * 
 * @author HVZHUNG
 *
 */
public abstract class BaseRedeemFragment extends Fragment implements
		OnCheckedChangeListener, OnClickListener {
	private static final String TAG = BaseRedeemFragment.class.getSimpleName();
	public static final String KEY_INFO = "info";

	/** 是否允许撤单 */
	private LabelTextView lvt_redeem_revoke;
	/** 持有份额 */
	private LabelTextView ltv_hold_quantity;
	/** 可用份额 */
	private LabelTextView ltv_usable_quantity;
	/** 购回份额 */
	private EditText et_redeem_quantity;
	/** 赎回方式选择 */
	private RadioGroup rg_redeem_way;

	/** 全部赎回 */
	private TextView tv_redeem_all;
	/** 日期选择 */
	private TextView tv_choose_date;
	/** 输入金额的大写显示 */
	private TextView tv_chinese_numeral;
	private BOCProductForHoldingInfo info;

	/**
	 * 是否全部赎回
	 */
	private boolean isRedeemAll = false;
	/**产品详情 */
	private Map<String, Object> responseDeal;
	/** 赎回开放日期*/
	private String remitDate;
	/** 赎回开放日期*/
	private TextView tv_remitDate;
	/** 日期选择 */
	private LinearLayout choose_date_layout;
	/** 赎回开放日期 */
	private LinearLayout remit_date_layout;
	/** 系统时间 */
	private String currenttime;
	/** 指定日期按钮 */
	private RadioButton rb_assign_date;
	/** 立即申请按钮 */
	private RadioButton rb_redeem_now;
	/** 是否允许部分赎回*/
	private LinearLayout redeem_layout;
	/** 赎回份额*/
	private TextView redeem_all;
	public static BaseRedeemFragment newInstance(BOCProductForHoldingInfo info) {
		BaseRedeemFragment instance;

		if (HoldingBOCProductInfoUtil.isValueType(info)) {
			instance = new RedeemForValueFragment();
		} else {
			instance = new RedeemForNoValueFragment();
		}
		Bundle data = new Bundle();
		data.putSerializable(KEY_INFO, info);
		instance.setArguments(data);
		return instance;
	}

	/**
	 * 初始化净值型，非净值型赎回共同部分的View，注意view需要包含bocinvt_redeem_common_view_p603
	 * 
	 * <code>
	 *  <include
	 *             android:layout_width="match_parent"
	 *             android:layout_height="wrap_content"
	 *             layout="@layout/bocinvt_redeem_common_view_p603" />
	 * </code>
	 * 
	 * @param view
	 */
	protected void initCommonView(View view) {
		lvt_redeem_revoke = (LabelTextView) view
				.findViewById(R.id.lvt_redeem_revoke);
		ltv_hold_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_hold_quantity);
		ltv_hold_quantity.setValueTextColor(TextColor.Red);
		ltv_usable_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_usable_quantity);
		ltv_usable_quantity.setValueTextColor(TextColor.Red);
		et_redeem_quantity = (EditText) view
				.findViewById(R.id.et_redeem_quantity);
		rg_redeem_way = (RadioGroup) view.findViewById(R.id.rg_redeem_way);
		rb_assign_date = (RadioButton) view.findViewById(R.id.rb_assign_date);
		rb_redeem_now = (RadioButton) view.findViewById(R.id.rb_redeem_now);
		rg_redeem_way.setOnCheckedChangeListener(this);
		tv_redeem_all = (TextView) view.findViewById(R.id.tv_redeem_all);
		tv_redeem_all.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		tv_redeem_all.getPaint().setAntiAlias(true);// 抗锯齿
		tv_redeem_all.setOnClickListener(this);

		tv_choose_date = (TextView) view.findViewById(R.id.tv_choose_date);
		tv_choose_date.setOnClickListener(this);

		Calendar c = Calendar.getInstance();
		tv_choose_date.setText(dateFormat.format(c.getTime()));

		tv_chinese_numeral = (TextView) view
				.findViewById(R.id.tv_chinese_numeral);
		tv_chinese_numeral.setVisibility(View.GONE);
		Trans2ChineseNumber.relateNumInputAndChineseShower(et_redeem_quantity,
				tv_chinese_numeral);
		tv_remitDate = (TextView) view.findViewById(R.id.tv_remitDate);
		choose_date_layout = (LinearLayout) view.findViewById(R.id.choose_date_layout);
		remit_date_layout = (LinearLayout) view.findViewById(R.id.remit_date_layout);
		redeem_layout = (LinearLayout) view.findViewById(R.id.redeem_layout);
		redeem_all = (TextView) view.findViewById(R.id.redeem_all);
		
	}

	/**
	 * 设置净值型非净值型共同部分显示内容
	 * 
	 * @param info
	 */
	protected void setCommonViewContent(BOCProductForHoldingInfo info) {
		if (info == null) {
			LogGloble.e(TAG, "BOCProductInfo is null!!!");
			return;
		}
		responseDeal = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		this.info = info;
		setMessage();
//		remitDate = "赎回开放日期为："+(String)responseDeal.get("redEmptionStartDate")+"至"+(String)responseDeal.get("redEmptionEndDate");
//		tv_remitDate.setText(remitDate);
//		lvt_redeem_revoke.setValueText(getResources().getString(
//				HoldingBOCProductInfoUtil.canRevoke(info) ? R.string.yes
//						: R.string.no));
//		lvt_redeem_revoke.setValueText(LocalData.isCanCancleStr.get((String)responseDeal.get(BocInvt.BOCINVT_ISCANCANCLE_RES)));
		if(((String)responseDeal.get("appdatered")).equals("0")){//是否允许指定日期赎回 0 否 1是
			rb_assign_date.setVisibility(View.GONE);
			rb_redeem_now.setVisibility(View.GONE);
		}
		ltv_hold_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyHoldingQuantity(info));
		ltv_usable_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyAvailableQuantity(info));
		// 默认选中的赎回方式
		if (rg_redeem_way.getCheckedRadioButtonId() == -1)
			rg_redeem_way.check(R.id.rb_redeem_now);
		tv_choose_date.setText(QueryDateUtils.getOneDayLater(BociDataCenter.getInstance().getDateTime("dateTime")));
		redeem_layout.setVisibility(info.canPartlyRedeem.equals("0")?View.VISIBLE:View.GONE);
		redeem_all.setVisibility(info.canPartlyRedeem.equals("0")?View.GONE:View.VISIBLE);
		if(redeem_all.getVisibility() == View.VISIBLE){
			isRedeemAll = true;
			redeem_all.setText(HoldingBOCProductInfoUtil.getFriendlyHoldingQuantity(info));
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_redeem_now:
			tv_choose_date.setVisibility(View.INVISIBLE);
			choose_date_layout.setVisibility(View.GONE);
			remit_date_layout.setVisibility(View.GONE);
			BociDataCenter.isRedeem = false;
			break;
		case R.id.rb_assign_date:
			tv_choose_date.setVisibility(View.VISIBLE);
			choose_date_layout.setVisibility(View.VISIBLE);
			remit_date_layout.setVisibility(View.VISIBLE);
			BociDataCenter.isRedeem = true;
			break;
		default:
			break;
		}
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			isRedeemAll = false;
			tv_chinese_numeral.removeTextChangedListener(this);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_redeem_all:// 全部赎回
			if (info == null) {
				return;
			}
//			DecimalFormat decimalFormat = new DecimalFormat("#.00");

//			BigDecimal big = new BigDecimal(info.availableQuantity);
			et_redeem_quantity.setText(StringUtil.append2Decimals(info.availableQuantity, 2));
//			et_redeem_quantity.setText(decimalFormat.format(big));
			et_redeem_quantity.setSelection(et_redeem_quantity.getText()
					.length());
//			isRedeemAll = true;
			tv_chinese_numeral.addTextChangedListener(textWatcher);
			break;

		case R.id.tv_choose_date:
			Calendar currentDate = geCurrentDate();
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(getActivity(),
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(0);
							c.set(Calendar.YEAR, year);
							c.set(Calendar.MONTH, monthOfYear);
							c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							tv_choose_date.setText(dateFormat.format(c
									.getTime()));

						}
					}, currentDate.get(Calendar.YEAR),
					currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		default:
			break;
		}

	}

	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * 获取当前选择的时间，默认为当天的时间
	 * 
	 * @return
	 */
	private Calendar geCurrentDate() {
		String tvStringDate = tv_choose_date.getText().toString();
		Calendar calendar = Calendar.getInstance();
		// 选择日期为空，或者选择日期不为yyyy/MM/dd格式，返回当前日期
		if (TextUtils.isEmpty(tvStringDate) || !tvStringDate.contains("/")) {
			return calendar;
		}
		Date date = null;
		try {
			date = dateFormat.parse(tvStringDate.trim());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (date != null) {
			calendar.setTime(date);
		} else { // 理论上这一段代码是不需要执行的
			calendar.setTimeInMillis(0);

			String[] split = tvStringDate.split("/");
			if (split.length != 3) {
				return calendar;
			}
			calendar.set(Calendar.YEAR, Integer.valueOf(split[0]));
			calendar.set(Calendar.MONTH, Integer.valueOf(split[1]));
			calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(split[2]));
		}

		return calendar;
	}

	/**
	 * 获取赎回日期，立即赎回返回空字符
	 * 
	 * @return
	 */
	public String getRedeemDate() {
		switch (rg_redeem_way.getCheckedRadioButtonId()) {
		case R.id.rb_assign_date:
			return tv_choose_date.getText().toString().trim();
		case R.id.rb_redeem_now:
		default:
			return "";
		}
	}

	/**
	 * 获取赎回份额
	 * 
	 * @return
	 */
	public String getRedeemQuantity() {
		if (isRedeemAll) {
			return "0";
		}
		return et_redeem_quantity.getText().toString().trim();
	}

	/**
	 * 检测输入的合法性
	 * 
	 * @return
	 */
	public boolean checkInput() {
		/** 可赎回份额 */
		double canredeemQuantity = Double.valueOf(info.availableQuantity);
		String canPartlyRedeem = info.canPartlyRedeem;
		String redeemQuantity;
		if (canPartlyRedeem.equals("0")) {
			/** 赎回份额 */
			redeemQuantity = et_redeem_quantity.getText().toString().trim();
			// 以下为验证
			// 赎回份额
			RegexpBean reb = new RegexpBean(getResources().getString(
					R.string.bocinvt_redeem_regex), redeemQuantity,
					"redeemShare");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//			lists.add(BocInvestControl.getRegexpBean(info.curCode.numberCode, "赎回份额", redeemQuantity, null));
//			lists.add(BocInvestControl.getRegexpBean(info.curCode.letterCode, "赎回份额", redeemQuantity, null));
			lists.add(reb);
			if (!RegexpUtils.regexpDate(lists)) {// 校验没有通过
				return false;
			}
			// 允许部分赎回
			if (Double.valueOf(redeemQuantity) > canredeemQuantity) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.bocinvt_error_canredeem1));
				return false;
			}

		} else {
			// 不允许部分赎回
			redeemQuantity = info.holdingQuantity;
		}
		return true;
	}
	/**
	 * 设置指定赎回日期提示信息
	 */
	private void setMessage(){
		String str_sellType=(String)responseDeal.get("sellType");//赎回开放规则,00：不允许赎回01：开放期赎回02：付息日赎回03：起息后每日赎回04：周期开放赎回
		if(!StringUtil.isNullOrEmpty(str_sellType)){
//		if (((String)responseDeal.get("appdatered")).equals("1")&&!str_sellType.equals("00")) {//是否允许指定日期赎回,0：否1：是   &&  00：不允许赎回
			if (str_sellType.equals("01")||str_sellType.equals("02")||str_sellType.equals("03")) {
				tv_remitDate.setText("赎回开放日期为"+DateUtils.formatStr((String)responseDeal.get("redEmptionStartDate"))+"至"+DateUtils.formatStr((String)responseDeal.get("redEmptionEndDate")));
			}else if (str_sellType.equals("04")) {
				String str_rStart=(String) responseDeal.get("redEmperiodStart");
				String str_rEnd=(String) responseDeal.get("redEmperiodEnd");
				String str_rer=(String)responseDeal.get("redEmperiodfReq");
				if (str_rer.equalsIgnoreCase("d")) {
					tv_remitDate.setText(str_rStart+"号至"+str_rEnd+"号可赎回");
				}else if (str_rer.equalsIgnoreCase("w")) {
					if (str_rEnd.equals("7")||str_rEnd.equals("七")) {
						tv_remitDate.setText("每周的星期"+str_rStart+"至周日可赎回");
					}else {
						tv_remitDate.setText("每周的星期"+str_rStart+"至星期"+str_rEnd+"可赎回");
					}
				}else if (str_rer.equalsIgnoreCase("m")) {
					tv_remitDate.setText("每月的"+str_rStart+"号至"+str_rEnd+"号可赎回");
				}
			}
//
//		}else {
//			tv_remitDate.setVisibility(View.GONE);
//		}
		}
	}
//	private String getSystemDate(){
//		String sub = BociDataCenter.getInstance().getDateTime("dateTime").substring(0, 10);
//		int tem=Integer.parseInt(sub.substring(8, 10))+1;
//		return tem>9?(sub.substring(0, 8)+tem):(sub.substring(0, 8)+"0"+tem);
//	}
}
