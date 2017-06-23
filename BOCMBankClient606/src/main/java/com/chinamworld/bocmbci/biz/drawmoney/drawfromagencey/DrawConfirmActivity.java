package com.chinamworld.bocmbci.biz.drawmoney.drawfromagencey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: DrawConfirmActivity
 * @Description: 代理点取款确认信息页面
 * @author JiangWei
 * @date 2013-7-24 上午11:41:01
 */
public class DrawConfirmActivity extends DrawBaseActivity {

	/** 页面内容提示信息 */
	private TextView textContentTiltle;
	/** 汇款编号 */
	private TextView textRemitNo;
	/** 收款人手机号 */
	private TextView textPhone;
	/** 收款人姓名 */
	private TextView textName;
	/** 币种 */
	private TextView textBiZhong;
	/** 金额 */
	private TextView textMoneyAmount;
	/** 附言 */
	private TextView textFuyan;
	/** 下一步 */
	private Button nextBtn;
	/** 取款信息详情 */
	private Map<String, Object> mapInfo;
	/** 取款密码layout */
	private LinearLayout passwordLayout;
	/** 取款密码输入框 */
	private SipBox sipBox_pw;
	/** 汇款编号 */
	private String remitNoStr;
	/** 取款密码 */
	private String password;
	private String password_RC;
	/** conversationId */
	private String conversationId;
	/** 防重标识 */
	private String token;
	/** 完成信息 */
	Map<String, Object> mapFinish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.drawmoney_draw_confirm_info, null);
		tabcontent.addView(view);
		setTitle(R.string.draw_from_agency_title);

		mapInfo = DrawMoneyData.getInstance().getDrawInfo();
		initConfirmView();
	}

	/**
	 * @Title: init
	 * @Description: 初始化确定页面及其数据
	 * @param
	 * @return void
	 */
	private void initConfirmView() {
		textContentTiltle = (TextView) this.findViewById(R.id.draw_title_tv);
		textRemitNo = (TextView) this
				.findViewById(R.id.draw_confirm_remitout_no);
		textPhone = (TextView) this.findViewById(R.id.draw_confirm_phone);
		textName = (TextView) this.findViewById(R.id.draw_confirm_name);
		textBiZhong = (TextView) this.findViewById(R.id.draw_cashremit_tv);
		textMoneyAmount = (TextView) this
				.findViewById(R.id.draw_confirm_amount);
		textFuyan = (TextView) this.findViewById(R.id.draw_confirm_fuyan);
		passwordLayout = (LinearLayout) this.findViewById(R.id.view_pass1);

		LinearLayout layoutPass1 = (LinearLayout) this
				.findViewById(R.id.layout_pass_1);
		sipBox_pw = new SipBox(this);
		sipBox_pw.setCipherType(SystemConfig.CIPHERTYPE);
		initPasswordSipBox(sipBox_pw);
		layoutPass1.addView(sipBox_pw);

		remitNoStr = (String) mapInfo.get(DrawMoney.REMIT_NO);
		textRemitNo.setText(remitNoStr);
		textPhone.setText((String) mapInfo.get(DrawMoney.PAYEE_MOBILE));
		textName.setText((String) mapInfo.get(DrawMoney.PAYEE_NAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textName);
		String currencyCode = (String) mapInfo.get(DrawMoney.CURRENY_CODE);
		textBiZhong.setText(LocalData.Currency.get(currencyCode));
		String amountString = (String) mapInfo.get(DrawMoney.REMIT_AMOUNT);
		textMoneyAmount.setText(StringUtil.parseStringPattern(amountString, 2));
		String remarkString = (String) mapInfo.get(DrawMoney.REMARK);
		textFuyan.setText(StringUtil.isNullOrEmpty(remarkString) ? "-"
				: remarkString);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, textFuyan);

		TextView textPhoneLable = (TextView) findViewById(R.id.draw_confirm_phone_lable);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				textPhoneLable);

		nextBtn = (Button) this.findViewById(R.id.draw_confirm_next_btn);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseNext();
			}

		});
	}

	/**
	 * @Title: initPasswordSipBox
	 * @Description: 初始化加密控件
	 * @param @param sipBox
	 * @return void
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		LinearLayout.LayoutParams param1 = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param1);
		// add by 2016年9月12日 luqp 修改
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setHint("6位数字");
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, DrawMoneyData.getInstance().getRandomNumber(), this);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.ATM_PASSWORD);
//		sipBox.setRandomKey_S(DrawMoneyData.getInstance().getRandomNumber());
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setHint("6位数字");
//		sipBox.setSipDelegator(this);
		InputFilter[] filters1 = { new InputFilter.LengthFilter(20) };
		sipBox.setFilters(filters1);
	}

	/**
	 * @Title: setFinishView
	 * @Description: 设置完成页面信息的显示
	 * @param
	 * @return void
	 */
	private void setFinishView() {
		textContentTiltle.setText(R.string.finish_draw_info);
		ibBack.setVisibility(View.GONE);
		nextBtn.setText(R.string.finish);
		passwordLayout.setVisibility(View.GONE);
		textRemitNo.setText((String) mapFinish.get(DrawMoney.REMIT_NO));
		textPhone.setText((String) mapFinish.get(DrawMoney.PAYEE_MOBILE));
		textName.setText((String) mapFinish.get(DrawMoney.PAYEE_NAME));
		String currencyCode = (String) mapFinish.get(DrawMoney.CURRENY_CODE);
		textBiZhong.setText(LocalData.Currency.get(currencyCode));
		String amountString = (String) mapInfo.get(DrawMoney.REMIT_AMOUNT);
		textMoneyAmount.setText(StringUtil.parseStringPattern(amountString, 2));
		String remark = (String) mapFinish.get(DrawMoney.REMARK);
		textFuyan.setText(StringUtil.isNullOrEmpty(remark) ? "-" : remark);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DrawInputInfoAcitivity.toClearOrNot = true;
				finish();
			}
		});
	}

	/**
	 * @Title: excuseNext
	 * @Description: 执行下一步
	 * @param
	 * @return void
	 */
	private void excuseNext() {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean psw_rb = new RegexpBean(
				this.getString(R.string.acc_atmpwd_regex), sipBox_pw.getText()
						.toString().trim(), "atmpass");
		lists.add(psw_rb);
		if (!RegexpUtils.regexpDate(lists)) {
			return;
		}
		try {
			password = sipBox_pw.getValue().getEncryptPassword();
			password_RC = sipBox_pw.getValue().getEncryptRandomNum();
		} catch (CodeException e) {
			LogGloble.exceptionPrint(e);
		}
		BaseHttpEngine.showProgressDialog();
		conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		requestPSNGetTokenId(conversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnMobileWithdrawal();
	}

	/**
	 * @Title: requestPsnMobileWithdrawal
	 * @Description: 请求“汇款解付”接口
	 * @param
	 * @return void
	 */
	private void requestPsnMobileWithdrawal() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_WITH_DRAWAL);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DrawMoney.REMIT_NO, remitNoStr);
		map.put(DrawMoney.WITH_DRAW_PWD, password);
		map.put(DrawMoney.WITH_DRAW_PWD_RC, password_RC);
		map.put(Comm.TOKEN_REQ, token);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileWithdrawalCallback");
	}

	/**
	 * @Title: requestPsnMobileWithdrawalCallback
	 * @Description: 请求“汇款解付”接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnMobileWithdrawalCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		mapFinish = (Map<String, Object>) biiResponseBody.getResult();
		setFinishView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (nextBtn.getText().toString().equals(getString(R.string.finish))) {
				DrawInputInfoAcitivity.toClearOrNot = true;
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
