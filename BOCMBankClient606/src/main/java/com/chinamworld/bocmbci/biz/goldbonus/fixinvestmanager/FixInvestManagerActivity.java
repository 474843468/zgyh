package com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.mode.IActionCall;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.userwidget.QueryListItemDetailView;
import com.chinamworld.bocmbci.userwidget.QueryView;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.DictionaryDataAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积利金定投管理的主界面
 * @author linyl
 *
 */
public class FixInvestManagerActivity extends GoldBonusBaseActivity implements 
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
	private QueryView queryView ;
	private RelativeLayout rl_tranhistory;
	private Spinner spTrfStatus;
	private TextView spTrfStatusTv;
	private ListView queryLv;
	private CommonAdapter<Map<String,Object>> adapter;
	private List<Map<String,Object>> queryList;
	private int recordNumber;
	/** 查询接口上送数据项  */
	private Map<String,Object> questParamMap = new HashMap<String,Object>();
	/** 当前页数 */
	private int currentIndex = 0;
	private DictionaryDataAdapter traStatusAdapter;
	/**默认查询标示  0 默认，1 非默认**/
	//	private String queryInitFlag = "0";
	/**列表 排序图标**/
	private ImageView fixinvestTimeIma;
	/**排序变量  交易时间、数量**/
	private OrdTypeFlag fixinvestTimeFlag;
	private String ordTypeForMore = "1";
	String linkAccFlag;
	LinearLayout ll_wenxin;
	List<Map<String,Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_fixinvestmanager);
		getBackgroundLayout().setRightButtonNewText(null);
		getBackgroundLayout().setLeftButtonNewClickListener(backClickListener);
		getBackgroundLayout().setPaddingWithParent(0, 0, 0, 0);
		setLeftSelectedPosition("goldbonusManager_3");//定投管理
		/**签约标示关联判断**/
		linkAccFlag = (String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery
				.get(GoldBonus.LINKACCTFLAG);
		if("2".equals(linkAccFlag)){//已签约未关联
			Intent intent = new Intent(this, GoldbounsReminderActivity.class);
			intent.putExtra("title", "定投管理");
			startActivity(intent);
			return;
		}
		GoldBonusTask task = GoldBonusTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				requestSystemDateTime();
				BaseHttpEngine.showProgressDialogCanGoBack();
			}

		},null);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		init();
	}

	private void init() {
		View view = LayoutInflater.from(this).inflate(R.layout.goldbonus_fixinvest_mian, null);
		this.getBackgroundLayout().addView(view);
		queryView =(QueryView) view.findViewById(R.id.queryControlView);
		rl_tranhistory = (RelativeLayout) view.findViewById(R.id.rl_tranhistory);
		spTrfStatus = (Spinner) view.findViewById(R.id.sp_goldbonus_trfStatus);
		spTrfStatusTv = (TextView) view.findViewById(R.id.textview_fixstatus_value);
		traStatusAdapter = new DictionaryDataAdapter(this, spTrfStatus, DictionaryData.FixStatusList);
		queryLv = (ListView) view.findViewById(R.id.goldbonus_tradequery_list);
		fixinvestTimeIma = (ImageView) view.findViewById(R.id.fixinvesttime_img);
		ll_wenxin = (LinearLayout) view.findViewById(R.id.wenxintv);
		queryView.setFenggeTextView("或选择起止日期查询");
		queryView.setBtnQueryBackgrouondResource(R.drawable.llbt_btn_corner_red);
		queryView.setQueryDateTvColor(getResources().getColor(R.color.fonts_dark_gray));
		queryView.setRadioButtonStyle(getResources().getColorStateList(R.color.color_button_text_new),
				R.drawable.new_button_left_sel,
				R.drawable.new_button_mid_sel,
				R.drawable.new_button_right_sel);
		fixinvestTimeIma.setOnClickListener(this);
		fixinvestTimeFlag = OrdTypeFlag.DOWN;
		//		queryView.getQueryBtn().performClick();
		queryView.initControl(dateTime, queryLv, new IActionCall(){
			@Override
			public void callBack() {
				//				queryInitFlag = "1";
				rl_tranhistory.setVisibility(View.GONE);
				requestPsnGoldBonusFixInvestListQuery(0,"1",true);
			}
		}); 

		queryView.DateTimeSimpleCheck = new IFunc<Boolean>(){

			@Override
			public Boolean callBack(Object param) {
				String startTime = queryView.getStartTime();
				String endTime = queryView.getEndTime();
				String systemTime = dateTime;
				/**允许两年内数据   可查6个月**/
				return QueryDateUtils.commQueryStartAndEndDateReg(FixInvestManagerActivity.this, startTime, endTime, systemTime, 2, 6);
			}

		};

		adapter = new CommonAdapter<Map<String,Object>>(this, queryLv, queryList, this);
		adapter.setTotalNumber(recordNumber);
		adapter.setRequestMoreDataListener(new IFunc<Boolean>() {

			@Override
			public Boolean callBack(Object param) {
				//				queryInitFlag = "1";
				currentIndex++;
				requestPsnGoldBonusFixInvestListQuery(currentIndex*10,ordTypeForMore,false);
				return true;
			}
		});

		//列表项点击事件
		queryLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoldbonusLocalData.getInstance().FixInvestListDetailQueryMap = adapter.getItem(position);
				Intent intentTemp = new Intent(FixInvestManagerActivity.this, FixInvestQueryDetailActivity.class);
				//				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.GOLDBONUS_FIXINVSTDETAIL_MAP,queryList.get(position));//存储选择项ItemMap数据
				startActivity(intentTemp);
//				startActivityForResult(intentTemp, 10001);
			}
		});
		/**默认查询**/
		//		requestPsnGoldBonusFixInvestListQuery(0,"1",true);
	}

	/**
	 * 
	 * 贵金属积利定投计划列表查询 接口请求   包括更多查询
	 * @param currentIndex  当前页索引
	 * @param ordType  排序
	 * @param _refresh  刷新标示
	 */
	private void requestPsnGoldBonusFixInvestListQuery(int currentIndex,String ordType, final boolean _refresh) {
		String conversionId = null;
		if(_refresh){//更多查询  _refresh = false
			conversionId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
		spTrfStatusTv.setText((String)spTrfStatus.getSelectedItem());
//		questParamMap.put("fixId", 
//				GoldbonusLocalData.getInstance().FixInvestSignMap == null ? "" : GoldbonusLocalData.getInstance().FixInvestSignMap.get("fixId"));
		questParamMap.put("fixId", "");//默认上送""
		questParamMap.put("startDate",queryView.getStartTime());
		questParamMap.put("endDate", queryView.getEndTime());
		//		questParamMap.put("fixStatus", "0".equals(queryInitFlag) ? "" : traStatusAdapter.getCurSelectedItem().getValue());//默认传空 查询全部
		questParamMap.put("fixStatus", traStatusAdapter.getCurSelectedItem().getValue());//默认传空 查询全部
		questParamMap.put("ordType", ordType);//1 降序  2 升序  空 不排序
		questParamMap.put("pageSize", "10");
		questParamMap.put("currentIndex", Integer.toString(currentIndex));
		questParamMap.put("_refresh", Boolean.toString(_refresh));
		/**温馨提示  避免接口报错时展示温馨提示**/
		if(StringUtil.isNullOrEmpty(list)){
			ll_wenxin.setVisibility(View.VISIBLE);
		}
		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusFixInvestListQuery", questParamMap, conversionId,new IHttpResponseCallBack<Map<String,Object>>(){

			@SuppressWarnings("unchecked")
			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					rl_tranhistory.setVisibility(View.GONE);
					ll_wenxin.setVisibility(View.VISIBLE);
					return;
				}
				list = (List<Map<String,Object>>)(result.get("list"));
				if (list == null || list.size() == 0) {
					rl_tranhistory.setVisibility(View.GONE);
					ll_wenxin.setVisibility(View.VISIBLE);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							FixInvestManagerActivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				} else{
					rl_tranhistory.setVisibility(View.VISIBLE);
					ll_wenxin.setVisibility(View.GONE);
					if(_refresh) {
						adapter.setSourceList(list, Integer.parseInt( (String)result.get("recordNumber")));
						//					if("1".equals(queryInitFlag)){
						queryView.scaleQueryControl();
						//					}
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
			view = new QueryListItemDetailView(FixInvestManagerActivity.this);
			convertView = view;
		}else{
			view = (QueryListItemDetailView) convertView;
		}
		view.setImageVisibility(View.VISIBLE);
		String dateTime = String.valueOf(currentItem.get("crtDate"));
		String date = dateTime.substring(0, dateTime.lastIndexOf("/")+3);
		view.setText(StringUtil.isNullChange(date),
				StringUtil.isNullChange(String.valueOf(currentItem.get("issueName"))),
				StringUtil.isNullChange(DictionaryData.getKeyByValue(String.valueOf(currentItem.get("fixStatus")), DictionaryData.FixStatusList)),
				"3:2:2");
		//		if (convertView == null) {
		//			convertView = inflater.inflate(
		//					R.layout.bocinvt_hispro_list_item, null);
		//		}
		//		/** 定投设置时间 */
		//		TextView tv_left = (TextView) convertView
		//				.findViewById(R.id.boci_product_name);
		//		/** 定投品种 */
		//		TextView tv_middle = (TextView) convertView
		//				.findViewById(R.id.boci_yearlyRR);
		//		/** 定投状态 */
		//		TextView tv_right = (TextView) convertView
		//				.findViewById(R.id.boci_timeLimit);
		////		boci_timeLimit.setTextColor(getResources()
		////				.getColor(R.color.red));
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(FixInvestManagerActivity.this,tv_left);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(FixInvestManagerActivity.this,tv_middle);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(FixInvestManagerActivity.this,tv_right);
		//		/** 右三角 */
		//		ImageView goDetail = (ImageView) convertView
		//				.findViewById(R.id.boci_gotoDetail);
		//		goDetail.setVisibility(View.VISIBLE);
		//		//赋值操作
		//		tv_left.setText(String.valueOf(currentItem
		//				.get("crtDate")));
		//		//TODO...反显字典信息待确认
		//		tv_middle.setText(String.valueOf(currentItem
		//				.get("tradeType")));
		//		tv_right.setText(String.valueOf(currentItem
		//				.get("fixStatus")));
		return convertView;
	}
	/**
	 * 列表排序 图标点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fixinvesttime_img:
			currentIndex = 0;
			if(fixinvestTimeFlag == OrdTypeFlag.DOWN){
				fixinvestTimeFlag = OrdTypeFlag.UP;
				fixinvestTimeIma.setImageResource(R.drawable.bocinvt_sort_up);
				requestPsnGoldBonusFixInvestListQuery(currentIndex,"2",true);
				ordTypeForMore = "2";
			}else if(fixinvestTimeFlag == OrdTypeFlag.UP || fixinvestTimeFlag == OrdTypeFlag.UN){
				fixinvestTimeFlag = OrdTypeFlag.DOWN;
				fixinvestTimeIma.setImageResource(R.drawable.bocinvt_sort_down);
				requestPsnGoldBonusFixInvestListQuery(currentIndex,"1",true);
				ordTypeForMore = "1";
			}
			break;

		}
	}

	/**返回键处理事件**/
	OnClickListener backClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ActivityTaskManager.getInstance().removeAllSecondActivity();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 10001:
			requestPsnGoldBonusFixInvestListQuery(0,"1",true);
			break;
		default:
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
				if ("PsnGoldBonusFixInvestListQuery"
						.equals(biiResponseBody.getMethod())) {
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {//不屏蔽后台错误，且显示查询前页面的温馨提示
						ll_wenxin.setVisibility(View.VISIBLE);
					}
				}
			}
		}
		return super.doBiihttpRequestCallBackPre(response);
	}

}
