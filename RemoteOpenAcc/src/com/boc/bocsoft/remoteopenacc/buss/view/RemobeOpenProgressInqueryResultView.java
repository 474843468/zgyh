package com.boc.bocsoft.remoteopenacc.buss.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemobeApplayOnlineAccResultFragment;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemobeOpenAccNavigationFragment;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemobeOpenAuthenticationFragment;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemobeOpenProgressInqueryFragment;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemoteOpenAccActivity;
import com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress.QueryOpenAccountProgressResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress.QueryOpenAccountProgressResultModel;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;
import com.boc.bocsoft.remoteopenacc.common.util.BocroaUtils;

public class RemobeOpenProgressInqueryResultView extends LinearLayout {

	protected Context mContext;
	private View rootView;
	private ImageView iv_result_icon;
	private TextView tv_result_title;
	private LinearLayout ll_electrical_card_one;
	private LinearLayout ll_electrical_card_two;
	// private LinearLayout ll_notice;
	// private RelativeLayout rl_notice1;
	// private RelativeLayout rl_notice2;
	private TextView tv_electrical_card_one;
	private TextView tv_electrical_card_two;
	// private TextView tv_acc_type_one;
	// private TextView tv_acc_type_two;
	private TextView tv_acc_one_detail;
	private TextView tv_acc_two_detail;
	private TextView tv_result_tip;
	// private TextView tv_result_bottom1;
	// private TextView tv_result_bottom2;
	private Button btn_confirm;
	/** 申请状态 */
	// private String appliStatStr;
	// 进度详情
	// private String failReasonStr;
	// 电子卡号
	// private String cardNumStr;

	private RemobeOpenAccNavigationFragment navigationFragment;
	private RemobeOpenAuthenticationFragment authenticationFragment;
	private RemoteOpenAccActivity mAccActivity;
	private BaseFragment mBaseFragment;

	public RemobeOpenProgressInqueryResultView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public RemobeOpenProgressInqueryResultView(Context context,
			BaseFragment fragment) {
		super(context);
		mBaseFragment = fragment;
		mContext = context;
		initView();
	}

	public RemobeOpenProgressInqueryResultView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	// delete by lgw 2015.10.28 由于要使用SDK 2.3.3(Android 10) 编译
	// public RemobeOpenProgressInqueryResultView(Context context,
	// AttributeSet attrs, int defStyle) {
	// super(context, attrs);
	// super(context, attrs, defStyle);
	// mContext = context;
	// initView();
	// }

