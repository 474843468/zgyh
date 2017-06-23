package com.boc.bocsoft.mobile.bocmobile.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.anim.DefaultVerticalAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.activity.anim.FragmentAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import java.util.List;

/**
 * 一个activity多个fragment时的activity基类
 * Created by Administrator on 2016/6/6.
 */
public class BussActivity extends BaseMobileActivity {

    public final static String MODULE_ID = "module_id";
    public final static String MODULE_CLASS = "modulefragmentclass";

    protected Fragmentation mFragmentation;
    protected FragmentAnimator mFragmentAnimator;
    private boolean mFragmentClickable = true;
    //private Module mModule;
    private String moduleName;
    protected int contentId = -1;

    private Handler mHandler;
    boolean mPopMulitpleNoAnim = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (restoreInstanceState()) {
            processRestoreInstanceState(savedInstanceState);
        }
        LogUtils.d("BussActivity"," activity onCreate:"+getClass().getSimpleName());
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mFragmentation = getFragmentation();
        mFragmentAnimator = onCreateFragmentAnimator();
    }

    /**
     * 防抖动(防止点击速度过快)
     */
    protected void setFragmentClickable(boolean clickable) {
        mFragmentClickable = clickable;
    }



    @Override
    public void initView() {
        super.initView();
        //mFragmentation = new Fragmentation(this, R.id.baseContentView);
        paseIntent(getIntent());
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("BussActivity"," activity newIntent:"+getClass().getSimpleName());

        paseIntent(intent);
    }

    private void paseIntent(Intent intent){

        moduleName = intent.getStringExtra(MODULE_ID);
        BussFragment fragment = null;

        if(moduleName!=null && moduleName.length()>0){
            BussApplication app = (BussApplication) this.getApplication();
            ModuleFactory mf = app.getModuleFactory();

            fragment  = mf.getModuleFragmentInstance(moduleName);
        }else{
            Class<BussFragment> fclass =
                (Class<BussFragment>) getIntent().getSerializableExtra(MODULE_CLASS);
            if(fclass == null)return;
            try {
                fragment = fclass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(fragment == null){
            //finish();
            return;
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Bundle bundle = new Bundle(extras);
            fragment.setArguments(bundle);
        }

        if (fragment != null) {
            start(fragment);
            return;
        }
    }

//恢复fragment隐藏显示状态
    private void processRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();

            if (fragments != null && fragments.size() > 0) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                for (int i = fragments.size() - 1; i >= 0; i--) {
                    Fragment fragment = fragments.get(i);

                    if (fragment instanceof BussFragment) {
                        BussFragment supportFragment = (BussFragment) fragment;
                        if (supportFragment.isSupportHidden()) {
                            ft.hide(supportFragment);
                        } else {
                            ft.show(supportFragment);
                        }
                    }
                }
                ft.commit();
            }
        }
    }

//获取fragment跳转控制类
    public Fragmentation getFragmentation() {
        if (mFragmentation == null) {
            // mFragmentation = new Fragmentation(this, setContainerId());
            if (contentId != -1) {
                mFragmentation = new Fragmentation(this, contentId);
            } else {
                mFragmentation = new Fragmentation(this, R.id.baseContentView);
            }

        }
        return mFragmentation;
    }

    Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    /**
     * 得到位于栈顶Fragment
     *
     * @return
     */
    public BussFragment getTopFragment() {
        return mFragmentation.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的framgent对象
     */
    public <T extends BussFragment> T findFragment(Class<T> fragmentClass) {
        return mFragmentation.findStackFragment(fragmentClass, this.getSupportFragmentManager(), false);
    }

    @Override
    public void onBackPressed() {

        // 这里是防止动画过程中，按返回键取消加载Fragment
        setFragmentClickable(true);

        // TODO fragment不跳子fragment
        //        BussFragment topFragment = getTopFragment();
        //        if (topFragment != null) {
        //            boolean result = topFragment.onBackPressedSupport();
        //            if (result) {
        //                return;
        //            }
        //        }
        BussFragment fragment = getTopFragment();
        if (fragment != null) {
            if(!fragment.onBack()){
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            mFragmentation.back(getSupportFragmentManager());
        } else {
            ActivityManager.getAppManager().finishActivity();
        }


    }

    /*****************************************************************************************************
     * fragment切换方法
     *****************************************************************************************************/
    public void start(BussFragment toFragment) {
        start(toFragment, BussFragment.STANDARD);
    }

    public void start(BussFragment toFragment, int launchMode) {
        LogUtils.d("BussActivity","开启页面:"+toFragment.getClass());
        mFragmentation.dispatchStartTransaction(getTopFragment(), toFragment, 0, launchMode, Fragmentation.TYPE_ADD);
    }

    public void startWithPop(BussFragment to) {
        LogUtils.d("BussActivity","开启页面:"+to.getClass());
        mFragmentation.dispatchStartTransaction(getTopFragment(), to, 0, BussFragment.STANDARD, Fragmentation.TYPE_ADD_WITH_POP);
    }

    /**
     * 出栈到目标fragment
     *
     * @param fragmentClass 目标fragment
     * @param includeSelf   是否包含该fragment
     */
    public void popTo(Class<?> fragmentClass, boolean includeSelf) {
        mFragmentation.popTo(fragmentClass, includeSelf, null, getSupportFragmentManager());
    }

    /**
     * 用于出栈后,立刻进行FragmentTransaction操作
     */
    public void popTo(Class<?> fragmentClass, boolean includeSelf, Runnable afterPopTransactionRunnable) {
        mFragmentation.popTo(fragmentClass, includeSelf, afterPopTransactionRunnable, getSupportFragmentManager());
    }

    /**
     * 出栈
     */
    public void pop() {
        mFragmentation.back(getSupportFragmentManager());
    }

    /*****************************************************************************************************
     * fragment切换动画
     *****************************************************************************************************/
    /**
     * 创建全局Fragment的切换动画
     *
     * @return
     */
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultVerticalAnimator();
    }

    /**
     * 获取设置的全局动画
     *
     * @return
     */
    public FragmentAnimator getFragmentAnimator() {
        return new FragmentAnimator(
                mFragmentAnimator.getEnter(), mFragmentAnimator.getExit(),
                mFragmentAnimator.getPopEnter(), mFragmentAnimator.getPopExit()
        );
    }

    /**
     * 设置全局动画
     *
     * @param fragmentAnimator
     */
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        this.mFragmentAnimator = fragmentAnimator;
    }

    /**
     * 防止
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 防抖动(防止点击速度过快)
        if (!mFragmentClickable)
            return true;

        return super.dispatchTouchEvent(ev);
    }

    /*****************************************************************************************************
     * titlebar 设置
     *****************************************************************************************************/
    /**
     * 是否显示标题栏，默认显示
     * 子类可以重写此方法，控制是否显示标题栏
     *
     * @return
     */
    protected boolean isHaveTitleBarView() {
        return false;// 不显示标题
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(dispatchKeyDown(keyCode,event)){
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private boolean dispatchKeyDown(int keyCode,KeyEvent event){

        BussFragment topFragment = getTopFragment();
        if(topFragment != null){
            return  topFragment.onKeyDown(keyCode,event);
        }

        return  false;
    }

    void preparePopMultiple() {
        mPopMulitpleNoAnim = true;
    }

    void popFinish() {
        mPopMulitpleNoAnim = false;
    }

    /**
     * 内存重启后,是否让Fragmentation帮你恢复Fragment状态
     *
     * @return
     */
    protected boolean restoreInstanceState() {
        return true;
    }
}
