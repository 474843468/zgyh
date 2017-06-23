package com.chinamworld.bocmbci.biz.goldbonus.timedepositmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.GoldbounsReminderActivity;
import com.chinamworld.bocmbci.biz.investTask.GoldBonusTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.userwidget.QueryListItemDetailView;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.CardWelcomGuideUtil;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定存管理主界面
 * @author linyl
 *
 */
public class TimeDepositManagerActivity extends GoldBonusBaseActivity implements ICommonAdapter<Map<String, Object>>, OnClickListener{

	/**排序标量**/
	enum OrdTypeFlag{
		/**升序**/
		UP,
		/**降序**/
		DOWN,
		/**不排序**/
		UN,
	}

	private View view;
	private ListView queryLv1,queryLv2;
	private CommonAdapter<Map<String,Object>> adapter1,adapter2;
	private List<Map<String,Object>> queryList1,queryList2;
	private int recordNumber1 = 0;
	private int recordNumber2 = 0;
	private int currentIndex1 = 0;
	private int currentIndex2 = 0;
	private int checkBtn = 0;

	/**产品查询上送数据**/
	private Map<String,Object> reqInfoQueryMap = new HashMap<String, Object>();
	/**账户查询上送数据**/
	private Map<String,Object> reqAccQueryMap = new HashMap<String, Object>();
	/**列表 排序图标**/
	private ImageView timeRateIma,startDateIma,expDateIma;
	/**排序变量  年利率、起息日、到期日**/
	private OrdTypeFlag TimeRateFlag,startDateFlag,expDateFlag;
	private String ordTypeForMore = "1";
	String linkAccFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_timedepositmanager);
		getBackgroundLayout().setRightButtonNewText(null);
		//		getBackgroundLayout().setPaddingWithParent(0, 0, 0, 0);
		setLeftSelectedPosition("goldbonusManager_4");//定存管理
		/**签约标示关联判断**/
		linkAccFlag = (String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery
				.get(GoldBonus.LINKACCTFLAG);
		if("2".equals(linkAccFlag)){//已签约未关联
			Intent intent = new Intent(this, GoldbounsReminderActivity.class);
			intent.putExtra("title", "定存管理");
			startActivity(intent);
			return;
		}
		GoldBonusTask task = GoldBonusTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				requestCommConversationId();
			}

		},null);

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		initView();
		requestPsnGoldBonusProductInfoQuery("1",0,true);
	}

	/**
	 * 贵金属积利产品信息查询  接口请求  定期   包括更多查询
	 * @param ordType  排序
	 * @param currentIndex  当前页
	 * @param isRefresh  是否刷新
	 */
	private void requestPsnGoldBonusProductInfoQuery(String ordType, int currentIndex,final boolean isRefresh) {
		reqInfoQueryMap.put("xpadgPeriodType", "1");//固定定期
		reqInfoQueryMap.put("ordType", ordType);//1 利率降序  ， 2利率升序
		reqInfoQueryMap.put("currentIndex", Integer.toString(currentIndex));
		reqInfoQueryMap.put("pageSize", "10");
		reqInfoQueryMap.put("_refresh", Boolean.toString(isRefresh));
		String conversationId = isRefresh ? (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID) : null ;
		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusProductInfoQuery", reqInfoQueryMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@SuppressWarnings("unchecked")
			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				//				GoldbonusLocalData.getInstance().ProductInfoQueryList = (List<Map<String,Object>>)(result.get("list"));//存储返回数据
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				List<Map<String,Object>> list = (List<Map<String,Object>>)(result.get("list"));	
				if (list == null || list.size() == 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							TimeDepositManagerActivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				} else{
					if(isRefresh) {
						adapter1.setSourceList(list, Integer.parseInt( (String)result.get("recordNumber")));
					}
					else 
						adapter1.addSourceList(list);
				}
			}
		});
	}

	/**
	 * 初始化定存管理主界面 
	 */
	private void initView() {
		view = LayoutInflater.from(this).inflate(R.layout.goldbonus_timedeposit_main, null);
		this.getBackgroundLayout().addView(view);
		((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(chechOncliclk);
		timeRateIma = (ImageView) view.findViewById(R.id.time_rate_img);
		startDateIma = (ImageView) view.findViewById(R.id.startDate_img);
		expDateIma = (ImageView) view.findViewById(R.id.expDate_img);
		TimeRateFlag = OrdTypeFlag.DOWN;
		startDateFlag = OrdTypeFlag.DOWN;
		expDateFlag = OrdTypeFlag.UN;
		timeRateIma.setOnClickListener(this);
		startDateIma.setOnClickListener(this);
		expDateIma.setOnClickListener(this);
		queryLv1 = (ListView) view.findViewById(R.id.goldbonus_tradequery_list_one);
		queryLv2 = (ListView) view.findViewById(R.id.goldbonus_tradequery_list_two);
		adapter1 = new CommonAdapter<Map<String,Object>>(this, queryLv1, queryList1, this);
		adapter2 = new CommonAdapter<Map<String,Object>>(this, queryLv2, queryList2, this);
		adapter1.setTotalNumber(recordNumber1);
		adapter2.setTotalNumber(recordNumber2);
		adapter1.setRequestMoreDataListener(new IFunc<Boolean>(){
			@Override
			public Boolean callBack(Object param) {
				currentIndex1++;
				requestPsnGoldBonusProductInfoQuery(ordTypeForMore,currentIndex1*10,false);
				return true;
			}

		});
		queryLv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoldbonusLocalData.getInstance().ProductInfoQueryQueryMap = adapter1.getItem(position); 
				ActivityIntentTools.intentToActivity(TimeDepositManagerActivity.this, DepositProductActivity.class);
			}
		});

		adapter2.setRequestMoreDataListener(new IFunc<Boolean>(){
			@Override
			public Boolean callBack(Object param) {
				currentIndex2++;
				requestPsnGoldBonusAccountQuery(ordTypeForMore,currentIndex2*10,false);
				return true;
			}

		});
		//列表项点击事件
		queryLv2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoldbonusLocalData.getInstance().AccountQueryMap = adapter2.getItem(position); 
				ActivityIntentTools.intentToActivity(TimeDepositManagerActivity.this, TimePositionActivity.class);
			}
		});

		view.postDelayed(new Runnable() {
			@Override
			public void run() {


				/**获取lisview 在屏幕中的我位置**/
				int[] location = new int[2];
		queryLv1.getLocationOnScreen(location);
//							queryLv1.getLocationInWindow(location);
				// 显示帮助指南（前3次显示）
				CardWelcomGuideUtil.showGoldBonusPositionGuid(BaseDroidApp.getInstanse().getCurrentAct(),location[1]);

			}
		},20);

	}


	private OnCheckedChangeListener chechOncliclk = new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.btn1://定期产品
				checkBtn = 1;
				findViewById(R.id.fixmanager_product).setVisibility(View.VISIBLE);
				findViewById(R.id.fixmanager_chicang).setVisibility(View.GONE);
				requestPsnGoldBonusProductInfoQuery("1",0,true);
				break;
			case R.id.btn2://定期客户持仓信息
				checkBtn = 2;
				findViewById(R.id.fixmanager_product).setVisibility(View.GONE);
				findViewById(R.id.fixmanager_chicang).setVisibility(View.VISIBLE);
				requestPsnGoldBonusAccountQuery("1",0,true);
				break;

			}
		}
	};

	/**
	 * 贵金属积利账户查询
	 * @param ordType  排序方式
	 * @param currnetIndex 当前页
	 * @param isRefresh  是否刷新
	 */
	private void requestPsnGoldBonusAccountQuery(String ordType, int currnetIndex,
			final boolean isRefresh) {
		reqAccQueryMap.put("ordType", ordType);//1 起降 2起升  3到降  4到升
		reqAccQueryMap.put("currentIndex", Integer.toString(currnetIndex));
		reqAccQueryMap.put("pageSize", "10");
		reqAccQueryMap.put("_refresh", Boolean.toString(isRefresh));
		String conversationId = isRefresh ? (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID) : null;

		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusAccountQuery", reqAccQueryMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

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
							TimeDepositManagerActivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				} else{
					if(isRefresh) {
						adapter2.setSourceList(list, Integer.parseInt( (String)result.get("recordNumber")));
					}
					else 
						adapter2.addSourceList(list);
				}
			}
		});
	}


	@Override
	public View getView(int arg0, final Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		QueryListItemDetailView view;
		if(currentItem == null){
			return null;
		}
		if(convertView == null){
			view = new QueryListItemDetailView(TimeDepositManagerActivity.this);
			convertView = view;
		}else{
			view = (QueryListItemDetailView) convertView;
		}
		view.setImageVisibility(View.VISIBLE);
		if(checkBtn == 1 || checkBtn == 0){
			view.setText(StringUtil.isNullChange(String.valueOf(currentItem.get("issueName"))),
					paseEndZero(String.valueOf(currentItem
							.get("issueRate"))) + "%", 
							null, 
					"2:1:0");
		}else if(checkBtn == 2){
			view.setText(StringUtil.isNullChange(String.valueOf(currentItem.get("issueName"))), 
					StringUtil.isNullChange(String.valueOf(currentItem.get("tradeDate"))),
					StringUtil.isNullChange(String.valueOf(currentItem.get("expDate"))),
					"1:1:1");
		}
		/********************************************/	
		//		if (convertView == null) {
		//			convertView = inflater.inflate(
		//					R.layout.bocinvt_hispro_list_item, null);
		//		}
		//		/** 期限/品种 */
		//		TextView tv_left = (TextView) convertView
		//				.findViewById(R.id.boci_product_name);
		//		/** 年化利率 /开始日期 */
		//		TextView tv_middle = (TextView) convertView
		//				.findViewById(R.id.boci_yearlyRR);
		//		/** 操作/期限 */
		//		TextView tv_right = (TextView) convertView
		//				.findViewById(R.id.boci_timeLimit);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(TimeDepositManagerActivity.this,tv_left);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(TimeDepositManagerActivity.this,tv_middle);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(TimeDepositManagerActivity.this,tv_right);
		//		
		//		//赋值操作
		//		if(checkBtn == 1 || checkBtn == 0){
		//			/** 右三角 */
		//			ImageView goDetail = (ImageView) convertView
		//					.findViewById(R.id.boci_gotoDetail);
		//			goDetail.setVisibility(View.GONE);
		//			tv_left.setText(String.valueOf(currentItem
		//					.get("issueName")));
		//			tv_middle.setText(String.valueOf(currentItem
		//					.get("issueRate")) + "%");
		//			tv_right.setText("将活期转为定期");
		//			tv_right.setTextColor(getResources()
		//				.getColor(R.color.blue));
		//			tv_right.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		//			tv_right.setOnClickListener(new View.OnClickListener() {
		//				
		//				@Override
		//				public void onClick(View v) {
		//					GoldbonusLocalData.getInstance().ProductInfoQueryQueryMap = currentItem; 
		//					ActivityIntentTools.intentToActivity(TimeDepositManagerActivity.this, DepositProductActivity.class);
		//				}
		//			});
		//		}else if(checkBtn == 2){
		//			/** 右三角 */
		//			ImageView goDetail = (ImageView) convertView
		//					.findViewById(R.id.boci_gotoDetail);
		//			goDetail.setVisibility(View.VISIBLE);
		//			tv_left.setText(String.valueOf(currentItem
		//					.get("issueName")));
		//			tv_middle.setText(String.valueOf(currentItem
		//					.get("tradeDate")));
		//			tv_right.setText(String.valueOf(currentItem
		//					.get("expDate")));
		//		}

		/***************************************************/
		//		TextView tv_left,tv_middle,tv_right;
		//		if(checkBtn == 1 || checkBtn == 0){
		//			if (convertView == null) {
		//				convertView = inflater.inflate(
		//						R.layout.bocinvt_hispro_list_item, null);
		//			}
		//			/** 期限/品种 */
		//			tv_left = (TextView) convertView
		//					.findViewById(R.id.boci_product_name);
		//			/** 年化利率 /开始日期 */
		//			tv_middle = (TextView) convertView
		//					.findViewById(R.id.boci_yearlyRR);
		//			/** 操作/期限 */
		//			tv_right = (TextView) convertView
		//					.findViewById(R.id.boci_timeLimit);
		//			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tv_left);
		//			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tv_middle);
		//			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tv_right);
		//			ImageView goDetail = (ImageView) convertView
		//					.findViewById(R.id.boci_gotoDetail);
		//			goDetail.setVisibility(View.GONE);
		//			tv_left.setText(String.valueOf(currentItem
		//					.get("issueName")));
		//			tv_middle.setText(StringUtil.append2Decimals(String.valueOf(currentItem
		//					.get("issueRate")),2) + "%");
		//			tv_right.setText("将活期转为定期");
		//			tv_right.setTextColor(getResources()
		//				.getColor(R.color.blue));
		//			tv_right.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		//			tv_right.setOnClickListener(new View.OnClickListener() {
		//				
		//				@Override
		//				public void onClick(View v) {
		//					GoldbonusLocalData.getInstance().ProductInfoQueryQueryMap = currentItem; 
		//					ActivityIntentTools.intentToActivity(TimeDepositManagerActivity.this, DepositProductActivity.class);
		//				}
		//			});
		//		}else if(checkBtn == 2){
		//			QueryListItemDetailView view;
		//			if (convertView == null) {
		//				view = new QueryListItemDetailView(this);
		//				convertView = view;
		//			} else {
		//				view = (QueryListItemDetailView) convertView;
		//			}
		//			view.setImageVisibility(View.VISIBLE);
		//			view.setText(StringUtil.isNullChange(String.valueOf(currentItem.get("issueName"))), 
		//					StringUtil.isNullChange(String.valueOf(currentItem.get("tradeDate"))),
		//					StringUtil.isNullChange(String.valueOf(currentItem.get("expDate"))),
		//					"1:1:1");
		//		}

		return convertView;
	}
	/**
	 * 排序图标点击事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time_rate_img://年利率排序
			if(TimeRateFlag == OrdTypeFlag.DOWN){
				TimeRateFlag = OrdTypeFlag.UP;
				timeRateIma.setImageResource(R.drawable.bocinvt_sort_up);
				requestPsnGoldBonusProductInfoQuery("2",0,true);
				ordTypeForMore = "2";
			}else if(TimeRateFlag == OrdTypeFlag.UP){
				TimeRateFlag = OrdTypeFlag.DOWN;
				timeRateIma.setImageResource(R.drawable.bocinvt_sort_down);
				requestPsnGoldBonusProductInfoQuery("1",0,true);
				ordTypeForMore = "1";
			}
			break;
		case R.id.startDate_img://起息日
			expDateFlag = OrdTypeFlag.UN;
			expDateIma.setImageResource(R.drawable.bocinvt_sort_un);
			if(startDateFlag == OrdTypeFlag.UN || startDateFlag == OrdTypeFlag.UP){
				startDateFlag = OrdTypeFlag.DOWN;
				startDateIma.setImageResource(R.drawable.bocinvt_sort_down);
				requestPsnGoldBonusAccountQuery("1",0,true);
				ordTypeForMore = "1";
			}else if(startDateFlag == OrdTypeFlag.DOWN){
				startDateFlag = OrdTypeFlag.UP;
				startDateIma.setImageResource(R.drawable.bocinvt_sort_up);
				requestPsnGoldBonusAccountQuery("2",0,true);
				ordTypeForMore = "2";
			}
			break;
		case R.id.expDate_img://到期日
			startDateFlag = OrdTypeFlag.UN;
			startDateIma.setImageResource(R.drawable.bocinvt_sort_un);
			if(expDateFlag == OrdTypeFlag.UN || expDateFlag == OrdTypeFlag.UP){
				expDateFlag = OrdTypeFlag.DOWN;
				expDateIma.setImageResource(R.drawable.bocinvt_sort_down);
				requestPsnGoldBonusAccountQuery("3",0,true);
				ordTypeForMore = "3";
			}else if(expDateFlag == OrdTypeFlag.DOWN){
				expDateFlag = OrdTypeFlag.UP;
				expDateIma.setImageResource(R.drawable.bocinvt_sort_up);
				requestPsnGoldBonusAccountQuery("4",0,true);
				ordTypeForMore = "4";
			}
			break;
		}

	}

}
