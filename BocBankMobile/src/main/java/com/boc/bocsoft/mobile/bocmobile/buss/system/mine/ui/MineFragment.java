package com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ConfirmDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContext;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.utils.UserPortraitUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.FileUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.GoHomeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.model.MineMenuVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.presenter.MinePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui.view.MineHeaderView;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.DataListener;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemoteOpenAccActivity;
import com.chinamworld.boc.commonlib.ModuleManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import rx.functions.Action1;

/**
 * Created by eyding on 16/7/9.
 * main - 个人页面
 */
public class MineFragment extends BussFragment implements View.OnClickListener, MineContract.View {

  //图片剪切的宽高
  private final int PHOTO_CROP_SIZE = 300;

  private NestedScrollView rootNScrollView;//根view
  private MineHeaderView viewPhotoHeader;
  private TextView btLogout;

  private MineContract.Presenter minePresenter;
  private ConfirmDialog logoutConfirmDialog;

  private View[] viewMenus;
  private TextView tvMenuTitles[];
  private ImageView ivMenuIcons[];
  private View viewLine3;
  private int itemHeight;//item高度 水平四等分

  private List<MineMenuVo> mineMenuVos;

  private TextView tvVersion;

  @Override protected View onCreateView(LayoutInflater mInflater) {

    return mInflater.inflate(R.layout.fragment_mine, null);
  }

  @Override public void beforeInitView() {

    //计算宽度
    int widthPixels = getResources().getDisplayMetrics().widthPixels;
    int divider_width = (int) (getResources().getDisplayMetrics().density * 1 +0.5f);
    itemHeight = (widthPixels - 3 * divider_width) / 4;
  }

  @Override public void initView() {
    beforeInitView();

    rootNScrollView = mViewFinder.find(R.id.nsv_root);
    tvVersion = mViewFinder.find(R.id.tv_version);

    viewPhotoHeader = mViewFinder.find(R.id.view_header);
    btLogout = mViewFinder.find(R.id.bt_logout);

    LinearLayout ll_item1 = mViewFinder.find(R.id.ll_item1);
    LinearLayout ll_item2 = mViewFinder.find(R.id.ll_item2);
    LinearLayout ll_item3 = mViewFinder.find(R.id.ll_item3);

    ll_item1.getLayoutParams().height = itemHeight;
    ll_item2.getLayoutParams().height = itemHeight;
    ll_item3.getLayoutParams().height = itemHeight;

    viewLine3 = ll_item3;

    viewMenus = new View[7];
    tvMenuTitles = new TextView[7];
    ivMenuIcons = new ImageView[7];

    for (int n = 0; n < viewMenus.length; n++) {
      View itemView;
      if (n < 3) {
        itemView = ll_item1.getChildAt(n);
      } else if (n < 6) {
        itemView = ll_item2.getChildAt(n - 3);
      } else {
        itemView = ll_item3.getChildAt(n - 6);
      }
      viewMenus[n] = itemView;
      itemView.setTag(Integer.valueOf(n));
      itemView.setOnClickListener(this);

      tvMenuTitles[n] = (TextView) itemView.findViewById(R.id.tv_title);
      ivMenuIcons[n] = (ImageView) itemView.findViewById(R.id.iv_icon);
    }
  }

  @Override public void initData() {
    minePresenter = new MinePresenter(this);
    //初始化设置原始登录状态
    mineMenuVos = minePresenter.getMenus();
    minePresenter.uptateLoginState();

    String version = ApplicationContext.getInstance().getVersion();
    tvVersion.setText(getVersionBlackStyle(version));
  }

  private CharSequence getVersionBlackStyle(String version){
    return  "当前版本"+version;

   /* android.text.SpannableString span = new android.text.SpannableString("当前版本:"+version);
    span.setSpan(new CharacterStyle() {
      @Override public void updateDrawState(TextPaint tp) {
        tp.setColor(getResources().getColor(R.color.boc_text_color_dark_gray));
      }
    },5,span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    return span;*/
  }

