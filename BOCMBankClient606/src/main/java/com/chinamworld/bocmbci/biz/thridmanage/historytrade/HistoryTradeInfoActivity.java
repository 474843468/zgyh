package com.chinamworld.bocmbci.biz.thridmanage.historytrade;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.TransactionStatus;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 历史交易查询详情
 * 
 * @author panwe
 * 
 */
public class HistoryTradeInfoActivity extends ThirdManagerBaseActivity {
	/** 主布局 **/
	private View viewContent;
	/** 交易时间 */
	private TextView tvTime;
	/** 交易类型 */
	private TextView tvType;
	/** 资金账户 */
	private TextView tvBankAcc;
	/** 保证金账户 */
	private TextView tvCecAcc;
	/** 币种 */
	private TextView tvBiZhong;
	/** 转账金额 */
	private TextView tvAmount;
	/** 交易状态 */
	private TextView tvState;
	/** 信息集合  */
	public static Map<String, String> mMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		if (getIntentData()) {
			viewContent = LayoutInflater.from(this).inflate(R.layout.third_historytrade_query_info, null);
			addView(viewContent);
			setTitle(R.string.bocinvt_history_titile);
			init();
			setData();
		} else {
			finish();
		}
	}

	@SuppressWarnings("unchecked")
	private boolean getIntentData() {
		mMap = (HashMap<String, String>) getIntent().getSerializableExtra("data");
		return mMap != null;
	}

	private void init() {
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});
		
		tvTime = (TextView) findViewById(R.id.tv_paytime);
		tvType = (TextView) findViewById(R.id.tv_type);
		tvBankAcc = (TextView) findViewById(R.id.tv_cec_bankacc);
		tvCecAcc = (TextView) findViewById(R.id.tv_cecacc);
		tvBiZhong = (TextView) findViewById(R.id.tv_cec_bizhong);
		tvAmount = (TextView) findViewById(R.id.tv_amout);
		tvState = (TextView) findViewById(R.id.tv_state);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvTime);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBankAcc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvCecAcc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBiZhong);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAmount);
	}

	private void setData() {
		tvTime.setText(mMap.get(Third.QUERY_PAYDATE));
		tvBankAcc.setText(StringUtil.getForSixForString(mMap.get(Third.CECURITYTRADE_BANKACCNUM)));
		tvCecAcc.setText(mMap.get(Third.CECURITY_AMOUT_CAACC));
		tvAmount.setText(mMap.get(Third.TRANSFER_COMIT_AMOUNT));
		String cecurityType = mMap.get(Third.CECURITY_AMOUT_BZ);
		tvBiZhong.setText(BiiConstant.CurrencyType.getCurrencyTypeStr(cecurityType));
		String transferWayType = mMap.get(Third.QOERY_TRADE_TYPE);
		tvType.setText(BiiConstant.TransferWayType.getTransferWayTypeStr(transferWayType));
		tvState.setText(TransactionStatus.getTransactionStatusStr(mMap.get(Third.LIST_STATE)));
	}
}
