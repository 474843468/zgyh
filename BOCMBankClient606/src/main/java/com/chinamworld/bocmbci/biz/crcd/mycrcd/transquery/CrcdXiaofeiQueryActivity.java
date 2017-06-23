package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.acc.adapter.AccTransGalleryAdapter;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdXiaofeiAdapter;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.CrcdTransDividedActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;

/**
 * 消费分期查询----条件、结果查询页面
 * 
 * @author huangyuchao
 * 
 */

public class CrcdXiaofeiQueryActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdXiaofeiQueryActivity";
	private View view;
	/** 查询结果list */
	private ListView lv_query_result;

	CrcdXiaofeiAdapter cxaAdapter;

	private int isShow = 1;
	/** 消费分期查询结果 */
	public List<Map<String, Object>> crcdTransInfo;
	/** 币种 */
	private Spinner et_loandate;
	/** 下拉列表框-币种list */
	List<String> strList = new ArrayList<String>();
	ArrayAdapter<String> typeAdapter;
	/** 查询条件顶部卡片 */
	private CustomGallery viewpager;
	private int list_item;
	/** 查询结果页面---账号 */
	private TextView tv_acc_info_currency_value;
	/** 查询结果页面---币种 */
	private TextView tv_beedtypevalue;
	/** 用户选择的当前卡片的账号 */
	private String bankNumber;

	RelativeLayout rl_query_transfer;
	/** 查询结果页面整个布局 */
//	private RelativeLayout rl_query_transfer_result;

	ImageView img_acc_query_back;
	/** 查询按钮 */
	private Button btnxiaofeiHistoryQuery;

	private LinearLayout ll_upLayout;
	/** 查询结果页面--顶部区域 */
	private LinearLayout acc_query_result_condition,acc_query_search_condition;

	RelativeLayout load_more;
	// 当前页面
	private int currentIndex = 0;
	// 每页显示的条数
	private int pageSize = 10;
	int maxNum;
	/** 左箭头 */
	private ImageView acc_frame_left;
	/** 右箭头 */
	private ImageView acc_btn_goitem;
	// --------------------------------
	/** 查询条件全部布局 */
//	private View searchCoditionView = null;
	/** 查询条件背景图片布局 */
	private View searchBackgroundView = null;
	/** 更多按钮 */
	private Button btn_load_more = null;
	/** 记录总数 */
	private int recordNumber = 0;
	/** 是否显示更多 */
	private boolean isShowLoadMore = true;
	private String accountId;
	private boolean isfirst = true;
	/** 初始化的时候显示PopupWindows */
	private boolean isFirstShow = true;
	/** 点击查询前的数据 */
	private Map<String, Object> beforeCurrentMap = null;
	private int currentPosition = -1;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "OnCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_sub_divide));
		if (view == null) {
			view = addView(R.layout.crcd_xiaofei_querytrans);
		}
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		bankSetupList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCD_BANKXIAOFEILIST);
		if (StringUtil.isNullOrEmpty(bankSetupList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		crcdTransInfo = new ArrayList<Map<String, Object>>();
		beforeCurrentMap = new HashMap<String, Object>();
		initConditionView();
		// 为卡片赋值
		initConditionDate();
		slipListener();
		initResultView();
		// 默认查询---start
		//BaseHttpEngine.showProgressDialogCanGoBack();
		//requestCommConversationId();
		// 默认查询---end

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		psnCrcdDividedPayConsumeQry(String.valueOf(isShowLoadMore));
	}

	/** 为顶部可滑动卡片赋值 */
	private void initConditionDate() {
		list_item = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		// 刚进入时，当前账户为选择的账户
		currentBankList = bankSetupList.get(list_item);
		beforeCurrentMap = bankSetupList.get(list_item);
		bankNumber = (String) currentBankList.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = (String) currentBankList.get(Crcd.CRCD_ACCOUNTID_RES);
		AccTransGalleryAdapter adapter = new AccTransGalleryAdapter(this, bankSetupList);
		viewpager.setAdapter(adapter);
		viewpager.setSelection(list_item);
		if (list_item == 0) {
			acc_frame_left.setVisibility(View.INVISIBLE);
			if (bankSetupList.size() == 1) {
				acc_btn_goitem.setVisibility(View.INVISIBLE);
			} else {
				acc_btn_goitem.setVisibility(View.VISIBLE);
			}

		} else if (list_item == bankSetupList.size() - 1) {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.INVISIBLE);
		} else {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.VISIBLE);
		}
	}

	/** 初始化查询条件页面 */
	private void initConditionView() {
		acc_query_search_condition=(LinearLayout) view.findViewById(R.id.acc_query_search_condition);
	
//		searchCoditionView = LayoutInflater.from(this).inflate(R.layout.crcd_xiaofei_querytrans_condition, null);
		rl_query_transfer = (RelativeLayout) view.findViewById(R.id.acc_query_transfer_layout);
		ll_upLayout = (LinearLayout) view.findViewById(R.id.ll_upLayout);
		// 币种下拉列表
		et_loandate = (Spinner) view.findViewById(R.id.et_loandate);
		strList.add(strCurrency);
		typeAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, strList);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		et_loandate.setAdapter(typeAdapter);
		et_loandate.setSelection(0);

		searchBackgroundView = view.findViewById(R.id.ll_query_condition);

		acc_frame_left = (ImageView) view.findViewById(R.id.acc_frame_left);
		acc_btn_goitem = (ImageView) view.findViewById(R.id.acc_btn_goitem);
		// 可滑动卡片
		viewpager = (CustomGallery) view.findViewById(R.id.viewPager);

		// 收起事件监听
		ll_upLayout.setOnClickListener(upQueryClick);
		// 查询按钮
		btnxiaofeiHistoryQuery = (Button) view.findViewById(R.id.btnxiaofeiHistoryQuery);
		// 显示查询条件
