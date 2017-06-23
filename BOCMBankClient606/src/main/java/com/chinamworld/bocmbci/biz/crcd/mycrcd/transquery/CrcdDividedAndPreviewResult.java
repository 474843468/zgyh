package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 提前结清入账结果页
 * 
 * @author huangyuchao
 * 
 */
public class CrcdDividedAndPreviewResult extends CrcdBaseActivity {

	private View view;

	private TextView finc_accNumber, finc_fenqidate, finc_miaoshu, finc_bizhong, finc_fincName, finc_qinum,
			finc_fenqitype, finc_firstamount, finc_nextmoney, finc_hasrumoney, finc_remiannonum, finc_remiannomoney,
			finc_nextdate;

	private Button sureButton;
	private TextView moneyText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_divided_jieqing_ruzhang));
		view = addView(R.layout.crcd_divided_info_detail_suuccess);
		init();

		back.setVisibility(View.GONE);
	}

	protected static Map<String, Object> detailMap;

	String instalmentPlan;

	/** 初始化界面 */
	public void init() {
		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqidate);
		finc_miaoshu = (TextView) view.findViewById(R.id.finc_miaoshus);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_miaoshu);
		finc_bizhong = (TextView) view.findViewById(R.id.finc_bizhong);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_bizhong);
		finc_fincName = (TextView) view.findViewById(R.id.finc_fincName);
		finc_qinum = (TextView) view.findViewById(R.id.finc_qinum);
		finc_fenqitype = (TextView) view.findViewById(R.id.finc_fenqitype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_bizhong);
		finc_firstamount = (TextView) view.findViewById(R.id.finc_firstamount);
		finc_nextmoney = (TextView) view.findViewById(R.id.finc_nextmoney);
		finc_hasrumoney = (TextView) view.findViewById(R.id.finc_hasrumoney);
		finc_remiannonum = (TextView) view.findViewById(R.id.finc_remiannonum);
		finc_remiannomoney = (TextView) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) view.findViewById(R.id.finc_nextdate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_nextdate);
		detailMap = CrcdDividedDetailListDetail.detailMap;

		moneyText = (TextView) findViewById(R.id.money);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);

		finc_accNumber.setText(StringUtil.getForSixForString(CrcdDividedHistoryQueryList.accountNumber));
		finc_fenqidate.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMTDATE)));
		finc_miaoshu.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMEDESCRIPTION)));
		finc_bizhong.setText(CrcdDividedDetailListDetail.strCurrency);
		finc_fincName.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_AMOUNT)), 2));
		finc_qinum.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMTCOUNT)));
		finc_fenqitype.setText(CrcdDividedDetailListDetail.strChargeMode);
		finc_firstamount.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_FIRSTINAMOUNT)),
				2));
		finc_nextmoney
				.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_NEXTTIMEAMOUNT)), 2));
		finc_hasrumoney
				.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_INCOMEAMOUNT)), 2));
		finc_remiannonum.setText(String.valueOf(detailMap.get(Crcd.CRCD_RESTTIMECOUNT)));

		finc_remiannomoney.setText(StringUtil.parseStringPattern(
				String.valueOf(detailMap.get(Crcd.CRCD_RESTAMOUNT_RES)), 2));
		finc_nextdate.setText(String.valueOf(detailMap.get(Crcd.CRCD_NEXTINCOMEDATE)));
		instalmentPlan = String.valueOf(detailMap.get(Crcd.CRCD_INSTALMENTPLAN));

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(CrcdDividedAndPreviewResult.this, CrcdDividedHistoryQueryList.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
			}
		});

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

}
