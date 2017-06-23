package com.boc.bocsoft.mobile.bocmobile.yun;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.util.Base64;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.yun.db.AccountDao;
import com.boc.bocsoft.mobile.bocmobile.yun.db.CloudInfoDao;
import com.boc.bocsoft.mobile.bocmobile.yun.db.DictDao;
import com.boc.bocsoft.mobile.bocmobile.yun.db.LoginInfoDao;
import com.boc.bocsoft.mobile.bocmobile.yun.db.UserInfoDao;
import com.boc.bocsoft.mobile.bocmobile.yun.model.AccountServiceBean;
import com.boc.bocsoft.mobile.bocmobile.yun.model.DictVoBean;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.bocmobile.yun.other.DictKey;
import com.boc.bocsoft.mobile.bocmobile.yun.other.YunUtils;
import com.boc.bocsoft.mobile.bocyun.common.client.YunClient;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000001.UserPortraitGetParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000001.UserPortraitGetResult;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.AccountBean;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.UserLastAccountIdGetParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.UserLastAccountIdGetResult;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000009.UBAS000009Param;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000009.UBAS000009Result;
import com.boc.bocsoft.mobile.bocyun.model.UBAS010001.UBAS010001Param;
import com.boc.bocsoft.mobile.bocyun.model.UBAS010001.UBAS010001Result;
import com.boc.bocsoft.mobile.bocyun.service.YunService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 云中心
 * Created by dingeryue on 2016年10月16.
 *  处理所有数据,沟通本地缓存和服务器
 */

public class BocCloudCenter {
  private static String TAG = "yun";
  private static BocCloudCenter center = new BocCloudCenter();
  private BocCloudCenter(){}

  public static BocCloudCenter getInstance() {
    return center;
  }

  private String bocnetCustNo;//网银客户ID
  private String bancsCustNo;//核心客户号
  private String mobilePhPh;//手机号

  private String user;
  /**
   * 初始化方法 ,登录后调用
   * @param bancsCustNo  核心客户号
   * @param bocnetCustNo  网银客户ID
   * @param mobilePh 手机号
   */
  public void init(String bancsCustNo,String bocnetCustNo,String mobilePh){
    LogUtils.d(TAG,"------云初始化 --- 核心:>"+bancsCustNo+"  网银:"+bocnetCustNo+"  手机:"+mobilePh);
    try{
      this.bancsCustNo = bancsCustNo;
      this.bocnetCustNo = bocnetCustNo;
      this.mobilePhPh = mobilePh;
      user = bancsCustNo;

      //netDownImage();
      saveUserInfo();

      netDownDict();
      getYunUpdateInfo();
      UpLoadCenter.getInstance().init(bancsCustNo,bocnetCustNo,mobilePh);

    }catch (Exception e){
      e.printStackTrace();
      LogUtils.d(TAG,"----- 初始化失败 --- "+e.getMessage());
    }

  }

  /**
   * 清除
   * @param user
   */
  public void clear(String user){
     Observable.just("")
         .subscribeOn(Schedulers.io())
         .map(new Func1<String, String>() {
           @Override public String call(String s) {
             try {
               new CloudInfoDao().clearData();
             }catch (Exception e){

             }

             try {
               new UserInfoDao().clearData();
             }catch (Exception e){

             }


             try {
               new AccountDao().clearData();
             }catch (Exception e){

             }


             return "";
           }
         }).onErrorReturn(new Func1<Throwable, String>() {
       @Override public String call(Throwable throwable) {
         return null;
       }
     }).subscribe(new Action1<String>() {
       @Override public void call(String s) {
         LogUtils.d(TAG,"清除成功");
       }
     });
  }

  public void checkUpload(){
    UpLoadCenter.getInstance().check();
  }

  // ----- get --------------------------

  /**
   * 获取用户使用的账户id
   *
   * @param type 类型 {@link AccountType}
   * @return 返回的是accountID 的sha256摘要, 调用者做对比时需要将比较的accountid 进行sha256处理 {{@link
   * #getSha256String(String)}}
   *
   * String accountId = getAccountId(AccountType.ACC_TYPE_NOCARD_DRAW);
   * //注意判空
   * if(StringUtils.isEmptyOrNull(accountId))return  null;
   * //遍历查找,accountid做sha256处理,然后比较
   * for(Account account:accountlist){
   * if(accountId.equals(getSha256String(account.getAccountId()))){
   * return account;
   * }
   * }
   * @deprecated  see {{@link #getUserDict(String)}}
   */
  public String getAccountId(final String type){
    return getUserDict(type);
  }

