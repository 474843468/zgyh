package com.chinamworld.bocmbci.biz.safety.safetyproduct.riskAssessment;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 风险评估问题控件
 * 
 * @author Zhi
 */
public class QuestionView extends LinearLayout {
	private static final String TAG = "QuestionView";
	/** 上下文 */
	private Context context;
	/** 问题 */
	private TextView tvQuestion;
	/** 答案 */
	private RadioGroup rgAnswers;
	/** 放答案的容器 */
	private LinearLayout llQuestion;
	/** 选择项 */
	private char selectAnswer = '0';

	public QuestionView(Context context) {
		this(context, null);
	}

	public QuestionView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuestionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.safety_riskassessmentdetail_question, this, true);
		tvQuestion = (TextView) findViewById(R.id.tv_question);
		llQuestion = (LinearLayout) findViewById(R.id.ll_question);
	}

	/**
	 * 设置问题
	 * 
	 * @param questionStr
	 *            问题字符串
	 */
	public void setQuestionStr(String questionStr) {
		tvQuestion.setText(questionStr);
	}

	/**
	 * 设置答案
	 * 
	 * @param answers
	 *            答案列表
	 */
	public void setAnswers(List<String> answers) {
		if (answers == null || answers.size() == 0) {
			return;
		}

		rgAnswers  = (RadioGroup) LinearLayout.inflate(context, R.layout.safety_riskassessmentdetail_answer_item, null);
		
		if (selectAnswer != '0') {
			LogGloble.i(TAG, "selectAnswer:" + selectAnswer);
			for (int i = 0; i < answers.size(); i++) {
				RadioButton rb = (RadioButton) rgAnswers.getChildAt(i);
				rb.setVisibility(View.VISIBLE);
				rb.setText("\r" + answers.get(i));
				int selectAnswerInt = selectAnswer;
				LogGloble.i(TAG, "i + 'A' = " + (i + 'A'));
				LogGloble.i(TAG, "selectAnswerInt = " + selectAnswerInt);
				if (selectAnswerInt == (i + 'A')) {
					rb.setChecked(true);
				}
				rb.setEnabled(false);
			}
		} else {
			for (int i = 0; i < answers.size(); i++) {
				RadioButton rb = (RadioButton) rgAnswers.getChildAt(i);
				rb.setVisibility(View.VISIBLE);
				rb.setEnabled(true);
				rb.setText("\r" + answers.get(i));
				rb.setTag(i + 'A');
				rb.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int selectAnswerInt = (Integer) v.getTag();
						selectAnswer = (char) selectAnswerInt;
					}
				});
			}
		}
		llQuestion.addView(rgAnswers);
	}
	
	/** 获取选择的答案*/
	public char getSelectAnswer() {
		return selectAnswer;
	}
	
	public TextView getTvQuestion() {
		return tvQuestion;
	}
	
	public void setSelectAnswer(char selectAnswer) {
		this.selectAnswer = selectAnswer;
	}
}
