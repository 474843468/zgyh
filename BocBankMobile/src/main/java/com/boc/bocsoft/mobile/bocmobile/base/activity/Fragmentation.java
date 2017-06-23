package com.boc.bocsoft.mobile.bocmobile.base.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransactionBugFixHack;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.framework.ui.BaseFragment;

import java.lang.reflect.Field;
import java.util.List;

/**
 * void popBackStack()
 *
 * 弹出堆栈中的一个并且显示，类似按下返回键的操作；
 *
 * popBackStack(String tag,int flags)
 *
 * tag可以为null或者相对应的tag，flags只有0和1(POP_BACK_STACK_INCLUSIVE)两种情况
 *
 * 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment。
 *
 * 如果tag为null ，flags为1时，弹出回退栈中所有fragment。
 *
 * 如果tag不为null，那就会找到这个tag所对应的fragment，flags为0时，弹出该
 *
 * fragment以上的Fragment，如果是1，弹出该fragment（包括该fragment）以上的fragment。
 *
 * popBackStack(int id,int flags)
 *
 * 与popBackStack(String tag,int flags)类似，找到id代表的fragment，然后执行一样的操作
 * popBackStackImmediate(int id, int flags)
 *
 * popBackStackImmediate(String name, int flags)
 *
 * popBackStackImmediate()
 *
 * 这几个方法类似以上的方法，只不过这几个在内部调用时会立即弹出
 *
 * 使用FragmentManager.getFragments()，然后拿List< Fragment >里的
 *
 * 对象时特别要注意判空。
 *
 *
 *
 *
 * fragment跳转控制类
 *
 * 解释：newFragment代替了控件IDR.id.fragment_container所指向的ViewGroup中所含的任何fragment。然后调用addToBackStack()，此时被代替的fragment就被放入后退栈中，于是当用户按下返回键时，事务发生回溯，原先的fragment又回来了。
 *
 * 如果你向事务添加了多个动作，比如多次调用了add(),remove()等之后又调用了addToBackStack()方法，那么所有的在commit()之前调用的方法都被作为一个事务。当用户按返回键时，所有的动作都被反向执行（事务回溯）。
 *
 * 事务中动作的执行顺序可随意，但要注意以下两点：
 *
 * 1你必须最后调用commit()。
 *
 * 2如果你添加了多个fragment，那么它们的显示顺序跟添加顺序一至（后显示的覆盖前面的）。
 *
 *解决popTo多个fragment时动画引起的异常问题,一次多个Fragment被出栈。index混乱
 * 解决以singleTask或singleTop模式start时,pop多个fragment时动画引起的异常问题
 * FragmentManagerImpl的mAvailIndices，对其进行一次Collections.reverseOrder()降序排序，保证栈内Fragment的index的正确。
 *
 *
 *
 * 当调用popbackstack及commit方法对fragment进行进栈出栈操作时，可能就会出现illegalStateException can not perform this
 * action after
 * onsaveinstancestate异常
 * 该异常出现的条件是在系统检测到activity可能要销毁时会调用onSaveInstanceState对activity的状态进行保存，在状态未恢复时不允许对activity的
 * fragment进行进出栈操作，
 * 所以就会报illegalStateException异常
 *
 * 2.将commit方法替换成commitAllowingStateLoss()方法，commitAllowingStateLoss()允许状态丢失，所以正常情况下使用此方法提交事务可能会提交失败
 *
 * 3.在调用popbackstack及commit方法前手动调用onstart（）方法将activity的状态恢复，我看过源码之后了解到在activity调用onstart、onpostresume、
 * onActivityResult、onNewIntnet时会恢复activity状态
 *
 * 4.用Java反射机制修改FragmentManager的成员变量mStateSaved的值为false，或者执行noteStateNotSaved方法来修改mStateSaved的值。
 *
 *
 *
 * 如果你在执行的事务中有删除fragment的动作，而且没有调用addToBackStack()，那么当事务提交时，那些被删除的fragment就被销毁了。反之，那些fragment就不会被销毁，而是处于停止状态。当用户返回时，它们会被恢复。
 *
 * 密技：对于fragment事务，你可以应用动画。在commit()之前调用setTransition()就行。――一般银我不告诉他哦。
 *
 * 但是，调用commit()后，事务并不会马上执行。它会在activity的UI线程（其实就是主线程）中等待直到线程能执行的时候才执行（废话）。如果必要，你可以在UI线程中调用executePendingTransactions()方法来立即执行事务。但一般不需这样做，除非有其它线程在等待事务的执行。
 *
 * 警告：你只能在activity处于可保存状态的状态时，比如running中，onPause()方法和onStop()方法中提交事务，否则会引发异常。
 * 这是因为fragment的状态会丢失。如果要在可能丢失状态的情况下提交事务，请使用commitAllowingStateLoss()。
 * Created by lxw on 2016/6/4.
 */
