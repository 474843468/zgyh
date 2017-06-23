package com.boc.bocsoft.mobile.bocmobile.buss.system.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by dingeryue on 2016年08月31.
 * 用户头像工具类， 提供头像保存获取
 */
public class UserPortraitUtils {



  public static void saveProtraitAsync(final Bitmap bitmap){
    if(StringUtils.isEmptyOrNull(getLoginName())){
      return;
    }
    BocCloudCenter.getInstance().updateUserPhoto(bitmap);
  }


  public static Observable<Bitmap> loadProtraitOBS(final  String user){
    return Observable.create(new Observable.OnSubscribe<Bitmap>() {
      @Override public void call(Subscriber<? super Bitmap> subscriber) {
        if(subscriber.isUnsubscribed())return;
        subscriber.onNext(loadProtrait(user));
        subscriber.onCompleted();
      }
    });
  }




  public static Bitmap loadProtrait(String user){

    String encodeImgage = BocCloudCenter.getInstance().getUserPhoto(user);

    if(encodeImgage == null || encodeImgage.length() == 0){
      //w未保存图片
      return getDefaultBitmap();
    }
    byte[] decode = Base64.decode(encodeImgage, Base64.NO_WRAP);
    try{
      Bitmap bitmap =   BitmapFactory.decodeByteArray(decode,0,decode.length);
      return bitmap;
    }catch (Exception e){

    }
    return getDefaultBitmap();
  }

  public static  Bitmap getDefaultBitmap(){
    return ((BitmapDrawable)ApplicationContext.getAppContext().getResources().getDrawable(R.drawable.boc_logo)).getBitmap();
  }

  private static String getLoginName(){
    User user = ApplicationContext.getInstance().getUser();
    return  user == null?null:user.getLoginName();
  }
}





