package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.investTask.BOCDoThreeTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
/**
 * 中银理财 投资协议管理主界面
 * @author linyl
 *
 */
public class InvestInvalidAgreeQueryActivity extends BocInvtBaseActivity 
				implements OnItemClickListener, OnItemSelectedListener, OnClickListener,ICommonAdapter<Map<String,Object>> {
	public static final String TAG = "InvestInvalidAgreeQueryActivity";
	/**协议类型选中项内容**/
	private Spinner mSpAgrType_success,mSpAgrType_fail;
	/**协议列表**/
	private ListView mAgrQueryLv;
	/**适配器**/
	private CommonAdapter<Map<String, Object>> adapter;
	/**协议类型选中项内容**/
	private String mSelectSpAgrType;
	/** 协议状态 **/
	private String mAgrState;
	/** 失效协议查询 服务器返回对象 **/
	private Map<String,Object> capacityQueryMap;
	/** 协议查询 服务器返回数据列表 **/
	private List<Map<String,Object>> capacityQueryList;
	/** 失效协议查询 列表更多按钮 服务器返回对象 **/
	private Map<String,Object> capacityQueryForMoreMap;
	/** 失效协议查询 列表更多按钮 服务器返回数据列表 **/
	private List<Map<String,Object>> capacityQueryForMoreList;
	/** 存储 协议列表项被点击项的 数据对象  **/
	private Map<String,Object> itemMap;
	/** 存储 客户协议详情接口 的上送字段值 **/
	private String agrTypeReq,custAgrCodeReq;
	/** 存储服务器返回 协议详情信息的Map **/
	private Map<String,Object> agrInfoQueryMap;
	/** 失效协议 查询 返回数据列表 记录条数 **/
	private int recordNumber = 0;
	private int loadNumber = 0;
	/**适配器数据源**/
	List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
	/**列表更多项布局**/
	private RelativeLayout load_more;
	/** 协议类型布局 **/
	private RelativeLayout mAgrTypeSuccess_rl,mAgrTypeFail_rl;
	/**更多按钮**/
	private Button mBtnLoadmore;
	/**顶部请查看按钮布局**/
	private RelativeLayout queryTitle_rl;
	/** 请查看按钮 **/
	private Button mBtn_query;
	/** 更多按钮点击标示 **/
	private boolean mBtnLoadmoreIsClick = false;
//	/**单例实例**/
//	public static InvestInvalidAgreeQueryActivity mInvestInvalidAgreeQueryActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		getBackgroundLayout().setTitleText(R.string.boc_invest_agrquery_title_two);
		getBackgroundLayout().setRightButtonText(null);
		getBackgroundLayout().setPaddingWithParent(0, 0, 0, 0);
		getBackgroundLayout().setLeftButtonClickListener(mBackClickListener);
		setContentView(R.layout.boci_investinvalidagree);
		setLeftSelectedPosition("bocinvtManager_4");
		
		BOCDoThreeTask task = BOCDoThreeTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialogCanGoBack();
			}
		},2);
		
	}
	/**
	 * 重写父类方法   请求conversationId 接口回调
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		init();
	}
	
	/**
	 * 获取单例
	 */
