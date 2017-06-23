package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.circlemenu.CircleIMenuItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.circlemenu.CircleMenuLayout;

/**
 * 作者：xwg on 16/11/1 15:05
 * 跨境金融 主界面
 */
public class OverseasHomeFragment extends BussFragment implements CircleMenuLayout.OnItemSelectedListener,CircleMenuLayout.OnItemClickListener{
    private CircleMenuLayout mCircleMenuLayout;
    private TextView mSelectedTextView;
    private TextView mSelectedTextContent;
    private View rootView;
    private ImageView mIvBack;
    private int mSelectedTextLineCount;
    private AnimatorSet animatorSetRecover;//还原动画

    private LinearGradient mLinearGradient;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView=mInflater.inflate(R.layout.boc_fragment_overseaszoo_home,null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();

        mCircleMenuLayout = (CircleMenuLayout) rootView.findViewById(R.id.circleMenuLayout);
        mSelectedTextView = (TextView)rootView.findViewById(R.id.tv_overseas_menu_name);
        mSelectedTextContent = (TextView)rootView.findViewById(R.id.tv_overseas_menu_content);

        mIvBack=(ImageView)rootView.findViewById(R.id.iv_back);
    }

    @Override
    public void initData() {
        super.initData();

        mSelectedTextView.setText(mCircleMenuLayout.getSelectedItem().getName());
        mSelectedTextContent.setText(mCircleMenuLayout.getSelectedItem().getContent());

        ViewTreeObserver observer=mSelectedTextContent.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mSelectedTextLineCount=mSelectedTextContent.getLineCount();
                setContentTextColor();
                return true;
            }
        });

    }
    /**
     *   设置内容文字为渐变色
     *LinearGradient.TileMode.CLAMP 这种模式表示重复最后一种颜色直到该View结束的地方
 LinearGradient.TileMode.REPEAT表示着色器在水平或者垂直方向上对控件进行重复着色，类似于windows系统桌面背景中的“平铺”
     *LinearGradient.TileMode.MIRROR模式会在水平方向或者垂直方向上以镜像的方式进行渲染，这种渲染方式的一个特征就是具有翻转的效果，
     *
     *  Paint paint = new Paint();
     paint.setColor(Color.GREEN);
     LinearGradient linearGradient = new LinearGradient(0, 0, getMeasuredWidth()/2, getMeasuredHeight()/2,new
     int[]{Color.RED, Color.WHITE, Color.BLUE}, null, LinearGradient.TileMode.REPEAT);
     paint.setShader(linearGradient);
     canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);
     }

     */
    private void setContentTextColor() {
        //给mSelectedTextContent 设置渐变色
        mLinearGradient=new LinearGradient(0,0,0,mSelectedTextContent.getTextSize()*mSelectedTextLineCount,
                Color.rgb(0,240,170),Color.rgb(0,192,255), Shader.TileMode.CLAMP);
        mSelectedTextContent.getPaint().setShader(mLinearGradient);
    }

    @Override
    public void setListener() {
        super.setListener();
        mCircleMenuLayout.setOnItemSelectedListener(this);
        mCircleMenuLayout.setOnItemClickListener(this);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onItemClick(CircleIMenuItemView view, final int position) {
        ObjectAnimator animatorX=ObjectAnimator.ofFloat(view,"scaleX",CircleMenuLayout.MAX_SCALE_COEFFICIENT,6);
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(view,"scaleY",CircleMenuLayout.MAX_SCALE_COEFFICIENT,6);

        ObjectAnimator animatorAlpha=ObjectAnimator.ofFloat(view,"alpha",1,0.2f);

        ObjectAnimator animatorX1=ObjectAnimator.ofFloat(view,"scaleX",CircleMenuLayout.MAX_SCALE_COEFFICIENT);
        ObjectAnimator animatorY1=ObjectAnimator.ofFloat(view,"scaleY",CircleMenuLayout.MAX_SCALE_COEFFICIENT);

        ObjectAnimator animatorAlpha1=ObjectAnimator.ofFloat(view,"alpha",1);

        //放大和减小透明度动画
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setStartDelay(0);
        animatorSet.setDuration(500);
        animatorSet.playTogether(animatorX,animatorY,animatorAlpha);
//        animatorSet.playTogether(animatorX,animatorY);
        animatorSet.start();
        //还原动画
        animatorSetRecover=new AnimatorSet();
        animatorSetRecover.setStartDelay(0);
        animatorSetRecover.setDuration(10);
        animatorSetRecover.playTogether(animatorX1,animatorY1,animatorAlpha1);
//        animatorSetRecover.playTogether(animatorX1,animatorY1);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCircleMenuLayout.setClickable(true);
                startFragment(position);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void startFragment(int position) {
        switch (position){
            case 0://国际商旅
                start(new DetailMainFragment(DetailMainFragment.ABROAD_TRAVEL));
                break;
            case 1:// 出国留学
                start(new DetailMainFragment(DetailMainFragment.ABROAD_STUDY));
                break;
            case 2://来华人士
                start(new PeopleInChinaFragment());
                break;
            case 3://外派工作
                start(new DetailMainFragment(DetailMainFragment.ABROAD_WORK));
                break;
            case 4://投资移民
                start(new DetailMainFragment(DetailMainFragment.ABROAD_INVEST));
                break;
        }

    }

    @Override
    public void onItemSelected(View view, int position, long id, String name, String content) {
        mSelectedTextView.setText(name);
        mSelectedTextContent.setText(content);
    }

    /**
     *对之前点击的view做还原操作
     */
    public void recoverView(){
        if (animatorSetRecover!=null){
            animatorSetRecover.start();
        }
    }
}
