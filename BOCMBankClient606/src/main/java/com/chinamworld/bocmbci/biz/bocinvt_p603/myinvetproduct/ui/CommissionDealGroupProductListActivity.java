package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGroupInfo;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 委托交易组合购买的被组合产品列表
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealGroupProductListActivity extends BocInvtBaseActivity implements ICommonAdapter<Map<String,Object>>{

	private TextView listHeaderLeftView;
	private TextView listHeaderMiddleView;
	private TextView listHeaderRightView;
	private ListView list;
	private List<Map<String, Object>> ProductResult;
	//	private GroupAdapter adapter;
	private CommonAdapter<Map<String,Object>> adapter;
	private String tranSeq2;
	private CommissionDealForGroupInfo info;
	private String ibknum;
	private String typeOfAccount;
	private String accountKey;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_commission_deal_group_detail_activity_p603);
		setTitle("被组合产品详情");
		if(StringUtil.isNullOrEmpty(getIntent().getStringExtra("temp"))){
			info=(CommissionDealForGroupInfo) getIntent().getSerializableExtra("info");
		}else{
			if(getIntent().getStringExtra("temp").equals("hisQueryDetail")){//历史交易组合购买页面
				tranSeq2=getIntent().getStringExtra("tranSeq2");
				accountKey = getIntent().getStringExtra("accountKey");
			}
		}
		ibknum=getIntent().getStringExtra("ibknum");
		typeOfAccount=getIntent().getStringExtra("typeOfAccount");		
		requestPsnXpadQueryGuarantyProductResult();
	}

	private void requestPsnXpadQueryGuarantyProductResult() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnXpadQueryGuarantyProductResult");
		Map<String, Object> parms=new HashMap<String, Object>();
		if(StringUtil.isNullOrEmpty(getIntent().getStringExtra("temp"))){
			parms.put("tranSeq", info.tranSeq);//组合交易流水号
			parms.put("accountKey", info.accountKey);//账户缓存标示
		}else{
			if(getIntent().getStringExtra("temp").equals("hisQueryDetail")){
				parms.put("tranSeq", tranSeq2);//组合交易流水号
				parms.put("accountKey", accountKey);//账户缓存标示
			}
		}
		parms.put("ibknum", ibknum);//省行联行号
		parms.put("typeOfAccount", typeOfAccount);//账户类型
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadQueryGuarantyProductResultCallBack");

	}

	public void requestPsnXpadQueryGuarantyProductResultCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		ProductResult=HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(ProductResult)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			return;

		}

//		initView();	
		listHeaderLeftView = (TextView) findViewById(R.id.left);
//		listHeaderLeftView.setText("产品代码");
		listHeaderLeftView.setText("产品名称");
		listHeaderMiddleView = (TextView) findViewById(R.id.middle);
		listHeaderMiddleView.setText("交易状态");
		listHeaderRightView = (TextView) findViewById(R.id.right);
		listHeaderRightView.setText("被组合份额");
		list = (ListView) findViewById(R.id.list);
		adapter = new CommonAdapter<Map<String,Object>>(CommissionDealGroupProductListActivity.this, ProductResult, this);
		list.setAdapter(adapter);
	}

//	private void initView() {
//		listHeaderLeftView = (TextView) findViewById(R.id.left);
//		listHeaderLeftView.setText("产品代码");
//		listHeaderMiddleView = (TextView) findViewById(R.id.middle);
//		listHeaderMiddleView.setText("产品名称");
//		listHeaderRightView = (TextView) findViewById(R.id.right);
//		listHeaderRightView.setText("被组合份额");
//		list = (ListView) findViewById(R.id.list);
//		adapter = new CommonAdapter<Map<String,Object>>(CommissionDealGroupProductListActivity.this, ProductResult, this);
//		list.setAdapter(adapter);
//	}

	class ViewHolder {
		public TextView left;
		public TextView middle;
		public TextView right;
		public View view;

	}

	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			/**列表项一行显示**/
//			convertView = inflater.inflate(
//					R.layout.bocinvt_three_view_bisection_horizontal, null);
//			holder.left = (TextView) convertView.findViewById(R.id.left);
//			holder.left.setTextColor(getResources().getColor(R.color.black));
//			holder.middle = (TextView) convertView.findViewById(R.id.middle);
//			holder.middle.setTextColor(getResources().getColor(R.color.black));
//			holder.right = (TextView) convertView.findViewById(R.id.right);
//			holder.right.setTextColor(getResources().getColor(R.color.red));
			/**列表项两行显示**/
			convertView = inflater.inflate(
					R.layout.bocinvt_hispro_list_item, null);
			/**产品名称**/
			holder.left = (TextView) convertView.findViewById(R.id.boci_product_name);
			/**交易状态**/
			holder.middle = (TextView) convertView.findViewById(R.id.boci_yearlyRR);
			/**被组合份额**/
			holder.right = (TextView) convertView.findViewById(R.id.boci_timeLimit);
			holder.right.setTextColor(getResources().getColor(R.color.red));
			/** 右三角 */
			ImageView goDetail = (ImageView) convertView
					.findViewById(R.id.boci_gotoDetail);
			goDetail.setVisibility(View.GONE);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(CommissionDealGroupProductListActivity.this,holder.left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(CommissionDealGroupProductListActivity.this,holder.middle);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(CommissionDealGroupProductListActivity.this,holder.right);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//赋值
		if (StringUtil.isNullOrEmpty(currentItem)) {
			return null;
		}
		holder.left.setText((String)currentItem.get("prodName"));
		holder.middle.setText(LocalData.GuarantyImpawnPermitMap.get((String)currentItem.get("impawnPermit")));	
		if(String.valueOf(currentItem.get("freezeUnit")) == null || (String.valueOf(currentItem.get("freezeUnit")).equals("0.000000")) || (String.valueOf(currentItem.get("freezeUnit")).equals("0.00"))){//被组合份额为空 显示 - 
			holder.right.setText("-");
		}else{//非空
			if(StringUtil.isNullOrEmpty(getIntent().getStringExtra("temp"))){//委托交易
//				holder.right.setText(StringUtil.parseStringCodePattern(info.currency,(String)currentItem.get("freezeUnit"), 2));
				holder.right.setText(StringUtil.parseStringPattern((String)currentItem.get("freezeUnit"), 2));
			}else{
				if(getIntent().getStringExtra("temp").equals("hisQueryDetail")){//历史交易
//					holder.right.setText(StringUtil.parseStringCodePattern(getIntent().getStringExtra("currency"), (String)currentItem.get("freezeUnit"), 2));
					holder.right.setText(StringUtil.parseStringPattern((String)currentItem.get("freezeUnit"), 2));
				}
			}
		}
		
		return convertView;
	}

}