//		acc_query_search_condition.addView(view);
//		/PopupWindowUtils.getInstance().getQueryPopupWindow(searchCoditionView, this);

		btnxiaofeiHistoryQuery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				searchBackgroundView.setBackgroundResource(R.drawable.img_bg_query_j);
				isFirstShow = false;
				currentIndex = 0;
				beforeCurrentMap = bankSetupList.get(currentPosition);
				bankNumber = (String) beforeCurrentMap.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				accountId = (String) beforeCurrentMap.get(Crcd.CRCD_ACCOUNTID_RES);
				getTopDate(bankNumber);
				if (crcdTransInfo != null && !crcdTransInfo.isEmpty()) {
					crcdTransInfo.clear();
				}
				if (cxaAdapter != null) {
					cxaAdapter.dataChanged(new ArrayList<Map<String, Object>>());
				}
				if (lv_query_result.getFooterViewsCount() > 0) {
					lv_query_result.removeFooterView(load_more);
				}

				isShowLoadMore = true;
				// 查询消费账单
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
				// psnCrcdDividedPayConsumeQry(String.valueOf(isShowLoadMore));
			}
		});
	}

	/** 卡片滑动监听事件 */
	private void slipListener() {
		if (isfirst) {
			isfirst = false;
			viewpager.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
					currentPosition = position;
					if (position == 0) {
						acc_frame_left.setVisibility(View.INVISIBLE);
						if (bankSetupList.size() == 1) {
							acc_btn_goitem.setVisibility(View.INVISIBLE);
						} else {
							acc_btn_goitem.setVisibility(View.VISIBLE);
						}
					} else if (position == bankSetupList.size() - 1) {
						acc_frame_left.setVisibility(View.VISIBLE);
						acc_btn_goitem.setVisibility(View.INVISIBLE);
					} else {
						acc_frame_left.setVisibility(View.VISIBLE);
						acc_btn_goitem.setVisibility(View.VISIBLE);
					}
					currentBankList = bankSetupList.get(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

		}

	}

	/** 查询结果页面 */
	private void initResultView() {
//		rl_query_transfer_result = (RelativeLayout) view.findViewById(R.id.acc_query_result_layout);
		acc_query_result_condition = (LinearLayout) view.findViewById(R.id.acc_query_result_condition);
		lv_query_result = (ListView) view.findViewById(R.id.lv_query_result);
		tv_acc_info_currency_value = (TextView) view.findViewById(R.id.tv_acc_info_currency_value);
		tv_beedtypevalue = (TextView) view.findViewById(R.id.tv_beedtypevalue);
		// 下拉箭头
		img_acc_query_back = (ImageView) view.findViewById(R.id.img_acc_query_back);
		// 下拉事件
		acc_query_result_condition.setOnClickListener(downQueryClick);
		// 显示更多
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(goMoreClickListener);
	}

	/** 更多按钮事件 */
	OnClickListener goMoreClickListener = new OnClickListener() {
		public void onClick(View v) {
			currentIndex += pageSize;
			isShowLoadMore = false;
			LogGloble.d(TAG + " currentIndex", currentIndex + "");
			// 查询消费账单
			BaseHttpEngine.showProgressDialog();
			psnCrcdDividedPayConsumeQry(String.valueOf(isShowLoadMore));
		};
	};

	/** 收起事件----隐藏查询条件 */
	OnClickListener upQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (crcdTransInfo == null || crcdTransInfo.size() <= 0) {

			} else {
//				/PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				acc_query_search_condition.setVisibility(View.GONE);
				
//				rl_query_transfer_result.setVisibility(View.VISIBLE);
				acc_query_result_condition.setVisibility(View.VISIBLE);
			}
		}
	};

	/** 下拉事件-----显示查询条件 */
	OnClickListener downQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// /PopupWindowUtils.getInstance().getQueryPopupWindow(searchCoditionView,
			// CrcdXiaofeiQueryActivity.this);
