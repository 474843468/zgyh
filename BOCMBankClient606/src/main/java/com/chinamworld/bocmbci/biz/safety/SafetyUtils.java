package com.chinamworld.bocmbci.biz.safety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;

public class SafetyUtils {

	public static final String COUNTRY_AND_CITY = "safetycountryandcity.txt";
	public static final String COUNTRY = "safetycountry.txt";

	/**
	 * 处理返回数据
	 * 
	 * @param result
	 * @return
	 */
//	public static Object httpResponseDeal(Object result) {
//		BiiResponse biiResponse = (BiiResponse) result;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		return biiResponseBody.getResult();
//	}

	/**
	 * 构造spinner数据
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
//	public static List<String> initSpinnerData(List<Map<String, Object>> list, String key) {
//		if (StringUtil.isNullOrEmpty(list)) {
//			return null;
//		}
//		ArrayList<String> mList = new ArrayList<String>();
//		for (int i = 0; i < list.size(); i++) {
//			if (key.equals(Comm.ACCOUNTNUMBER)) {
//				mList.add(StringUtil.getForSixForString((String) list.get(i).get(key)));
//			} else {
//				mList.add((String) list.get(i).get(key));
//			}
//		}
//		return mList;
//	}

	/**
	 * 初始化下拉框
	 * 
	 * @param c
	 * @param sp
	 * @param list
	 */
	public static void initSpinnerView(Context c, Spinner sp, List<String> list) {
		boolean isdefault = true;
		if (StringUtil.isNullOrEmpty(list)) {
			list = new ArrayList<String>();
			list.add("");
			isdefault = false;
		}
		
		if (list.size() == 1) {
			isdefault = false;
			sp.setEnabled(false);
		} else {
			isdefault = true;
			sp.setEnabled(true);
		}
		
		setSpinnerBackground(sp, isdefault);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<ArrayList<String>> mAdapter = new ArrayAdapter(c, R.layout.spinner_item, list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(mAdapter);
	}

	/**
	 * 将银行账号字符串列表换成四六四格式
	 * 
	 * @param mapList
	 * @return
	 */
	public static List<String> parseStringListToForSixFor(List<String> list) {
		if (!StringUtil.isNullOrEmpty(list)) {
			List<String> arrayList = new ArrayList<String>();
			for (String string : list) {
				String str = StringUtil.getForSixForString(string);
				if (str != null)
					arrayList.add(str);
			}
			return arrayList;
		} else {
			return new ArrayList<String>();
		}
	}

	/**
	 * 读取assets目录文件
	 * 
	 * @param c
	 * @param fileName
	 * @return
	 */
//	public static String readAssetsFile(Context c, String fileName) {
//		String result = null;
//		InputStream in;
//		try {
//			in = c.getAssets().open(fileName);
//			int length = in.available();
//			byte[] buffer = new byte[length];
//			in.read(buffer);
//			result = EncodingUtils.getString(buffer, "UTF-8");
//		} catch (IOException e) {
//			LogGloble.exceptionPrint(e);
//		}
//		return result;
//	}

	/**
	 * 读取城市列表
	 * 
	 * @param c
	 * @param key父code
	 * @return
	 */
	public static List<Map<String, Object>> getCountryAndCity(Context c, String key) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			JSONObject jb = new JSONObject(Utils.readAssetsFile(c, COUNTRY_AND_CITY));
			if (jb.has(key)) {
				JSONArray ja = (JSONArray) jb.getJSONArray(key);
				for (int i = 0; i < ja.length(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					JSONObject object = (JSONObject) ja.opt(i);
					map.put(Safety.NAME, object.getString(Safety.NAME));
					map.put(Safety.CODE, object.getString(Safety.CODE));
					map.put(Safety.PARENTCODE, object.getString(Safety.PARENTCODE));
					list.add(map);
				}
			}
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
		return list;
	}

	/**
	 * 读取国籍
	 * 
	 * @param c
	 * @return
	 */
	public static List<Map<String, Object>> getCountry(Context c) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			JSONObject jb = new JSONObject(Utils.readAssetsFile(c, COUNTRY));
			JSONArray ja = (JSONArray) jb.getJSONArray(Safety.COUNTRY);
			for (int i = 0; i < ja.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject object = (JSONObject) ja.opt(i);
				map.put(Safety.NAME, object.getString(Safety.NAME));
				map.put(Safety.CODE, object.getString(Safety.CODE));
				map.put(Safety.SELECT, false);
				list.add(map);
			}
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
		return list;
	}