public class Fragmentation {

  static final String ARG_REQUEST_CODE = "fragmentation_arg_request_code";
  static final String ARG_RESULT_CODE = "fragmentation_arg_result_code";
  static final String ARG_RESULT_BUNDLE = "fragmentation_arg_bundle";
  static final String ARG_IS_ROOT = "fragmentation_arg_is_root";

  public static final long BUFFER_TIME = 300L;

  private BussActivity mActivity;
  private FragmentManager mFragmentManager;
  private int mContainerId;

  // 添加
  public static final int TYPE_ADD = 0;
  // 替换
  public static final int TYPE_ADD_WITH_POP = 1;
  private Handler mHandler;

  /**
   * 构造方法
   */
  public Fragmentation(BussActivity activity, int containerId) {
    this.mActivity = activity;
    mContainerId = containerId;
    this.mFragmentManager = mActivity.getSupportFragmentManager();

    mHandler = mActivity.getHandler();
  }

  /**
   * 分发事务
   */
  public void dispatchStartTransaction(BussFragment from, BussFragment to, int requestCode,
      int launchMode, int type) {
    if (from != null) {
      mFragmentManager = from.getFragmentManager();
    }

    // 移动到popTo 后
    FragmentTransactionBugFixHack.reorderIndices(mFragmentManager);

    if (type == TYPE_ADD) {
      saveRequestCode(to, requestCode);
    }

    if (handleLaunchMode(to, launchMode)) return;

    // 在SingleTask/SingleTop启动模式之后 开启防抖动
    mActivity.setFragmentClickable(false);

    switch (type) {
      case TYPE_ADD:
        start(from, to);//添加一个fragment并显示该fragment
        break;
      case TYPE_ADD_WITH_POP:
        if (from != null) {
          startWithFinish(from, to);//替换顶层的fragment
        } else {
          throw new RuntimeException("startWithPop(): getTopFragment() is null");
        }
        break;
    }
  }

  /**
   * 添加一个fragment并显示该fragment
   */
  void start(BussFragment from, BussFragment to) {
    String toName = to.getClass().getName();
    FragmentTransaction ft = mFragmentManager.beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .add(mContainerId, to, toName);

    if (from != null) {
      ft.hide(from);
    } else {
      Bundle bundle = to.getArguments();
      bundle.putBoolean(ARG_IS_ROOT, true);
    }

    ft.addToBackStack(toName);//添加到返回栈
    //你只能在activity处于可保存状态的状态时，比如running中，onPause()方法和onStop()方法中提交事务，否则会引发异常。
    // 这是因为fragment的状态会丢失。如果要在可能丢失状态的情况下提交事务，请使用commitAllowingStateLoss()。
    boolean tmp = tmp(mFragmentManager);
    if (tmp) {
      ft.commitAllowingStateLoss();
    } else {
      ft.commit();
    }
  }