  /**
   * 获取用户字典数据  ， 私密数据的获取使用参照 getAccountId 注释
   * @param type  {@link AccountType}
   * @return
   */
  public String getUserDict(final String type){
    try{
      return new AccountDao().getAccount(user,type);
    }catch (Exception e){
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 获取码表参数
   * @param key 码表key {@link DictKey}
   * @return 码表value
   */
  public String getDirt(String key){
    try {

      String dictValue = new DictDao().getDictValue(key);
      //ToastUtils.show(key+":"+dictValue);
      LogUtils.d(TAG,"------>key:"+key+"  value:"+dictValue);
      return  dictValue;

    }catch (Exception e){
      e.printStackTrace();

    }

    return "";
  }

  /**
   * 批量 获取码表参数
   * @param keys 码表keys {@link DictKey}
   * @return
   */
  public HashMap<String,String> getDirt(String ... keys){

    try {
      return new  DictDao().getDictValue(keys);

    }catch (Exception e){
      e.printStackTrace();
    }
    return new HashMap<>();
  }


  public String getUserPhoto(String loginName){
   return new UserInfoDao().getBase64Image(loginName);
  }


  // ----- get  结束--------------------------

  // ------ 上传 保存 ------------------------

  /**
   * 更新最新用户字典 加密(使用的账户id)
   * @param type 账号类型 见{@link AccountType}
   * @param accId 账号
   * @deprecated  {{@link #updateUserSecretDict(String, String)}}
   */
  public void updateLastAccountId(final String type, String accId){
    updateUserDict(type,accId,true);
  }

  /**
   * 保存用户私密数据
   * @param key    {@link AccountType}
   * @param value
   */
  public void updateUserSecretDict(String key,String value){
    updateUserDict(key,value,true);
  }

  /**
   * 更新用户字典， 字段不做加密
   * @param key 字典key  {@link AccountType}
   * @param value 字典value
   */
  public void updateUserDict(String key,String value){
    updateUserDict(key,value,false);
  }

  private void updateUserDict(final String key,String value,boolean isSha256){
    if(value == null)return;
    LogUtils.d(TAG,"保存用户字典:"+key+"  "+value+"  是否加密:"+isSha256);

    final  String bancsCustNo = this.bancsCustNo;
    final  String bocnetCustNo = this.bocnetCustNo;
    final  String mobilePhPh = this.mobilePhPh;

    final String account = isSha256?getSha256String(value):value;

    if(account == null || account.length()==0)return;

    final AccountDao dao = new AccountDao();
    Observable.just("").observeOn(Schedulers.io())
        .filter(new Func1<String, Boolean>() {
          @Override public Boolean call(String s) {
            String nowAccount = dao.getAccount(user, key);
            if(account.equals(nowAccount))return false;
            return true;
          }
        }).map(new Func1<String, Object>() {
      @Override public Object call(String s) {
        new AccountDao().updateAccount(bancsCustNo,bocnetCustNo,mobilePhPh,key,account,AccountDao.STATE_NEW);
        return s;
      }
    }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Object>() {
          @Override public void onCompleted() {
            LogUtils.d(TAG,"保存账号成功");
          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
            LogUtils.d(TAG,"保存账户异常:"+key+"  "+account+"  "+e.getMessage());
          }

          @Override public void onNext(Object o) {
            LogUtils.d(TAG,"保存账号成功,开始检查上传");
            UpLoadCenter.getInstance().upLoadAccount();
          }
        });
  }


  /**
   * 更新最新使用的账户id
   * @param list
   */
/*  public void updateLastAccountId(List<AccountServiceBean> list){

    //TODO ....
  }*/


  /**
   * 更新头像
   */
  public void updateUserPhoto(String bitMap){
    try{
      new UserInfoDao().saveLocalImage(bancsCustNo,bitMap);
    }catch (Exception e){

    }
  }

  /**
   * 更新头像
   */
  public void updateUserPhoto(final Bitmap bitMap){
    Observable.just("")
        .subscribeOn(Schedulers.io())
        .map(new Func1<String, Boolean>() {
          @Override public Boolean call(String s) {
            new UserInfoDao().saveLocalImage(bancsCustNo,getBase64Str(bitMap));
            return null;
          }
        }).subscribe(new Subscriber<Boolean>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable throwable) {
      }
      @Override public void onNext(Boolean aBoolean) {
        LogUtils.d("");
        UpLoadCenter.getInstance().upLoadUserPhoto();
      }
    });
  }

