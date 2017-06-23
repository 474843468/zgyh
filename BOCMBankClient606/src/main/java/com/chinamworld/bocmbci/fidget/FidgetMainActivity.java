package com.chinamworld.bocmbci.fidget;

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
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.draggridview.PagedDragDropGrid;
import com.chinamworld.bocmbci.widget.draggridview.PagedDragDropGrid.DrawPage;

/**
 * 掌聚生活的入口类(四种计算机涵盖在里面)
 * 
 * @author xiabaoying
 * 
 *         2013-4-26
 * 
 */
public class FidgetMainActivity extends FidgetBaseActiviy implements
		/*OnClickListener, */DrawPage {

	private PagedDragDropGrid mainGrid;

//	private FidgetGridAdapter gridAdapter;

//	private List<PageFidget> pages = new ArrayList<PageFidget>();

	/** 绘制底部圆点layout */
	private LinearLayout pointLayout;
	/** 是否处于被删除状态 */
	private boolean isMoving = false;
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

	/** 程序注册的包名 支付 */
	private static final String AppPath_zhifu = "com.chinamworld.electronicpayment";
	/** 主页面的Activity的全路径 支付 */
	private static final String ActAllPath_zhifu = "com.chinamworld.electronicpayment.Logo";
	/** 支付主页面的Activity 的名称 */
	private static final String ActName_zhifu = "Logo";
	/** 支付 apk名字 */
	private static final String ApkName_zhifu = "zhifu.mp3";
	
	
	//集成掌聚apk需要的变量"
	String packName = "com.chinamworld.bocmbca.one";
	ArrayList<String> packlist;
	String fileName = "BOCMPCA.apk";
//	//当前版本versionCode
//
//	int currentVersionCode = 5;

	int oldVersionCode ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.main_activity_zj);

		// 初始化弹出框
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
//		((Button) findViewById(R.id.ib_top_right_btn)).setVisibility(View.GONE);
//
//		mainGrid = (PagedDragDropGrid) this.findViewById(R.id.main_grid);
//		// 初始化控件
//		initData();
		// gridAdapter = new FidgetGridAdapter(this, mainGrid, pages);

