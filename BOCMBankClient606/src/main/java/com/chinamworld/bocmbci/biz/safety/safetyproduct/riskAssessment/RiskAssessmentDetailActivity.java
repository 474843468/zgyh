package com.chinamworld.bocmbci.biz.safety.safetyproduct.riskAssessment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;

/**
 * 保险风险评估题目页面
 * 
 * @author Zhi
 */
public class RiskAssessmentDetailActivity extends RiskAssessmentBaseActivity {

	/** 主显示布局 */
	private View mMainView;
	/** 问题列表 */
	private List<Map<String, Object>> listQuestions = new ArrayList<Map<String,Object>>();
	/** 问题视图索引列表 */
	private List<QuestionView> listQV = new ArrayList<QuestionView>();
	/** 页面状态 0-用户风评页 1-上次风评页 */
	private int pageState;
	/** 下一步按钮 */
	private TextView btnNext;
	/** 取消按钮 */
	private TextView btnCancle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_riskassessmentdetail_act);
		setTitle(R.string.boci_evaluation_title);
		pageState = getIntent().getIntExtra(SafetyConstant.RISKFLAG, 0);
		findView();
		viewSet();
	}
	
	private void findView() {
		btnNext = (TextView) mMainView.findViewById(R.id.btnConfirm);
		btnCancle = (TextView) mMainView.findViewById(R.id.btnQuit);
	}
	
	/** 视图设置 */
	private void viewSet() {
		if (pageState == 0) {
			btnNext.setText(getResources().getString(R.string.submit));
			btnCancle.setText(getResources().getString(R.string.quit));
			btnNext.setOnClickListener(nextListener);
			btnCancle.setOnClickListener(quitListener);
		} else {
			btnNext.setText(getResources().getString(R.string.safety_riskAssessment_continueBtn));
			btnCancle.setText(getResources().getString(R.string.back));
			btnNext.setOnClickListener(evaluationAgainListener);
			btnCancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		getQuestions();
		addListItem();
	}

	/** 从文件中读取问题列表 */
	@SuppressWarnings("unchecked")
	private void getQuestions() {
		JSONArray ja = JSONArray.parseArray(Utils.readAssetsFile(this, "safetyQuestions.txt"));
		for (int i = 0; i < ja.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("question", ja.getJSONObject(i).get("question"));
			map.put("answer", (List<String>) ja.getJSONObject(i).get("answer"));
			listQuestions.add(map);
		}
	}
	
	/** 为视图添加问题视图 */
	@SuppressWarnings("unchecked")
	private void addListItem() {
		String allOptions;
		if (pageState == 1) {
			allOptions = (String) SafetyDataCenter.getInstance().getMapInsuranceRiskEvaluationQuery().get(Safety.ALLOPTIONS);

			for (int i = 0; i < listQuestions.size(); i++) {
				QuestionView qv = new QuestionView(this);
				qv.setSelectAnswer(allOptions.charAt(i));
				Map<String, Object> map = getQuestionsItem(i);
				qv.setQuestionStr((String) map.get("question"));
				qv.setAnswers((List<String>) map.get("answer"));
				listQV.add(qv);
				((LinearLayout) mMainView.findViewById(R.id.ll_questions)).addView(qv);
			}
		} else {
			for (int i = 0; i < listQuestions.size(); i++) {
				QuestionView qv = new QuestionView(this);
				Map<String, Object> map = getQuestionsItem(i);
				qv.setQuestionStr((String) map.get("question"));
				qv.setAnswers((List<String>) map.get("answer"));
				listQV.add(qv);
				((LinearLayout) mMainView.findViewById(R.id.ll_questions)).addView(qv);
			}
		}
	}
	
	/** 获取问题列表项 */
	private Map<String, Object> getQuestionsItem(int position) {
		return listQuestions.get(position);
	}
	
	/** 获取列表选择结果，直接打包成上送参数中的列表 */
	private String getSelectResult() {
		String str = "";
		for (int i = 0; i < listQV.size(); i++) {
			char selectAnswer = listQV.get(i).getSelectAnswer();
			if (selectAnswer == '0') {
				requestFocus(listQV.get(i).getTvQuestion());
				BaseDroidApp.getInstanse().showInfoMessageDialog("请您选择个人客户风险评估问卷所有问题的答案");
				return null;
			}
			str += selectAnswer;
		}
		return str;
	}

	private void requestFocus(View v) {
		 v.setFocusable(true);
         v.setFocusableInTouchMode(true);
         v.requestFocus();
         v.requestFocusFromTouch();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 6) {
			if (resultCode == 1) {
				pageState = 0;
				listQV.clear();
				((LinearLayout) mMainView.findViewById(R.id.ll_questions)).removeAllViews();
				listQuestions.clear();
				viewSet();
			} else if (resultCode == 4) {
				setResult(4);
				finish();
			}
		}
	}
	
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}
	
	/** 下一步监听 */
	private OnClickListener nextListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (StringUtil.isNull(getSelectResult())) {
//				BaseDroidApp.getInstanse().showInfoMessageDialog("您有未选择的题目");
				return;
			}
			BaseHttpEngine.showProgressDialog();
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
	};
	
	/** 重新测事件，将本页置为风评页 */
	private OnClickListener evaluationAgainListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			pageState = 0;
			((LinearLayout) mMainView.findViewById(R.id.ll_questions)).removeAllViews();
			listQV.clear();
			listQuestions.clear();
			viewSet();
		}
	};
	
	/** 退出监听 */
	private OnClickListener quitListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};
	
	/** 请求token回调 */
	@SuppressWarnings("unchecked")
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.APPL_IDTYPE, SafetyDataCenter.IDENTITYTYPE_credType.get(logInfo.get(Comm.IDENTITYTYPE)));
		params.put(Safety.APPL_IDNO, logInfo.get(Comm.IDENTITYNUMBER));
		params.put(Safety.APPL_NAME, logInfo.get(Inves.CUSTOMERNAME));
		params.put(Safety.INSURANCE_ID, getIntent().getStringExtra(Safety.INSURANCE_ID));
		params.put(Safety.RISKCODE, getIntent().getStringExtra(Safety.RISKCODE));
		params.put(Safety.ALLOPTIONS, getSelectResult());
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		getHttpTools().requestHttp(Safety.PSNINSURANCERISKEVALUATIONSUBMIT, "requestPsnInsuranceRiskEvaluationSubmitCallBack", params, true);
	}
	
	/** 风评提交回调 */
	public void requestPsnInsuranceRiskEvaluationSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		SafetyDataCenter.getInstance().setMapInsuranceRiskEvaluationQuery(resultMap);
		
		Intent intent = new Intent();
		
//		if ("0".equals(resultMap.get(Safety.MACHFLAG))) {
//			// 客户类型与产品风险等级不匹配
//			intent.setClass(this, RiskAssessmentActivity.class);
//			intent.putExtra(SafetyConstant.RISKFLAG, "2");
//			intent.putExtra(Safety.INSURANCE_ID, getIntent().getStringExtra(Safety.INSURANCE_ID));
//			intent.putExtra(Safety.RISKCODE, getIntent().getStringExtra(Safety.RISKCODE));
//			startActivity(intent);
//			finish();
//			return;
//		}
		
		intent.setClass(this, RiskAssessmentResultActivity.class).putExtra(Safety.MACHFLAG, (String) resultMap.get(Safety.MACHFLAG));
		startActivityForResult(intent, 6);
	}
}
