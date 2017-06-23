package com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.userwidget.QueryListItemDetailView;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定投 执行明细页面
 * @author linyl
 */
public class ExecutionDetailsActivity extends GoldBonusBaseActivity implements
ICommonAdapter<Map<String,Object>>, OnClickListener {
	/**排序标量**/
	enum OrdTypeFlag{
		/**升序**/
		UP,
		/**降序**/
		DOWN,
		/**不排序**/
		UN,
	}
	/**执行明细结果列表**/
	private ListView execDetailLv;
	/**采用通用适配器接口实现**/
	private CommonAdapter<Map<String, Object>> adapter;
	/**接口返回数据源**/
	private List<Map<String,Object>> execdetailList;
	private int recordNumber = 0; 
	/**定投执行明细列表查询接口上送数据 **/
	private Map<String,Object> reqParamMap = new HashMap<String, Object>();
	/** 当前页数 */
	private int currentIndex = 0;
	/**列表 排序图标**/
	private ImageView fixinvestDateIma;
	/**排序变量  交易时间、数量**/
	private OrdTypeFlag fixinvestDateFlag;
	private String ordTypeForMore = "1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_fixinvestmanager);
		//		getBackgroundLayout().setRightButtonText("主界面");
		setContentView(R.layout.goldbonus_fixinvest_execdetail);
		execDetailLv = (ListView) findViewById(R.id.goldbonus_execdetail_list);
		fixinvestDateIma = (ImageView) findViewById(R.id.fixinvestdate_img);
		fixinvestDateIma.setOnClickListener(this);
		fixinvestDateFlag = OrdTypeFlag.DOWN;
		//		adapter = new CommonAdapter<Map<String,Object>>(this,execdetailList, this);
		//		execDetailLv.setAdapter(adapter);
		adapter = new CommonAdapter<Map<String,Object>>(this, execDetailLv, execdetailList, this);
		adapter.setTotalNumber(recordNumber);
		adapter.setRequestMoreDataListener(new IFunc<Boolean>(){

			@Override
			public Boolean callBack(Object param) {
				currentIndex++;
				requestPsnGoldBonusFixInvestDetailQry(currentIndex*10,ordTypeForMore,false);
				return true;
			}

		});
		//列表项点击事件
		execDetailLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoldbonusLocalData.getInstance().FixInvestDetailQryMap = adapter.getItem(position);
				ActivityIntentTools.intentToActivity(ExecutionDetailsActivity.this, ExecutionDetailInfoActivity.class);
			}
		});
		requestCommConversationId();
		BaseHttpEngine.showProgressDialogCanGoBack();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnGoldBonusFixInvestDetailQry(0,"1",true);
	}


	/**
	 * 贵金属积利定投计划执行情况查询  接口请求   包括更多
	 * @param currentInex  当前页索引
	 * @param isRefresh  是否刷新
	 */
	private void requestPsnGoldBonusFixInvestDetailQry(int currentInex,String ordType, final boolean isRefresh) {
		reqParamMap.put("fixId", 
				GoldbonusLocalData.getInstance().FixInvestListDetailQueryMap.get("fixId"));
		reqParamMap.put("ordType", ordType);
		reqParamMap.put("pageSize", "10");
		reqParamMap.put("currentIndex", Integer.toString(currentInex));
		reqParamMap.put("_refresh", Boolean.toString(isRefresh));

		String conversationId = isRefresh ?  null : (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);

		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusFixInvestDetailQry", reqParamMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@SuppressWarnings("unchecked")
			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				List<Map<String,Object>> list = (List<Map<String,Object>>)(result.get("list"));
				if (list == null || list.size() == 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							ExecutionDetailsActivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				} else{
					if(isRefresh) {
						adapter.setSourceList(list, Integer.parseInt( (String)result.get("recordNumber")));
					}else{
						adapter.addSourceList(list);
					}
				}
			}
		});
	}

	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		QueryListItemDetailView view;
		if(convertView == null){
			view = new QueryListItemDetailView(ExecutionDetailsActivity.this);
			convertView = view;
		}else{
			view = (QueryListItemDetailView) convertView;
		}
		view.setImageVisibility(View.VISIBLE);
		view.setText(StringUtil.isNullChange(String.valueOf(currentItem.get("fixPayDate"))), 
				StringUtil.isNullChange(paseEndZero(String.valueOf(currentItem.get("tranPrice")))), 
				StringUtil.isNullChange(DictionaryData.getKeyByValue(String.valueOf(currentItem.get("fixStatus")),DictionaryData.fixStatusResList)), 
				"2:3:1");
//		if (convertView == null) {
//			convertView = inflater.inflate(
//					R.layout.query_list_item_detail_layout, null);
//		}
//		/** 定投执行日期 */
//		TextView tv_left = (TextView) convertView
//				.findViewById(R.id.text1);
//		/** 成交牌价（人民币/克） */
//		TextView tv_middle = (TextView) convertView
//				.findViewById(R.id.text2);
//		/** 执行结果 */
//		TextView tv_right = (TextView) convertView
//				.findViewById(R.id.text3);
//		//		boci_timeLimit.setTextColor(getResources()
//		//				.getColor(R.color.red));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(ExecutionDetailsActivity.this,tv_left);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(ExecutionDetailsActivity.this,tv_middle);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(ExecutionDetailsActivity.this,tv_right);
//		/** 右三角 */
//		ImageView goDetail = (ImageView) convertView
//				.findViewById(R.id.gotoDetailImage);
//		goDetail.setVisibility(View.VISIBLE);
//		//赋值操作  
//		tv_left.setText(String.valueOf(currentItem
//				.get("fixPayDate")));
//		tv_middle.setText(StringUtil.parseStringPattern2(String.valueOf(currentItem
//				.get("tranPrice")),2));
//		tv_right.setText(DictionaryData.getKeyByValue(String.valueOf(currentItem
//				.get("fixStatus")),DictionaryData.fixStatusResList));
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fixinvestdate_img:
			currentIndex = 0;
			if(fixinvestDateFlag == OrdTypeFlag.DOWN){
				fixinvestDateFlag = OrdTypeFlag.UP;
				fixinvestDateIma.setImageResource(R.drawable.bocinvt_sort_up);
				requestPsnGoldBonusFixInvestDetailQry(currentIndex, "2", true);
				ordTypeForMore = "2";
			}else if(fixinvestDateFlag == OrdTypeFlag.UP){
				fixinvestDateFlag = OrdTypeFlag.DOWN;
				fixinvestDateIma.setImageResource(R.drawable.bocinvt_sort_down);
				requestPsnGoldBonusFixInvestDetailQry(currentIndex, "1", true);
				ordTypeForMore = "1";
			}
			break;
		}

	}

}