//		BTCFidgetManager.setFidgetGrid(FidgetMainActivity.this);
//		back.setVisibility(View.GONE);
//		btn_right.setVisibility(View.GONE);
//		packlist = getPackName();
//		intallAndOpenSoft();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		back.setVisibility(View.GONE);
		btn_right.setVisibility(View.GONE);
		packlist = getPackName();
		intallAndOpenSoft();
	}
	
	private void intallAndOpenSoft(){
		
//		boolean isCopySuccess = copyApkFromAssets(this, fileName, "");
		// 502添加本地判断 掌聚版本更新
		if (packlist.contains(packName)) {
//			String sign = Utils.readAssetsFile(this, "bocmbc_signature.txt");
			String str = Utils.getPackageSignature(this, "com.chinamworld.bocmbca.one");
//			String []tmp = sign.split(";;");
			//掌聚生产签名信息
			String sign = "3082026d308201d6a003020102020453b68559300d06092a864886f70d0101050500307a310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e8a5bfe59f8ee58cba31153013060355040a0c0ce4b8ade59bbde993b6e8a18c31183016060355040b0c0fe7bd91e7bb9ce98791e89e8de983a83115301306035504030c0ce4b8ade59bbde993b6e8a18c3020170d3134303730343130343333375a180f32353037303530313130343333375a307a310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e8a5bfe59f8ee58cba31153013060355040a0c0ce4b8ade59bbde993b6e8a18c31183016060355040b0c0fe7bd91e7bb9ce98791e89e8de983a83115301306035504030c0ce4b8ade59bbde993b6e8a18c30819f300d06092a864886f70d010101050003818d00308189028181009cf447699d305bfc3a8a3a3ead1fa24581e3a933ea3ec79b8a44e8e281ea229a1c0a9e748aee2ae86928d3496b1df11f55f8c9b0fef9a3120e4c0d4b12d495e6abe9cfe464827ae2abc2ff4fb8d3dd39d29e7a5ab4db2c0b1a1814668d364d1f46c955b234b3d58f725c1275d0498384158e0461ee1bb8f91fdee262859f165b0203010001300d06092a864886f70d01010505000381810060238fc3c131567e82945c4e5d696152ff30e568cdf1459351e1b4e699fb5fb36f8bd3402e9e0f64bca30fbfdee1fb41b8b37c6eaa872341ef45281b64e5fc55f572ef253cb876975b41c8fd738269993a649f01e688763b905d169714e3105db54eceab3b389d1ddd6374e2d7c18c51f2dff9a93c9c90ce3e85b6c1b19406b1";
			if(sign.equals(str) == false){
				// 签名不一致报错
				BaseDroidApp.getInstanse().showMessageDialog("签名不一致", new OnClickListener(){

					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissMessageDialog();
						FidgetMainActivity.this.finish();
					}
					
				});
				
				return;
			}
			
			if(oldVersionCode<SystemConfig.currentVersionCode){
				BaseDroidApp.getInstanse().createDialog("","中国银行掌聚生活版本已经更新，请点击“确定”按钮完成版本更新后再继续使用！",new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						boolean isCopySuccess = copyApkFromAssets(
								FidgetMainActivity.this, fileName,
								"");
						if (isCopySuccess) {
							Intent intent = new Intent(
									Intent.ACTION_VIEW);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setDataAndType(
									Uri.parse("file://"
											+ Environment
													.getExternalStorageDirectory()
													.getAbsolutePath()
											+ "/BOCMPCA.apk"),
									"application/vnd.android.package-archive");
							FidgetMainActivity.this
									.startActivity(intent);
							FidgetMainActivity.this.finish();
						} else {
							BaseDroidApp.getInstanse().createDialog("",FidgetMainActivity.this
													.getResources()
													.getString(
															R.string.install_failed), new OnClickListener() {

												@Override
												public void onClick(
														View v) {
													FidgetMainActivity.this
															.finish();
												}
											});
						}
					}
				});
			}else{
				openApp(packName);
				FidgetMainActivity.this.finish();
			}
		} else {
			BaseDroidApp.getInstanse().showErrorDialog(
					getString(R.string.zj_third_no_instal), R.string.cancle, 
					R.string.confirm,new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							switch ((Integer) arg0.getTag()) {
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissMessageDialog();
								FidgetMainActivity.this.finish();
								break;
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissMessageDialog();
//								requestCommConversationId();
								boolean isCopySuccess = copyApkFromAssets(
										FidgetMainActivity.this, fileName,
										"");
								if (isCopySuccess) {
									Intent intent = new Intent(
											Intent.ACTION_VIEW);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setDataAndType(
											Uri.parse("file://"
													+ Environment
															.getExternalStorageDirectory()
															.getAbsolutePath()
													+ "/BOCMPCA.apk"),
											"application/vnd.android.package-archive");
									FidgetMainActivity.this
											.startActivity(intent);
									FidgetMainActivity.this.finish();
								} else {
									BaseDroidApp.getInstanse().createDialog("",FidgetMainActivity.this
															.getResources()
															.getString(
																	R.string.install_failed), new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															FidgetMainActivity.this
																	.finish();
														}
													});
								}
								break;
							}
			
				}
			});
			
		}
	}

	/**
	 * 获取页数
	 * 
	 * @return
	 */
//	private int getPageSize() {
//		// 下载的飞聚的个数
//		int fidgetCount = BTCFidgetManager.meumList.size();
//		return (fidgetCount + LayoutValue.FIDGET_STARTCOUNT - 1) / 12 + 1;
//	}