	private void initView() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.bocroa_view_remote_open_progress_result, this);
		iv_result_icon = (ImageView) rootView.findViewById(R.id.iv_result_icon);
		tv_result_title = (TextView) rootView
				.findViewById(R.id.tv_result_title);
		ll_electrical_card_one = (LinearLayout) rootView
				.findViewById(R.id.ll_electrical_card_one);
		ll_electrical_card_two = (LinearLayout) rootView
				.findViewById(R.id.ll_electrical_card_two);
		// rl_notice1 = (RelativeLayout) rootView.findViewById(R.id.rl_notice1);
		// rl_notice2 = (RelativeLayout) rootView.findViewById(R.id.rl_notice2);
		// ll_notice = (LinearLayout) rootView.findViewById(R.id.ll_notice);
		// tv_acc_type_one = (TextView) rootView
		// .findViewById(R.id.tv_acc_type_one);
		// tv_acc_type_two = (TextView) rootView
		// .findViewById(R.id.tv_acc_type_two);
		tv_acc_one_detail = (TextView) rootView
				.findViewById(R.id.tv_acc_one_detail);
		tv_acc_two_detail = (TextView) rootView
				.findViewById(R.id.tv_acc_two_detail);
		tv_electrical_card_one = (TextView) rootView
				.findViewById(R.id.tv_electrical_card_one);
		tv_electrical_card_two = (TextView) rootView
				.findViewById(R.id.tv_electrical_card_two);
		tv_result_tip = (TextView) rootView.findViewById(R.id.tv_result_tip);
		// tv_result_bottom1 = (TextView) rootView
		// .findViewById(R.id.tv_result_bottom1);
		// tv_result_bottom2 = (TextView) rootView
		// .findViewById(R.id.tv_result_bottom2);
		btn_confirm = (Button) rootView.findViewById(R.id.btn_confirm);
		// tv_result_bottom1.setText(Html.fromHtml(mContext.getResources()
		// .getString(R.string.bocroa_succ_notice1), imageGetter, null));
		// tv_result_bottom2.setText(Html.fromHtml(mContext.getResources()
		// .getString(R.string.bocroa_succ_notice2), imageGetter, null));
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (resultState == RESULTSUCC) {// 成功，立即登录手机银行
					mAccActivity.setResult(Activity.RESULT_OK);
					mAccActivity.finish();
				} else if (resultState == RESULTFALSE) {// 失败，重新申请
					if (authenticationFragment == null) {
						authenticationFragment = new RemobeOpenAuthenticationFragment();
					}
					mAccActivity.jump2Fragment(authenticationFragment, false);
				} else if (resultState == RESULTCHECKING) {// 审核中，返回
					// if (navigationFragment == null) {
					// navigationFragment = new
					// RemobeOpenAccNavigationFragment();
					// }
					// mAccActivity.jump2Fragment(navigationFragment);
					mAccActivity.goBack();
				} else if (resultState == RESULTRESEND) {// 重新发送
					mFragment.getResendOpenAccount(reUuid1, reUuid2);
				} else if (resultState == RESULTREAPPLY) {// 重新申请
					if (navigationFragment == null) {
						navigationFragment = new RemobeOpenAccNavigationFragment();
					}
					mAccActivity.jump2Fragment(navigationFragment, false);
				}
				// // 结果页面底部按钮跳转
				// if ("0".equals(appliStatStr) || "3".equals(appliStatStr)
				// || "5".equals(appliStatStr)) {
				// // 跳转到首页
				// if (navigationFragment == null) {
				// navigationFragment = new RemobeOpenAccNavigationFragment();
				// }
				// mAccActivity.jump2Fragment(navigationFragment);
				// } else if ("1".equals(appliStatStr) ||
				// "4".equals(appliStatStr)) {
				// // 跳转到首页
				// mAccActivity.setResult(Activity.RESULT_OK);
				// mAccActivity.finish();
				// // if (navigationFragment == null) {
				// // navigationFragment = new
				// // RemobeOpenAccNavigationFragment();
				// // }
				// // mAccActivity.jump2Fragment(navigationFragment);
				// } else if ("2".equals(appliStatStr)) {
				// // 跳转到身份验证
				// if (authenticationFragment == null) {
				// authenticationFragment = new
				// RemobeOpenAuthenticationFragment();
				// }
				// mAccActivity.jump2Fragment(authenticationFragment);
				// } else if ("6".equals(appliStatStr)) {// 如果是6请求重发接口
				// 4怎么处理？
				// if (mFragment != null) {
				// mFragment.getResendOpenAccount(reUuid1, reUuid2);
				// }
				// }
			}
		});
	}

	// private ImageGetter imageGetter = new ImageGetter() {
	//
	// @Override
	// public Drawable getDrawable(String source) {
	// return mContext.getResources().getDrawable(R.drawable.bocroa_arraw);
	// }
	// };
	private RemobeOpenProgressInqueryFragment mFragment;

	/**
	 * 待重发二（二、三同时）类账户开户申请uuid String(32) N reUuid1和reUuid2不能同时为空
	 */
	private String reUuid1 = "";
	/**
	 * 待重发三类账户开户申请uuid String(32) N
	 */
	private String reUuid2 = "";

	/**
	 * 加载数据
	 */
	public void initData(QueryOpenAccountProgressResponseModel mParams,
			RemoteOpenAccActivity mActivity,
			RemobeOpenProgressInqueryFragment fragment) {
		this.mAccActivity = mActivity;
		this.mFragment = fragment;
		// 判断二类、三类账户
		handPageInfo(mParams.list);
		// this.appliStatStr = mParams.appliStat;// 接口变化，注销
		// this.failReasonStr = mParams.failReason;
		// this.cardNumStr = mParams.vcardNo;
	}

	/**
	 * 根据数据展示页面信息。如果如果有两条数据，有一个成功就成功
	 * 
	 * @param progressResultList
	 */
	private void handPageInfo(
			List<QueryOpenAccountProgressResultModel> progressResultList) {
		if (progressResultList.size() == 1) {
			QueryOpenAccountProgressResultModel model = progressResultList
					.get(0);
			// openAccountType 开卡类型 String(2) Y 01开立二类账户
			// 02开立三类账户
			// 03同时开立二、三类账户
			if ("01".equalsIgnoreCase(model.openAccountType)) {
				reUuid1 = progressResultList.get(0).uuid;
			} else if ("02".equalsIgnoreCase(model.openAccountType)) {
				reUuid2 = progressResultList.get(0).uuid;
			} else if ("03".equalsIgnoreCase(model.openAccountType)) {
				reUuid1 = progressResultList.get(0).uuid;
			}
			judgeOneAppliState(model);
		} else if (progressResultList.size() == 2) {
			QueryOpenAccountProgressResultModel model1 = progressResultList
					.get(0);
			QueryOpenAccountProgressResultModel model2 = progressResultList
					.get(1);
			// model1
			if ("01".equalsIgnoreCase(model1.openAccountType)) {
				reUuid1 = progressResultList.get(0).uuid;
			} else if ("02".equalsIgnoreCase(model1.openAccountType)) {
				reUuid2 = progressResultList.get(0).uuid;
			} else if ("03".equalsIgnoreCase(model1.openAccountType)) {
				reUuid1 = progressResultList.get(0).uuid;
			}
			// model2
			if ("01".equalsIgnoreCase(model2.openAccountType)) {
				reUuid1 = progressResultList.get(0).uuid;
			} else if ("02".equalsIgnoreCase(model2.openAccountType)) {
				reUuid2 = progressResultList.get(0).uuid;
			} else if ("03".equalsIgnoreCase(model2.openAccountType)) {
				reUuid1 = progressResultList.get(0).uuid;
			}
			judgeTwoAppliState(model1, model2);
		}
	}

	private void judgeTwoAppliState(QueryOpenAccountProgressResultModel model1,
			QueryOpenAccountProgressResultModel model2) {
		boolean model1Succ = "01".equalsIgnoreCase(model1.msgStat)
				|| "02".equalsIgnoreCase(model1.msgStat)
				|| BocroaUtils.isEmpty(model1.msgStat);
		boolean model2Succ = "01".equalsIgnoreCase(model2.msgStat)
				|| "02".equalsIgnoreCase(model2.msgStat)
				|| BocroaUtils.isEmpty(model2.msgStat);
		if (model1Succ || model2Succ) {// 有一个成功则认为成功
			if ("01".equalsIgnoreCase(model1.bindVcardExStat)
					|| "01".equalsIgnoreCase(model1.bindVcardStat)
					|| "01".equalsIgnoreCase(model2.bindVcardExStat)
					|| "01".equalsIgnoreCase(model2.bindVcardStat)) {// 成功
				if ("02".equals(model1.bindVcardStat)
						|| "02".equals(model1.bindVcardExStat)
						|| "02".equals(model2.bindVcardStat)
						|| "02".equals(model2.bindVcardExStat)) {// 成功中的重发
					reSend(true, model1.failReason, RESULTRESEND);// TODO
				} else {
					showSuccView(RESULTSUCC, model1, model2);
				}
			} else {// 失败
				if ("01".equals(model1.creatVcardStat)
						|| "01".equals(model1.creatVcardExStat)
						|| "01".equals(model2.creatVcardStat)
						|| "01".equals(model2.creatVcardExStat)) {// 失败中的重发
					reSend(false, model1.failReason, RESULTRESEND);
				} else if ("02".equals(model1.queryRcpsStat)
						|| "02".equals(model1.creatCifStat)
						|| (!"01".equals(model1.bindVcardStat) && !"01"
								.equals(model1.bindVcardExStat))
						|| "02".equals(model2.queryRcpsStat)
						|| "02".equals(model2.creatCifStat)
						|| (!"01".equals(model2.bindVcardStat) && !"01"
								.equals(model2.bindVcardExStat))) {// 开户失败重新申请
					reApply(model1.failReason, RESULTREAPPLY);
				} else {
					showFailView(RESULTFALSE, model1, model2);
				}
			}
		} else {
			showCheckingView(RESULTCHECKING);
		}
	}

	private void reApply(String reason, int resultState) {// 重新申请
		this.resultState = resultState;
		// 审核中
		iv_result_icon.setImageResource(R.drawable.bocroa_progress_fail);
		tv_result_title.setText("开户失败");
		tv_result_tip.setPadding(33, 50, 22, 15);
		tv_result_tip.setText(reason);
		tv_result_tip.setVisibility(View.VISIBLE);
		ll_electrical_card_one.setVisibility(View.GONE);
		ll_electrical_card_two.setVisibility(View.GONE);
		// ll_notice.setVisibility(View.GONE);
		btn_confirm.setText("重新申请");

		// 开户失败，重新申请
		// RemobeApplayOnlineAccResultFragment reApplyFragment = new
		// RemobeApplayOnlineAccResultFragment() {
		// @Override
		// public boolean onBtnClick() {
		// if (navigationFragment == null) {
		// navigationFragment = new RemobeOpenAccNavigationFragment();
		// }
		// ((RemoteOpenAccActivity) getActivity()).jump2Fragment(
		// navigationFragment, false);
		// return true;
		// }
		//
		// @Override
		// public String getMainTitleText() {
		// return "开户失败";
		// }
		// };
		// Bundle reApply = new Bundle();
		// reApply.putInt("drawable", R.drawable.bocroa_progress_fail);
		// reApply.putString("title", "开户失败");
		// reApply.putString("notice", reason);
		// reApply.putString("btnName", "重新申请");
		// reApplyFragment.setArguments(reApply);
		// mBaseFragment.jumpToFragment(reApplyFragment, false);

	}

	/**
	 * 重新发送
	 * 
	 * @param isSucc
	 * @param reason
	 * @param resultState
	 */
	private void reSend(boolean isSucc, String reason, int resultState) {// 重新发送
		this.resultState = resultState;
		// 审核中
		iv_result_icon.setImageResource(R.drawable.bocroa_progress_fail);
		tv_result_title.setText("开户失败");
		tv_result_tip.setPadding(33, 50, 22, 15);
		tv_result_tip.setText(reason);
		tv_result_tip.setVisibility(View.VISIBLE);
		ll_electrical_card_one.setVisibility(View.GONE);
		ll_electrical_card_two.setVisibility(View.GONE);
		// ll_notice.setVisibility(View.GONE);
		btn_confirm.setText("重新发送");

		// 开户失败，重发,重发后还是走调度
		// RemobeApplayOnlineAccResultFragment resendFragment = new
		// RemobeApplayOnlineAccResultFragment() {
		// @Override
		// public boolean onBtnClick() {
		// mFragment.getResendOpenAccount(reUuid1, reUuid2);
		// return true;
		// }
		//
		// @Override
		// public String getMainTitleText() {
		// return "开户失败";
		// }
		// };
		// Bundle reSend = new Bundle();
		// reSend.putInt("drawable", R.drawable.bocroa_progress_fail);
		// reSend.putString("title", "开户失败");
		// reSend.putString("notice", reason);
		// reSend.putString("btnName", "重新发送");
		// resendFragment.setArguments(reSend);
		// mBaseFragment.jumpToFragment(resendFragment, false);

	}

	/**
	 * 是否成功
	 * 
	 * @param appliStat
	 * @return
	 */
	// private boolean isSucc(String appliStat) {
	// if ("1".equals(appliStat) || "3".equals(appliStat)
	// || "4".equals(appliStat) || "5".equals(appliStat)) {
	// return true;
	// }
	// return false;
	// }

	private int resultState;// 记录查询结果用于处理底部按钮点击事件
	private final int RESULTSUCC = 1;// 成功
	private final int RESULTFALSE = 2;// 失败
	private final int RESULTCHECKING = 3;// 审核中
	private final int RESULTRESEND = 4;// 重发
	private final int RESULTREAPPLY = 5;// 重新申请

	/**
	 * 根据不同申请状态处理
	 * 
	 * @param appliStatStr
	 */
	private void judgeOneAppliState(QueryOpenAccountProgressResultModel model) {
		if ("01".equalsIgnoreCase(model.msgStat)
				|| "02".equalsIgnoreCase(model.msgStat)
				|| BocroaUtils.isEmpty(model.msgStat)) {// 申请处理结束
			if ("01".equalsIgnoreCase(model.bindVcardExStat)
					|| "01".equalsIgnoreCase(model.bindVcardStat)) {// 成功
				// 成功后分为重发和成功
				if ("02".equals(model.bindVcardStat)
						|| "02".equals(model.bindVcardExStat)) {// 开户成功重发
					reSend(true, model.failReason, RESULTRESEND);// TODO
				} else {
					showSuccView(RESULTSUCC, model);
				}
			} else {// 失败
				// 失败分为重新申请、重新发送、失败
				if ("01".equals(model.creatVcardStat)
						|| "01".equals(model.creatVcardExStat)) {// 开户失败重发
					reSend(false, model.failReason, RESULTRESEND);
				} else if ("02".equals(model.queryRcpsStat)
						|| "02".equals(model.creatCifStat)
						|| (!"01".equals(model.bindVcardStat) && !"01"
								.equals(model.bindVcardExStat))) {// 开户失败重新申请
					reApply(model.failReason, RESULTREAPPLY);
				} else {
					showFailView(RESULTFALSE, model);
				}
			}
		} else {
			showCheckingView(RESULTCHECKING);
		}
		//
		// if ("0".equals(model.appliStat)) {
		// showCheckingView();
		// } else if (isSucc(model.appliStat)) {
		// showSuccView(model);
		//
		// } else if ("2".equals(model.appliStat)) {// 失败
		// showFailView(model);
		// } else if ("6".equals(model.appliStat)) {
		// // p601 appliStat新增一个状态6，标识为重发状态。如果重发跳转重发页面，lgw 15.12.10
		// iv_result_icon.setImageResource(R.drawable.bocroa_progress_fail);
		// tv_result_title.setText("审核未通过");
		// tv_result_tip.setPadding(33, 50, 22, 15);
		// tv_result_tip.setVisibility(View.VISIBLE);
		// tv_result_tip.setText(model.failReason);
		// ll_electrical_card_one.setVisibility(View.GONE);
		// ll_electrical_card_two.setVisibility(View.GONE);
		// tv_result_bottom1.setVisibility(View.GONE);
		// tv_result_bottom2.setVisibility(View.GONE);
		// btn_confirm.setText("重新发送");
		// }
	}

	/**
	 * 失败view
	 * 
	 * @param model
	 */
	private void showFailView(int resultState,
			QueryOpenAccountProgressResultModel... model) {
		this.resultState = resultState;
		iv_result_icon.setImageResource(R.drawable.bocroa_progress_fail);
		tv_result_title.setText("审核未通过");
		tv_result_tip.setPadding(33, 50, 22, 15);
		tv_result_tip.setVisibility(View.VISIBLE);
		// if (condition) {

		// } else {
		tv_result_tip.setText(model[0].failReason);// TODO 从接口取值还是显示设计图上的？？
		// }
		tv_result_tip.setVisibility(View.VISIBLE);
		ll_electrical_card_one.setVisibility(View.GONE);
		ll_electrical_card_two.setVisibility(View.GONE);
		// ll_notice.setVisibility(View.GONE);
		// tv_result_bottom1.setVisibility(View.GONE);
		// tv_result_bottom2.setVisibility(View.GONE);
		btn_confirm.setText("重新申请");
	}

	/**
	 * 显示成功view
	 * 
	 * @param model
	 */
	private void showSuccView(int resultState,
			QueryOpenAccountProgressResultModel... model) {
		this.resultState = resultState;
		QueryOpenAccountProgressResultModel model1Succ = null;
		QueryOpenAccountProgressResultModel model2Succ = null;
		if (model.length > 0) {
			model1Succ = model[0];// 返回一个结果
			if (model.length > 1) {
				model2Succ = model[1];// 返回两个结果
			}
		}
		iv_result_icon.setImageResource(R.drawable.bocroa_progress_success);
		tv_result_title.setText("开户成功");
		tv_result_tip.setVisibility(View.GONE);
		// ll_notice.setVisibility(View.VISIBLE);
		if (model.length == 1) {// 一个返回结果
			// 成功
			// 判断二类三类账户有几个
			if (!TextUtils.isEmpty(model1Succ.vcardNo)
					&& !TextUtils.isEmpty(model1Succ.vcardExNo)) {// 二类三类都有
				ll_electrical_card_one.setVisibility(View.VISIBLE);
				ll_electrical_card_two.setVisibility(View.VISIBLE);
				tv_electrical_card_one.setText(model1Succ.vcardNo);
				tv_electrical_card_two.setText(model1Succ.vcardExNo);
				setSuccDetailNotice(model1Succ);
			} else {
				if (!TextUtils.isEmpty(model1Succ.vcardNo)) {// 只有二类
					ll_electrical_card_one.setVisibility(View.VISIBLE);
					ll_electrical_card_two.setVisibility(View.GONE);
					tv_electrical_card_one.setText(model1Succ.vcardNo);
					setSuccDetailNotice(model1Succ);
				} else if (!TextUtils.isEmpty(model1Succ.vcardExNo)) {// 只有三类
					ll_electrical_card_one.setVisibility(View.GONE);
					ll_electrical_card_two.setVisibility(View.VISIBLE);
					tv_electrical_card_two.setText(model1Succ.vcardExNo);
					setSuccDetailNotice(model1Succ);
				}
			}
			// if ("".equals(model1Succ.failReason)) {
			// tv_result_tip.setVisibility(View.VISIBLE);
			// tv_result_tip.setPadding(33, 50, 22, 15);
			// tv_result_tip.setText(getResources().getString(
			// R.string.bocroa_progress_success_content));
			// } else {
			// tv_result_tip.setVisibility(View.VISIBLE);
			// tv_result_tip.setPadding(33, 50, 22, 15);
			// tv_result_bottom1.setVisibility(View.VISIBLE);
			// tv_result_bottom1.setText(getResources().getString(
			// R.string.bocroa_progress_success_content));
			// }
		} else if (model.length == 2) {// 两个返回结果
			// 成功账户类型view都显示
			ll_electrical_card_one.setVisibility(View.VISIBLE);
			ll_electrical_card_two.setVisibility(View.VISIBLE);
			// 判断model1是二类还是三类
			if ("01".equalsIgnoreCase(model1Succ.openAccountType)) {// 二类
				tv_electrical_card_one.setText(model1Succ.vcardNo);
			} else if ("02".equalsIgnoreCase(model1Succ.openAccountType)) {// 三类
				tv_electrical_card_two.setText(model1Succ.vcardExNo);
			}
			// 判断model2是二类还是三类
			if ("01".equalsIgnoreCase(model2Succ.openAccountType)) {// 二类
				tv_electrical_card_one.setText(model2Succ.vcardNo);
			} else if ("02".equalsIgnoreCase(model2Succ.openAccountType)) {// 三类
				tv_electrical_card_two.setText(model2Succ.vcardExNo);
			}
			setSuccDetailNotice(model1Succ);
			setSuccDetailNotice(model2Succ);
			// onTwoResult(model1Succ, model2Succ);
			// 开卡类型 String(2) Y 01开立二类账户 02开立三类账户 03同时开立二、三类账户
			// if ("01".equalsIgnoreCase(model2Succ.openAccountType)) {
			// tv_acc_type_two.setText("中银E财账户");
			// tv_acc_two_detail.setText(String.format(mContext.getResources()
			// .getString(R.string.bocroa_progress_succ_notice),
			// "中银E财账户", model2Succ.vcardNo, "开户网点", "网点号码"));
			// // 取值
			// } else if ("02".equalsIgnoreCase(model2Succ.openAccountType)) {
			// tv_acc_type_two.setText("中银E捷账户");
			// tv_acc_two_detail.setText(String.format(mContext.getResources()
			// .getString(R.string.bocroa_progress_succ_notice),
			// "中银E捷账户", model2Succ.vcardNo, "开户网点", "网点号码"));
			// } else if ("03".equalsIgnoreCase(model2Succ.openAccountType)) {
			//
			// }
		}
		// if ("1".equals(model.appliStat) || "4".equals(model.appliStat)) {
		btn_confirm.setText("立即登录手机银行");
		// } else {
		// btn_confirm.setText("完成");
		// }
	}

	/**
	 * 设置详细提示信息
	 * 
	 * @param model
	 * @param title
	 * @param detailNotice
	 */
	private void setSuccDetailNotice(QueryOpenAccountProgressResultModel model) {
		if ("01".equalsIgnoreCase(model.openAccountType)) {
			// tv_acc_type_one.setText("中银E财账户");
			tv_acc_one_detail.setText(String.format(mContext.getResources()
					.getString(R.string.bocroa_progress_succ_notice2),
					model.vcardNo, model.orgName));
		} else if ("02".equalsIgnoreCase(model.openAccountType)) {
			// tv_acc_type_two.setText("中银E捷账户");
			tv_acc_two_detail.setText(String.format(mContext.getResources()
					.getString(R.string.bocroa_progress_succ_notice3),
					model.vcardExNo, model.orgName));
		} else if ("03".equalsIgnoreCase(model.openAccountType)) {
			// tv_acc_type_one.setText("中银E财账户");
			// tv_acc_type_two.setText("中银E捷账户");
			tv_acc_one_detail.setText(String.format(mContext.getResources()
					.getString(R.string.bocroa_progress_succ_notice2),
					model.vcardNo, model.orgName));
			tv_acc_two_detail.setText(String.format(mContext.getResources()
					.getString(R.string.bocroa_progress_succ_notice3),
					model.vcardExNo, model.orgName));
		}

	}

	/**
	 * 审核中
	 */
	private void showCheckingView(int resultState) {
		this.resultState = resultState;
		// 审核中
		iv_result_icon.setImageResource(R.drawable.bocroa_progress_on);
		tv_result_title.setText("审核中...");
		tv_result_tip.setPadding(33, 50, 22, 15);
		tv_result_tip.setText(getResources().getString(
				R.string.bocroa_progress_on_tip));
		tv_result_tip.setVisibility(View.VISIBLE);
		ll_electrical_card_one.setVisibility(View.GONE);
		ll_electrical_card_two.setVisibility(View.GONE);
		// ll_notice.setVisibility(View.GONE);
		// tv_result_bottom2.setVisibility(View.GONE);
		btn_confirm.setText("返回");
	}

	/**
	 * 重发请求后执行的方法
	 * 
	 * @param isSucc
	 *            是否请求成功
	 * @param msg
	 *            失败的消息，成功传空
	 */
	public void onResend(boolean isSucc, String msg) {
		if (isSucc) {
			if (mBaseFragment != null) {
				mBaseFragment
						.jumpToFragment(new RemobeApplayOnlineAccResultFragment());
			}
		} else {
			mFragment.showErrorDialog(msg);
		}
	}
}
