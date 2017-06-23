package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 中银理财-投资协议申请页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyAgreementApplyActivity extends BocInvtBaseActivity implements ICommonAdapter<Map<String, Object>>{

	/**用户选择的账户 产品信息*/
//	private Map<String, Object> chooseMap; 
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
	/** 用户选择的账户信息 */
	private Map<String, Object> accound_map;
//	/** 投资协议列表 */
//	private Map<String, Object> result_agreement;
	/** 用户选择的协议类型，中文文字 */
	/** 所有数据列表 */
	private List<Map<String, Object>> list_date_all=new ArrayList<Map<String,Object>>();
	private int currentIndex=0,total_number;
	private TextView tv_sp1;
	private TextView tv_sp2;
	private View layout_invest_way;
	private ListView listview;
	private CommonAdapter<Map<String, Object>> adapter_listview;
	private RelativeLayout layout_btn_more;
	private Button btn_load_more;
	private boolean refresh;
	private int postion_sp1,postion_sp2;
	/**true/智能协议*/
	private boolean zn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_agreement);
		initUI();
		initDate();
	}
	/**
	 * 初始化listview中的"更多"按钮
	 */
	private void initButton_more(){
		layout_btn_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, listview,false);
		btn_load_more = (Button) layout_btn_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestPsnXpadProductInvestTreatyQuery(false);
			}
		});
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		initButton_more();
		layout_invest_way = findViewById(R.id.layout_invest_way);
		listview = (ListView) findViewById(R.id.listview);
		tv_sp1 = (TextView) findViewById(R.id.tv_sp1);
		tv_sp2 = (TextView) findViewById(R.id.tv_sp2);
		//赋值
		addHeaderViewForListView(listview);
		setListView(listview);
		tv_sp1.setText(BocInvestControl.list_agrType.get(0));
		tv_sp2.setText(BocInvestControl.list_instType.get(0));
		tv_sp1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog(BocInvestControl.list_agrType,getResources().getString(R.string.bocinvt_tv_15),
						BocInvestControl.list_agrType.indexOf(((TextView)v).getText()),BocInvestControl.TV_SP1_ID);
			}
		});
		tv_sp2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog(BocInvestControl.list_instType,getResources().getString(R.string.bocinvt_tv_53),
						BocInvestControl.list_instType.indexOf(((TextView)v).getText()),BocInvestControl.TV_SP2_ID);
			}
		});
	}
	/**
	 * 创建spinner弹窗框
	 * @param list
	 * @param title
	 * @param position
	 */
	private void createDialog(List<String> list,String title,int position,int vId){
		ListView mListView = new ListView(this);
		mListView.setId(vId);
		mListView.setFadingEdgeLength(0);
		mListView.setOnItemClickListener(onItemClickListener);
		mListView.setScrollingCacheEnabled(false);
		AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(this, list,position);
		mListView.setAdapter(mAdapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(mListView, title);
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			if (parent.getId()==BocInvestControl.TV_SP1_ID) {
				postion_sp1 = position;
				zn = BocInvestControl.list_agrType.get(position).equals(BocInvestControl.map_agrType_code_key.get("1"));
				dateFilter();
			}else if (parent.getId()==BocInvestControl.TV_SP2_ID) {
				postion_sp2 = position;
				dateFilter();
			}
		}
	};
	/**
	 *设置listview 
	 * @param listview
	 */
	private void setListView(ListView listview){//协议列表点事件
		adapter_listview=new CommonAdapter<Map<String, Object>>(ProductQueryAndBuyAgreementApplyActivity.this, 
				list_date_all, this);
		listview.setAdapter(adapter_listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2==0) {
					return;
				}
				BocInvestControl.map_listview_choose.clear();
				BocInvestControl.map_listview_choose.putAll(adapter_listview.getSourceList().get(arg2-1));
				Intent intent = new Intent(ProductQueryAndBuyAgreementApplyActivity.this,ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity.class);
				startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
			}
		});
	}
	private void addHeaderViewForListView(ListView lv){
		if (lv.getHeaderViewsCount()>0) {
			return;
		}
		View view = LayoutInflater.from(this).inflate(R.layout.product_query_and_buy_listview_headerview, lv,false);
		TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
		TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
		TextView tv_3 = (TextView) view.findViewById(R.id.tv_3);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_3);
		lv.addHeaderView(view);
	}
	/**
	 * 根据用户选择	筛选数据
	 */
	private void dateFilter(){
		requestPsnXpadProductInvestTreatyQuery(true);
//		}
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap=BociDataCenter.getInstance().getDetailMap();
		accound_map=BocInvestControl.accound_map;
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnXpadProductInvestTreatyQuery(true);
	}
	/**
	 * 请求 投资协议列表查询
	 * @param _refresh 是否重新加载
	 */
	public void requestPsnXpadProductInvestTreatyQuery(boolean _refresh){
		refresh=_refresh;
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCI_ACCOUNTID_REQ, accound_map.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES).toString());
		parms_map.put(BocInvestControl.PROID, detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES).toString());
		parms_map.put(BocInvt.BOCINCT_XPADTRAD_PAGESIZE_REQ, BocInvestControl.PAGESIZE);
		parms_map.put(BocInvestControl.AGRTYPE, BocInvestControl.map_agrType_code_value.get(BocInvestControl.list_agrType.get(postion_sp1)));
		if (zn) {
			parms_map.put(BocInvestControl.INSTTYPE, BocInvestControl.map_instType_code_value.get(BocInvestControl.list_instType.get(postion_sp2)));
		}else {
			parms_map.put(BocInvestControl.INSTTYPE, "0");
		}
		if (_refresh) {
			parms_map.put(BocInvt.BOCINCT_XPADTRAD_CURRENTINDEX_REQ, "0");
		}else {
			parms_map.put(BocInvt.BOCINCT_XPADTRAD_CURRENTINDEX_REQ, currentIndex+"");
		}
		parms_map.put(BocInvt.BOCINCT_XPADTRAD_REFRESH_REQ, String.valueOf(_refresh));
		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvestControl.PSNXPADPRODUCTINVESTTREATYQUERY, "requestPsnXpadProductInvestTreatyQueryCallBack", parms_map, true);
	}
	/**
	 * 请求 投资协议列表查询 回调
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadProductInvestTreatyQueryCallBack(Object resultObj){
		if (zn) {
			layout_invest_way.setVisibility(View.VISIBLE);
		}else {
			layout_invest_way.setVisibility(View.GONE);
			postion_sp2 = 0;
		}
		tv_sp1.setText(BocInvestControl.list_agrType.get(postion_sp1));
		tv_sp2.setText(BocInvestControl.list_instType.get(postion_sp2));
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Map<String, Object> result_map = (Map<String, Object>)getHttpTools().getResponseResult(resultObj);
		List<Map<String, Object>> list_agreement_all = (List<Map<String, Object>>) result_map.get(BocInvestControl.key_list);
		if (StringUtil.isNullOrEmpty(list_agreement_all)) {
			list_date_all.clear();
			adapter_listview.notifyDataSetChanged();
			if (listview.getFooterViewsCount()>0) {
				listview.removeFooterView(layout_btn_more);
			}
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("没有可支持的协议");
			return;
		}
		if (refresh) {
			total_number=Integer.parseInt(result_map.get("recordNumber").toString());
			currentIndex=list_agreement_all.size();
			list_date_all.clear();
			list_date_all.addAll(list_agreement_all);
		}else {
			currentIndex+=list_agreement_all.size();
			list_date_all.addAll(list_agreement_all);
		}
//		tv_sp1.setText(BocInvestControl.list_agrType.get(postion_sp1));
//		tv_sp2.setText(BocInvestControl.list_instType.get(postion_sp2));
//		if (zn) {
//			layout_invest_way.setVisibility(View.VISIBLE);
//		}else {
//			layout_invest_way.setVisibility(View.GONE);
//		}
		addFoolterView();
		adapter_listview.notifyDataSetChanged();
		BaseHttpEngine.dissMissProgressDialog();
	}
	private void addFoolterView(){
		if (total_number>currentIndex) {
			if (listview.getFooterViewsCount()<=0) {
				listview.addFooterView(layout_btn_more);
			}
		}else {
			if (listview.getFooterViewsCount()>0) {
				listview.removeFooterView(layout_btn_more);
			}
		}
	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("投资协议申请");
		getBackgroundLayout().setLeftButtonText("返回");
		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
	}
	/**
	 * 监听事件，返回
	 */
	private OnClickListener backClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		ViewHolder holder=null;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.product_query_and_buy_agreement_listview_item, viewGroup,false);
			holder.tv_1=(TextView) convertView.findViewById(R.id.tv_1);
			holder.tv_2=(TextView) convertView.findViewById(R.id.tv_2);
			holder.tv_3=(TextView) convertView.findViewById(R.id.tv_3);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tv_1.setText(currentItem.get(BocInvestControl.AGRNAME).toString());
		holder.tv_2.setText(currentItem.get(BocInvestControl.PRONAM).toString());
		holder.tv_3.setText(BocInvestControl.map_agrType_code_key.get(currentItem.get(BocInvestControl.AGRTYPE).toString()));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, holder.tv_1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, holder.tv_2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, holder.tv_3);
		return convertView;
	}
	class ViewHolder{
		TextView tv_1;
		TextView tv_2;
		TextView tv_3;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

}
