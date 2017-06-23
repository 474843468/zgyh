package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回答问题页面
 * 
 * @author wangmengmeng
 * 
 */
public class TestInvtEvaluationAnswerActivity extends BociBaseActivity
		implements OnGestureListener {
	private static final String TAG = "InvtEvaluationAnswerActivity";
	/** 回答问题页 */
	private View view;
	/** 问题页容器 */
	private ArrayList<View> list;
	private LayoutInflater inflater;
	/** 答案集合 */
	private Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
	/** 分数 */
	private int score;
	/** 风险评估的类型 add xby */
	private int riskType;
	private int i = 0;
	/** 上一步 */
	private Button btn_last;
	/** 下一步 */
	private Button btn_next;
	/** 提交 */
	private Button btnSubmit;
	Animation[] animations = new Animation[4];
//	GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		riskType = getIntent().getIntExtra(InvestConstant.RISKTYPE, -1);
		// 为界面标题赋值
		setTitle(this.getString(R.string.boci_evaluation_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.close));
		//		606 设置背景颜色
		btn_right.setBackgroundColor(getResources().getColor(R.color.fundp606_title));
		// 添加布局
		view = addView(R.layout.bocinvt_evaluation_answer);

		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		goneLeftView();
		// 界面初始化
//		detector = new GestureDetector(this);
		animations[0] = AnimationUtils.loadAnimation(this, R.anim.left_in);
		animations[1] = AnimationUtils.loadAnimation(this, R.anim.left_out);
		animations[2] = AnimationUtils.loadAnimation(this, R.anim.right_in);
		animations[3] = AnimationUtils.loadAnimation(this, R.anim.right_out);
		init();
		if (getIntent().getBooleanExtra(InvestConstant.MAINSTASRT, false)) {// 从投资理财服务转进来的页面
																			// xby
			goneLeftView();// 屏蔽左侧二级菜单
		}

	}


	/** 界面初始化 */
	private void init() {
		//		606
		RelativeLayout titleBackground = (RelativeLayout) findViewById(R.id.main_layout);
		//		设置标题背景颜色
		titleBackground.setBackgroundColor(getResources().getColor(R.color.fundp606_title));

		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_eva_step1),
						this.getResources().getString(
								R.string.bocinvt_eva_step2),
						this.getResources().getString(
								R.string.bocinvt_eva_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		if (riskType == InvestConstant.FUNDRISK) {// 基金的风险评估页 没有分布条
			((LinearLayout) findViewById(R.id.stepbar))
					.setVisibility(View.GONE);
		}
		// 添加引导页
		inflater = LayoutInflater.from(this);

		for (int i = 0; i < 10; i++) {
			resultMap.put(i, -100);
		}
		initPage();

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (TestInvtEvaluationAnswerActivity.this.getIntent()
					.getBooleanExtra(ConstantGloble.BOCINVT_ISNEWEVA, false)) {
				// 关闭—重新评估
				setResult(RESULT_OK);
				finish();
			} else {
				// 关闭-第一次评估
				BociDataCenter.getInstance().setI(1);
				setResult(RESULT_CANCELED);
				finish();
			}

		}
	};

	/**
	 * 初始化page容器
	 */
	private void initPage() {
		list = new ArrayList<View>();
		for (int i = 0; i < 10; i++) {
			list.add(inflater.inflate(
					R.layout.bocinvt_evaluation_answer_detail, null));
			View view = list.get(i);
			TextView bocinvt_eva_answer_title = (TextView) view
					.findViewById(R.id.bocinvt_eva_answer_title);
			bocinvt_eva_answer_title
					.setText(LocalData.questionTitleList.get(i));
			RadioGroup radioGroup_answer = (RadioGroup) view
					.findViewById(R.id.radioGroup_answer);
			RadioButton radioA = (RadioButton) view
					.findViewById(R.id.radio_answer0);
			radioA.setText(LocalData.answerList.get(i).get(0));
			RadioButton radioB = (RadioButton) view
					.findViewById(R.id.radio_answer1);
			radioB.setText(LocalData.answerList.get(i).get(1));
			RadioButton radioC = (RadioButton) view
					.findViewById(R.id.radio_answer2);
			radioC.setText(LocalData.answerList.get(i).get(2));
			RadioButton radioD = (RadioButton) view
					.findViewById(R.id.radio_answer3);
			TextView bocinvt_eva_answer = (TextView) view
					.findViewById(R.id.bocinvt_eva_answer);
//			if (i == 0) {
//
//			} else {
//				bocinvt_eva_answer
//						.setText(TestInvtEvaluationAnswerActivity.this
//								.getString(R.string.bocinvt_answer_title));
//			}
			if (i != 0) {
				bocinvt_eva_answer.setText("");
			}
			RadioButton radioE = (RadioButton) view
					.findViewById(R.id.radio_answer4);
			switch (i + 1) {
			case 2:
			case 5:
			case 10:
				// 有5个答案
				radioD.setVisibility(View.VISIBLE);
				radioE.setVisibility(View.VISIBLE);
				radioD.setText(LocalData.answerList.get(i).get(3));
				radioE.setText(LocalData.answerList.get(i).get(4));
				break;
			case 1:
			case 3:
			case 4:
			case 6:
			case 7:
			case 8:
				// 有4个答案
				radioD.setVisibility(View.VISIBLE);
				radioE.setVisibility(View.GONE);
				radioD.setText(LocalData.answerList.get(i).get(3));
				break;
			case 9:
				// 有3个答案
				radioD.setVisibility(View.GONE);
				radioE.setVisibility(View.GONE);
				break;
			}
			final int j = i;
			radioGroup_answer
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							switch (checkedId) {
							case R.id.radio_answer0:
								resultMap.put(j, LocalData.riskScoreList.get(j)
										.get(0));
								break;
							case R.id.radio_answer1:
								resultMap.put(j, LocalData.riskScoreList.get(j)
										.get(1));
								break;
							case R.id.radio_answer2:
								resultMap.put(j, LocalData.riskScoreList.get(j)
										.get(2));
								break;
							case R.id.radio_answer3:
								resultMap.put(j, LocalData.riskScoreList.get(j)
										.get(3));
								break;
							case R.id.radio_answer4:
								resultMap.put(j, LocalData.riskScoreList.get(j)
										.get(4));
								break;
							default:
								break;
							}

						}
					});
		}
		LinearLayout answerLayout = (LinearLayout)findViewById(R.id.answer_layout);
		for (int i = 0; i < list.size(); i++) {
			answerLayout.addView(list.get(i));
//			viewPager.addView(list.get(i));
		}
		// viewPager.setAdapter(new MyAdapter());

		btn_last = (Button) view.findViewById(R.id.btnLast);
		btn_next = (Button) view.findViewById(R.id.btnNext);
		btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
		btnSubmit.setVisibility(View.VISIBLE);
		btnshow();
		btn_last.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (i == 0) {
					if (riskType == InvestConstant.FUNDRISK) {// 基金的风险评估页 没有上一步
						v.setClickable(false);
					} else {
						BociDataCenter.getInstance().setI(0);
						setResult(RESULT_CANCELED);
						finish();
					}
				} else {
					// 为flipper设置切换的的动画效果
//					viewPager.setInAnimation(animations[2]);
//					viewPager.setOutAnimation(animations[3]);
//					viewPager.showPrevious();
					i--;
				}
				if (i != 9) {
//					btnSubmit.setVisibility(View.GONE);
					btn_next.setVisibility(View.GONE);
				}
			}
		});

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (i == 9) {
				// btnSubmit.setVisibility(View.VISIBLE);
				// btn_next.setVisibility(View.GONE);
				// }else{
				// 为flipper设置切换的的动画效果
				if (resultMap.get(i) == -100) {
					CustomDialog.toastInCenter(
							TestInvtEvaluationAnswerActivity.this,
							BaseDroidApp
									.getInstanse()
									.getCurrentAct()
									.getString(
											R.string.bocinvt_answer_error_new));
				} else {
					if (i == 8) {
						btnSubmit.setVisibility(View.VISIBLE);
						btn_next.setVisibility(View.GONE);
					}
//					viewPager.setInAnimation(animations[0]);
//					viewPager.setOutAnimation(animations[1]);
//					viewPager.showNext();
					i++;
				}
				// }
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < 10; i++) {
					if (resultMap.get(i) == -100) {
						CustomDialog.toastInCenter(
								TestInvtEvaluationAnswerActivity.this,
								BaseDroidApp
										.getInstanse()
										.getCurrentAct()
										.getString(
												R.string.bocinvt_answer_error_new));
						return;
					}
				}
				for (int k = 0; k < 10; k++) {
					score += resultMap.get(k);
				}
				// 回答完了所有问题
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
	}

	public void btnshow() {
		if (TestInvtEvaluationAnswerActivity.this.getIntent().getBooleanExtra(
				ConstantGloble.BOCINVT_ISNEWEVA, false)) {
			// 关闭—重新评估
			btn_last.setVisibility(View.GONE);
//			btnSubmit.setVisibility(View.INVISIBLE);
			btn_next.setVisibility(View.GONE);
		} else {
			if (riskType == InvestConstant.FUNDRISK) {
				btn_last.setVisibility(View.GONE);
//				btnSubmit.setVisibility(View.INVISIBLE);
				btn_next.setVisibility(View.GONE);
			} else {
				// 第一次评估
				if (i == 0) {
					btn_last.setText(this.getString(R.string.last));
					btn_last.setVisibility(View.VISIBLE);
					btn_last.setBackgroundResource(R.drawable.btn_red_big);
					btn_next.setVisibility(View.GONE);
//					btnSubmit.setVisibility(View.GONE);
					btn_last.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							BociDataCenter.getInstance().setI(0);
							setResult(RESULT_CANCELED);
							finish();
						}
					});
				}
			}

		}
		// if (i == 9) {
		// btnSubmit.setVisibility(View.VISIBLE);
		// btn_next.setVisibility(View.GONE);
		// }
		// if (i != 9) {
		// btnSubmit.setVisibility(View.GONE);
		// btn_next.setVisibility(View.GONE);
		// }
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	/**
	 * 获取tocken
	 */
	private void requestPSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNGetTokenIdCallback");
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
		} else {
			if (riskType == InvestConstant.FUNDRISK) {// 基金的风险评估
				requestPsnFundRiskEvaluationSubmitResult(tokenId);
			} else {// 投资理财的风险评估
				requestPsnInvtEvaluationResult(tokenId);
			}
		}

	}

	/** 请求基金风险评估提交 */
	public void requestPsnFundRiskEvaluationSubmitResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Inves.PSNFUNDRISKEVALUATIONSUBMITRESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		if (score < 0) {
			score = 0;
		}
		paramsmap.put(BocInvt.BOCINVT_EVA_RISKSCORE_REQ, String.valueOf(score));
		paramsmap.put(BocInvt.BOCINVT_EVA_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundRiskEvaluationSubmitResultCallback");
	}

	/** 请求基金风险评估提交—回调 */
	public void requestPsnFundRiskEvaluationSubmitResultCallback(
			Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap()
					.put(ConstantGloble.BOCINVT_EVALUATION_RESULT, resultMap);
			Intent intent = new Intent(TestInvtEvaluationAnswerActivity.this,
					InvtEvaluationResultActivity.class);
			if (riskType == InvestConstant.FUNDRISK) {
				intent.putExtra(InvestConstant.RISKTYPE,
						InvestConstant.FUNDRISK);
				intent.putExtra(ConstantGloble.ACC_ISMY, true);
			}
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
		}
	}

	/** 请求风险评估提交 */
	public void requestPsnInvtEvaluationResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PSNINVTEVALUATIONRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_REQUESTMAP);
		// 判断如果分数为负 则上送0
		if (score < 0) {
			score = 0;
		}
		paramsmap.put(BocInvt.BOCINVT_EVA_RISKSCORE_REQ, String.valueOf(score));
		paramsmap.put(BocInvt.BOCINVT_EVA_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvtEvaluationResultCallback");
	}

	/** 请求风险评估提交—回调 */
	public void requestPsnInvtEvaluationResultCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap()
					.put(ConstantGloble.BOCINVT_EVALUATION_RESULT, resultMap);
			Intent intent = new Intent(TestInvtEvaluationAnswerActivity.this,
					InvtEvaluationResultActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
		}
	}

	class MyAdapter extends PagerAdapter {
		/** 这个方法，是获取当前窗体界面数 **/
		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * 这个方法，在帮助文档中原文是could be implemented as return view == object,
		 * 
		 * 也就是用于判断是否由对象生成界面
		 **/
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

		/** 这个方法，是从ViewGroup中移出当前View **/
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {

			((ViewPager) arg0).removeView(list.get(arg1));
		}

		/**
		 * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象
		 * 
		 * 放在当前的ViewPager中
		 **/
		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager) arg0).addView(list.get(arg1));

			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {

			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	final int FLIP_DISTANCE = 50;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			break;
		}
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY) {
		// 右向左
		if (event1.getX() - event2.getX() > FLIP_DISTANCE) {
			if (i == 9) {
				return false;
			} else {
				// 为flipper设置切换的的动画效果
				if (resultMap.get(i) == -100) {
					CustomDialog.toastInCenter(
							TestInvtEvaluationAnswerActivity.this,
							BaseDroidApp
									.getInstanse()
									.getCurrentAct()
									.getString(
											R.string.bocinvt_answer_error_new));
				} else {
					btn_last.setVisibility(View.GONE);
					if (i == 8) {
//						btnSubmit.setVisibility(View.VISIBLE);
						btn_next.setVisibility(View.GONE);
					} else {
//						btnSubmit.setVisibility(View.INVISIBLE);
						btn_next.setVisibility(View.GONE);
					}
//					viewPager.setInAnimation(animations[0]);
//					viewPager.setOutAnimation(animations[1]);
//					viewPager.showNext();
					i++;
				}
				return true;
			}
		}
		// 左向右
		else if (event2.getX() - event1.getX() > FLIP_DISTANCE) {
			if (i == 0) {
				return false;
			} else {
				if (TestInvtEvaluationAnswerActivity.this
						.getIntent()
						.getBooleanExtra(ConstantGloble.BOCINVT_ISNEWEVA, false)) {
					// 关闭—重新评估
					if (i == 0) {
						btn_last.setVisibility(View.GONE);
//						btnSubmit.setVisibility(View.INVISIBLE);
						btn_next.setVisibility(View.GONE);
					} else {
						btn_last.setVisibility(View.GONE);
//						btnSubmit.setVisibility(View.INVISIBLE);
						btn_next.setVisibility(View.GONE);
					}

				} else {
					if (riskType == InvestConstant.FUNDRISK) {
						btn_last.setVisibility(View.GONE);
//						btnSubmit.setVisibility(View.INVISIBLE);
						btn_next.setVisibility(View.GONE);
					} else {
						// 第一次评估
						if (i == 1) {
							btn_last.setText(this.getString(R.string.last));
							btn_last.setVisibility(View.VISIBLE);
							btn_last.setBackgroundResource(R.drawable.btn_red_big);
							btn_next.setVisibility(View.GONE);
//							btnSubmit.setVisibility(View.GONE);
							btn_last.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									BociDataCenter.getInstance().setI(0);
									setResult(RESULT_CANCELED);
									finish();
								}
							});
						} else {
							btn_last.setVisibility(View.GONE);
//							btnSubmit.setVisibility(View.INVISIBLE);
							btn_next.setVisibility(View.GONE);
						}
					}
				}
				// 为flipper设置切换的的动画效果
//				viewPager.setInAnimation(animations[2]);
//				viewPager.setOutAnimation(animations[3]);
//				viewPager.showPrevious();
				i--;
				return true;
			}
		}
		return false;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		super.dispatchTouchEvent(ev);
//		detector.onTouchEvent(ev);
//		return true;
//	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
	}

	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent event) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		return false;
	}


}