  private void saveUserInfo(){
    UserInfoDao userInfoDao = new UserInfoDao();
    userInfoDao.saveUser(mobilePhPh,bancsCustNo,bocnetCustNo);
  }


  /**
   * 采集用户登录信息
   */
  public void updateUserLoginInfo(final boolean isCheck){
    // 收集用户信息
    Observable.just("").subscribeOn(Schedulers.io())
        .map(new Func1<String, String>() {
          @Override public String call(String s) {
            LoginInfoDao dao = new LoginInfoDao();
            ContentValues values = new ContentValues();

            values.put(LoginInfoDao.FIELD_BOCNETCUSTNO,bocnetCustNo);
            values.put(LoginInfoDao.FIELD_BANCSCUSTNO,bancsCustNo);
            values.put(LoginInfoDao.FIELD_MOBILEPH,mobilePhPh);

            values.put(LoginInfoDao.FIELD_LOGINTIME, YunUtils.getFullFormatDate(Calendar.getInstance()));
            values.put(LoginInfoDao.FIELD_RAM, YunUtils.getRam());
            String[] romMemory2 = YunUtils.getRomMemory2();
            values.put(LoginInfoDao.FIELD_ROM,romMemory2[0]);
            values.put(LoginInfoDao.FIELD_USEDROM,romMemory2[1]);
            values.put(LoginInfoDao.FIELD_RESOLUTION,YunUtils.getResolution());
            values.put(LoginInfoDao.FIELD_LOGOUTTIME,"");
            dao.insertRecord(values);

            return s;
          }
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            throwable.printStackTrace();

          }

          @Override public void onNext(String s) {
            if(isCheck){
              UpLoadCenter.getInstance().check();
            }
          }
        });
  }

  /**
   * 用户登出
   */
  public void updateUserLogout(){
    //new LoginInfoDao().updateLoginoutTime(YunUtils.getFullFormatDate(Calendar.getInstance()));
  }


  // ------ 上传 保存结束   ------------------------

  /**
   * 查询云备份信息 , 哪些云备份更新了
   *  一生只有一次。。。
   */
  public void getYunUpdateInfo(){

    final CloudInfoDao infoDao = new CloudInfoDao();
    boolean hasInit = infoDao.hasInit(user);
    if(hasInit){
      LogUtils.d(TAG,"用户:"+user+" 已经初始化过,不用再次初始化");
      checkUpdate();
      return;
    }else{
      netDownYun();
    }
  }


  public void checkUpdate(){
    LogUtils.d(TAG,"检测需要下载的项目:");
    Observable.just("").subscribeOn(Schedulers.io())
        .map(new Func1<String, String>() {
          @Override public String call(String s) {
            final CloudInfoDao infoDao = new CloudInfoDao();

            List<String> needLoadTrCodes = infoDao.getNeedLoadTrCodes(user);

            LogUtils.d(TAG,"----需要下载的项目:"+needLoadTrCodes);
            for(String item:needLoadTrCodes){
               switch (item){
                 case "ubas000001"://用户头像
                   LogUtils.d(TAG,"---yun头型");
                   netDownPhoto();
                   break;
                 case "ubas000005"://上次使用的交易id
                   LogUtils.d(TAG,"---yun头像");
                   netDownAccountIds();
                   break;
                 case "ubas010001"://码表参数
                   LogUtils.d(TAG,"---yun码表");
                   //netDownDict();
                   break;
               }

            }

            return null;
          }
        }).onErrorReturn(new Func1<Throwable, String>() {
      @Override public String call(Throwable throwable) {
        return null;
      }
    }).subscribe(new Action1<String>() {
      @Override public void call(String s) {

      }
    });
  }

  public void netDownYun(){

    UBAS000009Param param = new UBAS000009Param();
    param.setBancsCustNo(user);
    param.setCurStrNo("1");
    param.setOrderCnt("100");

    new YunService().getUserCloudInfo(param).subscribeOn(Schedulers.io())
        .map(new Func1<UBAS000009Result, UBAS000009Result>() {
          @Override public UBAS000009Result call(UBAS000009Result result) {

            LogUtils.d(TAG,"-----云信息请求成功:");
            List<UBAS000009Result.YunItemBen> list = result.getList();

            List<String> array = new ArrayList<>();
            for(UBAS000009Result.YunItemBen ben:list){
              array.add(ben.getTrCode());
            }
            new CloudInfoDao().updateTrades(user,array);
            LogUtils.d(TAG,"-----云信息保存成功:");

            return result;
          }
        })
        .subscribe(new Subscriber<UBAS000009Result>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            LogUtils.d(TAG,"-----云信息保存失败:");
          }

          @Override public void onNext(UBAS000009Result result) {
            checkUpdate();
          }
        });
  }

  private void netDownPhoto(){
    LogUtils.d(TAG,"检测到有头像需要下载:");

    //--- 子线程
    final UserInfoDao dao = new UserInfoDao();
    final String user = bancsCustNo;

    String imageUrl = dao.getImageUrl(user);

    Observable<String> downUrlObs = null;
    if(imageUrl != null && imageUrl.length()>5){
      downUrlObs = Observable.just(imageUrl);
      LogUtils.d(TAG,"头像地址已经获取,直接下载头像:"+imageUrl);
    }else{
      LogUtils.d(TAG,"头像地址不存在,请求usb00001");
      UserPortraitGetParam param = new UserPortraitGetParam();
      param.setBancsCustNo(bancsCustNo);

      downUrlObs =  new YunService().getUserPortrait(param).flatMap(new Func1<UserPortraitGetResult, Observable<String>>() {
        @Override public Observable<String> call(UserPortraitGetResult userPortraitGetResult) {
          String imageUrl = userPortraitGetResult.getImageUrl();
          if(imageUrl!=null){
            if(imageUrl.startsWith("http://") || imageUrl.startsWith("https")){

            }else{
              imageUrl = "http://"+imageUrl;
            }
          }
          LogUtils.d(TAG,"头像地址usb00001下载成功");
          dao.saveYunImage(user,imageUrl,"");
          return Observable.just(imageUrl);
        }
      });
    }

    downUrlObs.subscribeOn(Schedulers.io())
        .filter(new Func1<String, Boolean>() {
      @Override public Boolean call(String s) {
        return s!=null && (s.startsWith("http")||s.startsWith("https"));
      }
    }).subscribe(new Subscriber<String>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable throwable) {
        LogUtils.d(TAG,"头像路径获取异常:"+throwable.getMessage());

      }

      @Override public void onNext(String s) {
        netDownImage();
      }
    });
  }


  public void netDownAccountIds(){
    LogUtils.d(TAG,"---开始下载账户ids");

    final String custNo = bancsCustNo;
    final String custNetNo = bocnetCustNo;
    final String mobile = mobilePhPh;

    UserLastAccountIdGetParam param = new UserLastAccountIdGetParam();
    param.setBancsCustNo(custNo);
    param.setCurStrNo("1");
    param.setOrderCnt("100");
    param.setServiceNo("");

    new YunService().getUserLastAccountId(param).subscribeOn(Schedulers.io())
        .map(new Func1<UserLastAccountIdGetResult, UserLastAccountIdGetResult>() {
          @Override public UserLastAccountIdGetResult call(UserLastAccountIdGetResult result) {

            List<AccountBean> list = result.getList();
            List<AccountServiceBean> results = new ArrayList<>();
            for(AccountBean bean:list){
              AccountServiceBean serviceBean = new AccountServiceBean();
              serviceBean.setLastAccId(bean.getLastAccId());
              serviceBean.setServiceCode(bean.getServiceNo());
              serviceBean.setState(AccountDao.STATE_OLD);

              serviceBean.setBancsCustNo(custNo);
              serviceBean.setBocnetCustNo(custNetNo);
              serviceBean.setMobilePh(mobile);

              results.add(serviceBean);
            }
            new AccountDao().updateAccountList(results);

            LogUtils.d(TAG,"----账户id 下载并保存成功");
            return result;
          }


        })
        .subscribe(new Subscriber<UserLastAccountIdGetResult>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            LogUtils.d(TAG,"--2--获取账户id失败:"+throwable);
            throwable.printStackTrace();
          }

          @Override public void onNext(UserLastAccountIdGetResult result) {
            LogUtils.d(TAG,"--2--获取账户id成功:"+result.getList().size());
            new CloudInfoDao().updateState(user,"ubas000005",CloudInfoDao.STATE_UPDATED);
          }
        });

  }

  public void netDownDict(){
    LogUtils.d(TAG,"---开始下载码表");
    /*
    if(new DictDao().hasInit()){
      LogUtils.d(TAG,"---码表---已下载 :");
      return;
    }
    */

    UBAS010001Param param = new UBAS010001Param();
    param.setQueryFlag("0");
    param.setDictKey("");
    param.setDictTypeCode("");

    new YunService().getDirct(param).subscribeOn(Schedulers.io())
        .map(new Func1<UBAS010001Result, UBAS010001Result>() {
          @Override public UBAS010001Result call(UBAS010001Result result) {
            if(result == null)return result;

            List<UBAS010001Result.DictBean> list = result.getList();
            if(list == null || list.size()==0)return result;

            List<DictVoBean> results = new ArrayList<>();
            for(UBAS010001Result.DictBean bean:list){
              DictVoBean vo = new DictVoBean();
              vo.setKey(bean.getDictKey());
              vo.setType(bean.getDictTypeCode());
              vo.setValue(bean.getDictValue());
              results.add(vo);
            }

            new DictDao().updateDict(results);

            return result;
          }
        })
        .subscribe(new Subscriber<UBAS010001Result>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            LogUtils.d(TAG,"数据字典请求失败:"+throwable.getMessage());
          }

          @Override public void onNext(UBAS010001Result ubas010001Result) {
            LogUtils.d(TAG,"数据字典请求成功");
            //TODO 更新
          }
        });
  }


  private void netDownImage(){
    LogUtils.d(TAG,"---下载头像----");

    final  String innerUser = bancsCustNo;

    final UserInfoDao dao = new UserInfoDao();
    boolean needDownImage = dao.isNeedDownImage(innerUser);
    if(!needDownImage)return;

    LogUtils.d(TAG," --  头像bitmap 下载 开始");

    final String[] tmp = new String[1];
    Observable.just("").subscribeOn(Schedulers.io())
        .flatMap(new Func1<String, Observable<Response>>() {
          @Override public Observable<Response> call(String s) {

            final String url = dao.getImageUrl(bancsCustNo);
            LogUtils.d(TAG," --  头像bitmap url:"+url);
            tmp[0] = url;
            return YunClient.instance.get(url);
          }
        }).map(new Func1<Response, Object>() {
      @Override public Object call(Response response) {

        try {
          byte[] bytes = response.body().bytes();
          String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
          dao.saveYunImage(innerUser,tmp[0],base64);

          new CloudInfoDao().updateState(user,"ubas000001",CloudInfoDao.STATE_UPDATED);

          LogUtils.d(TAG,"---头像下载成功,");
          notifyPhotoDown();

        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
      }
    }).subscribe(new Subscriber<Object>() {
      @Override public void onCompleted() {


      }

      @Override public void onError(Throwable throwable) {
        LogUtils.d(TAG," --  头像bitmap 下载失败2");
      }

      @Override public void onNext(Object o) {

      }
    });
  }



  private static String getBase64Str(Bitmap bitmap){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try{
      int opt = 100;//压缩质量
      for(;;){
        baos.reset();
        bitmap.compress(Bitmap.CompressFormat.JPEG, opt, baos);
        if(baos.size()/1024 < 48){
          break;
        }
        LogUtils.d(TAG,"当前质量:"+opt+" ,图片体积:"+baos.size()/1024);
        opt = opt-5;
      }
      LogUtils.d(TAG,"压缩成功 质量:"+opt+" ,图片体积:"+baos.size()/1024);
    }catch (Exception e){
      LogUtils.d(TAG,"图片压缩异常");
    }

    return Base64.encodeToString( baos.toByteArray(), Base64.NO_WRAP);
  }

  /**
   * 获取sha256摘要
   * @param input
   * @return
   */
  public static String getSha256String(String input){
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(input.getBytes());

      byte[] digest = md.digest();
      return byte2Hex(digest);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String byte2Hex(byte[] bs){
    StringBuilder sb = new StringBuilder();

    if(bs == null || bs.length == 0)return null;
    for(int index=0 ;index<bs.length;index++){
      int i = bs[index] & 0xff;
      String s = Integer.toHexString(i);
      if(s.length()<2){
        sb.append("0");
      }
      sb.append(s);
    }
    return sb.toString();
  }

  private void notifyPhotoDown(){
    DataListener.notifyPhotoDown();
  }
}
