package com.chinamworld.bocmbci.biz.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.AbstractMianSetting;
import com.chinamworld.bocmbci.base.application.BaseApplication;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * @ClassName: MainSetting
 * @Description: 读取默认配置
 * @author luql
 * @date 2013-10-9 下午04:16:10
 */
public class MainSetting extends AbstractMianSetting{
	public MainSetting()
	{
		Instance = this;
	}
	private static final String TAG = MainSetting.class.getSimpleName();

	public static final String MAIN = "main.db";
	public static final String MAIN_KEY = "main";
	public static final String VERSION_KEY = "version";
	public static List<Item> mInitItem = null;

	public static final String SECOND_MAIN = "secondmain.db";
	public static final String SECOND_MAIN_KEY = "secondmain";
	public static List<Item> msecdInitItem = null;// 投资理财模块

	/**
	 * 获取主页位置配置 如果 配置版本不匹配初始化原始循序
	 * 
	 * @param context
	 * @return
	 */
	public static List<Item> loadMainItem(Context context) {
		List<Item> result = null;
		try {
			boolean isResetCofig = false;
			if (checkVersion(context, BaseApplication.APP_VERSION_CODE)) {
				// 版本号匹配
				SharedPreferences sp = context.getSharedPreferences(MAIN,
						Context.MODE_PRIVATE);
				String mainKey = sp.getString(MAIN_KEY, "");
				if (!TextUtils.isEmpty(mainKey)) {
					result = getSortItems(context, mainKey);
					boolean checkItem = checkItem(context, result);
					if (!checkItem) {
						// 数据校验失败
						isResetCofig = true;
					}
				}
			} else {
				// 版本号不匹配
				isResetCofig = true;
			}

			if (isResetCofig) {
				clearMainItem(context);
				result = initItem(context);
			}

		} catch (Exception e) {
			// 清空配置
			LogGloble.e(TAG, e.getMessage(), e);
			clearMainItem(context);
			result = null;
		} finally {
			if (result == null || result.isEmpty()) {
				result = initItem(context);
			}
		}
		return result;
	}

	/**
	 * 获取主页位置配置 如果 配置版本不匹配初始化原始循序
	 * 
	 * @param context
	 * @return
	 */
	public static List<Item> loadSecondMainItem(Context context) {
		List<Item> result = null;
		try {
			boolean isResetCofig = false;
			if (checkVersion(context, BaseApplication.APP_VERSION_CODE)) {
				// 版本号匹配
				SharedPreferences sp = context.getSharedPreferences(SECOND_MAIN, Context.MODE_PRIVATE);
				String mainKey = sp.getString(SECOND_MAIN_KEY, "");
				if (!TextUtils.isEmpty(mainKey)) {
					result = getSecondSortItems(context, mainKey);
					boolean checkItem = checkItem(context, result);
					if (!checkItem) {
						// 数据校验失败
						isResetCofig = true;
					}
				}
			} else {
				// 版本号不匹配
				isResetCofig = true;
			}

			if (isResetCofig) {
				clearsecondMainItem(context);
				result = initsecondItem(context);
			}

		} catch (Exception e) {
			// 清空配置
			LogGloble.e(TAG, e.getMessage(), e);
			clearsecondMainItem(context);
			result = null;
		} finally {
			if (result == null || result.isEmpty()) {
				result = initsecondItem(context);
			}
		}
		return result;
	}

	/**
	 * 检查版本，是否支持
	 * 
	 * @param oldVersion
	 *            旧版本
	 * @param newVersion
	 *            新版本
	 * @return true支持，false不支持
	 */
	public static boolean checkVersion(Context context, int sysVersion) {
		SharedPreferences sp = context.getSharedPreferences(MAIN,
				Context.MODE_PRIVATE);
		int currentVersion = sp.getInt(VERSION_KEY, -1);
		return currentVersion == sysVersion && sysVersion > 0;
	}

