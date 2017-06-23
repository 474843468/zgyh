package com.chinamworld.bocmbci.biz.invest.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.biz.invest.InverstBaseActivity;
import com.chinamworld.bocmbci.biz.invest.adapter.CardAdapter;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 投资理财服务是否已开通页面(也是刚进入的初始页面)
 * 
 * @author xby
 * 
 *         2013-5-17
 */
public class InvesHasOpenActivity extends InverstBaseActivity {
	private boolean isOpen = false;
	private View tempView;

	/** 往适配器上传递的数据 */
	private ArrayList<ArrayList<String>> accountList = new ArrayList<ArrayList<String>>();

	/** 存放卡数据的集合 */
	private HashMap<String, ArrayList<String>> mAccounttMap = new HashMap<String, ArrayList<String>>();
	private ListView listview;

	CardAdapter adapter;
	private String tokenId;
	/** 是否做过投资理财风险评估 */
	private boolean isrequestInvtEvaluationed = false;
	/** 投资理财风险评估等级 */
	private int invtLevel = 0;
	/** 投资理财风险评估是否到期 */
	private boolean isrequestInvtEvaluationTimeOut = false;

	/** 是否做过基金风险评估 */
	private boolean isEvaluated = false;
	/** 基金风险评估等级 */
	private int evaLevel = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mian_menu12));
		
		// 右上角按钮点击事件
		setRightBtnClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭投资理财服务
				BaseDroidApp.getInstanse().showErrorDialog(
						InvesHasOpenActivity.this.getResources().getString(
								R.string.confirmcloseinvest), R.string.cancle,
						R.string.confirm, new OnClickListener() {

							@Override
							public void onClick(View view) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								switch (view.getId()) {
								case R.id.retry_btn:
									// 先发送ConversitionId的请求
									requestCommConversationId();BaseHttpEngine.showProgressDialog();
									break;

								}

							}
						});

			}
		});
		back.setVisibility(View.VISIBLE);
		// 请求是否开通投资理财服务的请求
		requestPsnInvestmentManageIsOpen();
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求TokinId
		requestPSNTokenId();
		// BaseDroidApp.getInstanse().getBizDataMap()
		// .put(ConstantGloble.CONVERSATION_ID, commConversationId);
	}

	/**
	 * 获取tocken
	 */
	private void requestPSNTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNTokenIdCallback");
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();

		// 再去发送关闭投资理财服务的请求
		requestPsnInvestmentManageCancel();

	}



	/**
	 * 关闭投资理财服务的请求
	 */
	private void requestPsnInvestmentManageCancel() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Inves.CLOSEINVESTMETHOD);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Comm.TOKEN_REQ, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentManageCancelCallBack");

	}

	/**
	 * 关闭投资理财服务的回调方法
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageCancelCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		// 关闭成功
//		CustomDialog.toastShow(this,
//				getResources().getString(R.string.closeinvestsucess));
//		InvesHasOpenActivity.this.finish();
		isOpen = false;

		 ModelBoc.updateOpenWealthStatus(isOpen);
		addView(R.layout.psninvestmentmanage_will_open);
		((Button) findViewById(R.id.btnConfim))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// 跳转到开通投资理财服务的页面
						Intent intent = new Intent(
								InvesHasOpenActivity.this,
								InvesAgreeFirstActivity.class);
						intent.putExtra(InvestConstant.FROMMYSELF, true);
						startActivityForResult(intent,
								InvestConstant.OPENINVEST);
					}
				});
	}

	/**
	 * 判断是否开通投资理财服务
	 */
	private void requestPsnInvestmentManageIsOpen() {
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentManageIsOpenCallback");
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		String isOpenOr = (String) biiResponseBody.getResult();
		if (!Boolean.valueOf((isOpenOr))) {// 未开通
			BaseHttpEngine.dissMissProgressDialog();
			isOpen = false;

			addView(R.layout.psninvestmentmanage_will_open);
			((Button) findViewById(R.id.btnConfim))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							// 跳转到开通投资理财服务的页面
							Intent intent = new Intent(
									InvesHasOpenActivity.this,
									InvesAgreeFirstActivity.class);
							intent.putExtra(InvestConstant.FROMMYSELF, true);
							startActivityForResult(intent,
									InvestConstant.OPENINVEST);
						}
					});

		} else {// 已开通 继续发送请求，获取投资理财列表
			BaseHttpEngine.dissMissProgressDialog();
			isOpen = true;
			addView(R.layout.psninvestmentmanage_has_open);
			initHasOpenView();
			// 判断是否进行过投资理财服务的风险评估
			// requestInvtEvaluation();

		}

	}

	/**
	 * 初始化开通成功的页面
	 */
	private void initHasOpenView() {
		// 关闭按钮
		Button btnCloseInvest = (Button) findViewById(R.id.btnConfim);
		btnCloseInvest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 关闭投资理财服务
				BaseDroidApp.getInstanse().showErrorDialog(
						InvesHasOpenActivity.this.getResources().getString(
								R.string.confirmcloseinvest), R.string.cancle,
						R.string.confirm, new OnClickListener() {

							@Override
							public void onClick(View view) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								switch (view.getId()) {
								case R.id.retry_btn:
									// 先发送ConversitionId的请求
									requestCommConversationId();BaseHttpEngine.showProgressDialog();
									break;

								}

							}
						});
			}
		});
		btn_right.setVisibility(View.GONE);

	}

	/**
	 * 请求投资理财服务列表
	 * 
	 * @param invtType
	 *            投资服务的类型
	 */
	private void requestInvestmentAccount(String invtType) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Inves.QUERYINVTBINDINGINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Inves.INVTTYPE, invtType);
		HttpManager.requestBii(biiRequestBody, this,
				"requestInvestmentAccountCallback", invtType);

	}

	/**
	 * 请求投资理财服务列表回调
	 * 
	 * @param resultObj
	 */
	public void requestInvestmentAccountCallback(Object resultObj,
			String invtType) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		ArrayList<String> list = new ArrayList<String>();
		list.add((String) resultMap.get(Inves.ACCOUNTID));
		list.add((String) resultMap.get(Inves.ACCOUNTTYPE));
		list.add((String) resultMap.get(Inves.INVESTACCOUNT));
		list.add((String) resultMap.get(Inves.ACCOUNT));
		list.add((String) resultMap.get(Inves.ACCOUNTNICKNAME));
		// if(Inves.WAIHUI.equals(invtType)){
		// accountList.add(0, list);
		// }else if(Inves.GUIJINSHU.equals(invtType)){
		//
		// }else if(Inves.JIJIN.equals(invtType)){
		//
		// }else if(Inves.ZHONGYINLICAIJIHUA.equals(invtType)){
		//
		// }
		mAccounttMap.put(invtType, list);
		// 得到result
		Inves.REQUET_ACCOUNT--;
		if (Inves.REQUET_ACCOUNT <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			// 将数据放到集合
			accountList.add(mAccounttMap.get(InvestConstant.WAIHUI));
			accountList.add(mAccounttMap.get(InvestConstant.GUIJINSHU));
			accountList.add(mAccounttMap.get(InvestConstant.JIJIN));
			accountList
					.add(mAccounttMap.get(InvestConstant.ZHONGYINLICAIJIHUA));
			listview = (ListView) findViewById(R.id.listview);
			adapter = new CardAdapter(accountList, this,
					isrequestInvtEvaluationed, isEvaluated, invtLevel, evaLevel);
			listview.setAdapter(adapter);
		}

	}

	/**
	 * 启动的页面结束时的回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:// 代表开通成功
			switch (requestCode) {
			case InvestConstant.OPENINVEST:// 开通投资理财服务成功
				// btn_right.setVisibility(View.VISIBLE);
				// // 添加布局
				// addView(R.layout.psninvestmentmanage_hasopen);
				// // 判断是否进行过投资理财服务的风险评估
				// requestInvtEvaluation();
				// finish();
				isOpen = true;
				ModelBoc.updateOpenWealthStatus(isOpen);
				addView(R.layout.psninvestmentmanage_has_open);
				initHasOpenView();
				break;
			case InvestConstant.INVTRISK:// 投资理财服务风险评估完成
				Map<String, Object> evaluationMap = (Map<String, Object>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BOCINVT_EVALUATION_RESULT);
				invtLevel = Integer.valueOf((String) evaluationMap
						.get(BocInvt.BOCINVT_EVA_RISKLEVEL_RES));
				LogGloble.d("info", "invtlevel-----------" + invtLevel);
				isrequestInvtEvaluationed = true;
				adapter.changeDate(accountList, this,
						isrequestInvtEvaluationed, isEvaluated, invtLevel,
						evaLevel);
				break;
			case InvestConstant.FUNDRISK:// 基金风险评估完成

				break;
			}
			break;
		case RESULT_CANCELED:
			switch (requestCode) {
			case InvestConstant.OPENINVEST:// 开通投资理财服务失败
				btn_right.setVisibility(View.GONE);
				addView(R.layout.psninvestmentmanage_will_open);
				((Button) findViewById(R.id.btnConfim))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								// 跳转到开通投资理财服务的页面
								Intent intent = new Intent(
										InvesHasOpenActivity.this,
										InvesAgreeFirstActivity.class);
								intent.putExtra(InvestConstant.FROMMYSELF, true);
								startActivityForResult(intent,
										InvestConstant.OPENINVEST);
								overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
							}
						});
				break;

			default:
				break;
			}
			break;

		}
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 *            新加入的View
	 * @param leftOrRight
	 *            向左还是向右滑动 0 代表向左 1代表向右
	 * @return 引入布局
	 */
	public View addView(int resource, int leftOrRight) {
		final View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
//		Animation animation_left_out = AnimationUtils.loadAnimation(this,
//				R.anim.push_left_out);
//		Animation animation_right_in = AnimationUtils.loadAnimation(this,
//				R.anim.push_right_in);
//
//		Animation animation_left_in = AnimationUtils.loadAnimation(this,
//				R.anim.push_left_in);
//		Animation animation_right_out = AnimationUtils.loadAnimation(this,
//				R.anim.push_right_out);
//
//		AnimationListener animationListener = new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				tabcontentTwo.removeAllViews();
//				tabcontentTwo.setVisibility(View.GONE);
//				tempView = view;
//			}
//		};
//		animation_left_out.setAnimationListener(animationListener);
//		animation_right_in.setAnimationListener(animationListener);
//		animation_left_in.setAnimationListener(animationListener);
//		switch (leftOrRight) {
//		case 0:// 向左
//			tabcontent.removeAllViews();
//			tabcontent.addView(view);
//			tabcontent.startAnimation(animation_right_in);
//			tabcontentTwo.removeAllViews();
//			tabcontentTwo.addView(tempView);
//			tabcontentTwo.setVisibility(View.VISIBLE);
//			tabcontentTwo.startAnimation(animation_left_out);
//
//			break;
//
//		case 1:// 向右
//			tabcontent.removeAllViews();
//			tabcontent.addView(view);
//			tabcontent.startAnimation(animation_left_in);
//			tabcontentTwo.removeAllViews();
//			tabcontentTwo.addView(tempView);
//			tabcontentTwo.setVisibility(View.VISIBLE);
//			tabcontentTwo.startAnimation(animation_right_out);
//
//			break;
//		}

		return view;
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {

		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		tempView = view;
		return view;
	}

}
