package com.chinamworld.bocmbci.biz;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.database.entity.ShotCutFile;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.TabGrid;
import com.chinamworld.bocmbci.widget.adapter.ShotcutTabGridAdapter;
import com.chinamworld.bocmbci.widget.entity.ImageTextAndAct;
import com.j256.ormlite.dao.Dao;

public class WelcomeActivity extends BaseActivity {

	private static final String TAG = "WelcomeActivity";
	// private ArrayList<ImageAndText> icons = null;

	// 屏幕下方菜单
	private ArrayList<ImageTextAndAct> icons = null;
	// 一级菜单
	private ArrayList<ImageTextAndAct> mainFunction = null;
	// 删除快捷菜单之后 快捷框显示的data
	private ArrayList<ImageTextAndAct> leftMainFunction = null;

	// private ArrayList<Integer> selected = new ArrayList<Integer>();;
	// private ArrayList<Boolean> isSelected;
	/** 手机银行 */
	private Button BtnmobileBank;
	/** 快捷菜单 */
	private TabGrid shotCutGrid;
	EditText etDddress ;

	private ShotcutTabGridAdapter ShotcutTabGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.welcome_activity_layout);

		initPulldownBtn();
		GetPhoneInfo.initActFirst(this);

		// 初始化弹出快捷菜单
		initAddBtnData();
		// 初始化弹出框快捷菜单
		initShotcutIcons();
		etDddress = (EditText)findViewById(R.id.etAddress);
		
		BtnmobileBank = (Button) findViewById(R.id.btn_mobilebank);
//		String[] strs ={"1","2","3"};
//		int[] ints = {R.drawable.foot_add,R.drawable.foot_add,R.drawable.foot_add};
//		PopupWindowUtils.getInstance().setOnPullSelecterListener(this, BtnmobileBank, strs, ints, null);
		 BtnmobileBank.setOnClickListener(btnMobileBankListener);
		
		shotCutGrid = (TabGrid) findViewById(R.id.shotcut_grid);
		shotCutGrid.setColNum(4);
		shotCutGrid.setRowNum(1);
		ShotcutTabGridAdapter = new ShotcutTabGridAdapter(this, icons,
				R.layout.grid_item_img_text);
		shotCutGrid.setAdapter(ShotcutTabGridAdapter);

		shotCutGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (TabGrid.oneMove < 10) {
					int curPostion = (Integer) view.getTag();
					// 如果点击的是添加按钮
					if (icons.get(curPostion).getActName().equals(
							ConstantGloble.ADD_BUTTON)) {
						initShotcutIcons();
						// 弹出快捷菜单框
						BaseDroidApp.getInstanse().showShotcutDialog(
								leftMainFunction, onItemClickListener);
					} else {// 如果点击的不是添加按钮
						String actName = icons.get(curPostion).getActName();
						if (StringUtil.isNullOrEmpty(actName)) {
							return;
						}
						Intent intent = new Intent();
						ComponentName comp = new ComponentName(
								ConstantGloble.PACKAGE_NAME, actName);// 第一个参数是目标包名，第二个参数是目标包的activity名
						intent.setComponent(comp);
						startActivity(intent);
					}
				}
			}
		});
		shotCutGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final int curPostion = (Integer) view.getTag();
				if (icons.get(curPostion).getActName().equals(
						ConstantGloble.ADD_BUTTON)) {
					// 长按添加按钮 不予反应
					return false;
				}

				ImageButton imageBtn = (ImageButton) ((RelativeLayout) view)
						.getChildAt(1);
				// 显示加号“+”
				imageBtn.setVisibility(ImageButton.VISIBLE);
				imageBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// 删除数据库中数据
						Dao shotcutFileDao = null;
						try {
							shotcutFileDao = getDbHelper().getDao(
									ShotCutFile.class);

							List<ShotCutFile> list = shotcutFileDao
									.queryForAll();
							for (int i = 0; i < list.size(); i++) {
								if (icons.get(curPostion).getActName().equals(
										list.get(i).getShotcutName())) {
									shotcutFileDao.delete(list.get(i));
								}
							}
						} catch (SQLException e) {
							LogGloble.exceptionPrint(e);
						}

						icons.remove(position);
						shotCutGrid.setAdapter(ShotcutTabGridAdapter);
						ShotcutTabGridAdapter.dataChanged(icons);
					}
				});
				return false;
			}
		});

	}

	/**
	 * 手机银行按钮监听
	 */
	OnClickListener btnMobileBankListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

//			if(!"".equals(etDddress.getText().toString())){
//				BiiApi.BASE_HTTP_URL = etDddress.getText().toString();
//				BiiApi.BASE_URL = BiiApi.BASE_HTTP_URL;
//				BiiApi.BASE_API_URL = BiiApi.BASE_URL + BiiApi.BASE_API;
//			}
			