	/**
	 * 初始化城市数据
	 * 
	 * @param c
	 * @param newInsurance
	 */
	public static void initCityData(Context c, boolean newInsurance) {
		SafetyDataCenter.getInstance().setProviceList(SafetyUtils.getCountryAndCity(c, Safety.INIT_PRO));
		SafetyDataCenter.getInstance().setProviceListHS(SafetyUtils.getCountryAndCity(c, Safety.INIT_PRO_HS));
		if (newInsurance) {
			initNewInsuranceAdress(c);
		} else {
			Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
			String applProvince = (String) map.get(Safety.APPL_PROVINCE);
			String applCity = (String) map.get(Safety.APPL_CITY);
			String benProvince = (String) map.get(Safety.BEN_PROVINCE);
			String benCity = (String) map.get(Safety.BEN_CITY);
			if (StringUtil.isNull(applProvince) || StringUtil.isNull(applCity)) {
				List<Map<String, Object>> cityList = SafetyUtils.getCountryAndCity(c, Safety.INIT_CITY);
				List<Map<String, Object>> countryList = SafetyUtils.getCountryAndCity(c, Safety.INIT_COUNTRY);
				SafetyDataCenter.getInstance().setSecondCityList(cityList);
				SafetyDataCenter.getInstance().setSecondCountryList(countryList);
			} else {
				SafetyDataCenter.getInstance().setSecondCityList(getCountryAndCity(c, applProvince));
				SafetyDataCenter.getInstance().setSecondCountryList(getCountryAndCity(c, applCity));
			}
			if (StringUtil.isNull(benProvince) || StringUtil.isNull(benCity)) {
				List<Map<String, Object>> cityList = SafetyUtils.getCountryAndCity(c, Safety.INIT_CITY);
				List<Map<String, Object>> countryList = SafetyUtils.getCountryAndCity(c, Safety.INIT_COUNTRY);
				SafetyDataCenter.getInstance().setThirdCityList(cityList);
				SafetyDataCenter.getInstance().setThirdCountryList(countryList);
				SafetyDataCenter.getInstance().setThirdCityListHS(cityList);
				SafetyDataCenter.getInstance().setThirdCountryListHS(countryList);
			} else {
				SafetyDataCenter.getInstance().setThirdCityList(getCountryAndCity(c, (String) map.get(Safety.BEN_PROVINCE)));
				SafetyDataCenter.getInstance().setThirdCountryList(getCountryAndCity(c, (String) map.get(Safety.BEN_CITY)));
			}
			if (!StringUtil.isNull((String) map.get(Safety.HOUSE_PROVINCE))) {
				SafetyDataCenter.getInstance().setThirdCityListHS(getCountryAndCity(c, (String) map.get(Safety.HOUSE_PROVINCE)));
			}
			if (!StringUtil.isNull((String) map.get(Safety.HOUSE_CITY))) {
				SafetyDataCenter.getInstance().setThirdCountryListHS(getCountryAndCity(c, (String) map.get(Safety.HOUSE_CITY)));
			}
//			initNotNewInsuranceAdress(c);
		}
	}
	
