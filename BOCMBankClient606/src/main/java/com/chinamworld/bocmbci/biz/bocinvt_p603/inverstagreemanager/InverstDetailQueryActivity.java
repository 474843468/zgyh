package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
/**
 * 投资明细查询页面
 * 
 * @author niuchf
 * 
 */
public class InverstDetailQueryActivity extends BociBaseActivity 
					implements ICommonAdapter<Map<String,Object>> {
	public static final String TAG = "InverstDetailQueryActivity";
	/** 投资明细查询页 */
	private View view;
	/** 投资明细查询列表 */
	private ListView invtDetailListView;
//	private InverstDetailQueryAdapter invtDetailAdapter;
	/**采用通用的Adapter接口来实现数据适配功能**/
	private CommonAdapter<Map<String, Object>> invtDetailAdapter;
//	private List<Map<String, Object>> invtList = new ArrayList<Map<String,Object>>();
	private int index;
	//存储协议列表 数据Map
	private Map<String,Object> itemMap;
	//交易明细接口上送字段
	private String agrType,custAgrCode;
	private OnClickListener setRightButtonClickListener = null;
	private List<Map<String, Object>> mapList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_inverst_detail_query));
//		gonerightBtn();
		setText("主界面");
		setRightBtnClick(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (setRightButtonClickListener != null) {
					setRightButtonClickListener.onClick(v);
					return;
				}
				if (InverstDetailQueryActivity.this.getActivityTaskType() == ActivityTaskType.TwoTask) {
//					ActivityTaskManager.getInstance().removeAllSecondActivity();
//					Intent intent = new Intent();
//					intent.setClass(InverstDetailQueryActivity.this, SecondMainActivity.class);
//					InverstDetailQueryActivity.this.startActivity(intent);
					goToMainActivity();
				} else {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(InverstDetailQueryActivity.this, MainActivity.class);
//					InverstDetailQueryActivity.this.startActivity(intent);
					goToMainActivity();
				}
			}
		});
		// 添加布局
		view = addView(R.layout.bocinvt_query_inverst_pager);
		// 界面初始化
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		invtDetailListView = (ListView) view.findViewById(R.id.boc_query_inverst_result);
		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
//		invtList.addAll(list);
//		invtDetailAdapter.setInverstDetailQueryList(invtList);
		final String proCur = getIntent().getStringExtra("currency");
		mapList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_CAPACITYTRANSLIST_MAP);
//		invtDetailAdapter = new InverstDetailQueryAdapter(this, mapList);
		invtDetailAdapter = new CommonAdapter<Map<String,Object>>(InverstDetailQueryActivity.this, mapList, this);
//		invtDetailAdapter.setInverstDetailQueryList(mapList);
		invtDetailListView.setAdapter(invtDetailAdapter);
		invtDetailListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				BociDataCenter.getInstance().setInverstDetailMap(
						mapList.get(position));
				Intent intent = new Intent(InverstDetailQueryActivity.this, 
						InverstDetailQueryInfoActivity.class);
				intent.putExtra("currency", proCur);
				startActivity(intent);
			}
		});
//		BiiHttpEngine.showProgressDialog();
//		requestPsnXpadCapacityTransList();
//		invtDetailAdapter = new InverstDetailQueryAdapter(this, invtList);
//		invtDetailListView.setAdapter(invtDetailAdapter);
	}

	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.bocinvt_hisproduct_list_item, null);
			viewHolder.tv_date = (TextView) convertView
					.findViewById(R.id.boci_product_name);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.boci_yearlyRR);
			viewHolder.tv_status = (TextView) convertView
					.findViewById(R.id.boci_timeLimit);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(InverstDetailQueryActivity.this,
					viewHolder.tv_date);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(InverstDetailQueryActivity.this,
					viewHolder.tv_type);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(InverstDetailQueryActivity.this,
					viewHolder.tv_status);
			viewHolder.iv_go = (ImageView) convertView
					.findViewById(R.id.boci_gotoDetail);
			viewHolder.iv_go.setVisibility(View.VISIBLE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 赋值操作
		viewHolder.tv_date.setText(String.valueOf(currentItem
				.get(BocInvt.BOCINVT_TDSDATE_RES)));
		viewHolder.tv_type.setText(LocalData.inverstTradeTypeStr.get(String.valueOf(currentItem
				.get(BocInvt.BOCINVT_TDSTYPE_RES))));
		String state = (String) currentItem.get(
				BocInvt.BOCINVT_TDSSTATE_RES);
		viewHolder.tv_status.setText(LocalData.fundTradeStateStr.get(state));	
		return convertView;
	}
	
	private class ViewHolder {
		/** 交易日期 */
		public TextView tv_date;
		/** 交易类型 */
		public TextView tv_type;
		/** 状态 */
		public TextView tv_status;
		public ImageView iv_go;
	}

//	/** 请求投资明细 */
//	@SuppressWarnings("unchecked")
//	public void requestPsnXpadCapacityTransList() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.PSNXPADCAPACITYTRANALIST);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		Map<String, String> params = new HashMap<String, String>();
//		itemMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
//						.get(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP);
//		agrType = String.valueOf(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_RES));
//		custAgrCode = String.valueOf(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_CUSTAGECODE_RES));
//		params.put(BocInvt.BOCINVT_TRANALIST_CUSTAGRCODE_REQ, custAgrCode);
//		params.put(BocInvt.BOCINVT_TRANALIST_AGRTYPE_REQ, agrType);
//		biiRequestBody.setParams(params);
//		BiiHttpEngine.showProgressDialog();
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestPsnXpadCapacityTransListCallBack");
//	}
//	/**
//	 * 请求投资明细回调
//	 */
//	@SuppressWarnings("unchecked")
//	public void requestPsnXpadCapacityTransListCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
////		Map<String, Object> map = (Map<String, Object>) BocinvtUtils.httpResponseDeal(resultObj);
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		/**** 返回字段无list ****/
//		mapList =  (List<Map<String, Object>>) biiResponseBody.getResult();
//		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_CAPACITYTRANSLIST_MAP, mapList);
////		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(BocInvt.BOCINVT_LIST_RES);
//		/**** 返回字段有list ****/
////		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
////		if(StringUtil.isNullOrEmpty(resultMap)) return;
////		final List<Map<String, Object>> mapList = (List<Map<String, Object>>) resultMap.get(BocInvt.BOCINVT_LIST_RES);
//		
//		if (StringUtil.isNullOrEmpty(mapList)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					this.getString(R.string.acc_transferquery_null));
//			return;
//		}
//		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
////		invtList.addAll(list);
////		invtDetailAdapter.setInverstDetailQueryList(invtList);
//		invtDetailAdapter = new InverstDetailQueryAdapter(this, mapList);
//		invtDetailAdapter.setInverstDetailQueryList(mapList);
//		invtDetailListView.setAdapter(invtDetailAdapter);
//		invtDetailListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				BociDataCenter.getInstance().setInverstDetailMap(
//						mapList.get(position));
//				Intent intent = new Intent(InverstDetailQueryActivity.this, 
//						InverstDetailQueryInfoActivity.class);
//				startActivity(intent);
//			}
//		});
//	}
}
