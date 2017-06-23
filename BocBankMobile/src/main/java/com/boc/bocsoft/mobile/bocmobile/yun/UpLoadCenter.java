package com.boc.bocsoft.mobile.bocmobile.yun;

import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.yun.db.AccountDao;
import com.boc.bocsoft.mobile.bocmobile.yun.db.LoginInfoDao;
import com.boc.bocsoft.mobile.bocmobile.yun.db.UserInfoDao;
import com.boc.bocsoft.mobile.bocmobile.yun.model.AccountServiceBean;
import com.boc.bocsoft.mobile.bocmobile.yun.model.LoginfoBean;
import com.boc.bocsoft.mobile.bocmobile.yun.model.UserInfoBean;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000002.UserPortraitUpdateParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.AccountBean;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000006.UserLastAccountIdUpdateParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS020001.UserLoginInfoUpdateParam;
import com.boc.bocsoft.mobile.bocyun.service.YunService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dingeryue on 2016年10月25.
 */

public class UpLoadCenter {

  private static  UpLoadCenter center = new UpLoadCenter();

  public static UpLoadCenter getInstance(){
    return center;
  }

  //private String bocnetCustNo;//网银客户ID
  //private String bancsCustNo;//核心客户号
  //private String mobilePhPh;//手机号
  //
  //private String user;
  /**
   * 初始化方法 ,登录后调用
   * @param bancsCustNo
   * @param bocnetCustNo
   * @param mobilePh
   */
  public void init(String bancsCustNo,String bocnetCustNo,String mobilePh){
  /*  this.bancsCustNo = bancsCustNo;
    this.bocnetCustNo = bocnetCustNo;
    this.mobilePhPh = mobilePh;
    user = bancsCustNo;*/
  }

  public  void check(){
    upLoadAccount();
    upLoadLoginInfo();
    upLoadUserPhoto();
  }

