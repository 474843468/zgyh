package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.BookingStatus;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 预约开户历史查询详情
 * 
 * @author panwe
 * 
 */
public class AcctOpenedInfoActivity extends ThirdManagerBaseActivity {

	/** 主布局 */
	private View viewContent;
	/** 接收上页面传过来的数据 **/
	public static Map<String, Object> dataMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_openacc_query_info, null);
		addView(viewContent);
		setTitle(this.getString(R.string.third_openacc_query));
		init();
	}

	private void init() {
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});
		TextView tvBankAcc = (TextView) viewContent.findViewById(R.id.tv_bankacc);
		TextView tvCompany = (TextView) viewContent.findViewById(R.id.tv_company);
		TextView tvCompanyPartName = (TextView) viewContent.findViewById(R.id.tv_companypart);
		TextView tvAdress = (TextView) viewContent.findViewById(R.id.tv_adress);
		TextView tvMoblie = (TextView) viewContent.findViewById(R.id.tv_mobile);
		TextView tvPas = (TextView) viewContent.findViewById(R.id.tv_company_pens);
		TextView tvBookDate = (TextView) viewContent.findViewById(R.id.tv_bookdate);
		TextView tvunDate = (TextView) viewContent.findViewById(R.id.tv_unuseddate);
		TextView tvstate = (TextView) viewContent.findViewById(R.id.tv_bookstate);

		tvBankAcc.setText(StringUtil.getForSixForString((String) dataMap.get(Third.OPENED_HISTORY_ACC)));
		tvCompany.setText((String) dataMap.get(Third.OPENED_HISTORY_COMANY));
		tvCompanyPartName.setText((String) dataMap.get(Third.OPENACC_STOVK_NAME));
		tvPas.setText((String) dataMap.get(Third.OPENED_HISTORY_LINK_MAN));
		tvMoblie.setText((String) dataMap.get(Third.OPENED_HISTORY_MOBILE));
		tvAdress.setText((String) dataMap.get(Third.OPENED_HISTORY_DRESS));
		tvBookDate
				.setText(QueryDateUtils.getCurDate(Long.parseLong((String) dataMap.get(Third.OPENED_HISTROY_BOOKDETE))));
		tvunDate.setText(QueryDateUtils.getCurDate(Long.parseLong((String) dataMap.get(Third.OPENED_HISTORY_INIDATE))));
		tvstate.setText(BookingStatus.getBookingStatusStr((String) dataMap.get(Third.OPENED_HISTORY_BOOKSTATE)));

		TextView tvMobileLable = (TextView) findViewById(R.id.tv_mobile_label);
		//
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvMobileLable);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvCompany);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvCompanyPartName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAdress);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvMoblie);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvPas);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBookDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvunDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvstate);
	}

}