//	public static InvestInvalidAgreeQueryActivity getInstance(){
//		if(mInvestInvalidAgreeQueryActivity == null){
//			mInvestInvalidAgreeQueryActivity = new InvestInvalidAgreeQueryActivity();
//		}
//		return mInvestInvalidAgreeQueryActivity;
//	}

	/**
	 * 初始化View
	 */
	private void init() {
		mAgrTypeSuccess_rl = (RelativeLayout) findViewById(R.id.rl_agrtype_success);
		mSpAgrType_success = (Spinner) findViewById(R.id.invest_agree_sp_agrtype_success);
		mAgrQueryLv = (ListView) findViewById(R.id.boci_agrquery_list);
		queryTitle_rl = (RelativeLayout) findViewById(R.id.queryagr_btntitle);
		mAgrState = LocalData.bocInvestAgrState.get("正常");
		mSelectSpAgrType = mSpAgrType_success.getSelectedItem().toString();
		mBtn_query = (Button) queryTitle_rl.findViewById(R.id.queryagr_btntitle_btn);
		mBtn_query.setOnClickListener(this);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		mBtnLoadmore = (Button) load_more.findViewById(R.id.btn_load_more);
		mBtnLoadmore.setBackgroundResource(R.color.transparent_00);
		//更多按钮的点击事件
//		mBtnLoadmore.setOnClickListener(this);
		mSpAgrType_success.setOnItemSelectedListener(this);
		setAgrTypePosition();
		adapter = new CommonAdapter<Map<String,Object>>(this, mAgrQueryLv, capacityQueryList, this);
		adapter.setTotalNumber(recordNumber);
		adapter.setRequestMoreDataListener(new IFunc<Boolean>(){

			@Override
			public Boolean callBack(Object param) {
				mBtnLoadmoreIsClick = true;
				if(mAgrTypeSuccess_rl.isShown()){
					requestPsnXpadCapacityQuery(mSpAgrType_success.getSelectedItemPosition(),String.valueOf(loadNumber),ConstantGloble.REFRESH_FOR_MORE);
				}else if(mAgrTypeFail_rl.isShown()){
					requestPsnXpadCapacityQuery(mSpAgrType_fail.getSelectedItemPosition(),String.valueOf(loadNumber),ConstantGloble.REFRESH_FOR_MORE);
				}
				return true;
			}
	    });
		mAgrQueryLv.setOnItemClickListener(this);
	}
	/**
	 * 设置协议类型spinner选择项索引
	 */
	public void setAgrTypePosition(){
		if(mAgrTypeSuccess_rl.isShown()){
			requestPsnXpadCapacityQuery(mSpAgrType_success.getSelectedItemPosition(),ConstantGloble.LOAN_CURRENTINDEX_VALUE,ConstantGloble.LOAN_REFRESH_FALSE);
		}else if(mAgrTypeFail_rl.isShown()){
			requestPsnXpadCapacityQuery(mSpAgrType_fail.getSelectedItemPosition(),ConstantGloble.LOAN_CURRENTINDEX_VALUE,ConstantGloble.LOAN_REFRESH_FALSE);
		}
	}

	/**
	 * 请求客户投资协议 查询接口（包括更多查询）
	 * @param agrTypePosition 协议类型 Spinner选中项的position
	 * @param currentIndex 当前页索引
	 * @param refresh  是否重新查询
	 */
	public void requestPsnXpadCapacityQuery(int agrTypePosition,String currentIndex,String refresh){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADCAPACITYQUERY_API);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		//TODO...开启智能投资协议类型
		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_REQ,String.valueOf(agrTypePosition));
