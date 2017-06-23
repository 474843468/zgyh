package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitForCashInfo;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 可以查看收益明细的产品的参考收益显示，目前“日积月累”，“业绩基准型”两类产品使用
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForHaveDetailFragment extends Fragment implements
		OnCheckedChangeListener {
	private static final String TAG = ReferenceProfitForHaveDetailFragment.class
			.getSimpleName();
	private static final String KEY_PRODUCT_INFO = "product_info";
	private static final String KEY_SHOW_REMINDER = "show_reminder";
	private static final String KEY_REFERENCE_INFO = "reference_info";
	private RadioGroup rg_type;

	private BOCProductForHoldingInfo productInfo;
	private ReferenceProfitForCashInfo referProfitInfo;
	private boolean isShowReminderView;

	public static ReferenceProfitForHaveDetailFragment newInstance(
			BOCProductForHoldingInfo info,
			ReferenceProfitForCashInfo referProfitInfo,
			boolean isShowReminderView) {
		ReferenceProfitForHaveDetailFragment instance = new ReferenceProfitForHaveDetailFragment();
		Bundle data = new Bundle();
		data.putSerializable(KEY_PRODUCT_INFO, info);
		data.putBoolean(KEY_SHOW_REMINDER, isShowReminderView);
		data.putSerializable(KEY_REFERENCE_INFO, referProfitInfo);
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_reference_profit_for_have_details, container,
				false);
		Bundle data = getArguments();
		productInfo = (BOCProductForHoldingInfo) data
				.getSerializable(KEY_PRODUCT_INFO);
		referProfitInfo = (ReferenceProfitForCashInfo) data
				.getSerializable(KEY_REFERENCE_INFO);
		isShowReminderView = data.getBoolean(KEY_SHOW_REMINDER);
		rg_type = (RadioGroup) view.findViewById(R.id.rg_type);
		rg_type.setOnCheckedChangeListener(this);
		rg_type.check(R.id.rb_state);
		if(!"0".equals(productInfo.standardPro)&&"1".equals(productInfo.standardPro)&&"-1".equals(productInfo.productTerm)){//业绩基准型转低收益隐藏切换按钮
			rg_type.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		Fragment instance = null;
		LogGloble.e(TAG, "info = " + productInfo);

		switch (checkedId) {
		case R.id.rb_state:

			instance = ReferenceProfitForStateFragment.newInstance(productInfo,
					referProfitInfo, isShowReminderView);
			break;
		case R.id.rb_detail:
			instance = ReferenceProfitForDetailFragment
					.newInstance(productInfo,referProfitInfo,isShowReminderView);
			break;
		default:
			break;
		}

		if (instance == null) {
			return;

		}
		getChildFragmentManager().beginTransaction()
				.replace(R.id.content, instance).commit();
		LogGloble.d(TAG, "replace:" + instance.getClass().getSimpleName());
	}
}
