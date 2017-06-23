package com.chinamworld.bocmbci.http;

import java.io.IOException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Contacts.People;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
/**
 * 获取设备指纹
 * @author  xbybaoying
 *
 * 2013-5-16
 */
@SuppressWarnings("deprecation")
public class GetPhoneInfo {
	private static TelephonyManager tm;
	private static WifiManager wifi;
	private static DisplayMetrics displayMetrics;
	private static String loc;
	private static LocationManager locationManager;
	/** 通信录个数 */
	private static int mContractCount;
	/** 基站cid */
	private static String mphoneDateCid;
	// private static WifiP2pManager wifiP2p ;
	private static ContentResolver resolver;
	private static TelephonyManager mTelNet;

	/**
	 * 初始化系统信息类参数
	 * 
	 * @param act
	 *            Activity对象
	 */
	public static void initActFirst(Activity act) {
		tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
		wifi = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
		// int sdk_level = android.os.Build.VERSION.SDK_INT ;
		/*
		 * // if(sdk_level>14){ wifiP2p =
		 * (WifiP2pManager)act.getSystemService(Context.WIFI_P2P_SERVICE);
		 * channel = wifiP2p.initialize(act, act.getMainLooper(),null); // }
		 */displayMetrics = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		// 设备主要语言
		loc = act.getResources().getConfiguration().locale.getCountry();
		locationManager = (LocationManager) act
				.getSystemService(Context.LOCATION_SERVICE);
		mTelNet = (TelephonyManager) act
				.getSystemService(Context.TELEPHONY_SERVICE);
		resolver = act.getContentResolver();
		WifiInfo mWifiInfo = wifi.getConnectionInfo();
		lastStrength = mWifiInfo.getRssi();// 获取wifi信号强度 int linkSpeed =
		// mWifiInfo.getLinkSpeed()

	}

