package com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定投管理查询详情页面
 * 定投管理 终止操作页面
 * @author linyl
 *
 */
public class FixInvestQueryDetailActivity extends GoldBonusBaseActivity implements OnClickListener {
	/**
	 * 枚举详情页面的标示  确认、结果
	 */
	public enum DetailView{
		/**定投输入页面**/
		inputDetailView,
	}
	DetailView detailView = null;
	/**详情信息展示布局**/
	private TitleAndContentLayout mAgreeDetailInfo;
	private Button btn_querylist,btn_cancel,btn_next;
	/**动态添加元素的布局**/
	private LinearLayout mContainerLayout;
	/**定投提前终止确认页面  备注布局**/
	private LinearLayout cancelConfirm_ll,cancelInputTitle;
	private LinearLayout cancelBtn_ll,ll_btn;
	/**列表项详情信息**/
	private Map<String,Object> itemDetailMap;
	private EditText et_fixCancelPs;
	/**贵金属积利定投计划终止确认 上送参数**/
	private Map<String,Object> requestParamMap = new HashMap<String,Object>();
	/**贵金属积利定投计划终止确认返回数据**/
	private Map<String,Object> fixInvestStopPreMap;
	String randomNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_fixinvestmanager);
//		getBackgroundLayout().setRightButtonText("主界面");
		setContentView(R.layout.goldbonus_fixinvest_detail);
		getBackgroundLayout().setLeftButtonNewClickListener(backClickListener);
		mAgreeDetailInfo = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout_detailinfo);
		mAgreeDetailInfo.setTitleVisibility(View.GONE);
		ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
		cancelConfirm_ll = (LinearLayout) findViewById(R.id.fixCancel_confirm);
		cancelBtn_ll = (LinearLayout) findViewById(R.id.ll_cancel_btn);
		btn_next = (Button) findViewById(R.id.btn_next);
		cancelInputTitle = (LinearLayout) findViewById(R.id.cancel_input_info_title);
		mContainerLayout = (LinearLayout) mAgreeDetailInfo.findViewById(R.id.myContainerLayout);
		et_fixCancelPs = (EditText) findViewById(R.id.et_fixcancel_ps);
		/**限制输入位数在100个字符**/
		EditTextUtils.setLengthMatcher(this, et_fixCancelPs, 100);
		//TODO...接口返回数据
		itemDetailMap = GoldbonusLocalData.getInstance().FixInvestListDetailQueryMap;
		//按钮事件监听
		btn_querylist = (Button) findViewById(R.id.btn_detaillist);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		//动态控制终止按钮的显示（状态为有效显示，反之隐藏，默认显示）
		if(!"1".equals((String)itemDetailMap.get("fixStatus"))){//隐藏终止按钮
			btn_cancel.setVisibility(View.GONE);
		}
		if("0".equals((String)itemDetailMap.get("fixPendCnt"))){
			btn_querylist.setVisibility(View.GONE);
		}
		btn_querylist.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		initDetailView();
	}
	
	/**
	 * 详情页面展示元素
	 */
	private void initDetailView() {
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_settime, (String)itemDetailMap.get("crtDate"),null));
		mContainerLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName, (String)itemDetailMap.get("issueName"),null));
		mContainerLayout.addView(createLabelTextView(R.string.goldbonus_fixinvest_status, 
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixStatus"),DictionaryData.FixStatusList),null));
		if("0".equals((String)itemDetailMap.get("fixTermType"))){//日
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList),null));
		}else if("1".equals((String)itemDetailMap.get("fixTermType"))){//周
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, 
					DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList) + "," +
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixPayDateValue"),DictionaryData.goldbonusfixPayDateValueWeekList),null));
		}else if("2".equals((String)itemDetailMap.get("fixTermType"))){//月
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, 
					DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList) + "," +
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixPayDateValue"),DictionaryData.goldbonusfixPayDateValueMounthList),null));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle,"-",null));
		}
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_buynum, StringUtil.parseStringPattern(StringUtil.deleateNumber((String)itemDetailMap.get("weight")),0)+" 克",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_fixPendCnt,
				"成功 "+String.valueOf((Long.parseLong((String)itemDetailMap.get("fixPendCnt")) - Long.parseLong((String)itemDetailMap.get("fixCount")))) +" 次，失败 " + (String)itemDetailMap.get("fixCount") +" 次",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_remaindcnt, (String)itemDetailMap.get("unfCount") +	" 次",null));
		if("2".equals((String)itemDetailMap.get("fixStatus")) || "3".equals((String)itemDetailMap.get("fixStatus"))){//状态为客户终止或者银行终止
			if(!"".equals(itemDetailMap.get("remark").toString().trim()) && StringUtil.isNullOrEmpty(itemDetailMap.get("remark"))){
				mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, "-", null));
			}else{
				mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, 
						(String)itemDetailMap.get("remark") , null));
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_detaillist://执行明细
			Intent intent = new Intent(this,ExecutionDetailsActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_cancel://提前终止---跳终止输入页面
			detailView = DetailView.inputDetailView;
			cancelInputTitle.setVisibility(View.VISIBLE);
			ll_btn.setVisibility(View.GONE);
			cancelBtn_ll.setVisibility(View.VISIBLE);
			btn_next.setVisibility(View.VISIBLE);
			cancelConfirm_ll.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_next://终止输入页面---跳终止确认页面
//			/**正则校验 提前终止原因输入信息**/
//			ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
//			RegexpBean rb = new RegexpBean("提前终止原因", et_fixCancelPs.getText().toString().trim(), "remarksnamecannel");
//			list.add(rb);
//			if (!RegexpUtils.regexpDate(list)) {// 校验不通过
//				return;
//			}else{//正则校验格式 校验通过
//				//弹安全工具
//				requestCommConversationId();
//			}
			//弹安全工具
			requestCommConversationId();
			break;
		}
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
		requestGetSecurityFactor("PB275");
	}
	
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						requestPsnGoldBonusFixInvestStopPre();
					}
				});
	}
	
	/**
	 * 贵金属积利定投计划终止确认
	 */
	private void requestPsnGoldBonusFixInvestStopPre(){
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		requestParamMap.put("fixId", 
				GoldbonusLocalData.getInstance().FixInvestListDetailQueryMap.get("fixId"));
		requestParamMap.put("xpadRemark", et_fixCancelPs.getText().toString());
		requestParamMap.put("_combinId", BaseDroidApp.getInstanse().getSecurityChoosed());
		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusFixInvestStopPre", requestParamMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				GoldbonusLocalData.getInstance().FixInvestStopPreMap = result;
				fixInvestStopPreMap = result;
				//请求随机数
				requestForRandomNumber();
			}
		});
	}
	
	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}
	
	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		/**随机数*/
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		//关闭通讯框
		BiiHttpEngine.dissMissProgressDialog();
		/**跳转到确认页面**/
		Intent intent = new Intent(this, FixInvestStopDetailActivity.class);
		intent.putExtra(Acc.RANDOMNUMBER, randomNumber);
		intent.putExtra("fixCancelPs", et_fixCancelPs.getText().toString());
		startActivity(intent);
//		startActivityForResult(intent, 10001);
		
//		InputMethodManager imm = (InputMethodManager)this
//			    .getSystemService(Context.INPUT_METHOD_SERVICE);
//
//	    imm.hideSoftInputFromWindow(et_fixCancelPs.getWindowToken(), 0);
	}
	

	/**返回键处理事件**/
	OnClickListener backClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			backClick();
		}
	};
	
	@Override
	public void onBackPressed() {
		backClick();
	}

	/**
	 * 返回事件的处理
	 */
	private void backClick(){
		if(detailView == DetailView.inputDetailView){
			cancelInputTitle.setVisibility(View.GONE);
			ll_btn.setVisibility(View.VISIBLE);
			cancelConfirm_ll.setVisibility(View.GONE);
			cancelBtn_ll.setVisibility(View.GONE);
			et_fixCancelPs.setText("");//返回后清空输入框信息
			detailView = null;
		}else{
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 10001:
			setResult(10001);
			finish();
			break;

		default:
			break;
		}
	}
	
}
