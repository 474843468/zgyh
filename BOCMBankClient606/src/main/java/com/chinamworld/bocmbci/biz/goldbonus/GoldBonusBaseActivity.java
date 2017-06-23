package com.chinamworld.bocmbci.biz.goldbonus;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.ViewGroup.LayoutParams;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.NewBaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.List;
import java.util.Map;

/**
 * 贵金属积利金 基类
 * 
 * @author yuht
 * 
 */
public class GoldBonusBaseActivity extends NewBaseActivity {

	public int[] REDBLACK = {R.color.fonts_pink,R.color.boc_text_color_dark_gray};
	public int[] BLACKRED = {R.color.boc_text_color_dark_gray,R.color.fonts_pink};

	/** 买入请求码 */
	public static final int REQUEST_LOGIN_CODE_BUY = 10003;
	/** 卖出请求码 */
	public static final int REQUEST_LOGIN_CODE_SELL = 10004;
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}
	public Map<String, Object> psnGoldBonusPriceListQuery;//积利金外置接口详情
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftSideList(this, LocalData.goldboundsLeftListData);
		this.getBackgroundLayout().setNewBaseTitle();
		this.getBackgroundLayout().setSlidingMenuViewBackground(getResources().getColor(R.color.boc_common_cell_color));
		this.getBackgroundLayout().setTitleBackground(getResources().getColor(R.color.btn_pink));
//		/**重写回主界面事件**/
//		this.getBackgroundLayout().setRightButtonNewClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View view) {
//				goToMainActivity();
//				finish();
//			}
//		});
	}

	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
//		if("0".equals((String)GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get(GoldBonus.LINKACCTFLAG)) && !"goldbonusManager_1".equals(menuItem.MenuID)){
		if("0".equals((String)GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get(GoldBonus.LINKACCTFLAG))){
			BaseDroidApp.getInstanse().showInfoMessageDialog("请先开通贵金属积利服务");
			return true;
		}
		super.selectedMenuItemHandler(context, menuItem);
		ActivityIntentTools.intentToActivityWithData(context, menuItem.getClassName(),null);
		return true;
	}



	/**
	 * 加载交易页面的单个TextView 字段键值对
	 * @param resid
	 * @param valuetext
	 * @param colorresid 设置字体颜色
	 */
	public LabelTextView createLabelTextView(int resid,String valuetext,String valuetexttwo, int...colorresid){
		LabelTextView v = new LabelTextView(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		v.setLabelText(resid);
		v.setValueText(StringUtil.isNullChange(valuetext));
//		v.setValueText(valuetext);
		if(valuetexttwo != null){
			v.setValueTextTwo(valuetexttwo);
		}
		/**设置文本颜色  默认**/
		v.setLabelTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
		v.setValueTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
		/**特殊设置颜色**/
		if(colorresid != null && colorresid.length == 1){
			v.setValueTextColor(getResources().getColor(colorresid[0]));
		}else if(colorresid != null && colorresid.length == 2){
			v.setValueTextColor(getResources().getColor(colorresid[0]));
			v.setValueTextTwoColor(getResources().getColor(colorresid[1]));
		}
		v.setWeightShowRate("2:3");
//		v.setEllipsize(TruncateAt.END);
		v.setLabelTextViewEllipsize(TruncateAt.MIDDLE);
		v.setValueTextViewEllipsize(TruncateAt.END);
		v.setValueTextTwoViewEllipsize(TruncateAt.END);


		return v;
	} 
	
	/**
	 * 加载提前终止文本信息（特殊处理  空不做- 展示处理）单个TextView 字段键值对 
	 * @param resid
	 * @param valuetext
	 * @param colorresid 设置字体颜色
	 */
	public LabelTextView createRemarkLabelTextView(int resid,String valuetext,String valuetexttwo, int...colorresid){
		LabelTextView v = new LabelTextView(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		v.setLabelText(resid);
//		v.setValueText(StringUtil.isNullChange(valuetext));
		v.setValueText(valuetext);
		if(valuetexttwo != null){
			v.setValueTextTwo(valuetexttwo);
		}
		/**设置文本颜色  默认**/
		v.setLabelTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
		v.setValueTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
		/**特殊设置颜色**/
		if(colorresid != null && colorresid.length == 1){
			v.setValueTextColor(getResources().getColor(colorresid[0]));
		}else if(colorresid != null && colorresid.length == 2){
			v.setValueTextColor(getResources().getColor(colorresid[0]));
			v.setValueTextTwoColor(getResources().getColor(colorresid[1]));
		}
		v.setWeightShowRate("2:3");
//		v.setEllipsize(TruncateAt.END);
		v.setLabelTextViewEllipsize(TruncateAt.MIDDLE);
		v.setValueTextViewEllipsize(TruncateAt.END);
		v.setValueTextTwoViewEllipsize(TruncateAt.END);
		return v;
	} 
	
	/**
	 * = 请求conversationId 来登录之外的conversation出Id
	 */
	public void requestGoldBounsCommConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestGoldBounsCommConversationIdCallBack");
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestGoldBounsCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);
	}
	// 末尾为0格式化方法,去掉末尾的0
		public String paseEndZero(String date) {
			if (date.indexOf(".") < 0) {

				return date;
			} else {
				date = date.replaceAll("0+?$", "");// 去掉多余的0
				date = date.replaceAll("[.]$", "");// 如最后一位是.则去掉
				return date;
			}
			
		}
}
