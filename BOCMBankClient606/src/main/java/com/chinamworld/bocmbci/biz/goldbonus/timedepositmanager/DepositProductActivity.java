package com.chinamworld.bocmbci.biz.goldbonus.timedepositmanager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 定期产品 活期转定期 详情页面(输入、确认、结果页面)
 * @author linyl
 *
 */
public class DepositProductActivity extends GoldBonusBaseActivity implements OnClickListener {
	/**
	 * 枚举详情页面的标示---输入、确认、结果
	 */
	public enum DetailView{
		/**输入页面**/
		inputDetailView,
		/**确认页面**/
		confirmDetailView,
		/**结果页面**/
		successDetailView,
	}
	private TitleAndContentLayout titleAndContentLayout;
	private LinearLayout myContainerLayou,ll_inputNum;
	private Button btnNext,btnConfirm,btnFinish;
	private LinearLayout confirmTitle,successTitle,ll_wenxin;
	private EditText et_inputNum;
	/**详情页面标示---输入、确认、结果**/
	private DetailView detailView = DetailView.inputDetailView;
	/**列表项选中数据**/
	private Map<String,Object> ItemMap;
	/**贵金属积利定存到期日查询  预交易接口上送数据**/
	private Map<String,Object> reqTimeDepositExpDateQryMap = new HashMap<String, Object>();
	/**贵金属积利定存到期日查询  提交交易接口上送数据**/
	private Map<String,Object> reqTimeDepositSubmitMap = new HashMap<String, Object>();
	private String expDate;
	private String systemDate;
	private String tokenId;
	private String transactionId,availDate,expDateSubmit;
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_timedepositmanager);
//		getBackgroundLayout().setRightButtonText("主界面");
		setContentView(R.layout.goldbonus_depositpro);
		getBackgroundLayout().setLeftButtonNewClickListener(backClickListener);
		titleAndContentLayout = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout);
		titleAndContentLayout.setTitleVisibility(View.GONE);
		myContainerLayou = (LinearLayout) findViewById(R.id.myContainerLayout);
		ll_inputNum = (LinearLayout) findViewById(R.id.input_num);
		ll_inputNum.setVisibility(View.GONE);
		et_inputNum = (EditText) findViewById(R.id.et_inputnum);
		confirmTitle = (LinearLayout) findViewById(R.id.depositpro_confirm_info_title);
		successTitle = (LinearLayout) findViewById(R.id.depositpro_success_info_title);
		tv = (TextView) findViewById(R.id.tv_success_title);
		tv.getPaint().setFakeBoldText(true);
		ll_wenxin = (LinearLayout) findViewById(R.id.ll_wenxin);
		btnNext = (Button) findViewById(R.id.depositpro_next);
		btnConfirm = (Button) findViewById(R.id.depositpro_confirm);
		btnFinish = (Button) findViewById(R.id.depositpro_finish);
		ItemMap = GoldbonusLocalData.getInstance().ProductInfoQueryQueryMap;
		btnNext.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
		requestSystemDateTime();
		BaseHttpEngine.showProgressDialogCanGoBack();
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		systemDate = QueryDateUtils.getcurrentDate(dateTime);
		/**调定存的到期日查询接口**/
		requestPsnGoldBonusTimeDepositExpDateQry();
	}
	/**
	 * 
	 * 贵金属积利定存到期日查询 接口
	 */
	private void requestPsnGoldBonusTimeDepositExpDateQry() {
		reqTimeDepositExpDateQryMap.put("issueNo", (String)ItemMap.get("issueNo"));
		reqTimeDepositExpDateQryMap.put("limitTime", (String)ItemMap.get("limitTime"));
		reqTimeDepositExpDateQryMap.put("limitTnit", (String)ItemMap.get("limitUnit"));
		
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusTimeDepositExpDateQry", reqTimeDepositExpDateQryMap, null,new IHttpResponseCallBack<Map<String,Object>>(){

			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				expDate = (String)(result.get("expDate"));
				if(StringUtil.isNullOrEmpty(expDate)){
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							DepositProductActivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				}
				initDetailView();
				ll_inputNum.setVisibility(View.VISIBLE);
			}
		});
	}
	/**
	 * 初始化定存管理 定期产品 活期转定期 页面元素
	 */
	private void initDetailView() {
		myContainerLayou.removeAllViews();
		if(detailView == DetailView.successDetailView){//结果页面
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_deposit_transactionId, transactionId,null));
//			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_deposit_tradeNo, "1"));
		}
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_deposit_accno, StringUtil
				.getForSixForString((String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery
						.get(GoldBonus.ACCOUNTNUM))+" "+DictionaryData.getKeyByValue((String)GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get("acctType"),
								DictionaryData.goldbonusAcctTypeList),null));
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName, (String)ItemMap.get("issueName"),null));
		if(detailView == DetailView.inputDetailView ){
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_deposit_yue, StringUtil.parseStringPattern(StringUtil.deleateNumber((String)GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery.get("balance")),0)," 克",REDBLACK));
		}
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_deposit_period, String.valueOf(ItemMap.get("limitTime")) +
				DictionaryData.getKeyByValue((String)ItemMap.get("limitUnit"),DictionaryData.goldbonuslimitUnitList),null));
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_deposit_rate,  paseEndZero(String.valueOf(ItemMap.get("issueRate")))+"%",null));
		if(detailView == DetailView.inputDetailView || detailView == DetailView.confirmDetailView){//确认页面、输入页面
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_startdate1, systemDate,null));
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_enddate1, expDate,null));
		}else if(detailView == DetailView.successDetailView){//结果页面
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_startdate1, availDate,null));
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_enddate1, expDateSubmit,null));
		}
		if(detailView == DetailView.confirmDetailView || detailView == DetailView.successDetailView){//确认页面 和 结果页面
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_deposit_num, StringUtil.parseStringPattern(et_inputNum.getText().toString(),0)+" 克",null));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.depositpro_next://下一步
			if("".equals(et_inputNum.getText().toString().trim())){
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入活期转定期数量");
				return;
			}
			
