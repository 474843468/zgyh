package com.chinamworld.bocmbci.biz.plps.payment.project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.payment.project.fragment.CommServiceFramgment;
import com.chinamworld.bocmbci.biz.plps.payment.project.fragment.PaymentCommonServerFragment;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PaymentAllProject extends PlpsBaseActivity {
	// 全部缴费项目
	private LinearLayout allPaymentProject;
	private ImageView allPaymentImage;
	private ImageView allPaymentGrayImage;
	private TextView allPaymentText;
	// 常用服务项目
	private LinearLayout commonPaymentService;
	private ImageView commonPaymentImage;
	private ImageView commonPaymentGrayImage;
	private TextView commonPaymentText;
	// 项目下的滑动条
	private TextView cursor;
	private TextView cursort;
	// viewPaper
//	private ViewPager viewPaper;
	private FrameLayout commLayout;
	private PaymentCommonServerFragment paymentCommonServiceFragment;
	private CommServiceFramgment commServiceFragement;
//	private ArrayList<Fragment> fragementList;
	private int currentIndex;
	// 设置字体和图片颜色
	private int selectedColor, unSelectedColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("plps_1");
		inflateLayout(R.layout.plps_payment_project);
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,0, 0);
		PlpsDataCenter.getInstance().setIsDelete(false);
		requestPlpsQueryDefaultArea();
	}

	/** 加载全部缴费项目页面 */
	private void initTextView() {
		selectedColor = getResources().getColor(R.color.red);
		unSelectedColor = getResources().getColor(R.color.black);

		allPaymentProject = (LinearLayout) findViewById(R.id.all_payment_project);
		allPaymentImage = (ImageView) findViewById(R.id.all_payment_image);
		allPaymentGrayImage = (ImageView)findViewById(R.id.all_payment_image_2);
		allPaymentText = (TextView) findViewById(R.id.all_payment_text);
//		allPaymentImage.setColorFilter(selectedColor);
		allPaymentText.setTextColor(selectedColor);

		commonPaymentService = (LinearLayout) findViewById(R.id.common_service_payment);
		commonPaymentImage = (ImageView) findViewById(R.id.common_service_image);
		commonPaymentGrayImage = (ImageView)findViewById(R.id.common_service_image_2);
		commonPaymentText = (TextView) findViewById(R.id.common_service_text);

		cursor = (TextView) findViewById(R.id.cursor);
		cursort = (TextView) findViewById(R.id.cursort);

		allPaymentProject.setOnClickListener(new txtPaymentListener(0));
		commonPaymentService.setOnClickListener(new txtPaymentListener(1));
		mRightButton.setVisibility(View.GONE);
		cityAdress.setVisibility(View.VISIBLE);
		
	}
	
	@Override
	public void requestPlpsQueryDefaultAreaCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPlpsQueryDefaultAreaCallBack(resultObj);
		// 初始化服务界面
		initTextView();
		initViewPaper();
	}
	public class txtPaymentListener implements View.OnClickListener {
		private int index = 0;

		public txtPaymentListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			FragmentManager fm = getFragmentManager();
			//开启fragment事务
			FragmentTransaction transaction = fm.beginTransaction();
			switch (index) {
			case 0:
				allPaymentText.setTextColor(selectedColor);
				allPaymentImage.setVisibility(View.VISIBLE);
				allPaymentGrayImage.setVisibility(View.GONE);
				commonPaymentImage.setVisibility(View.GONE);
				commonPaymentGrayImage.setVisibility(View.VISIBLE);
				commonPaymentText.setTextColor(unSelectedColor);
				cursor.setVisibility(View.VISIBLE);
				cursort.setVisibility(View.INVISIBLE);
				Boolean isDelete = PlpsDataCenter.getInstance().getIsDelete();
				if(!StringUtil.isNullOrEmpty(isDelete)){
					if(isDelete){
						findViewById(R.id.finish).setVisibility(View.GONE);
						findViewById(R.id.cityAdress).setVisibility(View.VISIBLE);
					}
				}
				
				paymentCommonServiceFragment = new PaymentCommonServerFragment();
				BaseHttpEngine.showProgressDialogCanGoBack();
				//使用当前fragment布局替代 framelayout控件
				transaction.replace(R.id.comm_fragment, paymentCommonServiceFragment);
				break;
			case 1:
				allPaymentImage.setVisibility(View.GONE);
				allPaymentGrayImage.setVisibility(View.VISIBLE);
				allPaymentText.setTextColor(unSelectedColor);
				commonPaymentImage.setVisibility(View.VISIBLE);
				commonPaymentGrayImage.setVisibility(View.GONE);
				commonPaymentText.setTextColor(selectedColor);
				cursor.setVisibility(View.INVISIBLE);
				cursort.setVisibility(View.VISIBLE);
				Boolean isDeleteOrno = PlpsDataCenter.getInstance().getIsDelete();
				if(!StringUtil.isNullOrEmpty(isDeleteOrno)){
					if(isDeleteOrno){
						findViewById(R.id.finish).setVisibility(View.GONE);
						findViewById(R.id.cityAdress).setVisibility(View.VISIBLE);
					}
				}
				commServiceFragement = new CommServiceFramgment();
				//使用当前fragment布局替代faamelayout控件
				transaction.replace(R.id.comm_fragment, commServiceFragement);
				break;
			}
//			viewPaper.setCurrentItem(index);
			transaction.commit();
		}
	}

	/** 加载viewPaper */
	private void initViewPaper() {
//		fragementList = new ArrayList<Fragment>();
//		Fragment paymentAllProject = new PaymentAllProjectFragment();
//		Fragment paymentCommonProject = new PaymentCommonServerFragment();
//		Fragment commServiceFragment = new CommServiceFramgment();
//		fragementList.add(paymentCommonProject);
//		fragementList.add(commServiceFragment);
//		commFragment = new PaymentCommonServerFragment();
//		commFragment.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),
//				fragementList));
//		viewPayment.addTouchables(paymentCommonProject);
//		viewPaper.setCurrentItem(0);
//		viewPaper.setOnPageChangeListener(new myOnpageChangeListener());
		FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        paymentCommonServiceFragment = new PaymentCommonServerFragment();
        transaction.replace(R.id.comm_fragment, paymentCommonServiceFragment);
        transaction.commit();

	}

	/** viewpaper滑动事件 */