	/**
	 * 获取手机各种信息 反欺诈上送(每次发送请求都要上从最新的信息) 调用该方法之前先调用initActFirst(Activity
	 * act)，对应用的设备信息类初始化
	 */
	public static void addPhoneInfo(Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		try {
			getPhoneDateCid();
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
		try {
			// IMEI(国际移动设备身份码)
			sb.append("hardwareId=").append(
					tm.getDeviceId() == null ? "" : tm.getDeviceId()).append(
					";");

			// WIFI MAC
			WifiInfo info = wifi.getConnectionInfo();
			sb.append("wiFiMacAddress=").append(
					info.getMacAddress() == null ? "" : info.getMacAddress())
					.append(";");
			// 设备型号 Device Model
			sb.append("deviceModel=").append(
					android.os.Build.MANUFACTURER == null ? ""
							: android.os.Build.MANUFACTURER).append(";");

			// 设备序列号 Device SN
			sb.append("osId=").append(
					tm.getDeviceId() == null ? "" : tm.getDeviceId()).append(
					";");
			// 设备主要语言
			sb.append("Languages=").append(loc == null ? "" : loc).append(";");

			// IMSI(国际移动用户识别码)
			sb.append("simId=").append(
					tm.getSimSerialNumber() == null ? "" : tm
							.getSimSerialNumber()).append(";");
			// 手机分辨率 screenSize

			String stringFenBian = displayMetrics.widthPixels + "*"
					+ displayMetrics.heightPixels;

			sb.append("screenSize=").append(stringFenBian).append(";");
			// // 手机IP地址
			// sb.append("IP:").append(
			// getLocalIpAddress() == null ? "" : getLocalIpAddress()).append(
			// ";");
			// 支持多任务
			sb.append("deviceMultiTaskingSupported=").append("true")
					.append(";");
			// 手机操作系统
			sb.append("deviceSystemName=").append("Android").append(";");
			// 内核版本号 OS Version
			sb.append("deviceSystemVersion=").append(
					android.os.Build.VERSION.RELEASE == null ? ""
							: android.os.Build.VERSION.RELEASE).append(";");
			// 获取基站cid
			sb.append("cellTowerID=").append(mphoneDateCid).append(";");
			// 手机运营商代码
			String numeric = tm.getSimOperator();
			try {
				String mnc = numeric.substring(3, numeric.length());
				sb.append("mnc=").append(mnc == null ? "" : mnc).append(";");
			} catch (Exception e2) {
				// TODO: handle exception
				sb.append("mnc=").append("").append(";");
			}
			// 手机国家码
			sb.append("mcc=").append(
					tm.getNetworkCountryIso() == null ? "" : tm
							.getNetworkCountryIso()).append(";");
			// 通信录个数
			sb.append("numberOfAddressBookEntries=").append(mContractCount)
					.append(";");
			// 手机号
			// 获取邻近的小区号
			List<NeighboringCellInfo> infos = tm.getNeighboringCellInfo();
			String cid = "";
			if (infos != null && infos.size() > 0) {
				cid = "" + infos.get(0).getCid();
			}
			sb.append("locationAreaCode=").append(cid).append(";");
			try {

				// 经纬度
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				criteria.setAltitudeRequired(false);
				criteria.setBearingRequired(false);
				criteria.setCostAllowed(false);
				criteria.setPowerRequirement(Criteria.POWER_LOW);
				String provider = locationManager.getBestProvider(criteria,
						true);
				if (provider != null) {
					Location location = locationManager
							.getLastKnownLocation(provider);
					if (location != null) {
						double lat = location.getLatitude();
						double lng = location.getLongitude();
						double mAccuracy = location.getAccuracy();
						double haiba = location.getAltitude();

						// 经度
						sb.append("longitude=").append("" + lng).append(";");
						// 纬度
						sb.append("latitude=").append("" + lat).append(";");
						// 横向精度
						sb.append("horizontalAccuracy=").append("" + mAccuracy)
								.append(";");
						// 海拔
						sb.append("Altitude=").append("" + haiba).append(";");

						// 时间戳 改： 使用如下方法 要求得到13位
						sb.append("timestamp=").append(
								"" + System.currentTimeMillis()).append(";");

					} else {
						// 经度
						sb.append("longitude=").append("").append(";");
						// 纬度
						sb.append("latitude=").append("").append(";");
						// 横向精度
						sb.append("horizontalAccuracy=").append("").append(";");
						// 海拔
						sb.append("Altitude=").append("").append(";");

						// 时间戳 改： 使用如下方法 要求得到13位
						sb.append("timestamp=").append(
								"" + System.currentTimeMillis()).append(";");
					}
				} else {
					// 经度
					sb.append("longitude=").append("").append(";");
					// 纬度
					sb.append("latitude=").append("").append(";");
					// 横向精度
					sb.append("horizontalAccuracy=").append("").append(";");
					// 海拔
					sb.append("Altitude=").append("").append(";");

					// 时间戳 改： 使用如下方法 要求得到13位
					sb.append("timestamp=").append(
							"" + System.currentTimeMillis()).append(";");
				}
			} catch (Exception e) {
				// TODO: handle exception
				// 经度
				sb.append("longitude=").append("").append(";");
				// 纬度
				sb.append("latitude=").append("").append(";");
				// 横向精度
				sb.append("horizontalAccuracy=").append("").append(";");
				// 海拔
				sb.append("Altitude=").append("").append(";");

				// 时间戳 改： 使用如下方法 要求得到13位
				sb.append("timestamp=").append("" + System.currentTimeMillis())
						.append(";");
			}
			// 信号强度
			sb.append("SignalStrength=").append("" + lastStrength).append(";");

			try {
				WifiInfo wifiInfo = wifi.getConnectionInfo();
				// ssid
				sb.append("SSID=").append(
						wifiInfo.getSSID() == null ? "" : wifiInfo.getSSID())
						.append(";");
				// bbsid
				sb.append("BBSID=").append(
						wifiInfo.getBSSID() == null ? "" : wifiInfo.getBSSID())
						.append(";");

			} catch (Exception e) {
				// TODO: handle exception
				// ssid
				sb.append("SSID=").append("").append(";");
				// bbsid
				sb.append("BBSID=").append("").append(";");

			}
			// channel
			sb.append("Channel=").append("");

			LogGloble.d("info", "设备指纹--- "+sb.toString());
			map.put(ConstantGloble.MBSAPPTMFEDEVICEINFO, URLEncoder.encode(sb
					.toString(), "UTF-8"));
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			map.put(ConstantGloble.MBSAPPTMFEDEVICEINFO, "");
		}

	}

	/**
	 * 获取手机各种信息 反欺诈上送(每次发送请求都要上从最新的信息) 调用该方法之前先调用initActFirst(Activity
	 * act)，对应用的设备信息类初始化
	 */
	public static void addPhoneInfoMapString(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		// 此处测试有问题 如果不给读取联系人的权限，就会导致出错
		try {
			getPhoneDateCid();
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
		try {
			// IMEI(国际移动设备身份码)
			sb.append("hardwareId=").append(
					tm.getDeviceId() == null ? "" : tm.getDeviceId()).append(
					";");

			// WIFI MAC
			WifiInfo info = wifi.getConnectionInfo();
			sb.append("wiFiMacAddress=").append(
					info.getMacAddress() == null ? "" : info.getMacAddress())
					.append(";");
			// 设备型号 Device Model
			sb.append("deviceModel=").append(
					android.os.Build.MANUFACTURER == null ? ""
							: android.os.Build.MANUFACTURER).append(";");

			// 设备序列号 Device SN
			sb.append("osId=").append(
					tm.getDeviceId() == null ? "" : tm.getDeviceId()).append(
					";");
			// 设备主要语言
			sb.append("Languages=").append(loc == null ? "" : loc).append(";");

			// IMSI(国际移动用户识别码)
			sb.append("simId=").append(
					tm.getSimSerialNumber() == null ? "" : tm
							.getSimSerialNumber()).append(";");
			// 手机分辨率 screenSize

			String stringFenBian = displayMetrics.widthPixels + "*"
					+ displayMetrics.heightPixels;

			sb.append("screenSize=").append(stringFenBian).append(";");
			// // 手机IP地址
			// sb.append("IP:").append(
			// getLocalIpAddress() == null ? "" : getLocalIpAddress()).append(
			// ";");
			// 支持多任务
			sb.append("deviceMultiTaskingSupported=").append("true")
					.append(";");
			// 手机操作系统
			sb.append("deviceSystemName=").append("Android").append(";");
			// 内核版本号 OS Version
			sb.append("deviceSystemVersion=").append(
					android.os.Build.VERSION.RELEASE == null ? ""
							: android.os.Build.VERSION.RELEASE).append(";");
			// 获取基站cid
			sb.append("cellTowerID=").append(mphoneDateCid).append(";");
			// 手机运营商代码
			String numeric = tm.getSimOperator();
			try {
				String mnc = numeric.substring(3, numeric.length());
				sb.append("mnc=").append(mnc == null ? "" : mnc).append(";");
			} catch (Exception e2) {
				// TODO: handle exception
				sb.append("mnc=").append("").append(";");
			}
			// 手机国家码
			sb.append("mcc=").append(
					tm.getNetworkCountryIso() == null ? "" : tm
							.getNetworkCountryIso()).append(";");
			// 通信录个数
			sb.append("numberOfAddressBookEntries=").append(mContractCount)
					.append(";");
			// 手机号
			// 获取邻近的小区号
			List<NeighboringCellInfo> infos = tm.getNeighboringCellInfo();
			String cid = "";
			if (infos != null && infos.size() > 0) {
				cid = "" + infos.get(0).getCid();
			}
			sb.append("locationAreaCode=").append(cid).append(";");
			try {

				// 经纬度
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				criteria.setAltitudeRequired(false);
				criteria.setBearingRequired(false);
				criteria.setCostAllowed(false);
				criteria.setPowerRequirement(Criteria.POWER_LOW);
				String provider = locationManager.getBestProvider(criteria,
						true);
				if (provider != null) {
					Location location = locationManager
							.getLastKnownLocation(provider);
					if (location != null) {
						double lat = location.getLatitude();
						double lng = location.getLongitude();
						double mAccuracy = location.getAccuracy();
						double haiba = location.getAltitude();

						// 经度
						sb.append("longitude=").append("" + lng).append(";");
						// 纬度
						sb.append("latitude=").append("" + lat).append(";");
						// 横向精度
						sb.append("horizontalAccuracy=").append("" + mAccuracy)
								.append(";");
						// 海拔
						sb.append("Altitude=").append("" + haiba).append(";");

						// 时间戳 改： 使用如下方法 要求得到13位
						sb.append("timestamp=").append(
								"" + System.currentTimeMillis()).append(";");

					} else {
						// 经度
						sb.append("longitude=").append("").append(";");
						// 纬度
						sb.append("latitude=").append("").append(";");
						// 横向精度
						sb.append("horizontalAccuracy=").append("").append(";");
						// 海拔
						sb.append("Altitude=").append("").append(";");

						// 时间戳 改： 使用如下方法 要求得到13位
						sb.append("timestamp=").append(
								"" + System.currentTimeMillis()).append(";");
					}
				} else {
					// 经度
					sb.append("longitude=").append("").append(";");
					// 纬度
					sb.append("latitude=").append("").append(";");
					// 横向精度
					sb.append("horizontalAccuracy=").append("").append(";");
					// 海拔
					sb.append("Altitude=").append("").append(";");

					// 时间戳 改： 使用如下方法 要求得到13位
					sb.append("timestamp=").append(
							"" + System.currentTimeMillis()).append(";");
				}
			} catch (Exception e) {
				// TODO: handle exception
				// 经度
				sb.append("longitude=").append("").append(";");
				// 纬度
				sb.append("latitude=").append("").append(";");
				// 横向精度
				sb.append("horizontalAccuracy=").append("").append(";");
				// 海拔
				sb.append("Altitude=").append("").append(";");

				// 时间戳 改： 使用如下方法 要求得到13位
				sb.append("timestamp=").append("" + System.currentTimeMillis())
						.append(";");
			}
			// 信号强度
			sb.append("SignalStrength=").append("" + lastStrength).append(";");

			try {
				WifiInfo wifiInfo = wifi.getConnectionInfo();
				// ssid
				sb.append("SSID=").append(
						wifiInfo.getSSID() == null ? "" : wifiInfo.getSSID())
						.append(";");
				// bbsid
				sb.append("BBSID=").append(
						wifiInfo.getBSSID() == null ? "" : wifiInfo.getBSSID())
						.append(";");

			} catch (Exception e) {
				// TODO: handle exception
				// ssid
				sb.append("SSID=").append("").append(";");
				// bbsid
				sb.append("BBSID=").append("").append(";");

			}
			// channel
			sb.append("Channel=").append("");

			map.put(ConstantGloble.MBSAPPTMFEDEVICEINFO, URLEncoder.encode(sb
					.toString(), "UTF-8"));
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			map.put(ConstantGloble.MBSAPPTMFEDEVICEINFO, "");
		}

	}

//	/**
//	 * 获取手机IP地址
//	 * 
//	 * @return
//	 */
//	private static String getLocalIpAddress() {
//		try {
//			for (Enumeration<NetworkInterface> en = NetworkInterface
//					.getNetworkInterfaces();
//
//			en.hasMoreElements();) {
//				NetworkInterface intf = en.nextElement();
//				for (Enumeration<InetAddress> enumIpAddr = intf
//						.getInetAddresses();
//
//				enumIpAddr.hasMoreElements();) {
//					InetAddress inetAddress = enumIpAddr.nextElement();
//					if (!inetAddress.isLoopbackAddress()
//							&& (inetAddress instanceof Inet4Address)) {
//						if (inetAddress.getHostAddress().toString() == null)
//							return "";
//						else
//							return inetAddress.getHostAddress().toString();
//					}
//				}
//
//			}
//		} catch (SocketException ex) {
//			return "";
//		}
//		return "";
//
//	}

	/**
	 * 获取通信录个数
	 * 
	 * @param mContext
	 * @return
	 */
	public static void getPhoneContracts() {
		try {

			// 获取手机联系人
			@SuppressWarnings("deprecation")
			Cursor phoneCursor = resolver.query(People.CONTENT_URI, null, null,
					null, null); // 传入正确的uri
			mContractCount = phoneCursor.getCount();
			phoneCursor.close();
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			mContractCount = 0;
		}
	}

	/**
	 * 获取基站cid
	 * 
	 * @param act
	 */
	public static void getPhoneDateCid() {
		try {
			GsmCellLocation location = (GsmCellLocation) mTelNet
					.getCellLocation();
			mphoneDateCid = String.valueOf(location.getCid());
		} catch (Exception e) {
			mphoneDateCid = "";
			LogGloble.exceptionPrint(e);
		}

	}

	private static BTCMyPhoneStateListener instance;// 栈实例对象
	private static int lastStrength = 0;

	public static class BTCMyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onSignalStrengthChanged(int asu) {
			// TODO Auto-generated method stub
			super.onSignalStrengthChanged(asu);
			lastStrength = asu;

		}

		public static BTCMyPhoneStateListener getMyPhoneStaticListener() {
			if (instance == null) {
				instance = new BTCMyPhoneStateListener();
			}
			return instance;
		}
	}

	public static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

}