/*	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);

		int index = (Integer) v.getTag();
		switch ((int) (index)) {
		
		case 0:// 金汇掌中宝 插件集成
			aboutMapQuery(AppPath_Jinhui, ActAllPath_Jinhui, ApkName_Jinhui,
					ActAllPath_Jinhui);
			break;
		case 1:// 中银财富通 插件集成
			aboutMapQuery(AppPath_Zhongyin, ActAllPath_Zhongyin,
					ApkName_Zhongyin, ActAllPath_Zhongyin);
			break;
		}
		if (index >= LayoutValue.FIDGET_STARTCOUNT) {
			BTCFidgetManager.setSelectFidget(index
					- LayoutValue.FIDGET_STARTCOUNT);

			final int uid = 100 BTCFidgetManager.getUpdataID();
			if (uid == -1) {
				BaseDroidApp.getInstanse().showErrorDialog(
						"",
						FidgetMainActivity.this.getResources().getString(
								R.string.severityInf), new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
							}
						});
			} else if (uid == -2) {
				BaseDroidApp.getInstanse().showErrorDialog(
						FidgetMainActivity.this.getResources().getString(
								R.string.orgUpdate), R.string.confirm,
						R.string.cancle, new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								switch ((Integer) v.getTag()) {
								case CustomDialog.TAG_SURE:// 确定
									new BTCFidgetManager()
											.updataJS(FidgetMainActivity.this);
									break;
								}

							}
						});
			} else if (uid == -3) {

				// BTCFidgetManager.removeFidget(FidgetMainActivity.this);

				mainGrid.getGrid().getDeleteViewIndex(v);
				mainGrid.deleteChild();

				// 代表刚刚删除了所在页面的最后一项，页面已经跳转到前一个页面
//				if (gridAdapter.itemCountInPage(mainGrid.currentPage()) == 0) {
//					mainGrid.setmActivePage(mainGrid.currentPage() - 1);
//
//				}
//				refreshPageView();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						FidgetMainActivity.this.getResources().getString(
								R.string.orgStop));
			} else if (uid >= 0) {

				BaseDroidApp.getInstanse().showErrorDialog(
						FidgetMainActivity.this.getResources().getString(
								R.string.orgUpdate), R.string.confirm,
						R.string.cancle, new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
//								switch ((Integer) v.getTag()) {
//								case CustomDialog.TAG_SURE:// 确定
//									new BTCFidgetManager().download(
//											FidgetMainActivity.this, uid);
//									break;
//								}

							}
						});

			} else if (uid == -4) {
				BaseDroidApp.getInstanse().showErrorDialog(
						FidgetMainActivity.this.getResources().getString(
								R.string.orgUpdateVn), R.string.confirm,
						R.string.cancle, new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								switch ((Integer) v.getTag()) {
								case CustomDialog.TAG_SURE:// 确定
//									new BTCFidgetManager().download(
//											FidgetMainActivity.this, uid);
									break;
								}

							}
						});
			} else if (uid == -5) {
				// modify by xxh 2011-09-15 点击每一个特色业务时，弹出一个免责条款提示框
				// modify by xxh 2012-1-8 点击非中行特色业务时，弹出一个免责条款提示框，中行不弹
				try {
//					if (BTCFidgetManager.fidget.getOwned().equals("0")) {
//						Intent intent1 = new Intent();
//						intent1.setClass(FidgetMainActivity.this, BTCWeb.class);
//						FidgetMainActivity.this.startActivity(intent1);
//					} else {
//						BaseDroidApp.getInstanse().showErrorDialog(
//								FidgetMainActivity.this.getResources()
//										.getString(R.string.MZTK_Infomation),
//								R.string.cancle, R.string.confirm,
//								new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										BaseDroidApp.getInstanse()
//												.dismissErrorDialog();
//										switch ((Integer) v.getTag()) {
//										case CustomDialog.TAG_SURE:// 确定
//											Intent intent = new Intent();
//											intent.setClass(
//													FidgetMainActivity.this,
//													BTCWeb.class);
//											FidgetMainActivity.this
//													.startActivity(intent);
//											break;
//										}
//
//									}
//								});
//					}
				} catch (ActivityNotFoundException e) {
					LogGloble.exceptionPrint(e);
				}
				// end
			}

		}
	}*/

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		BTCFidgetManager.initParams(this);
//		((TextView) findViewById(R.id.tv_title)).setText(R.string.fidget);
//		if (BTCFidgetManager.defaultCity != null) {
//			((TextView) findViewById(R.id.tv_title_org)).setText("["
//					+ BTCFidgetManager.defaultCity + "]");
//		} else {
//			((TextView) findViewById(R.id.tv_title_org)).setText("["
//					+ getResources().getString(R.string.org) + "]");
//		}
//
//		BTCFidgetManager.setFidgetGrid(FidgetMainActivity.this);
//		pages.clear();
//		// 移动支付、金汇掌中宝、中银国际证券
//		for (int i = 0; i < getPageSize(); i++) {
//			if (i == 0) {
//				initItem();
//			} else {
//				pages.add(new PageFidget());
//			}
//		}
//		gridAdapter = new FidgetGridAdapter(this, mainGrid,
//				BTCFidgetManager.meumList, new String[] { "ItemImage",
//						"ItemText" },
//				new int[] { R.id.ItemImage, R.id.ItemText }, pages);
//		mainGrid.notifyDataSetChanged(gridAdapter);
//
//		if (StringUtil.isNullOrEmpty(BTCFidgetManager.meumList)) {
//			((TextView) findViewById(R.id.tvHint)).setVisibility(View.VISIBLE);
//		} else {
//			((TextView) findViewById(R.id.tvHint)).setVisibility(View.GONE);
//		}
//		// mainGrid.setAdapter(gridAdapter);
//		mainGrid.setClickListener(this);
//		mainGrid.setDrawPage(this);
//		// TODO 测试打开
//		mainGrid.setLongclickable(true);
//		mainGrid.setLongListner(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				btn_right.setText(R.string.finish);
//				btn_right.setTextSize(Integer.valueOf(getResources().getString(
//						R.string.sipboxtextsize)));
//				isMoving = true;
//			}
//		});
//		// 设置删除的监听
//		mainGrid.setDeleteClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				
//				LogGloble.i("tag", BTCFidgetManager.meumList.get(0).get("ItemText").toString());
//				
//				String orgDelName = BTCFidgetManager.meumList.get(0).get("ItemText").toString();
//				orgDelName = FidgetMainActivity.this.getResources().getString(
//						R.string.orgDelInfopr) + orgDelName + FidgetMainActivity.this.getResources().getString(
//								R.string.orgDelInfoprT);
//				BaseDroidApp.getInstanse().showErrorDialog(
//						orgDelName, R.string.cancle,
//						R.string.confirm, new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								BaseDroidApp.getInstanse().dismissErrorDialog();
//								switch ((Integer) v.getTag()) {
//								case CustomDialog.TAG_SURE:// 确定
//									mainGrid.deleteChild();
//
//									// 代表刚刚删除了所在页面的最后一项，页面已经跳转到前一个页面
//									if (gridAdapter.itemCountInPage(mainGrid
//											.currentPage()) == 0
//											&& mainGrid.currentPage() > 0) {
//										mainGrid.setmActivePage(mainGrid
//												.currentPage() - 1);
//
//									}
//									// 减少-1 控件讲最后的删除图标去掉了
//									if (mainGrid.getGrid().getChildCount() - 1 == LayoutValue.FIDGET_STARTCOUNT) {// 代表删除的是剩下的最后一项
//										btn_right
//												.setText(R.string.fidgetaddbtn);
//										btn_right.setTextSize(Integer
//												.valueOf(getResources()
//														.getString(
//																R.string.sipboxtextsize)));
//										mainGrid.resumeChild();
//										isMoving = false;
//										((TextView) findViewById(R.id.tvHint))
//												.setVisibility(View.VISIBLE);
//									}
//									refreshPageView();
//
//									break;
//
//								}
//
//							}
//						});
//
//			}
//		});
//		pointLayout = (LinearLayout) this.findViewById(R.id.point_layout);
//
//		if (pages.size() > 1) {
//			refreshPageView();
//		}
//		btn_right.setText(R.string.fidgetaddbtn);
//		btn_right.setTextSize(Integer.valueOf(getResources().getString(
//				R.string.sipboxtextsize)));
//		mainGrid.resumeChild();
//		isMoving = false;
//		// BTCFidgetManager.setFidgetGrid(ZhongYinActivity.this,
//		// mainGrid,pages);
//	}

	/** 左上角返回点击事件 */
