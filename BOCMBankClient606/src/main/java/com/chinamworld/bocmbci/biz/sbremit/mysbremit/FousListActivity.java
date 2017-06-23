package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.widget.JustifyTextView;

public class FousListActivity extends SBRemitBaseActivity {

	private static final String TAG = "SBRemitSpecialTipsActivity";
	private View view;
	/** 提示 */
	private JustifyTextView tips_view;
	/** 选择框 */
	private CheckBox agree_cb;
	/** 选择框信息 */
	private TextView checkText;
	/** 警告 */
	private TextView tips_warn;
	/** 风险提示函内容 */
	private TextView messagedate;
	/** 下一步 */
	private Button btnNext;
	private TextView agree_tv;
	/** 姓名 */
	private String custName;
	/** 证件号吗 */
	private String identityNumber;
	/** 证件类型 */
	private String identityType;
	/** 起始日期 */
	private String pubDate;
	/** 发布原因 */
	private String pubReason;
	/** 结束日期 */
	private String endDate;
	/** 结购汇类型 */
	private String sbType;
	// 第一行信息
	private TextView messagefirst;
	// 关注期限
	private TextView message_new;
	// 关注原因
	private TextView messageReason;
	// 主体姓名
	private TextView custnameTextView;
	// 确认日期自动生成时间
	private TextView autoTime;
	// 提示语句
	private TextView reminder_message;
	/** 已用额度、剩余额度、随即值、表单 */
	private Map<String, Object> resultAmount;

