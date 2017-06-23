package com.chinamworld.bocmbci.biz.blpt.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.blpt.BillPaymentBaseActivity;
import com.chinamworld.bocmbci.biz.blpt.BlptUtil;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 已申请缴费服务
 * 
 * @author panwe
 * 
 */
public class BillHadAppliedActivity extends BillPaymentBaseActivity {
	/** 主布局 */
	private View viewContent;
	/** 竖向 List */
	private ListView verList;
	/** 横向显示的头信息 */
	private TextView tvCompany;
	private TextView tvTitleAcc;
	private TextView tvTitle;
	private SimpleAdapter mAdapter;
	/*** 横向显示文字 */
	private String payeeDispName;
	/*** 城市 */
	private String city;
	/** 手机号 */
	private List<Map<String, String>> mData;
	/** 日期 */
	private String strTitle;
	private View vLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		int maRight = this.getResources().getDimensionPixelSize(
				R.dimen.query_btn_width);
		int maTop = this.getResources().getDimensionPixelSize(
				R.dimen.common_row_margin);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		RelativeLayout.LayoutParams wiParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		wiParams.setMargins(maRight, maTop, 0, 0);
		tvTitle.setLayoutParams(wiParams);
		tvTitle.setText(this.getString(R.string.blpt_sign_billhadapplied));
		// 右上角按钮赋值
		setRightBtnClick(rightBtnClick);
		setText(this.getString(R.string.blpt_sign_btn_apply));
		// 添加布局
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_hadapplied_list, null);
		addView(viewContent);
		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		// 获取缴费项信息
		Map<String, String> msgMap = BlptUtil.getInstance().getMapData();
		getSignedInfo(msgMap.get(Blpt.KEY_PROVICESHORTNAME),
				msgMap.get(Blpt.KEY_MERCHID), msgMap.get(Blpt.KEY_PAYJNUM));
	}

	private void init() {

		tvTitle = (TextView) viewContent.findViewById(R.id.tvtitle_type);
		verList = (ListView) viewContent.findViewById(R.id.ver_hadapply);
		tvCompany = (TextView) viewContent.findViewById(R.id.tv_company);
//		tvDispname = (TextView) viewContent.findViewById(R.id.tv_dispname);
		tvTitleAcc = (TextView) viewContent.findViewById(R.id.tvtitle_acc);
		vLine = (View) viewContent.findViewById(R.id.view_line);
		// 读取临时数据
		Map<String, String> msgMap = BlptUtil.getInstance().getMapData();
		payeeDispName = msgMap.get(Blpt.KEY_PAYEENAME);
		city = msgMap.get(Blpt.KEY_CITY);

		if (!StringUtil.isNullOrEmpty(payeeDispName)) {
			tvCompany.setText(payeeDispName);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					tvCompany);
		}
		verList.setOnItemClickListener(verListclick);
		// 获取缴费项信息
		getSignedInfo(msgMap.get(Blpt.KEY_PROVICESHORTNAME),
				msgMap.get(Blpt.KEY_MERCHID), msgMap.get(Blpt.KEY_PAYJNUM));
	}

	/** 右上角按钮，跳往申请服务页面 */
	private OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(BillHadAppliedActivity.this,
					BillNewApplyActivity.class);
			startActivity(intent);
		}
	};

	/*** 竖向 列表点击事件 **/
	private OnItemClickListener verListclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent it = new Intent(BillHadAppliedActivity.this,
					BillHadAppliedInfoActivity.class);
			Map<String, String> map = mData.get(position);
			Bundle b = new Bundle();
			b.putString(Blpt.KEY_PAYEENAME, payeeDispName);
			b.putString(Blpt.KEY_CITY, city);
			b.putString(Comm.ACCOUNTNUMBER, map.get("payAcctNumber"));
			b.putString(Comm.ACCOUNT_ID, map.get("acctId"));
			b.putString("MOBILE", map.get("signNumber"));
			b.putString("PHONETITLE", strTitle);
			b.putInt(Blpt.KEY_TAG, 6);
			it.putExtra(Blpt.KEY_BUNDLE, b);
			BillHadAppliedActivity.this.startActivity(it);
		}
	};

	/**
	 * 获取已签约缴费详情
	 * 
	 * @param province
	 * @param merchantid
	 * @param jnum
	 */
	private void getSignedInfo(String province, String merchantid, String jnum) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_BILL_SING_INFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.BILL_SIGN_MERCHANID, merchantid);
		map.put(Blpt.BILL_SIGN_JNUM, jnum);
		map.put(Blpt.BILL_SIGN_PROCINCENAME, province);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "signInfoCallBack");
	}

	/** 已签约信息返回处理 */
	public void signInfoCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();

		initTitle(result);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) result
				.get(Blpt.BILL_SIGNAPPL_LIST);

		if (list != null && list.size() != 0) {
			mData = getData(list);
			if (StringUtil.isNullOrEmpty(mData)) {
				vLine.setVisibility(View.GONE);
				verList.setVisibility(View.GONE);
				BaseDroidApp.getInstanse()
						.showErrorDialog(
								getString(R.string.blpt_hadapplylist_error),
								R.string.cancle, R.string.blpt_sign_btn_apply,
								msgClick);
				return;
			}
			vLine.setVisibility(View.VISIBLE);
			verList.setVisibility(View.VISIBLE);
			mAdapter = new SimpleAdapter(this, mData,
					R.layout.blpt_hadapplied_item, new String[] { "acctNumber",
							"signNumber" }, new int[] {
							R.id.blpt_hadapplied_acc,
							R.id.blpt_hadapplied_number });
			verList.setAdapter(mAdapter);
		}
	}

	// 显示标题
	private void initTitle(Map<String, Object> result) {
		strTitle = (String) result.get(Blpt.BILLSIGNAPP_TITLE);
		tvTitle.setText(strTitle);
		if (strTitle.length() > 10) {
			tvTitle.setEllipsize(TruncateAt.MIDDLE);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					tvTitle);
		}
		tvTitleAcc.setText(this.getString(R.string.blpt_had_new_list_acct));
	}

	/**
	 * 初始化ListView数据
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getData(List<Map<String, Object>> list) {
		List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> accMap = (Map<String, Object>) list.get(i).get(
					Blpt.BILLSIGNAPPL_ACC);
			List<Object> signNumList = (List<Object>) list.get(i).get(
					Blpt.BILLSIIGNAPP_NAME);

			if (!StringUtil.isNullOrEmpty(accMap)
					&& !StringUtil.isNullOrEmpty(signNumList)) {
				String accNumber = (String) accMap
						.get(Blpt.SIGN_PAY_ACC_NUM_RE);
				String accId = (String) accMap.get(Blpt.SIGN_PA_ACC_ID_RE);
				for (int j = 0; j < signNumList.size(); j++) {
					Map<String, String> mMap = new HashMap<String, String>();
					mMap.put("acctNumber",
							StringUtil.getForSixForString(accNumber));
					mMap.put("payAcctNumber", accNumber);
					mMap.put("acctId", accId);
					mMap.put("signNumber", ((String) signNumList.get(j)).trim());
					mList.add(mMap);
				}
			}
		}
		return mList;
	}

	/** 提示框事件 */
	OnClickListener msgClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (Integer.parseInt(v.getTag() + "")) {
			case CustomDialog.TAG_SURE:
				// 确定
				Intent intent = new Intent();
				intent.setClass(BillHadAppliedActivity.this,
						BillNewApplyActivity.class);
				startActivity(intent);
				break;
			case CustomDialog.TAG_CANCLE:
				// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			}
		}
	};
}