	/**
	 * 保存配置文件，如果没有对应位置失败
	 * 
	 * @param context
	 * @param items
	 * @throws IllegalArgumentException
	 */
	public static void saveMainItem(Context context, List<Item> items)
			throws IllegalArgumentException {
		List<Item> initItem = initItem(context);
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = items.size(); i < count; i++) {
			String indexOf = null;
			Item srcItem = items.get(i);
			for (int j = 0; j < initItem.size(); j++) {
				Item dercItem = initItem.get(j);
				ItemPosition srcLocation = srcItem.getLocation();
				ItemPosition dercLocation = dercItem.getLocation();
				if (srcLocation != null && dercLocation != null) {
					if (srcLocation.getPage() == dercLocation.getPage()
							&& srcLocation.getPosition() == dercLocation
									.getPosition()) {
						indexOf = srcLocation.getPage() + "-"
								+ srcLocation.getPosition();
						break;
					}
				}
			}
			if (indexOf == null) {
				throw new IllegalArgumentException("item:" + srcItem.getName()
						+ ",未找到对应的位置");
			}
			sb.append(indexOf);
			if (i < count - 1) {
				sb.append("|");
			}
		}

		SharedPreferences sp = context.getSharedPreferences(MAIN,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(MAIN_KEY, sb.toString());
		edit.putInt(VERSION_KEY, BaseApplication.APP_VERSION_CODE);
		edit.commit();
	}

	/**
	 * 保存配置文件，如果没有对应位置失败 投资理财
	 * 
	 * @param context
	 * @param items
	 * @throws IllegalArgumentException
	 */
	public static void saveSecondMainItem(Context context, List<Item> items) throws IllegalArgumentException {
		List<Item> initItem = initsecondItem(context);
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = items.size(); i < count; i++) {
			String indexOf = null;
			Item srcItem = items.get(i);
			for (int j = 0; j < initItem.size(); j++) {
				Item dercItem = initItem.get(j);
				ItemPosition srcLocation = srcItem.getLocation();
				ItemPosition dercLocation = dercItem.getLocation();
				if (srcLocation != null && dercLocation != null) {
					if (srcLocation.getPage() == dercLocation.getPage()
							&& srcLocation.getPosition() == dercLocation.getPosition()) {
						indexOf = srcLocation.getPage() + "-" + srcLocation.getPosition();
						break;
					}
				}
			}
			if (indexOf == null) {
				throw new IllegalArgumentException("item:" + srcItem.getName() + ",未找到对应的位置");
			}
			sb.append(indexOf);
			if (i < count - 1) {
				sb.append("|");
			}
		}

		SharedPreferences sp = context.getSharedPreferences(SECOND_MAIN, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(SECOND_MAIN_KEY, sb.toString());
		edit.putInt(VERSION_KEY, BaseApplication.APP_VERSION_CODE);
		edit.commit();
	}

	/**
	 * 清空配置文件
	 * 
	 * @param context
	 */
	public static void clearMainItem(Context context) {
		SharedPreferences sp = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.commit();
	}

	public static void clearsecondMainItem(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SECOND_MAIN, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.commit();
	}

	private static List<Item> getSortItems(Context context, String sorts) {
		// page-position
		List<Item> result = new ArrayList<Item>();
		List<Item> initItem = initItem(context);
		String[] ss = sorts.split("\\|");

		if (ss.length == initItem.size()) {
			// 配置文件与初始化长度匹配
			for (int i = 0; i < ss.length; i++) {
				String s = ss[i];
				Item item = initItem.get(i);
				String[] split = s.split("-");
				if (split != null && split.length == 2) {
					int page = Integer.parseInt(split[0]);
					int position = Integer.parseInt(split[1]);
					ItemPosition location = item.getLocation();
					location.setPage(page);
					location.setPosition(position);
					location.setCurrentPage(page);
					location.setCurrentPosition(position);
					result.add(item);
				}
			}
		}
		return result;
	}