	/** 初始化默认省市城区下拉框数据 */
	private static void initNewInsuranceAdress(Context c) {
		List<Map<String, Object>> cityList = SafetyUtils.getCountryAndCity(c, Safety.INIT_CITY);
		List<Map<String, Object>> countryList = SafetyUtils.getCountryAndCity(c, Safety.INIT_COUNTRY);
		SafetyDataCenter.getInstance().setSecondCityList(cityList);
		SafetyDataCenter.getInstance().setSecondCountryList(countryList);
		SafetyDataCenter.getInstance().setThirdCityList(cityList);
		SafetyDataCenter.getInstance().setThirdCountryList(countryList);
		SafetyDataCenter.getInstance().setThirdCityListHS(cityList);
		SafetyDataCenter.getInstance().setThirdCountryListHS(countryList);
	}
	
	/** 初始化指定省市城区下拉框数据 */
//	private void initNotNewInsuranceAdress(Context c) {
//		Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
//		SafetyDataCenter.getInstance().setSecondCityList(getCountryAndCity(c, (String) map.get(Safety.APPL_PROVINCE)));
//		SafetyDataCenter.getInstance().setSecondCountryList(getCountryAndCity(c, (String) map.get(Safety.APPL_CITY)));
//		SafetyDataCenter.getInstance().setThirdCityList(getCountryAndCity(c, (String) map.get(Safety.BEN_PROVINCE)));
//		SafetyDataCenter.getInstance().setThirdCountryList(getCountryAndCity(c, (String) map.get(Safety.BEN_CITY)));
//		if (!StringUtil.isNull((String) map.get(Safety.HOUSE_PROVINCE))) {
//			SafetyDataCenter.getInstance().setThirdCityListHS(getCountryAndCity(c, (String) map.get(Safety.HOUSE_PROVINCE)));
//		}
//		if (!StringUtil.isNull((String) map.get(Safety.HOUSE_CITY))) {
//			SafetyDataCenter.getInstance().setThirdCountryListHS(getCountryAndCity(c, (String) map.get(Safety.HOUSE_CITY)));
//		}
//	}

	/**
	 * 根据RadioButton状态反显
	 * 
	 * @param raBtn1
	 * @param raBtn2
	 * @return
	 */
	public static String getTextFromRaBtn(RadioButton raBtn1, RadioButton raBtn2) {
		if (raBtn1 == null || raBtn2 == null) {
			return "";
		}
		if (raBtn1.isChecked()) {
			return raBtn1.getText().toString();
		}
		if (raBtn2.isChecked()) {
			return raBtn2.getText().toString();
		}
		return "";
	}

	/**
	 * 把字符串 转成 ｂｏｏｌｅａｎ 类型 1:true 0:false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean parseStrToBoolean(String str) {
		if (str == null) {
			return false;
		}
		if (str.equals("0")) {
			return false;
		}
		if (str.equals("1")) {
			return true;
		}
		return false;

	}

	/**
	 * 性别上传数据
	 * 
	 * @param rb
	 * @return
	 */
	public static String getGenderTorq(RadioButton rb) {
		if (rb.isChecked()) {
			return "1";
		}
		return "0";
	}

	/**
	 * 家财险判断
	 * 
	 * @param productType
	 * @return
	 */
	public static boolean isHouseType(String productType) {
		if (StringUtil.isNull(productType)) {
			return false;
		}
		if (productType.equals("0") || productType.equals("4")) {
			return true;
		}
		if (productType.equals("1") || productType.equals("5")) {
			return false;
		}
		return false;
	}

	/**
	 * 单选按钮选中
	 * 
	 * @param btnY
	 *            是
	 * @param btnN
	 *            否
	 * @param tag
	 */
	public static void settRadioButton(RadioButton btnY, RadioButton btnN, String tag) {
		if (StringUtil.isNull(tag)) {
			return;
		}
		if (tag.equals("1")) {
			btnY.setChecked(true);
			btnN.setChecked(false);
		} else if (tag.equals("Y")) {
			btnY.setChecked(true);
			btnN.setChecked(false);
		} else if (tag.equals("0")) {
			btnY.setChecked(false);
			btnN.setChecked(true);
		} else if (tag.equals("N")) {
			btnY.setChecked(false);
			btnN.setChecked(true);
		} else if (tag.equals("2")) {
			btnY.setChecked(false);
			btnN.setChecked(true);
		}
	}

