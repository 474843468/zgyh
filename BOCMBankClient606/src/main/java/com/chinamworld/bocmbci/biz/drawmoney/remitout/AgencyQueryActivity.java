package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.entity.ObjectValueSerializable;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * @ClassName: AgencyQueryActivity
 * @Description: 代理点查询
 * @author JiangWei
 * @date 2013-7-29 上午9:49:10
 */
public class AgencyQueryActivity extends DrawBaseActivity implements OnItemClickListener {
	protected static final String TAG = AgencyQueryActivity.class.getSimpleName();
	/** 省份列表数据 */
	private List<String> provinceListData;
	/** 省份id列表数据 */
	private List<String> provinceIdListData;
	/** 搜索后的省份列表 */
	private List<String> provinceListAfterSearch;
	/** 搜索后的省份id列表 */
	private List<String> provinceIdListAfterSearch;
	/** 列表view */
	private ListView listView;
	/** 选中省份的id */
	private String provinceId;
	private String strProvince = "";
	/** 输入框 */
	private EditText searchEdit;
	/** 搜索按钮 */
	private Button btnSearch;
	/** listview的adapter */
	private ProvinceListAdapter adapter;
	/** 是否是查询的模式 */
	private boolean isSearchMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.drawmoney_agency_activity, null);
		tabcontent.addView(view);
		setTitle(R.string.agence_query);
		// 隐藏左侧菜单
		setLeftButtonPopupGone();
		// 隐藏返回按钮
		this.findViewById(R.id.ib_back).setVisibility(View.GONE);
		// 右上角按钮显示为“关闭”
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setText(R.string.close);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}

		init();
	}

	private void init() {
		listView = (ListView) this.findViewById(R.id.blpt_lv_province);
		searchEdit = (EditText) this.findViewById(R.id.edit_search);
		btnSearch = (Button) this.findViewById(R.id.btn_query_trans_records);
		provinceListData = Arrays.asList(this.getResources().getStringArray(R.array.provinceList));
		provinceIdListData = Arrays.asList(this.getResources().getStringArray(R.array.provinceIdList));

		provinceListAfterSearch = new ArrayList<String>();
		provinceIdListAfterSearch = new ArrayList<String>();

		refreshListView(provinceListData);
		listView.setOnItemClickListener(this);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isSearchMode = true;
				excuseSearch(false);
			}
		});
		searchEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 为空+搜索过+搜索列表为空
				if (StringUtil.isNullOrEmpty(s) && isSearchMode && StringUtil.isNullOrEmpty(provinceListAfterSearch)) {
					isSearchMode = true;
					excuseSearch(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 执行搜索
	 */
	private void excuseSearch(boolean isAuto) {

		provinceListAfterSearch.clear();
		provinceIdListAfterSearch.clear();
		String searchStr = searchEdit.getText().toString().trim();
		for (int i = 0; i < provinceListData.size(); i++) {
			String str = provinceListData.get(i);
			if (str.contains(searchStr)) {
				provinceListAfterSearch.add(str);
				provinceIdListAfterSearch.add(provinceIdListData.get(i));
			}
		}
		refreshListView(provinceListAfterSearch);
		// 关闭键盘
		// closeInput();
		if (!isAuto) {
			closeInput(searchEdit);
			if (StringUtil.isNullOrEmpty(provinceListAfterSearch)) {
				CustomDialog.toastInCenter(this, this.getString(R.string.no_list_data_for_search_province));
			}
		}
	}

	/**
	 * 刷新列表
	 */
	private void refreshListView(List<String> list) {
		if (adapter == null) {
			adapter = new ProvinceListAdapter(this, list);
			listView.setAdapter(adapter);
		} else {
			adapter.setData(list);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (isSearchMode) {
			provinceId = provinceIdListAfterSearch.get(position);
			strProvince = provinceListAfterSearch.get(position);
		} else {
			provinceId = provinceIdListData.get(position);
			strProvince = provinceListData.get(position);
		}
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestPsnMobileAgentQuery();
	}
	
	/**
	 * @Title: requestPsnMobileAgentQuery
	 * @Description:代理点查询
	 * @param  
	 * @return void
	 */
	private void requestPsnMobileAgentQuery(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_AGENT_QUERY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DrawMoney.PRV_IBK_NUM, provinceId);
		map.put(DrawMoney.CURRENT_INDEX, 0);
		map.put(DrawMoney.PAGE_SIZE, "10");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileAgentQueryCallback");
	}
	
	/**
	 * @Title: requestPsnMobileAgentQueryCallback
	 * @Description: 代理点查询的回调
	 * @param @param resultObj 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnMobileAgentQueryCallback(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		List<Map<String, Object>>  dataList = (List<Map<String, Object>>) map.get(ConstantGloble.LIST);
		if(StringUtil.isNullOrEmpty(dataList)){
			dataList = (List<Map<String, Object>>) map.get("List");
		}
		if(dataList == null || dataList.size() == 0){
			BaseDroidApp.getInstanse().showMessageDialog(strProvince + this.getString(R.string.no_query_agency_result), new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			return;
		};
		ObjectValueSerializable mObjectValueSerializable = new ObjectValueSerializable(map);
		Intent intent = new Intent(this, AgencyResultListActivity.class);
		intent.putExtra(PROVINCE_ID, provinceId);
		intent.putExtra(PROVINCE, strProvince);
		intent.putExtra("agency_map", mObjectValueSerializable);
		startActivityForResult(intent, 1001);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1001) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}

}
