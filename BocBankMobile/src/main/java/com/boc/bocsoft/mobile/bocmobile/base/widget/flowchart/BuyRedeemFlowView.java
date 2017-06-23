package com.boc.bocsoft.mobile.bocmobile.base.widget.flowchart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 中银理财-赎回-竖向流程view
 */
public class BuyRedeemFlowView extends LinearLayout {

	protected View rootView;
	private Context mContext;

	protected LinearLayout ll_5;
	protected TextView tv_value1, tv_value2, tv_value3;
	protected ImageView line_bottom,iv_4;
	private CompleteRedeemStatusT status = CompleteRedeemStatusT.ONE;
	/***
	 * 是否是两中状态， true 是（2）；false 不是（大部分是3）
	 */
	private boolean isTwoStatus = false;

	public enum CompleteRedeemStatusT {
		ONE, TWO, THREE
	}

	private String[] text = new String[] { "今日赎回", "本金到帐", "收益到帐" };
	private String[] date = new String[] { "2018/05/06", "2018/05/06赎回，本金实时到账",
			"预计2017/09/12" };

	public BuyRedeemFlowView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public BuyRedeemFlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	@SuppressLint("NewApi")
	public BuyRedeemFlowView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		init();
	}

	/**
	 * 初始化View
	 */
	private void init() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.buyredeem_view, this);

		tv_value1 = (TextView) rootView.findViewById(R.id.tv_1);
		tv_value2 = (TextView) rootView.findViewById(R.id.tv_2);

		tv_value3 = (TextView) rootView.findViewById(R.id.tv_3);
		line_bottom = (ImageView) rootView.findViewById(R.id.line_bottom);
		iv_4 = (ImageView) rootView.findViewById(R.id.iv_4);
		ll_5 = (LinearLayout) rootView
				.findViewById(R.id.ll_5);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (isTwoStatus) {

			tv_value1.setText(text[0]+"\n"+date[0]);
			tv_value2.setText(text[1]+"\n"+date[1]);

			tv_value3.setVisibility(View.GONE);
			ll_5.setVisibility(View.GONE);
			iv_4.setVisibility(View.GONE);
			line_bottom.setVisibility(View.INVISIBLE);

		} else {
			tv_value1.setText(text[0]+"\n"+date[0]);
			tv_value2.setText(text[1]+"\n"+date[1]);
			tv_value3.setText(text[2]+"\n"+date[2]);

			tv_value3.setVisibility(View.VISIBLE);
			ll_5.setVisibility(View.VISIBLE);
			iv_4.setVisibility(View.VISIBLE);
			line_bottom.setVisibility(View.VISIBLE);

		}

	}

	public CompleteRedeemStatusT getStatus() {
		return status;
	}

	/**
	 * 设置流程走到的位置
	 * 
	 * @param status
	 */
	public void setStatus(CompleteRedeemStatusT status) {
		this.status = status;
		initData();
	}

	/**
	 * 设置所显示的 文本信息 value
	 * 
	 * @param date
	 */
	public void setItemViewValue(String[] date) {
		this.date = date;
	}

	/**
	 * 设置所显示的 文本信息 title
	 * 
	 * @param text
	 */
	public void setItemViewTitle(String[] text) {
		this.text = text;
		if (text.length == 2) {
			isTwoStatus = true;
		} else if (text.length == 3) {
			isTwoStatus = false;
		}

	}

	/**
	 * 根据手机分辨率吧dp转换成px
	 */
	private int getPXvalue(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale - 0.5f);
	}

}
