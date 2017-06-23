package com.chinamworld.bocmbci.biz.blpt.bdlocation;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.biz.blpt.BillPaymentBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 百度移动定位
 * 
 * @author panwe
 * 
 */
public class BDLocationCenter {

	private final String BD_SERVICE_NAME = "com.baidu.location.service_v2.9";
	private final String BD_ADDRESS_TYPE = "all";
	private final int VIBRATE = 1000;
	public LocationClient mLocationClient;
	private Vibrator mVibrator;
	private Handler handler;
	private Context cn;

	public BDLocationCenter(Handler h, Context mContext) {
		this.handler = h;
		this.cn = mContext;
		mLocationClient = new LocationClient(cn);
	}

	/**
	 * 百度省份、城市定位
	 */
	public void getLocationInfo() {
		
		mVibrator = (Vibrator) cn.getSystemService(Service.VIBRATOR_SERVICE);
		if (mLocationClient != null && !mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		setLocationOption(mLocationClient);
		// 网络、gps定位
		mLocationClient.requestLocation();
		BDLocationListener bdListener = new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				String city = "", cityId = "";
				String province = "", provinceId = "";
				String area = "", areaId = "";// add by fsm
				if (location == null)
					return;

				LogGloble.i(BillPaymentBaseActivity.TAG,
						"time : = " + location.getTime());
				LogGloble.i(BillPaymentBaseActivity.TAG, "latitude : = "
						+ location.getLatitude());
				LogGloble.i(BillPaymentBaseActivity.TAG, "lontitude : = "
						+ location.getLongitude());

				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					if (location.getProvince() != null) {
						province = location.getProvince();
					}
					if (location.getCity() != null) {
						city = location.getCity();
						cityId = location.getCityCode();
					}
					//add by fsm
					if (location.getDistrict() != null) {
						area = location.getDistrict();
					}

				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					if (location.getProvince() != null) {
						province = location.getProvince();
					}
					if (location.getCity() != null) {
						city = location.getCity();
						cityId = location.getCityCode();
					}
					//add by fsm
					if (location.getDistrict() != null) {
						area = location.getDistrict();
					}
				}
				LogGloble.i(BillPaymentBaseActivity.TAG, "province : = "
						+ province);
				LogGloble.i(BillPaymentBaseActivity.TAG, "city : = " + city);

				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString(Blpt.KEY_CITY, city);b.putString(Blpt.KEY_CITY_CODE, cityId);
				b.putString(Blpt.KEY_PROVICENAME, province);
				b.putString(Blpt.KEY_PROVICENAME_CODE, provinceId);
				b.putString(Blpt.KEY_AREA, area);b.putString(Blpt.KEY_AREA_CODE, areaId);
				msg.setData(b);
				handler.sendMessage(msg);
			}

			public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null) {
					return;
				}
			}
		};
		mLocationClient.registerLocationListener(bdListener);
	}

	/**
	 * 相关参数设定
	 * 
	 * @param lc
	 */
	private void setLocationOption(LocationClient lc) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setServiceName(BD_SERVICE_NAME);
		option.setPoiExtraInfo(true);
		option.setAddrType(BD_ADDRESS_TYPE);
		option.setPriority(LocationClientOption.GpsFirst); // 不设置，默认是gps优先
		option.setPoiNumber(10);
		option.disableCache(true);
		lc.setLocOption(option);
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			mVibrator.vibrate(VIBRATE);
		}
	}
}
