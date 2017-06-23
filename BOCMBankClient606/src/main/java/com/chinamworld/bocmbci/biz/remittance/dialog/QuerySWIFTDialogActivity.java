package com.chinamworld.bocmbci.biz.remittance.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.QuerySWIFTListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 查询SWIFT码和银行页面
 * 
 * @author Zhi
 */
public class QuerySWIFTDialogActivity extends BaseActivity {

	private RelativeLayout rl_bank;
	/** SWIFT码输入框 */
	private EditText etSwiftCode;
	/** 银行名称 */
	private EditText etBankName;
	/** 列表控件 */
	private ListView lvSWIFTInfo;
	/** SWIFT码信息列表 */
	private List<Map<String, Object>> listSWIFT;
	/** 列表适配器 */
	private QuerySWIFTListAdapter listAdapter;
	/** 查询按钮 */
	private Button btnQuery;
	/** 底部更多视图 */
	private View mFooterView;
	/** SWIFT码 */
	private String strSwiftCode;
	/** 银行名称 */
	private String strBankName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		rl_bank.removeAllViews();
		rl_bank.addView(initView());
		
	}
	
	/** 初始化视图 */
	private View initView() {
		View view = LayoutInflater.from(this).inflate(R.layout.remittance_info_input_query_swift_list, null);
		etSwiftCode = (EditText) view.findViewById(R.id.et_swiftCode);
		etBankName = (EditText) view.findViewById(R.id.et_bankname);
		lvSWIFTInfo = (ListView) view.findViewById(R.id.listview);
		btnQuery = (Button) view.findViewById(R.id.btn_query);
		EditTextUtils.setLengthMatcher(this, etSwiftCode, 11);
		mFooterView = LayoutInflater.from(this).inflate(R.layout.epay_tq_list_more, null);

		view.findViewById(R.id.img_exit_accdetail).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnQuery.setOnClickListener(queryListener);
		return view;
	}

	/**
	 * 添加分页布局
	 * 
	 * @param totalCount
	 *            后台的产品列表总长度
	 */
	private void addFooterView(String totalCount) {
		final int listSize = listSWIFT.size();
		if (Integer.valueOf(totalCount) > listSize) {
			if (lvSWIFTInfo.getFooterViewsCount() <= 0) {
				lvSWIFTInfo.addFooterView(mFooterView);
			}
		} else {
			if (lvSWIFTInfo.getFooterViewsCount() > 0) {
				lvSWIFTInfo.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 请求更多
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Remittance.SWIFTCODE, strSwiftCode);
				params.put(Remittance.BANKNAME, strBankName);
				params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
				params.put(Remittance.CURRENTINDEX, listSize);
				requestHttp(Remittance.PSNINTERNATIONALTRANSFERSWIFTQUERY, "requestPsnInternationalTransferSwiftQueryCallBack", params, false);
			}
		});
	}

	/** 查询按钮点击事件 */
	private OnClickListener queryListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			strSwiftCode = etSwiftCode.getText().toString().trim();
			strBankName = etBankName.getText().toString().trim();
			if (!StringUtil.isNull(strSwiftCode)) {
				if(!submitRegexpCode(true)){
					return;
				}
			}
			if (!StringUtil.isNull(strBankName)) {
				if(!submitRegexpName(true)){
					return;
				}
			}
			
			if (StringUtil.isNull(strSwiftCode) && StringUtil.isNull(strBankName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("SWIFT代码和银行名称不能同时为空");
				return;
			}
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Remittance.SWIFTCODE, strSwiftCode);
			params.put(Remittance.BANKNAME, strBankName);
			params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
			params.put(Remittance.CURRENTINDEX, "0");
			requestHttp(Remittance.PSNINTERNATIONALTRANSFERSWIFTQUERY, "requestPsnInternationalTransferSwiftQueryCallBack", params, false);
			if (!StringUtil.isNullOrEmpty(listSWIFT)) {
				listSWIFT.clear();
				listAdapter.setData(listSWIFT); 
				addFooterView("0");
			}
		}
	};
	
	/**
	 * 为请求网络提供统一的请求方法
	 * 
	 * @param requestMethod
	 *            要请求的接口
	 * @param responseMethod
	 *            请求成功后的回调方法
	 * @param params
	 *            参数列表，子类准备此参数
	 * @param needConversationId
	 *            是否需要ConversationId
	 */
	public void requestHttp(String requestMethod, String responseMethod, Map<String, Object> params, boolean needConversationId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(requestMethod);
		biiRequestBody.setParams(params);
		// 如果需要ConversationId
		if (needConversationId)
			biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, responseMethod);
	}

	/**
	 * 处理返回数据
	 * 
	 * @param result
	 * @return 网络返回数据
	 */
//	public static Object httpResponseDeal(Object result) {
//		BiiResponse biiResponse = (BiiResponse) result;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		return biiResponseBody.getResult();
//	}
	
	/** 查询SWIFT回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInternationalTransferSwiftQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get(Remittance.LIST);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.query_no_result));
			return;
		}
		if (StringUtil.isNullOrEmpty(listSWIFT)) {
			listSWIFT = resultList;
		} else {
			listSWIFT.addAll(resultList);
		}
		String recordNumber = (String) resultMap.get(Remittance.RECORDNUMBER);
		addFooterView(recordNumber);
		
		if (listAdapter == null) {
			listAdapter = new QuerySWIFTListAdapter(this, listSWIFT);
			listAdapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					RemittanceDataCenter.getInstance().setListPsnInternationalTransferSwiftQuery(listSWIFT);
					setResult(RemittanceContent.QUERY_SWIFT, new Intent().putExtra("SWIFT", arg2));
					finish();
				}
			});
			lvSWIFTInfo.setAdapter(listAdapter);
		} else {
			listAdapter.setData(listSWIFT);
		}
	}
	/** 输入数据判空或校验 */
	private boolean submitRegexpName(boolean required){
		ArrayList<RegexpBean> lists=new ArrayList<RegexpBean>();
		/** 银行名称 */
		if(onlyRegular(required, etBankName.getText().toString().trim())){
			RegexpBean etName=new RegexpBean(RemittanceContent.BANKNAME_CN, 
					etBankName.getText().toString().trim(), RemittanceContent.REMITTANCEQUERYBANKNAME);
			lists.add(etName);
		}
		if(!RegexpUtils.regexpDate(lists)){
			return false;
		}
		return true;
	}
	/** 输入数据判空或校验 */
	private boolean submitRegexpCode(boolean required){
		ArrayList<RegexpBean> lists=new ArrayList<RegexpBean>();
		/** SWIFT代码 */
		if(onlyRegular(required, etSwiftCode.getText().toString().trim())){
			RegexpBean etSwift=new RegexpBean(RemittanceContent.QUERYSWIFT_CN,
					etSwiftCode.getText().toString().trim(), RemittanceContent.REMITTANCEQUERYSWIFTCODE);
			lists.add(etSwift);
		}
		
		if(!RegexpUtils.regexpDate(lists)){
			return false;
		}
		return true;
	}
	/** 只作正则校验 */
	private boolean onlyRegular(boolean required,String content){
		if((!required && StringUtil.isNull(content))||required){
			return true;
		}
		return false;
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
