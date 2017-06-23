package com.chinamworld.bocmbci.commonlibtools;

import android.content.Context;

import com.chinamworld.boc.commonlib.SaveDataManager;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.audio.AudioKeyManager;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.Map;

public class DataManager extends SaveDataManager{

	public DataManager(){
		instance = this;
	}



	@Override
	public void saveSegmentId(String segmentId) {
		BaseDroidApp.getInstanse().setSegmentInfo(segmentId);
	}
	@Override
	public void saveLoginCommonInfo(Map<String, Object> info) {
		BaseDroidApp.getInstanse().getBizDataMap().put("login_result_data", info);
	}
	@Override
	public void saveLoginInfo(String str) {
		BaseDroidApp.getInstanse().setLoginInfo(str);
	}
	@Override
	public void saveECardFlag(Map<String, Object> map,boolean arg0) {

		if(arg0){
			for(ImageAndText icon: LocalData.deptStorageCashLeftList){
				if(icon.getClassName().equals("com.chinamworld.bocmbci.biz.dept.fiexibleinterest.FiexibleInterestActivity")){
					LocalData.deptStorageCashLeftList.remove(icon);
					break;
				}
			}

					BaseDroidApp.getInstanse().setEcardMap(map);
					ImageAndText icon = new ImageAndText(R.drawable.icon_left_lhjx, "特色存款",true,false,"deptStorageCash_100");
					icon.setClassName("com.chinamworld.bocmbci.biz.dept.fiexibleinterest.FiexibleInterestActivity");
					LocalData.deptStorageCashLeftList.add(3, icon);

		}

	}
	@Override
	public void saveEKeyOpenStatus(boolean hasAudioKey) {
		AudioKeyManager.getInstance().setHasAudioKey(hasAudioKey);
		if(hasAudioKey){
			BaseDroidApp.getInstanse().initAudioReceiver();
		}

	}

	@Override
	public void BindingDeviceForPushServiceSuccess(Context context) {
		PushDevice pd = PushDevice.load();
		PushManager.getInstance(context).setPushDevice(pd);
		PushManager.getInstance(context).restartPushService();
	}


}
