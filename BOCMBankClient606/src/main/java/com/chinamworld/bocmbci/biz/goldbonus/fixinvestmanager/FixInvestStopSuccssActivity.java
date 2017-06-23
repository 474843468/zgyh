package com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.busitrade.BusiTradeAvtivity;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 贵金属积利金模块 定投管理 提前终止结果页面
 * @author linyl
 *
 */
public class FixInvestStopSuccssActivity extends GoldBonusBaseActivity implements OnClickListener {
	/**详情信息展示布局**/
	private TitleAndContentLayout mAgreeDetailInfo;
	private Button btn_success;
	LinearLayout cancelSuccess_ll,cancelSuccessTitle;
	/**动态添加元素的布局**/
	private LinearLayout mContainerLayout;
	/**点击 这里 链接**/
	private TextView tv_link;
	/**列表项详情信息**/
	private Map<String,Object> itemDetailMap;
//	/**贵金属积利定投计划终止确认返回数据**/
//	private Map<String,Object> fixInvestStopPreMap;
//	/**贵金属积利定投计划终止提交 上送参数**/
//	private Map<String,Object> requestStopCommitParamMap = new HashMap<String,Object>();
	private SpannableString msp = null;
	String fixCancelPsStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_fixinvestmanager);
		setContentView(R.layout.goldbonus_fixinvest_stop_success);
		getBackgroundLayout().setLeftButtomNewVisibility(View.GONE);
		mAgreeDetailInfo = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout_detailinfo);
		mAgreeDetailInfo.setTitleVisibility(View.GONE);
		cancelSuccess_ll = (LinearLayout) findViewById(R.id.ll_cancel_success_href);
		btn_success = (Button) findViewById(R.id.btn_success);//终止结果页面 完成按钮
		tv_link = (TextView) findViewById(R.id.tv_link);
		fixCancelPsStr = this.getIntent().getStringExtra("fixCancelPs");
//		fixInvestStopPreMap = GoldbonusLocalData.getInstance().FixInvestStopPreMap;
		msp = new SpannableString("*您已提前终止本条定投预约，您可点击这里重新进行定投预约。");
		msp.setSpan(new UnderlineSpan(), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new ForegroundColorSpan(Color.BLUE), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_link.setText(getClickableSpan());
		tv_link.setMovementMethod(LinkMovementMethod.getInstance());  
		cancelSuccessTitle = (LinearLayout) findViewById(R.id.cancel_success_info_title);
		mContainerLayout = (LinearLayout) mAgreeDetailInfo.findViewById(R.id.myContainerLayout);
		itemDetailMap = GoldbonusLocalData.getInstance().FixInvestListDetailQueryMap;
		btn_success.setOnClickListener(this);
		initDetailView();
	}

	private SpannableString getClickableSpan() {
		 //监听器  
      View.OnClickListener listener = new View.OnClickListener() {  
          @Override  
          public void onClick(View v) {  
          	Intent intent2 = new Intent(FixInvestStopSuccssActivity.this, BusiTradeAvtivity.class);
          	intent2.putExtra("issueType", GoldbonusLocalData.FIXINVESTINTENTFLAG);
  			startActivity(intent2);
  			finish();
          }  
      };  
      SpannableString spanableInfo = new SpannableString("*您已提前终止本条定投预约，您可点击这里重新进行定投预约。");
      spanableInfo.setSpan(new Clickable(listener), 18, 20, Spanned.SPAN_MARK_MARK);
		return spanableInfo;
	}
	
	class Clickable extends ClickableSpan implements View.OnClickListener {  
      private final View.OnClickListener mListener;  

      public Clickable(View.OnClickListener listener) {  
          mListener = listener;  
      }  

      @Override  
      public void onClick(View view) {  
          mListener.onClick(view);  
      }  
	}
	
	/**
	 * 详情页面展示元素
	 */
	private void initDetailView() {
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_settime, (String)itemDetailMap.get("crtDate"),null));
		mContainerLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName, (String)itemDetailMap.get("issueName"),null));
		mContainerLayout.addView(createLabelTextView(R.string.goldbonus_fixinvest_status, 
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixStatus"),DictionaryData.FixStatusList),null));
		if("0".equals((String)itemDetailMap.get("fixTermType"))){//日
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList),null));
		}else if("1".equals((String)itemDetailMap.get("fixTermType"))){//周
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, 
					DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList) + "," +
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixPayDateValue"),DictionaryData.goldbonusfixPayDateValueWeekList),null));
		}else if("2".equals((String)itemDetailMap.get("fixTermType"))){//月
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, 
					DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList) + "," +
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixPayDateValue"),DictionaryData.goldbonusfixPayDateValueMounthList),null));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle,"-",null));
		}
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_buynum, StringUtil.parseStringPattern(StringUtil.deleateNumber((String)itemDetailMap.get("weight")),0)+" 克",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_fixPendCnt,
				"成功 "+String.valueOf((Long.parseLong((String)itemDetailMap.get("fixPendCnt")) - Long.parseLong((String)itemDetailMap.get("fixCount")))) +" 次，失败 " + (String)itemDetailMap.get("fixCount") +" 次",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_remaindcnt, (String)itemDetailMap.get("unfCount") +	" 次",null));
		if("2".equals((String)itemDetailMap.get("fixStatus")) || "3".equals((String)itemDetailMap.get("fixStatus"))){//状态为客户终止或者银行终止
			if(!"".equals(itemDetailMap.get("remark").toString().trim()) && StringUtil.isNullOrEmpty(itemDetailMap.get("remark"))){
				mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, "-", null));
			}else{
				mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, 
						(String)itemDetailMap.get("remark") , null));
			}
		}
			mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, fixCancelPsStr,null));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_success://终止结果页面 完成
			Intent intent2 = new Intent(this, FixInvestManagerActivity.class);
			startActivity(intent2);
			finish();
			break;
		}
	}

}
