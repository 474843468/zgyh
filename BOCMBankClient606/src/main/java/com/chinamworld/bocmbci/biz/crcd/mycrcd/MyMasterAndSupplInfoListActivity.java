package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
/**
 * 我的信用卡 信用卡附属卡管理
 * 
 * @author sunh
 * 
 */
public class MyMasterAndSupplInfoListActivity extends CrcdBaseActivity implements ICommonAdapter<CardEntity> {

	private View view;
	private List<Map<String, Object>> returnList = null;
	private List<Map<String, Object>> allAccountList = null;
	private Map<String, Object>accountid = null;
	private Map<String, Object>accounttype = null;
	private List<CardEntity> masterandsupplList = null;
	/** 信用卡列表 */
	private ListView myListView;
//	MasterAndSupplAdapter adapter;
	CommonAdapter adapter;
	/** 信用卡列表View */
	private View has_acc_view = null;
	/** 关联账户View */
	private View no_acc_view = null;
	/** 关联新账户按钮 */
	private Button btn_description = null;
	Button sureButton;
	TextView tv_title;
	public static int selectPostion = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("myCrcd_4");
		// 为界面标题赋值
		setTitle(this.getString(R.string.card_masterandsuppl_tittle));
		view = addView(R.layout.crcd_masterandsuppl_list);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		btn_right.setVisibility(View.GONE);
		returnList = new ArrayList<Map<String, Object>>();
		accountid= new  HashMap<String, Object>();
		accounttype= new  HashMap<String, Object>();
		masterandsupplList = new ArrayList<CardEntity>();
		// 获取信用卡列表
		BaseDroidApp.getInstanse().setCurrentAct(this);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCrcdList();

	}

	public void requestCrcdList() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ZHONGYIN, GREATWALL, SINGLEWAIBI };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestCrcdListCallBack");
	}

	/**
	 * 请求信用卡列表回调
	 */
	public void requestCrcdListCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnList = (List<Map<String, Object>>) body.getResult();
		
		if (returnList == null || returnList.size() <= 0) {
			// 没有信用卡---关联信用卡
			BaseHttpEngine.dissMissProgressDialog();
			BaseHttpEngine.canGoBack = false;
			init();
			getViewDate();
			
			return;
		}else{
		
			for(int i=0;i<returnList.size();i++){
				
				String accountnum=String.valueOf(returnList.get(i).get(
						Crcd.CRCD_ACCOUNTNUMBER_RES));
				String accountId = String.valueOf(returnList.get(i).get(
						Crcd.CRCD_ACCOUNTID_RES));
				String accountType = String.valueOf(returnList.get(i).get(
						Crcd.CRCD_ACCOUNTTYPE_RES));
				accountid.put(accountnum, accountId);
				accounttype.put(accountnum, accountType);
			}
			
			requestPsnCrcdQueryMasterAndSupplInfo();
			
		}
	
	}

	/** 信用卡主附卡信息查询 */
	private void requestPsnCrcdQueryMasterAndSupplInfo() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYMASTERANDSUPPLINFO);
		String accountId = String.valueOf(returnList.get(0).get(
				Crcd.CRCD_ACCOUNTID_RES));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdQueryMasterAndSupplInfoCallBack");

	}

	@SuppressWarnings({ "unused", "unchecked" })
	public void requestPsnCrcdQueryMasterAndSupplInfoCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body
				.getResult();