	public static void spinnerSetText(Spinner sp, List<String> list, String rpdata) {
		if (StringUtil.isNull(rpdata)) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (rpdata.equals(list.get(i))) {
				sp.setSelection(i);
			}
		}
	}

	public static void setCitySpinnerText(Spinner sp, List<Map<String, Object>> list, String rpdata) {
		if (StringUtil.isNull(rpdata)) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (rpdata.equals(list.get(i).get(Safety.CODE))) {
				sp.setSelection(i);
			}
		}
	}

	/**
	 * 前往目的地数据读取
	 * 
	 * @param key
	 * @return
	 */
	public static String getDestinationInfo(String key) {
		List<Map<String, String>> countyList = SafetyDataCenter.getInstance().getCountyList();
		if (StringUtil.isNullOrEmpty(countyList)) {
			return "";
		}
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < countyList.size(); i++) {
			b.append(countyList.get(i).get(key));
			if (i != countyList.size() - 1) {
				b.append(",");
			}
		}
		return b.toString();
	}

	/**
	 * 读取文本数据
	 * 
	 * @param v
	 * @return
	 */
	public static String getText(TextView v) {
		if (v != null) {
			return v.getText().toString().trim();
		}
		return "";
	}

	/**
	 * 循环遍历设置浮动框
	 * 
	 * @param textViews控件列表
	 */
	public static void setOnShowAllTextListener(Context context, TextView... textViews) {
		if (textViews == null || textViews.length <= 0)
			return;
		for (TextView tv : textViews) {
			if (tv != null)
				PopupWindowUtils.getInstance().setOnShowAllTextListener(context, tv);
		}
	}

	/**
	 * 设置sipnner背景
	 * 
	 * @param v
	 * @param isdefault
	 */
	public static void setSpinnerBackground(View v, boolean isdefault) {
		if (v != null) {
			if (isdefault) {
				v.setClickable(true);
				v.setBackgroundResource(R.drawable.bg_spinner);
			} else {
				v.setClickable(false);
				v.setBackgroundResource(R.drawable.bg_spinner_default);
			}
		}
	}

	/**
	 * 根据身份证获取性别
	 * 
	 * @param identity
	 * @return 男 1； 女0
	 */
	public static String getGender(String identity) {
		int value = 0;
		if (StringUtil.isNull(identity)) {
			return "";
		}
		int length = identity.length();
		if (length == 15) {
			value = Integer.valueOf(identity.substring(14, 15));
		}
		if (length == 18) {
			value = Integer.valueOf(identity.substring(16, 17));
		}
		return String.valueOf(value % 2);
	}

	/**
	 * 根据身份证获取出生日期
	 * 
	 * @param identity
	 * @return
	 */
	public static String getBirthday(String identity) {
		if (StringUtil.isNull(identity)) {
			return "";
		}
		StringBuffer b = new StringBuffer();
		int length = identity.length();
		if (length == 15) {
			b.append("19");
			b.append(identity.substring(6, 8));
			b.append("/");
			b.append(identity.substring(8, 10));
			b.append("/");
			b.append(identity.substring(10, 12));
		}
		if (length == 18) {
			b.append(identity.substring(6, 10));
			b.append("/");
			b.append(identity.substring(10, 12));
			b.append("/");
			b.append(identity.substring(12, 14));
		}
		return b.toString();
	}

	/** 保险起期时间校验 **/
	public static boolean checkTime(String startDate, String endDate, String tip) {
		String oneDayLater = QueryDateUtils.getOneDayLater(startDate);
		if (!QueryDateUtils.compareDate(oneDayLater, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(tip + "不能早于等于系统当前日期");
			return false;
		}
		if (!QueryDateUtils.compareDateNextYear(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(tip + "不能晚于系统时间一年");
			return false;
		}
		return true;
	}

	/**
	 * 文本反显
	 * 
	 * @param text
	 * @return
	 */
//	public static String setText(String text) {
//		if (StringUtil.isNull(text.trim())) {
//			return "-";
//		}
//		return text;
//	}
}
