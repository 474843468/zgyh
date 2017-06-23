package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

/**
 * @ClassName: AgencyResultListActivity
 * @Description: 代理点查询结果列表页面
 * @author JiangWei
 * @date 2013-7-30 上午11:37:45
 */
public class AgencyResultListActivity extends DrawBaseActivity{
	
	/**  代理点列表 */
	private ListView listView;
	private String provinceId;
	private String strProvince;
	private AgencyResultAdapter adapter;
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	/** 总条目数 */
	private int totalCount = 0;
	/** listview的更多条目 */
	private View viewFooter;
	private Button btnMore;
	private String pageSize = "10";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.blpt_province_list, null);
		tabcontent.addView(view);
		setTitle(R.string.agence_query);
		//隐藏左侧菜单
		setLeftButtonPopupGone();
		//右上角按钮显示为“关闭”
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setText(R.string.close);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setResult(RESULT_OK);
					finish();
				}
			});
		}
		
		provinceId = this.getIntent().getStringExtra(PROVINCE_ID);
		strProvince = this.getIntent().getStringExtra(PROVINCE);
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化控件和数据
	 * @param  
	 * @return void
	 */
	private void init() {
		listView = (ListView) this.findViewById(R.id.blpt_lv_province);
		viewFooter = LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		listView.addFooterView(viewFooter);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		btnMore.setBackgroundColor(Color.TRANSPARENT);
		btnMore.setVisibility(View.GONE);
		btnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				requestPsnMobileAgentQuery();
			}
		});
		Map<String, Object> map = ((ObjectValueSerializable)getIntent().getSerializableExtra("agency_map")).getMap();
		if(map == null || map.isEmpty()){
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestPsnMobileAgentQuery();
		}else{
			initAgencyData(map);
		}
	}
	
	private void refreshListView(List<Map<String, Object>> list){
		if(adapter == null){
			adapter = new AgencyResultAdapter(this, list);
			listView.setAdapter(adapter);
		}else{
			adapter.setData(list);
		}
		
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
		map.put(DrawMoney.CURRENT_INDEX, mCurrentIndex);
		map.put(DrawMoney.PAGE_SIZE, pageSize);
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
		initAgencyData(map);
	}
	
	private void initAgencyData(Map<String, Object> map){
		String recordNumber = (String) map.get(DrawMoney.RECORD_NUMBER);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>>  dataList = (List<Map<String, Object>>) map.get(ConstantGloble.LIST);
		if(StringUtil.isNullOrEmpty(dataList)){
			dataList = (List<Map<String, Object>>) map.get("List");
		}
		if(dataList == null || dataList.size() == 0){
			BaseDroidApp.getInstanse().showMessageDialog(strProvince + this.getString(R.string.no_query_agency_result), new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					 finish();
				}
			});
		};
		for(int i= 0;i < dataList.size(); i++){
			listData.add((Map<String, Object>)dataList.get(i));
		}
		if(listData.size() >= totalCount){
			btnMore.setVisibility(View.GONE);
		}else{
			mCurrentIndex += Integer.parseInt(pageSize);
			btnMore.setVisibility(View.VISIBLE);
		}
		refreshListView(listData);
	}

}
