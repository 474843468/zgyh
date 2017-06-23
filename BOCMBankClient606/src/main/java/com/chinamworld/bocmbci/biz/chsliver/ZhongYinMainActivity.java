package com.chinamworld.bocmbci.biz.chsliver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderMainActivity;
import com.chinamworld.bocmbci.biz.onlineservice.OnlineServiceWebviewActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.fidget.BTCConstant;
import com.chinamworld.bocmbci.fidget.WebViewForUrl;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.Utils;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.adapter.MainPagedDragDropGridAdapter;
import com.chinamworld.bocmbci.widget.adapter.Page;
import com.chinamworld.bocmbci.widget.draggridview.PagedDragDropGrid;
import com.chinamworld.bocmbci.widget.draggridview.PagedDragDropGrid.DrawPage;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;
import com.chinamworld.bocmbci.widget.entity.Item;

/**
 * 掌聚生活的入口类(四种计算机涵盖在里面)
 * 
 * @author xiabaoying
 * 
 *         2013-4-26
 * 
 */
public class ZhongYinMainActivity extends ZhongYinBaseActiviy implements
		OnClickListener, DrawPage {

	private ArrayList<ImageAndText> icons = null;

	private PagedDragDropGrid mainGrid;
	
	private MainPagedDragDropGridAdapter gridAdapter;


	private List<Page> pages = new ArrayList<Page>();

	/** 程序注册的包名 中银 */
	private static final String AppPath_Zhongyin = "gaotimezyjc.viewActivity";
	/** 主页面的Activity的全路径 中银 */
	private static final String ActAllPath_Zhongyin = "gaotimezyjc.viewActivity.WaitingActivity";
	/** 中银主页面的Activity 的名称 */
	private static final String ActName_Zhongyin = "WaitingActivity";
	/** 中银 apk名字 */
	private static final String ApkName_Zhongyin = "zhongyincaifu.mp3";

	/** 程序注册的包名 金汇 */
	private static final String AppPath_Jinhui = "cn.bestv.boc.iforex";
	/** 主页面的Activity的全路径 金汇 */
	private static final String ActAllPath_Jinhui = "cn.bestv.boc.iforex.SplashActivity";
	/** 金汇主页面的Activity 的名称 */
	private static final String ActName_Jinhui = "SplashActivity";
	/** 金汇 apk名字 */
	private static final String ApkName_Jinhui = "jinhui.mp3";
	
//	/**电子地图程序注册的包名*/
//    private static final String AppPath_ditu = "com.mapabc.bc";
//    /**电子地图主页面的Activity的全路径*/
//    private static final String ActAllPath_ditu = "com.mapabc.bc.activity.BCMapabcActivity";
//    /**电子地图主页面的Activity 的名称*/
//    private static final String ActName_ditu = "BCMapabcActivity";
//    /**电子地图 apk名字 */
//	private static final String ApkName_ditu = "BCMapabcPhone_android1.0.mp3";
	
	/**程序注册的包名*/
    private static final String AppPath_ditu = "com.mapabc.bc";
    /**主页面的Activity的全路径*/
    private static final String ActAllPath_ditu = "com.mapabc.bc.activity.BCMapabcActivity";
    /**主页面的Activity 的名称*/
    private static final String ActName_ditu = "BCMapabcActivity";
    /**apkName*/
    private static final String ApkName_ditu ="BCMapabcMobile.mp3";
	
	/** 程序注册的包名 支付 */
	private static final String AppPath_zhifu = "com.chinamworld.electronicpayment";
	/** 主页面的Activity的全路径 支付 */
	private static final String ActAllPath_zhifu = "com.chinamworld.electronicpayment.Logo";
	/** 支付主页面的Activity 的名称 */
	private static final String ActName_zhifu = "Logo";
	/** 支付 apk名字 */
	private static final String ApkName_zhifu = "zhifu.mp3";
    
	/** 绘制底部圆点layout */
	private LinearLayout pointLayout;
	/** 是否处于被删除状态 */
	private boolean isMoving = false;
	private int pagesSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		// 初始化弹出框
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		((Button) findViewById(R.id.ib_top_right_btn)).setVisibility(View.GONE);

		mainGrid = (PagedDragDropGrid) this.findViewById(R.id.main_grid);
		// 初始化控件
		initData();
		// gridAdapter = new FidgetGridAdapter(this, mainGrid, pages);
		
		pagesSize = pages.size();
		
		gridAdapter = new MainPagedDragDropGridAdapter(this, mainGrid, pages);
		mainGrid.setAdapter(gridAdapter);

		// mainGrid.setAdapter(gridAdapter);
		mainGrid.setClickListener(this);

		mainGrid.setDrawPage(this);
		pointLayout = (LinearLayout) this.findViewById(R.id.point_layout);
		refreshPageView();
		//
		// // mainGrid.setAdapter(gridAdapter);
		// mainGrid.setClickListener(this);
		// mainGrid.setDrawPage(this);
		// // TODO 测试打开
		// mainGrid.setLongclickable(true);
		// // 设置删除的监听
		// mainGrid.setDeleteClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// mainGrid.deleteChild();
		// if (mainGrid.getGrid().getChildCount() - 1 ==
		// LayoutValue.FIDGET_STARTCOUNT) {// 代表删除的是剩下的最后一项
		// btnRight.setText(R.string.fidgetaddbtn);
		// btnRight.setTextSize(20);
		// mainGrid.resumeChild();
		// isMoving = false;
		// }
		// refreshPageView();
		// }
		// });
		// pointLayout = (LinearLayout) this.findViewById(R.id.point_layout);
		// gridAdapter = new FidgetGridAdapter(this, mainGrid,
		// BTCFidgetManager.meumList, new String[] { "ItemImage",
		// "ItemText" },
		// new int[] { R.id.ItemImage, R.id.ItemText }, pages);
		// // mainGrid.setAdapter(gridAdapter);
		// if (pages.size() > 1) {
		// refreshPageView();
		// }

		//

	}


	// public void setAdapter() {
	// gridAdapter = new FidgetGridAdapter(this, mainGrid,
	// BTCFidgetManager.meumList, new String[] { "ItemImage",
	// "ItemText" },
	// new int[] { R.id.ItemImage, R.id.ItemText }, pages);
	// mainGrid.setAdapter(gridAdapter);
	// }

	

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		int curPage = mainGrid.currentPage();
		int index = ((Integer) v.getTag()) % 12;
		Item item = gridAdapter.getItem(curPage, index);
		if(item==null){
			return;
		}
		int itemId = (int) (item.getId());

		switch (itemId) {
		case 0:// 服务公告 
			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
					getResources().getString(R.string.zj_wufugonggao));
			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
					BTCConstant.FIDGET_WEB_URL_FUWUGONGGAO);
			startActivity(intent);
			break;
		case 1:// 优惠活动
			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
					getResources().getString(R.string.zj_youhuihuodong));
			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
			BTCConstant.FIDGET_WEB_URL_YOUHUIHUODONG);
			startActivity(intent);
			break;
		case 2:// 优惠商户
			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
					getResources().getString(R.string.zj_youhuishanghu));
			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
					BTCConstant.FIDGET_WEB_URL_YOUHUISHANGHU);
			startActivity(intent);
			break;
		case 3:// ATM查询
			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
					getResources().getString(R.string.zj_atmchaxun));
			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
					BTCConstant.FIDGET_WEB_URL_ATMCHAXUN);
			startActivity(intent);
			break;
		case 4:// 网点查询
			aboutMapQuery(AppPath_ditu, ActAllPath_ditu, ApkName_ditu,
					ActAllPath_ditu);