//	public void clickTopLeftClick() {
//		back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
//	public View addView(int resource) {
//		View view = LayoutInflater.from(this).inflate(resource, null);
//		tabcontent.addView(view);
//		return view;
//	}

	/**
	 * 初始化图标
	 */
//	private void initData() {
//		initItem();
//
//		((Button) findViewById(R.id.ib_back)).setVisibility(View.INVISIBLE);
//		btn_right = ((Button) findViewById(R.id.ib_top_right_btn));
//		btn_right.setText(R.string.fidgetaddbtn);
//		btn_right.setVisibility(View.VISIBLE);
//		btn_right.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if (!isMoving) {// 没有处于删除状态
//
//					LayoutInflater inflater = (LayoutInflater) FidgetMainActivity.this
//							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					View vv = inflater.inflate(R.layout.alert_button, null);
//					vv.setPadding(20, 20, 20, 20);
//					vv.findViewById(R.id.fidgetButton).setOnClickListener(// 增加特色服务
//							new View.OnClickListener() {
//								public void onClick(View v) {
//									// BaseDroidApp.getInstanse()
//									// .dismissMessageDialog();
//									// BTCFidgetManager.setCityId(0);
//									// Intent intent = new Intent();
//									// intent.setClass(ZhongYinActivity.this,
//									// BTCServiceOrgList.class);
//									// ZhongYinActivity.this.startActivity(intent);
									// BaseDroidApp.getInstanse()