//	public class myOnpageChangeListener implements OnPageChangeListener {
//
//		@Override
//		public void onPageScrollStateChanged(int arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPageSelected(int arg0) {
//			// TODO Auto-generated method stub
//			switch (arg0) {
//			case 0:
//				allPaymentImage.setVisibility(View.VISIBLE);
//				allPaymentGrayImage.setVisibility(View.GONE);
//				allPaymentText.setTextColor(selectedColor);
//				commonPaymentImage.setVisibility(View.GONE);
//				commonPaymentGrayImage.setVisibility(View.VISIBLE);
//				commonPaymentText.setTextColor(unSelectedColor);
//				cursor.setVisibility(View.VISIBLE);
//				cursort.setVisibility(View.INVISIBLE);
//				Boolean isDelete = PlpsDataCenter.getInstance().getIsDelete();
//				if(!StringUtil.isNullOrEmpty(isDelete)){
//					if(isDelete){
//						findViewById(R.id.finish).setVisibility(View.GONE);
//						findViewById(R.id.cityAdress).setVisibility(View.VISIBLE);
//					}
//				}
//				break;
//			case 1:
//				allPaymentImage.setVisibility(View.GONE);
//				allPaymentGrayImage.setVisibility(View.VISIBLE);
//				allPaymentText.setTextColor(unSelectedColor);
//				commonPaymentImage.setVisibility(View.VISIBLE);
//				commonPaymentGrayImage.setVisibility(View.GONE);
//				commonPaymentText.setTextColor(selectedColor);
//				cursor.setVisibility(View.INVISIBLE);
//				cursort.setVisibility(View.VISIBLE);
//				Boolean isDeleteOrno = PlpsDataCenter.getInstance().getIsDelete();
//				if(!StringUtil.isNullOrEmpty(isDeleteOrno)){
//					if(isDeleteOrno){
//						findViewById(R.id.finish).setVisibility(View.VISIBLE);
//						findViewById(R.id.cityAdress).setVisibility(View.GONE);
//					}
//				}
//				break;
//			}
//	}
//
//	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(R.string.plps_liveservice_title);
	}
	
}