	private static List<Item> getSecondSortItems(Context context, String sorts) {
		// page-position
		List<Item> result = new ArrayList<Item>();
		List<Item> initItem = initsecondItem(context);
		String[] ss = sorts.split("\\|");

		if (ss.length == initItem.size()) {
			// 配置文件与初始化长度匹配
			for (int i = 0; i < ss.length; i++) {
				String s = ss[i];
				Item item = initItem.get(i);
				String[] split = s.split("-");
				if (split != null && split.length == 2) {
					int page = Integer.parseInt(split[0]);
					int position = Integer.parseInt(split[1]);
					ItemPosition location = item.getLocation();
					location.setPage(page);
					location.setPosition(position);
					location.setCurrentPage(page);
					location.setCurrentPosition(position);
					result.add(item);
				}
			}
		}
		return result;
	}

	/**
	 * 校验加载后的数据,true为校验通过，false为校验失败
	 * 
	 * @param context
	 *            上下文
	 * @param result
	 *            待校验数据
	 * @return true为校验通过，false为校验失败
	 */
	private static boolean checkItem(Context context, List<Item> result) {
		List<Item> src = result;
		if (src == null) {
			throw new NullPointerException(
					"mainSetting mainkey load result null");
		}
		// 校验个数
		List<Item> initItem = initItem(context);
		if (initItem.size() != src.size()) {
			return false;
		}
		// 校验相同个数
		for (Item destItem : initItem) {
			int ok = 0;
			String name = destItem.getName();
			for (Item srcItem : src) {
				if (name.equals(srcItem.getName())) {
					ok++;
				}
			}
			if (ok != 1) {
				return false;
			}
		}
		return true;

	}

	/**
	 * 获取初始ImageAndText。getInitFastMainName应于getInitFastMainImageAndText对应
	 * 
	 * @param context
	 * @return
	 */
	@Override
	public  List<ArrayList<ImageAndText>> getInitFastMainImageAndText(
			Context context) {
		List<ArrayList<ImageAndText>> result = new ArrayList<ArrayList<ImageAndText>>();
		List<Item> initItem = initItem(context);
		List<Item> initsecondItem = initsecondItem(context);

		for (Item item : initItem) {
			
			String name = item.getName();		
			if (item.getImageAndText() != null && name != null) {
				ArrayList<ImageAndText> imageAndText=new ArrayList<ImageAndText>();
				imageAndText.addAll(item.getImageAndText());
				for(int i=0;i<imageAndText.size();i++){
					if(!imageAndText.get(i).isAddFast()){
						imageAndText.remove(i);
					}
				}
				result.add(imageAndText);
			}
		}
		for (Item item : initsecondItem) {
//			ArrayList<ImageAndText> imageAndText = item.getImageAndText();
//			String name = item.getName();
//			if (imageAndText != null && name != null) {
//				result.add(imageAndText);
//			}
			String name = item.getName();		
			if (item.getImageAndText() != null && name != null) {
				ArrayList<ImageAndText> imageAndText=new ArrayList<ImageAndText>();
				imageAndText.addAll(item.getImageAndText());
				for(int i=0;i<imageAndText.size();i++){
					if(!imageAndText.get(i).isAddFast()){
						imageAndText.remove(i);
					}
				}
				result.add(imageAndText);
			}
		
		}

		return result;
	}

	/**
	 * 获取初始ImageAndText 的name。getInitFastMainName应于getInitFastMainImageAndText对应
	 * 
	 * @param context
	 * @return
	 */
	public  String[] getInitFastMainName(Context context) {
		List<String> result = new ArrayList<String>();
		List<Item> initItem = initItem(context);
		List<Item> initsecondItem = initsecondItem(context);
		for (Item item : initItem) {
			ArrayList<ImageAndText> imageAndText = item.getImageAndText();
			String name = item.getName();
			if (imageAndText != null && name != null) {
				result.add(name);
			}
		}
		for (Item item : initsecondItem) {
			ArrayList<ImageAndText> imageAndText = item.getImageAndText();
			String name = item.getName();
			if (imageAndText != null && name != null) {
				result.add(name);
			}
		}
		String[] ss = new String[result.size()];
		return result.toArray(ss);
	}