//											.dissmissFootChooseDialog();
//									// 获取程序列表
//									new BTCFidgetManager().updataFidget(
//											FidgetMainActivity.this,
//											BTCFidgetManager.defaultCityID);
//								}
//							});
//					vv.findViewById(R.id.orgButton).setOnClickListener(// 服务地区切换
//							new View.OnClickListener() {
//								public void onClick(View v) {
//									// add by QR 2012-01-10
//									BTCFidgetManager.update_DB_index();
//									BaseDroidApp.getInstanse()
//											.dissmissFootChooseDialog();
//									BTCFidgetManager.setCityId(-1);
									// Intent intent = new Intent();
//									intent.setClass(FidgetMainActivity.this,
									// BTCServiceOrgList.class);
//									FidgetMainActivity.this
//											.startActivity(intent);
//								}
//							});
//					vv.findViewById(R.id.cancleButton).setOnClickListener(// 取消
//							new View.OnClickListener() {
//								public void onClick(View v) {
//									BaseDroidApp.getInstanse()
//											.dissmissFootChooseDialog();
//								}
//							});
//
//					BaseDroidApp.getInstanse().showFootChooseDialog(vv);
//				} else {// 处于删除状态
//					btn_right.setText(R.string.fidgetaddbtn);
//					btn_right.setTextSize(Integer.valueOf(getResources()
//							.getString(R.string.sipboxtextsize)));
//					mainGrid.resumeChild();
//					isMoving = false;
//
//				}
//			}
//		});
//
//	}

