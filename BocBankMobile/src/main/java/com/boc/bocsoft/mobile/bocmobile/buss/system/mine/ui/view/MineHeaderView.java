package com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 头像区域
 * Created by eyding on 16/8/9.
 */
public class MineHeaderView extends RelativeLayout implements View.OnClickListener {

  private CircleImageView ivPhoto;//头像
  private View viewBGRight;
  private TextView tvLeft;
  private TextView tvLogin;
  private ImageView ivBg;
  private String timeLastSucc;
  private String timeLastFail;

  private HeaderViewInterface headerViewInterface;

  public MineHeaderView(Context context) {
    super(context);
    initView();
  }

  public MineHeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public MineHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  private void initView(){
    LayoutInflater.from(getContext()).inflate(R.layout.view_mine_header,this,true);

    ivBg = (ImageView) findViewById(R.id.iv_bg);

    ivPhoto = (CircleImageView) findViewById(R.id.iv_photo);
    //ivPhoto.setImageResource(R.drawable.ic_test_0);

    viewBGRight = findViewById(R.id.view_right_bg);
    tvLeft = (TextView) findViewById(R.id.tv_left);
    tvLogin = (TextView) findViewById(R.id.tv_login);
    //initRightBg();

    ivPhoto.setOnClickListener(this);
    tvLeft.setOnClickListener(this);
    changeLoginState(false);

    //final Drawable drawable = ivBg.getDrawable();

    //boc_mine_header_bg
    Drawable drawable = getResources().getDrawable(R.drawable.boc_mine_header_bg);
    int intrinsicWidth = drawable.getIntrinsicWidth();
    int intrinsicHeight = drawable.getIntrinsicHeight();

    int widthPixels = getResources().getDisplayMetrics().widthPixels;

    widthPixels  = (int) (widthPixels - getResources().getDisplayMetrics().density*30 +0.5f);

    int h = (int) (widthPixels*intrinsicHeight*1.f/intrinsicWidth  +0.5f);

    RelativeLayout.LayoutParams layoutParams = (LayoutParams) ivBg.getLayoutParams();
    if(layoutParams == null){
      layoutParams = new RelativeLayout.LayoutParams(widthPixels,h);
      layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    }else{
      layoutParams.width = widthPixels;
      layoutParams.height = h;
    }

    ivBg.setLayoutParams(layoutParams);
    ivBg.setBackgroundDrawable(drawable);

    ViewGroup.LayoutParams lp = ivPhoto.getLayoutParams();
    if(lp == null){
      lp = new RelativeLayout.LayoutParams(-2,-2);
    }
    int i = (int) (h * 0.8f);
    lp.width = i;
    lp.height = i;
    ivPhoto.setLayoutParams(lp);


    /*ivBg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        Drawable drawable1 = ivBg.getDrawable();
        int i = (int) (drawable1.getIntrinsicHeight() * ivBg.getMeasuredWidth()*0.8f/ (drawable1.getIntrinsicWidth()));

        ViewGroup.LayoutParams layoutParams = ivPhoto.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i;
        ivPhoto.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          ivBg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }else{
          ivBg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
      }
    });*/
  }

  public void changeLoginState(boolean isLogin){
    if(isLogin){
      tvLogin.setVisibility(View.GONE);
      tvLogin.setOnClickListener(null);
      tvLeft.setVisibility(View.VISIBLE);
      viewBGRight.setVisibility(View.VISIBLE);
      ivBg.setVisibility(View.VISIBLE);
    }else{
      tvLogin.setVisibility(View.VISIBLE);
      tvLogin.setOnClickListener(this);
      tvLeft.setVisibility(View.INVISIBLE);
      viewBGRight.setVisibility(View.INVISIBLE);
      ivBg.setVisibility(View.INVISIBLE);
    }
  }

  public void updatePhoto(int res){
    ivPhoto.setImageResource(res);
  }
  public void upatePhoto(Bitmap bitmap){
    ivPhoto.setImageBitmap(bitmap);
  }

  public void updateWelcomeInfo(String info){
    tvLeft.setText(info);
  }

  public void updateLoginInfo(String timeLastSucc,String timeLastFail){
    this.timeLastSucc = timeLastSucc;
    this.timeLastFail = timeLastFail;

    TextView tv = (TextView) viewBGRight;

    StringBuilder sb = new StringBuilder();
    if(timeLastSucc != null){
      sb.append("最近一次登录成功\n"+timeLastSucc);
    }

    if(timeLastFail != null){
      if(sb.length()>0){
        sb.append("\n");
      }
      sb.append("最近一次登录失败\n"+timeLastFail);
    }

    tv.setText(sb);

    //viewBGRight.invalidateDrawable(viewBGRight.getBackground());
  }


  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
    heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (measuredWidth*0.45),MeasureSpec.EXACTLY);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);

    RelativeLayout.LayoutParams st =
        (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
    //ivPhoto.offsetTopAndBottom(-10);
  }

  public void setHeaderViewInterface(HeaderViewInterface headerViewInterface) {
    this.headerViewInterface = headerViewInterface;
  }

  @Override public void onClick(View v) {
    if(headerViewInterface == null){
      return;
    }
    if(v == tvLogin){
      headerViewInterface.onLoginClick();
    }
    if(v == ivPhoto){
      headerViewInterface.onPhotoClick();
    }
    if(v == tvLeft){
      headerViewInterface.onLeftClick();
    }
  }

  public interface HeaderViewInterface{
    void onPhotoClick();
    void onLoginClick();
    void onLeftClick();
  }

}
