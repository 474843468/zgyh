package com.chinamworld.bocmbci.biz.goldbonus.realextract;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.GoldbounsReminderActivity;
import com.chinamworld.bocmbci.biz.investTask.GoldBonusTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;

/**
 * 贵金属实物提取主页面
 * @author linyl
 *
 */
public class RealExtractActivity extends GoldBonusBaseActivity {
	private TitleAndContentLayout v ;
	String linkAccFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_title_extract);
		getBackgroundLayout().setRightButtonNewText(null);
		setLeftSelectedPosition("goldbonusManager_6");//贵金属实物提取
		/**签约标示关联判断**/
		linkAccFlag = (String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get(GoldBonus.LINKACCTFLAG);
		if("2".equals(linkAccFlag)){//已签约未关联
			Intent intent = new Intent(this, GoldbounsReminderActivity.class);
			intent.putExtra("title", "贵金属实物提取");
			startActivity(intent);
			return;
		}							
		GoldBonusTask task = GoldBonusTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				setContentView(R.layout.goldbonus_realextract_main);
				v = (TitleAndContentLayout) findViewById(R.id.agr_cancel_info_content_confirm);
				v.setTitleVisibility(View.GONE);
			}

		},null);
	}
}
