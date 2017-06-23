package com.boc.bocsoft.mobile.bocmobile.yun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingeryue on 2016年10月27.
 */

public class DataListener {

  public interface  PhotoDownListener{
     void onDown();
  }

  private static List<PhotoDownListener> photoDownListeners = new ArrayList<>();


  public static void addPhotoDownListener(PhotoDownListener listener){
    photoDownListeners.add(listener);
  }

  public static void removePhotoDownListener(PhotoDownListener listener){
    photoDownListeners.remove(listener);
  }

  public static void notifyPhotoDown(){
    for(PhotoDownListener downListener:photoDownListeners){
      downListener.onDown();
    }
  }

}
