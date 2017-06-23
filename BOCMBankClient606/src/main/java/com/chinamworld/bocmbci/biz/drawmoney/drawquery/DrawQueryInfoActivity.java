package com.chinamworld.bocmbci.biz.drawmoney.drawquery;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: DrawQueryInfoActivity
 * @Description: 取款查询详情页面
 * @author JiangWei
 * @date 2013-7-23 下午2:24:42
 */
public class DrawQueryInfoActivity extends DrawBaseActivity {

	/** 详情数据 */
	private Map<String, Object> detailMap;
	/** 代理点收款账户 */
	private String agencyAcountStr;
	/** 收款人手机号 */
	private String payeeMobileStr;
	/** 收款人姓名 */
	private String payeeNameStr;
	/** 汇款金额 */
	private String remitAmountStr;
	/** 汇款编号 */
	private String remitNoStr;
	/** 汇款币种 */
	// private String remitCurrencyCodeStr;
	/** 取款状态 */
	private String remitStatusStr;
	/** 代理点名称 */
	private String remitAgencyNameStr;
	/** 代理点编号 */
	private String remitAgencyNumberStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.drawmoney_draw_query_detail, null);
		tabcontent.addView(view);
		setTitle(R.string.draw_query_title);

		detailMap = DrawMoneyData.getInstance().getQueryResultDetail();
		agencyAcountStr = this.getIntent().getStringExtra(DrawMoney.AGENT_ACCT_NUMBER);
		remitStatusStr = this.getIntent().getStringExtra(DrawMoney.REMIT_STATUS);

		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化页面数据
	 * @param
	 * @return void
	 */
	private void init() {
		TextView textAgenceAcount = (TextView) this.findViewById(R.id.tv_agence_payee_account);
		TextView textremitNoStr = (TextView) this.findViewById(R.id.tv_remitout_no);
		TextView textBiZhong = (TextView) this.findViewById(R.id.tv_remit_bizhong);
		TextView textMoneyAmount = (TextView) this.findViewById(R.id.tv_amount_for_drawing);
		TextView textRemitName = (TextView) this.findViewById(R.id.tv_get_remit_name);
		TextView textRemitPhone = (TextView) this.findViewById(R.id.tv_get_remit_phone);
		TextView textRemitDate = (TextView) this.findViewById(R.id.tv_remit_date);
		TextView textRemitStatus = (TextView) this.findViewById(R.id.tv_get_remit_status);
		TextView textAgencyName = (TextView) this.findViewById(R.id.tv_remit_agence_name);
		TextView textAgencyNumber = (TextView) this.findViewById(R.id.tv_remit_agence_number);
		TextView channelView = (TextView) this.findViewById(R.id.channel);

		payeeMobileStr = (String) detailMap.get(DrawMoney.PAYEE_MOBILE);
		payeeNameStr = (String) detailMap.get(DrawMoney.PAYEE_NAME);
		remitAmountStr = (String) detailMap.get(DrawMoney.REMIT_AMOUNT);
		// remitCurrencyCodeStr = (String)
		// detailMap.get(DrawMoney.CURRENY_CODE);
		remitNoStr = (String) detailMap.get(DrawMoney.REMIT_NO);
		remitAgencyNameStr = (String) detailMap.get(DrawMoney.AGENT_NAME);
		remitAgencyNumberStr = (String) detailMap.get(DrawMoney.AGENT_NUM);

		String agencyAcountFixed = StringUtil.getForSixForString(String.valueOf(agencyAcountStr));
		textAgenceAcount.setText(agencyAcountFixed);
		// textBiZhong.setText(remitCurrencyCodeStr);
		textBiZhong.setText(R.string.tran_currency_rmb);
		textMoneyAmount.setText(StringUtil.parseStringPattern(remitAmountStr, 2));
		textRemitName.setText(payeeNameStr);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textRemitName);
		textRemitPhone.setText(StringUtil.isNull(payeeMobileStr) ? "-" : payeeMobileStr);
		String tranDate = (String) detailMap.get(DrawMoney.TRAN_DATE);
		textRemitDate.setText(tranDate.substring(0, 10));
		textremitNoStr.setText(remitNoStr);
		textRemitStatus.setText(LocalData.drawStatus.get(remitStatusStr));
		textAgencyName.setText(StringUtil.isNull(remitAgencyNameStr) ? "-" : remitAgencyNameStr);
		textAgencyNumber.setText(StringUtil.isNull(remitAgencyNumberStr) ? "-" : remitAgencyNumberStr);

		String channel = (String) detailMap.get(DrawMoney.CHANNEL);
		channelView.setText(DrawMoney.ChannelType.getChannelStr(channel));
	}

}