  public void upLoadAccount(){

    //上传需要更新的账户
    LogUtils.d("dding","检查 是否有新的账户id需要上传");
   final AccountDao accountDao = new AccountDao();
    final List<AccountServiceBean> tmpList = new ArrayList<>();

    Observable.just("").subscribeOn(Schedulers.io())
        .map(new Func1<String,List<AccountServiceBean>>() {
          @Override public List<AccountServiceBean> call(String s) {
            return accountDao.getNeedUpdateAccounts();
          }
        }).filter(new Func1<List<AccountServiceBean>, Boolean>() {
      @Override public Boolean call(List<AccountServiceBean> beanList) {
        return beanList!=null && beanList.size()>0;
      }
    }).flatMap(new Func1<List<AccountServiceBean>, Observable<Integer>>() {
      @Override public Observable<Integer> call(List<AccountServiceBean> beanList) {
        //记录更新账户列表
        tmpList.addAll(beanList);

        UserLastAccountIdUpdateParam param = new UserLastAccountIdUpdateParam();

        List<AccountBean> accountBeanList = new ArrayList<>();

        if(beanList.size()>0){
          AccountServiceBean bean = beanList.get(0);
          //设置上传参数
          param.setBancsCustNo(bean.getBancsCustNo());
          param.setMobilePh(bean.getMobilePh());
          param.setBocnetCustNo(bean.getBocnetCustNo());
        }

        for(AccountServiceBean item:beanList){

          AccountBean bean = new AccountBean();
          bean.setServiceNo(item.getServiceCode());
          bean.setLastAccId(item.getLastAccId());
          accountBeanList.add(bean);
        }

        param.setList(accountBeanList);

        LogUtils.d("dding","开始上传账户id ,数量:"+accountBeanList.size()+"  "+beanList.get(0).getMobilePh()+" "+accountBeanList );

        return new YunService().updateUserLastAccountId(param);
      }
    }).subscribe(new Subscriber<Integer>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable throwable) {
        throwable.printStackTrace();
        LogUtils.d("dding","账户id上传失败"+throwable.getMessage());
      }

      @Override public void onNext(Integer integer) {
        LogUtils.d("dding","账户id上传成功,更新数据库上传状态");
        if(tmpList.size()<=0){
          return;
        }
        for(AccountServiceBean bean:tmpList){
          bean.setState(AccountDao.STATE_OLD);
        }
        accountDao.updateAccountList(tmpList);
      }
    });


  }

  /**
   * 检查并上传用户登录信息
   */
  public void upLoadLoginInfo(){
    final YunService service = new YunService();
    final LoginInfoDao dao = new LoginInfoDao();

    final LoginfoBean[] tmps = new LoginfoBean[1];

    LogUtils.d("dding","开始 检测是否有需要上传的用户登录信息");
    Observable.just("").subscribeOn(Schedulers.io())
        .delay(1, TimeUnit.SECONDS)
        .flatMap(new Func1<String, Observable<LoginfoBean>>() {
          @Override public Observable<LoginfoBean> call(String s) {
            return Observable.from( dao.getNeedUploadList());
          }
        }).flatMap(new Func1<LoginfoBean, Observable<Integer>>() {
      @Override public Observable<Integer> call(LoginfoBean loginfoBean) {
        LogUtils.d("dding","上传用户登录信息:"+loginfoBean.getLoginid()+"  "+loginfoBean.getLoginTime());
        UserLoginInfoUpdateParam param = new UserLoginInfoUpdateParam();

        param.setBancsCustNo(loginfoBean.getBancsCustNo());
        param.setBocnetCustNo(loginfoBean.getBocnetCustNo());
        param.setMobilePh(loginfoBean.getMobilePh());

        param.setLoginTime(loginfoBean.getLoginTime());
        param.setLogoutTime(loginfoBean.getLogoutTime());

        param.setRam(loginfoBean.getRam());
        param.setRom(loginfoBean.getRom());
        param.setUsedRom(loginfoBean.getUserdRom());
        param.setResolution(loginfoBean.getResolution());

        tmps[0] = loginfoBean;

        return service.updateLoginInfo(param);
      }
    }).subscribe(new Subscriber<Integer>() {
      @Override public void onCompleted() {
        LogUtils.d("dding","用户登录信息检测,并上传完毕");
      }

      @Override public void onError(Throwable throwable) {
        LogUtils.d("dding","用户登录信息检测,上传出错:"+throwable.getMessage());
      }

      @Override public void onNext(Integer integer) {
        if(tmps[0] != null){
          LogUtils.d("dding","用户登录信息 上传成功,删除db:"+tmps[0].getLoginid()+"  "+tmps[0].getLoginTime());
          dao.deleteRecord(tmps[0].getLoginid());
        }
      }
    });

  }

  public void upLoadUserPhoto(){
    final UserInfoDao dao = new UserInfoDao();

    LogUtils.d("dding","检查 是否有新的头像需要上传");

    List<UserInfoBean> needUploadUser = dao.getNeedUploadUser();
    if(needUploadUser == null || needUploadUser.size() == 0)return;

    Observable.from(needUploadUser).subscribeOn(Schedulers.io())
        .flatMap(new Func1<UserInfoBean, Observable<UserInfoBean>>() {
          @Override public Observable<UserInfoBean> call(final UserInfoBean userInfoBean) {
            UserPortraitUpdateParam param = new UserPortraitUpdateParam();

            param.setBancsCustNo(userInfoBean.getCustno());
            param.setBocnetCustNo(userInfoBean.getCustnetno());
            param.setMobilePhPh(userInfoBean.getMobilePh());

            param.setSuffix("jpg");
            param.setImageEntity(userInfoBean.getLocalBase64());

            LogUtils.d("dding","开始上传新头像:"+userInfoBean.getMobilePh()+" ,"+userInfoBean.getLocalBase64());

            return new YunService().updateUserPortrait(param).map(new Func1<Integer, UserInfoBean>() {
              @Override public UserInfoBean call(Integer integer) {
                return userInfoBean;
              }
            }).onErrorReturn(new Func1<Throwable, UserInfoBean>() {
              @Override public UserInfoBean call(Throwable throwable) {
                LogUtils.d("dding","上传头像出错,"+throwable.getMessage());
                return null;
              }
            });
          }
        }).map(new Func1<UserInfoBean, UserInfoBean>() {
      @Override public UserInfoBean call(UserInfoBean userInfoBean) {

        if(userInfoBean != null){
          dao.updateUploadState(userInfoBean.getCustno(),UserInfoDao.UPLOAD_ED);
          LogUtils.d("dding","上传头像成功,更新数据库状态");
        }
        return userInfoBean;
      }
    }).subscribe(new Subscriber<UserInfoBean>() {
      @Override public void onCompleted() {
        LogUtils.d("dding","上传头像成功操作结束");
      }

      @Override public void onError(Throwable throwable) {
        LogUtils.d("dding","上传头像成功操作失败,"+throwable.getMessage());
      }

      @Override public void onNext(UserInfoBean o) {

      }
    });

  }
}