//		if (StringUtil.isNullOrEmpty(returnList)) {
//			BiiHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					this.getString(R.string.acc_transferquery_null));
//			return;
//		}
		if (returnList == null || returnList.size() <= 0) {

		} else {
			// 主副卡标示，主卡 卡号 ，副卡卡号
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				CardEntity Master = new CardEntity();
				Master.setMastercrcdNum(String.valueOf(returnList.get(i).get(Crcd.CRCD_MASTERCRCDNUM)));
				Master.setMasterorsuppl("1");
				
				List<Map<String, Object>> supplList = (List<Map<String, Object>>) returnList
						.get(i).get("list");
				if(!StringUtil.isNullOrEmpty(supplList)){					
					masterandsupplList.add(Master);
				for (int j = 0; j < supplList.size(); j++) {
					CardEntity suppl = new CardEntity();
					String accountnum=String.valueOf(returnList.get(i).get(Crcd.CRCD_MASTERCRCDNUM));
					suppl.setMastercrcdNum(accountnum);
					suppl.setSubcrcdNum(String.valueOf(supplList.get(j).get(
							"subCrcdNum")));
					suppl.setSubcrcdname(String.valueOf(supplList.get(j).get(
							"subCrcdHolder")));
					suppl.setMasterorsuppl("2");
					suppl.setAccountid(String.valueOf(accountid.get(accountnum)));
					suppl.setAccounttype(String.valueOf(accounttype.get(accountnum)));
					masterandsupplList.add(suppl);
				}
				}
			}

		}
		BaseHttpEngine.dissMissProgressDialog();
		BaseHttpEngine.canGoBack = false;
		init();
		getViewDate();
		

	}

	/** 初始化界面 */
	private void init() {
		has_acc_view = view.findViewById(R.id.has_acc);
		no_acc_view = view.findViewById(R.id.no_acc);
		btn_description = (Button) view.findViewById(R.id.btn_description);
		btn_description.setVisibility(View.GONE);//屏蔽自助关联
		btn_description.setOnClickListener(goRelevanceClickListener);
		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);

	}

	/** 根据返结果加载布局 */
	private void getViewDate() {
		if (returnList == null || returnList.size() <= 0) {
			// 没有信用卡---关联信用卡
			no_acc_view.setVisibility(View.VISIBLE);

			btn_right.setVisibility(View.GONE);
//			setLeftButtonPopupGone();
//			setLeftGoneAfterVisiable();
			has_acc_view.setVisibility(View.GONE);
			return;
		}

		else {
			if(masterandsupplList==null||masterandsupplList.size()<=0){
//				if(masterandsupplList!=null||masterandsupplList.size()>0){//测试代码
			// 没有附属卡	
				btn_right.setVisibility(View.GONE);
				has_acc_view.setVisibility(View.VISIBLE);
				SpannableString sp = new SpannableString(this.getString(R.string.cardsuppllist_isnull));
////				final Intent userIntent = new Intent();
////
////				userIntent.setClass(MyMasterAndSupplInfoListActivity.this, AccInputRelevanceAccountActivity.class);
////			
//				sp.setSpan(new IntentSpan(new OnClickListener() {
//
//					public void onClick(View view) {
//
////						MyMasterAndSupplInfoListActivity.this.startActivity(userIntent);
//						BusinessModelControl.gotoAccRelevanceAccount(MyMasterAndSupplInfoListActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
////						finish();
//					}
//
//				}), 53, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);		
				BaseDroidApp.getInstanse().showcancheckErrorDialog(sp,exitClick);
				
			}else{
				btn_right.setVisibility(View.GONE);
				no_acc_view.setVisibility(View.GONE);
				has_acc_view.setVisibility(View.VISIBLE);
				setListViewDate();	
			}
			
		}
	}

	/** 为listView赋值 */
	private void setListViewDate() {

//		adapter = new MasterAndSupplAdapter(this, masterandsupplList);
		adapter=new CommonAdapter<CardEntity>(MyMasterAndSupplInfoListActivity.this, masterandsupplList, this);
		adapter.notifyDataSetChanged();
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CardEntity cardentity = (CardEntity) adapter.getItem(position);

				if (cardentity.getMasterorsuppl().equals("1")) {
					return;
				} else {
					String mastercrcdNum = cardentity.getMastercrcdNum();
					String subcrcdNum = cardentity.getSubcrcdNum();
					String accountid = cardentity.getAccountid();
					String accounttype = cardentity.getAccounttype();
					Intent intent = new Intent(
							MyMasterAndSupplInfoListActivity.this,
							MySupplymentDetailNewActivity.class);
					intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, mastercrcdNum);
					intent.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subcrcdNum);
					intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountid);
					intent.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, accounttype);
					startActivity(intent);
				}

			}
		});
	}

	/** 进行自助关联监听事件 */
	OnClickListener goRelevanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

//			startActivityForResult((new Intent(
//					MyMasterAndSupplInfoListActivity.this,
//					AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			BusinessModelControl.gotoAccRelevanceAccount(MyMasterAndSupplInfoListActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);

		}

	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			BaseHttpEngine.showProgressDialog();
			requestCrcdList();
			break;

		default:
			break;
		}
	}

		protected OnClickListener exitClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				finish();
			}
		};

		@Override
		public View getView(int arg0, CardEntity currentItem,
				LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
			ViewHodler h;

			if (convertView == null) {
				h = new ViewHodler();
				convertView = inflater.inflate(R.layout.card_masterandsuppl_item,null);			
				h.card_bg= (LinearLayout) convertView.findViewById(R.id.card_bg);
				h.num= (RelativeLayout) convertView.findViewById(R.id.num);
				h.name= (RelativeLayout) convertView.findViewById(R.id.name);
				h.card_num= (TextView) convertView.findViewById(R.id.card_num);
				h.card_numvalue= (TextView) convertView.findViewById(R.id.card_numvalue);
				h.card_name= (TextView) convertView.findViewById(R.id.card_name);
				h.card_namevalue= (TextView) convertView.findViewById(R.id.card_namevalue);
				
				convertView.setTag(h);
			} else {
				h = (ViewHodler) convertView.getTag();
			}
			
//			CardEntity cardentity = masterandsupplList.get(position);
			
			if(currentItem.getMasterorsuppl().equals("1")){
				// 显示组卡信息
				h.card_bg.setBackgroundResource(R.color.outlay_line);
				h.num.setVisibility(View.VISIBLE);
				h.name.setVisibility(View.GONE);
				h.card_num.setText(getString(R.string.mycrcd_carFlag_zhuka1));
				h.card_numvalue.setText(StringUtil.getForSixForString(currentItem.getMastercrcdNum()));
				
			}else// (cardentity.getMasterorsuppl().equals("2"))
			{
				h.card_bg.setBackgroundResource(R.color.white);
				h.name.setVisibility(View.VISIBLE);
				h.card_num.setText(getString(R.string.mycrcd_fushuka_num));
				h.card_numvalue.setText(StringUtil.getForSixForString(currentItem.getSubcrcdNum()));
				h.card_name.setText(getString(R.string.mycrcd_fushuka_card_name));
				h.card_namevalue.setText(currentItem.getSubcrcdname());
				
			}
			
			return convertView;
		}
		public class ViewHodler {
			public  LinearLayout card_bg;
			public  RelativeLayout num;// 卡号布局
			public  RelativeLayout name;	//	
			public TextView card_num;// 卡号
			public TextView card_numvalue;// 卡号值
			public TextView card_name;// 账号名称
			public TextView card_namevalue;//账号名称 值
			
		}
}
