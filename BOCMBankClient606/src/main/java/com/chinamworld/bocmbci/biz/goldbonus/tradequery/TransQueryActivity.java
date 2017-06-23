package com.chinamworld.bocmbci.biz.goldbonus.tradequery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.GoldbounsReminderActivity;
import com.chinamworld.bocmbci.biz.investTask.GoldBonusTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.mode.IActionCall;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.userwidget.QueryListItemDetailView;
import com.chinamworld.bocmbci.userwidget.QueryView;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.DictionaryDataAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积利金交易查询主界面
 * @author linyl
 *
 */
public class TransQueryActivity extends GoldBonusBaseActivity implements 
ICommonAdapter<Map<String, Object>>, OnClickListener {
	/**排序标量**/
	enum OrdTypeFlag{
		/**升序**/
		UP,
		/**降序**/
		DOWN,
		/**不排序**/
		UN,
	}

	private RelativeLayout rl_tranhistory;
	private TextView sp_trfType;

	/** 交易查询结果 */
	private List<Map<String, Object>> tranQueryList;
	/** 查询列表 */
	private ListView queryListView;
	/**采用通用适配器接口实现**/
	private CommonAdapter<Map<String, Object>> adapter;
	private int recordNumber = 0;

	/** 查询接口上送数据项  */
	private Map<String,Object> questParamMap = new HashMap<String,Object>();
	/** 当前页数 */
	private int currentIndex = 0;
	/**列表 排序图标**/
	private ImageView traTimeIma,traNumIma;
	/**排序变量  交易时间、数量**/
	private OrdTypeFlag TimeFlag,NumFlag;
	private String ordTypeForMore = "1";

	/** 结束时间 */
	public String endTime;
	String linkAccFlag;
	LinearLayout ll_wenxin;
	List<Map<String,Object>> queryList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		getBackgroundLayout().setTitleNewText(R.string.goldbonus_title_query);
		//		getBackgroundLayout().setRightButtonText("主界面");
		getBackgroundLayout().setRightButtonNewText(null);
		getBackgroundLayout().setPaddingWithParent(0, 0, 0, 0);
		setLeftSelectedPosition("goldbonusManager_5");//交易查询
		/**签约标示关联判断**/
		linkAccFlag = (String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery
				.get(GoldBonus.LINKACCTFLAG);
		if("2".equals(linkAccFlag)){//已签约未关联
			Intent intent = new Intent(TransQueryActivity.this, GoldbounsReminderActivity.class);
			intent.putExtra("title", "交易查询");
			startActivity(intent);
			return;
		}
		GoldBonusTask task = GoldBonusTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialogCanGoBack();
			}

		},null);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		init();
	}
	/**查询控件*/
	QueryView queryView;

	TextView tradestatusTextView,tradeTypeTextView;
	Context context;
//	DictionaryDataAdapter trfTypeAadapter;
	DictionaryDataAdapter trfStatusAadapter;
	List<String> list;
	int mIndex;
	/**
	 * 页面初始化
	 */
	private void init() {

		View view = LayoutInflater.from(this).inflate(R.layout.goldbonus_tranquery_mian, null);
		this.getBackgroundLayout().addView(view);
		queryView =(QueryView) view.findViewById(R.id.queryControlView);
		traTimeIma = (ImageView) view.findViewById(R.id.tratime_img);
		traNumIma = (ImageView) view.findViewById(R.id.tranum_img);
		ll_wenxin = (LinearLayout) view.findViewById(R.id.wenxintv);
		queryView.setFenggeTextView("或选择起止日期查询");
		queryView.setBtnQueryBackgrouondResource(R.drawable.llbt_btn_corner_red);
		queryView.setQueryDateTvColor(getResources().getColor(R.color.fonts_dark_gray));
		queryView.setRadioButtonStyle(getResources().getColorStateList(R.color.color_button_text_new),
				R.drawable.new_button_left_sel,
				R.drawable.new_button_mid_sel,
				R.drawable.new_button_right_sel);
		traTimeIma.setOnClickListener(this);
		traNumIma.setOnClickListener(this);
		TimeFlag = OrdTypeFlag.DOWN;
		NumFlag = OrdTypeFlag.UN;
		rl_tranhistory = (RelativeLayout) view.findViewById(R.id.rl_tranhistory);
		//		sp_trfStatus = (Spinner) view.findViewById(R.id.sp_goldbonus_trfStatus);
		sp_trfType = (TextView) view.findViewById(R.id.sp_goldbonus_trfType);
		//		tradestatusTextView= (TextView)view.findViewById(R.id.textview_tradestatus_value);
		list = new ArrayList<String>();
		list.add("买入活期贵金属积利");
		list.add("卖出活期贵金属积利");
		list.add("贵金属积利活期转为定期");
		list.add("贵金属积利定期转为活期");
		sp_trfType.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			ListView mListView = new ListView(TransQueryActivity.this);
			mListView.setFadingEdgeLength(0);
			mListView.setOnItemClickListener(acctItemOnClick);
			mListView.setScrollingCacheEnabled(false);
			AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(TransQueryActivity.this, list,mIndex);
			mListView.setAdapter(mAdapter);
			BaseDroidApp.getInstanse().showSpinnerDialog(mListView, "交易类型");
		}
	});
		
		tradeTypeTextView= (TextView)view.findViewById(R.id.trade_type_value);

