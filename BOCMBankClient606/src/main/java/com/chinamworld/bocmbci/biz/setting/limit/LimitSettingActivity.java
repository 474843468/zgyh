package com.chinamworld.bocmbci.biz.setting.limit;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.setting.adapter.LimitSettingListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LimitSettingActivity extends LimitSettingBaseActivity {
	private static final String TAG = "SettingBaseActivity";
	/***
	 * 显示用户交易限额列表的listview
	 */
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View childView = mainInflater.inflate(
				R.layout.setting_limitsetting_list, null);
		tabcontent.addView(childView);
		listView = (ListView) findViewById(R.id.listView1);
		setTitle(getResources().getString(R.string.set_title_limitsetting));
		right.setVisibility(View.GONE);
		setLeftSelectedPosition("settingManager_2");
		
		requestCommConversationId();
		BaseHttpEngine.showProgressDialogCanGoBack();
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("settingManager_2");
//	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		settingControl.editLimitConversationId = String.valueOf(BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		queryLimit();
	}

	@Override
	public void queryLimitCallback(Object resultObj) {
		super.queryLimitCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, String>> datalist = (List<Map<String, String>>) biiResponseBody
				.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		listView.setAdapter(new LimitSettingListAdapter(this, datalist));
	}



}
