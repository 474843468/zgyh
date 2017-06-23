package com.chinamworld.bocmbci.biz.plps.interprovincial;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsMenu;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentDialogAdapter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/*
 * 跨省异地交通违法罚款 页面
 * @author zxj
 */
public class InterproLodisActivity extends PlpsBaseActivity{
	/**跨省异地交通违法罚款图片*/
	private int[] imageIds = new int[]{
			R.drawable.finespay,
			R.drawable.payquery
	};
	/**跨省异地交通违法罚款列表*/
	private String[] textIds = new String[]{
			"交通罚款缴纳",
			"交易查询"
	};
	/**列表数据加载*/
	private ArrayList<HashMap<String, Object>> accountList;
	/**点击按钮位置*/
//	private int mPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("跨省异地交通罚款");
		inflateLayout(R.layout.plps_interprov_main);
		accountList = new ArrayList<HashMap<String,Object>>();
		for(int i=0; i<imageIds.length; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			map.put("text", textIds[i]);
			accountList.add(map);
		}
		//初始化布局
		setUpView();
	}
	/**初始化布局*/
	private void setUpView(){
		GridView gridView = (GridView)findViewById(R.id.gridview);
		PaymentDialogAdapter interproAdapter = new PaymentDialogAdapter(this, accountList);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(interproAdapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
//		mPosition = position;
		if(position == 0){
			annuityIntentAction(0, PlpsMenu.INTERPROV);
		}else if (position == 1) {
			BaseHttpEngine.showProgressDialog();
			requestSystemDateTime();
		}
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
		PlpsDataCenter.getInstance().setSysDate(dateTime);	
		requestPsnCommonQueryAllChinaBankAccount();
	}
	/**帐号请求 只请求借记卡*/
	private void requestPsnCommonQueryAllChinaBankAccount(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> accountparams = new ArrayList<String>();
		accountparams.add("119");
		params.put(Comm.ACCOUNT_TYPE, accountparams);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCommonQueryAllChinaBankAccountCallBack");
	}
	@SuppressWarnings("unchecked")
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		PlpsDataCenter.getInstance().setAcctList(null);
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(list)){
			 return;
		}else {
			PlpsDataCenter.getInstance().setAcctList(list);
			annuityIntentAction(1, PlpsMenu.INTERPROV);
		}
	}
}
