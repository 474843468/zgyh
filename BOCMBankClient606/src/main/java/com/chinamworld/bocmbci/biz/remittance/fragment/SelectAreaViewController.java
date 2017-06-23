package com.chinamworld.bocmbci.biz.remittance.fragment;

import android.view.View;

import com.chinamworld.bocmbci.R;

public class SelectAreaViewController {
	public static void viewControl(View mMainView, int selectPosition) {
		switch (selectPosition) {
		case 0:
			areaSelectZeroControl(mMainView);
			break;
		case 1:
			areaSelectFirstControl(mMainView);
			break;
		case 2:
			areaSelectSecondControl(mMainView);
			break;
		case 3:
			areaSelectThirdControl(mMainView);
			break;
		case 4:
			areaSelectFourthControl(mMainView);
			break;
		case 5:
			areaSelectFifthControl(mMainView);
			break;
		case 6:
			areaSelectSixthControl(mMainView);
			break;
		}
	}
	

	/** 选择汇款地区为第0个的视图控制方法 */
	private static void areaSelectZeroControl(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip3).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip4).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip5).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip6).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip7).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip8).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_bankNumber).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbAdress).setVisibility(View.GONE);
	}
	
	/** 境外中行选择汇款地区为第0个的视图控制方法 */
	private static void areaSelectZeroControlOver(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);

		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);	
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
	}

	/** 选择汇款地区为第2个的视图控制方法 */
	private static void areaSelectSecondControlOver(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.VISIBLE);

		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
	}


	/** 境外中行选择汇款地区为第5个的视图控制方法 */
	private static void areaSelectFifthControlOver(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);

		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.VISIBLE);
	}


	/** 选择汇款地区为第1个的视图控制方法 */
	private static void areaSelectFirstControl(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip4).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip5).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip6).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip7).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip8).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_bankNumber).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbAdress).setVisibility(View.GONE);
	}
	
	/** 选择汇款地区为第2个的视图控制方法 */
	private static void areaSelectSecondControl(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip4).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip5).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip6).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip7).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip8).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_bankNumber).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbAdress).setVisibility(View.GONE);
	}
	

	
	/** 选择汇款地区为第3个的视图控制方法 */
	private static void areaSelectThirdControl(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip4).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip5).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip6).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip7).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip8).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_bankNumber).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbAdress).setVisibility(View.GONE);
	}
	

	
	/** 选择汇款地区为第4个的视图控制方法 */
	private static void areaSelectFourthControl(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip2).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip4).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip5).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip6).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip7).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip8).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_bankNumber).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbAdress).setVisibility(View.GONE);
	}

	
	/** 选择汇款地区为第5个的视图控制方法 */
	private static void areaSelectFifthControl(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip4).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip5).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip6).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip7).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip8).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_bankNumber).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_rbAdress).setVisibility(View.VISIBLE);
	}

	
	/** 选择汇款地区为第6个的视图控制方法 */
	private static void areaSelectSixthControl(View mMainView) {
		mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip4).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tip5).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip6).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip7).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip8).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_bankNumber).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_rbAdress).setVisibility(View.GONE);
	}
	

}