  @Override public void setListener() {
    btLogout.setOnClickListener(this);
    viewPhotoHeader.setHeaderViewInterface(new MineHeaderView.HeaderViewInterface() {
      @Override public void onPhotoClick() {

        if (minePresenter.isLogin()) {
          //选头像
          showPicturePicker(getActivity());
        } else {
          //actionLoginClick();
        }
      }
      @Override public void onLoginClick() {
        actionLoginClick();
      }

      @Override public void onLeftClick() {
        actionHintClick();
      }
    });

  }

  @Override public void updateUILogin() {
    uILogin = true;
    viewLine3.setVisibility(View.GONE);
    //登录后隐藏在线开户
    setRemoteOpenItemViewVisisable(false);

    btLogout.setVisibility(View.VISIBLE);
    viewPhotoHeader.changeLoginState(true);

    User user = ApplicationContext.getInstance().getUser();

    loadBitmap();
    //上次登录成功时间
    String lastLoginTime = user.getLastLogin();
    //最后登录失败时间
    String lastErrorTime = user.getLoginErrorDate();


    //loginHint = "后天小雨天上下雨啊啊";

    LogUtils.d("dding", "--user info: {最后成功:+"
        + lastLoginTime
        + "  最后失败:+"
        + lastErrorTime
        + "  欢迎信息:"
        + "}");
    updateLoginHint();

    refrenshMenu();

    viewPhotoHeader.updateLoginInfo(lastLoginTime,lastErrorTime);

    DataListener.addPhotoDownListener(photoDownListener);
  }

  private void updateLoginHint(){
    viewPhotoHeader.updateWelcomeInfo(getHintMessage());
  }

  @Override public void updateUINoLogin() {
    uILogin = false;
    viewLine3.setVisibility(View.VISIBLE);
    setRemoteOpenItemViewVisisable(true);
    loadBitmap();
    viewPhotoHeader.changeLoginState(false);
    btLogout.setVisibility(View.GONE);

    refrenshMenu();

    DataListener.removePhotoDownListener(photoDownListener);
  }

  private void refrenshMenu() {
    if(mineMenuVos == null)return;
    MineMenuVo mineMenuVo;
    final boolean isLogin = minePresenter.isLogin();
    for (int index = 0; index < mineMenuVos.size(); index++) {
      mineMenuVo = mineMenuVos.get(index);
      tvMenuTitles[index].setText(isLogin?mineMenuVo.titleLogin():mineMenuVo.titleNoLogin());
      ivMenuIcons[index].setImageResource(mineMenuVo.icon());
    }
  }

  private void actionLoginClick() {
    Intent intent = new Intent();
    intent.setClass(getActivity(), LoginBaseActivity.class);
    startActivity(intent);
    //start(new TestBussFragment());
  }

  private void actionHintClick(){
    ErrorDialog errorDialog = new ErrorDialog(getActivity());
    errorDialog.setBtnText("关闭").setErrorData(getHintMessage());
    errorDialog.setCanceledOnTouchOutside(true);
    errorDialog.show();
  }

  private String getHintMessage(){
    User user = ApplicationContext.getInstance().getUser();
    if(user == null){
      return "" ;
    }
    //欢迎信息
    String loginHint = user.getLoginHint();
    if(StringUtils.isEmpty(loginHint)){
      loginHint = "HI,"+(user.getCustomerName()==null?"":user.getCustomerName());
    }
    return loginHint;
  }

  final int REQ_CAMERA = 3;
  final int REQ_PIC = 2;
  final int REQ_CROP = 4;