	/** 币种 */
	private String moneyType;
	/** 结售汇类型 */
	private String cashRemit;
	/** 可用余额 */
	private String availanle_balance;
	/** 人民币余额 */
	private String availableBalanceRMB;
	/** 个人可结售汇金额折成美元 */
	private String annRmeAmtUSD;
	private String token;
	private String acountId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();
		initViews();
	}

	private void initViews() {
		Map<String, Object> resultAmount_new = SBRemitDataCenter.getInstance()
				.getResultAmount();
		// 获得发布原因
		pubReason = (String) resultAmount_new.get(SBRemit.PUBREASON);
		tips_warn = (TextView) findViewById(R.id.reminder_message);
		messagefirst = (TextView) findViewById(R.id.messsage);
		messagedate = (TextView) findViewById(R.id.messsage_date);
		messageReason = (TextView) findViewById(R.id.messsage_reason);
		// custnameTextView=findViewById(R.id.)
		agree_tv = (TextView) findViewById(R.id.reminder_message);
		agree_cb = (CheckBox) findViewById(R.id.checkboxone);
		checkText = (TextView) findViewById(R.id.checktext);
		message_new = (TextView) findViewById(R.id.message_new);
		btnNext = (Button) findViewById(R.id.btnNext);
		// 开始计时
		timer.start();
		// agree_tv.setOnClickListener(checkedListener);
		checkText.setOnClickListener(checkedListener);

		messagefirst = (TextView) findViewById(R.id.messsage);
		custnameTextView = (TextView) findViewById(R.id.custname);
		autoTime = (TextView) findViewById(R.id.autotime);
		reminder_message = (TextView) findViewById(R.id.reminder_message);
		// font-size:25px;;<span
		// style=font-weight:bold>";;;<font size="Xpx"></font>
		// "<font color=\"#ba001d\">";;;<span
		// style="font-weight:bold;">加粗</span>
		String custnameString = "<big>" + custName + "</big>";

		// String messageFirst =Html.fromHtml (custnameString + "(身份证号码" +
		// identityNumber + ")");
		// 转化为全角
		String identityNumberNew = ToDBC(custnameString + "(身份证号码"
				+ identityNumber + ")" + ",被列为个人外汇管理“关注对象”");
		// 拼接第一行的字符串
		messagefirst.setText(ToDBC(custName+"("+"身份证件号码："+identityNumber+")"+":"));
		// 拼接第二行的字符串
		String pubDateNew=formatStr(pubDate);
		String endDateNew=formatStr(endDate);
		String messagedStringate = pubDateNew + "至" + endDateNew;
		String messageFrontString="        根据《国家外汇管理局关于进一步完善个人结售汇业务管理的通知》（汇发[2009]56号）等相关规定，您办理的个人外汇业务，涉嫌再次出借本人额度协助他人或以借用他人额度等方式规避额度及真实性管理，已被外汇管理局列入“关注名单”管理，关注期限自";
		String messageBehindString="。在关注期限内，您可凭本人有效身份证件和有交易额的相关证明材料到银行办理个人结售汇业务。如您对被列入“关注名单”有异议，可联系当地外汇管理局。";
		message_new.setText(messageFrontString+messagedStringate+messageBehindString);
		messagedate.setText(messagedStringate);
		String messagereasonString = pubReason;
		messageReason.setText(messagereasonString);

		custnameTextView.setText(custName);
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		String time = t.year + "年 " + (t.month + 1) + "月 " + t.monthDay + "日 ";
		// 去掉空格
		String time_new = time.replaceAll(" ", "");
		autoTime.setText(time_new);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, autoTime);
	}

	private OnClickListener checkedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (agree_cb.isChecked()) {
				agree_cb.setChecked(false);
			} else {
				agree_cb.setChecked(true);
			}
		}
	};

	private OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		}
	};

	private void startActivity() {
		if (sbType.equals("01")) {
			startSRemit();
		} else {
			startBRemit();
		}
	}

	/**
	 * @Title: requestForCardListCallBack
	 * @Description: 请求最大金额试算返回
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestbiggesttryCallBack(Object resultObj) {
		super.requestbiggesttryCallBack(resultObj);
		String annRmeAmtCUR = (String) SBRemitDataCenter.getInstance()
				.getTryMap().get("annRmeAmtCUR");
		startActivity();

	}

	@SuppressWarnings("unchecked")
	private void init() {
		setTitle(getString(R.string.fast_mysbremit));
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = LayoutInflater.from(this).inflate(R.layout.focus_list, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		tabcontent.setPadding(0, 0, 0, 0);
		LogGloble.e("asd", "0000"
				+ SBRemitDataCenter.getInstance().getResultAmount());
		resultAmount = (Map<String, Object>) SBRemitDataCenter.getInstance()
				.getResultAmount();
		// ((Button)
		// this.findViewById(R.id.ib_top_right_btn)).setVisibility(View.GONE);
		Intent intent = getIntent();
		custName = intent.getStringExtra(SBRemit.CUSTNAME);
		pubDate = intent.getStringExtra(SBRemit.PUBDATE);
		endDate = intent.getStringExtra(SBRemit.END_DATE);
		sbType = intent.getStringExtra(SBRemit.SB_TYPE);
		identityNumber = intent.getStringExtra(SBRemit.IDENTITYNUMBER);
		identityType = intent.getStringExtra(SBRemit.IDENTITYTYPE);
		LogGloble.e("asd", "1234" + identityNumber);
		moneyType = intent.getStringExtra(SBRemit.CURRENCY);
		LogGloble.e("asd", "currency" + moneyType);
		availableBalanceRMB = intent
				.getStringExtra(SBRemit.AVAILABLE_BALANCERMB);
		annRmeAmtUSD = intent.getStringExtra(SBRemit.ANNRMEAMTUSD);
		cashRemit = intent.getStringExtra(SBRemit.CASH_REMIT);
		acountId = intent.getStringExtra(SBRemit.ACCOUNT_ID);
		availanle_balance = intent.getStringExtra(SBRemit.AVAILABLE_BALANCE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 隐藏左侧二级菜单
		setLeftButtonPopupGone();
	}

	/**
	 * 启动结汇 输入信息页
	 */
	private void startSRemit() {
		Intent intent = new Intent();
		intent.putExtra(SBRemit.IDENTITYNUMBER, identityNumber);
		intent.putExtra(SBRemit.CURRENCY, moneyType);
		intent.putExtra(SBRemit.CASH_REMIT, cashRemit);
		intent.putExtra(SBRemit.SB_TYPE, sbType);
		intent.putExtra(SBRemit.AVAILABLE_BALANCE, availanle_balance);
		intent.putExtra(SBRemit.MONERY_TYPE_new, moneyType);
		intent.putExtra(SBRemit.AVAILABLE_BALANCE_new, availableBalanceRMB);
		intent.putExtra(SBRemit.USED_AMOUNT,
				isObjNull(resultAmount.get(SBRemit.USED_AMOUNT)));
		intent.putExtra(SBRemit.REMAIN_AMOUNT,
				isObjNull(resultAmount.get(SBRemit.REMAIN_AMOUNT)));
		// intent.putExtra(SBRemit.DATA_TABLE,
		// isObjNull(resultAmount.get(SBRemit.DATA_TABLE)));

		// intent.putExtra(SBRemit.EXCHANGE_RATE,
		// isObjNull(map.get(SBRemit.EXCHANGE_RATE)));
		intent.putExtra(SBRemit.IMPORTANT_FOCUS,
				isObjNull(resultAmount.get(SBRemit.IMPORTANT_FOCUS)));
		intent.putExtra(SBRemit.SIGNSTATUS,
				isObjNull(resultAmount.get(SBRemit.SIGNSTATUS)));
		intent.putExtra(SBRemit.PUBDATE,
				isObjNull(resultAmount.get(SBRemit.PUBDATE)));
		intent.putExtra(SBRemit.END_DATE,
				isObjNull(resultAmount.get(SBRemit.END_DATE)));
		intent.putExtra(SBRemit.CUSTTYPRCODE,
				isObjNull(resultAmount.get(SBRemit.CUSTTYPRCODE)));
		intent.putExtra(SBRemit.CUSTNAME, custName);
		intent.putExtra(SBRemit.ANNRMEAMTUSD, annRmeAmtUSD);
		intent.putExtra(SBRemit.IDENTITYTYPE, identityType);
		if (resultAmount.containsKey(SBRemit.TRANS_RANDOM)
				&& resultAmount.get(SBRemit.TRANS_RANDOM) != null)
			intent.putExtra(SBRemit.TRANS_RANDOM,
					resultAmount.get(SBRemit.TRANS_RANDOM).toString());
		intent.setClass(this, SRemitInputInfoActivity.class);
		startActivityForResult(intent, SREMIT_OPERATION);
	}

	/**
	 * 启动购汇 输入信息页
	 */
	private void startBRemit() {
		Intent intent = new Intent();
		intent.putExtra(SBRemit.CASH_REMIT, cashRemit);
		intent.putExtra(SBRemit.USED_AMOUNT,
				isObjNull(resultAmount.get(SBRemit.USED_AMOUNT)));
		intent.putExtra(SBRemit.REMAIN_AMOUNT,
				isObjNull(resultAmount.get(SBRemit.REMAIN_AMOUNT)));
		intent.putExtra(SBRemit.DATA_TABLE,
				isObjNull(resultAmount.get(SBRemit.DATA_TABLE)));
		intent.putExtra(SBRemit.AVAILABLE_BALANCE, availanle_balance);
		intent.putExtra(SBRemit.ANNRMEAMTUSD, annRmeAmtUSD);
		intent.putExtra(SBRemit.IDENTITYNUMBER, identityNumber);
		intent.putExtra(SBRemit.CUSTNAME, custName);
		intent.putExtra(SBRemit.SB_TYPE, sbType);
		intent.putExtra(SBRemit.IMPORTANT_FOCUS,
				isObjNull(resultAmount.get(SBRemit.IMPORTANT_FOCUS)));
		intent.putExtra(SBRemit.SIGNSTATUS,
				isObjNull(resultAmount.get(SBRemit.SIGNSTATUS)));
		intent.putExtra(SBRemit.PUBDATE,
				isObjNull(resultAmount.get(SBRemit.PUBDATE)));
		intent.putExtra(SBRemit.END_DATE,
				isObjNull(resultAmount.get(SBRemit.END_DATE)));
		intent.putExtra(SBRemit.IDENTITYTYPE, identityType);
		if (resultAmount.containsKey(SBRemit.TRANS_RANDOM)
				&& resultAmount.get(SBRemit.TRANS_RANDOM) != null)
			intent.putExtra(SBRemit.TRANS_RANDOM,
					resultAmount.get(SBRemit.TRANS_RANDOM).toString());
		intent.setClass(this, BRemitInputInfoActivity.class);
		startActivityForResult(intent, BREMIT_OPERATION);
	}

	/**
	 * 结汇购汇：结汇重点关注对象确认书
	 */
	public void SRemitimportantIssues() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.ImportantIssues);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.TOKEN, token);// 防止重复提交令牌
		paramsmap.put(SBRemit.ACCOUNT_ID, acountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "importantIssuesCallback");
	}

	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		BaseHttpEngine.showProgressDialog();
		SRemitimportantIssues();
		if (sbType.equals("11")) {
			startBRemit();
		} else {
			// if (false == isRequestbiggesttry(sbType, cashRemit,
			// availableBalanceRMB, moneyType, annRmeAmtUSD)) {
			startSRemit();
			// }
		}

	}

	/***
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	// 倒计时10秒钟
	CountDownTimer timer = new CountDownTimer(11000, 1000) {

		@Override
		public void onTick(long arg0) {
			String string = "<font color=\"#ba001d\">" + arg0 / 1000
					+ "</font>";
			String string2 = "<big>" + string + "</big>";
			btnNext.setText(Html.fromHtml("确认" + string2));
			btnNext.setOnClickListener(CantOnClick);
			// btnNext.setTextColor(color.black);
			// btnNext.setBackgroundColor(com.chinamworld.bocmbci.R.color.gray);
			btnNext.setBackgroundResource(R.drawable.button_important);

		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			timer.cancel();
			btnNext.setBackgroundResource(R.drawable.btn_red_big_long);
			btnNext.setText(Html.fromHtml("确认"));
			btnNext.setOnClickListener(onClick);
		}
	};
	private OnClickListener CantOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};
	/**
	 * 输入 yyyy/MM/dd  输出  yyyy年MM月dd日
	 * @param dateStr
	 * @return
	 */
	public static String formatStr(String dateStr) {
		try {
			SimpleDateFormat dataformat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = dataformat.parse(dateStr);
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			String dateString = format.format(date);
			return dateString;
		} catch (ParseException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return "-";
	}
}
