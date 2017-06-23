package com.chinamworld.bocmbci.biz.thridmanage.platforacct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.AccoutType;
import com.chinamworld.bocmbci.biz.thridmanage.ServiceType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.adapter.AccOpenCardAdapter;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;

/**
 * 台账查询主页
 * 
 * @author panwe
 * 
 */
public class PlatforAcctActivity extends ThirdManagerBaseActivity implements OnClickListener {

	/** 主布局 */
	private View viewContent;
	/** 卡列表 */
	private ListView lvAccCard;
	/** 确定按钮 */
	private Button btnOk;
	/** 列表选中条目 */
	private int selectposition = -1;
	private AccOpenCardAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_openacc_cardlist, null);
		addView(viewContent);
		setTitle(this.getString(R.string.third_platfor));
		btnRight.setVisibility(View.GONE);

		findView();
		setLeftSelectedPosition("thirdMananger_3");
		BiiHttpEngine.showProgressDialogCanGoBack();
		checkServiceState(ServiceType.InvestmentService);
	}

	@Override
	public void serviceOpenState() {
		super.serviceOpenState();
		// 获取卡列表
		List<String> paramslist = new ArrayList<String>();
//		paramslist.add(Third.accountTypeList.get(3));
		paramslist.add(AccoutType.CHANGCHENG_DIANZI_CARD_CODE);
//		BaseHttpEngine.showProgressDialogCanGoBack();
		getAllBankList(paramslist/*, true*/);
	}

	private void findView() {
		TextView tvTip = (TextView) viewContent.findViewById(R.id.text_tip);
		tvTip.setText(this.getString(R.string.third_platfor_card_tip));
		btnOk = (Button) viewContent.findViewById(R.id.btnconfirm);
		btnOk.setOnClickListener(this);
		lvAccCard = (ListView) viewContent.findViewById(R.id.cardlist);
		lvAccCard.setOnItemClickListener(cardItemClick);
	}

	@Override
	public void onClick(View v) {
		if (selectposition > -1) {
			Intent it = new Intent();
			it.putExtra("position", selectposition);
			it.putExtra(
					"accNum",
					(String) ThirdDataCenter.getInstance().getBankAccountList().get(selectposition)
							.get(Comm.ACCOUNTNUMBER));
			it.setClass(this, PlatforAcctListActivity.class);
			startActivity(it);
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_please_select_query_acct));
		}
	}

	/** 列表点击事件 **/
	private OnItemClickListener cardItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (selectposition == position) {
				return;
			} else {
				selectposition = position;
				mAdapter.setSelectedPosition(selectposition);
			}
		}
	};

	/** 获取账户列表返回 */
	@Override
	public void allBankAccListCallBack(Object resultObj) {
		super.allBankAccListCallBack(resultObj);
		List<Map<String, Object>> bankAccountList = ThirdDataCenter.getInstance().getBankAccountList();
		if (bankAccountList == null || bankAccountList.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(this.getString(R.string.third_common_error), errorClick);
			return;
		}
		mAdapter = new AccOpenCardAdapter(PlatforAcctActivity.this, bankAccountList);
		lvAccCard.setAdapter(mAdapter);
	}
}
