package cfca.mobile.sip;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;

/**
 * Created by dingeryue on 2016年09月27.
 */

public class BocSipBox extends SipBox{
  public BocSipBox(Activity activity) {
    super(activity);
    initView();
  }

  public BocSipBox(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    initView();
  }

  private NoRootView noRootView;
  private Field field = null;

  private boolean isInDialog = false;

  /**
   * 设置是否在dialog使用 并且弹出时需要调整dialog位置已漏出输入框
   * @param isInDialog
   */
  public void setIsInDialogNeedRelocation(boolean isInDialog){
    this.isInDialog = isInDialog;
  }

  private void initView(){
    noRootView = new NoRootView(getContext());
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  @Override public View getRootView() {

    if(!isInDialog)return getSuperRootView();

    //if(true)return super.getRootView();
    StackTraceElement[] stackTrace = new Exception().getStackTrace();
    if("c".equals(stackTrace[1].getMethodName())){
      return getSuperRootView()==null?null:noRootView;
    }
    return getSuperRootView();
  }

  private View getSuperRootView(){
    return super.getRootView();
  }


  private int getSipOffset(){
    try {
      if(field == null){
        field = getClass().getSuperclass().getDeclaredField("o");
        field.setAccessible(true);
      }
      Activity activity = (Activity) field.get(this);
      return activity.getWindowManager().getDefaultDisplay().getHeight();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return  0;
  }

  public class NoRootView extends ViewGroup{

    public NoRootView(Context context) {
      super(context);
    }

    public NoRootView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    public NoRootView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override public void getLocationOnScreen(int[] location) {
      location[0]=0;
      location[1]=-getSipOffset();
    }

    @Override public IBinder getWindowToken() {
      return getSuperRootView()==null?null:getSuperRootView().getWindowToken();
    }

    public ViewGroup.LayoutParams getLayoutParams() {
      return getSuperRootView().getLayoutParams();
    }


  }
}
