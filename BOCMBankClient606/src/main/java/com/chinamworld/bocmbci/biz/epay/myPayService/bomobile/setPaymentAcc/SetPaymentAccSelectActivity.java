package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.setPaymentAcc;

import java.util.HashMap;
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

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.adapter.SPSAccountListAdapter;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;

/**
 * 电子支付-添加支付账户账户选择页
 * 
 */
public class SetPaymentAccSelectActivity extends EPayBaseActivity {

	private static final String TAG = SetPaymentAccSelectActivity.class.getSimpleName();
	private String tag = "SetPaymentAccSelectActivity";
	private View ePayOpenPaymentAccSelect;
	// 账户列表
	private ListView lv_acc_list;

	// 下一步按钮
	private Button bt_next;

	// private Spinner s_confirm_type;

	// 所有账户列表
	private List<Object> accountList;
	// 已开通账户列表
	private List<Object> dredgedAccList;

	private Context bomTransContext;

	private String combinId;
	private String conversationId;
	private int excuteType;

	private PubHttpObserver httpObserver;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_BOM);
		ePayOpenPaymentAccSelect = LayoutInflater.from(this).inflate(R.layout.epay_bom_spa_acc_select, null);
		
		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(ePayOpenPaymentAccSelect);
		super.onCreate(savedInstanceState);
		
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 1, new String[] { "选择账户", "填写信息", "开通结果" });
		// 初始化当前页
		initCurPage();

	}

	private void initCurPage() {
		lv_acc_list = (ListView) ePayOpenPaymentAccSelect.findViewById(R.id.acc_select_listview);
		// s_confirm_type =
		// (Spinner)ePayOpenPaymentAccSelect.findViewById(R.id.s_confirm_type);
		bt_next = (Button) ePayOpenPaymentAccSelect.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bomTransContext.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST).isEmpty()) {
//					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户！");
					BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.please_select_electronic_account).toString());
					return;
				}
				BiiHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
		transDataDispose();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = EpayUtil.getString(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID), "");
		httpObserver.setConversationId(conversationId);
		bomTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		requestGetSecurityFactor("PB200C2");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp bdApp = BaseDroidApp.getInstanse();
		bdApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				combinId = bdApp.getSecurityChoosed();
				Map< Object, Object> params = new HashMap< Object, Object>();
				params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
				httpObserver.req_setBomPaymentServiceAccPre(params, "setPaymentServiceAccPre");
			}
		});

	}

	/**
	 * 设置支付账户预交易
	 * 
	 * @param resultObj
	 */
	public void setPaymentServiceAccPre(Object resultObj) {
		BiiHttpEngine.showProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		
		// TODO 安全因子
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);
		
		List<Object> factorList = EpayUtil.getFactorList(resultMap);

		bomTransContext.setData(PubConstants.PUB_FIELD_FACTORLIST, factorList);
//		for (int i = 0; i < bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).size(); i++) {
//			Map<Object, Object> accountTemp = EpayUtil.getMap(bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).get(i));
//			accountTemp.put(PubConstants.PUB_FIELD_ISSELECTED, false);
//		}
		BiiHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, SetPaymentAccConfirmActivity.class);
		intent.putExtra("acclistFlag", getIntent().getBooleanExtra("acclistFlag", false));
		this.startActivityForResult(intent, 0);
	}

	private void initDisplay() {
		activateListView();
		BiiHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 设置账户列表数据
	 */
	public void activateListView() {
		final SPSAccountListAdapter accListAdapter = new SPSAccountListAdapter(this);
		lv_acc_list.setAdapter(accListAdapter);
		lv_acc_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<Object, Object> accountData = EpayUtil.getMap(bomTransContext.getList(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST).get(position));
				List<Object> selectedList = bomTransContext.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
				// 获取当前账户选中状态
				boolean isSelected = EpayUtil.getBoolean(accountData.get(PubConstants.PUB_FIELD_ISSELECTED));
				if (!isSelected) {// 未选中 用户点击后更改状态为选中
					accountData.put(PubConstants.PUB_FIELD_ISSELECTED, true);
					selectedList.add(accountData);
				} else { // 已选中 用户点击后更改状态为未选中
					accountData.put(PubConstants.PUB_FIELD_ISSELECTED, false);
					selectedList.remove(accountData);
				}
				accListAdapter.setSelectedPosition(position);
			}
		});
	}

	/**
	 * 交易数据处理
	 */
	private void transDataDispose() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		Intent intent = getIntent();
		excuteType = intent.getIntExtra("excuteType", 0);
		TransContext.getBomContext().clear(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
		switch (excuteType) {
		case 0:
			initDisplay();
//			httpObserver.req_queryAllAccount(EpayUtil.getAccTypeList(), "queryAllAccount");
			break;
//		case 2: // 信用卡流程
//			List<Object> selectedAccList = new ArrayList<Object>();
//			selectedAccList.add(VirtualBCServiceListActivity.getBankSetupMap());
//
//			TransContext.getBomContext().setData("excuteType", excuteType);
//			TransContext.getBomContext().setData(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST, selectedAccList);
//			BaseHttpEngine.showProgressDialog();
//			requestCommConversationId();
//			break;
		default:
			break;
		}
	}
	
	@Override
	public void finish() {
		bomTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
		setResult(RESET_DATA);
		super.finish();
	}

}