//		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_REQ,agrTypePosition == 0 ? String.valueOf(agrTypePosition) : String.valueOf(agrTypePosition+1));
		
		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_AGRSTATE_REQ, mAgrState);
		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_PAGESIZE_REQ, ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_CURRENTINDEX_REQ, currentIndex);
		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_REFRESH_REQ, refresh);
		biiRequestBody.setParams(paramsMap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadCapacityQueryCallBack");
	}
	/**
	 * 请求客户投资协议 查询接口 回调   （包括更多查询）
	 * @param resultObj  返回数据 
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadCapacityQueryCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		if(!mBtnLoadmoreIsClick){//查询
			capacityQueryMap = (Map<String, Object>) biiResponseBody.getResult();
			//判断返回对象是否为空
			if(StringUtil.isNullOrEmpty(capacityQueryMap)) return;
			recordNumber = Integer.valueOf((String)capacityQueryMap
					.get(BocInvt.BOCINVT_CAPACITYQUERY_RECORDNUMBER_RES));
			capacityQueryList = (List<Map<String, Object>>) capacityQueryMap
					.get(BocInvt.BOCINVT_CAPACITYQUERY_LIST_RES);	
			// 判断返回数据列表是否为空或者列表长度为0
			if(capacityQueryList.size() == 0 || capacityQueryList == null ){
				BaseDroidApp.getInstanse().showInfoMessageDialog(InvestInvalidAgreeQueryActivity.this
						.getString(R.string.acc_transferquery_null));
				return;
			}
//			if(recordNumber >10){
//				mAgrQueryLv.addFooterView(load_more);
//			}
			loadNumber = capacityQueryList.size();
//			adapter = new MyInvestAgreeQueryAdapter(this, capacityQueryList);
//			mAgrQueryLv.setAdapter(adapter);	
//			mAgrQueryLv.setOnItemClickListener(this);
			adapter.setSourceList(capacityQueryList, recordNumber);
		}else{//更多
			capacityQueryForMoreMap = (Map<String, Object>) biiResponseBody.getResult();
			if(StringUtil.isNullOrEmpty(capacityQueryForMoreMap)) return ;
			recordNumber = Integer.valueOf((String)capacityQueryForMoreMap
					.get(BocInvt.BOCINVT_CAPACITYQUERY_RECORDNUMBER_RES));
			capacityQueryForMoreList = (List<Map<String, Object>>) capacityQueryForMoreMap
					.get(BocInvt.BOCINVT_CAPACITYQUERY_LIST_RES);
			if(capacityQueryForMoreList == null || capacityQueryForMoreList.size() == 0){
				BaseDroidApp.getInstanse().showInfoMessageDialog(InvestInvalidAgreeQueryActivity.this
						.getString(R.string.acc_transferquery_null));
				return;
			}
			for(Map<String,Object> map : capacityQueryForMoreList){
//				loadNumber++;
				capacityQueryList.add(map);
			}
//			if(loadNumber < recordNumber){
//
//			}else{
//				mAgrQueryLv.removeFooterView(load_more);
//			}
			loadNumber = capacityQueryList.size();
			adapter.notifyDataSetChanged();
			mBtnLoadmoreIsClick = false;
		}
	}

//	/**请求客户投资协议 更多查询接口**/
//	public void requestPsnXpadCapacityQueryForMore(int agrTypePosition){
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADCAPACITYQUERY_API);
//		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		Map<String,Object> paramsMap = new HashMap<String, Object>();
//		//		if(mAgrTypeSuccess_rl.isShown()){
//		//			paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_REQ, mSpAgrType_success.getSelectedItemPosition());
//		//		}else if(mAgrTypeFail_rl.isShown()){
//		//			paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_REQ, mSpAgrType_fail.getSelectedItemPosition());
//		//		}
//		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_REQ,agrTypePosition);
//		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_AGRSTATE_REQ, mAgrState);
//		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_PAGESIZE_REQ, ConstantGloble.LOAN_PAGESIZE_VALUE);
//		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_CURRENTINDEX_REQ, String.valueOf(loadNumber));
//		paramsMap.put(BocInvt.BOCINVT_CAPACITYQUERY_REFRESH_REQ, ConstantGloble.REFRESH_FOR_MORE);
//		biiRequestBody.setParams(paramsMap);
//		BiiHttpEngine.showProgressDialog();
//		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadCapacityQueryForMoreCallBack");
//	}
//	/**
//	 * 请求客户投资协议 更多查询 接口 回调
//	 * @param resultObj  服务端返回数据
//	 */
//	@SuppressWarnings("unchecked")
//	public void requestPsnXpadCapacityQueryForMoreCallBack(Object resultObj){
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> responseList = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = responseList.get(0);
//		BiiHttpEngine.dissMissProgressDialog();
//		capacityQueryForMoreMap = (Map<String, Object>) biiResponseBody.getResult();
//		if(StringUtil.isNullOrEmpty(capacityQueryForMoreMap)) return ;
//		recordNumber = Integer.valueOf((String)capacityQueryForMoreMap
//				.get(BocInvt.BOCINVT_CAPACITYQUERY_RECORDNUMBER_RES));
//		capacityQueryForMoreList = (List<Map<String, Object>>) capacityQueryForMoreMap
//				.get(BocInvt.BOCINVT_CAPACITYQUERY_LIST_RES);
//		if(capacityQueryForMoreList == null || capacityQueryForMoreList.size() == 0){
//			BaseDroidApp.getInstanse().showInfoMessageDialog(InvestInvalidAgreeQueryActivity.this
//					.getString(R.string.acc_transferquery_null));
//			return;
//		}
//		for(Map<String,Object> map : capacityQueryForMoreList){
//			loadNumber++;
//			capacityQueryList.add(map);
//		}
//		if(loadNumber < recordNumber){
//
//		}else{
//			mAgrQueryLv.removeFooterView(load_more);
//		}
//		adapter.notifyDataSetChanged();
//	}

	@Override
	/**
	 * 查询协议列表的点击事件  调用客户协议详情接口
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//保存列表项 被点击项数据（服务器返回）
//		if(!StringUtil.isNullOrEmpty(itemMap)){
//			itemMap.clear();
//		}
		itemMap = capacityQueryList.get(position);
		//存储列表项数据信息
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP, itemMap);
		agrTypeReq = (String) itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_RES);
		custAgrCodeReq = (String) itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_CUSTAGECODE_RES);
		//交易状态status 为0 时 列表项不可点击状态
		String status = String.valueOf(itemMap.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
		if(status.equals("0")){//委托代处理状态不可进详情
			return;
		}
		requestPsnXpadAgreementInfoQuery(custAgrCodeReq,agrTypeReq);
	}

	/**
	 * 客户投资协议详情 请求接口
	 */
	public void requestPsnXpadAgreementInfoQuery(String custAgrCode, String agrType){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADAGREEMENTINFOQUERY_API);
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(BocInvt.BOCINVT_AGRINFOQUERY_CUSTAGRCODE_REQ, custAgrCode);
		paramsMap.put(BocInvt.BOCINVT_AGRINFOQUERY_AGRTYPE_REQ, agrType);
		biiRequestBody.setParams(paramsMap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadAgreementInfoQueryCallBack");
	}
	/**
	 * 客户投资协议详情 请求接口 回调 
	 * @param resultObj  服务端返回数据 
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadAgreementInfoQueryCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		agrInfoQueryMap = (Map<String, Object>) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(agrInfoQueryMap)) return;
		BaseDroidApp.getInstanse().getBizDataMap()
		.put(ConstantGloble.BOCINVT_AGREEMENTINFOQUERY_MAP, agrInfoQueryMap);
		Intent intent = null;
		if(Integer.valueOf(mAgrState) == 1){//正常
			intent = new Intent(InvestInvalidAgreeQueryActivity.this, PeriodAgreeDetailNewActivity.class);
//			intent.putExtra("AgrState", "1");
		}else if(Integer.valueOf(mAgrState) == 2){//失效
			intent = new Intent(InvestInvalidAgreeQueryActivity.this, InvestInvalidAgreeQueryDetailAvtivity.class);
//			intent.putExtra("AgrState", "2");
		}
		startActivity(intent);
	}
	/**
	 * 配置协议类型对应 调用协议查询接口
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		listClear();
		setAgrTypePosition();
	}

	/**
	 * 清空列表项
	 */
	public void listClear(){
		if(!StringUtil.isNullOrEmpty(capacityQueryList)){
			capacityQueryList.clear();
		}
//		if(mAgrQueryLv.getFooterViewsCount() > 0){
//			mAgrQueryLv.removeFooterView(load_more);
//		}
		adapter.setSourceList(null, 0);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/**
	 * 返回按钮点击事件
	 */
	OnClickListener mBackClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			backClick();
		}
	};

	/**
	 * 返回键的事件处理
	 */
	public void backClick() {
		if(queryTitle_rl.isShown()){
//			finish();
//			return;
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent = new Intent(InvestInvalidAgreeQueryActivity.this,SecondMainActivity.class);
//			startActivity(intent);
			goToMainActivity();
			return;
		}
		queryTitle_rl.setVisibility(View.VISIBLE);
		mAgrState = LocalData.bocInvestAgrState.get("正常");
		getBackgroundLayout().setRightButtonText(null);
		mAgrTypeSuccess_rl.setVisibility(View.VISIBLE);
		mAgrTypeFail_rl.setVisibility(View.GONE);
		mSelectSpAgrType = mSpAgrType_success.getSelectedItem().toString();
		listClear();
		setAgrTypePosition();
	}

	@Override
	public void onBackPressed() {
		backClick();
	}

	@Override
	/**
	 * 按钮点击事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.queryagr_btntitle_btn://请查看
			getBackgroundLayout().setRightButtonText("主界面");
			queryTitle_rl.setVisibility(View.GONE);
			mAgrTypeSuccess_rl.setVisibility(View.GONE);
			mAgrTypeFail_rl = (RelativeLayout) findViewById(R.id.rl_agrtype_fail);
			mSpAgrType_fail = (Spinner) findViewById(R.id.invest_agree_sp_agrtype_fail);
			mAgrTypeFail_rl.setVisibility(View.VISIBLE);
			mAgrState = LocalData.bocInvestAgrState.get("失效");
//			mSelectSpAgrType = mSpAgrType_fail.getSelectedItem().toString();
			mSelectSpAgrType = mSpAgrType_fail.getItemAtPosition(0).toString();//默认选择全部
			listClear();
			mSpAgrType_fail.setSelection(0);//默认选择全部
			setAgrTypePosition();
			mSpAgrType_fail.setOnItemSelectedListener(this);
			break;
		case R.id.btn_load_more://更多
			mBtnLoadmoreIsClick = true;
			if(mAgrTypeSuccess_rl.isShown()){
				requestPsnXpadCapacityQuery(mSpAgrType_success.getSelectedItemPosition(),String.valueOf(loadNumber),ConstantGloble.REFRESH_FOR_MORE);
			}else if(mAgrTypeFail_rl.isShown()){
				requestPsnXpadCapacityQuery(mSpAgrType_fail.getSelectedItemPosition(),String.valueOf(loadNumber),ConstantGloble.REFRESH_FOR_MORE);
			}
			break;
		}
	}
	
	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		if(convertView == null){
			convertView = inflater.inflate(
					R.layout.boc_invest_agrquery_list_item, null);
		}
		TextView mAgrName = (TextView) convertView.findViewById(R.id.boc_invest_agrname_tv);
		TextView mExecPro = (TextView) convertView.findViewById(R.id.boc_invest_execpro_tv);
		TextView mAgrType = (TextView) convertView.findViewById(R.id.boc_invest_agrtype_tv);
		ImageView mGotoDetail = (ImageView) convertView.findViewById(R.id.boc_invest_gotoDetail);
		//添加文本的点击事件
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mAgrName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mExecPro);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mAgrType);
		//取值
		mAgrName.setText(String.valueOf(currentItem.get(BocInvt.BOCINVT_CAPACITYQUERY_AGRNAE_RES)));
		mExecPro.setText(String.valueOf(currentItem.get(BocInvt.BOCINVT_CAPACITYQUERY_PRONAME_RES)));
		mAgrType.setText(LocalData.bocInvestAgrTypeRes.get(String.valueOf(currentItem.get(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_RES))));
		mGotoDetail.setVisibility(View.VISIBLE);
		return convertView;
	}

}