//			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
//			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
//					getResources().getString(R.string.zj_wangdianchaxun));
//			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
//					BTCConstant.FIDGET_WEB_URL_WANGDIANCHAXUN);
//			startActivity(intent);
//			intent.setClass(ZhongYinMainActivity.this,BCMapabcActivity.class);
//			intent.putExtra("isLogin",BaseDroidApp.getInstanse().isLogin()); // 客户未登录，显示网点预约排队按钮
//			BranchOrderDataCenter.isFromMain = false;
//			startActivity(intent);
			break;
		case 5:// 存贷款利率
			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
					getResources().getString(R.string.zj_cundaikuanlilv));
			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
					BTCConstant.FIDGET_WEB_URL_CUNDAIKUANLILV);
			startActivity(intent);
			break;
		case 6:// 外汇牌价
			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
					getResources().getString(R.string.zj_waihuipaijia));
			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
					BTCConstant.FIDGET_WEB_URL_WAIHUIPAIJIA);
			startActivity(intent);
			break;
		case 7:// 理财产品
			ZhongYinMainActivity.this.startActivity(new Intent(ZhongYinMainActivity.this, BranchOrderMainActivity.class));
//			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
//			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
//					getResources().getString(R.string.zj_licaichanpin));
//			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
//					BTCConstant.FIDGET_WEB_URL_LICAICHANPIN);
//			startActivity(intent);
			break;
		case 8:// 基金净值
			
		
			
