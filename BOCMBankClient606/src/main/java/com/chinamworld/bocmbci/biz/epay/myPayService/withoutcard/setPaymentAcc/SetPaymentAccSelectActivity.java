package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.setPaymentAcc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.WithoutCardActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.adapter.SPSAccountListAdapter;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;

/**
 * 电子支付开通-账户选择页
 * 
 */
public class SetPaymentAccSelectActivity extends EPayBaseActivity {

	private String tag = "EPaySetPaymentAccSelectActivity";

	private View ePayOpenPaymentAccSelect;
	// 账户列表
	private ListView lv_acc_list;

	// 下一步按钮
	private Button bt_next;

	// 提示信息
	private TextView tv_msg;

	private Context withoutCardTransContext;

	@SuppressWarnings("rawtypes")
	private Class backClz = WithoutCardActivity.class;
	private List<Object> unDredgedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		ePayOpenPaymentAccSelect = LayoutInflater.from(this).inflate(
				R.layout.epay_wc_spa_acc_select, null);

		super.setType(0);
		super.setShowBackBtn(true);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
		super.setContentView(ePayOpenPaymentAccSelect);
		super.onCreate(savedInstanceState);

		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 1, new String[]{"选择账户","填写信息","确认信息"});

		initCurPage();
	}

	private void initCurPage() {
		tv_msg = (TextView) ePayOpenPaymentAccSelect.findViewById(R.id.tv_msg);
		if (serviceType == 1) {
			tv_msg.setText(getResources().getString(
					R.string.epay_wc_open_service_tv_title));
		} else if (serviceType == 2) {
			tv_msg.setText(getResources().getString(
					R.string.epay_wc_open_service_tv_title1));
		}

		lv_acc_list = (ListView) ePayOpenPaymentAccSelect
				.findViewById(R.id.acc_select_listview);
		bt_next = (Button) ePayOpenPaymentAccSelect.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (withoutCardTransContext.getMap(
						PubConstants.CONTEXT_FIELD_SELECTED_ACC).isEmpty()) {
					// BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户！");
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getText(R.string.please_select_needopen_account)
									.toString());
					return;
				}

				Intent intent = new Intent(SetPaymentAccSelectActivity.this,
						SetPaymentAccInputActivity.class);
				SetPaymentAccSelectActivity.this.startActivityForResult(intent,
						0);
			}
		});
		initDisplay();
	}

	private void initDisplay() {
		activateListView();
	}

	/**
	 * 设置账户列表数据
	 */
	public void activateListView() {
		final SPSAccountListAdapter accListAdapter = new SPSAccountListAdapter(
				this);
		lv_acc_list.setAdapter(accListAdapter);
		lv_acc_list.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<Object, Object> accountData = EpayUtil
						.getMap(TransContext
								.getWithoutCardContext()
								.getList(
										PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST)
								.get(position));
				withoutCardTransContext.setData(
						PubConstants.CONTEXT_FIELD_SELECTED_ACC, accountData);
				accListAdapter.setSelectedPosition(position);
			}
		});
	}

	@Override
	public void finish() {
		withoutCardTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		setResult(RESET_DATA);
		super.finish();
	}

}
