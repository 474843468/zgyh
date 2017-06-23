package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.setMerchantRelation;

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
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.adapter.MerchantAdapter;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;

public class SelecteMerchantActivity extends EPayBaseActivity {

	private View selectMerchant;

	private ListView lv_add_merchant;

	private Button bt_next;

	private Context treatyContext;

	private List<Object> merchantList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		treatyContext = TransContext.getTreatyTransContext();

		selectMerchant = LayoutInflater.from(this).inflate(R.layout.epay_treaty_add_merchant, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(selectMerchant);
		super.onCreate(savedInstanceState);

		// // 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "修改限额", "确认信息",
		// "修改成功" });
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		// 清空选择
		treatyContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_MERCHANT);

		lv_add_merchant = (ListView) selectMerchant.findViewById(R.id.lv_add_merchant);
		bt_next = (Button) selectMerchant.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (treatyContext.getMap(PubConstants.CONTEXT_FIELD_SELECTED_MERCHANT).isEmpty()) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择签约商户");
					return;
				}

				Intent intent = new Intent(SelecteMerchantActivity.this, InputMsgActivity.class);
				SelecteMerchantActivity.this.startActivityForResult(intent, 0);
			}
		});
		merchantList = treatyContext.getList(TreatyConstants.PUB_FEILD_TREATY_UN_MERCHANTS);
		final MerchantAdapter merchantAdapter = new MerchantAdapter(this, merchantList);
		lv_add_merchant.setAdapter(merchantAdapter);
		lv_add_merchant.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Map<Object, Object> merchant = EpayUtil.getMap(merchantList.get(position));
				boolean isSelected = EpayUtil.getBoolean(merchant.get(PubConstants.PUB_FIELD_ISSELECTED));
				if (isSelected) {
					treatyContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_MERCHANT);
				} else {
					treatyContext.setData(PubConstants.CONTEXT_FIELD_SELECTED_MERCHANT, merchantList.get(position));
				}
				merchantAdapter.setSelectedPosition(position);
			}
		});

	}

	@Override
	public void finish() {
		treatyContext.clear(TreatyConstants.PUB_FEILD_TREATY_UN_MERCHANTS);
		treatyContext.setRightButtonClick(false);
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESET_DATA);
			finish();
			break;
		}
	}
}
