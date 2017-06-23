package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldbounsRateCheckListActivity extends GoldBonusBaseActivity
		implements ICommonAdapter<Map<String, Object>> {
	private ListView quotations_lv;
	/** 采用通用适配器接口实现 **/
	private CommonAdapter<Map<String, Object>> adapter;
	/** 适配器集合 **/
	private List<Map<String, Object>> positionList;
	private int recordNumber = 0;
	/** 当前页数 */
	private int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setTitle(R.string.goldbonus_account_manager);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_account_manager);
		getBackgroundLayout().setRightButtonNewText(null);
		setContentView(R.layout.goldbonus_rate_check_list);
		//调用系统当前时间（待定）
		quotations_lv=(ListView) findViewById(R.id.quotations_lv);
		
		
		//调用利率查询接口
		requestPsnGoldBonusHistoryRateQuery("", "", "", "", currentIndex, "");
	}

	public void requestPsnGoldBonusHistoryRateQuery(String xpadgPeriodType,
			String startDate, String endDate, String pageSize,
			int currentIndex, String refresh) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(GoldBonus.XPADGPERIODTYPE, xpadgPeriodType);// 期限类型
		parmMap.put(GoldBonus.STARTDATE, startDate);// 开始日期
		parmMap.put(GoldBonus.ENDDATE, endDate);// 结束日期
		parmMap.put(GoldBonus.PAGESIZE,pageSize);// 页面大小
		parmMap.put(GoldBonus.CURRENTINDEX, Integer.valueOf(currentIndex));// 当前引索页
		parmMap.put(GoldBonus.REFRESH, refresh);// 刷线标志
		getHttpTools().requestHttp(GoldBonus.PSNGOLDBONUSHISTORYRATEQUERY,
				"requestPsnGoldBonusHistoryRateQueryCallBack", parmMap, true);
	}

	public void requestPsnGoldBonusHistoryRateQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		positionList = (List<Map<String, Object>>) resultMap
				.get(GoldBonus.LIST_NEW);
		adapter = new CommonAdapter<Map<String, Object>>(this, quotations_lv,
				positionList, this);
		adapter.setTotalNumber(recordNumber);
		adapter.setRequestMoreDataListener(new IFunc<Boolean>() {
			@Override
			public Boolean callBack(Object param) {
				currentIndex++;
				// 再次掉接口

				requestPsnGoldBonusHistoryRateQuery("","","", "",currentIndex,
						"false");
				return true;
			}

		});

	}

	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.goldbouns_positionmessage_listiterm, null);
			viewHolder = new ViewHolder();
			viewHolder.first_item = (TextView) convertView
					.findViewById(R.id.first_item);
			viewHolder.second_item = (TextView) convertView
					.findViewById(R.id.second_item);
			viewHolder.third_item = (TextView) convertView
					.findViewById(R.id.third_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.first_item.setText((String) currentItem
				.get(GoldBonus.UPDATE));
		viewHolder.second_item.setText((String) currentItem
				.get(GoldBonus.ISSUENAME));
		viewHolder.third_item.setText((String) currentItem
				.get(GoldBonus.ISSUERATE));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				GoldbounsRateCheckListActivity.this, viewHolder.first_item);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				GoldbounsRateCheckListActivity.this, viewHolder.second_item);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				GoldbounsRateCheckListActivity.this, viewHolder.third_item);
		return convertView;
	}
	public class ViewHolder {
		private TextView first_item;
		private TextView second_item;
		private TextView third_item;
	}
}