  private boolean tmp(FragmentManager mFragmentManager) {
    try {
      Field mStateSavedField = mFragmentManager.getClass().getDeclaredField("mStateSaved");
      mStateSavedField.setAccessible(true);
      return mStateSavedField.getBoolean(mFragmentManager);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 替换顶层的fragment
   */
  void startWithFinish(BussFragment from, BussFragment to) {
    BussFragment preFragment = getPreFragment(from);
    if (preFragment != null) {
      handlerFinish(preFragment, from, to);
    }
    passSaveResult(from, to);

    mFragmentManager.beginTransaction().remove(from).commit();
    mFragmentManager.popBackStack();

    String toName = to.getClass().getName();
    FragmentTransaction ft = mFragmentManager.beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .add(mContainerId, to, toName)
        .addToBackStack(toName);

    if (preFragment != null) {
      ft.hide(preFragment);
    }
    ft.commit();
  }

  /**
   * fix anim
   */
  @Nullable private void handlerFinish(BussFragment preFragment, BussFragment from,
      BussFragment to) {
    View view = preFragment.getView();
    if (view != null) {
      // 不调用 会闪屏
      view.setVisibility(View.VISIBLE);

      ViewGroup viewGroup;
      final View fromView = from.getView();

      if (fromView != null && view instanceof ViewGroup) {
        viewGroup = (ViewGroup) view;
        ViewGroup container = (ViewGroup) mActivity.findViewById(mContainerId);
        if (container != null) {
          container.removeView(fromView);
          if (fromView.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
            fromView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
          }

          if (viewGroup instanceof LinearLayout) {
            viewGroup.addView(fromView, 0);
          } else {
            viewGroup.addView(fromView);
          }

          final ViewGroup finalViewGroup = viewGroup;
          to.setEnterAnimEndListener(new OnEnterAnimEndListener() {
            @Override public void onAnimationEnd() {
              finalViewGroup.removeView(fromView);
            }
          });
        }
      }
    }
  }

  /**
   * pass on Result
   */
  private void passSaveResult(BussFragment from, BaseFragment to) {
    saveRequestCode(to, from.getRequestCode());
    Bundle bundle = to.getArguments();
    bundle.putInt(ARG_RESULT_CODE, from.getResultCode());
    bundle.putBundle(ARG_RESULT_BUNDLE, from.getResultBundle());
  }

  /**
   * 获取目标Fragment的前一个Fragment
   *
   * @param fragment 目标Fragment
   */
  BussFragment getPreFragment(BussFragment fragment) {
    List<Fragment> fragmentList = fragment.getFragmentManager().getFragments();
    if (fragmentList == null) return null;

    int index = fragmentList.indexOf(fragment);
    for (int i = index - 1; i >= 0; i--) {
      Fragment preFragment = fragmentList.get(i);
      if (preFragment instanceof BaseFragment) {
        return (BussFragment) preFragment;
      }
    }
    return null;
  }

  /**
   * save requestCode
   */
  private void saveRequestCode(Fragment to, int requestCode) {
    Bundle bundle = to.getArguments();
    if (bundle == null) {
      bundle = new Bundle();
      to.setArguments(bundle);
    }
    bundle.putInt(ARG_REQUEST_CODE, requestCode);
  }

  /**
   * 处理启动方式
   * handle LaunchMode
   */
  private boolean handleLaunchMode(Fragment to, int launchMode) {
    if (launchMode == BussFragment.SINGLETOP) {
      List<Fragment> fragments = mFragmentManager.getFragments();
      int index = fragments.indexOf(to);
      // 在栈顶
      if (index == mFragmentManager.getBackStackEntryCount() - 1) {
        if (handleNewBundle(to)) return true;
      }
    } else if (launchMode == BussFragment.SINGLETASK) {
      popToFix(to, 0, mFragmentManager);
      if (handleNewBundle(to)) return true;
    }
    return false;
  }

  private boolean handleNewBundle(Fragment to) {
    if (to instanceof BussFragment) {
      BussFragment bussFragment = (BussFragment) to;
      Bundle newBundle = bussFragment.getNewBundle();
      bussFragment.onNewBundle(newBundle);
      return true;
    }
    return false;
  }

  /**
   * 解决以singleTask或singleTop模式start时,pop多个fragment时动画引起的异常问题
   */
  private void popToFix(Fragment targetFragment, int flag, final FragmentManager fragmentManager) {
    fragmentManager.popBackStackImmediate(targetFragment.getClass().getName(), flag);

    long popAniDuration;

    if (targetFragment instanceof BussFragment) {
      BussFragment fragment = (BussFragment) targetFragment;
      popAniDuration =
          Math.max(fragment.getPopEnterAnimDuration(), fragment.getPopExitAnimDuration());
    } else {
      popAniDuration = BUFFER_TIME;
    }

    mHandler.postDelayed(new Runnable() {
      @Override public void run() {
        FragmentTransactionBugFixHack.reorderIndices(fragmentManager);
      }
    }, popAniDuration);
  }

  /**
   * 获得栈顶SupportFragment
   */
  public BussFragment getTopFragment(FragmentManager fragmentManager) {
    List<Fragment> fragmentList = fragmentManager.getFragments();
    if (fragmentList == null) return null;

    for (int i = fragmentList.size() - 1; i >= 0; i--) {
      Fragment fragment = fragmentList.get(i);
      if (fragment instanceof BussFragment) {
        return (BussFragment) fragment;
      }
    }
    return null;
  }

  /**
   * 返回
   */
  public void back(FragmentManager fragmentManager) {
    int count = fragmentManager.getBackStackEntryCount();

    if (count > 1) {
      handleBack(fragmentManager);
    }
  }

  /**
   * 出栈到目标fragment
   *
   * @param fragmentClass 目标fragment
   * @param includeSelf 是否包含该fragment
   */
  void popTo(Class<?> fragmentClass, boolean includeSelf, Runnable afterPopTransactionRunnable,
      FragmentManager fragmentManager) {
    BussFragment targetFragment =
        (BussFragment) fragmentManager.findFragmentByTag(fragmentClass.getName());
    if (includeSelf) {
      targetFragment = getPreFragment(targetFragment);
      if (targetFragment == null) {
        throw new RuntimeException(
            "Do you want to pop all Fragments? Please call mActivity.finish()");
      }
    }
    BussFragment fromFragment = getTopFragment(fragmentManager);

    int flag = includeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0;

    if (afterPopTransactionRunnable != null) {
      if (targetFragment == fromFragment) {
        mHandler.post(afterPopTransactionRunnable);
        return;
      }

      fixPopToAnim(targetFragment, fromFragment);
      fragmentManager.beginTransaction().remove(fromFragment).commit();
      popToWithTransactionFix(fragmentClass, flag, fragmentManager);
      mHandler.post(afterPopTransactionRunnable);
    } else {
      popToFix(targetFragment, flag, fragmentManager);
    }
  }

  /**
   * 返回处理
   * handle result
   */
  private void handleBack(final FragmentManager fragmentManager) {
    List<Fragment> fragmentList = fragmentManager.getFragments();
    int count = 0;
    int requestCode = 0, resultCode = 0;
    long lastAnimTime = 0;
    Bundle data = null;

    for (int i = fragmentList.size() - 1; i >= 0; i--) {
      Fragment fragment = fragmentList.get(i);
      if (fragment instanceof BussFragment) {
        final BussFragment bussFragment = (BussFragment) fragment;
        if (count == 0) {
          // TODO
          requestCode = bussFragment.getRequestCode();
          resultCode = bussFragment.getResultCode();
          data = bussFragment.getResultBundle();

          lastAnimTime = bussFragment.getExitAnimDuration();

          count++;
        } else {

          if (requestCode != 0 && resultCode != 0) {
            final int finalRequestCode = requestCode;
            final int finalResultCode = resultCode;
            final Bundle finalData = data;

            long animTime = bussFragment.getPopEnterAnimDuration();

            fragmentManager.popBackStackImmediate();

            mHandler.postDelayed(new Runnable() {
              @Override public void run() {
                bussFragment.onFragmentResult(finalRequestCode, finalResultCode, finalData);
              }
            }, Math.max(animTime, lastAnimTime));
            return;
          }
          break;
        }
      }
    }
    fragmentManager.popBackStackImmediate();
  }

  /**
   * find Fragment from FragmentStack
   */
  @SuppressWarnings("unchecked") <T extends BussFragment> T findStackFragment(
      Class<T> fragmentClass, FragmentManager fragmentManager, boolean isChild) {
    Fragment fragment = null;
    if (isChild) {
      // 如果是 查找子Fragment,则有可能是在FragmentPagerAdapter/FragmentStatePagerAdapter中,这种情况下,
      // 它们的Tag是以android:switcher开头,所以这里我们使用下面的方式
      List<Fragment> childFragmentList = fragmentManager.getFragments();
      if (childFragmentList == null) return null;

      for (int i = childFragmentList.size() - 1; i >= 0; i--) {
        Fragment childFragment = childFragmentList.get(i);
        if (childFragment != null && childFragment.getClass()
            .getName()
            .equals(fragmentClass.getName())) {
          fragment = childFragment;
          break;
        }
      }
    } else {
      fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
    }
    if (fragment == null) {
      return null;
    }
    return (T) fragment;
  }

  /**
   * 动画结束 监听
   * Created by YoKeyword on 16/1/28.
   */
  public interface OnEnterAnimEndListener {
    void onAnimationEnd();
  }

  /**
   *
   * 解决popTo多个fragment时动画引起的异常问题,一次多个Fragment被出栈。index混乱
   */
  private void popToWithTransactionFix(Class<?> fragmentClass, int flag,
      final FragmentManager fragmentManager) {

    mActivity.preparePopMultiple();
    fragmentManager.popBackStackImmediate(fragmentClass.getName(), flag);
    mActivity.popFinish();

    mHandler.post(new Runnable() {
      @Override public void run() {
        //hack FragmentManagerImpl的mAvailIndices，对其进行一次Collections.reverseOrder()降序排序，保证栈内Fragment的index的正确。
        FragmentTransactionBugFixHack.reorderIndices(fragmentManager);
      }
    });
  }

  /**
   * fix popTo anim
   */
  @Nullable private void fixPopToAnim(Fragment rootFragment, BussFragment fromFragment) {
    if (rootFragment != null) {
      View view = rootFragment.getView();
      if (view != null) {
        // 不调用 会闪屏
        view.setVisibility(View.VISIBLE);

        ViewGroup viewGroup;
        final View fromView = fromFragment.getView();

        if (fromView != null && view instanceof ViewGroup) {
          viewGroup = (ViewGroup) view;
          ViewGroup container = (ViewGroup) mActivity.findViewById(mContainerId);
          if (container != null) {
            container.removeView(fromView);
            if (fromView.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
              fromView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            }

            if (viewGroup instanceof LinearLayout) {
              viewGroup.addView(fromView, 0);
            } else {
              viewGroup.addView(fromView);
            }

            final ViewGroup finalViewGroup = viewGroup;
            mHandler.postDelayed(new Runnable() {
              @Override public void run() {
                finalViewGroup.removeView(fromView);
              }
            }, Math.max(fromFragment.getExitAnimDuration(), BUFFER_TIME));
          }
        }
      }
    }
  }
}
