package com.chinamworld.bocmbci.biz.prms.myaccount;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsAccsListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.server.LocalDataService;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属账户设定
 * 
 * @author xyl
 * 
 */
public class PrmsAccSettingActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsAccSettingActivity";
	/**
	 * 贵金属账户设定View
	 */
	private View prmsAccSettingView = null;
	/**
	 * 查询到账户信息
	 */
	private List<Map<String, String>> dataList = null;
	/**
	 * 可设定为贵金属账户的账户信息
	 */
	private ListView prmsAccListView;
	/**
	 * 如果没有账户，显示提示信息，如果有贵金属账户，显示贵金属账户信息
	 */
	private TextView accInfo;
	/**
	 * 下一步
	 */
	private Button next;
	/**
	 * 账户号码
	 */
	private String accNum;
	/**
	 * 账户类型
	 */
	private String accstyle;
	/**
	 * 账户别名
	 */
	private String accalais;
	/**
	 * 账户id
	 */
	private String accountId;

	private PrmsAccsListAdapter adapter;
	
	//accountIbkNum 联行号 wuhan
	private String ibkNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		dataList = prmsControl.prmsAccList;
		adapter = new PrmsAccsListAdapter(this, dataList);
		prmsAccListView.setAdapter(adapter);
		prmsAccListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 提前更新选中背景
				Map<String, String> map = dataList.get(position);
				accNum = map.get(Prms.QUERY_PRMSACCS_ACCOUNTNUMBER);
				accstyle = map.get(Prms.QUERY_PRMSACCS_ACCOUNTTYPE);
				accalais = map.get(Prms.QUERY_PRMSACCS_NICKNAME);
				accountId = map.get(Prms.QUERY_PRMSACCS_ACCOUNTID);
				String accNum = map.get(Prms.QUERY_PRMSACCS_ACCOUNTNUMBER);
				adapter.setSelectedPosition(accNum);
				
//				//wuhan 保存accountIbkNum
				ibkNum = map.get("accountIbkNum");
				LogGloble.i("info", "ibkNum=="+ibkNum);
				LocalDataService.getInstance().saveIbkNum(ConstantGloble.Prms, ibkNum);
				// adapter = new
				// PrmsAccsListAdapter(PrmsAccSettingActivity.this,
				// dataList, position);
				// prmsAccListView.setAdapter(adapter);
//				prmsAccListView.setSelection(position);
			}

		});
	}

	/**
	 * 初始化图像
	 */
	private void init() {
		settingbaseinit();
		prmsAccSettingView = mainInflater.inflate(R.layout.prms_accsetting,
				null);
		tabcontent.addView(prmsAccSettingView);
		accInfo = (TextView) findViewById(R.id.prms_accsetting_info);
		prmsAccListView = (ListView) prmsAccSettingView
				.findViewById(R.id.listview);
		prmsAccListView.setSelected(true);
		String title = getResources().getString(
				R.string.prms_title_accsetingconfirm);
		StepTitleUtils.getInstance().initTitldStep(this,
				prmsControl.getStepsForAccSetting());
		StepTitleUtils.getInstance().setTitleStep(1);
		setTitle(title);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
		String accInfoStr = null;
		accInfoStr = getResources().getString(
				R.string.prms_str_accsetting_header1);
		accInfo.setText(accInfoStr);
		right.setText(getResources().getString(R.string.switch_off));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		back.setVisibility(View.GONE);

	}

	/**
	 * 启动的页面结束时的回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:// 代表开通成功
			setResult(RESULT_OK);
			finish();
			break;

		}
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		prmsAccSetting(accountId, tokenId);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void prmsAccSettingCallBack(Object resultObj) {
		super.prmsAccSettingCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(this, PrmsAccSettingSuccessActivity.class);
		intent.putExtra(Prms.PRMS_ACCOUNTNUMBER, accNum);
		intent.putExtra(Prms.PRMS_ACCOUNTTYPE, accstyle);
		intent.putExtra(Prms.PRMS_NIKENAME, accalais);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.next:
			// // 参数： id，类型，别名
			// Intent intent = new Intent(PrmsAccSettingActivity.this,
			// PrmsAccSettingConfirmActivity.class);
			if (StringUtil.isNull(accNum) || StringUtil.isNull(accstyle)) {
				String errorInfo = getResources().getString(
						R.string.prms_notselectaaccount_error2);
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
				return;
			} else {
				// if (!StringUtil.isNullOrEmpty(prmsControl.accNum)
				// && prmsControl.accNum.equals(accNum)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog(
				// getString(R.string.prms_resetAcc_error));
				// } else {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
				// }
				// intent.putExtra(Prms.PRMS_ACCOUNTNUMBER, accNum);
				// intent.putExtra(Prms.PRMS_ACCOUNTTYPE, accstyle);
				// intent.putExtra(Prms.PRMS_NIKENAME, accalais);
				// intent.putExtra(Prms.PRMS_ACCOUNTID, accountId);
				// startActivityForResult(intent,
				// ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);
			}
			break;
		case R.id.ib_top_right_btn:
			finish();
			break;
		default:
			break;
		}
	}

}
