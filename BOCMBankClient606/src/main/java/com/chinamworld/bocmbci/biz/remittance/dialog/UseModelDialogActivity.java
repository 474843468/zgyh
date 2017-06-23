package com.chinamworld.bocmbci.biz.remittance.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.UseModelAdapter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class UseModelDialogActivity extends BaseActivity {
	private RelativeLayout rl_bank;
	/** 列表控件 */
	private ListView mListView;
	/** 关闭图标 */
	private ImageView mIv;
	/** 模板信息列表 */
	private List<Map<String, Object>> mList;
	/** 列表适配器 */
	private UseModelAdapter modelListAdapter;
	/** 更多视图 */
	private View mFooterView;

	private boolean isOverseasChinaBank;

	private int isOversearChinaBankModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置为Dialog
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 设置布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		rl_bank.removeAllViews();
		rl_bank.addView(initView());
		isOversearChinaBankModel = getIntent().getIntExtra("isOversearChinaBankModel",1);
	}

	private View initView() {
		View view = LayoutInflater.from(this).inflate(R.layout.remittance_click_usermodel, null);
		mListView = (ListView) view.findViewById(R.id.lv_model_list);
		mIv = (ImageView) view.findViewById(R.id.img_exit_accdetail_1);
		mFooterView = LayoutInflater.from(this).inflate(R.layout.remittance_template_list_footer, null);
		mIv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		getData();
		return view;
	}

	private void getData() {
		List<Map<String, Object>> modelList = RemittanceDataCenter.getInstance().getModelList();
		if (StringUtil.isNullOrEmpty(mList)) {
			mList = modelList;
		} else {
			mList.addAll(modelList);
		}
		String totalNumber = RemittanceDataCenter.getInstance().getTotalNumber();
		addFootView(totalNumber);
		if (modelListAdapter == null) {
			modelListAdapter = new UseModelAdapter(mList, this);
			modelListAdapter.setOnItemClickLis(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// 查询模板详情
					BaseHttpEngine.showProgressDialog();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Remittance.TEMPLATEID, mList.get(position).get(Remittance.TEMPLATEID));
					params.put(Remittance.IDENTITYTYPE, mList.get(position).get(Remittance.IDENTITYTYPE));
					params.put(Remittance.IDENTITYNUMBER, mList.get(position).get(Remittance.IDENTITYNUMBER));
					getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEDETAILQUERY, "queryModelDetailCallBack", params);

				}
			});
			mListView.setAdapter(modelListAdapter);
		} else {
			modelListAdapter.changeData(mList);
		}
	}

	//模板详情中收款行Swift
	private String payeeBankSwift;
	/**
	 * 查询模板详情返回信息
	 */
	public void queryModelDetailCallBack(Object resultObj) {

		Map<String, Object> resultMap = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		payeeBankSwift = (String) resultMap.get("payeeBankSwift");
		payeeBankSwift = payeeBankSwift.substring(0,8);
		RemittanceDataCenter.getInstance().setMapPsnInternationalTransferTemplateDetailQuery(resultMap);

		requestPsnBOCPayeeBankInfoQuery();
		// 查询收款人常驻国家列表
		//getHttpTools().requestHttp(Remittance.PSNQRYINTERNATIONALTRANS4CNYCOUNTRY, "queryCountryCodeCallBack", null);
	}

	/**
	 * 调用查询境外中行收款银行信息接口
	 */
	public void requestPsnBOCPayeeBankInfoQuery() {

		Map<String, Object> params = new HashMap<String, Object>();

		getHttpTools().requestHttp("PsnBOCPayeeBankInfoQuery", "requestPsnBOCPayeeBankInfoQueryCallBack", params,false);

	}

	private String bocPayeeBankRegionCN;
	/**
	 * 境外中行收款行信息查询接口回调
	 * @param resultObj
	 */
	public void requestPsnBOCPayeeBankInfoQueryCallBack(Object resultObj) {

		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}

		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) result
				.get("bocPayeeBankInfoList");

		String bocPayeeBankSwift;
		for (int i = 0; i < list.size() ; i++) {
			bocPayeeBankSwift = (String) list.get(i).get("bocPayeeBankSwift");
			bocPayeeBankSwift = bocPayeeBankSwift.substring(0,8);
			if(payeeBankSwift.equals(bocPayeeBankSwift)) {
				isOverseasChinaBank = true;
				bocPayeeBankRegionCN = (String) list.get(i).get("bocPayeeBankRegionCN");
				RemittanceDataCenter.getInstance().setmapPayeeBankOver(list.get(i));
				break;
			} else {
				isOverseasChinaBank = false;
				continue;
			}
		}

		// 查询收款人常驻国家列表
		getHttpTools().requestHttp(Remittance.PSNQRYINTERNATIONALTRANS4CNYCOUNTRY, "queryCountryCodeCallBack", null);
	}

	/**
	 * 查询收款人常驻国家列表返回信息
	 */
	@SuppressWarnings("unchecked")
	public void queryCountryCodeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = getHttpTools().getResponseResult(resultObj);
		List<Map<String, String>> resultList = (List<Map<String, String>>) resultMap.get(Remittance.COUNTRYLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		RemittanceDataCenter.getInstance().setListPsnQryInternationalTrans4CNYCountry(resultList);
//		startActivity(new Intent(this, RemittanceInfoInputActivity.class).putExtra(RemittanceContent.JUMPFLAG, true));
//		setResult(RemittanceContent.CHOOSE_MODE);
//		finish();

		if (isOverseasChinaBank) {
			if (isOversearChinaBankModel == 1) {
				startActivity(new Intent(UseModelDialogActivity.this, OverseasChinaBankRemittanceInfoInputActivity.class)
						.putExtra(RemittanceContent.JUMPFLAG, true).putExtra("isOhter",true));

			} else {

				setResult(RemittanceContent.CHOOSE_MODE,getIntent());
			}

		} else {
			//startActivity(new Intent(UseModelDialogActivity.this, RemittanceInfoInputActivity.class).putExtra(RemittanceContent.JUMPFLAG, true));
			if (isOversearChinaBankModel == 1) {
				setResult(RemittanceContent.CHOOSE_MODE);
			} else {
				startActivity(new Intent(UseModelDialogActivity.this,RemittanceInfoInputActivity.class)
						.putExtra(RemittanceContent.JUMPFLAG, true).putExtra("isOhter",true));
			}
		}
		finish();
	}

		/**
         * 添加分页布局
         *
         * @param totalCount后台查询返回的模板列表总条数
         */
	private void addFootView(String totalCount) {
		int listSize = mList.size();
		if (Integer.valueOf(totalCount) > listSize) {
			if (mListView.getFooterViewsCount() <= 0) {
				mListView.addFooterView(mFooterView);
			}
		} else {
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(mFooterView);
			}
		}
		mFooterView.findViewById(R.id.finc_listiterm_tv1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 请求更多
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Remittance.CURRENTINDEX, String.valueOf(mList.size()));
				params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
				params.put(Remittance._REFRESH, "false");
				getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEQUERY, "transferTemplateQueryCallBack", params,true);
			}
		});
	}
	
	/** 跨境汇款模板查询回调，点更多时回调这个方法 */
	@SuppressWarnings("unchecked")
	public void transferTemplateQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = getHttpTools().getResponseResult(resultObj);
		String totalNumber = (String) resultMap.get(Remittance.RECORDNUMBER);
		List<Map<String, Object>> modelList = (List<Map<String, Object>>) resultMap.get(Remittance.TEMPLATELIST);
		if (StringUtil.isNullOrEmpty(modelList)) {
			return;
		}
		mList.addAll(modelList);
		addFootView(totalNumber);
		modelListAdapter.changeData(mList);
		RemittanceDataCenter.getInstance().setTotalNumber(totalNumber);
		RemittanceDataCenter.getInstance().setModelList(mList);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}

}