//		trfTypeAadapter = new DictionaryDataAdapter(this,sp_trfType,GoldbonusLocalData.SaleTypeList);
		//		trfStatusAadapter = new DictionaryDataAdapter(this,sp_trfStatus,DictionaryData.TransStatusList);
		queryListView = (ListView) view.findViewById(R.id.goldbonus_tradequery_list);

		queryView.initControl(dateTime, queryListView, new IActionCall(){
			@Override
			public void callBack() {
				if("请选择".equals(sp_trfType.getText().toString())){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择交易类型"); return;
				}
				//				rl_tranhistory.setVisibility(View.GONE);
				requestPsnGoldBonusTransDetailQuery(0,"1",true);
			}
		}); 

		queryView.DateTimeSimpleCheck = new IFunc<Boolean>(){

			@Override
			public Boolean callBack(Object param) {
				String startTime = queryView.getStartTime();
				String endTime = queryView.getEndTime();
				String systemTime = dateTime;
				/**允许两年内数据   可查6个月**/
				return QueryDateUtils.commQueryStartAndEndDateReg(TransQueryActivity.this, startTime, endTime, systemTime, 2, 6);
			}

		};


		adapter = new CommonAdapter<Map<String,Object>>(this, queryListView,tranQueryList, this);
		adapter.setTotalNumber(recordNumber);
		adapter.setRequestMoreDataListener(new IFunc<Boolean>(){
			@Override
			public Boolean callBack(Object param) {
				currentIndex++;
				requestPsnGoldBonusTransDetailQuery(currentIndex*10,ordTypeForMore,false);
				return true;
			}

		});
		//列表项点击事件 
		queryListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoldbonusLocalData.getInstance().TradeQueryDetailMap = adapter.getItem(position);
				ActivityIntentTools.intentToActivity(TransQueryActivity.this, TransQueryDetailActivity.class);
			}
		});
	}
	
	private OnItemClickListener acctItemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mIndex = position;
			BaseDroidApp.getInstanse().dismissMessageDialog();
			sp_trfType.setText(list.get(position));
		}
	};
	
	/**
	 * 贵金属积利交易明细查询 接口请求  包括更多查询
	 * @param currentInex  当前页
	 * @param ordType  排序方式
	 * @param isRefresh  刷新标示
	 */
	private void requestPsnGoldBonusTransDetailQuery(int currentInex,String ordType, final boolean isRefresh) {
		//		tradestatusTextView.setText((String)sp_trfStatus.getSelectedItem());
		tradeTypeTextView.setText(sp_trfType.getText().toString());
		//		questParamMap.put("saleStatus", trfStatusAadapter.getCurSelectedItem().getValue());
//		questParamMap.put("saleType", trfTypeAadapter.getCurSelectedItem().getValue());
		questParamMap.put("saleType", String.valueOf(mIndex));
		questParamMap.put("ordType", ordType);//1 日期降  2日期升 3数量降  4数量升  空不排序
		questParamMap.put("startDate", queryView.getStartTime());
		questParamMap.put("endDate", queryView.getEndTime());
		questParamMap.put("fixId", "");//定投编号  非必送项
		questParamMap.put("pageSize", "10");
		questParamMap.put("currentIndex", Integer.toString(currentInex));
		questParamMap.put("_refresh", Boolean.toString(isRefresh));

		String conversationId = isRefresh ? (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID) : null;
		if(StringUtil.isNullOrEmpty(queryList)){
			ll_wenxin.setVisibility(View.VISIBLE);
		}
		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusTransDetailQuery", questParamMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@SuppressWarnings("unchecked")
			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					rl_tranhistory.setVisibility(View.GONE);
					ll_wenxin.setVisibility(View.VISIBLE);
					return;
				}
				queryList = (List<Map<String,Object>>)(result.get("list"));
				if (queryList == null || queryList.size() == 0) {
					rl_tranhistory.setVisibility(View.GONE);
					ll_wenxin.setVisibility(View.VISIBLE);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							TransQueryActivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				} else{
					rl_tranhistory.setVisibility(View.VISIBLE);
					ll_wenxin.setVisibility(View.GONE);
					if(isRefresh) {
						adapter.setSourceList(queryList, Integer.parseInt( (String)result.get("recordNumber")));
						queryView.scaleQueryControl();
					}
					else 
						adapter.addSourceList(queryList);
				}
			}

		});
	}

	/**适配器getview方法**/
	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		QueryListItemDetailView view;
		if(currentItem == null){
			return null;
		}
		if (convertView == null) {
			view = new QueryListItemDetailView(TransQueryActivity.this);
			convertView = view;
		} else {
			view = (QueryListItemDetailView) convertView;
		}

		view.setText(StringUtil.isNullChange((String)currentItem.get("tranDate")), 
				StringUtil.isNullChange(DictionaryData.getKeyByValue((String)currentItem.get("saleType"),
						GoldbonusLocalData.SaleTypeList)),
						StringUtil.isNullChange(StringUtil.parseStringPattern2(StringUtil.deleateNumber((String)currentItem.get("weight")),0)),
				"1:1:1");
		return convertView;
	}
	/**
	 * 排序图标点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tratime_img:
			currentIndex = 0;
			NumFlag = OrdTypeFlag.UN;
			traNumIma.setImageResource(R.drawable.bocinvt_sort_un);
			if(TimeFlag == OrdTypeFlag.UN || TimeFlag == OrdTypeFlag.UP){
				TimeFlag = OrdTypeFlag.DOWN;
				traTimeIma.setImageResource(R.drawable.bocinvt_sort_down);
				requestPsnGoldBonusTransDetailQuery(currentIndex,"1",true);
				ordTypeForMore = "1";
			}else if(TimeFlag == OrdTypeFlag.DOWN){
				TimeFlag = OrdTypeFlag.UP;
				traTimeIma.setImageResource(R.drawable.bocinvt_sort_up);
				requestPsnGoldBonusTransDetailQuery(currentIndex,"2",true);
				ordTypeForMore = "2";
			}
			break;
		case R.id.tranum_img:
			currentIndex = 0;
			TimeFlag = OrdTypeFlag.UN;
			traTimeIma.setImageResource(R.drawable.bocinvt_sort_un);
			if(NumFlag == OrdTypeFlag.UN || NumFlag == OrdTypeFlag.UP){
				NumFlag = OrdTypeFlag.DOWN;
				traNumIma.setImageResource(R.drawable.bocinvt_sort_down);
				requestPsnGoldBonusTransDetailQuery(currentIndex,"3",true);
				ordTypeForMore = "3";
			}else if(NumFlag == OrdTypeFlag.DOWN){
				NumFlag = OrdTypeFlag.UP;
				traNumIma.setImageResource(R.drawable.bocinvt_sort_up);
				requestPsnGoldBonusTransDetailQuery(currentIndex,"4",true);
				ordTypeForMore = "4";
			}
			break;
		}

	}
	
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		BaseHttpEngine.dissMissProgressDialog();
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {
				if ("PsnGoldBonusTransDetailQuery"
						.equals(biiResponseBody.getMethod())) {
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						ll_wenxin.setVisibility(View.VISIBLE);
					}
				}
			}
		}
		return super.doBiihttpRequestCallBackPre(response);
	}

}
