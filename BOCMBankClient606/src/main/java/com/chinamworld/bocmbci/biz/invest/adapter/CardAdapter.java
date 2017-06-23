package com.chinamworld.bocmbci.biz.invest.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.InvtEvaluationInputActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.TestInvtEvaluationAnswerActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.LocalData;

/**
 * 账户信息的适配器
 * 
 * @author xby
 * 
 */
public class CardAdapter extends BaseAdapter {

	/** 数据源 */
	private ArrayList<ArrayList<String>> dataList;
	private Activity context;
	/** 是否做过投资理财风险评估 */
	private boolean isrequestInvtEvaluationed = false;
	/** 是否做过基金风险评估 */
	private boolean isEvaluated = false;
	/** 投资理财风险评估等级 */
	private int invtLevel = 1;
	/** 基金风险评估等级 */
	private int evaLevel = 1;

	public CardAdapter(ArrayList<ArrayList<String>> dataList, Activity context,
			boolean isrequestInvtEvaluationed, boolean isEvaluated,
			int invtLevel, int evaLevel) {
		if (dataList != null) {
			this.dataList = dataList;
		} else {
			this.dataList = new ArrayList<ArrayList<String>>();
		}
		this.context = context;
		this.isrequestInvtEvaluationed = isrequestInvtEvaluationed;
		this.isEvaluated = isEvaluated;
		this.invtLevel = invtLevel;
		this.evaLevel = evaLevel;
	}

	public void changeDate(ArrayList<ArrayList<String>> dataList,
			Activity context, boolean isrequestInvtEvaluationed,
			boolean isEvaluated, int invtLevel, int evaLevel) {
		if (dataList != null) {
			this.dataList = dataList;
		} else {
			this.dataList = new ArrayList<ArrayList<String>>();
		}
		this.context = context;
		this.isrequestInvtEvaluationed = isrequestInvtEvaluationed;
		this.isEvaluated = isEvaluated;
		this.invtLevel = invtLevel;
		this.evaLevel = evaLevel;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.invest_item, null);

		}
		TextView tv1 = (TextView) convertView.findViewById(R.id.tv1);
		TextView tv3 = (TextView) convertView.findViewById(R.id.tv3);
		Button btn_invest = (Button) convertView.findViewById(R.id.btn_invest);

		btn_invest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switch (position) {
				case 2:// 基金

					if (!isEvaluated) {
						// 进行风险评估
						Intent intent = new Intent(BaseDroidApp.getInstanse()
								.getCurrentAct(),
								TestInvtEvaluationAnswerActivity.class);
						intent.putExtra(InvestConstant.RISKTYPE,
								InvestConstant.FUNDRISK);
						intent.putExtra(InvestConstant.MAINSTASRT, true);
						BaseDroidApp
								.getInstanse()
								.getCurrentAct()
								.startActivityForResult(intent,
										InvestConstant.FUNDRISK);
						context.overridePendingTransition(R.anim.push_up_in,
								R.anim.no_animation);
					}
					break;
				case 3:// 投资理财风险评估
					if (!isrequestInvtEvaluationed) {

						Intent intent = new Intent(BaseDroidApp.getInstanse()
								.getCurrentAct(),
								InvtEvaluationInputActivity.class);
						intent.putExtra(InvestConstant.MAINSTASRT, true);
						BaseDroidApp
								.getInstanse()
								.getCurrentAct()
								.startActivityForResult(intent,
										InvestConstant.INVTRISK);
						context.overridePendingTransition(R.anim.push_up_in,
								R.anim.no_animation);
					}
					break;

				}

			}
		});
		// LogGloble.d("info",
		// "dataList.get(position).get(3)=="+dataList.get(position).get(3));
		// tv3.setText(StringUtil
		// .getForSixForString(dataList.get(position).get(3)));
		switch (position) {
		case 0:
			tv1.setText(R.string.inverstwaihui);
			btn_invest.setVisibility(View.INVISIBLE);
			break;
		case 1:
			tv1.setText(R.string.inverstguijinshu);
			btn_invest.setVisibility(View.INVISIBLE);
			break;
		case 2:
			tv1.setText(R.string.investtjijin);
			btn_invest.setVisibility(View.VISIBLE);
			if (isEvaluated) {
				// 显示基金风险评估等级
				btn_invest.setText(LocalData.riskLevelList.get(evaLevel - 1));
			} else {
				btn_invest.setText(R.string.investpingu);
			}
			break;
		case 3:
			tv1.setText(R.string.investzhongyinlicai);
			btn_invest.setVisibility(View.VISIBLE);
			if (isrequestInvtEvaluationed) {
				// 显示投资理财服务风险评估等级
				btn_invest.setText(LocalData.riskLevelList.get(invtLevel - 1));
			} else {
				btn_invest.setText(R.string.investpingu);
			}
			break;

		}

		return convertView;
	}

}