//			if(et_inputNum.getText().toString().trim().startsWith("0") || Long.valueOf(et_inputNum.getText().toString()) > 3000){
//				BaseDroidApp.getInstanse().showInfoMessageDialog("活期转定期数量应为整数，且不应低于1克或高于3000克"); return;
//			}
			if(et_inputNum.getText().toString().trim().startsWith("0")){
				BaseDroidApp.getInstanse().showInfoMessageDialog("活期转定期数量只能为大于0的整数"); return;
			}
			
			if(Long.valueOf(et_inputNum.getText().toString()) > Long
					.valueOf(StringUtil.deleateNumber((String)GoldbonusLocalData.getInstance()
							.PsnGoldBonusAccountQuery.get("balance")))){//输入数量不可大于活期余额
				BaseDroidApp.getInstanse().showInfoMessageDialog("活期转定期数量不能大于活期贵金属积利余额");
				return;
			}
			
			detailView = DetailView.confirmDetailView;
			ll_wenxin.setVisibility(View.GONE);
			btnConfirm.setVisibility(View.VISIBLE);
			ll_inputNum.setVisibility(View.GONE);
			confirmTitle.setVisibility(View.VISIBLE);
			initDetailView();
			break;
		case R.id.depositpro_confirm://确认
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
			break;
		case R.id.depositpro_finish://完成
			finish();
			break;
		}
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		//获取Token
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		//调贵金属积利定存 提交接口
		requestPsnGoldBonusTimeDepositSubmit();
	}
	
	/**
	 * 贵金属积利定存 提交接口
	 */
	private void requestPsnGoldBonusTimeDepositSubmit() {
		reqTimeDepositSubmitMap.put("issueNo", (String)ItemMap.get("issueNo"));
		reqTimeDepositSubmitMap.put("issueName", (String)ItemMap.get("issueName"));
		reqTimeDepositSubmitMap.put("xpadIssueType", (String)ItemMap.get("issueType"));
		reqTimeDepositSubmitMap.put("issueKind", (String)ItemMap.get("issueKind"));
		reqTimeDepositSubmitMap.put("limitTime", (String)ItemMap.get("limitTime"));
		reqTimeDepositSubmitMap.put("limitTnit", (String)ItemMap.get("limitUnit"));
		reqTimeDepositSubmitMap.put("issueRate", (String)ItemMap.get("issueRate"));
		reqTimeDepositSubmitMap.put("weight", et_inputNum.getText().toString().trim());
		reqTimeDepositSubmitMap.put("token", tokenId);
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusTimeDepositSubmit", reqTimeDepositSubmitMap, (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID),new IHttpResponseCallBack<Map<String,Object>>(){

			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				transactionId = (String) result.get("transactionId");
				availDate = (String) result.get("availDate");
				expDateSubmit = (String) result.get("expDate");
				DepositProductActivity.this.getBackgroundLayout().setLeftButtomNewVisibility(View.GONE);
				detailView = DetailView.successDetailView;
				confirmTitle.setVisibility(View.GONE);
				successTitle.setVisibility(View.VISIBLE);
				ll_wenxin.setVisibility(View.GONE);
				btnConfirm.setVisibility(View.GONE);
				initDetailView();
				btnFinish.setVisibility(View.VISIBLE);
				
			}
		});
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
		if(detailView == DetailView.successDetailView || detailView == DetailView.inputDetailView){//结果页面
			finish();
		}else if(detailView == DetailView.confirmDetailView){//确认页面--->输入页
			detailView = DetailView.inputDetailView;
			ll_wenxin.setVisibility(View.VISIBLE);
			btnConfirm.setVisibility(View.GONE);
			ll_inputNum.setVisibility(View.VISIBLE);
			confirmTitle.setVisibility(View.GONE);
			initDetailView();
		}
	}

}
