package com.chinamworld.bocmbci.biz.goldbonus.busitrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 积利金 买卖交易 定投预约
 * @author linyl
 *
 */
public class FixInvestSignActivity extends GoldBonusBaseActivity {
	
	private Button btnNext;
	/** 加密控件里的随机数 */
	private String randomNumber;
	private Map<String,Object> requestParamMap = new HashMap<String,Object>();
	private Map<String,Object> reqPayDateQryParamsMap = new HashMap<String, Object>();
	private Spinner spTermUnit,spTraDate,spEnd;
	private TextView info;
	private List<String> termNumList,codevalList;
	private ArrayAdapter<String> adapter,payDateAdapter;  
	/**扣款日期限查询单项Map**/
	private List<Map<String,Object>> payDateList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_busitrademain_title);
		setContentView(R.layout.goldbonus_fixinvest_sign);
		info = (TextView) findViewById(R.id.textinfo);
		spTermUnit = (Spinner) findViewById(R.id.sp_goldbonus_fixsign_fixFreque);
		spTraDate = (Spinner) findViewById(R.id.sp_goldbonus_fixsign_fixPayDate);
		spEnd = (Spinner) findViewById(R.id.sp_goldbonus_fixsign_termNum);
//		spTermUnitAdapter = new DictionaryDataAdapter(this, spTermUnit, DictionaryData.goldbonusfixPayDateQryList);
		spTermUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){//日
					info.setText("周末及法定节假日除外");
				}else if(position == 1){//周
					info.setText("如遇法定节假日，交易将延后至下一工作日执行");
				}else if(position == 2){//月
					info.setText("如遇法定节假日，交易将延后至下一工作日执行");
				}
				//清空集合
				if(termNumList != null){
					termNumList.clear();
				}
				if(codevalList != null){
					codevalList.clear();
				}
				requestPsnGoldBonusFixInvestPayDateQry(String.valueOf(spTermUnit.getSelectedItemPosition()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		btnNext = (Button) findViewById(R.id.btnnext);
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
		//扣款日查询接口  
		requestPsnGoldBonusFixInvestPayDateQry("0");//默认上送日
	}
	/**
	 * 贵金属积利定投扣款日期限查询 
	 */
	private void requestPsnGoldBonusFixInvestPayDateQry(final String termUnit) {
		reqPayDateQryParamsMap.put("termUnit", termUnit);
//		reqPayDateQryParamsMap.put("termUnit", String.valueOf(spTermUnit.getSelectedItemPosition()));
		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusFixInvestPayDateQry", reqPayDateQryParamsMap, null,new IHttpResponseCallBack<Map<String,Object>>(){

			@SuppressWarnings("unchecked")
			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				List<Map<String,Object>> list = (List<Map<String, Object>>) result.get("list");
				if (list == null || list.size() == 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							FixInvestSignActivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				} 
//				PayDateQryMap = list.get(0);
				termNumList = new ArrayList<String>();
				for(int i = 0;i<list.size();i++){
					termNumList.add((String)list.get(i).get("termNum"));
				}
				adapter = new ArrayAdapter<String>(FixInvestSignActivity.this,android.R.layout.simple_spinner_item,termNumList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spEnd.setAdapter(adapter);
				codevalList = new ArrayList<String>();
				payDateList = (List<Map<String, Object>>) result.get("payDateList");
				if("0".equals(termUnit)){//日
					codevalList.add("每日");
				}else{// 周  和  月  
					for(int i = 0;i<Integer.parseInt((String)result.get("payDateListSize"));i++){
						if("1".equals(termUnit)){//周 
							switch (Integer.parseInt((String)payDateList.get(i).get("codeval"))) {
							case 1:
								codevalList.add("周一");
								break;
							case 2:
								codevalList.add("周二");
								break;
							case 3:
								codevalList.add("周三");
								break;
							case 4:
								codevalList.add("周四");
								break;
							case 5:
								codevalList.add("周五");
								break;
							case 6:
								codevalList.add("周六");
								break;
							case 7:
								codevalList.add("周日");
								break;
							default:
								break;
							}
						}else{
							codevalList.add((String) payDateList.get(i).get("codeval")+"号");
						}
					}
				}
				payDateAdapter = new ArrayAdapter<String>(FixInvestSignActivity.this,android.R.layout.simple_spinner_item,codevalList);
				payDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spTraDate.setAdapter(payDateAdapter);
			}
			
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
		requestGetSecurityFactor("PB274");
	}
	
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						requestPsnGoldBonusFixInvestSignPre();
//						ActivityIntentTools.intentToActivity(FixInvestSignActivity.this, FixInvestSignDetailActivity.class);
					}
				});

	}
	/**
	 * 贵金属积利定投计划签约确认 接口
	 */
	public void requestPsnGoldBonusFixInvestSignPre(){
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		Map<String,Object> map = GoldbonusLocalData.getInstance().ProductInfoQueryList.get(0);
		requestParamMap.put("issueNo", (String)map.get("issueNo"));
		requestParamMap.put("issueName", (String)map.get("issueName"));
		requestParamMap.put("xpadIssueType", (String)map.get("issueType"));
		requestParamMap.put("issueKind", (String)map.get("issueKind"));
		requestParamMap.put("fixFreque", String.valueOf(spTermUnit.getSelectedItemPosition()));
		//TODO...扣款日期限查询 扣款日值 
		requestParamMap.put("fixPayDate", (String)(payDateList.get(spTraDate.getSelectedItemPosition()).get("codeval")));
		requestParamMap.put("fixValidMonth",(String)spEnd.getSelectedItem());//定投周期 
		requestParamMap.put("fixAmt", this.getIntent().getStringExtra("weight"));//暂取交易数量
		requestParamMap.put("_combinId", BaseDroidApp.getInstanse().getSecurityChoosed());
//		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusFixInvestSignPre", requestParamMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				GoldbonusLocalData.getInstance().FixInvestSignPreMap = result;
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
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		//关闭通讯框
		BiiHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, FixInvestSignDetailActivity.class);
		intent.putExtra("weight", this.getIntent().getStringExtra("weight"));
		intent.putExtra("fixFreque", String.valueOf(spTermUnit.getSelectedItemPosition()));
		//TODO...扣款日期限查询 扣款日值 
		intent.putExtra("fixPayDate", (String)(payDateList.get(spTraDate.getSelectedItemPosition()).get("codeval")));
		intent.putExtra("fixValidMonth",  (String)spEnd.getSelectedItem());
		intent.putExtra("termUnit", (String)spTermUnit.getSelectedItem());
		intent.putExtra("traDate", (String)spTraDate.getSelectedItem());
		intent.putExtra("end", (String)spEnd.getSelectedItem());
		intent.putExtra(Acc.RANDOMNUMBER, randomNumber);
		startActivityForResult(intent, 10001);	
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1001:
			finish();
			break;

		default:
			break;
		}
	}

}
