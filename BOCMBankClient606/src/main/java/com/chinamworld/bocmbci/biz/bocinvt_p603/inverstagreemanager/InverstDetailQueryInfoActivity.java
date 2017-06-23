package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 投资明细信息页面
 * 
 * @author niuchf
 * 
 */
public class InverstDetailQueryInfoActivity extends BociBaseActivity {
	public static final String TAG = "InverstDetailQueryInfoActivity";
	/** 投资明细查询信息页 */
	private View view;
	private Map<String, Object> inverstDetailMap;
	/** 交易日期 */
	private TextView tv_dealDate;
	/** 交易类型 */
	private TextView tv_dealType;
	/** 交易金额 */
	private TextView tv_dealCash;
	/** 交易份额 */
	private TextView tv_dealNum;
	/** 状态 */
	private TextView tv_dealStatus;
	/** 失败原因 */
	private TextView tv_failReason;
	private LinearLayout failReason_layout;
	private OnClickListener setRightButtonClickListener = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_inverst_detail_query));
//		gonerightBtn();
		setText("主界面");
		setRightBtnClick(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (setRightButtonClickListener != null) {
					setRightButtonClickListener.onClick(v);
					return;
				}
				if (InverstDetailQueryInfoActivity.this.getActivityTaskType() == ActivityTaskType.TwoTask) {
//					ActivityTaskManager.getInstance().removeAllSecondActivity();
//					Intent intent = new Intent();
//					intent.setClass(InverstDetailQueryInfoActivity.this, SecondMainActivity.class);
//					InverstDetailQueryInfoActivity.this.startActivity(intent);
					goToMainActivity();
				} else {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(InverstDetailQueryInfoActivity.this, MainActivity.class);
//					InverstDetailQueryInfoActivity.this.startActivity(intent);
					goToMainActivity();
				}
			}
		});
		// 添加布局
		view = addView(R.layout.boc_inverst_detail_query_info);
		// 界面初始化
		init();
	}

	private void init() {
		inverstDetailMap = BociDataCenter.getInstance().getInverstDetailMap();
		String proCur = getIntent().getStringExtra("currency");
		LinearLayout ll_dealNum = (LinearLayout) view.findViewById(R.id.ll_dealNum);//份额
		LinearLayout ll_dealCash = (LinearLayout) view.findViewById(R.id.ll_dealCash);//金额
		tv_dealDate = (TextView) view.findViewById(R.id.tv_dealDate);
		tv_dealType = (TextView) view.findViewById(R.id.tv_dealType);
		tv_dealCash = (TextView) view.findViewById(R.id.tv_dealCash);
		tv_dealNum = (TextView) view.findViewById(R.id.tv_dealNum);
		tv_dealStatus = (TextView) view.findViewById(R.id.tv_dealStatus);
		tv_failReason = (TextView) view.findViewById(R.id.tv_failReason);
		failReason_layout = (LinearLayout) view.findViewById(R.id.failReason_layout);
		tv_dealDate.setText((String) inverstDetailMap
				.get(BocInvt.BOCINVT_TDSDATE_RES));
		tv_dealType.setText(LocalData.inverstTradeTypeStr.get((String) inverstDetailMap
				.get(BocInvt.BOCINVT_TDSTYPE_RES)));
		if(String.valueOf(inverstDetailMap.get(BocInvt.BOCINVT_TDSTYPE_RES)).equals("0")){//赎回
//			tv_dealCash.setText("-");
			ll_dealCash.setVisibility(View.GONE);
			if(((String) inverstDetailMap
					.get(BocInvt.BOCINVT_TDSUNIT_RES)).equals("-1.00") || ((String) inverstDetailMap
							.get(BocInvt.BOCINVT_TDSUNIT_RES)).equals("-1")){//-1 全额赎回
				tv_dealNum.setText("全额赎回");
			}else{//交易份额 取消 日元格式化  ，显示两位小数
//				tv_dealNum.setText(StringUtil.parseStringCodePattern(proCur,(String) inverstDetailMap
//						.get(BocInvt.BOCINVT_TDSUNIT_RES),2));
				tv_dealNum.setText(StringUtil.parseStringPattern((String) inverstDetailMap
						.get(BocInvt.BOCINVT_TDSUNIT_RES),2));
			}
			tv_dealNum.setTextColor(getResources().getColor(R.color.red));
		}else if(String.valueOf(inverstDetailMap.get(BocInvt.BOCINVT_TDSTYPE_RES)).equals("1")){//购买
			tv_dealCash.setText(StringUtil.parseStringCodePattern(proCur,(String) inverstDetailMap
					.get(BocInvt.BOCINVT_TDSAMT_RES),2));
			
			tv_dealCash.setTextColor(getResources().getColor(R.color.red));
//			tv_dealNum.setText("-");
			ll_dealNum.setVisibility(View.GONE);
		}
//		tv_dealNum.setText((String) inverstDetailMap
//				.get(BocInvt.BOCINVT_TDSUNIT_RES));
		String dealStatus = (String) inverstDetailMap
				.get(BocInvt.BOCINVT_TDSSTATE_RES);
		tv_dealStatus.setText(LocalData.fundTradeStateStr.get(dealStatus));
		if(!StringUtil.isNullOrEmpty(dealStatus)){
			if(dealStatus.equals("1")){//失败
				failReason_layout.setVisibility(View.VISIBLE);
				tv_failReason.setText((String) inverstDetailMap
						.get(BocInvt.BOCINVT_MEMO_RES));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
						tv_failReason);
			}
		}
	
	}

}
