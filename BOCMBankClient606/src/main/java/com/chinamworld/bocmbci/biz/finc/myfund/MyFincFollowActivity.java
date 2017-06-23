package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.MyFincFollowAdapter;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.fundprice.FincFundDetailActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的基金 我关注的基金
 * 
 * @author 宁焰红
 * 
 */
public class MyFincFollowActivity extends FincBaseActivity {
	private final String TAG = "MyFincFollowActivity";
	/** 我关注的基金主view */
	private View myFincView = null;
	/** 基金信息列表 */
	private ListView listView = null;
	/** 关注的基金 */
	private List<Map<String, Object>> fundDetailFundList;
	
	private  MyFincFollowAdapter adapter;
	

	private boolean isCreate = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fundDetailFundList = fincControl.attentionFundList;
		init();
	}

	


	/**
	 * 初始化控件
	 */
	private void init() {
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_follow_main,
				null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_follow));
		//基金名称,单位净值,日净值增长率
		initListHeaderView(R.string.finc_fundname, R.string.finc_netvalue, R.string.fincn_daynetvaluerate);
		listView = (ListView) findViewById(R.id.finc_ListView);
		adapter = new MyFincFollowAdapter(this, fundDetailFundList);
		adapter.setConvertViewItermClicListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				fincControl.fundDetails = fundDetailFundList.get(position);
				
				String fundCode = (String) fincControl.fundDetails.get(Finc.FINC_FUNDCODE);
				//通过基金代码 获取到基金的基本信息
				getFincFund(fundCode);
				
			}
		});
		listView.setAdapter(adapter);
		
		initRightBtnForMain();
	}
	
	
	  /**
	    * 基金基本信息查询  回调处理
	    */
		@Override
		public void getFincFundCallback(Object resultObj) {
			super.getFincFundCallback(resultObj);
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			fincControl.fincFundDetails= (Map<String, Object>) biiResponseBody
					.getResult();
			goNextActivity();
		}
	
	private void goNextActivity(){
		
		Intent intent = new Intent(MyFincFollowActivity.this,
				FincFundDetailActivity.class);
		intent.putExtra(Finc.I_ATTENTIONFLAG, FincFundDetailActivity.ATTENTION);
		FincControl.getInstance().isAttentionFlag = true;
		FincControl.getInstance().setAttentionFlag(true);
		startActivity(intent);
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(!isCreate){
			fundDetailFundList = new ArrayList<Map<String,Object>>();
			adapter.notifyDataSetChanged(fundDetailFundList);
			BaseHttpEngine.showProgressDialogCanGoBack();
			attentionFundQuery();
		}else{
			isCreate = false;
		}
	}
	@Override
	public void attentionFundQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.attentionFundQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap
				.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST))) {
			BaseDroidApp.getInstanse().showMessageDialog(
					getString(R.string.finc_query_noresult_error),new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							MyFincFollowActivity.this.finish();
						}
					});
			return;
		}
		fincControl.attentionFundList = (List<Map<String, Object>>) resultMap
				.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST);
		fundDetailFundList = fincControl.attentionFundList;
		adapter.notifyDataSetChanged(fundDetailFundList);
	}

	

}