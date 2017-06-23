package com.chinamworld.bocmbci.biz.goldbonus.timedepositmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 定存管理  定期客户持仓信息 详情页面
 * @author linyl
 *
 */
public class TimePositionActivity extends GoldBonusBaseActivity {
	private TitleAndContentLayout titleAndContentLayout;
	private LinearLayout myContainerLayou;
	private Map<String,Object> ItemMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_timedepositmanager);
//		getBackgroundLayout().setRightButtonText("主界面");
		setContentView(R.layout.goldbonus_timeposition_detail);
		titleAndContentLayout = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout);
		titleAndContentLayout.setTitleVisibility(View.GONE);
		myContainerLayou = (LinearLayout) findViewById(R.id.myContainerLayout);
		ItemMap = GoldbonusLocalData.getInstance().AccountQueryMap;
		initDetailView();
	}
	/**
	 * 初始化定存管理 持仓信息的详情页面
	 */
	private void initDetailView() {
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName, String.valueOf(ItemMap.get("issueName")),null));
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_startdate1, String.valueOf(ItemMap.get("tradeDate")),null));
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_period, String.valueOf(ItemMap.get("limitTime")) + 
				DictionaryData.getKeyByValue((String)ItemMap.get("limitUnit"),DictionaryData.goldbonuslimitUnitList),null));
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_enddate1, String.valueOf(ItemMap.get("expDate")),null));
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_num, StringUtil.deleateNumber(String.valueOf(ItemMap.get("tradeWeight")))+" 克",null));
		myContainerLayou.addView(createLabelTextView(R.string.goldbonus_timedeposit_rate1, paseEndZero(String.valueOf(ItemMap.get("issueRate")))+"%",null));
		if("0.00".equals(StringUtil.parseStringPattern(String.valueOf(ItemMap.get("regBonus")),2))){
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_shoud_premium, "人民币元 ", "-",BLACKRED));
		}else{
			myContainerLayou.addView(createLabelTextView(R.string.goldbonus_shoud_premium, "人民币元 ", StringUtil.parseStringPattern(String.valueOf(ItemMap.get("regBonus")),2),BLACKRED));
		}
	}
	

}
