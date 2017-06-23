package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.servOpen;

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
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.adapter.AccountListAdapter;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;

/**
 * 电子支付开通-账户选择页
 * 
 */
public class ServOpenAccountSelectActivity extends EPayBaseActivity {

	private String tag = "EPayServOpenAccountSelectActivity";

	private View epayServiceOpenAccountSelect;
	// 账户列表
	private ListView lv_acc_list;
	// 下一步按钮
	private Button bt_next;

	private Context bomTransContext;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		epayServiceOpenAccountSelect = LayoutInflater.from(this).inflate(R.layout.epay_bom_service_open_account_select, null);
		
		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(epayServiceOpenAccountSelect);
		super.onCreate(savedInstanceState);
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		lv_acc_list = (ListView) epayServiceOpenAccountSelect.findViewById(R.id.lv_account_listview);
		bt_next = (Button) epayServiceOpenAccountSelect.findViewById(R.id.bt_next);

		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (bomTransContext.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST).isEmpty()) {
//					BaseDroidApp.getInstanse().showInfoMessageDialog("您还没有选择需要开通的账户");
					BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.please_select_electronic_account).toString());
					return;
				}
//				for (int i = 0; i < bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).size(); i++) {
//					Map<Object, Object> accountTemp = (Map<Object, Object>) bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).get(i);
//					accountTemp.put(PubConstants.PUB_FIELD_ISSELECTED, false);
//				}
				Intent intent = new Intent(ServOpenAccountSelectActivity.this, ServOpenMsgInputActivity.class);
				//添加标志位
				intent.putExtra("acclistFlag", true);
				startActivityForResult(intent, 0);
			}
		});

		activateListView();
	}

	/**
	 * 设置账户列表数据
	 */
	public void activateListView() {
		final AccountListAdapter accListAdapter = new AccountListAdapter(this);
		lv_acc_list.setAdapter(accListAdapter);
		lv_acc_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<Object, Object> accountData = EpayUtil.getMap(bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).get(position));
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
	
	@Override
	public void finish() {
		bomTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
		super.finish();
	}
}
