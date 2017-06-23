package com.chinamworld.bocmbci.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderCustomerActivity;

/**
 * 网点排队服务
 * @author xby
 */
public class MapQueueService extends Service {
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//启动网点排队页面  
		//TODO 1,需要修改跳转页面名称   2,考虑是否根据当前停留页面做页面跳转处理
		Intent it = new Intent(MapQueueService.this, BranchOrderCustomerActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
		//传递网点信息数据
		it.putExtra(Comm.MAPQUEUEINFO_KEY, intent.getStringExtra(Comm.MAPQUEUEINFO_KEY));
		startActivity(it);
		//完成启动后关闭当前服务
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