//			/PopupWindowUtils.getInstance().showQueryPopupWindow();
			acc_query_result_condition.setVisibility(View.GONE);
			acc_query_search_condition.setVisibility(View.VISIBLE);
			
			
		}
	};

	/** 消费账单查询 */
	private void psnCrcdDividedPayConsumeQry(String refresh) {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDDIVIDEDPAYCONSUMEQRY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, refresh);
		// 币种
		String uploadcurrency = LocalData.queryCurrencyMap.get(et_loandate.getSelectedItem().toString());

		params.put(Crcd.CRCD_CURRENCYCODE, uploadcurrency);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdDividedPayConsumeQryCallBack");
	}

	/** 消费账单查询-----回调 */
	public void psnCrcdDividedPayConsumeQryCallBack(Object object) {
		BaseHttpEngine.dissMissProgressDialog();
		isFirstShow = false;
		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.forex_no_list));
			return;
		}
		recordNumber = Integer.valueOf(String.valueOf(returnList.get(Crcd.CRCD_RECORDNUMBER_REQ)));
		LogGloble.d(TAG + " recordNumber", recordNumber + "");
		if (!returnList.containsKey(Crcd.CRCD_SUPPLYLIIST)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.forex_no_list));
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) returnList.get(Crcd.CRCD_SUPPLYLIIST);
		if (StringUtil.isNullOrEmpty(lists)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.forex_no_list));
			return;
		} else {
			crcdTransInfo.addAll(lists);
//			/PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			acc_query_search_condition.setVisibility(View.GONE);
//			rl_query_transfer_result.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.VISIBLE);
			searchBackgroundView.setBackgroundResource(R.drawable.img_bg_query_no);
		}

		if (recordNumber > pageSize && isShowLoadMore) {
			lv_query_result.addFooterView(load_more);
		}
		if (!isShowLoadMore) {
			if (currentIndex + pageSize >= recordNumber) {
				lv_query_result.removeFooterView(load_more);
			}
		}
		// 为顶部赋值
		getTopDate(bankNumber);
		if (isShowLoadMore) {
			cxaAdapter = new CrcdXiaofeiAdapter(this, crcdTransInfo, isShow);
			lv_query_result.setAdapter(cxaAdapter);
		} else {
			// 数据往后加载
			cxaAdapter.dataChanged(crcdTransInfo);
		}
		cxaAdapter.setOncrcdTransDivideListener(onCrcdTransDivideListener);

//		/PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		acc_query_search_condition.setVisibility(View.GONE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
	}

	/** 查询结果顶部赋值 */
	private void getTopDate(String accountNumber) {
		tv_acc_info_currency_value.setText(StringUtil.getForSixForString(accountNumber));
		tv_beedtypevalue.setText(et_loandate.getSelectedItem().toString());
	}

	/** 点击分期按钮 */
	protected OnItemClickListener onCrcdTransDivideListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCE_XIAOFEI_RESULT, crcdTransInfo);
			Intent it = new Intent(CrcdXiaofeiQueryActivity.this, CrcdTransDividedActivity.class);
			it.putExtra("position", position);
			it.putExtra("currency", currency);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, bankNumber);
			currentBankList = beforeCurrentMap;
			startActivity(it);
		}
	};
    /**当activity绘制结束后获得焦点*/
//	public void onWindowFocusChanged(boolean hasFocus) {
//		if (isFirstShow && bankSetupList != null && bankSetupList.size() > 0) {
////			/PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
//			acc_query_search_condition.setVisibility(View.VISIBLE);
//			
//		}
//		isFirstShow = false;
//	};

}
