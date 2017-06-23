package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.servOpen;

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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceMenuActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceWriteConfirmActivity;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子支付-开通结果页面
 * 
 * @author Administrator
 * 
 */
public class ServOpenResultActivity extends EPayBaseActivity {

	private View epayServiceOpenResult;

	private TextView tv_obligateMsg;
	private TextView tv_dayMaxQuota;
	private TextView tv_perMaxQuota;
	private TextView tv_custMaxQuota;
	private LinearLayout ll_obligate_msg;
	private LinearLayout ll_selected_acclist;

	private Button bt_ensure;

	private Context bomTransContext;

	private String obligate;
	private String dayMaxQuota;
	private String perMaxQuota;
	private String custMaxQuota;
	private List<Object> selectedList;

	private boolean booleanExtra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		epayServiceOpenResult = LayoutInflater.from(this).inflate(R.layout.epay_bom_service_open_result, null);
		getTransData();

		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(epayServiceOpenResult);
		super.onCreate(savedInstanceState);

		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "确认信息", "开通成功", ""});
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		ll_obligate_msg = (LinearLayout) epayServiceOpenResult.findViewById(R.id.ll_obligate_msg);
		tv_obligateMsg = (TextView) epayServiceOpenResult.findViewById(R.id.tv_obligate_msg);
		tv_dayMaxQuota = (TextView) epayServiceOpenResult.findViewById(R.id.tv_day_max_quota);
		tv_perMaxQuota = (TextView) epayServiceOpenResult.findViewById(R.id.tv_per_max_quota);
		tv_custMaxQuota = (TextView) epayServiceOpenResult.findViewById(R.id.tv_cust_max_quota);
		ll_selected_acclist = (LinearLayout) epayServiceOpenResult.findViewById(R.id.ll_selected_acclist);
		bt_ensure = (Button) epayServiceOpenResult.findViewById(R.id.bt_ensure);

		// 添加提示
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenResult.findViewById(R.id.tip_one));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenResult.findViewById(R.id.tip_two));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenResult.findViewById(R.id.tip_three));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_obligateMsg);

		bt_ensure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (booleanExtra) {
					//
					finish();
				} else {
					// 进入银行卡服务
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent5 = new Intent(ServOpenResultActivity.this, VirtualBCServiceMenuActivity.class);
					startActivity(intent5);
				}

			}
		});

		initDisplay();
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}

	private void initDisplay() {
		booleanExtra = getIntent().getBooleanExtra("acclistFlag", false);
		if (booleanExtra) {
			for (int i = 0; i < selectedList.size(); i++) {
				Map<Object, Object> map = EpayUtil.getMap(selectedList.get(i));
				View view = LayoutInflater.from(this).inflate(R.layout.epay_bom_selected_acc_list_item, null);
				TextView tv_acc_number = (TextView) view.findViewById(R.id.item_acc_number);
				// TextView tv_acc_type =
				// (TextView)view.findViewById(R.id.item_acc_type);
				// TextView tv_acc_nick_name =
				// (TextView)view.findViewById(R.id.item_acc_nickname);
				String acc_number = EpayUtil.getString(
						map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "");
				tv_acc_number.setText(StringUtil.getForSixForString(acc_number));
				// String acc_nickName =
				// EpayUtil.getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME),
				// "");
				// tv_acc_nick_name.setText(acc_nickName);
				// String acc_type =
				// EpayUtil.getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
				// "");
				// tv_acc_type.setText(LocalData.AccountType.get(acc_type));
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

		if (!StringUtil.isNullOrEmpty(obligate)) {
			tv_obligateMsg.setText(obligate);
		} else {
			// ll_obligate_msg.setVisibility(View.GONE);
			tv_obligateMsg.setText("-");
		}
		tv_obligateMsg.setText(obligate);
		tv_dayMaxQuota.setText(StringUtil.parseStringPattern(dayMaxQuota, 2));
		tv_perMaxQuota.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
		tv_custMaxQuota.setText(StringUtil.parseStringPattern(custMaxQuota, 2));
		EpayUtil.getMap(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA)).put("loginHint",
				obligate);
	}

	private void getTransData() {
		obligate = bomTransContext.getString(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_OBLIGATE_MSG, "");
		dayMaxQuota = bomTransContext.getString(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX, "");
		perMaxQuota = bomTransContext.getString(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX, "");
		custMaxQuota = bomTransContext.getString(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT, "");
		selectedList = bomTransContext.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);

		bomTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
		bomTransContext.clear(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST);
		bomTransContext.clear(PubConstants.PUB_FIELD_CONVERSATION_ID);
		bomTransContext.clear(PubConstants.PUB_FIELD_FACTORLIST);
	}

}