//	private void initItem() {
//		List<BTCFidget> items = new ArrayList<BTCFidget>();
//		items.add(new BTCFidget(this.getString(R.string.jinhuizhzhb),
//				R.drawable.jinhuizhangzhongselecter));
//		items.add(new BTCFidget(this.getString(R.string.zhongyincet),
//				R.drawable.zhongyincaifuselecter));
//
//		PageFidget page1 = new PageFidget();
//		page1.setItems(items);
//		pages.add(page1);
//
//	}

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
//		if (pages.size() > 1) {
//			for (int page = 0; page < pages.size(); page++) {
//				ImageView imgView = new ImageView(this);
//				if (page == mainGrid.currentPage()) {
//					imgView.setBackgroundDrawable(this.getResources()
//							.getDrawable(R.drawable.current_page));
//				} else {
//					imgView.setBackgroundDrawable(this.getResources()
//							.getDrawable(R.drawable.other_page));
//				}
//				imgView.setLayoutParams(lp);
//				pointLayout.addView(imgView);
//			}
//		}
				}

	@Override
	public void onBackPressed() {
		if (!BaseDroidApp.isExit) {
			BaseDroidApp.isExit = true;
			CustomDialog.toastInCenter(getApplicationContext(), getResources()
					.getString(R.string.exit_first_info));
			// 利用handler延迟发送更改状态信息
			BaseDroidApp.getInstanse().mHandlerExit.sendEmptyMessageDelayed(0,
					SystemConfig.EXIT_BETWEEN_TIME);
		} else {
			exitApp();
		}
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

		} else {
			if(actAllName.equals(ActAllPath_zhifu)){
				startPayPlui();
			}else{
				startOtherApp(new Intent(),appPath, actAllName);
			}
		}
	}

	private void startOtherApp(final Intent tIntent,final String appPath, final String actAllName) {
		try {
			ComponentName tComp = new ComponentName(appPath, actAllName);
			tIntent.setComponent(tComp);
			tIntent.setAction("Android.intent.action.MAIN");
			tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(tIntent);
		} catch (ActivityNotFoundException e) {
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
		List<PackageInfo> packs = FidgetMainActivity.this.getPackageManager()
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

	private void installThreadApp(String apkName) throws IOException {
		// 先把文件写到缓存上，再安装，注意要配置权限，如果没有权限则装不上
		File cacheDir = getCacheDir();
		String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
		retrieveApkFromAssets(this, apkName, cachePath);
		chmod("666", cachePath);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + cachePath),
				"application/vnd.android.package-archive");

		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == ConstantGloble.ACTIVITY_REQUEST_CODE_ZHIFU) {//支付登录返回页面
			startPayPlui();
		}
	}

	/**
	 * 启动移动支付插件
	 */
	private void startPayPlui() {
		//注意启动支付插件时关掉手机银行后台的三分钟计时
		Intent tIntent = new Intent();
		tIntent.putExtra(ConstantGloble.ZHIFU_KEY_TYPE, ConstantGloble.APP_PACAGENAME);
		tIntent.putExtra(ConstantGloble.ZHIFU_KEY_COOKIE, BiiHttpEngine.cookieCurrent);
		tIntent.putExtra(ConstantGloble.ZHIFU_KEY_LOGININFO, BaseDroidApp.getInstanse().getLoginInfo());
		startOtherApp(tIntent, AppPath_zhifu, ActAllPath_zhifu);
	}
	
	/**
	 * 获取手机安装软件的包名
	 * @return
	 */
	private ArrayList<String> getPackName() {

		ArrayList<String> list = new ArrayList<String>();

		List<PackageInfo> packInfo = getPackageManager()
				.getInstalledPackages(0);

		for (int i = 0; i < packInfo.size(); i++) {
			String packageName = packInfo.get(i).packageName;
			list.add(packInfo.get(i).packageName);
			if(packName.equals(packageName)){
				oldVersionCode = packInfo.get(i).versionCode;
			}
		}

		return list;
	}
	
	/**
	 * 打开其他应用程序
	 * @param packageName
	 */
	private void openApp(String packageName) {
		// PackageInfo pi = getPackageManager().getPackageInfo(packageName, 0);

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN);
//		resolveIntent.addCategory(Intent.CATEGORY_DEFAULT);
		resolveIntent.setPackage(packageName);

		List<ResolveInfo> apps = getPackageManager().queryIntentActivities(resolveIntent,0);
		if (StringUtil.isNullOrEmpty(apps)) return;

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			// String packageName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			startActivity(intent);
		}
	}
	
	/**
	 * @param context
	 *            上下文对象
	 * @param fileName
	 *            文件名
	 * @param path
	 *            存放的路径(绝对路径)
	 * @return
	 */
	public boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
//			fileName = "BOCMPCA_production.apk";
			path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/BOCMPCA.apk";
			InputStream is = context.getAssets().open("apks/"+fileName);
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
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}
}
