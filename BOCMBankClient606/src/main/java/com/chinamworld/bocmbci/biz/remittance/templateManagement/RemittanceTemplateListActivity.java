package com.chinamworld.bocmbci.biz.remittance.templateManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.TemplateListAdapter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnOtherOperationListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnStartRemittanceListener;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 模板管理
 * 
 * @author Zhi
 */
public class RemittanceTemplateListActivity extends RemittanceBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 跨境汇款列表 */
	private List<Map<String, Object>> listTemplate;
	/** 跨境汇款列表控件 */
	private ListView lvTemplate;
	/** 列表适配器 */
	private TemplateListAdapter adapter;
	/** 当前操作的列表项下标 */
	private int position;
	/** 更多视图 */
	private View mFooterView;
	/** 新模板名称输入框 */
	private EditText etNewName;
	/** 动作标记 0-修改模板别名 1-删除模板 获取token回调方法要根据该标记确定走哪个流程 */
	private int actionFlag;
	/** 请求模板详情标记 0-发起汇款 1-汇款详情页 */
	private int detailFlag;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.remittance_model_manage));
		addView(R.layout.remittance_template_list);
		initView();
	}
	
	private void initView() {
		lvTemplate = (ListView) findViewById(R.id.listview);
		mFooterView = View.inflate(this, R.layout.remittance_template_list_footer, null);
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/**
	 * 添加分页布局
	 * 
	 * @param totalCount
	 *            后台的产品列表总长度
	 */
	private void addFooterView(String totalCount) {
		int listSize = listTemplate.size();
		if (Integer.valueOf(totalCount) > listSize) {
			if (lvTemplate.getFooterViewsCount() <= 0) {
				lvTemplate.addFooterView(mFooterView);
			}
		} else {
			if (lvTemplate.getFooterViewsCount() > 0) {
				lvTemplate.removeFooterView(mFooterView);
			}
		}
		((TextView) mFooterView.findViewById(R.id.finc_listiterm_tv1)).
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 请求更多
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Remittance.CURRENTINDEX, String.valueOf(listTemplate.size()));
				params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
				params.put(Remittance._REFRESH, "false");
				getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEQUERY, "requestPsnInternationalTransferTemplateQueryCallBack", params, true);
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 发起汇款监听 */
	private OnStartRemittanceListener startRemittanceListener = new OnStartRemittanceListener() {
		
		@Override
		public void onStartRemittance(int position) {
			detailFlag = 0;
			RemittanceTemplateListActivity.this.position = position;
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Remittance.TEMPLATEID, listTemplate.get(position).get(Remittance.TEMPLATEID));
			params.put(Remittance.IDENTITYTYPE, listTemplate.get(position).get(Remittance.IDENTITYTYPE));
			params.put(Remittance.IDENTITYNUMBER, listTemplate.get(position).get(Remittance.IDENTITYNUMBER));
			getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEDETAILQUERY, "requestPsnInternationalTransferTemplateDetailQueryCallBack", params, false);
		}
	};
	
	/** 其他操作监听 */
	private OnOtherOperationListener othorOperationListener = new OnOtherOperationListener() {
		
		@Override
		public void onOtherOperation(final int position) {
			RemittanceTemplateListActivity.this.position = position;
			View menuView = new CustomDialog(RemittanceTemplateListActivity.this).initRemitTemplateMenuDialog(
					new OnClickListener() {
						// 修改模板名称
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dissmissFootChooseDialog();
							View dialogView = View.inflate(RemittanceTemplateListActivity.this, R.layout.remittance_template_updatename_dialog, null);
							((TextView) dialogView.findViewById(R.id.tv_name)).setText((String) listTemplate.get(position).get(Remittance.TEMPLATENAME));;
							PopupWindowUtils.getInstance().setOnShowAllTextListener(RemittanceTemplateListActivity.this, ((TextView) dialogView.findViewById(R.id.tv_name)));
							etNewName = (EditText) dialogView.findViewById(R.id.et_newName);
							EditTextUtils.setLengthMatcher(RemittanceTemplateListActivity.this, etNewName, 50);
							BaseDroidApp.getInstanse().showDialog(dialogView);
						}
					}, 
					// 查询模板详情
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							detailFlag = 1;
							BaseDroidApp.getInstanse().dissmissFootChooseDialog();
							BaseHttpEngine.showProgressDialog();
							Map<String, Object> params = new HashMap<String, Object>();
							params.put(Remittance.TEMPLATEID, listTemplate.get(position).get(Remittance.TEMPLATEID));
							params.put(Remittance.IDENTITYTYPE, listTemplate.get(position).get(Remittance.IDENTITYTYPE));
							params.put(Remittance.IDENTITYNUMBER, listTemplate.get(position).get(Remittance.IDENTITYNUMBER));
							getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEDETAILQUERY, "requestPsnInternationalTransferTemplateDetailQueryCallBack", params, false);
						}
					}, 
					// 删除模板
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dissmissFootChooseDialog();
							BaseDroidApp.getInstanse().showErrorDialog(
									getResources().getString(R.string.remittance_template_otherOperation_delateTip),
									R.string.cancle, R.string.confirm,
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											switch (Integer.parseInt(v.getTag() + "")) {
											case CustomDialog.TAG_SURE:
												actionFlag = 1;
												BaseHttpEngine.showProgressDialog();
												requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
												break;
											case CustomDialog.TAG_CANCLE:
												BaseDroidApp.getInstanse().dismissErrorDialog();
												break;
											}
										}
									});
						}
					}, 
					// 取消
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dissmissFootChooseDialog();
						}
					});
			BaseDroidApp.getInstanse().showFootChooseDialog(menuView);
		}
	};
	
	public void dialogClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancle:
			BaseDroidApp.getInstanse().dismissErrorDialog();
			break;

		case R.id.btn_confirm:
			String newName = etNewName.getText().toString().trim();
			if (StringUtil.isNull(newName)) {
				CustomDialog.toastInCenter(this, getResources().getString(R.string.remittance_template_otherOperation_pleaseInputNewName));
				return;
			}
			if(!submitRegexp(true)){
				return;
			}
			actionFlag = 0;
			BaseHttpEngine.showProgressDialog();
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			break;
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Remittance.CURRENTINDEX, "0");
		params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
		params.put(Remittance._REFRESH, "true");
		getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEQUERY, "requestPsnInternationalTransferTemplateQueryCallBack", params, true);
		getHttpTools().registErrorCode(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEQUERY,"CCSS.S0012");
	}
	
	/** 请求模板列表回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInternationalTransferTemplateQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) getHttpTools().getResponseResult(resultObj);
		String recordNumber = (String) resultMap.get(Remittance.RECORDNUMBER);
		List<Map<String, Object>> templateList = (List<Map<String, Object>>) resultMap.get(Remittance.TEMPLATELIST);
		if (StringUtil.isNullOrEmpty(templateList)) {
			return;
		}
		if (StringUtil.isNullOrEmpty(listTemplate)) {
			listTemplate = templateList;
		} else {
			listTemplate.addAll(templateList);	
		}
		addFooterView(recordNumber);
		if (adapter == null) {
			adapter = new TemplateListAdapter(this, listTemplate, startRemittanceListener, othorOperationListener);
			lvTemplate.setAdapter(adapter);
		} else {
			adapter.setData(listTemplate);
		}
	}
	
	@Override
	public boolean doHttpErrorHandler(String method, BiiError biiError) {
		super.doHttpErrorHandler(method, biiError);
		String errorCode = biiError.getCode();
		if ("CCSS.S0012".equals(errorCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("您尚未保存模板！");
		}
		return true;
	}
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Remittance.TEMPLATEID, listTemplate.get(position).get(Remittance.TEMPLATEID));
		params.put(Remittance.IDENTITYTYPE, listTemplate.get(position).get(Remittance.IDENTITYTYPE));
		params.put(Remittance.IDENTITYNUMBER, listTemplate.get(position).get(Remittance.IDENTITYNUMBER));
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		if (actionFlag == 0) {
			params.put(Remittance.TEMPLATENAME, etNewName.getText().toString().trim());
			getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATENAMEMODIFY, "requestPsnInternationalTransferTemplateNameModifyCallBack", params, true);
		} else if (actionFlag == 1) {
			getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEDEL, "requestPsnInternationalTransferTemplateDelCallBack", params, true);
		}
	}
	
	/** 修改账户别名回调 */
	public void requestPsnInternationalTransferTemplateNameModifyCallBack(Object resultObj) {
		BaseDroidApp.getInstanse().dismissErrorDialog();
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,getString(R.string.set_header_editlimitsuccess));
		listTemplate.clear();
		adapter.setData(listTemplate);
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	private String payeeBankSwift;

	private boolean isOverseasChinaBank;

	/** 查询模板详情回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInternationalTransferTemplateDetailQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = (Map<String, Object>) getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		resultMap.putAll(listTemplate.get(position));
		RemittanceDataCenter.getInstance().setMapPsnInternationalTransferTemplateDetailQuery(resultMap);

		payeeBankSwift = (String) resultMap.get("payeeBankSwift");
		payeeBankSwift = payeeBankSwift.substring(0,8);

		requestPsnBOCPayeeBankInfoQuery();
//			getHttpTools().requestHttp(Remittance.PSNQRYINTERNATIONALTRANS4CNYCOUNTRY,
//					"requestPsnQryInternationalTrans4CNYCountryCallBack", null, false);

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

		for (int i = 0; i < list.size() ; i++) {
			String bocPayeeBankSwift = (String) list.get(i).get("bocPayeeBankSwift");
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

		getHttpTools().requestHttp(Remittance.PSNQRYINTERNATIONALTRANS4CNYCOUNTRY,
				"requestPsnQryInternationalTrans4CNYCountryCallBack", null, false);
	}
	
	/** 删除模板回调 */
	public void requestPsnInternationalTransferTemplateDelCallBack(Object resultObj) {
		BaseDroidApp.getInstanse().dismissErrorDialog();
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,getString(R.string.safety_delete_success));
		listTemplate.clear();
		adapter.setData(listTemplate);
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	
	/** 查询收款人常驻地区回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnQryInternationalTrans4CNYCountryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = getHttpTools().getResponseResult(resultObj);
		List<Map<String, String>> resultList = (List<Map<String, String>>) resultMap.get(Remittance.COUNTRYLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}

		RemittanceDataCenter.getInstance().setListPsnQryInternationalTrans4CNYCountry(resultList);
		if (detailFlag == 0) {
			Log.v("100",isOverseasChinaBank+"-----");
			if (isOverseasChinaBank) {
//				startActivity(new Intent(RemittanceTemplateListActivity.this, OverseasChinaBankRemittanceInfoInputActivity.class)
//						.putExtra(RemittanceContent.JUMPFLAG, true).putExtra("bocPayeeBankRegionCN",bocPayeeBankRegionCN));
				startActivity(new Intent(this, OverseasChinaBankRemittanceInfoInputActivity.class).putExtra(RemittanceContent.JUMPFLAG, true));
			} else {
				startActivity(new Intent(this, RemittanceInfoInputActivity.class).putExtra(RemittanceContent.JUMPFLAG, true));
			}
			finish();
		} else if (detailFlag == 1) {
			startActivity(new Intent(this, RemittanceTemplateDetailActivity.class));
		}
	}
	/** 输入数据判空及校验*/
	private boolean submitRegexp(boolean required){
		ArrayList<RegexpBean> lists=new ArrayList<RegexpBean>();
		//新模板名称
		if(onlyRegular(required, etNewName.getText().toString().trim())){
			RegexpBean newModelName=new RegexpBean(RemittanceContent.MODELNAME_CN,
					etNewName.getText().toString().trim(), RemittanceContent.REMITTANCEMODELNAME);
			lists.add(newModelName);
		}
		if(!RegexpUtils.regexpDate(lists)){
			return false;
		}
		return true;
	}
	/** 只作正则校验*/
	private boolean onlyRegular(boolean required,String content){
		if((!required && !StringUtil.isNull(content))||required){
			return true;
		}
		return false;
	}
}
