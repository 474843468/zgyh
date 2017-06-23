package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.InvtEvaluationResultAdapter;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 答题结果页面
 * 
 * @author wangmengmeng
 * 
 */
public class InvtEvaluationResultActivity extends BociBaseActivity implements
		OnClickListener {
	private static final String TAG = "InvtEvaluationResultActivity";
	/** 输入信息页 */
	private View view;
	/** 评定结果视图 */
	private LinearLayout my_result;
	/** 评定等级 */
	private TextView riskLevel;
	/** 日期 */
	private TextView result_date;
	/** 有效期 */
	private TextView valid_date;
	/** 投资经验 */
	private TextView hasInvestExperience;
	/** 详情 */
	private TextView detail;
	/** 其它的等级详情 */
	private ListView bocinvt_resultListView;
	// 按钮
	/** 重新评估 */
	private Button newEvaluationBtn;
	/** 确定 */
	private Button sureBtn;
	/** 评估提交结果 */
	private Map<String, Object> evaluationMap;
	/** 展开三角 */
	private ImageView img_down;
	/** 等级ListView的List */
	private List<String> list = new ArrayList<String>();
	private int selectPosition = -1;
	/** 风险评估的类型 */
	private int riskType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		riskType = getIntent().getIntExtra(InvestConstant.RISKTYPE, -1); // xby
		// 为界面标题赋值
		setTitle(this.getString(R.string.boci_evaluation_title));
		// 右上角按钮赋值
		// setText(this.getString(R.string.close));
		// 添加布局
		view = addView(R.layout.bocinvt_evaluation_result);

		goneLeftView();
		gonerightBtn();
		// 界面初始化
		init();
	}

	/** 界面初始化 */
	private void init() {
		if (riskType == InvestConstant.FUNDRISK) {// 基金的风险评估页 没有分布条
			((LinearLayout) findViewById(R.id.stepbar))
					.setVisibility(View.GONE);
		}
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		evaluationMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_EVALUATION_RESULT);
//		606
		RelativeLayout titleBackground = (RelativeLayout) findViewById(R.id.main_layout);
