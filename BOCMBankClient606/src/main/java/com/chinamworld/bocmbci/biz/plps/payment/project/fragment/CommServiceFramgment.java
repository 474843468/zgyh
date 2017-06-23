package com.chinamworld.bocmbci.biz.plps.payment.project.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.biz.plps.liveservice.CommServiceDetailActivity;
import com.chinamworld.bocmbci.biz.plps.order.OrderAdressCityActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.mode.IActionCall;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.userwidget.QueryView;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

public class CommServiceFramgment extends Fragment{
	
	static BaseActivity baseActivity;
	public static String dateTime;
	private View view;
	//查询框
	private static QueryView queryView;
	//服务类别
	private static TextView serviceCategory;
	//查询后头布局
//	private LinearLayout resultCondition;
	//查询后头布局时间
	private TextView queryDate;
	//查询结果listView
	private static RelativeLayout layoutResult;
	//查询数据列表
	private ListView listView;
	/** 查询接口上送数据项  */
	private static Map<String,Object> questParamMap = new HashMap<String,Object>();
	private final int SERVICECATEGORY=101;
	private int serviceCategoryIndex;
	private static String menusId;
	private static CommonAdapter<Map<String,Object>> adapter;
	private static List<Map<String,Object>> queryList = new ArrayList<Map<String, Object>>();
	/**当前页数*/
	private int currentIndex=0;
	//listView中选择点击位置
	private int selectionPosition;
	private static int totalNumber;
	//地址
	private TextView cityAdres;
	//最近一周
	private View btnAccOnweek;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.plps_live_list, container, false);
			return view;
	}
	private void init(){
		queryView = (QueryView)view.findViewById(R.id.queryControlView);
		serviceCategory = (TextView)view.findViewById(R.id.spPlanname);
		serviceCategory.setOnClickListener(serviceCategoryClickListener);
//		resultCondition = (LinearLayout)view.findViewById(R.id.result_condition);
		queryDate = (TextView)view.findViewById(R.id.query_date);
		layoutResult = (RelativeLayout)view.findViewById(R.id.layout_result);
		listView = (ListView)view.findViewById(R.id.listview);
		listView.setOnItemClickListener(listItemClickListener);
		cityAdres = (TextView) CommServiceFramgment.this.getActivity()
				.findViewById(R.id.cityAdress);
		cityAdres.setOnClickListener(cityAdresClick);
		btnAccOnweek = view.findViewById(R.id.btn_acc_onweek);
	
		queryView.initControl(dateTime, listView, new IActionCall() {
			
			@Override
			public void callBack() {
				queryView.setStartTime(QueryDateUtils.getlastWeek(dateTime));
				queryView.setQueryTimeText("最近一周；最近一个月；最近三个月");
				requestHistoryRecods(0,true,false);
				
			}
		});
		btnAccOnweek.performClick();
		adapter = new CommonAdapter<Map<String,Object>>(getActivity(), listView, queryList, new ICommonAdapter<Map<String,Object>>(){

			@Override
			public View getView(int position, Map<String, Object> currentItem,
					LayoutInflater inflater, View convertView,
					ViewGroup viewGroup) {
				ViewHodler h;
				if (convertView == null) {
					h = new ViewHodler();
					convertView = View.inflate(getActivity(), R.layout.plps_simple_item, null);
					h.mTextView1 = (TextView) convertView.findViewById(R.id.text1);
					PlpsUtils.setOnShowAllTextListener(getActivity(), h.mTextView1);
					h.mTextView2 = (TextView) convertView.findViewById(R.id.text2);
					h.mTextView3 = (TextView) convertView.findViewById(R.id.text3);
					convertView.setTag(h);
				}else{
					h = (ViewHodler) convertView.getTag();
				}
				final Map<String, Object> map = currentItem;
				
				h.mTextView1.setText((String)map.get(Plps.TRANSTIME));
				h.mTextView2.setText((String)map.get(Plps.MENUNAME));
//					h.mTextView3.setVisibility(View.GONE);
				h.mTextView3.setText((String)map.get(Plps.CITYNAME));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(getActivity(),h.mTextView2);
				return convertView;
			}});
		
		adapter.setRequestMoreDataListener(new IFunc<Boolean>() {
			
			@Override
			public Boolean callBack(Object param) {
		
				currentIndex = currentIndex +10;
				requestHistoryRecods(currentIndex,false, false);
				return true;

			}
		});
		
		}

	public class ViewHodler {
		public TextView mTextView1;
		public TextView mTextView2;
		public TextView mTextView3;
	}
	OnClickListener cityAdresClick = new OnClickListener() {
		
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(CommServiceFramgment.this.getActivity(),
					OrderAdressCityActivity.class);
			intent.putExtra(Plps.PRVCDISPNAME, PlpsDataCenter.getInstance().getPrvcDispName());
			intent.putExtra(Plps.CITYDISPNAME, PlpsDataCenter.getInstance().getCityDispName());
//			startActivity(intent);
			startActivityForResult(intent, 1);
			
		}
	};
	
	OnClickListener serviceCategoryClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			List<String> mList = PublicTools.getSpinnerDataWithDefaultValue(PlpsDataCenter.getInstance().getLiveMenus(), Plps.MENUSNAME,Plps.SP_ALLTXT);
			createDialog(mList,"服务类别",SERVICECATEGORY,serviceCategoryIndex);
		}
	};
	OnItemClickListener listItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			selectionPosition = position;
			PlpsDataCenter.getInstance().setHistoryRecodDetail(queryList.get(position));
			String flowFileId = (String)queryList.get(position).get(Plps.FLOWFILEID);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Plps.FLOWFILEID, flowFileId);
			BaseHttpEngine.showProgressDialog();
			baseActivity.getHttpTools().requestHttp(Plps.PSNPLPSGETHISVALFLAG, params, new IHttpResponseCallBack<Map<String, Object>>() {

				@Override
				public void httpResponseSuccess(Map<String, Object> result, String method) {
					// TODO Auto-generated method stub
					BaseHttpEngine.dissMissProgressDialog();
					if(StringUtil.isNullOrEmpty(result)){
						return;
					}
					String valFlag = (String)result.get("valFlag");
					startActivityForResult(new Intent(getActivity(), CommServiceDetailActivity.class)
						.putExtra("valFlag", valFlag), 1001);
				}
			});
		}
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 10111){
			/**将在缴费详情页面添加常用成功的服务的isOftenUse改为false*/
			setFlowisOftenUse();
		}else if (resultCode == 111) {
			queryList.remove(selectionPosition);
			adapter.setSourceList(queryList, totalNumber-1);
		}else if (requestCode == 1) {
			if(resultCode == CommServiceFramgment.this.getActivity().RESULT_OK ){
				String prvcName = data.getStringExtra(Plps.PRVCDISPNAME);
				String cityName = data.getStringExtra(Plps.CITYDISPNAME);
				if(PlpsDataCenter.municiplGovernment.contains(prvcName)){
					cityAdres.setText(prvcName);
				}else {
					cityAdres.setText(prvcName + cityName);
				}
				PlpsDataCenter.getInstance().setPrvcDispName(null);
				PlpsDataCenter.getInstance().setCityDispName(null);
				PlpsDataCenter.getInstance().setCityDispName(cityName);
				PlpsDataCenter.getInstance().setPrvcDispName(prvcName);
			}
		}
	};
	/**将在缴费详情页面添加常用成功的服务的isOftenUse改为false*/
	private void setFlowisOftenUse(){
		for(int i=0; i<queryList.size(); i++){
			String flowFileId = (String)queryList.get(selectionPosition).get(Plps.FLOWFILEID);
			String flowFileIdt = (String)queryList.get(i).get(Plps.FLOWFILEID);
			if(flowFileId.equals(flowFileIdt)){
				queryList.get(i).put(Plps.ISOFTENUSE, "1");
			}
		}
	}
	
	/**
	 * 创建spinner弹窗框
	 */
	private void createDialog(List<String> list,String title, int vId, int position){
		ListView listView = new ListView(baseActivity);
		listView.setId(vId);
		listView.setFadingEdgeLength(0);
		listView.setOnItemClickListener(serviceCategoryItemListener);
		listView.setScrollbarFadingEnabled(false);
		AnnuitySpinnerAdapter adapter = new AnnuitySpinnerAdapter(baseActivity, list, position);
		listView.setAdapter(adapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(listView, title);
	}
	public OnItemClickListener serviceCategoryItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			BaseDroidApp.getInstanse().dismissMessageDialog();
			serviceCategoryIndex = position;
			serviceCategory.setText(PublicTools.getSpinnerDataWithDefaultValue(
					PlpsDataCenter.getInstance().getLiveMenus(),
					Plps.MENUSNAME, Plps.SP_ALLTXT).get(position));
			if(position>0){
				menusId = (String)PlpsDataCenter.getInstance().getLiveMenus().get(position-1).get(Plps.MENUSID);
			}
			
		}
	};
	
	/**
	 * 请求常用缴费列表
	 * @param refresh
	 * isPaymentProject 表示是否从常用项目滑到该查询页面
	 */
	public static void requestHistoryRecods(int currentIndex, final Boolean refresh, Boolean isPaymentProject){
		questParamMap.put(Plps.PAGESIZE, ConstantGloble.FOREX_PAGESIZE);
		questParamMap.put(Plps.REFRESH, String.valueOf(refresh));
		questParamMap.put(Plps.CURRENTINDEX, String.valueOf(currentIndex));
		if(!isPaymentProject){
			if(serviceCategory.getText().toString().equals("全部")){
				
			}else {
				questParamMap.put(Plps.MUNUID, menusId);
			}
		}
		if(isPaymentProject){
			questParamMap.put(Plps.STARTDATE, QueryDateUtils.getlastWeek(dateTime));
			questParamMap.put(Plps.ENDDATE, QueryDateUtils.getcurrentDate(dateTime));
		}else {
			questParamMap.put(Plps.STARTDATE, queryView.getStartTime());
			questParamMap.put(Plps.ENDDATE, queryView.getEndTime());
		}
		
		BaseHttpEngine.showProgressDialog();
		baseActivity.getHttpTools().requestHttpWithConversationId(Plps.METHODHISTORYRECORDS, questParamMap, null, new IHttpResponseCallBack<Map<String, Object>>() {

			@Override
			public void httpResponseSuccess(Map<String, Object> result,
					String method) {
				BaseHttpEngine.dissMissProgressDialog();
				
				List<Map<String, Object>> list = (List<Map<String,Object>>)result.get(Plps.HISTORYRECORDSLIST);
				if(StringUtil.isNullOrEmpty(list)){
					BaseDroidApp.getInstanse().showInfoMessageDialog("没有符合查询条件的结果"); return;
				}
				layoutResult.setVisibility(View.VISIBLE);
//				queryList=list;
				if(refresh){
					queryList.clear();
					queryList.addAll(list);
					totalNumber = Integer.parseInt( (String)result.get("recordNumber"));
					adapter.setSourceList(queryList, Integer.parseInt( (String)result.get("recordNumber")));
					queryView.scaleQueryControl();
				}else {
//					queryList.addAll(list);
					adapter.addSourceList(list);
				}
			}
		});
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		baseActivity = (BaseActivity)activity;
		BaseHttpEngine.showProgressDialog();
		baseActivity.getHttpTools().requestHttp(Comm.QRY_SYSTEM_TIME, null, new IHttpResponseCallBack<Map<String, Object>>() {

			@Override
			public void httpResponseSuccess(Map<String, Object> result, String method) {
				// TODO Auto-generated method stub
				BaseHttpEngine.dissMissProgressDialog();
				dateTime = (String)result.get(Comm.DATETME);
				init();
			}
		});
	}
}
