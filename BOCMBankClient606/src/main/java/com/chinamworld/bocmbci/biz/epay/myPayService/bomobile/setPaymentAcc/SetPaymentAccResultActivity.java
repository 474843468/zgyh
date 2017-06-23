package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.setPaymentAcc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceWriteConfirmActivity;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子支付 - 添加支付账户结果页面
 * @author Administrator
 * 
 */
public class SetPaymentAccResultActivity extends EPayBaseActivity {

	private View ePaySetPaymentAccResult;

	private Button bt_finish;

	private LinearLayout ll_selected_acclist;
	private List<Object> selectedAccList;

	private Context bomTransContext;

	private int excuteType;

	private String tag = "SetPaymentAccResultActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		getTransData();
		ePaySetPaymentAccResult = LayoutInflater.from(this).inflate(R.layout.epay_bom_spa_result, null);

		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(ePaySetPaymentAccResult);
		super.onCreate(savedInstanceState);

		// //初始化导航条
		// EpayPubUtil.initStepBar(this, 3, new String[]{"选择账户","填写信息","开通成功"});
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		final boolean acclistFlag = getIntent().getBooleanExtra("acclistFlag", false);
		ll_selected_acclist = (LinearLayout) ePaySetPaymentAccResult.findViewById(R.id.ll_selected_acclist);
		if (acclistFlag) {
			for (int i = 0; i < selectedAccList.size(); i++) {
				Map<Object, Object> temp = EpayUtil.getMap(selectedAccList.get(i));
				View view = LayoutInflater.from(this).inflate(R.layout.epay_bom_selected_acc_list_item, null);
				TextView tv_acc_number = (TextView) view.findViewById(R.id.item_acc_number);
				tv_acc_number.setText(StringUtil.getForSixForString(EpayUtil.getString(
						temp.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "")));
				ll_selected_acclist.addView(view);
			}
		} else {
			// 虚拟卡
			String virtualNo = String.valueOf(VirtualBCServiceWriteConfirmActivity.vcardinfo
					.get(Crcd.CRCD_VIRTUALCARDNO));
			View view = LayoutInflater.from(this).inflate(R.layout.epay_bom_selected_acc_list_item, null);
			TextView tv_acc_number = (TextView) view.findViewById(R.id.item_acc_number);
			tv_acc_number.setText(StringUtil.getForSixForString(virtualNo));
			ll_selected_acclist.addView(view);
		}

		bt_finish = (Button) ePaySetPaymentAccResult.findViewById(R.id.bt_finish);
		bt_finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (acclistFlag) {
					finish();
				} else {
					Intent it = new Intent(SetPaymentAccResultActivity.this, VirtualBCServiceListActivity.class);
					startActivity(it);
				}
			}
		});
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}

	private void addDredgedAccList() {
		List<Object> newAccList = bomTransContext.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
//		List<Object> dredgedList = bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST);
//		boolean isAdd = false;
		for (int i = 0; i < newAccList.size(); i++) {
			Map<Object, Object> map = EpayUtil.getMap(newAccList.get(i));
			map.put(PubConstants.PUB_FIELD_ISSELECTED, false);
			bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).add(map);

			Object accType = map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE);
			Object nickName = map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME);
			Object accNumber = map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER);

			map.put(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_TYPE, accType);
			map.put(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_NICKNAME, nickName);
			map.put(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_NUMBER, accNumber);

//			String accountIdPattern = EpayUtil.getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
//			for (int j = 0; j < dredgedList.size(); j++) {
//				Map<String, Object> temp = (Map<String, Object>) dredgedList.get(j);
//				String accountId = EpayUtil.getString(temp.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_ID), "");
//				if (accountId.equals(accountIdPattern)) {
//					isAdd = false;
//					break;
//				}
//				isAdd = true;
//			}
//			map.put(PubConstants.PUB_FIELD_ISSELECTED, false);
//			if (isAdd) {
//				bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).add(map);
//			}
		}

		LogGloble
				.d(tag, "dregedList size : " + bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size());
	}

	private void getTransData() {
		excuteType = PublicTools.getInt(bomTransContext.getData("excuteType"), 0);
		selectedAccList = bomTransContext.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
		addDredgedAccList();
		bomTransContext.clear("excuteType");
		bomTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
		bomTransContext.clear(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST);
		bomTransContext.clear(PubConstants.PUB_FIELD_CONVERSATION_ID);
		bomTransContext.clear(PubConstants.PUB_FIELD_FACTORLIST);
	}

}