//		设置标题背景颜色
		titleBackground.setBackgroundColor(getResources().getColor(R.color.fundp606_title));

		// 步骤条
		// StepTitleUtils.getInstance().initTitldStep(
		// this,
		// new String[] {
		// this.getResources().getString(
		// R.string.bocinvt_eva_step1),
		// this.getResources().getString(
		// R.string.bocinvt_eva_step2),
		// this.getResources().getString(
		// R.string.bocinvt_eva_step3) });
		// StepTitleUtils.getInstance().setTitleStep(3);
		LinearLayout ll_experience = (LinearLayout) view
				.findViewById(R.id.ll_experience);
		LinearLayout ll_validate = (LinearLayout) view
				.findViewById(R.id.ll_validate);

		my_result = (LinearLayout) view.findViewById(R.id.my_result);
		// 评定结果
		riskLevel = (TextView) view.findViewById(R.id.riskLevel);
		result_date = (TextView) view.findViewById(R.id.result_date);
		valid_date = (TextView) view.findViewById(R.id.valid_date);
		hasInvestExperience = (TextView) view
				.findViewById(R.id.hasInvestExperience);
		detail = (TextView) view.findViewById(R.id.detail);
		img_down = (ImageView) view.findViewById(R.id.img_down);
		img_down.setImageResource(R.drawable.img_arrow_gray_down606);
		// 其它等级详情
		bocinvt_resultListView = (ListView) view
				.findViewById(R.id.bocinvt_result);
		// 按钮
		newEvaluationBtn = (Button) view.findViewById(R.id.new_evaluation);
		sureBtn = (Button) view.findViewById(R.id.sure);
		newEvaluationBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		for (int i = 0; i < 5; i++) {
			if (String.valueOf(
					evaluationMap.get(BocInvt.BOCINVT_EVA_RISKLEVEL_RES))
					.equalsIgnoreCase(LocalData.riskLevelCodeList.get(i))) {

			} else {
				list.add(LocalData.riskLevelCodeList.get(i));
			}
		}
		riskLevel
				.setText(LocalData.myriskLevelMap.get(String
						.valueOf(evaluationMap
								.get(BocInvt.BOCINVT_EVA_RISKLEVEL_RES))));
		if (riskType == InvestConstant.FUNDRISK) {// xby
			result_date.setText(String.valueOf(evaluationMap
					.get(Inves.BOCINVT_EVA_EVALDATE_RES)));
			ll_experience.setVisibility(View.GONE);
			ll_validate.setVisibility(View.GONE);
			if (this.getIntent()
					.getBooleanExtra(ConstantGloble.ACC_ISMY, false)) {
				LayoutParams param = (LayoutParams) sureBtn.getLayoutParams();
				newEvaluationBtn.setLayoutParams(param);
			} else {
				sureBtn.setVisibility(View.GONE);
				// 右上角按钮赋值
				setText(this.getString(R.string.close));
				//		606 设置背景颜色
				btn_right.setBackgroundColor(getResources().getColor(R.color.fundp606_title));
				// 右上角按钮点击事件
				setRightBtnClick(rightBtnClick);
			}

		} else {
			LayoutParams param = (LayoutParams) sureBtn.getLayoutParams();
			newEvaluationBtn.setLayoutParams(param);
			result_date.setText(String.valueOf(evaluationMap
					.get(BocInvt.BOCINVT_EVA_EVALDATE_RES)));
			ll_experience.setVisibility(View.VISIBLE);
			ll_validate.setVisibility(View.VISIBLE);
			hasInvestExperience
					.setText(LocalData.hasInvestExperienceMap.get(String.valueOf(evaluationMap
							.get(BocInvt.BOCINVT_EVA_HASINVESTEXPERIENCE_RES))));
			valid_date.setText(String.valueOf(evaluationMap
					.get(BocInvt.BOCINVT_EVA_VALIDTHRUDATE_RES)));
		}

		detail.setText(LocalData.riskLevelDetailMap.get(String
				.valueOf(evaluationMap.get(BocInvt.BOCINVT_EVA_RISKLEVEL_RES))));
		if (detail.getVisibility() == View.GONE) {
			detail.setVisibility(View.VISIBLE);
			img_down.setImageResource(R.drawable.img_arrow_gray_up606);
		}
		// 评定结果展示框点击事件—展示详情
		RelativeLayout layout_content = (RelativeLayout) view
				.findViewById(R.id.layout_content);
		layout_content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (detail.getVisibility() == View.GONE) {
					detail.setVisibility(View.VISIBLE);
					img_down.setImageResource(R.drawable.img_arrow_gray_up606);
				} else if (detail.getVisibility() == View.VISIBLE) {
					detail.setVisibility(View.GONE);
					img_down.setImageResource(R.drawable.img_arrow_gray_down606);
				}
			}
		});
		my_result.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (detail.getVisibility() == View.GONE) {
					detail.setVisibility(View.VISIBLE);
					img_down.setImageResource(R.drawable.img_arrow_gray_up606);
				} else if (detail.getVisibility() == View.VISIBLE) {
					detail.setVisibility(View.GONE);
					img_down.setImageResource(R.drawable.img_arrow_gray_down606);
				}
			}
		});

		// 配置适配器
		final InvtEvaluationResultAdapter adapter = new InvtEvaluationResultAdapter(
				this, list);
		bocinvt_resultListView.setAdapter(adapter);
		bocinvt_resultListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (selectPosition == position) {
							selectPosition = -1;
							adapter.setSelectPosition(selectPosition);
							return;
						} else {
							selectPosition = position;
							adapter.setSelectPosition(position);
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_evaluation:
			Intent intent = new Intent(InvtEvaluationResultActivity.this,
					TestInvtEvaluationAnswerActivity.class);
			intent.putExtra(ConstantGloble.BOCINVT_ISNEWEVA, true);
			if (riskType == InvestConstant.FUNDRISK) {// 基金的风险评估页 没有分布条
				intent.putExtra(InvestConstant.RISKTYPE,
						InvestConstant.FUNDRISK);
			}
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
			break;
		case R.id.sure:
			// 确定
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭
			setResult(RESULT_CANCELED);
			finish();
		}
	};

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
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