  /**
   * 显示选择图片dialog
   */
  public void showPicturePicker(Context context) {

    LinearLayout ll_root = new LinearLayout(context);
    ll_root.setOrientation(LinearLayout.VERTICAL);
    ll_root.setMinimumWidth(10000000);

    final Dialog dialog = new Dialog(getActivity());
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(ll_root);

    int titleHeight = getResources().getDimensionPixelSize(R.dimen.boc_space_between_120px);
    int itemHeight = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_110px);
    int paddingLeft = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_30px);
    int dividerHeight = getResources().getDimensionPixelOffset(R.dimen.boc_divider_1px);
    int dividerColor = getResources().getColor(R.color.boc_divider_line_color);
    int textColor = getResources().getColor(R.color.boc_text_color_dark_gray);

    int colorNormal = 0xffffffff;
    int colorPress = 0xffefefef;

    TextView tvTitle = new TextView(context);
    tvTitle.setTextColor(textColor);
    tvTitle.setText("编辑头像");
    tvTitle.setBackgroundColor(colorNormal);
    tvTitle.getPaint().setFakeBoldText(true);
    tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.boc_text_size_20dp));
    tvTitle.setMinHeight(titleHeight);
    tvTitle.setGravity(Gravity.CENTER);

    View dividerView1 = new View(context);
    dividerView1.setBackgroundColor(dividerColor);

    StateListDrawable stateListDrawable = new StateListDrawable();
    stateListDrawable.addState(new int[] { android.R.attr.state_pressed },
        new ColorDrawable(colorPress));
    stateListDrawable.addState(new int[] {}, new ColorDrawable(colorNormal));

    stateListDrawable.mutate();

    TextView tvPhoto = new TextView(context);
    tvPhoto.setText("拍照");
    tvPhoto.setMinHeight(itemHeight);
    tvPhoto.setGravity(Gravity.CENTER | Gravity.LEFT);
    tvPhoto.setBackgroundDrawable(stateListDrawable);
    tvPhoto.setTextColor(textColor);
    tvPhoto.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.boc_text_size_common));
    tvPhoto.setPadding(paddingLeft, 0, 0, 0);

    View dividerView2 = new View(context);
    dividerView2.setBackgroundColor(Color.parseColor("#ffdadada"));

    StateListDrawable secondDrawable = new StateListDrawable();
    secondDrawable.addState(new int[] { android.R.attr.state_pressed },
        new ColorDrawable(colorPress));
    secondDrawable.addState(new int[] {}, new ColorDrawable(colorNormal));

    TextView tvPic = new TextView(context);
    tvPic.setText("从手机相册选择");
    tvPic.setMinHeight(itemHeight);
    tvPic.setGravity(Gravity.CENTER | Gravity.LEFT);
    tvPic.setBackgroundDrawable(secondDrawable);
    tvPic.setTextColor(textColor);
    tvPic.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.boc_text_size_common));
    tvPic.setPadding(paddingLeft, 0, 0, 0);

    //添加头
    ll_root.addView(tvTitle);
    ll_root.addView(dividerView1, -1, dividerHeight);
    //添加拍照
    ll_root.addView(tvPhoto, ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    ll_root.addView(dividerView2, -1, dividerHeight);
    //添加相册
    ll_root.addView(tvPic, ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);

    tvPhoto.setOnClickListener(new View.OnClickListener() {

      public void onClick(View v) {
        dialog.cancel();
        actionPickFromCamera();
      }
    });
    tvPic.setOnClickListener(new View.OnClickListener() {
      // choose picture
      public void onClick(View v) {
        dialog.cancel();
        actionPickFromAlbum();
      }
    });

    dialog.show();
  }

  private void actionPickFromCamera() {

    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Uri imageUri = Uri.fromFile(new File(getTempFile()));
    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    //请求相机
    startActivityForResult(openCameraIntent, REQ_CAMERA);
  }

  private String getTempFile(){
    File file = new File(Environment.getExternalStorageDirectory(),"boc/temp/temp.jpg");
    File parentFile = file.getParentFile();
    parentFile.mkdirs();

    return file.getPath();
  }

  private String getTmpCrop(){
    File file = new File(Environment.getExternalStorageDirectory(),"boc/temp/tempcrop.jpg");
    File parentFile = file.getParentFile();
    parentFile.mkdirs();
    return file.getPath();
  }

  private void actionPickFromAlbum() {

    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
    openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(openAlbumIntent, REQ_PIC);
  }

  public void cropImage(Uri uri,int requestCode) {
    boolean isCropSys = true;
    if(!isCropSys){
      //crop other
    }else{
      //系统裁切
      crop(uri,PHOTO_CROP_SIZE,PHOTO_CROP_SIZE,requestCode);
    }
  }

  private void crop(Uri uri,int outputX,int outputY,int reqcode){

    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("outputX", outputX);
    intent.putExtra("outputY", outputY);
    intent.putExtra("return-data", false);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getTmpCrop())));
    intent.putExtra("outputFormat", "jpg");
    intent.putExtra("noFaceDetection", true);
    startActivityForResult(intent, reqcode);
  }

  @Override protected boolean isHaveTitleBarView() {
    return false;
  }

  @Override public void onClick(View v) {
    if (v == btLogout) {
      actionLogoutClick();
    } else if (v.getTag() != null) {
      actionItemClick((Integer) v.getTag());
    }
  }
  private TitleAndBtnDialog logoutDialog;

  private void actionLogoutClick() {

    if(logoutDialog == null){
      logoutDialog = new TitleAndBtnDialog(getActivity());
      logoutDialog.setTitle("");
      logoutDialog.isShowTitle(false);


      logoutDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
          getResources().getColor(R.color.boc_text_color_red),
          getResources().getColor(R.color.boc_common_cell_color),
          getResources().getColor(R.color.boc_common_cell_color));
      logoutDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
          getResources().getColor(R.color.boc_common_cell_color),
          getResources().getColor(R.color.boc_text_color_red),
          getResources().getColor(R.color.boc_text_color_red));

      logoutDialog.getTvNotice().setMinHeight(
          (int) getResources().getDimension(R.dimen.boc_space_between_200px));


      logoutDialog.setNoticeContent("请确认是否退出当前登录账号?");
      logoutDialog.setBtnName(new String[]{"取消","确认"});
      logoutDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
        @Override public void onLeftBtnClick(View view) {
          logoutDialog.dismiss();
        }

        @Override public void onRightBtnClick(View view) {
          logoutDialog.dismiss();
          minePresenter.logOut();
          BocCloudCenter.getInstance().updateUserLogout();
          BocCloudCenter.getInstance().checkUpload();
        }
      });
    }

    logoutDialog.show();
   /* if(true)return;

    if (logoutConfirmDialog == null) {
      logoutConfirmDialog = new ConfirmDialog(getActivity());
      logoutConfirmDialog.setMessage("请确认是否退出当前登录账号?");
      logoutConfirmDialog.setRightButtonStyle(getResources().getColor(R.color.boc_text_color_red),getResources().getColor(R.color.boc_common_cell_color));
      logoutConfirmDialog.setLeftButton("取消")
          .setRightButton("确定")
          .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface() {
            @Override public void onLeftClick(ConfirmDialog warnDialog) {
              warnDialog.dismiss();
            }

            @Override public void onRightClick(ConfirmDialog warnDialog) {
              warnDialog.dismiss();
              minePresenter.logOut();
            }
          });
    }

    logoutConfirmDialog.show();*/
  }
  private void actionItemClick(Integer pos) {
  /*  start(new TestBussFragment());
   //showPicturePicker(getContext());
    if(true)return;*/
    if (pos == null || mineMenuVos == null || pos >= mineMenuVos.size()) {
      return;
    }

    if(pos == 5){
      //中行应用
      ModuleManager.instance.gotoMoreApplicationActivity(getActivity());
      return;
    }

    if(pos == 6 && !minePresenter.isLogin()){
      //在线开户
      Intent intent = new Intent();
      intent.setClass(getContext(), RemoteOpenAccActivity.class);
      startActivity(intent);
      return;
    }

    if(pos == 4){
      //服务记录
      if(minePresenter.isLogin()){
        ModuleManager.instance.gotoServiceRecordQueryActivity(getActivity());
      }else{
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginBaseActivity.class);
        LoginContext.instance.setCallback(new LoginCallback() {
          @Override public void success() {
            if(isResume){
              ModuleManager.instance.gotoServiceRecordQueryActivity(getActivity());
            }else{
              nextAction = new Runnable() {
                @Override public void run() {
                  ModuleManager.instance.gotoServiceRecordQueryActivity(getActivity());
                }
              };
            }
          }
        });
        startActivity(intent);
      }
      return;
    }

    ModuleActivityDispatcher.dispatch((BussActivity) getActivity(), mineMenuVos.get(pos).target());
  }

  @Override public void startLoading(String loading) {
    showLoadingDialog();
  }

  @Override public void endLoading() {
    closeProgressDialog();
  }

  @Override public void setPresenter(BasePresenter presenter) {

  }

  //private Boolean oldLoginState = false;
  private boolean isResume = false;
  private Runnable nextAction;
  private Boolean uILogin;

  @Override public void onResume() {
    super.onResume();
    isResume = true;

    if(nextAction != null){
      nextAction.run();
      nextAction = null;
    }

    if (uILogin != null && uILogin != minePresenter.isLogin()) {
      minePresenter.uptateLoginState();
    }

    if(minePresenter.isLogin()){
      // 刷新预留信息
      updateLoginHint();
    }
  }

  @Override public void endLogOut() {
    //结束
    //退出结束跳转功能首页面
   //ModuleActivityDispatcher.popToHomePage();
    MainActivity activity = (MainActivity) getActivity();
    BocEventBus.getInstance().post(new GoHomeEvent());
  }

  @Override public void onPause() {
    super.onPause();
  }

  @Override public void onStop() {
    isResume = false;
    super.onStop();
  }

  private void setRemoteOpenItemViewVisisable(boolean isVisiable){
    ViewGroup vp = (ViewGroup) viewMenus[6];
    int count = vp.getChildCount();
    for(int index=0;index<count;index++){
      vp.getChildAt(index).setVisibility(isVisiable?View.VISIBLE:View.INVISIBLE);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      //TODO
      switch (requestCode) {

        case REQ_CAMERA:
          Uri uri;
          if(data == null || data.getData() == null){
            uri = Uri.fromFile(new File(getTempFile()));
          }else{
            uri = data.getData();
          }
          LogUtils.d("dding", "----拍照 reqResult:" + uri);
          cropImage(uri, REQ_CROP);
          break;
        case REQ_PIC:
          Uri originalUri = data.getData();
          LogUtils.d("dding", "----选取 reqResult:" + originalUri);
          //部分手机裁切功能无法处理content://xxx/xx 图片,这里将图片copy到本地临时文件夹
          if(originalUri != null && ContentResolver.SCHEME_CONTENT.equals(originalUri.getScheme())){
            try {
              InputStream ins = mContext.getContentResolver().openInputStream(originalUri);
              FileUtils.copyFile(ins,getTempFile());
              originalUri = Uri.fromFile(new File(getTempFile()));
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            }
          }
          cropImage(originalUri, REQ_CROP);
          break;
        case REQ_CROP:

          onCropSuccess(data);

          break;
      }
    }
  }

  private void onCropSuccess(Intent data){
    Uri photoUri = data.getData();
    if(photoUri == null){
      photoUri = Uri.fromFile(new File(getTmpCrop()));
    }
    LogUtils.d("dding","---裁剪:"+photoUri);
    Picasso.with(getContext())
        .load(photoUri)
        .memoryPolicy(MemoryPolicy.NO_CACHE, new MemoryPolicy[]{MemoryPolicy.NO_STORE})
        .into(new Target() {
      @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

        if (bitmap != null) {
          viewPhotoHeader.upatePhoto(bitmap);
          savePhoto(bitmap);
        }
      }

      @Override public void onBitmapFailed(Drawable drawable) {

        LogUtils.d("dding","失败");
      }

      @Override public void onPrepareLoad(Drawable drawable) {

      }
    });
  }

  /**
   * 保存选中文件到本地
   */
  private void savePhoto(Bitmap bitmap) {
    UserPortraitUtils.saveProtraitAsync(bitmap);
  }

  /**
   * 从sp 中加载图片
   */
  private void loadBitmap() {
    //上次保存的用户名
    String preferencePhone = SpUtils.getLNhoneSpString(mContext, SpUtils.SPKeys.KEY_LOGINNAME, null);
    UserPortraitUtils.loadProtraitOBS(preferencePhone)
        .compose(SchedulersCompat.<Bitmap>applyIoSchedulers())
        .subscribe(new Action1<Bitmap>() {
          @Override public void call(Bitmap bitmap) {
            viewPhotoHeader.upatePhoto(bitmap);
          }
        });
  }

  private DataListener.PhotoDownListener photoDownListener = new DataListener.PhotoDownListener() {
    @Override public void onDown() {
      loadBitmap();
    }
  };
}
