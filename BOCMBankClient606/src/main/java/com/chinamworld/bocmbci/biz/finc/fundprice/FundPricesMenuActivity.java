package com.chinamworld.bocmbci.biz.finc.fundprice;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincFollowActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金行情 三级菜单页面
 * 
 * @author xyl
 * 
 */
public class FundPricesMenuActivity extends FincBaseActivity {
	private static final String TAG = "FundPricesMenuActivity";
	/**
	 * 全部行情 右侧按钮
	 */
	private TextView allPricesImageBtn;

	private View allPricesRelativeLayout;
	private View foundAttentionRelativeLayout;
	/** 检查标记　 */
	private int flag;
	/** 全部行情 */
	private final int ALLPRICE = 1;
	/** 我关注的基金 */
	private final int MYATTENTION = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_prices_menu, null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_title_fundinvest_new);
		allPricesImageBtn = (TextView) childView
				.findViewById(R.id.finc_allprices_imagebtn);
		allPricesRelativeLayout = childView
				.findViewById(R.id.finc_allprices_imagebtn_RelativeLayout);
		foundAttentionRelativeLayout = childView
				.findViewById(R.id.finc_prices_attenttion);
		foundAttentionRelativeLayout.setOnClickListener(this);
		allPricesRelativeLayout.setOnClickListener(this);
		allPricesImageBtn.setOnClickListener(this);
		right.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fincControl.cleanAllData();
		setLeftSelectedPosition("finc_1");
	}

	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
			switch (flag) {
			case ALLPRICE:
				BaseHttpEngine.dissMissProgressDialog();
//				startActivity(new Intent(this, FundPricesActivity.class));
				startActivity(new Intent(this, FundPricesActivityNew.class));
				break;
			case MYATTENTION:
				attentionFundQuery();
				break;

			default:
				break;
			}

	}

	@Override
	public void attentionFundQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.attentionFundQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap
				.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST))) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		fincControl.attentionFundList = (List<Map<String, Object>>) resultMap
				.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST);
		startActivity(new Intent(this, MyFincFollowActivity.class));

	}

	@Override
	public void getAttentionedFundCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.getAttentionedFundCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		} else {
			fincControl.attentionFundList = resultList;
			startActivity(new Intent(this, MyFincFollowActivity.class));
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_allprices_imagebtn:// 全部行情
		case R.id.finc_allprices_imagebtn_RelativeLayout:
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(FundPricesMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FundPricesMenuActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = ALLPRICE;
//			BaseHttpEngine.showProgressDialog();
//			doCheckRequestPsnInvestmentManageIsOpen();
			startActivity(new Intent(this, FundPricesActivityNew.class));
			break;
		case R.id.finc_prices_attenttion:
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(FundPricesMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FundPricesMenuActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = MYATTENTION;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		default:
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifInvestMent = true;
				if (!fincControl.ifhaveaccId) {// 如果还没有基金账户 getPopup();
					getPopup();
				}
				break;

			default:
				fincControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifhaveaccId = true;
				switch (flag) {
				case ALLPRICE:
//					startActivity(new Intent(this, FundPricesActivity.class));
					startActivity(new Intent(this, FundPricesActivityNew.class));
					break;
				case MYATTENTION:
					BaseHttpEngine.showProgressDialog();
					attentionFundQuery();
					break;

				default:
					break;
				}
				break;

			default:
				fincControl.ifhaveaccId = false;
				getPopup();
				break;
			}
			break;
		case InvestConstant.FUNDRISK:// 基金风险评估
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifdorisk = true;
				switch (flag) {
				case ALLPRICE:
//					startActivity(new Intent(this, FundPricesActivity.class));
					startActivity(new Intent(this, FundPricesActivityNew.class));
					break;
				case MYATTENTION:
					BaseHttpEngine.showProgressDialog();
					attentionFundQuery();
					break;

				default:
					break;
				}
				break;
			default:
				fincControl.ifdorisk = false;
				getPopup();
				break;
			}
			break;

		default:
			break;
		}
	}
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		super.httpRequestCallBackPre(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_ACCCHECIN_ERROR)||biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_ACCNO_ERROR)||biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_ACCNO_ERROR_2)) {
				fincControl.ifhaveaccId = false;
				fincControl.ifdorisk = false;
				BaseHttpEngine.dissMissProgressDialog();
				getPopup();
				return true;
			}

		}
		return super.httpRequestCallBackPre(resultObj);

	}
}
