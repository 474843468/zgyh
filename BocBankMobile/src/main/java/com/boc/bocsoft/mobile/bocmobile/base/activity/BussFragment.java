package com.boc.bocsoft.mobile.bocmobile.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.anim.FragmentAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.GlobalLoadingDialog;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.ui.BaseFragment;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 一个activity多个Fragment中的fragmetn基类
 * Created by Administrator on 2016/6/4.
 */
public class BussFragment extends BaseFragment
        implements GlobalLoadingDialog.CloseLoadingDialogListener {

    private static final String FRAGMENTATION_STATE_SAVE_ANIMATOR =
            "fragmentation_state_save_animator";
    private static final String FRAGMENTATION_STATE_SAVE_IS_HIDDEN =
            "fragmentation_state_save_status";

    protected static final String FRAGMENTATION_ANI_SETTING = "fragmentation_ani_setting";

    // LaunchMode
    public static final int STANDARD = 0;
    public static final int SINGLETOP = 1;
    public static final int SINGLETASK = 2;

    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    private static final long SHOW_SPACE = 200L;

    private int mRequestCode = 0, mResultCode = RESULT_CANCELED;
    private Bundle mResultBundle;
    private Bundle mNewBundle;
    private boolean mIsRoot;
    // 用于记录Fragment show/hide 状态
    private boolean mIsHidden = true;

    protected BussActivity mActivity;
    protected Fragmentation mFragmentation;
    private InputMethodManager mInputMethodManager;
    private FragmentAnimator mFragmentAnimator;
    private Animation mNoAnim, mEnterAnim, mExitAnim, mPopEnterAnim, mPopExitAnim;
    // fragmentation所用
    private Fragmentation.OnEnterAnimEndListener mOnAnimEndListener;
    // 防抖动监听动画
    private DebounceAnimListener mDebounceAnimListener;

    // 隐藏软键盘
    private boolean mNeedHideSoft;
    // 用于记录无动画时 直接 解除防抖动处理
    private boolean mEnterAnimFlag = false;

    private IWXAPI api;//第三方app和微信通信的接口

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LoggerUtils.Log(getClass().getSimpleName() + "  onCreate");
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(mContext, ApplicationConst.APP_ID, false);
        api.registerApp(ApplicationConst.APP_ID);//将应用的APPID注册到微信

        Activity activity = this.getActivity();
        if (activity instanceof BussActivity) {
            this.mActivity = (BussActivity) activity;
            mFragmentation = mActivity.getFragmentation();
        } else {
            throw new RuntimeException(activity.toString() + "must extends BussActivity!");
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            mRequestCode = bundle.getInt(Fragmentation.ARG_REQUEST_CODE, 0);
            mResultCode = bundle.getInt(Fragmentation.ARG_RESULT_CODE, 0);
            mResultBundle = bundle.getBundle(Fragmentation.ARG_RESULT_BUNDLE);
            mIsRoot = bundle.getBoolean(Fragmentation.ARG_IS_ROOT, false);
            mFragmentAnimator = bundle.getParcelable(FRAGMENTATION_ANI_SETTING);
        }
        if (savedInstanceState == null) {
            if (mFragmentAnimator == null) {
                mFragmentAnimator = onCreateFragmentAnimation();
                if (mFragmentAnimator == null) {
                    mFragmentAnimator = mActivity.getFragmentAnimator();
                }
            }
        } else {
            mFragmentAnimator = savedInstanceState.getParcelable(FRAGMENTATION_STATE_SAVE_ANIMATOR);
            mIsHidden = savedInstanceState.getBoolean(FRAGMENTATION_STATE_SAVE_IS_HIDDEN);
        }
        initAnim();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LoggerUtils.Log(getClass().getSimpleName() + "  onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        initFragmentBackground(view);
        // 防止某种情况 上一个Fragment仍可点击问题
        assert view != null;
        view.setClickable(true);

        if (savedInstanceState != null) {
            // 强杀重启时,系统默认Fragment恢复时无动画,所以这里手动调用下
            onEnterAnimationEnd();
            mActivity.setFragmentClickable(true);
        } else if (mEnterAnimFlag) {
            mActivity.setFragmentClickable(true);
        }
    }

    protected void initFragmentBackground(View view) {
        if (view != null && view.getBackground() == null) {
            int background = getWindowBackground();
            view.setBackgroundResource(background);
        }
    }

    protected int getWindowBackground() {
        TypedArray a = mActivity.getTheme().obtainStyledAttributes(new int[] {
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();
        return background;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return null;
    }

    @Override
    public void beforeInitView() {
        hideSoftInput();
    }

    public IWXAPI getApi() {
        if(!api.isWXAppInstalled()){
            showErrorDialog(mContext.getString(R.string.wx_share_message));
            return null;
        }
        return api;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onPause() {
        LoggerUtils.Log(getClass().getSimpleName() + "  onPause");
        super.onPause();
        if (mNeedHideSoft) {
            hideSoftInput();
        }
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        if (view == null) {
            return;
        }
        initImm();
        view.requestFocus();
        mNeedHideSoft = true;
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, SHOW_SPACE);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        if (getView() != null) {
            initImm();
            mInputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    boolean isSupportHidden() {
        return mIsHidden;
    }

    /**
     * 初始化软键盘
     */
    private void initImm() {
        if (mInputMethodManager == null) {
            mInputMethodManager =
                    (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    public void setEnterAnimEndListener(Fragmentation.OnEnterAnimEndListener onAnimEndListener) {
        this.mOnAnimEndListener = onAnimEndListener;
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     */
    public void putNewBundle(Bundle newBundle) {
        this.mNewBundle = newBundle;
    }

    Bundle getNewBundle() {
        return mNewBundle;
    }

    Bundle getResultBundle() {
        return mResultBundle;
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, TargetFragment回调该方法
     *
     * @param args 通过上个Fragment的putNewBundle(Bundle newBundle)时传递的数据
     */
    protected void onNewBundle(Bundle args) {

    }

    /**
     * 启动一个fragmetn，追加到队列，并显示
     *
     * @param toFragment 启动的fragment
     */
    public void start(BussFragment toFragment) {
        start(toFragment, STANDARD);
    }

    /**
     * 启动一个fragmetn，追加到队列，并显示
     */
    public void start(final BussFragment toFragment, final int launchMode) {
        LogUtils.d("buss","开启:"+toFragment);
        mFragmentation.dispatchStartTransaction(this, toFragment, 0, launchMode,
                Fragmentation.TYPE_ADD);
    }

    public void startForResult(final BussFragment toFragment, int requestCode) {
        LogUtils.d("buss","开启:"+toFragment);
        mFragmentation.dispatchStartTransaction(this, toFragment, requestCode, STANDARD,
                Fragmentation.TYPE_ADD);
    }

    public void startWithPop(BussFragment toFragment) {
        mFragmentation.dispatchStartTransaction(this, toFragment, 0, STANDARD,
                Fragmentation.TYPE_ADD_WITH_POP);
    }

    /**
     * 获取栈内的framgent对象
     */
    public <T extends BussFragment> T findFragment(Class<T> fragmentClass) {
        return mFragmentation.findStackFragment(fragmentClass, getFragmentManager(), false);
    }

    /**
     * 出栈
     */
    public void pop() {
        mFragmentation.back(getFragmentManager());
    }

    /**
     * 出栈到目标fragment
     *
     * @param fragmentClass 目标fragment
     * @param includeSelf 是否包含该fragment
     */
    public void popTo(Class<?> fragmentClass, boolean includeSelf) {
        mFragmentation.popTo(fragmentClass, includeSelf, null, getFragmentManager());
    }

    /**
     * 出栈到目标fragment(目标fragment本身不出栈),并调用目标fragment的reInit方法重新初始化
     *
     * @param clazz 目标fragment
     */
    public void popToAndReInit(Class<? extends BussFragment> clazz) {
        popTo(clazz, false);
        findFragment(clazz).reInit();
    }

    /**
     * 重新初始化
     */
    public void reInit() {
    }

    /*****************************************************************************************************
     * 返回处理
     *****************************************************************************************************/

    /**
     * 设置Result数据 (通过startForResult)
     *
     * @param resultCode resultCode
     * @param bundle 设置Result数据
     */
    public void setFramgentResult(int resultCode, Bundle bundle) {
        mResultCode = resultCode;
        mResultBundle = bundle;

        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putInt(Fragmentation.ARG_RESULT_CODE, mResultCode);
        args.putBundle(Fragmentation.ARG_RESULT_BUNDLE, mResultBundle);
    }

    /**
     * 接受Result数据 (通过startForResult的返回数据)
     *
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data Result数据
     */
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
    }

    /*****************************************************************************************************
     * fragment切换动画
     *****************************************************************************************************/

    /**
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        // TODO
        //        if (mActivity.mPopMulitpleNoAnim || mLocking) {
        //            return mNoAnim;
        //        }
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
            if (enter) {
                if (mIsRoot) {
                    mNoAnim.setAnimationListener(mDebounceAnimListener);
                    return mNoAnim;
                }
                return mEnterAnim;
            } else {
                return mPopExitAnim;
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
            if (enter) {
                return mPopEnterAnim;
            } else {
                return mExitAnim;
            }
        }
        return null;
    }

    protected FragmentAnimator onCreateFragmentAnimation() {
        return mActivity.getFragmentAnimator();
    }

    private void initAnim() {
        handleNoAnim();

        mNoAnim = AnimationUtils.loadAnimation(mActivity, R.anim.boc_fragment_no_anim);
        mEnterAnim = AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getEnter());
        mExitAnim = AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getExit());
        mPopEnterAnim = AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getPopEnter());
        mPopExitAnim = AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getPopExit());

        // 监听动画状态(for防抖动)
        mDebounceAnimListener = new DebounceAnimListener();
        mEnterAnim.setAnimationListener(mDebounceAnimListener);
    }

    private void handleNoAnim() {
        if (mFragmentAnimator == null) {
            return;
        }
        if (mFragmentAnimator.getEnter() == 0) {
            //mEnterAnimFlag = true;
            mFragmentAnimator.setEnter(R.anim.boc_fragment_no_anim);
        }
        if (mFragmentAnimator.getExit() == 0) {
            mFragmentAnimator.setExit(R.anim.boc_fragment_no_anim);
        }
        if (mFragmentAnimator.getPopEnter() == 0) {
            mFragmentAnimator.setPopEnter(R.anim.boc_fragment_no_anim);
        }
        if (mFragmentAnimator.getPopExit() == 0) {
            // 用于解决 start新Fragment时,转场动画过程中上一个Fragment页面空白问题
            mFragmentAnimator.setPopExit(R.anim.boc_fragment_pop_exit_no_anim);
        }
    }

    /**
     * 入栈动画 结束时,回调
     */
    protected void onEnterAnimationEnd() {
    }

    long getEnterAnimDuration() {
        return mEnterAnim.getDuration();
    }

    long getExitAnimDuration() {
        return mExitAnim.getDuration();
    }

    long getPopEnterAnimDuration() {
        return mPopEnterAnim.getDuration();
    }

    long getPopExitAnimDuration() {
        return mPopExitAnim.getDuration();
    }

    /**
     * 为了防抖动(点击过快)的动画监听器
     */
    private class DebounceAnimListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            onEnterAnimationEnd();
            mActivity.setFragmentClickable(true);

            if (mOnAnimEndListener != null) {
                mOnAnimEndListener.onAnimationEnd();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    protected void titleLeftIconClick() {

        if (!onBack()) {
            return;
        }

        if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 1) {
            mFragmentation.back(mActivity.getSupportFragmentManager());
        } else {
            ActivityManager.getAppManager().finishActivity();
        }
    }

    /**
     * 返回时调用该方法
     * 返回false时，交给各个功能自己处理返回操作
     */
    public boolean onBack() {

        return true;
    }

    int getRequestCode() {
        return mRequestCode;
    }

    int getResultCode() {
        return mResultCode;
    }

    public Context getContext() {
        return mContext;
    }

    /********************************************************************************************
     * 通用对话框
     ********************************************************************************************/

    /**
     * 显示loading框,默认可关闭
     */
    public GlobalLoadingDialog showLoadingDialog() {
        return showLoadingDialog("加载中...");
    }

    /**
     * 显示loading框
     * @param cancelable    是否有关闭按钮 true:有;false没有
     * @return loading框
     */
    public GlobalLoadingDialog showLoadingDialog(boolean cancelable) {
        return showLoadingDialog("加载中...",cancelable);
    }


    /**
     * 显示loading框，默认可关闭
     */
    public GlobalLoadingDialog showLoadingDialog(String message) {
        return showLoadingDialog(message, true);
    }

    /**
     * 显示loading框,flag控制是否可关闭，true可关闭，false不可关闭
     */
    public GlobalLoadingDialog showLoadingDialog(String message, boolean flag) {
        GlobalLoadingDialog globalLoadingDialog = mActivity.showLoadingDialog(message, flag);
        if (flag) {
            globalLoadingDialog.setCloseLoadingDialogListener(this);
        }
        return globalLoadingDialog;
    }

    /**
     * 关闭loading框
     */
    public void closeProgressDialog() {
        mActivity.closeProgressDialog();
    }

    /**
     * 错误提示框
     */
    public void showErrorDialog(String errorMessage) {
        mActivity.showErrorDialog(errorMessage);
    }

    public boolean onBackPress() {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回处理
            return onBackPress();
        }
        return false;
    }

    @Override
    public void onCloseLoadingDialog() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FRAGMENTATION_STATE_SAVE_ANIMATOR, mFragmentAnimator);
        outState.putBoolean(FRAGMENTATION_STATE_SAVE_IS_HIDDEN, isHidden());
    }
}