	private static void addMainItem(Context context, int nameId, int resId, ArrayList<ImageAndText> imageAndText, boolean isPlugin,String menuId,int ... isRemoveMenuID){
		boolean isContainFlag = false;
		if(isRemoveMenuID.length != 0){
			for(int id :isRemoveMenuID){
				if(id == nameId){
					isContainFlag = true;
					break;
				}
			}
		}
		if(isContainFlag == false){
			mInitItem.add(new Item(context,nameId, resId, imageAndText,isPlugin,menuId));
		}
	}
	/**
	 * 初始化视图
	 * 
	 * @return
	 */
	public static List<Item> initItem(Context context,int ... isRemoveMenuID) {
		if(mInitItem != null && isRemoveMenuID.length == 0) {
			return mInitItem;
		}
		mInitItem = new ArrayList<Item>();
		List<Item> items = new ArrayList<Item>();
		// 账户管理
		addMainItem(context, R.string.mian_menu1, R.drawable.icon1selecter, LocalData.accountManagerlistData,false,"accountManager_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.mian_menu1, R.drawable.icon1selecter, LocalData.accountManagerlistData,false));
		// 转账汇款
		addMainItem(context, R.string.mian_menu2, R.drawable.icon2selecter, LocalData.tranManagerLeftList, false,"tranManager_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.mian_menu2, R.drawable.icon2selecter, LocalData.tranManagerLeftList, false));
		// 存款管理
		addMainItem(context, R.string.mian_menu3, R.drawable.icon3selecter, LocalData.deptStorageCashLeftList,false,"deptStorageCash_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.mian_menu3, R.drawable.icon3selecter, LocalData.deptStorageCashLeftList,false));
		// 跨行资金归集
//		 items.add(new Item(context, R.string.mian_collect,
//		 R.drawable.icon_collect_selecter,
//		 LocalData.CollectLeftList, false));
		 //addMainItem(context, R.string.mian_collect, R.drawable.icon_collect_selecter, LocalData.CollectLeftList,false,isRemoveMenuID);

		// 贷款管理
		addMainItem(context, R.string.mian_menu4, R.drawable.icon4selecter, LocalData.loanLeftMenuList, false,"loan_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.mian_menu4, R.drawable.icon4selecter, LocalData.loanLeftMenuList, false));

		
		// 信用卡
		addMainItem(context, R.string.mian_menu5, R.drawable.icon5selecter, LocalData.myCrcdListData, false,"myCrcd_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.mian_menu5, R.drawable.icon5selecter, LocalData.myCrcdListData, false));

		// 结售汇
		addMainItem(context, R.string.main_menu25, R.drawable.icon21selecter, LocalData.sbRemitLeftList, false,"sbRemit_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.main_menu25, R.drawable.icon21selecter, LocalData.sbRemitLeftList, false));
			 
		// 账单缴付
		addMainItem(context, R.string.mian_menu7, R.drawable.icon7selecter, LocalData.PaymentLeftList, false,"pay_main",isRemoveMenuID);
//     	items.add(new Item(context, R.string.mian_menu7, R.drawable.icon7selecter, LocalData.PaymentLeftList, false));

					
		// 民生服务
		addMainItem(context, R.string.main_menu29,R.drawable.plps_main, LocalData.plpsLeftListData, false,"plps_main",isRemoveMenuID);
//		 items.add(new Item(context, R.string.main_menu29,R.drawable.plps_main, LocalData.plpsLeftListData, false));

		// 中银证券
//			items.add(new Item(context, R.string.zj_zhongyin, R.drawable.icon_zhongyinselecter, null, false));

		// 手机取款
		addMainItem(context, R.string.mian_menu20, R.drawable.iconshoujiqukuanselecter,LocalData.DrawMoneyLeftList, false,"DrawMoney_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.mian_menu20, R.drawable.iconshoujiqukuanselecter,LocalData.DrawMoneyLeftList, false));

	
		// 二维码转账
		addMainItem(context, R.string.main_menu23, R.drawable.icon25selecter, null, false,"qr_transer_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.main_menu23, R.drawable.icon25selecter, null, false));

		// 主动收款
		addMainItem(context, R.string.main_menu24, R.drawable.iconzhudongshoukuanselecter,LocalData.GatherInitiativeLeftList, false,"GatherInitiative_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.main_menu24, R.drawable.iconzhudongshoukuanselecter,LocalData.GatherInitiativeLeftList, false));
			

		// 跨境汇款 -by sunzhi
		addMainItem(context, R.string.main_menu31, R.drawable.remittance_icon, LocalData.crossBorderRemitLeftListData, false,"crossBorderRemit_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.main_menu31, R.drawable.remittance_icon, LocalData.crossBorderRemitLeftListData, false));
					
		// 移动支付
		addMainItem(context, R.string.zj_dianzizhifu, R.drawable.dianzizhifuselecter, null, false,"zhifu_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.zj_dianzizhifu, R.drawable.dianzizhifuselecter, null, false));
		
		// 服务设定
		addMainItem(context, R.string.mian_menu13, R.drawable.icon16selecter, LocalData.settingManagerlistData,false,"settingManager_main",isRemoveMenuID);
//		items.add(new Item(context, R.string.mian_menu13, R.drawable.icon16selecter, LocalData.settingManagerlistData,false));
		
		//远程开户 
		addMainItem(context, R.string.online_open_an_account, R.drawable.yuanchengkaihuselecter, null, false,"remoteOpen_main",isRemoveMenuID);
//	    items.add(new Item(context, R.string.online_open_an_account, R.drawable.yuanchengkaihuselecter, null, false));
		

		//资产管理
		addMainItem(context, R.string.main_menu33, R.drawable.icon33selector, LocalData.list_imageAndText,false,"asssetManager_main",isRemoveMenuID);
		//items.add(new Item(context, R.string.main_menu33, R.drawable.icon33selector, LocalData.list_imageAndText, false));
		
		items = mInitItem;
		
		HashMap<String, Object[]> subNames = new HashMap<String, Object[]>();
		// 初始化位置
		int cow = MainActivity.ROW;
		int column = MainActivity.COLUMN;
		for (int i = 0, size = items.size(); i < size; i++) {
			int page = i / (cow * column);
			int position = (i - page * cow * column);
			Item item = items.get(i);
			item.setLocation(new ItemPosition(page, position));
			ArrayList<ImageAndText> imageAndTexts = item.getImageAndText();
			if (imageAndTexts != null) {
				for (ImageAndText imageAndText : imageAndTexts) {
					boolean containsKey = subNames.containsKey(imageAndText
							.getText());
					if (containsKey) {
						// 检测子模块名称如果有重名，fastShowText前加模块名称[fastShowText快捷方式显示名称]
						// 双向宝-交易查询 ==> 双向宝-双向宝交易查询
						// 外汇-交易查询 ==>外汇-外汇交易查询
						Object[] subImageAndText = subNames
								.get(imageAndText.getText());
						Item oItem = (Item) subImageAndText[0];
						ImageAndText oImageAndText = (ImageAndText) subImageAndText[1];
						oImageAndText.setFastShowText(oItem.getName()
								+ oImageAndText.getText());
						imageAndText.setFastShowText(item.getName()
								+ imageAndText.getText());
					} else {
						Object[] subImageAndText = new Object[2];
						subImageAndText[0] = item;
						subImageAndText[1] = imageAndText;
						subNames.put(imageAndText.getText(),
								subImageAndText);
					}
				}
			}
		}
		subNames.clear();
		mInitItem = items;
		return mInitItem;
	}

	/**
	 * 初始化视图
	 * 
	 * @return
	 */
	public static List<Item> initsecondItem(Context context) {
		if (msecdInitItem == null) {
			List<Item> items = new ArrayList<Item>();
			//现金宝
//			items.add(new Item(context, R.string.main_menu27,
//					 R.drawable.icon27selecter, LocalData.cashBankLeftList, false));
			// 理财计划
			items.add(new Item(context, R.string.mian_menu11, R.drawable.icon13selecter, LocalData.bocinvtManagerLeftList,
					false,"bocinvtManager_main"));
			// 基金
			items.add(new Item(context, R.string.mian_menu10, R.drawable.icon10selecter, LocalData.fincLeftListData, false,"finc_main"));
			// 账户贵金属
			items.add(new Item(context, R.string.mian_menu9, R.drawable.icon9selecter, LocalData.prmsManagerlistData, false,"prmsManage_main"));

			// 结售汇
			items.add(new Item(context, R.string.main_menu25, R.drawable.icon21selecter, null, false,"sbRemit_main"));
			// 双向宝
			items.add(new Item(context, R.string.mian_menu19, R.drawable.icon22selecter,
					LocalData.isForexStorageCashLeftList, false,"isForexStorageCash_main"));
			// 外汇
			items.add(new Item(context, R.string.mian_menu8, R.drawable.icon8selecter, LocalData.forexStorageCashLeftList,
					false,"forexStorageCash_main"));

			// 第三方存管
			items.add(new Item(context, R.string.mian_menu16, R.drawable.iconthridmanageselecter,
					LocalData.thirdManangerLeftListData, false,"thirdMananger_main"));
			// 债券
			items.add(new Item(context, R.string.mian_menu21, R.drawable.iconguozhaiselecter, LocalData.bondLeftListData,
					false,"bond_main"));
			
			// 保险
			items.add(new Item(context, R.string.main_menu26, R.drawable.icon26selecter, LocalData.safetyLeftListData, false,"safety_main"));
			//积利金
			items.add(new Item(context, R.string.main_menu32, R.drawable.icon32selecter, LocalData.goldboundsLeftListData, false,"goldbonusManager_main"));
			//贵金属积存
			items.add(new Item(context, R.string.main_menu34, R.drawable.icon32selecter, LocalData.goldboundsLeftListData, false,"goldstore_main"));
			HashMap<String, Object[]> subNames = new HashMap<String, Object[]>();
			// 初始化位置
			int cow = MainActivity.ROW;
			int column = MainActivity.COLUMN;
			for (int i = 0, size = items.size(); i < size; i++) {
				int page = i / (cow * column);
				int position = (i - page * cow * column);
				Item item = items.get(i);
				item.setLocation(new ItemPosition(page, position));
				ArrayList<ImageAndText> imageAndTexts = item.getImageAndText();
				if (imageAndTexts != null) {
					for (ImageAndText imageAndText : imageAndTexts) {
						boolean containsKey = subNames.containsKey(imageAndText.getText());
						if (containsKey) {
							// 检测子模块名称如果有重名，fastShowText前加模块名称[fastShowText快捷方式显示名称]
							// 双向宝-交易查询 ==> 双向宝-双向宝交易查询
							// 外汇-交易查询 ==>外汇-外汇交易查询
							Object[] subImageAndText = subNames.get(imageAndText.getText());
							Item oItem = (Item) subImageAndText[0];
							ImageAndText oImageAndText = (ImageAndText) subImageAndText[1];
							oImageAndText.setFastShowText(oItem.getName() + oImageAndText.getText());
							imageAndText.setFastShowText(item.getName() + imageAndText.getText());
						} else {
							Object[] subImageAndText = new Object[2];
							subImageAndText[0] = item;
							subImageAndText[1] = imageAndText;
							subNames.put(imageAndText.getText(), subImageAndText);
						}
					}
				}
			}
			subNames.clear();
			msecdInitItem = items;
		}
		return msecdInitItem;
	}

}
