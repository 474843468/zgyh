package com.boc.bocsoft.mobile.bocmobile.base.activity.pdf;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Environment;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dingeryue on 2016年11月21.
 */

public class PDFPresenterImpl implements PDFContract.PDFPresenter {

  private PDFContract.PDFView pdfView;
  private RxLifecycleManager lifecycleManager;
  private int viewSize;
  public PDFPresenterImpl(PDFContract.PDFView pdfView){
    this.pdfView = pdfView;
    lifecycleManager = new RxLifecycleManager();
    setViewSize(ApplicationContext.getInstance().getResources().getDisplayMetrics().widthPixels);
  }

  public void setViewSize(int viewSize){
    if(viewSize>0){
      this.viewSize = viewSize;
    }
  }

  @Override public void loadPDF(Uri mUri) {
    if(mUri==null)return;

    String scheme = mUri.getScheme();

    Observable<Uri> obs = null;

    pdfView.showLoading("正在加载...");

    if(ContentResolver.SCHEME_FILE.equals(scheme)){
      //文件
      ToastUtils.show("功能开发中.....");
    }else if("http".equals(scheme) || "https".equals(scheme)){
      //网络 , 进行下载 ,
      obs = readFromNet(mUri.toString());
    }

    if(obs == null){
      pdfView.closeLoading();
      return;
    }
    obs.observeOn(AndroidSchedulers.mainThread())
        .compose(lifecycleManager.<Uri>bindToLifecycle())
        .subscribe(new Subscriber<Uri>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable throwable) {
        pdfView.closeLoading();
        pdfView.onLoadFaile();
      }

      @Override public void onNext(Uri uri) {
        pdfView.closeLoading();
        pdfView.onLoadSuccess(uri);
      }
    });
  }

  private Observable<Uri> readFromNet(final String path){
    if(path == null)return null;
    LogUtils.d("dding","load:"+path);
    return Observable.just(path).compose(lifecycleManager.<String>bindToLifecycle())
        .delaySubscription(500, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .flatMap(new Func1<String, Observable<Response>>() {
          @Override public Observable<Response> call(String path) {
            return  BIIClient.instance.get(path);
          }
        }).map(new Func1<Response, byte[]>() {
          @Override public byte[] call(Response response) {
            if(response.isSuccessful()){
              try {
                return response.body().bytes();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            return null;
          }
        }).map(new Func1<byte[], Uri>() {
          @Override public Uri call(byte[] bytes) {
            if(bytes == null)return null;

            LogUtils.d("dding","---changding:"+bytes.length);

            File file = new File(Environment.getExternalStorageDirectory(),"/boc/tmp.pdf");
            if(file.exists()){
              file.delete();
            }
            file.getParentFile().mkdirs();
            FileOutputStream os = null;
            try {
              os = new FileOutputStream(file);

              os.write(bytes,0,bytes.length);

              os.flush();
              os.close();

              return Uri.fromFile(file);

            } catch (Exception e) {
              e.printStackTrace();
            }

            return null;

          }
        });

  }

  @Override public void subscribe() {

  }

  @Override public void unsubscribe() {
    lifecycleManager.onDestroy();
  }
}
