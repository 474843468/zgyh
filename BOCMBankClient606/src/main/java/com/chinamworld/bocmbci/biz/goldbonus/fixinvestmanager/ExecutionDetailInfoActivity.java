package com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager;

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
 * 定投执行明细详情页面
 * @author linyl
 *
 */
public class ExecutionDetailInfoActivity extends GoldBonusBaseActivity {
	private TitleAndContentLayout v ;
	private LinearLayout mContainerLayout;
	private Map<String,Object> ItemMap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbonus_exedetailinfo);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_fixinvestmanager);
//		getBackgroundLayout().setRightButtonText("主界面");
		v = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout);
		mContainerLayout = (LinearLayout) v.findViewById(R.id.myContainerLayout);
		v.setTitleVisibility(View.GONE);
		ItemMap = GoldbonusLocalData.getInstance().FixInvestDetailQryMap;
		initDetailInfoView();
	}
	/**
	 * 详情页面展示
	 */
	private void initDetailInfoView() {
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_execDate,(String)ItemMap.get("fixPayDate"),null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_buynum2,StringUtil.parseStringPattern((String)ItemMap.get("weight"),0)+" 克",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_baseprice,
				paseEndZero((String)ItemMap.get("tranBfprice"))+" 人民币元/克",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tranPrice,
				paseEndZero((String)ItemMap.get("tranPrice"))," 人民币元/克",REDBLACK));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_traAmount,"人民币元 ",StringUtil
				.parseStringPattern2((String)ItemMap.get("fixAmt"),2),BLACKRED));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_execuResult,
				DictionaryData.getKeyByValue((String)ItemMap.get("fixStatus"),DictionaryData.fixStatusResList),null));
		if(!"1".equals((String)ItemMap.get("fixStatus"))){
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_failmemo,(String)ItemMap.get("proResult"),null));
		}
	}

}
