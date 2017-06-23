//package com.chinamworld.bocmbci.biz.plps.liveservice;
//
//import java.util.Map;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.bii.constant.Plps;
//import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
//import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
//import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
//
///**
// * 我的民生主页
// * @author panwe
// *
// */
//public class LiveServiceMainActivity extends PlpsBaseActivity{
//	private int mPosition;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		inflateLayout(R.layout.plps_main);
//		setTitle(R.string.plps_liveservice_title);
////		mRightButton.setVisibility(View.GONE);
//		setUpView();
//	}
//	
//	private void setUpView(){
//		ListView mListView = (ListView) findViewById(R.id.listview);
//		SimpleAdapter mAdapter = new SimpleAdapter(this, PlpsDataCenter.getInstance().getLiveMenus()
//				,R.layout.plps_main_item, new String[] {Plps.MENUSNAME}, new int[] {R.id.menusName});
//		mListView.setOnItemClickListener(this);
//		mListView.setAdapter(mAdapter);
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//		super.onItemClick(parent, view, position, id);
//		mPosition = position;
//		BaseHttpEngine.showProgressDialog();
//		requestSystemDateTime();
//	}
//
//	@Override
//	public void requestSystemDateTimeCallBack(Object resultObj) {
//		super.requestSystemDateTimeCallBack(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
//		PlpsDataCenter.getInstance().setSysDate(dateTime);
//		Map<String, Object> map = PlpsDataCenter.getInstance().getLiveMenus().get(mPosition);
//		String menusId = (String) map.get(Plps.MENUSID);
//		startActivity(new Intent(this, CommServiceActivity.class).putExtra(Plps.MENUSID, menusId));
//	}
//}