//			Intent intent = new Intent();
//			intent.setClass(WelcomeActivity.this, MainActivity.class);
//			startActivity(intent);
			goToMainActivity();
			finish();

		}
	};

	/**
	 * 快捷菜单弹出框 监听
	 */
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			int i = (Integer) view.getTag();
			// 一次只能选择一个
			Dao shotcutFileDao = null;
			try {
				shotcutFileDao = getDbHelper().getDao(ShotCutFile.class);
				ShotCutFile sf = new ShotCutFile();
				// sf.setShotcutID(i);
				sf.setShotcutName(leftMainFunction.get(i).getActName());
				sf.setShotcutIconId(leftMainFunction.get(i).getImageId());
				sf.setShotcutTextId(leftMainFunction.get(i).getText());
				shotcutFileDao.create(sf);

				icons.add(0, leftMainFunction.get(i));
				shotCutGrid.setAdapter(ShotcutTabGridAdapter);
				ShotcutTabGridAdapter.dataChanged(icons);

				BaseDroidApp.getInstanse().dismissMessageDialog();
			} catch (SQLException e) {
				LogGloble.exceptionPrint(e);
			}
			// TODO 可一次选择多想进行添加
			/*
			 * if(isSelected.get(i)){ for(int j=0; j<selected.size(); j++){ if(i
			 * == selected.get(j)){ selected.remove(j); } } isSelected.set(i,
			 * false); ImageButton imageBtn = (ImageButton) ((RelativeLayout)
			 * view) .getChildAt(1); // 隐藏加号“+”
			 * imageBtn.setVisibility(ImageButton.GONE); }else{ selected.add(i);
			 * isSelected.set(i, true); ImageButton imageBtn = (ImageButton)
			 * ((RelativeLayout) view) .getChildAt(1); // 显示加号“+”
			 * imageBtn.setVisibility(ImageButton.VISIBLE); }
			 */
		}
	};

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// if(hasFocus)
		// Rect frame = new Rect();
		// WelcomeActivity.this.getWindow().getDecorView()
		// .getWindowVisibleDisplayFrame(frame);
		// int statusBarHeight = frame.top;
		// int contentTop = WelcomeActivity.this.getWindow()
		// .findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// 状态栏的高度
		LayoutValue.TITLEHEIGHT = LayoutValue.SCREEN_HEIGHT
				- ((LinearLayout) findViewById(R.id.rltop)).getMeasuredHeight();
		LogGloble.d("info", "LayoutValue.TITLEHEIGHT-------------  "+LayoutValue.TITLEHEIGHT);
		TextView tv = (TextView)findViewById(R.id.tv);
		int[] location = new int[2];
		tv.getLocationOnScreen(location);
		int y = location[1];
		LayoutValue.SCREEN_BOTTOMHIGHT = LayoutValue.SCREEN_HEIGHT-y;
		
		LogGloble.d("info", "LayoutValue.SCREEN_BOTTOMHIGHT-------------  "+LayoutValue.SCREEN_BOTTOMHIGHT);
	}

	/**
	 * 初始化弹出快捷菜单
	 */
	private void initAddBtnData() {
		icons = new ArrayList<ImageTextAndAct>();
		ImageTextAndAct icon0 = new ImageTextAndAct(R.drawable.add_btn_icon, this
				.getResources().getString(R.string.add_button),
				ConstantGloble.ADD_BUTTON, false);
		icons.add(icon0);

		// 查询数据数 添加到icons里面
		Dao shotcutFileDao = null;
		try {
			shotcutFileDao = getDbHelper().getDao(ShotCutFile.class);
			List<ShotCutFile> list = shotcutFileDao.queryForAll();
			for (int i = 0; i < list.size(); i++) {
				ImageTextAndAct ita = new ImageTextAndAct();
				ita.setImageId(list.get(i).getShotcutIconId());
				ita.setText(list.get(i).getShotcutTextId());
				ita.setActName(list.get(i).getShotcutName());
				icons.add(0, ita);
			}
		} catch (SQLException e) {
			LogGloble.exceptionPrint(e);
		}
	}

	/**
	 * 初始化快捷菜单
	 */
	private void initShotcutIcons() {
		// 初始化一级菜单
		initMainFunction();

		Dao shotcutFileDao = null;
		try {
			shotcutFileDao = getDbHelper().getDao(ShotCutFile.class);
			// ShotCutFile cf2 = (ShotCutFile) shotcutFileDao.queryForId("1");
			List<ShotCutFile> list = shotcutFileDao.queryForAll();
			int len = mainFunction.size();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < len; i++) {
					for (int j = 0; j < list.size(); j++) {
						ShotCutFile sf = list.get(j);
						if (sf.getShotcutName().equals(
								mainFunction.get(i).getActName())) {
							// 删除快捷菜单中有的项
							mainFunction.get(i).setShotcut(true);
						}
					}
				}
			}
			// 删除快捷菜单中有的项
			leftMainFunction = new ArrayList<ImageTextAndAct>();
			for (int i = 0; i < len; i++) {
				if (!mainFunction.get(i).isShotcut()) {
					leftMainFunction.add(mainFunction.get(i));
				}
			}

		} catch (SQLException e) {
			LogGloble.exceptionPrint(e);
		}

		// TODO 带确定是否可以多选
		// isSelected = new ArrayList<Boolean>();
		// for (int i = 0; i < mainFunction.size(); i++) {
		// isSelected.add(false);
		// }

	}

	/**
	 * 初始化一级菜单数据
	 */
	private void initMainFunction() {
		mainFunction = new ArrayList<ImageTextAndAct>();
		// TODO 预留 带确定
		// ImageTextAndAct icon0 = new ImageTextAndAct(R.drawable.icon1, "中银资讯",
		// "com.chinamworld.bocmbci.biz.login.LoginActivity");
		// ImageTextAndAct icon1 = new ImageTextAndAct(R.drawable.icon2, "掌聚生活",
		// "AccManageActivity");
		// ImageTextAndAct icon2 = new ImageTextAndAct(R.drawable.icon3, "地图查询",
		// "MyTransferActivity");
		// ImageTextAndAct icon3 = new ImageTextAndAct(R.drawable.icon4, "手机炒股",
		// "LoanAccountListActivity");
		// ImageTextAndAct icon5 = new ImageTextAndAct(R.drawable.shotcut2,
		// "添加",
		// "LoanAccountListActivity");
		ImageTextAndAct mainFunction1 = new ImageTextAndAct(R.drawable.icon1,
				this.getResources().getString(R.string.mian_menu1),
				ConstantGloble.MAIN_ACTIVITY1, false);
		ImageTextAndAct mainFunction2 = new ImageTextAndAct(R.drawable.icon2,
				this.getResources().getString(R.string.mian_menu2),
				ConstantGloble.MAIN_ACTIVITY2, false);
		// ImageTextAndAct mainFunction3 = new ImageTextAndAct(R.drawable.icon3,
		// this.getResources().getString(R.string.mian_menu3), "3",false);
		ImageTextAndAct mainFunction4 = new ImageTextAndAct(R.drawable.icon4,
				this.getResources().getString(R.string.mian_menu4),
				ConstantGloble.MAIN_ACTIVITY4, false);
		ImageTextAndAct mainFunction5 = new ImageTextAndAct(R.drawable.icon5,
				this.getResources().getString(R.string.mian_menu5),
				ConstantGloble.MAIN_ACTIVITY5, false);
		// ImageTextAndAct mainFunction6 = new ImageTextAndAct(R.drawable.icon6,
		// this.getResources().getString(R.string.mian_menu6), "6",false);
		// ImageTextAndAct mainFunction7 = new ImageTextAndAct(R.drawable.icon7,
		// this.getResources().getString(R.string.mian_menu7), "7",false);
		ImageTextAndAct mainFunction8 = new ImageTextAndAct(R.drawable.icon8,
				this.getResources().getString(R.string.mian_menu8),
				ConstantGloble.MAIN_ACTIVITY8, false);
		ImageTextAndAct mainFunction9 = new ImageTextAndAct(R.drawable.icon9,
				this.getResources().getString(R.string.mian_menu9),
				ConstantGloble.MAIN_ACTIVITY9, false);
		// ImageTextAndAct mainFunction10 = new
		// ImageTextAndAct(R.drawable.icon10,
		// this.getResources().getString(R.string.mian_menu10), "10",false);
		// ImageTextAndAct mainFunction11 = new
		// ImageTextAndAct(R.drawable.icon13,
		// this.getResources().getString(R.string.mian_menu11), "11",false);
		// ImageTextAndAct mainFunction12 = new
		// ImageTextAndAct(R.drawable.icon15,
		// this.getResources().getString(R.string.mian_menu12), "12",false);
		ImageTextAndAct mainFunction13 = new ImageTextAndAct(R.drawable.icon16,
				this.getResources().getString(R.string.mian_menu13),
				ConstantGloble.MAIN_ACTIVITY13, false);
		mainFunction.add(mainFunction1);
		mainFunction.add(mainFunction2);
		// mainFunction.add(mainFunction3);
		mainFunction.add(mainFunction4);
		mainFunction.add(mainFunction5);
		// mainFunction.add(mainFunction6);
		// mainFunction.add(mainFunction7);
		mainFunction.add(mainFunction8);
		mainFunction.add(mainFunction9);
		// mainFunction.add(mainFunction10);
		// mainFunction.add(mainFunction11);
		// mainFunction.add(mainFunction12);
		mainFunction.add(mainFunction13);
	}


	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}

}
