package com.chinamworld.bocmbci.biz.infoserve.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.MyJSON;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDataCenter;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDetialActivity;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeMainActivity;
import com.chinamworld.bocmbci.biz.push.DefaultNotificationBuilder;
import com.chinamworld.bocmbci.biz.push.PushConfig;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.j256.ormlite.stmt.query.In;

import java.util.HashMap;
import java.util.Map;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.BocCommonTools;
/**
 * Created by Administrator on 2016/9/8.
 */
public class PushReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static String deviceId=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogGloble.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                LogGloble.d("PushReceiver","第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    LogGloble.d("PushReceiver", "receiver payload : " + data);
//                    Toast.makeText(context,"receiverMessage=="+ data,Toast.LENGTH_LONG).show();// tost clientid
                    doPushMessage(context,data);
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
//                Toast.makeText(context,"clientid=="+ cid,Toast.LENGTH_LONG).show();// tost clientid
                deviceId=cid;
                LogGloble.i("PushReceiver", "cid=="+ cid);
                break;
            case PushConsts.GET_SDKONLINESTATE:
                boolean online = bundle.getBoolean("onlineState");
                LogGloble.d("PushReceiver", "online = " + online);
                break;

            case PushConsts.SET_TAG_RESULT:
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;

            default:
                break;
        }
    }

    private void doPushMessage(Context mContext,String data) {
        LogGloble.i("PushReceiver", "data：" + data);
        try {
        Map<String, Object> resultmap  = MyJSON.parseObject(data, HashMap.class);
        LogGloble.i("PushReceiver", "resultmap=="+ resultmap);
            Map<String, Object> android=( Map<String, Object>)resultmap.get("android");


//            String notification = vip.get(Push.PNS001_Notification_RESULT);
//            String applicationid = vip.get(Push.PNS001_ApplicationID_RESULT);


            String alert=(String)android.get("alert");
            int badge=(Integer) android.get("badge");
            String sourceId=(String)resultmap.get("SourceId");
            String contentId=(String)resultmap.get("ContentId");
            String contentStyle=(String)resultmap.get("ContentStyle");
            String dateTime=(String)resultmap.get("DateTime");

            PushMessage newMessage = new PushMessage();
            newMessage.setContentId(contentId);
            newMessage.setNotification(alert);
            newMessage.setSourceId(sourceId);
            newMessage.setContentStyle(contentStyle);
            newMessage.setApplicationId("");
            newMessage.setDateTime(InfoServeMainActivity.sdf.parse(dateTime).getTime());
            new  BocCommonTools().setBadgeNumber(true,badge);
            Intent intent = null;
//            InfoServeDataCenter dataCenter = InfoServeDataCenter.getInstance();
//            int notifiCacheNum = dataCenter.getCacheNotificationNumber();
//            notifiCacheNum+=1;
//            dataCenter.setNotificationNumber(notifiCacheNum);
            if (badge == 1 &&newMessage.getMessageType() == PushMessage.MessageType.New) {
                // 只有1条并且是特殊消息进入详情
                intent = new Intent(mContext, InfoServeDetialActivity.class);
                intent.putExtra(Push.INTENT_MESSAGE, newMessage);
                intent.putExtra(Push.INTENT_SRC, Push.INTENT_NOTIFICATION);
                // 是否清空记录标志
                intent.putExtra(Push.INTENT_RESET, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                // 有多条进入列表
//                intent = new Intent(mContext, InfoServeMainActivity.class);
//                intent.putExtra(Push.INTENT_MESSAGE, newMessage);
//                intent.putExtra(Push.INTENT_SRC, Push.INTENT_NOTIFICATION);
//                // 是否清空记录标志
//                intent.putExtra(Push.INTENT_RESET, true);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent= new BocCommonTools().getToMessageIntent(mContext);
                intent.putExtra(Push.INTENT_MESSAGE, newMessage);
                intent.putExtra(Push.INTENT_SRC, Push.INTENT_NOTIFICATION);
                intent.putExtra(Push.INTENT_RESET, true);
            }

            String title = String.format(mContext.getText(R.string.notification_title)
                    .toString(), badge);
                     StringBuffer content = new StringBuffer();
            content.append(alert);

            // TODO 根据类型进入不同页面
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addParentStack(InfoServeMainActivity.class);
            stackBuilder.addNextIntent(intent);

            PendingIntent pdIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder binder = DefaultNotificationBuilder.binder(mContext,
                    title, title, content.toString(), R.drawable.icon, pdIntent);
            binder.setContentIntent(pdIntent);

            NotificationManager nm = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationId= SharedPreUtils.getInstance().getInt("notificationId",1);
            nm.notify(notificationId, binder.build());
            LogGloble.i("PushReceiver", "NotificationId=="+ notificationId);
            if(notificationId>50000){
                // 当NotificationId大于5万 重置为1
                SharedPreUtils.getInstance().addOrModifyInt("notificationId",1);
            }else{
                // 当NotificationId小于5万 notificationId+1
                notificationId+=1;
                SharedPreUtils.getInstance().addOrModifyInt("notificationId",notificationId);
            }

//            BaseActivity currentAct = (BaseActivity) BaseDroidApp.getInstanse().getCurrentAct();
//            if (currentAct != null) {
//                currentAct.updateMessageView();
//            }

        } catch (Exception e) {
            LogGloble.exceptionPrint(e);
        }

    }
}