//			intent.setClass(ZhongYinMainActivity.this, WebViewForUrl.class);
//			intent.putExtra(BTCConstant.FIDGET_WEB_URL_NAME,
//					getResources().getString(R.string.zj_jijinjingzhi));
//			intent.putExtra(BTCConstant.FIDGET_WEB_URL,
//					BTCConstant.FIDGET_WEB_URL_JIJINJINGZHI);
//			startActivity(intent);
			break;
			
			
		case 9:// 金汇掌中宝 插件集成
			aboutMapQuery(AppPath_Jinhui, ActAllPath_Jinhui, ApkName_Jinhui,
					ActAllPath_Jinhui);
			break;
		case 10:// 中银财富通 插件集成
			aboutMapQuery(AppPath_Zhongyin, ActAllPath_Zhongyin,
					ApkName_Zhongyin, ActAllPath_Zhongyin);
			break;
		case 11:// 存储计算器
			intent.setClass(ZhongYinMainActivity.this, WebViewActivity.class);
			intent.putExtra(ConstantGloble.CALUTOR_TYPE,
					ConstantGloble.CALUTOR_TYPE_CUNCHU);
			startActivity(intent);
			break;
		case 12:// 贷款计算机
			intent.setClass(ZhongYinMainActivity.this, WebViewActivity.class);
			intent.putExtra(ConstantGloble.CALUTOR_TYPE,
					ConstantGloble.CALUTOR_TYPE_DAIKUAN);
			startActivity(intent);

			break;
		case 13:// 基金计算机
			intent.setClass(ZhongYinMainActivity.this, WebViewActivity.class);
			intent.putExtra(ConstantGloble.CALUTOR_TYPE,
					ConstantGloble.CALUTOR_TYPE_JIJIN);
			startActivity(intent);
			break;
		case 14:// 外汇计算机
			intent.setClass(ZhongYinMainActivity.this, WebViewActivity.class);
			intent.putExtra(ConstantGloble.CALUTOR_TYPE,
					ConstantGloble.CALUTOR_TYPE_WAIHUI);
			startActivity(intent);
			break;
		case 15:// 支付 插件集成
			aboutMapQuery(AppPath_zhifu, ActAllPath_zhifu, ApkName_zhifu,
					ActAllPath_zhifu);
			break;
			
		case 16:// 金汇掌中宝
			Utils.aboutMapQuery(ZhongYinMainActivity.this,AppPath_Jinhui, ActAllPath_Jinhui, ApkName_Jinhui,
					 ActAllPath_Jinhui);
			break;
		case 17:// 中银国际证券
			Utils.aboutMapQuery(ZhongYinMainActivity.this,AppPath_Zhongyin, ActAllPath_Zhongyin,
					ApkName_Zhongyin, ActAllPath_Zhongyin);
			break;
			
		case 18:// 网点预约排队
			ZhongYinMainActivity.this.startActivity(new Intent(ZhongYinMainActivity.this, BranchOrderMainActivity.class));
		
			break;
		case 19:// 在线客服
			ZhongYinMainActivity.this.startActivity(new Intent(ZhongYinMainActivity.this, OnlineServiceWebviewActivity.class));
		
			break;
		
		}
		
	
	}

	/** 左上角返回点击事件 */
	public void clickTopLeftClick() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.addView(view);
		return view;
	}

	/**
	 * 初始化图标
	 */
	private void initData() {
		initItem();

		((Button) findViewById(R.id.ib_back)).setVisibility(View.GONE);
		

		((TextView) findViewById(R.id.tv_title)).setText(R.string.zhongyzx);

	}

	private void initItem() {

		List<Item> items = new ArrayList<Item>();
//		items.add(new Item(9,this.getString(R.string.jinhuizhzhb),
//				R.drawable.jinhuizhangzhong));
//		items.add(new Item(10,this.getString(R.string.zhongyincet),
//				R.drawable.zhongyincaifu));
		items.add(new Item(11,this.getString(R.string.calculate_cunchu),
				R.drawable.cunchujisuanqiselecter));
		items.add(new Item(12,this.getString(R.string.calculate_daikuan),
				R.drawable.daikuanjisuanqiselecter));
		items.add(new Item(13,this.getString(R.string.calculate_jijin),
				R.drawable.jijinjisuanqiselecter));
		items.add(new Item(14,this.getString(R.string.calculate_waihui),
				R.drawable.waihuijisuanqiselecter));

		items.add(new Item(1,this.getString(R.string.zj_youhuihuodong),
				R.drawable.iconyouhuihuodongselecter));
		
		items.add(new Item(0,this.getString(R.string.zj_wufugonggao),
				R.drawable.iconfuwugonggaoselecter));
		
		items.add(new Item(2,this.getString(R.string.zj_youhuishanghu),
				R.drawable.iconyouhuishanghuselecter));
//		items.add(new Item(3,this.getString(R.string.zj_atmchaxun),
//				R.drawable.iconatmchaxunselecter));
//		items.add(new Item(4,this.getString(R.string.zj_wangdianchaxun),
//				R.drawable.iconwangdianchaxunselecter));
		items.add(new Item(5,this.getString(R.string.zj_cundaikuanlilv),
				R.drawable.iconcundaikuanlilvselecter));
		
//		items.add(new Item(6,this.getString(R.string.zj_waihuipaijia),
//				R.drawable.iconwaihuipaijiaselecter));
//		items.add(new Item(7,this.getString(R.string.zj_licaichanpin),
//				R.drawable.iconlicaichanpinselecter));
		items.add(new Item(18,this.getString(R.string.order_main_title),
				R.drawable.iconlicaichanpinselecter));
		items.add(new Item(19,this.getString(R.string.online_service),
				R.drawable.icon28selecter));
		
		Page page1 = new Page();
		page1.setItems(items);
		
		Page page2 = new Page();
		List<Item> items2 = new ArrayList<Item>();
		
//		items2.add(new Item(8,this.getString(R.string.zj_jijinjingzhi),
//				R.drawable.iconjijinjingzhi));
		items2.add(new Item(16,this.getString(R.string.jinhuizhzhb),
				R.drawable.jinhuizhangzhong));
		items2.add(new Item(17,this.getString(R.string.zhongyincet),
				R.drawable.zhongyincaifu));
//		items2.add(new Item(15,this.getString(R.string.zj_dianzizhifu),
//				R.drawable.dianzizhifu));

		// items.add(new BTCFidget(this.getString(R.string.jinhuizhzhb),
		// R.drawable.jinhuizhangzhong));
		// items.add(new BTCFidget(this.getString(R.string.zhongyincet),
		// R.drawable.zhongyincaifu));
		// items.add(new BTCFidget(this.getString(R.string.calculate_cunchu),
		// R.drawable.cunchujisuanqi));
		// items.add(new BTCFidget(this.getString(R.string.calculate_daikuan),
		// R.drawable.daikuanjisuanqi));
		// items.add(new BTCFidget(this.getString(R.string.calculate_jijin),
		// R.drawable.jijinjisuanqi));
		// items.add(new BTCFidget(this.getString(R.string.calculate_waihui),
		// R.drawable.waihuijisuanqi));
		page2.setItems(items2);
		
		pages.add(page1);
//		pages.add(page2);

	}

	/**
	 * 
	 * @param appPath
	 *            注册包名
	 * @param actname
	 *            入口Activity 名字
	 * @param apkname
	 *            apk名字 .mp3
	 * @param actAllName
	 *            MainActivity全路径名字
	 */
	private void aboutMapQuery(final String appPath, final String actname,
			final String apkname, final String actAllName) {
		boolean updateFlag = needUpdateOrInstall(appPath, actname);
		String message = getResources().getString(R.string.third_no_instal);

		if (!updateFlag) {// 未安装
			BaseDroidApp.getInstanse().showErrorDialog("", message,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							switch ((Integer) v.getTag()) {
							case CustomDialog.TAG_SURE:// 确定
								try {
									installThreadApp(apkname);
								} catch (Exception e) {
									LogGloble.exceptionPrint(e);
									BaseDroidApp
											.getInstanse()
											.showMessageDialog(
													getResources()
															.getString(
																	R.string.install_failed),
													new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															BaseDroidApp
																	.getInstanse()
																	.dismissMessageDialog();

														}
													});

								}

							}
						}
					});

			// Dialog dialog = new AlertDialog.Builder(FidgetMainActivity.this)
			// .setTitle(getResources().getString(R.string.mention))
			// .setMessage(message)
			// .setPositiveButton(
			// getResources().getString(R.string.confirm),
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int which) {
			// try {
			// installThreadApp(apkname);
			// } catch (Exception e) {
			// LogGloble.exceptionPrint(e);
			// new AlertDialog.Builder(
			// FidgetMainActivity.this)
			// .setTitle(
			// getResources()
			// .getString(
			// R.string.mention))
			// .setMessage(
			// getResources()
			// .getString(
			// R.string.install_failed))
			// .setNegativeButton(
			// getResources()
			// .getString(
			// R.string.confirm),
			// null).create().show();
			// }
			// }
			// })
			// .setNegativeButton(
			// getResources().getString(R.string.cancle), null)
			// .show();

		} else {
			try {
				Intent tIntent = new Intent();
				ComponentName tComp = new ComponentName(appPath, actAllName);
				tIntent.setComponent(tComp);
				tIntent.setAction("Android.intent.action.MAIN");
				tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(tIntent);
			} catch (ActivityNotFoundException e) {
			}
		}
	}

	/**
	 * 判断程序是否安装
	 * 
	 * @param packageName
	 *            包名
	 * @param actName
	 *            入口的页面名称
	 * @return
	 */
	private boolean needUpdateOrInstall(String packageName, String actName) {
		List<PackageInfo> packs = ZhongYinMainActivity.this.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				if (p.packageName.equals(packageName)) {

					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 方法功能说明：把流转换成字符串
	 * 
	 * @param
	 * @return String
	 * @see
	 */
	public static String inputStream2String(InputStream inStream)
			throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {// 把字符串读取到buffer，并判断是否读取到结尾
			outSteam.write(buffer, 0, len);// 把buffer写入到输出流
		}
		outSteam.close();// 读取完毕关闭输出流
		// 把输出流根据UTF-8编码转为字符串对象返回
		return new String(outSteam.toByteArray(), "UTF-8");
	}

	private void installThreadApp(String apkName) throws IOException {
		// 先把文件写到缓存上，再安装，注意要配置权限，如果没有权限则装不上
		File cacheDir = getCacheDir();
		String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
		retrieveApkFromAssets(ZhongYinMainActivity.this, apkName, cachePath);
		chmod("666", cachePath);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + cachePath),
				"application/vnd.android.package-archive");

		startActivity(intent);
	}

	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(command);
		} catch (IOException e) {
			LogGloble.exceptionPrint(e);
		}
	}

	public static boolean retrieveApkFromAssets(Context context,
			String fileName, String path) {
		boolean bRet = false;
		try {
			InputStream is = context.getAssets().open("apks/" + fileName);

			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);

			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			bRet = true;
		} catch (IOException e) {
			LogGloble.exceptionPrint(e);
		}

		return bRet;
	}

	@Override
	public void refreshPageView() {
		pointLayout.removeAllViews();
		int imgWidth = getResources().getDimensionPixelSize(
				R.dimen.main_page_icon_height);
		LayoutParams lp = new LayoutParams(imgWidth, imgWidth);
		int gag = getResources().getDimensionPixelSize(R.dimen.dp_three);
		lp.setMargins(gag, gag, gag, gag);
		if (pages.size() > 1) {
			for (int page = 0; page < pages.size(); page++) {
				ImageView imgView = new ImageView(this);
				if (page == mainGrid.currentPage()) {
					imgView.setBackgroundDrawable(this.getResources()
							.getDrawable(R.drawable.current_page));
				} else {
					imgView.setBackgroundDrawable(this.getResources()
							.getDrawable(R.drawable.other_page));
				}
				imgView.setLayoutParams(lp);
				pointLayout.addView(imgView);
			}
		}
	}

	
	@Override
	public void onBackPressed() {
		if(!BaseDroidApp.isExit){
			BaseDroidApp.isExit = true;
			CustomDialog.toastInCenter(getApplicationContext(), getResources().getString(R.string.exit_first_info));
            // 利用handler延迟发送更改状态信息
			BaseDroidApp.getInstanse().mHandlerExit.sendEmptyMessageDelayed(0, SystemConfig.EXIT_BETWEEN_TIME);
		}else{
			exitApp();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogGloble.d("info", "----FidgetMainActivity---onActivityResult-----");
	}

}
